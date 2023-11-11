/*
 * RequiredVisitorTest.java (c) 15 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.27
 * @see com.cp.common.beans.support.CommonSupportTestCase
 * @see com.cp.common.beans.support.RequiredVisitor
 */

package com.cp.common.beans.support;

import com.cp.common.enums.State;
import com.cp.common.lang.support.AuditableVisitor;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Visitor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RequiredVisitorTest extends CommonSupportTestCase {

  public RequiredVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RequiredVisitorTest.class);
    //suite.addTest(new RequiredVisitorTest("testName"));
    return suite;
  }

  private Set<String> getActualRequiredProperties(final PropertyDescriptor... properties) {
    final Set<String> actualRequiredProperties = new HashSet<String>(properties.length);

    for (final PropertyDescriptor property : properties) {
      actualRequiredProperties.add(property.getName());
    }

    return actualRequiredProperties;
  }

  private Set<String> getExpectedRequiredProperties(final String... properties) {
    final Set<String> expectedRequiredProperties = new HashSet<String>(4 + properties.length);
    expectedRequiredProperties.add("createdBy");
    expectedRequiredProperties.add("createdDate");
    expectedRequiredProperties.add("modifiedBy");
    expectedRequiredProperties.add("modifiedDate");
    expectedRequiredProperties.addAll(Arrays.asList(properties));
    return expectedRequiredProperties;
  }

  public void testGetRequiredProperties() throws Exception {
    final RequiredVisitor visitor = new RequiredVisitor();

    Set<String> expectedRequiredProperties = getExpectedRequiredProperties("dateOfBirth", "firstName", "lastName");
    Set<String> actualRequiredProperties = getActualRequiredProperties(visitor.getRequiredProperties(new PersonImpl()));

    assertNotNull(actualRequiredProperties);
    assertEquals(expectedRequiredProperties, actualRequiredProperties);

    expectedRequiredProperties = getExpectedRequiredProperties("street1", "city", "state", "zipCode");
    actualRequiredProperties = getActualRequiredProperties(visitor.getRequiredProperties(new AddressImpl()));

    assertNotNull(actualRequiredProperties);
    assertEquals(expectedRequiredProperties, actualRequiredProperties);

    expectedRequiredProperties = getExpectedRequiredProperties("areaCode", "prefix", "suffix");
    actualRequiredProperties = getActualRequiredProperties(visitor.getRequiredProperties(new PhoneNumberImpl()));

    assertNotNull(actualRequiredProperties);
    assertEquals(expectedRequiredProperties, actualRequiredProperties);
  }

  public void testVisit() throws Exception {
    final Visitor requiredVisitor = new RequiredVisitor();

    final Person person = new PersonImpl();
    person.setDateOfBirth(DateUtil.getCalendar(1988, Calendar.FEBRUARY, 21));
    person.setFirstName("Jon");
    person.accept(AuditableVisitor.getInstance(getUser("root")));

    try {
      person.accept(requiredVisitor);
      fail("Visiting the Person object with a null lastName should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("Property (lastName) of bean (" + person.getClass().getName() + ") is required!", e.getMessage());
    }
    catch (Exception e) {
      fail("Visiting the Person object with a null lastName threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    person.setLastName("Bloom");

    try {
      person.accept(requiredVisitor);
    }
    catch (Exception e) {
      fail("Visiting the Person object with all required properties set threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    final Address address = new AddressImpl(1);
    address.setStreet1("100 Main St.");
    address.setCity("Portland");
    address.setZipCode("97205");
    person.setAddress(address);
    person.accept(AuditableVisitor.getInstance(getUser("jblum")));

    try {
      person.accept(requiredVisitor);
      fail("Visiting the Person object having an Address with a null state should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("Property (state) of bean (" + address.getClass().getName() + ") is required!", e.getMessage());
    }
    catch (Exception e) {
      fail("Visiting the Person object having an Address with a null state threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    address.setState(State.OREGON);

    try {
      person.accept(requiredVisitor);
    }
    catch (Exception e) {
      fail("Visiting the Person object having and Address with all required properties set threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    final PhoneNumber phoneNumber = new PhoneNumberImpl();
    phoneNumber.setPrefix("555");
    phoneNumber.setSuffix("1234");
    person.setPhoneNumber(phoneNumber);
    person.accept(AuditableVisitor.getInstance(getUser("admin")));

    try {
      person.accept(requiredVisitor);
      fail("Visiting the Person object having a PhoneNumber with a null areaCode should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("Property (areaCode) of bean (" + phoneNumber.getClass().getName() + ") is required!", e.getMessage());
    }
    catch (Exception e) {
      fail("Visiting the Person object having a PhoneNumber with a null areaCode threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    phoneNumber.setAreaCode("503");

    try {
      person.accept(requiredVisitor);
    }
    catch (Exception e) {
      fail("Visiting the Person object having an Address and PhoneNumber with all required properties set threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testVisitWithNoRequiredPropertiesOnBean() throws Exception {
    final Household household = new HouseholdImpl();
    household.accept(AuditableVisitor.getInstance(getUser("root")));

    final Visitor requiredVisitor = new RequiredVisitor();

    assertNull(household.getHouseholdId());
    assertNull(household.getHouseholdName());
    assertNull(household.getHouseholdNumber());
    assertEquals(0, household.size());

    try {
      household.accept(requiredVisitor);
    }
    catch (Exception e) {
      fail("Visiting the Household object with no required properties threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

}
