/*
 * AbstractPropertyDeclarationTest.java (c) 7 8 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.7
 * @see com.cp.common.beans.definition.AbstractPropertyDeclaration
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractPropertyDeclarationTest extends TestCase {

  public AbstractPropertyDeclarationTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractPropertyDeclarationTest.class);
    return suite;
  }

  protected PropertyDeclaration getPropertyDeclaration(final String propertyName, final String className) {
    return new DefaultPropertyDeclaration(propertyName, className);
  }

  public void testGettersAndSetters() throws Exception {
    final PropertyDeclaration propertyDeclaration = getPropertyDeclaration("myProperty", "java.lang.Object");

    assertNotNull(propertyDeclaration);
    assertEquals("myProperty", propertyDeclaration.getName());
    assertEquals("java.lang.Object", propertyDeclaration.getClassName());
    assertNull(propertyDeclaration.getFormatPattern());
    assertNull(propertyDeclaration.getRefId());
    assertNull(propertyDeclaration.getValue());

    propertyDeclaration.setFormatPattern("MM/dd/yyyy hh:mm:ss a");
    propertyDeclaration.setRefId("myBean");
    propertyDeclaration.setValue("test");

    assertEquals("myProperty", propertyDeclaration.getName());
    assertEquals("java.lang.Object", propertyDeclaration.getClassName());
    assertEquals("MM/dd/yyyy hh:mm:ss a", propertyDeclaration.getFormatPattern());
    assertEquals("myBean", propertyDeclaration.getRefId());
    assertEquals("test", propertyDeclaration.getValue());
  }

}
