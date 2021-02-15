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

import org.springframework.beans.factory.annotation.Qualifier;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.resolver.MethodHandlerResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A qualifier to be declared on a method handler, i.e. bean of type {@link MethodHandler},
 * to make it be recognized by the framework as the default method handler.
 *
 * The default method handler is to handle invocations of all methods that do not have
 * a specific method handler defined by {@link Handler} or {@link MethodHandlerResolver}.
 *
 * There can only be one default method handler in the application.
 *
 * @see MethodHandler
 * @see MethodHandlerResolver
 * @see Handler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Qualifier
public @interface DefaultMethodHandler {
}
