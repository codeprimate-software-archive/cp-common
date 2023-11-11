/*
 * PropertyBeanFactoryTest.java (c) 10 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.12
 * @see com.cp.common.beans.factory.PropertyBeanFactory
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.Scope;
import com.cp.common.context.config.Config;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.SystemException;
import java.util.MissingResourceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class PropertyBeanFactoryTest extends TestCase {

  public PropertyBeanFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PropertyBeanFactoryTest.class);
    return suite;
  }

  public void testInstantiate() throws Exception {
    BeanFactory propertyBeanFactory = null;

    assertNull(propertyBeanFactory);

    try {
      propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    }
    catch (Exception e) {
      fail("Creating an instance of the PropertyBeanFactory class with a non-null Config object threw an unexpected Exception ("
        + e.getMessage() + ")");
    }

    assertNotNull(propertyBeanFactory);
  }

  public void testInstantiateWithNullConfig() throws Exception {
    BeanFactory propertyBeanFactory = null;

    assertNull(propertyBeanFactory);

    try {
      propertyBeanFactory = new PropertyBeanFactory(null);
      fail("Creating an instance of the PropertyBeanFactory with a null Config object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The configuration object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Creating an instance of the PropertyBeanFactory class with a null Config object threw an unexpected Exception ("
        + e.getMessage() + ")");
    }

    assertNull(propertyBeanFactory);
  }

  public void testGetAliases() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(true);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    String[] actualAliases = null;

    assertNull(actualAliases);

    try {
      actualAliases = propertyBeanFactory.getAliases("testBean");
    }
    catch (Exception e) {
      fail("Calling getAliases on bean (testBean) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    TestUtil.assertEquals(new String[0], actualAliases);
    EasyMock.verify(mockConfig);
  }

  public void testGetAliasesWithBeanNotFound() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(false);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);

    String[] aliases = null;

    assertNull(aliases);

    try {
      aliases = propertyBeanFactory.getAliases("testBean");
      fail("Calling getAliases with a bean (testBean) not found should have thrown a BeanNotFoundException!");
    }
    catch (BeanNotFoundException e) {
      assertEquals("The bean identified by id (testBean) cannot be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getAliases with a bean (testBean) not found threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(aliases);
    EasyMock.verify(mockConfig);
  }

  public void testGetAliasesWithEmptyBeanId() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    String[] aliases = null;

    assertNull(aliases);

    try {
      aliases = propertyBeanFactory.getAliases(" ");
      fail("Calling getAliases with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getAliases with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(aliases);
  }

  public void testGetAliasesWithNullBeanId() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    String[] aliases = null;

    assertNull(aliases);

    try {
      aliases = propertyBeanFactory.getAliases(null);
      fail("Calling getAliases with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getAliases with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(aliases);
  }

  public void testGetBean() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue("mockBean")).andReturn(MockBeanImpl.class.getName());
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    MockBean mockBean = null;

    assertNull(mockBean);

    try {
      mockBean = propertyBeanFactory.getBean("mockBean");
    }
    catch (Exception e) {
      fail("Calling getBean for bean (mockBean) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(mockBean);
    assertNull(mockBean.getId());
    assertNull(mockBean.getValue());
    EasyMock.verify(mockConfig);
  }

  public void testGetBeanWithArguments() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue("mockBean")).andReturn(MockBeanImpl.class.getName());
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    MockBean mockBean = null;

    assertNull(mockBean);

    try {
      mockBean = propertyBeanFactory.getBean("mockBean", new Object[] { 1 });
    }
    catch (Exception e) {
      fail("Calling getBean for bean (mockBean) with an integer ID argument threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(mockBean);
    assertEquals(new Integer(1), mockBean.getId());
    assertNull(mockBean.getValue());
    EasyMock.verify(mockConfig);
  }

  public void testGetBeanWithArgumentTypesAndArguments() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue("mockBean")).andReturn(MockBeanImpl.class.getName());
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);

    MockBean expectedBean = new MockBeanImpl("test");
    MockBean mockBean = null;

    assertNull(mockBean);

    try {
      mockBean = propertyBeanFactory.getBean("mockBean", new Class[] { MockBean.class }, new Object[] { expectedBean });
    }
    catch (Exception e) {
      fail("Calling getBean for bean (mockBean) with a MockBean argument threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(mockBean);
    assertNull(mockBean.getId());
    assertEquals("test", mockBean.getValue());
    EasyMock.verify(mockConfig);
  }

  public void testGetBeanWithEmptyBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Bean actualBean = null;

    assertNull(actualBean);

    try {
      actualBean = propertyBeanFactory.getBean(" ");
      fail("Calling getBean with an empty bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBean with an empty bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBean);
  }

  public void testGetBeanWithNullBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Bean actualBean = null;

    assertNull(actualBean);

    try {
      actualBean = propertyBeanFactory.getBean(null, new Object[] { 1 });
      fail("Calling getBean with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBean with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBean);
  }

  public void testGetBeanWithMissingResource() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue("mockBean")).andThrow(
      new MissingResourceException("Resource Not Found!", "com.mycompany.mypackage.MyClass", "mockBean"));
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    MockBean mockBean = null;

    assertNull(mockBean);

    try {
      mockBean = propertyBeanFactory.getBean("mockBean");
      fail("Calling getBean on bean (mockBean) not declared in the configuration should have thrown a BeanNotFoundException!");
    }
    catch (BeanNotFoundException e) {
      assertEquals("The bean identified by id (mockBean) cannot be found!", e.getMessage());
      assertTrue(e.getCause() instanceof MissingResourceException);
      assertEquals("Resource Not Found!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling getBean on bean (mockBean) not declared in the configuration threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(mockBean);
    EasyMock.verify(mockConfig);
  }

  public void testGetScope() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(true);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Scope actualScope = null;

    assertNull(actualScope);

    try {
      actualScope = propertyBeanFactory.getScope("testBean");
    }
    catch (Exception e) {
      fail("Calling getScope for bean (testBean) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(Scope.PROTOTYPE, actualScope);
    EasyMock.verify(mockConfig);
  }

  public void testGetScopeWithBeanNotFound() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(false);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Scope actualScope = null;

    assertNull(actualScope);

    try {
      actualScope = propertyBeanFactory.getScope("testBean");
      fail("Calling getScope for bean (testBean) not found in configuration should have thrown a BeanNotFoundException!");
    }
    catch (BeanNotFoundException e) {
      assertEquals("The bean identified by id (testBean) cannot be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getScope for bean (testBean) not found in configuration threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualScope);
    EasyMock.verify(mockConfig);
  }

  public void testGetScopeWithEmptyBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Scope actualScope = null;

    assertNull(actualScope);

    try {
      actualScope = propertyBeanFactory.getScope(" ");
      fail("Calling getScope with an empty bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getScope with an empty bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualScope);
  }

  public void testGetScopeWithNullBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Scope actualScope = null;

    assertNull(actualScope);

    try {
      actualScope = propertyBeanFactory.getScope(null);
      fail("Calling getScope with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getScope with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualScope);
  }

  public void testGetType() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("mockBean")).andReturn(true);
    EasyMock.expect(mockConfig.getStringPropertyValue("mockBean")).andReturn(MockBeanImpl.class.getName());
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType("mockBean");
    }
    catch (Exception e) {
      fail("Calling getType for bean (mockBean) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(MockBeanImpl.class, actualType);
    EasyMock.verify(mockConfig);
  }

  public void testGetTypeWithBeanNotFound() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("mockBean")).andReturn(false);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType("mockBean");
      fail("Calling getType for bean (mockBean) not found in configuration should have thrown a BeanNotFoundException!");
    }
    catch (BeanNotFoundException e) {
      assertEquals("The bean identified by id (mockBean) cannot be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType for bean (mockBean) not found in configuration threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualType);
    EasyMock.verify(mockConfig);
  }

  public void testGetTypeWithEmptyBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType(" ");
      fail("Calling getType with an empty bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType with an empty bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualType);
  }

  public void testGetTypeWithNullBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType(null);
      fail("Calling getType with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualType);
  }

  public void testGetTypeWithEmptyBeanClassName() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(true);
    EasyMock.expect(mockConfig.getStringPropertyValue("testBean")).andReturn(" ");
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType("testBean");
      fail("Calling getType for bean (testBean) with an empty property value for class name should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("The fully qualified bean class name was not defined for bean identified by id (testBean)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType for bean (testBean) with an empty property value for class name should threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualType);
    EasyMock.verify(mockConfig);
  }

  public void testGetTypeWithBeanClassNotFound() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(true);
    EasyMock.expect(mockConfig.getStringPropertyValue("testBean")).andReturn("com.mycompany.mypackage.NonExistingBeanClass");
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);
    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = propertyBeanFactory.getType("testBean");
      fail("Calling getType for bean (testBean) having a class type not found in the CLASSPATH should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("The fully qualified bean class name (com.mycompany.mypackage.NonExistingBeanClass) for bean identified by id (testBean) could not be found in the CLASSPATH!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType for bean (testBean) having a class type not found in the CLASSPATH threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualType);
    EasyMock.verify(mockConfig);
  }

  public void testContainsBean() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.contains("testBean")).andReturn(true);
    EasyMock.expect(mockConfig.contains("unfoundBean")).andReturn(false);
    EasyMock.replay(mockConfig);

    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(mockConfig);

    assertTrue(propertyBeanFactory.containsBean("testBean"));
    assertFalse(propertyBeanFactory.containsBean("unfoundBean"));

    EasyMock.verify(mockConfig);
  }

  public void testContainsBeanWithEmptyBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));

    try {
      propertyBeanFactory.containsBean(" ");
      fail("Calling containsBean with an empty bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling containsBean with an empty bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testContainsBeanWithNullBeanId() throws Exception {
    final BeanFactory propertyBeanFactory = new PropertyBeanFactory(EasyMock.createMock(Config.class));

    try {
      propertyBeanFactory.containsBean(null);
      fail("Calling containsBean with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling containsBean with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

}
