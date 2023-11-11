/*
 * ChainedRuntimeExceptionTest.java (c) 13 August 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see com.cp.common.util.ChainedRuntimeException
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ChainedRuntimeExceptionTest extends TestCase {

  public ChainedRuntimeExceptionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ChainedRuntimeExceptionTest.class);
    //suite.addTest(new ChainedRuntimeExceptionTest("testName"));
    return suite;
  }

  private void throwNullPointerException() {
    throw new NullPointerException("Null value!");
  }

  private void throwConfigurationException() {
    try {
      throwNullPointerException();
    }
    catch (NullPointerException e) {
      throw new ConfigurationException("Configuration error!", e);
    }
  }

  private void throwConversionException() {
    try {
      throwConfigurationException();
    }
    catch (ConfigurationException e) {
      throw new ConversionException("Conversion error!", e);
    }
  }

  private void throwMockChainedRuntimeException() {
    try {
      throwConversionException();
    }
    catch (ConversionException e) {
      throw new MockChainedRuntimeException("TEST", e);
    }
  }

  public void testChainedRuntimeException() throws Exception {
    try {
      throwMockChainedRuntimeException();
    }
    catch (ChainedRuntimeException e) {
      assertTrue(e instanceof MockChainedRuntimeException);
      assertEquals("TEST", e.getMessage());
      assertTrue(e.getCause() instanceof ConversionException);
      assertEquals("Conversion error!", e.getCause().getMessage());
      assertTrue(e.getCause().getCause() instanceof ConfigurationException);
      assertEquals("Configuration error!", e.getCause().getCause().getMessage());
      assertTrue(e.getCause().getCause().getCause() instanceof NullPointerException);
      assertEquals("Null value!", e.getCause().getCause().getCause().getMessage());
      //e.printStackTrace();
    }
  }

  private static final class MockChainedRuntimeException extends ChainedRuntimeException {

    public MockChainedRuntimeException() {
    }

    public MockChainedRuntimeException(final String message) {
      super(message);
    }

    public MockChainedRuntimeException(final Throwable cause) {
      super(cause);
    }

    public MockChainedRuntimeException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

}
