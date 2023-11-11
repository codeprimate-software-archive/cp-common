/*
 * RecordUtilTest.java (c) 18 January 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.util.record.RecordUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.util.record;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.util.DateUtil;
import java.util.Arrays;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecordUtilTest extends TestCase {

  private static final Log log = LogFactory.getLog(RecordUtilTest.class);

  private boolean flag = false;

  public RecordUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RecordUtilTest.class);
    //suite.addTest(new RecordUtilTest("testName"));
    return suite;
  }

  protected Column getColumnInstance(final String name, final Class type) {
    return new ColumnImpl(name, type);
  }

  protected Record getRecordInstance() {
    return AbstractRecordFactory.getInstance().getRecordInstance();
  }

  protected Record getRecordInstance(final Column[] columns, final Object[] data) {
    final Record record = getRecordInstance();
    int index = 0;
    for (Column column : columns) {
      record.addField(column.getName(), data[index++]);
    }
    return record;
  }

  protected RecordTable getRecordTableInstance() {
    return AbstractRecordFactory.getInstance().getRecordTableInstance();
  }

  protected RecordTable getRecordTableInstance(final Column[] columns, final Object[][] data) {
    final RecordTable recordTable = com.cp.common.util.record.AbstractRecordFactory.getInstance().getRecordTableInstance(columns);
    for (Object[] row : data) {
      recordTable.addRow(new RecordAdapter(getRecordInstance(columns, row), Arrays.<Column>asList(columns)));
    }
    return recordTable;
  }

  private Runnable getSynchronizedRecordRunnable(final Record record,
                                                 final String field,
                                                 final Object value,
                                                 final int sleepTime) {
    return new Runnable() {
      public void run() {
        synchronized (record) {
          synchronized (RecordUtilTest.this) {
            flag = true;
            RecordUtilTest.this.notifyAll();
          }
          try {
            if (sleepTime > 0) {
              Thread.sleep(sleepTime);
            }
            record.setValue(field, value);
          }
          catch (InterruptedException e) {
            fail("Failed to sleep for the specified amount of time (" + sleepTime
              + ") and set the field (" + field + ") to value (" + value + ")!");
          }
          catch (NoSuchFieldException e) {
            fail("The Record does NOT contain field (" + field + ")!");
          }
        }
      }
    };
  }

  private Runnable getSynchronizedRecordTableRunnable(final RecordTable recordTable,
                                                      final Column columnToInsert,
                                                      final int sleepTime) {
    return new Runnable() {
      public void run() {
        synchronized (recordTable) {
          synchronized (RecordUtilTest.this) {
            flag = true;
            RecordUtilTest.this.notifyAll();
          }
          try {
            if (sleepTime > 0) {
              Thread.sleep(sleepTime);
            }
            recordTable.insertColumn(columnToInsert, 0);
          }
          catch (InterruptedException e) {
            fail("Failed to insert column (" + columnToInsert + ") in the RecordTable!");
          }
        }
      }
    };
  }

  public void testSynchronizedRecord() throws Exception {
    Record record = getRecordInstance();
    record.addField("pin", new Integer(12345));
    record.addField("firstName", "Jack");
    record.addField("lastName", "Handy");
    record.addField("dob", DateUtil.getCalendar(1984, Calendar.SEPTEMBER, 21));

    assertNotNull(record);

    final Thread t0 = new Thread(getSynchronizedRecordRunnable(record, "firstName", "Jon", 200));
    flag = false;
    t0.start();
    synchronized (this) {
      wait();
    }

    assertTrue("Asserting the flag is true for firstName Thread!", flag);

    record.setValue("firstName", "Ben");
    t0.join();

    assertEquals("Jon", record.getValue("firstName"));

    // *** Synchronize the Record object! ***
    record = RecordUtil.synchronizedRecord(record);

    assertNotNull(record);

    final Thread t1 = new Thread(getSynchronizedRecordRunnable(record, "lastName", "Bon", 200));
    flag = false;
    t1.start();
    synchronized (this) {
      wait();
    }

    assertTrue("Asserting the flag is true for lastName Thread!", flag);

    record.setValue("lastName", "Doe");
    t1.join();

    assertEquals("Doe", record.getValue("lastName"));
  }

  public void testSynchronizedRecordTable() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Jon", "Doe" },
      { "Jack", "Handy" },
      { "Sandy", "Handy" }
    };

    RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    final Column gender = getColumnInstance("gender", String.class);
    final Column race = getColumnInstance("race", String.class);

    assertNotNull(gender);
    assertNotNull(race);
    assertFalse("The RecordTable did contain column (" + gender.getName() + ")!", recordTable.contains(gender));
    assertFalse("The RecordTable did contain column (" + race.getName() + ")!", recordTable.contains(race));

    final Thread t0 = new Thread(getSynchronizedRecordTableRunnable(recordTable, gender, 200));
    flag = false;
    t0.start();
    synchronized (this) {
      // Make sure Thread t0 starts up before we try to insert a column on the main Thread to ensure that
      // we synchronized around the RecordTable before inserting the race column on the main Thread!
      wait();
    }
    assertTrue("The flag is false!", flag);
    recordTable.insertColumn(race, 0);
    t0.join();

    assertTrue("The RecordTable did NOT contain column (" + gender.getName() + ")!", recordTable.contains(gender));
    assertTrue("The RecordTable did NOT contain column (" + race.getName() + ")!", recordTable.contains(race));
    assertEquals("Column (" + recordTable.getColumn(0).getName() + ") is the first column in the RecordTable, NOT "
      + gender.getName() + "!", 0, recordTable.getColumnIndex(gender));
    assertEquals("Column (" + recordTable.getColumn(1).getName() + ") is the second column in the RecordTable, NOT "
      + race.getName() + "!", 1, recordTable.getColumnIndex(race));

    // *** Synchronize the RecordTable! ***
    recordTable = RecordUtil.synchronizedRecordTable(recordTable);

    assertNotNull(recordTable);

    final Column personId = getColumnInstance("personId", Integer.class);
    final Column ssn = getColumnInstance("ssn", String.class);

    assertNotNull(personId);
    assertNotNull(ssn);
    assertFalse("The RecordTable did contain column (" + personId.getName() + ")!", recordTable.contains(personId));
    assertFalse("The RecordTable did contain column (" + ssn.getName() + ")!", recordTable.contains(ssn));

    final Thread t1 = new Thread(getSynchronizedRecordTableRunnable(recordTable, personId, 200));
    flag = false;
    t1.start();
    synchronized (this) {
      // Make sure Thread t1 starts up before we try to insert a column on the main Thread to ensure that
      // we synchronized around the RecordTable before inserting the ssn column on the main Thread!
      wait();
    }
    assertTrue("The flag was false!", flag);
    recordTable.insertColumn(ssn, 0);
    t1.join();

    assertTrue("The RecordTable did NOT contain column (" + personId.getName() + ")!", recordTable.contains(personId));
    assertTrue("The RecordTable did NOT contain column (" + ssn.getName() + ")!", recordTable.contains(ssn));
    assertEquals("Column (" + recordTable.getColumn(0).getName() + ") is the first column in the RecordTable, NOT "
      + ssn.getName() + "!", 0, recordTable.getColumnIndex(ssn));
    assertEquals("Column (" + recordTable.getColumn(1).getName() + ") is the second column in the RecordTable, NOT "
      + personId.getName() + "!", 1, recordTable.getColumnIndex(personId));
  }

  public void testUnmodifiableRecord() throws Exception {
    final Record originalRecord = AbstractRecordFactory.getInstance().getRecordInstance();
    originalRecord.addField("personId", new Integer(101));
    originalRecord.addField("firstName", "Jon");
    originalRecord.addField("middleInitial", "J");
    originalRecord.addField("lastName", "Doe");
    originalRecord.addField("ssn", "123-45-6789");
    originalRecord.addField("dob", DateUtil.getCalendar(1965, Calendar.NOVEMBER, 23));
    if (log.isDebugEnabled()) {
      log.debug("originalRecord (" + originalRecord + ")");
    }

    assertFalse(originalRecord.isEmpty());
    assertTrue(originalRecord.isMutable());

    // Set the Record as read-only!
    final Record readOnlyRecord = RecordUtil.unmodifiableRecord(originalRecord);
    assertNotNull(readOnlyRecord);
    assertFalse(readOnlyRecord.isEmpty());
    assertFalse(readOnlyRecord.isMutable());
    assertEquals(originalRecord, readOnlyRecord);
    log.debug("originalRecord and readOnlyRecord setup complete!");

    // First, try to add a new field with addField(:String, :Object) and addField(:String)
    try {
      readOnlyRecord.addField("gender", "Male");
      fail("Record.addField(:String, :Object) should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // This is the expected behavior!
    }

    assertFalse(readOnlyRecord.containsField("gender"));
    log.debug("Did not contain \"gender\".");

    try {
      readOnlyRecord.addField("age");
      fail("Record.addField(:String) should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // This is the expected behavior!
    }

    assertFalse(readOnlyRecord.containsField("age"));
    log.debug("Did not contain \"age\".");

    // Second, try removing a field with removeField(I) and removeField(:String)
    try {
      readOnlyRecord.removeField(readOnlyRecord.getFieldIndex("firstName"));
      fail("Record.removeField(I) should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // This is the expected behavior!
    }

    assertTrue(readOnlyRecord.containsField("firstName"));
    log.debug("\"firstName\" was not removed.");

    try {
      readOnlyRecord.removeField("lastName");
      fail("Record.removeField(:String) should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // This is the expected behavior!
    }

    assertTrue(readOnlyRecord.containsField("lastName"));
    log.debug("\"lastName\" was not removed.");

    try {
      readOnlyRecord.removeField("age");
      fail("Record.removeField(:String) should have thrown an ObjectImmutableException!");
    }
    catch (ObjectImmutableException e) {
      // This is the expected behavior!
    }

    assertFalse(readOnlyRecord.containsField("age"));
    log.debug("\"age\" was not removed.");
    assertEquals(originalRecord, readOnlyRecord);

    // Third, try modifying the value of one of readOnlyRecord's fields, but do not modify
    // readOnlyRecord itsetlf.
    try {
      final Calendar dob = readOnlyRecord.getCalendarValue("dob");
      dob.set(Calendar.YEAR, 1975);
    }
    catch (ObjectImmutableException e) {
      fail("Modifying the \"value\" of the dob (Date of Birth) field value should not have thrown an ObjectImmutableException!");
    }

    // NOTE: the Record object's did not change; only the field value was modified with a reference
    // to the field, which both the originalRecord and readOnlyRecord share a reference to.
    assertEquals(originalRecord.size(), readOnlyRecord.size());
    // NOTE: the originalRecord and readOnlyRecord refer to the same Calendar object in memory
    // for the dob (Date of Birth) field, and thus both will be modified.
    assertEquals(originalRecord, readOnlyRecord);
    log.debug("Successfully modified dob (Date of Birth) field.");

    // Finally, add a new field to the originalRecord and assert that it can be modified
    // independently of the readOnlyRecord.
    try {
      originalRecord.addField("gender", "Male");
    }
    catch (ObjectImmutableException e) {
      fail("The addField(:String, :Object) should not have thrown an ObjectImmutaleException!");
    }

    assertTrue(originalRecord.size() != readOnlyRecord.size());
    assertFalse(originalRecord.equals(readOnlyRecord));
    log.debug("Added field to originalRecord!");

    // Ensure that mutability cannot be modified!
    try {
      readOnlyRecord.setMutable(Mutable.MUTABLE);
      fail("setMutable(B) should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      // This is the expected behavior!
    }
  }

  public void testUnmodifiableRecordTable() throws Exception {
    final Column[] columns = {
      getColumnInstance("firstName", String.class),
      getColumnInstance("lastName", String.class)
    };

    final Object[][] data = {
      { "Samantha", "Summers" },
      { "Nicole", "Smith" }
    };

    RecordTable recordTable = getRecordTableInstance(columns, data);

    assertNotNull(recordTable);
    assertEquals(columns.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());
    assertTrue("The RecordTable is not mutable!", recordTable.isMutable());

    // Test removing Nicole Smith from the RecordTable!
    final Record nicoleSmith = new RecordAdapter(getRecordInstance(columns, data[1]), Arrays.<Column>asList(columns));

    assertNotNull(nicoleSmith);
    assertTrue("Asserting that RecordTable contains row/Record (" + nicoleSmith + ") before removing!", recordTable.contains(nicoleSmith));

    try {
      recordTable.removeRow(recordTable.getRowIndex(nicoleSmith));
    }
    catch (ObjectImmutableException e) {
      fail("Calling RecordTable.removeRow should not have thrown an ObjectImmutableException for removing Nicole Smith from the table!");
    }

    assertFalse("Asserting that RecordTable contains row/Record (" + nicoleSmith + ") after removing!", recordTable.contains(nicoleSmith));
    assertEquals("Asserting rowCount after removing Nicole Smith!", data.length - 1, recordTable.rowCount());

    // Test inserting a Column!
    final Column personId = getColumnInstance("personId", Integer.class);

    assertNotNull(personId);
    assertFalse("Asserting that RecordTable contains Column (" + personId.getName() + ")!", recordTable.contains(personId));

    try {
      recordTable.insertColumn(personId, 0);
    }
    catch (ObjectImmutableException e) {
      fail("Calling RecordTable.addColumn should not have thrown an ObjectImmutableException for inserting the personId column!");
    }

    assertTrue("The RecordTable did not contain column (" + personId.getName() + ")!", recordTable.contains(personId));
    assertEquals(columns.length + 1, recordTable.columnCount());
    assertEquals(personId, recordTable.getFirstColumn());

    // Test modifying a RecordTable cell value!
    final Record samantha = recordTable.getFirstRow();

    assertNotNull(samantha);
    assertTrue("Asserting the RecordTable contains Samantha!", recordTable.contains(samantha));
    assertEquals("Asserting rowIndex of Samanatha is 0!", 0, recordTable.getRowIndex(samantha));
    assertEquals("Samantha", samantha.getValue(columns[0]));
    assertEquals("Summers", samantha.getValue(columns[1]));
    assertEquals("Summers", recordTable.getCellValue(recordTable.getRowIndex(samantha), recordTable.getColumnIndex(columns[1])));

    try {
      recordTable.setCellValue(recordTable.getRowIndex(samantha), recordTable.getColumnIndex(columns[1]), "Swanson");
    }
    catch (ObjectImmutableException e) {
      fail("The RecordTable.setCellValue should NOT have thrown an ObjectImmutableException for changing Samantha Summers last name to Swanson!");
    }

    assertEquals("Swanson", recordTable.getCellValue(recordTable.getRowIndex(samantha), recordTable.getColumnIndex(columns[1])));
    assertEquals("Swanson", samantha.getValue(columns[1]));

    // *** Set the RecordTable to Read-Only! ***
    recordTable = RecordUtil.unmodifiableRecordTable(recordTable);

    assertNotNull(recordTable);
    assertEquals("Testing columnCount after setting the RecordTable to read-only!", columns.length + 1, recordTable.columnCount());
    assertEquals("Testing rowCount after setting the RecordTable to read-only!", data.length - 1, recordTable.rowCount());
    assertFalse("The RecordTable is Mutable!", recordTable.isMutable());

    // Try adding the Record Bob Smith to the RecordTable.
    final Column[] customColumns = { personId, columns[0], columns[1] };
    final Record bobSmith = new RecordAdapter(getRecordInstance(customColumns, new Object[] { new Integer(101), "Bob", "Smith" }),
      Arrays.<Column>asList(customColumns));

    assertEquals(1, recordTable.rowCount());
    assertFalse("Asserting that RecordTable does not contain row/Record (" + bobSmith + ") before insert!", recordTable.contains(bobSmith));

    try {
      recordTable.addRow(bobSmith);
      fail("Calling RecordTable.addRow should have thrown an ObjectImmutableException for trying to add the Bob Smith Record!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    assertFalse("Assert that the RecordTable does NOT contain Record (" + bobSmith + ") after insert!", recordTable.contains(bobSmith));
    assertEquals(1, recordTable.rowCount());

    // Test removing the personId Column from the RecordTable!
    assertEquals(3, recordTable.columnCount());
    assertTrue("Asserting the RecordTable contains Column (" + personId.getName() + ")!", recordTable.contains(personId));

    try {
      recordTable.removeColumn(recordTable.getColumnIndex(personId));
      fail("The RecordTable.removeColumn method should have thrown an ObjectImmutableException for trying to remove the personId Column!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    assertTrue("Asserting the RecordTable contains Column (" + personId.getName() + ")!", recordTable.contains(personId));
    assertEquals(3, recordTable.columnCount());

    // Try modifying a cell value of the RecordTable!
    final Record samantha2 = recordTable.getFirstRow();

    assertNotNull(samantha2);
    assertEquals("Samantha", samantha2.getValue(columns[0]));
    assertEquals("Swanson", samantha2.getValue(columns[1]));
    assertTrue("Asserting the RecordTable contains Samantha2!", recordTable.contains(samantha2));
    assertEquals("Asserting the rowIndex of Samantha2 is 0!", 0, recordTable.getRowIndex(samantha2));
    assertEquals("Asserting Samantha and Samantha2 are equal!", samantha2, samantha);
    assertTrue(recordTable.contains(samantha));
    assertEquals("Asserting rowIndex of Samantha is 0!", 0, recordTable.getRowIndex(samantha));
    assertEquals("Swanson", recordTable.getCellValue(0, 2));

    try {
      recordTable.setCellValue(0, 2, "Kind");
      fail("The RecordTable.setCellValue should have thrown an ObjectImmutableException for setting the last name of Samantha Swanson to Kind!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    assertEquals("Swanson", recordTable.getCellValue(0, 2));
    assertEquals("Swanson", samantha.getValue(columns[1]));
    assertEquals("Swanson", samantha2.getValue(columns[1]));

    // Assert that the RecordTable mutable property cannot be changed!
    try {
      recordTable.setMutable(true);
      fail("The RecordTable.setMutable method should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      // expected behavior
    }

    assertFalse("Asserting the RecordTable is Immutable!", recordTable.isMutable());
  }

}
