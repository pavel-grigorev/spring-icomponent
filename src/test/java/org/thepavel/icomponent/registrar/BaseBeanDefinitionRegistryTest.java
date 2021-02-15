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

package org.thepavel.icomponent.registrar;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

abstract class BaseBeanDefinitionRegistryTest {
  static final String[] EXPECTED_BEANS = {
      "testInterfaceComponent",
      "testInterfaceService"
  };
  static final String[] UNEXPECTED_BEANS = {
      "testInterface",
      "testAnnotation",
      "testAbstractClass",
      "testClass",
      "testCustomAnnotation"
  };

  BeanDefinitionRegistry registry;

  @BeforeEach
  public void initRegistry() {
    registry = new DummyBeanDefinitionRegistry();
  }

  void thenRegistryContainsBeanDefinitions(String... beanNames) {
    Arrays.stream(beanNames).forEach(this::assertContainsBeanDefinition);
  }

  void andRegistryDoesNotContainBeanDefinitions(String... beanNames) {
    Arrays.stream(beanNames).forEach(this::assertDoesNotContainBeanDefinition);
  }

  private void assertContainsBeanDefinition(String beanName) {
    boolean contains = registry.containsBeanDefinition(beanName);
    if (!contains) {
      fail(beanName + " expected to be registered in BeanDefinitionRegistry but it is not");
    }
  }

  private void assertDoesNotContainBeanDefinition(String beanName) {
    boolean contains = registry.containsBeanDefinition(beanName);
    if (contains) {
      fail(beanName + " expected not to be registered in BeanDefinitionRegistry but it is");
    }
  }
}
