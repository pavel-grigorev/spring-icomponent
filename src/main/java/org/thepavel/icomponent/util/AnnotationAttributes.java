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

package org.thepavel.icomponent.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.core.annotation.MergedAnnotation.VALUE;

public class AnnotationAttributes<T extends Annotation> {
  private final Class<T> annotationType;
  private MergedAnnotation<T> annotation;

  private AnnotationAttributes(Class<T> annotationType) {
    this.annotationType = annotationType;
  }

  private AnnotationAttributes(MergedAnnotation<T> annotation) {
    this.annotationType = null;
    this.annotation = annotation;
  }

  public static <T extends Annotation> AnnotationAttributes<T> of(Class<T> annotationType) {
    return new AnnotationAttributes<>(annotationType);
  }

  public static <T extends Annotation> AnnotationAttributes<T> of(MergedAnnotation<T> annotation) {
    return new AnnotationAttributes<>(annotation);
  }

  public AnnotationAttributes<T> declaredOn(AnnotatedTypeMetadata metadata) {
    if (annotationType == null) {
      throw new IllegalStateException("Annotation type is undefined");
    }
    annotation = metadata.getAnnotations().get(annotationType);
    return this;
  }

  private MergedAnnotation<T> getAnnotation() {
    if (annotation == null) {
      throw new IllegalStateException("Annotation is undefined");
    }
    return annotation;
  }

  public Optional<String> getString(String name) {
    return getAnnotation()
        .getValue(name, String.class)
        .filter(StringUtils::isNotBlank);
  }

  public Optional<List<String>> getStrings(String name) {
    return getAnnotation()
        .getValue(name, String[].class)
        .map(this::filterOutBlankStrings)
        .filter(list -> !list.isEmpty());
  }

  private List<String> filterOutBlankStrings(String[] strings) {
    return Arrays
        .stream(strings)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }

  public Optional<Class<?>> getClass(String name) {
    return getAnnotation()
        .getValue(name, Class.class)
        .map(c -> (Class<?>) c);
  }

  @SuppressWarnings("unchecked")
  public Optional<List<Class<?>>> getClasses(String name) {
    Optional<List<Class<?>>> result = getAnnotation()
        .getValue(name, Class[].class)
        .map(Arrays::asList);

    return result.filter(list -> !list.isEmpty());
  }

  public Optional<String> getValueAsString() {
    return getString(VALUE);
  }

  public Optional<List<String>> getValueAsStrings() {
    return getStrings(VALUE);
  }

  public Optional<Class<?>> getValueAsClass() {
    return getClass(VALUE);
  }

  public Optional<List<Class<?>>> getValueAsClasses() {
    return getClasses(VALUE);
  }
}
