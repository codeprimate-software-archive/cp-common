/*
 * BeanDefinitionsUtilTest.java (c) 7 8 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.24
 * @see com.cp.common.beans.definition.support.BeanDefinitionsUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.beans.definition.DefaultBeanDefinition;
import com.cp.common.beans.definition.DefaultInvocationArgument;
import com.cp.common.beans.definition.DefaultListenerDefinition;
import com.cp.common.beans.definition.DefaultParameterHandler;
import com.cp.common.beans.definition.DefaultPropertyDefinition;
import com.cp.common.beans.definition.DefaultReferenceObject;
import com.cp.common.beans.definition.InvocationArgument;
import com.cp.common.beans.definition.ListenerDefinition;
import com.cp.common.beans.definition.Parameterizable;
import com.cp.common.beans.definition.PropertyDefinition;
import com.cp.common.beans.definition.ReferenceObject;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BeanDefinitionsUtilTest extends TestCase {

  public BeanDefinitionsUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BeanDefinitionsUtilTest.class);
    return suite;
  }

  public void testToStringOnBeanDefinition() throws Exception {
    final BeanDefinition beanDefinition = new DefaultBeanDefinition("myBean", "com.mycompany.mypackage.MyBean");

    final StringBuffer expectedString = new StringBuffer(beanDefinition.getClass().getName());
    expectedString.append("(").append(beanDefinition.getId());
    expectedString.append(", ").append(beanDefinition.getClassName());
    expectedString.append(")");

    assertNull(BeanDefinitionsUtil.toString((BeanDefinition) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(beanDefinition));
  }

  public void testToStringOnInvocationArgument() throws Exception {
    final InvocationArgument invocationArgument = new DefaultInvocationArgument("java.lang.Object", "test");

    assertNull(invocationArgument.getRefId());

    final StringBuffer expectedString = new StringBuffer(invocationArgument.getClass().getName());
    expectedString.append("(java.lang.Object, test)");

    assertNull(BeanDefinitionsUtil.toString((InvocationArgument) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(invocationArgument));

    invocationArgument.setRefId("myBean");
    expectedString.delete(0, expectedString.length());
    expectedString.append(invocationArgument.getClass().getName());
    expectedString.append("(refId=myBean)");

    assertEquals("myBean", invocationArgument.getRefId());
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(invocationArgument));
  }

  public void testToStringOnListenerDefinition() throws Exception {
    final ListenerDefinition listenerDefinition = new DefaultListenerDefinition("com.mycompany.mypackage.MyListener");

    final StringBuffer expectedString = new StringBuffer(listenerDefinition.getClass().getName());
    expectedString.append("(com.mycompany.mypackage.MyListener)");

    assertNull(BeanDefinitionsUtil.toString((ListenerDefinition) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(listenerDefinition));
  }

  public void testToStringParameterizable() throws Exception {
    final InvocationArgument nullInvocationArgument = new DefaultInvocationArgument("java.lang.Object", "null");

    final InvocationArgument refInvocationArgument = new DefaultInvocationArgument("java.lang.Object", "test");
    refInvocationArgument.setRefId("myBean");

    final Parameterizable parameterHandler = new DefaultParameterHandler();
    parameterHandler.add(nullInvocationArgument);
    parameterHandler.add(refInvocationArgument);

    final StringBuffer expectedString = new StringBuffer(parameterHandler.getClass().getName());
    expectedString.append("[").append(nullInvocationArgument.getClass().getName());
    expectedString.append("(").append(nullInvocationArgument.getStringType());
    expectedString.append(", ").append(nullInvocationArgument.getStringValue());
    expectedString.append("), ").append(refInvocationArgument.getClass().getName());
    expectedString.append("(refId=").append(refInvocationArgument.getRefId()).append(")]");

    assertNull(BeanDefinitionsUtil.toString((Parameterizable) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(parameterHandler));
  }

  public void testToStringPropertyDefinition() throws Exception {
    final PropertyDefinition propertyDefinition = new DefaultPropertyDefinition("myProperty", "java.lang.Object");

    final StringBuffer expectedString = new StringBuffer(propertyDefinition.getClass().getName());
    expectedString.append("(").append(propertyDefinition.getName());
    expectedString.append(", ").append(propertyDefinition.getClassName());
    expectedString.append(")");

    assertNull(BeanDefinitionsUtil.toString((PropertyDefinition) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(propertyDefinition));
  }

  public void testToStringReferenceObject() throws Exception {
    final ReferenceObject referenceObject = new DefaultReferenceObject("myBean", Object.class);

    final StringBuffer expectedString = new StringBuffer(referenceObject.getClass().getName());
    expectedString.append("(").append(referenceObject.getReferenceId()).append(")");

    assertNull(BeanDefinitionsUtil.toString((ReferenceObject) null));
    assertEquals(expectedString.toString(), BeanDefinitionsUtil.toString(referenceObject));
  }

}
