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

package org.thepavel.icomponent;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.registrar.InterfaceComponentBeanDefinitionRegistrar;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runs component scanning targeted at annotated interfaces.
 * The annotation is {@link Component} by default but can be changed
 * using {@link #annotation()} and {@link #beanNameAnnotationAttribute()}.
 *
 * @see MethodHandler
 * @see Handler
 * @see DefaultMethodHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({
    InterfaceComponentConfiguration.class,
    InterfaceComponentBeanDefinitionRegistrar.class
})
public @interface InterfaceComponentScan {
  /**
   * Alias for {@link #basePackages()}.
   */
  @AliasFor("basePackages")
  String[] value() default {};

  /**
   * Packages to scan.
   */
  @AliasFor("value")
  String[] basePackages() default {};

  /**
   * The package of each class will be scanned.
   */
  Class<?>[] basePackageClasses() default {};

  /**
   * Interfaces marked with annotation of this type will be registered.
   */
  Class<? extends Annotation> annotation() default Component.class;

  /**
   * Name of the attribute under {@link #annotation()} specifying bean name.
   */
  String beanNameAnnotationAttribute() default "value";
}
