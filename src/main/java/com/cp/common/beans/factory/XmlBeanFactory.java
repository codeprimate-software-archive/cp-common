/*
 * XmlBeanFactory.java (c) 23 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.12.23
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.definitions.BeanDeclaration
 * @see com.cp.common.beans.definitions.Scope
 * @see com.cp.common.beans.definitions.support.BeanDefinitionParser
 * @see com.cp.common.beans.definitions.support.XmlBeanDeclarationParser
 * @see com.cp.common.beans.factory.AbstractBeanFactory
 * @see com.cp.common.beans.factory.support.AbstractBeanBuilderVisitor
 * @see com.cp.common.beans.factory.support.DefaultBeanBuilderVisitor
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.lang.Destroyable
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.Scope;
import com.cp.common.beans.definition.support.BeanDefinitionParser;
import com.cp.common.beans.definition.support.XmlBeanDeclarationParser;
import com.cp.common.beans.factory.support.BeanBuilderVisitor;
import com.cp.common.beans.factory.support.DefaultBeanBuilderVisitor;
import com.cp.common.context.config.Config;
import com.cp.common.lang.Assert;
import com.cp.common.lang.Destroyable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.SystemException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class XmlBeanFactory extends AbstractBeanFactory implements Destroyable {

  protected static final Scope DEFAULT_BEAN_SCOPE = Scope.PROTOTYPE;

  protected static final String BEAN_DEFINITIONS_FILE_PROPERTY = "cp-common.bean.definitions.file";
  protected static final String BEAN_DEFINITION_PARSER_PROPERTY = "cp-common.bean.definition.parser";
  protected static final String DEFAULT_BEAN_DEFINITIONS_FILE = "/etc/config/beans.xml";

  private BeanDefinitionParser<BeanDeclaration> beanDefinitionParser;

  private File beanDefinitionsFile;

  private Map<String, BeanDeclaration> beanDeclarationMap;
  private Map<String, Bean> beanObjectMap = new TreeMap<String, Bean>();

  /**
   * Creates an instance of the XmlBeanFactory class initialized with the specified configuration object.
   * @param config the Config object used to obtain configuration information for the application.
   */
  public XmlBeanFactory(final Config config) {
    super(config);
  }

  /**
   * Creates an instance of the XmlBeanFactory class initialized with the specified configuration object
   * as well as the specified bean definitions file containing bean declarations.
   * @param config the Config object used to obtain configuration information for the application.
   * @param beanDefinitionsFile the bean definitions file containing bean declarations created by this factory.
   */
  public XmlBeanFactory(final Config config, final File beanDefinitionsFile) {
    super(config);
    setBeanDefinitionsFile(beanDefinitionsFile);
  }

  /**
   * Creates an instance of the XmlBeanFactory class initialized with the specified configuration object
   * and Resource object specifying the bean definitions resource containing bean declarations.
   * @param config the Config object used to obtain configuration information for the application.
   * @param beanDefinitionsResource the bean definitions resource containing bean declarations created by this factory.
   */
  public XmlBeanFactory(final Config config, final Resource beanDefinitionsResource) {
    super(config);
    setBeanDefinitionsResource(beanDefinitionsResource);
  }

  /**
   * Initialiazes the XmlBeanFactory by parsing the beans XML configuration file and pre-instantiating
   * the singleton beans.
   * @see XmlBeanFactory#destroy()
   */
  // TODO figure out the caller for the initialization method outside of a Spring application context.
  public void init() {
    try {
      beanDeclarationMap = getBeanDefinitionParser().parse(getBeanDefinitionsFile());
      Assert.notNull(beanDeclarationMap, "The bean declaration map cannot be null!");

      for (final BeanDeclaration beanDeclaration : beanDeclarationMap.values()) {
        if (beanDeclaration.isSingleton() && !beanDeclaration.isLazyInit()) {
          final BeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitorImpl(this, null, null);

          beanDeclaration.accept(beanBuilderVisitor);

          final Bean bean = beanBuilderVisitor.getBean();

          invokeInitMethod(beanDeclaration, bean);
          beanObjectMap.put(beanDeclaration.getId(), bean);
        }
      }
    }
    catch (FileNotFoundException e) {
      logger.error("Failed to initialize the bean factory!", e);
      throw new SystemException("Failed to initialize the bean factory!", e);
    }
  }

  public boolean isDestroyed() {
    throw new UnsupportedOperationException("Not Implemented");
  }

  /**
   * Destroys all singleton beans managed by this bean factory.
   * @see XmlBeanFactory#init()
   */
  public void destroy() {
    for (final String beanId : getBeanObjectMap().keySet()) {
      final BeanDeclaration beanDeclaration = getBeanDeclarationMap().get(beanId);
      final Bean bean = getBeanObjectMap().get(beanId);

      invokeDestroyMethod(beanDeclaration, bean);
    }
  }

  /**
   * Invokes the destroy method specified in the bean declaration for the specified bean if the bean defined
   * a destroy method.
   * @param beanDeclaration the BeanDeclaration object associated with the specified bean.
   * @param bean the actual Bean object to invoke the destroy method on.
   * @see XmlBeanFactory#invokeInitMethod(BeanDeclaration, Bean)
   */
  protected void invokeDestroyMethod(final BeanDeclaration beanDeclaration, final Bean bean) {
    if (isMethodOf(bean, beanDeclaration.getDestroyMethod())) {
      ObjectUtil.invokeInstanceMethod(bean, beanDeclaration.getDestroyMethod());
    }
  }

  /**
   * Invokes the init method specified in the bean declaration for the specified bean if the bean defined
   * a init method.
   * @param beanDeclaration the BeanDeclaration object associated with the specified bean.
   * @param bean the actual Bean object to invoke the init method on.
   * @see XmlBeanFactory#invokeDestroyMethod(BeanDeclaration, Bean)
   */
  protected void invokeInitMethod(final BeanDeclaration beanDeclaration, final Bean bean) {
    if (isMethodOf(bean, beanDeclaration.getInitMethod())) {
      ObjectUtil.invokeInstanceMethod(bean, beanDeclaration.getInitMethod());
    }
  }

  /**
   * Determines whether the specified method by name exists on the Class type of the specified object.
   * @param obj the Object used in determining whether the specified method by name exists on the Object's Class.
   * @param methodName a String specifying the name of the method.
   * @return a boolean value indicating whether the specified method is a member of the specified object's Class.
   * @throws ConfigurationException if the method indicated by name is not a public member of the object's Class.
   */
  protected boolean isMethodOf(final Object obj, final String methodName) {
    Assert.notNull(obj, "The object cannot be null!");

    if (StringUtil.isNotEmpty(methodName)) {
      if (!ClassUtils.hasAtLeastOneMethodWithName(obj.getClass(), methodName)) {
        throw new ConfigurationException("No such public method (" + methodName + ") exists on class ("
          + obj.getClass().getName() + ")!");
      }

      return true;
    }

    return false;
  }

  /**
   * Gets the aliases for a particular bean identified by the unique identifier.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a String array of aliases that are synonymous with the specified bean id for a bean.
   */
  public String[] getAliases(final String beanId) {
    return getBeanDeclaration(beanId).getNames();
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @return a Bean object identified by the specified unique identifier.
   * @see XmlBeanFactory#getBeanObject(String, Class[], Object[])
   */
  public <T extends Bean> T getBean(final String beanId) {
    return (T) getBeanObject(beanId, null, null);
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   * @see XmlBeanFactory#getBeanObject(String, Class[], Object[])
   */
  public <T extends Bean> T getBean(final String beanId, final Object[] args) {
    return (T) getBeanObject(beanId, null, args);
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param argTypes a Class array containing the argument types.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   * @see XmlBeanFactory#getBeanObject(String, Class[], Object[])
   */
  public <T extends Bean> T getBean(final String beanId, final Class[] argTypes, final Object[] args) {
    return (T) getBeanObject(beanId, argTypes, args);
  }

  /**
   * Gets the implementation of the AbstractBeanBuilderVisitor object used to construct an instance of a bean defined by
   * the bean's bean declaration.
   * @param beanFactory the BeanFactory object associated with the Visitor.
   * @param argTypes a Class array specifying the argument types.
   * @param args an Object array containing arguments to initialize the bean instance.
   * @return an instance of the AbstractBeanBuilderVisitor class to construct instances of beans declared in a
   * bean definitions file.
   */
  protected BeanBuilderVisitor getBeanBuilderVisitorImpl(final BeanFactory beanFactory,
                                                         final Class[] argTypes,
                                                         final Object[] args) {
    return new DefaultBeanBuilderVisitor(beanFactory, argTypes, args);
  }

  /**
   * Gets the BeanDeclaration having the specified bean ID or alias specified by the following bean ID.
   * @param beanId a String value identifying the bean declaration.
   * @return a BeanDeclaration identified by the specified bean ID, or null if no bean with ID exists!
   */
  protected BeanDeclaration getBeanDeclaration(final String beanId) {
    Assert.notEmpty(beanId, "The bean id cannot be null or empty!");

    final BeanDeclaration theBeanDeclaration = getBeanDeclarationMap().get(beanId);

    if (ObjectUtil.isNotNull(theBeanDeclaration)) {
      return theBeanDeclaration;
    }
    else {
      for (final BeanDeclaration beanDeclaration : getBeanDeclarationMap().values()) {
        if (Arrays.asList(beanDeclaration.getNames()).contains(beanId)) {
          return beanDeclaration;
        }
      }
    }

    logger.warn("The bean identified by id (" + beanId + ") cannot be found!");
    throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!");
  }

  /**
   * Gets a mapping of bean id to BeanDefinition objects.
   * @return a Map object containing a mapping of bean id to BeanDefinition objects.
   */
  protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
    Assert.state(ObjectUtil.isNotNull(beanDeclarationMap), "The XmlBeanFactory has not been properly initialized with a beans configuration file!");
    return beanDeclarationMap;
  }

  /**
   * Gets the bean definition parser used by this BeanFactory to parse the bean definitions file.
   * @return an instance of the BeanDefinitionParser used to parse the bean definition file declaring the beans
   * for this bean factory.
   */
  protected synchronized BeanDefinitionParser<BeanDeclaration> getBeanDefinitionParser() {
    if (ObjectUtil.isNull(beanDefinitionParser)) {
      try {
        beanDefinitionParser = getInstance(BEAN_DEFINITION_PARSER_PROPERTY);
      }
      catch (MissingResourceException e) {
        logger.warn("The property (" + BEAN_DEFINITION_PARSER_PROPERTY
          + ") has not been defined in the application configuration resource: " + e.getMessage());
        beanDefinitionParser = new XmlBeanDeclarationParser();
      }
    }

    return beanDefinitionParser;
  }

  /**
   * Sets the bean definition parser used by this BeanFactory to parse the bean definitions file.
   * @param beanDefinitionParser an instance of the BeanDefinitionParser used to parse the bean definition file
   * declaring the beans for this bean factory.
   */
  public synchronized void setBeanDefinitionParser(final BeanDefinitionParser<BeanDeclaration> beanDefinitionParser) {
    Assert.notNull(beanDefinitionParser, "The bean definition parser cannot be null!");
    this.beanDefinitionParser = beanDefinitionParser;
  }

  /**
   * Gets the File object referencing the bean definitions file containing declarations of beans returned by this
   * bean factory.
   * @return a File object referencing the bean definitions file declaring the beans for this bean facctory.
   * @throws FileNotFoundException if the bean definitions file does not exist!
   */
  protected synchronized File getBeanDefinitionsFile() throws FileNotFoundException {
    if (ObjectUtil.isNull(beanDefinitionsFile)) {
      final String beanDefinitionsFilePathname = getConfig().getStringPropertyValue(BEAN_DEFINITIONS_FILE_PROPERTY,
        DEFAULT_BEAN_DEFINITIONS_FILE);

      if (logger.isDebugEnabled()) {
        logger.debug("bean definitions file pathname (" + beanDefinitionsFilePathname + ")");
      }

      try {
        final URL resource = XmlBeanFactory.class.getResource(beanDefinitionsFilePathname);

        if (ObjectUtil.isNotNull(resource)) {
          beanDefinitionsFile = new File(resource.toURI());
        }
        else {
          logger.warn("The bean definitions file (" + beanDefinitionsFilePathname + ") could not be found!");
          throw new FileNotFoundException("The bean definitions file (" + beanDefinitionsFilePathname
            + ") could not be found!");
        }
      }
      catch (URISyntaxException e) {
        logger.error("Failed to find bean definitions file at the following location (" + beanDefinitionsFilePathname
          + ")!", e);
        throw new FileNotFoundException("Failed to find bean definitions file at the following location ("
          + beanDefinitionsFilePathname + ")!");
      }
    }

    return beanDefinitionsFile;
  }

  /**
   * Sets the File object referencing the bean definitions file containing declarations of beans returned by this
   * bean factory.
   * @param beanDefinitionsFile a File object referencing the bean definitions file declaring the beans for this
   * bean facctory.
   */
  public synchronized void setBeanDefinitionsFile(final File beanDefinitionsFile) {
    Assert.notNull(beanDefinitionsFile, "The bean definitions file cannot be null!");
    this.beanDefinitionsFile = beanDefinitionsFile;
  }

  /**
   * Sets the Resource object referencing the bean definitions file containing declarations of beans returned by this
   * bean factory.
   * @param beanDefinitionsResource a Resource object referencing the bean definitions file declaring the beans for this
   * bean facctory.
   */
  public synchronized void setBeanDefinitionsResource(final Resource beanDefinitionsResource) {
    Assert.notNull(beanDefinitionsResource, "The bean definitions resource cannot be null!");

    try {
      setBeanDefinitionsFile(beanDefinitionsResource.getFile());
    }
    catch (IOException e) {
      logger.error("Failed to configure the bean factory (" + getClass().getName() + ") with bean definitions resource ("
        + beanDefinitionsResource + ")!", e);
      throw new ConfigurationException("Failed to configure the bean factory (" + getClass().getName()
        + ") with bean definitions resource (" + beanDefinitionsResource + ")!", e);
    }
  }

  /**
   * Gets an instance of the bean object identified by bean id initialized with the specified arguments.
   * @param beanId a String value uniquely identifying the bean object.
   * @param argTypes a Class array specifying the arguments types.
   * @param args an Object array of values used to initialize the bean object on instantiation.
   * @return a Bean object identified by the bean id.
   * @see XmlBeanFactory#getBean(String)
   * @see XmlBeanFactory#getBean(String, Object[])
   * @see XmlBeanFactory#getBean(String, Class[], Object[])
   * @see XmlBeanFactory#getBeanDeclaration(String)
   * @see XmlBeanFactory#getBeanObjectMap()
   */
  protected synchronized <T extends Bean> T getBeanObject(final String beanId, final Class[] argTypes, final Object[] args) {
    final BeanDeclaration beanDeclaration = getBeanDeclaration(beanId);

    // if the bean is a singleton and lazy init was set to false, the bean instance will be in the bean object map
    T beanObject = (T) getBeanObjectMap().get(beanDeclaration.getId());

    if (ObjectUtil.isNull(beanObject)) {
      final BeanBuilderVisitor beanBuilderVisitor = getBeanBuilderVisitorImpl(this, argTypes, args);
      beanDeclaration.accept(beanBuilderVisitor);
      beanObject = (T) beanBuilderVisitor.getBean();
      invokeInitMethod(beanDeclaration, beanObject);

      // the bean may be a singleton but the lazy-init attribute/property may have been true
      if (beanDeclaration.isSingleton()) {
        getBeanObjectMap().put(beanDeclaration.getId(), beanObject);
      }
    }

    return beanObject;
  }

  /**
   * Gets a mapping of bean id to Bean objects for all singleton beans defined in the bean definitions file.
   * @return a Map object containing a mapping of bean id to Bean objects for all singleton beans in the
   * bean definitions file.
   */
  protected Map<String, Bean> getBeanObjectMap() {
    return beanObjectMap;
  }

  /**
   * Gets the specified scope of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Scope enumerated value specifying the scope of the bean identified by the specified bean id.
   */
  public Scope getScope(final String beanId) {
    return ObjectUtil.getDefaultValue(getBeanDeclaration(beanId).getScope(), DEFAULT_BEAN_SCOPE);
  }

  /**
   * Gets the Class type of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Class object specifying the type of the bean identified by the specified bean id.
   */
  public Class getType(final String beanId) {
    final BeanDeclaration beanDeclaration = getBeanDeclaration(beanId);

    try {
      return beanDeclaration.getType();
    }
    catch (ClassNotFoundException e) {
      logger.error("The fully qualified class name (" + beanDeclaration.getClassName() + ") of bean identified by id ("
        + beanId + ") cannot be found in the CLASSPATH!", e);
      throw new SystemException("The fully qualified class name (" + beanDeclaration.getClassName()
        + ") of bean identified by id (" + beanId + ") cannot be found in the CLASSPATH!", e);
    }
  }

  /**
   * Determines whether this factory manages the bean identified by the specified bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a boolean value indicating whether the bean identified by the specified bean id is managed by this factory.
   */
  public boolean containsBean(final String beanId) {
    boolean contains = getBeanDeclarationMap().containsKey(beanId);

    if (!contains) {
      for (final BeanDeclaration beanDeclaration : getBeanDeclarationMap().values()) {
        contains |= Arrays.asList(beanDeclaration.getNames()).contains(beanId);
        if (contains) {
          break;
        }
      }
    }

    return contains;
  }

}
