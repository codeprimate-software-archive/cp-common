/*
 * SpringBeanFactoryTest.java (c) 12 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.12
 * @see com.cp.common.beans.factory.SpringBeanFactory
 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests
 */

package com.cp.common.beans.factory;

import java.util.Arrays;

import com.cp.common.beans.definition.Scope;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SpringBeanFactoryTest extends AbstractDependencyInjectionSpringContextTests {

  private BeanFactory beanFactory;

  public SpringBeanFactoryTest(String testName) {
    super(testName);
    //setAutowireMode(AUTOWIRE_BY_NAME);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SpringBeanFactoryTest.class);
    return suite;
  }

  protected BeanFactory getBeanFactory() {
    Assert.state(ObjectUtil.isNotNull(beanFactory), "The bean factory was not properly configured in the Spring application context!");
    return beanFactory;
  }

  public void setBeanFactory(final SpringBeanFactory beanFactory) {
    Assert.notNull(beanFactory, "The SpringBeanFactory cannot be null!");
    this.beanFactory = beanFactory;
  }

  protected String[] getConfigLocations() {
    return new String[] {
      "classpath:/etc/xml/mock-applicationContext.xml"
    };
  }

  public void testGetAliases() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);

    final String[] expectedAliases = { "mockBean", "testBean", "theBean" };
    final String[] actualAliases = springBeanFactory.getAliases("myBean");

    assertNotNull(actualAliases);
    assertEquals(expectedAliases.length, actualAliases.length);
    assertTrue(Arrays.asList(actualAliases).containsAll(Arrays.asList(expectedAliases)));
  }

  public void testGetBean() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);

    final MockBean mockBean = springBeanFactory.getBean("myBean");

    assertNotNull(mockBean);
    assertNull(mockBean.getId());
    assertEquals("test", mockBean.getValue());
  }

  public void testGetBeanWithArguments() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();
    MockBean mockBean = null;

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);
    assertNull(mockBean);

    try {
      mockBean = springBeanFactory.getBean("myBean", new Object[] { 1 });
      fail("Calling getBean with a bean ID and arguments should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Not Implemented!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBean with a bean ID and arguments threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(mockBean);
  }

  public void testGetBeanWithArgumentTypesAndArguments() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();
    final MockBean expectedBean = new MockBeanImpl("test");
    MockBean mockBean = null;

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);
    assertNull(mockBean);

    try {
      mockBean = springBeanFactory.getBean("myBean", new Class[] { MockBean.class }, new Object[] { expectedBean });
      fail("Calling getBean with a bean ID, argument types, and arguments should have thrown an UnsupportedOperationException!");
    }
    catch (UnsupportedOperationException e) {
      assertEquals("Not Implemented!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBean with a bean ID, argument types, and arguments threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(mockBean);
  }

  public void testGetScope() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);
    assertEquals(Scope.PROTOTYPE, springBeanFactory.getScope(null));
    assertEquals(Scope.PROTOTYPE, springBeanFactory.getScope(" "));
    assertEquals(Scope.PROTOTYPE, springBeanFactory.getScope("myBean"));
    assertEquals(Scope.PROTOTYPE, springBeanFactory.getScope("testBean"));
    assertEquals(Scope.PROTOTYPE, springBeanFactory.getScope("nonExistingBean"));
  }

  public void testGetType() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);
    assertEquals(MockBeanImpl.class, springBeanFactory.getType("myBean"));
    assertEquals(MockBeanImpl.class, springBeanFactory.getType("testBean"));
  }

  public void testContainsBean() throws Exception {
    final BeanFactory springBeanFactory = getBeanFactory();

    assertNotNull(springBeanFactory);
    assertTrue(springBeanFactory instanceof SpringBeanFactory);
    assertTrue(springBeanFactory.containsBean("myBean"));
    assertTrue(springBeanFactory.containsBean("mockBean"));
    assertTrue(springBeanFactory.containsBean("testBean"));
    assertTrue(springBeanFactory.containsBean("theBean"));
    assertFalse(springBeanFactory.containsBean("nonExistingBean"));
  }
}
