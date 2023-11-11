/*
 * MockBean.java (c) 18 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.14
 * @see com.cp.common.beans.Bean
 */

package com.cp.common.test.mock;

import com.cp.common.beans.Bean;

public interface MockBean extends Bean<Integer> {

  public Object getValue();

  public void setValue(Object value);

}
