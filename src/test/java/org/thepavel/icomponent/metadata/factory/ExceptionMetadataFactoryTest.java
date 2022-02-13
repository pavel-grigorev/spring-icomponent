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

package org.thepavel.icomponent.metadata.factory;

import org.junit.jupiter.api.Test;
import org.thepavel.icomponent.metadata.ResolvedTypeMetadata;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ExceptionMetadataFactoryTest extends BaseFactoryTest {
  @Test
  public void testExceptionMetadataFactory() {
    ExceptionMetadataFactory factory = new ExceptionMetadataFactory(getTypeParametersResolver());

    List<ResolvedTypeMetadata> metadataList = factory.getExceptionMetadata(getTestMethod());

    checkExceptionMetadata(metadataList);
  }

  static void checkExceptionMetadata(List<ResolvedTypeMetadata> metadataList) {
    assertNotNull(metadataList);
    assertEquals(1, metadataList.size());

    ResolvedTypeMetadata metadata = metadataList.get(0);

    assertSame(NullPointerException.class, metadata.getResolvedType());
    assertAnnotationPresent(TestExceptionAnnotation.class, metadata);
  }
}
