/*
 * AbstractBeanHistoryTest.java (c) 26 January 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.AbstractBeanHistory
 * @see com.cp.common.beans.CommonBeanTestCase
 */

package com.cp.common.beans;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.AbstractVisitableCollection;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.ComposableFilter;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Filter;
import com.cp.common.util.InvertedFilter;
import com.cp.common.util.Visitor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AbstractBeanHistoryTest extends CommonBeanTestCase {

  public AbstractBeanHistoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBeanHistoryTest.class);
    //suite.addTest(new AbstractBeanHistoryTest("testName"));
    return suite;
  }

  protected void assertBeanHistoryDetail(final BeanHistory<?> beanHistory,
                                         final boolean expectedEmpty,
                                         final int expectedSize) {
    assertNotNull(beanHistory);
    assertEquals(expectedEmpty, beanHistory.isEmpty());
    assertEquals(expectedSize, beanHistory.size());
  }

  protected void assertCollectionDetail(final Collection<?> collection,
                                        final boolean expectedEmpty,
                                        final int expectedSize) {
    assertNotNull(collection);
    assertEquals(expectedEmpty, collection.isEmpty());
    assertEquals(expectedSize, collection.size());
  }

  protected <T extends Bean> void assertContains(final T[] expectedBeans,
                                final BeanHistory<T> actualBeanHistory,
                                final int... indices) {
    assertNotNull("The expected bean array should not be null!", expectedBeans);
    assertNotNull("The expected bean history should not be null!", actualBeanHistory);

    for (final int index : indices) {
      assertTrue(actualBeanHistory.contains(expectedBeans[index]));
    }
  }

  protected void assertContains(final Bean[] expectedArray,
                                final Collection<? extends Bean> actualCollection,
                                final int... indices) {
    assertNotNull("The expected array should not be null!", expectedArray);
    assertNotNull("The actual collection should not be null!", actualCollection);

    for (final int index : indices) {
      assertTrue(actualCollection.contains(expectedArray[index]));
    }
  }

  protected void assertContains(final Bean[] expectedBeans,
                                final Iterator<? extends Bean> actualIt,
                                final int... indices) {
    assertNotNull("The expected bean array should not be null!", expectedBeans);
    assertNotNull("The expected bean iterator should not be null!", actualIt);

    for (final int index : indices) {
      assertTrue(actualIt.hasNext());
      assertEquals(expectedBeans[index], actualIt.next());
    }

    assertFalse(actualIt.hasNext());
  }

  protected <T extends Bean> Collection<T> getCollection(final BeanHistory<T> beanHistory) {
    final Collection<T> theCollection = new LinkedList<T>();

    for (final T bean : beanHistory) {
      theCollection.add(bean);
    }

    return theCollection;
  }

  public void testAccept() throws Exception {
    final BeanHistory<MockBeanImpl> mockBeanHistory = AbstractBeanHistory.getBeanHistoryList();
    mockBeanHistory.add(new MockBeanImpl("MockBean1"));
    mockBeanHistory.add(new MockBeanImpl("MockBean2"));
    mockBeanHistory.add(new MockBeanImpl("MockBean3"));

    assertFalse(mockBeanHistory.isEmpty());
    assertEquals(3, mockBeanHistory.size());

    for (final MockBeanImpl bean : mockBeanHistory) {
      assertFalse(bean.isVisited());
    }

    mockBeanHistory.accept(new SetVisitedVisitor());

    for (final MockBeanImpl bean : mockBeanHistory) {
      assertTrue(bean.isVisited());
    }
  }

  public void testAddToList() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    final Person jonBloom = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");
    final Person sarahBloom = getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333");

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(jonBloom));
    assertTrue(personBeanHistory.add(sarahBloom));
    assertBeanHistoryDetail(personBeanHistory, false, 2);
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
    assertSame(personBeanHistory, sarahBloom.getBeanHistory());

    try {
      personBeanHistory.add(null);
      fail("Adding a null Person bean to the bean history should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean object being added to this bean history cannot be null!", e.getMessage());
    }

    assertEquals(2, personBeanHistory.size());

    final BeanHistory<Person> mockBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    final Person babyBloom = getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null);
    babyBloom.setBeanHistory(mockBeanHistory);

    assertSame(mockBeanHistory, babyBloom.getBeanHistory());

    try {
      personBeanHistory.add(babyBloom);
      fail("Adding Baby Bloom to the person bean history should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history (" + ClassUtil.getClassName(babyBloom.getBeanHistory()) + ") of bean ("
        + babyBloom.getClass().getName() + ") identified by id (" + babyBloom.getId()
        + ") is not null!", e.getMessage());
    }

    assertSame(mockBeanHistory, babyBloom.getBeanHistory());
    assertEquals(2, personBeanHistory.size());

    final Person johnBlum = getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    assertNull(johnBlum.getBeanHistory());
    assertTrue(personBeanHistory.add(johnBlum));
    assertEquals(3, personBeanHistory.size());
    assertSame(personBeanHistory, johnBlum.getBeanHistory());
  }

  public void testAddToSet() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    final Person jonBloom = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");
    final Person sarahBloom = getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333");

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(jonBloom));
    assertTrue(personBeanHistory.add(sarahBloom));
    assertBeanHistoryDetail(personBeanHistory, false, 2);
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
    assertSame(personBeanHistory, sarahBloom.getBeanHistory());

    try {
      personBeanHistory.add(null);
      fail("Adding a null Person bean to the bean history should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean object being added to this bean history cannot be null!", e.getMessage());
    }

    assertEquals(2, personBeanHistory.size());

    final BeanHistory<Person> mockBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    final Person babyBloom = getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null);
    babyBloom.setBeanHistory(mockBeanHistory);

    assertSame(mockBeanHistory, babyBloom.getBeanHistory());

    try {
      personBeanHistory.add(babyBloom);
      fail("Adding Baby Bloom to the person bean history should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history (" + ClassUtil.getClassName(babyBloom.getBeanHistory()) + ") of bean ("
        + babyBloom.getClass().getName() + ") identified by id (" + babyBloom.getId()
        + ") is not null!", e.getMessage());
    }

    assertSame(mockBeanHistory, babyBloom.getBeanHistory());
    assertEquals(2, personBeanHistory.size());

    final Person johnBlum = getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    assertNull(johnBlum.getBeanHistory());
    assertFalse(personBeanHistory.add(johnBlum));
    assertEquals(2, personBeanHistory.size());
    assertNull(johnBlum.getBeanHistory());
  }

  public void testAddWhenImmutable() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.isMutable());

    personBeanHistory.setMutable(Mutable.IMMUTABLE);

    assertFalse(personBeanHistory.isMutable());

    try {
      assertFalse(personBeanHistory.add(getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(2008, Calendar.MAY, 27), null)));
      fail("Calling addAll when the BeanHistory object is immutable should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history is not mutable!", e.getMessage());
    }

    assertFalse(personBeanHistory.isMutable());
    assertBeanHistoryDetail(personBeanHistory, true, 0);
  }

  public void testAddWithFilter() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    final Person jonBloom = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");
    final Person sarahBloom = getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789");
    final Person babyBloom = getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null);

    final Filter<Person> bornFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar today = DateUtil.getCalendar(2008, Calendar.FEBRUARY, 5);
        return (today.compareTo(person.getDateOfBirth()) >= 0);
      }
    };

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(jonBloom));
    assertTrue(personBeanHistory.add(sarahBloom));
    assertTrue(personBeanHistory.add(babyBloom));
    assertBeanHistoryDetail(personBeanHistory, false, 3);

    personBeanHistory.clear();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(jonBloom, bornFilter));
    assertTrue(personBeanHistory.add(sarahBloom, bornFilter));
    assertFalse(personBeanHistory.add(babyBloom, bornFilter));
    assertBeanHistoryDetail(personBeanHistory, false, 2);
  }

  public void testAddAll() throws Exception {
    final Person[] people = {
      getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333"),
      getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(2, "Sara", "Blum", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333"),
      getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")
    };

    final List<Person> peopleList = Arrays.asList(people);

    BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(people));
    assertBeanHistoryDetail(personBeanHistory, false, 5);

    AbstractVisitableCollection.getVisitableList(people).accept(new ResetBeanHistoryVisitor());
    personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.addAll(peopleList));
    assertBeanHistoryDetail(personBeanHistory, false, 3);
  }

  public void testAddAllWhenImmutable() throws Exception {
    final Person[] people = {
      getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333"),
      getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(2, "Sara", "Blum", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "111-22-3333"),
      getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.isMutable());

    personBeanHistory.setMutable(Mutable.IMMUTABLE);

    assertFalse(personBeanHistory.isMutable());

    try {
      assertFalse(personBeanHistory.addAll(people));
      fail("Calling addAll when the BeanHistory object is immutable should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history is not mutable!", e.getMessage());
    }

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.isMutable());
  }

  public void testAddAllWithFilter() throws Exception {
    final Person[] people = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(0, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(1, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(2, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(3, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(4, "Sour", "Doe", DateUtil.getCalendar(2004, Calendar.DECEMBER, 2), "555-55-5555"),
    };

    final List<Person> peopleList = Arrays.asList(people);

    final Filter<Person> lastNameFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return "bloom".equalsIgnoreCase(person.getLastName());
      }
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(people));
    assertBeanHistoryDetail(personBeanHistory, false, 8);

    personBeanHistory.clear();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.addAll(peopleList, lastNameFilter));
    assertBeanHistoryDetail(personBeanHistory, false, 3);
  }

  public void testAddAllFromBeanHistory() throws Exception {
    final Person[] people = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(0, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(1, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(2, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(3, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(4, "Sour", "Doe", DateUtil.getCalendar(2004, Calendar.DECEMBER, 2), "555-55-5555"),
    };

    final BeanHistory<Person> mockBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(mockBeanHistory, true, 0);
    assertTrue(mockBeanHistory.addAll(people));
    assertBeanHistoryDetail(mockBeanHistory, false, 8);

    for (final Person person : people) {
      assertSame(mockBeanHistory, person.getBeanHistory());
    }

    BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(mockBeanHistory));
    assertBeanHistoryDetail(personBeanHistory, false, 8);
    assertBeanHistoryDetail(mockBeanHistory, true, 0);

    for (final Person person : people) {
      assertSame(personBeanHistory, person.getBeanHistory());
    }

    assertBeanHistoryDetail(mockBeanHistory, true, 0);
    assertTrue(mockBeanHistory.addAll(personBeanHistory));
    assertBeanHistoryDetail(mockBeanHistory, false, 8);

    for (final Person person : people) {
      assertSame(mockBeanHistory, person.getBeanHistory());
    }

    personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.addAll(mockBeanHistory));
    assertBeanHistoryDetail(personBeanHistory, false, 5);
    assertContains(people, getCollection(personBeanHistory), 0, 1, 2, 6, 7);
  }

  public void testAddAllFromBeanHistoryWhenImmutable() throws Exception {
    final Person[] people = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(0, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(1, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(2, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(3, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(4, "Sour", "Doe", DateUtil.getCalendar(2004, Calendar.DECEMBER, 2), "555-55-5555"),
    };

    final BeanHistory<Person> mockBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(mockBeanHistory, true, 0);
    assertTrue(mockBeanHistory.addAll(people));
    assertBeanHistoryDetail(mockBeanHistory, false, 8);

    for (final Person person : people) {
      assertSame(mockBeanHistory, person.getBeanHistory());
    }

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.isMutable());

    personBeanHistory.setMutable(Mutable.IMMUTABLE);

    assertFalse(personBeanHistory.isMutable());

    try {
      assertFalse(personBeanHistory.addAll(mockBeanHistory));
      fail("Calling addAll when the BeanHistory object is immutable should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history is not mutable!", e.getMessage());
    }

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.isMutable());
    assertBeanHistoryDetail(mockBeanHistory, false, 8);

    for (final Person person : people) {
      assertSame(mockBeanHistory, person.getBeanHistory());
    }
  }

  public void testAddAllFromBeanHistoryWithFilter() throws Exception {
    final Person[] people = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(0, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(1, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(2, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(3, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(4, "Sour", "Doe", DateUtil.getCalendar(2004, Calendar.DECEMBER, 2), "555-55-5555"),
    };

    final BeanHistory<Person> mockBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(mockBeanHistory, true, 0);
    assertTrue(mockBeanHistory.addAll(people));
    assertBeanHistoryDetail(mockBeanHistory, false, 8);

    final Filter<Person> augustBirthdayFilter = new Filter<Person>() {

      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (dateOfBirth != null && Calendar.AUGUST == dateOfBirth.get(Calendar.MONTH));
      }
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(mockBeanHistory, augustBirthdayFilter));
    assertBeanHistoryDetail(personBeanHistory, false, 2);
    assertBeanHistoryDetail(mockBeanHistory, false, 6);
    assertContains(people, getCollection(mockBeanHistory), 0, 1, 3, 4, 5, 7);
    assertContains(people, getCollection(personBeanHistory), 2, 6);
  }

  public void testClear() throws Exception {
    final Person[] people = {
      getPerson(1, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(2, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(people));
    assertBeanHistoryDetail(personBeanHistory, false, 2);

    for (final Person person : people) {
      assertSame(personBeanHistory, person.getBeanHistory());
    }

    personBeanHistory.clear();

    assertBeanHistoryDetail(personBeanHistory, true, 0);

    for (final Person person : people) {
      assertNull(person.getBeanHistory());
    }
  }

  public void testClearWhenImmutable() throws Exception {
    final Person[] people = {
      getPerson(1, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(2, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(people));
    assertBeanHistoryDetail(personBeanHistory, false, 2);

    for (final Person person : people) {
      assertSame(personBeanHistory, person.getBeanHistory());
    }

    assertTrue(personBeanHistory.isMutable());

    personBeanHistory.setMutable(Mutable.IMMUTABLE);

    assertFalse(personBeanHistory.isMutable());

    try {
      personBeanHistory.clear();
      fail("Calling clear on an immutable BeanHistory object should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The bean history is not mutable!", e.getMessage());
    }

    assertFalse(personBeanHistory.isMutable());
    assertBeanHistoryDetail(personBeanHistory, false, 2);
  }

  public void testContains() throws Exception {
    final Person[] people = {
      getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.addAll(people)); // note, not all people will be added to the bean history
    assertBeanHistoryDetail(personBeanHistory, false, 3);
    assertTrue(personBeanHistory.contains(people[0]));
    assertTrue(personBeanHistory.contains(people[1]));
    assertTrue(personBeanHistory.contains(people[2]));
    assertFalse(personBeanHistory.contains(people[3]));
  }

  public void testContainsWithFilter() throws Exception {
    final Person[] people = {
      getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(2, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(3, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(1, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JULY, 4), "333-22-4444"),
      getPerson(2, "Sandy", "Handy", DateUtil.getCalendar(1971, Calendar.JUNE, 16), "111-22-3333"),
      getPerson(3, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.FEBRUARY, 5), "123-45-6789")
    };

    final Filter<Person> uniqueSsnFilter = new Filter<Person>() {

      private final Set<String> ssnSet = new TreeSet<String>();

      public boolean accept(final Person person) {
        return (ObjectUtil.isNull(person.getSsn()) || ssnSet.add(person.getSsn()));
      }
    };

    final Filter<Person> augustBirthdayFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && Calendar.AUGUST == dateOfBirth.get(Calendar.MONTH));
      }
    };

    final Filter<Person> februaryOrJulyBirthdayFilter = new Filter<Person>() {

      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && (Calendar.FEBRUARY == dateOfBirth.get(Calendar.MONTH)
          || Calendar.JULY == dateOfBirth.get(Calendar.MONTH)));
      }
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.addAll(people, uniqueSsnFilter)); // note, not all people will be added to the bean history
    assertBeanHistoryDetail(personBeanHistory, false, 4);
    assertTrue(personBeanHistory.contains(augustBirthdayFilter));
    assertFalse(personBeanHistory.contains(februaryOrJulyBirthdayFilter));
  }

  public void testGet() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);

    final Filter<Person> augustBirthdayFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && Calendar.AUGUST == dateOfBirth.get(Calendar.MONTH));
      }
    };

    Person person = personBeanHistory.get(augustBirthdayFilter);

    assertNotNull(person);
    assertEquals("Expected Baby Bloom!", expectedPeople[2], person);

    final Filter<Person> firstNameFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return "pie".equalsIgnoreCase(person.getFirstName());
      }
    };

    person = personBeanHistory.get(firstNameFilter);

    assertNotNull(person);
    assertEquals("Expected Pie Doe!", expectedPeople[6], person);

    final Filter<Person> augustBirthdayFirstNameFilter =
      ComposableFilter.composeAnd(augustBirthdayFilter, firstNameFilter);

    person = personBeanHistory.get(augustBirthdayFirstNameFilter);

    assertNotNull(person);
    assertEquals("Expected Pie Doe with August Birthday and First Name of 'Pie'!", expectedPeople[6], person);

    final Filter<Person> idFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return new Integer(11).equals(person.getPersonId());
      }
    };

    person = personBeanHistory.get(idFilter);

    assertNull(person);

    person = personBeanHistory.get((Filter<Person>[]) null);

    assertNotNull(person);
    assertEquals("Expected Jon Bloom!", expectedPeople[0], person);
  }

  public void testGetAll() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);

    final Filter<Person> lastNameFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return "doe".equalsIgnoreCase(person.getLastName());
      }
    };

    Collection<Person> actualPeople = personBeanHistory.getAll(lastNameFilter);

    assertCollectionDetail(actualPeople, false, 5);
    assertContains(expectedPeople, actualPeople, 3, 4, 5, 6, 7);

    final Filter<Person> augustBirthdayFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && Calendar.AUGUST == dateOfBirth.get(Calendar.MONTH));
      }
    };

    actualPeople = personBeanHistory.getAll(augustBirthdayFilter);

    assertCollectionDetail(actualPeople, false, 2);
    assertContains(expectedPeople, actualPeople, 2, 6);

    final Filter<Person> augustBirthdayLastNameFilter =
      ComposableFilter.composeAnd(augustBirthdayFilter, lastNameFilter);

    actualPeople = personBeanHistory.getAll(augustBirthdayLastNameFilter);

    assertCollectionDetail(actualPeople, false, 1);
    assertContains(expectedPeople, actualPeople, 6);

    final Filter<Person> firstNameFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return "jon".equalsIgnoreCase(person.getFirstName());
      }
    };

    actualPeople = personBeanHistory.getAll(firstNameFilter);

    assertCollectionDetail(actualPeople, false, 2);
    assertContains(expectedPeople, actualPeople, 0, 3);

    final Filter<Person> nullSsnFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return ObjectUtil.isNull(person.getSsn());
      }
    };

    actualPeople = personBeanHistory.getAll(nullSsnFilter);

    assertCollectionDetail(actualPeople, false, 4);
    assertContains(expectedPeople, actualPeople, 2, 8, 9, 10);

    actualPeople = personBeanHistory.getAll(new InvertedFilter<Person>(nullSsnFilter));

    assertCollectionDetail(actualPeople, false, 7);
    assertContains(expectedPeople, actualPeople, 0, 1, 3, 4, 5, 6, 7);

    final Filter<Person> negativeIdFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return (ObjectUtil.isNotNull(person.getId()) && person.getId() < 0);
      }
    };

    actualPeople = personBeanHistory.getAll(negativeIdFilter);

    assertCollectionDetail(actualPeople, true, 0);

    actualPeople = personBeanHistory.getAll((Filter<Person>[]) null);

    assertCollectionDetail(actualPeople, false, 11);
    assertContains(expectedPeople, actualPeople, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  public void testGetById() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);

    Person person = personBeanHistory.getById(1);

    assertNotNull(person);
    assertEquals("Expected Sarah Bloom!", expectedPeople[1], person);

    person = personBeanHistory.getById(-1);

    assertNull(person);
  }

  public void testIsEmpty() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertFalse(personBeanHistory.remove(getPerson(-1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertTrue(personBeanHistory.remove(getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "123-45-6789")));
    assertBeanHistoryDetail(personBeanHistory, true, 0);
  }

  public void testIteratorUsingList() throws Exception {
    final Person[] expectedPeople = {
      getPerson(1, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(2, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(3, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(4, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(5, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555")
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 5);

    int index = 0;

    for (final Person person : personBeanHistory) {
      assertEquals(expectedPeople[index++], person);
    }
  }

  public void testIteratorUsingFilter() throws Exception {
    final Person[] expectedPeople = {
      getPerson(1, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(2, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(3, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(4, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(5, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555")
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 5);

    final Filter<Person> personFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && dateOfBirth.get(Calendar.DAY_OF_MONTH) == 14);
      }
    };

    assertContains(expectedPeople, personBeanHistory.iterator(personFilter), 0, 2, 3);
  }

  public void testRemove() throws Exception {
    final Person jonBloom = getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");
    final Person johnBlum = getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444");

    BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertNull(jonBloom.getBeanHistory());
    assertFalse(personBeanHistory.contains(jonBloom));
    assertTrue(personBeanHistory.add(jonBloom));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertTrue(personBeanHistory.contains(jonBloom));
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
    assertNull(johnBlum.getBeanHistory());
    assertFalse(personBeanHistory.contains(johnBlum));
    assertFalse(personBeanHistory.remove(johnBlum));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertTrue(personBeanHistory.contains(jonBloom));
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
    assertTrue(personBeanHistory.remove(jonBloom));
    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertNull(jonBloom.getBeanHistory());
    assertFalse(personBeanHistory.contains(jonBloom));

    personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertNull(jonBloom.getBeanHistory());
    assertFalse(personBeanHistory.contains(jonBloom));
    assertTrue(personBeanHistory.add(jonBloom));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertTrue(personBeanHistory.contains(jonBloom));
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
    assertNull(johnBlum.getBeanHistory());
    assertFalse(personBeanHistory.contains(johnBlum));
    assertTrue(personBeanHistory.remove(johnBlum));
    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertFalse(personBeanHistory.contains(jonBloom));
    assertSame(personBeanHistory, jonBloom.getBeanHistory());
  }

  public void testRemoveUsingFilter() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);

    final Filter<Person> personFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && dateOfBirth.get(Calendar.DAY_OF_MONTH) % 2 == 0);
      }
    };

    assertTrue(personBeanHistory.remove(personFilter));
    assertBeanHistoryDetail(personBeanHistory, false, 3);
    assertContains(expectedPeople, personBeanHistory, 0, 9, 10);
  }

  public void testRemoveAll() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistoryList();

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);
    assertContains(expectedPeople, personBeanHistory, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    final Set<Person> expectedPeopleSet = CollectionUtil.getSet(
      expectedPeople[3],
      expectedPeople[4],
      expectedPeople[5],
      expectedPeople[6],
      expectedPeople[7]
    );

    assertTrue(personBeanHistory.removeAll(expectedPeopleSet));
    assertBeanHistoryDetail(personBeanHistory, false, 6);
    assertContains(expectedPeople, personBeanHistory, 0, 1, 2, 8, 9, 10);
  }

  public void testSize() throws Exception {
    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.add(getPerson(1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertFalse(personBeanHistory.add(getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "123-45-6789")));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertFalse(personBeanHistory.remove(getPerson(-1, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444")));
    assertBeanHistoryDetail(personBeanHistory, false, 1);
    assertTrue(personBeanHistory.remove(getPerson(1, "John", "Blum", DateUtil.getCalendar(1974, Calendar.MAY, 27), "123-45-6789")));
    assertBeanHistoryDetail(personBeanHistory, true, 0);
  }

  public void testSizeWithFilter() throws Exception {
    final Person[] expectedPeople = {
      getPerson(0, "Jon", "Bloom", DateUtil.getCalendar(1974, Calendar.MAY, 27), "333-22-4444"),
      getPerson(1, "Sarah", "Bloom", DateUtil.getCalendar(1975, Calendar.JANUARY, 22), "123-45-6789"),
      getPerson(2, "Baby", "Bloom", DateUtil.getCalendar(2008, Calendar.AUGUST, 26), null),
      getPerson(3, "Jon", "Doe", DateUtil.getCalendar(1977, Calendar.JULY, 14), "111-11-1111"),
      getPerson(4, "Jane", "Doe", DateUtil.getCalendar(1979, Calendar.DECEMBER, 2), "222-22-2222"),
      getPerson(5, "Bob", "Doe", DateUtil.getCalendar(2001, Calendar.FEBRUARY, 14), "333-33-3333"),
      getPerson(6, "Pie", "Doe", DateUtil.getCalendar(2002, Calendar.AUGUST, 14), "444-44-4444"),
      getPerson(7, "Sour", "Doe", DateUtil.getCalendar(2005, Calendar.DECEMBER, 2), "555-55-5555"),
      getPerson(8, "Jack", "Handy", DateUtil.getCalendar(1969, Calendar.JUNE,  16), null),
      getPerson(9, "Sandy", "Handy", DateUtil.getCalendar(1970, Calendar.NOVEMBER, 11), null),
      getPerson(10, "Randy", "Handy", DateUtil.getCalendar(1991, Calendar.OCTOBER, 3), null)
    };

    final BeanHistory<Person> personBeanHistory = AbstractBeanHistory.getBeanHistorySet(null);

    assertBeanHistoryDetail(personBeanHistory, true, 0);
    assertTrue(personBeanHistory.addAll(expectedPeople));
    assertBeanHistoryDetail(personBeanHistory, false, 11);

    final Filter<Person> augustBirthdayFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        final Calendar dateOfBirth = person.getDateOfBirth();
        return (ObjectUtil.isNotNull(dateOfBirth) && dateOfBirth.get(Calendar.MONTH) == Calendar.AUGUST);
      }
    };

    assertEquals(2, personBeanHistory.size(augustBirthdayFilter));

    final Filter<Person> lastNameDoeFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return "doe".equalsIgnoreCase(person.getLastName());
      }
    };

    assertEquals(5, personBeanHistory.size(lastNameDoeFilter));

    final Filter<Person> nullSsnFilter = new Filter<Person>() {
      public boolean accept(final Person person) {
        return ObjectUtil.isNull(person.getSsn());
      }
    };

    assertEquals(4, personBeanHistory.size(nullSsnFilter));
  }

  protected static final class MockBeanImpl extends com.cp.common.test.mock.MockBeanImpl {

    private boolean visited = false;

    public MockBeanImpl(final Object value) {
      super(value);
    }

    public boolean isVisited() {
      return visited;
    }

    public void setVisited(final boolean visited) {
      this.visited = visited;
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof MockBeanImpl)) {
        return false;
      }

      final MockBeanImpl that = (MockBeanImpl) obj;

      return ObjectUtil.equals(getValue(), that.getValue())
        && ObjectUtil.equals(isVisited(), that.isVisited());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(isVisited());
      return hashValue;
    }
  }

  protected static final class ResetBeanHistoryVisitor implements Visitor {

    public void visit(final Visitable obj) {
      if (obj instanceof Bean) {
        ((Bean) obj).setBeanHistory(null);
      }
    }
  }

  protected static final class SetVisitedVisitor implements Visitor {

    public void visit(final Visitable obj) {
      if (obj instanceof MockBeanImpl) {
        ((MockBeanImpl) obj).setVisited(true);
      }
    }
  }

}
