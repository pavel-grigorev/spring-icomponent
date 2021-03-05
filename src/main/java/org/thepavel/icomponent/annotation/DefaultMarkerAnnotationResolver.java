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

package org.thepavel.icomponent.annotation;

import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.util.AnnotationAttributes;

import java.lang.annotation.Annotation;

public class DefaultMarkerAnnotationResolver implements MarkerAnnotationResolver {
  protected String getAnnotationAttributeName() {
    return "annotation";
  }

  protected String getBeanNameAttributeName() {
    return "beanNameAnnotationAttribute";
  }

  protected Class<? extends Annotation> getDefaultAnnotationClass() {
    return Component.class;
  }

  protected String getDefaultBeanNameAnnotationAttribute() {
    return "value";
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<? extends Annotation> getAnnotationType(MergedAnnotation<?> annotation) {
    return (Class<? extends Annotation>) AnnotationAttributes
        .of(annotation)
        .getClass(getAnnotationAttributeName())
        .orElseGet(this::getDefaultAnnotationClass);
  }

  @Override
  public String getBeanNameAnnotationAttribute(MergedAnnotation<?> annotation) {
    return AnnotationAttributes
        .of(annotation)
        .getString(getBeanNameAttributeName())
        .orElseGet(this::getDefaultBeanNameAnnotationAttribute);
  }
}
