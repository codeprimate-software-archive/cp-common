/*
 * EnumRendererTest.java (c) 17 November 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.11.17
 */

package com.cp.common.log4j;

import com.cp.common.enums.Contact;
import com.cp.common.enums.Continent;
import com.cp.common.enums.Country;
import com.cp.common.enums.Currency;
import com.cp.common.enums.Gender;
import com.cp.common.enums.Language;
import com.cp.common.enums.MaritalStatus;
import com.cp.common.enums.Oceans;
import com.cp.common.enums.Race;
import com.cp.common.enums.Relationship;
import com.cp.common.enums.State;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class EnumRendererTest extends TestCase {

  public EnumRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EnumRendererTest.class);
    //suite.addTest(new EnumRendererTest("testName"));
    return suite;
  }

  public void testDoRender() throws Exception {
    final ObjectRenderer enumRenderer = new EnumRenderer();

    assertEquals("Email", enumRenderer.doRender(Contact.EMAIL));
    assertEquals("North America", enumRenderer.doRender(Continent.NORTH_AMERICA));
    assertEquals("UNITED STATES", enumRenderer.doRender(Country.UNITED_STATES));
    assertEquals("US dollar", enumRenderer.doRender(Currency.US_DOLLAR));
    assertEquals("Female", enumRenderer.doRender(Gender.FEMALE));
    assertEquals("English", enumRenderer.doRender(Language.ENGLISH));
    assertEquals("Married", enumRenderer.doRender(MaritalStatus.MARRIED));
    assertEquals("Pacific Ocean", enumRenderer.doRender(Oceans.PACIFIC_OCEAN));
    assertEquals("White", enumRenderer.doRender(Race.WHITE));
    assertEquals("Father", enumRenderer.doRender(Relationship.FATHER));
    assertEquals("Oregon", enumRenderer.doRender(State.OREGON));
  }

}
