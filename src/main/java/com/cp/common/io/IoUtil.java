/*
 * IoUtil.java (c) 15 February 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see java.io.Closeable
 */

package com.cp.common.io;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.SystemException;
import java.io.Closeable;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class IoUtil {

  private static final Log logger = LogFactory.getLog(IoUtil.class);

  /**
   * Default private constructor to enforce non-instantiability!
   */
  private IoUtil() {
  }

  /**
   * Invokes the close method on the specified Closeable object and wraps the checked IOException
   * in a SystemException instance.
   * @param closeable the Closeable object to invoke the close method call on.
   * @throws SystemException if the close operation of Closeable throws an IOException!
   */
  public static void close(final Closeable closeable) {
    try  {
      if (ObjectUtil.isNotNull(closeable)) {
        closeable.close();
      }
    }
    catch (IOException e) {
      logger.error("Failed to close Closeable!", e);
      throw new SystemException("Failed to close Closeable!", e);
    }
  }

}
