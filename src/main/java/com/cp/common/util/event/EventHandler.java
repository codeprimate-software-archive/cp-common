/*
 * EventHandler.java (c) 7 November 2004
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.9.5
 * @see com.cp.common.util.event.EventQueue
 * @see com.cp.common.util.event.EventSource
 * @see java.util.EventObject
 */

package com.cp.common.util.event;

import java.util.EventListener;
import java.util.EventObject;

public interface EventHandler extends EventListener {

  /**
   * Handles the specified event encapsulated by the EventObject
   * @param event the EventObject encapsulating details about the event.
   */
  public void handle(EventObject event);

}
