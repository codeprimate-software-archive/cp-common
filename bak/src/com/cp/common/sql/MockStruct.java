/*
 * MockStruct.java (c) 11 January 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.11
 */

package com.cp.common.sql;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import org.apache.log4j.Logger;

public class MockStruct implements Struct {

  private static final Logger logger = Logger.getLogger(MockStruct.class);

  private final Object[] attributes;

  /**
   * Creates an instance of the MockStruct class to represent the ordered attributes of a SQL STRUCT.
   * @param values the ordered attribute values of a SQL STRUCT.
   */
  public MockStruct(final Object... values) {
    this.attributes = values;
  }

  /**
   * Produces the ordered values of the attributes of the SQL structurec type that this Struct object represents.
   * This method uses the type map associated with the connection for customizations of the type mappings. If
   * there is no entry in the connection's type map that matches the structured type that this Struct object
   * represents, the driver uses the standard mapping.
   *
   * Conceptually, this method calls the method getObject on each attribute of the structured type and returns a
   * Java array containing the result.
   * @return an array containing the ordered attribute values.
   * @throws SQLException if a database access error occurs.
   */
  public Object[] getAttributes() throws SQLException {
    return attributes;
  }

  /**
   * Produces the ordered values of the attributes of the SQL structurec type that this Struct object represents.
   * This method uses the given type map for customizations of the type mappings. If there is no entry in the
   * given type map that matches the structured type that this Struct object represents, the driver uses the
   * standard mapping. This method never uses the type map associated with the connection.
   *
   * Conceptually, this method calls the method getObject on each attribute of the structured type and returns a
   * Java array containing the result.
   * @param map a mapping of SQL type names to Java classes.
   * @return an array containing the ordered attribute values.
   * @throws SQLException if a database access error occurs.
   */
  public Object[] getAttributes(final Map<String, Class<?>> map) throws SQLException {
    return attributes;
  }

  /**
   * Retrieves the SQL type name of the SQL structured type that this Struct object represents.
   * @return the fully-qualified type name of the SQL structured type for which this Struct object is the generic
   * representation.
   * @throws SQLException if a database access error occurs.
   */
  public String getSQLTypeName() throws SQLException {
    return "JAVA_OBJECT";
  }

}
