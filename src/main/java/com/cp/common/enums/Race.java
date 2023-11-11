/*
 * Race.java (c) 18 October 2004
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

public class Race extends AbstractEnum {

  private static final Set<Race> RACE_SET = new HashSet<Race>(7);

  public static final Race ALASKAN_NATIVE = getRaceFactory().createInstance("alaskan", "Alaskan Native", "AN");
  public static final Race ASIAN = getRaceFactory().createInstance("asian", "Asian", "A");
  public static final Race BLACK = getRaceFactory().createInstance("black", "Black", "B");
  public static final Race HISPANIC = getRaceFactory().createInstance("hispanic", "Hispanic", "H");
  public static final Race NATIVE_AMERICAN = getRaceFactory().createInstance("native american", "Native American", "NA");
  public static final Race WHITE = getRaceFactory().createInstance("white", "White", "W");

  private static RaceFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Race.factory";

  /**
   * Creates an instance of the Race enumerated-type class.
   * @param id the unique identifier for the Race enumerated-type.
   * @param code a String value for looking up enumerated-types of the Race classification.
   * @param description descriptive information concerning the Race enumerated-type.
   */
  protected Race(final Integer id, final String code, final String description) {
    super(id, code, description);
    RACE_SET.add(this);
  }

  /**
   * Creates an instance of the Race enumerated-type class.
   * @param id the unique identifier for the Race enumerated-type.
   * @param code a String value for looking up enumerated-types of the Race classification.
   * @param description descriptive information concerning the Race enumerated-type.
   * @param externalCode the external code for the Race enumerated-type.
   */
  protected Race(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    RACE_SET.add(this);
  }

  /**
   * Returns the instance of the RaceFactory class to create instances of Race enumerated-types.
   * @return an instance of the RaceFactory class for creating Race enumerated-type instances.
   */
  protected static RaceFactory getRaceFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Race.class) {
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
  public static Race getByCode(final String code) {
    return getInstance(RACE_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Race getByDescription(final String description) {
    return getInstance(RACE_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Race getByExternalCode(final String externalCode) {
    return getInstance(RACE_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Race getById(final Integer id) {
    return getInstance(RACE_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Race> values() {
    return Collections.unmodifiableSet(RACE_SET);
  }

  protected interface RaceFactory extends EnumFactory<Race> {

    public Race createInstance(String code, String description);

    public Race createInstance(String code, String description, String externalCode);

  }

  static final class DefaultRaceFactory implements RaceFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultRaceFactory() {
    }

    public Race createInstance(final String code, final String description) {
      return new Race(getNextSequence(), code, description);
    }

    public Race createInstance(final String code, final String description, final String externalCode) {
      return new Race(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
