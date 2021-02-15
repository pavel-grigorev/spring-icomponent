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

package org.thepavel.icomponent.metadata.factory;

import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ReflectionUtils;
import org.thepavel.icomponent.generic.GenericTypeParametersResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class BaseFactoryTest {
  GenericTypeParametersResolver getTypeParametersResolver() {
    return new GenericTypeParametersResolver(TestInterface.class);
  }

  Method getTestMethod() {
    Method method = ReflectionUtils.findMethod(TestSuperinterface.class, "test", Object.class);
    assertNotNull(method);
    return method;
  }

  static <T extends Annotation> void assertAnnotationPresent(Class<T> annotationType, AnnotatedTypeMetadata metadata) {
    MergedAnnotations annotations = metadata.getAnnotations();

    assertNotNull(annotations);
    assertTrue(annotations.isPresent(annotationType));
  }
}
