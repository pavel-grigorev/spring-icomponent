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

package org.thepavel.icomponent.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MethodHelperTest {
  @Test
  public void testAbstractMethod() {
    givenClass(TestInterface.class);
    whenMethodTested("abstractMethod");
    thenMethodIsAbstract(true);
    andMethodIsStatic(false);
  }

  @Test
  public void testStaticMethod() {
    givenClass(TestInterface.class);
    whenMethodTested("staticMethod");
    thenMethodIsAbstract(false);
    andMethodIsStatic(true);
  }

  private Class<?> clazz;
  private Boolean isAbstract;
  private Boolean isStatic;

  @BeforeEach
  public void beforeEach() {
    clazz = null;
    isAbstract = null;
    isStatic = null;
  }

  @SuppressWarnings("SameParameterValue")
  private void givenClass(Class<?> clazz) {
    this.clazz = clazz;
  }

  private void whenMethodTested(String methodName) {
    Method method = ReflectionUtils.findMethod(clazz, methodName);
    assertNotNull(method);

    isAbstract = MethodHelper.isAbstract(method);
    isStatic = MethodHelper.isStatic(method);
  }

  private void thenMethodIsAbstract(Boolean expected) {
    assertEquals(expected, isAbstract);
  }

  private void andMethodIsStatic(Boolean expected) {
    assertEquals(expected, isStatic);
  }

  @SuppressWarnings("unused")
  private interface TestInterface {
    void abstractMethod();
    static void staticMethod() {}
  }
}
