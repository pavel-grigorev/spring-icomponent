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
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.generic.GenericTypeParametersResolver;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.ClassMetadataImpl;

import static org.thepavel.icomponent.util.AnnotationMetadataHelper.getSourceClass;

public class ClassMetadataFactoryBean implements ClassMetadataFactory {
  @Override
  public ClassMetadata getClassMetadata(AnnotationMetadata annotationMetadata) {
    Class<?> annotatedClass = getSourceClass(annotationMetadata);
    MergedAnnotations annotations = annotationMetadata.getAnnotations();

    ClassMetadataImpl classMetadata = new ClassMetadataImpl(annotatedClass, annotations);

    getMethodMetadataFactory(annotatedClass)
        .getMethodMetadata(classMetadata)
        .forEach(classMetadata::addMethodMetadata);

    return classMetadata;
  }

  private static MethodMetadataFactory getMethodMetadataFactory(Class<?> clazz) {
    return new MethodMetadataFactory(new GenericTypeParametersResolver(clazz));
  }
}
