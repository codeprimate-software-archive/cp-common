/*
 * AbstractListenerTest.java (c) 30 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.4
 * @see com.cp.common.beans.event.AbstractListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.annotation.Required;
import com.cp.common.beans.util.NoSuchPropertyException;
import com.cp.common.test.util.TestUtil;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractListenerTest extends TestCase {

  public AbstractListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractListenerTest.class);
    //suite.addTest(new AbstractListenerTest("testName"));
    return suite;
  }

  protected void assertListenerState(final MockAbstractListener listener,
                                     final boolean expectedCalled,
                                     final String expectedModifiedProperty,
                                     final Object expectedNewValue)
  {
    assertNotNull(listener);
    assertEquals(expectedCalled, listener.isCalled());
    TestUtil.assertNullEquals(expectedModifiedProperty, listener.getModifiedProperty());
    TestUtil.assertNullEquals(expectedNewValue, listener.getNewValue());
  }

  public void testInstantiation() throws Exception {
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener();
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener using the default constructor threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithBean() throws Exception {
    final Bean mockBean = new MockBean();
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the AbstractListener with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener((Object) null);
      fail("Instantiating an instance of AbstractListener with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithProperty() throws Exception {
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener("myProperty");
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener initialized with property 'myProperty' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals("myProperty", listener.getPropertyName());
  }

  public void testInstantiationWithEmptyProperty() throws Exception {
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener(" ");
      fail("Instantiating an instance of AbstractListener with an empty property name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener with an empty property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullProperty() throws Exception {
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener((String) null);
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener with a null property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiateWithBeanAndProperty() throws Exception {
    final Bean mockBean = new MockBean();
    AbstractListener listener = null;

    assertNull(listener);

    try {
      listener = new TestListener(mockBean, "null");
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractListener with a non-null Bean and non-empty property name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertEquals("null", listener.getPropertyName());
  }

  public void testGetAnnotation() throws Exception {
    final Bean mockAnnotatedBean = new MockAnnotatedBean();
    final AbstractListener listener = new TestListener(mockAnnotatedBean);

    assertNotNull(listener);
    assertSame(mockAnnotatedBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    final MockAnnotation mockAnnotation = listener.getAnnotation("value", MockAnnotation.class);

    assertNotNull(mockAnnotation);
    assertEquals("test", mockAnnotation.value());

    final Required requiredAnnotation = listener.getAnnotation("value", Required.class);

    assertNull(requiredAnnotation);
  }

  public void testIsAnnotationPresent() throws Exception {
    final Bean mockAnnotatedBean = new MockAnnotatedBean();
    final AbstractListener listener = new TestListener(mockAnnotatedBean);

    assertNotNull(listener);
    assertSame(mockAnnotatedBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
    assertTrue(listener.isAnnotationPresent("value", MockAnnotation.class));
    assertFalse(listener.isAnnotationPresent("value", Required.class));
  }

  public void testIsInterestedAllProperties() throws Exception {
    final AbstractListener allListener = new MockPropertyChangeListener();

    assertNull(allListener.getPropertyName());
    assertTrue(allListener.isInterested(null));
    assertTrue(allListener.isInterested(" "));
    assertTrue(allListener.isInterested("null"));
    assertTrue(allListener.isInterested("anyProperty"));
    assertTrue(allListener.isInterested("personId"));
    assertTrue(allListener.isInterested("address"));
    assertTrue(allListener.isInterested("phoneNumber"));
  }

  public void testIsInterestedOnlyOneProperty() throws Exception {
    final AbstractListener ssnListener = new MockVetoableChangeListener("ssn");

    assertEquals("ssn", ssnListener.getPropertyName());
    assertFalse(ssnListener.isInterested(null));
    assertFalse(ssnListener.isInterested(" "));
    assertFalse(ssnListener.isInterested("null"));
    assertFalse(ssnListener.isInterested("personId"));
    assertFalse(ssnListener.isInterested("address"));
    assertFalse(ssnListener.isInterested("phoneNumber"));
    assertFalse(ssnListener.isInterested("socialSecurityNumber"));
    assertFalse(ssnListener.isInterested("SSN"));
    assertFalse(ssnListener.isInterested("Ssn"));
    assertTrue(ssnListener.isInterested("ssn"));
  }

  public void testGetPropertyDescriptorForNonExistingProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = listener.getPropertyDescriptor("nonExistentProperty");
      fail("Getting the property descriptor for a non-existent property should have thrown a NoSuchPropertyException!");
    }
    catch (NoSuchPropertyException e) {
      assertEquals("(nonExistentProperty) is not a property of bean (" + mockBean.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Getting the property descriptor for a non-existing property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(propertyDescriptor);
  }

  public void testGetPropertyDescriptorForReadOnlyProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = listener.getPropertyDescriptor("readOnlyValue");
    }
    catch (Exception e) {
      fail("Getting the property descriptor for property 'readOnlyValue' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyDescriptor);
    assertEquals("readOnlyValue", propertyDescriptor.getName());
    assertEquals(String.class, propertyDescriptor.getPropertyType());
    assertNotNull(propertyDescriptor.getReadMethod());
    assertNull(propertyDescriptor.getWriteMethod());
  }

  public void testGetPropertyDescriptorForWriteOnlyProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    PropertyDescriptor propertyDescriptor = null;

    assertNull(propertyDescriptor);

    try {
      propertyDescriptor = listener.getPropertyDescriptor("writeOnlyValue");
    }
    catch (Exception e) {
      fail("Getting the property descriptor for property 'readOnlyValue' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(propertyDescriptor);
    assertEquals("writeOnlyValue", propertyDescriptor.getName());
    assertEquals(String.class, propertyDescriptor.getPropertyType());
    assertNull(propertyDescriptor.getReadMethod());
    assertNotNull(propertyDescriptor.getWriteMethod());
  }

  public void testGetWriteMethodForReadOnlyProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    Method writeMethod = null;

    assertNull(writeMethod);

    try {
      writeMethod = listener.getWriteMethod("readOnlyValue");
      fail("Calling getWriteMethod for property (readOnlyValue) should have thrown a NoSuchMethodException!");
    }
    catch (NoSuchPropertyException e) {
      fail("Calling getWriteMethod for property (readOnlyValue) should not have thrown a NoSuchPropertyException!");
    }
    catch (NoSuchMethodException e) {
      assertEquals("No write method exists for property (readOnlyValue) on bean (" + mockBean.getClass().getName()
        + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getWriteMethod for property (readOnlyValue) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(writeMethod);
  }

  public void testGetWriteMethodForReadWriteProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    Method writeMethod = null;

    assertNull(writeMethod);

    try {
      writeMethod = listener.getWriteMethod("readWriteValue");
    }
    catch (Exception e) {
      fail("Getting the write method for property (readWriteValue) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(writeMethod);
    assertEquals("setReadWriteValue", writeMethod.getName());
  }

  public void testGetWriteMethodForWriteOnlyProperty() throws Exception {
    final Bean mockBean = new MockBean();
    final AbstractListener listener = new TestListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    Method writeMethod = null;

    assertNull(writeMethod);

    try {
      writeMethod = listener.getWriteMethod("writeOnlyValue");
    }
    catch (Exception e) {
      fail("Getting the write method for property (writeOnlyValue) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(writeMethod);
    assertEquals("setWriteOnlyValue", writeMethod.getName());
  }

  public void testPropertyListening() throws Exception {
    final MockPropertyChangeListener allPropertyListener = new MockPropertyChangeListener();
    final MockPropertyChangeListener enabledPropertyListener = new MockPropertyChangeListener("enabled");

    final MockVetoableChangeListener allVetoListener = new MockVetoableChangeListener();
    final MockVetoableChangeListener foregroundVetoListener = new MockVetoableChangeListener("foreground");

    final JComponent mockComponent = new MockComponent();
    mockComponent.addPropertyChangeListener(allPropertyListener);
    mockComponent.addPropertyChangeListener(enabledPropertyListener);
    mockComponent.addVetoableChangeListener(allVetoListener);
    mockComponent.addVetoableChangeListener(foregroundVetoListener);

    mockComponent.setEnabled(!mockComponent.isEnabled());

    assertListenerState(allPropertyListener, true, "enabled", mockComponent.isEnabled());
    assertListenerState(enabledPropertyListener, true, "enabled", mockComponent.isEnabled());
    assertListenerState(allVetoListener, true, "enabled", mockComponent.isEnabled());
    assertListenerState(foregroundVetoListener, false, null, null);

    mockComponent.setBackground(Color.LIGHT_GRAY);

    assertEquals(Color.LIGHT_GRAY, mockComponent.getBackground());
    assertListenerState(allPropertyListener, true, "background", Color.LIGHT_GRAY);
    assertListenerState(enabledPropertyListener, false, "enabled", mockComponent.isEnabled());
    assertListenerState(allVetoListener, true, "background", Color.LIGHT_GRAY);
    assertListenerState(foregroundVetoListener, false, null, null);

    mockComponent.setVisible(!mockComponent.isVisible());

    assertListenerState(allPropertyListener, true, "visible", mockComponent.isVisible());
    assertListenerState(enabledPropertyListener, false, "enabled", mockComponent.isEnabled());
    assertListenerState(allVetoListener, true, "visible", mockComponent.isVisible());
    assertListenerState(foregroundVetoListener, false, null, null);

    mockComponent.setForeground(Color.WHITE);

    assertEquals(Color.WHITE, mockComponent.getForeground());
    assertListenerState(allPropertyListener, true, "foreground", mockComponent.getForeground());
    assertListenerState(enabledPropertyListener, false, "enabled", mockComponent.isEnabled());
    assertListenerState(allVetoListener, true, "foreground", mockComponent.getForeground());
    assertListenerState(foregroundVetoListener, true, "foreground", mockComponent.getForeground());
  }

  @Documented
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  protected static @interface MockAnnotation {

    public String value();

  }

  public static final class MockAnnotatedBean extends AbstractBean {

    private Object value;

    public Object getValue() {
      return value;
    }

    @MockAnnotation(value="test")
    public void setValue(final Object value) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockAnnotatedBean.this.value = value;
        }
      };
      processChange("value", this.value, value, callbackHandler);
    }
  }

  public static final class MockBean extends AbstractBean {

    private final String readOnlyValue = "readOnlyValue";
    private String readWriteValue = null;
    private String writeOnlyValue = null;

    public String getReadOnlyValue() {
      return readOnlyValue;
    }

    public String getReadWriteValue() {
      return readWriteValue;
    }

    public void setReadWriteValue(final String value) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.readWriteValue = value;
        }
      };
      processChange("readWriteValue", this.readWriteValue, value, callbackHandler);
    }

    public void setWriteOnlyValue(final String value) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.writeOnlyValue = value;
        }
      };
      processChange("writeOnlyValue", this.writeOnlyValue, value, callbackHandler);
    }
  }

  protected static final class MockComponent extends JComponent {

    @Override
    public void setBackground(final Color backgroundColor) {
      try {
        fireVetoableChange("background", getBackground(), backgroundColor);
        super.setBackground(backgroundColor);
      }
      catch (PropertyVetoException e) {
        throw new IllegalArgumentException("(" + backgroundColor + ") is not a valid background color!");
      }
    }

    @Override
    public void setEnabled(final boolean enabled) {
      try {
        fireVetoableChange("enabled", isEnabled(), enabled);
        super.setEnabled(enabled);
      }
      catch (PropertyVetoException e) {
        throw new IllegalArgumentException("(" + enabled + ") is not a valid state for enabled!");
      }
    }

    @Override
    public void setForeground(final Color foregroundColor) {
      try {
        fireVetoableChange("foreground", getForeground(), foregroundColor);
        super.setForeground(foregroundColor);
      }
      catch (PropertyVetoException e) {
        throw new IllegalArgumentException("(" + foregroundColor + ") is not a valid foreground color!");
      }
    }

    @Override
    public void setVisible(final boolean visable) {
      try {
        final boolean oldVisible = isVisible();
        fireVetoableChange("visible", oldVisible, visable);
        super.setVisible(visable);
        firePropertyChange("visible", oldVisible, visable);
      }
      catch (PropertyVetoException e) {
        throw new IllegalArgumentException("(" + visable + ") is not a valid state for visible!");
      }
    }
  }

  protected static interface MockAbstractListener {

    public boolean isCalled();

    public String getModifiedProperty();

    public Object getNewValue();

  }

  protected static class MockPropertyChangeListener extends AbstractPropertyChangeListener implements MockAbstractListener {

    private boolean called = false;
    private Object newValue = null;
    private String modifiedProperty = null;

    public MockPropertyChangeListener() {
    }

    public MockPropertyChangeListener(final Object annotatedBean) {
      super(annotatedBean);
    }

    public MockPropertyChangeListener(final String propertyName) {
      super(propertyName);
    }

    public MockPropertyChangeListener(final Object annotatedBean, final String propertyName) {
      super(annotatedBean, propertyName);
    }

    // performs destructive read
    public boolean isCalled() {
      final boolean called = this.called;
      setCalled(false);
      return called;
    }

    private void setCalled(final boolean called) {
      this.called = called;
    }

    public String getModifiedProperty() {
      return modifiedProperty;
    }

    private void setModifiedProperty(final String modifiedProperty) {
      this.modifiedProperty = modifiedProperty;
    }

    public Object getNewValue() {
      return newValue;
    }

    private void setNewValue(final Object newValue) {
      this.newValue = newValue;
    }

    public void handle(final PropertyChangeEvent event) {
      setCalled(true);
      setModifiedProperty(event.getPropertyName());
      setNewValue(event.getNewValue());
    }
  }

  protected static class MockVetoableChangeListener extends AbstractVetoableChangeListener implements MockAbstractListener {

    private boolean called = false;
    private Object newValue = null;
    private String modifiedProperty = null;

    public MockVetoableChangeListener() {
    }

    public MockVetoableChangeListener(final Object annotatedBean) {
      super(annotatedBean);
    }

    public MockVetoableChangeListener(final String propertyName) {
      super(propertyName);
    }

    public MockVetoableChangeListener(final Object annotatedBean, final String propertyName) {
      super(annotatedBean, propertyName);
    }

    // performs destructive read
    public boolean isCalled() {
      final boolean called = this.called;
      setCalled(false);
      return called;
    }

    private void setCalled(final boolean called) {
      this.called = called;
    }

    public String getModifiedProperty() {
      return modifiedProperty;
    }

    private void setModifiedProperty(final String modifiedProperty) {
      this.modifiedProperty = modifiedProperty;
    }

    public Object getNewValue() {
      return newValue;
    }

    private void setNewValue(final Object newValue) {
      this.newValue = newValue;
    }

    protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
      setCalled(true);
      setModifiedProperty(event.getPropertyName());
      setNewValue(event.getNewValue());
    }
  }

  protected static final class TestListener extends AbstractListener {

    public TestListener() {
    }

    public TestListener(final Object annotatedBean) {
      super(annotatedBean);
    }

    public TestListener(final String propertyName) {
      super(propertyName);
    }

    public TestListener(final Object annotatedBean, final String propertyName) {
      super(annotatedBean, propertyName);
    }
  }

}
