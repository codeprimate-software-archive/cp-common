/*
 * AbstractExtendedFossilDaoTest.java (c) 17 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.17
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.DefaultUser
 * @see com.cp.common.beans.User
 * @see com.cp.common.dao.AbstractExtendedFossilDao
 */

package com.cp.common.dao;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.lang.ExceptionUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.util.CollectionUtil;

import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractExtendedFossilDaoTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractExtendedFossilDaoTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractExtendedFossilDaoTest.class);
    return suite;
  }

  protected static User getUser(final String username) {
    return new DefaultUser(username);
  }

  protected <T extends Bean<I>, I extends Comparable<I>> ExtendedFossilDao<T, I> getExtendedFossilDao(final Set<I> idSet, final boolean throwException) {
    return new TestExtendedFossilDao<T, I>(idSet, throwException);
  }

  public void testLoadAll() throws Exception {
    final Set<Integer> idSet = CollectionUtil.getSet(1, 2, 3);
    final ExtendedFossilDao<Bean<Integer>, Integer> dao = getExtendedFossilDao(idSet, false);
    List<Bean<Integer>> actualBeans = null;

    assertNull(actualBeans);

    try {
      actualBeans = dao.loadAll(new MockBeanImpl());
    }
    catch (Exception e) {
      fail("Calling loadAll threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualBeans);
    assertEquals(3, actualBeans.size());

    for (final Bean<Integer> actualBean : actualBeans) {
      assertTrue(idSet.contains(actualBean.getId()));
      assertEquals(getUser("AbstractExtendedFossilDaoTest"), actualBean.getCreatedBy());
    }
  }

  public void testLoadAllWhenFindAllReturnsNoIds() throws Exception {
    final ExtendedFossilDao<Bean<Integer>, Integer> dao = getExtendedFossilDao(Collections.<Integer>emptySet(), false);
    List<Bean<Integer>> actualBeans = null;

    assertNull(actualBeans);

    try {
      actualBeans = dao.loadAll(new MockBeanImpl());
    }
    catch (Exception e) {
      fail("Calling loadAll when findAll returns no IDs threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualBeans);
    assertTrue(actualBeans.isEmpty());
  }

  public void testLoadAllWhenLoadThrowsDataAccessException() throws Exception {
    final Set<Integer> idSet = new TreeSet<Integer>(CollectionUtil.getSet(1, 2, 3));
    final ExtendedFossilDao<Bean<Integer>, Integer> dao = getExtendedFossilDao(idSet, true);
    List<Bean<Integer>> actualBeans = null;

    assertNull(actualBeans);

    try {
      actualBeans = dao.loadAll(new MockBeanImpl());
      fail("Calling loadAll when load throws a DataAccessException should have thrown a DataAccessException!");
    }
    catch (DataAccessException e) {
      assertEquals("Loading Bean having ID (1) from the data source failed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling loadAll when load throws a DataAccessException threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualBeans);
  }

  public void testLoadAllWhenExceptionIsThrown() throws Exception {
    final ExtendedFossilDao<Bean<Integer>, Integer> dao = this.<Bean<Integer>, Integer>getExtendedFossilDao(
      new TreeSet<Integer>(CollectionUtil.<Integer>getSet(1, 2, 3)), false);
    List<Bean<Integer>> actualBeans = null;

    assertNull(actualBeans);

    try {
      actualBeans = dao.loadAll(new TestBean(1));
      fail("Calling loadAll when an Exception is thrown should have thrown a DataAccessException!");
    }
    catch (DataAccessException e) {
      assertEquals("Failed to load Bean having ID (1) from the data source!", e.getMessage());
      final Throwable cause = ExceptionUtil.getRootCause(e);
      assertTrue(cause instanceof UnsupportedOperationException);
      assertEquals("Not Implemented!", cause.getMessage());
    }
    catch (Exception e) {
      fail("Calling loadAll when an Exception is thrown threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBeans);
  }

  public void testRemoveAllDeletesAllBeans() throws Exception {
    final Bean<Integer> mockBean0 = context.mock(Bean.class, "0");
    final Bean<Integer> mockBean1 = context.mock(Bean.class, "1");
    final Bean<Integer> mockBean2 = context.mock(Bean.class, "2");

    context.checking(new Expectations() {{
      one(mockBean0).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean0).isModified();
      will(returnValue(true));
      one(mockBean1).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean1).isModified();
      will(returnValue(true));
      one(mockBean2).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean2).isModified();
      will(returnValue(true));
    }});

    final ExtendedFossilDao<Bean<Integer>, Integer> dao = this.<Bean<Integer>, Integer>getExtendedFossilDao(null, false);
    final List<Bean<Integer>> actualBeans = CollectionUtil.getList(mockBean0, mockBean1, mockBean2);

    try {
      assertTrue(dao.removeAll(actualBeans));
    }
    catch (Exception e) {
      fail("Calling removeAll deleting all Beans in the Collection threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testRemoveAllDeletesSomeBeans() throws Exception {
    final Bean<Integer> mockBean0 = context.mock(Bean.class, "0");
    final Bean<Integer> mockBean1 = context.mock(Bean.class, "1");
    final Bean<Integer> mockBean2 = context.mock(Bean.class, "2");

    context.checking(new Expectations() {{
      one(mockBean0).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean0).isModified();
      will(returnValue(true));
      one(mockBean1).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean1).isModified();
      will(returnValue(false));
      one(mockBean2).setId(with(Matchers.nullValue(Integer.class)));
      one(mockBean2).isModified();
      will(returnValue(true));
    }});

    final ExtendedFossilDao<Bean<Integer>, Integer> dao = this.<Bean<Integer>, Integer>getExtendedFossilDao(null, false);
    final List<Bean<Integer>> actualBeans = CollectionUtil.getList(mockBean0, mockBean1, mockBean2);

    try {
      assertFalse(dao.removeAll(actualBeans));
    }
    catch (Exception e) {
      fail("Calling removeAll deleting only a subset of the Beans in the Collection threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testSaveAll() throws Exception {
    final Bean<Integer> mockBean0 = context.mock(Bean.class, "0");
    final Bean<Integer> mockBean1 = context.mock(Bean.class, "1");
    final Bean<Integer> mockBean2 = context.mock(Bean.class, "2");

    context.checking(new Expectations() {{
      one(mockBean0).getId();
      will(returnValue(1));
      one(mockBean0).commit();
      one(mockBean1).getId();
      will(returnValue(2));
      one(mockBean1).commit();
      one(mockBean2).getId();
      will(returnValue(3));
      one(mockBean2).commit();
    }});

    final ExtendedFossilDao<Bean<Integer>, Integer> dao = this.<Bean<Integer>, Integer>getExtendedFossilDao(null, false);

    final List<Bean<Integer>> expectedBeans = CollectionUtil.getList(mockBean0, mockBean1, mockBean2);
    List<Bean<Integer>> actualBeans = null;

    assertNull(actualBeans);

    try {
      actualBeans = dao.saveAll(expectedBeans);
    }
    catch (Exception e) {
      fail("Calling saveAll with a Collection of Beans threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualBeans);

    int index = 0;

    for (final Bean expectedBean : expectedBeans) {
      assertSame(expectedBean, actualBeans.get(index++));
    }

    context.assertIsSatisfied();
  }

  protected static final class TestBean extends AbstractBean<Integer> {

    public TestBean() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public TestBean(final Integer id) {
      setId(id);
    }

    public TestBean(final TestBean bean) {
      setId(bean.getId());
    }
  }

  protected static final class TestExtendedFossilDao<T extends Bean<I>, I extends Comparable<I>> extends AbstractExtendedFossilDao<T, I> {

    private final boolean throwException;
    private final Set<I> idSet;

    public TestExtendedFossilDao(final Set<I> idSet, final boolean throwException) {
      this.idSet = ObjectUtil.getDefaultValue(idSet, Collections.<I>emptySet());
      this.throwException = throwException;
    }

    public Set<I> findAll(final T bean) throws DataAccessException {
      return idSet;
    }

    public T load(final T bean) throws DataAccessException {
      if (!throwException) {
        bean.setCreatedBy(getUser("AbstractExtendedFossilDaoTest"));
        bean.commit();
        return bean;
      }

      throw new DataAccessException("Loading Bean having ID (" + bean.getId() + ") from the data source failed!");
    }

    public boolean remove(final T bean) throws DataAccessException {
      bean.setId(null);
      return bean.isModified();
    }

    public T save(final T bean) throws DataAccessException {
      bean.getId();
      bean.commit();
      return bean;
    }
  }
}
