/*
 * CPActionFormTest.java (c) 27 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 */

package com.cp.common.struts;

import com.cp.common.lang.ObjectNotFoundException;
import com.cp.common.struts.validation.Validator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// TODO add test cases for other CPActionForm methods
public class CPActionFormTest extends TestCase {

  public CPActionFormTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CPActionFormTest.class);
    //suite.addTest(new CPActionFormTest("testName"));
    return suite;
  }

  public void testToString() throws Exception {
    final MockActionForm mockForm = new MockActionForm();
    mockForm.setFirstName("Jack");
    mockForm.setLastName("Handy");

    final StringBuffer expectedString = new StringBuffer("{class = class ");
    expectedString.append(MockActionForm.class.getName());
    expectedString.append(", firstName = ").append("Jack");
    expectedString.append(", lastName = ").append("Handy");
    expectedString.append("}:").append(MockActionForm.class.getName());

    final String actualString = mockForm.toString();

    assertNotNull(actualString);
    assertEquals(expectedString.toString(), actualString);
  }

  public static final class MockActionForm extends CPActionForm {

    private String firstName;
    private String lastName;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(final String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(final String lastName) {
      this.lastName = lastName;
    }

    protected Validator getValidatorInstance(final String validatorKey) throws ObjectNotFoundException {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
