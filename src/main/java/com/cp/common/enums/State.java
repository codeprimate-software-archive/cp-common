/*
 * State.java (c) 18 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.5.29
 * @see com.cp.common.enums.AbstractEnum
 */

package com.cp.common.enums;

import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class State extends AbstractEnum {

  private static final Set<State> STATE_SET = new HashSet<State>(51);

  public static final State ALABAMA = getStateFactory().createInstance("AL", "Alabama", "AL");
  public static final State ALASKA = getStateFactory().createInstance("AK", "Alaska", "AK");
  public static final State ARIZONA = getStateFactory().createInstance("AZ", "Arizona", "AZ");
  public static final State ARKANSAS = getStateFactory().createInstance("AR", "Arkansas", "AR");
  public static final State CALIFORNIA = getStateFactory().createInstance("CA", "California", "CA");
  public static final State COLORADO = getStateFactory().createInstance("CO", "Colorado", "CO");
  public static final State CONNECTICUT = getStateFactory().createInstance("CT", "Connecticut", "CT");
  public static final State DELAWARE = getStateFactory().createInstance("DE", "Delaware", "DE");
  public static final State DISTRICT_OF_COLUMBIA = getStateFactory().createInstance("DC", "District of Columbia", "DC");
  public static final State FLORIDA = getStateFactory().createInstance("FL", "Florida", "FL");
  public static final State GEORGIA = getStateFactory().createInstance("GA", "Georgia", "GA");
  public static final State GUAM = getStateFactory().createInstance("GU", "Guam", "GU");
  public static final State HAWAII = getStateFactory().createInstance("HI", "Hawaii", "HI");
  public static final State IDAHO = getStateFactory().createInstance("ID", "Idaho", "ID");
  public static final State ILLINOIS = getStateFactory().createInstance("IL", "Illinois", "IL");
  public static final State INDIANA = getStateFactory().createInstance("IN", "Indiana", "IN");
  public static final State IOWA = getStateFactory().createInstance("IA", "Iowa", "IA");
  public static final State KANSAS = getStateFactory().createInstance("KS", "Kansas", "KS");
  public static final State KENTUCKY = getStateFactory().createInstance("KY", "Kentucky", "KY");
  public static final State LOUISIANA = getStateFactory().createInstance("LA", "Louisiana", "LA");
  public static final State MAINE = getStateFactory().createInstance("ME", "Maine", "ME");
  public static final State MARYLAND = getStateFactory().createInstance("MD", "Maryland", "MD");
  public static final State MASSACHUSETTS = getStateFactory().createInstance("MA", "Massachusetts", "MA");
  public static final State MICHIGAN = getStateFactory().createInstance("MI", "Michigan", "MI");
  public static final State MINNESOTA = getStateFactory().createInstance("MN", "Minnesota", "MN");
  public static final State MISSISSIPPI = getStateFactory().createInstance("MS", "Mississippi", "MS");
  public static final State MISSOURI = getStateFactory().createInstance("MO", "Missouri", "MO");
  public static final State MONTANA = getStateFactory().createInstance("MT", "Montana", "MT");
  public static final State NEBRASKA = getStateFactory().createInstance("NE", "Nebraska", "NE");
  public static final State NEVADA = getStateFactory().createInstance("NV", "Nevada", "NV");
  public static final State NEW_HAMPSHIRE = getStateFactory().createInstance("NH", "New Hampshire", "NH");
  public static final State NEW_JERSEY = getStateFactory().createInstance("NJ", "New Jersey", "NJ");
  public static final State NEW_MEXICO = getStateFactory().createInstance("NM", "New Mexico", "NM");
  public static final State NEW_YORK = getStateFactory().createInstance("NY", "New York", "NY");
  public static final State NORTH_CAROLINA = getStateFactory().createInstance("NC", "North Carolina", "NC");
  public static final State NORTH_DAKOTA = getStateFactory().createInstance("ND", "North Dakota", "ND");
  public static final State OHIO = getStateFactory().createInstance("OH", "OHIO", "OH");
  public static final State OKLAHOMA = getStateFactory().createInstance("OK", "Oklahoma", "OK");
  public static final State OREGON = getStateFactory().createInstance("OR", "Oregon", "OR");
  public static final State PENNSYLVANIA = getStateFactory().createInstance("PA", "Pennsylvania", "PA");
  public static final State PUERTO_RICO = getStateFactory().createInstance("PR", "Puerto Rico", "PR");
  public static final State RHODE_ISLAND = getStateFactory().createInstance("RI", "Rhode Island", "RI");
  public static final State SOUTH_CAROLINA = getStateFactory().createInstance("SC", "South Carolina", "SC");
  public static final State SOUTH_DAKOTA = getStateFactory().createInstance("SD", "South Dakota", "SD");
  public static final State TENNESSEE = getStateFactory().createInstance("TN", "Tennessee", "TN");
  public static final State TEXAS = getStateFactory().createInstance("TX", "Texas", "TX");
  public static final State UTAH = getStateFactory().createInstance("UT", "Utah", "UT");
  public static final State VERMONT = getStateFactory().createInstance("VT", "Vermont", "VT");
  public static final State VIRGINIA = getStateFactory().createInstance("VA", "Verginia", "VA");
  public static final State VIRGIN_ISLANDS = getStateFactory().createInstance("VI", "Virgin Islands", "VI");
  public static final State WASHINGTON = getStateFactory().createInstance("WA", "Washington", "WA");
  public static final State WEST_VIRGINIA = getStateFactory().createInstance("WV", "West Virginia", "WV");
  public static final State WISCONSIN = getStateFactory().createInstance("WI", "Wisconsin", "WI");
  public static final State WYOMING = getStateFactory().createInstance("WY", "Wyoming", "WY");

  private static StateFactory FACTORY;

  private static final String FACTORY_PROPERTY_KEY = "cp-common.com.cp.common.enums.State.factory";

  /**
   * Creates an instance of the State enumerated-type class.
   * @param id the unique identifier for the State enumerated-type.
   * @param code a String value for looking up enumerated-types of the State classification.
   * @param description descriptive information concerning the State enumerated-type.
   */
  protected State(final Integer id, final String code, final String description) {
    super(id, code, description);
    STATE_SET.add(this);
  }

  /**
   * Creates an instance of the State enumerated-type class.
   * @param id the unique identifier for the State enumerated-type.
   * @param code a String value for looking up enumerated-types of the State classification.
   * @param description descriptive information concerning the State enumerated-type.
   * @param externalCode the external code for the State enumerated-type.
   */
  protected State(final Integer id, final String code, final String description, final String externalCode) {
    super(id, code, description, externalCode);
    STATE_SET.add(this);
  }

  /**
   * Returns the instance of the StateFactory class to create instances of State enumerated-types.
   * @return an instance of the StateFactory class for creating State enumerated-type instances.
   */
  protected static StateFactory getStateFactory() {
    if (ObjectUtil.isNull(FACTORY)) {
      synchronized (State.class) {
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
  public static State getByCode(final String code) {
    return getInstance(STATE_SET, new CodeLookupStrategy(code));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified description.
   * @param description the String description lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified description.
   * @throws java.lang.IllegalArgumentException if description is not a valid enumerated-type description.
   */
  public static State getByDescription(final String description) {
    return getInstance(STATE_SET, new DescriptionLookupStrategy(description));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified external code.
   * @param externalCode the String external code lookup value for the specified enumerated-type.
   * @return the enumerated-type for the specified external code.
   * @throws java.lang.IllegalArgumentException if external code is not a valid enumerated-type external code.
   */
  public static State getByExternalCode(final String externalCode) {
    return getInstance(STATE_SET, new ExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Returns an enumerated-type instance of this enum classification for the specified unique identifier.
   * @param id an Integer value id uniquely identifying the enumerated-type.
   * @return the enumerated-type for the specified ID.
   * @throws java.lang.IllegalArgumentException if ID does not identify an enumerated-type for this enum
   * classification.
   */
  public static State getById(final Integer id) {
    return getInstance(STATE_SET, new IdLookupStrategy(id));
  }

  /**
   * Returns the Set of enumerated types defined by this enum classification.
   * @return a Set of enumerated types for this enum classification.
   */
  public static Set<State> values() {
    return Collections.unmodifiableSet(STATE_SET);
  }

  protected interface StateFactory extends EnumFactory<State> {

    public State createInstance(String code, String description);

    public State createInstance(String code, String description, String externalCode);

  }

  static final class DefaultStateFactory implements StateFactory {

    private int sequence = 0;

    // Note, need a default constructor to explicitly specify public access modifier, making it accessible
    // for Java reflection & introspection.
    public DefaultStateFactory() {
    }

    public State createInstance(final String code, final String description) {
      return new State(getNextSequence(), code, description);
    }

    public State createInstance(final String code, final String description, final String externalCode) {
      return new State(getNextSequence(), code, description, externalCode);
    }

    private Integer getNextSequence() {
      return new Integer(sequence++);
    }
  }

}
