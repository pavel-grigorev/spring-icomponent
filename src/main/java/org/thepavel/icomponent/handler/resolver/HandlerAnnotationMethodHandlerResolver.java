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

import org.springframework.core.type.AnnotatedTypeMetadata;
import org.thepavel.icomponent.Handler;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.util.AnnotationAttributes;
import org.thepavel.icomponent.util.BeanLookup;

public class HandlerAnnotationMethodHandlerResolver implements MethodHandlerResolver {
  public static final String NAME =
      "org.thepavel.icomponent.handler.resolver.internalHandlerAnnotationMethodHandlerResolver";

  private static final String BEAN_NAME = "beanName";
  private static final String BEAN_TYPE = "beanType";

  private final BeanLookup beanLookup;

  public HandlerAnnotationMethodHandlerResolver(BeanLookup beanLookup) {
    this.beanLookup = beanLookup;
  }

  @Override
  public MethodHandler getMethodHandler(MethodMetadata methodMetadata) {
    MethodHandler methodHandler = getFromAnnotation(methodMetadata);

    if (methodHandler == null) {
      methodHandler = getFromAnnotation(methodMetadata.getSourceClassMetadata());
    }

    return methodHandler;
  }

  private MethodHandler getFromAnnotation(AnnotatedTypeMetadata metadata) {
    AnnotationAttributes<?> attributes =
        AnnotationAttributes.of(Handler.class).declaredOn(metadata);

    String beanName = attributes.getString(BEAN_NAME).orElse(null);
    Class<?> beanType = attributes.getClass(BEAN_TYPE).orElse(null);

    return getMethodHandler(beanName, beanType);
  }

  private MethodHandler getMethodHandler(String beanName, Class<?> beanType) {
    return (MethodHandler) beanLookup.getBean(beanName, beanType);
  }
}
