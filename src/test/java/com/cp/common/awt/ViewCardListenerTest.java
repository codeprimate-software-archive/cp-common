/*
 * ViewCardListenerTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.ViewCardListener
 */

package com.cp.common.awt;

import java.awt.CardLayout;
import java.awt.Container;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ViewCardListenerTest extends TestCase {

  public ViewCardListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ViewCardListenerTest.class);
    return suite;
  }

  public void testInstantiation() throws Exception {
    final Container container = new Container();
    container.setLayout(new CardLayout(5, 5));

    ViewCardListener listener = null;

    assertNull(listener);

    try {
      listener = new ViewCardListener(container, "test");
      assertNotNull(listener);
      assertEquals(container, listener.getContainer());
      assertEquals("test", listener.getCardName());
    }
    catch (Throwable t) {
      fail("Creating an instance of the ViewCardListener class with a non-null Container and non-null, non-empty card name threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetContainerWithInvalidLayoutManager() throws Exception {
    final Container container = new Container();
    container.setLayout(new VerticalFlowLayout());

    try {
      new ViewCardListener(container, "test");
      fail("Instantiating an instance of the ViewCardListener with a Container using the VerticalFlowLayout as it's LayoutManager should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The container must use the CardLayout manager!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ViewCardListener with a Container using the VerticalFlowLayout as it's LayoutManager threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetNullContainer() throws Exception {
    try {
      new ViewCardListener(null, "test");
      fail("Instantiating an instance of the ViewCardListener class with a null Container should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The container cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ViewCardListener class with a null Container threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetEmptyCardName() throws Exception {
    final Container container = new Container();
    container.setLayout(new CardLayout(5, 5));

    try {
      new ViewCardListener(container, " ");
      fail("Instantiating an instance of the ViewCardListener class with an empty card name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("( ) is not a valid card name!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ViewCardListener class with an empty card name threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetNullCardName() throws Exception {
    final Container container = new Container();
    container.setLayout(new CardLayout(5, 5));

    try {
      new ViewCardListener(container, null);
      fail("Instantiating an instance of the ViewCardListener class with a null card name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(null) is not a valid card name!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ViewCardListener class with a null card name threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

}
