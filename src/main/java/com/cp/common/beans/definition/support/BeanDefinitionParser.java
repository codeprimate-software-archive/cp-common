/*
 * BeanDefinitionParser.java (c) 24 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.10
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see java.io.File
 * @see org.springframework.core.io.Resource
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.Resource;

public interface BeanDefinitionParser<T extends BeanDefinition> {

  /**
   * Parses the specified bean definitions file and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsFile a File specifying the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition file.
   * @throws FileNotFoundException if the File object designating the bean definitions source does not exist!
   * @see BeanDefinitionParser#parse(Resource)
   * @see BeanDefinitionParser#parse(String)
   */
  public Map<String, T> parse(File beanDefinitionsFile) throws FileNotFoundException;

  /**
   * Parses the specified bean definitions resource and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsResource a Resource specifying the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition resource.
   * @throws IOException if the Resource designating the bean definitions source cannot be opened and read.
   * @see BeanDefinitionParser#parse(File)
   * @see BeanDefinitionParser#parse(String)
   */
  public Map<String, T> parse(Resource beanDefinitionsResource) throws IOException;

  /**
   * Parses the specified bean definitions pathname and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsPathname a String indicating the pathname specifying the source of the bean definitions
   * to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition pathname.
   * @see BeanDefinitionParser#parse(File)
   * @see BeanDefinitionParser#parse(Resource)
   */
  public Map<String, T> parse(String beanDefinitionsPathname);

}
