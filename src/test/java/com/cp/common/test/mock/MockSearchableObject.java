/*
 * MockSearchableObject.java (c) 25 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.25
 * @see com.cp.common.lang.Searchable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Searchable;
import java.util.Iterator;

public abstract class MockSearchableObject implements Searchable {

  public Object get(final int index) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public Iterator iterator() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public int size() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
