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

package org.thepavel.icomponent.integration;

import org.springframework.stereotype.Component;
import org.thepavel.icomponent.handler.MethodHandler;
import org.thepavel.icomponent.metadata.MethodMetadata;

@Component
class ReturningIntegerMethodHandler implements MethodHandler {
  @Override
  public Object handle(Object[] arguments, MethodMetadata methodMetadata) {
    Integer argument = (Integer) arguments[0];
    return argument + 10;
  }
}
