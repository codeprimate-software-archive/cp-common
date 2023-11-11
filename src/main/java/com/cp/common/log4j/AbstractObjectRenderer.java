/*
 * AbstractObjectRenderer.java (c) 22 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.10
 * @see com.cp.common.log4j.NullRenderer
 * @see org.apache.log4j.or.ObjectRenderer
 */

package com.cp.common.log4j;

import com.cp.common.lang.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;

public abstract class AbstractObjectRenderer implements ObjectRenderer {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Attempts to obtain the configured ObjectRenderer for the specified Object parameter.  If the
   * ObjectRenderer for the Object cannot be determined, then a default ObjectRenderer, as determined
   * by the RendererMap.getDefaultRenderer, is returned.
   * @param obj the Object to determined the ObjectRenderer for.
   * @return an ObjectRenderer that will render the specified Object.
   */
  public static ObjectRenderer getRenderer(final Object obj) {
    if (ObjectUtil.isNotNull(obj)) {
      final LoggerRepository loggerRepository = Logger.getRootLogger().getLoggerRepository();
      if (loggerRepository instanceof RendererSupport) {
        return ((RendererSupport) loggerRepository).getRendererMap().get(obj);
      }
      else {
        return (new RendererMap()).getDefaultRenderer();
      }
    }
    else {
      return NullRenderer.INSTANCE;
    }
  }

}
