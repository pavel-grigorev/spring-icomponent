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

package org.thepavel.icomponent.handler.resolver;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.core.annotation.Order;
import org.thepavel.icomponent.DefaultMethodHandler;
import org.thepavel.icomponent.Handler;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.metadata.MethodMetadata;

/**
 * Beans of this type are to resolve a method handler, i.e. bean of type {@link MethodHandler},
 * for the given method represented by {@link MethodMetadata}.
 *
 * Multiple method handler resolvers may be defined in the application, each being responsible
 * for a specific type of method handler.
 *
 * When proxies are being created fo the interfaces the framework performs method handler lookup
 * for every interface method. It does so by running all method handler resolvers one by one
 * until it obtains a method handler. The first method handler obtained is going to be linked
 * with the method. To control the order in which the resolvers run use {@link Order}.
 *
 * If all method handler resolvers return {@code null} for the given method then
 * {@link BeanInstantiationException} is thrown.
 *
 * There are two pre-built method handler resolvers:
 *
 * {@link HandlerAnnotationMethodHandlerResolver} is responsible for {@link Handler} annotation.
 *
 * {@link DefaultMethodHandlerResolver} is responsible for {@link DefaultMethodHandler} annotation.
 *
 * @see MethodHandler
 */
public interface MethodHandlerResolver {
  MethodHandler getMethodHandler(MethodMetadata methodMetadata);
}
