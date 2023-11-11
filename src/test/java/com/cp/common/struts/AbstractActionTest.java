/*
 * AbstractActionTest.java (c) 25 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 */

package com.cp.common.struts;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// TODO add test cases for other AbstractAction methods
public class AbstractActionTest extends TestCase {

  public AbstractActionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractActionTest.class);
//    suite.addTest(new AbstractActionTest("testName"));
    return suite;
  }

  public void testCopyFromObjectToObject() throws Exception {
    final MockObject expectedMockObject = new MockObject();
    expectedMockObject.setDateValue(DateUtil.getCalendar(2009, Calendar.DECEMBER, 21, 0, 48, 30, 15));
    expectedMockObject.setNumericValue(1);
    expectedMockObject.setStringValue("TEST");

    final MockObject actualMockObject = new MockObject();
    AbstractAction.copyFromObjectToObject(expectedMockObject, actualMockObject);

    assertEquals(expectedMockObject, actualMockObject);
  }

  public static final class MockObject {

    private Integer numericValue;
    private Calendar dateValue;
    private String stringValue;

    public Calendar getDateValue() {
      return dateValue;
    }

    public void setDateValue(final Calendar dateValue) {
      this.dateValue = dateValue;
    }

    public Integer getNumericValue() {
      return numericValue;
    }

    public void setNumericValue(Integer numericValue) {
      this.numericValue = numericValue;
    }

    public String getStringValue() {
      return stringValue;
    }

    public void setStringValue(final String stringValue) {
      this.stringValue = stringValue;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof MockObject)) {
        return false;
      }

      final MockObject that = (MockObject) obj;

      return ObjectUtil.equals(getDateValue(), that.getDateValue())
        && ObjectUtil.equals(getNumericValue(), that.getNumericValue())
        && ObjectUtil.equals(getStringValue(), that.getStringValue());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getDateValue());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getNumericValue());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getStringValue());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{dateValue = ");
      buffer.append(getDateValue());
      buffer.append(", numericValue = ").append(getNumericValue());
      buffer.append(", stringValue = ").append(getStringValue());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
