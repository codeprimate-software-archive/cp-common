/*
 * BeanBuilderVisitor.java (c) 23 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.23
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.beans.factory.support;

import com.cp.common.beans.Bean;
import com.cp.common.util.Visitor;

public interface BeanBuilderVisitor extends Visitor {

  public <T extends Bean> T getBean();

}
