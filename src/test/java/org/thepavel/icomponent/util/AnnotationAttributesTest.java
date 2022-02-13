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

package org.thepavel.icomponent.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("SameParameterValue")
public class AnnotationAttributesTest {
  @Test
  public void ignoresEmptyStringValues() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithDefaultValues.class);
    whenGetStringMethodCalledForAttribute("stringAttribute");
    thenResultIsEmptyOptional();
  }

  @Test
  public void ignoresBlankStringValues() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithBlankStringAttribute.class);
    whenGetStringMethodCalledForAttribute("stringAttribute");
    thenResultIsEmptyOptional();
  }

  @Test
  public void returnsNonBlankStringValues() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithNonBlankStringAttribute.class);
    whenGetStringMethodCalledForAttribute("stringAttribute");
    thenResultIsOptionalContaining("dummy");
  }

  @Test
  public void ignoresEmptyStringArrays() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithDefaultValues.class);
    whenGetStringsMethodCalledForAttribute("stringArrayAttribute");
    thenResultIsEmptyOptional();
  }

  @Test
  public void ignoresStringArraysWithAllStringsBlank() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithAllStringsBlankInArrayAttribute.class);
    whenGetStringsMethodCalledForAttribute("stringArrayAttribute");
    thenResultIsEmptyOptional();
  }

  @Test
  public void removesBlankStringsFromStringArrays() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithSomeStringsBlankInArrayAttribute.class);
    whenGetStringsMethodCalledForAttribute("stringArrayAttribute");
    thenResultIsOptionalContainingList("dummy");
  }

  @Test
  public void returnsClassValue() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithClassAttribute.class);
    whenGetClassMethodCalledForAttribute("classAttribute");
    thenResultIsOptionalContaining(Double.class);
  }

  @Test
  public void ignoresEmptyClassArrays() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithDefaultValues.class);
    whenGetClassesMethodCalledForAttribute("classArrayAttribute");
    thenResultIsEmptyOptional();
  }

  @Test
  public void returnsClassArray() {
    givenAnnotation(TestAnnotation.class);
    declaredOn(TestAnnotationWithClassArrayAttribute.class);
    whenGetClassesMethodCalledForAttribute("classArrayAttribute");
    thenResultIsOptionalContainingList(String.class, Integer.class);
  }

  @Test
  public void returnsValueAttributeOfTypeString() {
    givenAnnotation(TestStringValueAnnotation.class);
    declaredOn(TestAnnotationWithValueAttributeOfTypeString.class);
    whenGetValueAsStringMethodCalled();
    thenResultIsOptionalContaining("hello");
  }

  @Test
  public void returnsValueAttributeOfTypeStringArray() {
    givenAnnotation(TestStringArrayValueAnnotation.class);
    declaredOn(TestAnnotationWithValueAttributeOfTypeStringArray.class);
    whenGetValueAsStringsMethodCalled();
    thenResultIsOptionalContainingList("hello", "world");
  }

  @Test
  public void returnsValueAttributeOfTypeClass() {
    givenAnnotation(TestClassValueAnnotation.class);
    declaredOn(TestAnnotationWithValueAttributeOfTypeClass.class);
    whenGetValueAsClassMethodCalled();
    thenResultIsOptionalContaining(Integer.class);
  }

  @Test
  public void returnsValueAttributeOfTypeClassArray() {
    givenAnnotation(TestClassArrayValueAnnotation.class);
    declaredOn(TestAnnotationWithValueAttributeOfTypeClassArray.class);
    whenGetValueAsClassesMethodCalled();
    thenResultIsOptionalContainingList(Boolean.class, Long.class);
  }

  @Test
  public void throwsExceptionWhenDeclaredOnNotCalled() {
    givenAnnotation(TestClassArrayValueAnnotation.class);
    assertThrows(IllegalStateException.class, this::whenGetValueAsClassesMethodCalled);
  }

  private AnnotationAttributes<?> annotationAttributes;
  private String stringAttributeValue;
  private List<String> stringArrayAttributeValue;
  private Class<?> classAttributeValue;
  private List<Class<?>> classArrayAttributeValue;

  @BeforeEach
  public void beforeEach() {
    annotationAttributes = null;
    stringAttributeValue = null;
    stringArrayAttributeValue = null;
    classAttributeValue = null;
    classArrayAttributeValue = null;
  }

  private <T extends Annotation> void givenAnnotation(Class<T> annotationType) {
    annotationAttributes = AnnotationAttributes.of(annotationType);
  }

  private void declaredOn(Class<?> clazz) {
    annotationAttributes.declaredOn(AnnotationMetadata.introspect(clazz));
  }

  private void whenGetStringMethodCalledForAttribute(String name) {
    stringAttributeValue = annotationAttributes.getString(name).orElse(null);
  }

  private void whenGetStringsMethodCalledForAttribute(String name) {
    stringArrayAttributeValue = annotationAttributes.getStrings(name).orElse(null);
  }

  private void whenGetClassMethodCalledForAttribute(String name) {
    classAttributeValue = annotationAttributes.getClass(name).orElse(null);
  }

  private void whenGetClassesMethodCalledForAttribute(String name) {
    classArrayAttributeValue = annotationAttributes.getClasses(name).orElse(null);
  }

  private void whenGetValueAsStringMethodCalled() {
    stringAttributeValue = annotationAttributes.getValueAsString().orElse(null);
  }

  private void whenGetValueAsStringsMethodCalled() {
    stringArrayAttributeValue = annotationAttributes.getValueAsStrings().orElse(null);
  }

  private void whenGetValueAsClassMethodCalled() {
    classAttributeValue = annotationAttributes.getValueAsClass().orElse(null);
  }

  private void whenGetValueAsClassesMethodCalled() {
    classArrayAttributeValue = annotationAttributes.getValueAsClasses().orElse(null);
  }

  private void thenResultIsEmptyOptional() {
    assertNull(stringAttributeValue);
    assertNull(stringArrayAttributeValue);
    assertNull(classAttributeValue);
    assertNull(classArrayAttributeValue);
  }

  private void thenResultIsOptionalContaining(String value) {
    assertEquals(value, stringAttributeValue);
  }

  private void thenResultIsOptionalContainingList(String... values) {
    assertEquals(Arrays.asList(values), stringArrayAttributeValue);
  }

  private void thenResultIsOptionalContaining(Class<?> value) {
    assertEquals(value, classAttributeValue);
  }

  private void thenResultIsOptionalContainingList(Class<?>... values) {
    assertEquals(Arrays.asList(values), classArrayAttributeValue);
  }

  @TestAnnotation
  private interface TestAnnotationWithDefaultValues {
  }

  @TestAnnotation(stringAttribute = " \t\n\r")
  private interface TestAnnotationWithBlankStringAttribute {
  }

  @TestAnnotation(stringAttribute = "dummy")
  private interface TestAnnotationWithNonBlankStringAttribute {
  }

  @TestAnnotation(stringArrayAttribute = {"", " \t\n\r", "\t"})
  private interface TestAnnotationWithAllStringsBlankInArrayAttribute {
  }

  @TestAnnotation(stringArrayAttribute = {"", " \t\n\r", "dummy"})
  private interface TestAnnotationWithSomeStringsBlankInArrayAttribute {
  }

  @TestAnnotation(classAttribute = Double.class)
  private interface TestAnnotationWithClassAttribute {
  }

  @TestAnnotation(classArrayAttribute = {String.class, Integer.class})
  private interface TestAnnotationWithClassArrayAttribute {
  }

  @TestStringValueAnnotation("hello")
  private interface TestAnnotationWithValueAttributeOfTypeString {
  }

  @TestStringArrayValueAnnotation({"hello", "world"})
  private interface TestAnnotationWithValueAttributeOfTypeStringArray {
  }

  @TestClassValueAnnotation(Integer.class)
  private interface TestAnnotationWithValueAttributeOfTypeClass {
  }

  @TestClassArrayValueAnnotation({Boolean.class, Long.class})
  private interface TestAnnotationWithValueAttributeOfTypeClassArray {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface TestAnnotation {
    String stringAttribute() default "";
    String[] stringArrayAttribute() default {};
    Class<?> classAttribute() default Object.class;
    Class<?>[] classArrayAttribute() default {};
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface TestStringValueAnnotation {
    String value() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface TestStringArrayValueAnnotation {
    String[] value() default {};
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface TestClassValueAnnotation {
    Class<?> value() default Object.class;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface TestClassArrayValueAnnotation {
    Class<?>[] value() default {};
  }
}
