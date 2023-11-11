/*
 * AbstractPropertyChangeManagerTest.java (c) 17 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.support.AbstractPropertyChangeManager
 * @see com.cp.common.beans.support.CommonSupportTestCase
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class AbstractPropertyChangeManagerTest extends CommonSupportTestCase {

  public AbstractPropertyChangeManagerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractPropertyChangeManagerTest.class);
    //suite.addTest(new AbstractPropertyChangeManagerTest("testName"));
    return suite;
  }

  public void testGetBean() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final MockPropertyChangeManager propertyChangeManager = new MockPropertyChangeManager(mockBean);

    assertSame(mockBean, propertyChangeManager.getBean());
  }

  public void testInstantiation() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    AbstractPropertyChangeManager propertyChangeManager = null;

    assertNull(propertyChangeManager);

    try {
      propertyChangeManager = new MockPropertyChangeManager(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the AbstractPropertyChangeManager with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyChangeManager);
    assertSame(mockBean, propertyChangeManager.getBean());
  }

  public void testInstantiationWithNullBean() throws Exception {
    AbstractPropertyChangeManager propertyChangeManager = null;

    assertNull(propertyChangeManager);

    try {
      propertyChangeManager = new MockPropertyChangeManager(null);
      fail("Instantiating an instance of MockPropertyChangeManager with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean managed by this PropertyChangeManager cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the AbstractPropertyChangeManager with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyChangeManager);
  }

  private static final class MockPropertyChangeManager extends AbstractPropertyChangeManager {

    public MockPropertyChangeManager(final Bean bean) {
      super(bean);
    }

    public boolean contains(final String propertyName) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public boolean doCommit(final Bean bean) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public boolean doRollback(final Bean bean) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public Set<String> getModifiedProperties() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public boolean hasModifiedProperties() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public void propertyChange(final PropertyChangeEvent evt) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
