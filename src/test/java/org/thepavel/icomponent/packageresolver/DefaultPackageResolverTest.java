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

package org.thepavel.icomponent.packageresolver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.InterfaceComponentScan;
import org.thepavel.icomponent.integration.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class DefaultPackageResolverTest {
  private static final String THIS_PACKAGE =
      DefaultPackageResolverTest.class.getPackage().getName();

  @Test
  public void resolvesFromSingleValue() {
    givenClass(SingleValueTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre("dummy");
  }

  @Test
  public void resolvesFromMultipleValues() {
    givenClass(MultipleValuesTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre("test1", "test2");
  }

  @Test
  public void resolvesFromSingleBasePackageClass() {
    givenClass(SingleBasePackageClassTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre(IntegrationTest.class.getPackage().getName());
  }

  @Test
  public void resolvesFromMultipleBasePackageClasses() {
    givenClass(MultipleBasePackageClassesTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre(IntegrationTest.class.getPackage().getName(), THIS_PACKAGE);
  }

  @Test
  public void resolvesFromClassNameWhenAnnotationHasNoValue() {
    givenClass(NoValueTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre(THIS_PACKAGE);
  }

  @Test
  public void resolvesFromClassNameWhenAnnotationHasBlankValue() {
    givenClass(BlankValueTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre(THIS_PACKAGE);
  }

  @Test
  public void resolvesFromClassNameWhenAnnotationHasMultipleBlankValues() {
    givenClass(MultipleBlankValuesTest.class);
    whenPackagesResolved();
    thenResolvedPackagesAre(THIS_PACKAGE);
  }

  private static PackageResolver packageResolver;

  @BeforeAll
  public static void beforeAll() {
    packageResolver = new DefaultPackageResolver();
  }

  @AfterAll
  public static void afterAll() {
    packageResolver = null;
  }

  private String className;
  private MergedAnnotation<?> annotation;
  private String[] resolvedPackages;

  @BeforeEach
  public void beforeEach() {
    className = null;
    annotation = null;
    resolvedPackages = null;
  }

  private void givenClass(Class<?> clazz) {
    className = clazz.getName();
    annotation = AnnotationMetadata
        .introspect(clazz)
        .getAnnotations()
        .get(InterfaceComponentScan.class);
  }

  private void whenPackagesResolved() {
    resolvedPackages = packageResolver.getPackageNames(annotation, className);
  }

  private void thenResolvedPackagesAre(String... expected) {
    assertArrayEquals(expected, resolvedPackages);
  }

  @InterfaceComponentScan("dummy")
  private interface SingleValueTest {
  }

  @InterfaceComponentScan({"test1", "test2"})
  private interface MultipleValuesTest {
  }

  @InterfaceComponentScan(basePackageClasses = IntegrationTest.class)
  private interface SingleBasePackageClassTest {
  }

  @InterfaceComponentScan(basePackageClasses = {
      IntegrationTest.class,
      DefaultPackageResolverTest.class
  })
  private interface MultipleBasePackageClassesTest {
  }

  @InterfaceComponentScan
  private interface NoValueTest {
  }

  @InterfaceComponentScan(" \t\n\r")
  private interface BlankValueTest {
  }

  @InterfaceComponentScan({" \t\n\r", " ", ""})
  private interface MultipleBlankValuesTest {
  }
}
