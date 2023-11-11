/*
 * MockArray.java (c) 7 January 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.9
 */

package com.cp.common.sql;

import com.cp.common.lang.ObjectUtil;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import org.apache.log4j.Logger;

public class MockArray implements Array {

  private static final Logger logger = Logger.getLogger(Array.class);

  private final Object[] array;

  /**
   * Creates an instance of the MockArray class intialized to the specified Object array.
   * @param array an Object array containing the contents of the SQL Array.
   */
  public MockArray(final Object[] array) {
    if (ObjectUtil.isNull(array)) {
      logger.warn("The Java array Object represented by this SQL Array class cannot be null!");
      throw new NullPointerException("The Java array Object represented by this SQL Array class cannot be null!");
    }
    this.array = array;
  }

  /**
   * Retrieves the contents of the SQL ARRAY value designated by this Array object in the form of an array in the
   * Java programming language.
   * @return an array in the Java programming language that contains the ordered elements of the SQL ARRAY value
   * designated by this Array object.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public Object getArray() throws SQLException {
    return array;
  }

  /**
   * Retrieves a slice of the SQL ARRAY value designated by this Array object, beginning with the specified index and
   * containing up to count successive elements of the SQL array. This method uses the type map associated with the
   * connection for customizations of the type mappings.
   * @param index the array index of the first element to retrieve; the first element is at index 1.
   * @param count the number of successive SQL array elements to retrieve.
   * @return an array containing up to count consecutive elements of the SQL array, beginning with element index.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public Object getArray(final long index, final int count) throws SQLException {
    return getArray(index, count, null);
  }

  /**
   * Retreives a slice of the SQL ARRAY value designated by this Array object, beginning with the specified index and
   * containing up to count successive elements of the SQL array.
   *
   * This method uses the specified map for type map customizations unless the base type of the array does not match
   * a user-defined type in map, in which case it uses the standard mapping. This version of the method getArray uses
   * either the given type map or the standard mapping; it never uses the type map associated with the connection.
   * @param index the array index of the first element to retrieve; the first element is at index 1.
   * @param count the number of successive SQL array elements to retrieve.
   * @param map a java.util.Map object that contains SQL type names and the classes in the Java programming language
   * to which they are mapped.
   * @return an array containing up to count consecutive elements of the SQL ARRAY value designated by this Array
   * object, beginning with element index.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public Object getArray(long index, final int count, final Map<String, Class<?>> map) throws SQLException {
    try {
      final Object[] dest = new Object[count];
      System.arraycopy(array, (int) --index, dest, 0, count);
      return dest;
    }
    catch (IndexOutOfBoundsException e) {
      logger.error("An error occurred while accessing the SQL array!", e);
      throw new SQLException("An error occurred while accessing the SQL array!");
    }
  }

  /**
   * Retrieves the contents of the SQL ARRAY value designated by this Array object. This method uses the specified map
   * for type map customizations unless the base type of the array does not match a user-defined type in map, in which
   * case it uses the standard mapping. This version of the method getArray uses either the given type map or the
   * standard mapping; it never uses the type map associated with the connection.
   * @param map a java.util.Map object that contains mappings of SQL type names to classes in the Java programming
   * language.
   * @return an array in the Java programming language that contains the ordered elements of the SQL array designated
   * by this object.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public Object getArray(final Map<String, Class<?>> map) throws SQLException {
    return getArray();
  }

  /**
   * Retrieves the JDBC type of the elements in the array designated by this Array object.
   * @return a constant from the class Types that is the type code for the elements in the array designated by this
   * Array object.
   * @throws SQLException if an error occurs while attempting to access the base type.
   */
  public int getBaseType() throws SQLException {
    return Types.JAVA_OBJECT;
  }

  /**
   * Retrieves the SQL type name of the elements in the array designated by this Array object. If the elements are a
   * built-in type, it returns the database-specific type name of the elements. If the elements are a user-defined type
   * (UDT), this method returns the fully-qualified SQL type name.
   * @return a String that is the database-specific name for a built-in base type; or the fully-qualified SQL type name
   * for a base type that is a UDT.
   * @throws SQLException if an error occurs while attempting to access the type name.
   */
  public String getBaseTypeName() throws SQLException {
    return "JAVA_OBJECT";
  }

  /**
   * Retrieves a result set that contains the elements of the SQL ARRAY value designated by this Array object. If
   * appropriate, the elements of the array are mapped using the connection's type map; otherwise, the standard mapping
   * is used.
   *
   * The result set contains one row for each array element, with two columns in each row. The second column stores the
   * element value; the first column stores the index into the array for that element (with the first array element
   * being at index 1). The rows are in ascending order corresponding to the order of the indices.
   * @return a ResultSet object containing one row for each of the elements in the array designated by this Array
   * object, with the rows in ascending order based on the indices.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public ResultSet getResultSet() throws SQLException {
    return getResultSet(0, array.length);
  }

  /**
   * Retrieves a result set holding the elements of the subarray that starts at index index and contains up to count
   * successive elements. This method uses the connection's type map to map the elements of the array if the map
   * contains an entry for the base type. Otherwise, the standard mapping is used.
   *
   * The result set has one row for each element of the SQL array designated by this object, with the first row
   * containing the element at index index. The result set has up to count rows in ascending order based on the
   * indices. Each row has two columns: The second column stores the element value; the first column stores the index
   * into the array for that element.
   * @param index the array index of the first element to retrieve; the first element is at index 1.
   * @param count the number of successive SQL array elements to retrieve.
   * @return a ResultSet object containing up to count consecutive elements of the SQL array designated by this Array
   * object, starting at index index.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public ResultSet getResultSet(final long index, final int count) throws SQLException {
    return getResultSet(index, count, null);
  }

  /**
   * Retrieves a result set holding the elements of the subarray that starts at index index and contains up to count
   * successive elements. This method uses the specified map for type map customizations unless the base type of the
   * array does not match a user-defined type in map, in which case it uses the standard mapping. This version of the
   * method getResultSet uses either the given type map or the standard mapping; it never uses the type map associated
   * with the connection.
   *
   * The result set has one row for each element of the SQL array designated by this object, with the first row
   * containing the element at index index. The result set has up to count rows in ascending order based on the
   * indices. Each row has two columns: The second column stores the element value; the first column stroes the index
   * into the array for that element.
   * @param index the array index of the first element to retrieve; the first element is at index 1.
   * @param count the number of successive SQL array elements to retrieve.
   * @param map the Map object that contains the mapping of SQL type names to classes in the Java(tm) programming
   * language.
   * @return a ResultSet object containing up to count consecutive elements of the SQL array designated by this Array
   * object, starting at index index.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public ResultSet getResultSet(long index, final int count, final Map<String, Class<?>> map) throws SQLException {
    try {
      final String[] columns = {
        "index",
        "value"
      };

      final Object[][] data = new Object[2][count];

      for (int rowIndex = 0; rowIndex < count; rowIndex++) {
        data[0][rowIndex] = rowIndex + 1;
        data[1][rowIndex] = array[rowIndex];
      }

      return new MockResultSet(columns, data);
    }
    catch (IndexOutOfBoundsException e) {
      logger.error("An error occurred while accessing the SQL array!", e);
      throw new SQLException("An error occurred while accessing the SQL array!");
    }
  }

  /**
   * Retrieves a result set that contains the elements of the SQL ARRAY value designated by this Array object. This
   * method uses the specified map for type map customizations unless the base type of the array does not match a
   * user-defined type in map, in which case it uses the standard mapping. This version of the method getResultSet
   * uses either the given type map or the standard mapping; it never uses the type map associated with the connection.
   *
   * The result set contains one row for each array element, with two columns in each row. The second column stores
   * the element value; the first column stores the index into the array for that element (with the first array element
   * being at index 1). The rows are in ascending order corresponding to the order of the indices.
   * @param map contains the mapping of SQL user-defined types to classes in the Java programming language.
   * @return a ResultSet object containing one row for each of the elements in the array designated by this Array
   * object, with the rows in ascending order based on the indices.
   * @throws SQLException if an error occurs while attempting to access the array.
   */
  public ResultSet getResultSet(final Map<String, Class<?>> map) throws SQLException {
    return getResultSet();
  }

}
