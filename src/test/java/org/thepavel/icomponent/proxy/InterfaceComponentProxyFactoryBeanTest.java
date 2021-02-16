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

package org.thepavel.icomponent.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("SameParameterValue")
public class InterfaceComponentProxyFactoryBeanTest {
  @Test
  public void providesObjectType() {
    givenAnnotationMetadataFor(InterfaceComponentProxyFactoryBeanTest.class);
    whenProxyFactoryBeanCreated();
    thenProxyFactoryBeanHasObjectType(InterfaceComponentProxyFactoryBeanTest.class);
  }

  @Test
  public void delegatesToProxyFactory() {
    Object object = new Object();

    givenAnnotationMetadataFor(InterfaceComponentProxyFactoryBeanTest.class);
    andProxyFactoryReturning(object);
    whenProxyFactoryBeanCreated();
    thenProxyFactoryBeanReturnsObject(object);
  }

  private AnnotationMetadata annotationMetadata;
  private InterfaceComponentProxyFactoryBean proxyFactoryBean;
  private Object proxyObject;

  @BeforeEach
  public void beforeEach() {
    annotationMetadata = null;
    proxyFactoryBean = null;
    proxyObject = null;
  }

  private void givenAnnotationMetadataFor(Class<?> clazz) {
    annotationMetadata = AnnotationMetadata.introspect(clazz);
  }

  private void andProxyFactoryReturning(Object proxyObject) {
    this.proxyObject = proxyObject;
  }

  private void whenProxyFactoryBeanCreated() {
    proxyFactoryBean = new InterfaceComponentProxyFactoryBean(annotationMetadata);
    proxyFactoryBean.setProxyFactory(new DummyProxyFactory());
  }

  private void thenProxyFactoryBeanHasObjectType(Class<?> expected) {
    assertEquals(expected, proxyFactoryBean.getObjectType());
  }

  private void thenProxyFactoryBeanReturnsObject(Object expected) {
    assertSame(expected, proxyFactoryBean.getObject());
  }

  private class DummyProxyFactory extends InterfaceComponentProxyFactory {
    private DummyProxyFactory() {
      super(null, null, null);
    }

    @Override
    public Object createProxy(AnnotationMetadata annotationMetadata) {
      return proxyObject;
    }
  }
}
