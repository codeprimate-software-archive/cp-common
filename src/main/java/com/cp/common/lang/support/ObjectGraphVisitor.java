/*
 * DateUtil.java (c) 2 December 2007
 *
 * This class contains date functions and operations for Calendar and Date objects.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Identifiable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import java.util.HashMap;
import java.util.Map;

public class ObjectGraphVisitor implements Visitor {

  private final Map<Class, Map<Integer, Object>> objectGraph = new HashMap<Class, Map<Integer, Object>>();

  /**
   * Gets the object graph after traversing the object hierarchy.
   * @return a Map object containing the graph of objects in the object hierarchy.
   */
  public Map<Class, Map<Integer, Object>> getObjectGraph() {
    return objectGraph;
  }

  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof Identifiable) {
      final Identifiable<Integer> beanObject = (Identifiable<Integer>) visitableObject;

      Map<Integer, Object> objectSubGraph = getObjectGraph().get(beanObject.getClass());

      if (objectSubGraph == null) {
        objectSubGraph = new HashMap<Integer, Object>();
        getObjectGraph().put(beanObject.getClass(), objectSubGraph);
      }

      objectSubGraph.put(beanObject.getId(), beanObject);
    }
  }

}
