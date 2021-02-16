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

package org.thepavel.icomponent.proxy;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactory;
import org.thepavel.icomponent.metadata.validation.ClassMetadataValidationException;
import org.thepavel.icomponent.metadata.validation.ClassMetadataValidator;

import java.util.List;

@Component(InterfaceComponentProxyFactory.NAME)
public class InterfaceComponentProxyFactory {
  public static final String NAME =
      "org.thepavel.icomponent.proxy.internalInterfaceComponentProxyFactory";
  public static final String METHOD_NAME = "createProxy";

  private final ClassMetadataFactory classMetadataFactory;
  private final List<ClassMetadataValidator> classMetadataValidators;
  private final InterfaceComponentInterceptorFactory interceptorFactory;

  @Autowired
  public InterfaceComponentProxyFactory(
      ClassMetadataFactory classMetadataFactory,
      List<ClassMetadataValidator> classMetadataValidators,
      InterfaceComponentInterceptorFactory interceptorFactory
  ) {
    this.classMetadataFactory = classMetadataFactory;
    this.classMetadataValidators = classMetadataValidators;
    this.interceptorFactory = interceptorFactory;
  }

  @SuppressWarnings("unused")
  public Object createProxy(AnnotationMetadata annotationMetadata) {
    ClassMetadata classMetadata = getClassMetadata(annotationMetadata);
    validateClassMetadata(classMetadata);
    return createProxy(classMetadata);
  }

  private ClassMetadata getClassMetadata(AnnotationMetadata annotationMetadata) {
    return classMetadataFactory.getClassMetadata(annotationMetadata);
  }

  private void validateClassMetadata(ClassMetadata classMetadata) {
    for (ClassMetadataValidator classMetadataValidator : classMetadataValidators) {
      try {
        classMetadataValidator.validate(classMetadata);
      } catch (ClassMetadataValidationException e) {
        throw new BeanInstantiationException(classMetadata.getSourceClass(), e.getMessage());
      }
    }
  }

  private Object createProxy(ClassMetadata classMetadata) {
    return ProxyFactory.getProxy(classMetadata.getSourceClass(), getInterceptor(classMetadata));
  }

  private InterfaceComponentInterceptor getInterceptor(ClassMetadata classMetadata) {
    return interceptorFactory.getInterceptor(classMetadata);
  }
}
