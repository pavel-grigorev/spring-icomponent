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

package org.thepavel.icomponent.customannotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CustomAnnotationConfiguration.class)
public class CustomAnnotationIntegrationTest {
  @Autowired
  private BeanFactory beanFactory;

  @Test
  public void testCustomAnnotation() {
    assertTrue(beanFactory.containsBean("testDefaultBeanName"));
    assertTrue(beanFactory.containsBean("magicBean"));
    assertTrue(beanFactory.containsBean("testDummyComponent"));
    assertFalse(beanFactory.containsBean("testCustomBeanName"));
    assertFalse(beanFactory.containsBean("testComponent"));
  }
}
