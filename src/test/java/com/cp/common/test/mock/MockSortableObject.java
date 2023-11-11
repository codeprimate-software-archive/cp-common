/*
 * MockSortableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Sortable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Sortable;

public abstract class MockSortableObject implements Sortable {

  public Object get(final int index) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void set(final Object object, final int index) throws Exception {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public int size() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
