/*
 * UserConverter.java (c) 10 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.3.7
 * @see com.cp.common.beans.DefaultUser
 * @see com.cp.common.beans.User
 * @see com.cp.common.beans.util.converters.AbstractConverter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public class UserConverter extends AbstractConverter<User> {

  /**
   * Default constructor used to instantiate an instance of the UserConverter class.
   */
  public UserConverter() {
  }

  /**
   * Instantiates an instance of the UserConverter class initialized with a default User value that is returned
   * when converting null Object values.
   * @param defaultValue the default User value returned when converting null Object values.
   */
  public UserConverter(final User defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiates an instance of the UserConverter class initialized with a boolean value indicating whether
   * a default User value should be used when converting null Object values.
   * @param usingDefaultValue a boolean value indicating whether or not a default User value will be returned
   * when converting null Object values.
   */
  public UserConverter(final boolean usingDefaultValue) {
    super(usingDefaultValue);
  }

  /**
   * Converts the specified value into an implementation of the User interface.
   * @param type the User interface type.
   * @param value an Object value assumed to specify the username as a String value being converted
   * to an User object.
   * @return a User object implementation conataining the specified Objedct value as the username.
   */
  protected Object convertImpl(final Class type, final Object value) {
    if (StringUtil.isNotEmpty(ObjectUtil.toString(value))) {
      return getUserImpl(value.toString().trim());
    }

    return null;
  }

  /**
   * Gets an instance implementing the User interface initialized with the specified username.
   * @param username a String value specifying the name of the user.
   * @return a User object instance initialized with the specified username.
   */
  protected User getUserImpl(final String username) {
    return new DefaultUser(username);
  }

}
