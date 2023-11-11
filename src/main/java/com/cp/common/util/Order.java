/*
 * Order.java (c) 4 May 2009 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.7
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;

public enum Order {
  ASCENDING(1, "asc", "Ascending"),
  DESCENDING(-1, "desc", "Descending");

  private final int orderId;

  private final String code;
  private final String description;

  /**
   * Creates an instance of the Order Enum initialized with the order identifier, the order code
   * and a description of this order.
   * @param orderId the unique identifier of this Order enumerated constant.
   * @param code a String value code for this Order enumerated constant.
   * @param description a String value describing this Order enumerated constant.
   */
  Order(final int orderId, final String code, final String description) {
    this.orderId = orderId;
    this.code = code;
    this.description = description;
  }

  /**
   * Looks up a Order enumerated type value by code.
   * @param code a String value specifying the Order enumerated type value's code.
   * @return an Order enumerated type value by code.
   */
  public static Order getOrderByCode(final String code) {
    for (final Order order : values()) {
      if (ObjectUtil.equals(order.getCode(), code)) {
        return order;
      }
    }

    return null;
  }

  /**
   * Looks up a Order enumerated type value by description.
   * @param description a String value specifying the Order enumerated type value's description.
   * @return an Order enumerated type value by description.
   */
  public static Order getOrderByDescription(final String description) {
    for (final Order order : values()) {
      if (ObjectUtil.equals(order.getDescription(), description)) {
        return order;
      }
    }

    return null;
  }

  /**
   * Looks up a Order enumerated type value by ID.
   * @param id an integer value specifying the Order enumerated type value's identifier.
   * @return an Order enumerated type value by ID.
   */
  public static Order getOrderById(final int id) {
    for (final Order order : values()) {
      if (ObjectUtil.equals(order.getOrderId(), id)) {
        return order;
      }
    }

    return null;
  }

  /**
   * Gets a String value indicating the code for the Order enumerated constant.
   * @return a String value indicating the code for this Order enumerated constant.
   */
  public String getCode() {
    return code;
  }

  /**
   * Gets a String value describing this Order enumerated constant.
   * @return a String value describing this Order enumerated constant.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets an integer value uniquely identifying the Order enumerated constant.
   * @return an integer value uniquely identifying the Order enumerated constant.
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * Gets a String value describing the internal state of this Order enumerated constant.
   * @return a String value describing the internal state of this Order enumerated constant.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{code = ");
    buffer.append(getOrderId());
    buffer.append(", description = ").append(getDescription());
    buffer.append(", orderId = ").append(getOrderId());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
