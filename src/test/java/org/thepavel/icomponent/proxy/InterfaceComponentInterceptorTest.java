/*
 * Copyright (c) 2020-2022 Pavel Grigorev.
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
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.ReflectionUtils.findMethod;

@SuppressWarnings("SameParameterValue")
public class InterfaceComponentInterceptorTest {
  @Test
  public void delegatesToMethodHandler() throws Throwable {
    Method method = TestInterface.class.getMethods()[0];
    Object[] arguments = new Object[0];

    givenClass(TestInterface.class);
    whenMethodCallIntercepted(method, arguments);
    thenMethodHandlerReceivedArguments(arguments);
    andMethodHandlerReceivedMethodMetadataFor(method);
  }

  @Test
  public void handlesToStringMethodInvocations() throws Throwable {
    givenClass(TestInterface.class);
    whenMethodCallIntercepted(findMethod(Object.class, "toString"));
    thenMethodHandlerReceivedArguments(null);
    andMethodHandlerReceivedMethodMetadataFor(null);
    andInvocationResultIs(r -> r instanceof String && ((String) r).startsWith("java.lang.Object@"));
  }

  private ClassMetadata classMetadata;
  private DummyMethodHandler methodHandler;
  private Object invocationResult;

  @BeforeEach
  public void beforeEach() {
    classMetadata = null;
    methodHandler = new DummyMethodHandler();
    invocationResult = null;
  }

  private void givenClass(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    classMetadata = new ClassMetadataFactoryBean().getClassMetadata(annotationMetadata);
  }

  private void whenMethodCallIntercepted(Method method, Object... arguments) throws Throwable {
    MethodHandlerHashMap methodHandlerMap = new MethodHandlerHashMap();
    methodHandlerMap.put(method, methodHandler);

    InterfaceComponentInterceptor interceptor =
        new InterfaceComponentInterceptor(classMetadata, methodHandlerMap);

    invocationResult = interceptor.invoke(new DummyMethodInvocation(method, arguments));
  }

  private void thenMethodHandlerReceivedArguments(Object[] arguments) {
    assertSame(arguments, methodHandler.getPassedArguments());
  }

  private void andMethodHandlerReceivedMethodMetadataFor(Method method) {
    if (method == null) {
      assertNull(methodHandler.getPassedMethodMetadata());
      return;
    }

    MethodMetadata methodMetadata = classMetadata.getMethodMetadata(method);

    assertNotNull(methodMetadata);
    assertSame(methodMetadata, methodHandler.getPassedMethodMetadata());
  }

  private void andInvocationResultIs(Predicate<Object> tester) {
    assertTrue(tester.test(invocationResult));
  }
}
