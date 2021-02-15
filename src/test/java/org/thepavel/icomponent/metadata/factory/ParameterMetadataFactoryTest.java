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

package org.thepavel.icomponent.metadata.factory;

import org.junit.jupiter.api.Test;
import org.thepavel.icomponent.metadata.ParameterMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ParameterMetadataFactoryTest extends BaseFactoryTest {
  @Test
  public void testParameterMetadataFactory() {
    ParameterMetadataFactory factory = new ParameterMetadataFactory(getTypeParametersResolver());
    Method method = getTestMethod();

    List<ParameterMetadata> metadataList = factory.getParameterMetadata(method);

    checkParameterMetadata(metadataList, method);
  }

  static void checkParameterMetadata(List<ParameterMetadata> metadataList, Method method) {
    assertNotNull(metadataList);
    assertEquals(1, metadataList.size());

    ParameterMetadata metadata = metadataList.get(0);
    Parameter sourceParameter = method.getParameters()[0];

    assertSame(String.class, metadata.getResolvedType());
    assertAnnotationPresent(TestParameterAnnotation.class, metadata);
    assertEquals(sourceParameter, metadata.getSourceParameter());
    assertEquals(0, metadata.getOrder());
  }
}
