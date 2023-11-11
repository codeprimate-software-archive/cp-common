/*
 * MapRenderer.java (c) 22 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.log4j.AbstractRenderer
 */

package com.cp.common.log4j;

import java.util.Iterator;
import java.util.Map;

public class MapRenderer extends AbstractObjectRenderer {

  /**
   * Renderers the specified Object parameter as a Map.  The String representation of the Object parameter
   * is rendered by this MapRenderer if the Object parameter implements Map.  If Object is not a Map, then a
   * configured ObjectRenderer for the Object parameter is used to render the Object.  If no such ObjectRenderer
   * for the Object parameter has been configured, then the default ObjectRenderer, as determined by
   * RendererMap.getDefaultRenderer, is used to render the Object.
   *
   * Note, each key-value pair of the Map is rendered according to a configured ObjectRenderer for the value.
   * If no such ObjectRenderer can be found, the default ObjectRenderer, determined by RendererMap.getDefaultRenderer,
   * is used to render the value.
   *
   * @param obj an Object that implements the Map interface.
   * @return a String representation of the specified Object.
   */
  public String doRender(final Object obj) {
    if (obj instanceof Map) {
      final Map map = (Map) obj;
      final StringBuffer buffer = new StringBuffer("[");

      for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
        final Object key = it.next();
        final Object value = map.get(key);

        buffer.append("{key = ").append(getRenderer(key).doRender(key));
        buffer.append(", value = ").append(getRenderer(value).doRender(value));
        buffer.append("}").append(it.hasNext() ? ", " : "");
      }

      buffer.append("]:").append(map.getClass().getName());

      return buffer.toString();
    }

    return getRenderer(obj).doRender(obj);
  }

}
