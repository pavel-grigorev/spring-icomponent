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

package org.thepavel.icomponent.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactory;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;
import org.thepavel.icomponent.metadata.validation.ClassMetadataValidator;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SameParameterValue")
public class InterfaceComponentProxyFactoryTest {
  @Test
  public void createsJdkDynamicProxy() {
    givenClass(TestInterface.class);
    whenProxyBeanCreated();
    thenProxyBeanIs(AopUtils::isJdkDynamicProxy);
  }

  @Test
  public void validatesClassMetadata() {
    givenClass(TestInterface.class);
    andClassMetadataValidator(new FailingClassMetadataValidator());
    whenProxyBeanCreated();
    thenProxyBeanIs(Objects::isNull);
    andExceptionIs(BeanInstantiationException.class);
  }

  @Test
  public void delegatesToInterfaceComponentInterceptor() {
    givenClass(TestInterface.class);
    whenProxyBeanCreated();
    andMethodCalledOnProxyBean(TestInterface::test);
    thenInterceptorReceivedInvocationObjectForMethod("test");
  }

  private static DummyInterfaceComponentInterceptor interceptor;
  private static InterfaceComponentInterceptorFactory interceptorFactory;
  private static ClassMetadataFactory classMetadataFactory;

  @BeforeAll
  public static void beforeAll() {
    interceptor = new DummyInterfaceComponentInterceptor();
    interceptorFactory = new DummyInterfaceComponentInterceptorFactory(interceptor);
    classMetadataFactory = new ClassMetadataFactoryBean();
  }

  @AfterAll
  public static void afterAll() {
    interceptor = null;
    interceptorFactory = null;
    classMetadataFactory = null;
  }

  private Class<?> targetClass;
  private ClassMetadataValidator validator;
  private Object proxyBean;
  private Class<?> exceptionType;

  @BeforeEach
  public void beforeEach() {
    targetClass = null;
    validator = new DummyClassMetadataValidator();
    proxyBean = null;
    exceptionType = null;
  }

  private void givenClass(Class<?> targetClass) {
    this.targetClass = targetClass;
  }

  private void andClassMetadataValidator(ClassMetadataValidator validator) {
    this.validator = validator;
  }

  private void whenProxyBeanCreated() {
    try {
      proxyBean = getProxyFactory().createProxy(AnnotationMetadata.introspect(targetClass));
      assertNotNull(proxyBean);
    } catch (Exception e) {
      exceptionType = e.getClass();
    }
  }

  @SuppressWarnings("unchecked")
  private <T> void andMethodCalledOnProxyBean(Consumer<T> consumer) {
    consumer.accept((T) proxyBean);
  }

  private void thenProxyBeanIs(Predicate<Object> tester) {
    assertTrue(tester.test(proxyBean));
  }

  private void andExceptionIs(Class<?> expected) {
    assertSame(expected, exceptionType);
  }

  private void thenInterceptorReceivedInvocationObjectForMethod(String methodName) {
    MethodInvocation invocation = interceptor.getPassedMethodInvocation();

    assertNotNull(invocation);
    assertEquals(getMethod(methodName), invocation.getMethod());
  }

  private Method getMethod(String methodName) {
    Method method = ReflectionUtils.findMethod(targetClass, methodName);
    assertNotNull(method);
    return method;
  }

  private InterfaceComponentProxyFactory getProxyFactory() {
    return new InterfaceComponentProxyFactory(classMetadataFactory, singletonList(validator), interceptorFactory);
  }
}
