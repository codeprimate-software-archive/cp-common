/*
 * LoggingPropertyChangeListenerTest.java (c) 16 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.24
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.LoggingPropertyChangeListener
 */

package com.cp.common.beans.event;

import java.io.StringWriter;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

public class LoggingPropertyChangeListenerTest extends CommonEventTestCase {

  private static final StringWriter writer = new StringWriter();

  public LoggingPropertyChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LoggingPropertyChangeListenerTest.class);
    //suite.addTest(new LoggingPropertyChangeListenerTest("testName"));
    return new LoggingPropertyChangeListenerTestSetup(suite);
  }

  private static void flush(final StringWriter writer) {
    final StringBuffer buffer = writer.getBuffer();
    buffer.delete(0, buffer.length());
  }

  private String getLogOutput(final Class clazz,
                              final String propertyName,
                              final Object oldValue,
                              final Object newValue) {
    final StringBuffer buffer = new StringBuffer("INFO - property (");
    buffer.append(propertyName);
    buffer.append(") of bean (");
    buffer.append(clazz.getName());
    buffer.append(") changed from (");
    buffer.append(oldValue);
    buffer.append(") to (");
    buffer.append(newValue);
    buffer.append(")");
    return buffer.toString();
  }

  public void testLoggingPropertyChangeListener() throws Exception {
    Person person = new PersonImpl();

    Logger.getLogger(PersonImpl.class).setLevel(Level.WARN);
    flush(writer);
    person.setFirstName("Jon");
    person.setLastName("Bloom");

    assertEquals("", writer.toString().trim());

    Logger.getLogger(PersonImpl.class).setLevel(Level.INFO);
    flush(writer);
    person.setFirstName("John");

    assertEquals(getLogOutput(PersonImpl.class, "firstName", "Jon", "John"), writer.toString().trim());
  }

  private static final class LoggingPropertyChangeListenerTestSetup extends TestSetup {

    public LoggingPropertyChangeListenerTestSetup(final Test test) {
      super(test);
    }

    protected void setUp() throws Exception {
      final Logger logger = Logger.getLogger(PersonImpl.class);
      final WriterAppender appender = (WriterAppender) logger.getAppender("InMemory");
      appender.setWriter(writer);
    }

    protected void tearDown() throws Exception {
      flush(writer);
      Logger.getLogger(PersonImpl.class).setLevel(Level.OFF);
    }
  }

}
