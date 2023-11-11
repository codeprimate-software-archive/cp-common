/*
 * Timer.java (c) 15 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.12.15
 */

package com.cp.common.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public final class Timer {

  private static final Logger logger = Logger.getLogger(Timer.class);

  private static final Map TIMER_INSTANCE_MAP = new HashMap();

  public static final String DEFAULT_TIMER = "DefaultTimer";

  private long startTime;
  private long stopTime;

  private String name;

  public Timer() {
    this(DEFAULT_TIMER);
  }

  public Timer(final String name) {
    this.name = name;
    TIMER_INSTANCE_MAP.put(name, this);
  }

  public static Timer getInstance(final String name) {
    return (Timer) TIMER_INSTANCE_MAP.get(name);
  }

  /**
   * Returns the difference between startTime and stopTime in milliseconds.
   * @return a integer value specifying the number of milliseconds that elapsed between startTime and endTime.
   */
  public long diff() {
    return (getStopTime() - getStartTime());
  }

  /**
   * Returns the name of the Timer object.
   * @return a String value specifying the name of this Timer object.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value of startTime in milliseconds.
   * @return an integer value specifying the milliseconds of startTime.
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Returns the value of stopTime in milliseconds.
   * @return an integer value specifying the milliseconds of stopTime.
   */
  public long getStopTime() {
    return stopTime;
  }

  /**
   * Starts the timer by recording the currentTimeInMillis as startTime.
   */
  public void start() {
    startTime = System.currentTimeMillis();
  }

  /**
   * Stops the timer by recording the currentTimeInMillis as stopTime.
   */
  public void stop() {
    stopTime = System.currentTimeMillis();
  }

  /**
   * Returns a String representation of the Timer object.
   * @return a String containing the state of the Time object.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{name = ");
    buffer.append(getName());
    buffer.append(", startTime = ").append(getStartTime());
    buffer.append(", stopTime = ").append(getStopTime());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
