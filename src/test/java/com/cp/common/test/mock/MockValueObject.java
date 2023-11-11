/*
 * MockValueObject.java (c) 21 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.16
 * @see com.cp.common.test.mock.MockPrimitiveObject
 */

package com.cp.common.test.mock;

public interface MockValueObject<T> {

  public boolean isProtectedMethodInvoked();

  public T getValue();

  public void setValue(T value);

  public boolean isVoidMethodInvoked();

}
