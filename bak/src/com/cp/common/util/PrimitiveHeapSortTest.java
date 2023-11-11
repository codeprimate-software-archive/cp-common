
package com.cp.common.util;

/**
 * Implements the Heap Sort algorithm.
 * Test Number Set (5 8 16 2 4 20).
 * @author blumj
 * @created 2004 April 30
 */
public final class PrimitiveHeapSortTest {
  
  private static int count = 0;

  private static void heapSort(final int numbers[]) {
    int i;
    int temp;

    for (i = (numbers.length / 2) - 1; i >= 0; i--) {
      siftDown(numbers, i, numbers.length);
    }

    for (i = numbers.length - 1; i >= 1; i--) {
      temp = numbers[0];
      numbers[0] = numbers[i];
      numbers[i] = temp;
      print(numbers);
      siftDown(numbers, 0, i - 1);
    }
  }

  private static void siftDown(final int numbers[], int root, final int bottom) {
    boolean done = false;
    int maxChild;
    int temp;

    while ((root * 2 <= bottom) && (!done)) {
      if (root * 2 == bottom) {
        maxChild = root * 2;
      }
      else if (numbers[root * 2] > numbers[root * 2 + 1]) {
        maxChild = root * 2;
      }
      else {
        maxChild = root * 2 + 1;
      }

      if (numbers[root] < numbers[maxChild]) {
        temp = numbers[root];
        numbers[root] = numbers[maxChild];
        numbers[maxChild] = temp;
        root = maxChild;
        print(numbers);
      }
      else {
        done = true;
      }
    }
  }
  
  private static void print(final int[] numbers) {
    System.out.print((++count) + ")");
    for (int index = 0; index < numbers.length; index++) {
      System.out.print("\t");
      System.out.print(numbers[index]);
    }
    System.out.println("\n");
  }
  
  public static void main(final String[] args) {
    if (args.length == 0) {
      System.err.println("Please specify an array of numbers!");
      System.exit(0);
    }
    
    final int[] numbers = new int[args.length]; 
    for (int index = 0; index < args.length; index++) {
      numbers[index] = Integer.parseInt(args[index]);
    }

    heapSort(numbers);
  }

}

