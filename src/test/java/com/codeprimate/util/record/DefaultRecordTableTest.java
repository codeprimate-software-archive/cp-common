/*
 * DefaultRecordTableTest.java (c) 31 March 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.uti.AbstractRecordTableTest
 * @see com.codeprimate.util.record.DefaultRecordTable
 */

package com.codeprimate.util.record;

import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.support.MutableVisitor;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.record.AbstractRecordTableTest;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.InvalidColumnValueSizeException;
import com.cp.common.util.record.InvalidColumnValueTypeException;
import com.cp.common.util.record.NonUniqueColumnValueException;
import com.cp.common.util.record.NullColumnValueException;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordAdapter;
import com.cp.common.util.record.RecordTable;
import com.cp.common.util.search.LinearSearch;
import com.cp.common.util.search.SearchException;
import com.cp.common.util.search.SearchFilter;
import com.cp.common.util.search.Searcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DefaultRecordTableTest extends AbstractRecordTableTest {

  public DefaultRecordTableTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultRecordTableTest.class);
    //suite.addTest(new DefaultRecordTableTest("testName"));
    return suite;
  }

  protected com.cp.common.util.record.Record getRecordInstance(final Column[] columns, final Object[] data) {
    return new RecordAdapter(super.getRecordInstance(columns, data), Arrays.<Column>asList(columns));
  }

  public void testColumnIterator() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("ssn", String.class, true, 11, true)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    int columnIndex = 0;
    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      final Column actualColumn = it.next();
      assertEquals(columns[columnIndex], actualColumn);
      assertNotSame(columns[columnIndex], actualColumn);
    }

    // remove odd columns (firstName, dob)
    columnIndex = 0;
    for (Iterator<com.cp.common.util.record.Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      it.next();
      if ((columnIndex % 2) == 1) {
        it.remove();
      }
    }

    assertEquals(3, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    columnIndex = 0;
    for (Iterator<com.cp.common.util.record.Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex += 2) {
      final com.cp.common.util.record.Column actualColumn = it.next();
      assertEquals(columns[columnIndex], actualColumn);
      assertNotSame(columns[columnIndex], actualColumn);
    }

    final Column dob = getColumnInstance("dob", Calendar.class, true, 0, false);
    final com.cp.common.util.record.Column gender = getColumnInstance("gender", Gender.class, true, 0, false);
    final Column race = getColumnInstance("race", Race.class, true, 0, false, Race.WHITE);

    assertNotNull(dob);
    assertNotNull(gender);
    assertNotNull(race);
    assertFalse(recordTable.contains(dob));
    assertFalse(recordTable.contains(gender));
    assertFalse(recordTable.contains(race));
    assertTrue(recordTable.insertColumn(dob, 1));
    assertTrue(recordTable.insertColumn(gender, 3));
    assertTrue(recordTable.addColumn(race));
    assertTrue(recordTable.contains(dob));
    assertTrue(recordTable.contains(gender));
    assertTrue(recordTable.contains(race));

    // personId, dob, lastName, gender, ssn, race
    final com.cp.common.util.record.Column[] newColumns = {columns[0], dob, columns[2], gender, columns[4], race};

    assertEquals(newColumns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    columnIndex = 0;
    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      final Column actualColumn = it.next();
      assertEquals(newColumns[columnIndex], actualColumn);
      assertNotSame(newColumns[columnIndex], actualColumn);
    }

    try {
      final Iterator<Column> it = recordTable.columnIterator();
      assertTrue(it.hasNext());
      assertEquals(columns[0], it.next()); // personId
      it.remove();
      assertTrue(it.hasNext());
      assertEquals(dob, it.next()); // dob
      assertEquals(0, recordTable.getColumnIndex(dob));
      assertEquals(dob, recordTable.removeColumn(0));
      assertTrue(it.hasNext());
      it.next();
      fail("The Iterator.next() method should have thrown a ConcurrentModificationException after a call to RecordTable.removeColumn!");
    }
    catch (ConcurrentModificationException e) {
      // expected behavior!
    }
  }

  public void testCopy() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false)
    };

    final Object[][] data = {
      {new Integer(101), "Jack", "Handy", DateUtil.getCalendar(1968, Calendar.MAY, 21)},
      {new Integer(202), "Sandy", "Handy", DateUtil.getCalendar(1971, Calendar.OCTOBER, 2)},
      {new Integer(303), "Jon", "Doe", DateUtil.getCalendar(1956, Calendar.DECEMBER, 22)},
      {new Integer(404), "Jane", "Doe", DateUtil.getCalendar(1962, Calendar.FEBRUARY, 22)}
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final RecordTable recordTableCopy = (RecordTable) recordTable.copy();

    assertNotNull(recordTableCopy);
    assertNotSame(recordTable, recordTableCopy);
    assertEquals(recordTable.columnCount(), recordTableCopy.columnCount());
    assertEquals(recordTable.rowCount(), recordTableCopy.rowCount());

    for (Iterator<Column> columnIterator = recordTable.columnIterator(), columnCopyIterator = recordTableCopy.columnIterator(); columnIterator.hasNext();) {
      final Column column = columnIterator.next();
      final Column columnCopy = columnCopyIterator.next();
      assertEquals(column, columnCopy);
      assertNotSame(column, columnCopy);
    }

    for (Iterator<com.cp.common.util.record.Record> rowIterator = recordTable.rowIterator(), rowCopyIterator = recordTableCopy.rowIterator(); rowIterator.hasNext();) {
      final Record record = rowIterator.next();
      final com.cp.common.util.record.Record recordCopy = rowCopyIterator.next();
      assertEquals(record, recordCopy);
      assertNotSame(record, recordCopy);
    }

    // modify Record Sandy Handy in the record table copy
    final Record sandyHandyCopy = recordTableCopy.getRow(1);

    assertNotNull(sandyHandyCopy);
    assertEquals("Sandy", sandyHandyCopy.getValue(columns[1])); // firstName
    assertEquals("Handy", sandyHandyCopy.getValue(columns[2])); // lastName
    assertEquals(1, recordTable.getRowIndex(sandyHandyCopy));
    assertEquals("Handy", sandyHandyCopy.setValue(columns[2], "Beach")); // change Sandy Handy's lastName to Beach
    assertEquals("Beach", sandyHandyCopy.getValue(columns[2])); // lastName
    assertEquals("Beach", recordTableCopy.getCellValue(recordTableCopy.getRowIndex(sandyHandyCopy), columns[2])); // lastName
    assertFalse(recordTable.contains(sandyHandyCopy));

    final Record sandyHandy = recordTable.getRow(1);

    assertNotNull(sandyHandy);
    assertEquals("Sandy", sandyHandy.getValue(columns[1])); // firstName
    assertEquals("Handy", sandyHandy.getValue(columns[2])); // lastName

    // modify Record Jane Doe in the record table
    assertEquals("Jane", recordTable.getCellValue(3, columns[1])); // firstName
    assertEquals("Doe", recordTable.getCellValue(3, columns[2])); // lastName

    recordTable.setCellValue(3, columns[1], "Pie");

    assertEquals("Pie", recordTable.getCellValue(3, columns[1])); // firstName
    assertEquals("Doe", recordTable.getCellValue(3, columns[2])); // lastName
    assertEquals("Pie", recordTable.getRow(3).getValue(columns[1])); // firstName
    assertEquals("Jane", recordTableCopy.getCellValue(3, columns[1])); // firstName
    assertEquals("Doe", recordTableCopy.getCellValue(3, columns[2])); // lastName

    // remove Record Jack Handy from record table copy
    final com.cp.common.util.record.Record jackHandy = recordTable.getRow(0);

    assertNotNull(jackHandy);
    assertEquals("Jack", jackHandy.getValue(columns[1])); // firstName
    assertEquals("Handy", jackHandy.getValue(columns[2])); // lastName
    assertTrue(recordTableCopy.contains(jackHandy));
    assertEquals(0, recordTableCopy.removeRow(jackHandy)); // remove Jack Handy
    assertFalse(recordTableCopy.contains(jackHandy));
    assertTrue(recordTable.contains(jackHandy));
    assertEquals(recordTable.columnCount(), recordTableCopy.columnCount());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(columns.length, recordTableCopy.columnCount());
    TestUtil.assertNotEquals(recordTable.rowCount(), recordTableCopy.rowCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(3, recordTableCopy.rowCount());

    // add Record Bob Smith to record table
    final com.cp.common.util.record.Record bobSmith = getRecordInstance(columns, new Object[]{new Integer(505), "Bob", "Smith", null});

    assertNotNull(bobSmith);
    assertFalse(recordTable.contains(bobSmith));
    assertFalse(recordTableCopy.contains(bobSmith));
    assertTrue(recordTable.addRow(bobSmith)); // add Bob Smith
    assertTrue(recordTable.contains(bobSmith));
    assertFalse(recordTableCopy.contains(bobSmith));
    assertEquals(recordTable.columnCount(), recordTableCopy.columnCount());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(columns.length, recordTableCopy.columnCount());
    TestUtil.assertNotEquals(recordTable.rowCount(), recordTableCopy.rowCount());
    assertEquals(5, recordTable.rowCount());
    assertEquals(3, recordTableCopy.rowCount());

    // modify Column personId in record table copy
    final com.cp.common.util.record.Column personIdCopy = recordTableCopy.getColumn("personId");

    assertNotNull(personIdCopy);
    assertEquals("personId", personIdCopy.getName());
    assertTrue(recordTable.contains(personIdCopy));
    assertFalse(personIdCopy.isNullable());
    assertTrue(personIdCopy.isUnique());

    personIdCopy.setNullable(true);
    personIdCopy.setUnique(false);

    assertTrue(personIdCopy.isNullable());
    assertFalse(personIdCopy.isUnique());
    assertTrue(recordTableCopy.contains(personIdCopy));
    assertFalse(recordTable.contains(personIdCopy));

    final Column personId = recordTable.getColumn("personId");

    assertNotNull(personId);
    assertEquals("personId", personId.getName());
    assertFalse(personId.isNullable());
    assertTrue(personId.isUnique());

    // try to add a Record with duplicate personId to each record table
    final Record harryToeCopy = getRecordInstance((Column[]) recordTableCopy.getColumns().toArray(new Column[recordTableCopy.columnCount()]),
      new Object[]{new Integer(303), "Harry", "Toe", null});

    assertNotNull(harryToeCopy);
    assertFalse(recordTableCopy.contains(harryToeCopy));

    try {
      assertTrue(recordTableCopy.addRow(harryToeCopy));
    }
    catch (NonUniqueColumnValueException e) {
      fail("Adding Record Harry Toe with duplicate personId (303) to the record table copy should not have thrown a NonUniqueColumnValueException!");
    }

    assertTrue(recordTableCopy.contains(harryToeCopy));

    final com.cp.common.util.record.Record goldFinger = getRecordInstance((Column[]) recordTable.getColumns().toArray(new Column[recordTable.columnCount()]),
      new Object[]{new Integer(505), "Gold", "Finger", null});

    assertNotNull(goldFinger);
    assertFalse(recordTable.contains(goldFinger));

    try {
      recordTable.addRow(goldFinger);
      fail("Adding Record Gold Finger with duplicate personId (505) should have thrown a NonUniqueColumnValueException!");
    }
    catch (NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(goldFinger));

    // add column to record table and remove column from record table copy
    final Column dob = columns[3];
    final Column ssn = getColumnInstance("ssn", String.class, true, 11, true);

    assertNotNull(ssn);
    assertEquals("dob", dob.getName());
    assertTrue(recordTable.contains(dob));
    assertFalse(recordTable.contains(ssn));
    assertTrue(recordTableCopy.contains(dob));
    assertFalse(recordTableCopy.contains(ssn));
    assertTrue(recordTable.addColumn(ssn)); // add ssn Column to record table
    assertEquals(3, recordTableCopy.removeColumn(dob)); // remove dob Column from record table copy
    assertTrue(recordTable.contains(dob));
    assertTrue(recordTable.contains(ssn));
    assertFalse(recordTableCopy.contains(dob));
    assertFalse(recordTableCopy.contains(ssn));
    TestUtil.assertNotEquals(recordTable.columnCount(), recordTableCopy.columnCount());
    assertEquals(5, recordTable.columnCount());
    assertEquals(3, recordTableCopy.columnCount());
    TestUtil.assertNotEquals(recordTable.rowCount(), recordTableCopy.rowCount());
    assertEquals(5, recordTable.rowCount());
    assertEquals(4, recordTableCopy.rowCount());
  }

  public void testInsertColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false)
    };

    final Object[][] data = {
      {new Integer(101), "Jack", "Handy", DateUtil.getCalendar(1968, Calendar.MAY, 21)},
      {new Integer(202), "Sandy", "Handy", DateUtil.getCalendar(1971, Calendar.OCTOBER, 2)}
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Column gender = getColumnInstance("gender", Gender.class, true, 0, false);

    assertNotNull(gender);
    assertFalse(recordTable.contains(gender));

    try {
      assertTrue(recordTable.insertColumn(gender, recordTable.getLastColumnIndex()));
    }
    catch (Exception e) {
      fail("Inserting Column gender should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertEquals(3, recordTable.getColumnIndex(gender));
    assertEquals(5, recordTable.columnCount());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertNull(it.next().getValue(gender));
    }

    final Column race = getColumnInstance("race", Race.class, false, 0, false, Race.WHITE);

    assertNotNull(race);
    assertFalse(recordTable.contains(race));

    try {
      assertTrue(recordTable.insertColumn(race, recordTable.getLastColumnIndex()));
    }
    catch (Exception e) {
      fail("Inserting Column race into the recrod table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertEquals(4, recordTable.getColumnIndex(race));
    assertEquals(6, recordTable.columnCount());

    recordTable.clear();

    assertEquals(6, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final Column ssn = getColumnInstance("ssn", String.class, false, 11, true);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.insertColumn(ssn, 0);
    }
    catch (Exception e) {
      fail("Inserting Column ssn into the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertEquals(0, recordTable.getColumnIndex(ssn));
    assertEquals(7, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns[3], recordTable.getLastColumn());
  }

  public void testInsertColumnExceptionFlow() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 10, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(101), "Homer", "Simpson" },
      { new Integer(202), "Marge", "Simpson" },
      { new Integer(303), "Bart", "Simpson" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.insertColumn(null, 0);
      fail("Inserting a null Column should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }

    final com.cp.common.util.record.Column ssn = getColumnInstance("ssn", String.class, false, 11, true, "000-00-0000");

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.insertColumn(ssn, -1);
      fail("Inserting the ssn Column @ a -1 index should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(columns.length, recordTable.columnCount());

    try {
      recordTable.insertColumn(ssn, recordTable.columnCount() + 1);
      fail("Inserting the ssn Column 1 index past the last valid column index in the record table should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(columns.length, recordTable.columnCount());

    final Column firstName = getColumnInstance("firstName", String.class);

    assertNotNull(firstName);
    assertFalse(recordTable.contains(firstName));

    try {
      recordTable.insertColumn(firstName, recordTable.columnCount());
      fail("Inserting the firstName Column into the record table should have thrown an IllegalArgumentException for duplicate column name!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(firstName));
    assertEquals(columns.length, recordTable.columnCount());

    try {
      recordTable.insertColumn(ssn, recordTable.columnCount());
      fail("Inserting the ssn Column into the record table should have thrown an IllegalStateExcption for being non-nullable and unique!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(columns.length, recordTable.columnCount());
    assertFalse(ssn.isNullable());
    assertTrue(ssn.isUnique());
    assertEquals("000-00-0000", ssn.getDefaultValue());

    ssn.setUnique(false);
    ssn.setDefaultValue(null);

    assertFalse(ssn.isUnique());
    assertNull(ssn.getDefaultValue());
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.insertColumn(ssn, 0);
      fail("Inserting Column ssn into the record table should have thrown an IllegalStateException for being non-nullable with no default value!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }
  }

  public void testInsertRow() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 10, true),
      getColumnInstance("firstName", String.class, false, 30, false),
      getColumnInstance("lastName", String.class, false, 50, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, false, 0, false, Race.WHITE)
    };

    final Object[][] data = {
      { new Integer(101), "Jack", "Handy", DateUtil.getCalendar(1967, Calendar.DECEMBER, 5), Gender.MALE, Race.ASIAN },
      { new Integer(202), "Sandy", "Handy", DateUtil.getCalendar(1972, Calendar.MAY, 13), Gender.FEMALE, Race.ASIAN }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record jonDoe = getRecordInstance(columns, new Object[] { new Integer(303), "Jon", "Doe", null, Gender.MALE, null });

    assertNotNull("The Record Jon Doe was null!", jonDoe);
    assertFalse("The record table contains Record Jon Doe!", recordTable.contains(jonDoe));

    try {
      assertTrue("Failed to insert Record Jon Doe into record table @ index 0!", recordTable.insertRow(jonDoe, 0));
    }
    catch (Exception e) {
      fail("Inserting Record Jon Doe into the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertNull(jonDoe.setValue(columns[5], Race.WHITE)); // race
    assertTrue("The record table does not contain Record Jon Doe!", recordTable.contains(jonDoe));
    assertEquals("The row index of Record Jon Doe in the record table was not 0!", 0, recordTable.getRowIndex(jonDoe));
    assertEquals(jonDoe, recordTable.getRow(0));
    assertNotSame(jonDoe, recordTable.getRow(0));
    assertEquals("The record table column count does not equal columns length!", columns.length, recordTable.columnCount());
    assertEquals("The number of rows in the record table did not equal 3!", 3, recordTable.rowCount());

    final Column ssn = getColumnInstance("ssn", String.class, true, 11, true);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      assertTrue(recordTable.addColumn(ssn));
    }
    catch (Exception e) {
      fail("Adding Column ssn to the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertTrue(recordTable.contains(ssn));
    assertEquals(7, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    final Record janeDoe = getRecordInstance(recordTable.getColumns().toArray(new Column[recordTable.columnCount()]),
      new Object[] { new Integer(404), "Jane", "Doe", null, Gender.FEMALE, null, "111-11-1111" });

    assertNotNull(janeDoe);
    assertFalse(recordTable.contains(janeDoe));

    try {
      assertTrue(recordTable.insertRow(janeDoe, 1));
    }
    catch (Exception e) {
      fail("Inserting Record Jane Doe into the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertNull(janeDoe.setValue(columns[5], Race.WHITE)); // race
    assertTrue(recordTable.contains(janeDoe));
    assertEquals(1, recordTable.getRowIndex(janeDoe));
    assertEquals(7, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    final Record jamieFoxx = getRecordInstance(recordTable.getColumns().toArray(new Column[recordTable.columnCount()]),
      new Object[] { new Integer(505), "Jamie", "Foxx", DateUtil.getCalendar(1969, Calendar.APRIL, 14), Gender.FEMALE, Race.BLACK, null });

    assertNotNull(jamieFoxx);
    assertFalse(recordTable.contains(jamieFoxx));

    try {
      assertTrue(recordTable.insertRow(jamieFoxx, recordTable.rowCount()));
    }
    catch (Exception e) {
      fail("Inserting Record Jamie Foxx into the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertTrue(recordTable.contains(jamieFoxx));
    assertEquals(recordTable.getLastRowIndex(), recordTable.getRowIndex(jamieFoxx));
    assertEquals(7, recordTable.columnCount());
    assertEquals(5, recordTable.rowCount());
  }

  public void testInsertRowExceptionFlow() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 10, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, false, 0, false, Race.WHITE)
    };

    final Object[][] data = {
      { new Integer(101), "Susan", "Anthony", DateUtil.getCalendar(1955, Calendar.NOVEMBER, 11), Gender.FEMALE, Race.WHITE }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record fredMeyer = getRecordInstance(columns, new Object[] { new Integer(202), "Fred", "Meyer", null, Gender.MALE, Race.WHITE });

    assertNotNull(fredMeyer);
    assertFalse(recordTable.contains(fredMeyer));

    try {
      recordTable.insertRow(fredMeyer, -1);
      fail("Inserting Record Fred Meyer @ index of -1 into the record table should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(fredMeyer));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.insertRow(fredMeyer, recordTable.rowCount() + 1);
      fail("Inserting Record Fred Meyer @ index of 1 past the last valid row index into the record table should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(fredMeyer));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertTrue(recordTable.isMutable());

    recordTable.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertFalse(recordTable.isMutable());

    final Record susanAnthony = getRecordInstance(columns, data[0]);

    assertNotNull(susanAnthony);
    assertTrue(recordTable.contains(susanAnthony));

    try {
      recordTable.insertRow(susanAnthony, 0);
      fail("Inserting Record Susan Anthony into an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(susanAnthony));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertFalse(recordTable.isMutable());

    recordTable.accept(new MutableVisitor(Mutable.MUTABLE));

    assertTrue(recordTable.isMutable());

    try {
      recordTable.insertRow(susanAnthony, 0);
      fail("Inserting Record Susan Anthony into the record table should have thrown a NonUniqueColumnValueException!");
    }
    catch (NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(susanAnthony));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertFalse(recordTable.contains((com.cp.common.util.record.Record) null));

    try {
      recordTable.insertRow(null, 0);
      fail("Inserting a null Record into the record table should have thrown a IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains((com.cp.common.util.record.Record) null));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final com.cp.common.util.record.Column ssn = getColumnInstance("ssn", String.class, true, 11, true);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    // ssn, firstName, lastName, dob, gender, race
    final Record ryanSanders = getRecordInstance(new Column[] { ssn, columns[1], columns[2], columns[3], columns[4], columns[5] },
      new Object[] { "333-22-4444", "Ryan", "Sanders", DateUtil.getCalendar(1950, Calendar.JUNE, 29), Gender.MALE, null });

    assertNotNull(ryanSanders);
    assertFalse(recordTable.contains(ryanSanders));

    try {
      recordTable.insertRow(ryanSanders, 0);
      fail("Inserting Record Ryan Sanders into the record table with incompatible columns should have thrown an IncomaptibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ryanSanders));
    assertFalse(recordTable.contains(ssn));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record thomasEdison = getRecordInstance(columns, new Object[] { new Integer(222), "Thomas", "Edison", null, "male", null });

    assertNotNull(thomasEdison);
    assertFalse(recordTable.contains(thomasEdison));

    try {
      recordTable.insertRow(thomasEdison, 0);
      fail("Inserting Record Thomas Edison into record table with an invalid String type for gender should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(thomasEdison));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record nullNull = getRecordInstance(columns, new Object[] { new Integer(222), null, null, null, null, null });

    assertNotNull(nullNull);
    assertFalse(recordTable.contains(nullNull));

    try {
      recordTable.insertRow(nullNull, 0);
      fail("Inserting Record nullNull into record table with null first and last names should have thrown an NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(nullNull));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final com.cp.common.util.record.Record albertEinstein = getRecordInstance(columns, new Object[] { new Integer(222), "Albert", "EinsteinWithAVeryLongLastNameExceedingMaxSize", null, Gender.MALE, null });

    assertNotNull(albertEinstein);
    assertFalse(recordTable.contains(albertEinstein));

    try {
      recordTable.insertRow(albertEinstein, 0);
      fail("Inserting Record Albert Einstein into record table with lastName of 'EinsteinWithAVeryLongLastNameExceedingMaxSize' should have thrown an InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(albertEinstein));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testMutability() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false)
    };

    final Object[][] data = {
      { new Integer(1), "Joe", "Blow", DateUtil.getCalendar(1968, Calendar.MAY, 12) },
      { new Integer(2), "Gonzo", "Gonzalas", DateUtil.getCalendar(1947, Calendar.OCTOBER, 1) },
      { new Integer(3), "Rip", "Vandike", DateUtil.getCalendar(1977, Calendar.APRIL, 22) }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertTrue(recordTable.isMutable());

    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertTrue(it.next().isMutable());
    }

    recordTable.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertFalse(recordTable.isMutable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertFalse(it.next().isMutable());
    }

    // assert adding a Record/row
    final com.cp.common.util.record.Record benDover = getRecordInstance(columns, new Object[] { new Integer(4), "Ben", "Dover", null });

    assertNotNull(benDover);
    assertFalse(recordTable.contains(benDover));

    try {
      recordTable.addRow(benDover);
      fail("Adding Record Ben Dover to an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expecte behavior!
    }

    assertFalse(recordTable.contains(benDover));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // assert removing a Record/row
    final Record joeBlow = recordTable.getRow(0);

    assertNotNull(joeBlow);
    assertEquals("Joe", joeBlow.getValue(columns[1])); // firstName
    assertEquals("Blow", joeBlow.getValue(columns[2])); // lastName
    assertTrue(recordTable.contains(joeBlow));

    try {
      recordTable.removeRow(joeBlow);
      fail("Removing Record Joe Blow from an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(joeBlow));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // assert adding a Column
    final Column gender = getColumnInstance("gender", Gender.class, true, 0, false);

    assertNotNull(gender);
    assertFalse(recordTable.contains(gender));

    try {
      recordTable.addColumn(gender);
      fail("Adding Column gender to an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(gender));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // assert removing a Column
    final Column dob = recordTable.getColumn("dob");

    assertNotNull("Column dob was null!", dob);
    assertEquals("dob", dob.getName());
    assertTrue(recordTable.contains(dob));

    try {
      recordTable.removeColumn(dob);
      fail("Removing Column dob from an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(dob));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // assert changing Column dob's properties
    assertFalse(dob.isUnique());

    try {
      dob.setUnique(true);
      fail("Setting Column dob to unique in an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expectec behavior!
    }

    assertFalse(dob.isUnique());

    // assert modifying the record table contents using setCellValue
    assertEquals("Blow", recordTable.getCellValue(recordTable.getRowIndex(joeBlow), columns[2])); // lastName

    try {
      recordTable.setCellValue(recordTable.getRowIndex(joeBlow), columns[2], "Hoe"); // lastName
      fail("Setting table cell Joe Blow's lastName to 'Hoe' in an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertEquals("Blow", recordTable.getCellValue(recordTable.getRowIndex(joeBlow), columns[2])); // lastName
    assertEquals("Blow", joeBlow.getValue(columns[2])); // lastName

    // assert modifying the record table contents using Record.setValue
    final com.cp.common.util.record.Record ripVandike = recordTable.getRow(recordTable.getLastRowIndex());

    assertNotNull(ripVandike);
    assertEquals("Rip", ripVandike.getValue(columns[1])); // firstName
    assertEquals("Vandike", ripVandike.getValue(columns[2])); // lastName
    assertEquals(DateUtil.getCalendar(1977, Calendar.APRIL, 22), ripVandike.getValue(dob));

    try {
      ripVandike.setValue(dob, null);
      fail("Setting Record Rip Vandike's dob to null in an immutable record table should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertNotNull(ripVandike.getValue(dob));
    assertEquals(DateUtil.getCalendar(1977, Calendar.APRIL, 22), ripVandike.getValue(dob));
    assertFalse(recordTable.isMutable());

    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertFalse(it.next().isMutable());
    }

    recordTable.accept(new MutableVisitor(Mutable.MUTABLE));

    assertTrue(recordTable.isMutable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertTrue(it.next().isMutable());
    }

    // assert adding Record Ben Dover
    assertNotNull(benDover);
    assertFalse(recordTable.contains(benDover));

    try {
      assertTrue(recordTable.addRow(benDover));
    }
    catch (ObjectImmutableException e) {
      fail("Adding Record Ben Dover to a mutable record table should not have thrown an ObjectImmutableException!");
    }

    assertTrue(recordTable.contains(benDover));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    // assert modifying a cell value
    assertNotNull(ripVandike.getValue(dob));

    try {
      assertEquals(DateUtil.getCalendar(1977, Calendar.APRIL, 22), ripVandike.setValue(dob, null));
    }
    catch (ObjectImmutableException e) {
      fail("Setting Record Rip Vandike's dob to null in a mutable record table should not have thrown an ObjectImmtuableException!");
    }

    assertNull(ripVandike.getValue(dob));
    assertNull(recordTable.getCellValue(recordTable.getRowIndex(ripVandike), dob));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    // assert removing Column dob
    assertTrue(recordTable.contains(dob));

    try {
      assertEquals(3, recordTable.removeColumn(dob));
    }
    catch (ObjectImmutableException e) {
      fail("Removing Column dob from a mutable record table should not have thrown an ObjectImmutableException!");
    }

    assertFalse(recordTable.contains(dob));
    assertNull(recordTable.getColumn("dob"));
    assertEquals(3, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());
  }

  public void testRecordCompatibility() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dateOfBirth", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, false, 0, false, Race.WHITE),
      getColumnInstance("ssn", String.class, true, 0, true)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertFalse(recordTable.contains((com.cp.common.util.record.Record) null));

    try {
      recordTable.addRow(null);
      fail("Adding a null Record into the record table should have thrown a IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains((com.cp.common.util.record.Record) null));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    // personId, firstName, lastName, dateOfBirth, gender, race
    final Record bobDole = getRecordInstance(new com.cp.common.util.record.Column[] { columns[0], columns[1], columns[2], columns[3], columns[4], columns[5] },
      new Object[] { new Integer(1), "Bob", "Dole", DateUtil.getCalendar(1955, Calendar.NOVEMBER, 22), Gender.MALE, null });

    assertNotNull(bobDole);
    assertFalse(recordTable.contains(bobDole));

    try {
      recordTable.insertRow(bobDole, 0);
      fail("Inserting Record Bob Dole into the record table should have thrown a IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(bobDole));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final Column[] incompatibleColumns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, true, 0, false),
      getColumnInstance("socialSecurityNumber", String.class, true, 0, true)
    };

    final com.cp.common.util.record.Record mrSmith = getRecordInstance(incompatibleColumns, new Object[] { new Integer(1), "Mr", "Smith", null, Gender.MALE, Race.WHITE, "333-22-4444" });

    assertNotNull(mrSmith);
    assertFalse(recordTable.contains(mrSmith));

    try {
      recordTable.addRow(mrSmith);
      fail("Adding Record Mr. Smith to the record table should have thrown an IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // exptected behavior!
    }

    assertFalse(recordTable.contains(mrSmith));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final Column ssn = columns[6];

    assertNotNull(ssn);
    assertEquals("ssn", ssn.getName());
    assertTrue(recordTable.contains(ssn));
    assertEquals(6, recordTable.removeColumn(ssn));
    assertFalse(recordTable.contains(ssn));
    assertFalse(recordTable.contains(bobDole));

    try {
      assertTrue(recordTable.addRow(bobDole));
    }
    catch (Exception e) {
      fail("Adding Record Bob Dole to the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertNull(bobDole.setValue(columns[5], columns[5].getDefaultValue())); // race
    assertEquals(Race.WHITE, bobDole.getValue(columns[5])); // race
    assertTrue(recordTable.contains(bobDole));
    assertEquals(6, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());

    final Record mrsSmith = getRecordInstance(columns, new Object[] { new Integer(2), "Mrs", "Smith", DateUtil.getCalendar(1969, Calendar.APRIL, 1), Gender.FEMALE, null, "111-22-3333" });

    assertNotNull(mrsSmith);
    assertFalse(recordTable.contains(mrsSmith));

    try {
      recordTable.insertRow(mrsSmith, recordTable.rowCount());
      fail("Inserting Record Mrs. Smith into the record table should have thrown an IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(mrsSmith));
    assertEquals(6, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
  }

  public void testSearchRecordTable() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false)
    };

    final Object[][] data = {
      { new Integer(101), "Jack", "Handy", DateUtil.getCalendar(1971, Calendar.SEPTEMBER, 20) },
      { new Integer(202), "Jenny", "Park", DateUtil.getCalendar(1982, Calendar.MARCH, 2) },
      { new Integer(303), "Stan", "Cooper", DateUtil.getCalendar(1955, Calendar.OCTOBER, 14) },
      { new Integer(404), "Jon", "Doe", DateUtil.getCalendar(1963, Calendar.JUNE, 12) },
      { new Integer(505), "Ryan", "Sanders", DateUtil.getCalendar(1945, Calendar.OCTOBER, 7) },
      { new Integer(606), "Cory", "Adams", DateUtil.getCalendar(1976, Calendar.JULY, 16) },
      { new Integer(707), "Jane", "Doe", DateUtil.getCalendar(1973, Calendar.FEBRUARY, 29) },
      { new Integer(808), "Sara", "Smith", DateUtil.getCalendar(1986, Calendar.DECEMBER, 30) },
      { new Integer(909), "Sandy", "Handy", DateUtil.getCalendar(1999, Calendar.APRIL, 1) }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // Search for all Records/rows whose firstName begins with the letter 'J'.
    SearchFilter searchFilter = new SearchFilter() {
      public boolean matches(final Object obj) throws SearchException {
        try {
          final com.cp.common.util.record.Record record = (Record) obj;
          return ((String) record.getValue(columns[1])).startsWith("J"); // firstName
        }
        catch (NoSuchFieldException ignore) {
          return false;
        }
      }
    };

    Searcher searcher = new com.cp.common.util.search.LinearSearch(searchFilter);

    Set<Integer> expectedResultSet = new HashSet<Integer>();
    expectedResultSet.add(new Integer(101));
    expectedResultSet.add(new Integer(202));
    expectedResultSet.add(new Integer(404));
    expectedResultSet.add(new Integer(707));

    Collection<com.cp.common.util.record.Record> actualResultSet = (Collection<Record>) searcher.search(recordTable);

    assertNotNull(actualResultSet);
    assertEquals(expectedResultSet.size(), actualResultSet.size());

    for (Iterator<Record> it = actualResultSet.iterator(); it.hasNext();) {
      assertTrue(expectedResultSet.contains(it.next().getValue(columns[0]))); // personId
    }

    // Search for all Records/rows who's lastName is "Doe".
    searchFilter = new SearchFilter() {
      public boolean matches(final Object obj) throws SearchException {
        try {
          final com.cp.common.util.record.Record record = (Record) obj;
          return "Doe".equals(record.getValue(columns[2])); // lastName
        }
        catch (NoSuchFieldException ignore) {
          return false;
        }
      }
    };

    searcher = new LinearSearch(searchFilter);

    expectedResultSet = new HashSet<Integer>();
    expectedResultSet.add(new Integer(404));
    expectedResultSet.add(new Integer(707));

    actualResultSet = (Collection<Record>) searcher.search(recordTable);

    assertNotNull(actualResultSet);
    assertEquals(expectedResultSet.size(), actualResultSet.size());

    for (Iterator<Record> it = actualResultSet.iterator(); it.hasNext();) {
      assertTrue(expectedResultSet.contains(it.next().getValue(columns[0]))); // personId
    }

    // Search for all Records/rows who's dob is after 1975.
    searchFilter = new SearchFilter() {
      public boolean matches(final Object obj) throws SearchException {
        try {
          final com.cp.common.util.record.Record record = (Record) obj;
          return record.getCalendarValue(columns[3]).get(Calendar.YEAR) > 1975; // dob
        }
        catch (NoSuchFieldException ignore) {
          return false;
        }
      }
    };

    searcher = new LinearSearch(searchFilter);

    expectedResultSet = new HashSet<Integer>();
    expectedResultSet.add(new Integer(202));
    expectedResultSet.add(new Integer(606));
    expectedResultSet.add(new Integer(808));
    expectedResultSet.add(new Integer(909));

    actualResultSet = (Collection<Record>) searcher.search(recordTable);

    assertNotNull(actualResultSet);
    assertEquals(expectedResultSet.size(), actualResultSet.size());

    for (Iterator<Record> it = actualResultSet.iterator(); it.hasNext();) {
      assertTrue(expectedResultSet.contains(it.next().getValue(columns[0]))); // personId
    }

    // Search all Records/rows who's firstName begins with 'S' and are older than 21.
    searchFilter = new SearchFilter() {
      public boolean matches(final Object obj) throws SearchException {
        try {
          final Record record = (Record) obj;
          final int age = DateUtil.getDiffInYears(record.getCalendarValue(columns[3])); // dob
          return (record.getStringValue(columns[1]).startsWith("S") && age >= 21);
        }
        catch (NoSuchFieldException e) {
          return false;
        }
      }
    };

    searcher = new com.cp.common.util.search.LinearSearch(searchFilter);

    expectedResultSet = new HashSet<Integer>();
    expectedResultSet.add(new Integer(303));

    actualResultSet = (Collection<Record>) searcher.search(recordTable);

    assertNotNull(actualResultSet);
    assertEquals(expectedResultSet.size(), actualResultSet.size());

    for (Iterator<com.cp.common.util.record.Record> it = actualResultSet.iterator(); it.hasNext(); ) {
      assertTrue(expectedResultSet.contains(it.next().getIntegerValue(columns[0])));
    }
  }

  public void testSetColumnToNonNull() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, true, 25, false),
      getColumnInstance("lastName", String.class, true, 35, false),
      getColumnInstance("dob", Calendar.class,  true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, true, 0, false, Race.WHITE),
      getColumnInstance("ssn", String.class, true, 11, true)
    };

    final Object[][] data = {
      { new Integer(1), "Jack", "Handy", null, Gender.MALE, null, "111-11-1111" },
      { new Integer(2), "Null", "Null", null, null, null, "222-22-2222" },
      { new Integer(3), "Sandy", "Handy", null, Gender.FEMALE, null, "333-33-3333" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      final Record dataRecord = getRecordInstance(columns, data[rowIndex]);
      dataRecord.setValue(columns[5], Race.WHITE); // race
      assertEquals(dataRecord, it.next());
    }

    final Column firstName = recordTable.getColumn("firstName");
    final com.cp.common.util.record.Column lastName = recordTable.getColumn("lastName");

    assertNotNull(firstName);
    assertNotNull(lastName);
    assertTrue(firstName.isNullable());
    assertTrue(lastName.isNullable());

    try {
      firstName.setNullable(false);
      lastName.setNullable(false);
    }
    catch (IllegalStateException e) {
      fail("Setting Columns firstName & lastName to non-nullable should not have thrown an IllegalStateException!");
    }

    assertFalse(firstName.isNullable());
    assertFalse(lastName.isNullable());

    rowIndex = 0;
    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      final Record tableRecord = it.next();
      assertEquals(data[rowIndex][1], tableRecord.getValue(firstName));
      assertEquals(data[rowIndex][2], tableRecord.getValue(lastName));
    }

    final Column gender = recordTable.getColumn("gender");

    assertNotNull(gender);
    assertTrue(gender.isNullable());

    try {
      gender.setNullable(false);
      fail("Setting Column gender to non-nullable should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertTrue(gender.isNullable());
    assertNull(gender.getDefaultValue());

    gender.setDefaultValue(Gender.MALE);

    assertEquals(Gender.MALE, gender.getDefaultValue());

    try {
      gender.setNullable(false);
    }
    catch (IllegalStateException e) {
      fail("Setting Column gender to non-nullable with a default value of Male should not have thrown an IllegalStateException!");
    }

    assertFalse(gender.isNullable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertNotNull(it.next().getValue(gender));
    }

    final com.cp.common.util.record.Column race = recordTable.getColumn("race");

    assertNotNull(race);
    assertTrue(race.isNullable());

    try {
      race.setNullable(false);
    }
    catch (IllegalStateException e) {
      fail("Setting Column race to non-nullable should not have thrown an IllegalStateException!");
    }

    assertFalse(race.isNullable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertNotNull(it.next().getValue(race));
    }

    final Column ssn = recordTable.getColumn("ssn");

    assertNotNull(ssn);
    assertTrue(ssn.isNullable());

    try {
      ssn.setNullable(false);
    }
    catch (IllegalStateException e) {
      fail("Setting Column ssn to non-nullable should not have an IllegalStateException!");
    }

    assertFalse(ssn.isNullable());

    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertNotNull(it.next().getValue(ssn));
    }

    try {
      recordTable.setCellValue(0, ssn, null);
      fail("Setting the ssn Column value for row 0 to null should have thrown a NullColumnValueException!");
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      // expected behavior!
    }
  }

  public void testSetColumnToUnique() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, false),
      getColumnInstance("firstName", String.class, true, 25, false),
      getColumnInstance("lastName", String.class, true, 35, false),
      getColumnInstance("dob", Calendar.class,  true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, true, 0, false, Race.WHITE),
      getColumnInstance("ssn", String.class, true, 11, false)
    };

    final Object[][] data = {
      { new Integer(1), "Jon", "Doe", null, Gender.MALE, Race.BLACK, "111-11-1111" },
      { new Integer(2), "Null", "Null", null, null, null, "222-22-2222" },
      { new Integer(3), "Jane", "Doe", null, Gender.FEMALE, Race.WHITE, "333-33-3333" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      final com.cp.common.util.record.Record dataRecord = getRecordInstance(columns, data[rowIndex]);
      if (ObjectUtil.isNull(dataRecord.getValue(columns[5]))) {
        dataRecord.setValue(columns[5], Race.WHITE);
      }
      assertEquals(dataRecord, it.next());
    }

    final com.cp.common.util.record.Column personId = recordTable.getColumn("personId");

    assertNotNull(personId);
    assertFalse(personId.isUnique());

    try {
      personId.setUnique(true);
    }
    catch (IllegalStateException e) {
      fail("Setting Column personId to unique should not have thrown an IllegalStateException!");
    }

    assertTrue(personId.isUnique());

    final Column lastName = recordTable.getColumn("lastName");

    assertNotNull(lastName);
    assertFalse(lastName.isUnique());

    try {
      lastName.setUnique(true);
      fail("Setting Column lastName to unique should have thrown a IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(lastName.isUnique());

    final Column dob = recordTable.getColumn("dob");

    assertNotNull(dob);
    assertFalse(dob.isUnique());

    try {
      dob.setUnique(true);
    }
    catch (IllegalStateException e) {
      fail("Setting Column dob to unique should not have thrown an IllegalStateException!");
    }

    assertTrue(dob.isUnique());

    final com.cp.common.util.record.Column gender = recordTable.getColumn("gender");

    assertNotNull(gender);
    assertFalse(gender.isUnique());
    assertNull(gender.getDefaultValue());

    gender.setDefaultValue(Gender.FEMALE);

    assertEquals(Gender.FEMALE, gender.getDefaultValue());

    try {
      gender.setUnique(true);
    }
    catch (IllegalStateException e) {
      fail("Setting Column gender to unique should not have thrown an IllegalStateException!");
    }

    assertTrue(gender.isUnique());

    final Column race = recordTable.getColumn("race");

    assertNotNull(race);
    assertFalse(race.isUnique());

    try {
      race.setUnique(true);
      fail("Setting Column race to unique should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(race.isUnique());

    final Column ssn = recordTable.getColumn("ssn");

    assertNotNull(ssn);
    assertFalse(ssn.isUnique());

    try {
      ssn.setUnique(true);
    }
    catch (IllegalStateException e) {
      fail("Setting Column ssn to unique should not have thrown an IllegalStateException!");
    }

    assertTrue(ssn.isUnique());

    try {
      for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); ) {
        it.next().setValue(ssn, null);
      }
    }
    catch (Exception e) {
      fail("Setting ssn Column value to null for all rows in the record table, although unique, should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSortRecordTable() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false)
    };

    final Object[][] data = {
      { new Integer(101), "Jack", "Handy", DateUtil.getCalendar(1971, Calendar.SEPTEMBER, 20) },
      { new Integer(202), "Jenny", "Park", DateUtil.getCalendar(1982, Calendar.MARCH, 2) },
      { new Integer(303), "Stan", "Cooper", DateUtil.getCalendar(1955, Calendar.OCTOBER, 14) },
      { new Integer(404), "Jon", "Doe", DateUtil.getCalendar(1963, Calendar.JUNE, 12) },
      { new Integer(505), "Ryan", "Sanders", DateUtil.getCalendar(1945, Calendar.OCTOBER, 7) },
      { new Integer(606), "Cory", "Adams", DateUtil.getCalendar(1976, Calendar.JULY, 16) },
      { new Integer(707), "Jane", "Doe", DateUtil.getCalendar(1973, Calendar.FEBRUARY, 29) },
      { new Integer(808), "Sara", "Smith", DateUtil.getCalendar(1986, Calendar.DECEMBER, 30) },
      { new Integer(909), "Sandy", "Handy", DateUtil.getCalendar(1999, Calendar.APRIL, 1) }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // order by (sort) by lastName first, firstName last
    Comparator<Record> sortComparator = new Comparator<Record>() {
      public int compare(final Record rec1, final com.cp.common.util.record.Record rec2) {
        int compareValue = rec1.getStringValue(2).compareTo(rec2.getStringValue(2));
        return (!NumberUtil.isZero(compareValue) ? compareValue : rec1.getStringValue(1).compareTo(rec2.getStringValue(1)));
      }
    };

    com.cp.common.util.sort.Sorter sorter = new com.cp.common.util.sort.QuickSort(sortComparator);

    List expectedResultList = new ArrayList();
    expectedResultList.add(new Integer(606));
    expectedResultList.add(new Integer(303));
    expectedResultList.add(new Integer(707));
    expectedResultList.add(new Integer(404));
    expectedResultList.add(new Integer(101));
    expectedResultList.add(new Integer(909));
    expectedResultList.add(new Integer(202));
    expectedResultList.add(new Integer(505));
    expectedResultList.add(new Integer(808));

    sorter.sort(recordTable);

    int index = 0;
    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); index++) {
      assertEquals(expectedResultList.get(index), it.next().getValue(columns[0]));
    }

    // order by (sort) by birtdate, chronological order
    sortComparator = new Comparator<Record>() {
      final Comparator calendarComaprator = DateUtil.getCalendarComparator();
      public int compare(final com.cp.common.util.record.Record rec1, final Record rec2) {
        return calendarComaprator.compare(rec1.getValue(3), rec2.getValue(3));
      }
    };

    sorter = new com.cp.common.util.sort.InsertionSort(sortComparator);

    expectedResultList = new ArrayList();
    expectedResultList.add(new Integer(505));
    expectedResultList.add(new Integer(303));
    expectedResultList.add(new Integer(404));
    expectedResultList.add(new Integer(101));
    expectedResultList.add(new Integer(707));
    expectedResultList.add(new Integer(606));
    expectedResultList.add(new Integer(202));
    expectedResultList.add(new Integer(808));
    expectedResultList.add(new Integer(909));

    sorter.sort(recordTable);

    index = 0;
    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); index++) {
      assertEquals(expectedResultList.get(index), it.next().getValue(columns[0]));
    }
  }

}
