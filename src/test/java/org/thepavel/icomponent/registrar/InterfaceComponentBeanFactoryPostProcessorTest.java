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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.thepavel.icomponent.proxy.InterfaceComponentProxyFactoryBean;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterfaceComponentBeanFactoryPostProcessorTest {
  @Test
  public void setsBeanClassNameToFactoryBeanClassName() {
    givenBeanDefinitionFor(TestInterface.class);
    whenInterfaceComponentBeanFactoryPostProcessorRan();
    thenBeanDefinitionHasBeanClassName(InterfaceComponentProxyFactoryBean.class.getName());
    andBeanDefinitionHasConstructorArguments(beanDefinition.getMetadata());
  }

  @Test
  public void ignoresAbstractClasses() {
    givenBeanDefinitionFor(TestAbstractClass.class);
    whenInterfaceComponentBeanFactoryPostProcessorRan();
    thenBeanDefinitionHasBeanClassName(TestAbstractClass.class.getName());
    andBeanDefinitionHasConstructorArguments();
  }

  @Test
  public void ignoresConcreteClasses() {
    givenBeanDefinitionFor(TestClass.class);
    whenInterfaceComponentBeanFactoryPostProcessorRan();
    thenBeanDefinitionHasBeanClassName(TestClass.class.getName());
    andBeanDefinitionHasConstructorArguments();
  }

  private AnnotatedBeanDefinition beanDefinition;

  @BeforeEach
  public void beforeEach() {
    beanDefinition = null;
  }

  private void givenBeanDefinitionFor(Class<?> type) {
    beanDefinition = new AnnotatedGenericBeanDefinition(type);
  }

  private void whenInterfaceComponentBeanFactoryPostProcessorRan() {
    new InterfaceComponentBeanFactoryPostProcessor()
        .postProcessBeanFactory(new DummyConfigurableListableBeanFactory(beanDefinition));
  }

  private void thenBeanDefinitionHasBeanClassName(String expected) {
    assertEquals(expected, beanDefinition.getBeanClassName());
  }

  private void andBeanDefinitionHasConstructorArguments(Object... arguments) {
    assertArrayEquals(arguments, getConstructorArguments());
  }

  private Object[] getConstructorArguments() {
    return beanDefinition
        .getConstructorArgumentValues()
        .getGenericArgumentValues()
        .stream()
        .map(ValueHolder::getValue)
        .toArray();
  }
}
