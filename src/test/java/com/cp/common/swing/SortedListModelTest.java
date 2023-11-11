/*
 * SortedListModelTest.java (c) 10 August 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.10
 */

package com.cp.common.swing;

import com.cp.common.lang.StringUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SortedListModelTest extends TestCase {

  public SortedListModelTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SortedListModelTest.class);
    //suite.addTest(new SortedListModelTest("testName"));
    return suite;
  }

  private List<String> getList(final ListModel listModel) {
    final List<String> list = new ArrayList<String>(listModel.getSize());

    for (int index = 0, size = listModel.getSize(); index < size; index++) {
      list.add(listModel.getElementAt(index).toString());
    }

    return list;
  }

  public void testSortedListModel() throws Exception {
    final DefaultListModel defaultListModel = new DefaultListModel();
    defaultListModel.addElement("08-baboon");
    defaultListModel.addElement("03-elephant");
    defaultListModel.addElement("07-advark");
    defaultListModel.addElement("02-shark");
    defaultListModel.addElement("05-cat");

    List expectedList = new ArrayList<String>(defaultListModel.getSize());
    expectedList.add("08-baboon");
    expectedList.add("03-elephant");
    expectedList.add("07-advark");
    expectedList.add("02-shark");
    expectedList.add("05-cat");

    List actualList = getList(defaultListModel);

    assertEquals(expectedList, actualList);

    final SortedListModel sortedListModel = new SortedListModel(defaultListModel, new TextComparator());

    expectedList.clear();
    expectedList.add("07-advark");
    expectedList.add("08-baboon");
    expectedList.add("05-cat");
    expectedList.add("03-elephant");
    expectedList.add("02-shark");

    actualList = getList(sortedListModel);

    assertEquals(expectedList, actualList);

    defaultListModel.add(1, "01-whale");
    defaultListModel.add(2, "09-jaguar");
    defaultListModel.addElement("04-dolphin");

    expectedList.clear();
    expectedList.add("08-baboon");
    expectedList.add("01-whale");
    expectedList.add("09-jaguar");
    expectedList.add("03-elephant");
    expectedList.add("07-advark");
    expectedList.add("02-shark");
    expectedList.add("05-cat");
    expectedList.add("04-dolphin");

    actualList = getList(defaultListModel);

    assertEquals(expectedList, actualList);

    expectedList.clear();
    expectedList.add("07-advark");
    expectedList.add("08-baboon");
    expectedList.add("05-cat");
    expectedList.add("04-dolphin");
    expectedList.add("03-elephant");
    expectedList.add("09-jaguar");
    expectedList.add("02-shark");
    expectedList.add("01-whale");

    actualList = getList(sortedListModel);

    assertEquals(expectedList, actualList);

    sortedListModel.setOrderBy(new NumericComparator());

    expectedList.clear();
    expectedList.add("01-whale");
    expectedList.add("02-shark");
    expectedList.add("03-elephant");
    expectedList.add("04-dolphin");
    expectedList.add("05-cat");
    expectedList.add("07-advark");
    expectedList.add("08-baboon");
    expectedList.add("09-jaguar");

    actualList = getList(sortedListModel);

    assertEquals(expectedList, actualList);
  }

  private static final class NumericComparator implements Comparator<String> {

    public int compare(final String text0, final String text1) {
      return text0.compareToIgnoreCase(text1);
    }
  }

  private static final class TextComparator implements Comparator<String> {

    public int compare(final String text0, final String text1) {
      final String modText0 = StringUtil.getLettersOnly(text0);
      final String modText1 = StringUtil.getLettersOnly(text1);
      return modText0.compareToIgnoreCase(modText1);
    }
  }

}
