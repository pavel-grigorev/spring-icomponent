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

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.proxy.InterfaceComponentProxyFactoryBean;
import org.thepavel.icomponent.util.BeanDefinitionHelper;

import java.util.Arrays;

@Component(InterfaceComponentBeanFactoryPostProcessor.NAME)
public class InterfaceComponentBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
  public static final String NAME =
      "org.thepavel.icomponent.registrar.internalInterfaceComponentBeanFactoryPostProcessor";

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    Arrays
        .stream(beanFactory.getBeanDefinitionNames())
        .map(beanFactory::getBeanDefinition)
        .filter(beanDefinition -> beanDefinition instanceof AnnotatedBeanDefinition)
        .map(beanDefinition -> (AnnotatedBeanDefinition) beanDefinition)
        .filter(BeanDefinitionHelper::isInterface)
        .forEach(this::setFactoryBean);
  }

  private void setFactoryBean(AnnotatedBeanDefinition beanDefinition) {
    beanDefinition.setBeanClassName(getFactoryBeanClassName());

    beanDefinition
        .getConstructorArgumentValues()
        .addGenericArgumentValue(beanDefinition.getMetadata());
  }

  protected String getFactoryBeanClassName() {
    return InterfaceComponentProxyFactoryBean.class.getName();
  }
}
