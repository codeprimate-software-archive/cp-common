/*
 * MockInitializableObject.java (c) 25 April 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Initializable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Initializable;

public abstract class MockInitializableObject<T> implements Initializable<T> {

  public void initialize(final T configuration) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
