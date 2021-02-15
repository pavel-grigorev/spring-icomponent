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

package org.thepavel.icomponent.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanDefinitionHelperTest {
  @Test
  public void returnsTrueIfArgumentIsInterface() {
    givenBeanDefinitionFor(TestInterface.class);
    whenIsInterfaceMethodCalled();
    thenResultIs(true);
  }

  @Test
  public void returnsFalseIfArgumentIsAnnotation() {
    givenBeanDefinitionFor(TestAnnotation.class);
    whenIsInterfaceMethodCalled();
    thenResultIs(false);
  }

  @Test
  public void returnsFalseIfArgumentIsClass() {
    givenBeanDefinitionFor(TestClass.class);
    whenIsInterfaceMethodCalled();
    thenResultIs(false);
  }

  @Test
  public void returnsFalseIfArgumentIsAbstractClass() {
    givenBeanDefinitionFor(TestAbstractClass.class);
    whenIsInterfaceMethodCalled();
    thenResultIs(false);
  }

  @Test
  public void returnsFalseIfArgumentIsEnum() {
    givenBeanDefinitionFor(TestEnum.class);
    whenIsInterfaceMethodCalled();
    thenResultIs(false);
  }

  private AnnotatedBeanDefinition beanDefinition;
  private Boolean result;

  @BeforeEach
  public void beforeEach() {
    beanDefinition = null;
    result = null;
  }

  private void givenBeanDefinitionFor(Class<?> clazz) {
    beanDefinition = new AnnotatedGenericBeanDefinition(clazz);
  }

  private void whenIsInterfaceMethodCalled() {
    result = BeanDefinitionHelper.isInterface(beanDefinition);
  }

  private void thenResultIs(Boolean expected) {
    assertEquals(expected, result);
  }

  private interface TestInterface {
  }

  private @interface TestAnnotation {
  }

  private static class TestClass {
  }

  private static abstract class TestAbstractClass {
  }

  private enum TestEnum {
  }
}
