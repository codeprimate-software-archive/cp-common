/*
 * EventQueue.java (c) 7 November 2004
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.9.5
 * @see com.cp.common.util.event.EventHandler
 * @see com.cp.common.util.event.EventSource
 * @see java.util.LinkedList
 * @see java.util.Queue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.PriorityBlockingQueue
 */

package com.cp.common.util.event;

import com.cp.common.lang.Assert;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Deprecated a java.util.concurrent implementation of Queue should be used in preference to this Queue implementation.
 */
public final class EventQueue {

  protected static final int DEFAULT_MAX_SIZE = 1024;
  protected static final int DEFAULT_THRESHOLD = 512;

  private int maxSize = DEFAULT_MAX_SIZE;
  private int threshold = DEFAULT_THRESHOLD;

  protected final Log logger = LogFactory.getLog(getClass());

  private final Queue<Object> eventQueue = new LinkedList<Object>();

  /**
   * Creates an instance of the EventQueue class with a default maximum size and threshold.
   */
  public EventQueue() {
    this(DEFAULT_MAX_SIZE, DEFAULT_THRESHOLD);
  }

  /**
   * Creates an instance of the EventQueue class with the specified maximum size and default threshold.
   * @param maxSize an integer value defining an upper limit for the number of event objects that can be stored
   * in this EventQueue.
   */
  public EventQueue(final int maxSize) {
    this(maxSize, DEFAULT_THRESHOLD);
  }

  /**
   * Creates an instance of the EventQueue class initialized with the specified maximum size and threshold.
   * @param maxSize an integer value defining an upper limit for the number of event objects that can be stored
   * in this EventQueue.
   * @param threshold an integer value defining an lower bound after this EventQueue has been filled when event objects
   * are allowed to be placed on the EventQueue again.
   */
  public EventQueue(final int maxSize, final int threshold) {
    setMaxSize(maxSize);
    this.threshold = threshold;
  }

  /**
   * Determines whether the EventQueue is currently empty.
   * @return a boolean value indicating if this EventQueue is empty or not.
   */
  public synchronized boolean isEmpty() {
    return eventQueue.isEmpty();
  }

  /**
   * Returns the upper-bound specifying the number of Objects that can be put on the EventQueue.
   * @return an integer value specifying the maximum size of the EventQueue.
   */
  public final int getMaxSize() {
    return maxSize;
  }

  /**
   * Sets the upper-bound determining the number of Objects that can be placed on the EventQueue.
   * @param maxSize the upper-bound to which the EventQueue may grow.
   * @throws IllegalArgumentException if the maxSize parameter is less than or equal to the threshold.
   */
  public final void setMaxSize(final int maxSize) {
    Assert.greaterThan(maxSize, getThreshold(), "The maximum size of the event queue (" + maxSize
      + ") must be greater than the threshold (" + getThreshold() + ")!");
    this.maxSize = maxSize;
  }

  /**
   * Read-only property returning the number of Objects currently on the EventQueue.
   * @return an integer value specifying the number of Objects on this EventQueue.
   */
  public synchronized int getSize() {
    return eventQueue.size();
  }

  /**
   * Returns the number of Objects once the EventQueue has reached maximum size that must only be present in order
   * to push more Objects on the EventQueue.
   * @return an integer value specifying the threshold of this EventQueue.
   */
  public final int getThreshold() {
    return threshold;
  }

  /**
   * Sets the value of the threshold determining when Objects may be added to the EventQueue again.
   * @param threshold an integer value specifying the threshold for this EventQueue.
   * @throws IllegalArgumentException if the threshold value is greater than or equal to maxSize.
   */
  public final void setThreshold(final int threshold) {
    Assert.lessThan(threshold, getMaxSize(), "The threshold (" + threshold
      + ") must be less than the maximum size of the event queue (" + getMaxSize() + ")!");
    this.threshold = Math.max(0, threshold);
  }

  /**
   * Removes an Object from the EventQueue for processing.  This method will block if there are not Objects on
   * the EventQueue to process.  Note, this method will also notify Threads waiting to push Objects on this
   * EventQueue if the EventQueue has been filled.
   * @return an Object on this EventQueue and removes the Object from the queue.
   * @throws InterruptedException if the Thread waiting on the queue is interrupted.
   */
  public synchronized Object pop() throws InterruptedException {
    while (isEmpty()) {
      wait();
    }

    final Object poppedObject = eventQueue.poll();

    if (getSize() < getThreshold()) {
      if (logger.isDebugEnabled()) {
        logger.debug("The EventQueue size (" + getSize() + ") has dropped below the threshold (" + getThreshold() + ").");
      }

      notifyAll();
    }

    return poppedObject;
  }

  /**
   * Adds an Object to the EventQueue for future processing.  Note, this method will block if the maximum size
   * of the EventQueue has been reached.
   * @param obj the Object to push onto the EventQueue for future processing.
   * @throws InterruptedException if the Threading waiting to push an Object on this EventQueue is interrupted.
   */
  public synchronized void push(final Object obj) throws InterruptedException {
    while (getSize() >= getMaxSize()) {
      if (logger.isDebugEnabled()) {
        logger.debug("The EventQueue is full (" + getSize() + "); the maximum size is (" + getMaxSize() + ").");
      }

      wait();
    }

    eventQueue.offer(obj);
    notifyAll();
  }

}
