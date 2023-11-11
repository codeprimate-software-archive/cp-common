/*
 * Describable.java (c) 29 July 2009
 *
 * The Describable interface declares methods that allow any objects of classes implementing this interface
 * to be self-describing.  Both a detailed description as well as a general summary/synopsis of the object
 * is provided to give adequate, comprehensive information about the object.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.4.7
 * @see java.lang.Object#toString
 */

package com.cp.common.lang;

public interface Describable {

  /**
   * Gets a String detailing the object who's class implements this interface.  The description is a much more detailed,
   * in-depth and elaborate body of information describing the object and perhaps it's current state.
   * @return a String value description of the implementing object.
   * @see Describable#getSynopsis()
   */
  public String getDescription();

  /**
   * Sets the String detailing the object who's class implements this interface.  The description is a much more detailed,
   * in-depth and elaborate body of information describing the object and perhaps it's current state.
   * @param description a String value description of the implementing object.
   */
  public void setDescription(String description);

  /**
   * Gets a String summarizing the details of the object who's class implements this interface.  The synopsis provides
   * an overview and quick summary highlighting key points about the object and perhaps it's state.
   * @return a String value summarizing the details of the implementing object.
   * @see Describable#getDescription()
   */
  public String getSynopsis();

  /**
   * Gets a String summarizing the details of the object who's class implements this interface.  The synopsis provides
   * an overview and quick summary highlighting key points about the object and perhaps it's state.
   * @param synopsis a String value summarizing the details of the implementing object.
   */
  public void setSynopsis(String synopsis);

}
