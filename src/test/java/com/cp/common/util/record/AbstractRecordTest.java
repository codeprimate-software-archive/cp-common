/*
 * AbstractRecordTest.java (c) 18 March 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.util.record.AbstractRecord
 * @see junit.framework.TestCase
 */

package com.cp.common.util.record;

import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;

public class AbstractRecordTest extends TestCase {

  public AbstractRecordTest(final String testName) {
    super(testName);
  }

  private Map.Entry getMapEntry(final Map.Entry mapEntry) {
    return getMapEntry(mapEntry.getKey(), mapEntry.getValue());
  }

  private Map.Entry getMapEntry(final Object key, final Object value) {
    return new Map.Entry() {
      public Object getKey() {
        return key;
      }
      public Object getValue() {
        return value;
      }
      public Object setValue(Object value) {
        throw new UnsupportedOperationException("Not Implemented!");
      }
    };
  }

  private Record getRecordInstance() {
    return AbstractRecordFactory.getInstance().getRecordInstance();
  }

  private Record getRecordInstance(final Record record) {
    return com.cp.common.util.record.AbstractRecordFactory.getInstance().getRecordInstance(record);
  }

  public void testClone() throws Exception {
    final Record originalRecord = getRecordInstance();
    originalRecord.addField("personId", new Integer(101));
    originalRecord.addField("firstName", "Jon");
    originalRecord.addField("lastName", "Doe");
    originalRecord.addField("middleInitial");
    originalRecord.addField("dob", DateUtil.getCalendar(1974, Calendar.MAY, 27));

    Record recordCopy = getRecordInstance(originalRecord);

    assertNotNull(recordCopy);
    assertEquals(originalRecord, recordCopy);

    recordCopy.setValue("firstName", "Jack");
    recordCopy.setValue("lastName", "Handy");
    recordCopy.setValue("dob", DateUtil.getCalendar(1988, Calendar.AUGUST, 21));

    assertEquals("Jon", originalRecord.getValue("firstName"));
    assertEquals("Doe", originalRecord.getValue("lastName"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27), originalRecord.getValue("dob"));
    assertFalse(originalRecord.equals(recordCopy));

    recordCopy = getRecordInstance(originalRecord);

    assertNotNull(recordCopy);
    assertEquals(originalRecord, recordCopy);

    recordCopy.addField("xfactor", new Double(3.14159));
    recordCopy.removeField("middleInitial");

    assertFalse(originalRecord.containsField("xfactor"));
    assertTrue(originalRecord.containsField("middleInitial"));
    assertFalse(originalRecord.equals(recordCopy));
  }

  public void testCompareTo() throws Exception {
    final Record record0 = getRecordInstance();
    record0.addField("firstName", "Jack");
    record0.addField("mi", "R");
    record0.addField("lastName", "Handy");
    record0.addField("dob", DateUtil.getCalendar(1965, Calendar.OCTOBER, 24));
    record0.registerComparator(Calendar.class, DateUtil.getCalendarComparator());

    final Record record1 = getRecordInstance();
    record1.addField("firstName", "Sandy");
    record1.addField("lastName", "Handy");
    record1.addField("mi", "P");
    record1.addField("dob", DateUtil.getCalendar(1971, Calendar.APRIL, 3));
    record1.registerComparator(Calendar.class, DateUtil.getCalendarComparator());

    assertEquals(0, record0.compareTo(record0));
    TestUtil.assertNegative(record0.compareTo(record1));
    TestUtil.assertPositive(record1.compareTo(record0));
    assertEquals(0, record1.compareTo(record1));

    record1.addField("ssn", "123-45-6789");
    record1.addField("pin", new Integer(12345));

    try {
      record0.compareTo(record1);
      fail("The Record.compareTo method should have thrown a IllegalArgumentException for incompatible Record structures!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    record1.remove("ssn");
    record1.remove("pin");
    record1.addField("dateOfBirth", record1.remove("dob"));

    try {
      record0.compareTo(record1);
      fail("The Record.compareTo method should have thrown a IllegalArgumentException for incompatible Record structures!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    record0.remove("dob");

    try {
      record0.compareTo(record1);
      fail("The Record.compareTo method should have thrown a IllegalArgumentException for incompatible Record structures!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior
    }

    try {
      record1.compareTo(new Integer(1000));
      fail("The Record.compareTo method should have thrown a ClassCastException since Integer is not comparable with Record!");
    }
    catch (ClassCastException e) {
      // expected behavior
    }
  }

  public void testContainsField() throws Exception {
    final Record record = getRecordInstance();
    record.addField("personId", new Integer(101));
    record.addField("firstName", "Jack");
    record.addField("lastName", "Handy");
    record.addField("dob", DateUtil.getCalendar(1991, Calendar.DECEMBER, 12));

    assertTrue(record.containsField("personId"));
    assertTrue(record.containsField("firstName"));
    assertTrue(record.containsField("lastName"));
    assertTrue(record.containsField("dob"));
    assertFalse(record.containsField("pid"));
    assertFalse(record.containsField("person"));
    assertFalse(record.containsField("personID"));
    assertFalse(record.containsField("FirstName"));
    assertFalse(record.containsField("lasName"));
    assertFalse(record.containsField("dateOfBirth"));
    assertFalse(record.containsField("middleInitial"));
    assertFalse(record.containsField("ssn"));

    record.addField("ssn", "123-45-6789");

    assertTrue(record.containsField("ssn"));

    try {
      record.addField("");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertFalse(record.containsField(""));

    try {
      record.addField(null, "Some Value!");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertFalse(record.containsField(null));
  }

  public void testEntrySetAdd() throws Exception {
    final Record record = getRecordInstance();
    record.addField("personId", new Integer(101));
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");

    final Set entrySet = record.entrySet();

    assertFalse(record.containsField("middleInitial"));
    entrySet.add(getMapEntry("middleInitial", null));
    assertTrue(record.containsField("middleInitial"));
    assertNull(record.getValue("middleInitial"));

    entrySet.add(getMapEntry("firstName", "Jack"));

    assertTrue(record.containsField("firstName"));
    assertEquals("Jon", record.getValue("firstName"));

    try {
      entrySet.add(getMapEntry(null, "null field value!"));
      fail("The Record.entrySet.add method should have thrown a IllegalArgumentException when attempting to add a NULL field!");
    }
    catch (IllegalArgumentException e) {
      // expected bahavior
    }

    try {
      entrySet.add(getMapEntry("", null));
      fail("The Record.entrySet.add method should have thrown a IllegalArgumentException when attempting to add an empty String field!");
    }
    catch (IllegalArgumentException e) {
      // expected bahavior
    }

    record.setMutable(Mutable.IMMUTABLE);

    assertFalse(record.isMutable());

    try {
      entrySet.add(getMapEntry("dob", DateUtil.getCalendar(1981, Calendar.FEBRUARY, 10)));
      fail("The Record.entrySet.add method should have thrown a ObjectImmutableException for an immutable Record object!");
    }
    catch (ObjectImmutableException e) {
      // expected behavior
    }

    record.setMutable(Mutable.MUTABLE);

    assertTrue(record.isMutable());

    try {
      entrySet.add(getMapEntry(new Integer(10), "String Value"));
      fail("The Record.entrySet.add method should have thrown a ClassCastException for trying to add an Integer as a field!");
    }
    catch (ClassCastException e) {
      // expected behavior
    }
  }

  public void testEntrySetIterator() throws Exception {
    final Record record = getRecordInstance();
    record.addField("personId", new Integer(101));
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");

    for (Iterator it = record.entrySet().iterator(); it.hasNext(); ) {
      final Map.Entry mapEntry = (Map.Entry) it.next();
      //logger.debug("containsKey = " + record.containsKey(mapEntry.getKey()));
      assertTrue(record.containsKey(mapEntry.getKey()));
      //logger.debug("{" + mapEntry.getValue() + ", " + record.getValue(mapEntry.getKey().toString()) + "}");
      assertEquals(mapEntry.getValue(), record.getValue(mapEntry.getKey().toString()));
    }

    Iterator it = record.entrySet().iterator();
    Map.Entry mapEntry = null;

    assertTrue(it.hasNext());
    assertNotNull(mapEntry = getMapEntry((Map.Entry) it.next()));

    it.remove();

    //logger.debug("mapEntry: " + mapEntry);
    //logger.debug("record: " + record);
    assertFalse(record.containsField(mapEntry.getKey().toString()));

    int count = 0;
    for ( ; it.hasNext(); ) {
      it.next();
      count++;
    }

    assertEquals(2, count);

    it = record.entrySet().iterator();
    record.addField("middleInitial", "J");

    try  {
      it.next();
      fail("The Record.entrySet.iterator method should have thrown a ConcurrentModificationException!");
    }
    catch (ConcurrentModificationException e) {
      // expected behavior
    }
  }

  public void testEntrySetSize() throws Exception {
    final Record record = getRecordInstance();
    record.addField("personId", new Integer(202));
    record.addField("firstName", "Jon");
    record.addField("lastName", "Doe");
    record.addField("dob", DateUtil.getCalendar(1987, Calendar.MARCH, 21));

    assertEquals(4, record.size());
    assertEquals(4, record.entrySet().size());

    record.addField("ssn", "123-45-6789");

    assertEquals(5, record.size());
    assertEquals(5, record.entrySet().size());

    record.addField("firstName", "Jack");

    assertEquals(5, record.size());
    assertEquals(5, record.entrySet().size());
    assertEquals("Jon", record.getValue("firstName"));

    try {
      record.addField("");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertEquals(5, record.size());
    assertEquals(5, record.entrySet().size());

    try {
      record.addField(null, "Some Value!");
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    assertEquals(5, record.size());
    assertEquals(5, record.entrySet().size());

    record.removeField("personId");
    record.removeField("dob");

    assertEquals(3, record.size());
    assertEquals(3, record.entrySet().size());

    try {
      record.removeField("mi");
    }
    catch (NoSuchFieldException e) {
      // ignore
    }

    assertEquals(3, record.size());
    assertEquals(3, record.entrySet().size());
  }

  public void testGetBooleanValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("booleanTrue", Boolean.TRUE);
    record.addField("integerOne", new Integer(1));
    record.addField("stringNot", "Not");

    assertEquals(Boolean.TRUE, record.getBooleanValue("booleanTrue"));
    assertEquals(Boolean.TRUE, record.getBooleanValue(0));

    try {
      record.getBooleanValue("integerOne");
      fail("Getting integerOne as a Boolean value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getBooleanValue("stringNot");
      fail("Getting stringNot as a Boolean value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetByteValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("byteValue", new Byte((byte) 1));
    record.addField("doubleValue", new Double(3.14159));
    record.addField("longValue", new Long(123456789));
    record.addField("shortValue", new Short((short) 12345));
    record.addField("stringValue", "123");

    assertEquals(new Byte((byte) 1), record.getByteValue("byteValue"));
    assertEquals(new Byte((byte) 1), record.getByteValue(0)); // byteValue

    try {
      record.getByteValue("doubleValue");
      fail("Getting doubleValue as an Byte value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getByteValue("longValue");
      fail("Getting longValue as an Byte value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getByteValue("shortValue");
      fail("Getting shortValue as an Byte value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getByteValue("stringValue");
      fail("Getting stringValue as an Byte value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetCalendarValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("calendarValue", DateUtil.getCalendar(1974, Calendar.MAY, 27));
    record.addField("dateValue", DateUtil.getCalendar(1776, Calendar.JULY, 4).getTime());
    record.addField("longMillisecond", new Long(123456789));
    record.addField("stringApril", "1 April 2005");

    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27), record.getCalendarValue("calendarValue"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27), record.getCalendarValue(0));

    try {
      record.getCalendarValue("dateValue");
      fail("Getting dateValue as a Calendar value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getCalendarValue("longMillisecond");
      fail("Getting longMillisecond as a Calendar value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getCalendarValue("stringApril");
      fail("Getting stringApril as a Calendar value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetCharacterValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("characterValue", new Character('C'));
    record.addField("asciiCharacter", new Integer(127));
    record.addField("stringCharacter", "Q");

    assertEquals(new Character('C'), record.getCharacterValue("characterValue"));
    assertEquals(new Character('C'), record.getCharacterValue(0));

    try {
      record.getCharacterValue("asciiCharacter");
      fail("Getting asciiCharacter as a Character value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getCharacterValue("stringCharacter");
      fail("Getting stringCharacter as a Character value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetDateValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("dateValue", DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime());
    record.addField("calendarValue", DateUtil.getCalendar(1979, Calendar.JUNE, 1));
    record.addField("longMillisecond", new Long(123456789));
    record.addField("stringDate", "1 April 2000");

    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime(), record.getDateValue("dateValue"));
    assertEquals(DateUtil.getCalendar(1974, Calendar.MAY, 27).getTime(), record.getDateValue(0));

    try {
      record.getDateValue("calendarValue");
      fail("Getting calendarValue as a Date value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getDateValue("longMillisecond");
      fail("Getting longMillisecond as a Date value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getDateValue("stringDate");
      fail("Getting stringDate as a Date value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetDoubleValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("doubleValue", new Double(3.14159));
    record.addField("floatValue", new Float(3.14159f));
    record.addField("integerValue", new Integer(314159));
    record.addField("stringValue", "3.14159");

    assertEquals(new Double(3.14159), record.getDoubleValue("doubleValue"));
    assertEquals(new Double(3.14159), record.getDoubleValue(0)); // doubleValue

    try {
      record.getDoubleValue("floatValue");
      fail("Getting floatValue as a Double value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getDoubleValue("integerValue");
      fail("Getting integerValue as a Double value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getDoubleValue("stringValue");
      fail("Getting stringValue as a Double value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetFloatValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("floatValue", new Float(3.14159f));
    record.addField("doubleValue", new Double(3.14159));
    record.addField("integerValue", new Integer(314159));
    record.addField("stringValue", "3.14159");

    assertEquals(new Float(3.14159), record.getFloatValue("floatValue"));
    assertEquals(new Float(3.14159), record.getFloatValue(0)); // floatValue

    try {
      record.getFloatValue("doubleValue");
      fail("Getting doubleValue as a Float value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getFloatValue("integerValue");
      fail("Getting integerValue as a Float value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getFloatValue("stringValue");
      fail("Getting stringValue as a Float value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetIntegerValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("integerValue", new Integer(2));
    record.addField("doubleValue", new Double(3.14159));
    record.addField("longValue", new Long(123456789));
    record.addField("shortValue", new Short((short) 12345));
    record.addField("stringValue", "123");

    assertEquals(new Integer(2), record.getIntegerValue("integerValue"));
    assertEquals(new Integer(2), record.getIntegerValue(0)); // integerValue

    try {
      record.getIntegerValue("doubleValue");
      fail("Getting doubleValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getIntegerValue("longValue");
      fail("Getting longValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getIntegerValue("shortValue");
      fail("Getting shortValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getIntegerValue("stringValue");
      fail("Getting stringValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetLongValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("longValue", new Long(123456789));
    record.addField("byteValue", new Byte((byte) 127));
    record.addField("doubleValue", new Double(3.14159));
    record.addField("integerValue", new Integer(2));
    record.addField("stringValue", "123");

    assertEquals(new Long(123456789), record.getLongValue("longValue"));
    assertEquals(new Long(123456789), record.getLongValue(0)); // longValue

    try {
      record.getLongValue("byteValue");
      fail("Getting byteValue as an Long value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getLongValue("doubleValue");
      fail("Getting doubleValue as an Long value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getLongValue("integerValue");
      fail("Getting integerValue as an Long value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getLongValue("stringValue");
      fail("Getting stringValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetShortValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("shortValue", new Short((short) 31000));
    record.addField("byteValue", new Byte((byte) 127));
    record.addField("floatValue", new Float(3.14159f));
    record.addField("integerValue", new Integer(2));
    record.addField("stringValue", "123");

    assertEquals(new Short((short) 31000), record.getShortValue("shortValue"));
    assertEquals(new Short((short) 31000), record.getShortValue(0)); // shortValue

    try {
      record.getShortValue("byteValue");
      fail("Getting byteValue as an Short value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getShortValue("floatValue");
      fail("Getting floatValue as an Short value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getShortValue("integerValue");
      fail("Getting integerValue as an Short value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }

    try {
      record.getShortValue("stringValue");
      fail("Getting stringValue as an Integer value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetStringValue() throws Exception {
    final Record record = getRecordInstance();
    record.addField("stringValue", "string");
    record.addField("characterValue", new Character('J'));

    assertEquals("string", record.getStringValue("stringValue"));
    assertEquals("string", record.getStringValue(0)); // stringValue

    try {
      record.getStringValue("characterValue");
      fail("Getting characterValue as a String value should have thrown a ClassCastException!");
    }
    catch (ClassCastException e) {
      // expected behavior!
    }
  }

  public void testGetValue() throws Exception {
    final Record<String> jackHandy = getRecordInstance();
    jackHandy.addField("personId", new Integer(1));
    jackHandy.addField("firstName", "Jack");
    jackHandy.addField("middleInitial", new Character('B'));
    jackHandy.addField("lastName", "Handy");
    jackHandy.addField("dob", DateUtil.getCalendar(1969, Calendar.NOVEMBER, 30));
    jackHandy.addField("gender", Gender.MALE);
    jackHandy.addField("race", Race.WHITE);

    try {
      final Integer personId = jackHandy.<Integer>getValue("personId");
      final String firstName = jackHandy.<String>getValue("firstName");
      final Character middleInitial = jackHandy.<Character>getValue("middleInitial");
      final String lastName = jackHandy.<String>getValue("lastName");
      final Calendar dob = jackHandy.<Calendar>getValue("dob");
      final Gender gender = jackHandy.<Gender>getValue("gender");
      final Race race = jackHandy.<Race>getValue("race");
    }
    catch (ClassCastException e) {
      fail("Calling getValue for the various fields on Jack Handy should not have thrown a ClassCastException!");
    }
  }

}
