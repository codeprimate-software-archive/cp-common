/*
 * AbstractListenerDefinitionTest.java (c) 4 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.AbstractListenerDefinition
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.test.util.TestUtil;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractListenerDefinitionTest extends TestCase {

  public AbstractListenerDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractListenerDefinitionTest.class);
    return suite;
  }

  protected ListenerDefinition getListenerDefinition(final String className) {
    return new TestListenerDefinition(className);
  }

  public void testInstantiate() throws Exception {
    ListenerDefinition listenerDefinition = null;

    try {
      listenerDefinition = getListenerDefinition("com.mycompany.mypackage.MyListener");
    }
    catch (Exception e) {
      fail("Instantiating an instance of ListenerDefinition with a non-null, non-empty listener class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listenerDefinition);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDefinition.getClassName());
  }

  public void testInstantiateWithEmptyListener() throws Exception {
    ListenerDefinition listenerDefinition = null;

    try {
      listenerDefinition = getListenerDefinition(" ");
      fail("Instantiating an instance of ListenerDefinition with an empty listener class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the listener cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ListenerDefinition with an empty listener class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listenerDefinition);
  }

  public void testInstantiateWithNullListener() throws Exception {
    ListenerDefinition listenerDefinition = null;

    try {
      listenerDefinition = getListenerDefinition(null);
      fail("Instantiating an instance of ListenerDefinition with a null listener class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the listener cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ListenerDefinition with a null listener class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listenerDefinition);
  }

  public void testAddProperty() throws Exception {
    final ListenerDefinition listenerDefinition = getListenerDefinition("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDefinition);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDefinition.getClassName());
    assertFalse(listenerDefinition.add(null));
    assertFalse(listenerDefinition.add(""));
    assertFalse(listenerDefinition.add(" "));
    assertFalse(listenerDefinition.add("   "));
    assertTrue(listenerDefinition.add("null"));
    assertTrue(listenerDefinition.add("myProperty"));
    assertTrue(listenerDefinition.add("MyProperty"));
    assertTrue(listenerDefinition.add("MYPROPERTY"));
    assertFalse(listenerDefinition.add("myProperty"));
  }

  public void testCompareTo() throws Exception {
    final ListenerDefinition listenerDefinition0 = getListenerDefinition("COM.MYCOMPANY.MYPACKAGE.MYLISTENER");
    final ListenerDefinition listenderDefinition1 = getListenerDefinition("com.mycompany.mypackage.MyListener");
    final ListenerDefinition listenerDefinition2 = getListenerDefinition("org.codeprimate.event.MockListener");

    TestUtil.assertNegative(listenerDefinition0.compareTo(listenderDefinition1));
    TestUtil.assertZero(listenderDefinition1.compareTo(listenderDefinition1));
    TestUtil.assertPositive(listenerDefinition2.compareTo(listenderDefinition1));
  }

  public void testContainsProperty() throws Exception {
    final ListenerDefinition listenerDefinition = getListenerDefinition("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDefinition);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDefinition.getClassName());
    assertFalse(listenerDefinition.contains("myProperty"));
    assertTrue(listenerDefinition.add("myProperty"));
    assertTrue(listenerDefinition.contains("myProperty"));
    assertFalse(listenerDefinition.contains("MyProperty"));
    assertFalse(listenerDefinition.contains("MYPROPERTY"));
    assertFalse(listenerDefinition.add(null));
    assertFalse(listenerDefinition.contains(null));
    assertFalse(listenerDefinition.add(""));
    assertFalse(listenerDefinition.contains(""));
    assertFalse(listenerDefinition.add(" "));
    assertFalse(listenerDefinition.contains(" "));
  }

  public void testGetProperties() throws Exception {
    final ListenerDefinition listenerDefinition = getListenerDefinition("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDefinition);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDefinition.getClassName());
    assertFalse(listenerDefinition.add(null));
    assertFalse(listenerDefinition.add(""));
    assertFalse(listenerDefinition.add(" "));
    assertTrue(listenerDefinition.add("personId"));
    assertTrue(listenerDefinition.add("firstName"));
    assertTrue(listenerDefinition.add("lastName"));
    assertFalse(listenerDefinition.add("personId"));

    final Set<String> expectedProperties = new TreeSet<String>();
    expectedProperties.add("personId");
    expectedProperties.add("firstName");
    expectedProperties.add("lastName");

    final Set<String> actualProperties = listenerDefinition.getProperties();

    assertNotNull(actualProperties);
    assertEquals(expectedProperties, actualProperties);
  }

  public void testRemoveProperty() throws Exception {
    final ListenerDefinition listenerDefinition = getListenerDefinition("com.mycompany.mypackage.MyListener");

    assertNotNull(listenerDefinition);
    assertEquals("com.mycompany.mypackage.MyListener", listenerDefinition.getClassName());
    assertFalse(listenerDefinition.contains("myProperty"));
    assertFalse(listenerDefinition.remove("myProperty"));
    assertTrue(listenerDefinition.add("myProperty"));
    assertTrue(listenerDefinition.contains("myProperty"));
    assertFalse(listenerDefinition.remove("MYPROPERTY"));
    assertTrue(listenerDefinition.contains("myProperty"));
    assertFalse(listenerDefinition.remove("MyProperty"));
    assertTrue(listenerDefinition.contains("myProperty"));
    assertTrue(listenerDefinition.remove("myProperty"));
    assertFalse(listenerDefinition.contains("myProperty"));
    assertFalse(listenerDefinition.remove(null));
    assertFalse(listenerDefinition.remove(""));
    assertFalse(listenerDefinition.remove(" "));
  }

  protected static final class TestListenerDefinition extends AbstractListenerDefinition {

    public TestListenerDefinition(final String className) {
      super(className);
    }
  }

}
