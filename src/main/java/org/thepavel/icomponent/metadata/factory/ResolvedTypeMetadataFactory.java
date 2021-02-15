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

package org.thepavel.icomponent.metadata.factory;

import org.springframework.core.annotation.MergedAnnotations;
import org.thepavel.icomponent.generic.GenericTypeParametersResolver;
import org.thepavel.icomponent.metadata.ResolvedTypeMetadata;
import org.thepavel.icomponent.metadata.ResolvedTypeMetadataImpl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

abstract class ResolvedTypeMetadataFactory {
  private final GenericTypeParametersResolver typeParametersResolver;

  ResolvedTypeMetadataFactory(GenericTypeParametersResolver typeParametersResolver) {
    this.typeParametersResolver = typeParametersResolver;
  }

  ResolvedTypeMetadata getResolvedTypeMetadata(Type genericType, AnnotatedElement annotatedElement) {
    return new ResolvedTypeMetadataImpl(
        typeParametersResolver.resolveType(genericType),
        MergedAnnotations.from(annotatedElement)
    );
  }
}
