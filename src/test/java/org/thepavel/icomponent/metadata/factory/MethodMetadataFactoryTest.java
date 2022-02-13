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
import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.ClassMetadataImpl;
import org.thepavel.icomponent.metadata.MethodMetadata;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MethodMetadataFactoryTest extends BaseFactoryTest {
  @Test
  public void testMethodMetadataFactory() {
    MethodMetadataFactory factory = new MethodMetadataFactory(getTypeParametersResolver());
    ClassMetadata classMetadata = new ClassMetadataImpl(TestInterface.class, null);

    List<MethodMetadata> metadataList = factory.getMethodMetadata(classMetadata);

    checkMethodMetadata(metadataList, classMetadata);
  }

  static void checkMethodMetadata(Collection<MethodMetadata> metadataList, ClassMetadata classMetadata) {
    assertNotNull(metadataList);
    assertEquals(1, metadataList.size());

    MethodMetadata methodMetadata = metadataList.iterator().next();
    Method sourceMethod = classMetadata.getSourceClass().getMethods()[0];

    assertSame(classMetadata, methodMetadata.getSourceClassMetadata());
    assertEquals(sourceMethod, methodMetadata.getSourceMethod());
    assertAnnotationPresent(TestMethodAnnotation.class, methodMetadata);

    ReturnTypeMetadataFactoryTest.checkReturnTypeMetadata(methodMetadata.getReturnTypeMetadata());
    ParameterMetadataFactoryTest.checkParameterMetadata(methodMetadata.getParametersMetadata(), sourceMethod);
    ExceptionMetadataFactoryTest.checkExceptionMetadata(methodMetadata.getExceptionsMetadata());
  }
}
