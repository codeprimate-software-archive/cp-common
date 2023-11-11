
package com.cp.common.util;

import java.util.Calendar;
import java.util.Random;
import junit.framework.Assert;

public final class PrimitiveQuickSortTest {

  private static final int ARRAY_SIZE = 1000000;

  private static void quickSort(int numbers[], int array_size) {
    q_sort(numbers, 0, array_size - 1);
  }

  private static void q_sort(final int numbers[], int left, int right) {

    int pivot, l_hold, r_hold;

    l_hold = left;
    r_hold = right;
    pivot = numbers[left];

    while (left < right) {

      while ((numbers[right] >= pivot) && (left < right)) {
        right--;
      }

      if (left != right) {
        numbers[left] = numbers[right];
        left++;
      }

      while ((numbers[left] <= pivot) && (left < right)) {
        left++;
	  }

      if (left != right) {
        numbers[right] = numbers[left];
        right--;
      }
    }

    numbers[left] = pivot;
    pivot = left;
    left = l_hold;
    right = r_hold;

    if (left < pivot) {
      q_sort(numbers, left, pivot-1);
    }

    if (right > pivot) {
      q_sort(numbers, pivot+1, right);
    }
  }

  private static int[] getNumbers() {
	final Random randomNumberGenerator = new Random(Calendar.getInstance().getTimeInMillis());
	final int[] numbers = new int[ARRAY_SIZE];

	for (int count = ARRAY_SIZE; --count >= 0; ) {
      numbers[count] = randomNumberGenerator.nextInt(ARRAY_SIZE);
	}

	return numbers;
  }

  private static void assertShuffled(final int[] numbers) {
	int previousValue = Integer.MIN_VALUE;
	for (int index = 0; index < numbers.length; index++) {
	  if (numbers[index] < previousValue) {
		return;
      }
      previousValue = numbers[index];
	}
	throw new IllegalStateException("The numbers array is not shuffled!");
  }

  private static void assertSorted(final int[] numbers) {
	for (int index = 1; index < numbers.length; index++) {
      Assert.assertTrue("(" + numbers[index] + ") is not greater than, equal to ("
        + numbers[index - 1] + ")!", numbers[index] >= numbers[index - 1]);
	}
  }

  public static void main(final String[] args) {
	final int[] numbers = getNumbers();

	assertShuffled(numbers);

    System.out.println("Sorting...");
	final long t0 = System.currentTimeMillis();
	quickSort(numbers, numbers.length);
	final long t1 = System.currentTimeMillis();

	assertSorted(numbers);

	final long diff = (t1 - t0);

	System.out.println("Sorting (" + ARRAY_SIZE + ") integer values took ("
	  + TimeUnit.convertTo(diff, TimeUnit.MILLISECOND) + ")!");
  }

}
