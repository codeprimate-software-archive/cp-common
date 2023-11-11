/*
 * MockCopyableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Copyable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Copyable;

public abstract class MockCopyableObject implements Copyable {

  public Object copy() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
