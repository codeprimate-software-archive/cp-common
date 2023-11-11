/*
 * IdentifiableVisitor.java (c) 14 June 2007
 *
 * The IdentifiableVisitor class walks the object graph, starting with the object who's accept method is called
 * with an instance of this Visitor, in search of objects implementing the Identifiable interface.  All Identifiable
 * objects visited in the object graph will have their ID property set to null.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.13
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Identifiable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IdentifiableVisitor implements Visitor {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Visits all Identifiable objects in an object graph resetting the Id property to null.
   * @param obj the Identifiable object who's ID will be set to null.
   */
  public void visit(final Visitable obj) {
    if (obj instanceof Identifiable) {
      ((Identifiable) obj).setId(null);
    }
  }

}
