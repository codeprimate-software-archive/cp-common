/*
 * ProcessConverterTest.java (c) 10 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.ProcessConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.ConvertUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ProcessConverterTest extends TestCase {

  public ProcessConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ProcessConverterTest.class);
    //suite.addTest(new ProcessConverterTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ConvertUtil.unregister(com.cp.common.beans.Process.class);
  }

  public void testConvert() throws Exception {
    final AbstractConverter processConverter = new ProcessConverter();

    assertNotNull(processConverter);
    assertNull(processConverter.getDefaultValue());
    assertFalse(processConverter.isUsingDefaultValue());
    assertNull(processConverter.convert(com.cp.common.beans.Process.class, null));
    assertNull(processConverter.convert(com.cp.common.beans.Process.class, " "));
    assertNull(processConverter.convert(com.cp.common.beans.Process.class, "null"));
    assertNull(processConverter.convert(com.cp.common.beans.Process.class, " Null "));
    assertEquals(new DefaultProcess("batch"), processConverter.convert(com.cp.common.beans.Process.class, "batch"));
    assertEquals(new DefaultProcess("conversion"), processConverter.convert(com.cp.common.beans.Process.class, "conversion"));
    assertEquals(new DefaultProcess("system"), processConverter.convert(com.cp.common.beans.Process.class, "system"));
  }

  public void testConvertHavingDefaultValue() throws Exception {
    final AbstractConverter processConverter = new ProcessConverter(new DefaultProcess("kernel"));
    Object convertedValue = null;

    assertNotNull(processConverter);
    assertEquals(new DefaultProcess("kernel"), processConverter.getDefaultValue());
    assertTrue(processConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = processConverter.convert(com.cp.common.beans.Process.class, "null");
    }
    catch (Exception e) {
      fail("Calling convert with a null String literal value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue(convertedValue instanceof com.cp.common.beans.Process);
    assertEquals(new DefaultProcess("kernel"), convertedValue);
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter processConverter = new ProcessConverter(true);
    Object convertedValue = null;

    assertNotNull(processConverter);
    assertNull(processConverter.getDefaultValue());
    assertTrue(processConverter.isUsingDefaultValue());
    assertNull(convertedValue);

    try {
      convertedValue = processConverter.convert(com.cp.common.beans.Process.class, "null");
    }
    catch (Exception e) {
      fail("Calling convert with a null String literal value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(convertedValue);
    assertTrue("Expected the Process type; but was (" + convertedValue.getClass().getName() + ")!",
      convertedValue instanceof com.cp.common.beans.Process);
    assertEquals(new DefaultProcess("system"), convertedValue);
  }

  public void testSetBeanProperty() throws Exception {
    final com.cp.common.beans.Process os = new DefaultProcess("os");
    final MockBean bean = new MockBean();

    assertNotNull(bean);
    assertNull(bean.getProcess());

    BeanUtil.setPropertyValue(bean, "process", "kernel");

    assertEquals(new DefaultProcess("kernel"), bean.getProcess());

    BeanUtil.setPropertyValue(bean, "process", os);

    assertEquals(os, bean.getProcess());
    assertNotSame(os, bean.getProcess());

    BeanUtil.setPropertyValue(bean, "process", "null");

    assertNull(bean.getProcess());
  }

  public void testSetBeanPropertyHavingDefaultValue() throws Exception {
    ConvertUtil.register(com.cp.common.beans.Process.class, new ProcessConverter(new DefaultProcess("kernel")));
    final MockBean bean = new MockBean(new DefaultProcess("batch"));

    assertEquals(new DefaultProcess("batch"), bean.getProcess());

    BeanUtil.setPropertyValue(bean, "process", "null");

    assertEquals(new DefaultProcess("kernel"), bean.getProcess());
  }

  public void testSetBeanPropertyUsingDefaultValue() throws Exception {
    ConvertUtil.register(com.cp.common.beans.Process.class, new ProcessConverter(true));
    final MockBean bean = new MockBean(new DefaultProcess("batch"));

    assertEquals(new DefaultProcess("batch"), bean.getProcess());

    BeanUtil.setPropertyValue(bean, "process", "null");

    assertEquals(new DefaultProcess("system"), bean.getProcess());
  }

  public static final class MockBean {

    private com.cp.common.beans.Process process;

    public MockBean() {
    }

    public MockBean(final com.cp.common.beans.Process process) {
      setProcess(process);
    }

    public com.cp.common.beans.Process getProcess() {
      return process;
    }

    public void setProcess(final com.cp.common.beans.Process process) {
      this.process = process;
    }
  }

}
