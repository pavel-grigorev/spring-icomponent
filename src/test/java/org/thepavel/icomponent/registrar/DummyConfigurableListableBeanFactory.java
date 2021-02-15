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

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.lang.annotation.Annotation;
import java.security.AccessControlContext;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class DummyConfigurableListableBeanFactory implements ConfigurableListableBeanFactory {
  private static final String BEAN_DEFINITION_NAME = "dummy";

  private final BeanDefinition beanDefinition;

  public DummyConfigurableListableBeanFactory(BeanDefinition beanDefinition) {
    this.beanDefinition = beanDefinition;
  }

  @Override
  public void ignoreDependencyType(Class<?> type) {

  }

  @Override
  public void ignoreDependencyInterface(Class<?> ifc) {

  }

  @Override
  public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue) {

  }

  @Override
  public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
    return BEAN_DEFINITION_NAME.equals(beanName) ? beanDefinition : null;
  }

  @Override
  public Iterator<String> getBeanNamesIterator() {
    return null;
  }

  @Override
  public void clearMetadataCache() {

  }

  @Override
  public void freezeConfiguration() {

  }

  @Override
  public boolean isConfigurationFrozen() {
    return false;
  }

  @Override
  public void preInstantiateSingletons() throws BeansException {

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
    return new String[] {BEAN_DEFINITION_NAME};
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
  public <T> T createBean(Class<T> beanClass) throws BeansException {
    return null;
  }

  @Override
  public void autowireBean(Object existingBean) throws BeansException {

  }

  @Override
  public Object configureBean(Object existingBean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
    return null;
  }

  @Override
  public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
    return null;
  }

  @Override
  public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException {

  }

  @Override
  public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {

  }

  @Override
  public Object initializeBean(Object existingBean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public void destroyBean(Object existingBean) {

  }

  @Override
  public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
    return null;
  }

  @Override
  public Object resolveBeanByName(String name, DependencyDescriptor descriptor) throws BeansException {
    return null;
  }

  @Override
  public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) throws BeansException {
    return null;
  }

  @Override
  public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
    return null;
  }

  @Override
  public void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException {

  }

  @Override
  public void setBeanClassLoader(ClassLoader beanClassLoader) {

  }

  @Override
  public ClassLoader getBeanClassLoader() {
    return null;
  }

  @Override
  public void setTempClassLoader(ClassLoader tempClassLoader) {

  }

  @Override
  public ClassLoader getTempClassLoader() {
    return null;
  }

  @Override
  public void setCacheBeanMetadata(boolean cacheBeanMetadata) {

  }

  @Override
  public boolean isCacheBeanMetadata() {
    return false;
  }

  @Override
  public void setBeanExpressionResolver(BeanExpressionResolver resolver) {

  }

  @Override
  public BeanExpressionResolver getBeanExpressionResolver() {
    return null;
  }

  @Override
  public void setConversionService(ConversionService conversionService) {

  }

  @Override
  public ConversionService getConversionService() {
    return null;
  }

  @Override
  public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {

  }

  @Override
  public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {

  }

  @Override
  public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {

  }

  @Override
  public void setTypeConverter(TypeConverter typeConverter) {

  }

  @Override
  public TypeConverter getTypeConverter() {
    return null;
  }

  @Override
  public void addEmbeddedValueResolver(StringValueResolver valueResolver) {

  }

  @Override
  public boolean hasEmbeddedValueResolver() {
    return false;
  }

  @Override
  public String resolveEmbeddedValue(String value) {
    return null;
  }

  @Override
  public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

  }

  @Override
  public int getBeanPostProcessorCount() {
    return 0;
  }

  @Override
  public void registerScope(String scopeName, Scope scope) {

  }

  @Override
  public String[] getRegisteredScopeNames() {
    return new String[0];
  }

  @Override
  public Scope getRegisteredScope(String scopeName) {
    return null;
  }

  @Override
  public AccessControlContext getAccessControlContext() {
    return null;
  }

  @Override
  public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {

  }

  @Override
  public void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException {

  }

  @Override
  public void resolveAliases(StringValueResolver valueResolver) {

  }

  @Override
  public BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
    return null;
  }

  @Override
  public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
    return false;
  }

  @Override
  public void setCurrentlyInCreation(String beanName, boolean inCreation) {

  }

  @Override
  public boolean isCurrentlyInCreation(String beanName) {
    return false;
  }

  @Override
  public void registerDependentBean(String beanName, String dependentBeanName) {

  }

  @Override
  public String[] getDependentBeans(String beanName) {
    return new String[0];
  }

  @Override
  public String[] getDependenciesForBean(String beanName) {
    return new String[0];
  }

  @Override
  public void destroyBean(String beanName, Object beanInstance) {

  }

  @Override
  public void destroyScopedBean(String beanName) {

  }

  @Override
  public void destroySingletons() {

  }

  @Override
  public Object getBean(String name) throws BeansException {
    return null;
  }

  @Override
  public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return null;
  }

  @Override
  public Object getBean(String name, Object... args) throws BeansException {
    return null;
  }

  @Override
  public <T> T getBean(Class<T> requiredType) throws BeansException {
    return null;
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
  public void registerSingleton(String beanName, Object singletonObject) {

  }

  @Override
  public Object getSingleton(String beanName) {
    return null;
  }

  @Override
  public boolean containsSingleton(String beanName) {
    return false;
  }

  @Override
  public String[] getSingletonNames() {
    return new String[0];
  }

  @Override
  public int getSingletonCount() {
    return 0;
  }

  @Override
  public Object getSingletonMutex() {
    return null;
  }
}
