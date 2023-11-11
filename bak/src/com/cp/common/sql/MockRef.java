/*
 * MockRef.java (c) 10 January 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.11
 */

package com.cp.common.sql;

import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;
import org.apache.log4j.Logger;

public class MockRef implements Ref {

  private static final Logger logger = Logger.getLogger(MockRef.class);

  private Object ref;

  /**
   * Creates an instance of the MockRef class referencing the specified Object.
   * @param ref the Object represented by this SQL REF object.
   */
  public MockRef(final Object ref) {
    this.ref = ref;
  }

  /**
   * Retrieves the fully-qualified SQL name of the SQL structured type that this Ref object references.
   * @return the fully-qualified SQL name of the referenced SQL structured type.
   * @throws SQLException if a database access error occurs.
   */
  public String getBaseTypeName() throws SQLException {
    return "JAVA_OBJECT";
  }

  /**
   * Retrieves the SQL structured type instance referenced by this Ref object. If the connection's type map has an
   * entry for the structured type, the instance will be custom mapped to the Java class indicated in the type
   * map. Otherwise, the structured type instance will be mapped to a Struct object.
   * @return a Java Object that is the mapping for the SQL structured type to which this Ref object refers.
   * @throws SQLException if a database access error occurs.
   */
  public Object getObject() throws SQLException {
    return ref;
  }

  /**
   * Retrieves the referenced object and maps it to a Java type using the given type map.
   * @param map a java.util.Map object that contains the mapping to use (the fully-qualified name of the SQL structured
   * type being referenced and the class object for SQLData implementation to which the SQL structured type will be
   * mapped).
   * @return a Java Object that is the custom mapping for the SQL structured type to which this Ref object refers.
   * @throws SQLException if a database access error occurs.
   */
  public Object getObject(final Map<String, Class<?>> map) throws SQLException {
    return ref;
  }

  /**
   * Sets the structured type value that this Ref object references to the given instance of Object. The driver
   * converts this to an SQL structured type when it sends it to the database.
   * @param value an Object representing the SQL structured type instance that this Ref object will reference.
   * @throws SQLException if a database access error occurs.
   */
  public void setObject(final Object value) throws SQLException {
    this.ref = value;
  }

}
