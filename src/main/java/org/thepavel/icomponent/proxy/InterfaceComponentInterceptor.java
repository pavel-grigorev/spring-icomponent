/*
 * Copyright (c) 2020-2021 Pavel Grigorev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thepavel.icomponent.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.MethodHandlerMap;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.util.Lazy;
import org.thepavel.icomponent.util.MethodInvocationHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class InterfaceComponentInterceptor implements MethodInterceptor {
  private final ClassMetadata classMetadata;
  private final MethodHandlerMap methodHandlerMap;

  private final MethodHandleLookup methodHandleLookup = MethodHandleLookup.getMethodHandleLookup();
  private final Map<Method, MethodHandle> methodHandleCache = new ConcurrentReferenceHashMap<>(10, ConcurrentReferenceHashMap.ReferenceType.WEAK);

  public InterfaceComponentInterceptor(ClassMetadata classMetadata, MethodHandlerMap methodHandlerMap) {
    this.classMetadata = classMetadata;
    this.methodHandlerMap = methodHandlerMap;
  }

  ClassMetadata getClassMetadata() {
    return classMetadata;
  }

  MethodHandlerMap getMethodHandlerMap() {
    return methodHandlerMap;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();

    if (method.isDefault()) {
        Object[] arguments = invocation.getArguments();
        Object proxy = ((ProxyMethodInvocation) invocation).getProxy();

        return getMethodHandle(method).bindTo(proxy).invokeWithArguments(arguments);
    }

    if (ReflectionUtils.isToStringMethod(method)) {
      return MethodInvocationHelper.getToStringValueFor(invocation);
    }

    MethodMetadata methodMetadata = classMetadata.getMethodMetadata(method);
    MethodHandler methodHandler = methodHandlerMap.getMethodHandler(method);

    return methodHandler.handle(invocation.getArguments(), methodMetadata);
  }

  private MethodHandle getMethodHandle(Method method) throws Exception {

    MethodHandle handle = methodHandleCache.get(method);

    if (handle == null) {

      handle = methodHandleLookup.lookup(method);
      methodHandleCache.put(method, handle);
    }

    return handle;
  }

  /**
   * Strategies for {@link MethodHandle} lookup.
   * Stolen from <a href="https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/projection/DefaultMethodInvokingMethodInterceptor.java">DefaultMethodInvokingMethodInterceptor</a>
   */
  enum MethodHandleLookup {

    /**
     * Encapsulated {@link MethodHandle} lookup working on Java 9.
     */
    ENCAPSULATED {

      private final @Nullable Method privateLookupIn = ReflectionUtils.findMethod(MethodHandles.class,
          "privateLookupIn", Class.class, Lookup.class);

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#lookup(java.lang.reflect.Method)
       */
      @Override
      MethodHandle lookup(Method method) throws ReflectiveOperationException {

        if (privateLookupIn == null) {
          throw new IllegalStateException("Could not obtain MethodHandles.privateLookupIn!");
        }

        return doLookup(method, getLookup(method.getDeclaringClass(), privateLookupIn));
      }

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#isAvailable()
       */
      @Override
      boolean isAvailable() {
        return privateLookupIn != null;
      }

      private Lookup getLookup(Class<?> declaringClass, Method privateLookupIn) {

        Lookup lookup = MethodHandles.lookup();

        try {
          return (Lookup) privateLookupIn.invoke(MethodHandles.class, declaringClass, lookup);
        } catch (ReflectiveOperationException e) {
          return lookup;
        }
      }
    },

    /**
     * Open (via reflection construction of {@link Lookup}) method handle lookup. Works with Java 8 and
     * with Java 9 permitting illegal access.
     */
    OPEN {

      private final Lazy<Constructor<Lookup>> constructor = Lazy.of(MethodHandleLookup::getLookupConstructor);

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#lookup(java.lang.reflect.Method)
       */
      @Override
      MethodHandle lookup(Method method) throws ReflectiveOperationException {

        if (!isAvailable()) {
          throw new IllegalStateException("Could not obtain MethodHandles.lookup constructor!");
        }

        Constructor<Lookup> constructor = this.constructor.get();

        return constructor.newInstance(method.getDeclaringClass()).unreflectSpecial(method, method.getDeclaringClass());
      }

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#isAvailable()
       */
      @Override
      boolean isAvailable() {
        return constructor.orElse(null) != null;
      }
    },

    /**
     * Fallback {@link MethodHandle} lookup using {@link MethodHandles#lookup() public lookup}.
     *
     * @since 2.1
     */
    FALLBACK {

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#lookup(java.lang.reflect.Method)
       */
      @Override
      MethodHandle lookup(Method method) throws ReflectiveOperationException {
        return doLookup(method, MethodHandles.lookup());
      }

      /*
       * (non-Javadoc)
       * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#isAvailable()
       */
      @Override
      boolean isAvailable() {
        return true;
      }
    };

    private static MethodHandle doLookup(Method method, Lookup lookup)
        throws NoSuchMethodException, IllegalAccessException {

      MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

      if (Modifier.isStatic(method.getModifiers())) {
        return lookup.findStatic(method.getDeclaringClass(), method.getName(), methodType);
      }

      return lookup.findSpecial(method.getDeclaringClass(), method.getName(), methodType, method.getDeclaringClass());
    }

    /**
     * Lookup a {@link MethodHandle} given {@link Method} to look up.
     *
     * @param method must not be {@literal null}.
     * @return the method handle.
     * @throws ReflectiveOperationException
     */
    abstract MethodHandle lookup(Method method) throws ReflectiveOperationException;

    /**
     * @return {@literal true} if the lookup is available.
     */
    abstract boolean isAvailable();

    /**
     * Obtain the first available {@link MethodHandleLookup}.
     *
     * @return the {@link MethodHandleLookup}
     * @throws IllegalStateException if no {@link MethodHandleLookup} is available.
     */
    public static MethodHandleLookup getMethodHandleLookup() {

      for (MethodHandleLookup it : MethodHandleLookup.values()) {

        if (it.isAvailable()) {
          return it;
        }
      }

      throw new IllegalStateException("No MethodHandleLookup available!");
    }

    @Nullable
    private static Constructor<Lookup> getLookupConstructor() {

      try {

        Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class);
        ReflectionUtils.makeAccessible(constructor);

        return constructor;
      } catch (Exception ex) {

        // this is the signal that we are on Java 9 (encapsulated) and can't use the accessible constructor approach.
        if (ex.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
          return null;
        }

        throw new IllegalStateException(ex);
      }
    }
  }
}
