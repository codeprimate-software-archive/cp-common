/*
 * Continent.java (c) 19 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.6.2
 * @see com.cp.common.enums.AbstractEnum
 */

package com.cp.common.enums;

import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Continent extends AbstractEnum {

  private static final Set<Continent> CONTINENT_SET = new HashSet<Continent>(11);

  public static final Continent AFRICA = getContinentFactory().createInstance("AF", "Africa");
  public static final Continent ANTARTICA = getContinentFactory().createInstance("AN", "Antartica");
  public static final Continent ASIA = getContinentFactory().createInstance("AS", "Asia");
  public static final Continent AUSTRALIA = getContinentFactory().createInstance("AU", "Australia");
  public static final Continent EUROPE = getContinentFactory().createInstance("EU", "Europe");
  public static final Continent NORTH_AMERICA = getContinentFactory().createInstance("NA", "North America");
  public static final Continent OCEANIA = getContinentFactory().createInstance("OC", "Oceania");
  public static final Continent SOUTH_AMERICA = getContinentFactory().createInstance("SA", "South America");

  private static ContinentFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Continent.factory";

  /**
   * Creates an instance of the Continent enumerated-type class.
   * @param id the unique identifier for the Continent enumerated-type.
   * @param code a String value for looking up enumerated-types of the Continent classification.
   * @param description descriptive information concerning the Continent enumerated-type.
   */
  protected Continent(final Integer id, final String code, final String description) {
    super(id, code, description);
    CONTINENT_SET.add(this);
  }

  /**
   * Creates an instance of the Continent enumerated-type class.
   * @param id the unique identifier for the Continent enumerated-type.
   * @param code a String value for looking up enumerated-types of the Continent classification.
   * @param description descriptive information concerning the Continent enumerated-type.
   * @param externalCode the external code for the Continent enumerated-type.
   */
  protected Continent(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    CONTINENT_SET.add(this);
  }

  /**
   * Returns the instance of the ContinentFactory class to create instances of Continent enumerated-types.
   * @return an instance of the ContinentFactory class for creating Continent enumerated-type instances.
   */
  protected static ContinentFactory getContinentFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Continent.class) {
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
  public static Continent getByCode(final String code) {
    return getInstance(CONTINENT_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Continent getByDescription(final String description) {
    return getInstance(CONTINENT_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Continent getByExternalCode(final String externalCode) {
    return getInstance(CONTINENT_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Continent getById(final Integer id) {
    return getInstance(CONTINENT_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Continent> values() {
    return Collections.unmodifiableSet(CONTINENT_SET);
  }

  protected interface ContinentFactory extends EnumFactory<Continent> {

    public Continent createInstance(String code, String description);

    public Continent createInstance(String code, String description, String externalCode);

  }

  static final class DefaultContinentFactory implements ContinentFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultContinentFactory() {
    }

    public Continent createInstance(final String code, final String description) {
      return new Continent(getNextSequence(), code, description);
    }

    public Continent createInstance(final String code, final String description, final String externalCode) {
      return new Continent(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
