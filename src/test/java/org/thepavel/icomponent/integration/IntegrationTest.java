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

package org.thepavel.icomponent.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class IntegrationTest {
  @Autowired
  private TestHandlerOnClass testHandlerOnClass;

  @Autowired
  private TestHandlerOnClassWithDefaultMethod testHandlerOnClassWithDefaultMethod;

  @Autowired
  private TestHandlerOnMethod testHandlerOnMethod;

  @Autowired
  private TestDefaultMethodHandler testDefaultMethodHandler;

  @Autowired
  private TestCustomResolver testCustomResolver;

  @Test
  public void testHandlerOnClass() {
    assertEquals(20, testHandlerOnClass.test(10));
  }

  @Test
  public void TestHandlerOnClassWithDefaultMethod() {
    assertEquals(30, testHandlerOnClassWithDefaultMethod.testDefault(10));
  }

  @Test
  public void testHandlerOnMethod() {
    assertEquals("Argument: dummy", testHandlerOnMethod.test("dummy"));
  }

  @Test
  public void testDefaultMethodHandler() {
    String argument = "Hello, world!";
    assertSame(argument, testDefaultMethodHandler.test(argument));
  }

  @Test
  public void testCustomResolver() {
    assertTrue(testCustomResolver.test(false));
  }
}
