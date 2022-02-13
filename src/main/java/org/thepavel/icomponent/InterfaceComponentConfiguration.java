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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.handler.resolver.DefaultMethodHandlerResolver;
import org.thepavel.icomponent.handler.resolver.HandlerAnnotationMethodHandlerResolver;
import org.thepavel.icomponent.handler.resolver.MethodHandlerMapResolver;
import org.thepavel.icomponent.handler.resolver.MethodHandlerMapResolverBean;
import org.thepavel.icomponent.handler.resolver.MethodHandlerResolver;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactory;
import org.thepavel.icomponent.metadata.factory.ClassMetadataFactoryBean;
import org.thepavel.icomponent.metadata.validation.ClassMetadataValidator;
import org.thepavel.icomponent.proxy.InterfaceComponentInterceptorFactory;
import org.thepavel.icomponent.proxy.InterfaceComponentProxyFactory;
import org.thepavel.icomponent.util.BeanLookup;
import org.thepavel.icomponent.util.BeanLookupImpl;

import java.util.List;

@Configuration
public class InterfaceComponentConfiguration {
  @Bean(DefaultMethodHandlerResolver.NAME)
  @Order
  MethodHandlerResolver defaultMethodHandlerResolver(@DefaultMethodHandler ObjectProvider<MethodHandler> defaultMethodHandler) {
    return new DefaultMethodHandlerResolver(defaultMethodHandler.getIfAvailable());
  }

  @Bean(HandlerAnnotationMethodHandlerResolver.NAME)
  @Order(Ordered.HIGHEST_PRECEDENCE)
  MethodHandlerResolver handlerAnnotationMethodHandlerResolver(BeanLookup beanLookup) {
    return new HandlerAnnotationMethodHandlerResolver(beanLookup);
  }

  @Bean(MethodHandlerMapResolver.NAME)
  MethodHandlerMapResolver methodHandlerMapResolverBean(List<MethodHandlerResolver> methodHandlerResolvers) {
    return new MethodHandlerMapResolverBean(methodHandlerResolvers);
  }

  @Bean(ClassMetadataFactory.NAME)
  ClassMetadataFactory classMetadataFactoryBean() {
    return new ClassMetadataFactoryBean();
  }

  @Bean(InterfaceComponentInterceptorFactory.NAME)
  InterfaceComponentInterceptorFactory interfaceComponentInterceptorFactory(MethodHandlerMapResolver methodHandlerMapResolver) {
    return new InterfaceComponentInterceptorFactory(methodHandlerMapResolver);
  }

  @Bean(InterfaceComponentProxyFactory.NAME)
  InterfaceComponentProxyFactory interfaceComponentProxyFactory(ClassMetadataFactory classMetadataFactory, List<ClassMetadataValidator> classMetadataValidators, InterfaceComponentInterceptorFactory interceptorFactory) {
    return new InterfaceComponentProxyFactory(classMetadataFactory, classMetadataValidators, interceptorFactory);
  }

  @Bean(BeanLookup.NAME)
  BeanLookup beanLookupImpl(ApplicationContext context) {
    return new BeanLookupImpl(context);
  }
}
