/*
 * AbstractBeanDeclarationTest.java (c) 5 August 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.AbstractBeanDeclaration
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractBeanDeclarationTest extends TestCase {

  public AbstractBeanDeclarationTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBeanDeclarationTest.class);
    return suite;
  }

  protected BeanDeclaration getBeanDeclaration(final String id, final String className) {
    return new DefaultBeanDeclaration(id, className);
  }

  public void testGetScope() throws Exception {
    final BeanDeclaration beanDeclaration = getBeanDeclaration("1", "com.mycompany.mypackage.MyBean");

    assertNotNull(beanDeclaration);
    assertTrue(beanDeclaration.isPrototype());
    assertFalse(beanDeclaration.isSingleton());
    assertEquals(Scope.PROTOTYPE, beanDeclaration.getScope());

    beanDeclaration.setScope(Scope.SINGLETON);

    assertFalse(beanDeclaration.isPrototype());
    assertTrue(beanDeclaration.isSingleton());
    assertEquals(Scope.SINGLETON, beanDeclaration.getScope());

    beanDeclaration.setScope(Scope.PROTOTYPE);

    assertTrue(beanDeclaration.isPrototype());
    assertFalse(beanDeclaration.isSingleton());
    assertEquals(Scope.PROTOTYPE, beanDeclaration.getScope());

    beanDeclaration.setScope(null);

    assertTrue(beanDeclaration.isPrototype());
    assertFalse(beanDeclaration.isSingleton());
    assertEquals(Scope.PROTOTYPE, beanDeclaration.getScope());
  }

  public void testGetType() throws Exception {
    final BeanDeclaration beanDeclaration = getBeanDeclaration("1", TestBean.class.getName());

    assertNotNull(beanDeclaration);
    assertEquals("1", beanDeclaration.getId());
    assertEquals(TestBean.class.getName(), beanDeclaration.getClassName());

    Class type = null;

    try {
      type = beanDeclaration.getType();
    }
    catch (Exception e) {
      fail("Getting the Class type of the bean declaration threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(type);
    assertEquals(TestBean.class, type);
  }

  public void testGetTypeThrowsClassNotFoundException() throws Exception {
    final BeanDeclaration beanDeclaration = getBeanDeclaration("1", "com.mycompany.mypackage.MyBean");

    assertNotNull(beanDeclaration);
    assertEquals("1", beanDeclaration.getId());
    assertEquals("com.mycompany.mypackage.MyBean", beanDeclaration.getClassName());

    Class type = null;

    try {
      type = beanDeclaration.getType();
      fail("Calling getType on a fully-qualified class name not found in the classpath should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getType on a fully-qualified class name not found in the classpath threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(type);
  }

  public static final class TestBean {
  }

}
