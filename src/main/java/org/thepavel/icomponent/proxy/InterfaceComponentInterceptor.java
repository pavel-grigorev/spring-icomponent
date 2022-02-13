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
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.MethodHandlerMap;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.util.Lazy;
import org.thepavel.icomponent.util.MethodInvocationHelper;

import java.lang.reflect.Method;

public class InterfaceComponentInterceptor implements MethodInterceptor {
  private final ClassMetadata classMetadata;
  private final MethodHandlerMap methodHandlerMap;
  private final Lazy<DefaultMethodInterceptor> defaultMethodInterceptor;

  public InterfaceComponentInterceptor(ClassMetadata classMetadata, MethodHandlerMap methodHandlerMap) {
    this.classMetadata = classMetadata;
    this.methodHandlerMap = methodHandlerMap;
    this.defaultMethodInterceptor = Lazy.of(DefaultMethodInterceptor::new);
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
      return defaultMethodInterceptor.get().invoke(invocation);
    }

    if (ReflectionUtils.isToStringMethod(method)) {
      return MethodInvocationHelper.getToStringValueFor(invocation);
    }

    MethodMetadata methodMetadata = classMetadata.getMethodMetadata(method);
    MethodHandler methodHandler = methodHandlerMap.getMethodHandler(method);

    return methodHandler.handle(invocation.getArguments(), methodMetadata);
  }
}
