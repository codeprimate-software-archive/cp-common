/*
 * AbstractListenerDeclarationTest.java (c) 6 August 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.AbstractListenerDeclaration
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractListenerDeclarationTest extends TestCase {

  public AbstractListenerDeclarationTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractListenerDeclarationTest.class);
    return suite;
  }

  protected ListenerDeclaration getListenerDeclaration(final String className) {
    return new DefaultListenerDeclaration(className);
  }

  public void testGetScope() throws Exception {
    final ListenerDeclaration listenerDeclaration = getListenerDeclaration("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDeclaration);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDeclaration.getClassName());
    assertEquals(Scope.PROTOTYPE, listenerDeclaration.getScope());
    assertTrue(listenerDeclaration.isPrototype());
    assertFalse(listenerDeclaration.isSingleton());

    listenerDeclaration.setScope(Scope.SINGLETON);

    assertEquals(Scope.SINGLETON, listenerDeclaration.getScope());
    assertFalse(listenerDeclaration.isPrototype());
    assertTrue(listenerDeclaration.isSingleton());

    listenerDeclaration.setScope(null);

    assertEquals(Scope.PROTOTYPE, listenerDeclaration.getScope());
    assertTrue(listenerDeclaration.isPrototype());
    assertFalse(listenerDeclaration.isSingleton());

    listenerDeclaration.setScope(Scope.PROTOTYPE);

    assertEquals(Scope.PROTOTYPE, listenerDeclaration.getScope());
    assertTrue(listenerDeclaration.isPrototype());
    assertFalse(listenerDeclaration.isSingleton());
  }

  public void testGetType() throws Exception {
    final ListenerDeclaration listenerDeclaration = getListenerDeclaration(TestListener.class.getName());

    assertNotNull(listenerDeclaration);
    assertEquals(TestListener.class.getName(), listenerDeclaration.getClassName());

    Class type = null;

    try {
      type = listenerDeclaration.getType();
    }
    catch (Exception e) {
      fail("Calling getType with a valid listener class name threw an unexpected Exception (" + e.getMessage() + "!");
    }

    assertNotNull(type);
    assertEquals(TestListener.class, type);
  }

  public void testGetTypeThrowsClassNotFoundException() throws Exception {
    final ListenerDeclaration listenerDeclaration = getListenerDeclaration("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDeclaration);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDeclaration.getClassName());

    Class type = null;

    try {
      type = listenerDeclaration.getType();
      fail("Calling getType with an invalid listener class name should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getType with an invalid listener class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(type);
  }

  public static final class TestListener {
  }

}
