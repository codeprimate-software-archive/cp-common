/*
 * DataSourceAdaptorTest.java (c) 6 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.6
 * @see com.cp.common.sql.DataSourceAdapter
 * @see junit.framework.TestCase
 */

package com.cp.common.sql;

import java.io.PrintWriter;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class DataSourceAdaptorTest extends TestCase {

  private final Mockery context = new Mockery();

  public DataSourceAdaptorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DataSourceAdaptorTest.class);
    //suite.addTest(new DataSourceAdaptorTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);
    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    assertNotNull(dataSource);
    assertSame(mockDataSource, dataSource.getDataSource());
  }

  public void testGetDataSourceUninitialized() throws Exception {
    final DataSourceAdaptor dataSource = new DataSourceAdaptor();

    try {
      dataSource.getDataSource();
      fail("Calling getDataSource on the DataSourceAdaptor unitialized should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The data source was not properly initilized on the data source adaptor!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDataSource on the DataSourceAdaptor unitialized threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testSetDataSourceWithNullValue() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);
    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    assertEquals(mockDataSource, dataSource.getDataSource());

    try {
      dataSource.setDataSource(null);
      fail("Calling setDataSource with a null DataSource object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The backing data source used by the data source adaptor cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setDataSource with a null DataSource object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertEquals(mockDataSource, dataSource.getDataSource());
  }

  public void testGetConnection() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).getConnection();
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    dataSource.getConnection();

    context.assertIsSatisfied();
  }

  public void testGetConnectionWithUsernameAndPassword() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).getConnection(with(equal("dbadmin")), with(equal("test")));
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    dataSource.getConnection("dbadmin", "test");

    context.assertIsSatisfied();
  }

  public void testGetLoginTimeout() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).getLoginTimeout();
      will(returnValue(30));
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    assertEquals(30, dataSource.getLoginTimeout());

    context.assertIsSatisfied();
  }

  public void testSetLoginTimeout() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).setLoginTimeout(with(equal(60)));
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    dataSource.setLoginTimeout(60);

    context.assertIsSatisfied();
  }

  public void testGetLogWriter() throws Exception {
    final PrintWriter mockWriter = new PrintWriter(System.out);
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).getLogWriter();
      will(returnValue(mockWriter));
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    assertEquals(mockWriter, dataSource.getLogWriter());

    context.assertIsSatisfied();
  }

  public void testSetLogWriter() throws Exception {
    final PrintWriter mockWriter = new PrintWriter(System.out);
    final DataSource mockDataSource = context.mock(DataSource.class);

    context.checking(new Expectations() {{
      oneOf(mockDataSource).setLogWriter(with(same(mockWriter)));
    }});

    final DataSourceAdaptor dataSource = new DataSourceAdaptor(mockDataSource);

    dataSource.setLogWriter(mockWriter);

    context.assertIsSatisfied();
  }

}
