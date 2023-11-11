/*
 * AbstractEnum.java (c) 17 October 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.11
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.annotation.Required
 * @see com.cp.common.enums.Enum
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.util.ComparableComparator
 * @see com.cp.common.util.ConfigurationException
 * @see com.cp.common.util.PropertyManager
 */

package com.cp.common.enums;

import com.cp.common.beans.annotation.Required;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ComparableComparator;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.PropertyManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractEnum implements Enum {

  private static final Log logger = LogFactory.getLog(AbstractEnum.class);

  private Integer id;
  private Integer sequence;

  private String code;
  private String description;
  private String externalCode;

  /**
   * Creates an instance of the AbstractEnum class.
   * @param id the unique identifier for this enumerated-type.
   * @param code a String identifier for this enumerated-type, used in lookup operations.
   * @param description descriptive information about this enumerated-type.
   */
  protected AbstractEnum(final Integer id, final String code, final String description) {
    this(id, code, description, null, null);
  }

  /**
   * Creates an instance of the AbstractEnum class.
   * @param id the unique identifier for this enumerated-type.
   * @param code a String identifier for this enumerated-type, used in lookup operations.
   * @param description descriptive information about this enumerated-type.
   * @param externalCode the external code for this enumerated-type.
   */
  protected AbstractEnum(final Integer id, final String code, final String description, final String externalCode) {
    this(id, code, description, externalCode, null);
  }

  /**
   * Creates an instance of the AbstractEnum class.
   * @param id the unique identifier for this enumerated-type.
   * @param code a String identifier for this enumerated-type, used in lookup operations.
   * @param description descriptive information about this enumerated-type.
   * @param externalCode the external code for this enumerated-type.
   * @param sequence the order of this enumerated-type in the enumeration.
   */
  protected AbstractEnum(final Integer id,
                         final String code,
                         final String description,
                         final String externalCode,
                         final Integer sequence) {
    setId(id);
    setCode(code);
    setDescription(description);
    setExternalCode(externalCode);
    setSequence(sequence);
  }

  /**
   * Returns the factory class instance for the enumerated-type.
   * @param factoryPropertyKey the property key in the cp-common.properties file specifying the factory class
   * implementation for this specified enumerated-type.
   * @return an instance of the EnumFactory used to created instances of this enumerated-type.
   * @throws ConfigurationException if the factory class cannot be found or instantiated upon request.
   */
  protected static <F extends EnumFactory> F getFactory(final String factoryPropertyKey) {
    if (logger.isDebugEnabled()) {
      logger.debug("factory property key (" + factoryPropertyKey + ")");
    }

    String factoryClassName = null;

    try {
      factoryClassName = PropertyManager.getInstance().getStringPropertyValue(factoryPropertyKey);

      if (logger.isDebugEnabled()) {
        logger.debug("factory class name (" + factoryClassName + ")");
      }

      final Class factoryClass = Class.forName(factoryClassName);
      final Constructor factoryClassConstructor = factoryClass.getConstructor((Class[]) null);

      return (F) factoryClassConstructor.newInstance((Object[]) null);
    }
    catch (ClassNotFoundException e) {
      logger.error("Unable to find enum factory class (" + factoryClassName + ") in classpath!", e);
      throw new ConfigurationException("Unable to find enum factory class (" + factoryClassName + ") in classpath!", e);
    }
    catch (ConfigurationException e) {
      logger.error("Failed to determine enum factory class for property key (" + factoryPropertyKey
        + ") in the configuration file!", e);
      throw new ConfigurationException("Failed to determine enum factory class for property key (" + factoryPropertyKey
        + ") in the configuration file!", e);
    }
    catch (NoSuchMethodException e) {
      logger.error("No default constructor in enum factory class (" + factoryClassName + ")!", e);
      throw new ConfigurationException("No default constructor in enum factory class (" + factoryClassName + ")!", e);
    }
    catch (InstantiationException e) {
      logger.error("Failed to create an instance of enum factory class (" + factoryClassName + ")!", e);
      throw new ConfigurationException("Failed to create an instance of enum factory class (" + factoryClassName
        + ")!", e);
    }
    catch (IllegalAccessException e) {
      logger.error("Unprivileged access to enum factory class (" + factoryClassName + ") during instantiation!", e);
      throw new ConfigurationException("Unprivileged access to enum factory class (" + factoryClassName
        + ") during instantiation!", e);
    }
    catch (InvocationTargetException e) {
      logger.error("Constructor invocation of the enum factory class (" + factoryClassName + ") failed!", e);
      throw new ConfigurationException("Constructor invocation of the enum factory class (" + factoryClassName
        + ") failed!", e);
    }
  }

  /**
   * Returns an instance of the AbstractEnum class determined by the LookupStrategy used.
   * @param enumSet the Set of elements in the enumeration of type E.
   * @param lookupStrategy a strategy for getting and instance of the AbstractEnum class, such as by ID or by code.
   * @return an instance of the AbstractEnum class that statisfies the LookupStrategy.
   * @throws IllegalArgumentException if the LookupStrategy value does not correspond to one of the enumerated-types
   * for this particular enumerated-type classification.
   */
  protected static <E extends AbstractEnum> E getInstance(final Set<E> enumSet, final LookupStrategy lookupStrategy) {
    for (E enumx : enumSet)  {
      if (lookupStrategy.accept(enumx)) {
        return enumx;
      }
    }
    throw new IllegalArgumentException(MessageFormat.format("(" + lookupStrategy.getLookupValue()
      + ") is not a valid {0} for this enumerated-type!", lookupStrategy.getArguments()));
  }

  /**
   * Returns the code identifier for this enumerated-type.
   * @return a String identifier signifying the enumerated-types code, used in lookup operations.
   */
  public final String getCode() {
    return code;
  }

  /**
   * Sets the code identifier for this enumerated-type.
   * @param code a String identifier signifying the enumerated types code, used in lookup operations.
   */
  @Required
  private void setCode(final String code) {
    Assert.notNull(code, "The enum's code cannot be null!");
    this.code = code;
  }

  /**
   * Returns this enumerated-type's descriptive information.
   * @return a String describing this enumerated-type.
   */
  public final String getDescription() {
    return description;
  }

  /**
   * Sets this enumerated-type's descriptive information.
   * @param description a String describing this enumerated-type.
   */
  @Required
  private void setDescription(final String description) {
    Assert.notNull(description, "The enum's description cannot be null!");
    this.description = description;
  }

  /**
   * Returns the external code for this enumerated-type.
   * @return a String representing the external code for this enumerated-type.
   */
  public final String getExternalCode() {
    return externalCode;
  }

  /**
   * Sets the external code for this enumerated-type.
   * @param externalCode a String value specifying the external code for this enumerated-type.
   */
  private void setExternalCode(final String externalCode) {
    this.externalCode = externalCode;
  }

  /**
   * Returns the unique identifier for this enumerated-type.
   * @return a Integer specifying the unique identifier for this enumerated-type.
   */
  public final Integer getId() {
    return id;
  }

  /**
   * Sets the unique identifier for this enumerated-type.
   * @param id an Integer specifying the unique identifier for this enumerated-type.
   */
  @Required
  private void setId(final Integer id) {
    Assert.notNull(id, "The enum's id cannot be null!");
    this.id = id;
  }

  /**
   * Returns the order of this Enum value in the sequence of Enums.
   * @return an Integer value specifying this Enum value's order.
   */
  public Integer getSequence() {
    return sequence;
  }

  /**
   * Sets the order of this Enum value in the sequence of Enums.
   * @param sequence an Integer value specifying the order of this Enum value.
   */
  protected void setSequence(final Integer sequence) {
    this.sequence = sequence;
  }

  /**
   * Determines whether this enumerated type id is equal to the specified Object.
   * @param obj the object compared for equality with this enumerated type id.
   * @return a boolean id indicating if the specified Object is equal to this enumerated type id.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    // note, the instanceof operator implicitly handles null, we have to explicitly handle null
    // in our conditional check since we are not using the instanceof operator.
    if (ObjectUtil.isNull(obj) || !(getClass().isAssignableFrom(obj.getClass()))) {
      return false;
    }

    final AbstractEnum that = (AbstractEnum) obj;

    return ObjectUtil.equals(getId(), that.getId())
      && ObjectUtil.equals(getCode(), that.getCode())
      && ObjectUtil.equals(getDescription(), that.getDescription())
      && ObjectUtil.equals(getExternalCode(), that.getExternalCode());
  }

  /**
   * Computes a hash id from the properties of this enumerated type id.
   * @return an integer id specifying the computed hash id of this enumerated type.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getCode());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getExternalCode());
    return hashValue;
  }

  /**
   * Returns a String representation of this enumerated type.
   * @return a String representation of this enumerated type.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{id = ");
    buffer.append(getId());
    buffer.append(", code = ").append(getCode());
    buffer.append(", description = ").append(getDescription());
    buffer.append(", externalCode = ").append(getExternalCode());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * Interface for enumerated-type factories.
   */
  protected interface EnumFactory<E extends AbstractEnum> {

    public E createInstance(String code, String description);

    public E createInstance(String code, String description, String externalCode);

  }

  /**
   * Comparator based on the code property of the Enum.
   */
  public static final class CodeComparator implements Comparator<AbstractEnum> {

    public int compare(AbstractEnum enum0, AbstractEnum enum1) {
      return ComparableComparator.<String>getInstance().compare(enum0.getCode(), enum1.getCode());
    }
  }

  /**
   * Comparator based on the description property of the Enum.
   */
  public static final class DescriptionComparator implements Comparator<AbstractEnum> {

    public int compare(AbstractEnum enum0, AbstractEnum enum1) {
      return ComparableComparator.<String>getInstance().compare(enum0.getDescription(), enum1.getDescription());
    }
  }

  /**
   * Comparator based on the external code property of the Enum.
   */
  public static final class ExternalCodeComparator implements Comparator<AbstractEnum> {

    public int compare(AbstractEnum enum0, AbstractEnum enum1) {
      return ComparableComparator.<String>getInstance().compare(enum0.getExternalCode(), enum1.getExternalCode());
    }
  }

  /**
   * Comparator based on the id property of the Enum.
   */
  public static final class IdComparator implements Comparator<AbstractEnum> {

    public int compare(AbstractEnum enum0, AbstractEnum enum1) {
      return ComparableComparator.<Integer>getInstance().compare(enum0.getId(), enum1.getId());
    }
  }

  /**
   * Comparator based on the sequence property of the Enum.
   */
  public static final class SequenceComparator implements Comparator<AbstractEnum> {

    public int compare(AbstractEnum enum0, AbstractEnum enum1) {
      return ComparableComparator.<Integer>getInstance().compare(enum0.getSequence(), enum1.getSequence());
    }
  }

  /**
   * Interface for defining different strategies of obtaining specific enumerated-types declared by
   * an enumerated-type classification.
   */
  protected interface LookupStrategy {

    public boolean accept(AbstractEnum enumx);

    public Object[] getArguments();

    public Object getLookupValue();

  }

  /**
   * LookupStrategy for finding enumerated-types based on the code property value.
   */
  protected static final class CodeLookupStrategy implements LookupStrategy {

    private final String code;

    public CodeLookupStrategy(final String code) {
      Assert.notEmpty(code, "The code for the lookup strategy cannot be empty!");
      this.code = code;
    }

    public boolean accept(final AbstractEnum enumx) {
      return ObjectUtil.equals(code, enumx.getCode());
    }

    public Object[] getArguments() {
      return new Object[] { "code" };
    }

    public Object getLookupValue() {
      return code;
    }
  }

  /**
   * LookupStrategy for finding enumerated-types based on the description property value.
   */
  protected static final class DescriptionLookupStrategy implements LookupStrategy {

    private final String description;

    public DescriptionLookupStrategy(final String description) {
      Assert.notEmpty(description, "The description for the lookup strategy cannot be empty!");
      this.description = description;
    }

    public boolean accept(final AbstractEnum enumx) {
      return ObjectUtil.equals(description, enumx.getDescription());
    }

    public Object[] getArguments() {
      return new Object[] { "description" };
    }

    public Object getLookupValue() {
      return description;
    }
  }

  /**
   * LookupStrategy for finding enumerated-types based on the externalCode property value.
   */
  protected static final class ExternalCodeLookupStrategy implements LookupStrategy {

    private final String externalCode;

    public ExternalCodeLookupStrategy(final String externalCode) {
      Assert.notEmpty(externalCode, "The external code for this lookup strategy cannot be empty!");
      this.externalCode = externalCode;
    }

    public boolean accept(final AbstractEnum enumx) {
      return ObjectUtil.equals(externalCode, enumx.getExternalCode());
    }

    public Object[] getArguments() {
      return new Object[] { "external code" };
    }

    public Object getLookupValue() {
      return externalCode;
    }
  }

  /**
   * LookupStrategy for finding enumerated-types based on the id property value.
   */
  protected static final class IdLookupStrategy implements LookupStrategy {

    private final Integer id;

    public IdLookupStrategy(final Integer id) {
      Assert.notNull(id, "The id for this lookup strategy cannot be null!");
      this.id = id;
    }

    public boolean accept(final AbstractEnum enumx) {
      return ObjectUtil.equals(id, enumx.getId());
    }

    public Object[] getArguments() {
      return new Object[] { "ID" };
    }

    public Object getLookupValue() {
      return id;
    }
  }

}
