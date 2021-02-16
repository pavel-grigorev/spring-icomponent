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

package org.thepavel.icomponent.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SameParameterValue")
public class AnnotationMetadataHelperTest {
  @Test
  public void resolvesClassNameIntoClass() {
    givenAnnotationMetadataFor(AnnotationMetadataHelperTest.class);
    whenClassResolved();
    thenResolvedClassIs(AnnotationMetadataHelperTest.class);
  }

  private AnnotationMetadata metadata;
  private Class<?> resolvedClass;

  @BeforeEach
  public void beforeEach() {
    metadata = null;
    resolvedClass = null;
  }

  private void givenAnnotationMetadataFor(Class<?> clazz) {
    metadata = AnnotationMetadata.introspect(clazz);
  }

  private void whenClassResolved() {
    resolvedClass = AnnotationMetadataHelper.getSourceClass(metadata);
  }

  private void thenResolvedClassIs(Class<?> expected) {
    assertEquals(expected, resolvedClass);
  }
}
