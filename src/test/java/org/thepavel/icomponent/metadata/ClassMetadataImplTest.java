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
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClassMetadataImplTest {
  @Test
  public void providesSourceClass() {
    assertSame(sourceClass, classMetadata.getSourceClass());
  }

  @Test
  public void providesAnnotations() {
    assertSame(expectedAnnotations, classMetadata.getAnnotations());
  }

  @Test
  public void methodsMetadataCollectionIsUnmodifiable() {
    Collection<MethodMetadata> methodsMetadata = classMetadata.getMethodsMetadata();

    assertNotNull(methodsMetadata);
    assertThrows(
        UnsupportedOperationException.class,
        () -> methodsMetadata.add(dummyMethodMetadata())
    );
  }

  @Test
  public void providesMethodMetadataForMethod() {
    Method method = getTestMethod();

    classMetadata.addMethodMetadata(createMethodMetadata(method));

    MethodMetadata methodMetadata = classMetadata.getMethodMetadata(method);

    assertNotNull(methodMetadata);
    assertEquals(method, methodMetadata.getSourceMethod());
  }

  private static Class<?> sourceClass;
  private static MergedAnnotations expectedAnnotations;
  private static ClassMetadataImpl classMetadata;

  @BeforeAll
  public static void beforeAll() {
    sourceClass = ClassMetadataImplTest.class;
    expectedAnnotations = MergedAnnotations.from(sourceClass);
    classMetadata = new ClassMetadataImpl(sourceClass, expectedAnnotations);
  }

  @AfterAll
  public static void afterAll() {
    sourceClass = null;
    expectedAnnotations = null;
    classMetadata = null;
  }

  private static MethodMetadata dummyMethodMetadata() {
    return createMethodMetadata(null);
  }

  private static MethodMetadata createMethodMetadata(Method method) {
    return new MethodMetadataImpl(classMetadata, method);
  }

  private static Method getTestMethod() {
    Method method = ReflectionUtils.findMethod(ClassMetadataImplTest.class, "testMethod");
    assertNotNull(method);
    return method;
  }

  @SuppressWarnings("unused")
  private void testMethod() {
  }
}
