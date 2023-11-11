/*
 * EnumRenderer.java (c) 17 November 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.11.17
 * @see com.cp.common.log4j.AbstractObjectRenderer
 */

package com.cp.common.log4j;

public class EnumRenderer extends AbstractObjectRenderer {

  public String doRender(final Object obj) {
    if (obj instanceof com.cp.common.enums.Enum) {
      final com.cp.common.enums.Enum enumObj = (com.cp.common.enums.Enum) obj;
      return enumObj.getDescription();
    }

    return getRenderer(obj).doRender(obj);
  }

}
