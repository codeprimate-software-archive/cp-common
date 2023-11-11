/*
 * Echo.java (c) 28 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.6.28
 */

package com.codeprimate.tools.bin;

import com.cp.common.lang.Assert;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ArrayUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Echo {

  private static final Log logger = LogFactory.getLog(Echo.class);

  private static final String SYSTEM_PROPERTY_NOTATION = "*";

  public static void doEcho(String value) {
    value = StringUtil.trim(value);

    Assert.notEmpty(value, "The String value to echo cannot be empty!");

    if (value.startsWith(SYSTEM_PROPERTY_NOTATION)) {
      value = System.getProperty(value.substring(1));
    }

    logger.info(value);
    System.out.println(value);
  }

  public static void main(final String... args) {
    if (ArrayUtil.isEmpty(args)) {
      System.err.println("> java com.codeprimate.tools.bin.Echo {$<system property> | $<java system property> | string}*");
      System.exit(-1);
    }

    for (String arg : args) {
      doEcho(arg);
    }
  }

}
