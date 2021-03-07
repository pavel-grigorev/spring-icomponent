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
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.MethodHandlerMap;
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;

import java.util.List;
import java.util.Objects;

public class MethodHandlerMapResolverBean implements MethodHandlerMapResolver {
  private final List<MethodHandlerResolver> methodHandlerResolvers;

  public MethodHandlerMapResolverBean(List<MethodHandlerResolver> methodHandlerResolvers) {
    this.methodHandlerResolvers = methodHandlerResolvers;
  }

  @Override
  public MethodHandlerMap getMethodHandlerMap(ClassMetadata classMetadata) {
    return classMetadata
        .getMethodsMetadata()
        .stream()
        .collect(MethodHandlerMap.collector(MethodMetadata::getSourceMethod, this::getMethodHandler));
  }

  private MethodHandler getMethodHandler(MethodMetadata methodMetadata) {
    return methodHandlerResolvers
        .stream()
        .map(resolver -> resolver.getMethodHandler(methodMetadata))
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(() -> noMethodHandlerFound(methodMetadata));
  }

  private static BeanInstantiationException noMethodHandlerFound(MethodMetadata metadata) {
    return new BeanInstantiationException(
        metadata.getSourceClassMetadata().getSourceClass(),
        "No method handler found for method " + metadata.getSourceMethod()
    );
  }
}
