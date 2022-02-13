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

package org.thepavel.icomponent.util;

import org.springframework.context.ApplicationContext;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BeanLookupImpl implements BeanLookup {
  private final ApplicationContext context;

  public BeanLookupImpl(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public Object getBean(String beanName, Class<?> beanType) {
    if (isNotBlank(beanName) && beanType != null) {
      return context.getBean(beanName, beanType);
    }
    if (isNotBlank(beanName)) {
      return context.getBean(beanName);
    }
    if (beanType != null) {
      return context.getBean(beanType);
    }
    return null;
  }
}
