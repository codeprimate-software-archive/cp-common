/*
 * ConfigUtil.java (c) 11 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.11
 */

package com.cp.common.context.config;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

  /**
   * Converts a Map object containing properties (keys) mapped to values into a Properties object.
   * @param propertyValueMap the Map object containing a mapping of properties (keys) to values.
   * @return a Properties object containing the keys of the Map as properties and the values as the
   * value of the property.
   */
  public static Properties getProperties(final Map<String, Object> propertyValueMap) {
    Assert.notNull(propertyValueMap, "The property to value Map cannot be null!");

    final Properties properties = new Properties();

    for (String property : propertyValueMap.keySet()) {
      properties.setProperty(property, ObjectUtil.toString(propertyValueMap.get(property)));
    }

    return properties;
  }

}
