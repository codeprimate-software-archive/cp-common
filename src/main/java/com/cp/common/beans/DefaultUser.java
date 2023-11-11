/*
 * DefaultUser.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.6.10
 * @see com.cp.common.beans.User
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.ObjectUtil
 */

package com.cp.common.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public class DefaultUser implements User {

  private final String username;

  /**
   * Creates an instance of DefaultUser initialized to the specifed username.
   * @param username the name of the User represented by this object.
   */
  public DefaultUser(final String username) {
    Assert.notNull(username, "The username cannot be null!");
    this.username = username;
  }

  /**
   * Creates an instance of the DefaultUser class intialized to the User object.
   * @param user the User object used to initialize this instance.
   */
  public DefaultUser(final User user) {
    Assert.notNull(user, "The user cannot be null!");
    this.username = user.getUsername();
  }

  /**
   * Return the name of the user represented by this object.
   * @return a String value indicating the name of the user.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Determines whether the specified object is equal to this user.
   * @param obj the Object value being compared for equality with this user.
   * @return a boolean value indicating whether the specified object is equal to this user.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof User)) {
      return false;
    }

    final User that = (User) obj;

    return ObjectUtil.equals(getUsername(), that.getUsername());
  }

  /**
   * Computes the hash value of this user.
   * @return a integer value of the computed hash code of this user.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getUsername());
    return hashValue;
  }

  /**
   * Returns a String representation of this user.
   * @return the username.
   */
  @Override
  public String toString() {
    return getUsername();
  }

}
