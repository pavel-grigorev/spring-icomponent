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

package org.thepavel.icomponent.generic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.apache.commons.lang3.reflect.TypeUtils.genericArrayType;
import static org.apache.commons.lang3.reflect.TypeUtils.parameterize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericTypeParametersResolverTest {
  @Test
  public void resolvesTypeParametersFromSuperclass() {
    givenGenericTypeParametersResolverFor(TestClass.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperclass);
    thenResultIs(String.class);
  }

  @Test
  public void resolvesTypeParametersFromSuperinterface() {
    givenGenericTypeParametersResolverFor(TestClass.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperinterface);
    thenResultIs(Integer.class);
  }

  @Test
  public void resolvesTypeParametersFromMultiLeveledHierarchy() {
    givenGenericTypeParametersResolverFor(TestInterface.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperinterface);
    thenResultIs(Boolean.class);
  }

  @Test
  public void resolvesParameterizedTypes() {
    givenGenericTypeParametersResolverFor(TestList.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperinterface);
    thenResultIs(parameterize(List.class, String.class));
  }

  @Test
  public void resolvesGenericArrayTypes() {
    givenGenericTypeParametersResolverFor(TestArray.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperinterface);
    thenResultIs(genericArrayType(Integer.class));
  }

  @Test
  public void resolvesUnspecifiedGenericTypeToObjectClass() {
    givenGenericTypeParametersResolverFor(TestUnspecifiedType.class);
    whenResolveTypeMethodCalledFor(typeParameterOnSuperinterface);
    thenResultIs(Object.class);
  }

  @Test
  public void resolvesNullToObjectClass() {
    givenGenericTypeParametersResolverFor(TestInterface.class);
    whenResolveTypeMethodCalledFor(null);
    thenResultIs(Object.class);
  }

  @Test
  public void resolvesClassToItself() {
    givenGenericTypeParametersResolverFor(TestInterface.class);
    whenResolveTypeMethodCalledFor(String.class);
    thenResultIs(String.class);
  }

  private static Type typeParameterOnSuperclass;
  private static Type typeParameterOnSuperinterface;

  @BeforeAll
  public static void beforeAll() {
    typeParameterOnSuperclass = TestSuperclass.class.getTypeParameters()[0];
    typeParameterOnSuperinterface = TestSuperinterface.class.getTypeParameters()[0];
  }

  @AfterAll
  public static void afterAll() {
    typeParameterOnSuperclass = null;
    typeParameterOnSuperinterface = null;
  }

  private GenericTypeParametersResolver resolver;
  private Type result;

  @BeforeEach
  public void beforeEach() {
    resolver = null;
    result = null;
  }

  private void givenGenericTypeParametersResolverFor(Class<?> clazz) {
    resolver = new GenericTypeParametersResolver(clazz);
  }

  private void whenResolveTypeMethodCalledFor(Type type) {
    result = resolver.resolveType(type);
  }

  private void thenResultIs(Type expected) {
    assertEquals(expected, result);
  }

  private static class TestClass extends TestSuperclass<String> implements TestSuperinterface<Integer> {
  }

  @SuppressWarnings("unused")
  private static class TestSuperclass<T> {
  }

  @SuppressWarnings("unused")
  private interface TestSuperinterface<T> {
  }

  private interface TestInterface extends TestIntermediateInterface<Boolean> {
  }

  private interface TestIntermediateInterface<T> extends TestSuperinterface<T> {
  }

  private interface TestList extends IntermediateList<String> {
  }

  private interface IntermediateList<T> extends TestSuperinterface<List<T>> {
  }

  private interface TestArray extends IntermediateArray<Integer> {
  }

  private interface IntermediateArray<T> extends TestSuperinterface<T[]> {
  }

  @SuppressWarnings("rawtypes")
  private interface TestUnspecifiedType extends TestSuperinterface {
  }
}
