/*
 * AbstractBeanDefinitionTest.java (c) 3 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.7.13
 * @see com.cp.common.beans.definition.AbstractBeanDefinition
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.support.StackedObjectGraphVisitor;
import com.cp.common.test.util.TestUtil;
import java.util.Stack;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;
import org.easymock.IAnswer;

public class AbstractBeanDefinitionTest extends TestCase {

  public AbstractBeanDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBeanDefinitionTest.class);
    return suite;
  }

  protected <L extends ListenerDefinition, P extends PropertyDefinition> BeanDefinition<L, P> getBeanDefinition(final String id, final String className) {
    return new TestBeanDefinition<L, P>(id, className);
  }

  public void testInstantiation() throws Exception {
    BeanDefinition beanDefinition = null;

    try {
      beanDefinition = getBeanDefinition("myBean", "com.mycompany.mypackage.MyBean");
    }
    catch (Exception e) {
      fail("Instantiating an instance of BeanDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(beanDefinition);
    assertEquals("myBean", beanDefinition.getId());
    assertEquals("com.mycompany.mypackage.MyBean", beanDefinition.getClassName());
  }

  public void testInstantiationWithEmptyClassName() throws Exception {
    BeanDefinition beanDefinition = null;

    try {
      beanDefinition = getBeanDefinition("myBean", "");
      fail("Instantiating an instance of BeanDefinition with an empty class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the bean definition cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BeanDefinition with an empty class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanDefinition);
  }

  public void testInstantiationWithEmptyId() throws Exception {
    BeanDefinition beanDefinition = null;

    try {
      beanDefinition = getBeanDefinition("", "com.mycompany.mypackage.MyBean");
      fail("Instantiating an instance of BeanDefinition with an empty ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The id of the bean definition cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BeanDefinition with an empty ID threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanDefinition);
  }

  public void testInstantiationWithNullClassName() throws Exception {
    BeanDefinition beanDefinition = null;

    try {
      beanDefinition = getBeanDefinition("myBean", null);
      fail("Instantiating an instance of BeanDefinition with a null class name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The fully-qualified class name of the bean definition cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BeanDefinition with a null class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanDefinition);
  }

  public void testInstantiationWithNullId() throws Exception {
    BeanDefinition beanDefinition = null;

    try {
      beanDefinition = getBeanDefinition(null, "com.mycompany.mypackage.MyBean");
      fail("Instantiating an instance of BeanDefinition with a null ID should have thrown an IllegalArgumentExcepiton!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The id of the bean definition cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BeanDefinition with a null ID threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanDefinition);
  }

  public void testSetNameSingular() throws Exception {
    final BeanDefinition beanDefinition = getBeanDefinition("myBean", "com.mycompany.mypackage.MyBean");

    assertNotNull(beanDefinition);
    assertEquals("myBean", beanDefinition.getId());
    assertEquals("com.mycompany.mypackage.MyBean", beanDefinition.getClassName());
    assertNull(beanDefinition.getName());

    beanDefinition.setName("myBeannie");

    assertEquals("myBeannie", beanDefinition.getName());
    TestUtil.assertEquals(new String[] { "myBeannie" }, beanDefinition.getNames());
  }

  public void testSetNamePlural() throws Exception {
    final BeanDefinition beanDefinition = getBeanDefinition("myBean", "com.mycompany.mypackage.MyBean");

    assertNotNull(beanDefinition);
    assertEquals("myBean", beanDefinition.getId());
    assertEquals("com.mycompany.mypackage.MyBean", beanDefinition.getClassName());
    assertNull(beanDefinition.getName());

    beanDefinition.setName("aBean, myBeannie, theBean");

    assertEquals("aBean, myBeannie, theBean", beanDefinition.getName());
    TestUtil.assertEquals(new String[] { "aBean", "myBeannie", "theBean" }, beanDefinition.getNames());

    beanDefinition.setName("aBean, bBean, cBean");

    assertEquals("aBean, bBean, cBean", beanDefinition.getName());
    TestUtil.assertEquals(new String[] { "aBean", "bBean", "cBean" }, beanDefinition.getNames());

    beanDefinition.setName("aBean, null, nill");

    assertEquals("aBean, null, nill", beanDefinition.getName());
    TestUtil.assertEquals(new String[] { "aBean", "null", "nill" }, beanDefinition.getNames());

    beanDefinition.setName(null);

    assertNull(beanDefinition.getName());
    TestUtil.assertEquals(new String[0], beanDefinition.getNames());
  }

  public void testSetNameWithEmptyName() throws Exception {
    final BeanDefinition beanDefinition = getBeanDefinition("myBean", "com.mycompany.mypackage.MyBean");

    assertNotNull(beanDefinition);
    assertEquals("myBean", beanDefinition.getId());
    assertEquals("com.mycompany.mypackage.MyBean", beanDefinition.getClassName());
    assertNull(beanDefinition.getName());

    try {
      beanDefinition.setName("aBean, , theBean");
      fail("Calling setName with an empty name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setName with an empty name threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(beanDefinition.getName());
    TestUtil.assertEquals(new String[0], beanDefinition.getNames());
  }

  public void testAccept() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final StackedObjectGraphVisitor visitor = new StackedObjectGraphVisitor();

    final PropertyDefinition mockPropertyDefinition0 = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition0.getName()).andReturn("property0");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition0.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    mockPropertyDefinition0.accept(visitor);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        visitor.visit(mockPropertyDefinition0);
        return null;
      }
    });
    EasyMock.replay(mockPropertyDefinition0);

    assertTrue(beanDefinition.add(mockPropertyDefinition0));

    final PropertyDefinition mockPropertyDefinition1 = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition1.getName()).andReturn("property1");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition1.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    mockPropertyDefinition1.accept(visitor);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        visitor.visit(mockPropertyDefinition1);
        return null;
      }
    });
    EasyMock.replay(mockPropertyDefinition1);

    assertTrue(beanDefinition.add(mockPropertyDefinition1));

    final ListenerDefinition mockListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinition.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().once();
    mockListenerDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    mockListenerDefinition.accept(visitor);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        visitor.visit(mockListenerDefinition);
        return null;
      }
    });
    EasyMock.replay(mockListenerDefinition);

    assertTrue(beanDefinition.add(mockListenerDefinition));

    beanDefinition.accept(visitor);

    EasyMock.verify(mockPropertyDefinition0);
    EasyMock.verify(mockPropertyDefinition1);
    EasyMock.verify(mockListenerDefinition);

    final Stack<Object> expectedObjectStack = new Stack<Object>();
    expectedObjectStack.push(beanDefinition);
    expectedObjectStack.push(mockPropertyDefinition0);
    expectedObjectStack.push(mockPropertyDefinition1);
    expectedObjectStack.push(mockListenerDefinition);

    final Stack<Object> actualObjectStack = visitor.getObjectStack();

    assertNotNull(actualObjectStack);
    assertFalse(actualObjectStack.isEmpty());
    assertEquals(expectedObjectStack.size(), actualObjectStack.size());

    while (!expectedObjectStack.isEmpty()) {
      assertSame(expectedObjectStack.pop(), actualObjectStack.pop());
    }
  }

  public void testAddListener() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final ListenerDefinition mockListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinition.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().once();
    mockListenerDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDefinition);

    try {
      beanDefinition.add(mockListenerDefinition);
    }
    catch (Exception e) {
      fail("Calling add with the mock ListenerDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDefinition);

    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertSame(mockListenerDefinition, beanDefinition.getListeners().iterator().next());
  }

  public void testAddExistingListener() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final ListenerDefinition mockListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinition.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().once();
    mockListenerDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDefinition);

    try {
      beanDefinition.add(mockListenerDefinition);
    }
    catch (Exception e) {
      fail("Calling add with the mock kListenerDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDefinition);

    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertSame(mockListenerDefinition, beanDefinition.getListeners().iterator().next());

    final ListenerDefinition mockListenerDefinitionCopy = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinitionCopy.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().once();
    mockListenerDefinitionCopy.compareTo(mockListenerDefinition);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        return 0;
      }
    });
    EasyMock.replay(mockListenerDefinitionCopy);

    try {
      beanDefinition.add(mockListenerDefinitionCopy);
      fail("Calling add with a copy of the mock ListenerDefinition should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The listener class (com.mycompany.mypackage.MyListener) has already been registered on this bean definition (myBean)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling add with a copy of the mock ListenerDefinition threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDefinitionCopy);

    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertSame(mockListenerDefinition, beanDefinition.getListeners().iterator().next());
  }

  public void testAddNullListener() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    try {
      beanDefinition.add((ListenerDefinition) null);
      fail("Adding a null ListenerDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The listener definition to add to this bean definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Adding a null ListenerDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(beanDefinition.getListeners().isEmpty());
  }

  public void testAddProperty() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final PropertyDefinition mockPropertyDefinition = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockPropertyDefinition);

    try {
      beanDefinition.add(mockPropertyDefinition);
    }
    catch (Exception e) {
      fail("Calling add with a mock PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockPropertyDefinition);

    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertSame(mockPropertyDefinition, beanDefinition.getProperties().iterator().next());
  }

  public void testAddExistingProperty() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final PropertyDefinition mockPropertyDefinition = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockPropertyDefinition);

    try {
      beanDefinition.add(mockPropertyDefinition);
    }
    catch (Exception e) {
      fail("Calling add with a mock PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockPropertyDefinition);

    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertSame(mockPropertyDefinition, beanDefinition.getProperties().iterator().next());

    final PropertyDefinition mockPropertyDefinitionCopy = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinitionCopy.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockPropertyDefinitionCopy);

    try {
      beanDefinition.add(mockPropertyDefinitionCopy);
      fail("Adding a copy of the mock PropertyDefinition should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The property (myProperty) has already been defined on this bean definition (myBean)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Adding a copy of the mock PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockPropertyDefinitionCopy);

    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertSame(mockPropertyDefinition, beanDefinition.getProperties().iterator().next());
  }

  public void testAddNullProperty() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    try {
      beanDefinition.add((PropertyDefinition) null);
      fail("Adding a null PropertyDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The property definition being added to this bean definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Adding a null PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(beanDefinition.getProperties().isEmpty());
  }

  public void testGetListenerDefinition() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final ListenerDefinition mockListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinition.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().times(4);
    mockListenerDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDefinition);

    try {
      beanDefinition.add(mockListenerDefinition);
    }
    catch (Exception e) {
      fail("Adding the mock ListenerDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertNull(beanDefinition.getListenerDefinition(null));
    assertNull(beanDefinition.getListenerDefinition("org.mycompany.mypackage.MyListener"));
    assertSame(mockListenerDefinition, beanDefinition.getListenerDefinition("com.mycompany.mypackage.MyListener"));

    EasyMock.verify(mockListenerDefinition);
  }

  public void testGetPropertyDefinition() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final PropertyDefinition mockPropertyDefinition = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockPropertyDefinition);

    try {
      beanDefinition.add(mockPropertyDefinition);
    }
    catch (Exception e) {
      fail("Adding the mock PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertNull(beanDefinition.getPropertyDefinition(null));
    assertNull(beanDefinition.getPropertyDefinition("myProp"));
    assertSame(mockPropertyDefinition, beanDefinition.getPropertyDefinition("myProperty"));

    EasyMock.verify(mockPropertyDefinition);
  }

  public void testRemoveListener() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final ListenerDefinition mockListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    EasyMock.expect(mockListenerDefinition.getClassName()).andReturn("com.mycompany.mypackage.MyListener");
    EasyMock.expectLastCall().once();
    mockListenerDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockListenerDefinition);

    try {
      beanDefinition.add(mockListenerDefinition);
    }
    catch (Exception e) {
      fail("Adding the mock ListenerDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockListenerDefinition);

    final ListenerDefinition nonMemberListenerDefinition = EasyMock.createMock(ListenerDefinition.class);
    nonMemberListenerDefinition.compareTo(mockListenerDefinition);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        return -1;
      }
    });
    EasyMock.replay(nonMemberListenerDefinition);

    EasyMock.reset(mockListenerDefinition);
    mockListenerDefinition.setBeanDefinition(null);
    EasyMock.expectLastCall().once();
    mockListenerDefinition.compareTo(mockListenerDefinition);
    EasyMock.expectLastCall().andStubAnswer(new IAnswer<Object>() {
      public Object answer() throws Throwable {
        return 0;
      }
    });
    EasyMock.replay(mockListenerDefinition);

    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertFalse(beanDefinition.remove((ListenerDefinition) null));
    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertFalse(beanDefinition.remove(nonMemberListenerDefinition));
    assertFalse(beanDefinition.getListeners().isEmpty());
    assertEquals(1, beanDefinition.getListeners().size());
    assertTrue(beanDefinition.remove(mockListenerDefinition));
    assertTrue(beanDefinition.getListeners().isEmpty());
    assertEquals(0, beanDefinition.getListeners().size());

    EasyMock.verify(nonMemberListenerDefinition);
    EasyMock.verify(mockListenerDefinition);
  }

  public void testRemoveProperty() throws Exception {
    final BeanDefinition<ListenerDefinition, PropertyDefinition> beanDefinition = getBeanDefinition(
      "myBean", "com.mycompany.mypackage.MyBean");

    final PropertyDefinition mockPropertyDefinition = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(mockPropertyDefinition.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().times(3);
    mockPropertyDefinition.setBeanDefinition(beanDefinition);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockPropertyDefinition);

    try {
      beanDefinition.add(mockPropertyDefinition);
    }
    catch (Exception e) {
      fail("Adding the mock PropertyDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockPropertyDefinition);

    final PropertyDefinition nonMemberPropertyDefinition = EasyMock.createMock(PropertyDefinition.class);
    EasyMock.expect(nonMemberPropertyDefinition.getName()). andReturn("myProp");
    EasyMock.expectLastCall().once();
    EasyMock.replay(nonMemberPropertyDefinition);

    EasyMock.reset(mockPropertyDefinition);
    EasyMock.expect(mockPropertyDefinition.getName()).andReturn("myProperty");
    EasyMock.expectLastCall().once();
    mockPropertyDefinition.setBeanDefinition(null);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockPropertyDefinition);

    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertFalse(beanDefinition.remove((PropertyDefinition) null));
    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertFalse(beanDefinition.remove(nonMemberPropertyDefinition));
    assertFalse(beanDefinition.getProperties().isEmpty());
    assertEquals(1, beanDefinition.getProperties().size());
    assertTrue(beanDefinition.remove(mockPropertyDefinition));
    assertTrue(beanDefinition.getProperties().isEmpty());
    assertEquals(0, beanDefinition.getProperties().size());

    EasyMock.verify(nonMemberPropertyDefinition);
    EasyMock.verify(mockPropertyDefinition);
  }

  protected static final class TestBeanDefinition<L extends ListenerDefinition, P extends PropertyDefinition> extends AbstractBeanDefinition<L, P> {

    public TestBeanDefinition(final String id, final String className) {
      super(id, className);
    }
  }

}
