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

package org.thepavel.icomponent.metadata.validation;

import org.thepavel.icomponent.metadata.ClassMetadata;
import org.thepavel.icomponent.metadata.MethodMetadata;
import org.thepavel.icomponent.util.MethodHelper;

import java.lang.reflect.Method;
import java.util.Optional;

public class ClassMetadataValidatorBean implements ClassMetadataValidator {
  public static final String NAME =
      "org.thepavel.icomponent.metadata.validation.internalClassMetadataValidatorBean";

  @Override
  public void validate(ClassMetadata classMetadata) throws ClassMetadataValidationException {
    Optional<Method> defaultMethod = findDefaultMethod(classMetadata);
    if (defaultMethod.isPresent()) {
      throw unsupportedMethod(defaultMethod.get());
    }
  }

  private static Optional<Method> findDefaultMethod(ClassMetadata classMetadata) {
    return classMetadata
        .getMethodsMetadata()
        .stream()
        .map(MethodMetadata::getSourceMethod)
        .filter(MethodHelper::isDefault)
        .findFirst();
  }

  private static ClassMetadataValidationException unsupportedMethod(Method method) {
    return new ClassMetadataValidationException("Unsupported method " + method);
  }
}
