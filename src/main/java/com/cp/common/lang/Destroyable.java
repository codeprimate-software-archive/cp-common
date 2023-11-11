/*
 * Destroyable.java (c) 2 May 2007
 *
 * The Destroyable interface declares a destroy operation used by objects that need to perform resource clean-up
 * before being garbage collected, or before termination of the application in which they are part.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.7
 * @see com.cp.common.lang.Initializable
 */

package com.cp.common.lang;

public interface Destroyable {

  public boolean isDestroyed();

  /**
   * Releases any resources held by the object implementing this interface.
   */
  public void destroy();

}
