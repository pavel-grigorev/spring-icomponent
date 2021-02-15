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

package org.thepavel.icomponent.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.reflect.TypeUtils.getTypeArguments;

public class GenericTypeParametersCollector {
  private final Map<TypeVariable<?>, Type> typeParameters = new HashMap<>();

  public GenericTypeParametersCollector(Class<?> clazz) {
    collectFromSupertypes(clazz);
  }

  private void collectFromSupertypes(Class<?> clazz) {
    Optional
        .ofNullable(clazz.getGenericSuperclass())
        .ifPresent(this::collectFromGenericType);

    Arrays
        .stream(clazz.getGenericInterfaces())
        .forEach(this::collectFromGenericType);
  }

  private void collectFromGenericType(Type type) {
    Class<?> actualClass;

    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      collectFromParameterizedType(parameterizedType);
      actualClass = (Class<?>) parameterizedType.getRawType();
    } else {
      actualClass = (Class<?>) type;
    }

    collectFromSupertypes(actualClass);
  }

  private void collectFromParameterizedType(ParameterizedType type) {
    Optional
        .ofNullable(getTypeArguments(type))
        .ifPresent(typeParameters::putAll);
  }

  public Type getValue(TypeVariable<?> type) {
    return typeParameters.get(type);
  }
}
