/*
 * MaritalStatus.java (c) 18 October 2004
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

public class MaritalStatus extends AbstractEnum {

  private static final Set<MaritalStatus> MARITAL_STATUS_SET = new HashSet<MaritalStatus>(7);

  public static final MaritalStatus ANNULLED = getMaritalStatusFactory().createInstance("annulled", "Annulled");
  public static final MaritalStatus COMMON_LAW = getMaritalStatusFactory().createInstance("common-law", "Common-Law");
  public static final MaritalStatus DIVORCED = getMaritalStatusFactory().createInstance("divorced", "Divorced");
  public static final MaritalStatus MARRIED = getMaritalStatusFactory().createInstance("married", "Married");
  public static final MaritalStatus SEPARATED = getMaritalStatusFactory().createInstance("separated", "Separated");
  public static final MaritalStatus SINGLE = getMaritalStatusFactory().createInstance("single", "Single");
  public static final MaritalStatus WIDOWED = getMaritalStatusFactory().createInstance("widowed", "Widowed");

  private static MaritalStatusFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.MaritalStatus.factory";

  /**
   * Creates an instance of the MaritalStatus enumerated-type class.
   * @param id the unique identifier for the MaritalStatus enumerated-type.
   * @param code a String value for looking up enumerated-types of the MaritalStatus classification.
   * @param description descriptive information concerning the MaritalStatus enumerated-type.
   */
  protected MaritalStatus(final Integer id, final String code, final String description) {
    super(id, code, description);
    MARITAL_STATUS_SET.add(this);
  }

  /**
   * Creates an instance of the MaritalStatus enumerated-type class.
   * @param id the unique identifier for the MaritalStatus enumerated-type.
   * @param code a String value for looking up enumerated-types of the MaritalStatus classification.
   * @param description descriptive information concerning the MaritalStatus enumerated-type.
   * @param externalCode the external code for the MaritalStatus enumerated-type.
   */
  protected MaritalStatus(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    MARITAL_STATUS_SET.add(this);
  }

  /**
   * Returns the instance of the MaritalStatusFactory class to create instances of MaritalStatus enumerated-types.
   * @return an instance of the MaritalStatusFactory class for creating MaritalStatus enumerated-type instances.
   */
  protected static MaritalStatusFactory getMaritalStatusFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (MaritalStatus.class) {
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
  public static MaritalStatus getByCode(final String code) {
    return getInstance(MARITAL_STATUS_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static MaritalStatus getByDescription(final String description) {
    return getInstance(MARITAL_STATUS_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static MaritalStatus getByExternalCode(final String externalCode) {
    return getInstance(MARITAL_STATUS_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static MaritalStatus getById(final Integer id) {
    return getInstance(MARITAL_STATUS_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<MaritalStatus> values() {
    return Collections.unmodifiableSet(MARITAL_STATUS_SET);
  }

  protected interface MaritalStatusFactory extends EnumFactory<MaritalStatus> {

    public MaritalStatus createInstance(String code, String description);

    public MaritalStatus createInstance(String code, String description, String externalCode);

  }

  static final class DefaultMaritalStatusFactory implements MaritalStatusFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultMaritalStatusFactory() {
    }

    public MaritalStatus createInstance(final String code, final String description) {
      return new MaritalStatus(getNextSequence(), code, description);
    }

    public MaritalStatus createInstance(final String code, final String description, final String externalCode) {
      return new MaritalStatus(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
