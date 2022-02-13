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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Lazily initialized value.
 */
public class Lazy<T> implements Supplier<T> {
  private final Supplier<? extends T> supplier;
  private final AtomicBoolean resolved = new AtomicBoolean();
  private volatile T value;

  private Lazy(Supplier<? extends T> supplier) {
    this.supplier = supplier;
  }

  public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
    return new Lazy<>(supplier);
  }

  public T get() {
    if (resolved.compareAndSet(false, true)) {
      value = supplier.get();
    }
    return value;
  }
}
