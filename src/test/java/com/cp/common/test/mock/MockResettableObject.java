/*
 * MockResettableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Resettable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Resettable;

public abstract class MockResettableObject implements Resettable {

  public void reset() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
