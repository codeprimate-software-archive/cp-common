/*
 * CommonBeanTestCase.java (c) 17 February 2007
 *
 * Common test case class for the classes and components of the com.cp.common.beans package.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.17
 * @see com.cp.common.beans.AbstractBeanTest (subclass)
 * @see com.cp.common.beans.event.CommonEventTestCase (subclass)
 * @see com.cp.common.beans.support.CommonSupportTestCase (subclass)
 * @see com.cp.common.beans.util.BeanUtilTest (subclass)
 * @see com.cp.common.beans.util.ConvertUtilTest (subclass)
 * @see com.cp.common.beans.util.converters.IdentifiableConverterTest (subclass)
 * @see com.cp.common.test.util.TestUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.beans;

import com.cp.common.beans.annotation.BoundedNumber;
import com.cp.common.beans.annotation.Required;
import com.cp.common.lang.Auditable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.TestCase;
import org.apache.log4j.Level;

public abstract class CommonBeanTestCase extends TestCase {

  public CommonBeanTestCase(final String testName) {
    super(testName);
  }

  protected void assertCreatedAndModifiedProperties(final User expectedChangedBy, 
                                                    final Calendar expectedChangedDateTime,
                                                    final Auditable actual) {
    assertCreatedAndModifiedProperties(expectedChangedBy, expectedChangedDateTime, expectedChangedBy,
      expectedChangedDateTime, actual);
  }

  protected void assertCreatedAndModifiedProperties(final User expectedCreatedBy,
                                                    final Calendar expectedCreatedDateTime,
                                                    final User expectedModifiedBy,
                                                    final Calendar expectedModifiedDateTime,
                                                    final Auditable actual) {
    TestUtil.assertNullEquals(expectedCreatedBy, actual.getCreatedBy());
    TestUtil.assertNullEquals(expectedCreatedDateTime, actual.getCreatedDateTime());
    TestUtil.assertNullEquals(expectedModifiedBy, actual.getModifiedBy());
    TestUtil.assertNullEquals(expectedModifiedDateTime, actual.getModifiedDateTime());
  }

  protected void assertLastModifiedProperties(final User expectedLastModifiedBy,
                                              final Calendar expectedLastModifiedDateTime,
                                              final Auditable auditable) {
    TestUtil.assertNullEquals(expectedLastModifiedBy, auditable.getLastModifiedBy());
    TestUtil.assertNullEquals(expectedLastModifiedDateTime, auditable.getLastModifiedDateTime());
  }

  protected Consumer getConsumer(final Integer yearBorn) {
    final Consumer consumer = new ConsumerImpl();
    consumer.setYearBorn(yearBorn);
    return consumer;
  }

  protected Consumer getEmptyConsumer() {
    return new ConsumerImpl();
  }

  protected Person getEmptyPerson() {
    return new PersonImpl();
  }

  protected Person getPerson(final Integer personId,
                             final String firstName,
                             final String lastName,
                             final Calendar dateOfBirth,
                             final String ssn) {
    final Person person = new PersonImpl(firstName, lastName, dateOfBirth, ssn);
    person.setPersonId(personId);
    return person;
  }

  protected Process getProcess(final String processName) {
    return new DefaultProcess(processName);
  }

  protected User getUser(final String username) {
    return new DefaultUser(username);
  }

  public static interface Person extends Bean<Integer> {

    public Integer getPersonId();

    public void setPersonId(Integer personId);

    public String getFirstName();

    public void setFirstName(String firstName);

    public String getLastName();

    public void setLastName(String lastName);

    public Calendar getDateOfBirth();

    public void setDateOfBirth(Calendar dateOfBirth);

    public String getSsn();

    public void setSsn(String ssn);

  }

  public static class PersonImpl extends AbstractBean<Integer> implements Person {

    private Calendar dateOfBirth;

    private String firstName;
    private String lastName;
    private String ssn;

    public PersonImpl() {
    }

    public PersonImpl(final Integer personId) {
      super(personId);
    }

    public PersonImpl(final String firstName, final String lastName, final Calendar dateOfBirth, final String ssn) {
      setFirstName(firstName);
      setLastName(lastName);
      setDateOfBirth(dateOfBirth);
      setSsn(ssn);
    }

    public PersonImpl(final Person person) {
      setFirstName(person.getFirstName());
      setLastName(person.getLastName());
      setDateOfBirth(person.getDateOfBirth());
      setSsn(person.getSsn());
    }

    public Integer getPersonId() {
      return getId();
    }

    public void setPersonId(final Integer personId) {
      setId(personId);
    }

    public String getFirstName() {
      return firstName;
    }

    @Required
    public void setFirstName(final String firstName) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.firstName = firstName;
        }
      };
      processChange("firstName", this.firstName, firstName, callbackHandler);
    }

    public String getLastName() {
      return lastName;
    }

    @Required
    public void setLastName(final String lastName) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.lastName = lastName;
        }
      };
      processChange("lastName", this.lastName, lastName, callbackHandler);
    }

    public Calendar getDateOfBirth() {
      return DateUtil.copy(this.dateOfBirth);
    }

    @Required
    public void setDateOfBirth(final Calendar dateOfBirth) {
      final Calendar dateOfBirthCopy = DateUtil.copy(dateOfBirth);
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.dateOfBirth = dateOfBirthCopy;
        }
      };
      processChange("dateOfBirth", getDateOfBirth(), dateOfBirth, callbackHandler);
    }

    public String getSsn() {
      return ssn;
    }

    public void setSsn(final String ssn) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.ssn = ssn;
        }
      };
      processChange("ssn", this.ssn, ssn, callbackHandler);
    }

    protected Level getLogLevel() {
      return Level.INFO;
    }

    public void reset() {
      super.reset();
      setPersonId(null);
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

      return ObjectUtil.equals(getPersonId(), that.getPersonId())
        && ObjectUtil.equals(getFirstName(), that.getFirstName())
        && ObjectUtil.equals(getLastName(), that.getLastName())
        && ObjectUtil.equals(getDateOfBirth(), that.getDateOfBirth())
        && ObjectUtil.equals(getSsn(), that.getSsn());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPersonId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getFirstName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getLastName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getDateOfBirth());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getSsn());
      return hashValue;
    }
  }

  public static interface Consumer extends Person {

    public Integer getYearBorn();

    public void setYearBorn(Integer yearBorn);

  }

  public static class ConsumerImpl extends PersonImpl implements Consumer {

    private Integer yearBorn;

    public ConsumerImpl() {
    }

    public ConsumerImpl(final Consumer consumer) {
      super(consumer);
      setYearBorn(consumer.getYearBorn());
    }

    public Integer getYearBorn() {
      return yearBorn;
    }

    @BoundedNumber(min=1986, max=2086)
    public void setYearBorn(final Integer yearBorn) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          ConsumerImpl.this.yearBorn = yearBorn;
        }
      };
      processChange("yearBorn", this.yearBorn, yearBorn, callbackHandler);
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Consumer)) {
        return false;
      }

      final Consumer that = (Consumer) obj;

      return super.equals(obj) && ObjectUtil.equals(getYearBorn(), that.getYearBorn());
    }

    @Override
    public int hashCode() {
      int hashValue = super.hashCode();
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getYearBorn());
      return hashValue;
    }
  }

}
