/*
 * CommonSupportTestCase.java (c) 14 Aug 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.CommonBeanTestCase
 * @see com.cp.common.beans.annotation.Required
 * @see com.cp.common.enums.State
 * @see com.cp.common.util.Visitor
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.support;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.CommonBeanTestCase;
import com.cp.common.beans.annotation.Required;
import com.cp.common.enums.State;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.Visitor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class CommonSupportTestCase extends CommonBeanTestCase {

  public CommonSupportTestCase(final String testName) {
    super(testName);
  }

  protected void assertAddress(final Integer expectedAddressId,
                               final String expectedStreet1,
                               final String expectedStreet2,
                               final String expectedCity,
                               final State expectedState,
                               final String expectedZipCode,
                               final Address actualAddress) {
    TestUtil.assertNullEquals(expectedAddressId, actualAddress.getAddressId());
    TestUtil.assertNullEquals(expectedStreet1, actualAddress.getStreet1());
    TestUtil.assertNullEquals(expectedStreet2, actualAddress.getStreet2());
    TestUtil.assertNullEquals(expectedCity, actualAddress.getCity());
    TestUtil.assertNullEquals(expectedState, actualAddress.getState());
    TestUtil.assertNullEquals(expectedZipCode, actualAddress.getZipCode());
  }

  // TODO review implementation of the assertPerson method
  protected void assertPerson(final Integer expectedPersonId,
                              final String expectedFirstName,
                              final String expectedLastName,
                              final Person actualPerson) {
    TestUtil.assertNullEquals(expectedPersonId, actualPerson.getPersonId());
    TestUtil.assertNullEquals(expectedFirstName, actualPerson.getFirstName());
    TestUtil.assertNullEquals(expectedLastName, actualPerson.getLastName());
  }

  protected void assertPhoneNumber(final Integer expectedPhoneNumberId,
                                   final String expectedAreaCode,
                                   final String expectedPrefix,
                                   final String expectedSuffix,
                                   final String expectedExtension,
                                   final PhoneNumber actualPhoneNumber) {
    TestUtil.assertNullEquals(expectedPhoneNumberId, actualPhoneNumber.getPhoneNumberId());
    TestUtil.assertNullEquals(expectedAreaCode, actualPhoneNumber.getAreaCode());
    TestUtil.assertNullEquals(expectedPrefix, actualPhoneNumber.getPrefix());
    TestUtil.assertNullEquals(expectedSuffix, actualPhoneNumber.getSuffix());
    TestUtil.assertNullEquals(expectedExtension, actualPhoneNumber.getExtension());
  }

  protected Address getAddress(final Integer addressId,
                               final String street1,
                               final String street2,
                               final String city,
                               final State state,
                               final String zipCode) {
    return new AddressImpl(addressId, street1, street2, city, state, zipCode);
  }

  protected Household getHousehold(final Integer householdId, final String householdName, final Integer householdNumber) {
    return new HouseholdImpl(householdId, householdName, householdNumber);
  }

  // TODO review implementation of the getPerson method
  protected Person getPerson(final Integer personId, final String firstName, final String lastName) {
    return new PersonImpl(personId, firstName, lastName);
  }

  protected PhoneNumber getPhoneNumber(final Integer phoneNumberId,
                                       final String areaCode,
                                       final String prefix,
                                       final String suffix,
                                       final String extension) {
    return new PhoneNumberImpl(phoneNumberId, areaCode, prefix, suffix, extension);
  }

  protected Address getEmptyAddress() {
    return new AddressImpl();
  }

  protected Household getEmptyHousehold() {
    return new HouseholdImpl();
  }

  protected Person getEmptyPerson() {
    return new PersonImpl();
  }

  protected PhoneNumber getEmptyPhoneNumber() {
    return new PhoneNumberImpl();
  }

  public static interface Address extends Bean<Integer> {

    public Integer getAddressId();

    public void setAddressId(Integer addressId);

    public String getStreet1();

    public void setStreet1(String street1);

    public String getStreet2();

    public void setStreet2(String street2);

    public String getCity();

    public void setCity(String city);

    public State getState();

    public void setState(State state);

    public String getZipCode();

    public void setZipCode(String zipCode);

  }

  public static class AddressImpl extends AbstractBean<Integer> implements Address {

    private State state;

    private String city;
    private String street1;
    private String street2;
    private String zipCode;

    public AddressImpl() {
    }

    public AddressImpl(final Integer addressId) {
      setAddressId(addressId);
    }

    public AddressImpl(final Integer addressId,
                       final String street1,
                       final String street2,
                       final String city,
                       final State state,
                       final String zipCode) {
      setAddressId(addressId);
      setStreet1(street1);
      setStreet2(street2);
      setCity(city);
      setState(state);
      setZipCode(zipCode);
    }

    public Integer getAddressId() {
      return getId();
    }

    public void setAddressId(final Integer addressId) {
      setId(addressId);
    }

    public String getCity() {
      return city;
    }

    @Required
    public void setCity(final String city) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          AddressImpl.this.city = city;
        }
      };
      processChange("city", this.city, city, callbackHandler);
    }

    public State getState() {
      return state;
    }

    @Required
    public void setState(final State state) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          AddressImpl.this.state = state;
        }
      };
      processChange("state", this.state, state, callbackHandler);
    }

    public String getStreet1() {
      return street1;
    }

    @Required
    public void setStreet1(final String street1) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          AddressImpl.this.street1 = street1;
        }
      };
      processChange("street1", this.street1, street1, callbackHandler);
    }

    public String getStreet2() {
      return street2;
    }

    public void setStreet2(final String street2) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          AddressImpl.this.street2 = street2;
        }
      };
      processChange("street2", this.street2, street2, callbackHandler);
    }

    public String getZipCode() {
      return zipCode;
    }

    @Required
    public void setZipCode(final String zipCode) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          AddressImpl.this.zipCode = zipCode;
        }
      };
      processChange("zipCode", this.zipCode, zipCode, callbackHandler);
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Address)) {
        return false;
      }

      final Address that = (Address) obj;

      return ObjectUtil.equals(getAddressId(), that.getAddressId())
        && ObjectUtil.equals(getStreet1(), that.getStreet1())
        && ObjectUtil.equals(getStreet2(), that.getStreet2())
        && ObjectUtil.equals(getCity(), that.getCity())
        && ObjectUtil.equals(getState(), that.getState())
        && ObjectUtil.equals(getZipCode(), that.getZipCode());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getAddressId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getStreet1());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getStreet2());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getCity());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getState());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getZipCode());
      return hashValue;
    }
  }

  public static interface Household extends Bean<Integer> {

    public Integer getHouseholdId();

    public void setHouseholdId(Integer householdId);

    public String getHouseholdName();

    public void setHouseholdName(String householdName);

    public Integer getHouseholdNumber();

    public void setHouseholdNumber(Integer householdNumber);

    public void addMember(Person member);

    public Person getMember(Integer personId);

    public Person getMember(String firstName, String lastName);

    public Set<Person> getMembers();

    public void removeMember(Person member);

    public int size();

  }

  public static class HouseholdImpl extends AbstractBean<Integer> implements Household {

    private Integer householdNumber;

    private Set<Person> members = new HashSet<Person>();

    private String householdName;

    public HouseholdImpl() {
    }

    public HouseholdImpl(final Integer householdId) {
      this(householdId, null, null);
    }

    public HouseholdImpl(final Integer householdId, final String householdName, final Integer householdNumber) {
      setHouseholdId(householdId);
      setHouseholdName(householdName);
      setHouseholdNumber(householdNumber);
    }

    public Integer getHouseholdId() {
      return getId();
    }

    public void setHouseholdId(final Integer householdId) {
      setId(householdId);
    }

    public String getHouseholdName() {
      return householdName;
    }

    public void setHouseholdName(final String householdName) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          HouseholdImpl.this.householdName = householdName;
        }
      };
      processChange("householdName", this.householdName, householdName, callbackHandler);
    }

    public Integer getHouseholdNumber() {
      return householdNumber;
    }

    public void setHouseholdNumber(final Integer householdNumber) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          HouseholdImpl.this.householdNumber = householdNumber;
        }
      };
      processChange("householdNumber", this.householdNumber, householdNumber, callbackHandler);
    }

    public void accept(Visitor visitor) {
      super.accept(visitor);

      for (final Person person : getMembers()) {
        person.accept(visitor);
      }
    }

    public void addMember(final Person person) {
      members.add(person);
    }

    // TODO review implementation
    public void commit() {
      super.commit();

      for (Person person : getMembers()) {
        person.commit();
      }
    }

    public Person getMember(final Integer personId) {
      for (Person person : getMembers()) {
        if (ObjectUtil.equals(person.getPersonId(), personId)) {
          return person;
        }
      }
      return null;
    }

    public Person getMember(final String firstName, final String lastName) {
      for (Person person : getMembers()) {
        if (ObjectUtil.equals(person.getFirstName(), firstName) && ObjectUtil.equals(person.getLastName(), lastName)) {
          return person;
        }
      }
      return null;
    }

    public Set<Person> getMembers() {
      return Collections.unmodifiableSet(members);
    }

    public void removeMember(final Person person) {
      members.remove(person);
    }

    // TODO review implementation
    public void rollback() {
      super.rollback();

      for (final Person person : getMembers()) {
        person.rollback();
      }
    }

    public int size() {
      return getMembers().size();
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Household)) {
        return false;
      }

      final Household that = (Household) obj;

      return ObjectUtil.equals(getHouseholdId(), that.getHouseholdId())
        && ObjectUtil.equals(getHouseholdName(), that.getHouseholdName())
        && ObjectUtil.equals(getHouseholdNumber(), that.getHouseholdNumber())
        && ObjectUtil.equals(getMembers(), that.getMembers());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getHouseholdId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getHouseholdName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getHouseholdNumber());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMembers());
      return hashValue;
    }
  }

  public static interface Person extends CommonBeanTestCase.Person {

    public Address getAddress();

    public void setAddress(Address address);

    public String getName();

    public void setName(String firstName, String lastName);

    public PhoneNumber getPhoneNumber();

    public void setPhoneNumber(PhoneNumber phoneNumber);

  }

  public static class PersonImpl extends CommonBeanTestCase.PersonImpl implements Person {

    private Address address;

    private PhoneNumber phoneNumber;

    public PersonImpl() {
    }

    public PersonImpl(final Integer personId) {
      super(personId);
    }

    public PersonImpl(final Integer personId, final String firstName, final String lastName) {
      setPersonId(personId);
      setFirstName(firstName);
      setLastName(lastName);
    }

    public PersonImpl(final Person person) {
      super(person);
      setAddress(person.getAddress());
      setPhoneNumber(person.getPhoneNumber());
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(final Address address) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.address = address;
        }
      };
      processChange("address", this.address, address, callbackHandler);
    }

    public String getName() {
      final StringBuffer buffer = new StringBuffer(StringUtil.toString(getFirstName()));
      buffer.append(" ");
      buffer.append(StringUtil.toString(getLastName()));
      return buffer.toString();
    }

    public void setName(final String firstName, final String lastName) {
      setFirstName(firstName);
      setLastName(lastName);
    }

    public PhoneNumber getPhoneNumber() {
      return phoneNumber;
    }

    public void setPhoneNumber(final PhoneNumber phoneNumber) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.phoneNumber = phoneNumber;
        }
      };
      processChange("phoneNumber", this.phoneNumber, phoneNumber, callbackHandler);
    }

    public void accept(final Visitor visitor) {
      super.accept(visitor);

      if (ObjectUtil.isNotNull(getAddress())) {
        getAddress().accept(visitor);
      }

      if (ObjectUtil.isNotNull(getPhoneNumber())) {
        getPhoneNumber().accept(visitor);
      }
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Person)) {
        return false;
      }

      final Person that = (Person) obj;

      return super.equals(that)
        && ObjectUtil.equals(getAddress(), that.getAddress())
        && ObjectUtil.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
      int hashValue = super.hashCode();
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getAddress());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPhoneNumber());
      return hashValue;
    }
  }

  public static interface PhoneNumber extends Bean<Integer> {

    public Integer getPhoneNumberId();

    public void setPhoneNumberId(Integer phoneNumberId);

    public String getAreaCode();

    public void setAreaCode(String areaCode);

    public String getPrefix();

    public void setPrefix(String prefix);

    public String getSuffix();

    public void setSuffix(String suffix);

    public String getExtension();

    public void setExtension(String extension);

  }

  public static class PhoneNumberImpl extends AbstractBean<Integer> implements PhoneNumber {

    private String areaCode;
    private String extension;
    private String prefix;
    private String suffix;

    public PhoneNumberImpl() {
    }

    public PhoneNumberImpl(final Integer phoneNumberId) {
      setPhoneNumberId(phoneNumberId);
    }

    public PhoneNumberImpl(final Integer phoneNumberId,
                           final String areaCode,
                           final String prefix,
                           final String suffix,
                           final String extension) {
      setPhoneNumberId(phoneNumberId);
      setAreaCode(areaCode);
      setPrefix(prefix);
      setSuffix(suffix);
      setExtension(extension);
    }

    public Integer getPhoneNumberId() {
      return getId();
    }

    public void setPhoneNumberId(final Integer phoneNumberId) {
      setId(phoneNumberId);
    }

    public String getAreaCode() {
      return areaCode;
    }

    @Required
    public void setAreaCode(final String areaCode) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumberImpl.this.areaCode = areaCode;
        }
      };
      processChange("areaCode", this.areaCode, areaCode, callbackHandler);
    }

    public String getPrefix() {
      return prefix;
    }

    @Required
    public void setPrefix(final String prefix) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumberImpl.this.prefix = prefix;
        }
      };
      processChange("prefix", this.prefix, prefix, callbackHandler);
    }

    public String getSuffix() {
      return suffix;
    }

    @Required
    public void setSuffix(final String suffix) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumberImpl.this.suffix = suffix;
        }
      };
      processChange("suffix", this.suffix, suffix, callbackHandler);
    }

    public String getExtension() {
      return extension;
    }

    public void setExtension(final String extension) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PhoneNumberImpl.this.extension = extension;
        }
      };
      processChange("extension", this.extension, extension, callbackHandler);
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof PhoneNumber)) {
        return false;
      }

      final PhoneNumber that = (PhoneNumber) obj;

      return ObjectUtil.equals(getPhoneNumberId(), that.getPhoneNumberId())
        && ObjectUtil.equals(getAreaCode(), that.getAreaCode())
        && ObjectUtil.equals(getPrefix(), that.getPrefix())
        && ObjectUtil.equals(getSuffix(), that.getSuffix())
        && ObjectUtil.equals(getExtension(), that.getExtension());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPhoneNumberId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getAreaCode());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPrefix());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getSuffix());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getExtension());
      return hashValue;
    }
  }

}
