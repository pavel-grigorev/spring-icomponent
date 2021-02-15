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

package org.thepavel.icomponent.packageresolver;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.thepavel.icomponent.util.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseAnnotationPackageResolver implements PackageResolver {
  private Set<String> result;

  protected abstract Class<? extends Annotation> getAnnotationType();

  protected String getBasePackagesAttributeName() {
    return "basePackages";
  }

  protected String getBasePackageClassesAttributeName() {
    return "basePackageClasses";
  }

  @Override
  public String[] getPackageNames(AnnotationMetadata metadata) {
    result = new LinkedHashSet<>();

    collectFromAnnotation(metadata);

    if (result.isEmpty()) {
      collectFromClassName(metadata);
    }

    return result.toArray(new String[0]);
  }

  private void collectFromAnnotation(AnnotationMetadata metadata) {
    AnnotationAttributes<?> attributes = getAnnotationAttributes(metadata);

    collectFromBasePackages(attributes);
    collectFromBasePackageClasses(attributes);
  }

  private AnnotationAttributes<?> getAnnotationAttributes(AnnotationMetadata metadata) {
    return AnnotationAttributes
        .of(getAnnotationType())
        .declaredOn(metadata);
  }

  private void collectFromBasePackages(AnnotationAttributes<?> attributes) {
    String attributeName = getBasePackagesAttributeName();

    if (isBlank(attributeName)) {
      return;
    }

    attributes
        .getStrings(attributeName)
        .ifPresent(result::addAll);
  }

  private void collectFromBasePackageClasses(AnnotationAttributes<?> attributes) {
    String attributeName = getBasePackageClassesAttributeName();

    if (isBlank(attributeName)) {
      return;
    }

    attributes
        .getClasses(attributeName)
        .ifPresent(this::collectFromClasses);
  }

  private void collectFromClasses(List<Class<?>> classes) {
    classes
        .stream()
        .map(ClassUtils::getPackageName)
        .forEach(result::add);
  }

  private void collectFromClassName(AnnotationMetadata metadata) {
    result.add(getPackageOfClass(metadata));
  }

  private static String getPackageOfClass(AnnotationMetadata metadata) {
    return ClassUtils.getPackageName(metadata.getClassName());
  }
}
