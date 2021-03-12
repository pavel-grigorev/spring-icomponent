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

package org.thepavel.icomponent.registrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.InterfaceComponentScan;
import org.thepavel.icomponent.annotation.DefaultMarkerAnnotationResolver;
import org.thepavel.icomponent.annotation.MarkerAnnotationResolver;
import org.thepavel.icomponent.packageresolver.DefaultPackageResolver;
import org.thepavel.icomponent.packageresolver.PackageResolver;

import java.lang.annotation.Annotation;

public class InterfaceComponentBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
  @Override
  public void registerBeanDefinitions(AnnotationMetadata configMetadata, BeanDefinitionRegistry registry) {
    registerConfiguration(registry);

    String className = configMetadata.getClassName();
    PackageResolver packageResolver = getPackageResolver();
    MarkerAnnotationResolver markerAnnotationResolver = getMarkerAnnotationResolver();

    configMetadata
        .getAnnotations()
        .stream(getAnnotationType())
        .forEach(annotation -> {
          String[] packageNames = packageResolver.getPackageNames(annotation, className);

          Class<? extends Annotation> markerAnnotation = markerAnnotationResolver.getAnnotationType(annotation);
          String beanNameAnnotationAttribute = markerAnnotationResolver.getBeanNameAnnotationAttribute(annotation);

          getScanner(registry, markerAnnotation, beanNameAnnotationAttribute).scan(packageNames);
        });
  }

  protected void registerConfiguration(BeanDefinitionRegistry registry) {
    InterfaceComponentConfigurationRegistrar.registerConfiguration(registry);
  }

  protected PackageResolver getPackageResolver() {
    return new DefaultPackageResolver();
  }

  protected MarkerAnnotationResolver getMarkerAnnotationResolver() {
    return new DefaultMarkerAnnotationResolver();
  }

  protected Class<? extends Annotation> getAnnotationType() {
    return InterfaceComponentScan.class;
  }

  protected ClassPathBeanDefinitionScanner getScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> markerAnnotation, String beanNameAnnotationAttribute) {
    return new InterfaceComponentBeanDefinitionScanner(registry, markerAnnotation, beanNameAnnotationAttribute);
  }
}
