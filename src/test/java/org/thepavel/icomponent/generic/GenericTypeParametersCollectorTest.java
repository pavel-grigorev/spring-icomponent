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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericTypeParametersCollectorTest {
  @Test
  public void collectsTypeParametersFromSuperclass() {
    givenGenericTypeParametersCollectorFor(TestClass.class);
    whenGetValueMethodCalledFor(TestSuperclass.class.getTypeParameters()[0]);
    thenResultIs(String.class);
  }

  @Test
  public void collectsTypeParametersFromSuperinterface() {
    givenGenericTypeParametersCollectorFor(TestClass.class);
    whenGetValueMethodCalledFor(TestSuperinterface.class.getTypeParameters()[0]);
    thenResultIs(Integer.class);
  }

  @Test
  public void collectsTypeParametersFromMultiLeveledHierarchy() {
    givenGenericTypeParametersCollectorFor(TestInterface.class);
    whenGetValueMethodCalledFor(TestSuperinterface.class.getTypeParameters()[0]);
    thenResultIs(TestIntermediateInterface.class.getTypeParameters()[0]);
  }

  private GenericTypeParametersCollector collector;
  private Type result;

  @BeforeEach
  public void beforeEach() {
    collector = null;
    result = null;
  }

  private void givenGenericTypeParametersCollectorFor(Class<?> clazz) {
    collector = new GenericTypeParametersCollector(clazz);
  }

  private void whenGetValueMethodCalledFor(TypeVariable<?> type) {
    result = collector.getValue(type);
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
}
