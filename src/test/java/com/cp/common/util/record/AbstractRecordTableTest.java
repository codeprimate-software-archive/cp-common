/*
 * AbstractRecordTableTest.java (c) 31 March 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.util.record.AbstractRecordTable
 * @see junit.framework.TestCase
 */

package com.cp.common.util.record;

import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.support.MutableVisitor;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.DateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

public class AbstractRecordTableTest extends TestCase {

  public AbstractRecordTableTest(final String testName) {
    super(testName);
  }

  protected com.cp.common.util.record.Column getColumnInstance(final String name, final Class type) {
    return new com.cp.common.util.record.ColumnImpl(name, type);
  }

  protected Column getColumnInstance(final String name,
                                     final Class type,
                                     final boolean nullable,
                                     final int size,
                                     final boolean unique) {
    final com.cp.common.util.record.Column column = getColumnInstance(name, type);
    column.setNullable(nullable);
    column.setUnique(unique);
    column.setSize(size);
    return column;
  }

  protected Column getColumnInstance(final String name,
                                     final Class type,
                                     final boolean nullable,
                                     final int size,
                                     final boolean unique,
                                     final Object defaultValue) {
    final com.cp.common.util.record.Column column = getColumnInstance(name, type, nullable, size, unique);
    column.setDefaultValue(defaultValue);
    return column;
  }

  protected Record getRecordInstance() {
    return getRecordInstance(new com.cp.common.util.record.Column[0], new Object[0]);
  }

  protected Record getRecordInstance(final com.cp.common.util.record.Column[] columns) {
    return getRecordInstance(columns, new Object[columns.length]);
  }

  protected Record getRecordInstance(final com.cp.common.util.record.Column[] columns, final Object[] data) {
    final Record record = com.cp.common.util.record.AbstractRecordFactory.getInstance().getRecordInstance();
    for (int index = 0; index < columns.length; index++) {
      record.addField(columns[index].getName(), data[index]);
    }
    return record;
  }

  protected RecordTable getRecordTableInstance() {
    return getRecordTableInstance(null);
  }

  protected com.cp.common.util.record.RecordTable getRecordTableInstance(final Column[] columns) {
    return getRecordTableInstance(columns, null);
  }

  protected com.cp.common.util.record.RecordTable getRecordTableInstance(final Column[] columns, final Object[][] data) {
    final RecordTable recordTable = AbstractRecordFactory.getInstance().getRecordTableInstance(columns);
    if (ArrayUtil.isNotEmpty(data)) {
      for (Object[] rowData : data) {
        recordTable.addRow(getRecordInstance(columns, rowData));
      }
    }
    return recordTable;
  }

  protected String getSsn(final String singleDigitValue) {
    final StringBuffer buffer = new StringBuffer(singleDigitValue);
    for (int index = 0; index < 8; index++) {
      buffer.append(singleDigitValue);
    }
    return buffer.toString();
  }

  public void testAccept() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "D", "Doe" },
      { "Jane", "T", "Doe" },
      { "Jack", "R", "Handy" },
      { "Sandy", "P", "Handy" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertTrue(recordTable.isMutable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertTrue(it.next().isMutable());
    }

    recordTable.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertFalse(recordTable.isMutable());

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertFalse(it.next().isMutable());
    }
  }

  public void testAddAll() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Collection<Record> recordCollection = new ArrayList<Record>(4);
    recordCollection.add(getRecordInstance(columns, new Object[] { "Jon", "Doe" }));
    recordCollection.add(getRecordInstance(columns, new Object[] { "Jane", "Doe" }));
    recordCollection.add(getRecordInstance(columns, new Object[] { "Jack", "Handy" }));
    recordCollection.add(getRecordInstance(columns, new Object[] { "Sandy", "Handy" }));

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    recordTable.addAll(recordCollection);

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(recordCollection.size(), recordTable.rowCount());

    for (Iterator<Record> rowIterator = recordTable.rowIterator(), recordIterator = recordCollection.iterator(); recordIterator.hasNext(); ) {
      final com.cp.common.util.record.Record record = recordIterator.next();
      final com.cp.common.util.record.Record row = rowIterator.next();
      assertEquals(record, row);
      assertNotSame(record, row);
    }
  }

  public void testAddColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    // Add some data so that the operation of inserting a column on the table excersizes the logic
    // of adding the field to the Records in the table.
    final Object[][] data = {
      { "Jack", "Handy" },
      { "Sandy", "Handy" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int columnIndex = 0;
    for (Iterator<com.cp.common.util.record.Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      assertEquals(columns[columnIndex], it.next());
    }

    final Column ssnColumn = getColumnInstance("ssn", String.class, true, 9, true, null);

    assertNotNull(ssnColumn);
    assertFalse(recordTable.contains(ssnColumn));
    assertTrue(recordTable.addColumn(ssnColumn));
    assertTrue(recordTable.contains(ssnColumn));
    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(2, recordTable.getColumnIndex(ssnColumn));
    assertEquals(ssnColumn, recordTable.getColumn(2));
    assertEquals(ssnColumn, recordTable.getColumn("ssn"));
    assertNotSame(ssnColumn, recordTable.getColumn("ssn"));

    final Column dobColumn = getColumnInstance("dob", Calendar.class);
    final com.cp.common.util.record.Column genderColumn = getColumnInstance("gender", Gender.class);

    assertNotNull(dobColumn);
    assertNotNull(genderColumn);
    assertFalse(recordTable.contains(dobColumn));
    assertFalse(recordTable.contains(genderColumn));
    assertTrue(recordTable.addColumn(genderColumn));
    assertTrue(recordTable.addColumn(dobColumn));
    assertTrue(recordTable.contains(dobColumn));
    assertTrue(recordTable.contains(genderColumn));
    assertEquals(5, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(3, recordTable.getColumnIndex(genderColumn));
    assertEquals(4, recordTable.getColumnIndex(dobColumn));
    assertEquals(genderColumn, recordTable.getColumn(3));
    assertEquals(dobColumn, recordTable.getColumn(4));
    assertEquals(dobColumn, recordTable.getColumn("dob"));
    assertNotSame(dobColumn, recordTable.getColumn("dob"));
    assertEquals(genderColumn, recordTable.getColumn("gender"));
    assertNotSame(genderColumn, recordTable.getColumn("gender"));

    final com.cp.common.util.record.Column raceColumn = getColumnInstance("race", Race.class, false, 0, false, Race.WHITE);

    assertNotNull(raceColumn);
    assertFalse(recordTable.contains(raceColumn));
    assertTrue(recordTable.addColumn(raceColumn));
    assertTrue(recordTable.contains(raceColumn));
    assertEquals(6, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(5, recordTable.getColumnIndex(raceColumn));
    assertEquals(raceColumn, recordTable.getColumn(5));
    assertEquals(raceColumn, recordTable.getColumn("race"));
    assertNotSame(raceColumn, recordTable.getColumn("race"));

    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      assertEquals(Race.WHITE, it.next().getValue(raceColumn));
    }
  }

  public void testAddColumnExceptionalFlowInvalidColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    // Add some data so that the operation of inserting a column on the table excersizes the logic
    // of adding the field to the Records in the table.
    final Object[][] data = {
      { "Jack", "Handy" },
      { "Sandy", "Handy" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add a non-nullable, unique column (ssn) having a default value and rows already in the record table
    final Column ssn = getColumnInstance("ssn", String.class, false, 9, true, "333224444");

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.addColumn(ssn);
      fail("Calling RecordTable.addColumn with a non-nullable, unique column (ssn) having a default value should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add a non-nullable, non-unique column (gender) with a null default value and rows already in the record table
    final com.cp.common.util.record.Column gender = getColumnInstance("gender", Gender.class, false, 0, false, null);

    assertNotNull(gender);
    assertFalse(recordTable.contains(gender));

    try {
      recordTable.addColumn(gender);
      fail("Calling RecordTable.addColumn with a non-nullable, non-unique column (gender) with a null default value should have thrown a IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(gender));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add an existing column (firstName) to the record table
    final Column firstName = getColumnInstance("firstName", String.class);

    assertNotNull(firstName);
    assertTrue(recordTable.contains(firstName));

    try {
      recordTable.addColumn(firstName);
      fail("Calling RecordTable.addColumn with firstName should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(firstName));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // try to add a null column to the record table.
    assertFalse(recordTable.contains((Column) null));

    try {
      recordTable.addColumn(null);
      fail("Calling RecordTable.addColumn with a null column should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains((Column) null));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // try to add a column (lastname, all lower case) to an immutable record table.
    final com.cp.common.util.record.Column lastname = getColumnInstance("lastname", String.class);
    recordTable.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertNotNull(lastname);
    assertFalse(recordTable.isMutable());
    assertFalse(recordTable.contains(lastname));

    try {
      recordTable.addColumn(lastname);
      fail("Calling RecordTable.addColumn on an immutable record table with lastname should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(lastname));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add duplicate column (lastname) with different case to the record table
    recordTable.accept(new MutableVisitor(Mutable.MUTABLE));

    assertTrue(recordTable.isMutable());
    assertFalse(recordTable.contains(lastname));

    try {
      assertTrue(recordTable.addColumn(lastname));
    }
    catch (Exception e) {
      fail("Calling RecordTable.addColumn with column lastname should not have thrown a IllegalArgumentExeption!");
    }

    assertTrue(recordTable.contains(lastname));
    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testAddRow() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jack", "Handy" },
      { "Samantha", "Tate" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    final com.cp.common.util.record.Record tonyMalony = getRecordInstance(columns, new Object[] { "Tony", "Malony" });

    assertNotNull(tonyMalony);
    assertFalse(recordTable.contains(tonyMalony));
    assertTrue(recordTable.addRow(tonyMalony));
    assertTrue(recordTable.contains(tonyMalony));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());
    assertEquals(3, recordTable.getRowIndex(tonyMalony));
    assertEquals(tonyMalony, recordTable.getRow(3));
    assertNotSame(tonyMalony, recordTable.getRow(3));

    final com.cp.common.util.record.Record benDover = getRecordInstance(columns, new Object[] { "Ben", "Dover" });
    final com.cp.common.util.record.Record imaPigg = getRecordInstance(columns, new Object[] { "Ima", "Pigg" });
    final com.cp.common.util.record.Record richardLittle = getRecordInstance(columns, new Object[] { "Richard", "Little" });

    assertNotNull(benDover);
    assertNotNull(imaPigg);
    assertNotNull(richardLittle);
    assertFalse(recordTable.contains(benDover));
    assertFalse(recordTable.contains(imaPigg));
    assertFalse(recordTable.contains(richardLittle));
    assertTrue(recordTable.addRow(richardLittle));
    assertTrue(recordTable.addRow(imaPigg));
    assertTrue(recordTable.addRow(benDover));
    assertTrue(recordTable.contains(benDover));
    assertTrue(recordTable.contains(imaPigg));
    assertTrue(recordTable.contains(richardLittle));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(7, recordTable.rowCount());
    assertEquals(4, recordTable.getRowIndex(richardLittle));
    assertEquals(5, recordTable.getRowIndex(imaPigg));
    assertEquals(6, recordTable.getRowIndex(benDover));
    assertEquals(richardLittle, recordTable.getRow(4));
    assertEquals(imaPigg, recordTable.getRow(5));
    assertEquals(benDover, recordTable.getRow(6));
    assertNotSame(richardLittle, recordTable.getRow(4));
    assertNotSame(imaPigg, recordTable.getRow(5));
    assertNotSame(benDover, recordTable.getRow(6));
  }

  public void testAddRowExceptionalFlowIncompatibleRecord() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class, false, 9, true, null)
    };

    final Object[][] data = {
      { "Jack", "Handy", "111111111" },
      { "Sandy", "Handy", "222222222" },
      { "Jon", "Doe", "333333333" },
      { "Jane", "Doe", "444444444" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add a null record to the record table
    assertFalse(recordTable.contains((com.cp.common.util.record.Record) null));

    try {
      assertFalse(recordTable.addRow(null));
      fail("Calling RecordTable.addRow with a null record should have thrown an IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains((Record) null));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add an incompatible record (wrong number of columns) to the record table
    Record incompatibleRecord = getRecordInstance(new com.cp.common.util.record.Column[] { columns[0], columns[1] },
      new Object[] { "Incompatible", "Record" });

    assertNotNull(incompatibleRecord);
    assertFalse(recordTable.contains(incompatibleRecord));

    try {
      assertFalse(recordTable.addRow(incompatibleRecord));
      fail("Calling RecordTable.addRow with an incompatible record (wrong number of columns) should have thrown an IncompatibleRecordException!");
    }
    catch (com.cp.common.util.record.IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(incompatibleRecord));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add an incompatible record (wrong type of columns) to the record table
    final Column personIdColumn = getColumnInstance("personId", Integer.class, false, 0, true, null);

    assertNotNull(personIdColumn);
    assertFalse(recordTable.contains(personIdColumn));

    incompatibleRecord = getRecordInstance(new com.cp.common.util.record.Column[] { personIdColumn, columns[0], columns[1] },
      new Object[] { new Integer(0), "Incompatible", "Record" });

    assertNotNull(incompatibleRecord);
    assertFalse(recordTable.contains(incompatibleRecord));

    try {
      assertFalse(recordTable.addRow(incompatibleRecord));
      fail("Calling RecordTable.addRow with an incompatible record (wrong types of columns) should have thrown an IncompatibleRecordException!");
    }
    catch (IncompatibleRecordException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(incompatibleRecord));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testAddRowExceptionalFlowInvalidColumnValues() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class, false, 9, true, null)
    };

    final Object[][] data = {
      { "Jack", "Handy", "111111111" },
      { "Sandy", "Handy", "222222222" },
      { "Jon", "Doe", "333333333" },
      { "Jane", "Doe", "444444444" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add malicious hacker (null ssn record) to record table
    final com.cp.common.util.record.Record maliciousHacker = getRecordInstance(columns, new Object[] { "Malicious", "Hacker", null });

    assertNotNull(maliciousHacker);
    assertFalse(recordTable.contains(maliciousHacker));

    try {
      assertFalse(recordTable.addRow(maliciousHacker));
      fail("Calling RecordTable.addRow with record malicious hacker, having a null ssn, should have thrown a NullColumnValueException!");
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(maliciousHacker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add malicious cracker (duplicate ssn record) to record table
    final Record maliciousCraker = getRecordInstance(columns, new Object[] { "Malicious", "Cracker", "222222222" });

    assertNotNull(maliciousCraker);
    assertFalse(recordTable.contains(maliciousCraker));

    try {
      assertFalse(recordTable.addRow(maliciousCraker));
      fail("Calling RecordTable.addRow with record malicious cracker, having duplicate ssn, should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(maliciousCraker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add malicious attacker (invalid ssn value size (10) record) to record table
    final Record maliciousAttacker = getRecordInstance(columns, new Object[] { "Malicious", "Attacker", "5555555555" });

    assertNotNull(maliciousAttacker);
    assertFalse(recordTable.contains(maliciousAttacker));

    try {
      assertFalse(recordTable.addRow(maliciousAttacker));
      fail("Calling RecordTable.addRow with malicious attacker, having an invalid ssn column value size of 10, should have thrown a InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(maliciousAttacker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testClear() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jane", "Doe" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    recordTable.clear();

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());

    recordTable.clear();

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());
  }

  public void testClone() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jane", "Doe" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final RecordTable recordTableClone = (com.cp.common.util.record.RecordTable) ((AbstractRecordTable) recordTable).clone();

    assertNotNull(recordTableClone);
    assertNotSame(recordTable, recordTableClone);
    assertEquals(recordTable.columnCount(), recordTableClone.columnCount());
    assertEquals(recordTable.rowCount(), recordTableClone.rowCount());

    for (Iterator<com.cp.common.util.record.Column> columnIterator = recordTable.columnIterator(), columnCloneIterator = recordTableClone.columnIterator(); columnIterator.hasNext(); ) {
      final Column column = columnIterator.next();
      final com.cp.common.util.record.Column columnClone = columnCloneIterator.next();
      assertEquals(column, columnClone);
      assertNotSame(column, columnClone);
    }

    for (Iterator<Record> rowIterator = recordTable.rowIterator(), rowCloneIterator = recordTableClone.rowIterator(); rowIterator.hasNext(); ) {
      final com.cp.common.util.record.Record row = rowIterator.next();
      final Record rowClone = rowCloneIterator.next();
      assertEquals(row, rowClone);
      assertNotSame(row, rowClone);
    }
  }

  public void testColumnCount() throws Exception {
    RecordTable recordTable = getRecordTableInstance();

    assertNotNull(recordTable);
    assertEquals(0, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.addColumn(getColumnInstance("firstName", String.class)));
    assertTrue(recordTable.addColumn(getColumnInstance("lastName", String.class)));
    assertEquals(2, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(getColumnInstance("lastName", String.class), recordTable.removeColumn(1));
    assertEquals(1, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.addColumn(getColumnInstance("middleInitial", String.class)));
    assertTrue(recordTable.addColumn(getColumnInstance("lastName", String.class)));
    assertTrue(recordTable.addColumn(getColumnInstance("ssn", String.class, false, 9, true, null)));
    assertTrue(recordTable.addColumn(getColumnInstance("dob", Calendar.class, false, 0, false)));
    assertTrue(recordTable.addColumn(getColumnInstance("gender", Gender.class, true, 0, false)));
    assertTrue(recordTable.addColumn(getColumnInstance("race", Race.class, true, 0, false, Race.WHITE)));
    assertEquals(7, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final String[] columnNames = { "firstName", "middleInitial", "lastName", "ssn", "dob", "gender", "race" };

    assertEquals(columnNames.length, recordTable.columnCount());

    int columnIndex = 0;
    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); ) {
      assertEquals(columnNames[columnIndex++], it.next().getName());
    }

    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true, null),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("dob", Calendar.class)
    };

    final Object[][] data = {
      { new Integer(1), "Jon", "Doe", null },
      { new Integer(2), "Jane", "Doe", null }
    };

    recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // try to add ssn column
    final com.cp.common.util.record.Column ssnColumn = getColumnInstance("ssn", String.class, false, 9, true, null);

    assertNotNull(ssnColumn);
    assertFalse(recordTable.contains(ssnColumn));

    try {
      assertFalse(recordTable.addColumn(ssnColumn));
      fail("Add non-nullable, unique ssn column should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssnColumn));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // remove the dob column
    assertTrue(recordTable.contains(columns[3]));
    assertEquals(3, recordTable.getColumnIndex(columns[3]));
    assertEquals(columns[3], recordTable.removeColumn(3));
    assertFalse(recordTable.contains(columns[3]));
    assertEquals(-1, recordTable.getColumnIndex(columns[3]));
    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add the race column
    final Column raceColumn = getColumnInstance("race", Race.class, false, 0, false, Race.WHITE);

    assertNotNull(raceColumn);
    assertFalse(recordTable.contains(raceColumn));

    try {
      assertTrue(recordTable.addColumn(raceColumn));
    }
    catch (Exception e) {
      fail("Adding non-nullable, non-unique race column should not have thrown an Exception!");
    }

    assertTrue(recordTable.contains(raceColumn));
    assertEquals(4, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testContainsAll() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Lynus", "Torvalds" },
      { "James", "Gosling" },
      { "Kent", "Beck" },
      { "Gordon", "Moore" },
      { "Rod", "Johnson" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record[] records = {
      getRecordInstance(columns, new Object[] { "Lynus", "Torvalds" }),
      getRecordInstance(columns, new Object[] { "James", "Gosling" }),
      getRecordInstance(columns, new Object[] { "Kent", "Beck" }),
      getRecordInstance(columns, new Object[] { "Gordon", "Moore" }),
      getRecordInstance(columns, new Object[] { "Rod", "Johnson" })
    };

    assertTrue(recordTable.containsAll(Arrays.<Record>asList(records)));

    final Collection<Record> subList = new ArrayList<com.cp.common.util.record.Record>(3);
    subList.add(records[0]); // Lynus Torvalds
    subList.add(records[2]); // Kent Beck
    subList.add(records[3]); // Gordon Moore

    assertTrue(recordTable.containsAll(subList));
    assertTrue(recordTable.contains(records[1])); // James Gosling
    assertTrue(recordTable.contains(records[4])); // Rod Johnson
    assertEquals(records[1], recordTable.removeRow(1)); // James Gosling
    assertEquals(records[4], recordTable.removeRow(3)); // Rod Johnson, which is @ row index 3, not 4, since we already removed a row.
    assertFalse(recordTable.contains(records[1])); // James Gosling
    assertFalse(recordTable.contains(records[4])); // Rod Johnson
    assertFalse(recordTable.containsAll(Arrays.<Record>asList(records)));
    assertTrue(recordTable.containsAll(subList));
  }

  public void testContainsColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true, null),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("dob", Calendar.class),
      getColumnInstance("ssn", String.class)
    };

    com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    for (com.cp.common.util.record.Column column : columns) {
      assertTrue(recordTable.contains(column));
    }

    assertFalse(recordTable.contains((Column) null));
    assertFalse(recordTable.contains(getColumnInstance("personId", Integer.class, false, 0, true, new Integer(0))));
    assertFalse(recordTable.contains(getColumnInstance("dateOfBirth", Calendar.class)));
    assertFalse(recordTable.contains(getColumnInstance("race", Race.class, true, 0, false, null)));
    assertFalse(recordTable.contains(getColumnInstance("SSN", String.class)));

    final com.cp.common.util.record.Column genderColumn = getColumnInstance("gender", String.class);

    assertNotNull(genderColumn);
    assertFalse(recordTable.contains(genderColumn));
    assertNull(recordTable.getColumn("gender"));
    assertTrue(recordTable.addColumn(genderColumn)); // add column
    assertTrue(recordTable.contains(genderColumn));
    assertNotNull(recordTable.getColumn("gender"));
    assertEquals(genderColumn, recordTable.getColumn("gender"));
    assertNotSame(genderColumn, recordTable.getColumn("gender"));

    final Column personIdColumn = recordTable.getColumn("personId");

    assertNotNull(personIdColumn);
    assertTrue(recordTable.contains(personIdColumn));
    assertEquals(personIdColumn, recordTable.getColumn("personId"));
    assertSame(personIdColumn, recordTable.getColumn("personId"));
    assertEquals(0, recordTable.removeColumn(personIdColumn)); // remove column
    assertFalse(recordTable.contains(personIdColumn));
    assertNull(recordTable.getColumn("personId"));

    final com.cp.common.util.record.RecordTable recordTableTwo = getRecordTableInstance();

    assertNotNull(recordTableTwo);
    assertEquals(0, recordTableTwo.columnCount());
    assertEquals(0, recordTableTwo.rowCount());

    for (Column column : columns) {
      assertFalse(recordTableTwo.contains(column));
    }

    assertTrue(recordTableTwo.addColumn(columns[0])); // add personId
    assertEquals(1, recordTableTwo.columnCount());
    assertEquals(0, recordTableTwo.rowCount());
    assertTrue(recordTableTwo.contains(columns[0])); // personId
    assertEquals(columns[0], recordTableTwo.getColumn(0));
    assertEquals(columns[0], recordTableTwo.getColumn("personId"));
    assertNotSame(columns[0], recordTableTwo.getColumn("personId"));
    assertFalse(recordTableTwo.contains(columns[1])); // firstName
    assertTrue(recordTableTwo.addColumn(columns[2])); // lastName
    assertTrue(recordTableTwo.addColumn(columns[4])); // ssn
    assertEquals(3, recordTableTwo.columnCount());
    assertEquals(0, recordTableTwo.rowCount());

    for (int columnIndex = 0; columnIndex < columns.length; columnIndex += 2) {
      assertTrue(recordTableTwo.contains(columns[columnIndex]));
      assertEquals(columns[columnIndex], recordTableTwo.getColumn(columns[columnIndex].getName()));
      assertNotSame(columns[columnIndex], recordTableTwo.getColumn(columns[columnIndex].getName()));
    }
  }

  public void testContainsRecord() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Record[] records = {
      getRecordInstance(columns, new Object[] { "Lynus", "Torvalds" }),
      getRecordInstance(columns, new Object[] { "James", "Gosling" }),
      getRecordInstance(columns, new Object[] { "Kent", "Beck" }),
      getRecordInstance(columns, new Object[] { "Gordon", "Moore" }),
      getRecordInstance(columns, new Object[] { "Rod", "Johnson" })
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    for (int rowIndex = 1; rowIndex <= records.length; rowIndex++) {
      if ((rowIndex % 2) != 0) {
        assertTrue(recordTable.addRow(records[rowIndex - 1]));
      }
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    for (int rowIndex = 1; rowIndex <= records.length; rowIndex++) {
      assertEquals(((rowIndex % 2) == 1), recordTable.contains(records[rowIndex -  1]));
    }
  }

  public void testGetCellValue() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class),
      getColumnInstance("dob", Calendar.class)
    };

    final Object[][] data = {
      { "Jack", "R", "Handy", "111-11-1111", DateUtil.getCalendar(1962, Calendar.OCTOBER, 21) },
      { "Sandy", "P", "Handy", "121-11-1221", DateUtil.getCalendar(1958, Calendar.MARCH, 6) },
      { "Jon", "D", "Doe", "333-33-3333", DateUtil.getCalendar(1971, Calendar.JULY, 5) },
      { "Jane", "W", "Doe", "343-33-3443", DateUtil.getCalendar(1976, Calendar.JANUARY, 30) },
      { "Jerry", "B", "Steinfeld", "555-55-5555", DateUtil.getCalendar(1941, Calendar.AUGUST, 12) },
      { "Samantha", "T", "Smith", "616-23-1234", DateUtil.getCalendar(1956, Calendar.DECEMBER, 24) }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    for (int rowIndex = recordTable.rowCount(); --rowIndex >= 0; ) {
      for (int columnIndex = recordTable.columnCount(); --columnIndex >= 0; ) {
        assertEquals(data[rowIndex][columnIndex], recordTable.getCellValue(rowIndex, columnIndex));
      }
    }

    try {
      recordTable.getCellValue(data.length, 0);
      fail("Calling RecordTable.getCellValue with an invalid row index should have thrown an IndexOutBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.getCellValue(0, columns.length);
      fail("Calling RecordTable.getCellValue with an invalid column index should have thrown an IndexOutBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    com.cp.common.util.record.Record jordanTate = getRecordInstance(columns, new Object[] { "Jordan", "W", "Tate", "717-21-7890",
      DateUtil.getCalendar(1978, Calendar.MAY, 21) });

    assertNotNull(jordanTate);
    assertEquals(columns.length, jordanTate.size());
    assertFalse(recordTable.contains(jordanTate));
    assertTrue(recordTable.addRow(jordanTate)); // add Jordan Tate to the record table
    assertTrue(recordTable.contains(jordanTate));
    assertEquals(6, recordTable.getRowIndex(jordanTate));
    assertEquals(jordanTate, recordTable.getRow(6));
    assertNotSame(jordanTate, recordTable.getRow(6));
    assertEquals(jordanTate.getValue(columns[0]), recordTable.getCellValue(6, 0));
    assertEquals(jordanTate.getValue(columns[1]), recordTable.getCellValue(6, 1));
    assertEquals(jordanTate.getValue(columns[2]), recordTable.getCellValue(6, 2));
    assertEquals(jordanTate.getValue(columns[3]), recordTable.getCellValue(6, 3));
    assertEquals(jordanTate.getValue(columns[4]), recordTable.getCellValue(6, 4));

    jordanTate = recordTable.getRow(6);

    assertNotNull(jordanTate);
    assertEquals("Jordan", jordanTate.getValue(columns[0]));
    assertEquals("W", jordanTate.getValue(columns[1]));
    assertEquals("Tate", jordanTate.getValue(columns[2]));
    assertEquals("717-21-7890", jordanTate.getValue(columns[3]));
    assertEquals(DateUtil.getCalendar(1978, Calendar.MAY, 21), jordanTate.getValue(columns[4]));

    jordanTate.setValue(columns[0], "Jordas");
    jordanTate.setValue(columns[2], "Unga");

    assertEquals("Jordas", recordTable.getCellValue(6, columns[0]));
    assertEquals("W", recordTable.getCellValue(6, columns[1]));
    assertEquals("Unga", recordTable.getCellValue(6, columns[2]));
    assertEquals("717-21-7890", recordTable.getCellValue(6, columns[3]));
    assertEquals(DateUtil.getCalendar(1978, Calendar.MAY, 21), recordTable.getCellValue(data.length, 4));
  }

  public void testGetColumnByIndex() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());

    int columnIndex = 0;
    for (Column column : columns) {
      assertEquals(column, recordTable.getColumn(columnIndex++));
    }

    assertTrue(recordTable.contains(columns[0]));
    assertEquals(0, recordTable.removeColumn(columns[0])); // remove firstName column
    assertFalse(recordTable.contains(columns[0]));
    assertEquals(0, recordTable.rowCount());
    assertEquals(1, recordTable.columnCount());
    assertEquals(columns[1], recordTable.getColumn(0));

    final Column middleInitial = getColumnInstance("middleInitial", String.class);

    recordTable.addColumn(columns[0]); // add firstName column
    recordTable.addColumn(middleInitial);

    assertTrue(recordTable.contains(columns[0]));
    assertTrue(recordTable.contains(middleInitial));
    assertEquals(0, recordTable.rowCount());
    assertEquals(3, recordTable.columnCount());
    assertEquals(columns[1], recordTable.getColumn(0)); // lastName
    assertEquals(columns[0], recordTable.getColumn(1)); // firsName
    assertEquals(middleInitial, recordTable.getColumn(2));
  }

  public void testGetColumnByIndexExceptionalFlow() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());

    int columnIndex = 0;
    for (Column column : columns) {
      assertEquals(column, recordTable.getColumn(columnIndex++));
    }

    try {
      recordTable.getColumn(-1);
      fail("Calling RecordTable.getColumn by column index with a value of a -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.getColumn(2);
      fail("Calling RecordTable.getColumn by column index with a value of 2 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    final com.cp.common.util.record.Column ssnColumn = getColumnInstance("ssn", String.class);
    recordTable.addColumn(ssnColumn);

    assertEquals(0, recordTable.rowCount());
    assertEquals(3, recordTable.columnCount());
    assertTrue(recordTable.contains(ssnColumn));
    assertEquals(columns[0], recordTable.getColumn(0));
    assertEquals(columns[1], recordTable.getColumn(1));

    try {
      assertEquals(ssnColumn, recordTable.getColumn(2));
    }
    catch (IndexOutOfBoundsException e) {
      fail("Calling RecordTable.getColumn with a column index of 2 should not have thrown an IndexOutOfBoundsException!");
    }

    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); ) {
      it.next();
      it.remove();
    }

    assertEquals(0, recordTable.rowCount());
    assertEquals(0, recordTable.columnCount());

    try {
      recordTable.getColumn(0);
      fail("Calling RecordTable.getColumn with a column index of 0 should have thrown a IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
  }

  public void testGetColumnByName() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());

    for (Column column : columns) {
      assertEquals(column, recordTable.getColumn(column.getName()));
      assertNotSame(column, recordTable.getColumn(column.getName()));
    }

    assertNull(recordTable.getColumn(null));
    assertNull(recordTable.getColumn("firstname"));
    assertNull(recordTable.getColumn("ssn"));

    final Column ssn = getColumnInstance("ssn", String.class);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));
    assertTrue(recordTable.addColumn(ssn)); // add ssn column
    assertTrue(recordTable.contains(ssn));
    assertEquals(0, recordTable.rowCount());
    assertEquals(3, recordTable.columnCount());
    assertNull(recordTable.getColumn("SSN"));
    assertNull(recordTable.getColumn("socialSecurityNumber"));
    assertEquals(ssn, recordTable.getColumn("ssn"));
    assertNotSame(ssn, recordTable.getColumn("ssn"));

    // remove firstName column
    assertEquals(columns[0], recordTable.getColumn("firstName"));
    assertEquals(columns[0], recordTable.removeColumn(0));
    assertNull(recordTable.getColumn("firstName"));
  }

  public void testGetColumnIndex() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class),
      getColumnInstance("dateOfBirth", Calendar.class),
      getColumnInstance("gender", Gender.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());

    int columnIndex = 0;
    for (Column column : columns) {
      assertEquals(columnIndex++, recordTable.getColumnIndex(column));
    }

    final com.cp.common.util.record.Column raceColumn = getColumnInstance("race", Race.class);

    assertNotNull(raceColumn);
    assertFalse(recordTable.contains(raceColumn));
    assertEquals(-1, recordTable.getColumnIndex(raceColumn));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("firstname", String.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("SSN", String.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("socialSecurityNumber", String.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("ssn", Integer.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("DOB", Calendar.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("birthDate", Calendar.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("dateOfBirth", Date.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("gender", String.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("Gender", Gender.class)));
    assertEquals(-1, recordTable.getColumnIndex(getColumnInstance("gendr", Gender.class)));
    assertEquals(4, recordTable.getColumnIndex(columns[4])); // gender
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.removeColumn(columns[1])); // remove lastName
    assertTrue(recordTable.addColumn(raceColumn));
    assertTrue(recordTable.contains(raceColumn));
    assertFalse(recordTable.contains(columns[1])); // lastName
    assertTrue(recordTable.addColumn(columns[1])); // lastName
    assertTrue(recordTable.contains(columns[1])); // lastName
    assertEquals(4, recordTable.getColumnIndex(raceColumn));
    assertEquals(5, recordTable.getColumnIndex(columns[1]));
    assertEquals(0, recordTable.rowCount());
    assertEquals(6, recordTable.columnCount());
  }

  public void testGetColumns() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("dateOfBirth", Calendar.class),
      getColumnInstance("gender", Gender.class),
      getColumnInstance("race", Race.class),
      getColumnInstance("ssn", String.class)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(Arrays.<com.cp.common.util.record.Column>asList(columns), recordTable.getColumns());

    final com.cp.common.util.record.Column nameSuffix = getColumnInstance("nameSuffix", String.class);

    assertNotNull(nameSuffix);
    assertEquals(0, recordTable.removeColumn(columns[0])); // remove personId
    assertEquals(2, recordTable.removeColumn(columns[3])); // remove dateOfBirth
    assertEquals(2, recordTable.removeColumn(columns[4])); // remove gender
    assertEquals(2, recordTable.removeColumn(columns[5])); // remove race
    assertFalse(recordTable.contains(nameSuffix));
    assertTrue(recordTable.addColumn(nameSuffix));
    assertTrue(recordTable.contains(nameSuffix));
    assertEquals(3, recordTable.getColumnIndex(nameSuffix));

    final List<Column> expectedColumnList = new ArrayList<Column>(4);
    expectedColumnList.add(columns[1]); // firstName
    expectedColumnList.add(columns[2]); // lastName
    expectedColumnList.add(columns[6]); // ssn
    expectedColumnList.add(nameSuffix); // nameSuffix

    assertEquals(0, recordTable.rowCount());
    assertEquals(expectedColumnList.size(), recordTable.columnCount());
    assertEquals(expectedColumnList, recordTable.getColumns());

    final com.cp.common.util.record.RecordTable recordTableNoColumns = getRecordTableInstance();

    assertNotNull(recordTableNoColumns);
    assertEquals(0, recordTableNoColumns.columnCount());
    assertEquals(0, recordTableNoColumns.rowCount());
    assertNotNull(recordTableNoColumns.getColumns());
    assertTrue(recordTableNoColumns.getColumns().isEmpty());
  }

  public void testGetColumnValue() throws Exception {
    final com.cp.common.util.record.Column personId = getColumnInstance("personId", Integer.class, false, 0, true);
    final com.cp.common.util.record.Column lastName = getColumnInstance("lastName", String.class, false, 25, false);
    final com.cp.common.util.record.Column dob = getColumnInstance("dob", Calendar.class, true, 0, false);
    final Column gender = getColumnInstance("gender", Gender.class, true, 0, false, Gender.MALE);
    final com.cp.common.util.record.Column race = getColumnInstance("race", Race.class, false, 0, false, Race.WHITE);
    final Column ssn = getColumnInstance("ssn", String.class, true, 11, true, "000-00-0000");

    assertNotNull(personId);
    assertNotNull(lastName);
    assertNotNull(dob);
    assertNotNull(gender);
    assertNotNull(race);
    assertNotNull(ssn);

    assertEquals(new Integer(1), AbstractRecordTable.getColumnValue(personId, new Integer(1)));
    assertNull(AbstractRecordTable.getColumnValue(personId, null));
    assertEquals("BLUM", AbstractRecordTable.getColumnValue(lastName, "BLUM"));
    assertNull(AbstractRecordTable.getColumnValue(lastName, null));
    assertEquals(DateUtil.getCalendar(2005, Calendar.NOVEMBER, 15),
      AbstractRecordTable.getColumnValue(dob, DateUtil.getCalendar(2005, Calendar.NOVEMBER, 15)));
    assertNull(AbstractRecordTable.getColumnValue(dob, null));
    assertEquals(Gender.FEMALE, AbstractRecordTable.getColumnValue(gender, Gender.FEMALE));
    assertEquals(Gender.MALE, AbstractRecordTable.getColumnValue(gender, null));
    assertEquals(Race.BLACK, AbstractRecordTable.getColumnValue(race, Race.BLACK));
    assertEquals(Race.WHITE, AbstractRecordTable.getColumnValue(race, null));
    assertEquals("333-22-4444", AbstractRecordTable.getColumnValue(ssn, "333-22-4444"));
    assertNull(AbstractRecordTable.getColumnValue(ssn, null));
  }

  public void testGetFirstColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(0, recordTable.rowCount());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(columns[0], recordTable.getFirstColumn()); // personId

    // remove the first column
    assertEquals(0, recordTable.removeColumn(columns[0])); // remove personId
    assertFalse(recordTable.contains(columns[0])); // personId
    assertTrue(recordTable.addColumn(columns[0])); // add personId
    assertTrue(recordTable.contains(columns[0])); // personId
    assertEquals(columns[1], recordTable.getFirstColumn()); // firstName

    final Column ssn = getColumnInstance("ssn", String.class);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));
    assertTrue(recordTable.insertColumn(ssn, 0));
    assertTrue(recordTable.contains(ssn));
    assertEquals(ssn, recordTable.getFirstColumn());
    assertEquals(ssn, recordTable.removeColumn(0));
    assertEquals(0, recordTable.removeColumn(columns[1])); // firstName
    assertEquals(0, recordTable.removeColumn(columns[2])); // lastName
    assertEquals(columns[0], recordTable.getFirstColumn()); // personId

    final com.cp.common.util.record.RecordTable recordTableNoColumns = getRecordTableInstance();

    assertNotNull(recordTableNoColumns);
    assertEquals(0, recordTableNoColumns.columnCount());
    assertEquals(0, recordTableNoColumns.rowCount());
    assertNull(recordTableNoColumns.getFirstColumn());
  }

  public void testGetFirstColumnIndex() throws Exception {
    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance();

    assertNotNull(recordTable);
    assertEquals(0, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getFirstColumnIndex());

    // add some columns
    assertTrue(recordTable.addColumn(getColumnInstance("firstName", String.class)));
    assertTrue(recordTable.addColumn(getColumnInstance("lastName", String.class)));
    assertEquals(2, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(0, recordTable.getFirstColumnIndex());

    // remove all columns
    assertEquals(getColumnInstance("firstName", String.class), recordTable.removeColumn(0));
    assertEquals(0, recordTable.getFirstColumnIndex());
    assertEquals(getColumnInstance("lastName", String.class), recordTable.removeColumn(0));
    assertEquals(-1, recordTable.getFirstColumnIndex());
  }

  public void testGetFirstRow() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class, false, 10, true)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jack", "Handy" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(getRecordInstance(columns, data[0]), recordTable.getFirstRow()); // Jon Doe

    final com.cp.common.util.record.Record pieDoe = getRecordInstance(columns, new Object[] { "Pie", "Doe" });

    assertNotNull(pieDoe);
    assertFalse(recordTable.contains(pieDoe));

    try {
      recordTable.insertRow(pieDoe, 0);
      fail("Calling RecordTable.insertRow with Pie Doe should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(pieDoe));
    assertEquals(getRecordInstance(columns, data[0]), recordTable.getFirstRow()); // Jon Doe
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // remove all
    assertTrue(recordTable.removeAll());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertNull(recordTable.getFirstRow());
  }

  public void testGetFirstRowIndex() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getFirstRowIndex());

    final com.cp.common.util.record.Record benDover = getRecordInstance(columns, new Object[] { null, "Ben", "Dover" });

    assertNotNull(benDover);
    assertNull(benDover.getValue(columns[0])); // value of personId
    assertFalse(recordTable.contains(benDover));

    try {
      recordTable.addRow(benDover);
      fail("Calling RecordTable.addRow with Ben Dover should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(benDover));
    assertEquals(-1, recordTable.getFirstRowIndex());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final com.cp.common.util.record.Record jackHandy = getRecordInstance(columns, new Object[] { new Integer(1), "Jack", "Handy" });

    assertNotNull(jackHandy);
    assertFalse(recordTable.contains(jackHandy));
    assertTrue(recordTable.addRow(jackHandy));
    assertTrue(recordTable.contains(jackHandy));
    assertEquals(0, recordTable.getFirstRowIndex());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
  }

  public void testGetLastColumn() throws Exception {
    RecordTable recordTable = getRecordTableInstance();

    assertNotNull(recordTable);
    assertEquals(0, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertNull(recordTable.getLastColumn());

    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jane", "Doe" }
    };

    recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(columns[1], recordTable.getLastColumn()); // lastName

    final com.cp.common.util.record.Column ssn = getColumnInstance("ssn", String.class, false, 9, true);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.addColumn(ssn);
      fail("Calling addColumn with SSN on a record table with data should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(columns[1], recordTable.getLastColumn()); // lastName
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Column personId = getColumnInstance("personId", Integer.class, true, 0, true);
    final Column gender = getColumnInstance("gender", Gender.class);
    final Column race = getColumnInstance("race", Race.class);

    assertNotNull(personId);
    assertNotNull(gender);
    assertNotNull(race);
    assertFalse(recordTable.contains(personId));
    assertFalse(recordTable.contains(gender));
    assertFalse(recordTable.contains(race));
    assertTrue(recordTable.insertColumn(personId, 0)); // insert personId
    assertEquals(columns[1], recordTable.getLastColumn()); // lastName
    assertTrue(recordTable.addColumn(gender)); // add gender column
    assertEquals(gender, recordTable.getLastColumn());
    assertTrue(recordTable.addColumn(race)); // add race column
    assertEquals(race, recordTable.getLastColumn());
    assertTrue(recordTable.contains(personId));
    assertEquals(0, recordTable.getColumnIndex(personId));
    assertTrue(recordTable.contains(gender));
    assertTrue(recordTable.contains(race));
    assertEquals(2, recordTable.removeColumn(columns[1])); // remove lastName column
    assertEquals(race, recordTable.getLastColumn());
    assertFalse(recordTable.contains(columns[1])); // lastName
    assertTrue(recordTable.addColumn(columns[1])); // add lastName column
    assertTrue(recordTable.contains(columns[1])); // lastName
    assertEquals(columns[1], recordTable.getLastColumn()); // lastName
    assertEquals(5, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testGetLastColumnIndex() throws Exception {
    com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance();

    assertNotNull(recordTable);
    assertEquals(0, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getLastColumnIndex());

    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jack", "Handy" },
      { "Sandy", "Handy" }
    };

    recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(1, recordTable.getLastColumnIndex());

    // remove the firstName column
    assertEquals(columns[0], recordTable.removeColumn(0));
    assertEquals(1, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertEquals(0, recordTable.getLastColumnIndex());

    final Column ssn = getColumnInstance("ssn", String.class, false, 9, false);

    assertNotNull(ssn);
    assertFalse(recordTable.contains(ssn));

    try {
      recordTable.addColumn(ssn);
      fail("Calling addColumn with SSN on a record table with data should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(ssn));
    assertEquals(0, recordTable.getLastColumnIndex());

    final Column personId = getColumnInstance("personId", Integer.class, true, 0, true);
    final com.cp.common.util.record.Column dateOfBirth = getColumnInstance("dateOfBirth", Calendar.class);

    assertNotNull(personId);
    assertNotNull(dateOfBirth);
    assertFalse(recordTable.contains(personId));
    assertFalse(recordTable.contains(dateOfBirth));
    assertTrue(recordTable.insertColumn(personId, 0));
    assertTrue(recordTable.addColumn(dateOfBirth));
    assertTrue(recordTable.contains(personId));
    assertTrue(recordTable.contains(dateOfBirth));
    assertEquals(2, recordTable.getLastColumnIndex());
    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testGetLastRow() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertNull(recordTable.getLastRow());

    final Record[] rows = {
      getRecordInstance(columns, new Object[] { "Bill", "Clinton" }),
      getRecordInstance(columns, new Object[] { "Hillary", "Clinton"})
    };

    recordTable.addAll(Arrays.<Record>asList(rows));

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(rows.length, recordTable.rowCount());
    assertEquals(rows[1], recordTable.getLastRow()); // Hillary Clinton

    final Record chelseyClinton = getRecordInstance(columns, new Object[] { "Chelsey", "Clinton" });

    assertNotNull(chelseyClinton);
    assertFalse(recordTable.contains(chelseyClinton));
    assertTrue(recordTable.insertRow(chelseyClinton, 0)); // insert Chelsey Clinton
    assertTrue(recordTable.contains(chelseyClinton));
    assertEquals(0, recordTable.getRowIndex(chelseyClinton));
    assertEquals(rows[1], recordTable.getLastRow()); // Hillary Clinton
    assertTrue(recordTable.contains(rows[0])); // Bill Clinton
    assertEquals(1, recordTable.removeRow(rows[0])); // remove Bill Clinton
    assertFalse(recordTable.contains(rows[0])); // Bill Clinton
    assertEquals(rows[1], recordTable.getLastRow()); // Hillary Clinton
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(2, recordTable.rowCount());

    final Column personId = getColumnInstance("personId", Integer.class, true, 0, true);

    assertNotNull(personId);
    assertFalse(recordTable.contains(personId));
    assertTrue(recordTable.addColumn(personId)); // add personId column
    assertTrue(recordTable.contains(personId));
    assertEquals(2, recordTable.getColumnIndex(personId));

    int personIdValue = 1;
    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); ) {
      it.next().setValue(personId, personIdValue++);
    }

    personIdValue = 1;
    for (int rowIndex = 0; rowIndex < recordTable.rowCount(); rowIndex++) {
      assertEquals(new Integer(personIdValue++), recordTable.getCellValue(rowIndex, personId));
    }

    final com.cp.common.util.record.Record hillaryClinton = rows[1];
    hillaryClinton.addField(personId, new Integer(2));

    final Record williamClinton = getRecordInstance(recordTable.getColumns().toArray(
      new com.cp.common.util.record.Column[recordTable.columnCount()]), new Object[] { "William", "Clinton", new Integer(1) });

    assertNotNull(williamClinton);
    assertFalse(recordTable.contains(williamClinton));

    try {
      recordTable.addRow(williamClinton);
      fail("Calling addRow with William Clinton should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(williamClinton));
    assertEquals(hillaryClinton, recordTable.getLastRow());
    assertTrue(recordTable.removeAll());
    assertEquals(3, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertNull(recordTable.getLastRow());
  }

  public void testGetLastRowIndex() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, true, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getLastRowIndex());

    final Record maliciousHacker = getRecordInstance(columns, new Object[] { new Integer(1), null, "Hacker" });

    assertNotNull(maliciousHacker);
    assertFalse(recordTable.contains(maliciousHacker));

    try {
      recordTable.addRow(maliciousHacker);
      fail("Calling addRow with Malicious Hacker should have thrown an NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(maliciousHacker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getLastRowIndex());

    final Record tinyTim = getRecordInstance(columns, new Object[] { new Integer(1), "Tiny", "Tim" });

    assertNotNull(tinyTim);
    assertFalse(recordTable.contains(tinyTim));
    assertTrue(recordTable.addRow(tinyTim));
    assertTrue(recordTable.contains(tinyTim));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
    assertEquals(0, recordTable.getLastRowIndex());

    final Record evilCracker = getRecordInstance(columns, new Object[] { new Integer(1), "Evil", "Cracker" });

    assertNotNull(evilCracker);
    assertFalse(recordTable.contains(evilCracker));

    try {
      recordTable.addRow(evilCracker);
      fail("Calling addRow with Evil Cracker should have thrown an NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(evilCracker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
    assertEquals(0, recordTable.getLastRowIndex());

    final Record badRecord = getRecordInstance(columns, new Object[] { new Integer(2), "OverlyLongFirstNameIllegalRecord", "Smith" });

    assertNotNull(badRecord);
    assertFalse(recordTable.contains(badRecord));

    try {
      recordTable.addRow(badRecord);
      fail("Calling addRow with a Record having a firstName column that is too large should have thrown a InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(badRecord));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
    assertEquals(0, recordTable.getLastRowIndex());
    assertTrue(recordTable.removeAll());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertEquals(-1, recordTable.getLastRowIndex());
  }

  public void testGetRow() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Peter", "Griffin" },
      { "Lois", "Griffin" },
      { "Meg", "Griffin" },
      { "Chris", "Griffin" },
      { "Stewie", "Griffin" },
      { "Brian", "Griffin" }
    };

    com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), recordTable.getRow(rowIndex));
    }

    final com.cp.common.util.record.Record joeSwanson = getRecordInstance(columns, new Object[] { "Joe", "Swanson" });
    final Record glenQuagmire = getRecordInstance(columns, new Object[] { "Glen", "Quagmire" });

    assertNotNull(joeSwanson);
    assertNotNull(glenQuagmire);
    assertFalse(recordTable.contains(joeSwanson));
    assertFalse(recordTable.contains(glenQuagmire));
    assertTrue(recordTable.insertRow(joeSwanson, 0)); // insert Joe Swanson
    assertTrue(recordTable.addRow(glenQuagmire)); // add Glen Quagmire
    assertTrue(recordTable.contains(joeSwanson));
    assertTrue(recordTable.contains(glenQuagmire));
    assertEquals(joeSwanson, recordTable.getRow(0));
    assertNotSame(joeSwanson, recordTable.getRow(0));
    assertEquals(glenQuagmire, recordTable.getRow(recordTable.getLastRowIndex()));
    assertNotSame(glenQuagmire, recordTable.getRow(recordTable.getLastRowIndex()));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(8, recordTable.rowCount());

    final com.cp.common.util.record.Record clevelandBrown = getRecordInstance(columns, new Object[] { "Cleveland", "Brown" });
    final Record tomTucker = getRecordInstance(columns, new Object[] { "Tom", "Tucker" });
    final com.cp.common.util.record.Record triciaTakanawa = getRecordInstance(columns, new Object[] { "Tricia", "Takanawa" });

    assertNotNull(clevelandBrown);
    assertNotNull(tomTucker);
    assertNotNull(triciaTakanawa);
    assertEquals(getRecordInstance(columns, data[0]), recordTable.removeRow(1)); // remove Peter Griffin
    assertEquals(getRecordInstance(columns, data[2]), recordTable.removeRow(2)); // remove Meg Griffin
    assertEquals(getRecordInstance(columns, data[3]), recordTable.removeRow(2)); // remove Chris Griffin
    assertEquals(getRecordInstance(columns, data[5]), recordTable.removeRow(3)); // remove Brian Griffin
    assertTrue(recordTable.insertRow(clevelandBrown, 0));
    assertTrue(recordTable.addRow(tomTucker));
    assertTrue(recordTable.addRow(triciaTakanawa));

    final Record[] expectedRows = {
      clevelandBrown,
      joeSwanson,
      getRecordInstance(columns, data[1]), // Lois Griffin
      getRecordInstance(columns, data[4]), // Stewie Griffin
      glenQuagmire,
      tomTucker,
      triciaTakanawa
    };

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(expectedRows.length, recordTable.rowCount());

    for (int rowIndex = 0; rowIndex < expectedRows.length; rowIndex++) {
      assertEquals(expectedRows[rowIndex], recordTable.getRow(rowIndex));
    }
  }

  public void testGetRowExceptionalFlow() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.getRow(0);
      fail("Calling getRow with a row index of 0 on a record table with no rows should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.getRow(-1);
      fail("Calling getRow with a row index of a -1 on a record table with no rows should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    final Record benDover = getRecordInstance(columns, new Object[] { new Integer(0), "Ben", "Dover" });

    assertNotNull(benDover);
    assertFalse(recordTable.contains(benDover));
    assertTrue(recordTable.addRow(benDover));
    assertTrue(recordTable.contains(benDover));

    try {
      recordTable.getRow(-1);
      fail("Calling getRow with a row index of a -1 on non-empty record table should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.getRow(recordTable.getLastRowIndex() + 1);
      fail("Calling getRow with a row index 1 greater than the last row index on a non-empty record table should have thrown a IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());

    try {
      assertEquals(benDover, recordTable.getRow(0));
    }
    catch (IndexOutOfBoundsException e) {
      fail("Calling getRow with a row index of 0 on a record table with 1 row should not have thrown an IndexOutOfBoundsException!");
    }
  }

  public void testGetRowIndex() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Peter", "Griffin" },
      { "Lois", "Griffin" },
      { "Meg", "Griffin" },
      { "Chris", "Griffin" },
      { "Stewie", "Griffin" },
      { "Brian", "Griffin" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
      assertEquals(rowIndex, recordTable.getRowIndex(getRecordInstance(columns, data[rowIndex])));
    }

    assertEquals(-1, recordTable.getRowIndex(null));
    assertEquals(-1, recordTable.getRowIndex(getRecordInstance(columns, new Object[] { "John", "Blum" })));
    assertEquals(0, recordTable.removeRow(getRecordInstance(columns, data[0]))); // remove Peter Griffin
    assertEquals(1, recordTable.removeRow(getRecordInstance(columns, data[2]))); // remove Meg Griffin
    assertEquals(1, recordTable.removeRow(getRecordInstance(columns, data[3]))); // remove Chris Griffin
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    final com.cp.common.util.record.Record adamWest = getRecordInstance(columns, new Object[] { "Adam", "West" });

    assertNotNull(adamWest);
    assertFalse(recordTable.contains(adamWest));
    assertEquals(-1, recordTable.getRowIndex(adamWest));
    assertTrue(recordTable.insertRow(adamWest, 0)); // insert Adam West
    assertTrue(recordTable.contains(adamWest));
    assertEquals(0, recordTable.getRowIndex(adamWest));

    final Record dianeSimmons = getRecordInstance(columns, new Object[] { "Diane", "Simmons" });

    assertNotNull(dianeSimmons);
    assertFalse(recordTable.contains(dianeSimmons));
    assertEquals(-1, recordTable.getRowIndex(dianeSimmons));
    assertTrue(recordTable.addRow(dianeSimmons)); // add Diane Simmons
    assertTrue(recordTable.contains(dianeSimmons));
    assertEquals(recordTable.getLastRowIndex(), recordTable.getRowIndex(dianeSimmons));

    final Record[] expectedRows = {
      adamWest,
      getRecordInstance(columns, data[1]), // Lois Griffin
      getRecordInstance(columns, data[4]), // Stewie Griffin
      getRecordInstance(columns, data[5]), // Brian Griffin
      dianeSimmons
    };

    int rowIndex = 0;
    for (com.cp.common.util.record.Record row : expectedRows) {
      assertEquals(rowIndex++, recordTable.getRowIndex(row));
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(5, recordTable.rowCount());
  }

  public void testIsEmpty() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(1), "John", "Blum" },
      { new Integer(2), "Sara", "Blum" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertFalse(recordTable.isEmpty());

    recordTable.clear();

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());

    final Record jonDoe = getRecordInstance(columns, new Object[] { null, "Jon", "Doe" });

    assertNotNull(jonDoe);
    assertFalse(recordTable.contains(jonDoe));

    try {
      recordTable.addRow(jonDoe);
      fail("Adding Jon Doe to the record table should have thrown a NullColumnValueException for a null personId value!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(jonDoe));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());

    final Record jackHandy = getRecordInstance(columns, new Object[] { new Integer(2), "Jack", "Handy" });

    assertNotNull(jackHandy);
    assertFalse(recordTable.contains(jackHandy));

    try {
      assertTrue(recordTable.addRow(jackHandy));
    }
    catch (Exception e) {
      fail("Adding Jack Handy to the record table should not have thrown an Exception!");
    }

    assertTrue(recordTable.contains(jackHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
    assertFalse(recordTable.isEmpty());

    final com.cp.common.util.record.Record sandyHandy = getRecordInstance(columns, new Object[] { new Integer(2), "Sandy", "Handy" });

    assertNotNull(sandyHandy);
    assertFalse(recordTable.contains(sandyHandy));

    try {
      recordTable.addRow(sandyHandy);
      fail("Adding Sandy Handy to the record table should have thrown a NonUniqueColumnValueException for the duplicate personId of 2!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(sandyHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());
    assertFalse(recordTable.isEmpty());
    assertTrue(recordTable.contains(jackHandy));
    assertTrue(recordTable.remove(jackHandy));
    assertFalse(recordTable.contains(jackHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());
  }

  public void testRemoveAll() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jack", "Handy" },
      { "Jon", "Doe" },
      { "Samantha", "Tate" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    for (Object[] row : data) {
      assertTrue(recordTable.contains(getRecordInstance(columns, row)));
    }

    assertTrue(recordTable.removeAll());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    for (Object[] row : data) {
      assertFalse(recordTable.contains(getRecordInstance(columns, row)));
    }

    assertTrue(recordTable.removeAll()); // call removeAll again; should be no change!
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final com.cp.common.util.record.Record sandyHandy = getRecordInstance(columns, new Object[] { "Sandy", "Handy" });

    assertNotNull(sandyHandy);
    assertFalse(recordTable.contains(sandyHandy));
    assertTrue(recordTable.addRow(sandyHandy));
    assertTrue(recordTable.contains(sandyHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(1, recordTable.rowCount());

    final Column personId = getColumnInstance("personId", Integer.class, false, 0, true);

    assertNotNull(personId);
    assertFalse(recordTable.contains(personId));

    try {
      recordTable.insertColumn(personId, 0);
      fail("Adding the personId column, which is non-nullable and unique, to a record table with rows should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(personId));
    assertTrue(recordTable.removeAll());
    assertFalse(recordTable.contains(sandyHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.insertColumn(personId, 0);
    }
    catch (Exception e) {
      fail("Adding the non-nullable, unique personId column to an empty record table should not have thrown an Exception!");
    }

    assertTrue(recordTable.contains(personId));
    assertEquals(3, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final com.cp.common.util.record.Record janeDoe = getRecordInstance(new Column[] { personId, columns[0], columns[1] }, new Object[] { null, "Jane", "Doe" });

    assertNotNull(janeDoe);
    assertFalse(recordTable.contains(janeDoe));

    try {
      recordTable.addRow(janeDoe);
      fail("Add Jane Doe to the record table having a null value for personId should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertEquals(3, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.removeAll());
  }

  public void testRemoveAllFromCollection() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { new Integer(1), "Sara", "Blum" },
      { new Integer(2), "Joe", "Koontz" },
      { new Integer(3), "Bridget", "Campbell" },
      { new Integer(4), "Araceli", "Reyes" },
      { new Integer(5), "Robb", "Dreyer" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    for (Object[] row : data) {
      assertTrue(recordTable.contains(getRecordInstance(columns, row)));
    }

    final com.cp.common.util.record.Record[] recordArray = {
      getRecordInstance(columns, data[2]), // Bridget Campbell
      getRecordInstance(columns, new Object[] { new Integer(11), "Annett", "Sullivan" }),
      getRecordInstance(columns, data[4]), // Robb Dreyer
      getRecordInstance(columns, new Object[] { new Integer(22), "Tara", "Bloom" })
    };

    assertTrue(recordTable.removeAll(Arrays.<Record>asList(recordArray)));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    final Record[] expectedRows = {
      getRecordInstance(columns, data[0]), // Sara Blum
      getRecordInstance(columns, data[1]), // Joe Koontz
      getRecordInstance(columns, data[3]), // Araceli Reyes
    };

    for (com.cp.common.util.record.Record expectedRow : expectedRows) {
      assertTrue(recordTable.contains(expectedRow));
    }
  }

  public void testRemoveColumn() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class, true, 9, true),
      getColumnInstance("dob", Calendar.class),
      getColumnInstance("gender", Gender.class),
      getColumnInstance("race", Race.class)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    int columnIndex = 0;
    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      assertEquals(columns[columnIndex], it.next());
    }

    assertEquals(columns[2], recordTable.removeColumn(2)); // remove middleInitial
    assertEquals(3, recordTable.removeColumn(columns[4])); // remove ssn
    assertEquals(columns[7], recordTable.removeColumn(recordTable.getLastColumnIndex())); // remove race
    assertEquals(5, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    final List<Column> expectedColumns = new ArrayList<Column>(5);
    expectedColumns.add(columns[0]); // personId
    expectedColumns.add(columns[1]); // firstName
    expectedColumns.add(columns[3]); // lastName
    expectedColumns.add(columns[5]); // dob
    expectedColumns.add(columns[6]); // gender

    assertEquals(expectedColumns, recordTable.getColumns());
  }

  public void testRemoveColumnExceptionalFlow() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    int columnIndex = 0;
    for (Iterator<Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      assertEquals(columns[columnIndex], it.next());
    }

    assertTrue(recordTable.contains(columns[0])); // personId

    try {
      recordTable.removeColumn(getColumnInstance("personId", Integer.class, true, 0, false));
      fail("Removing nullable, non-unique Column personId should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertTrue(recordTable.contains(columns[0])); // personId
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.removeColumn(-1);
      fail("Removing column @ index -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.removeColumn(recordTable.getLastColumnIndex() + 1);
      fail("Removing column @ index of 1 past the last row index should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.contains(columns[1])); // firstName

    try {
      recordTable.removeColumn(getColumnInstance("firstname", String.class, false, 25, false));
      fail("Removing column 'firstname' should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.contains(columns[1])); // firstName
    assertTrue(recordTable.contains(columns[2])); // lastName

    try {
      recordTable.removeColumn(getColumnInstance("lastName", String.class, true, 35, true));
      fail("Removing nullable, unique column 'lastName' should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.contains(columns[2])); // lastName

    try {
      recordTable.removeColumn(getColumnInstance("gender", Gender.class));
      fail("Removing column gender should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    columnIndex = 0;
    for (Iterator<com.cp.common.util.record.Column> it = recordTable.columnIterator(); it.hasNext(); columnIndex++) {
      assertEquals(columns[columnIndex], it.next());
    }
  }

  public void testRemoveRow() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { new Integer(1), "Jack", "Handy" },
      { new Integer(3), "Agent", "Smith" },
      { new Integer(5), "Jon", "Doe" },
      { new Integer(7), "Joe", "Dirt" },
      { new Integer(11), "Jane", "Doe" },
      { new Integer(13), "Sandy", "Handy" },
      { new Integer(17), "Peggy", "Simpleton" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    assertEquals(getRecordInstance(columns, data[1]), recordTable.removeRow(1)); // remove Agent Smith
    assertEquals(2, recordTable.removeRow(getRecordInstance(columns, data[3]))); // remove Joe Dirt
    assertEquals(getRecordInstance(columns, data[6]), recordTable.removeRow(recordTable.getLastRowIndex()));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    final List<com.cp.common.util.record.Record> expectedRows = new ArrayList<com.cp.common.util.record.Record>(4);
    expectedRows.add(getRecordInstance(columns, data[0])); // Jack Handy
    expectedRows.add(getRecordInstance(columns, data[2])); // Jon Doe
    expectedRows.add(getRecordInstance(columns, data[4])); // Jane Doe
    expectedRows.add(getRecordInstance(columns, data[5])); // Sandy Handy

    for (Iterator expectedRowIterator = expectedRows.iterator(), actualRowIterator = recordTable.rowIterator(); expectedRowIterator.hasNext(); ) {
      assertEquals(expectedRowIterator.next(), actualRowIterator.next());
    }

    final com.cp.common.util.record.Record jack = recordTable.getRow(0);

    assertNotNull(jack);
    assertEquals(new Integer(1), jack.getValue(columns[0])); // personId
    assertEquals("Jack", jack.getValue(columns[1])); // firstName
    assertEquals("Handy", jack.getValue(columns[2])); // lastName
    assertTrue(recordTable.contains(jack));
    assertEquals(new Integer(1), jack.setValue(columns[0], new Integer(19))); // personId
    assertEquals("Handy", jack.setValue(columns[2], "Black")); // lastName
    assertTrue(recordTable.contains(jack));

    try {
      recordTable.removeRow(jack);
    }
    catch (IndexOutOfBoundsException e) {
      fail("Removing Record Jack Handy after changing his personId to 19 and lastName to Black should not have thrown an IndexOutOfBoundsException!");
    }
  }

  public void testRemoveRowExceptionalFlow() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(1), "Agent", "Smith" },
      { new Integer(2), "Neo", "Anderson" },
      { new Integer(3), "Oracle", "Green" },
      { new Integer(4), "Trinity", "Blade" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    try {
      recordTable.removeRow(-1);
      fail("Removing row @ index -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.removeRow(recordTable.getLastRowIndex() + 1);
      fail("Removing row @ index 1 past the last row index should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final com.cp.common.util.record.Record trinityAnderson = getRecordInstance(columns, new Object[] { new Integer(1), "Trinity", "Anderson" });

    assertNotNull(trinityAnderson);
    assertFalse(recordTable.contains(trinityAnderson));

    try {
      recordTable.removeRow(trinityAnderson);
      fail("Removing Record Trinity Anderson should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testRetainAll() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(0), "Peter", "Griffin" },
      { new Integer(2), "Lois", "Griffin" },
      { new Integer(4), "Meg", "Griffin" },
      { new Integer(8), "Chris", "Griffin" },
      { new Integer(16), "Stewie", "Griffin" },
      { new Integer(32), "Brian", "Griffin" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    List<Record> collectionToRetain = new ArrayList<Record>(4);
    collectionToRetain.add(getRecordInstance(columns, data[0])); // Peter Griffin
    collectionToRetain.add(getRecordInstance(columns, data[3])); // Chris Griffin
    collectionToRetain.add(getRecordInstance(columns, data[4])); // Stewie Griffin
    collectionToRetain.add(getRecordInstance(columns, data[5])); // Brian Griffin

    assertTrue(recordTable.retainAll(collectionToRetain));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    for (Iterator expectedIterator = collectionToRetain.iterator(), actualIterator = recordTable.rowIterator(); expectedIterator.hasNext(); ) {
      assertEquals(expectedIterator.next(), actualIterator.next());
    }

    collectionToRetain.clear();
    collectionToRetain.add(getRecordInstance(columns, data[0])); // Peter Griffin
    collectionToRetain.add(getRecordInstance(columns, data[5])); // Brian Griffin
    collectionToRetain.add(getRecordInstance(columns, new Object[] { new Integer(64), "Stewie", "Griffin" }));
    collectionToRetain.add(getRecordInstance(columns, new Object[] { new Integer(128), "Glen", "Qwagmire" }));

    assertTrue(recordTable.retainAll(collectionToRetain));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(2, recordTable.rowCount());
    assertTrue(recordTable.contains(getRecordInstance(columns, data[0]))); // Peter Griffin
    assertTrue(recordTable.contains(getRecordInstance(columns, data[5]))); // Peter Griffin

    final Record loisGriffin = getRecordInstance(columns, data[1]); // Lois Griffin

    collectionToRetain.clear();
    collectionToRetain.add(loisGriffin);

    assertFalse(recordTable.contains(loisGriffin));
    assertTrue(recordTable.retainAll(collectionToRetain));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());
  }

  public void testRowCount() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(1), "Santa" , "Claus" },
      { new Integer(2), "Easter", "Bunny" },
      { new Integer(3), "Papa", "Smurf" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final com.cp.common.util.record.Record jackRabbit = getRecordInstance(columns, new Object[] { new Integer(4), "Jack", "Rabbit"});
    final Record oscarGrouch = getRecordInstance(columns, new Object[] { new Integer(5), "Oscar", "Grouch" });

    assertNotNull(jackRabbit);
    assertNotNull(oscarGrouch);
    assertFalse(recordTable.contains(jackRabbit));
    assertFalse(recordTable.contains(oscarGrouch));
    assertTrue(recordTable.addRow(jackRabbit));
    assertTrue(recordTable.addRow(oscarGrouch));
    assertTrue(recordTable.contains(jackRabbit));
    assertTrue(recordTable.contains(oscarGrouch));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(5, recordTable.rowCount());

    final Record evilBandit = getRecordInstance(columns, new Object[] { null, "Evil", "Bandit" });
    final Record papaSmurf = getRecordInstance(columns, new Object[] { new Integer(-3), "Papa", "Smurf" });

    assertNotNull(evilBandit);
    assertNotNull(papaSmurf);
    assertFalse(recordTable.contains(evilBandit));
    assertFalse(recordTable.contains(papaSmurf));

    try {
      recordTable.addRow(evilBandit);
      fail("Adding Evil Bandit with a null personId should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    try {
      recordTable.removeRow(papaSmurf);
      fail("Removing Papa Smurf with incorrect personId of -3 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(evilBandit));
    assertTrue(recordTable.contains(getRecordInstance(columns, data[2]))); // Papa Smurf
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(5, recordTable.rowCount());

    recordTable.clear();

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    assertTrue(recordTable.isEmpty());
  }

  public void testRowIterator() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jack", "R", "Handy" },
      { "Camron", "E", "Crowe" },
      { "Charlie", "B", "Weston" },
      { "Barbara", "E", "Swanson" },
      { "Fred", "L", "Glaskow" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
      if ((rowIndex % 2) == 1) {
        it.remove();
      }
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex += 2) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    try {
      final Iterator it = recordTable.rowIterator();
      it.hasNext();
      it.next();
      it.remove();
      recordTable.removeRow(recordTable.getFirstRowIndex());
      it.hasNext();
      it.next();
      fail("The RecordTable.rowIterator().next() method should have thrown a ConcurrentModificationException!");
    }
    catch (ConcurrentModificationException e) {
      // expected behavior!
    }
  }

  public void testSetCellValue() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("dob", Calendar.class),
      getColumnInstance("gender", Gender.class),
      getColumnInstance("race", Race.class)
    };

    final Object[][] data = {
      { "Jack", "B", "Handy", DateUtil.getCalendar(1968, Calendar.MAY, 27), Gender.MALE, Race.WHITE },
      { "Ben", "R", "Dover", DateUtil.getCalendar(1942, Calendar.OCTOBER, 24), Gender.MALE, Race.ASIAN },
      { "Jon", "D", "Doe", DateUtil.getCalendar(1971, Calendar.FEBRUARY, 12), Gender.MALE, Race.BLACK },
      { "Rick", "F", "Eleven", DateUtil.getCalendar(1958, Calendar.NOVEMBER, 2), Gender.MALE, Race.WHITE },
      { "Paco", "S", "Gringo", DateUtil.getCalendar(1982, Calendar.AUGUST, 15), Gender.MALE, Race.HISPANIC }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    // set Jack Handy's lastName to Black
    recordTable.setCellValue(0, columns[2], "Black");
    // set Ben Dover's dob to March 29, 1963
    recordTable.setCellValue(1, 3, DateUtil.getCalendar(1963, Calendar.MARCH, 29));
    // set Jon Doe's first name to Jane and sex to female
    recordTable.setCellValue(2, 0, "Jane");
    recordTable.setCellValue(2, columns[4], Gender.FEMALE);
    // set Rick Eleven's lastName to Seven and his Race to Native American
    recordTable.setCellValue(3, 2, "Seven");
    recordTable.setCellValue(3, columns[5], Race.NATIVE_AMERICAN);
    // set Paco Gringo's middleInitial to T and his dob to June 15, 1990
    recordTable.setCellValue(4, columns[1], "T");
    recordTable.setCellValue(4, 3, DateUtil.getCalendar(1990, Calendar.JUNE, 15));

    // assert Jack Black
    assertEquals("Jack", recordTable.getCellValue(0, 0));
    assertEquals("B", recordTable.getCellValue(0, columns[1]));
    assertEquals("Black", recordTable.getCellValue(0, 2));
    assertEquals(DateUtil.getCalendar(1968, Calendar.MAY,  27), recordTable.getCellValue(0, columns[3]));
    assertEquals(Gender.MALE, recordTable.getCellValue(0, 4));
    assertEquals(Race.WHITE, recordTable.getCellValue(0, columns[5]));

    // assert Ben Dover
    final Record benDover = recordTable.getRow(1);

    assertNotNull(benDover);
    assertEquals("Ben", benDover.getValue(0));
    assertEquals("Dover", benDover.getValue(2));
    assertEquals(DateUtil.getCalendar(1963, Calendar.MARCH, 29), benDover.getValue(columns[3]));
    assertEquals(Gender.MALE, benDover.getValue(4));

    // assert Jane Doe
    assertEquals("Jane", recordTable.getCellValue(2, 0));
    assertEquals("D", recordTable.getCellValue(2, columns[1]));
    assertEquals("Doe", recordTable.getCellValue(2, 2));
    assertEquals(DateUtil.getCalendar(1971, Calendar.FEBRUARY, 12), recordTable.getCellValue(2, columns[3]));
    assertEquals(Gender.FEMALE, recordTable.getCellValue(2, 4));
    assertEquals(Race.BLACK, recordTable.getCellValue(2, columns[5]));

    // assert Rick Seven
    final Record rickSeven = recordTable.getRow(3);

    assertNotNull(rickSeven);
    assertEquals("Rick", rickSeven.getValue(0));
    assertEquals("F", rickSeven.getValue(columns[1]));
    assertEquals("Seven", rickSeven.getValue(2));
    assertEquals(Gender.MALE, rickSeven.getValue(columns[4]));
    assertEquals(Race.NATIVE_AMERICAN, rickSeven.getValue(5));

    // assert Paco Gringo
    assertEquals("Paco", recordTable.getCellValue(4, columns[0]));
    assertEquals("T", recordTable.getCellValue(4, 1));
    assertEquals("Gringo", recordTable.getCellValue(4, columns[2]));
    assertEquals(DateUtil.getCalendar(1990, Calendar.JUNE, 15), recordTable.getCellValue(4, 3));
    assertEquals(Gender.MALE, recordTable.getCellValue(4, columns[4]));
    assertEquals(Race.HISPANIC, recordTable.getCellValue(4, 5));
  }

  public void testSetCellValueExceptionalFlowIndexOutOfBounds() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("ssn", String.class, true, 9, false)
    };

    final Object[][] data = {
      { new Integer(1), "Jon", "Doe", "111224444" },
      { new Integer(2), "Jane", "Doe", "222335555" },
      { new Integer(3), "Pie", "Doe", null }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    final com.cp.common.util.record.Column dob = getColumnInstance("dob", Calendar.class);

    try {
      recordTable.setCellValue(0, dob, DateUtil.getCalendar(1969, Calendar.JULY, 4));
      fail("Setting dob on Jon Doe should have thrown an IndexOutOfBoundsException; no such column (dob) exists in the record table!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    final com.cp.common.util.record.Column ssn = getColumnInstance("SSN", String.class, true, 9, false);

    try {
      recordTable.setCellValue(2, ssn, "333446666");
      fail("Setting a value for SSN on Pie Doe should have thrown a IndexOutOfBoundsException; ssn, not SSN, is a column in the record table!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertNull(recordTable.getCellValue(2, columns[3]));
    assertTrue(recordTable.contains(columns[3]));
    assertEquals(columns[3], recordTable.removeColumn(3)); // remove ssn column
    assertFalse(recordTable.contains(columns[3]));
    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.setCellValue(2, columns[3], "4444557777");
      fail("Setting a value for the ssn column on Pie Doe should have thrown an IndexOutOfBoundsException; the ssn column was removed from the record table!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    assertEquals(3, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.setCellValue(-1, columns[0], new Integer(10));
      fail("Setting cell value @ (-1, personId) should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.setCellValue(recordTable.getLastRowIndex() + 1, 1, "Bob");
      fail("Setting cell value @ (after last row index, firstName) should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    recordTable.clear();

    assertTrue(recordTable.isEmpty());
    assertEquals(3, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.setCellValue(0, 0, new Integer(100));
      fail("Setting cell value for personId @ row 0 should have thrown an IndexOutOfBoundsException; the record table is empty!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
  }

  public void testSetCellValueExceptionalFlowInvalidColumnValues() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("ssn", String.class, true, 9, true)
    };

    final Object[][] data = {
      { new Integer(1), "Jon", "Doe", null },
      { new Integer(2), "Sandy", "Handy", "333224444" },
      { new Integer(3), "Jane", "Doe", "111007777" },
      { new Integer(4), "Jack", "Handy", null }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    try {
      recordTable.setCellValue(2, columns[0], new Integer(1));
      fail("Setting Jane Doe's personId to 1 should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertEquals(new Integer(3), recordTable.getCellValue(2, columns[0]));

    try {
      recordTable.setCellValue(1, 1, null);
      fail("Setting Sandy Handy's firstName to null should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertEquals("Sandy", recordTable.getCellValue(1, 1));

    try {
      recordTable.setCellValue(0, columns[2], "AVeryLongLastNameExceedingTheThirtyFiveCharacterLimit");
      fail("Setting Jon Doe's lastName to 'AVeryLongLastNameExceedingTheThirtyFiveCharacterLimit' should have thrown an InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertEquals("Doe", recordTable.getCellValue(0, columns[2]));

    try {
      recordTable.setCellValue(3, 3, new char[] { '3', '3', '3', '2', '2', '4', '4', '4', '4'});
      fail("Setting Jack Handy's ssn to 333224444 as an array of chars should have thrown an IllegalColumnValueTypeException!'");
    }
    catch (com.cp.common.util.record.InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertNull(recordTable.getCellValue(3, 3));

    try {
      recordTable.setCellValue(3, columns[3], "333224444");
      fail("Setting Jack Handy's ssn to 333224444 should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertNull(recordTable.getCellValue(3, columns[3]));

    try {
      recordTable.setCellValue(1, 3, null);
    }
    catch (Exception e) {
      fail("Setting Sandy Handy's ssn to null should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable.getCellValue(1, 3));

    try {
      recordTable.setCellValue(3, columns[3], "333224444");
    }
    catch (Exception e) {
      fail("Setting Jack Handy's ssn to 333224444 should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertEquals("333224444", recordTable.getCellValue(3, columns[3]));

    try {
      recordTable.setCellValue(0, 0, null);
      fail("Setting Jon Doe's personId to null should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertEquals(new Integer(1), recordTable.getCellValue(0, 0));
  }

  public void testToArray() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false)
    };

    final Object[][] data = {
      { new Integer(1), "Eric", "Cartman" },
      { new Integer(2), "Stan", "Marsh" },
      { new Integer(3), "Kyle", "Broflovski" },
      { new Integer(4), "Kenny", "McCormick" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record[] expectedArray = {
      getRecordInstance(columns, data[0]),
      getRecordInstance(columns, data[1]),
      getRecordInstance(columns, data[2]),
      getRecordInstance(columns, data[3])
    };

    TestUtil.assertEquals(expectedArray, recordTable.<com.cp.common.util.record.Record>toArray(new Record[recordTable.rowCount()]));
  }

  public void testToTabular() throws Exception {
    com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("gender", Gender.class),
      getColumnInstance("race", Race.class),
      getColumnInstance("ssn", String.class)
    };

    Object[][] data = {
      { new Integer(1), "Meg", "A", "Ryan", Gender.FEMALE, Race.WHITE, "111-11-1111" },
      { new Integer(2), "Paris", "B", "Hilton", Gender.FEMALE, Race.WHITE, "222-22-2222" },
      { new Integer(3), "Audrey", "C", "Hepburn", Gender.FEMALE, Race.WHITE, "333-33-3333" },
      { new Integer(4), "Ashley", "D", "Judd", Gender.FEMALE, Race.WHITE, "444-44-4444" },
      { new Integer(5), "Natalie", "E", "Portman", Gender.FEMALE, Race.WHITE, "555-55-5555" },
      { new Integer(6), "Halle", "F", "Berry", Gender.FEMALE, Race.BLACK, "666-66-6666" }
    };

    com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    TestUtil.assertEquals(data, recordTable.toTabular());

    final Record parisHilton = getRecordInstance(columns, data[1]);
    final com.cp.common.util.record.Record audreyHepburn = getRecordInstance(columns, data[2]);
    final com.cp.common.util.record.Record halleBerry = getRecordInstance(columns, data[5]);

    assertTrue(recordTable.contains(parisHilton));
    assertEquals(1, recordTable.removeRow(parisHilton));
    assertFalse(recordTable.contains(parisHilton));
    assertTrue(recordTable.contains(audreyHepburn));
    assertEquals(1, recordTable.removeRow(audreyHepburn));
    assertFalse(recordTable.contains(audreyHepburn));
    assertTrue(recordTable.contains(halleBerry));
    assertEquals(halleBerry, recordTable.removeRow(recordTable.getLastRowIndex()));
    assertFalse(recordTable.contains(halleBerry));

    final Column dob = getColumnInstance("dob", Calendar.class, true, 0, false, DateUtil.getCalendar(1975, Calendar.JULY, 1));

    assertNotNull(dob);
    assertEquals(0, recordTable.removeColumn(columns[0])); // remove personId
    assertEquals(1, recordTable.removeColumn(columns[2])); // remove middleInitial
    assertEquals(4, recordTable.removeColumn(columns[6])); // remove ssn
    assertTrue(recordTable.insertColumn(dob, 2)); // insert colunn dob as the 3rd column in the record table

    columns = new Column[] {
      columns[1], // firstName
      columns[3], // lastName
      dob,
      columns[4], // gender
      columns[5], // race
    };

    final Record avrilLavigne = getRecordInstance(columns, new Object[] { "Avril", "Lavigne", null, Gender.FEMALE, Race.WHITE });
    final Record britneyMurphey = getRecordInstance(columns, new Object[] { "Britney", "Murphey", null, Gender.FEMALE, Race.WHITE });

    assertNotNull(avrilLavigne);
    assertNotNull(britneyMurphey);
    assertTrue(recordTable.insertRow(avrilLavigne, 0));
    assertTrue(recordTable.addRow(britneyMurphey));

    avrilLavigne.setValue(dob, dob.getDefaultValue());
    britneyMurphey.setValue(dob, dob.getDefaultValue());

    data = new Object[][] {
      avrilLavigne.values().toArray(),
      { "Meg", "Ryan", dob.getDefaultValue(), Gender.FEMALE, Race.WHITE },
      { "Ashley", "Judd", dob.getDefaultValue(), Gender.FEMALE, Race.WHITE },
      { "Natalie", "Portman", dob.getDefaultValue(), Gender.FEMALE, Race.WHITE },
      britneyMurphey.values().toArray()
    };

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    TestUtil.assertEquals(data, recordTable.toTabular());

    recordTable.clear();

    assertTrue(recordTable.isEmpty());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());
    TestUtil.assertEquals(new Object[0][recordTable.columnCount()], recordTable.toTabular());
  }

  public void testToTabularForColumns() throws Exception {
    Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class)
    };

    Object[][] data = {
      { new Integer(43), "George", "W", "Bush", "111-11-1111" },
      { new Integer(42), "Bill", "J", "Clinton", "222-22-2222" },
      { new Integer(41), "George", "H.W.", "Bush", "333-33-3333" },
      { new Integer(40), "Ronald", "W", "Reagan", "444-44-4444" },
      { new Integer(39), "Jimmy", "E", "Carter", "555-55-5555" },
      { new Integer(38), "Gerald", "R", "Ford", "666-66-6666" },
      { new Integer(37), "Richard", "M", "Nixon", "777-77-7777" }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    TestUtil.assertEquals(data, recordTable.toTabular(columns));

    columns = new com.cp.common.util.record.Column[] { columns[3], columns[1], columns[2] }; // lastName, firstName middleInitial

    data = new Object[][] {
      { data[0][3], data[0][1], data[0][2] }, // Bush, George W
      { data[1][3], data[1][1], data[1][2] }, // Clinton, Bill J
      { data[2][3], data[2][1], data[2][2] }, // Bush, George H.W.
      { data[3][3], data[3][1], data[3][2] }, // Reagan, Rondald W
      { data[4][3], data[4][1], data[4][2] }, // Carter, Jimmy E
      { data[5][3], data[5][1], data[5][2] }, // Ford, Gerald R
      { data[6][3], data[6][1], data[6][2] }, // Nixon, Richard M
    };

    TestUtil.assertEquals(data, recordTable.toTabular(columns));

    final com.cp.common.util.record.Column dob = getColumnInstance("dob", Calendar.class);

    assertNotNull(dob);

    columns = new Column[] { columns[1], columns[2], columns[0], dob }; // firstName middleInitial lastName dob

    try {
      recordTable.toTabular(columns);
      fail("Calling RecordTable.toTabular(:[Column) should have thrown an IndexOutOfBoundsException for having the invalid dob column!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
  }

  public void testToTabularForRows() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("middleInitial", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("ssn", String.class)
    };

    Object[][] data = {
      { new Integer(43), "George", "W", "Bush", "111-11-1111" },
      { new Integer(42), "Bill", "J", "Clinton", "222-22-2222" },
      { new Integer(41), "George", "H.W.", "Bush", "333-33-3333" },
      { new Integer(40), "Ronald", "W", "Reagan", "444-44-4444" },
      { new Integer(39), "Jimmy", "E", "Carter", "555-55-5555" },
      { new Integer(38), "Gerald", "R", "Ford", "666-66-6666" },
      { new Integer(37), "Richard", "M", "Nixon", "777-77-7777" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    TestUtil.assertEquals(data, recordTable.toTabular(columns));

    int[] rowIndices = { 1, 4 }; // democratic candidates

    data = new Object[][] {
      data[1], // Bill Clinton
      data[4] // Jimmy Carter
    };

    TestUtil.assertEquals(data, recordTable.toTabular(rowIndices));

    rowIndices = new int[] { -1, 0, 1, 2, recordTable.getLastRowIndex() + 1 };

    try {
      recordTable.toTabular(rowIndices);
      fail("Calling RecordTable.toTabular(rowIndices:[J) should have thrown an IndexOutOfBoundsException for having the invalid row indices!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
  }

  public void testToTabularForRowsAndColumns() throws Exception {
    Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class),
      getColumnInstance("seat", Integer.class)
    };

    Object[][] data = {
      { "Andre", "Agassi", new Integer(7) },
      { "Leyton", "Hewitt", new Integer(3) },
      { "Andy", "Roddick", new Integer(2) },
      { "Roger", "Federer", new Integer(1) },
      { "James", "Blake", new Integer(22) }
    };

    int[] rowIndices = { 0, 1, 2, 3, 4 };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    TestUtil.assertEquals(data, recordTable.toTabular(rowIndices, columns));

    columns = new Column[] { columns[1], columns[0] }; // lastName, firstName

    rowIndices = new int[] { 0, 2, 4 }; // American Tennis Pros

    data = new Object[][] {
      { data[0][1], data[0][0] }, // Agassi, Andre
      { data[2][1], data[2][0] }, // Roddick, Andy
      { data[4][1], data[4][0] } // Blake, James
    };

    TestUtil.assertEquals(data, recordTable.toTabular(rowIndices, columns));

    final com.cp.common.util.record.Column dob = getColumnInstance("dateOfBirth", Calendar.class);

    assertNotNull(dob);

    columns = new Column[] { columns[1], columns[0], dob }; // firstName lastName dob
    rowIndices = new int[] { 0 };

    try {
      recordTable.toTabular(rowIndices, columns);
      fail("Calling RecordTable.toTabular(rowIndices:[J, columns:[Column) should have thrown an IndexOutOfBoundsException for an invalid column (" + dob + ")!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    columns = new Column[] { columns[1], columns[0] }; // lastName fistName
    rowIndices = new int[] { 0, 2, 5 };

    try {
      recordTable.toTabular(rowIndices, columns);
      fail("Calling RecordTable.toTabular(rowIndices:[J, columns:[Column) should have thrown an IndexOutOfBoundsException for an invalid row index of (5)!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.toTabular(null, columns);
      fail("Calling RecordTable.toTabular(rowIndices:[J, columns:[Column) with null rowIndices should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }

    try {
      recordTable.toTabular(rowIndices, null);
      fail("Calling RecordTable.toTabular(rowIndices:[J, columns:[Column) with null columns should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      // expected behavior!
    }
  }

  public void testValidateColumnIndex() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final AbstractRecordTable recordTable = (AbstractRecordTable) getRecordTableInstance(columns);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.validateColumnIndex(-1);
      fail("-1 is an invalid column index and validateColumnIndex should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnIndex(recordTable.getLastColumnIndex() + 1);
      fail("1 index past the last valid column index is an invalid column index and validateColumnIndex should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnIndex(0);
    }
    catch (IndexOutOfBoundsException e) {
      fail("0 is a valid column index and validateColumnIndex should not have thrown an IndexOutOfBoundsException!");
    }

    for (com.cp.common.util.record.Column column : columns) {
      assertEquals(0, recordTable.removeColumn(column));
    }

    assertEquals(0, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.validateColumnIndex(0);
      fail("0 is no longer a valid column index as the record table does not have any columns; validateColumnIndex should have thrown a IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
  }

  public void testValidateColumnValue() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("race", Race.class, false, 0, false, Race.WHITE),
      getColumnInstance("ssn", String.class, true, 11, true)
    };

    final Column personId = columns[0];
    final Column firstName = columns[1];
    final com.cp.common.util.record.Column dob = columns[3];
    final Column race = columns[4];
    final Column ssn = columns[5];

    final Object[][] data = {
      { new Integer(100), "Jon", "Doe", DateUtil.getCalendar(1974, Calendar.MAY, 27), Race.BLACK, "111-11-1111" },
      { new Integer(101), "Jane", "Doe", null, Race.BLACK, "222-22-2222" },
      { new Integer(200), "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.SEPTEMBER, 2), null, null },
      { new Integer(201), "Sandy", "Handy", null, null, null }
    };

    final AbstractRecordTable recordTable = (AbstractRecordTable) getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // set Jack and Sandy Handy's race to white so we can assert the contents of the record table
    data[2][4] = Race.WHITE;
    data[3][4] = Race.WHITE;

    int rowIndex = 0;
    for (Iterator<com.cp.common.util.record.Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    try {
      recordTable.validateColumnValue(personId, new Long(2000)); // invalid-type personId
      fail("Validating on personId having an invalid type (Long) should have thrown an InvalidColumnValueTypeException!");
    }
    catch (com.cp.common.util.record.InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(personId, null); // null personId
      fail("Validating on personId having a null value should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(personId, new Integer(200)); // non-unique personId
      fail("Validating on personId having a non-unique (200) value should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(personId, new Integer(2000)); // valid personId
    }
    catch (Exception e) {
      fail("Validating on personId with a non-null, unique, Integer value of 2000 should not have thrown an Exception!");
    }

    try {
      recordTable.validateColumnValue(firstName, new char[] { 'T', 'o', 'n', 'y' }); // invalid-type firstName
      fail("Validating on firstName having an invalid type (char array) should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(firstName, "Null"); // valid null String literal firstName
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      fail("Validating on firstName having the literal String 'Null' should not have thrown an Exception!");
    }

    try {
      recordTable.validateColumnValue(firstName, "Jon"); // valid non-unique firstName
    }
    catch (NonUniqueColumnValueException e) {
      fail("Validating on firstName having a non-unique value of 'Jon' should not have thrown an Exception!");
    }

    try {
      recordTable.validateColumnValue(firstName, "JackTheHandyManBubbaLongNameHippyFahker"); // invalid-size firstName
      fail("Validating on firstName with too large a value should have thrown an InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(dob, DateUtil.getCalendar(2005, Calendar.NOVEMBER, 22)); // valid Calendar dob
    }
    catch (Exception e) {
      fail("Validating on dob with a valid Calendar value should not have thrown an Exception!");
    }

    try {
      recordTable.validateColumnValue(dob, new Date());
      fail("Validating on dob having an invalid type (Date) should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(dob, null); // valid null dob
    }
    catch (NullColumnValueException e) {
      fail("Validating on dob having a null value should not have thrown a NullColumnValueException!");
    }

    try {
      recordTable.validateColumnValue(dob, DateUtil.getCalendar(1974, Calendar.MAY, 27)); // non-unique dob
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      fail("Validating on dob having a non-unique date of May 27th, 1974 should not have thrown a NonUniqueColumnValueException!");
    }

    try {
      recordTable.validateColumnValue(race, Race.HISPANIC); // valid non-null, unique race
    }
    catch (Exception e) {
      fail("Validating on race having a non-null, unique value of Hispanic should not have thrown an Exception!");
    }

    try {
      recordTable.validateColumnValue(race, "white");
      fail("Validating on race having invalid String literal ethnic value (white) should have thrown a InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expecte behavior!
    }

    try {
      recordTable.validateColumnValue(race, null); // null race
    }
    catch (NullColumnValueException e) {
      fail("Validating on race having a null value should not have thrown a NullColumnValueException; the race column is not unique and not nullable, but has a default value of white!");
    }

    try {
      recordTable.validateColumnValue(race, Race.BLACK);
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      fail("Validating on race with a non-null, non-unique value of black should not have thrown a NonUniqueColumnValueException!");
    }

    try {
      recordTable.validateColumnValue(ssn, new Long(333224444));
      fail("Validating on ssn having an invalid type of Long should have thrown a InvalidColumnValueTypeException!");
    }
    catch (com.cp.common.util.record.InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(ssn, null);
    }
    catch (NullColumnValueException e) {
      fail("Validating on ssn having a null value should not have thrown a NullColumnValueException!");
    }

    try {
      recordTable.validateColumnValue(ssn, "111-11-1111");
      fail("Validating on ssn with a non-unique value (111-11-1111) should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    try {
      recordTable.validateColumnValue(ssn, "333-22-4444");
    }
    catch (Exception e) {
      fail("Validating on ssn with a non-null, unique value (333-22-4444) should not have thrown an Exception!");
    }
  }

  public void testValidateColumnValueNullity() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 30, false),
      getColumnInstance("lastName", String.class, false, 50, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, false, 0, false, Race.WHITE)
    };

    final Object[][] data = {
      { new Integer(101), "Jack", "Black", DateUtil.getCalendar(1970, Calendar.APRIL,  18), Gender.MALE, Race.ASIAN },
      { new Integer(202), "Terry", "Olsen", DateUtil.getCalendar(1954, Calendar.OCTOBER, 24), Gender.MALE, Race.BLACK }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int rowIndex = 0;
    for (Iterator<Record> it = recordTable.rowIterator(); it.hasNext(); rowIndex++) {
      assertEquals(getRecordInstance(columns, data[rowIndex]), it.next());
    }

    // add a record with null first and last names to the record table
    final Record nullyNull = getRecordInstance(columns, new Object[] { new Integer(303), null, null,
      DateUtil.getCalendar(1970, Calendar.JANUARY, 22), null, Race.NATIVE_AMERICAN });

    assertNotNull(nullyNull);
    assertFalse(recordTable.contains(nullyNull));

    try {
      recordTable.addRow(nullyNull);
      fail("Adding a Record with null first and last names to the record table should have thrown a NullColumnValueException!");
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(nullyNull));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    // add a valid record (Jack Handy) to the record table
    com.cp.common.util.record.Record jackHandy = getRecordInstance(columns, new Object[] { new Integer(303), "Jack", "Handy",
      DateUtil.getCalendar(1965, Calendar.NOVEMBER, 21), Gender.MALE,  Race.WHITE });

    assertNotNull(jackHandy);
    assertFalse(recordTable.contains(jackHandy));

    try {
      recordTable.addRow(jackHandy);
    }
    catch (Exception e) {
      fail("Adding Jack Handy to the record table should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertTrue(recordTable.contains(jackHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    // try to set Jack Black's first and last name to null via the record table and record
    final Record jackBlack = recordTable.getRow(0);

    assertNotNull(jackBlack);
    assertEquals(new Integer(101), jackBlack.getValue(columns[0])); // personId
    assertEquals("Jack", jackBlack.getValue(columns[1])); // firstName
    assertEquals("Black", jackBlack.getValue(columns[2])); // lastName
    assertEquals(DateUtil.getCalendar(1970, Calendar.APRIL, 18), jackBlack.getValue(columns[3])); // dob
    assertEquals(Gender.MALE, jackBlack.getValue(columns[4])); // gender
    assertEquals(Race.ASIAN, jackBlack.getValue(columns[5])); // race

    try {
      recordTable.setCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[1]), null); // firstName
      fail("Calling RecordTable.setCellValue to set Jack Black's firstName to null should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    try {
      recordTable.setCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[2]), null); // lastName
      fail("Calling RecordTable.setCellValue to set Jack Black's lastName to null should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    assertEquals("Jack", jackBlack.getValue(columns[1])); // firstName
    assertEquals("Black", jackBlack.getValue(columns[2])); // lastName

    try {
      jackBlack.setValue(columns[1], null); // firstName
      fail("Calling Record.setValue to set Jack Black's firstName to null should have thrown a NullColumnValueException!");
    }
    catch (NullColumnValueException e) {
      // expected behavior!
    }

    try {
      jackBlack.setValue(columns[2], null); // lastName
      fail("Calling Record.setValue to set Jack Black's lastName to null should have thrown a NullColumnValueException!");
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      // expected behavior!
    }

    assertEquals("Jack", recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[1]))); // firstName
    assertEquals("Black", recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[2]))); // lastName

    // change Jack Black's first name to Joe
    try {
      recordTable.setCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[1]), "Joe"); // firstName
    }
    catch (Exception e) {
      fail("Calling RecordTable.setCellValue to change Jack Black's firstName to Joe should not have thrown an Exception!");
    }

    assertEquals("Joe", jackBlack.getValue(columns[1])); // firstName
    assertEquals("Black", jackBlack.getValue(columns[2])); // lastName

    // change Joe Black's last name to "Null"
    try {
      jackBlack.setValue(columns[2], "Null"); // lastName
    }
    catch (Exception e) {
      fail("Calling Record.setValue to change Joe Black's lastName to 'Null' should not have thrown an Exception!");
    }

    assertEquals("Joe", recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[1]))); // firstName
    assertEquals("Null", recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[2]))); // lastName

    // change Joe Null's other properties...
    assertEquals("Null", jackBlack.getValue(columns[2])); // lastName
    assertEquals(DateUtil.getCalendar(1970, Calendar.APRIL, 18), jackBlack.getValue(columns[3])); // dob
    assertEquals(Gender.MALE, jackBlack.getValue(columns[4])); // gender
    assertEquals(Race.ASIAN, jackBlack.getValue(columns[5])); // race

    try {
      jackBlack.setValue(columns[2], "Olsen"); // lastName
      jackBlack.setValue(columns[3], null); // dob
      jackBlack.setValue(columns[4], Gender.FEMALE); // gender
      jackBlack.setValue(columns[5], null); // race
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      fail("Setting Joe Null's dob and race to null should not have thrown a NullColumnValueException!");
    }
    catch (Exception e) {
      fail("Changing Joe Null's lastName to Olsen, gender to Female and dob & race to null should not have thrown an Exception!");
    }

    assertEquals("Olsen", recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[2]))); // lastName
    assertNull(recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[3]))); // dob
    assertEquals(Gender.FEMALE, recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[4]))); // gender
    assertEquals(Race.WHITE, recordTable.getCellValue(recordTable.getRowIndex(jackBlack), recordTable.getColumnIndex(columns[5]))); // race

    // set lastName's default value to "Doe"
    columns[2].setDefaultValue("Doe"); // lastName
    recordTable.getColumn("lastName").setDefaultValue("Doe");

    assertEquals("Doe", columns[2].getDefaultValue()); // lastName
    assertEquals("Doe", recordTable.getColumn("lastName").getDefaultValue());
    assertFalse(recordTable.getColumn("lastName").isNullable());
    assertFalse(recordTable.getColumn("lastName").isUnique());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    // again, try to add Record (Null Johnson) with null firstName to record table
    final Record nullJohnson = getRecordInstance(columns, new Object[] { new Integer(404), null, "Johnson", null, null, null });

    assertNotNull(nullJohnson);
    assertNull(nullJohnson.getValue(columns[1])); // firstName
    assertFalse(recordTable.contains(nullJohnson));

    try {
      recordTable.addRow(nullJohnson);
      fail("Adding Record null Johnson to the record table with a null firstName should have thrown a NullColumnValueException!");
    }
    catch (com.cp.common.util.record.NullColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(nullJohnson));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    // add Record (Jon Doe) having a lastName of null (defaulting to "Doe") to the record table
    com.cp.common.util.record.Record jonDoe = getRecordInstance(columns, new Object[] { new Integer(404), "Jon", null, null, Gender.MALE, null });

    assertNotNull(jonDoe);
    assertNull(jonDoe.getValue(columns[2])); // lastName
    assertFalse(recordTable.contains(jonDoe));

    try {
      assertTrue(recordTable.addRow(jonDoe));
    }
    catch (NullColumnValueException e) {
      fail("Adding Record (Jon Doe) with null lastName to the record table should not have thrown a NullColumnValueException; the record table should have defaulted Jon's lastName to Doe!");
    }

    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());

    jonDoe = recordTable.getRow(recordTable.getLastRowIndex());

    assertNotNull(jonDoe);
    assertEquals(new Integer(404), jonDoe.getValue(columns[0])); // personId
    assertEquals("Jon", jonDoe.getValue(columns[1])); // firstName
    assertEquals("Doe", jonDoe.getValue(columns[2])); // lastName
    assertNull(jonDoe.getValue(columns[3])); // dob
    assertEquals(Gender.MALE, jonDoe.getValue(columns[4])); // gender
    assertEquals(Race.WHITE, jonDoe.getValue(columns[5])); // race

    // allow first names to be null
    final com.cp.common.util.record.Column firstName = recordTable.getColumn("firstName");

    assertNotNull(firstName);
    assertEquals("firstName", firstName.getName());
    assertFalse(firstName.isNullable());
    assertFalse(columns[1].isNullable()); // firstName

    firstName.setNullable(true);
    columns[1].setNullable(true);

    assertTrue(firstName.isNullable());
    assertTrue(columns[1].isNullable()); // firstName

    final Record terryOlsen = recordTable.getRow(1);

    assertNotNull(terryOlsen);
    assertEquals("Terry", recordTable.getCellValue(recordTable.getRowIndex(terryOlsen), columns[1]));
    assertEquals("Olsen", recordTable.getCellValue(recordTable.getRowIndex(terryOlsen), columns[2]));

    try {
      recordTable.setCellValue(recordTable.getRowIndex(terryOlsen), columns[1], null);
    }
    catch (Exception e) {
      fail("Calling RecordTable.setCellValue should not have thrown an Exception (" + e.getMessage() + ") for setting Terry Olsen's first name to null!");
    }

    assertNull(terryOlsen.getValue(columns[1])); // firstName
    assertEquals("Olsen", terryOlsen.getValue(columns[2])); //lastName

    try {
      terryOlsen.setValue(columns[2], null);
    }
    catch (NullColumnValueException e) {
      fail("Calling Record.setValue to set null Olsen (formerly Terry Olsen) lastName to null should not have thrown a NullColumnValueException!");
    }

    assertNull(recordTable.getCellValue(recordTable.getRowIndex(terryOlsen), columns[1]));
    assertEquals("Doe", recordTable.getCellValue(recordTable.getRowIndex(terryOlsen), columns[2]));

    jackHandy = recordTable.getRow(2);

    assertNotNull(jackHandy);
    assertEquals("Jack", jackHandy.getValue(columns[1])); // firstName
    assertEquals("Handy", jackHandy.getValue(columns[2])); // lastName

    try {
      jackHandy.setValue(columns[1], null);
    }
    catch (Exception e) {
      fail("Calling Record.setValue should not have thrown an Exception (" + e.getMessage() + ") for setting Jack Handy's first name to null!");
    }

    assertNull(recordTable.getCellValue(recordTable.getRowIndex(jackHandy), columns[1])); // firstName
    assertEquals("Handy", recordTable.getCellValue(recordTable.getRowIndex(jackHandy), columns[2])); // lastName

    try {
      recordTable.getColumn("firstName").setNullable(false);
      fail("Setting the firstName column to non-nullable in the record table should have thrown a IllegalStateException!");
    }
    catch (IllegalStateException e) {
      // expected behavior!
    }
  }

  public void testValidateColumnValueSize() throws Exception {
    final Column[] columns = {
      getColumnInstance("accountNumber,", Integer.class, false, 5, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("ssn", String.class, true, 9, true),
      getColumnInstance("amount", Double.class, true, 6, false)
    };

    final Object[][] data = {
      { new Integer(99999), "Jack", "Handy", "333224444", new Double(880.45) }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record jordanTate = getRecordInstance(columns, new Object[] { new Integer(555), "Jordan", "Tate", "111223333", new Double(1000.01) });

    assertNotNull(jordanTate);
    assertFalse(recordTable.contains(jordanTate));

    try {
      recordTable.addRow(jordanTate);
      fail("Adding Record Jordan Tate to the record table with an amount of 1000.01 should have thrown a InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(jordanTate));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record jackHandy = recordTable.getRow(0);

    assertNotNull(jackHandy);
    assertEquals("Jack", jackHandy.getValue(columns[1])); // firstName
    assertEquals("Handy", jackHandy.getValue(columns[2])); // lastName

    try {
      jackHandy.setValue(columns[0], new Integer(100202)); // accountNumber
      fail("Setting Jack Handy's accountNumber to 100202 should have thrown an InvalidColumnValueSizeException!");
    }
    catch (com.cp.common.util.record.InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertEquals(new Integer(99999), jackHandy.getValue(columns[0])); // accountNumber

    try {
      jackHandy.setValue(columns[3], "333-22-4444"); // ssn
      fail("Setting Jack Handy's ssn value to '333-22-4444' with dashes should have thrown an InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertEquals("333224444", jackHandy.getValue(columns[3])); // ssn

    final Record jimDandy = getRecordInstance(columns, new Object[] { new Integer(-12345), "Jim", "Dandy", "555116666", new Double(51.75) });

    assertNotNull(jimDandy);
    assertFalse(recordTable.contains(jimDandy));

    try {
      recordTable.insertRow(jimDandy, 0);
      fail("Inserting Record Jim Dandy into the record table with an accountNumber of -12345 should have thrown a InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(jimDandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.setCellValue(recordTable.getRowIndex(jackHandy), columns[1], "JackWithTooLongOfAFirstName"); // firstName
      fail("Setting Jack Handy's firstName to 'JackWithTooLongOfAFirstName' should have thrown a InvalidColumnValueSizeException!");
    }
    catch (InvalidColumnValueSizeException e) {
      // expected behavior!
    }

    assertEquals("Jack", jackHandy.getValue(columns[1])); // firstName

    try {
      recordTable.setCellValue(recordTable.getRowIndex(jackHandy), columns[2], "Blackberrytinkletoestoothfairy"); // lastName
    }
    catch (com.cp.common.util.record.InvalidColumnValueSizeException e) {
      fail("Setting Jack Handy's lastName to 'Blackberrytinkletoestoothfairy' should not have thrown an InvalidColumnValueSizeException!");
    }

    assertEquals("Blackberrytinkletoestoothfairy", jackHandy.getValue(columns[2])); // lastName

    final com.cp.common.util.record.Record jonDoe = getRecordInstance(columns, new Object[] { new Integer(-9999), "Jon", "Doe", "123-45-67", new Double(8192.)});

    assertNotNull(jonDoe);
    assertFalse(recordTable.contains(jonDoe));

    try {
      assertTrue(recordTable.addRow(jonDoe));
    }
    catch (InvalidColumnValueSizeException e) {
      fail("Adding Record Jon Doe to the record table should not have thrown an InvalidColumnValueSizeException!");
    }

    assertTrue(recordTable.contains(jonDoe));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(2, recordTable.rowCount());
  }

  public void testValidateColumnValueType() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, false),
      getColumnInstance("dob", Calendar.class, true, 0, false),
      getColumnInstance("gender", Gender.class, true, 0, false),
      getColumnInstance("race", Race.class, true, 0, false)
    };

    final Object[][] data = {
      { new Integer(100), "Jon", "Doe", DateUtil.getCalendar(1969, Calendar.DECEMBER, 4), Gender.MALE, Race.BLACK }
    };

    final RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record jonDoe = recordTable.getRow(0);

    assertNotNull(jonDoe);
    assertEquals("Jon", jonDoe.getValue(columns[1])); // firstName
    assertEquals("Doe", jonDoe.getValue(columns[2])); // lastName

    try {
      jonDoe.setValue(columns[0], new Long(123456)); // personId
      fail("Setting Jon Doe's personId as a Long value should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertTrue(jonDoe.getValue(columns[0]) instanceof Integer); // personId
    assertEquals(new Integer(100), jonDoe.getValue(columns[0])); // personId

    final Record jackHandy = getRecordInstance(columns, new Object[] { new Short((short) 202), "Jack", "Handy", null, Gender.MALE, Race.WHITE });

    assertNotNull(jackHandy);
    assertFalse(recordTable.contains(jackHandy));

    try {
      recordTable.addRow(jackHandy);
      fail("Adding Record Jack Handy to the record table with a short personId type should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(jackHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.setCellValue(recordTable.getRowIndex(jonDoe), columns[3], DateUtil.getCalendar(1979, Calendar.JANUARY, 22).getTime()); // dob
      fail("Setting Jon Doe's dob as a Date value should have thrown an InvalidColumnValueTypeException!");
    }
    catch (com.cp.common.util.record.InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertTrue(jonDoe.getValue(columns[3]) instanceof Calendar); // dob
    assertEquals(DateUtil.getCalendar(1969, Calendar.DECEMBER, 4), jonDoe.getValue(columns[3])); // dob

    try {
      jonDoe.setValue(columns[4], "female"); // gender
      fail("Setting Jon Doe's gender to female as a String value should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertTrue(recordTable.<Object>getCellValue(recordTable.getRowIndex(jonDoe), columns[4]) instanceof Gender); // gender
    assertEquals(Gender.MALE, recordTable.getCellValue(recordTable.getRowIndex(jonDoe), columns[4])); // gender

    try {
      jonDoe.setValue(columns[5], null); // race
    }
    catch (InvalidColumnValueTypeException e) {
      fail("Setting Jon Doe's race to null should not have thrown an InvalidColumnValueTypeException!");
    }

    assertNull(jonDoe.getValue(columns[5])); // race
    assertNull(recordTable.getCellValue(recordTable.getRowIndex(jonDoe), columns[5])); // race
    assertFalse(jonDoe.getValue(columns[5]) instanceof Race);
    assertEquals(Race.class, recordTable.getColumn("race").getType());

    try {
      jonDoe.setValue(columns[1], new Character[] { 'J', 'o', 'n' });
      fail("Setting Jon Doe's firstName to 'Jon' as a Character array should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertTrue(jonDoe.getValue(columns[1]) instanceof String);
    assertEquals("Jon", jonDoe.getValue(columns[1])); // firstName

    final com.cp.common.util.record.Record sandyHandy = getRecordInstance(columns, new Object[] { new Integer(202), "Sandy", new Character[] { 'H', 'a', 'n', 'd', 'y' },
        DateUtil.getCalendar(1977, Calendar.APRIL, 14), Gender.FEMALE, null});

    assertNotNull(sandyHandy);
    assertFalse(recordTable.contains(sandyHandy));

    try {
      recordTable.insertRow(sandyHandy, 0);
      fail("Inserting Record Sandy Handy with Character array as her lastName value should have thrown an InvalidColumnValueTypeException!");
    }
    catch (InvalidColumnValueTypeException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(sandyHandy));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
  }

  public void testValidateColumnValueUniqueness() throws Exception {
    final Column[] columns = {
      getColumnInstance("personId", Integer.class, false, 0, true),
      getColumnInstance("firstName", String.class, false, 25, false),
      getColumnInstance("lastName", String.class, false, 35, true),
      getColumnInstance("ssn", String.class, true, 11, true)
    };

    final Object[][] data = {
      { new Integer(1), "Luke", "Skywalker", "333-22-4444" }
    };

    final com.cp.common.util.record.RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Record jadeWalker = getRecordInstance(columns, new Object[] { new Integer(2), "Jade", "Walker", "333-22-4444" });

    assertNotNull(jadeWalker);
    assertFalse(recordTable.contains(jadeWalker));

    try {
      recordTable.addRow(jadeWalker);
      fail("Adding Record Jade Walker with an non-unique ssn should have thrown an NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(jadeWalker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    com.cp.common.util.record.Record leiaOrgana = getRecordInstance(columns, new Object[] { new Integer(2), "Leia", "Organa", "333224444" });

    assertNotNull(leiaOrgana);
    assertFalse(recordTable.contains(leiaOrgana));

    try {
      assertTrue(recordTable.addRow(leiaOrgana));
    }
    catch (NonUniqueColumnValueException e) {
      fail("Adding Record Leia Organa should not have thrown a NonUniqueColumnValueException!");
    }

    assertTrue(recordTable.contains(leiaOrgana));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(2, recordTable.rowCount());

    leiaOrgana = recordTable.getRow(recordTable.getLastRowIndex());

    assertNotNull(leiaOrgana);
    assertEquals(new Integer(2), leiaOrgana.getValue(columns[0])); // personId
    assertEquals("Leia", leiaOrgana.getValue(columns[1])); // firstName
    assertEquals("Organa", leiaOrgana.getValue(columns[2])); // lastName
    assertEquals("333224444", leiaOrgana.getValue(columns[3])); // ssn

    try {
      leiaOrgana.setValue(columns[0], new Integer(1)); // personId
      fail("Setting Leia Organa's personId to 1 should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertEquals(new Integer(2), leiaOrgana.getValue(columns[0])); // personId

    try {
      recordTable.setCellValue(recordTable.getRowIndex(leiaOrgana), columns[3], "333-22-4444"); // ssn
      fail("Setting Leia Organa's ssn to '333-22-4444' should have thrown a NonUniqueColumnValueException!");
    }
    catch (NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertEquals("333224444", recordTable.getCellValue(recordTable.getRowIndex(leiaOrgana), columns[3])); // ssn

    final Record anakinSkywalker = getRecordInstance(columns, new Object[] { new Integer(3), "Anakin", "Skywalker", null });

    assertNotNull(anakinSkywalker);
    assertFalse(recordTable.contains(anakinSkywalker));

    try {
      recordTable.addRow(anakinSkywalker);
      fail("Adding Record Anakin Skywalker to the record table with duplicate lastName should have thrown a NonUniqueColumnValueException!");
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      // expected behavior!
    }

    assertFalse(recordTable.contains(anakinSkywalker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(2, recordTable.rowCount());

    final Record darthVader = getRecordInstance(columns, new Object[] { new Integer(3), "Darth", "Vader", null });

    assertNotNull(darthVader);
    assertFalse(recordTable.contains(darthVader));

    try {
      assertTrue(recordTable.addRow(darthVader));
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      fail("Adding Record Darth Vader to the record table should not have thrown a NonUniqueColumnValueException!");
    }

    assertTrue(recordTable.contains(darthVader));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(3, recordTable.rowCount());

    assertEquals("333224444", leiaOrgana.getValue(columns[3])); // ssn

    try {
      recordTable.setCellValue(recordTable.getRowIndex(leiaOrgana), columns[3], null); // ssn
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      fail("Setting Leia Organa's ssn to null should not have thrown a NonUniqueColumnValueException!");
    }

    assertNull(leiaOrgana.getValue(columns[3])); // ssn

    final com.cp.common.util.record.Column lastName = recordTable.getColumn("lastName");

    assertNotNull(lastName);
    assertEquals("lastName", lastName.getName());
    assertEquals("lastName", columns[2].getName());
    assertTrue(lastName.isUnique());
    assertTrue(columns[2].isUnique());

    lastName.setUnique(false);
    columns[2].setUnique(false);

    assertFalse(lastName.isUnique());
    assertFalse(columns[2].isUnique());

    final com.cp.common.util.record.Record shmiSkywalker = getRecordInstance(columns, new Object[] { new Integer(4), "Shmi", "Skywalker", null });

    assertNotNull(shmiSkywalker);
    assertFalse(recordTable.contains(shmiSkywalker));

    try {
      assertTrue(recordTable.addRow(shmiSkywalker));
    }
    catch (com.cp.common.util.record.NonUniqueColumnValueException e) {
      fail("Adding REcord Shmi Skywalker to the the record table should not have thrown a NonUniqueColumnValuException!");
    }

    assertTrue(recordTable.contains(shmiSkywalker));
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(4, recordTable.rowCount());
  }

  public void testValidateRowIndex() throws Exception {
    final com.cp.common.util.record.Column[] columns = {
      getColumnInstance("personId", Integer.class),
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { new Integer(100), "Jon", "Doe" },
      { new Integer(200), "Jane", "Doe" },
      { new Integer(300), "Jack", "Handy" },
      { new Integer(400), "Sandy", "Handy" }
    };

    final AbstractRecordTable recordTable = (AbstractRecordTable) getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    try {
      recordTable.validateRowIndex(-1);
      fail("-1 is not a valid row index and validateRowIndex should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.validateRowIndex(recordTable.getLastRowIndex() + 1);
      fail("1 index past the last valid row index is not valid and validateRowIndex should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }

    try {
      recordTable.validateRowIndex(0);
    }
    catch (IndexOutOfBoundsException e) {
      fail("Accessing a row @ index should not have caused validateRowIndex to throw an IndexOutOfBoundsException!");
    }

    recordTable.clear();

    assertTrue(recordTable.isEmpty());
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(0, recordTable.rowCount());

    try {
      recordTable.validateRowIndex(0);
      fail("The record table is empty and a row index of 0 is invalid and should have caused validateRowIndex to throw an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
    }
  }

}
