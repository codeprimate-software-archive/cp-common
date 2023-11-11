/*
 * ProcessRenderer.java (c) 11 February 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.11
 * @see com.cp.common.beans.Process
 * @see com.cp.common.log4j.AbstractObjectRenderer
 */

package com.cp.common.log4j;

import com.cp.common.beans.Process;

public class ProcessRenderer extends AbstractObjectRenderer {

  public String doRender(final Object obj) {
    if (obj instanceof Process) {
      return ((Process) obj).getProcessName();
    }

    return getRenderer(obj).doRender(obj);
  }

}
