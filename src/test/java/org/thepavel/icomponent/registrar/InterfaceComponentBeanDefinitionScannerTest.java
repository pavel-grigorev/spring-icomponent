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

package org.thepavel.icomponent.registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

public class InterfaceComponentBeanDefinitionScannerTest extends BaseBeanDefinitionRegistryTest {
  private static final String THIS_PACKAGE =
      InterfaceComponentBeanDefinitionScannerTest.class.getPackage().getName();
  private static final String DUMMY_BEAN = "dummyBean";

  @Test
  public void registersInterfacesWithComponentAnnotation() {
    givenPackageName(THIS_PACKAGE);
    andAnnotationType(Component.class);
    andBeanNameAttribute("value");
    whenPackageScanned();
    thenRegistryContainsBeanDefinitions(EXPECTED_BEANS);
    andRegistryDoesNotContainBeanDefinitions(UNEXPECTED_BEANS);
    andRegistryDoesNotContainBeanDefinitions(DUMMY_BEAN);
    andBeanDefinitionsAreProperlyConfigured(EXPECTED_BEANS);
  }

  @Test
  public void registersInterfacesWithCustomAnnotation() {
    givenPackageName(THIS_PACKAGE);
    andAnnotationType(CustomAnnotation.class);
    andBeanNameAttribute("beanName");
    whenPackageScanned();
    thenRegistryContainsBeanDefinitions(DUMMY_BEAN);
    andRegistryDoesNotContainBeanDefinitions(EXPECTED_BEANS);
    andRegistryDoesNotContainBeanDefinitions(UNEXPECTED_BEANS);
    andBeanDefinitionsAreProperlyConfigured(DUMMY_BEAN);
  }

  private String packageName;
  private Class<? extends Annotation> annotationType;
  private String beanNameAttribute;

  @BeforeEach
  public void beforeEach() {
    packageName = null;
    annotationType = null;
    beanNameAttribute = null;
  }

  @SuppressWarnings("SameParameterValue")
  private void givenPackageName(String packageName) {
    this.packageName = packageName;
  }

  private void andAnnotationType(Class<? extends Annotation> annotationType) {
    this.annotationType = annotationType;
  }

  private void andBeanNameAttribute(String beanNameAttribute) {
    this.beanNameAttribute = beanNameAttribute;
  }

  private void whenPackageScanned() {
    new InterfaceComponentBeanDefinitionScanner(registry, annotationType, beanNameAttribute).scan(packageName);
  }
}
