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

package org.thepavel.icomponent;

import org.springframework.core.annotation.AliasFor;
import org.thepavel.icomponent.handler.MethodHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a method handler, i.e. bean of type {@link MethodHandler},
 * to be used to handle method invocations.
 *
 * When declared on a method, the specified method handler will handle
 * invocations of the method under the declaration.
 *
 * When declared on a class, the specified method handler will handle
 * invocations of all methods of the class that do not have their own
 * {@link Handler} declaration.
 *
 * @see MethodHandler
 * @see DefaultMethodHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Handler {
  /**
   * Bean name.
   */
  @AliasFor("beanName")
  String value() default "";

  /**
   * Bean name.
   */
  @AliasFor("value")
  String beanName() default "";

  /**
   * Bean type.
   */
  Class<? extends MethodHandler> beanType() default MethodHandler.class;
}
