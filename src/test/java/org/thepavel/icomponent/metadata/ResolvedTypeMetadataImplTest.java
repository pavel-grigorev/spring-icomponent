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

package org.thepavel.icomponent.metadata;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotations;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ResolvedTypeMetadataImplTest {
  @Test
  public void providesResolvedType() {
    assertSame(expectedType, metadata.getResolvedType());
  }

  @Test
  public void providesAnnotations() {
    assertSame(expectedAnnotations, metadata.getAnnotations());
  }

  private static Class<?> expectedType;
  private static MergedAnnotations expectedAnnotations;
  private static ResolvedTypeMetadata metadata;

  @BeforeAll
  public static void beforeAll() {
    expectedType = ResolvedTypeMetadataImplTest.class;
    expectedAnnotations = MergedAnnotations.from(expectedType);
    metadata = new ResolvedTypeMetadataImpl(expectedType, expectedAnnotations);
  }

  @AfterAll
  public static void afterAll() {
    expectedType = null;
    expectedAnnotations = null;
    metadata = null;
  }
}
