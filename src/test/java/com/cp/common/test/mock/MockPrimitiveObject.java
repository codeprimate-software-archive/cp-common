/*
 * MockPrimitiveObject.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.16
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.test.mock.MockValueObject
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Identifiable;

public interface MockPrimitiveObject extends Identifiable<Long> {

  public Boolean getBooleanProperty();

  public void setBooleanProperty(Boolean booleanProperty);

  public Character getCharacterProperty();

  public void setCharacterProperty(Character characterProperty);

  public Double getDoubleProperty();

  public void setDoubleProperty(Double doubleProperty);

  public Integer getIntegerProperty();

  public void setIntegerProperty(Integer integerProperty);

  public String getStringProperty();

  public void setStringProperty(String stringProperty);

}
