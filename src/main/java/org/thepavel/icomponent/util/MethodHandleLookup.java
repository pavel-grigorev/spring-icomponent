/*
 * Copyright 2015-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thepavel.icomponent.util;

import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Copied from <a href="https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/projection/DefaultMethodInvokingMethodInterceptor.java">DefaultMethodInvokingMethodInterceptor</a>.
 *
 * Strategies for {@link MethodHandle} lookup.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Mark Paluch
 */
public enum MethodHandleLookup {
  /**
   * Encapsulated {@link MethodHandle} lookup working on Java 9.
   */
  ENCAPSULATED {
    @Nullable
    private final Method privateLookupIn = ReflectionUtils
        .findMethod(MethodHandles.class, "privateLookupIn", Class.class, MethodHandles.Lookup.class);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#lookup(java.lang.reflect.Method)
     */
    @Override
    public MethodHandle lookup(Method method) throws ReflectiveOperationException {
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
  },

  /**
   * Open (via reflection construction of {@link MethodHandles.Lookup}) method handle lookup. Works with Java 8 and
   * with Java 9 permitting illegal access.
   */
  OPEN {
    private final Lazy<Constructor<MethodHandles.Lookup>> constructor = Lazy.of(MethodHandleLookup::getLookupConstructor);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#lookup(java.lang.reflect.Method)
     */
    @Override
    public MethodHandle lookup(Method method) throws ReflectiveOperationException {
      if (!isAvailable()) {
        throw new IllegalStateException("Could not obtain MethodHandles.lookup constructor!");
      }
      Constructor<MethodHandles.Lookup> constructor = this.constructor.get();
      Class<?> declaringClass = method.getDeclaringClass();
      return constructor.newInstance(declaringClass).unreflectSpecial(method, declaringClass);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup#isAvailable()
     */
    @Override
    boolean isAvailable() {
      return constructor.get() != null;
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
    public MethodHandle lookup(Method method) throws ReflectiveOperationException {
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

  private static MethodHandle doLookup(Method method, MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
    MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
    Class<?> declaringClass = method.getDeclaringClass();

    if (MethodHelper.isStatic(method)) {
      return lookup.findStatic(declaringClass, method.getName(), methodType);
    }

    return lookup.findSpecial(declaringClass, method.getName(), methodType, declaringClass);
  }

  private static MethodHandles.Lookup getLookup(Class<?> declaringClass, Method privateLookupIn) {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    try {
      return (MethodHandles.Lookup) privateLookupIn.invoke(MethodHandles.class, declaringClass, lookup);
    } catch (ReflectiveOperationException e) {
      return lookup;
    }
  }

  @Nullable
  private static Constructor<MethodHandles.Lookup> getLookupConstructor() {
    try {
      Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
      ReflectionUtils.makeAccessible(constructor);
      return constructor;
    } catch (Exception ex) {
      // this is the signal that we are on Java 9 (encapsulated) and can't use the accessible constructor approach.
      if (ex.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
        return null;
      }
      throw new IllegalStateException(ex.getMessage(), ex);
    }
  }

  /**
   * Lookup a {@link MethodHandle} given {@link Method} to look up.
   *
   * @param method must not be {@literal null}.
   * @return the method handle.
   * @throws ReflectiveOperationException when no such method exists.
   */
  public abstract MethodHandle lookup(Method method) throws ReflectiveOperationException;

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
}
