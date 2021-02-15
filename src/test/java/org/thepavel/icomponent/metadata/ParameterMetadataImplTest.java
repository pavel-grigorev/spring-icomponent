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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ParameterMetadataImplTest {
  @Test
  public void providesResolvedType() {
    assertSame(expectedType, metadata.getResolvedType());
  }

  @Test
  public void providesAnnotations() {
    assertSame(expectedAnnotations, metadata.getAnnotations());
  }

  @Test
  public void providesSourceParameter() {
    assertSame(parameter, metadata.getSourceParameter());
  }

  @Test
  public void providesParameterOrder() {
    assertEquals(PARAMETER_ORDER, metadata.getOrder());
  }

  private static final int PARAMETER_ORDER = 0;

  private static Parameter parameter;
  private static Class<?> expectedType;
  private static MergedAnnotations expectedAnnotations;
  private static ParameterMetadata metadata;

  @BeforeAll
  public static void beforeAll() {
    parameter = getTestMethod().getParameters()[PARAMETER_ORDER];
    expectedType = parameter.getType();
    expectedAnnotations = MergedAnnotations.from(parameter);
    metadata = new ParameterMetadataImpl(
        new ResolvedTypeMetadataImpl(expectedType, expectedAnnotations),
        parameter,
        PARAMETER_ORDER
    );
  }

  @AfterAll
  public static void afterAll() {
    parameter = null;
    expectedType = null;
    expectedAnnotations = null;
    metadata = null;
  }

  private static Method getTestMethod() {
    Method method = ReflectionUtils.findMethod(ParameterMetadataImplTest.class, "testMethod", String.class);
    assertNotNull(method);
    return method;
  }

  @SuppressWarnings("unused")
  private Integer testMethod(String s) {
    return null;
  }
}
