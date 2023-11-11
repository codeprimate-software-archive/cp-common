/*
 * ScopeTest.java (c) 3 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.23
 * @see com.cp.common.beans.definition.Scope
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ScopeTest extends TestCase {

  public ScopeTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ScopeTest.class);
    return suite;
  }

  public void testGetScopeByDescription() throws Exception {
    assertSame(Scope.PROTOTYPE, Scope.getScopeByDescription(Scope.PROTOTYPE.getDescription()));
    assertSame(Scope.SINGLETON, Scope.getScopeByDescription(Scope.SINGLETON.getDescription()));
    assertNull(Scope.getScopeByDescription(null));
    assertNull(Scope.getScopeByDescription(""));
    assertNull(Scope.getScopeByDescription(" "));
    assertNull(Scope.getScopeByDescription("application"));
    assertNull(Scope.getScopeByDescription("global"));
    assertNull(Scope.getScopeByDescription("local"));
    assertNull(Scope.getScopeByDescription("request"));
    assertNull(Scope.getScopeByDescription("session"));
  }

  public void testGetScopeById() throws Exception {
    int maxId = 0;

    for (final Scope scope : Scope.values()) {
      maxId += scope.getId();
    }

    assertSame(Scope.PROTOTYPE, Scope.getScopeById(Scope.PROTOTYPE.getId()));
    assertSame(Scope.SINGLETON, Scope.getScopeById(Scope.SINGLETON.getId()));
    assertNull(Scope.getScopeById(-1));
    assertNull(Scope.getScopeById(0));
    assertNull(Scope.getScopeById(maxId));
  }

}
