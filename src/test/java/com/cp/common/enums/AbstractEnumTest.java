/*
 * AbstractEnumTest.java (c) 19 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.11.17
 */

package com.cp.common.enums;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractEnumTest extends TestCase {

  public AbstractEnumTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractEnumTest.class);
    //suite.addTest(new AbstractEnumTest("testName"));
    return suite;
  }

  public void test() throws Exception {
    assertNotNull(Contact.EMAIL);
    assertNotNull(Continent.NORTH_AMERICA);
    assertNotNull(Country.UNITED_STATES);
    assertNotNull(Currency.US_DOLLAR);
    assertNotNull(Gender.MALE);
    assertNotNull(Language.ENGLISH);
    assertNotNull(MaritalStatus.MARRIED);
    assertNotNull(Race.WHITE);
    assertNotNull(Relationship.SELF);
    assertNotNull(State.OREGON);
  }

}
