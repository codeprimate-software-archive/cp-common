/*
 * AbstractInvocationArgumentTest.java (c) 6 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.17
 * @see com.cp.common.beans.definition.AbstractInvocationArgument
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.beans.util.converters.CalendarConverter;
import com.cp.common.util.DateUtil;
import com.cp.common.util.SystemException;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractInvocationArgumentTest extends TestCase {

  public AbstractInvocationArgumentTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractInvocationArgumentTest.class);
    //suite.addTest(new AbstractInvocationArgumentTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    CalendarConverter.removeDateFormatPattern("yyyy.MM.dd");
  }

  protected InvocationArgument getInvocationArgument(final String type, final String value) {
    return new AbstractInvocationArgument(type, value) {
    };
  }

  public void testBooleanInvocationArgument() throws Exception {
    final InvocationArgument booleanInvocationArgument = getInvocationArgument("java.lang.Boolean", "1");

    assertNotNull(booleanInvocationArgument);
    assertEquals("java.lang.Boolean", booleanInvocationArgument.getStringType());
    assertEquals("1", booleanInvocationArgument.getStringValue());
    assertEquals(Boolean.class, booleanInvocationArgument.getType());
    assertEquals(Boolean.TRUE, booleanInvocationArgument.getValue());
  }

  public void testCalendarInvocationArgument() throws Exception {
    final InvocationArgument calendarInvocationArgument = getInvocationArgument("java.util.Calendar", "03/06/2008");

    assertNotNull(calendarInvocationArgument);
    assertEquals("java.util.Calendar", calendarInvocationArgument.getStringType());
    assertEquals("03/06/2008", calendarInvocationArgument.getStringValue());
    assertEquals(Calendar.class, calendarInvocationArgument.getType());
    assertEquals(DateUtil.getCalendar(2008, Calendar.MARCH, 6), calendarInvocationArgument.getValue());
  }

  public void testCharacterInvocationArgument() throws Exception {
    final InvocationArgument characterInvocationArgument = getInvocationArgument("java.lang.Character", "X");

    assertNotNull(characterInvocationArgument);
    assertEquals("java.lang.Character", characterInvocationArgument.getStringType());
    assertEquals("X", characterInvocationArgument.getStringValue());
    assertEquals(Character.class, characterInvocationArgument.getType());
    assertEquals('X', characterInvocationArgument.getValue());
  }

  public void testComplexCalendarInvocationArgument() throws Exception {
    final InvocationArgument complexCalendarInvocationArgument = getInvocationArgument("java.util.Calendar", "2008.03.06");

    assertNotNull(complexCalendarInvocationArgument);
    assertEquals("java.util.Calendar", complexCalendarInvocationArgument.getStringType());
    assertEquals("2008.03.06", complexCalendarInvocationArgument.getStringValue());
    assertEquals(Calendar.class, complexCalendarInvocationArgument.getType());

    try {
      complexCalendarInvocationArgument.getValue();
      fail("Calling getValue without specifying a date format pattern for date value 2008.03.06 should have thrown an Exception!");
    }
    catch (Exception e) {
      // expected behavior!
    }

    complexCalendarInvocationArgument.setFormatPattern("yyyy.MM.dd");

    try {
      assertEquals(DateUtil.getCalendar(2008, Calendar.MARCH, 6), complexCalendarInvocationArgument.getValue());
    }
    catch (Exception e) {
      fail("Calling getValue with the date format pattern yyyy.MM.dd set threw an unexpected Exception ("
        + e.getMessage() + ") for date value 2008.03.06!");
    }
  }

  public void testDoubleInvocationArgument() throws Exception {
    final InvocationArgument doubleInvocationArgument = getInvocationArgument("java.lang.Double", String.valueOf(Math.PI));

    assertNotNull(doubleInvocationArgument);
    assertEquals("java.lang.Double", doubleInvocationArgument.getStringType());
    assertEquals(String.valueOf(Math.PI), doubleInvocationArgument.getStringValue());
    assertEquals(Double.class, doubleInvocationArgument.getType());
    assertEquals(Math.PI, doubleInvocationArgument.getValue());
  }

  public void testGetTypeWithNonExistingClass() throws Exception {
    final InvocationArgument invocationArgument = getInvocationArgument("com.mycompany.mypackage.NonExistingClass", "test");

    assertNotNull(invocationArgument);
    assertEquals("com.mycompany.mypackage.NonExistingClass", invocationArgument.getStringType());
    assertEquals("test", invocationArgument.getStringValue());

    try {
      invocationArgument.getType();
      fail("Calling getType with a non-existing class should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("The class (com.mycompany.mypackage.NonExistingClass) could not be found in the CLASSPATH!",
        e.getMessage());
      assertTrue(e.getCause() instanceof ClassNotFoundException);
    }
    catch (Exception e) {
      fail("Calling getType with a non-existing class threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    try {
      invocationArgument.getValue();
      fail("Calling getValue with a non-existing class type should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("The class (com.mycompany.mypackage.NonExistingClass) could not be found in the CLASSPATH!",
        e.getMessage());
      assertTrue(e.getCause() instanceof ClassNotFoundException);
    }
    catch (Exception e) {
      fail("Calling getValue with a non-existing class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testInstantiation() throws Exception {
    InvocationArgument invocationArgument = null;

    assertNull(invocationArgument);

    try {
      invocationArgument = getInvocationArgument("com.mycompany.mypackage.NonExistingClass", "nill");
    }
    catch (Exception e) {
      fail("Instantiating an instance of InvocationArgument with an invalid class an nill value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(invocationArgument);
    assertEquals("com.mycompany.mypackage.NonExistingClass", invocationArgument.getStringType());
    assertEquals("nill", invocationArgument.getStringValue());
  }

  public void testInstantiationWithEmptyType() throws Exception {
    InvocationArgument invocationArgument = null;

    assertNull(invocationArgument);

    try {
      invocationArgument = getInvocationArgument(" ", "null");
      fail("Instantiating an instance of InvocationArgument with an empty type should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The type of the invocation argument must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of InvocationArgument with an empty type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(invocationArgument);
  }

  public void testInstantiationWithNullType() throws Exception {
    InvocationArgument invocationArgument = null;

    assertNull(invocationArgument);

    try {
      invocationArgument = getInvocationArgument(null, "null");
      fail("Instantiating an instance of InvocationArgument with a null type should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The type of the invocation argument must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of InvocationArgument with a null type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(invocationArgument);
  }

  public void testInstantiationWithEmptyValue() throws Exception {
    InvocationArgument invocationArgument = null;

    assertNull(invocationArgument);

    try {
      invocationArgument = getInvocationArgument("java.lang.Object", " ");
      fail("Instantiating an instance of InvocationArgument with an empty value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The value of the invocation argument must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of InvocationArgument with an empty value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(invocationArgument);
  }

  public void testInstantiateWithNullValue() throws Exception {
    InvocationArgument invocationArgument = null;

    assertNull(invocationArgument);

    try {
      invocationArgument = getInvocationArgument("java.lang.Object", null);
      fail("Instantiating an instance of InvocationArgument with a null value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The value of the invocation argument must be specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of InvocationArgument with a null value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(invocationArgument);
  }

  public void testIntegerInvocationArgument() throws Exception {
    final InvocationArgument integerInvocationArgument = getInvocationArgument("java.lang.Integer", "2");

    assertNotNull(integerInvocationArgument);
    assertEquals("java.lang.Integer", integerInvocationArgument.getStringType());
    assertEquals("2", integerInvocationArgument.getStringValue());
    assertEquals(Integer.class, integerInvocationArgument.getType());
    assertEquals(2, integerInvocationArgument.getValue());
  }

  public void testNullInvocationArgument() throws Exception {
    final InvocationArgument nullInvocationArgument = getInvocationArgument("java.lang.String", "null");

    assertNotNull(nullInvocationArgument);
    assertEquals("java.lang.String", nullInvocationArgument.getStringType());
    assertEquals("null", nullInvocationArgument.getStringValue());
    assertEquals(String.class, nullInvocationArgument.getType());
    assertNull(nullInvocationArgument.getValue());
  }

  public void testProcessInvocationArgument() throws Exception {
    final InvocationArgument processInvocationArgument = getInvocationArgument("com.cp.common.beans.Process", "java");

    assertNotNull(processInvocationArgument);
    assertEquals("com.cp.common.beans.Process", processInvocationArgument.getStringType());
    assertEquals("java", processInvocationArgument.getStringValue());
    assertEquals(com.cp.common.beans.Process.class, processInvocationArgument.getType());
    assertEquals(new DefaultProcess("java"), processInvocationArgument.getValue());
  }

  public void testStringInvocationArgument() throws Exception {
    final InvocationArgument stringInvocationArgument = getInvocationArgument("java.lang.String", "test");

    assertNotNull(stringInvocationArgument);
    assertEquals("java.lang.String", stringInvocationArgument.getStringType());
    assertEquals("test", stringInvocationArgument.getStringValue());
    assertEquals(String.class, stringInvocationArgument.getType());
    assertEquals("test", stringInvocationArgument.getValue());
  }

  public void testUserInvocationArgument() throws Exception {
    final InvocationArgument userInvocationArgument = getInvocationArgument("com.cp.common.beans.User", "blumj");

    assertNotNull(userInvocationArgument);
    assertEquals("com.cp.common.beans.User", userInvocationArgument.getStringType());
    assertEquals("blumj", userInvocationArgument.getStringValue());
    assertEquals(User.class, userInvocationArgument.getType());
    assertEquals(new DefaultUser("blumj"), userInvocationArgument.getValue());
  }

}
