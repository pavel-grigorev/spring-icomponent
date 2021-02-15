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

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Collector;

public interface MethodHandlerMap {
  MethodHandler getMethodHandler(Method method);

  static <T> Collector<T, MethodHandlerHashMap, MethodHandlerMap> collector(
      Function<T, Method> methodFunction,
      Function<T, MethodHandler> methodHandlerFunction
  ) {
    return Collector.of(
        MethodHandlerHashMap::new,
        (a, e) -> a.put(methodFunction.apply(e), methodHandlerFunction.apply(e)),
        MethodHandlerHashMap::putAll,
        a -> a,
        Collector.Characteristics.IDENTITY_FINISH
    );
  }
}
