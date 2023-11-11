/*
 * MultithreadedQuickSort.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.codeprimate.util.sort;

import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Sortable;
import com.cp.common.util.sort.AbstractSorter;
import com.cp.common.util.sort.HeapSort;
import com.cp.common.util.sort.InsertionSort;
import com.cp.common.util.SortAscendingComparator;
import com.cp.common.util.sort.SortException;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.log4j.Logger;

public final class MultithreadedQuickSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(MultithreadedQuickSort.class);

  private static final int MIN_THRESHOLD = 25;
  private static final int NUMBER_OF_THREADS = 4;

  private boolean interrupted = false;

  private int activeThreadCount = 0;
  private int numThreads = NUMBER_OF_THREADS;
  private int threshold = MIN_THRESHOLD;

  private final EventDispatcher eventDispatcher;

  private final EventStack eventStack = new EventStack();

  private final com.cp.common.util.sort.HeapSort heapSort;
  private final InsertionSort insertionSort;

  private Sortable collection = null;

  private final ThreadGroup sortThreadGroup = new ThreadGroup("Quick Sort Thread Group");

  /**
   * Creates an instance of the Multithreaded Quick Sort algorithm class.  This class implements the quick sort
   * algorithm using Threads.
   */
  public MultithreadedQuickSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the Multithreaded Quick Sort algorithm class intialized with the sort order specified by
   * the Comparator.  This class implements the quick sort algorithm using Threads.
   * @param orderBy is a Comparator determining the order of the sort, either ascending or descending.
   */
  public MultithreadedQuickSort(final Comparator orderBy) {
    super(orderBy);

    // set the default sorting algorithm to heap sort/insertion sort
    heapSort = new com.cp.common.util.sort.HeapSort(orderBy);
    insertionSort = new InsertionSort(orderBy);

    // instantiate the EventDispatcher; delegating the responsibility of calling the appropriate EventHandler
    // based on the event type
    eventDispatcher = new EventDispatcher();

    // register this class as an interested listener to the EventStack
    eventStack.addListener(eventDispatcher);

    // start your engines!
    for (int count = numThreads; --count >= 0; ) {
      final WorkerThread workerThread = new WorkerThread();
      workerThread.addListener(eventDispatcher);
      new Thread(sortThreadGroup, workerThread, "Quick Sort Thread " + count).start();
    }
  }

  private synchronized void decActiveThreadCount() {
    if (logger.isDebugEnabled()) {
      logger.debug("Decrement activeThreadCount (" + getActiveThreadCount() + ")...");
    }
    if (getActiveThreadCount() > 0) {
      activeThreadCount--;
    }
  }

  private synchronized int getActiveThreadCount() {
    return activeThreadCount;
  }

  private synchronized void incActiveThreadCount() {
    if (logger.isDebugEnabled()) {
      logger.debug("Increment activeThreadCount (" + getActiveThreadCount() + ")...");
    }
    activeThreadCount++;
  }

  private Sortable getCollection() {
    return collection;
  }

  private void setCollection(final Sortable collection) {
    if (ObjectUtil.isNull(collection)) {
      logger.warn("The Sortable collection cannot be null!");
      throw new NullPointerException("The Sortable collection cannot be null!");
    }
    this.collection = collection;
  }

  private boolean isInterrupted() {
    return interrupted;
  }

  private void setInterrupted(final boolean interrupted) {
    logger.debug("interrupted = " + interrupted);
    this.interrupted = interrupted;
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(final int threshold) {
    logger.debug("threshold = " + threshold);
    this.threshold = threshold;
  }

  private synchronized void interrupt() {
    if (!isInterrupted()) {
      final Thread[] threadList = new Thread[sortThreadGroup.activeCount()];
      sortThreadGroup.enumerate(threadList);
      for (int index = threadList.length; --index >= 0; ) {
        threadList[index].interrupt();
      }
      setInterrupted(true);
    }
  }

  private void join() {
    while (sortThreadGroup.activeCount() > 0) {
      final Thread[] threadList = new Thread[sortThreadGroup.activeCount()];
      sortThreadGroup.enumerate(threadList);
      try {
        threadList[0].join();
      }
      catch (InterruptedException e) {
        logger.warn("Thread (" + threadList[0].getName() + ") has been interrupted!");
      }
    }
  }

  public void sort(final Sortable collection) throws com.cp.common.util.sort.SortException {
    setCollection(collection);
    eventStack.push(new WorkUnit(0, (collection.size() - 1)));
    join();
  }

  /**
   * An event type that is fired when a Thread begins processing a WorkUnit and sorting the Sortable collection.
   */
  private final class ActiveThreadEvent extends EventObject {
    public ActiveThreadEvent(final Object source) {
      super(source);
    }
  }

  private final class ActiveThreadEventHandler implements EventHandler {

    public void processEvent(final EventObject event) {
      if (event instanceof ActiveThreadEvent) {
        incActiveThreadCount();
      }
    }
  }

  /**
   * A class designated for calling the appropriate EventHandler object based on the event type of the event
   * received upon notification.
   */
  private final class EventDispatcher implements InterestedListener {

    private final Map eventHandlerMap = new HashMap();

    public EventDispatcher() {
      // register event handlers for the various events listened to this class to dispatch to the appropriate
      // event handler
      eventHandlerMap.put(ActiveThreadEvent.class, new ActiveThreadEventHandler());
      eventHandlerMap.put(InactiveThreadEvent.class, new InactiveThreadEventHandler());
      eventHandlerMap.put(ProcessingWorkUnitEvent.class, new ProcessingWorkUnitEventHandler());
    }

    public void notify(final EventObject event) {
      final EventHandler eventHandler = (EventHandler) eventHandlerMap.get(event.getClass());
      if (ObjectUtil.isNotNull(eventHandler)) {
        eventHandler.processEvent(event);
      }
      else {
        logger.warn("Received event of type (" + event.getClass().getName() + ") that does not have a registered handler!");
      }
    }
  }

  /**
   * A common interface to the EventHandlers that will process the various event types form the EventSources.
   * This class represents the sink.
   */
  private interface EventHandler {
    public void processEvent(EventObject event);
  }

  /**
   * A class designating it's subclasses and being sources for certain types of events.  This class handles the
   * mechanics of tracking and notify listeners interested in the events generated by this event source.
   */
  private abstract class EventSource {

    private final List eventListenerList = new LinkedList();

    public void addListener(final InterestedListener listener) {
      if (ObjectUtil.isNull(listener)) {
        logger.warn("Cannot add a null listener to the (" + getClass().getName() + ") class!");
        throw new NullPointerException("Cannot add a null listener to the (" + getClass().getName() + ") class!");
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Adding listener (" + listener.getClass().getName() + ")");
      }
      eventListenerList.add(listener);
    }

    protected void notifyListeners(final EventObject event) {
      for (Iterator it = eventListenerList.iterator(); it.hasNext(); ) {
        final InterestedListener listener = (InterestedListener) it.next();
        listener.notify(event);
      }
    }

    public void removeListener(final InterestedListener listener) {
      if (ObjectUtil.isNull(listener)) {
        logger.info("The listener to remove was null; returning!");
        return;
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Removing listener (" + listener.getClass().getName() + ")");
      }
      eventListenerList.remove(listener);
    }
  }

  /**
   * A container for work units created during the quick sort operation, stored and to be picked up by Worker Threads
   * processing the work units.
   */
  private final class EventStack extends EventSource {

    private final Stack stack = new Stack();

    public synchronized boolean isEmpty() {
      return stack.isEmpty();
    }

    public synchronized Object pop() throws InterruptedException {
      // if there is nothing to pop, wait until some process pushes an item to the Stack
      while (isEmpty()) {
        wait();
      }

      final Object poppedObj = stack.pop();
      if (logger.isDebugEnabled()) {
        logger.debug("popped obj (" + poppedObj + ")");
      }

      // nofity the appropriate handler that a worker Thread picked up a WorkUnit to process (sort)
      notifyListeners(new ActiveThreadEvent(Thread.currentThread()));

      return poppedObj;
    }

    public synchronized void push(final Object obj) {
      if (logger.isDebugEnabled()) {
        logger.debug("pushing obj (" + obj + ")");
      }
      stack.push(obj);
      notifyAll();
    }
  }

  /**
   * An event type that is fired when the worker Thread (processing Thread executing sort routine) is about to wait
   * for more work to do.
   */
  private final class InactiveThreadEvent extends EventObject {
    public InactiveThreadEvent(final Object source) {
      super(source);
    }
  }

  private final class InactiveThreadEventHandler implements EventHandler {

    public void processEvent(final EventObject event) {
      if (event instanceof InactiveThreadEvent) {
        decActiveThreadCount();
        if (NumberUtil.isZero(getActiveThreadCount()) && eventStack.isEmpty()) {
          interrupt();
        }
      }
    }
  }

  /**
   * An interface for notifying interested listeners of EventSources when certain events occur.
   */
  private interface InterestedListener extends EventListener {
    public void notify(EventObject event);
  }

  /**
   * A class used to adapt the Sortable interface and treat the Sortable collection as an instance of the List
   * interface.
   */
  private static final class ListAdapter extends AbstractList {

    private final Sortable collection;

    public ListAdapter(final Sortable collection) {
      this.collection = collection;
    }

    public Object get(final int index) {
      if (logger.isDebugEnabled()) {
        logger.debug("index = " + index);
      }
      return collection.get(index);
    }

    public Object set(final int index, final Object element) {
      final Object previousElementAtPosition = get(index);
      try {
        collection.set(element, index);
      }
      catch (Exception ignore) {
        logger.warn("Exception occurred while setting element on Sortable collection!", ignore);
      }
      return previousElementAtPosition;
    }

    public int size() {
      return collection.size();
    }
  }

  /**
   * An event fired when a worker Thread picks up a WorkUnit to process and begins the sort routine.
   */
  private final class ProcessingWorkUnitEvent extends EventObject {

    private final WorkUnit workUnit;

    public ProcessingWorkUnitEvent(final Object source, final WorkUnit workUnit) {
      super(source);
      this.workUnit = workUnit;
    }

    public WorkUnit getWorkUnit() {
      return workUnit;
    }
  }

  private final class ProcessingWorkUnitEventHandler implements EventHandler {

    public void processEvent(final EventObject event) {
      if (event instanceof ProcessingWorkUnitEvent) {
        if (logger.isDebugEnabled()) {
          final ProcessingWorkUnitEvent evt = (ProcessingWorkUnitEvent) event;
          logger.debug("Thread (" + Thread.currentThread().getName() + ") is processing WorkUnit ("
            + evt.getWorkUnit() + ")...");
        }
      }
    }
  }

  /**
   * Worker Thread used process work units by invoking the quick sort algorithm.
   */
  private final class WorkerThread extends EventSource implements Runnable {

    public void quickSort(final WorkUnit workUnit) throws com.cp.common.util.sort.SortException {
      //System.out.println("Processing WorkUnit (" + workUnit + ")...");
      if (logger.isDebugEnabled()) {
        notifyListeners(new ProcessingWorkUnitEvent(Thread.currentThread(), workUnit));
      }

      if (workUnit.isBeginGreaterThanEqualToEnd()) {
        return;
      }

      if (workUnit.getDifference() <= getThreshold()) {
        //heapSort.sort(new SearchableSortableCollection(new ListAdapter(getCollection()).subList(
        //  workUnit.getBeginIndex(), workUnit.getEndIndex())));
        insertionSort.insertionSort(getCollection(), workUnit.getBeginIndex(), workUnit.getEndIndex());
        return;
      }

      Object pivot = getCollection().get(workUnit.getBeginIndex());

      int bindex = workUnit.getBeginIndex() + 1;
      int eindex = workUnit.getEndIndex();

      while (bindex < eindex) {
        // Find a value greater than the pivot value.
        HIGH: while (bindex < eindex) {
          if (getOrderBy().compare(getCollection().get(bindex), pivot) > 0) {
            break HIGH;
          }
          ++bindex;
        }

        // Find a value less than the pivot value.
        LOW: while (eindex >= bindex) {
          if (getOrderBy().compare(getCollection().get(eindex), pivot) < 0) {
            break LOW;
          }
          --eindex;
        }

        if (bindex < eindex) {
          swap(getCollection(), bindex, eindex);
        }
      }

      swap(getCollection(), workUnit.getBeginIndex(), eindex); // Swap Pivot
      eventStack.push(new WorkUnit(workUnit.getBeginIndex(), (eindex - 1)));
      eventStack.push(new WorkUnit((eindex + 1), workUnit.getEndIndex()));
    }

    public void run() {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          final WorkUnit workUnit = (WorkUnit) eventStack.pop();
          // note, the notification for the active Thread event is in the pop method of the EventStack
          quickSort(workUnit);
          Thread.yield();
          notifyListeners(new InactiveThreadEvent(Thread.currentThread()));
        }
      }
      catch (InterruptedException e) {
        //System.out.println("Thread (" + Thread.currentThread().getName() + ") was interrupted; activeThreadCount ("
        //  + getActiveThreadCount() + ") event stack empty (" + eventStack.isEmpty() + ")!");
        logger.warn("Thread (" + Thread.currentThread().getName() + ") has been interrupted!", e);
      }
      catch (com.cp.common.util.sort.SortException e) {
        logger.error("Error occurred while sorting collection!", e);
      }
      //System.out.println("Thread (" + Thread.currentThread().getName() + ") is exiting!");
      if (logger.isInfoEnabled()) {
        logger.info("Thread (" + Thread.currentThread().getName() + ") is exiting!");
      }
    }
  }

  /**
   * Defines a unit of work in which the quick sort algorithm will perform the sort operation.
   */
  private static final class WorkUnit {

    private final int beginIndex;
    private final int endIndex;

    public WorkUnit(final int beginIndex, final int endIndex) {
      if (logger.isDebugEnabled()) {
        logger.debug("beginIndex = " + beginIndex);
        logger.debug("endIndex = " + endIndex);
      }
      this.beginIndex = beginIndex;
      this.endIndex = endIndex;
    }

    public int getBeginIndex() {
      return beginIndex;
    }

    public boolean isBeginGreaterThanEqualToEnd() {
      return (getBeginIndex() >= getEndIndex());
    }

    public int getDifference() {
      return (getEndIndex() - getBeginIndex());
    }

    public int getEndIndex() {
      return endIndex;
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof WorkUnit)) {
        return false;
      }

      final WorkUnit that = (WorkUnit) obj;

      return (getBeginIndex() == that.getBeginIndex())
        && (getEndIndex() == that.getEndIndex());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + getBeginIndex();
      hashValue = 37 * hashValue + getEndIndex();
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{beginIndex = ");
      buffer.append(getBeginIndex());
      buffer.append(", endIndex = ").append(getEndIndex());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
