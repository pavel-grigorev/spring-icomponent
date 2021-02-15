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

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.util.AnnotationAttributes;

import java.lang.annotation.Annotation;

public abstract class BaseMarkerAnnotationResolver implements MarkerAnnotationResolver {
  protected abstract Class<? extends Annotation> getDeclaringAnnotationType();

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
  public Class<? extends Annotation> getAnnotationType(AnnotationMetadata metadata) {
    return (Class<? extends Annotation>) getAnnotationAttributes(metadata)
        .getClass(getAnnotationAttributeName())
        .orElseGet(this::getDefaultAnnotationClass);
  }

  @Override
  public String getBeanNameAnnotationAttribute(AnnotationMetadata metadata) {
    return getAnnotationAttributes(metadata)
        .getString(getBeanNameAttributeName())
        .orElseGet(this::getDefaultBeanNameAnnotationAttribute);
  }

  private AnnotationAttributes<?> getAnnotationAttributes(AnnotationMetadata metadata) {
    return AnnotationAttributes
        .of(getDeclaringAnnotationType())
        .declaredOn(metadata);
  }
}
