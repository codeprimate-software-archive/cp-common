/*
 * CalendarRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.11
 * @see com.cp.common.log4j.CalendarRenderer
 */

package com.cp.common.log4j;

import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class CalendarRendererTest extends TestCase {

  public CalendarRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CalendarRendererTest.class);
    //suite.addTest(new CalendarRendererTest("testName"));
    return suite;
  }

  public void testDoRender() throws Exception {
    CalendarRenderer calendarRenderer = new CalendarRenderer();

    assertEquals("MM/dd/yyyy hh:mm:ss a", calendarRenderer.getDateFormatPattern());
    assertEquals("10/25/2006 09:22:40 PM", calendarRenderer.doRender(DateUtil.getCalendar(
      2006, Calendar.OCTOBER, 25, 21, 22, 40, 0)));
    assertEquals("10/25/2006 09:22:40 AM", calendarRenderer.doRender(DateUtil.getCalendar(
      2006, Calendar.OCTOBER, 25, 9, 22, 40, 0)));

    calendarRenderer = new CalendarRenderer("dd MMMMM yy HH:mm");

    assertEquals("dd MMMMM yy HH:mm", calendarRenderer.getDateFormatPattern());
    assertEquals("25 October 06 21:22", calendarRenderer.doRender(DateUtil.getCalendar(
      2006, Calendar.OCTOBER, 25, 21, 22, 40, 0)));
    assertEquals("25 October 06 09:22", calendarRenderer.doRender(DateUtil.getCalendar(
      2006, Calendar.OCTOBER, 25, 9, 22, 40, 0)));
  }

  public void testNonCalendarRendering() throws Exception {
    final ObjectRenderer calendarRenderer = new CalendarRenderer();

    assertEquals("TEST", calendarRenderer.doRender("TEST"));
    assertEquals("null", calendarRenderer.doRender(null));
  }

}
