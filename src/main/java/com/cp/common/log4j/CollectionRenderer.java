/*
 * CollectionRenderer.java (c) 22 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.log4j.AbstractObjectRenderer
 */

package com.cp.common.log4j;

import java.util.Collection;
import java.util.Iterator;

public class CollectionRenderer extends AbstractObjectRenderer {

  /**
   * Renderers the specified Object parameter as a Collection.  The String representation of the Object parameter
   * is rendered by this CollectionRenderer if the Object parameter implements Collection.  If Object is not a
   * Collection, then a configured ObjectRenderer for the Object parameter is used to render the Object.  If
   * no such ObjectRenderer for the Object parameter has been configured, then the default ObjectRenderer,
   * as determined by RendererMap.getDefaultRenderer, is used to render the Object.
   *
   * Note, each element of the Collection is rendered according to a configured ObjectRenderer for that element.
   * If no such ObjectRenderer can be found, the default ObjectRenderer, determined by RendererMap.getDefaultRenderer,
   * is used to render the element.
   *
   * @param obj an Object that implements the Collection interface.
   * @return a String representation of the specified Object.
   */
  public String doRender(final Object obj) {
    if (obj instanceof Collection) {
      final Collection collection = (Collection) obj;
      final StringBuffer buffer = new StringBuffer("{");

      for (Iterator it = collection.iterator(); it.hasNext(); ) {
        final Object element = it.next();
        buffer.append(getRenderer(element).doRender(element));
        buffer.append(it.hasNext() ? ", " : "");
      }

      buffer.append("}:").append(collection.getClass().getName());

      return buffer.toString();
    }

    return getRenderer(obj).doRender(obj);
  }

}
