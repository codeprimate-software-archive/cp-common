/*
 * MockEnum.java (c) 23 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.21
 * @see com.cp.common.enums.AbstractEnum
 */

package com.cp.common.test.mock;

import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MockEnum extends com.cp.common.enums.AbstractEnum {

  private static final Set<MockEnum> MOCK_ENUM_SET = new HashSet<MockEnum>(5);

  public static final MockEnum MOCK_ENUMERATED_TYPE_ONE = getMockEnumFactory().createInstance("MET1", "Mock Enumerated Type One");
  public static final MockEnum MOCK_ENUMERATED_TYPE_TWO = getMockEnumFactory().createInstance("MET2", "Mock Enumerated Type Two");
  public static final MockEnum MOCK_ENUMERATED_TYPE_ZERO = getMockEnumFactory().createInstance("MET0", "Mock Enumerated Type Zero");

  private static MockEnumFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.test.mock.MockEnum.factory";

  /**
   * Creates an instance of the MockEnum enumerated-type class.
   * @param id the unique identifier for the MockEnum enumerated-type.
   * @param code a String value for looking up enumerated-types of the MockEnum classification.
   * @param description descriptive information concerning the MockEnum enumerated-type.
   */
  protected MockEnum(final Integer id, final String code, final String description) {
    super(id, code, description);
    MOCK_ENUM_SET.add(this);
  }

  /**
   * Creates an instance of the MockEnum enumerated-type class.
   * @param id the unique identifier for the MockEnum enumerated-type.
   * @param code a String value for looking up enumerated-types of the MockEnum classification.
   * @param description descriptive information concerning the MockEnum enumerated-type.
   * @param externalCode the external code for the MockEnum enumerated-type.
   */
  protected MockEnum(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    MOCK_ENUM_SET.add(this);
  }

  /**
   * Returns the instance of the MockEnumFactory class to create instances of MockEnum enumerated-types.
   * @return an instance of the MockEnumFactory class for creating MockEnum enumerated-type instances.
   */
  protected static MockEnumFactory getMockEnumFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (MockEnum.class) {
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
  public static MockEnum getByCode(final String code) {
    return getInstance(MOCK_ENUM_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static MockEnum getByDescription(final String description) {
    return getInstance(MOCK_ENUM_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static MockEnum getByExternalCode(final String externalCode) {
    return getInstance(MOCK_ENUM_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static MockEnum getById(final Integer id) {
    return getInstance(MOCK_ENUM_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<MockEnum> values() {
    return Collections.unmodifiableSet(MOCK_ENUM_SET);
  }

  protected interface MockEnumFactory extends EnumFactory<MockEnum> {

    public MockEnum createInstance(String code, String description);

    public MockEnum createInstance(String code, String description, String externalCode);

  }

  static final class DefaultMockEnumFactory implements MockEnumFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultMockEnumFactory() {
    }

    public MockEnum createInstance(final String code, final String description) {
      return new MockEnum(getNextSequence(), code, description);
    }

    public MockEnum createInstance(final String code, final String description, final String externalCode) {
      return new MockEnum(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return sequence++;
    }
  }

}
