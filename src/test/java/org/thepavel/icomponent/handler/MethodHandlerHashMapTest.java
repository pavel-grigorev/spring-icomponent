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

package org.thepavel.icomponent.handler;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.metadata.MethodMetadata;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MethodHandlerHashMapTest {
  @Test
  public void putMethodTest() {
    Method method = getMethod("putMethodTest");
    MethodHandler methodHandler = MethodHandlerHashMapTest::dummyMethodHandler;

    MethodHandlerHashMap map = singleEntryMap(method, methodHandler);

    assertSame(methodHandler, map.getMethodHandler(method));
  }

  @Test
  public void putAllMethodTest() {
    Method method1 = getMethod("putMethodTest");
    Method method2 = getMethod("putAllMethodTest");
    MethodHandler methodHandler = MethodHandlerHashMapTest::dummyMethodHandler;

    MethodHandlerHashMap map1 = singleEntryMap(method1, methodHandler);
    MethodHandlerHashMap map2 = singleEntryMap(method2, methodHandler);

    map1.putAll(map2);

    assertSame(methodHandler, map1.getMethodHandler(method1));
    assertSame(methodHandler, map1.getMethodHandler(method2));
  }

  @Test
  public void putAllMethodReturnsThis() {
    MethodHandlerHashMap map = new MethodHandlerHashMap();
    assertSame(map, map.putAll(new MethodHandlerHashMap()));
  }

  @Test
  public void hashCodeAndEqualsTest() {
    Method method = getMethod("hashCodeAndEqualsTest");
    MethodHandler methodHandler = MethodHandlerHashMapTest::dummyMethodHandler;

    MethodHandlerHashMap map1 = singleEntryMap(method, methodHandler);
    MethodHandlerHashMap map2 = singleEntryMap(method, methodHandler);

    assertNotSame(map1, map2);
    assertEquals(map1.hashCode(), map2.hashCode());
    assertEquals(map1, map2);
  }

  private static Method getMethod(String name) {
    Method method = ReflectionUtils.findMethod(MethodHandlerHashMapTest.class, name);
    assertNotNull(method);
    return method;
  }

  private static Object dummyMethodHandler(Object[] arguments, MethodMetadata methodMetadata) {
    return null;
  }

  private static MethodHandlerHashMap singleEntryMap(Method method, MethodHandler methodHandler) {
    MethodHandlerHashMap map = new MethodHandlerHashMap();
    map.put(method, methodHandler);
    return map;
  }
}
