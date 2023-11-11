/*
 * UserConverterTest.java (c) 10 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.UserConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.ConvertUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserConverterTest extends TestCase {

  public UserConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(UserConverterTest.class);
    //suite.addTest(new UserConverterTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ConvertUtil.unregister(User.class);
  }

  public void testConvert() throws Exception {
    final AbstractConverter userConverter = new UserConverter();

    assertNotNull(userConverter);
    assertNull(userConverter.getDefaultValue());
    assertFalse(userConverter.isUsingDefaultValue());
    assertNull(userConverter.convert(User.class, null));
    assertNull(userConverter.convert(User.class, "null"));
    assertNull(userConverter.convert(User.class, "NULL"));
    assertNull(userConverter.convert(User.class, " Null "));
    assertNull(userConverter.convert(User.class, " "));
    assertEquals(new DefaultUser("admin"), userConverter.convert(User.class, "admin"));
    assertEquals(new DefaultUser("jblum"), userConverter.convert(User.class, "jblum"));
    assertEquals(new DefaultUser("root"), userConverter.convert(User.class, "root"));
  }

  public void testConvertHavingDefaultValue() throws Exception {
    final User blumj = new DefaultUser("blumj");
    final AbstractConverter userConverter = new UserConverter(blumj);
    Object convertedValue = null;

    assertNotNull(userConverter);
    assertEquals(blumj, userConverter.getDefaultValue());
    assertTrue(userConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = userConverter.convert(User.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue(convertedValue instanceof User);
    assertEquals(blumj, convertedValue);
    assertSame(blumj, convertedValue);
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter userConverter = new UserConverter(true);
    Object convertedValue = null;

    assertNotNull(userConverter);
    assertNull(userConverter.getDefaultValue());
    assertTrue(userConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = userConverter.convert(User.class, null);
    }
    catch (Exception e) {
      fail("Calling convert with a null Object value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue("Expected User type; but was (" + convertedValue.getClass().getName() + ")!",
      convertedValue instanceof User);
    assertEquals(new DefaultUser("root"), convertedValue);
  }

  public void testSetBeanProperty() throws Exception {
    final User jblum = new DefaultUser("jblum");
    final MockBean bean = new MockBean();

    assertNotNull(bean);
    assertNull(bean.getUser());

    BeanUtil.setPropertyValue(bean, "user", "blumj");

    assertEquals(new DefaultUser("blumj"), bean.getUser());

    BeanUtil.setPropertyValue(bean, "user", jblum);

    assertEquals(jblum, bean.getUser());
    assertNotSame(jblum, bean.getUser());

    BeanUtil.setPropertyValue(bean, "user", "null");

    assertNull(bean.getUser());
  }

  public void testSetBeanPropertyHavingDefaultValue() throws Exception {
    ConvertUtil.register(User.class, new UserConverter(new DefaultUser("blumj")));
    final MockBean bean = new MockBean(new DefaultUser("jblum"));

    assertNotNull(bean);
    assertEquals(new DefaultUser("jblum"), bean.getUser());

    BeanUtil.setPropertyValue(bean, "user", null);

    assertEquals(new DefaultUser("blumj"), bean.getUser());
  }

  public void testSetBeanPropertyUsingDefaultValue() throws Exception {
    ConvertUtil.register(User.class, new UserConverter(true));
    final MockBean bean = new MockBean(new DefaultUser("jblum"));

    assertNotNull(bean);
    assertEquals(new DefaultUser("jblum"), bean.getUser());

    BeanUtil.setPropertyValue(bean, "user", null);

    assertEquals(new DefaultUser("root"), bean.getUser());
  }

  public static final class MockBean {

    private User user;

    public MockBean() {
    }

    public MockBean(final User user) {
      setUser(user);
    }

    public User getUser() {
      return user;
    }

    public void setUser(final User user) {
      this.user = user;
    }
  }

}
