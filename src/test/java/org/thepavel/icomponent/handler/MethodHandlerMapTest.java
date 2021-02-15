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

package org.thepavel.icomponent.handler;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.metadata.MethodMetadata;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MethodHandlerMapTest {
  @Test
  public void collectorTest() {
    MethodHandler methodHandler = MethodHandlerMapTest::dummyMethodHandler;

    MethodHandlerMap map = Arrays
        .stream(TestInterface.class.getMethods())
        .collect(MethodHandlerMap.collector(m -> m, m -> methodHandler));

    assertSame(methodHandler, map.getMethodHandler(getMethod("method1")));
    assertSame(methodHandler, map.getMethodHandler(getMethod("method2")));
  }

  private static Object dummyMethodHandler(Object[] arguments, MethodMetadata methodMetadata) {
    return null;
  }

  private static Method getMethod(String name) {
    Method method = ReflectionUtils.findMethod(TestInterface.class, name);
    assertNotNull(method);
    return method;
  }

  @SuppressWarnings("unused")
  private interface TestInterface {
    void method1();
    void method2();
  }
}
