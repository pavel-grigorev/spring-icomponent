package org.thepavel.icomponent.proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;

class DummyBeanFactory implements BeanFactory {
  private final Object bean;

  private DummyBeanFactory(Object bean) {
    this.bean = bean;
  }

  static DummyBeanFactory of(Object bean) {
    return new DummyBeanFactory(bean);
  }

  @Override
  public Object getBean(String name) throws BeansException {
    return bean;
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
}
