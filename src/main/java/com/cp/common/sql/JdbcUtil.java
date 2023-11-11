/*
 * JdbcUtil.java (c) 4 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.6
 * @see java.sql.Connection
 * @see java.sql.ResultSet
 * @see java.sql.Statement
 */

package com.cp.common.sql;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class JdbcUtil {

  private static final Log logger = LogFactory.getLog(JdbcUtil.class);

  /**
   * Default private constructor to enforce non-instantiability.
   */
  private JdbcUtil() {
  }

  public static void cancelStatement(final Statement statement) {
    if (ObjectUtil.isNotNull(statement)) {
      try {
        statement.cancel();
      }
      catch (SQLException e) {
        throw new SystemException("Failed to cancel running SQL statement!", e);
      }
    }
  }

  public static void closeConnection(final Connection connection) {
    if (ObjectUtil.isNotNull(connection)) {
      try {
        connection.close();
      }
      catch (SQLException e) {
        logger.error("Failed to close JDBC connection!", e);
      }
    }
  }

  public static void closeResultSet(final ResultSet rs) {
    ResultSetUtil.closeResultSet(rs);
  }

  public static void closeStatement(final Statement statement) {
    if (ObjectUtil.isNotNull(statement)) {
      try {
        statement.close();
      }
      catch (SQLException e) {
        logger.error("Failed to close JDBC statement!", e);
      }
    }
  }

  public static void commitTransaction(final Connection connection) {
    if (ObjectUtil.isNotNull(connection)) {
      try {
        connection.commit();
      }
      catch (SQLException e) {
        throw new SystemException("Failed to commit pending transaction!", e);
      }
    }
  }

  public static void loadJdbcDriver(final String jdbcDriverClassName) {
    Assert.notEmpty(jdbcDriverClassName, "The JDBC driver class name must be specified!");

    try {
      Class.forName(jdbcDriverClassName);
    }
    catch (ClassNotFoundException e) {
      logger.error("Failed to load JDBC driver (" + jdbcDriverClassName + ")!", e);
      throw new SystemException("Failed to load JDBC driver (" + jdbcDriverClassName + ")!", e);
    }
  }

  public static void rollbackTransaction(final Connection connection) {
    if (ObjectUtil.isNotNull(connection)) {
      try {
        connection.rollback();
      }
      catch (SQLException e) {
        throw new SystemException("Failed to rollback pending transaction!", e);
      }
    }
  }

}
