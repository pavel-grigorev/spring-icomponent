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

package org.thepavel.icomponent.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.AnnotationMetadata;

import static org.thepavel.icomponent.util.AnnotationMetadataHelper.getSourceClass;

public class InterfaceComponentProxyFactoryBean implements FactoryBean<Object> {
  private final AnnotationMetadata metadata;
  private final Class<?> objectType;

  private InterfaceComponentProxyFactory proxyFactory;

  public InterfaceComponentProxyFactoryBean(AnnotationMetadata metadata) {
    this.metadata = metadata;
    objectType = getSourceClass(metadata);
  }

  @Autowired
  public void setProxyFactory(InterfaceComponentProxyFactory proxyFactory) {
    this.proxyFactory = proxyFactory;
  }

  @Override
  public Object getObject() {
    return proxyFactory.createProxy(metadata);
  }

  @Override
  public Class<?> getObjectType() {
    return objectType;
  }
}
