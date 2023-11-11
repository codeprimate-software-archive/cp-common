/*
 * LastModifiedFileFilterTest.java (c) 12 June 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.LastModifiedFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.cp.common.util.DateUtil;

import org.junit.Before;
import org.junit.Test;

public class LastModifiedFileFilterTest extends CommonIOTestCase {

  private static final File fileContentFinderMarch2004 = getFile("/etc/test/fileContentFinderTestFile.txt");
  private static final File monkeyJune2004 = getFile("/etc/test/subdir1/monkey.jar");
  private static final File testFileToday = getFile("/etc/test/testFile.txt");

  private static final Set<File> fileSet = new HashSet<File>(3);

  static {
    fileSet.add(fileContentFinderMarch2004);
    fileSet.add(monkeyJune2004);
    fileSet.add(testFileToday);
  }

  @Before
  public void setup() throws Exception {

    super.setup();

    assertTrue("Could not find file (" + fileContentFinderMarch2004 + ")!", fileContentFinderMarch2004.exists());
    assertTrue("Could not find file (" + monkeyJune2004 + ")!", monkeyJune2004.exists());
    assertTrue("Could not find file (" + testFileToday + ")!", testFileToday.exists());

    final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(testFileToday, true));
    final DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    fileWriter.write("Ran test on " + format.format(Calendar.getInstance().getTime()) + "\n");
    fileWriter.flush();
    fileWriter.close();
  }

  private Set<File> getFilteredFileSet(final FileFilter filter, final Set<File> fileSet) {
    assertNotNull("The FileFilter object cannot be null!", filter);

    final Set<File> filteredFileSet = new HashSet<File>(fileSet.size());

    for (final File file : fileSet) {
      if (filter.accept(file)) {
        filteredFileSet.add(file);
      }
    }

    return filteredFileSet;
  }

  @Test
  public void testGetAfterInstance() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getAfterInstance(
      DateUtil.getCalendar(2004, Calendar.JULY, 1)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(1, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(testFileToday));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getAfterInstance(
      DateUtil.getCalendar(2003, Calendar.DECEMBER, 31)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(3, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));
    assertTrue(filteredFileSet.contains(monkeyJune2004));
    assertTrue(filteredFileSet.contains(testFileToday));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getAfterInstance(
      DateUtil.getTomorrow()), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetAfterInstanceWithNullDate() throws Exception {
    try {
      LastModifiedFileFilter.getAfterInstance(null);
      fail("Calling getAfterInstance with a null Calendar date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The after date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getAfterInstance with a null Calendar date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetBeforeInstance() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getBeforeInstance(
      DateUtil.getCalendar(2004, Calendar.MAY, 1)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(1, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getBeforeInstance(
      DateUtil.getCalendar(2003, Calendar.DECEMBER, 31)), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getBeforeInstance(
      DateUtil.getTomorrow()), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(3, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));
    assertTrue(filteredFileSet.contains(monkeyJune2004));
    assertTrue(filteredFileSet.contains(testFileToday));
  }

  @Test
  public void testGetBeforeInstanceWithNullDate() throws Exception {
    try {
      LastModifiedFileFilter.getBeforeInstance(null);
      fail("Calling getBeforeInstance with a null Calendar date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The before date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getBeforeInstance with a null Calendar date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetBetweenInstance() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getBetweenInstance(
      DateUtil.getCalendar(2004, Calendar.MARCH, 1), DateUtil.getCalendar(2004, Calendar.JUNE, 30)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(2, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));
    assertTrue(filteredFileSet.contains(monkeyJune2004));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getBetweenInstance(DateUtil.getCalendar(2004, Calendar.APRIL, 1),
      DateUtil.getCalendar(2004, Calendar.MAY, 31)), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetBetweenInstanceWithNullBeginDate() throws Exception {
    try {
      LastModifiedFileFilter.getBetweenInstance(null, Calendar.getInstance());
      fail("Calling getBetweenInstance with a null Calendar begin date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The begin date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getBetweenInstance with a null Calendar begin date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetBetweenInstanceWithNullEndDate() throws Exception {
    try {
      LastModifiedFileFilter.getBetweenInstance(Calendar.getInstance(), null);
      fail("Calling getBetweenInstance with a null Calendar end date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The end date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getBetweenInstance with a null Calendar end date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetOnInstance() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getOnInstance(
      DateUtil.truncate(Calendar.getInstance())), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(1, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(testFileToday));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getOnInstance(
      DateUtil.getCalendar(2003, Calendar.OCTOBER, 15)), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetOnInstanceWithNullDate() throws Exception {
    try {
      LastModifiedFileFilter.getOnInstance(null);
      fail("Calling getOnInstance with a null Calendar date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The on date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getOnInstance with a null Calendar date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetOutsideInstance() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getOutsideInstance(
      DateUtil.getCalendar(2004, Calendar.JUNE, 1), DateUtil.getCalendar(2004, Calendar.JUNE, 30)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(2, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));
    assertTrue(filteredFileSet.contains(testFileToday));

    filteredFileSet = getFilteredFileSet(LastModifiedFileFilter.getOutsideInstance(
      DateUtil.getCalendar(2004, Calendar.APRIL, 1), DateUtil.getCalendar(2004, Calendar.MAY, 31)), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(3, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(fileContentFinderMarch2004));
    assertTrue(filteredFileSet.contains(monkeyJune2004));
    assertTrue(filteredFileSet.contains(testFileToday));
  }

  @Test
  public void testGetOutsideInstanceWithNullMinDate() throws Exception {
    try {
      LastModifiedFileFilter.getOutsideInstance(null, Calendar.getInstance());
      fail("Calling getOutsideInstance with a null Calendar minimum date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The minimum date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getOutsideInstance with a null Calendar minimum date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetOutsideInstanceWithNullMaxDate() throws Exception {
    try {
      LastModifiedFileFilter.getOutsideInstance(Calendar.getInstance(), null);
      fail("Calling getOutsideInstance with a null Calendar maximum date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The maximum date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getOutsideInstance with a null Calendar maximum date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }
}
