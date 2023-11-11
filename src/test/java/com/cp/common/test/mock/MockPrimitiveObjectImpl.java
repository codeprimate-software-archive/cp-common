/*
 * MockPrimitiveObjectImpl.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.9
 * @see com.cp.common.test.mock.MockPrimitiveObject
 */

package com.cp.common.test.mock;

import com.cp.common.lang.ObjectUtil;

public class MockPrimitiveObjectImpl implements MockPrimitiveObject {

  private Long id;

  private Boolean booleanProperty;
  private Character characterProperty;
  private Double doubleProperty;
  private Integer integerProperty;
  private String stringProperty;

  public MockPrimitiveObjectImpl() {
  }

  public MockPrimitiveObjectImpl(final Long id) {
    setId(id);
  }

  public MockPrimitiveObjectImpl(final MockPrimitiveObject primitiveObject) {
    setBooleanProperty(primitiveObject.getBooleanProperty());
    setCharacterProperty(primitiveObject.getCharacterProperty());
    setDoubleProperty(primitiveObject.getDoubleProperty());
    setIntegerProperty(primitiveObject.getIntegerProperty());
    setStringProperty(primitiveObject.getStringProperty());
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Boolean getBooleanProperty() {
    return booleanProperty;
  }

  public void setBooleanProperty(final Boolean booleanProperty) {
    this.booleanProperty = booleanProperty;
  }

  public Character getCharacterProperty() {
    return characterProperty;
  }

  public void setCharacterProperty(final Character characterProperty) {
    this.characterProperty = characterProperty;
  }

  public Double getDoubleProperty() {
    return doubleProperty;
  }

  public void setDoubleProperty(final Double doubleProperty) {
    this.doubleProperty = doubleProperty;
  }

  public Integer getIntegerProperty() {
    return integerProperty;
  }

  public void setIntegerProperty(final Integer integerProperty) {
    this.integerProperty = integerProperty;
  }

  public String getStringProperty() {
    return stringProperty;
  }

  public void setStringProperty(final String stringProperty) {
    this.stringProperty = stringProperty;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof MockPrimitiveObject)) {
      return false;
    }

    final MockPrimitiveObject that = (MockPrimitiveObject) obj;

    return ObjectUtil.equals(getId(), that.getId())
      && ObjectUtil.equals(getBooleanProperty(), that.getBooleanProperty())
      && ObjectUtil.equals(getCharacterProperty(), that.getCharacterProperty())
      && ObjectUtil.equals(getDoubleProperty(), that.getDoubleProperty())
      && ObjectUtil.equals(getIntegerProperty(), that.getIntegerProperty())
      && ObjectUtil.equals(getStringProperty(), that.getStringProperty());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getBooleanProperty());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getCharacterProperty());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDoubleProperty());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getIntegerProperty());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getStringProperty());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("id = ");
    buffer.append(getId());
    buffer.append(", booleanProperty = ").append(getBooleanProperty());
    buffer.append(", characterProperty = ").append(getCharacterProperty());
    buffer.append(", doubleProperty = ").append(getDoubleProperty());
    buffer.append(", integerProperty = ").append(getIntegerProperty());
    buffer.append(", stringProperty = ").append(getStringProperty());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
