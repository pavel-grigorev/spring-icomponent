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

package org.thepavel.icomponent.handler.resolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thepavel.icomponent.DefaultMethodHandler;
import org.thepavel.icomponent.InterfaceComponentConfiguration;
import org.thepavel.icomponent.handler.MethodHandler;

@Configuration
@Import(InterfaceComponentConfiguration.class)
class DefaultMethodHandlerConfiguration {
  @Bean
  @DefaultMethodHandler
  MethodHandler testMethodHandler() {
    return new DummyMethodHandler();
  }
}
