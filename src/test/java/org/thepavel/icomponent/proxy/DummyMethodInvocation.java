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

package org.thepavel.icomponent.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

class DummyMethodInvocation implements ProxyMethodInvocation {
  private final Object proxy = new Object();
  private final Method method;
  private final Object[] arguments;

  public DummyMethodInvocation(Method method, Object[] arguments) {
    this.method = method;
    this.arguments = arguments;
  }

  @Override
  public Object getProxy() {
    return proxy;
  }

  @Override
  public Method getMethod() {
    return method;
  }

  @Override
  public Object[] getArguments() {
    return arguments;
  }

  @Override
  public Object proceed() {
    return null;
  }

  @Override
  public Object getThis() {
    return null;
  }

  @Override
  public AccessibleObject getStaticPart() {
    return null;
  }

  @Override
  public MethodInvocation invocableClone() {
    return null;
  }

  @Override
  public MethodInvocation invocableClone(Object... arguments) {
    return null;
  }

  @Override
  public void setArguments(Object... arguments) {
  }

  @Override
  public void setUserAttribute(String key, Object value) {
  }

  @Override
  public Object getUserAttribute(String key) {
    return null;
  }
}
