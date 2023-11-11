/*
 * Contact.java (c) 22 October 2004
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

public class Contact extends AbstractEnum {

  private static final Set<Contact> CONTACT_SET = new HashSet<Contact>(7);

  public static final Contact CELL = getContactFactory().createInstance("cell", "Cell");
  public static final Contact EMAIL = getContactFactory().createInstance("email", "Email");
  public static final Contact FAX = getContactFactory().createInstance("fax", "Fax");
  public static final Contact IN_PERSON = getContactFactory().createInstance("in-person", "In-Person");
  public static final Contact MAIL = getContactFactory().createInstance("mail", "Mail");
  public static final Contact PHONE = getContactFactory().createInstance("phone", "Phone");

  private static ContactFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Contact.factory";

  /**
   * Creates an instance of the Contact enumerated-type class.
   * @param id the unique identifier for the Contact enumerated-type.
   * @param code a String value for looking up enumerated-types of the Contact classification.
   * @param description descriptive information concerning the Contact enumerated-type.
   */
  protected Contact(final Integer id, final String code, final String description) {
    super(id, code, description);
    CONTACT_SET.add(this);
  }

  /**
   * Creates an instance of the Contact enumerated-type class.
   * @param id the unique identifier for the Contact enumerated-type.
   * @param code a String value for looking up enumerated-types of the Contact classification.
   * @param description descriptive information concerning the Contact enumerated-type.
   * @param externalCode the external code for the Contact enumerated-type.
   */
  protected Contact(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    CONTACT_SET.add(this);
  }

  /**
   * Returns the instance of the ContactFactory class to create instances of Contact enumerated-types.
   * @return an instance of the ContactFactory class for creating Contact enumerated-type instances.
   */
  protected static ContactFactory getContactFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Contact.class) {
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
  public static Contact getByCode(final String code) {
    return getInstance(CONTACT_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Contact getByDescription(final String description) {
    return getInstance(CONTACT_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Contact getByExternalCode(final String externalCode) {
    return getInstance(CONTACT_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Contact getById(final Integer id) {
    return getInstance(CONTACT_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Contact> values() {
    return Collections.unmodifiableSet(CONTACT_SET);
  }

  protected interface ContactFactory extends EnumFactory<Contact> {

    public Contact createInstance(String code, String description);

    public Contact createInstance(String code, String description, String externalCode);

  }

  static final class DefaultContactFactory implements ContactFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultContactFactory() {
    }

    public Contact createInstance(final String code, final String description) {
      return new Contact(getNextSequence(), code, description);
    }

    public Contact createInstance(final String code, final String description, final String externalCode) {
      return new Contact(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
