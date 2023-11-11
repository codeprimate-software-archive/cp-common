/*
 * RollbackPropertyChangeManagerTest.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.2
 * @see com.cp.common.beans.support.CommonSupportTestCase
 * @see com.cp.common.beans.support.RollbackPropertyChangeManager
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.enums.State;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class RollbackPropertyChangeManagerTest extends CommonSupportTestCase {

  public RollbackPropertyChangeManagerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RollbackPropertyChangeManagerTest.class);
    //suite.addTest(new RollbackPropertyChangeManagerTest("testName"));
    return suite;
  }

  protected Person getPerson(final Integer personId, final String firstName, final String lastName) {
    return new TestPerson(personId, firstName, lastName);
  }

  public void testInstantiate() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    RollbackPropertyChangeManager propertyChangeManager = null;

    assertNull(propertyChangeManager);

    try {
      propertyChangeManager = new RollbackPropertyChangeManager(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RollbackPropertyChangeManager class with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
  }

  public void testInstantiateWithNullBean() throws Exception {
    RollbackPropertyChangeManager propertyChangeManager = null;

    assertNull(propertyChangeManager);

    try {
      propertyChangeManager = new RollbackPropertyChangeManager(null);
      fail("Instantiating an instance of the RollbackPropertyChangeManager class with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean managed by this PropertyChangeManager cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RollbackPropertyChangeManager class with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyChangeManager);
  }

  public void testContains() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final PropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertFalse(propertyChangeManager.contains("personId"));
    assertFalse(propertyChangeManager.contains("id"));
    assertFalse(propertyChangeManager.contains("firstName"));
    assertFalse(propertyChangeManager.contains("lastName"));
    assertFalse(propertyChangeManager.contains("dateOfBirth"));
    assertFalse(propertyChangeManager.contains("ssn"));
    assertFalse(propertyChangeManager.contains("gender"));
    assertFalse(propertyChangeManager.contains("race"));
    assertFalse(propertyChangeManager.contains("address"));
    assertFalse(propertyChangeManager.contains("phoneNumber"));

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "personId", 0, 1));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "firstName", "Jon", "John"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "lastName", "Bloom", "Blum"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "dateOfBirth", null, DateUtil.getCalendar(1974, Calendar.MAY, 27)));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "ssn", "333-22-4444", "333224444"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "gender", Gender.MALE, Gender.MALE));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "race", Race.NATIVE_AMERICAN, Race.WHITE));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "address", getAddress(null, "100 Main St.", null, "Portland", State.OREGON, "12345"), null));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "phoneNumber", null, null));

    assertTrue(propertyChangeManager.contains("personId"));
    assertFalse(propertyChangeManager.contains("id"));
    assertTrue(propertyChangeManager.contains("firstName"));
    assertTrue(propertyChangeManager.contains("lastName"));
    assertTrue(propertyChangeManager.contains("dateOfBirth"));
    assertTrue(propertyChangeManager.contains("ssn"));
    assertFalse(propertyChangeManager.contains("gender"));
    assertTrue(propertyChangeManager.contains("race"));
    assertTrue(propertyChangeManager.contains("address"));
    assertFalse(propertyChangeManager.contains("phoneNumber"));
  }

  public void testDoCommit() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", 0, 1));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", null, "test"));

    assertTrue(propertyChangeManager.hasModifiedProperties());
    assertTrue(propertyChangeManager.doCommit(mockBean));
    assertFalse(propertyChangeManager.hasModifiedProperties());
  }

  public void testDoCommitWithDifferentManagedAndArgumentBeans() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", null, "test"));

    assertTrue(propertyChangeManager.hasModifiedProperties());

    try {
      propertyChangeManager.doCommit(EasyMock.createMock(Bean.class));
      fail("Calling doCommit with a non-managed Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean managed by this PropertyChangeManager is not the same as the bean argument to the doCommit call!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling doCommit with a non-managed Bean should threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(propertyChangeManager.hasModifiedProperties());
  }

  public void testDoCommitWithNoPropertyChanges() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
    assertFalse(propertyChangeManager.hasModifiedProperties());
    assertTrue(propertyChangeManager.doCommit(mockBean));
    assertFalse(propertyChangeManager.hasModifiedProperties());
  }

  public void testDoRollback() throws Exception {
    final Person person = getPerson(null, "Jon", "Bloom");
    person.setSsn("333-22-4444");

    assertTrue(person.isNew());
    assertTrue(person.isModified());
    assertNull(person.getPersonId());
    assertNull(person.getDateOfBirth());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals("333-22-4444", person.getSsn());

    person.commit();

    assertTrue(person.isNew());
    assertFalse(person.isModified());
    assertNull(person.getPersonId());
    assertNull(person.getDateOfBirth());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals("333-22-4444", person.getSsn());

    final Calendar dateOfBirth = DateUtil.getCalendar(1974, Calendar.MAY, 27);
    person.setPersonId(1);
    person.setDateOfBirth(dateOfBirth);
    person.setLastName("Doe");
    person.setSsn("123456789");

    assertFalse(person.isNew());
    assertTrue(person.isModified());
    assertEquals(new Integer(1), person.getPersonId());
    assertEquals(dateOfBirth, person.getDateOfBirth());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    assertEquals("123456789", person.getSsn());

    try {
      person.rollback();
    }
    catch (Exception e) {
      fail("Calling rollback on the Person object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(person.isNew());
    assertFalse(person.isModified());
    assertNull(person.getPersonId());
    assertNull(person.getDateOfBirth());
    assertEquals("Jon", person.getFirstName());
    assertEquals("Bloom", person.getLastName());
    assertEquals("333-22-4444", person.getSsn());

    person.setLastName("Deere");

    assertTrue(person.isModified());
    assertEquals("Deere", person.getLastName());

    person.setLastName("Doe");

    assertTrue(person.isModified());
    assertEquals("Doe", person.getLastName());

    person.setLastName("Smith");

    assertTrue(person.isModified());
    assertEquals("Smith", person.getLastName());

    try {
      person.rollback();
    }
    catch (Exception e) {
      fail("Calling rollback on the Person object again threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(person.isModified());
    assertEquals("Bloom", person.getLastName());
  }

  public void testDoRollbackWithDifferentManagedAndArgumentBeans() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", null, "test"));

    assertTrue(propertyChangeManager.hasModifiedProperties());

    try {
      propertyChangeManager.doRollback(EasyMock.createMock(Bean.class));
      fail("Calling doRollback with a non-managed Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean managed by this PropertyChangeManager is not the same as the bean argument to the doRollback call!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling doRollback with a non-managed Bean threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(propertyChangeManager.hasModifiedProperties());
  }

  public void testDoRollbackWithNoPropertyChanges() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
    assertFalse(propertyChangeManager.hasModifiedProperties());
    assertTrue(propertyChangeManager.doRollback(mockBean));
    assertFalse(propertyChangeManager.hasModifiedProperties());
  }

  public void testGetModifiedProperties() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final RollbackPropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());

    Set<String> actualModifiedProperties = propertyChangeManager.getModifiedProperties();

    assertNotNull(actualModifiedProperties);
    assertTrue(actualModifiedProperties.isEmpty());
    assertEquals(0, actualModifiedProperties.size());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", null, 0));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "firstName", "Jon", "John"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "lastName", "Bloom", "Blum"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "dateOfBirth", null, null));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "gender", Gender.MALE, Gender.MALE));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "race", Race.NATIVE_AMERICAN, Race.WHITE));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "ssn", "333-22-4444", "333224444"));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", null, "null"));

    final Set<String> expectedModifiedProperties = new HashSet<String>(7);
    expectedModifiedProperties.add("id");
    expectedModifiedProperties.add("firstName");
    expectedModifiedProperties.add("lastName");
    expectedModifiedProperties.add("race");
    expectedModifiedProperties.add("ssn");
    expectedModifiedProperties.add("value");

    actualModifiedProperties = propertyChangeManager.getModifiedProperties();

    assertNotNull(actualModifiedProperties);
    assertEquals(expectedModifiedProperties, actualModifiedProperties);

    propertyChangeManager.doCommit(mockBean);
    actualModifiedProperties = propertyChangeManager.getModifiedProperties();

    assertNotNull(actualModifiedProperties);
    assertTrue(actualModifiedProperties.isEmpty());
    assertEquals(0, actualModifiedProperties.size());
  }

  public void testHasModifiedProperties() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final PropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", 0, 0));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "createdBy", "root", "root"));

    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", -1, 1));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "createdBy", null, "root"));

    assertTrue(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.doRollback(mockBean);

    assertFalse(propertyChangeManager.hasModifiedProperties());
  }

  public void testPropertyChange() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final PropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);
    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", 0, 0));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", "test", "test"));

    assertFalse(propertyChangeManager.hasModifiedProperties());

    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "id", -1, 1));
    propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(mockBean, "value", "test", "TEST"));

    assertTrue(propertyChangeManager.hasModifiedProperties());
  }

  public void testPropertyChangeWithDifferentManagedAndEventSourceBeans() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final PropertyChangeManager propertyChangeManager = new RollbackPropertyChangeManager(mockBean);

    assertNotNull(propertyChangeManager);

    try {
      propertyChangeManager.propertyChange(BeanUtil.getPropertyChangeEvent(EasyMock.createMock(Bean.class), "value", Boolean.TRUE, Boolean.FALSE));
      fail("Calling propertyChange with a non-managed Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The event source is not the same object as the bean managed by this PropertyChangeManager!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling propertyChange with a non-managed Bean threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public static final class TestPerson extends CommonSupportTestCase.PersonImpl {

    public TestPerson() {
    }

    public TestPerson(final Integer personId) {
      super(personId);
    }

    public TestPerson(final Integer personId, final String firstName, final String lastName) {
      super(personId, firstName, lastName);
    }

    public TestPerson(final Person person) {
      super(person);
    }

    @Override
    protected PropertyChangeManager getPropertyChangeManagerImpl() {
      return new RollbackPropertyChangeManager(this);
    }
  }

}
