/*
 * AbstractParameterHandlerTest.java (c) 29 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.21
 * @see com.cp.common.beans.definition.AbstractParameterHandlerTest
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import com.cp.common.test.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class AbstractParameterHandlerTest extends TestCase {

  public AbstractParameterHandlerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractParameterHandlerTest.class);
    return suite;
  }

  protected AbstractParameterHandler getParameterizable() {
    return new TestParameterHandler();
  }

  public void testInstantiate() throws Exception {
    AbstractParameterHandler parameterHandler = null;

    assertNull(parameterHandler);

    try {
      parameterHandler = getParameterizable();
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractParameterHandler threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(parameterHandler);
    assertNotNull(parameterHandler.getInvocationArgumentList());
    assertTrue(parameterHandler.getInvocationArgumentList().isEmpty());
    assertEquals(0, parameterHandler.getInvocationArgumentList().size());
  }

  public void testAdd() throws Exception {
    final InvocationArgument mockInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgument));
    assertTrue(parameterHandler.add(EasyMock.createMock(InvocationArgument.class)));
    assertTrue(parameterHandler.add(mockInvocationArgument));
  }

  public void testAddNull() throws Exception {
    try {
      getParameterizable().add(null);
      fail("Calling add with a null InvocationArgument should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The invocation argument being added to the list of constructor/method parameters cannot be null!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling add with a null InvocationArgument threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetInvocationArgumentByIndex() throws Exception {
    final InvocationArgument mockInvocationArgumentZero = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentOne = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentTwo = EasyMock.createMock(InvocationArgument.class);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgumentTwo));
    assertTrue(parameterHandler.add(mockInvocationArgumentZero));
    assertTrue(parameterHandler.add(mockInvocationArgumentOne));
    assertSame(mockInvocationArgumentTwo, parameterHandler.getInvocationArgument(0));
    assertSame(mockInvocationArgumentZero, parameterHandler.getInvocationArgument(1));
    assertSame(mockInvocationArgumentOne, parameterHandler.getInvocationArgument(2));
  }

  public void testGetInvocationArgumentByIndexThrowsIndexOutOfBoundsException() throws Exception {
    final InvocationArgument mockInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgument));
    assertSame(mockInvocationArgument, parameterHandler.getInvocationArgument(0));

    InvocationArgument actualInvocationArgument = null;

    assertNull(actualInvocationArgument);

    try {
      actualInvocationArgument = parameterHandler.getInvocationArgument(-1);
      fail("Calling getInvocationArgument with an index of -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getInvocationArgument with an index of -1 threw an unexpected Exception (" + e.getMessage() + "!)");
    }

    assertNull(actualInvocationArgument);

    try {
      actualInvocationArgument = parameterHandler.getInvocationArgument(1);
      fail("Calling getInvocationArgument with an index of 1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getInvocationArgument with an index of 1 threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualInvocationArgument);
  }

  public void testGetInvocationArguments() throws Exception {
    final InvocationArgument mockInvocationArgumentZero = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentOne = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentTwo = EasyMock.createMock(InvocationArgument.class);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgumentTwo));
    assertTrue(parameterHandler.add(mockInvocationArgumentZero));
    assertTrue(parameterHandler.add(mockInvocationArgumentOne));

    final List<InvocationArgument> expectedInvocationArgumentList = new ArrayList<InvocationArgument>(3);
    expectedInvocationArgumentList.add(mockInvocationArgumentTwo);
    expectedInvocationArgumentList.add(mockInvocationArgumentZero);
    expectedInvocationArgumentList.add(mockInvocationArgumentOne);

    final List<InvocationArgument> actualInvocationArgumentList = parameterHandler.getInvocationArguments();

    assertNotNull(actualInvocationArgumentList);
    assertEquals(expectedInvocationArgumentList, actualInvocationArgumentList);
  }

  public void testGetInvocationArgumentsHavingNoArguments() throws Exception {
    final Parameterizable parameterHandler = getParameterizable();
    final List<InvocationArgument> actualInvocationArguments = parameterHandler.getInvocationArguments();

    assertNotNull(actualInvocationArguments);
    assertTrue(actualInvocationArguments.isEmpty());
    assertEquals(0, actualInvocationArguments.size());
  }

  public void testGetInvocationArgumentsImmutability() throws Exception {
    final InvocationArgument mockInvocationArgumentOne = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentTwo = EasyMock.createMock(InvocationArgument.class);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgumentOne));
    assertTrue(parameterHandler.add(mockInvocationArgumentTwo));

    final List<InvocationArgument> actualInvocationArguments = parameterHandler.getInvocationArguments();

    assertNotNull(actualInvocationArguments);
    assertFalse(actualInvocationArguments.isEmpty());
    assertEquals(2, actualInvocationArguments.size());
    TestUtil.assertContainsAll(actualInvocationArguments, mockInvocationArgumentOne, mockInvocationArgumentTwo);

    try {
      actualInvocationArguments.add(EasyMock.createMock(InvocationArgument.class));
      fail("Adding an InvocationArgument to an immutable List of InvocationArgument objects should have thrown an Exception!");
    }
    catch (Exception e) {
      // expected behavior!
    }

    assertFalse(actualInvocationArguments.isEmpty());
    assertEquals(2, actualInvocationArguments.size());
    TestUtil.assertContainsAll(actualInvocationArguments, mockInvocationArgumentOne, mockInvocationArgumentTwo);

    try {
      actualInvocationArguments.remove(mockInvocationArgumentOne);
      fail("Removing invocationArgumentOne from an immutable List of InvocationArgument objects should have thrown an Exception!");
    }
    catch (Exception e) {
      // expected behavior!
    }

    assertFalse(actualInvocationArguments.isEmpty());
    assertEquals(2, actualInvocationArguments.size());
    TestUtil.assertContainsAll(actualInvocationArguments, mockInvocationArgumentOne, mockInvocationArgumentTwo);
  }

  public void testGetInvocationArgumentTypes() throws Exception {
    final InvocationArgument mockIntegerInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    EasyMock.expect(mockIntegerInvocationArgument.getType()).andReturn(Integer.class);
    EasyMock.replay(mockIntegerInvocationArgument);

    final InvocationArgument mockStringInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    EasyMock.expect(mockStringInvocationArgument.getType()).andReturn(String.class);
    EasyMock.replay(mockStringInvocationArgument);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockIntegerInvocationArgument));
    assertTrue(parameterHandler.add(mockStringInvocationArgument));

    final Class[] expectedInvocationArgumentTypes = {
      Integer.class,
      String.class
    };

    final Class[] actualInvocationArgumentTypes = parameterHandler.getInvocationArgumentTypes();

    EasyMock.verify(mockIntegerInvocationArgument);
    EasyMock.verify(mockStringInvocationArgument);

    assertNotNull(actualInvocationArgumentTypes);
    TestUtil.assertEquals(expectedInvocationArgumentTypes, actualInvocationArgumentTypes);
  }

  public void testGetInvocationArgumentValues() throws Exception {
    final InvocationArgument mockIntegerInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    EasyMock.expect(mockIntegerInvocationArgument.getValue()).andReturn(0);
    EasyMock.replay(mockIntegerInvocationArgument);

    final InvocationArgument mockStringInvocationArgument = EasyMock.createMock(InvocationArgument.class);
    EasyMock.expect(mockStringInvocationArgument.getValue()).andReturn("null");
    EasyMock.replay(mockStringInvocationArgument);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockIntegerInvocationArgument));
    assertTrue(parameterHandler.add(mockStringInvocationArgument));

    final Object[] expectedInvocationArgumentValues = {
      0,
      "null"
    };

    final Object[] actualInvocationArgumentValues = parameterHandler.getInvocationArgumentValues();

    EasyMock.verify(mockIntegerInvocationArgument);
    EasyMock.verify(mockStringInvocationArgument);

    assertNotNull(actualInvocationArgumentValues);
    TestUtil.assertEquals(expectedInvocationArgumentValues, actualInvocationArgumentValues);
  }

  public void testRemove() throws Exception {
    final InvocationArgument mockInvocationArgumentZero = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentOne = EasyMock.createMock(InvocationArgument.class);
    final InvocationArgument mockInvocationArgumentTwo = EasyMock.createMock(InvocationArgument.class);

    final Parameterizable parameterHandler = getParameterizable();

    assertTrue(parameterHandler.add(mockInvocationArgumentOne));
    assertTrue(parameterHandler.add(mockInvocationArgumentTwo));
    assertFalse(parameterHandler.remove(null));
    assertFalse(parameterHandler.remove(mockInvocationArgumentZero));
    assertTrue(parameterHandler.remove(mockInvocationArgumentOne));
    assertTrue(parameterHandler.remove(mockInvocationArgumentTwo));
  }

  protected static final class TestParameterHandler extends AbstractParameterHandler {
  }

}
