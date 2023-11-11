/*
 * Initializable.java (c) 2 May 2007
 *
 * The Initializable interface exposes a method on implementing classes who's instances are configurable
 * through some external means such a properties file, or XML file and so on, even from a database.  The idea is
 * once the object has been instantiated, the initalize method is called to finish configuring the object instance.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.13
 * @see com.cp.common.lang.Destroyable
 */

package com.cp.common.lang;

public interface Initializable<T> {

  public boolean isInitialized();

  /**
   * Initializes the object implementing this interface with the specified configuration.
   * @param configuration a configuration object of type T used to initialize any object implementing this interface.
   */
  public void initialize(T configuration);

}
