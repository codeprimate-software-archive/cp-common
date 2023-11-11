/*
 * Relationship.java (c) 21 October 2004
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

public class Relationship extends AbstractEnum {

  private static final Set<Relationship> RELATIONSHIP_SET = new HashSet<Relationship>(37);

  public static final Relationship AUNT = getRelationshipFactory().createInstance("aunt", "Aunt");
  public static final Relationship BROTHER = getRelationshipFactory().createInstance("brother", "Brother");
  public static final Relationship BOYFRIEND = getRelationshipFactory().createInstance("boyfriend", "Boyfriend");
  public static final Relationship CHILD = getRelationshipFactory().createInstance("child", "Child");
  public static final Relationship COUSIN = getRelationshipFactory().createInstance("cousin", "Cousin");
  public static final Relationship DAUGHTER = getRelationshipFactory().createInstance("daughter", "Daughter");
  public static final Relationship FATHER = getRelationshipFactory().createInstance("father", "Father");
  public static final Relationship FIANCE = getRelationshipFactory().createInstance("fiance", "Fiance");
  public static final Relationship FRIEND = getRelationshipFactory().createInstance("friend", "Friend");
  public static final Relationship GIRLFRIEND = getRelationshipFactory().createInstance("girlfriend", "Girlfriend");
  public static final Relationship GRANDDAUGHTER = getRelationshipFactory().createInstance("granddaughter", "Granddaughter");
  public static final Relationship GRANDFATHER = getRelationshipFactory().createInstance("grandfather", "Grandfather");
  public static final Relationship GRANDMOTHER = getRelationshipFactory().createInstance("grandmother", "Grandmother");
  public static final Relationship GRANDSON = getRelationshipFactory().createInstance("grandson", "Grandson");
  public static final Relationship GREAT_GRANDDAUGHTER = getRelationshipFactory().createInstance("great granddaughter", "Great Granddaughter");
  public static final Relationship GREAT_GRANDFATHER = getRelationshipFactory().createInstance("great grandfather", "Great Grandfather");
  public static final Relationship GREAT_GRANDMOTHER = getRelationshipFactory().createInstance("great grandmother", "Great Grandmother");
  public static final Relationship GREAT_GRANDSON = getRelationshipFactory().createInstance("great grandson", "Great Grandson");
  public static final Relationship HALF_BROTHER = getRelationshipFactory().createInstance("half brother", "Half Brother");
  public static final Relationship HALF_SISTER = getRelationshipFactory().createInstance("half sister", "Half Sister");
  public static final Relationship HUSBAND = getRelationshipFactory().createInstance("husband", "Husband");
  public static final Relationship MOTHER = getRelationshipFactory().createInstance("mother", "Mother");
  public static final Relationship NEPHEW = getRelationshipFactory().createInstance("nephew", "Nephew");
  public static final Relationship PARENT = getRelationshipFactory().createInstance("parent", "Parent");
  public static final Relationship PARTNER = getRelationshipFactory().createInstance("partner", "Partner");
  public static final Relationship SELF = getRelationshipFactory().createInstance("self", "Self");
  public static final Relationship SIBLING = getRelationshipFactory().createInstance("sibling", "Sibling");
  public static final Relationship SISTER = getRelationshipFactory().createInstance("sister", "Sister");
  public static final Relationship SON = getRelationshipFactory().createInstance("son", "Son");
  public static final Relationship STEP_BROTHER = getRelationshipFactory().createInstance("step brother", "Step Brother");
  public static final Relationship STEP_FATHER = getRelationshipFactory().createInstance("step father", "Step Father");
  public static final Relationship STEP_MOTHER = getRelationshipFactory().createInstance("step mother", "Step Mother");
  public static final Relationship STEP_SISTER = getRelationshipFactory().createInstance("step sister", "Step Sister");
  public static final Relationship UNCLE = getRelationshipFactory().createInstance("uncle", "Uncle");
  public static final Relationship WIFE = getRelationshipFactory().createInstance("wife", "Wife");

  private static RelationshipFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Relationship.factory";

  /**
   * Creates an instance of the Relationship enumerated-type class.
   * @param id the unique identifier for the Relationship enumerated-type.
   * @param code a String value for looking up enumerated-types of the Relationship classification.
   * @param description descriptive information concerning the Relationship enumerated-type.
   */
  protected Relationship(final Integer id, final String code, final String description) {
    super(id, code, description);
    RELATIONSHIP_SET.add(this);
  }

  /**
   * Creates an instance of the Relationship enumerated-type class.
   * @param id the unique identifier for the Relationship enumerated-type.
   * @param code a String value for looking up enumerated-types of the Relationship classification.
   * @param description descriptive information concerning the Relationship enumerated-type.
   * @param externalCode the external code for the Relationship enumerated-type.
   */
  protected Relationship(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    RELATIONSHIP_SET.add(this);
  }

  /**
   * Returns the instance of the RelationshipFactory class to create instances of Relationship enumerated-types.
   * @return an instance of the RelationshipFactory class for creating Relationship enumerated-type instances.
   */
  protected static RelationshipFactory getRelationshipFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Relationship.class) {
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
  public static Relationship getByCode(final String code) {
    return getInstance(RELATIONSHIP_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Relationship getByDescription(final String description) {
    return getInstance(RELATIONSHIP_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Relationship getByExternalCode(final String externalCode) {
    return getInstance(RELATIONSHIP_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Relationship getById(final Integer id) {
    return getInstance(RELATIONSHIP_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Relationship> values() {
    return Collections.unmodifiableSet(RELATIONSHIP_SET);
  }

  protected interface RelationshipFactory extends EnumFactory<Relationship> {

    public Relationship createInstance(String code, String description);

    public Relationship createInstance(String code, String description, String externalCode);

  }

  static final class DefaultRelationshipFactory implements RelationshipFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultRelationshipFactory() {
    }

    public Relationship createInstance(final String code, final String description) {
      return new Relationship(getNextSequence(), code, description);
    }

    public Relationship createInstance(final String code, final String description, final String externalCode) {
      return new Relationship(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
