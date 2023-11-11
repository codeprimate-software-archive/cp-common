/*
 * AccessType.java (c) 7 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.3
 * @see com.cp.common.enums.AbstractEnum
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.util.HashSet;
import java.util.Set;

public final class AccessType {

  public static final String ACCESS_DENIED_DESCRIPTION = "No Access"; // 000
  public static final String EXECUTE_DESCRIPTION = "Execute"; // 001
  public static final String WRITE_DESCRIPTION = "Write"; // 010
  public static final String WRITE_EXECUTE_DESCRIPTION = "Write Execute"; // 011
  public static final String READ_DESCRIPTION = "Read"; // 100
  public static final String READ_EXECUTE_DESCRIPTION = "Read Execute"; // 101
  public static final String READ_WRITE_DESCIPTION = "Read Write"; // 110
  public static final String READ_WRITE_EXECUTE_DESCRIPTION = "Read Write Execute"; // 111

  public static final String ACCESS_DENIED_CODE = "---";
  public static final String EXECUTE_CODE = "--x";
  public static final String WRITE_CODE = "-w-";
  public static final String WRITE_EXECUTE_CODE = "-wx";
  public static final String READ_CODE = "r--";
  public static final String READ_EXECUTE_CODE = "r-x";
  public static final String READ_WRITE_CODE = "rw-";
  public static final String READ_WRITE_EXECUTE_CODE = "rwx";

  private static int sequence = 0;

  public static final AccessType ACCESS_DENIED = new AccessType(sequence++, ACCESS_DENIED_CODE, ACCESS_DENIED_DESCRIPTION);
  public static final AccessType EXECUTE = new AccessType(sequence++, EXECUTE_CODE, EXECUTE_DESCRIPTION);
  public static final AccessType WRITE = new AccessType(sequence++, WRITE_CODE, WRITE_DESCRIPTION);
  public static final AccessType WRITE_EXECUTE = new AccessType(sequence++, WRITE_EXECUTE_CODE, WRITE_EXECUTE_DESCRIPTION);
  public static final AccessType READ = new AccessType(sequence++, READ_CODE, READ_DESCRIPTION);
  public static final AccessType READ_EXECUTE = new AccessType(sequence++, READ_EXECUTE_CODE, READ_EXECUTE_DESCRIPTION);
  public static final AccessType READ_WRITE = new AccessType(sequence++, READ_WRITE_CODE, READ_WRITE_DESCIPTION);
  public static final AccessType READ_WRITE_EXECUTE = new AccessType(sequence++, READ_WRITE_EXECUTE_CODE, READ_WRITE_EXECUTE_DESCRIPTION);

  private static final Set<AccessType> ACCESS_TYPE_SET = new HashSet<AccessType>(8);

  static {
    ACCESS_TYPE_SET.add(ACCESS_DENIED);
    ACCESS_TYPE_SET.add(EXECUTE);
    ACCESS_TYPE_SET.add(WRITE);
    ACCESS_TYPE_SET.add(WRITE_EXECUTE);
    ACCESS_TYPE_SET.add(READ);
    ACCESS_TYPE_SET.add(READ_EXECUTE);
    ACCESS_TYPE_SET.add(READ_WRITE);
    ACCESS_TYPE_SET.add(READ_WRITE_EXECUTE);
  }

  private final Integer id;

  private final String code;
  private final String description;

  /**
   * Default private constructor to enforce non-instantiability implementing this enumerated-type class.
   * @param id the unique identifier of this enumerated-type.
   * @param code a String value representing this enumerated-type.
   * @param description a String value describing this enumerated-type.
   */
  private AccessType(final Integer id, final String code, final String description) {
    Assert.notNull(id, "The id cannot be null!");
    Assert.notNull(code, "The code cannot be null!");
    Assert.notNull(description, "The description cannot be null!");
    this.id = id;
    this.code = code;
    this.description = description;
  }

  /**
   * Lookup method for the specified AccessType by code.
   * @param code a String describing the AccessType enumerated-type instance.
   * @return the AccessType matching the code or null if no AccessType matches the code.
   */
  public static AccessType getAccessTypeByCode(final String code) {
    for (AccessType accessType : ACCESS_TYPE_SET) {
      if (accessType.getCode().equals(code)) {
        return accessType;
      }
    }

    return null;
  }

  /**
   * Lookup method for the specified AccessType by description.
   * @param description a String describing the AccessType enumerated-type instance.
   * @return the AccessType matching the description or null if no AccessType matches the description.
   */
  public static AccessType getAccessTypeByDescription(final String description) {
    for (AccessType accessType : ACCESS_TYPE_SET) {
      if (accessType.getDescription().equals(description)) {
        return accessType;
      }
    }

    return null;
  }

  /**
   * Lookup method for the specified AccessType by unique identifier.
   * @param id an integer value uniquely identifying the AccessType enumerated-type instance.
   * @return the AccessType matching the id or null if no AccessType matches the id.
   */
  public static AccessType getAccessTypeById(final int id) {
    for (AccessType accessType : ACCESS_TYPE_SET) {
      if (accessType.getId().equals(id)) {
        return accessType;
      }
    }

    return null;
  }

  /**
   * Returns a String describing this enumerated-type value.
   * @return a String description this enumerated-type value.
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns a String describing this enumerated-type value.
   * @return a String description this enumerated-type value.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns an identifier uniquely identifying this enumerated-type value.
   * @return an integer value uniquely identifying this enumerated-type value.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Determines whether this enumerated-type value is equal to some Object.
   * @param obj the Object being compared with enumerated-type for equality.
   * @return a boolean value of true if the Object and this enumerated-type value are equal;
   * false otherwise.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof AccessType)) {
      return false;
    }

    final AccessType that = (AccessType) obj;

    return ObjectUtil.equals(getCode(), that.getCode())
      && ObjectUtil.equals(getDescription(), that.getDescription())
      && ObjectUtil.equals(getId(), that.getId());
  }

  /**
   * Computes a hash code value from the properties of this enumerated-type.
   * @return a integer value representing the computed hash value of this enumerated-type class.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getCode());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
    return hashValue;
  }

  /**
   * Returns a String describing this enumerated-type.
   * @return a String description of this enumerated-type.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{id = ");
    buffer.append(getId());
    buffer.append(", code = ").append(getCode());
    buffer.append(", description = ").append(getDescription());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
