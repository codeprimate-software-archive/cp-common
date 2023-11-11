/*
 * AbstractSortTest.java (c) 19 Septebmer 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.lang.Sortable;
import com.cp.common.util.SearchableSortableCollection;
import com.cp.common.util.SortAscendingComparator;
import com.cp.common.util.SortDescendingComparator;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;

public abstract class AbstractSortTest extends TestCase {

  public AbstractSortTest(final String testName) {
    super(testName);
  }

  protected final void assertShuffled(final List list, final Object[] sortedArray) {
    // Assert that the List has been shuffled and is not sorted.
    assertEquals(sortedArray.length, list.size());
    BREAK_LABEL: {
      int index = 0;
      for (Iterator it = list.iterator(); it.hasNext(); ) {
        if (!sortedArray[index++].equals(it.next())) {
          break BREAK_LABEL;
        }
      }
      fail("The animalList has not been shuffled!");
    }
  }

  protected final void assertSorted(final Collection list, final Object[] sortedArray) {
    // Assert the List is sorted.
    assertEquals(sortedArray.length, list.size());
    int index = 0;
    for (Iterator it = list.iterator(); it.hasNext(); ) {
      assertEquals(sortedArray[index++], it.next());
    }
  }

  protected abstract int getNumericListSize();

  private List getNumericListToSort() {
    final int listSize = getNumericListSize();
    final List numberList = new ArrayList(listSize);
    final Random numberGenerator = new Random(Calendar.getInstance().getTime().getTime());

    for (int count = listSize ; count-- > 0; ) {
      numberList.add(new Integer(numberGenerator.nextInt(listSize)));
    }

    return numberList;
  }

  protected abstract Sorter getSorterImplementation(Comparator orderBy);

  private void printArray(final Object[] array) {
    System.out.print("[");
    for (int index = 0; index < array.length; index++) {
      System.out.print(array[index]);
      if (index < (array.length - 1)) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
  }

  public void testNumberSort() throws Exception {
    SearchableSortableCollection list = new SearchableSortableCollection(getNumericListToSort());
    //printArray(list.toArray());
    getSorterImplementation(SortAscendingComparator.getInstance()).sort(list); // sort the List in ascending order
    //printArray(list.toArray());

    for (int index = 1; index < list.size(); index++) {
      final int previousValue = ((Integer) list.get(index - 1)).intValue();
      final int currentValue = ((Integer) list.get(index)).intValue();
      assertTrue(currentValue >= previousValue);
    }

    list = new SearchableSortableCollection(getNumericListToSort());
    //printArray(list.toArray());
    getSorterImplementation(SortDescendingComparator.getInstance()).sort(list); // sort the List in descending order
    //printArray(list.toArray());

    for (int index = 1; index < list.size(); index++) {
      final int previousValue = ((Integer) list.get(index - 1)).intValue();
      final int currentValue = ((Integer) list.get(index)).intValue();
      assertTrue(previousValue >= currentValue);
    }
  }

  public void testTextSort() throws Exception {
    final String[] ascAnimalArray = {
      "Ardvark",
      "Baboon",
      "Cat",
      "Dog",
      "Elephant",
      "Farret",
      "Goose",
      "Horse",
      "Iguana",
      "Jackel",
      "Kangeroo",
      "Lama",
      "Mouse",
      "Nwet",
      "Osterich",
      "Porcupine",
      "Quale",
      "Rabbit",
      "Snake",
      "Tiger",
      "Urchin",
      "Vulture",
      "Whale",
      "Yack",
      "Zebra",
    };

    final String[] descAnimalArray = new String[ascAnimalArray.length];
    for (int index = 0;  index < ascAnimalArray.length; index++) {
      descAnimalArray[ascAnimalArray.length - index - 1] = ascAnimalArray[index];
    }

    final List ascAnimalList = new ArrayList(Arrays.asList(ascAnimalArray));
    java.util.Collections.shuffle(ascAnimalList);

    assertShuffled(ascAnimalList, ascAnimalArray);
    assertShuffled(ascAnimalList, descAnimalArray);

    // Adapt the List (ascAnimalList) implementation to sort it.
    SearchableSortableCollection list = new SearchableSortableCollection(ascAnimalList);
    getSorterImplementation(SortAscendingComparator.getInstance()).sort(list); // sort the List in ascending order

    assertSorted(list, ascAnimalArray);

    final List descAnimalList = new ArrayList(Arrays.asList(descAnimalArray));
    Collections.shuffle(descAnimalList);

    assertShuffled(descAnimalList, descAnimalArray);
    assertShuffled(descAnimalList, ascAnimalArray);

    list = new SearchableSortableCollection(descAnimalList);
    getSorterImplementation(SortDescendingComparator.getInstance()).sort(list); // sort the List in descending order

    assertSorted(list, descAnimalArray);
  }

  protected static final class SortableList extends AbstractList implements Sortable {
    private final List theList;

    public SortableList(final List theList) {
      this.theList = theList;
    }

    public void add(int index, Object element) {
      theList.add(index, element);
    }

    public Object get(int index) {
      return theList.get(index);
    }

    public Object remove(int index) {
      return theList.remove(index);
    }

    public void set(Object object, int index) throws Exception {
      theList.set(index, object);
    }

    public int size() {
      return theList.size();
    }
  }

}
