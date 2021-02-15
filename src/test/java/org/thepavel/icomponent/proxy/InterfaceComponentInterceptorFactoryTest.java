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
import org.thepavel.icomponent.handler.MethodHandlerMap;
import org.thepavel.icomponent.handler.resolver.MethodHandlerMapResolver;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("SameParameterValue")
public class InterfaceComponentInterceptorFactoryTest {
  @Test
  public void getsMethodHandlerMapFromMethodHandlerMapResolver() {
    DummyMethodHandlerMapResolver resolver = new DummyMethodHandlerMapResolver();
    ClassMetadata classMetadata = getClassMetadata(TestInterface.class);

    givenMethodHandlerMapResolver(resolver);
    whenInterceptorCreatedByFactoryFor(classMetadata);
    thenInterceptorHasClassMetadata(classMetadata);
    andInterceptorHasMethodHandlerMap(resolver.getMethodHandlerMap());
  }

  private MethodHandlerMapResolver resolver;
  private InterfaceComponentInterceptor interceptor;

  @BeforeEach
  public void beforeEach() {
    resolver = null;
    interceptor = null;
  }

  private void givenMethodHandlerMapResolver(MethodHandlerMapResolver resolver) {
    this.resolver = resolver;
  }

  private void whenInterceptorCreatedByFactoryFor(ClassMetadata classMetadata) {
    interceptor = new InterfaceComponentInterceptorFactory(resolver).getInterceptor(classMetadata);
    assertNotNull(interceptor);
  }

  private void thenInterceptorHasClassMetadata(ClassMetadata classMetadata) {
    assertSame(classMetadata, interceptor.getClassMetadata());
  }

  private void andInterceptorHasMethodHandlerMap(MethodHandlerMap expected) {
    assertSame(expected, interceptor.getMethodHandlerMap());
  }

  private static ClassMetadata getClassMetadata(Class<?> clazz) {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(clazz);
    return new ClassMetadataFactoryBean().getClassMetadata(annotationMetadata);
  }
}
