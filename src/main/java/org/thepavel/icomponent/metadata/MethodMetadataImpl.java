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
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class MethodMetadataImpl implements MethodMetadata {
  private final ClassMetadata sourceClassMetadata;
  private final Method sourceMethod;
  private final List<ParameterMetadata> parameters = new ArrayList<>();
  private final List<ResolvedTypeMetadata> exceptions = new ArrayList<>();
  private ResolvedTypeMetadata returnTypeMetadata;
  private MergedAnnotations annotations;

  public MethodMetadataImpl(ClassMetadata sourceClassMetadata, Method sourceMethod) {
    this.sourceClassMetadata = sourceClassMetadata;
    this.sourceMethod = sourceMethod;
  }

  @Override
  public ClassMetadata getSourceClassMetadata() {
    return sourceClassMetadata;
  }

  @Override
  public Method getSourceMethod() {
    return sourceMethod;
  }

  @Override
  public ResolvedTypeMetadata getReturnTypeMetadata() {
    return returnTypeMetadata;
  }

  public void setReturnTypeMetadata(ResolvedTypeMetadata returnTypeMetadata) {
    this.returnTypeMetadata = returnTypeMetadata;
  }

  @Override
  public MergedAnnotations getAnnotations() {
    return annotations;
  }

  public void setAnnotations(MergedAnnotations annotations) {
    this.annotations = annotations;
  }

  @Override
  public List<ParameterMetadata> getParametersMetadata() {
    return unmodifiableList(parameters);
  }

  public void addParameterMetadata(ParameterMetadata metadata) {
    parameters.add(metadata);
  }

  @Override
  public List<ResolvedTypeMetadata> getExceptionsMetadata() {
    return unmodifiableList(exceptions);
  }

  public void addExceptionMetadata(ResolvedTypeMetadata metadata) {
    exceptions.add(metadata);
  }
}
