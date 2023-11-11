/*
 * DefaultRenderer.java (c) 10 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.10
 */

package com.cp.common.log4j;

import com.cp.common.lang.ObjectUtil;
import org.apache.log4j.or.ObjectRenderer;

public final class DefaultRenderer extends AbstractObjectRenderer {

  public static final ObjectRenderer INSTANCE = new DefaultRenderer();

  public String doRender(final Object obj) {
    return ObjectUtil.toString(obj);
  }

}
