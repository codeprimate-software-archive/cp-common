/*
 * ObjectUtilTest.java (c) 6 August 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.9
 * @see com.cp.common.test.mock.MockValueObject
 */

package com.cp.common.test.mock;

import com.cp.common.lang.ObjectUtil;

public class MockValueObjectImpl<T> implements MockValueObject<T> {

  private T value;

  private boolean protectedMethodInvoked = false;
  private boolean voidMethodInvoked = false;

  public MockValueObjectImpl() {
  }

  public MockValueObjectImpl(final T value) {
    setValue(value);
  }

  protected MockValueObjectImpl(final MockBean bean) {
    setValue((T) bean.getValue());
  }

  public MockValueObjectImpl(final MockPrimitiveObject primitiveObject) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public MockValueObjectImpl(final MockValueObject<T> valueObject) {
    setValue(valueObject.getValue());
  }

  public static <T> MockValueObject<T> create() {
    return new MockValueObjectImpl<T>();
  }

  public static <T> MockValueObject<T> create(T value) {
    return new MockValueObjectImpl<T>(value);
  }

  public T getValue() {
    return value;
  }

  public void setValue(final T value) {
    this.value = value;
  }

  public void exceptionalMethod() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  protected void protectedMethod() {
    protectedMethodInvoked = true;
  }

  public boolean isProtectedMethodInvoked() {
    final boolean protectedMethodInvoked = this.protectedMethodInvoked;
    this.protectedMethodInvoked = false;
    return protectedMethodInvoked;
  }

  public void voidMethod() {
    voidMethodInvoked = true;
  }

  public boolean isVoidMethodInvoked() {
    final boolean voidMethodInvoked = this.voidMethodInvoked;
    this.voidMethodInvoked = false;
    return voidMethodInvoked;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof MockValueObject)) {
      return false;
    }

    final MockValueObject that = (MockValueObject) obj;

    return ObjectUtil.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{value = ");
    buffer.append(getValue());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
