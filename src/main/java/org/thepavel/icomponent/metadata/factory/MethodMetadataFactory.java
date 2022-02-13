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

package org.thepavel.icomponent.metadata.factory;

import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.generic.GenericTypeParametersResolver;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.metadata.MethodMetadataImpl;
import org.thepavel.icomponent.metadata.ResolvedTypeMetadata;
import org.thepavel.icomponent.util.MethodHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MethodMetadataFactory {
  private final ReturnTypeMetadataFactory returnTypeMetadataFactory;
  private final ParameterMetadataFactory parameterMetadataFactory;
  private final ExceptionMetadataFactory exceptionMetadataFactory;

  MethodMetadataFactory(GenericTypeParametersResolver typeParametersResolver) {
    returnTypeMetadataFactory = new ReturnTypeMetadataFactory(typeParametersResolver);
    parameterMetadataFactory = new ParameterMetadataFactory(typeParametersResolver);
    exceptionMetadataFactory = new ExceptionMetadataFactory(typeParametersResolver);
  }

  List<MethodMetadata> getMethodMetadata(ClassMetadata classMetadata) {
    List<MethodMetadata> result = new ArrayList<>();

    ReflectionUtils.doWithMethods(
        classMetadata.getSourceClass(),
        method -> result.add(getMethodMetadata(classMetadata, method)),
        method -> !MethodHelper.isStatic(method)
    );

    return result;
  }

  private MethodMetadata getMethodMetadata(ClassMetadata classMetadata, Method method) {
    MethodMetadataImpl methodMetadata = new MethodMetadataImpl(classMetadata, method);
    methodMetadata.setReturnTypeMetadata(getReturnTypeMetadata(method));
    methodMetadata.setAnnotations(MergedAnnotations.from(method));
    addParametersMetadata(methodMetadata);
    addExceptionsMetadata(methodMetadata);
    return methodMetadata;
  }

  private ResolvedTypeMetadata getReturnTypeMetadata(Method method) {
    return returnTypeMetadataFactory.getReturnTypeMetadata(method);
  }

  private void addParametersMetadata(MethodMetadataImpl methodMetadata) {
    parameterMetadataFactory
        .getParameterMetadata(methodMetadata.getSourceMethod())
        .forEach(methodMetadata::addParameterMetadata);
  }

  private void addExceptionsMetadata(MethodMetadataImpl methodMetadata) {
    exceptionMetadataFactory
        .getExceptionMetadata(methodMetadata.getSourceMethod())
        .forEach(methodMetadata::addExceptionMetadata);
  }
}
