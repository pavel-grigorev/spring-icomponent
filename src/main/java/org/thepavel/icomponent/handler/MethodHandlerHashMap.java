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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodHandlerHashMap implements MethodHandlerMap {
  private final Map<Method, MethodHandler> map = new HashMap<>();

  @Override
  public MethodHandler getMethodHandler(Method method) {
    return map.get(method);
  }

  public void put(Method method, MethodHandler methodHandler) {
    map.put(method, methodHandler);
  }

  public MethodHandlerHashMap putAll(MethodHandlerHashMap sourceMap) {
    map.putAll(sourceMap.map);
    return this;
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != MethodHandlerHashMap.class) {
      return false;
    }
    MethodHandlerHashMap thatMap = (MethodHandlerHashMap) obj;
    return map.equals(thatMap.map);
  }
}
