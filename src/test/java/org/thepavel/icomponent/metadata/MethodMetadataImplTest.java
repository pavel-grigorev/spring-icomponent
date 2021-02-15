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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MethodMetadataImplTest {
  @Test
  public void providesSourceClassMetadata() {
    assertSame(classMetadata, methodMetadata.getSourceClassMetadata());
  }

  @Test
  public void providesSourceMethod() {
    assertSame(method, methodMetadata.getSourceMethod());
  }

  @Test
  public void hasSetterMethodForReturnTypeMetadata() {
    ResolvedTypeMetadata returnTypeMetadata = dummyResolvedTypeMetadata();
    methodMetadata.setReturnTypeMetadata(returnTypeMetadata);

    assertSame(returnTypeMetadata, methodMetadata.getReturnTypeMetadata());
  }

  @Test
  public void hasSetterMethodForAnnotations() {
    MergedAnnotations annotations = MergedAnnotations.from(method);
    methodMetadata.setAnnotations(annotations);

    assertSame(annotations, methodMetadata.getAnnotations());
  }

  @Test
  public void parametersMetadataListIsUnmodifiable() {
    List<ParameterMetadata> parametersMetadata = methodMetadata.getParametersMetadata();

    assertNotNull(parametersMetadata);
    assertThrows(
        UnsupportedOperationException.class,
        () -> parametersMetadata.add(dummyParameterMetadata())
    );
  }

  @Test
  public void hasAddMethodForParameterMetadata() {
    ParameterMetadata parameterMetadata = dummyParameterMetadata();
    methodMetadata.addParameterMetadata(parameterMetadata);

    List<ParameterMetadata> parametersMetadata = methodMetadata.getParametersMetadata();

    assertNotNull(parametersMetadata);
    assertEquals(1, parametersMetadata.size());
    assertSame(parameterMetadata, parametersMetadata.get(0));
  }

  @Test
  public void exceptionsMetadataListIsUnmodifiable() {
    List<ResolvedTypeMetadata> exceptionsMetadata = methodMetadata.getExceptionsMetadata();

    assertNotNull(exceptionsMetadata);
    assertThrows(
        UnsupportedOperationException.class,
        () -> exceptionsMetadata.add(dummyResolvedTypeMetadata())
    );
  }

  @Test
  public void hasAddMethodForExceptionMetadata() {
    ResolvedTypeMetadata exceptionMetadata = dummyResolvedTypeMetadata();
    methodMetadata.addExceptionMetadata(exceptionMetadata);

    List<ResolvedTypeMetadata> exceptionsMetadata = methodMetadata.getExceptionsMetadata();

    assertNotNull(exceptionsMetadata);
    assertEquals(1, exceptionsMetadata.size());
    assertSame(exceptionMetadata, exceptionsMetadata.get(0));
  }

  private static Class<?> clazz;
  private static ClassMetadata classMetadata;
  private static Method method;
  private static MethodMetadataImpl methodMetadata;

  @BeforeAll
  public static void beforeAll() {
    clazz = MethodMetadataImplTest.class;
    classMetadata = createClassMetadata();
    method = getTestMethod();
    methodMetadata = new MethodMetadataImpl(classMetadata, method);
  }

  @AfterAll
  public static void afterAll() {
    clazz = null;
    classMetadata = null;
    method = null;
    methodMetadata = null;
  }

  private static ClassMetadata createClassMetadata() {
    return new ClassMetadataImpl(clazz, MergedAnnotations.from(clazz));
  }

  private static Method getTestMethod() {
    Method method = ReflectionUtils.findMethod(clazz, "testMethod");
    assertNotNull(method);
    return method;
  }

  @SuppressWarnings("unused")
  private void testMethod() {
  }

  private static ResolvedTypeMetadata dummyResolvedTypeMetadata() {
    return new ResolvedTypeMetadataImpl(null, null);
  }

  private static ParameterMetadata dummyParameterMetadata() {
    return new ParameterMetadataImpl(dummyResolvedTypeMetadata(), null, 0);
  }
}
