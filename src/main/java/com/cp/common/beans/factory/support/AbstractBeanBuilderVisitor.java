/*
 * AbstractBeanBuilderVisitor.java (c) 26 December 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.definition.BeanDeclaration
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.PropertyDeclaration
 * @see com.cp.common.beans.factory.BeanFactory
 * @see com.cp.common.beans.factory.support.BeanBuilderVisitor
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.beans.factory.support;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.factory.BeanFactory;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Visitable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBeanBuilderVisitor implements BeanBuilderVisitor {

  private Bean beanObject;

  private final BeanFactory beanFactory;

  private final Class[] argTypes;

  protected final Log logger = LogFactory.getLog(getClass());

  private final Object[] args;

  /**
   * Creates an instance of the AbstractBeanBuilderVisitor class initialized with the specified BeanFactory.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   */
  public AbstractBeanBuilderVisitor(final BeanFactory beanFactory) {
    this(beanFactory, null, null);
  }

  /**
   * Creates an instance of the AbstractBeanBuilderVisitor class initialized with the specified BeanFactory as well as
   * the arguments used to initialize the bean instance.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   * @param args an Object array containing arguments to initialize the bean instance.
   */
  public AbstractBeanBuilderVisitor(final BeanFactory beanFactory, final Object[] args) {
    this(beanFactory, null, args);
  }

  /**
   * Creates an instance of the AbstractBeanBuilderVisitor class initialized with the specified BeanFactory as well as
   * the arguments and their associated types used to initialize the bean instance.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   * @param argTypes a Class array specifying the argument types.
   * @param args an Object array containing arguments to initialize the bean instance.
   */
  public AbstractBeanBuilderVisitor(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
    Assert.notNull(beanFactory, "The bean factory cannot be null!");
    this.beanFactory = beanFactory;
    this.argTypes = argTypes;
    this.args = args;
  }

  /**
   * Gets the arguments used to initialize the bean instance.
   * @return a Object array containing the arguments to initialize the bean instance.
   */
  protected Object[] getArguments() {
    return args;
  }

  /**
   * Gets the argument types of the arguments used to initialize the bean instance.
   * @return a Class array containing the argument types of the arguments used to initialize the bean instance.
   */
  protected Class[] getArgumentTypes() {
    return argTypes;
  }

  /**
   * Gets the Bean object that this Visitor is responsible for building.
   * @return a Bean object built from the visitation of the bean definition by this Visitor.
   */
  public <T extends Bean> T getBean() {
    return (T) beanObject;
  }

  /**
   * Sets the Bean object that this Visitor is responsible for building.
   * @param bean a Bean object built from the visitation of the bean definition by this Visitor.
   */
  protected <T extends Bean> void setBean(T bean) {
    this.beanObject = bean;
  }

  /**
   * Get the BeanFactory object for which this Visitor was called.
   * @return the BeanFactory for which this Visitor was called.
   */
  protected BeanFactory getBeanFactory() {
    return beanFactory;
  }

  /**
   * Gets an instance of the bean declaration handler used to process bean declarations.
   * @return an instance of the DeclarationHandler interface implemented to process bean declarations.
   */
  protected abstract DeclarationHandler<BeanDeclaration> getBeanDeclarationHandler();

  /**
   * Gets an instance of the listener declaration handler used to process listener declarations.
   * @return an instance of the DeclarationHandler interface implemented to process listener declarations.
   */
  protected abstract DeclarationHandler<ListenerDeclaration> getListenerDeclarationHandler();

  /**
   * Gets an instance of the property declaration handler used to process property declarations.
   * @return an instance of the DeclarationHandler interface implemented to process property declarations.
   */
  protected abstract DeclarationHandler<PropertyDeclaration> getPropertyDeclarationHandler();

  /**
   * Visits the bean definition hierarchy to constructor a bean, register listeners and set properties of the bean.
   * @param obj one of the beans definition objects being visited by this Visitor.
   */
  public void visit(final Visitable obj) {
    if (obj instanceof BeanDeclaration) {
      getBeanDeclarationHandler().handle((BeanDeclaration) obj);
    }
    else if (obj instanceof ListenerDeclaration) {
      getListenerDeclarationHandler().handle((ListenerDeclaration) obj);
    }
    else if (obj instanceof PropertyDeclaration) {
      getPropertyDeclarationHandler().handle((PropertyDeclaration) obj);
    }
    else {
      logger.warn("Failed to handle Visitable object of type (" + ClassUtil.getClassName(obj) + ")!");
    }
  }

  /**
   * The abstract DeclarationHandler interface used to model handler to process various bean definition objects such
   * as BeanDeclarations, ListenerDeclarations or PropertyDeclarations.
   */
  protected static interface DeclarationHandler<T> {

    public void handle(T declaration);

  }

}
