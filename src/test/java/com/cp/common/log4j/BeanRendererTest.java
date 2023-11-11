/*
 * BeanRendererTest.java (c) 17 November 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.2.11
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.DefaultProcess
 * @see com.cp.common.beans.DefaultUser
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.User
 * @see com.cp.common.log4j.BeanRenderer
 */

package com.cp.common.log4j;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.Process;
import com.cp.common.beans.User;
import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.support.AuditableVisitor;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class BeanRendererTest extends TestCase {

  public BeanRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BeanRendererTest.class);
    return suite;
  }

  private Process getProcess(final String processName) {
    return new DefaultProcess(processName);
  }

  private User getUser(final String username) {
    return new DefaultUser(username);
  }

  public void testDoRender() throws Exception {
    final Calendar changedDate = DateUtil.getCalendar(2006, Calendar.NOVEMBER, 17, 2, 30, 0, 0);

    final Person person = new PersonImpl(1);
    person.setFirstName("Jon");
    person.setLastName("Doe");
    person.setDateOfBirth(DateUtil.getCalendar(1991, Calendar.MAY, 15, 12, 30, 15, 0));
    person.setGender(Gender.MALE);
    person.setRace(Race.WHITE);
    person.accept(AuditableVisitor.getInstance(getUser("blumj"), changedDate));
    person.commit();

    final StringBuffer expectedString = new StringBuffer("{");
    expectedString.append("beanHistory = null, class = ").append(person.getClass());
    expectedString.append(", createdBy = blumj, createdDate = 11/17/2006 02:30:00 AM, creatingProcess = null, dateOfBirth = 05/15/1991 12:30:15 PM");
    expectedString.append(", firstName = Jon, gender = Male, id = 1, lastModifiedBy = blumj, lastModifiedDate = 11/17/2006 02:30:00 AM, lastName = Doe");
    expectedString.append(", modified = false, modifiedBy = blumj, modifiedDate = 11/17/2006 02:30:00 AM, modifiedProperties = {}:java.util.Collections$UnmodifiableSet");
    expectedString.append(", modifyingProcess = null, mutable = true, new = false, personId = 1, race = White, rollbackCalled = false, throwExceptionOnRollback = false");
    expectedString.append("}");

    final ObjectRenderer beanRenderer = new BeanRenderer();

    assertEquals(expectedString.toString(), beanRenderer.doRender(person));
  }

  public void testNoBeanRendering() throws Exception {
    final ObjectRenderer beanRenderer = new BeanRenderer();

    assertEquals("TEST", beanRenderer.doRender("TEST"));
    assertEquals("null", beanRenderer.doRender(null));
  }

  private static interface Person extends Bean<Integer> {

    public Integer getPersonId();

    public void setPersonId(Integer personId);

    public Calendar getDateOfBirth();

    public void setDateOfBirth(Calendar dateOfBirth);

    public String getFirstName();

    public void setFirstName(String firstName);

    public Gender getGender();

    public void setGender(Gender gender);

    public String getLastName();

    public void setLastName(String lastName);

    public Race getRace();

    public void setRace(Race race);

  }

  private static final class PersonImpl extends AbstractBean<Integer> implements Person {

    private Calendar dateOfBirth;

    private Gender gender;

    private Race race;

    private String firstName;
    private String lastName;

    public PersonImpl() {
    }

    public PersonImpl(final Integer personId) {
      setPersonId(personId);
    }

    public PersonImpl(final Person person) {
      setFirstName(person.getFirstName());
      setLastName(person.getLastName());
      setDateOfBirth(person.getDateOfBirth());
      setGender(person.getGender());
      setRace(person.getRace());
    }

    public Integer getPersonId() {
      return getId();
    }

    public void setPersonId(final Integer personId) {
      setId(personId);
    }

    public Calendar getDateOfBirth() {
      return dateOfBirth;
    }

    public void setDateOfBirth(final Calendar dateOfBirth) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.dateOfBirth = dateOfBirth;
        }
      };
      processChange("dateOfBirth", this.dateOfBirth, dateOfBirth, callbackHandler);
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(final String firstName) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.firstName = firstName;
        }
      };
      processChange("firstName", this.firstName, firstName, callbackHandler);
    }

    public Gender getGender() {
      return gender;
    }

    public void setGender(final Gender gender) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.gender = gender;
        }
      };
      processChange("gender", this.gender, gender, callbackHandler);
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(final String lastName) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.lastName = lastName;
        }
      };
      processChange("lastName", this.lastName, lastName, callbackHandler);
    }

    public Race getRace() {
      return race;
    }

    public void setRace(final Race race) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          PersonImpl.this.race = race;
        }
      };
      processChange("race", this.race, race, callbackHandler);
    }

    public boolean isNew() {
      return ObjectUtil.isNull(getPersonId());
    }

    public Object copy() {
      return new PersonImpl(this);
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Person)) {
        return false;
      }

      final Person that = (Person) obj;

      return ObjectUtil.equals(getPersonId(), that.getPersonId())
        && ObjectUtil.equals(getDateOfBirth(), that.getDateOfBirth())
        && ObjectUtil.equals(getFirstName(), that.getFirstName())
        && ObjectUtil.equals(getGender(), that.getGender())
        && ObjectUtil.equals(getLastName(), that.getLastName())
        && ObjectUtil.equals(getRace(), that.getRace());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPersonId());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getDateOfBirth());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getFirstName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getGender());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getLastName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getRace());
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{personId = ");

      buffer.append(getPersonId());
      buffer.append(", dateOfBirth = ").append(getDateOfBirth());
      buffer.append(", firstName = ").append(getFirstName());
      buffer.append(", gender = ").append(getGender());
      buffer.append(", lastName = ").append(getLastName());
      buffer.append(", race = ").append(getRace());
      buffer.append("}:").append(getClass().getName());

      return buffer.toString();
    }
  }

}
