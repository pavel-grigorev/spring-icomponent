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

package org.thepavel.icomponent.beanname;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.thepavel.icomponent.util.AnnotationAttributes;

import java.lang.annotation.Annotation;

public class CustomAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {
  private final Class<? extends Annotation> annotationType;
  private final String beanNameAttribute;

  public CustomAnnotationBeanNameGenerator(Class<? extends Annotation> annotationType, String beanNameAttribute) {
    this.annotationType = annotationType;
    this.beanNameAttribute = beanNameAttribute;
  }

  @Override
  protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
    return AnnotationAttributes
        .of(annotationType)
        .declaredOn(annotatedDef.getMetadata())
        .getString(beanNameAttribute)
        .orElse(null);
  }
}
