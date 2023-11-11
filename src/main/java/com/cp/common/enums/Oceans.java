/*
 * Oceans.java (c) 23 October 2006
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

public class Oceans extends AbstractEnum {

  private static final Set<Oceans> OCEANS_SET = new HashSet<Oceans>(7);

  public static final Oceans ARCTIC_OCEAN = getOceansFactory().createInstance("ARC", "Arctic Ocean");
  public static final Oceans ATLANTIC_OCEAN = getOceansFactory().createInstance("ATL", "Atlantic Ocean");
  public static final Oceans INDIAN_OCEAN = getOceansFactory().createInstance("IND", "Indian Ocean");
  public static final Oceans PACIFIC_OCEAN = getOceansFactory().createInstance("PAC", "Pacific Ocean");
  public static final Oceans SOUTHERN_OCEAN = getOceansFactory().createInstance("SOU", "Southern Ocean");

  private static OceansFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Oceans.factory";

  /**
   * Creates an instance of the Oceans enumerated-type class.
   * @param id the unique identifier for the Oceans enumerated-type.
   * @param code a String value for looking up enumerated-types of the Oceans classification.
   * @param description descriptive information concerning the Oceans enumerated-type.
   */
  protected Oceans(final Integer id, final String code, final String description) {
    super(id, code, description);
    OCEANS_SET.add(this);
  }

  /**
   * Creates an instance of the Oceans enumerated-type class.
   * @param id the unique identifier for the Oceans enumerated-type.
   * @param code a String value for looking up enumerated-types of the Oceans classification.
   * @param description descriptive information concerning the Oceans enumerated-type.
   * @param externalCode the external code for the Oceans enumerated-type.
   */
  protected Oceans(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    OCEANS_SET.add(this);
  }

  /**
   * Returns the instance of the OceansFactory class to create instances of Oceans enumerated-types.
   * @return an instance of the OceansFactory class for creating Oceans enumerated-type instances.
   */
  protected static OceansFactory getOceansFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Oceans.class) {
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
  public static Oceans getByCode(final String code) {
    return getInstance(OCEANS_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Oceans getByDescription(final String description) {
    return getInstance(OCEANS_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Oceans getByExternalCode(final String externalCode) {
    return getInstance(OCEANS_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Oceans getById(final Integer id) {
    return getInstance(OCEANS_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Oceans> values() {
    return Collections.unmodifiableSet(OCEANS_SET);
  }

  protected interface OceansFactory extends EnumFactory<Oceans> {

    public Oceans createInstance(String code, String description);

    public Oceans createInstance(String code, String description, String externalCode);

  }

  static final class DefaultOceansFactory implements OceansFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultOceansFactory() {
    }

    public Oceans createInstance(final String code, final String description) {
      return new Oceans(getNextSequence(), code, description);
    }

    public Oceans createInstance(final String code, final String description, final String externalCode) {
      return new Oceans(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
