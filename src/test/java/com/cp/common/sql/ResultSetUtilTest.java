/*
 * ResultSetUtilTest.java (c) 6 August 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.7
 * @see com.cp.common.sql.ResultSetUtil
 */

package com.cp.common.sql;

import com.cp.common.util.DateUtil;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.RecordTable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class ResultSetUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public ResultSetUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ResultSetUtilTest.class);
    //suite.addTest(new ResultSetUtilTest("testName"));
    return suite;
  }

  protected Map getResultMap(final String[] columnNames, final Object[] columnValues) {
    final Map<String, Object> resultMap = new TreeMap<String, Object>();

    for (int columnIndex = columnNames.length; --columnIndex >= 0; ) {
      resultMap.put(columnNames[columnIndex], columnValues[columnIndex]);
    }

    return resultMap;
  }

  public void testCloseResultSet() throws Exception {
    final String[] columnNames = { "mockColumnName" };
    final Object[][] data = {{ "mockColumnValue" }};
    final MockResultSet mockResultSet = new MockResultSet(columnNames, data);

    assertFalse(mockResultSet.isClosed());

    try {
      ResultSetUtil.closeResultSet(mockResultSet);
    }
    catch (Exception e) {
      fail("Calling close on the MockResultSet object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(mockResultSet.isClosed());
  }

  public void testCloseResultSetThrowsSQLException() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);

    context.checking(new Expectations() {{
      oneOf(mockResultSet).close();
      will(throwException(new SQLException("Failed to close ResultSet!")));
    }});

    try {
      ResultSetUtil.closeResultSet(mockResultSet);
    }
    catch (Exception e) {
      fail("Calling closeResultSet with a ResultSet object throwing a SQLException on close threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseResultSetWithANullResultSetObject() throws Exception {
    try {
      ResultSetUtil.closeResultSet(null);
    }
    catch (Exception e) {
      fail("Calling closeResultSet on a null ResultSet object threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetColumnClass() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      oneOf(mockResultSetMetaData).getColumnClassName(1);
      will(returnValue("java.lang.Integer"));
      oneOf(mockResultSetMetaData).getColumnClassName(2);
      will(returnValue("java.lang.String"));
      oneOf(mockResultSetMetaData).getColumnClassName(3);
      will(returnValue("java.lang.String"));
      oneOf(mockResultSetMetaData).getColumnClassName(4);
      will(returnValue("java.util.Calendar"));
    }});

    assertEquals(Integer.class, ResultSetUtil.getColumnClass(mockResultSet, 1));
    assertEquals(String.class, ResultSetUtil.getColumnClass(mockResultSet, 2));
    assertEquals(String.class, ResultSetUtil.getColumnClass(mockResultSet, 3));
    assertEquals(Calendar.class, ResultSetUtil.getColumnClass(mockResultSet, 4));

    context.assertIsSatisfied();
  }

  public void testGetColumnClassForClassNotFound() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      oneOf(mockResultSetMetaData).getColumnClassName(1);
      will(returnValue("com.mycompany.mypackage.MyClass"));
    }});

    assertEquals(Object.class, ResultSetUtil.getColumnClass(mockResultSet, 1));

    context.assertIsSatisfied();
  }

  public void testGetColumnClassWithNullResultSet() throws Exception {
    Class actualColumnClass = null;

    assertNull(actualColumnClass);

    try {
      actualColumnClass = ResultSetUtil.getColumnClass(null, 1);
      fail("Calling getColumnClass with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnClass with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualColumnClass);
  }

  public void testGetColumnCount() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      allowing(mockResultSetMetaData).getColumnCount();
      will(returnValue(7));
    }});

    assertEquals(7, ResultSetUtil.getColumnCount(mockResultSet));
    assertEquals(7, ResultSetUtil.getColumnCount(mockResultSet));

    context.assertIsSatisfied();
  }

  public void testGetColumnCountWithNullResultSet() throws Exception {
    int columnCount = -1;

    assertEquals(-1, columnCount);

    try {
      columnCount = ResultSetUtil.getColumnCount(null);
      fail("Calling getColumnCount with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnCount with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(-1, columnCount);
  }

  public void testGetColumnIndex() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      atLeast(1).of(mockResultSetMetaData).getColumnCount();
      will(returnValue(3));
      atLeast(1).of(mockResultSetMetaData).getColumnName(1);
      will(returnValue("person_id"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(2);
      will(returnValue("first_name"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(3);
      will(returnValue("last_name"));
    }});

    assertEquals(2, ResultSetUtil.getColumnIndex(mockResultSet, "first_name"));
    assertEquals(1, ResultSetUtil.getColumnIndex(mockResultSet, "person_id"));
    assertEquals(3, ResultSetUtil.getColumnIndex(mockResultSet, "last_name"));

    context.assertIsSatisfied();
  }

  public void testGetColumnIndexWithInvalidColumnName() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      atLeast(1).of(mockResultSetMetaData).getColumnCount();
      will(returnValue(3));
      atLeast(1).of(mockResultSetMetaData).getColumnName(1);
      will(returnValue("person_id"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(2);
      will(returnValue("first_name"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(3);
      will(returnValue("last_name"));
    }});

    int columnIndex = -1;

    assertEquals(-1, columnIndex);

    try {
      columnIndex = ResultSetUtil.getColumnIndex(mockResultSet, "personId");
      fail("Calling getColumnIndex with (personId) should have thrown a SQLException!");
    }
    catch (SQLException e) {
      assertEquals("(personId) is not the name of a column in the ResultSet!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnIndex with (personId) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(-1, columnIndex);

    context.assertIsSatisfied();
  }

  public void testGetColumnIndexWithNullResultSet() throws Exception {
    int columnIndex = -1;

    assertEquals(-1, columnIndex);

    try {
      columnIndex = ResultSetUtil.getColumnIndex(null, "person_id");
      fail("Calling getColumnIndex with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnIndex with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(-1, columnIndex);
  }

  public void testGetColumnName() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      oneOf(mockResultSetMetaData).getColumnName(1);
      will(returnValue("person_id"));
      oneOf(mockResultSetMetaData).getColumnName(2);
      will(returnValue("first_name"));
      oneOf(mockResultSetMetaData).getColumnName(3);
      will(returnValue("last_name"));
    }});

    assertEquals("person_id", ResultSetUtil.getColumnName(mockResultSet, 1));
    assertEquals("first_name", ResultSetUtil.getColumnName(mockResultSet, 2));
    assertEquals("last_name", ResultSetUtil.getColumnName(mockResultSet, 3));

    context.assertIsSatisfied();
  }

  public void testGetColumnNameWithNullResultSet() throws Exception {
    String columnName = null;

    assertNull(columnName);

    try {
      columnName = ResultSetUtil.getColumnName(null, 1);
      fail("Calling getColumnName with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnName with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(columnName);
  }

  public void testGetColumnNames() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      oneOf(mockResultSetMetaData).getColumnCount();
      will(returnValue(3));
      oneOf(mockResultSetMetaData).getColumnName(1);
      will(returnValue("person_id"));
      oneOf(mockResultSetMetaData).getColumnName(2);
      will(returnValue("first_name"));
      oneOf(mockResultSetMetaData).getColumnName(3);
      will(returnValue("last_name"));
    }});

    final List<String> expectedColumnNames = new ArrayList<String>(3);
    expectedColumnNames.add("person_id");
    expectedColumnNames.add("first_name");
    expectedColumnNames.add("last_name");

    final List<String> actualColumnNames = ResultSetUtil.getColumnNames(mockResultSet);

    assertNotNull(actualColumnNames);
    assertEquals(expectedColumnNames, actualColumnNames);

    context.assertIsSatisfied();
  }

  public void testGetColumnNamesWithNullResultSet() throws Exception {
    List<String> columnNames = null;

    assertNull(columnNames);

    try {
      columnNames = ResultSetUtil.getColumnNames(null);
      fail("Calling getColumnNames with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnNames with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(columnNames);
  }

  public void testGetColumnType() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      atLeast(1).of(mockResultSetMetaData).getColumnType(1);
      will(returnValue(Types.NUMERIC));
      atLeast(1).of(mockResultSetMetaData).getColumnType(2);
      will(returnValue(Types.VARCHAR));
      atLeast(1).of(mockResultSetMetaData).getColumnType(3);
      will(returnValue(Types.DATE));
    }});

    assertEquals(Types.VARCHAR, ResultSetUtil.getColumnType(mockResultSet, 2));
    assertEquals(Types.DATE, ResultSetUtil.getColumnType(mockResultSet, 3));
    assertEquals(Types.NUMERIC, ResultSetUtil.getColumnType(mockResultSet, 1));
    assertEquals(Types.VARCHAR, ResultSetUtil.getColumnType(mockResultSet, 2));

    context.assertIsSatisfied();
  }

  public void testGetColumnTypeWithNullResultSet() throws Exception {
    int columnType = -1;

    assertEquals(-1, columnType);

    try {
      columnType = ResultSetUtil.getColumnType(null, 1);
      fail("Calling getColumnType with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumnType with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(-1, columnType);
  }

  public void testGetMapFromResultSetRow() throws Exception {
    final String[] columnNames = {"personId", "firstName", "middleInitial", "lastName", "ssn", "dateOfBirth"};

    final Object[][] data = {
      { 1, "Jon", "D", "Doe", "111-11-1111", DateUtil.getCalendar(1970, Calendar.JUNE, 21) },
      { 2, "Jane", null, "Doe", "222-22-2222", DateUtil.getCalendar(1973, Calendar.MARCH, 12) },
      { 3, "Jack", "R", "Handy", "333-33-3333", DateUtil.getCalendar(1954, Calendar.SEPTEMBER, 2) },
      { 4, "Sandy", "P", "Handy", "444-44-4444", DateUtil.getCalendar(1961, Calendar.OCTOBER, 4) },
      { 5, "Randy", null, "Handy", "555-55-5555", DateUtil.getCalendar(1969, Calendar.FEBRUARY, 18) }
    };

    final ResultSet resultSet = new MockResultSet(columnNames, data);
    int index = 0;

    while (resultSet.next()) {
      assertEquals(getResultMap(columnNames, data[index++]), ResultSetUtil.getMapFromResultSetRow(resultSet));
    }
  }

  public void testGetRecordTableFromResultSet() throws Exception {
    final String[] columnNames =  { "personId", "firstName", "lastName" };

    final Object[][] data = {
      { new Integer[1], "Peter", "Griffin" },
      { new Integer[2], "Lois", "Griffin" },
      { new Integer[3], "Meg", "Griffin" },
      { new Integer[4], "Chris", "Griffin" },
      { new Integer[5], "Stewie", "Griffin" },
      { new Integer[6], "Brian", "Griffin" }
    };

    final ResultSet rs = new MockResultSet(columnNames, data);
    final RecordTable recordTable = ResultSetUtil.getRecordTableFromResultSet(rs);

    assertNotNull(recordTable);
    assertEquals(columnNames.length, recordTable.columnCount());
    assertEquals(data.length, recordTable.rowCount());

    int columnIndex = 0;

    for (Iterator<Column> columnIterator = recordTable.columnIterator(); columnIterator.hasNext(); ) {
      assertEquals(columnNames[columnIndex++], columnIterator.next().getName());
    }

    int columnCount;

    for (int rowIndex = 0, rowCount = recordTable.rowCount(); rowIndex < rowCount; rowIndex++) {
      for (columnIndex = 0, columnCount = recordTable.columnCount(); columnIndex < columnCount; columnIndex++) {
        assertEquals(data[rowIndex][columnIndex], recordTable.getCellValue(rowIndex, columnIndex));
      }
    }
  }

  public void testGetResultSetValueWithColumnIndex() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);
    final Object expectedResultSetValue = new Timestamp(DateUtil.getCalendar(2008, Calendar.DECEMBER, 7).getTimeInMillis());

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      atLeast(1).of(mockResultSetMetaData).getColumnType(1);
      will(returnValue(Types.TIMESTAMP));
      atLeast(1).of(mockResultSet).getTimestamp(1);
      will(returnValue(expectedResultSetValue));
    }});

    final Object actualResultSetValue = ResultSetUtil.getResultSetValue(mockResultSet, 1);

    assertNotNull(actualResultSetValue);
    assertEquals(expectedResultSetValue, actualResultSetValue);

    context.assertIsSatisfied();
  }

  public void testGetResultSetValueWithColumnName() throws Exception {
    final ResultSet mockResultSet = context.mock(ResultSet.class);
    final ResultSetMetaData mockResultSetMetaData = context.mock(ResultSetMetaData.class);

    context.checking(new Expectations() {{
      allowing(mockResultSet).getMetaData();
      will(returnValue(mockResultSetMetaData));
      atLeast(1).of(mockResultSetMetaData).getColumnCount();
      will(returnValue(3));
      atLeast(1).of(mockResultSetMetaData).getColumnName(1);
      will(returnValue("person_id"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(2);
      will(returnValue("first_name"));
      atLeast(1).of(mockResultSetMetaData).getColumnName(3);
      will(returnValue("last_name"));
      atLeast(1).of(mockResultSetMetaData).getColumnType(1);
      will(returnValue(Types.INTEGER));
      atLeast(1).of(mockResultSetMetaData).getColumnType(2);
      will(returnValue(Types.VARCHAR));
      atLeast(1).of(mockResultSetMetaData).getColumnType(3);
      will(returnValue(Types.VARCHAR));
      atLeast(1).of(mockResultSet).getInt(1);
      will(returnValue(1));
      atLeast(1).of(mockResultSet).getString(2);
      will(returnValue("jon"));
      atLeast(1).of(mockResultSet).getString(3);
      will(returnValue("bloom"));
    }});

    assertEquals("jon", ResultSetUtil.getResultSetValue(mockResultSet, "first_name"));
    assertEquals("bloom", ResultSetUtil.getResultSetValue(mockResultSet, "last_name"));
    assertEquals(new Integer(1), ResultSetUtil.getResultSetValue(mockResultSet, "person_id"));

    context.assertIsSatisfied();
  }

  public void testGetResultSetValueWithNullResultSet() throws Exception {
    Object resultSetValue = null;

    assertNull(resultSetValue);

    try {
      resultSetValue = ResultSetUtil.getResultSetValue(null, "person_id");
      fail("Calling getResultSetValue with a null ResultSet object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The result set cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getResultSetValue with a null ResultSet object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(resultSetValue);
  }

}
