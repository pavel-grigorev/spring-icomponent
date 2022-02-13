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

package org.thepavel.icomponent.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import static org.apache.commons.lang3.reflect.TypeUtils.genericArrayType;
import static org.apache.commons.lang3.reflect.TypeUtils.parameterize;

public class GenericTypeParametersResolver {
  private final GenericTypeParametersCollector parameters;

  public GenericTypeParametersResolver(Class<?> clazz) {
    parameters = new GenericTypeParametersCollector(clazz);
  }

  public Type resolveType(Type type) {
    if (type == null) {
      return Object.class;
    }
    if (type instanceof Class) {
      return type;
    }
    if (type instanceof TypeVariable) {
      return resolveTypeVariable((TypeVariable<?>) type);
    }
    if (type instanceof GenericArrayType) {
      return resolveArrayType((GenericArrayType) type);
    }
    if (type instanceof ParameterizedType) {
      return resolveParameterizedType((ParameterizedType) type);
    }
    return Object.class;
  }

  private Type resolveTypeVariable(TypeVariable<?> typeVariable) {
    return resolveType(parameters.getValue(typeVariable));
  }

  private Type resolveArrayType(GenericArrayType arrayType) {
    Type componentType = arrayType.getGenericComponentType();
    return genericArrayType(resolveType(componentType));
  }

  private Type resolveParameterizedType(ParameterizedType type) {
    return allTypeArgumentsResolved(type) ? type :
        parameterize((Class<?>) type.getRawType(), resolveTypeArguments(type));
  }

  private static boolean allTypeArgumentsResolved(ParameterizedType type) {
    return Arrays
        .stream(type.getActualTypeArguments())
        .allMatch(argument -> argument instanceof Class);
  }

  private Type[] resolveTypeArguments(ParameterizedType type) {
    Type[] arguments = type.getActualTypeArguments();
    Type[] resolved = new Type[arguments.length];

    for (int i = 0; i < arguments.length; i++) {
      resolved[i] = resolveType(arguments[i]);
    }

    return resolved;
  }
}
