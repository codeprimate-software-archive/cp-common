/*
 * AbstractReferenceObject.java (c) 27 4 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.21
 * @see com.cp.common.beans.definition.ReferenceObject
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractReferenceObject implements ReferenceObject {

  protected final Log logger = LogFactory.getLog(getClass());

  private final Class type;

  private final String referenceId;

  /**
   * Creates an instance of the AbstractReferenceObject class initialized with the specified reference id
   * and Class type.
   * @param referenceId a String value specifying the id reference of the Object being referred.
   * @param type a Class object specifing the referred Object's type.
   */
  public AbstractReferenceObject(final String referenceId, final Class type) {
    Assert.notEmpty(referenceId, "The reference id of the referred object cannot be null or empty!");
    this.referenceId = referenceId;
    this.type = type;
  }

  /**
   * Gets the id reference of some Object defined/declared in a configuration file.
   * @return a String value specifying the id reference of some Object defined/declared in a configuration file.
   */
  public String getReferenceId() {
    return referenceId;
  }

  /**
   * Gets the Class type of the Object referenced by the reference id property.
   * @return a Class object specifying the referenced Object's type.
   */
  public Class getType() {
    return type;
  }

  /**
   * Determines whether the specified Object is equal to this ReferenceObject.
   * @param obj the Object used in the equality comparison with this ReferenceObject.
   * @return a boolean value indicating whether the specified Object is equal to this ReferenceObject.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ReferenceObject)) {
      return false;
    }

    final ReferenceObject that = (ReferenceObject) obj;

    return ObjectUtil.equals(getReferenceId(), that.getReferenceId())
      && ObjectUtil.equals(getType(), that.getType());
  }

  /**
   * Computes the hash value of this ReferenceObject.
   * @return an integer value representing the hash of this ReferenceObject.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getReferenceId());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getType());
    return hashValue;
  }

  /**
   * Returns a String representation of the state of this ReferenceObject.
   * @return a String value containing the state of this ReferenceObject.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{referenceId = ");
    buffer.append(getReferenceId());
    buffer.append(", type = ").append(getType());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
