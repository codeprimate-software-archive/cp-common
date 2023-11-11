/*
 * UserRenderer.java (c) 23 April 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.11
 * @see com.cp.common.beans.User
 * @see com.cp.common.log4j.AbstractObjectRenderer
 */

package com.cp.common.log4j;

import com.cp.common.beans.User;

public class UserRenderer extends AbstractObjectRenderer {

  public String doRender(final Object obj) {
    if (obj instanceof User) {
      return ((User) obj).getUsername();
    }

    return getRenderer(obj).doRender(obj);
  }

}
