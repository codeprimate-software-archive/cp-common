/*
 * ClassRenderer.java (c) 11 February 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.log4j.AbstractObjectRenderer
 */

package com.cp.common.log4j;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;

public class ClassRenderer extends AbstractObjectRenderer {

  public String doRender(final Object obj) {
    if (ObjectUtil.isNotNull(obj)) {
      return ClassUtil.getClassName(obj);
    }

    return getRenderer(obj).doRender(obj);
  }

}
