/*
 * Gender.java (c) 18 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.5.29
 * @see com.cp.common.enums.AbstractEnum
 */

package com.cp.common.enums;

import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Gender extends AbstractEnum {

  private static final Set<Gender> GENDER_SET = new HashSet<Gender>(3);

  public static final Gender FEMALE = getGenderFactory().createInstance("female", "Female", "F");
  public static final Gender MALE = getGenderFactory().createInstance("male", "Male", "M");

  private static GenderFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Gender.factory";

  /**
   * Creates an instance of the Gender enumerated-type class.
   * @param id the unique identifier for the Gender enumerated-type.
   * @param code a String value for looking up enumerated-types of the Gender classification.
   * @param description descriptive information concerning the Gender enumerated-type.
   */
  protected Gender(final Integer id, final String code, final String description) {
    super(id, code, description);
    GENDER_SET.add(this);
  }

  /**
   * Creates an instance of the Gender enumerated-type class.
   * @param id the unique identifier for the Gender enumerated-type.
   * @param code a String value for looking up enumerated-types of the Gender classification.
   * @param description descriptive information concerning the Gender enumerated-type.
   * @param externalCode the external code for the Currency enumerated-type.
   */
  protected Gender(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    GENDER_SET.add(this);
  }

  /**
   * Returns the instance of the GenderFactory class to create instances of Gender enumerated-types.
   * @return an instance of the GenderFactory class for creating Gender enumerated-type instances.
   */
  protected static GenderFactory getGenderFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Gender.class) {
        if (ObjectUtil.isNull(FACTORY)) {
          FACTORY = getFactory(FACTORY_PROPERTY_KEY);
        }
      }
    }
    return FACTORY;
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified code.
   * @param code the String code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified code.
   * @throws java.lang.IllegalArgumentException if code is not a valid enumerated-type code.
   */
  public static Gender getByCode(final String code) {
    return getInstance(GENDER_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Gender getByDescription(final String description) {
    return getInstance(GENDER_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Gender getByExternalCode(final String externalCode) {
    return getInstance(GENDER_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Gender getById(final Integer id) {
    return getInstance(GENDER_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Gender> values() {
    return Collections.unmodifiableSet(GENDER_SET);
  }

  protected interface GenderFactory extends EnumFactory<Gender> {

    public Gender createInstance(String code, String description);

    public Gender createInstance(String code, String description, String externalCode);

  }

  static final class DefaultGenderFactory implements GenderFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultGenderFactory() {
    }

    public Gender createInstance(final String code, final String description) {
      return new Gender(getNextSequence(), code, description);
    }

    public Gender createInstance(final String code, final String description, final String externalCode) {
      return new Gender(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
