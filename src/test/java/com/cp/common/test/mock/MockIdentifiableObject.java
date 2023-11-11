/*
 * MockIdentifiableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Identifiable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Identifiable;

public abstract class MockIdentifiableObject<T extends Comparable<T>> implements Identifiable<T> {

  public T getId() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setId(final T id) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
