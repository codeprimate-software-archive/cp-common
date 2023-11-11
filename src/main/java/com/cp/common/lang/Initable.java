/*
 * Initable.java (c) 14 April 2010
 *
 * The Initable interface declares an init method in order to perform post instantiation intialization
 * and configuration of an object instance implementing this interface.
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.4.14
 * @see com.cp.common.lang.Initializable
 */

package com.cp.common.lang;

public interface Initable {

  public boolean isInited();

  /**
   * Init method used to initialize and configure an object implementing this interface after instantiation.
   */
  public void init();

}
