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
import org.thepavel.icomponent.generic.GenericTypeParametersResolver;
import org.thepavel.icomponent.metadata.ResolvedTypeMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ResolvedTypeMetadataFactoryTest extends BaseFactoryTest {
  @Test
  public void testResolvedTypeMetadataFactory() {
    ResolvedTypeMetadataFactory factory =
        new DummyResolvedTypeMetadataFactory(getTypeParametersResolver());

    Method annotatedMethod = getTestMethod();
    Type genericType = annotatedMethod.getGenericReturnType();

    ResolvedTypeMetadata metadata = factory.getResolvedTypeMetadata(genericType, annotatedMethod);

    assertSame(String.class, metadata.getResolvedType());
    assertAnnotationPresent(TestMethodAnnotation.class, metadata);
  }

  private static class DummyResolvedTypeMetadataFactory extends ResolvedTypeMetadataFactory {
    private DummyResolvedTypeMetadataFactory(GenericTypeParametersResolver resolver) {
      super(resolver);
    }
  }
}
