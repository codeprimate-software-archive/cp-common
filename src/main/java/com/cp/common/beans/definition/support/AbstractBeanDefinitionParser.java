/*
 * AbstractBeanDefinitionParser.java (c) 24 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.23
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.support.BeanDefinitionParser
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.lang.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

public abstract class AbstractBeanDefinitionParser<T extends BeanDefinition> implements BeanDefinitionParser<T> {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Parses the specified bean definitions input stream and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsInputStream an InputStream as the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition input stream.
   */
  protected abstract Map<String, T> parse(InputStream beanDefinitionsInputStream);

  /**
   * Parses the specified bean definitions file and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsFile a File specifying the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition file.
   * @throws FileNotFoundException if the File object designating the bean definitions source does not exist!
   * @see BeanDefinitionParser#parse(Resource)
   * @see BeanDefinitionParser#parse(String)
   */
  public Map<String, T> parse(final File beanDefinitionsFile) throws FileNotFoundException {
    Assert.notNull(beanDefinitionsFile, "The bean definitions file cannot be null!");
    return parse(new FileInputStream(beanDefinitionsFile));
  }

  /**
   * Parses the specified bean definitions resource and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsResource a Resource specifying the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition file.
   * @throws IOException if the Resource designating the bean definitions source cannot be opened and read.
   * @see BeanDefinitionParser#parse(File)
   * @see BeanDefinitionParser#parse(String)
   */
  public Map<String, T> parse(final Resource beanDefinitionsResource) throws IOException {
    Assert.notNull(beanDefinitionsResource, "The bean definitions resource cannot be null!");
    return parse(beanDefinitionsResource.getInputStream());
  }

  /**
   * Parses the specified bean definitions pathname and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsPathname a String indicating the pathname specifying the source of the bean definitions
   * to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition file.
   * @see BeanDefinitionParser#parse(File)
   * @see BeanDefinitionParser#parse(Resource)
   */
  public Map<String, T> parse(final String beanDefinitionsPathname) {
    Assert.notEmpty(beanDefinitionsPathname, "The absolute path of the bean definitions file cannot be null or empty!");
    return parse(getClass().getResourceAsStream(beanDefinitionsPathname));
  }

}
