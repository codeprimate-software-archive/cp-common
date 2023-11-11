/*
 * PropertySearchFilterTest.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.4.24
 */

package com.cp.common.util.search;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.search.*;
import com.cp.common.util.search.BinarySearch;
import com.cp.common.util.search.LinearSearch;
import com.cp.common.util.search.PropertySearchFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;

public class PropertySearchFilterTest extends com.cp.common.util.search.AbstractSearchTest {

  private static final Logger logger = Logger.getLogger(PropertySearchFilterTest.class);

  public PropertySearchFilterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PropertySearchFilterTest.class);
    //suite.addTest(new PropertySearchFilterTest("testName"));
    return suite;
  }

  public void testPropertySearchFilter() throws Exception {
    // Create Users!
    final User jonDoe = new User(new Integer(1), "Jon", "Doe");
    final User janeDoe = new User(new Integer(2), "Jane", "Doe");
    final User jackHandy = new User(new Integer(3), "Jack", "Handy");
    final User randyHandy = new User(new Integer(4), "Randy", "Handy");
    final User ronJohnson = new User(new Integer(5), "Ron", "Summers");
    final User berryAnderson = new User(new Integer(6), "Berry", "Johnson");
    final User timRenolds = new User(new Integer(7), "Tim", "Renolds");
    final User bainHandy = new User(new Integer(8), "Bain", "Handy");
    final User jackBain = new User(new Integer(9), "Jack", "Bain");

    // Create User List!
    final List userList = new ArrayList();
    userList.add(jonDoe);
    userList.add(jackHandy);
    userList.add(berryAnderson);
    userList.add(randyHandy);
    userList.add(ronJohnson);
    userList.add(janeDoe);
    userList.add(timRenolds);
    userList.add(bainHandy);
    userList.add(jackBain);

    // Create a SearchList from the userList.
    final SearchableList searchableUserList = new SearchableList(userList);

    // Search for all Handys (Linear Search)
    final Map propertyMap = new HashMap();
    propertyMap.put("lastName", "Handy");

    Searcher linearSearch = new LinearSearch(new PropertySearchFilter(propertyMap, "lastName"));

    Collection searchResults = (Collection) linearSearch.search(searchableUserList);
    assertNotNull(searchResults);
    assertEquals(3, searchResults.size());
    assertTrue(searchResults.contains(jackHandy));
    assertTrue(searchResults.contains(randyHandy));
    assertTrue(searchResults.contains(bainHandy));

    // Search for all Jacks (Linear Search)
    propertyMap.clear();
    propertyMap.put("firstName", "Jack");
    linearSearch = new LinearSearch(new PropertySearchFilter(propertyMap, "firstName"));
    searchResults = (Collection) linearSearch.search(searchableUserList);
    assertNotNull(searchResults);
    assertEquals(2, searchResults.size());
    assertTrue(searchResults.contains(jackBain));
    assertTrue(searchResults.contains(jackHandy));

    // Search for Tim Ryan (Linear Search)
    propertyMap.clear();
    propertyMap.put("firstName", "Tim");
    propertyMap.put("lastName", "Ryan");
    linearSearch = new LinearSearch(new PropertySearchFilter(propertyMap, "lastName"));
    searchResults = (Collection) linearSearch.search(searchableUserList);
    assertNotNull(searchResults);
    assertEquals(0, searchResults.size());

    // Search for ID 7 (Linear Search)
    propertyMap.clear();
    propertyMap.put("id", new Integer(7));
    linearSearch = new LinearSearch(new PropertySearchFilter(propertyMap, "id"));
    searchResults = (Collection) linearSearch.search(searchableUserList);
    assertNotNull(searchResults);
    assertEquals(1, searchResults.size());
    assertTrue(searchResults.contains(timRenolds));

    // Search for ID 2 (Binary Search)
    Collections.sort(searchableUserList, new BeanComparator("id"));
    //logger.debug("SearchableUserLit: " + searchableUserList);
    propertyMap.clear();
    propertyMap.put("id", new Integer(2));
    //logger.debug("PropertyMap: " + propertyMap);
    com.cp.common.util.search.Searcher binarySearch = new BinarySearch(new PropertySearchFilter(propertyMap, "id"));
    final User theUser = (User) binarySearch.search(searchableUserList);
    assertNotNull(theUser);
    assertEquals(janeDoe, theUser);
  }

  public void testValidatePropertyKey() throws Exception {
    // Create the propertyMap!
    final Map propertyMap = new HashMap();
    propertyMap.put("goodPropertyKey", "value");
    propertyMap.put("badPropertyKey", new Object());

    // Create the propertyKey!
    String propertyKey = null;

    PropertySearchFilter propertySearchFilter = null;

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      fail("The PropertySearchFilter.validatePropertyKey method should have thrown a NullPointerException!"
        + "  The propertyKey cannot be null!");
    }
    catch (NullPointerException e) {
      logger.debug("Test Passed: NullPointerException thrown because the propertyKey cannot be null!");
    }

    propertyKey = "null";

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      fail("The PropertySearchFilter.validatePropertyKey method should have thrown an IllegalArgumentException!"
        + "  The propertyKey must be a key of the propertyMap!");
    }
    catch (IllegalArgumentException e) {
      logger.debug("Test Passed: IllegalArgumentException thrown because the propertyKey is not a key of the propertyMap!");
    }

    propertyKey = "badPropertyKey";

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      fail("The PropertySearchFilter.validatePropertyKey method should have thrown an IllegalArgumentException!"
        + "  The propertyKey value must implement the Comparable interface!");
    }
    catch (IllegalArgumentException e) {
      logger.debug("Test Passed: IllegalArgumentException thrown because the propertyKey value did not implement the Comparable interface!");
    }

    propertyKey = "goodPropertyKey";

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      logger.debug("Test Passed: The propertyKey is not null, is a key of the propertyMap, "
        + "and it's value implements the Comparable interface!");
    }
    catch (NullPointerException e) {
      fail("The propertyKey is not null!");
    }
    catch (IllegalArgumentException e) {
      fail("The propertyKey is a key of the propertyMap and it's value implements the Comparable interface!");
    }
  }

  public void testValidatePropertyMap() throws Exception {
    // Create the propertyKey!
    final String propertyKey = "property";

    // Create the propertyMap!
    Map propertyMap = null;

    PropertySearchFilter propertySearchFilter = null;

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      fail("The PropertySearchFilter.validatePropertyMap should have thrown a NullPointerException!"
        + "  The propertyMap is null!");
    }
    catch (NullPointerException e) {
      logger.debug("Test Passed: NullPointerException thrown because the propertyMap is null!");
    }

    propertyMap = new HashMap();
    propertyMap.put(null, null);

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      fail("The PropertySearchFilter.validatePropertyMap should have thrown a NullPointerException!"
        + "  The propertyMap contains a null key!");
    }
    catch (IllegalArgumentException e) {
      logger.debug("Test Passed: IllegalArgumentException thrown because the propertyMap contains a null key!");
    }

    propertyMap.clear();
    propertyMap.put("property", "value");

    try {
      propertySearchFilter = new PropertySearchFilter(propertyMap, propertyKey);
      logger.debug("Test Passed: propertyMap is not null and does not contain any null keys!");
    }
    catch (NullPointerException e) {
      fail("The propertyMap is not null!");
    }
    catch (IllegalArgumentException e) {
      fail("The propertyMap does not contain any null keys!");
    }
  }

  public static final class User implements Comparable {
    private final Integer ID;

    private final String firstName;
    private final String lastName;

    public User(final Integer ID,
                final String firstName,
                final String lastName) {
      if (ObjectUtil.isNull(ID)) {
        throw new NullPointerException("The ID cannot be null!");
      }
      this.ID = ID;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public int compareTo(final Object o) {
      return ID.compareTo((Integer) o);
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof User)) {
        return false;
      }

      final User user = (User) obj;

      return getId().equals(user.getId())
        && (getFirstName() == null ? user.getFirstName() == null : getFirstName().equals(user.getFirstName()))
        && (getLastName() == null ? user.getLastName() == null : getLastName().equals(user.getLastName()));
    }

    public String getFirstName() {
      return firstName;
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + (getId() == null ? 0 : getId().hashCode());
      hashValue = 37 * hashValue + (getFirstName() == null ? 0 : getFirstName().hashCode());
      hashValue = 37 * hashValue + (getLastName() == null ? 0 : getLastName().hashCode());
      return hashValue;
    }

    public Integer getId() {
      return ID;
    }

    public String getLastName() {
      return lastName;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{id = ");
      buffer.append(getId());
      buffer.append(", firstName = ").append(getFirstName());
      buffer.append(", lastName = ").append(getLastName());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
