/*
 * AbstractSortFactory.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 */

package com.cp.common.util.sort;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.PropertyManager;
import com.cp.common.util.SortAscendingComparator;
import java.util.Comparator;
import org.apache.log4j.Logger;

public abstract class AbstractSortFactory {

  private static AbstractSortFactory INSTANCE;

  private static final Logger logger = Logger.getLogger(AbstractSortFactory.class);

  private static final String SORT_FACTORY_PROPERTY_KEY = "cp-common.sort.factory";

  /**
   * Factory method returning a service provider instance of the AbstractSortFactory class.
   * @return a service provider instance of the AbstractSortFatory class.
   */
  public synchronized static AbstractSortFactory getInstance() {
    if (ObjectUtil.isNull(INSTANCE)) {
      String defaultSortFactoryClassName = null;

      try {
        defaultSortFactoryClassName = PropertyManager.getInstance().getStringPropertyValue(SORT_FACTORY_PROPERTY_KEY);

        if (logger.isDebugEnabled()) {
          logger.debug("defaultSortFactoryClassName (" + defaultSortFactoryClassName + ")");
        }

        INSTANCE = (AbstractSortFactory) ClassUtil.getInstance(defaultSortFactoryClassName);
      }
      catch (ClassNotFoundException e) {
        logger.error("Unable to find provider class (" + defaultSortFactoryClassName + ") in CLASSPATH!", e);
        throw new ConfigurationException("Unable to find provider class (" + defaultSortFactoryClassName + ") in CLASSPATH!", e);
      }
      catch (ConfigurationException e) {
        logger.error("Unable to determine provider class from property (" + SORT_FACTORY_PROPERTY_KEY + ")!", e);
        throw new ConfigurationException("Unable to determine provider class from property (" + SORT_FACTORY_PROPERTY_KEY + ")!", e);
      }
      catch (InstantiationException e) {
        logger.error("Unable to create an instance of provider class (" + defaultSortFactoryClassName + ")!", e);
        throw new ConfigurationException("Unable to create an instance of provider class (" + defaultSortFactoryClassName + ")!", e);
      }
    }

    return INSTANCE;
  }

  /**
   * Gets an instance of the Bubble Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Bubble Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getBubbleSort() {
    return getBubbleSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Bubble Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Bubble Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getBubbleSort(final Comparator orderBy) {
    return new com.cp.common.util.sort.BubbleSort(orderBy);
  }

  /**
   * Gets an instance of the Heap Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Heap Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getHeapSort() {
    return getHeapSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Heap Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Heap Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getHeapSort(final Comparator orderBy) {
    return new com.cp.common.util.sort.HeapSort(orderBy);
  }

  /**
   * Gets an instance of the Insertion Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Insertion Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getInsertionSort() {
    return getInsertionSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Insertion Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Insertion Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getInsertionSort(final Comparator orderBy) {
    return new com.cp.common.util.sort.InsertionSort(orderBy);
  }

  /**
   * Gets an instance of the Merge Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Merge Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getMergeSort() {
    return getMergeSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Merge Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Merge Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getMergeSort(final Comparator orderBy) {
    return new MergeSort(orderBy);
  }

  /**
   * Gets an instance of the Multithreaded Quick Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Multithreaded Quick Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getMultithreadedQuickSort() {
    return getMultithreadedQuickSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Multithreaded Quick Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Multithreaded Quick Sort algorithm.
   */
  public abstract com.cp.common.util.sort.Sorter getMultithreadedQuickSort(Comparator orderBy);

  /**
   * Gets an instance of the Quick Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Quick Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getQuickSort() {
    return getQuickSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Quick Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Quick Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getQuickSort(final Comparator orderBy) {
    return new com.cp.common.util.sort.QuickSort(orderBy);
  }

  /**
   * Gets an instance of the Selection Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Selection Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getSelectionSort() {
    return getSelectionSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Selection Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Selection Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getSelectionSort(final Comparator orderBy) {
    return new com.cp.common.util.sort.SelectionSort(orderBy);
  }

  /**
   * Gets an instance of the Shell Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Shell Sort algorithm.
   */
  public Sorter getShellSort() {
    return getShellSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Shell Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Shell Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getShellSort(final Comparator orderBy) {
    return new ShellSort(orderBy);
  }

  /**
   * Gets an instance of the Tree Sort algorithm, sorting in ascending order.
   * @return a Sorter that implements the Tree Sort algorithm.
   */
  public Sorter getTreeSort() {
    return getTreeSort(SortAscendingComparator.getInstance());
  }

  /**
   * Gets an instance of the Tree Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Tree Sort algorithm.
   */
  public abstract Sorter getTreeSort(Comparator orderBy);

  /**
   * Gets a Sorter implemented the sorting algorithm based on the SortType enumerated-type.
   * @param sortType the enumerated-type specifying the sorting algorithm to get.
   * @return a Sorter implementing the sorting algorithm specified by the SortType enumerated-type.
   */
  public final com.cp.common.util.sort.Sorter getSorter(final com.cp.common.util.sort.SortType sortType) {
    return getSort(sortType, SortAscendingComparator.getInstance());
  }

  /**
   * Gets a Sorter implementing the sorting algorithm based on the SortType enumerated-type.
   * @param sortType the enumerated-type specifying the sorting algorithm to get.
   * @param orderBy determines the order to sort the elements in the Sortable colllection by the Sorter.
   * @return a Sorter implementing the sorting algorithm specified by the SortType enumerated-type.
   */
  public final Sorter getSort(final SortType sortType, final Comparator orderBy) {
    // TODO: implement this logic in more of a polymorphic way.
    if (com.cp.common.util.sort.SortType.BUBBLE_SORT.equals(sortType)) {
      return getBubbleSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.HEAP_SORT.equals(sortType)) {
      return getHeapSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.INSERTION_SORT.equals(sortType)) {
      return getInsertionSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.MERGE_SORT.equals(sortType)) {
      return getMergeSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.MULTITHREADED_QUICK_SORT.equals(sortType)) {
      return getMultithreadedQuickSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.QUICK_SORT.equals(sortType)) {
      return getQuickSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.SELECTION_SORT.equals(sortType)) {
      return getSelectionSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.SHELL_SORT.equals(sortType)) {
      return getShellSort(orderBy);
    }
    else if (com.cp.common.util.sort.SortType.TREE_SORT.equals(sortType)) {
      return getTreeSort(orderBy);
    }
    else {
      logger.warn("The sortType (" + sortType + ") is not a supported sorting algorithm!");
      throw new IllegalArgumentException("The sortType (" + sortType + ") is not a supported sorting algorithm!");
    }
  }

}
