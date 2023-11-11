/*
 * URLListener.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.1.16
 * @deprecated see the javax.swing.event.HyperlinkListener class.
 */

package com.cp.common.net;

import com.cp.common.lang.ObjectUtil;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import org.apache.log4j.Logger;

public class URLListener extends MouseAdapter implements KeyListener {

  private static final Logger logger = Logger.getLogger(URLListener.class);

  private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
  private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

  private Browser browser;

  private Component component;

  private URL address;

  /**
   * Creates a new instance of the URLListener class to handle click events
   * on hyperlinks embeded in applications/applets.
   *
   * @param component the Component containing the URL on which the mouse icon
   * will change and for which this listener is registered.
   * @param browser the Browser object implementing the Browser interface and
   * is capable of redirecting a Web browser to the new Internet resource
   * specified by the URL.
   * @param address a URL the uniform resource locator referring to the Web
   * resource.
   */
  public URLListener(final Component component,
                     final Browser browser,
                     final URL address) {
    setComponent(component);
    setBrowser(browser);
    setURL(address);
  }

  /**
   * Returns the Browser object responsible for retrieving the Web resource
   * and opening a Web browser to view the resource.
   * @return the Browser object.
   */
  private Browser getBrowser() {
    return browser;
  }

  /**
   * Sets the Browser object responsible for retrieving the Web resource
   * at URL and displaying the resource in a Web browser.
   * @param browser the Browser object used to fetch the Web content and
   * display it in a Web browser.
   */
  private void setBrowser(final Browser browser) {
    if (ObjectUtil.isNull(browser)) {
      logger.warn("The browser cannot be null!");
      throw new NullPointerException("The browser cannot be null!");
    }
    this.browser = browser;
  }

  /**
   * Returns the component containing the hyperlink.
   * @return the Component containing the hyperlink.
   */
  private Component getComponent() {
    return component;
  }

  /**
   * Sets the component containing the hyperlink.
   * @param component the Component with the hyperlink.
   */
  private void setComponent(final Component component) {
    if (ObjectUtil.isNull(component)) {
      logger.warn("The component cannot be null!");
      throw new NullPointerException("The component cannot be null!");
    }
    this.component = component;
    component.addKeyListener(this);
    //component.addMouseListener(this);
  }

  /**
   * Returns the URL to the Web resource.
   * @return a URL object referring to the Web resource that will be retrieved
   * and displayed in the Browser.
   */
  private URL getURL() {
    return address;
  }

  /**
   * Set the URL to the Web resource to the specified address.
   * @param address the URL to the Web resource.
   */
  private void setURL(final URL address) {
    logger.debug("address: " + address);
    if (ObjectUtil.isNull(address)) {
      logger.warn("The URL cannot be null!");
      throw new NullPointerException("The URL cannot be null!");
    }
    this.address = address;
  }

  /**
   * Captures key pressed events when the user presses a key when the
   * hyperlink is selected.
   * @param e the KeyEvent capturing the key pressed event on a
   * hyperlink.
   */
  public void keyPressed(final KeyEvent e) {
    logger.debug("keyPressed");
    if (getComponent().isEnabled()) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        getBrowser().goToURL(getURL());
      }
    }
    else {
      logger.warn("The component is disabled, consuming event!");
    }
  }

  /**
   * Null Implementation - default behavior is to do nothing.
   * @param e
   */
  public void keyReleased(KeyEvent e) {
  }

  /**
   * Null Implementation - default behavior is to do nothing.
   * @param e
   */
  public void keyTyped(KeyEvent e) {
  }

  /**
   * Called when the user moves his/her mouse over the hyperlink in the UI.
   * The method then proceeds in setting the mouse cursor to a pointing hand.
   *
   * @param me the MouseEvent object representing the mouse over the
   * hyperlink.
   */
  public void mouseEntered(final MouseEvent me) {
    logger.debug("mouseEntered");
    getComponent().setCursor(HAND_CURSOR);
  }

  /**
   * Called when the user moves the mouse off of the hyperlink in the UI.
   * The method then sets the mouse back to the default arrow.
   *
   * @param me the MouseEvent of the user moving the mouse off of the
   * hyperlink.
   */
  public void mouseExited(final MouseEvent me) {
    logger.debug("mouseExited");
    getComponent().setCursor(DEFAULT_CURSOR);
  }

  /**
   * Triggers the hyperlink and causes the Browser object to retrieve the resource
   * (content) at the specified URL.
   *
   * @param me the MouseEvent when the user activates the hyperlink by clicking
   * on it with the mouse or hitting the enter key when the hyperlink is selected.
   */
  public void mousePressed(final MouseEvent me) {
    logger.debug("mousePressed");
    if (getComponent().isEnabled()) {
      getBrowser().goToURL(getURL());
    }
    else {
      logger.warn("The component is disabled, consuming event!");
    }
  }

}
