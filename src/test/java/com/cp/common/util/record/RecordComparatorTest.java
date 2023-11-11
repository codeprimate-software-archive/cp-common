/*
 * RecordComparatorTest.java (c) 3 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.9.17
 */

package com.cp.common.util.record;

import com.cp.common.util.record.*;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.ColumnImpl;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordComparator;
import com.cp.common.util.DateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class RecordComparatorTest extends TestCase {

  private static final Logger logger = Logger.getLogger(RecordComparatorTest.class);

  public RecordComparatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RecordComparatorTest.class);
    //suite.addTest(new RecordComparatorTest("testName"));
    return suite;
  }

  private Column createTestColumn(final String columnName, final Class columnType) {
    return new ColumnImpl(columnName, columnType);
  }

  private Record createTestRecord(final Integer personId,
                                  final String firstName,
                                  final String lastName,
                                  final Calendar dateOfBirth) {
    final Record record = com.cp.common.util.record.AbstractRecordFactory.getInstance().getRecordInstance();
    record.addField("personId", personId);
    record.addField("firstName", firstName);
    record.addField("lastName", lastName);
    record.addField("dateOfBirth", dateOfBirth);
    return record;
  }

  public void testRecordComparator() throws Exception {
    final Column[] columnArray = {
      createTestColumn("personId", Integer.class),
      createTestColumn("firstName", String.class),
      createTestColumn("lastName", String.class),
      createTestColumn("dateOfBirth", Calendar.class)
    };

    final Record[] recordArray = {
      createTestRecord(new Integer(100), "Jon", "Doe", DateUtil.getCalendar(1970, Calendar.FEBRUARY, 12)),
      createTestRecord(new Integer(200), "Jack", "Handy", DateUtil.getCalendar(1982, Calendar.SEPTEMBER, 30)),
      createTestRecord(new Integer(300), "Ben", "Dover", DateUtil.getCalendar(1958, Calendar.MAY, 17)),
      createTestRecord(new Integer(400), "Ted", "Johnson", DateUtil.getCalendar(1969, Calendar.JUNE, 10)),
      createTestRecord(new Integer(500), "Amy", "Turner", DateUtil.getCalendar(1977, Calendar.NOVEMBER, 11)),
      createTestRecord(new Integer(600), "Sandy", "Handy", DateUtil.getCalendar(1954, Calendar.JULY, 2)),
      createTestRecord(new Integer(700), "Jon", "Ryan", DateUtil.getCalendar(1958, Calendar.MAY, 17))
    };

    final List recordList = new ArrayList(Arrays.asList(recordArray));

    assertNotNull(recordList);
    assertEquals(7, recordList.size());

    // Shuffle Records before Sorting...
    java.util.Collections.shuffle(recordList);

    logger.debug("Sorting by Last Name First, First Name Last...");
    // Sort by Last Name first, First Name last!
    Column[] columnArrayForSorting = { columnArray[2], columnArray[1] };
    RecordComparator recordComparator = new RecordComparator(columnArrayForSorting);
    java.util.Collections.sort(recordList, recordComparator);
    assertEquals(recordArray[0], recordList.get(0));
    assertEquals(recordArray[2], recordList.get(1));
    assertEquals(recordArray[1], recordList.get(2));
    assertEquals(recordArray[5], recordList.get(3));
    assertEquals(recordArray[3], recordList.get(4));
    assertEquals(recordArray[6], recordList.get(5));
    assertEquals(recordArray[4], recordList.get(6));

    logger.debug("Sorting by Date of Birth, First Name and then Last Name...");
    // Sort by Date of Birth, First Name and then Last Name!
    columnArrayForSorting = new Column[] { columnArray[3], columnArray[1], columnArray[2] };
    recordComparator = new RecordComparator(columnArrayForSorting);
    recordComparator.registerComparator(Calendar.class, DateUtil.getCalendarComparator());
    java.util.Collections.sort(recordList, recordComparator);
    assertEquals(recordArray[5], recordList.get(0));
    assertEquals(recordArray[2], recordList.get(1));
    assertEquals(recordArray[6], recordList.get(2));
    assertEquals(recordArray[3], recordList.get(3));
    assertEquals(recordArray[0], recordList.get(4));
    assertEquals(recordArray[4], recordList.get(5));
    assertEquals(recordArray[1], recordList.get(6));

    logger.debug("Sorting by First Name, Date of Birth...");
    // Sort by First Name, Date of Birth
    columnArrayForSorting = new Column[] { columnArray[1], columnArray[3] };
    recordComparator = new RecordComparator(columnArrayForSorting);
    recordComparator.registerComparator(Calendar.class, DateUtil.getCalendarComparator());
    java.util.Collections.sort(recordList, recordComparator);
    assertEquals(recordArray[4], recordList.get(0));
    assertEquals(recordArray[2], recordList.get(1));
    assertEquals(recordArray[1], recordList.get(2));
    assertEquals(recordArray[6], recordList.get(3));
    assertEquals(recordArray[0], recordList.get(4));
    assertEquals(recordArray[5], recordList.get(5));
    assertEquals(recordArray[3], recordList.get(6));

    logger.debug("Sorting by Person ID...");
    // Sort by Person ID
    columnArrayForSorting = new Column[] { columnArray[0] };
    recordComparator = new RecordComparator(columnArrayForSorting);
    java.util.Collections.sort(recordList, recordComparator);
    assertEquals(recordArray[0], recordList.get(0));
    assertEquals(recordArray[1], recordList.get(1));
    assertEquals(recordArray[2], recordList.get(2));
    assertEquals(recordArray[3], recordList.get(3));
    assertEquals(recordArray[4], recordList.get(4));
    assertEquals(recordArray[5], recordList.get(5));
    assertEquals(recordArray[6], recordList.get(6));
  }

}
