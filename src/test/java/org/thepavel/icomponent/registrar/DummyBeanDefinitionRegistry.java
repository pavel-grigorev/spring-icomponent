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

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class DummyBeanDefinitionRegistry implements BeanDefinitionRegistry {
  private final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

  @Override
  public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
    beanDefinitions.put(beanName, beanDefinition);
  }

  @Override
  public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
    beanDefinitions.remove(beanName);
  }

  @Override
  public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
    return beanDefinitions.get(beanName);
  }

  @Override
  public boolean containsBeanDefinition(String beanName) {
    return beanDefinitions.containsKey(beanName);
  }

  @Override
  public String[] getBeanDefinitionNames() {
    return beanDefinitions.keySet().toArray(new String[0]);
  }

  @Override
  public int getBeanDefinitionCount() {
    return beanDefinitions.size();
  }

  @Override
  public boolean isBeanNameInUse(String beanName) {
    return containsBeanDefinition(beanName);
  }

  @Override
  public void registerAlias(String name, String alias) {
  }

  @Override
  public void removeAlias(String alias) {
  }

  @Override
  public boolean isAlias(String name) {
    return false;
  }

  @Override
  public String[] getAliases(String name) {
    return new String[0];
  }
}
