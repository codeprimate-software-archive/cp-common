/*
 * AbstractDataModelTest.java (c) 17 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.30
 * @see com.cp.common.util.AbstractDataModel
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.event.RequiredFieldVetoableChangeListener;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractDataModelTest extends TestCase {

  public AbstractDataModelTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractDataModelTest.class);
    //suite.addTest(new AbstractDataModelTest("testName"));
    return suite;
  }

  public void testAddNotifyChangeListener() throws Exception {
    final PhoneNumber phoneNumber = new PhoneNumber("503", "555", "1234", null);

    final TestChangeListener changeListener = new TestChangeListener();
    phoneNumber.addChangeListener(changeListener);

    assertFalse(changeListener.isStateChanged());

    phoneNumber.setExtension("4444");

    assertTrue(changeListener.isStateChanged());
  }

  public void testAddNotifyRemovePropertyChangeListener() throws Exception {
    final PhoneNumber phoneNumber = new PhoneNumber("503", "555", "1234", null);

    final AreaCodePropertyChangeListener areaCodeListener = new AreaCodePropertyChangeListener();
    final WhichPropertyChangeListener whichListener = new WhichPropertyChangeListener();

    phoneNumber.addPropertyChangeListener("areaCode", areaCodeListener);
    phoneNumber.addPropertyChangeListener(whichListener);

    assertNull(whichListener.getPropertyName());
    assertNull(areaCodeListener.getAreaCode());
    assertEquals("555", phoneNumber.getPrefix());

    phoneNumber.setPrefix("111");

    assertEquals("prefix", whichListener.getPropertyName());
    assertEquals("111", phoneNumber.getPrefix());
    assertNull(areaCodeListener.getAreaCode());

    phoneNumber.setAreaCode("563");

    assertEquals("areaCode", whichListener.getPropertyName());
    assertEquals("563", phoneNumber.getAreaCode());
    assertEquals("563", areaCodeListener.getAreaCode());

    phoneNumber.setExtension("012");

    assertEquals("extension", whichListener.getPropertyName());
    assertEquals("012", phoneNumber.getExtension());
    assertEquals("563", areaCodeListener.getAreaCode());

    phoneNumber.removePropertyChangeListener("areaCode", areaCodeListener);
    phoneNumber.setAreaCode("303");

    assertEquals("areaCode", whichListener.getPropertyName());
    assertEquals("303", phoneNumber.getAreaCode());
    assertEquals("563", areaCodeListener.getAreaCode());
  }

  public void testAddNotifyRemoveVetoableChangeListener() throws Exception {
    final PhoneNumber phoneNumber = new PhoneNumber(null, "555", "1234", null);

    final AreaCodePropertyChangeListener areaCodeListener = new AreaCodePropertyChangeListener();
    final AreaCodeVetoableChangeListener areaCodeVetoListener = new AreaCodeVetoableChangeListener();
    final WhichPropertyChangeListener whichListener = new WhichPropertyChangeListener();

    phoneNumber.addPropertyChangeListener("areaCode", areaCodeListener);
    phoneNumber.addPropertyChangeListener(whichListener);
    phoneNumber.addVetoableChangeListener("areaCode", areaCodeVetoListener);
    phoneNumber.addVetoableChangeListener("prefix", RequiredFieldVetoableChangeListener.INSTANCE);
    phoneNumber.addVetoableChangeListener("suffix", RequiredFieldVetoableChangeListener.INSTANCE);

    assertNull(whichListener.getPropertyName());
    assertNull(areaCodeListener.getAreaCode());
    assertEquals("555", phoneNumber.getPrefix());

    try {
      phoneNumber.setPrefix("101");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the phone number prefix to '101' should not have thrown a ConstraintViolationException!");
    }

    assertEquals("prefix", whichListener.getPropertyName());
    assertEquals("101", phoneNumber.getPrefix());
    assertNull(areaCodeListener.getAreaCode());
    assertNull(phoneNumber.getExtension());

    try {
      phoneNumber.setExtension("4444");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the phone number extension to '4444' should not have thrown a ConstraintViolationException!");
    }

    assertEquals("extension", whichListener.getPropertyName());
    assertEquals("4444", phoneNumber.getExtension());
    assertNull(areaCodeListener.getAreaCode());
    assertNull(phoneNumber.getAreaCode());

    try {
      phoneNumber.setAreaCode("212");
      fail("Setting the phone number area code to '212' should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      // expected behavior!
    }

    assertEquals("extension", whichListener.getPropertyName());
    assertNull(phoneNumber.getAreaCode());
    assertNull(areaCodeListener.getAreaCode());
    assertEquals("1234", phoneNumber.getSuffix());

    try {
      phoneNumber.setSuffix(null);
      fail("Setting the phone number suffix to null should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      // expected behavior!
    }

    assertEquals("extension", whichListener.getPropertyName());
    assertEquals("1234", phoneNumber.getSuffix());
    assertNull(areaCodeListener.getAreaCode());
    assertNull(phoneNumber.getAreaCode());

    try {
      phoneNumber.setAreaCode("503");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the phone number area code to '503' should not have thrown a ConstraintViolationException!");
    }

    assertEquals("Expected area code property to be last modified!", "areaCode", whichListener.getPropertyName());
    assertEquals("503", phoneNumber.getAreaCode());
    assertEquals("503", areaCodeListener.getAreaCode());
    assertEquals("4444", phoneNumber.getExtension());

    try {
      phoneNumber.setExtension(null);
    }
    catch (ConstraintViolationException e) {
      fail("Setting the phone number extension to null should not have thrown a ConstraintViolationException!");
    }

    assertEquals("extension", whichListener.getPropertyName());
    assertNull(phoneNumber.getExtension());
    assertEquals("503", areaCodeListener.getAreaCode());

    phoneNumber.addVetoableChangeListener("areaCode", RequiredFieldVetoableChangeListener.INSTANCE);
    phoneNumber.removeVetoableChangeListener("areaCode", areaCodeVetoListener);

    try {
      phoneNumber.setAreaCode("303");
    }
    catch (ConstraintViolationException e) {
      fail("Setting the phone number area code to '303' should not have thrown a ConstraintViolationException!");
    }

    assertEquals("areaCode", whichListener.getPropertyName());
    assertEquals("303", phoneNumber.getAreaCode());
    assertEquals("303", areaCodeListener.getAreaCode());

    try {
      phoneNumber.setAreaCode(null);
      fail("Setting the phone number area code to null should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      // expected behavior!
    }

    assertEquals("areaCode", whichListener.getPropertyName());
    assertEquals("303", phoneNumber.getAreaCode());
    assertEquals("303", areaCodeListener.getAreaCode());
  }

  private final class AreaCodePropertyChangeListener implements PropertyChangeListener {

    private String areaCode = null;

    public String getAreaCode() {
      return areaCode;
    }

    public void propertyChange(final PropertyChangeEvent event) {
      areaCode = (String) event.getNewValue();
    }
  }

  private final class AreaCodeVetoableChangeListener implements VetoableChangeListener {

    public void vetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
      if (!"503".equals(event.getNewValue())) {
        throw new PropertyVetoException("(" + event.getNewValue() + ") is not valid area code!", event);
      }
    }
  }

  private final class PhoneNumber extends AbstractDataModel {

    private static final String DASH = "-";
    private static final String EXT = " ext. ";

    private String areaCode;
    private String extension;
    private String prefix;
    private String suffix;

    public PhoneNumber(final String areaCode, 
                       final String prefix,
                       final String suffix,
                       final String extension)
      throws PropertyVetoException
    {
      setAreaCode(areaCode);
      setPrefix(prefix);
      setSuffix(suffix);
      setExtension(extension);
    }

    public String getAreaCode() {
      return areaCode;
    }

    public void setAreaCode(final String areaCode) throws PropertyVetoException {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumber.this.areaCode = areaCode;
        }
      };
      processChange("areaCode", this.areaCode, areaCode, callbackHandler);
    }

    public String getExtension() {
      return extension;
    }

    public void setExtension(final String extension) throws PropertyVetoException {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumber.this.extension = extension;
        }
      };
      processChange("extension", this.extension, extension, callbackHandler);
    }

    public String getPrefix() {
      return prefix;
    }

    public void setPrefix(final String prefix) throws PropertyVetoException {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumber.this.prefix = prefix;
        }
      };
      processChange("prefix", this.prefix, prefix, callbackHandler);
    }

    public String getSuffix() {
      return suffix;
    }

    public void setSuffix(final String suffix) throws PropertyVetoException {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumber.this.suffix = suffix;
        }
      };
      processChange("suffix", this.suffix, suffix, callbackHandler);
    }

    public Object getValue() {
      final StringBuffer buffer = new StringBuffer();

      if (ObjectUtil.isNotNull(getAreaCode())) {
        buffer.append(getAreaCode()).append(DASH);
      }

      buffer.append(getPrefix());
      buffer.append(DASH);
      buffer.append(getSuffix());

      if (ObjectUtil.isNotNull(getExtension())) {
        buffer.append(EXT).append(getExtension());
      }

      return buffer.toString();
    }

    public void setValue(final Object value) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof PhoneNumber)) {
        return false;
      }

      final PhoneNumber that = (PhoneNumber) obj;

      return ObjectUtil.equals(getAreaCode(), that.getAreaCode())
        && ObjectUtil.equals(getPrefix(), that.getPrefix())
        && ObjectUtil.equals(getSuffix(), that.getSuffix())
        && ObjectUtil.equals(getExtension(), that.getExtension());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getAreaCode());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPrefix());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getSuffix());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getExtension());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{areaCode = ");
      buffer.append(getAreaCode());
      buffer.append(", prefix = ").append(getPrefix());
      buffer.append(", suffix = ").append(getSuffix());
      buffer.append(", extension = ").append(getExtension());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  private final class TestChangeListener implements ChangeListener {

    private boolean stateChanged;

    public boolean isStateChanged() {
      return stateChanged;
    }

    public void stateChanged(final ChangeEvent e) {
      stateChanged = true;
    }
  }

  private final class WhichPropertyChangeListener implements PropertyChangeListener {

    private String propertyName;

    public String getPropertyName() {
      return propertyName;
    }

    public void propertyChange(final PropertyChangeEvent event) {
      propertyName = event.getPropertyName();
    }
  }

}
