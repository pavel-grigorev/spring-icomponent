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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.InterfaceComponentScan;

@InterfaceComponentScan
public class InterfaceComponentBeanDefinitionRegistrarTest extends BaseBeanDefinitionRegistryTest {
  @Test
  public void registersBeanDefinitionsForAnnotatedInterfacesInPackage() {
    givenConfigurationClass(InterfaceComponentBeanDefinitionRegistrarTest.class);
    whenInterfaceComponentBeanDefinitionRegistrarRan();
    thenRegistryContainsBeanDefinitions(EXPECTED_BEANS);
    andRegistryDoesNotContainBeanDefinitions(UNEXPECTED_BEANS);
    andBeanDefinitionsAreProperlyConfigured(EXPECTED_BEANS);
  }

  private AnnotationMetadata annotationMetadata;

  @BeforeEach
  public void beforeEach() {
    annotationMetadata = null;
  }

  @SuppressWarnings("SameParameterValue")
  private void givenConfigurationClass(Class<?> clazz) {
    annotationMetadata = AnnotationMetadata.introspect(clazz);
  }

  private void whenInterfaceComponentBeanDefinitionRegistrarRan() {
    new InterfaceComponentBeanDefinitionRegistrar()
        .registerBeanDefinitions(annotationMetadata, registry);
  }
}
