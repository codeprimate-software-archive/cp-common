/*
 * BeanDefinitionsFactoryTest.java (c) 2 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.beans.definition.BeanDefinitionsFactory
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.event.LoggingPropertyChangeListener;
import com.cp.common.test.mock.MockBeanImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BeanDefinitionsFactoryTest extends TestCase {

  public BeanDefinitionsFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BeanDefinitionsFactoryTest.class);
    //suite.addTest(new BeanDefinitionsFactoryTest("testName"));
    return suite;
  }

  public void testGetBeanDeclaration() throws Exception {
    final BeanDeclaration beanDeclaration = BeanDefinitionsFactory.getBeanDeclaration("mockBean", "com.cp.common.test.mock.MockBeanImpl");

    assertNotNull(beanDeclaration);
    assertEquals(MockBeanImpl.class.getName(), beanDeclaration.getClassName());
    assertNull(beanDeclaration.getDestroyMethod());
    assertEquals("mockBean", beanDeclaration.getId());
    assertNull(beanDeclaration.getInitMethod());
    assertTrue(beanDeclaration.getInvocationArguments().isEmpty());
    assertTrue(beanDeclaration.getListeners().isEmpty());
    assertFalse(beanDeclaration.isLazyInit());
    assertNull(beanDeclaration.getName());
    assertTrue(beanDeclaration.getProperties().isEmpty());
    assertEquals(Scope.PROTOTYPE, beanDeclaration.getScope());
    assertEquals(MockBeanImpl.class, beanDeclaration.getType());
  }

  public void testGetBeanDefinition() throws Exception {
    final BeanDefinition beanDefinition = BeanDefinitionsFactory.getBeanDefinition("mockBean", "com.cp.common.test.mock.MockBeanImpl");

    assertNotNull(beanDefinition);
    assertEquals(MockBeanImpl.class.getName(), beanDefinition.getClassName());
    assertEquals("mockBean", beanDefinition.getId());
    assertTrue(beanDefinition.getListeners().isEmpty());
    assertNull(beanDefinition.getName());
    assertTrue(beanDefinition.getProperties().isEmpty());
  }

  public void testGetInvocationArgument() throws Exception {
    final InvocationArgument invocationArgument = BeanDefinitionsFactory.getInvocationArgument("java.lang.Object", "test");

    assertNotNull(invocationArgument);
    assertEquals(Object.class.getName(), invocationArgument.getStringType());
    assertEquals("test", invocationArgument.getStringValue());
  }

  public void testGetListenerDeclaration() throws Exception {
    final ListenerDeclaration listenerDeclaration = BeanDefinitionsFactory.getListenerDeclaration("com.cp.common.beans.event.LoggingPropertyChangeListener");

    assertNotNull(listenerDeclaration);
    assertEquals(LoggingPropertyChangeListener.class.getName(), listenerDeclaration.getClassName());
    assertTrue(listenerDeclaration.getInvocationArguments().isEmpty());
    assertTrue(listenerDeclaration.getProperties().isEmpty());
    assertEquals(Scope.PROTOTYPE, listenerDeclaration.getScope());
    assertEquals(LoggingPropertyChangeListener.class, listenerDeclaration.getType());
  }

  public void testGetListenerDefinition() throws Exception {
    final ListenerDefinition listenerDefinition = BeanDefinitionsFactory.getListenerDefinition("com.cp.common.beans.event.LoggingPropertyChangeListener");

    assertNotNull(listenerDefinition);
    assertNull(listenerDefinition.getBeanDefinition());
    assertEquals(LoggingPropertyChangeListener.class.getName(), listenerDefinition.getClassName());
    assertTrue(listenerDefinition.getProperties().isEmpty());
  }

  public void testGetParameterizable() throws Exception {
    final Parameterizable parameterizable = BeanDefinitionsFactory.getParameterizable();

    assertNotNull(parameterizable);
    assertTrue(parameterizable.getInvocationArguments().isEmpty());
  }

  public void testGetPropertyDeclaration() throws Exception {
    final PropertyDeclaration propertyDeclaration = BeanDefinitionsFactory.getPropertyDeclaration("value");

    assertNotNull(propertyDeclaration);
    assertNull(propertyDeclaration.getBeanDefinition());
    assertEquals(String.class.getName(), propertyDeclaration.getClassName());
    assertEquals("value", propertyDeclaration.getName());
    assertNull(propertyDeclaration.getFormatPattern());
    assertNull(propertyDeclaration.getRefId());
    assertNull(propertyDeclaration.getValue());
  }

  public void testGetPropertyDefinition() throws Exception {
    final PropertyDefinition propertyDefinition = BeanDefinitionsFactory.getPropertyDefinition("value", "java.lang.Object");

    assertNotNull(propertyDefinition);
    assertEquals(Object.class.getName(), propertyDefinition.getClassName());
    assertEquals("value", propertyDefinition.getName());
  }

  public void testGetReferenceObject() throws Exception {
    final ReferenceObject referenceObject = BeanDefinitionsFactory.getReferenceObject("myBean", MockBeanImpl.class);

    assertNotNull(referenceObject);
    assertEquals("myBean", referenceObject.getReferenceId());
    assertEquals(MockBeanImpl.class, referenceObject.getType());
  }

}
