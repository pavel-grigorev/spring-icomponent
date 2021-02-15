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

package org.thepavel.icomponent.metadata.validation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ClassMetadataValidatorBeanTest {
  @Test
  public void allowsAbstractMethodsInClass() {
    givenClass(TestAbstractMethod.class);
    whenValidated();
    thenExceptionIs(null);
  }

  @Test
  public void allowsStaticMethodsInClass() {
    givenClass(TestStaticMethod.class);
    whenValidated();
    thenExceptionIs(null);
  }
  @Test
  public void forbidsDefaultMethodsInClass() {
    givenClass(TestDefaultMethod.class);
    whenValidated();
    thenExceptionIs(ClassMetadataValidationException.class);
  }

  private static ClassMetadataValidator validator;

  @BeforeAll
  public static void beforeAll() {
    validator = new ClassMetadataValidatorBean();
  }

  @AfterAll
  public static void afterAll() {
    validator = null;
  }

  private ClassMetadata classMetadata;
  private Class<?> exceptionType;

  @BeforeEach
  public void beforeEach() {
    classMetadata = null;
    exceptionType = null;
  }

  private void givenClass(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    classMetadata = new ClassMetadataFactoryBean().getClassMetadata(annotationMetadata);
  }

  private void whenValidated() {
    try {
      validator.validate(classMetadata);
    } catch (Exception e) {
      exceptionType = e.getClass();
    }
  }

  private void thenExceptionIs(Class<?> expected) {
    assertSame(expected, exceptionType);
  }

  private interface TestAbstractMethod {
    void test();
  }

  private interface TestStaticMethod {
    static void test() {}
  }

  private interface TestDefaultMethod {
    default void test() {}
  }
}
