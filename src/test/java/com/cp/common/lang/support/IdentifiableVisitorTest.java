/*
 * IdentifiableVisitorTest.java (c) 14 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.13
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.lang.support.IdentifiableVisitor
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IdentifiableVisitorTest extends TestCase {

  public IdentifiableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IdentifiableVisitorTest.class);
    //suite.addTest(new IdentifiableVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final MockBean bean0 = new MockBeanImpl(0);
    final MockBean bean1 = new MockBeanImpl(1, bean0);
    final MockBean bean2 = new MockBeanImpl(2, bean1);

    assertEquals(new Integer(0), bean0.getId());
    assertEquals(new Integer(1), bean1.getId());
    assertEquals(new Integer(2), bean2.getId());

    bean2.accept(new IdentifiableVisitor());

    assertNull(bean0.getId());
    assertNull(bean1.getId());
    assertNull(bean2.getId());
  }

  protected static interface MockBean extends Identifiable<Integer>, Visitable {
  }

  protected static class MockBeanImpl implements MockBean {

    private Integer id;
    private Visitable visitableObject;

    public MockBeanImpl() {
    }

    public MockBeanImpl(final Integer id) {
      setId(id);
    }

    public MockBeanImpl(final Integer id, final Visitable visitableObject) {
      setId(id);
      this.visitableObject = visitableObject;
    }

    public Integer getId() {
      return id;
    }

    public void setId(final Integer id) {
      this.id = id;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(visitableObject)) {
        visitableObject.accept(visitor);
      }
    }
  }

}
