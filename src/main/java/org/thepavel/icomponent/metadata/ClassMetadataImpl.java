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

package org.thepavel.icomponent.metadata;

import org.springframework.core.annotation.MergedAnnotations;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableCollection;

public class ClassMetadataImpl implements ClassMetadata {
  private final Class<?> sourceClass;
  private final MergedAnnotations annotations;
  private final Map<Method, MethodMetadata> methodMetadataMap = new HashMap<>();

  public ClassMetadataImpl(Class<?> sourceClass, MergedAnnotations annotations) {
    this.sourceClass = sourceClass;
    this.annotations = annotations;
  }

  @Override
  public Class<?> getSourceClass() {
    return sourceClass;
  }

  @Override
  public MergedAnnotations getAnnotations() {
    return annotations;
  }

  @Override
  public Collection<MethodMetadata> getMethodsMetadata() {
    return unmodifiableCollection(methodMetadataMap.values());
  }

  @Override
  public MethodMetadata getMethodMetadata(Method method) {
    return methodMetadataMap.get(method);
  }

  public void addMethodMetadata(MethodMetadata metadata) {
    methodMetadataMap.put(metadata.getSourceMethod(), metadata);
  }
}
