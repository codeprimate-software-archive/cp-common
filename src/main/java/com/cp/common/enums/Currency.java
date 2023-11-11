/*
 * Currency.java (c) 24 October 2004
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

public class Currency extends AbstractEnum {

  private static final Set<Currency> CURRENCY_SET = new HashSet<Currency>(171);

  public static final Currency AFGHANISTAN_AFGHANI = getCurrencyFactory().createInstance("AFA", "Afghanistan afghani");
  public static final Currency ALBANIAN_LEK = getCurrencyFactory().createInstance("ALL", "Albanian lek");
  public static final Currency ALGERIAN_DINAR = getCurrencyFactory().createInstance("DZD", "Algerian dinar");
  public static final Currency ANGOLAN_KWANZA_REAJUSTADO = getCurrencyFactory().createInstance("AOR", "Angolan kwanza reajustado");
  public static final Currency ARGENTINE_PESO = getCurrencyFactory().createInstance("ARS", "Argentine peso");
  public static final Currency ARMENIAN_DRAM = getCurrencyFactory().createInstance("AMD", "Armenian dram");
  public static final Currency ARUBAN_GUILDER = getCurrencyFactory().createInstance("AWG", "Aruban guilder");
  public static final Currency AUSTRALIAN_DOLLAR = getCurrencyFactory().createInstance("AUD", "Australian dollar");
  public static final Currency AZERBAIJANIAN_MANAT = getCurrencyFactory().createInstance("AZM", "Azerbaijanian manat");
  public static final Currency BAHAMIAN_DOLLAR = getCurrencyFactory().createInstance("BSD", "Bahamian dollar");
  public static final Currency BAHRAINI_DINAR = getCurrencyFactory().createInstance("BHD", "Bahraini dinar");
  public static final Currency BANGLADESHI_TAKA = getCurrencyFactory().createInstance("BDT", "Bangladeshi taka");
  public static final Currency BARBADOS_DOLLAR = getCurrencyFactory().createInstance("BBD", "Barbados dollar");
  public static final Currency BELARUSIAN_RUBLE = getCurrencyFactory().createInstance("BYR", "Belarusian ruble");
  public static final Currency BELIZE_DOLLAR = getCurrencyFactory().createInstance("BZD", "Belize dollar");
  public static final Currency BERMUDIAN_DOLLAR = getCurrencyFactory().createInstance("BMD", "Bermudian dollar");
  public static final Currency BHUTAN_NGULTRUM = getCurrencyFactory().createInstance("BTN", "Bhutan ngultrum");
  public static final Currency BOLIVIAN_BOLIVIANO = getCurrencyFactory().createInstance("BOB", "Bolivian boliviano");
  public static final Currency BOTSWANA_PULA = getCurrencyFactory().createInstance("BWP", "Botswana pula");
  public static final Currency BRAZILIAN_REAL = getCurrencyFactory().createInstance("BRL", "Brazilian real");
  public static final Currency BRITISH_POUND = getCurrencyFactory().createInstance("GBP", "British pound");
  public static final Currency BRUNEI_DOLLAR = getCurrencyFactory().createInstance("BND", "Brunei dollar");
  public static final Currency BULGARIAN_LEV = getCurrencyFactory().createInstance("BGN", "Bulgarian lev");
  public static final Currency BURUNDI_FRANC = getCurrencyFactory().createInstance("BIF", "Burundi franc");
  public static final Currency CFA_FRANC_BCEAO = getCurrencyFactory().createInstance("XOF", "CFA franc BCEAO");
  public static final Currency CFA_FRANC_BEAC = getCurrencyFactory().createInstance("XAF", "CFA franc BEAC");
  public static final Currency CFP_FRANC = getCurrencyFactory().createInstance("XPF", "CFP franc");
  public static final Currency CAMBODIAN_RIEL = getCurrencyFactory().createInstance("KHR", "Cambodian riel");
  public static final Currency CANADIAN_DOLLAR = getCurrencyFactory().createInstance("CAD", "Canadian dollar");
  public static final Currency CAPE_VERDE_ESCUDO = getCurrencyFactory().createInstance("CVE", "Cape Verde escudo");
  public static final Currency CAYMAN_ISLANDS_DOLLAR = getCurrencyFactory().createInstance("KYD", "Cayman Islands dollar");
  public static final Currency CHILEAN_PESO = getCurrencyFactory().createInstance("CLP", "Chilean peso");
  public static final Currency CHINESE_YUAN_RENMINBI = getCurrencyFactory().createInstance("CNY", "Chinese yuan renminbi");
  public static final Currency COLOMBIAN_PESO = getCurrencyFactory().createInstance("COP", "Colombian peso");
  public static final Currency COMOROS_FRANC = getCurrencyFactory().createInstance("KMF", "Comoros franc");
  public static final Currency CONGOLESE_FRANC = getCurrencyFactory().createInstance("CDF", "Congolese franc");
  public static final Currency COSTA_RICAN_COLON = getCurrencyFactory().createInstance("CRC", "Costa Rican colon");
  public static final Currency CROATIAN_KUNA = getCurrencyFactory().createInstance("HRK", "Croatian kuna");
  public static final Currency CUBAN_PESO = getCurrencyFactory().createInstance("CUP", "Cuban peso");
  public static final Currency CYPRIOT_POUND = getCurrencyFactory().createInstance("CYP", "Cypriot pound");
  public static final Currency CZECH_KORUNA = getCurrencyFactory().createInstance("CZK", "Czech koruna");
  public static final Currency DANISH_KRONE = getCurrencyFactory().createInstance("DKK", "Danish krone");
  public static final Currency DJIBOUTI_FRANC = getCurrencyFactory().createInstance("DJF", "Djibouti franc");
  public static final Currency DOMINICAN_PESO = getCurrencyFactory().createInstance("DOP", "Dominican peso");
  public static final Currency EU_EURO = getCurrencyFactory().createInstance("EUR", "EU euro");
  public static final Currency EAST_CARIBBEAN_DOLLAR = getCurrencyFactory().createInstance("XCD", "East Caribbean dollar");
  public static final Currency EGYPTIAN_POUND = getCurrencyFactory().createInstance("EGP", "Egyptian pound");
  public static final Currency EL_SALVADOR_COLON = getCurrencyFactory().createInstance("SVC", "El Salvador colon");
  public static final Currency ERITREAN_NAKFA = getCurrencyFactory().createInstance("ERN", "Eritrean nakfa");
  public static final Currency ESTONIAN_KROON = getCurrencyFactory().createInstance("EEK", "Estonian kroon");
  public static final Currency ETHIOPIAN_BIRR = getCurrencyFactory().createInstance("ETB", "Ethiopian birr");
  public static final Currency FALKLAND_ISLANDS_POUND = getCurrencyFactory().createInstance("FKP", "Falkland Islands pound");
  public static final Currency FIJI_DOLLAR = getCurrencyFactory().createInstance("FJD", "Fiji dollar");
  public static final Currency GAMBIAN_DALASI = getCurrencyFactory().createInstance("GMD", "Gambian dalasi");
  public static final Currency GEORGIAN_LARI = getCurrencyFactory().createInstance("GEL", "Georgian lari");
  public static final Currency GHANAIAN_CEDI = getCurrencyFactory().createInstance("GHC", "Ghanaian cedi");
  public static final Currency GIBRALTAR_POUND = getCurrencyFactory().createInstance("GIP", "Gibraltar pound");
  public static final Currency GOLD_OUNCE = getCurrencyFactory().createInstance("XAU", "Gold (ounce)");
  public static final Currency GOLD_FRANC = getCurrencyFactory().createInstance("XFO", "Gold franc");
  public static final Currency GUATEMALAN_QUETZAL = getCurrencyFactory().createInstance("GTQ", "Guatemalan quetzal");
  public static final Currency GUINEAN_FRANC = getCurrencyFactory().createInstance("GNF", "Guinean franc");
  public static final Currency GUYANA_DOLLAR = getCurrencyFactory().createInstance("GYD", "Guyana dollar");
  public static final Currency HAITIAN_GOURDE = getCurrencyFactory().createInstance("HTG", "Haitian gourde");
  public static final Currency HONDURAN_LEMPIRA = getCurrencyFactory().createInstance("HNL", "Honduran lempira");
  public static final Currency HONG_KONG_SAR_DOLLAR = getCurrencyFactory().createInstance("HKD", "Hong Kong SAR dollar");
  public static final Currency HUNGARIAN_FORINT = getCurrencyFactory().createInstance("HUF", "Hungarian forint");
  public static final Currency IMF_SPECIAL_DRAWING_RIGHT = getCurrencyFactory().createInstance("XDR", "IMF special drawing right");
  public static final Currency ICELANDIC_KRONA = getCurrencyFactory().createInstance("ISK", "Icelandic krona");
  public static final Currency INDIAN_RUPEE = getCurrencyFactory().createInstance("INR", "Indian rupee");
  public static final Currency INDONESIAN_RUPIAH = getCurrencyFactory().createInstance("IDR", "Indonesian rupiah");
  public static final Currency IRANIAN_RIAL = getCurrencyFactory().createInstance("IRR", "Iranian rial");
  public static final Currency IRAQI_DINAR = getCurrencyFactory().createInstance("IQD", "Iraqi dinar");
  public static final Currency ISRAELI_NEW_SHEKEL = getCurrencyFactory().createInstance("ILS", "Israeli new shekel");
  public static final Currency JAMAICAN_DOLLAR = getCurrencyFactory().createInstance("JMD", "Jamaican dollar");
  public static final Currency JAPANESE_YEN = getCurrencyFactory().createInstance("JPY", "Japanese yen");
  public static final Currency JORDANIAN_DINAR = getCurrencyFactory().createInstance("JOD", "Jordanian dinar");
  public static final Currency KAZAKH_TENGE = getCurrencyFactory().createInstance("KZT", "Kazakh tenge");
  public static final Currency KENYAN_SHILLING = getCurrencyFactory().createInstance("KES", "Kenyan shilling");
  public static final Currency KUWAITI_DINAR = getCurrencyFactory().createInstance("KWD", "Kuwaiti dinar");
  public static final Currency KYRGYZ_SOM = getCurrencyFactory().createInstance("KGS", "Kyrgyz som");
  public static final Currency LAO_KIP = getCurrencyFactory().createInstance("LAK", "Lao kip");
  public static final Currency LATVIAN_LATS = getCurrencyFactory().createInstance("LVL", "Latvian lats");
  public static final Currency LEBANESE_POUND = getCurrencyFactory().createInstance("LBP", "Lebanese pound");
  public static final Currency LESOTHO_LOTI = getCurrencyFactory().createInstance("LSL", "Lesotho loti");
  public static final Currency LIBERIAN_DOLLAR = getCurrencyFactory().createInstance("LRD", "Liberian dollar");
  public static final Currency LIBYAN_DINAR = getCurrencyFactory().createInstance("LYD", "Libyan dinar");
  public static final Currency LITHUANIAN_LITAS = getCurrencyFactory().createInstance("LTL", "Lithuanian litas");
  public static final Currency MACAO_SAR_PATACA = getCurrencyFactory().createInstance("MOP", "Macao SAR pataca");
  public static final Currency MACEDONIAN_DENAR = getCurrencyFactory().createInstance("MKD", "Macedonian denar");
  public static final Currency MALAGASY_ARIARY = getCurrencyFactory().createInstance("MGA", "Malagasy ariary");
  public static final Currency MALAGASY_FRANC = getCurrencyFactory().createInstance("MGF", "Malagasy franc");
  public static final Currency MALAWI_KWACHA = getCurrencyFactory().createInstance("MWK", "Malawi kwacha");
  public static final Currency MALAYSIAN_RINGGIT = getCurrencyFactory().createInstance("MYR", "Malaysian ringgit");
  public static final Currency MALDIVIAN_RUFIYAA = getCurrencyFactory().createInstance("MVR", "Maldivian rufiyaa");
  public static final Currency MALTESE_LIRA = getCurrencyFactory().createInstance("MTL", "Maltese lira");
  public static final Currency MAURITANIAN_OUGUIYA = getCurrencyFactory().createInstance("MRO", "Mauritanian ouguiya");
  public static final Currency MAURITIUS_RUPEE = getCurrencyFactory().createInstance("MUR", "Mauritius rupee");
  public static final Currency MEXICAN_PESO = getCurrencyFactory().createInstance("MXN", "Mexican peso");
  public static final Currency MOLDOVAN_LEU = getCurrencyFactory().createInstance("MDL", "Moldovan leu");
  public static final Currency MONGOLIAN_TUGRIK = getCurrencyFactory().createInstance("MNT", "Mongolian tugrik");
  public static final Currency MOROCCAN_DIRHAM = getCurrencyFactory().createInstance("MAD", "Moroccan dirham");
  public static final Currency MOZAMBIQUE_METICAL = getCurrencyFactory().createInstance("MZM", "Mozambique metical");
  public static final Currency MYANMAR_KYAT = getCurrencyFactory().createInstance("MMK", "Myanmar kyat");
  public static final Currency NAMIBIAN_DOLLAR = getCurrencyFactory().createInstance("NAD", "Namibian dollar");
  public static final Currency NEPALESE_RUPEE = getCurrencyFactory().createInstance("NPR", "Nepalese rupee");
  public static final Currency NETHERLANDS_ANTILLIAN_GUILDER = getCurrencyFactory().createInstance("ANG", "Netherlands Antillian guilder");
  public static final Currency NEW_ZEALAND_DOLLAR = getCurrencyFactory().createInstance("NZD", "New Zealand dollar");
  public static final Currency NICARAGUAN_CORDOBA_ORO = getCurrencyFactory().createInstance("NIO", "Nicaraguan cordoba oro");
  public static final Currency NIGERIAN_NAIRA = getCurrencyFactory().createInstance("NGN", "Nigerian naira");
  public static final Currency NORTH_KOREAN_WON = getCurrencyFactory().createInstance("KPW", "North Korean won");
  public static final Currency NORWEGIAN_KRONE = getCurrencyFactory().createInstance("NOK", "Norwegian krone");
  public static final Currency OMANI_RIAL = getCurrencyFactory().createInstance("OMR", "Omani rial");
  public static final Currency PAKISTANI_RUPEE = getCurrencyFactory().createInstance("PKR", "Pakistani rupee");
  public static final Currency PALLADIUM_OUNCE = getCurrencyFactory().createInstance("XPD", "Palladium (ounce)");
  public static final Currency PANAMANIAN_BALBOA = getCurrencyFactory().createInstance("PAB", "Panamanian balboa");
  public static final Currency PAPUA_NEW_GUINEA_KINA = getCurrencyFactory().createInstance("PGK", "Papua New Guinea kina");
  public static final Currency PARAGUAYAN_GUARANI = getCurrencyFactory().createInstance("PYG", "Paraguayan guarani");
  public static final Currency PERUVIAN_NUEVO_SOL = getCurrencyFactory().createInstance("PEN", "Peruvian nuevo sol");
  public static final Currency PHILIPPINE_PESO = getCurrencyFactory().createInstance("PHP", "Philippine peso");
  public static final Currency PLATINUM_OUNCE = getCurrencyFactory().createInstance("XPT", "Platinum (ounce)");
  public static final Currency POLISH_ZLOTY = getCurrencyFactory().createInstance("PLN", "Polish zloty");
  public static final Currency QATARI_RIAL = getCurrencyFactory().createInstance("QAR", "Qatari rial");
  public static final Currency ROMANIAN_LEU = getCurrencyFactory().createInstance("ROL", "Romanian leu");
  public static final Currency RUSSIAN_RUBLE = getCurrencyFactory().createInstance("RUB", "Russian ruble");
  public static final Currency RWANDAN_FRANC = getCurrencyFactory().createInstance("RWF", "Rwandan franc");
  public static final Currency SAINT_HELENA_POUND = getCurrencyFactory().createInstance("SHP", "Saint Helena pound");
  public static final Currency SAMOAN_TALA = getCurrencyFactory().createInstance("WST", "Samoan tala");
  public static final Currency SAO_TOME_AND_PRINCIPE_DOBRA = getCurrencyFactory().createInstance("STD", "Sao Tome and Principe dobra");
  public static final Currency SAUDI_RIYAL = getCurrencyFactory().createInstance("SAR", "Saudi riyal");
  public static final Currency SERBIAN_DINAR = getCurrencyFactory().createInstance("CSD", "Serbian dinar");
  public static final Currency SEYCHELLES_RUPEE = getCurrencyFactory().createInstance("SCR", "Seychelles rupee");
  public static final Currency SIERRA_LEONE_LEONE = getCurrencyFactory().createInstance("SLL", "Sierra Leone leone");
  public static final Currency SILVER_OUNCE = getCurrencyFactory().createInstance("XAG", "Silver (ounce)");
  public static final Currency SINGAPORE_DOLLAR = getCurrencyFactory().createInstance("SGD", "Singapore dollar");
  public static final Currency SLOVAK_KORUNA = getCurrencyFactory().createInstance("SKK", "Slovak koruna");
  public static final Currency SLOVENIAN_TOLAR = getCurrencyFactory().createInstance("SIT", "Slovenian tolar");
  public static final Currency SOLOMON_ISLANDS_DOLLAR = getCurrencyFactory().createInstance("SBD", "Solomon Islands dollar");
  public static final Currency SOMALI_SHILLING = getCurrencyFactory().createInstance("SOS", "Somali shilling");
  public static final Currency SOUTH_AFRICAN_RAND = getCurrencyFactory().createInstance("ZAR", "South African rand");
  public static final Currency SOUTH_KOREAN_WON = getCurrencyFactory().createInstance("KRW", "South Korean won");
  public static final Currency SRI_LANKA_RUPEE = getCurrencyFactory().createInstance("LKR", "Sri Lanka rupee");
  public static final Currency SUDANESE_DINAR = getCurrencyFactory().createInstance("SDD", "Sudanese dinar");
  public static final Currency SURINAME_GUILDER = getCurrencyFactory().createInstance("SRG", "Suriname guilder");
  public static final Currency SWAZILAND_LILANGENI = getCurrencyFactory().createInstance("SZL", "Swaziland lilangeni");
  public static final Currency SWEDISH_KRONA = getCurrencyFactory().createInstance("SEK", "Swedish krona");
  public static final Currency SWISS_FRANC = getCurrencyFactory().createInstance("CHF", "Swiss franc");
  public static final Currency SYRIAN_POUND = getCurrencyFactory().createInstance("SYP", "Syrian pound");
  public static final Currency TAIWAN_NEW_DOLLAR = getCurrencyFactory().createInstance("TWD", "Taiwan New dollar");
  public static final Currency TAJIK_SOMONI = getCurrencyFactory().createInstance("TJS", "Tajik somoni");
  public static final Currency TANZANIAN_SHILLING = getCurrencyFactory().createInstance("TZS", "Tanzanian shilling");
  public static final Currency THAI_BAHT = getCurrencyFactory().createInstance("THB", "Thai baht");
  public static final Currency TONGAN_PAANGA = getCurrencyFactory().createInstance("TOP", "Tongan pa´anga");
  public static final Currency TRINIDAD_AND_TOBAGO_DOLLAR = getCurrencyFactory().createInstance("TTD", "Trinidad and Tobago dollar");
  public static final Currency TUNISIAN_DINAR = getCurrencyFactory().createInstance("TND", "Tunisian dinar");
  public static final Currency TURKISH_LIRA = getCurrencyFactory().createInstance("TRL", "Turkish lira");
  public static final Currency TURKMEN_MANAT = getCurrencyFactory().createInstance("TMM", "Turkmen manat");
  public static final Currency UAE_DIRHAM = getCurrencyFactory().createInstance("AED", "UAE dirham");
  public static final Currency UIC_FRANC = getCurrencyFactory().createInstance("XFU", "UIC franc");
  public static final Currency US_DOLLAR = getCurrencyFactory().createInstance("USD", "US dollar");
  public static final Currency UGANDA_NEW_SHILLING = getCurrencyFactory().createInstance("UGX", "Uganda new shilling");
  public static final Currency UKRAINIAN_HRYVNIA = getCurrencyFactory().createInstance("UAH", "Ukrainian hryvnia");
  public static final Currency URUGUAYAN_PESO_URUGUAYO = getCurrencyFactory().createInstance("UYU", "Uruguayan peso uruguayo");
  public static final Currency UZBEKISTANI_SUM = getCurrencyFactory().createInstance("UZS", "Uzbekistani sum");
  public static final Currency VANUATU_VATU = getCurrencyFactory().createInstance("VUV", "Vanuatu vatu");
  public static final Currency VENEZUELAN_BOLIVAR = getCurrencyFactory().createInstance("VEB", "Venezuelan bolivar");
  public static final Currency VIETNAMESE_DONG = getCurrencyFactory().createInstance("VND", "Vietnamese dong");
  public static final Currency YEMENI_RIAL = getCurrencyFactory().createInstance("YER", "Yemeni rial");
  public static final Currency ZAMBIAN_KWACHA = getCurrencyFactory().createInstance("ZMK", "Zambian kwacha");
  public static final Currency ZIMBABWE_DOLLAR = getCurrencyFactory().createInstance("ZWD", "Zimbabwe dollar");

  private static CurrencyFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Currency.factory";

  /**
   * Creates an instance of the Currency enumerated-type class.
   * @param id the unique identifier for the Currency enumerated-type.
   * @param code a String value for looking up enumerated-types of the Currency classification.
   * @param description descriptive information concerning the Currency enumerated-type.
   */
  protected Currency(final Integer id, final String code, final String description) {
    super(id, code, description);
    CURRENCY_SET.add(this);
  }

  /**
   * Creates an instance of the Currency enumerated-type class.
   * @param id the unique identifier for the Currency enumerated-type.
   * @param code a String value for looking up enumerated-types of the Currency classification.
   * @param description descriptive information concerning the Currency enumerated-type.
   * @param externalCode the external code for the Currency enumerated-type.
   */
  protected Currency(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    CURRENCY_SET.add(this);
  }

  /**
   * Returns the instance of the CurrencyFactory class to create instances of Currency enumerated-types.
   * @return an instance of the CurrencyFactory class for creating Currency enumerated-type instances.
   */
  protected static CurrencyFactory getCurrencyFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Currency.class) {
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
  public static Currency getByCode(final String code) {
    return getInstance(CURRENCY_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Currency getByDescription(final String description) {
    return getInstance(CURRENCY_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Currency getByExternalCode(final String externalCode) {
    return getInstance(CURRENCY_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Currency getById(final Integer id) {
    return getInstance(CURRENCY_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Currency> values() {
    return Collections.unmodifiableSet(CURRENCY_SET);
  }

  protected interface CurrencyFactory extends EnumFactory<Currency> {

    public Currency createInstance(String code, String description);

    public Currency createInstance(String code, String description, String externalCode);

  }

  static final class DefaultCurrencyFactory implements CurrencyFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultCurrencyFactory() {
    }

    public Currency createInstance(final String code, final String description) {
      return new Currency(getNextSequence(), code, description);
    }

    public Currency createInstance(final String code, final String description, final String externalCode) {
      return new Currency(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
