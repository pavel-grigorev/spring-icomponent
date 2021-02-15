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

package org.thepavel.icomponent.handler;

import org.springframework.stereotype.Component;
import org.thepavel.icomponent.DefaultMethodHandler;
import org.thepavel.icomponent.Handler;
import org.thepavel.icomponent.InterfaceComponentScan;
import org.thepavel.icomponent.handler.resolver.MethodHandlerResolver;
import org.thepavel.icomponent.metadata.MethodMetadata;

/**
 * Beans of this type are to handle invocations of methods under interfaces
 * annotated with {@link Component} or any other annotation configured
 * by {@link InterfaceComponentScan}.
 *
 * Method handler can be mapped to an actual method by using {@link Handler}
 * annotation or by implementing a bean of type {@link MethodHandlerResolver}.
 *
 * @see MethodHandlerResolver
 * @see DefaultMethodHandler
 * @see Handler
 */
public interface MethodHandler {
  Object handle(Object[] arguments, MethodMetadata methodMetadata);
}
