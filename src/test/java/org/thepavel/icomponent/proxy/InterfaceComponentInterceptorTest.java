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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.handler.MethodHandlerHashMap;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("SameParameterValue")
public class InterfaceComponentInterceptorTest {
  @Test
  public void delegatesToMethodHandler() {
    Method method = TestInterface.class.getMethods()[0];
    Object[] arguments = new Object[0];

    givenClass(TestInterface.class);
    whenMethodCallIntercepted(method, arguments);
    thenMethodHandlerReceivedArguments(arguments);
    andMethodHandlerReceivedMethodMetadataFor(method);
  }

  private ClassMetadata classMetadata;
  private DummyMethodHandler methodHandler;

  @BeforeEach
  public void beforeEach() {
    classMetadata = null;
    methodHandler = new DummyMethodHandler();
  }

  private void givenClass(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    classMetadata = new ClassMetadataFactoryBean().getClassMetadata(annotationMetadata);
  }

  private void whenMethodCallIntercepted(Method method, Object[] arguments) {
    MethodHandlerHashMap methodHandlerMap = new MethodHandlerHashMap();
    methodHandlerMap.put(method, methodHandler);

    new InterfaceComponentInterceptor(classMetadata, methodHandlerMap)
        .invoke(new DummyMethodInvocation(method, arguments));
  }

  private void thenMethodHandlerReceivedArguments(Object[] arguments) {
    assertSame(arguments, methodHandler.getPassedArguments());
  }

  private void andMethodHandlerReceivedMethodMetadataFor(Method method) {
    MethodMetadata methodMetadata = classMetadata.getMethodMetadata(method);

    assertNotNull(methodMetadata);
    assertSame(methodMetadata, methodHandler.getPassedMethodMetadata());
  }
}
