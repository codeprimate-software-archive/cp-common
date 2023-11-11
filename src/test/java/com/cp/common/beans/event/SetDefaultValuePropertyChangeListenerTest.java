/*
 * SetDefaultValuePropertyChangeListenerTest.java (c) 13 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.SetDefaultValuePropertyChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.CommonBeanTestCase;
import com.cp.common.beans.annotation.BoundedDate;
import com.cp.common.beans.annotation.Default;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.util.DateUtil;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class SetDefaultValuePropertyChangeListenerTest extends CommonEventTestCase {

  public SetDefaultValuePropertyChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SetDefaultValuePropertyChangeListenerTest.class);
    //suite.addTest(new SetDefaultValuePropertyChangeListenerTest("testName"));
    return suite;
  }

  @Override
  protected Person getEmptyPerson() {
    return new PersonImpl();
  }

  public void testInstantiation() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    SetDefaultValuePropertyChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new SetDefaultValuePropertyChangeListener(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of SetDefaultValuePropertyChangeListener with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    SetDefaultValuePropertyChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new SetDefaultValuePropertyChangeListener(null);
      fail("Instantiation an instance of SetDefaultValuePropertyChangeListener class with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of SetDefaultValuePropertyChangeListener with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testHandleWithDefaultValueViolatingPropertyConstraint() throws Exception {
    final Calendar july4th2000 = DateUtil.getCalendar(2000, Calendar.JULY, 4);
    final TestBean testBean = new TestBean(july4th2000);

    assertNotNull(testBean);
    assertEquals(july4th2000, testBean.getDate());

    try {
      testBean.setDate(null);
      fail("Setting the date property of TestBean to null with a default value violating the BoundedDate Annotation constraint should have thrown a MalformedAnnotationDeclationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("The value (09/26/2008 12:00:00 AM) for property (date) on bean (" + testBean.getClass().getName()
        + ") must be greater than equal (01/01/1991 12:00:00 AM) and less than equal (12/31/2001 12:00:00 AM)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the date property of TestBean to null with a default value violating the BoundedDate Annotation constraint threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(testBean.getDate());
  }

  public void testHandleWithDifferentRegisteredAndEventBeans() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final SetDefaultValuePropertyChangeListener listener = new SetDefaultValuePropertyChangeListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    try {
      listener.handle(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null, "test"));
      fail("Calling handle with a property change event having an event Bean different than the registered Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The registered bean and event bean should be the same object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling handle with a property change event having an event Bean different than the registered Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleWithNonNullNewValue() throws Exception {
    final Person person = getEmptyPerson();

    assertNotNull(person);
    assertNull(person.getDateOfBirth());

    final Calendar dateOfBirth = DateUtil.getCalendar(2008, Calendar.SEPTEMBER, 26);

    person.setDateOfBirth(dateOfBirth);

    assertEquals(dateOfBirth, person.getDateOfBirth());
  }

  public void testHandleWithNullDefaultValue() throws Exception {
    final Person person = getEmptyPerson();

    assertNotNull(person);
    assertNull(person.getSsn());

    person.setSsn("333-22-4444");

    assertEquals("333-22-4444", person.getSsn());

    try {
      person.setSsn(null);
      fail("Setting the ssn property of Person to null with a null default value should have thrown an MalformedAnnotationDeclarationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("The default value should not be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the ssn property of Person to null with a null default value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(person.getSsn());
  }

  public void testHandleWithNullNewValue() throws Exception {
    final Calendar defaultDateOfBirth = DateUtil.getCalendar(1977, Calendar.JULY, 7);
    final Calendar june16th1991 = DateUtil.getCalendar(1991, Calendar.JUNE, 16);
    final Calendar november2nd2002 = DateUtil.getCalendar(2002, Calendar.NOVEMBER, 2);

    final Person person = getEmptyPerson();
    person.setDateOfBirth(june16th1991);

    assertNotNull(person);
    assertEquals(june16th1991, person.getDateOfBirth());

    person.setDateOfBirth(null);

    assertEquals(defaultDateOfBirth, person.getDateOfBirth());

    person.setDateOfBirth(november2nd2002);

    assertEquals(november2nd2002, person.getDateOfBirth());
  }

  protected static final class PersonImpl extends CommonBeanTestCase.PersonImpl {

    public PersonImpl() {
    }

    public PersonImpl(final Integer personId) {
      super(personId);
    }

    public PersonImpl(final String firstName, final String lastName, final Calendar dateOfBirth, final String ssn) {
      super(firstName, lastName, dateOfBirth, ssn);
    }

    public PersonImpl(final Person person) {
      super(person);
    }

    @Override
    @Default(value="07/07/1977")
    public void setDateOfBirth(final Calendar dateOfBirth) {
      super.setDateOfBirth(dateOfBirth);
    }

    @Override
    @Default(value="null")
    public void setSsn(final String ssn) {
      super.setSsn(ssn);
    }
  }

  protected static final class TestBean extends AbstractBean<Integer> {

    private Calendar date;

    public TestBean() {
    }

    public TestBean(final Calendar date) {
      setDate(date);
    }

    public Calendar getDate() {
      return date;
    }

    @BoundedDate(min="01/01/1991", max="12/31/2001", pattern="MM/dd/yyyy")
    @Default(value="09/26/2008")
    public void setDate(final Calendar date) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          TestBean.this.date = date;
        }
      };
      processChange("date", this.date, date, callbackHandler);
    }
  }

}
