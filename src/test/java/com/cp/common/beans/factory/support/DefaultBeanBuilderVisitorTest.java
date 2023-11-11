/*
 * DefaultBeanBuilderVisitorTest.java (c) 14 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.12
 * @see com.cp.common.beans.factory.support.DefaultBeanBuilderVisitor
 */

package com.cp.common.beans.factory.support;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.BeanInstantiationException;
import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.event.LoggingPropertyChangeListener;
import com.cp.common.beans.event.ObjectImmutableVetoableChangeListener;
import com.cp.common.beans.factory.BeanFactory;
import com.cp.common.beans.factory.BeanNotFoundException;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.SystemException;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collections;
import java.util.EventListener;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class DefaultBeanBuilderVisitorTest extends TestCase {

  private static BeanFactory mockBeanFactory;
  private static DefaultBeanBuilderVisitor beanBuilderVisitor;

  public DefaultBeanBuilderVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultBeanBuilderVisitorTest.class);
    return new DefaultBeanBuilderVisitorTestSetup(suite);
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    beanBuilderVisitor.setBean(null);
  }

  protected DefaultBeanBuilderVisitor getBeanBuilderVisitor() {
    return beanBuilderVisitor;
  }

  protected BeanFactory getBeanFactory() {
    return mockBeanFactory;
  }

  public void testInstantiate() throws Exception {
    DefaultBeanBuilderVisitor beanBuilderVisitor = null;

    try {
      beanBuilderVisitor = new DefaultBeanBuilderVisitor(getBeanFactory());
    }
    catch (Exception e) {
      fail("Constructing an instance of the DefaultBeanBuilderVisitor class with a non-null BeanFactory should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(beanBuilderVisitor);
    assertNull(beanBuilderVisitor.getArguments());
    assertNull(beanBuilderVisitor.getArgumentTypes());
    assertNull(beanBuilderVisitor.getBean());
    assertSame(getBeanFactory(), beanBuilderVisitor.getBeanFactory());

    try {
      beanBuilderVisitor = new DefaultBeanBuilderVisitor(null);
      fail("Constructing an instance of the DefaultBeanBuilderVisitor class with a null BeanFactory should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean factory cannot be null!", e.getMessage());
      beanBuilderVisitor = null;
    }
    catch (Exception e) {
      fail("Constructing an instance of the DefaultBeanBuilderVisitor class with a null BeanFactory threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanBuilderVisitor);

    try {
      beanBuilderVisitor = new DefaultBeanBuilderVisitor(getBeanFactory(), new Class[] { Integer.class }, new Object[] { 1 });
    }
    catch (Exception e) {
      fail("Constructing an instance of the DefaultBeanBuilderVisitor class with a non-null BeanFactory and argument types and arguments should not have thrown an Exception ("
        + e.getMessage() + ")");
    }

    assertNotNull(beanBuilderVisitor);
    TestUtil.assertEquals(new Object[] { 1 }, beanBuilderVisitor.getArguments());
    TestUtil.assertEquals(new Class[] { Integer.class }, beanBuilderVisitor.getArgumentTypes());
    assertNull(beanBuilderVisitor.getBean());
    assertSame(getBeanFactory(), beanBuilderVisitor.getBeanFactory());
  }

  public void testGetBeanDeclarationHandler() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    assertNotNull(beanDeclarationHandler);
    assertSame(beanDeclarationHandler, getBeanBuilderVisitor().getBeanDeclarationHandler());
  }

  public void testGetListenerDeclarationHandler() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<ListenerDeclaration> listenerDeclarationHandler =
      getBeanBuilderVisitor().getListenerDeclarationHandler();

    assertNotNull(listenerDeclarationHandler);
    assertSame(listenerDeclarationHandler, getBeanBuilderVisitor().getListenerDeclarationHandler());
  }

  public void testGetPropertyDeclarationHandler() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<PropertyDeclaration> propertyDeclarationHandler =
      getBeanBuilderVisitor().getPropertyDeclarationHandler();

    assertNotNull(propertyDeclarationHandler);
    assertSame(propertyDeclarationHandler, getBeanBuilderVisitor().getPropertyDeclarationHandler());
  }

  public void testBeanDeclarationHandlerSimpleInstantiation() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(MockBeanImpl.class);
    EasyMock.replay(mockBeanDeclaration);

    beanDeclarationHandler.handle(mockBeanDeclaration);

    EasyMock.verify(mockBeanDeclaration);

    final Bean bean = getBeanBuilderVisitor().getBean();

    assertTrue(bean instanceof MockBeanImpl);
  }

  public void testBeanDeclarationHandlerUsingDeclaredConstructorArguments() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(new Class[] { Object.class });
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(new Object[] { "test" });
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(MockBeanImpl.class);
    EasyMock.replay(mockBeanDeclaration);

    beanDeclarationHandler.handle(mockBeanDeclaration);

    EasyMock.verify(mockBeanDeclaration);

    final Bean bean = getBeanBuilderVisitor().getBean();

    assertTrue(bean instanceof MockBeanImpl);

    final MockBean mockBean = (MockBean) bean;

    assertEquals("test", mockBean.getValue());
  }

  public void testBeanDeclarationHandlerUsingUserDefinedConstructorArguments() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = new DefaultBeanBuilderVisitor(getBeanFactory(),
      new Class[] { Object.class }, new Object[] { "myArgument" });

    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      beanBuilderVisitor.getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(MockBeanImpl.class);
    EasyMock.replay(mockBeanDeclaration);

    beanDeclarationHandler.handle(mockBeanDeclaration);

    EasyMock.verify(mockBeanDeclaration);

    final Bean bean = beanBuilderVisitor.getBean();

    assertTrue(bean instanceof MockBeanImpl);

    final MockBean mockBean = (MockBean) bean;

    assertEquals("myArgument", mockBean.getValue());
  }

  public void testBeanDeclarationHandlerUsingUserDefinedAndDeclaredConstructorArguments() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = new DefaultBeanBuilderVisitor(getBeanFactory(),
      new Class[] { Object.class }, new Object[] { "myArgument" });

    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      beanBuilderVisitor.getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(new Class[] { Object.class });
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(new Object[] { "test" });
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(MockBeanImpl.class);
    EasyMock.replay(mockBeanDeclaration);

    beanDeclarationHandler.handle(mockBeanDeclaration);

    EasyMock.verify(mockBeanDeclaration);

    final Bean bean = beanBuilderVisitor.getBean();

    assertTrue(bean instanceof MockBeanImpl);

    final MockBean mockBean = (MockBean) bean;

    assertEquals("myArgument", mockBean.getValue());
  }

  public void testBeanDeclarationHandlerWithNullBeanDeclaration() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    try {
      beanDeclarationHandler.handle(null);
      fail("Calling the handle method with a null BeanDeclaration object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean declaration cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the handle method with a null BeanDeclaration object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testBeanDeclarationHandlerWithBeanClassNotFound() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getType()).andThrow(new ClassNotFoundException("no.such.bean.Class cannot be found!"));
    EasyMock.expect(mockBeanDeclaration.getClassName()).andReturn("no.such.bean.Class");
    EasyMock.expectLastCall().times(2);
    EasyMock.expect(mockBeanDeclaration.getId()).andReturn("myBean");
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockBeanDeclaration);

    try {
      beanDeclarationHandler.handle(mockBeanDeclaration);
    }
    catch (BeanNotFoundException e) {
      assertEquals("The fully-qualified class (no.such.bean.Class) of bean having id (myBean) cannot be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the handle method on the BeanDeclarationHandler class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockBeanDeclaration);
  }

  public void testBeanDeclarationHandlerBeanInstantiationException() throws Exception {
    final DefaultBeanBuilderVisitor.DeclarationHandler<BeanDeclaration> beanDeclarationHandler =
      getBeanBuilderVisitor().getBeanDeclarationHandler();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentTypes()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getConstructorArgumentValues()).andReturn(null);
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(DefaultBeanBuilderVisitorTest.TestBean.class);
    EasyMock.expect(mockBeanDeclaration.getClassName()).andReturn(DefaultBeanBuilderVisitorTest.TestBean.class.getName());
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockBeanDeclaration);

    try {
      beanDeclarationHandler.handle(mockBeanDeclaration);
    }
    catch (BeanInstantiationException e) {
      assertEquals("Failed to create an instance of bean class ("
        + DefaultBeanBuilderVisitorTest.TestBean.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the handle method on the BeanDeclarationHandler class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockBeanDeclaration);
  }

  /*
  public void testListenerDeclarationHandler() throws Exception {
    fail("Not Implemented!");
  }
  */

  public void testListenerDeclarationHandlerCallGetInstanceMethod() throws Exception {
    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      getBeanBuilderVisitor().new DefaultListenerDeclarationHandler();

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getType()).andReturn(TestListener.class);
    EasyMock.replay(mockListenerDeclaration);

    EventListener listener = null;

    try {
      listener = listenerDeclarationHandler.callGetInstanceMethod(mockListenerDeclaration);
    }
    catch (Exception e) {
      fail("Calling the callGetInstanceMethod with the TestListener class should not have threw an Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDeclaration);

    assertTrue(listener instanceof TestListener);
    assertSame(TestListener.getInstance(), listener);
  }

  /*
  public void testListenerDeclarationHandlerGetInstance() throws Exception {
    fail("Not Implemented!");
  }
  */

  public void testListenerDeclarationHandlerRegisterPropertyChangeListenerNoProperties() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitor();

    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      beanBuilderVisitor.new DefaultListenerDeclarationHandler();

    final PropertyChangeListener mockPropertyChangeListener = EasyMock.createMock(PropertyChangeListener.class);

    final Bean mockBean = EasyMock.createMock(Bean.class);
    mockBean.addPropertyChangeListener(mockPropertyChangeListener);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBean);
    beanBuilderVisitor.setBean(mockBean);

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getProperties()).andReturn(Collections.<String>emptySet());
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDeclaration);

    listenerDeclarationHandler.registerPropertyChangeListener(mockListenerDeclaration, mockPropertyChangeListener);

    EasyMock.verify(mockBean);
    EasyMock.verify(mockListenerDeclaration);
  }

  public void testListenerDeclarationHandlerRegisterPropertyChangeListenerWithProperties() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitor();

    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      beanBuilderVisitor.new DefaultListenerDeclarationHandler();

    final PropertyChangeListener mockPropertyChangeListener = EasyMock.createMock(PropertyChangeListener.class);

    final Bean mockBean = EasyMock.createMock(Bean.class);
    mockBean.addPropertyChangeListener("propertyOne", mockPropertyChangeListener);
    EasyMock.expectLastCall().once();
    mockBean.addPropertyChangeListener("propertyTwo", mockPropertyChangeListener);
    EasyMock.expectLastCall().once();
    mockBean.addPropertyChangeListener("propertyThree", mockPropertyChangeListener);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBean);
    beanBuilderVisitor.setBean(mockBean);

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getProperties()).andReturn(CollectionUtil.<String>getSet("propertyOne", "propertyTwo", "propertyThree"));
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockListenerDeclaration);

    listenerDeclarationHandler.registerPropertyChangeListener(mockListenerDeclaration, mockPropertyChangeListener);

    EasyMock.verify(mockBean);
    EasyMock.verify(mockListenerDeclaration);
  }

  public void testListenerDeclarationHandlerRegisterVetoableChangeListenerNoProperties() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitor();

    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      beanBuilderVisitor.new DefaultListenerDeclarationHandler();

    final VetoableChangeListener mockVetoableChangeListener = EasyMock.createMock(VetoableChangeListener.class);

    final Bean mockBean = EasyMock.createMock(Bean.class);
    mockBean.addVetoableChangeListener(mockVetoableChangeListener);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBean);
    beanBuilderVisitor.setBean(mockBean);

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getProperties()).andReturn(Collections.<String>emptySet());
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDeclaration);

    listenerDeclarationHandler.registerVetoableChangeListener(mockListenerDeclaration, mockVetoableChangeListener);

    EasyMock.verify(mockBean);
    EasyMock.verify(mockListenerDeclaration);
  }

  public void testListenerDeclarationHandlerRegisterVetoableChangeListenerWithProperties() throws Exception {
    final DefaultBeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitor();

    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      beanBuilderVisitor.new DefaultListenerDeclarationHandler();

    final VetoableChangeListener mockVetoableChangeListener = EasyMock.createMock(VetoableChangeListener.class);

    final Bean mockBean = EasyMock.createMock(Bean.class);
    mockBean.addVetoableChangeListener("propertyOne", mockVetoableChangeListener);
    EasyMock.expectLastCall().once();
    mockBean.addVetoableChangeListener("propertyTwo", mockVetoableChangeListener);
    EasyMock.expectLastCall().once();
    mockBean.addVetoableChangeListener("propertyThree", mockVetoableChangeListener);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBean);
    beanBuilderVisitor.setBean(mockBean);

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getProperties()).andReturn(CollectionUtil.<String>getSet("propertyOne", "propertyTwo", "propertyThree"));
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockListenerDeclaration);

    listenerDeclarationHandler.registerVetoableChangeListener(mockListenerDeclaration, mockVetoableChangeListener);

    EasyMock.verify(mockBean);
    EasyMock.verify(mockListenerDeclaration);
  }

  public void testListenerDeclarationHandlerValueOfStaticInstanceField() throws Exception {
    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      getBeanBuilderVisitor().new DefaultListenerDeclarationHandler();

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getType()).andReturn(ObjectImmutableVetoableChangeListener.class);
    EasyMock.replay(mockListenerDeclaration);

    EventListener listener = null;

    try {
      listener = listenerDeclarationHandler.valueOfStaticInstanceField(mockListenerDeclaration);
    }
    catch (Exception e) {
      fail("Calling valueOfStaticInstanceField on the mockListenerDeclaration should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDeclaration);

    assertTrue(listener instanceof ObjectImmutableVetoableChangeListener);
    assertSame(ObjectImmutableVetoableChangeListener.INSTANCE, listener);
  }

  public void testListenerDeclarationHandlerValueOfStaticInstanceFieldThrowsIllegalAccessException() throws Exception {
    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      getBeanBuilderVisitor().new DefaultListenerDeclarationHandler();

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getType()).andReturn(TestListener.class);
    EasyMock.expect(mockListenerDeclaration.getClassName()).andReturn(TestListener.class.getName());
    EasyMock.replay(mockListenerDeclaration);

    EventListener listener = null;

    try {
      listener = listenerDeclarationHandler.valueOfStaticInstanceField(mockListenerDeclaration);
      fail("Calling the valueOfStaticInstanceField method with a listener having a protected static INSTANCE field should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to access static class member field (INSTANCE) on listener class (" + TestListener.class.getName()
              + "); please verify the field's access modifiers and system permissions!", e.getMessage());
      assertTrue(e.getCause() instanceof IllegalAccessException);
    }
    catch (Exception e) {
      fail("Calling the valueOfStaticInstanceField method with a listener having a protected static INSTANCE field threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDeclaration);

    assertNull(listener);
  }

  public void testListenerDeclarationHandlerValueOfStaticInstanceFieldThrowsSystemException() throws Exception {
    final DefaultBeanBuilderVisitor.DefaultListenerDeclarationHandler listenerDeclarationHandler =
      getBeanBuilderVisitor().new DefaultListenerDeclarationHandler();

    final ListenerDeclaration mockListenerDeclaration = EasyMock.createMock(ListenerDeclaration.class);
    EasyMock.expect(mockListenerDeclaration.getType()).andReturn(LoggingPropertyChangeListener.class);
    EasyMock.expect(mockListenerDeclaration.getClassName()).andReturn(LoggingPropertyChangeListener.class.getName());
    EasyMock.replay(mockListenerDeclaration);

    EventListener listener = null;

    try {
      listener = listenerDeclarationHandler.valueOfStaticInstanceField(mockListenerDeclaration);
      fail("Calling the valueOfStaticInstanceField method with the LoggingPropertyChangeListener should have thrown a SystemException for having no public static INSTANCE field!");
    }
    catch (SystemException e) {
      assertEquals("No static field INSTANCE exists on listener class (" + LoggingPropertyChangeListener.class.getName() + ")!",
        e.getMessage());
      assertNull(e.getCause());
    }
    catch (Exception e) {
      fail("Calling the valueOfStaticInstanceField method with the LoggingPropertyChangeListener threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDeclaration);

    assertNull(listener);
  }

  /*
  public void testPropertyDeclarationHandler() throws Exception {
    fail("Not Implemented!");
  }
  */

  /*
  public void testVisit() throws Exception {
    fail("Not Implemented!");
  }
  */

  protected static final class DefaultBeanBuilderVisitorTestSetup extends TestSetup {

    public DefaultBeanBuilderVisitorTestSetup(final Test test) {
      super(test);
    }

    @Override
    protected void setUp() throws Exception {
      super.setUp();
      mockBeanFactory = EasyMock.createMock(BeanFactory.class);
      beanBuilderVisitor = new DefaultBeanBuilderVisitor(mockBeanFactory);
    }

    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
      mockBeanFactory = null;
      beanBuilderVisitor = null;
    }
  }

  public static final class TestBean extends AbstractBean<Integer> {

    public TestBean() {
      throw new SystemException("Failed to create TestBean!");
    }
  }

  public static final class TestListener implements EventListener {

    private static final TestListener INSTANCE = new TestListener();

    public static TestListener getInstance() {
      return INSTANCE;
    }
  }

}
