/*
 * Browser.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.1.16
 * @deprecated see the javax.swing.event.HyperlinkListener class.
*/

package com.cp.common.net;

public interface Browser {

  /**
   * Redirects the web browser to the specified URL.
   *
   * @param address is the Uniform Resource Locator of the
   * Web resource to display in the browser.
   */
  public void goToURL(java.net.URL address);

}
