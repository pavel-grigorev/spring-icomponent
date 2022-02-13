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

package org.thepavel.icomponent.handler.resolver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.handler.MethodHandlerMap;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactory;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;

import java.lang.reflect.Method;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MethodHandlerMapResolverBeanTest {
  @Test
  public void buildsMethodHandlerMapContainingMethodHandlersForAllMethodsInClass() {
    givenClass(TestInterface.class);
    andMethodHandlerResolver(new DummyMethodHandlerResolver(new DummyMethodHandler()));
    whenMethodHandlerMapResolved();
    thenMethodHandlerMapContainsMethodHandlerForMethod("method1");
    thenMethodHandlerMapContainsMethodHandlerForMethod("method2");
  }

  @Test
  public void throwsExceptionWhenMethodHandlerForAnyMethodCanNotBeResolved() {
    givenClass(TestInterface.class);
    andMethodHandlerResolver(new DummyMethodHandlerResolver(null));
    assertThrows(
        BeanInstantiationException.class,
        this::whenMethodHandlerMapResolved
    );
  }

  private static ClassMetadataFactory classMetadataFactory;

  @BeforeAll
  public static void beforeAll() {
    classMetadataFactory = new ClassMetadataFactoryBean();
  }

  @AfterAll
  public static void afterAll() {
    classMetadataFactory = null;
  }

  private ClassMetadata classMetadata;
  private MethodHandlerResolver methodHandlerResolver;
  private MethodHandlerMap methodHandlerMap;

  @BeforeEach
  public void beforeEach() {
    classMetadata = null;
    methodHandlerResolver = null;
    methodHandlerMap = null;
  }

  @SuppressWarnings("SameParameterValue")
  private void givenClass(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    classMetadata = classMetadataFactory.getClassMetadata(annotationMetadata);
  }

  private void andMethodHandlerResolver(MethodHandlerResolver methodHandlerResolver) {
    this.methodHandlerResolver = methodHandlerResolver;
  }

  private void whenMethodHandlerMapResolved() {
    MethodHandlerMapResolver resolver =
        new MethodHandlerMapResolverBean(singletonList(methodHandlerResolver));

    methodHandlerMap = resolver.getMethodHandlerMap(classMetadata);
    assertNotNull(methodHandlerMap);
  }

  private void thenMethodHandlerMapContainsMethodHandlerForMethod(String methodName) {
    Method method = ReflectionUtils.findMethod(classMetadata.getSourceClass(), methodName);
    assertNotNull(method);
    assertNotNull(methodHandlerMap.getMethodHandler(method));
  }

  @SuppressWarnings("unused")
  private interface TestInterface {
    void method1();
    void method2();
  }
}
