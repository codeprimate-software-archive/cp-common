/*
 * Language.java (c) 18 October 2004
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

public class Language extends AbstractEnum {

  private static final Set<Language> LANGUAGE_SET = new HashSet<Language>(23);

  public static final Language ARABIC = getLanguageFactory().createInstance("arabic", "Arabic");
  public static final Language AUSTRIAN = getLanguageFactory().createInstance("austrian", "Austrian");
  public static final Language BANGALI = getLanguageFactory().createInstance("bangali", "Bangali");
  public static final Language BULGARIAN = getLanguageFactory().createInstance("bulgarian", "Bulgarian");
  public static final Language CHINESE = getLanguageFactory().createInstance("chinese", "Chinese");
  public static final Language DUTCH = getLanguageFactory().createInstance("dutch", "Dutch");
  public static final Language ENGLISH = getLanguageFactory().createInstance("english", "English");
  public static final Language ESTONIAN = getLanguageFactory().createInstance("estonian", "Estonian");
  public static final Language FRENCH = getLanguageFactory().createInstance("french", "French");
  public static final Language GERMAN = getLanguageFactory().createInstance("german", "German");
  public static final Language GREEK = getLanguageFactory().createInstance("greek", "Greek");
  public static final Language HINDI = getLanguageFactory().createInstance("hindi", "Hindi");
  public static final Language IRISH = getLanguageFactory().createInstance("irish", "Irish");
  public static final Language ITALIAN = getLanguageFactory().createInstance("italian", "Italian");
  public static final Language JAPANESE = getLanguageFactory().createInstance("japanese", "Japanese");
  public static final Language KOREAN = getLanguageFactory().createInstance("korean", "Korean");
  public static final Language PORTUGUESE = getLanguageFactory().createInstance("portuguese", "Portuguese");
  public static final Language RUSSIAN = getLanguageFactory().createInstance("russian", "Russian");
  public static final Language SPANISH = getLanguageFactory().createInstance("spanish", "Spanish");
  public static final Language TURKISH = getLanguageFactory().createInstance("turkish", "Turkish");
  public static final Language WELSH = getLanguageFactory().createInstance("welsh", "Welsh");
  public static final Language YIDDISH = getLanguageFactory().createInstance("yiddish", "Yiddish");

  private static LanguageFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Language.factory";

  /**
   * Creates an instance of the Language enumerated-type class.
   * @param id the unique identifier for the Language enumerated-type.
   * @param code a String value for looking up enumerated-types of the Language classification.
   * @param description descriptive information concerning the Language enumerated-type.
   */
  protected Language(final Integer id, final String code, final String description) {
    super(id, code, description);
    LANGUAGE_SET.add(this);
  }

  /**
   * Creates an instance of the Language enumerated-type class.
   * @param id the unique identifier for the Language enumerated-type.
   * @param code a String value for looking up enumerated-types of the Language classification.
   * @param description descriptive information concerning the Language enumerated-type.
   * @param externalCode the external code for the Language enumerated-type.
   */
  protected Language(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    LANGUAGE_SET.add(this);
  }

  /**
   * Returns the instance of the LanguageFactory class to create instances of Language enumerated-types.
   * @return an instance of the LanguageFactory class for creating Language enumerated-type instances.
   */
  protected static LanguageFactory getLanguageFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Language.class) {
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
  public static Language getByCode(final String code) {
    return getInstance(LANGUAGE_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Language getByDescription(final String description) {
    return getInstance(LANGUAGE_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Language getByExternalCode(final String externalCode) {
    return getInstance(LANGUAGE_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Language getById(final Integer id) {
    return getInstance(LANGUAGE_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Language> values() {
    return Collections.unmodifiableSet(LANGUAGE_SET);
  }

  protected interface LanguageFactory extends EnumFactory<Language> {

    public Language createInstance(String code, String description);

    public Language createInstance(String code, String description, String externalCode);

  }

  static final class DefaultLanguageFactory implements LanguageFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultLanguageFactory() {
    }

    public Language createInstance(final String code, final String description) {
      return new Language(getNextSequence(), code, description);
    }

    public Language createInstance(final String code, final String description, final String externalCode) {
      return new Language(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
