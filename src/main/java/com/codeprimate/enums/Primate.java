/*
 * Primate.java (c) 21 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.6.2
 */

package com.codeprimate.enums;

import com.cp.common.enums.AbstractEnum;
import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Primate extends AbstractEnum {

  private static final Set<Primate> PRIMATE_SET = new HashSet<Primate>(7);

  public static final Primate APE = getPrimateFactory().createInstance("ape", "Ape");
  public static final Primate BABOON = getPrimateFactory().createInstance("baboon", "Baboon");
  public static final Primate CHIMPANZEE = getPrimateFactory().createInstance("chimpanzee", "Chimpanzee");
  public static final Primate GORILLA = getPrimateFactory().createInstance("goriall", "Gorilla");
  public static final Primate MONKEY = getPrimateFactory().createInstance("monkey", "Monkey");
  public static final Primate ORANGUTANG = getPrimateFactory().createInstance("orangutang", "Orangutang");

  private static PrimateFactory factory;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.codeprimate.enums.Primate.factory";

  /**
   * Creates an instance of the Primate enumerated-type class.
   * @param id the unique identifier for the Primate enumerated-type.
   * @param code a String value for looking up enumerated-types of the Primate classification.
   * @param description descriptive information concerning the Primate enumerated-type.
   */
  protected Primate(final Integer id, final String code, final String description) {
    super(id, code, description);
    PRIMATE_SET.add(this);
  }

  /**
   * Creates an instance of the Primate enumerated-type class.
   * @param id the unique identifier for the Primate enumerated-type.
   * @param code a String value for looking up enumerated-types of the Primate classification.
   * @param description descriptive information concerning the Primate enumerated-type.
   * @param externalCode the external code for the Primate enumerated-type.
   */
  protected Primate(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    PRIMATE_SET.add(this);
  }

  /**
   * Returns the instance of the PrimateFactory class to create instances of Primate enumerated-types.
   * @return an instance of the PrimateFactory class for creating Primate enumerated-type instances.
   */
  protected static PrimateFactory getPrimateFactory() {
    if (ObjectUtil.isNull(factory)) {
      synchronized (Primate.class) {
        if (ObjectUtil.isNull(factory)) {
          factory = getFactory(FACTORY_PROPERTY_KEY);
        }
      }
    }
    return factory;
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified code.
   * @param code the String code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified code.
   * @throws java.lang.IllegalArgumentException if code is not a valid enumerated-type code.
   */
  public static Primate getByCode(final String code) {
    return getInstance(PRIMATE_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Primate getByDescription(final String description) {
    return getInstance(PRIMATE_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Primate getByExternalCode(final String externalCode) {
    return getInstance(PRIMATE_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Primate getById(final Integer id) {
    return getInstance(PRIMATE_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Primate> values() {
    return Collections.unmodifiableSet(PRIMATE_SET);
  }

  protected interface PrimateFactory extends EnumFactory<Primate> {

    public Primate createInstance(String code, String description);

    public Primate createInstance(String code, String description, String externalCode);
  }

  static final class DefaultPrimateFactory implements PrimateFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultPrimateFactory() {
    }

    public Primate createInstance(final String code, final String description) {
      return new Primate(getNextSequence(), code, description);
    }

    public Primate createInstance(final String code, final String description, final String externalCode) {
      return new Primate(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
