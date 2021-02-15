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

package org.thepavel.icomponent.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public class BeanLookupImplTest {
  /**
   * Delegates to {@link ApplicationContext#getBean(String, Class)}
   * when both beanName and beanType arguments provided.
   */
  @Test
  public void looksUpBeanByNameAndTypeWhenBothProvided() {
    givenBeanName("dummy");
    andBeanType(Object.class);
    whenGetBeanMethodCalled();
    thenResultIs(context.getBeanByNameAndType());
  }

  /**
   * Delegates to {@link ApplicationContext#getBean(String)}
   * when only the beanName argument provided.
   */
  @Test
  public void looksUpBeanByNameWhenOnlyNameProvided() {
    givenBeanName("dummy");
    andBeanType(null);
    whenGetBeanMethodCalled();
    thenResultIs(context.getBeanByName());
  }

  /**
   * Delegates to {@link ApplicationContext#getBean(Class)}
   * when only the beanType argument provided.
   */
  @Test
  public void looksUpBeanByClassWhenOnlyClassProvided() {
    givenBeanName(null);
    andBeanType(Object.class);
    whenGetBeanMethodCalled();
    thenResultIs(context.getBeanByType());
  }

  @Test
  public void ignoresEmptyBeanName() {
    givenBeanName("");
    andBeanType(Object.class);
    whenGetBeanMethodCalled();
    thenResultIs(context.getBeanByType());
  }

  @Test
  public void ignoresBlankBeanName() {
    givenBeanName(" \t\n\r");
    andBeanType(Object.class);
    whenGetBeanMethodCalled();
    thenResultIs(context.getBeanByType());
  }

  private static DummyApplicationContext context;
  private static BeanLookup beanLookup;

  @BeforeAll
  public static void beforeAll() {
    context = new DummyApplicationContext();
    beanLookup = new BeanLookupImpl(context);
  }

  @AfterAll
  public static void afterAll() {
    context = null;
    beanLookup = null;
  }

  private String beanName;
  private Class<?> beanType;
  private Object result;

  @BeforeEach
  public void beforeEach() {
    beanName = null;
    beanType = null;
    result = null;
  }

  private void givenBeanName(String beanName) {
    this.beanName = beanName;
  }

  private void andBeanType(Class<?> beanType) {
    this.beanType = beanType;
  }

  private void whenGetBeanMethodCalled() {
    result = beanLookup.getBean(beanName, beanType);
  }

  private void thenResultIs(Object expected) {
    assertSame(expected, result);
  }
}
