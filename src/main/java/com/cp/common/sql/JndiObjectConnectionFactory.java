/*
 * JndiObjectConnectionFactory.java (c) 11 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.6
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.sql.AbstractConnectionFactory
 * @see javax.naming.Context
 * @see javax.naming.InitialContext
 */

package com.cp.common.sql;

import com.cp.common.context.config.Config;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.SystemException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jndi.JndiTemplate;

public class JndiObjectConnectionFactory extends AbstractConnectionFactory {

  public static final String DEFAULT_DATASOURCE_NAME = "jdbc/MyDataSource";
  public static final String DEFAULT_JNDI_NAMESPACE = "java:comp/env/";
  public static final String JNDI_DATASOURCE_NAME_PROPERTY = "cp-common.jndi.datasource.name";
  public static final String JNDI_NAMESPACE_PROPERTY = "cp-common.jndi.namespace";

  private JndiTemplate jndiTemplate;

  /**
   * Creates an instance of the JndiObjectConnectionFactory class initialized with the specified Config object.
   * @param config the Config object used to obtain configuration information.
   */
  public JndiObjectConnectionFactory(final Config config) {
    super(config);
  }

  /**
   * Creates an instance of the JndiObjectConnectionFactory class initialized with the specified Config object
   * and JndiTemplate for performing JNDI Context lookup operations.
   * @param config the Config object used to obtain configuration information.
   * @param jndiTemplate the Spring JndiTemplate object used to perform operations on a JNDI Context.
   */
  public JndiObjectConnectionFactory(final Config config, final JndiTemplate jndiTemplate) {
    super(config);
    setJndiTemplate(jndiTemplate);
  }

  /**
   * Gets the fully qualified resource name in the JNDI context for the specified JNDI name.
   * @param jndiName a String value uniquely referencing a resource in a particular JNDI context.
   * @return a String value indicating the fully qualified resource name in the JNDI context
   * for the specified JNDI name.
   */
  protected String getFullyQualifiedJndiName(String jndiName) {
    Assert.notEmpty(jndiName, "The JNDI name of the resource must be specified!");

    final String jndiNamespace = getConfig().getStringPropertyValue(JNDI_NAMESPACE_PROPERTY, DEFAULT_JNDI_NAMESPACE);

    if (!jndiName.startsWith(jndiNamespace) && !jndiName.contains(":")) {
      jndiName = (jndiNamespace + jndiName);
    }

    return jndiName;
  }

  /**
   * Gets the Spring JndiTemplate object used to perform JNDI operations and specifically look up the DataSource object
   * used to create new Connections.
   * @return the Spring JndiTemplate object for performing JNDI operations.
   */
  protected JndiTemplate getJndiTemplate() {
    Assert.state(ObjectUtil.isNotNull(jndiTemplate), "The JndiTemplate object has not been properly configured for this ConnectionFactory!");
    return jndiTemplate;
  }

  /**
   * Sets the Spring JndiTemplate object used to perform JNDI operations and specifically look up the DataSource object
   * used to create new Connections.
   * @param jndiTemplate the Spring JndiTemplate object for performing JNDI operations.
   */
  public void setJndiTemplate(final JndiTemplate jndiTemplate) {
    Assert.notNull(jndiTemplate, "The JndiTemplate object cannot be null!");
    this.jndiTemplate = jndiTemplate;
  }

  /**
   * Opens a JDBC Connection to the underlying data source.
   * @return a JDBC Connection object to the data source.
   * @throws SystemException if the JDBC Connection cannot be opened!
   * @see ConnectionFactory#closeConnection(java.sql.Connection)
   */
  public Connection openConnection() throws SystemException {
    try {
      final String jndiDataSourceName = getFullyQualifiedJndiName(getConfig().getStringPropertyValue(
        JNDI_DATASOURCE_NAME_PROPERTY, DEFAULT_DATASOURCE_NAME));

      if (logger.isDebugEnabled()) {
        logger.debug("JNDI data source name (" + jndiDataSourceName + ")");
      }

      final DataSource dataSource = (DataSource) getJndiTemplate().lookup(jndiDataSourceName);
      Assert.notNull(dataSource, "The JNDI DataSource object cannot be null!");

      return dataSource.getConnection();
    }
    catch (ClassCastException e) {
      logger.error("The object resource to lookup in the JNDI context must be a DataSource object!", e);
      throw new SystemException("The object resource to lookup in the JNDI context must be a DataSource object!", e);
    }
    catch (NamingException e) {
      logger.error("Failed to lookup data source!", e);
      throw new SystemException("Failed to lookup data source!", e);
    }
    catch (SQLException e) {
      logger.error("Failed to get connection!", e);
      throw new SystemException("Failed to get connection!", e);
    }
  }

}
