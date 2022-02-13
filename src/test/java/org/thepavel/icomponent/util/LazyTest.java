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

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LazyTest {
  @Test
  public void computesValueOnes() {
    InvocationCountingSupplier s = new InvocationCountingSupplier();
    assertEquals(0, s.invocations);

    Lazy<Object> lazy = Lazy.of(s);
    assertEquals(0, s.invocations);

    Object o1 = lazy.get();
    assertEquals(1, s.invocations);

    Object o2 = lazy.get();
    assertEquals(1, s.invocations);

    assertSame(o1, o2);
  }

  private static class InvocationCountingSupplier implements Supplier<Object> {
    private int invocations;

    @Override
    public Object get() {
      invocations++;
      return new Object();
    }
  }
}