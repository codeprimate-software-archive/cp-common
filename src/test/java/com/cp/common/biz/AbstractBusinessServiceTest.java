/*
 * AbstractBusinessServiceTest.java (c) 17 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.8.13
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.biz.AbstractBusinessService
 * @see com.cp.common.dao.FossilDao
 * @see junit.framework.TestCase
 */

package com.cp.common.biz;

import com.cp.common.beans.Bean;
import com.cp.common.dao.FossilDao;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractBusinessServiceTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractBusinessServiceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBusinessServiceTest.class);
    return suite;
  }

  protected <T extends Bean, BUSINESS_PROCESS> AbstractBusinessService<T, BUSINESS_PROCESS> getBusinessHandler() {
    return new TestBusinessService<T, BUSINESS_PROCESS>();
  }

  protected <T extends Bean, BUSINESS_PROCESS> AbstractBusinessService<T, BUSINESS_PROCESS> getBusinessHandler(final FossilDao<T> dao) {
    return new TestBusinessService<T, BUSINESS_PROCESS>(dao);
  }

  public void testGetDaoWithNonNullReference() throws Exception {
    final FossilDao<Bean> expectedDao = context.mock(FossilDao.class);
    FossilDao<Bean> actualDao = null;

    final AbstractBusinessService<Bean, Object> businessHandler = getBusinessHandler(expectedDao);

    assertNull(actualDao);

    try {
      actualDao = businessHandler.getDao();
    }
    catch (Exception e) {
      fail("Calling getDao with a non-null FossilDao object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertSame(expectedDao, actualDao);
  }

  public void testGetDaoWithNullReference() throws Exception {
    final AbstractBusinessService<Bean, Object> businessHandler = getBusinessHandler();
    FossilDao<Bean> actualDao = null;

    assertNull(actualDao);

    try {
      actualDao = businessHandler.getDao();
      fail("Calling getDao with a null DAO reference should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The DAO for this business handler has not been properly initialized!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getDao with a null DAO reference threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualDao);
  }

  public void testSetDaoWithNonNullObject() throws Exception {
    final FossilDao<Bean> mockDao = context.mock(FossilDao.class);
    final AbstractBusinessService<Bean, Object> businessHandler = getBusinessHandler();

    try {
      businessHandler.setDao(mockDao);
    }
    catch (Exception e) {
      fail("Calling setDao with a non-null DAO object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertSame(mockDao, businessHandler.getDao());
  }

  public void testSetDaoWithNullObject() throws Exception {
    final AbstractBusinessService<Bean, Object> businessHandler = getBusinessHandler();

    try {
      businessHandler.setDao(null);
      fail("Calling setDao with a null object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The DAO for this business handler cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setDao with a non-null DAO object threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testLoad() throws Exception {
    final Bean<Integer> mockBean = context.mock(Bean.class);
    final FossilDao<Bean> mockDao = context.mock(FossilDao.class);

    context.checking(new Expectations() {{
      one(mockDao).load(with(Matchers.sameInstance(mockBean)));
      will(returnValue(mockBean));
    }});

    final BusinessService<Bean, Object> businessService = getBusinessHandler(mockDao);

    assertSame(mockBean, businessService.load(mockBean));

    context.assertIsSatisfied();
  }

  public void testProcess() throws Exception {
    final Bean<Integer> mockBean = context.mock(Bean.class);
    final FossilDao<Bean> mockDao = context.mock(FossilDao.class);

    final BusinessService<Bean, Object> businessService = getBusinessHandler(mockDao);

    try {
      businessService.process(mockBean, new Object());
      fail("Calling the process method on the AbstractBusinessService class should have thrown an UnsuportedOperationException for not being implemented!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Not Implemented!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling the process method on the AbstractBusinessService class threw an unexpected Throwable ("
        + t.getMessage() + ")");
    }
  }

  public void testRemove() throws Exception {
    final Bean<Integer> mockBean = context.mock(Bean.class);
    final FossilDao<Bean> mockDao = context.mock(FossilDao.class);

    context.checking(new Expectations() {{
      one(mockDao).remove(with(Matchers.sameInstance(mockBean)));
      will(returnValue(true));
    }});

    final BusinessService<Bean, Object> businessService = getBusinessHandler(mockDao);

    assertTrue(businessService.remove(mockBean));

    context.assertIsSatisfied();
  }

  public void testSave() throws Exception {
    final Bean<Integer> mockBean = context.mock(Bean.class);
    final FossilDao<Bean> mockDao = context.mock(FossilDao.class);

    context.checking(new Expectations() {{
      one(mockDao).save(with(Matchers.sameInstance(mockBean)));
      will(returnValue(mockBean));
    }});

    final BusinessService<Bean, Object> businessService = getBusinessHandler(mockDao);

    assertSame(mockBean, businessService.save(mockBean));

    context.assertIsSatisfied();
  }

  protected static final class TestBusinessService<T extends Bean, BUSINESS_PROCESS> extends AbstractBusinessService<T, BUSINESS_PROCESS> {

    public TestBusinessService() {
    }

    public TestBusinessService(final FossilDao<T> dao) {
      setDao(dao);
    }
  }

}
