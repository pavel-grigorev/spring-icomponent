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
package org.thepavel.icomponent.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ConcurrentReferenceHashMap.ReferenceType;
import org.thepavel.icomponent.util.MethodHandleLookup;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Copied from <a href="https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/projection/DefaultMethodInvokingMethodInterceptor.java">DefaultMethodInvokingMethodInterceptor</a>.
 *
 * Method interceptor to invoke default methods on a proxy.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Mark Paluch
 */
public class DefaultMethodInterceptor implements MethodInterceptor {
  private final MethodHandleLookup methodHandleLookup = MethodHandleLookup.getMethodHandleLookup();
  private final Map<Method, MethodHandle> methodHandleCache = new ConcurrentReferenceHashMap<>(10, ReferenceType.WEAK);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();

    if (!method.isDefault()) {
      throw new IllegalArgumentException("Method is not default: " + method);
    }

    Object[] arguments = invocation.getArguments();
    Object proxy = ((ProxyMethodInvocation) invocation).getProxy();

    return getMethodHandle(method).bindTo(proxy).invokeWithArguments(arguments);
  }

  private MethodHandle getMethodHandle(Method method) throws ReflectiveOperationException {
    MethodHandle handle = methodHandleCache.get(method);

    if (handle == null) {
      handle = methodHandleLookup.lookup(method);
      methodHandleCache.put(method, handle);
    }

    return handle;
  }
}
