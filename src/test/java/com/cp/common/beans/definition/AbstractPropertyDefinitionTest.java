/*
 * AbstractPropertyDefinitionTest.java (c) 4 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.7
 * @see com.cp.common.beans.definition.AbstractPropertyDefinition
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Visitable;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractPropertyDefinitionTest extends TestCase {

  public AbstractPropertyDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractPropertyDefinitionTest.class);
    return suite;
  }

  protected PropertyDefinition getPropertyDefinition(final String propertyName, final String className) {
    return new TestPropertyDefinition(propertyName, className);
  }

  public void testInstantiate() throws Exception {
    PropertyDefinition propertyDefinition = null;

    try {
      propertyDefinition = getPropertyDefinition("myProperty", "java.lang.Object");
    }
    catch (Exception e) {
      fail("Instantiating an instance of PropertyDefinition with a property name and class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyDefinition);
    assertEquals("myProperty", propertyDefinition.getName());
    assertEquals("java.lang.Object", propertyDefinition.getClassName());
  }

  public void testInstantiateWithEmptyClassName() throws Exception {
    PropertyDefinition propertyDefinition = null;

    try {
      propertyDefinition = getPropertyDefinition("myProperty", " ");
      fail("Instantiating an instance of PropertyDefinition with an empty class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the property type cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of PropertyDefinition with an empty class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDefinition);
  }

  public void testInstantiateWithEmptyPropertyName() throws Exception {
    PropertyDefinition propertyDefinition = null;

    try {
      propertyDefinition = getPropertyDefinition(" ", "java.lang.Object");
      fail("Instantiating an instance of PropertyDefinition with an empty property name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of PropertyDefinition with an empty property name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDefinition);
  }

  public void testInstantiateWithNullClassName() throws Exception {
    PropertyDefinition propertyDefinition = null;

    try {
      propertyDefinition = getPropertyDefinition("myProperty", null);
      fail("Instantiating an instance of PropertyDefinition with a null class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the property type cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of PropertyDefinition with a null class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDefinition);
  }

  public void testInstantiateWithNullPropertyName() throws Exception {
    PropertyDefinition propertyDefinition = null;

    try {
      propertyDefinition = getPropertyDefinition(null, "java.lang.Object");
      fail("Instantiating an instance of PropertyDefinition with a null property name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of PropertyDefinition with a null property name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDefinition);
  }

  public void testAccept() throws Exception {
    final PropertyDefinition propertyDefinition = getPropertyDefinition("myProperty", "java.lang.Object");

    assertNotNull(propertyDefinition);
    assertEquals("myProperty", propertyDefinition.getName());
    assertEquals("java.lang.Object", propertyDefinition.getClassName());

    final TestVisitor visitor = new TestVisitor();
    propertyDefinition.accept(visitor);

    assertSame(propertyDefinition, visitor.getVisitedObject());
  }

  public void testCompareTo() throws Exception {
    final PropertyDefinition propertyDefinition0 = getPropertyDefinition("personId", "java.lang.Integer");
    final PropertyDefinition propertyDefinition1 = getPropertyDefinition("firstName", "java.lang.String");
    final PropertyDefinition propertyDefinition2 = getPropertyDefinition("dob", "java.util.Calendar");

    TestUtil.assertNegative(propertyDefinition2.compareTo(propertyDefinition1));
    TestUtil.assertZero(propertyDefinition1.compareTo(propertyDefinition1));
    TestUtil.assertPositive(propertyDefinition0.compareTo(propertyDefinition1));
  }

  protected static final class TestPropertyDefinition extends AbstractPropertyDefinition {

    public TestPropertyDefinition(final String propertyName, final String className) {
      super(propertyName, className);
    }
  }

  protected static final class TestVisitor implements Visitor {

    private Visitable visitedObject;

    public Visitable getVisitedObject() {
      return visitedObject;
    }

    public void setVisitedObject(final Visitable visitedObject) {
      this.visitedObject = visitedObject;
    }

    public void visit(final Visitable obj) {
      setVisitedObject(obj);
    }
  }

}
