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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ReturnTypeMetadataFactoryTest extends BaseFactoryTest {
  @Test
  public void testReturnTypeMetadataFactory() {
    ReturnTypeMetadataFactory factory = new ReturnTypeMetadataFactory(getTypeParametersResolver());

    ResolvedTypeMetadata metadata = factory.getReturnTypeMetadata(getTestMethod());

    checkReturnTypeMetadata(metadata);
  }

  static void checkReturnTypeMetadata(ResolvedTypeMetadata metadata) {
    assertNotNull(metadata);
    assertSame(String.class, metadata.getResolvedType());
    assertAnnotationPresent(TestReturnTypeAnnotation.class, metadata);
  }
}
