/*
 * DefaultBeanBuilderVisitor.java (c) 26 December 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.7
 * @see com.cp.common.beans.factory.support.AbstractBeanBuilderVisitor
 */

package com.cp.common.beans.factory.support;

import com.cp.common.beans.Bean;
import com.cp.common.beans.BeanInstantiationException;
import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.factory.BeanFactory;
import com.cp.common.beans.factory.BeanNotFoundException;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.converters.CalendarConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.util.EventListener;

public class DefaultBeanBuilderVisitor extends AbstractBeanBuilderVisitor {

  private DeclarationHandler<BeanDeclaration> beanDeclarationHandler;
  private DeclarationHandler<ListenerDeclaration> listenerDeclarationHandler;
  private DeclarationHandler<PropertyDeclaration> propertyDeclarationHandler;

  /**
   * Creates an instance of the DefaultBeanBuilderVisitor class initialized with the specified BeanFactory.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   */
  public DefaultBeanBuilderVisitor(final BeanFactory beanFactory) {
    super(beanFactory);
  }

  /**
   * Creates an instance of the DefaultBeanBuilderVisitor class initialized with the specified BeanFactory as well as
   * the arguments used to initialize the bean instance.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   * @param args an Object array containing arguments to initialize the bean instance.
   */
  public DefaultBeanBuilderVisitor(final BeanFactory beanFactory, final Object[] args) {
    super(beanFactory, null, args);
  }

  /**
   * Creates an instance of the DefaultBeanBuilderVisitor class initialized with the specified BeanFactory as well as
   * the arguments and their associated types used to initialize the bean instance.
   * @param beanFactory the BeanFactory object specified as the caller of this Visitor.
   * @param argTypes a Class array specifying the argument types.
   * @param args an Object array containing arguments to initialize the bean instance.
   */
  public DefaultBeanBuilderVisitor(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
    super(beanFactory, argTypes, args);
  }

  /**
   * Gets an instance of the bean declaration handler used to process bean declarations.
   * @return an instance of the DeclarationHandler interface implemented to process bean declarations.
   */
  protected synchronized DeclarationHandler<BeanDeclaration> getBeanDeclarationHandler() {
    if (ObjectUtil.isNull(beanDeclarationHandler)) {
      beanDeclarationHandler = new DefaultBeanDeclarationHandler();
    }

    return beanDeclarationHandler;
  }

  /**
   * Gets an instance of the listener declaration handler used to process listener declarations.
   * @return an instance of the DeclarationHandler interface implemented to process listener declarations.
   */
  protected synchronized DeclarationHandler<ListenerDeclaration> getListenerDeclarationHandler() {
    if (ObjectUtil.isNull(listenerDeclarationHandler)) {
      listenerDeclarationHandler = new DefaultListenerDeclarationHandler();
    }

    return listenerDeclarationHandler;
  }

  /**
   * Gets an instance of the property declaration handler used to process property declarations.
   * @return an instance of the DeclarationHandler interface implemented to process property declarations.
   */
  protected synchronized DeclarationHandler<PropertyDeclaration> getPropertyDeclarationHandler() {
    if (ObjectUtil.isNull(propertyDeclarationHandler)) {
      propertyDeclarationHandler = new DefaultPropertyDeclarationHandler();
    }

    return propertyDeclarationHandler;
  }

  /**
   * The DefaultBeanDeclarationHandler class used to process bean declarations.
   */
  protected class DefaultBeanDeclarationHandler implements DeclarationHandler<BeanDeclaration> {

    public void handle(final BeanDeclaration beanDeclaration) {
      Assert.notNull(beanDeclaration, "The bean declaration cannot be null!");

      try {
        final Class[] argTypes = ObjectUtil.getSingleDefaultValue(getArgumentTypes(),
          beanDeclaration.getConstructorArgumentTypes());

        final Object[] args = ObjectUtil.getSingleDefaultValue(getArguments(),
          beanDeclaration.getConstructorArgumentValues());

        final Bean bean = ClassUtil.getInstance(beanDeclaration.getType(), argTypes, args);

        setBean(bean);
      }
      catch (ClassNotFoundException e) {
        logger.error("The fully-qualified class (" + beanDeclaration.getClassName() + ") of bean having id ("
          + beanDeclaration.getId() + ") cannot be found!", e);
        throw new BeanNotFoundException("The fully-qualified class (" + beanDeclaration.getClassName()
          + ") of bean having id (" + beanDeclaration.getId() + ") cannot be found!", e);
      }
      catch (InstantiationException e) {
        logger.error("Failed to create an instance of bean class (" + beanDeclaration.getClassName() + ")!", e);
        throw new BeanInstantiationException("Failed to create an instance of bean class ("
          + beanDeclaration.getClassName() + ")!", e);
      }
    }
  }

  /**
   * The DefaultListenerDeclarationHandler class used to process listener declarations in bean definitions.
   */
  protected class DefaultListenerDeclarationHandler implements DeclarationHandler<ListenerDeclaration> {

    public void handle(final ListenerDeclaration listenerDeclaration) {
      Assert.notNull(listenerDeclaration, "The listener declaration cannot be null!");

      try {
        final EventListener listener = getInstance(listenerDeclaration);

        if (listener instanceof PropertyChangeListener) {
          registerPropertyChangeListener(listenerDeclaration, (PropertyChangeListener) listener);
        }
        else {
          registerVetoableChangeListener(listenerDeclaration, (VetoableChangeListener) listener);
        }
      }
      catch (ClassNotFoundException e) {
        logger.error("The fully-qualified class (" + listenerDeclaration.getClassName()
          + ") of listener cannot be found!", e);
        throw new BeanNotFoundException("The fully-qualified class (" + listenerDeclaration.getClassName()
          + ") of listener cannot be found!", e);
      }
      catch (InstantiationException e) {
        logger.error("Failed to create an instance of listener class (" + listenerDeclaration.getClassName() + ")!", e);
        throw new BeanInstantiationException("Failed to create an instance of listener class ("
          + listenerDeclaration.getClassName() + ")!", e);
      }
    }

    protected EventListener callGetInstanceMethod(final ListenerDeclaration listenerDeclaration) throws ClassNotFoundException {
      return (EventListener) ObjectUtil.invokeClassMethod(listenerDeclaration.getType(), "getInstance");
    }

    protected EventListener getInstance(final ListenerDeclaration listenerDeclaration) throws ClassNotFoundException, InstantiationException {
      if (listenerDeclaration.isSingleton()) {
        try {
          return callGetInstanceMethod(listenerDeclaration);
        }
        catch (SystemException e) {
          try {
            logger.warn(e.getMessage(), e.getCause());
            return valueOfStaticInstanceField(listenerDeclaration);
          }
          catch (SystemException ex) {
            logger.warn(ex.getMessage(), ex.getCause());

            if (logger.isInfoEnabled()) {
              logger.info("Warning... listener class (" + listenerDeclaration.getClassName()
                + ") was declared as a Singleton, however no single instance was found; returning new instance!");
            }
          }
        }
      }

      return ClassUtil.getInstance(listenerDeclaration.getType(), null, null);
    }

    protected void registerPropertyChangeListener(final ListenerDeclaration listenerDeclaration,
                                                  final PropertyChangeListener listener) {
      if (!listenerDeclaration.getProperties().isEmpty()) {
        final Bean bean = getBean();

        for (final String propertyName : listenerDeclaration.getProperties()) {
          bean.addPropertyChangeListener(propertyName, listener);
        }
      }
      else {
        ((Bean) getBean()).addPropertyChangeListener(listener);
      }
    }

    protected void registerVetoableChangeListener(final ListenerDeclaration listenerDeclaration,
                                                  final VetoableChangeListener listener) {
      if (!listenerDeclaration.getProperties().isEmpty()) {
        final Bean bean = getBean();

        for (final String propertyName : listenerDeclaration.getProperties()) {
          bean.addVetoableChangeListener(propertyName, listener);
        }
      }
      else {
        ((Bean) getBean()).addVetoableChangeListener(listener);
      }
    }

    protected EventListener valueOfStaticInstanceField(final ListenerDeclaration listenerDeclaration) throws ClassNotFoundException {
      for (final Field field : listenerDeclaration.getType().getDeclaredFields()) {
        if ("INSTANCE".equalsIgnoreCase(field.getName())) {
          try {
            return (EventListener) field.get(null);
          }
          // NOTE the IllegalArgumentException should not be thrown from the get(null) call on the Field class.
          catch (IllegalAccessException e) {
            throw new SystemException("Failed to access static class member field (" + field.getName()
              + ") on listener class (" + listenerDeclaration.getClassName()
              + "); please verify the field's access modifiers and system permissions!", e);
          }
        }
      }

      throw new SystemException("No static field INSTANCE exists on listener class ("
        + listenerDeclaration.getClassName() + ")!");
    }
  }

  /**
   * The DefaultPropertyDeclarationHandler class used to process property declarations in bean definitions.
   */
  protected class DefaultPropertyDeclarationHandler implements DeclarationHandler<PropertyDeclaration> {

    public void handle(final PropertyDeclaration propertyDeclaration) {
      Assert.notNull(propertyDeclaration, "The property declaration cannot be null!");

      Object value;

      if (ObjectUtil.isNotNull(propertyDeclaration.getValue())) {
        // TODO handle other value format patterns besides Calendar!
        if (StringUtil.isNotEmpty(propertyDeclaration.getFormatPattern())) {
          CalendarConverter.addDateFormatPattern(propertyDeclaration.getFormatPattern());
        }

        value = propertyDeclaration.getValue();
      }
      else {
        Assert.notEmpty(propertyDeclaration.getRefId(), "The bean reference id for property ("
          + propertyDeclaration.getName() + ") of bean definition having id ("
          + propertyDeclaration.getBeanDefinition().getId() + ") should not be null or empty!");

        value = getBeanFactory().getBean(propertyDeclaration.getRefId());
      }

      BeanUtil.setPropertyValue(getBean(), propertyDeclaration.getName(), value);
    }
  }

}
