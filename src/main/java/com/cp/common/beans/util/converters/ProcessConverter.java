/*
 * ProcessConverter.java (c) 10 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.DefaultProcess
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.util.converters.AbstractConverter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.DefaultProcess;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public class ProcessConverter extends AbstractConverter<com.cp.common.beans.Process> {

  /**
   * Default constructor used to instantiate an instance of the ProcessConverter class.
   */
  public ProcessConverter() {
  }

  /**
   * Instantiates an instance of the ProcessConverter class initialized with a default Process value that is returned
   * when converting null Object values.
   * @param defaultValue the default Process value returned when converting null Object values.
   */
  public ProcessConverter(final com.cp.common.beans.Process defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiates an instance of the ProcessConverter class initialized with a boolean value indicating whether
   * a default Process value should be used when converting null Object values.
   * @param usingDefaultValue a boolean value indicating whether or not a default Process value will be returned
   * when converting null Object values.
   */
  public ProcessConverter(final boolean usingDefaultValue) {
    super(usingDefaultValue);
  }

  /**
   * Converts the specified value into an implementation of the Process interface.
   * @param type the Process interface type.
   * @param value an Object value assumed to specify the process name as a String value being converted
   * to a Process object.
   * @return a Process object implementation conataining the specified Objedct value as the process name.
   */
  protected Object convertImpl(final Class type, final Object value) {
    if (StringUtil.isNotEmpty(ObjectUtil.toString(value))) {
      return getProcessImpl(value.toString().trim());
    }

    return null;
  }

  /**
   * Gets an instance implementing the Process interface initialized with the specified process name.
   * @param processName a String value specifying the name of the process.
   * @return a Process object instance initialized with the specified process name.
   */
  protected com.cp.common.beans.Process getProcessImpl(final String processName) {
    return new DefaultProcess(processName);
  }

}
