/*
 * UniqueList.java (c) 24 January 2007
 *
 * The UniqueList class implements the Decorator design pattern by decorating any tye of Collection Framework
 * List object enforcing a unique constraint.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.3
 * @see java.util.AbstractList
 * @see java.util.List
 * @see java.util.Set
 */

package com.cp.common.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UniqueList<E> extends AbstractList<E> {

  private static final Log logger = LogFactory.getLog(UniqueList.class);

  private static final int DEFAULT_INITIAL_CAPACITY = 51;

  private final List<E> backingList;

  /**
   * Creates an instance of the UniqueList class with a default backing List implementation (java.util.ArrayList).
   * @see java.util.ArrayList
   */
  public UniqueList() {
    this(new ArrayList<E>(DEFAULT_INITIAL_CAPACITY));
  }

  /**
   * Creates an instance of the UniqueList class initialized and backed by the specified List object.
   * @param backingList the List object decorated by this class to enforce uniqueness.
   */
  public UniqueList(final List<E> backingList) {
    this.backingList = backingList;
  }

  /**
   * Adds an element to this list providing the element is unique.
   * @param index an integer value specifying the index in the list where the element is added.
   * @param element the Object to add to the list.
   */
  public void add(final int index, final E element) {
    if (contains(element)) {
      logger.warn("Element (" + element + ") is already in the List!");
      throw new IllegalArgumentException("Element (" + element + ") is already in the List!");
    }
    backingList.add(index, element);
  }

  /**
   * Returns the element at index in the list.
   * @param index the index in the list of the element to return.
   * @return the Object element at index in the list.
   */
  public E get(final int index) {
    return backingList.get(index);
  }

  /**
   * Removes an element from the list at the specified index.
   * @param index the index in the list of the element to remove.
   * @return the element at index in the list.
   */
  public E remove(final int index) {
    return backingList.remove(index);
  }

  /**
   * Sets the element at index in this list to the specified Object element.
   * @param index the index in the list of the element to set.
   * @param element the Object element to set in the list at index.
   * @return the previous element at index in the list.
   */
  public E set(final int index, final E element) {
    if (contains(element)) {
      logger.warn("Element (" + element + ") is already contained in the List!");
      throw new IllegalArgumentException("Element (" + element + ") is already contained in the List!");
    }
    return backingList.set(index, element);
  }

  /**
   * Returns the number of elements in this list.
   * @return a integer value specifying the number of elements in this list.
   */
  public int size() {
    return backingList.size();
  }

  /**
   * Prints to standard out the contents of this list.
   * @param list the List object to print.
   */
  private static void printList(final List list) {
    for (Object element : list) {
      System.out.println(element);
    }
  }

  public static void main(final String[] args) {
    final List<String> animalList = new UniqueList<String>(new ArrayList<String>(5));
    animalList.add("Ape");
    animalList.add("Baboon");
    animalList.add("Chimpanzee");
    animalList.add("Orangutang");
    //animalList.add(10);

    try {
      animalList.add("Ape");
      assert false : "The List already contains 'Ape' and should have thrown an IllegalArgumentException!";
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    printList(animalList);

    final List<Integer> primeNumberList = new UniqueList<Integer>(new LinkedList<Integer>());
    primeNumberList.add(1);
    primeNumberList.add(2);
    primeNumberList.add(3);
    primeNumberList.add(5);
    primeNumberList.add(7);

    try {
      primeNumberList.set(0, 5);
      assert false : "The List already contains 5 and should have thrown an IllegalArgumentException!";
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    printList(primeNumberList);
  }

}
