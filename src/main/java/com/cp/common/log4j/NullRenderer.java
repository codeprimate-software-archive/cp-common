/*
 * NullRenderer.java (c) 24 April 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.10
 * @see com.cp.common.log4j.AbstractRenderer
 */

package com.cp.common.log4j;

import org.apache.log4j.or.ObjectRenderer;

public final class NullRenderer extends AbstractObjectRenderer {

  public static final ObjectRenderer INSTANCE = new NullRenderer();

  public String doRender(final Object obj) {
    return "null";
  }

}
