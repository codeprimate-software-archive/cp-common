/*
 * AbstractFossilDaoTest.java (c) 17 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.17
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.dao.AbstractFossilDao
 */

package com.cp.common.dao;

import com.cp.common.beans.Bean;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractFossilDaoTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractFossilDaoTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractFossilDaoTest.class);
    return suite;
  }

  protected <T extends Bean> FossilDao<T> getFossilDao(final boolean found, final boolean throwException) {
    return new TestFossilDao<T>(found, throwException);
  }

  public void testFindHasFoundBean() throws Exception {
    try {
      assertTrue(getFossilDao(true, false).find(context.mock(Bean.class)));
    }
    catch (Exception e) {
      fail("Calling find with an existing mock Bean threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testFindWithUnfoundBean() throws Exception {
    try {
      assertFalse(getFossilDao(false, false).find(context.mock(Bean.class)));
    }
    catch (Exception e) {
      fail("Calling find with an unfound mock Bean threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testFindWhenLoadThrowsDataAccessException() throws Exception {
    final Bean mockBean = context.mock(Bean.class);

    context.checking(new Expectations()  {{
      oneOf(mockBean).getId();
      will(returnValue(1));
    }});

    try {
      assertFalse(getFossilDao(false, true).find(mockBean));
    }
    catch (Exception e) {
      fail("Calling find with a mock Bean object when load throws a DataAccessException threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  protected static final class TestFossilDao<T extends Bean> extends AbstractFossilDao<T> {

    private final boolean found;
    private final boolean throwException;

    public TestFossilDao(final boolean found, final boolean throwException) {
      this.found = found;
      this.throwException = throwException;
    }

    public T load(final T bean) throws DataAccessException {
      if (throwException) {
        throw new DataAccessException("Failed to load Bean having ID (" + bean.getId() + ")!");
      }
      else {
        return (found ? bean : null);
      }
    }

    public boolean remove(final T bean) throws DataAccessException {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public T save(final T bean) throws DataAccessException {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
