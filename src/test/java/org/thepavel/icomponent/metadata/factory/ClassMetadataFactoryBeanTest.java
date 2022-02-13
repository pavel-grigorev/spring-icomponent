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

package org.thepavel.icomponent.metadata.factory;

import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.thepavel.icomponent.metadata.ClassMetadata;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ClassMetadataFactoryBeanTest extends BaseFactoryTest {
  @Test
  public void testClassMetadataFactory() {
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(TestInterface.class);
    ClassMetadata metadata = new ClassMetadataFactoryBean().getClassMetadata(annotationMetadata);

    assertSame(TestInterface.class, metadata.getSourceClass());
    assertAnnotationPresent(TestClassAnnotation.class, metadata);

    MethodMetadataFactoryTest.checkMethodMetadata(metadata.getMethodsMetadata(), metadata);
  }
}
