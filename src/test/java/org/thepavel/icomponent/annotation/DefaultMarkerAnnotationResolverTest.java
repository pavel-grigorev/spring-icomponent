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

package org.thepavel.icomponent.annotation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.InterfaceComponentScan;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DefaultMarkerAnnotationResolverTest {
  @Test
  public void resolvesAnnotationType() {
    givenClass(CustomMarkerAnnotationTest.class);
    whenMarkerAnnotationTypeResolved();
    thenMarkerAnnotationTypeIs(CustomAnnotation.class);
  }

  @Test
  public void resolvesBeanNameAnnotationAttribute() {
    givenClass(CustomMarkerAnnotationTest.class);
    whenBeanNameAnnotationAttributeResolved();
    thenBeanNameAnnotationAttributeIs("beanName");
  }

  @Test
  public void annotationTypeDefaultsToComponent() {
    givenClass(DefaultMarkerAnnotationTest.class);
    whenMarkerAnnotationTypeResolved();
    thenMarkerAnnotationTypeIs(Component.class);
  }

  @Test
  public void beanNameAnnotationAttributeDefaultsToValue() {
    givenClass(DefaultMarkerAnnotationTest.class);
    whenBeanNameAnnotationAttributeResolved();
    thenBeanNameAnnotationAttributeIs("value");
  }

  private static MarkerAnnotationResolver resolver;

  @BeforeAll
  public static void beforeAll() {
    resolver = new DefaultMarkerAnnotationResolver();
  }

  @AfterAll
  public static void afterAll() {
    resolver = null;
  }

  private MergedAnnotation<?> annotation;
  private Class<? extends Annotation> markerAnnotationType;
  private String beanNameAnnotationAttribute;

  @BeforeEach
  public void beforeEach() {
    annotation = null;
    markerAnnotationType = null;
    beanNameAnnotationAttribute = null;
  }

  private void givenClass(Class<?> clazz) {
    annotation = AnnotationMetadata
        .introspect(clazz)
        .getAnnotations()
        .get(InterfaceComponentScan.class);
  }

  private void whenMarkerAnnotationTypeResolved() {
    markerAnnotationType = resolver.getAnnotationType(annotation);
  }

  private void whenBeanNameAnnotationAttributeResolved() {
    beanNameAnnotationAttribute = resolver.getBeanNameAnnotationAttribute(annotation);
  }

  private void thenMarkerAnnotationTypeIs(Class<? extends Annotation> expected) {
    assertSame(expected, markerAnnotationType);
  }

  private void thenBeanNameAnnotationAttributeIs(String expected) {
    assertEquals(expected, beanNameAnnotationAttribute);
  }

  @InterfaceComponentScan
  private interface DefaultMarkerAnnotationTest {
  }

  @InterfaceComponentScan(
      annotation = CustomAnnotation.class,
      beanNameAnnotationAttribute = "beanName"
  )
  private interface CustomMarkerAnnotationTest {
  }

  private @interface CustomAnnotation {
    String beanName() default "";
  }
}