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

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.thepavel.icomponent.beanname.CustomAnnotationBeanNameGenerator;
import org.thepavel.icomponent.util.BeanDefinitionHelper;

import java.lang.annotation.Annotation;

/**
 * Adds bean definitions for annotated interfaces.
 */
public class InterfaceComponentBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
  public InterfaceComponentBeanDefinitionScanner(
      BeanDefinitionRegistry registry,
      Class<? extends Annotation> annotationType,
      String beanNameAnnotationAttribute
  ) {
    super(registry, false);
    addIncludeFilter(new AnnotationTypeFilter(annotationType));
    setBeanNameGenerator(new CustomAnnotationBeanNameGenerator(annotationType, beanNameAnnotationAttribute));
  }

  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    return BeanDefinitionHelper.isInterface(beanDefinition);
  }
}
