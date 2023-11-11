/*
 * LoggingConfigurer.java (c) 10 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.23
 */

package com.cp.common.log4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LoggingConfigurer {

  private static boolean configured = false;

  private static final String LOG4J_PROPERTIES = "log4j.properties";
  private static final String LOG_CONFIG_FILE = "log4j.lcf";
  private static final String LCF_JAR_FILE_PATH = "/etc/config/";
  private static final String LCF_FILE_SYSTEM_PATH = System.getProperty("lcf.filesystem.path");

  private LoggingConfigurer() {
  }

  public synchronized static void configure() {
    if (!isConfigured()) {
      final Properties props = loadLogConfigFromFileSystem(loadLogConfigFromJARFile(null));

      if (props != null) {
        PropertyConfigurator.configure(props);
      }
      else {
        BasicConfigurator.configure();
      }

      setConfigured(true);
      // cause the commons-logging LogFactory to be loaded by this class loader
      LogFactory.getFactory();
      Logger.getLogger(LoggingConfigurer.class).info("Unit Test Logger Configured!");
    }
  }

  private static boolean isConfigured() {
    return configured;
  }

  private static void setConfigured(final boolean configured) {
    LoggingConfigurer.configured = configured;
  }

  private static Properties loadLogConfigFromFileSystem(Properties props) {
    if (props == null) {
      final StringBuffer path = new StringBuffer();
      path.append(LCF_FILE_SYSTEM_PATH);
      path.append(File.separator);
      path.append(LOG4J_PROPERTIES);

      final File logConfigFile = new File(path.toString());

      if (logConfigFile.exists()) {
        System.out.println("Found Log Config File in File System (" + logConfigFile + ")");
        try {
          props = new Properties();
          props.load(new BufferedInputStream(new FileInputStream(logConfigFile)));
          return props;
        }
        catch (Exception e) {
          System.err.println("Error loading log config file ( " + logConfigFile.getName() + "): " + e.getMessage());
          props = null;
        }
      }
      else {
        System.err.println("No Log Config File (LCF) in File System (" + logConfigFile + ")!");
      }
    }

    return props;
  }

  private static Properties loadLogConfigFromJARFile(Properties props) {
    if (props == null) {
      try {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(LCF_JAR_FILE_PATH);
        buffer.append(LOG4J_PROPERTIES);
        props = new Properties();
        props.load(LoggingConfigurer.class.getResourceAsStream(buffer.toString()));
        System.out.println("Found Log Config File in JAR file (" + buffer.toString() + ")");
        return props;
      }
      catch (Exception ignore) {
        System.err.println("No Log Config File (LCF) in JAR File!");
        props = null;
      }
    }

    return props;
  }

}
