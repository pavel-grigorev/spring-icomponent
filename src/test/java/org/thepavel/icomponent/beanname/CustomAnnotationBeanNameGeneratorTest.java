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

package org.thepavel.icomponent.beanname;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.thepavel.icomponent.registrar.DummyBeanDefinitionRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomAnnotationBeanNameGeneratorTest {
  @Test
  public void getsBeanNameFromAnnotationAttribute() {
    givenClass(CustomBeanNameTest.class);
    whenBeanNameGenerated();
    thenBeanNameIs("customBean");
  }

  @Test
  public void defaultsToDecapitalizedClassName() {
    givenClass(DefaultBeanNameTest.class);
    whenBeanNameGenerated();
    thenBeanNameIs("defaultBeanNameTest");
  }

  private static BeanNameGenerator generator;
  private static BeanDefinitionRegistry registry;

  @BeforeAll
  public static void beforeAll() {
    generator = new CustomAnnotationBeanNameGenerator(CustomAnnotation.class, "beanName");
    registry = new DummyBeanDefinitionRegistry();
  }

  @AfterAll
  public static void afterAll() {
    generator = null;
    registry = null;
  }

  private BeanDefinition beanDefinition;
  private String beanName;

  @BeforeEach
  public void beforeEach() {
    beanDefinition = null;
    beanName = null;
  }

  private void givenClass(Class<?> clazz) {
    beanDefinition = new AnnotatedGenericBeanDefinition(clazz);
  }

  private void whenBeanNameGenerated() {
    beanName = generator.generateBeanName(beanDefinition, registry);
  }

  private void thenBeanNameIs(String expected) {
    assertEquals(expected, beanName);
  }
}
