/*
 * AbstractReferenceObjectTest.java (c) 4 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.21
 * @see com.cp.common.beans.definition.AbstractReferenceObject
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.test.mock.MockBean;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractReferenceObjectTest extends TestCase {

  public AbstractReferenceObjectTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractReferenceObjectTest.class);
    return suite;
  }

  protected ReferenceObject getReferenceObject(final String referenceId, final Class type) {
    return new TestReferenceObject(referenceId, type);
  }

  public void testInstantiate() throws Exception {
    ReferenceObject referenceObject = null;

    assertNull(referenceObject);

    try {
      referenceObject = getReferenceObject("myBean", MockBean.class);
    }
    catch (Exception e) {
      fail("Instantiating an instance of ReferenceObject with a non-null, non-empty reference ID and Class type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(referenceObject);
    assertEquals("myBean", referenceObject.getReferenceId());
    assertEquals(MockBean.class, referenceObject.getType());
  }

  public void testInstantiateWithEmptyId() throws Exception {
    ReferenceObject referenceObject = null;

    assertNull(referenceObject);

    try {
      referenceObject = getReferenceObject(" ", MockBean.class);
      fail("Instantiating an instance of ReferenceObject with an empty reference ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The reference id of the referred object cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ReferenceObject with an empty reference ID threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(referenceObject);
  }

  public void testInstantiateWithNullId() throws Exception {
    ReferenceObject referenceObject = null;

    assertNull(referenceObject);

    try {
      referenceObject = getReferenceObject(null, MockBean.class);
      fail("Instantiating an instance of ReferenceObject with a null reference ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The reference id of the referred object cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ReferenceObject with a null reference ID threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(referenceObject);
  }

  public void testInstantiateWithNullType() throws Exception {
    ReferenceObject referenceObject = null;

    assertNull(referenceObject);

    try {
      referenceObject = getReferenceObject("myBean", null);
    }
    catch (Exception e) {
      fail("Instantiating an instance of ReferenceObject with a non-null, non-empty reference ID and null Class type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(referenceObject);
    assertEquals("myBean", referenceObject.getReferenceId());
    assertNull(referenceObject.getType());
  }

  protected static final class TestReferenceObject extends AbstractReferenceObject {

    public TestReferenceObject(final String referenceId, final Class type) {
      super(referenceId, type);
    }
  }

}
