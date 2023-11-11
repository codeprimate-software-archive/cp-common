/*
 * AbstractBeanBuilderVisitorTest.java (c) 13 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.1.2
 * @see com.cp.common.beans.factory.support.AbstractBeanBuilderVisitor
 */

package com.cp.common.beans.factory.support;

import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.factory.BeanFactory;
import com.cp.common.test.mock.MockVisitableObject;
import com.cp.common.test.util.TestUtil;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class AbstractBeanBuilderVisitorTest extends TestCase {

  private static BeanFactory mockBeanFactory;

  public AbstractBeanBuilderVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBeanBuilderVisitorTest.class);
    //suite.addTest(new AbstractBeanBuilderVisitorTest("testName"));
    return new AbstractBeanBuilderVisitorTestSetup(suite);
  }

  protected AbstractBeanBuilderVisitor getBeanBuilderVisitor(final BeanFactory beanFactory) {
    return new MockBeanBuilderVisitor(beanFactory);
  }

  protected AbstractBeanBuilderVisitor getBeanBuilderVisitor(final BeanFactory beanFactory, final Object[] args) {
    return new MockBeanBuilderVisitor(beanFactory, args);
  }

  protected AbstractBeanBuilderVisitor getBeanBuilderVisitor(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
    return new MockBeanBuilderVisitor(beanFactory, argTypes, args);
  }

  protected BeanFactory getBeanFactory() {
    return mockBeanFactory;
  }

  public void testInstantiateWithNullBeanFactory() throws Exception {
    try {
      getBeanBuilderVisitor(null);
      fail("Constructing an instance of the AbstractBeanBuilderVisitor class with a null BeanFactory object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean factory cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Constructing an instance of the AbstractBeanBuilderVisitor class with a null BeanFactory object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testInstantiateWithNonNullBeanFactory() throws Exception {
    AbstractBeanBuilderVisitor beanBuilderVisitor = null;

    assertNull(beanBuilderVisitor);

    try {
      beanBuilderVisitor = getBeanBuilderVisitor(getBeanFactory());
    }
    catch (Throwable t) {
      fail("Constructing an instance of the AbstractBeanBuilderVisitor class with a non-null BeanFactory threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(beanBuilderVisitor);
    assertNull(beanBuilderVisitor.getArguments());
    assertNull(beanBuilderVisitor.getArgumentTypes());
    assertNull(beanBuilderVisitor.getBean());
    assertSame(getBeanFactory(), beanBuilderVisitor.getBeanFactory());
  }

  public void testInstantiateWithNonNullBeanFactoryAndArguments() throws Exception {
    AbstractBeanBuilderVisitor beanBuilderVisitor = null;

    assertNull(beanBuilderVisitor);

    try {
      beanBuilderVisitor = getBeanBuilderVisitor(getBeanFactory(), new Object[] { true, 1, 'J', Math.PI, "test" });
    }
    catch (Throwable t) {
      fail("Constructing an instance of the AbstractBeanBuilderVisitor class with a non-null BeanFactory and constructor arguments threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(beanBuilderVisitor);
    TestUtil.assertEquals(new Object[] { true, 1, 'J', Math.PI, "test" }, beanBuilderVisitor.getArguments());
    assertNull(beanBuilderVisitor.getArgumentTypes());
    assertNull(beanBuilderVisitor.getBean());
    assertSame(getBeanFactory(), beanBuilderVisitor.getBeanFactory());
  }

  public void testInstantiateWithNonNullBeanFactoryArgumentsAndArgumentTypes() throws Exception {
    AbstractBeanBuilderVisitor beanBuilderVisitor = null;

    assertNull(beanBuilderVisitor);

    try {
      beanBuilderVisitor = getBeanBuilderVisitor(getBeanFactory(), new Class[] { String.class }, new Object[] { "test" });
    }
    catch (Throwable t) {
      fail("Constructing an instance of the AbstractBeanBuilderVisitor class with a non-null BeanFactory, constructor argument types and constructor arguments threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(beanBuilderVisitor);
    TestUtil.assertEquals(new Object[] { "test" }, beanBuilderVisitor.getArguments());
    TestUtil.assertEquals(new Class[] { String.class }, beanBuilderVisitor.getArgumentTypes());
    assertNull(beanBuilderVisitor.getBean());
    assertSame(getBeanFactory(), beanBuilderVisitor.getBeanFactory());
  }

  public void testVisit() throws Exception {
    final MockBeanBuilderVisitor visitor = (MockBeanBuilderVisitor) getBeanBuilderVisitor(getBeanFactory());

    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getBeanDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getListenerDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getPropertyDeclarationHandler()).isHandleCalled());

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    visitor.visit(mockBeanDeclaration);

    assertTrue(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getBeanDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getListenerDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getPropertyDeclarationHandler()).isHandleCalled());

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    visitor.visit(mockListenerDeclaration);

    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getBeanDeclarationHandler()).isHandleCalled());
    assertTrue(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getListenerDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getPropertyDeclarationHandler()).isHandleCalled());

    final PropertyDeclaration mockPropertyDeclaration = EasyMock.createMock(PropertyDeclaration.class);
    visitor.visit(mockPropertyDeclaration);

    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getBeanDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getListenerDeclarationHandler()).isHandleCalled());
    assertTrue(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getPropertyDeclarationHandler()).isHandleCalled());

    visitor.visit(new MockVisitableObject(0));

    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getBeanDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getListenerDeclarationHandler()).isHandleCalled());
    assertFalse(((MockBeanBuilderVisitor.MockDeclarationHandler) visitor.getPropertyDeclarationHandler()).isHandleCalled());
  }

  protected static final class MockBeanBuilderVisitor extends AbstractBeanBuilderVisitor {

    private final DeclarationHandler<BeanDeclaration> beanDeclarationHandler = new MockDeclarationHandler<BeanDeclaration>();
    private final DeclarationHandler<ListenerDeclaration> listenerDeclarationHandler = new MockDeclarationHandler<ListenerDeclaration>();
    private final DeclarationHandler<PropertyDeclaration> propertyDeclarationHandler = new MockDeclarationHandler<PropertyDeclaration>();

    public MockBeanBuilderVisitor(final BeanFactory beanFactory) {
      super(beanFactory);
    }

    public MockBeanBuilderVisitor(final BeanFactory beanFactory, final Object[] args) {
      super(beanFactory, args);
    }

    public MockBeanBuilderVisitor(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
      super(beanFactory, argTypes, args);
    }

    protected DeclarationHandler<BeanDeclaration> getBeanDeclarationHandler() {
      return beanDeclarationHandler;
    }

    protected DeclarationHandler<ListenerDeclaration> getListenerDeclarationHandler() {
      return listenerDeclarationHandler;
    }

    protected DeclarationHandler<PropertyDeclaration> getPropertyDeclarationHandler() {
      return propertyDeclarationHandler;
    }

    protected static abstract class AbstractDeclarationHandler<T> implements DeclarationHandler<T> {

      private boolean handleCalled = false;

      public boolean isHandleCalled() {
        final boolean handleCalled = this.handleCalled;
        setHandleCalled(false);
        return handleCalled;
      }

      public void setHandleCalled(final boolean handleCalled) {
        this.handleCalled = handleCalled;
      }
    }

    protected static final class MockDeclarationHandler<T> extends AbstractDeclarationHandler<T> {

      public void handle(final T declaration) {
        setHandleCalled(true);
      }
    }
  }

  protected static final class AbstractBeanBuilderVisitorTestSetup extends TestSetup {

    public AbstractBeanBuilderVisitorTestSetup(final Test test) {
      super(test);
    }

    @Override
    protected void setUp() throws Exception {
      super.setUp();
      mockBeanFactory = EasyMock.createMock(BeanFactory.class);
      EasyMock.replay(mockBeanFactory);
    }

    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
      EasyMock.verify(mockBeanFactory);
      mockBeanFactory = null;
    }
  }

}
