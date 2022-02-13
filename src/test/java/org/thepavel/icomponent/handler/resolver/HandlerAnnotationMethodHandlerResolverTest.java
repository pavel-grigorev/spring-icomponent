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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thepavel.icomponent.Handler;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactory;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnqualifiedMethodHandlerConfiguration.class)
public class HandlerAnnotationMethodHandlerResolverTest {
  @Autowired
  private HandlerAnnotationMethodHandlerResolver resolver;

  @Test
  public void looksUpMethodHandlerByNameAndType() {
    givenClass(TestInterface.class);
    andMethod("lookupMethodHandlerByNameAndType");
    whenHandlerAnnotationMethodHandlerResolverRan();
    thenMethodHandlerFound();
  }

  @Test
  public void looksUpMethodHandlerByName() {
    givenClass(TestInterface.class);
    andMethod("lookupMethodHandlerByName");
    whenHandlerAnnotationMethodHandlerResolverRan();
    thenMethodHandlerFound();
  }

  @Test
  public void looksUpMethodHandlerByType() {
    givenClass(TestInterface.class);
    andMethod("lookupMethodHandlerByType");
    whenHandlerAnnotationMethodHandlerResolverRan();
    thenMethodHandlerFound();
  }

  @Test
  public void looksUpMethodHandlerByNameInValueAttribute() {
    givenClass(TestInterface.class);
    andMethod("lookupMethodHandlerByNameInValueAttribute");
    whenHandlerAnnotationMethodHandlerResolverRan();
    thenMethodHandlerFound();
  }

  @Test
  public void returnsNullWhenAnnotationIsNotDeclared() {
    givenClass(TestInterface.class);
    andMethod("lookupMethodHandlerNoAnnotation");
    whenHandlerAnnotationMethodHandlerResolverRan();
    thenMethodHandlerNotFound();
  }

  @Test
  public void throwsExceptionWhenNonExistentBeanNameProvided() {
    givenClass(TestInterface.class);
    andMethod("nonExistentBeanName");
    assertThrows(
        NoSuchBeanDefinitionException.class,
        this::whenHandlerAnnotationMethodHandlerResolverRan
    );
  }

  @Test
  public void throwsExceptionWhenNonExistentBeanTypeProvided() {
    givenClass(TestInterface.class);
    andMethod("nonExistentBeanType");
    assertThrows(
        NoSuchBeanDefinitionException.class,
        this::whenHandlerAnnotationMethodHandlerResolverRan
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
  private MethodMetadata methodMetadata;
  private MethodHandler methodHandler;

  @BeforeEach
  public void beforeEach() {
    classMetadata = null;
    methodMetadata = null;
    methodHandler = null;
  }

  @SuppressWarnings("SameParameterValue")
  private void givenClass(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    classMetadata = classMetadataFactory.getClassMetadata(annotationMetadata);
  }

  private void andMethod(String methodName) {
    Optional<MethodMetadata> methodMetadata = getMethodMetadata(methodName);
    assertTrue(methodMetadata.isPresent());
    this.methodMetadata = methodMetadata.get();
  }

  private Optional<MethodMetadata> getMethodMetadata(String methodName) {
    return classMetadata
        .getMethodsMetadata()
        .stream()
        .filter(m -> m.getSourceMethod().getName().equals(methodName))
        .findFirst();
  }

  private void whenHandlerAnnotationMethodHandlerResolverRan() {
    methodHandler = resolver.getMethodHandler(methodMetadata);
  }

  private void thenMethodHandlerFound() {
    assertNotNull(methodHandler);
  }

  private void thenMethodHandlerNotFound() {
    assertNull(methodHandler);
  }

  private interface TestInterface {
    @Handler(beanName = "testMethodHandler", beanType = DummyMethodHandler.class)
    void lookupMethodHandlerByNameAndType();

    @Handler(beanName = "testMethodHandler")
    void lookupMethodHandlerByName();

    @Handler(beanType = DummyMethodHandler.class)
    void lookupMethodHandlerByType();

    @Handler("testMethodHandler")
    void lookupMethodHandlerByNameInValueAttribute();

    void lookupMethodHandlerNoAnnotation();

    @Handler("fakeBean")
    void nonExistentBeanName();

    @Handler(beanType = FakeBean.class)
    void nonExistentBeanType();
  }

  private interface FakeBean extends MethodHandler {
  }
}
