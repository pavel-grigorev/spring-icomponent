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
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.thepavel.icomponent.proxy.InterfaceComponentProxyFactory;
import org.thepavel.icomponent.proxy.InterfaceComponentProxyFactoryBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

abstract class BaseBeanDefinitionRegistryTest {
  private static final String EXPECTED_BEAN_CLASS_NAME = InterfaceComponentProxyFactoryBean.class.getName();
  private static final String[] EXPECTED_DEPENDENCIES = { InterfaceComponentProxyFactory.NAME };

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

  void andBeanDefinitionsAreProperlyConfigured(String... names) {
    Arrays
        .stream(names)
        .map(name -> registry.getBeanDefinition(name))
        .map(d -> (AnnotatedBeanDefinition) d)
        .forEach(this::testBeanDefinition);
  }

  private void testBeanDefinition(AnnotatedBeanDefinition beanDefinition) {
    assertEquals(EXPECTED_BEAN_CLASS_NAME, beanDefinition.getBeanClassName());

    List<ValueHolder> values = beanDefinition.getConstructorArgumentValues().getGenericArgumentValues();
    assertNotNull(values);
    assertEquals(1, values.size());
    assertSame(beanDefinition.getMetadata(), values.get(0).getValue());

    assertArrayEquals(EXPECTED_DEPENDENCIES, beanDefinition.getDependsOn());
  }
}
