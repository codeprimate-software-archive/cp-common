/*
 * Enum.java (c) 17 November 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.5.18
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.lang.Identifiable
 * @see java.io.Serializable
 */

package com.cp.common.enums;

import java.io.Serializable;

public interface Enum extends Serializable {

  public Integer getId();

  public String getCode();

  public String getDescription();

  public String getExternalCode();

}
