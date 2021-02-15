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

package org.thepavel.icomponent.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.resolver.MethodHandlerResolver;
import org.thepavel.icomponent.metadata.MethodMetadata;

import java.lang.reflect.Type;

@Component
class ReturningBooleanMethodHandlerResolver implements MethodHandlerResolver {
  @Autowired
  private ReturningBooleanMethodHandler returningBooleanMethodHandler;

  @Override
  public MethodHandler getMethodHandler(MethodMetadata methodMetadata) {
    return isBoolean(getReturnType(methodMetadata)) ?
        returningBooleanMethodHandler : null;
  }

  private static Type getReturnType(MethodMetadata methodMetadata) {
    return methodMetadata
        .getReturnTypeMetadata()
        .getResolvedType();
  }

  private static boolean isBoolean(Type type) {
    return type == boolean.class || type == Boolean.class;
  }
}
