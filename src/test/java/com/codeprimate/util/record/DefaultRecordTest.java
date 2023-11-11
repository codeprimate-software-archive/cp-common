/*
 * DefaultRecordTest.java (c) 18 March 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.codeprimate.util.record.DefaultRecord
 * @see junit.framework.TestCase
 */

package com.codeprimate.util.record;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.util.DateUtil;
import com.cp.common.util.record.AbstractRecordTest;
import com.cp.common.util.record.Record;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DefaultRecordTest extends AbstractRecordTest {

  public DefaultRecordTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultRecordTest.class);
    //suite.addTest(new DefaultRecordTest("testName"));
    return suite;
  }

  public void testAddField() throws Exception {
    final Record record = new DefaultRecord();
    record.addField("personId", null);
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");
    record.addField("middleInitial");

    assertTrue(record.isMutable());
    assertTrue(record.containsField("personId"));
    assertTrue(record.containsField("firstName"));
    assertFalse(record.containsField("FirstName"));
    assertFalse(record.containsField("1stName"));
    assertTrue(record.containsField("lastName"));
    assertTrue(record.containsField("middleInitial"));
    assertFalse(record.containsField("dateOfBirth"));
    assertEquals("Jon", record.getValue("firstName"));
    assertEquals("Doe", record.getValue("lastName"));
    assertNull(record.getValue("personId"));
    assertNull(record.getValue("middleInitial"));

    try {
      record.getValue("dateOfBirth");
      fail("The Record.getValue method should have thrown a NoSuchFieldException for the dateOfBirth field!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior!
    }

    record.setMutable(Mutable.IMMUTABLE);

    assertFalse(record.isMutable());

    try {
      record.addField("dateOfBirth");
      fail("The Record.addField method should have thrown a ObjectImmutableException for an immutable Record object!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    record.setMutable(Mutable.MUTABLE);

    assertTrue(record.isMutable());

    try {
      record.addField("ssn", "123-45-6789");
    }
    catch (ObjectImmutableException e) {
      fail("The Record.addField method should NOT have thrown an ObjectImmutableException for a mutable Record object!");
    }

    assertTrue(record.containsField("ssn"));
    assertEquals("123-45-6789", record.getValue("ssn"));

    try {
      record.addField(null, "05/12/1975");
      fail("The Record.addField method should have thrown a IllegalArgumentException for trying to add a NULL field!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    try {
      record.addField("");
      fail("The Record.addField method should have thrown a IllegalArgumentException for trying to add an empty String as a Field!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    record.addField("personId", new Integer(101));
    record.addField("lastName", "Handy");

    assertNull(record.getValue("personId"));
    assertEquals("Doe", record.getValue("lastName"));

    try {
      record.put(new Integer(202), "Tim Johnson");
      fail("The Record.put method should have thrown a ClassCastException; fields can only be of type String!");
    }
    catch (ClassCastException e) {
      // expected behavior
    }
  }

  public void testGetFieldAndGetFieldIndex() throws Exception {
    final com.cp.common.util.record.Record record = new DefaultRecord();
    record.addField("personId", new Integer(101));
    record.addField("firstName", "Jack");
    record.addField("lastName", "Handy");
    record.addField("dob", DateUtil.getCalendar(2000, Calendar.JUNE, 27));

    assertEquals("firstName", record.getField(1));
    assertEquals("personId", record.getField(0));
    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals(2, record.getFieldIndex("lastName"));
    assertEquals(3, record.getFieldIndex("dob"));

    try {
      record.getField(5);
      fail("The Record.getField method should have thrown a IndexOutOfBoundsException for index of 5!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior
    }

    try {
      record.getField(-2);
      fail("The Record.getField method should have thrown a IndexOutOfBoundsException for index of -2!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior
    }

    record.remove("firstName");

    assertFalse(record.containsField("firstName"));
    assertEquals("personId", record.getField(0));
    assertEquals("dob", record.getField(2));
    assertEquals("lastName", record.getField(1));
    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals(1, record.getFieldIndex("lastName"));
    assertEquals(2, record.getFieldIndex("dob"));

    try {
      record.getFieldIndex("firstName");
      fail("The Record.getFieldIndex method should have thrown a NoSuchFieldException; firstName is not a valid field!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    record.addField("firstName");

    assertTrue(record.containsField("firstName"));
    assertEquals("firstName", record.getField(3));
    assertEquals("lastName", record.getField(1));
    assertEquals(3, record.getFieldIndex("firstName"));
    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals(2, record.getFieldIndex("dob"));
    assertEquals(1, record.getFieldIndex("lastName"));

    try {
      record.addField(null, "Some Value!");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertEquals("firstName", record.getField(3));
    assertEquals("lastName", record.getField(1));
    assertEquals("personId", record.getField(0));
    assertEquals("dob", record.getField(2));
    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals(3, record.getFieldIndex("firstName"));

    try {
      record.addField("");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertEquals("lastName", record.getField(1));
    assertEquals("dob", record.getField(2));
    assertEquals("firstName", record.getField(3));
    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals(3, record.getFieldIndex("firstName"));

    record.put("firstName", "Jon");
    record.remove("dob");
    record.put("ssn", "123-45-6789");

    assertEquals(0, record.getFieldIndex("personId"));
    assertEquals("personId", record.getField(0));
    assertEquals(1, record.getFieldIndex("lastName"));
    assertEquals("lastName", record.getField(1));
    assertEquals(2, record.getFieldIndex("firstName"));
    assertEquals("firstName", record.getField(2));
    assertEquals(3, record.getFieldIndex("ssn"));
    assertEquals("ssn", record.getField(3));
  }

  public void testGetValueAndSetValue() throws Exception {
    final Record record = new DefaultRecord();
    record.addField("personId", new Integer(404));
    record.addField("firstName", "Jane");
    record.addField("lastName", "Doe");
    record.addField("dob", DateUtil.getCalendar(1982, Calendar.AUGUST, 14));

    assertEquals(new Integer(404), record.getValue("personId"));
    assertEquals("Jane", record.getValue("firstName"));
    assertEquals("Doe", record.getValue("lastName"));
    assertEquals(DateUtil.getCalendar(1982, Calendar.AUGUST, 14), record.getValue("dob"));

    record.setValue("firstName", "Pie");
    record.setValue("dob", null);

    assertEquals("Pie", record.getValue("firstName"));
    assertNull(record.getValue("dob"));

    try {
      record.getValue("FirstName");
      fail("The Record.getValue method should have thrown a NoSuchFieldException for 'FirstName'!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    try {
      record.getValue("");
      fail("The Record.getValue method should have thrown a NoSuchFieldException for an empty field name!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    try {
      record.getValue(null);
      fail("The Record.getValue method should have thrown a NoSuchFieldException for a NULL field name!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    try {
      record.getValue("dateOfBirth");
      fail("The Record.getValue method should have thrown a NoSuchFieldException for 'dateOfBirth'!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    try {
      record.setValue("LastName", "Dole");
      fail("The Record.setValue method should have thrown a NoSuchFieldException for 'LastName'!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

    assertEquals("Doe", record.getValue("lastName"));

    record.setMutable(Mutable.IMMUTABLE);

    assertFalse(record.isMutable());

    try {
      assertEquals("Pie", record.getValue("firstName"));
    }
    catch (ObjectImmutableException e) {
      fail("The Record.getValue method should NOT have thrown an ObjectImmutableException on a read operation of an immutable Record object!");
    }

    try {
      record.setValue("firstName", "Jon");
      fail("The Record.setValue method should have thrown an ObjectImmutableException on the write operation of an immutable Record object!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    record.setMutable(Mutable.MUTABLE);

    assertTrue(record.isMutable());
    assertTrue(record.containsField("dob"));
    assertEquals(record.getValue("dob"), record.removeField("dob"));
    assertFalse(record.containsField("dob"));

    try {
      record.getValue("dob");
      fail("The Record.getValue method should have thrown a NoSuchFieldException on the 'dob' field!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }

  }

  public void testRemoveField() throws Exception {
    final Record record = new DefaultRecord();
    record.addField("personId", null);
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");
    record.addField("middleInitial");

    assertTrue(record.isMutable());
    assertTrue(record.containsField("personId"));
    assertTrue(record.containsField("firstName"));
    assertTrue(record.containsField("lastName"));
    assertTrue(record.containsField("middleInitial"));

    record.removeField("middleInitial");

    assertFalse(record.containsField("middleInitial"));

    record.setMutable(Mutable.IMMUTABLE);

    assertFalse(record.isMutable());

    try {
      record.removeField("firstName");
      fail("The Record.removeField should have thrown a ObjectImmutableException for an immutable Record object!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    record.setMutable(Mutable.MUTABLE);

    assertTrue(record.isMutable());

    Object value = null;
    try {
      record.setValue("personId", new Integer(202));
      value = record.removeField("personId");
    }
    catch (ObjectImmutableException e) {
      fail("The Record.removeField should NOT have thrown a ObjectImmutableException for removing the personId field!");
    }

    assertFalse(record.containsField("personId"));
    assertEquals(new Integer(202), value);

    try {
      value = record.removeField("FirstName");
      fail("The Record.removeField method should have thrown a NoSuchFieldException for FirstName!");
    }
    catch (NoSuchFieldException e) {
      // expected behavior
    }
  }

  public void testSize() throws Exception {
    final com.cp.common.util.record.Record record = new com.codeprimate.util.record.DefaultRecord();
    record.addField("personId", null);
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");
    record.addField("middleInitial");

    assertTrue(record.containsField("personId"));
    assertTrue(record.containsField("firstName"));
    assertTrue(record.containsField("lastName"));
    assertTrue(record.containsField("middleInitial"));
    assertEquals(4, record.size());

    record.removeField("personId");

    assertFalse(record.containsField("personId"));
    assertEquals(3, record.size());

    record.addField("firstName", "Jack");
    try {
      record.addField(null, "Value for null field!");
    }
    catch (IllegalArgumentException e) {
      // ignore!
    }

    try {
      record.addField("");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertTrue(record.containsField("firstName"));
    assertEquals("Jon", record.getValue("firstName"));
    assertFalse(record.containsField(null));
    assertFalse(record.containsField(""));
    assertEquals(3, record.size());

    record.addField("dob", "05/27/1974");
    record.addField("ssn", "123-45-6789");
    record.setMutable(Mutable.IMMUTABLE);

    assertFalse(record.isMutable());

    try {
      record.addField("pin", new Integer(12345));
    }
    catch (ObjectImmutableException e) {
      // ignore
    }

    assertTrue(record.containsField("dob"));
    assertTrue(record.containsField("ssn"));
    assertFalse(record.containsField("pin"));
    assertEquals(5, record.size());

    try {
      record.removeField("lastName");
    }
    catch (ObjectImmutableException e) {
      // ignore
    }

    assertTrue(record.containsField("lastName"));
    assertEquals(5, record.size());

    record.setMutable(Mutable.MUTABLE);

    assertTrue(record.isMutable());

    record.addField("FirstName", "Jack");

    assertNull(record.removeField("middleInitial"));
    assertEquals("05/27/1974", record.removeField("dob"));
    assertEquals("123-45-6789", record.removeField("ssn"));
    assertTrue(record.containsField("FirstName"));
    assertTrue(record.containsField("firstName"));
    assertFalse(record.containsField("1stName"));
    assertFalse(record.containsField("FIRSTName"));
    assertFalse(record.containsField("middleInitial"));
    assertFalse(record.containsField("dob"));
    assertFalse(record.containsField("ssn"));
    assertEquals("Jon", record.getValue("firstName"));
    assertEquals("Jack", record.getValue("FirstName"));
    assertEquals(3, record.size());
  }

}
