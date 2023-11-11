/*
 * MockDescribableObject.java (c) 23 August 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.8.23
 * @see com.cp.common.lang.Describable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Describable;

public class MockDescribableObject implements Describable {

  private String description;
  private String synopsis;

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(final String synopsis) {
    this.synopsis = synopsis;
  }

}
