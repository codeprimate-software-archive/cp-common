/*
 * ChainedExceptionTest.java (c) 13 August 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see com.cp.common.util.ChainedException
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectNotFoundException;
import com.cp.common.util.search.SearchException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ChainedExceptionTest extends TestCase {

  public ChainedExceptionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ChainedExceptionTest.class);
    //suite.addTest(new ChainedExceptionTest("testName"));
    return suite;
  }

  private void throwClassNotFoundException() throws ClassNotFoundException {
    throw new ClassNotFoundException("Class not found!");
  }

  private void throwObjectNotFoundException() throws ObjectNotFoundException {
    try {
      throwClassNotFoundException();
    }
    catch (ClassNotFoundException e) {
      throw new ObjectNotFoundException("Object not found!", e);
    }
  }

  private void throwSearchException() throws SearchException {
    try {
      throwObjectNotFoundException();
    }
    catch (ObjectNotFoundException e) {
      throw new SearchException("Search Failed!", e);
    }
  }

  private void throwMockChainedException() throws ChainedException {
    try {
      throwSearchException();
    }
    catch (SearchException e) {
      throw new MockChainedException("TEST", e);
    }
  }

  public void testChainedException() throws Exception {
    try {
      throwMockChainedException();
    }
    catch (ChainedException e) {
      assertTrue(e instanceof MockChainedException);
      assertEquals("TEST", e.getMessage());
      assertTrue(e.getCause() instanceof com.cp.common.util.search.SearchException);
      assertEquals("Search Failed!", e.getCause().getMessage());
      assertTrue(e.getCause().getCause() instanceof ObjectNotFoundException);
      assertEquals("Object not found!", e.getCause().getCause().getMessage());
      assertTrue(e.getCause().getCause().getCause() instanceof ClassNotFoundException);
      assertEquals("Class not found!", e.getCause().getCause().getCause().getMessage());
      //e.printStackTrace();
    }
  }

  private static final class MockChainedException extends ChainedException {

    public MockChainedException() {
    }

    public MockChainedException(final String message) {
      super(message);
    }

    public MockChainedException(final Throwable cause) {
      super(cause);
    }

    public MockChainedException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

}
