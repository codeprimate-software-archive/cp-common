/*
 * Country.java (c) 19 October 2004
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

public class Country extends AbstractEnum {

  private static final Set<Country> COUNTRY_SET = new HashSet<Country>(241);

  public static final Country AFGHANISTAN = getCountryFactory().createInstance("AF", "AFGHANISTAN");
  public static final Country ALAND_ISLANDS = getCountryFactory().createInstance("AX", "ÅLAND ISLANDS");
  public static final Country ALBANIA = getCountryFactory().createInstance("AL", "ALBANIA");
  public static final Country ALGERIA = getCountryFactory().createInstance("DZ", "ALGERIA");
  public static final Country AMERICAN_SAMOA = getCountryFactory().createInstance("AS", "AMERICAN SAMOA");
  public static final Country ANDORRA = getCountryFactory().createInstance("AD", "ANDORRA");
  public static final Country ANGOLA = getCountryFactory().createInstance("AO", "ANGOLA");
  public static final Country ANGUILLA = getCountryFactory().createInstance("AI", "ANGUILLA");
  public static final Country ANTARCTICA = getCountryFactory().createInstance("AQ", "ANTARCTICA");
  public static final Country ANTIGUA_AND_BARBUDA = getCountryFactory().createInstance("AG", "ANTIGUA AND BARBUDA");
  public static final Country ARGENTINA = getCountryFactory().createInstance("AR", "ARGENTINA");
  public static final Country ARMENIA = getCountryFactory().createInstance("AM", "ARMENIA");
  public static final Country ARUBA = getCountryFactory().createInstance("AW", "ARUBA");
  public static final Country AUSTRALIA = getCountryFactory().createInstance("AU", "AUSTRALIA");
  public static final Country AUSTRIA = getCountryFactory().createInstance("AT", "AUSTRIA");
  public static final Country AZERBAIJAN = getCountryFactory().createInstance("AZ", "AZERBAIJAN");
  public static final Country BAHAMAS = getCountryFactory().createInstance("BS", "BAHAMAS");
  public static final Country BAHRAIN = getCountryFactory().createInstance("BH", "BAHRAIN");
  public static final Country BANGLADESH = getCountryFactory().createInstance("BD", "BANGLADESH");
  public static final Country BARBADOS = getCountryFactory().createInstance("BB", "BARBADOS");
  public static final Country BELARUS = getCountryFactory().createInstance("BY", "BELARUS");
  public static final Country BELGIUM = getCountryFactory().createInstance("BE", "BELGIUM");
  public static final Country BELIZE = getCountryFactory().createInstance("BZ", "BELIZE");
  public static final Country BENIN = getCountryFactory().createInstance("BJ", "BENIN");
  public static final Country BERMUDA = getCountryFactory().createInstance("BM", "BERMUDA");
  public static final Country BHUTAN = getCountryFactory().createInstance("BT", "BHUTAN");
  public static final Country BOLIVIA = getCountryFactory().createInstance("BO", "BOLIVIA");
  public static final Country BOSNIA_AND_HERZEGOVINA = getCountryFactory().createInstance("BA", "BOSNIA AND HERZEGOVINA");
  public static final Country BOTSWANA = getCountryFactory().createInstance("BW", "BOTSWANA");
  public static final Country BOUVET_ISLAND = getCountryFactory().createInstance("BV", "BOUVET ISLAND");
  public static final Country BRAZIL = getCountryFactory().createInstance("BR", "BRAZIL");
  public static final Country BRITISH_INDIAN_OCEAN_TERRITORY = getCountryFactory().createInstance("IO", "BRITISH INDIAN OCEAN TERRITORY");
  public static final Country BRUNEI_DARUSSALAM = getCountryFactory().createInstance("BN", "BRUNEI DARUSSALAM");
  public static final Country BULGARIA = getCountryFactory().createInstance("BG", "BULGARIA");
  public static final Country BURKINA_FASO = getCountryFactory().createInstance("BF", "BURKINA FASO");
  public static final Country BURUNDI = getCountryFactory().createInstance("BI", "BURUNDI");
  public static final Country CAMBODIA = getCountryFactory().createInstance("KH", "CAMBODIA");
  public static final Country CAMEROON = getCountryFactory().createInstance("CM", "CAMEROON");
  public static final Country CANADA = getCountryFactory().createInstance("CA", "CANADA");
  public static final Country CAPE_VERDE = getCountryFactory().createInstance("CV", "CAPE VERDE");
  public static final Country CAYMAN_ISLANDS = getCountryFactory().createInstance("KY", "CAYMAN ISLANDS");
  public static final Country CENTRAL_AFRICAN_REPUBLIC = getCountryFactory().createInstance("CF", "CENTRAL AFRICAN REPUBLIC");
  public static final Country CHAD = getCountryFactory().createInstance("TD", "CHAD");
  public static final Country CHILE = getCountryFactory().createInstance("CL", "CHILE");
  public static final Country CHINA = getCountryFactory().createInstance("CN", "CHINA");
  public static final Country CHRISTMAS_ISLAND = getCountryFactory().createInstance("CX", "CHRISTMAS ISLAND");
  public static final Country COCOS_KEELING_ISLANDS = getCountryFactory().createInstance("CC", "COCOS (KEELING) ISLANDS");
  public static final Country COLOMBIA = getCountryFactory().createInstance("CO", "COLOMBIA");
  public static final Country COMOROS = getCountryFactory().createInstance("KM", "COMOROS");
  public static final Country CONGO = getCountryFactory().createInstance("CG", "CONGO");
  public static final Country CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE = getCountryFactory().createInstance("CD", "CONGO, THE DEMOCRATIC REPUBLIC OF THE");
  public static final Country COOK_ISLANDS = getCountryFactory().createInstance("CK", "COOK ISLANDS");
  public static final Country COSTA_RICA = getCountryFactory().createInstance("CR", "COSTA RICA");
  public static final Country COTE_DIVOIRE = getCountryFactory().createInstance("CI", "COTE D'IVOIRE");
  public static final Country CROATIA = getCountryFactory().createInstance("HR", "CROATIA");
  public static final Country CUBA = getCountryFactory().createInstance("CU", "CUBA");
  public static final Country CYPRUS = getCountryFactory().createInstance("CY", "CYPRUS");
  public static final Country CZECH_REPUBLIC = getCountryFactory().createInstance("CZ", "CZECH REPUBLIC");
  public static final Country DENMARK = getCountryFactory().createInstance("DK", "DENMARK");
  public static final Country DJIBOUTI = getCountryFactory().createInstance("DJ", "DJIBOUTI");
  public static final Country DOMINICA = getCountryFactory().createInstance("DM", "DOMINICA");
  public static final Country DOMINICAN_REPUBLIC = getCountryFactory().createInstance("DO", "DOMINICAN REPUBLIC");
  public static final Country ECUADOR = getCountryFactory().createInstance("EC", "ECUADOR");
  public static final Country EGYPT = getCountryFactory().createInstance("EG", "EGYPT");
  public static final Country EL_SALVADOR = getCountryFactory().createInstance("SV", "EL SALVADOR");
  public static final Country EQUATORIAL_GUINEA = getCountryFactory().createInstance("GQ", "EQUATORIAL GUINEA");
  public static final Country ERITREA = getCountryFactory().createInstance("ER", "ERITREA");
  public static final Country ESTONIA = getCountryFactory().createInstance("EE", "ESTONIA");
  public static final Country ETHIOPIA = getCountryFactory().createInstance("ET", "ETHIOPIA");
  public static final Country FALKLAND_ISLANDS_MALVINAS = getCountryFactory().createInstance("FK", "FALKLAND ISLANDS (MALVINAS)");
  public static final Country FAROE_ISLANDS = getCountryFactory().createInstance("FO", "FAROE ISLANDS");
  public static final Country FIJI = getCountryFactory().createInstance("FJ", "FIJI");
  public static final Country FINLAND = getCountryFactory().createInstance("FI", "FINLAND");
  public static final Country FRANCE = getCountryFactory().createInstance("FR", "FRANCE");
  public static final Country FRENCH_GUIANA = getCountryFactory().createInstance("GF", "FRENCH GUIANA");
  public static final Country FRENCH_POLYNESIA = getCountryFactory().createInstance("PF", "FRENCH POLYNESIA");
  public static final Country FRENCH_SOUTHERN_TERRITORIES = getCountryFactory().createInstance("TF", "FRENCH SOUTHERN TERRITORIES");
  public static final Country GABON = getCountryFactory().createInstance("GA", "GABON");
  public static final Country GAMBIA = getCountryFactory().createInstance("GM", "GAMBIA");
  public static final Country GEORGIA = getCountryFactory().createInstance("GE", "GEORGIA");
  public static final Country GERMANY = getCountryFactory().createInstance("DE", "GERMANY");
  public static final Country GHANA = getCountryFactory().createInstance("GH", "GHANA");
  public static final Country GIBRALTAR = getCountryFactory().createInstance("GI", "GIBRALTAR");
  public static final Country GREECE = getCountryFactory().createInstance("GR", "GREECE");
  public static final Country GREENLAND = getCountryFactory().createInstance("GL", "GREENLAND");
  public static final Country GRENADA = getCountryFactory().createInstance("GD", "GRENADA");
  public static final Country GUADELOUPE = getCountryFactory().createInstance("GP", "GUADELOUPE");
  public static final Country GUAM = getCountryFactory().createInstance("GU", "GUAM");
  public static final Country GUATEMALA = getCountryFactory().createInstance("GT", "GUATEMALA");
  public static final Country GUINEA = getCountryFactory().createInstance("GN", "GUINEA");
  public static final Country GUINEABISSAU = getCountryFactory().createInstance("GW", "GUINEA-BISSAU");
  public static final Country GUYANA = getCountryFactory().createInstance("GY", "GUYANA");
  public static final Country HAITI = getCountryFactory().createInstance("HT", "HAITI");
  public static final Country HEARD_ISLAND_AND_MCDONALD_ISLANDS = getCountryFactory().createInstance("HM", "HEARD ISLAND AND MCDONALD ISLANDS");
  public static final Country HOLY_SEE_VATICAN_CITY_STATE = getCountryFactory().createInstance("VA", "HOLY SEE (VATICAN CITY STATE)");
  public static final Country HONDURAS = getCountryFactory().createInstance("HN", "HONDURAS");
  public static final Country HONG_KONG = getCountryFactory().createInstance("HK", "HONG KONG");
  public static final Country HUNGARY = getCountryFactory().createInstance("HU", "HUNGARY");
  public static final Country ICELAND = getCountryFactory().createInstance("IS", "ICELAND");
  public static final Country INDIA = getCountryFactory().createInstance("IN", "INDIA");
  public static final Country INDONESIA = getCountryFactory().createInstance("ID", "INDONESIA");
  public static final Country IRAN_ISLAMIC_REPUBLIC_OF = getCountryFactory().createInstance("IR", "IRAN, ISLAMIC REPUBLIC OF");
  public static final Country IRAQ = getCountryFactory().createInstance("IQ", "IRAQ");
  public static final Country IRELAND = getCountryFactory().createInstance("IE", "IRELAND");
  public static final Country ISRAEL = getCountryFactory().createInstance("IL", "ISRAEL");
  public static final Country ITALY = getCountryFactory().createInstance("IT", "ITALY");
  public static final Country JAMAICA = getCountryFactory().createInstance("JM", "JAMAICA");
  public static final Country JAPAN = getCountryFactory().createInstance("JP", "JAPAN");
  public static final Country JORDAN = getCountryFactory().createInstance("JO", "JORDAN");
  public static final Country KAZAKHSTAN = getCountryFactory().createInstance("KZ", "KAZAKHSTAN");
  public static final Country KENYA = getCountryFactory().createInstance("KE", "KENYA");
  public static final Country KIRIBATI = getCountryFactory().createInstance("KI", "KIRIBATI");
  public static final Country KOREA_DEMOCRATIC_PEOPLES_REPUBLIC_OF = getCountryFactory().createInstance("KP", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF");
  public static final Country KOREA_REPUBLIC_OF = getCountryFactory().createInstance("KR", "KOREA, REPUBLIC OF");
  public static final Country KUWAIT = getCountryFactory().createInstance("KW", "KUWAIT");
  public static final Country KYRGYZSTAN = getCountryFactory().createInstance("KG", "KYRGYZSTAN");
  public static final Country LAO_PEOPLES_DEMOCRATIC_REPUBLIC = getCountryFactory().createInstance("LA", "LAO PEOPLE'S DEMOCRATIC REPUBLIC");
  public static final Country LATVIA = getCountryFactory().createInstance("LV", "LATVIA");
  public static final Country LEBANON = getCountryFactory().createInstance("LB", "LEBANON");
  public static final Country LESOTHO = getCountryFactory().createInstance("LS", "LESOTHO");
  public static final Country LIBERIA = getCountryFactory().createInstance("LR", "LIBERIA");
  public static final Country LIBYAN_ARAB_JAMAHIRIYA = getCountryFactory().createInstance("LY", "LIBYAN ARAB JAMAHIRIYA");
  public static final Country LIECHTENSTEIN = getCountryFactory().createInstance("LI", "LIECHTENSTEIN");
  public static final Country LITHUANIA = getCountryFactory().createInstance("LT", "LITHUANIA");
  public static final Country LUXEMBOURG = getCountryFactory().createInstance("LU", "LUXEMBOURG");
  public static final Country MACAO = getCountryFactory().createInstance("MO", "MACAO");
  public static final Country MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF = getCountryFactory().createInstance("MK", "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF");
  public static final Country MADAGASCAR = getCountryFactory().createInstance("MG", "MADAGASCAR");
  public static final Country MALAWI = getCountryFactory().createInstance("MW", "MALAWI");
  public static final Country MALAYSIA = getCountryFactory().createInstance("MY", "MALAYSIA");
  public static final Country MALDIVES = getCountryFactory().createInstance("MV", "MALDIVES");
  public static final Country MALI = getCountryFactory().createInstance("ML", "MALI");
  public static final Country MALTA = getCountryFactory().createInstance("MT", "MALTA");
  public static final Country MARSHALL_ISLANDS = getCountryFactory().createInstance("MH", "MARSHALL ISLANDS");
  public static final Country MARTINIQUE = getCountryFactory().createInstance("MQ", "MARTINIQUE");
  public static final Country MAURITANIA = getCountryFactory().createInstance("MR", "MAURITANIA");
  public static final Country MAURITIUS = getCountryFactory().createInstance("MU", "MAURITIUS");
  public static final Country MAYOTTE = getCountryFactory().createInstance("YT", "MAYOTTE");
  public static final Country MEXICO = getCountryFactory().createInstance("MX", "MEXICO");
  public static final Country MICRONESIA_FEDERATED_STATES_OF = getCountryFactory().createInstance("FM", "MICRONESIA, FEDERATED STATES OF");
  public static final Country MOLDOVA_REPUBLIC_OF = getCountryFactory().createInstance("MD", "MOLDOVA, REPUBLIC OF");
  public static final Country MONACO = getCountryFactory().createInstance("MC", "MONACO");
  public static final Country MONGOLIA = getCountryFactory().createInstance("MN", "MONGOLIA");
  public static final Country MONTSERRAT = getCountryFactory().createInstance("MS", "MONTSERRAT");
  public static final Country MOROCCO = getCountryFactory().createInstance("MA", "MOROCCO");
  public static final Country MOZAMBIQUE = getCountryFactory().createInstance("MZ", "MOZAMBIQUE");
  public static final Country MYANMAR = getCountryFactory().createInstance("MM", "MYANMAR");
  public static final Country NAMIBIA = getCountryFactory().createInstance("NA", "NAMIBIA");
  public static final Country NAURU = getCountryFactory().createInstance("NR", "NAURU");
  public static final Country NEPAL = getCountryFactory().createInstance("NP", "NEPAL");
  public static final Country NETHERLANDS = getCountryFactory().createInstance("NL", "NETHERLANDS");
  public static final Country NETHERLANDS_ANTILLES = getCountryFactory().createInstance("AN", "NETHERLANDS ANTILLES");
  public static final Country NEW_CALEDONIA = getCountryFactory().createInstance("NC", "NEW CALEDONIA");
  public static final Country NEW_ZEALAND = getCountryFactory().createInstance("NZ", "NEW ZEALAND");
  public static final Country NICARAGUA = getCountryFactory().createInstance("NI", "NICARAGUA");
  public static final Country NIGER = getCountryFactory().createInstance("NE", "NIGER");
  public static final Country NIGERIA = getCountryFactory().createInstance("NG", "NIGERIA");
  public static final Country NIUE = getCountryFactory().createInstance("NU", "NIUE");
  public static final Country NORFOLK_ISLAND = getCountryFactory().createInstance("NF", "NORFOLK ISLAND");
  public static final Country NORTHERN_MARIANA_ISLANDS = getCountryFactory().createInstance("MP", "NORTHERN MARIANA ISLANDS");
  public static final Country NORWAY = getCountryFactory().createInstance("NO", "NORWAY");
  public static final Country OMAN = getCountryFactory().createInstance("OM", "OMAN");
  public static final Country PAKISTAN = getCountryFactory().createInstance("PK", "PAKISTAN");
  public static final Country PALAU = getCountryFactory().createInstance("PW", "PALAU");
  public static final Country PALESTINIAN_TERRITORY_OCCUPIED = getCountryFactory().createInstance("PS", "PALESTINIAN TERRITORY, OCCUPIED");
  public static final Country PANAMA = getCountryFactory().createInstance("PA", "PANAMA");
  public static final Country PAPUA_NEW_GUINEA = getCountryFactory().createInstance("PG", "PAPUA NEW GUINEA");
  public static final Country PARAGUAY = getCountryFactory().createInstance("PY", "PARAGUAY");
  public static final Country PERU = getCountryFactory().createInstance("PE", "PERU");
  public static final Country PHILIPPINES = getCountryFactory().createInstance("PH", "PHILIPPINES");
  public static final Country PITCAIRN = getCountryFactory().createInstance("PN", "PITCAIRN");
  public static final Country POLAND = getCountryFactory().createInstance("PL", "POLAND");
  public static final Country PORTUGAL = getCountryFactory().createInstance("PT", "PORTUGAL");
  public static final Country PUERTO_RICO = getCountryFactory().createInstance("PR", "PUERTO RICO");
  public static final Country QATAR = getCountryFactory().createInstance("QA", "QATAR");
  public static final Country REUNION = getCountryFactory().createInstance("RE", "REUNION");
  public static final Country ROMANIA = getCountryFactory().createInstance("RO", "ROMANIA");
  public static final Country RUSSIAN_FEDERATION = getCountryFactory().createInstance("RU", "RUSSIAN FEDERATION");
  public static final Country RWANDA = getCountryFactory().createInstance("RW", "RWANDA");
  public static final Country SAINT_HELENA = getCountryFactory().createInstance("SH", "SAINT HELENA");
  public static final Country SAINT_KITTS_AND_NEVIS = getCountryFactory().createInstance("KN", "SAINT KITTS AND NEVIS");
  public static final Country SAINT_LUCIA = getCountryFactory().createInstance("LC", "SAINT LUCIA");
  public static final Country SAINT_PIERRE_AND_MIQUELON = getCountryFactory().createInstance("PM", "SAINT PIERRE AND MIQUELON");
  public static final Country SAINT_VINCENT_AND_THE_GRENADINES = getCountryFactory().createInstance("VC", "SAINT VINCENT AND THE GRENADINES");
  public static final Country SAMOA = getCountryFactory().createInstance("WS", "SAMOA");
  public static final Country SAN_MARINO = getCountryFactory().createInstance("SM", "SAN MARINO");
  public static final Country SAO_TOME_AND_PRINCIPE = getCountryFactory().createInstance("ST", "SAO TOME AND PRINCIPE");
  public static final Country SAUDI_ARABIA = getCountryFactory().createInstance("SA", "SAUDI ARABIA");
  public static final Country SENEGAL = getCountryFactory().createInstance("SN", "SENEGAL");
  public static final Country SERBIA_AND_MONTENEGRO = getCountryFactory().createInstance("CS", "SERBIA AND MONTENEGRO");
  public static final Country SEYCHELLES = getCountryFactory().createInstance("SC", "SEYCHELLES");
  public static final Country SIERRA_LEONE = getCountryFactory().createInstance("SL", "SIERRA LEONE");
  public static final Country SINGAPORE = getCountryFactory().createInstance("SG", "SINGAPORE");
  public static final Country SLOVAKIA = getCountryFactory().createInstance("SK", "SLOVAKIA");
  public static final Country SLOVENIA = getCountryFactory().createInstance("SI", "SLOVENIA");
  public static final Country SOLOMON_ISLANDS = getCountryFactory().createInstance("SB", "SOLOMON ISLANDS");
  public static final Country SOMALIA = getCountryFactory().createInstance("SO", "SOMALIA");
  public static final Country SOUTH_AFRICA = getCountryFactory().createInstance("ZA", "SOUTH AFRICA");
  public static final Country SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS = getCountryFactory().createInstance("GS", "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS");
  public static final Country SPAIN = getCountryFactory().createInstance("ES", "SPAIN");
  public static final Country SRI_LANKA = getCountryFactory().createInstance("LK", "SRI LANKA");
  public static final Country SUDAN = getCountryFactory().createInstance("SD", "SUDAN");
  public static final Country SURINAME = getCountryFactory().createInstance("SR", "SURINAME");
  public static final Country SVALBARD_AND_JAN_MAYEN = getCountryFactory().createInstance("SJ", "SVALBARD AND JAN MAYEN");
  public static final Country SWAZILAND = getCountryFactory().createInstance("SZ", "SWAZILAND");
  public static final Country SWEDEN = getCountryFactory().createInstance("SE", "SWEDEN");
  public static final Country SWITZERLAND = getCountryFactory().createInstance("CH", "SWITZERLAND");
  public static final Country SYRIAN_ARAB_REPUBLIC = getCountryFactory().createInstance("SY", "SYRIAN ARAB REPUBLIC");
  public static final Country TAIWAN_PROVINCE_OF_CHINA = getCountryFactory().createInstance("TW", "TAIWAN, PROVINCE OF CHINA");
  public static final Country TAJIKISTAN = getCountryFactory().createInstance("TJ", "TAJIKISTAN");
  public static final Country TANZANIA_UNITED_REPUBLIC_OF = getCountryFactory().createInstance("TZ", "TANZANIA, UNITED REPUBLIC OF");
  public static final Country THAILAND = getCountryFactory().createInstance("TH", "THAILAND");
  public static final Country TIMORLESTE = getCountryFactory().createInstance("TL", "TIMOR-LESTE");
  public static final Country TOGO = getCountryFactory().createInstance("TG", "TOGO");
  public static final Country TOKELAU = getCountryFactory().createInstance("TK", "TOKELAU");
  public static final Country TONGA = getCountryFactory().createInstance("TO", "TONGA");
  public static final Country TRINIDAD_AND_TOBAGO = getCountryFactory().createInstance("TT", "TRINIDAD AND TOBAGO");
  public static final Country TUNISIA = getCountryFactory().createInstance("TN", "TUNISIA");
  public static final Country TURKEY = getCountryFactory().createInstance("TR", "TURKEY");
  public static final Country TURKMENISTAN = getCountryFactory().createInstance("TM", "TURKMENISTAN");
  public static final Country TURKS_AND_CAICOS_ISLANDS = getCountryFactory().createInstance("TC", "TURKS AND CAICOS ISLANDS");
  public static final Country TUVALU = getCountryFactory().createInstance("TV", "TUVALU");
  public static final Country UGANDA = getCountryFactory().createInstance("UG", "UGANDA");
  public static final Country UKRAINE = getCountryFactory().createInstance("UA", "UKRAINE");
  public static final Country UNITED_ARAB_EMIRATES = getCountryFactory().createInstance("AE", "UNITED ARAB EMIRATES");
  public static final Country UNITED_KINGDOM = getCountryFactory().createInstance("GB", "UNITED KINGDOM");
  public static final Country UNITED_STATES = getCountryFactory().createInstance("US", "UNITED STATES");
  public static final Country UNITED_STATES_MINOR_OUTLYING_ISLANDS = getCountryFactory().createInstance("UM", "UNITED STATES MINOR OUTLYING ISLANDS");
  public static final Country URUGUAY = getCountryFactory().createInstance("UY", "URUGUAY");
  public static final Country UZBEKISTAN = getCountryFactory().createInstance("UZ", "UZBEKISTAN");
  public static final Country VANUATU = getCountryFactory().createInstance("VU", "VANUATU");
  public static final Country VENEZUELA = getCountryFactory().createInstance("VE", "VENEZUELA");
  public static final Country VIET_NAM = getCountryFactory().createInstance("VN", "VIET NAM");
  public static final Country VIRGIN_ISLANDS_BRITISH = getCountryFactory().createInstance("VG", "VIRGIN ISLANDS, BRITISH");
  public static final Country VIRGIN_ISLANDS_US = getCountryFactory().createInstance("VI", "VIRGIN ISLANDS, U.S.");
  public static final Country WALLIS_AND_FUTUNA = getCountryFactory().createInstance("WF", "WALLIS AND FUTUNA");
  public static final Country WESTERN_SAHARA = getCountryFactory().createInstance("EH", "WESTERN SAHARA");
  public static final Country YEMEN = getCountryFactory().createInstance("YE", "YEMEN");
  public static final Country ZAMBIA = getCountryFactory().createInstance("ZM", "ZAMBIA");
  public static final Country ZIMBABWE = getCountryFactory().createInstance("ZW", "ZIMBABWE");

  private static CountryFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.Country.factory";

  /**
   * Creates an instance of the Country enumerated-type class.
   * @param id the unique identifier for the Country enumerated-type.
   * @param code a String value for looking up enumerated-types of the Country classification.
   * @param description descriptive information concerning the Country enumerated-type.
   */
  protected Country(final Integer id, final String code, final String description) {
    super(id, code, description);
    COUNTRY_SET.add(this);
  }

  /**
   * Creates an instance of the Country enumerated-type class.
   * @param id the unique identifier for the Country enumerated-type.
   * @param code a String value for looking up enumerated-types of the Country classification.
   * @param description descriptive information concerning the Country enumerated-type.
   * @param externalCode the external code for the Country enumerated-type.
   */
  protected Country(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    COUNTRY_SET.add(this);
  }

  /**
   * Returns the instance of the CountryFactory class to create instances of Country enumerated-types.
   * @return an instance of the CountryFactory class for creating Country enumerated-type instances.
   */
  protected static CountryFactory getCountryFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (Country.class) {
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
  public static Country getByCode(final String code) {
    return getInstance(COUNTRY_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static Country getByDescription(final String description) {
    return getInstance(COUNTRY_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static Country getByExternalCode(final String externalCode) {
    return getInstance(COUNTRY_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static Country getById(final Integer id) {
    return getInstance(COUNTRY_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<Country> values() {
    return Collections.unmodifiableSet(COUNTRY_SET);
  }

  protected interface CountryFactory extends EnumFactory<Country> {

    public Country createInstance(String code, String description);

    public Country createInstance(String code, String description, String externalCode);

  }

  static final class DefaultCountryFactory implements CountryFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultCountryFactory() {
    }

    public Country createInstance(final String code, final String description) {
      return new Country(getNextSequence(), code, description);
    }

    public Country createInstance(final String code, final String description, final String externalCode) {
      return new Country(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
