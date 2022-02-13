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

package org.thepavel.icomponent.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

class DummyApplicationContext implements ApplicationContext {
  private final Object beanByNameAndType = new Object();
  private final Object beanByName = new Object();
  private final Object beanByType = new Object();

  public Object getBeanByNameAndType() {
    return beanByNameAndType;
  }

  public Object getBeanByName() {
    return beanByName;
  }

  public Object getBeanByType() {
    return beanByType;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getApplicationName() {
    return null;
  }

  @Override
  public String getDisplayName() {
    return null;
  }

  @Override
  public long getStartupDate() {
    return 0;
  }

  @Override
  public ApplicationContext getParent() {
    return null;
  }

  @Override
  public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
    return null;
  }

  @Override
  public BeanFactory getParentBeanFactory() {
    return null;
  }

  @Override
  public boolean containsLocalBean(String name) {
    return false;
  }

  @Override
  public boolean containsBeanDefinition(String beanName) {
    return false;
  }

  @Override
  public int getBeanDefinitionCount() {
    return 0;
  }

  @Override
  public String[] getBeanDefinitionNames() {
    return new String[0];
  }

  @Override
  public String[] getBeanNamesForType(ResolvableType type) {
    return new String[0];
  }

  @Override
  public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
    return new String[0];
  }

  @Override
  public String[] getBeanNamesForType(Class<?> type) {
    return new String[0];
  }

  @Override
  public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
    return new String[0];
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    return null;
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
    return null;
  }

  @Override
  public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
    return new String[0];
  }

  @Override
  public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
    return null;
  }

  @Override
  public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
    return null;
  }

  @Override
  public Object getBean(String name) throws BeansException {
    return beanByName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return (T) beanByNameAndType;
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> requiredType) throws BeansException {
    return (T) beanByType;
  }

  @Override
  public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
    return null;
  }

  @Override
  public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
    return null;
  }

  @Override
  public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
    return null;
  }

  @Override
  public boolean containsBean(String name) {
    return false;
  }

  @Override
  public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
    return null;
  }

  @Override
  public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
    return null;
  }

  @Override
  public String[] getAliases(String name) {
    return new String[0];
  }

  @Override
  public void publishEvent(Object event) {

  }

  @Override
  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    return null;
  }

  @Override
  public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
    return null;
  }

  @Override
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
    return null;
  }

  @Override
  public Environment getEnvironment() {
    return null;
  }

  @Override
  public Resource[] getResources(String locationPattern) {
    return new Resource[0];
  }

  @Override
  public Resource getResource(String location) {
    return null;
  }

  @Override
  public ClassLoader getClassLoader() {
    return null;
  }
}
