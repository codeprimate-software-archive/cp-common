/*
 * EnumConverterTest.java (c) 22 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.8
 * @see com.cp.common.beans.util.converters.EnumConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.enums.AbstractEnum;
import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.enums.State;
import com.cp.common.util.ConversionException;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.Converter;

public class EnumConverterTest extends TestCase {

  public EnumConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EnumConverterTest.class);
    //suite.addTest(new EnumConverterTest("testName"));
    return suite;
  }

  public void testConvert() throws Exception {
    final AbstractConverter enumConverter = new EnumConverter();

    assertNotNull(enumConverter);
    assertNull(enumConverter.getDefaultValue());
    assertFalse(enumConverter.isUsingDefaultValue());
    assertNull(enumConverter.convert(Gender.class, null));
    assertNull(enumConverter.convert(Race.class, null));
    assertEquals(Gender.FEMALE, enumConverter.convert(Gender.class, Gender.FEMALE));
    assertEquals(Race.NATIVE_AMERICAN, enumConverter.convert(Race.class, Race.NATIVE_AMERICAN.getCode()));
    assertEquals(Race.BLACK, enumConverter.convert(Race.class, Race.BLACK.getExternalCode()));
    assertEquals(Race.WHITE, enumConverter.convert(Race.class, Race.WHITE.getId()));
    assertEquals(Gender.MALE, enumConverter.convert(Gender.class, Gender.MALE.getId().toString()));
  }

  public void testConvertWithIncompatibleType() throws Exception {
    final Converter enumConverter = new EnumConverter();
    com.cp.common.enums.Enum enumx = null;

    assertNull(enumx);

    try {
      enumx = (com.cp.common.enums.Enum) enumConverter.convert(Enum.class, Race.WHITE.getId());
      fail("Calling convert with an incompatible Class type (JDK Enum) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(" + Enum.class.getName() + ") must implement the com.cp.common.enums.Enum interface!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an incompatible Class type (JDK Enum) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(enumx);
  }

  public void testConvertWithInvalidValue() throws Exception {
    final Converter enumConverter = new EnumConverter();
    com.cp.common.enums.Enum enumx = null;

    assertNull(enumx);

    try {
      enumx = (com.cp.common.enums.Enum) enumConverter.convert(Race.class, Boolean.TRUE);
      fail("Calling convert with an invalid Object value should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Unable to determine Enum of type (" + Race.class.getName() + ") for value (true)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with an invalid Object value threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(enumx);
  }

  public void testConvertWithNullType() throws Exception {
    final Converter enumConverter = new EnumConverter();
    com.cp.common.enums.Enum enumx = null;

    assertNull(enumx);

    try {
      enumx = (com.cp.common.enums.Enum) enumConverter.convert(null, Gender.FEMALE.getId());
      fail("Calling convert with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type of the Enum to convert to cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(enumx);
  }

  public void testGetEnumByValue() throws Exception {
    final EnumConverter converter = new EnumConverter();

    assertEquals(Gender.FEMALE, converter.getEnumByCode(Gender.class, Gender.FEMALE.getCode()));
    assertEquals(Race.WHITE, converter.getEnumByCode(Race.class, Race.WHITE.getCode()));
    assertEquals(State.WISCONSIN, converter.getEnumByCode(State.class, State.WISCONSIN.getCode()));
    assertEquals(Gender.MALE, converter.getEnumByExternalCode(Gender.class, Gender.MALE.getExternalCode()));
    assertEquals(Race.BLACK, converter.getEnumByExternalCode(Race.class, Race.BLACK.getExternalCode()));
    assertEquals(State.IOWA, converter.getEnumByExternalCode(State.class, State.IOWA.getExternalCode()));
    assertEquals(Gender.FEMALE, converter.getEnumById(Gender.class, Gender.FEMALE.getId()));
    assertEquals(Race.NATIVE_AMERICAN, converter.getEnumById(Race.class, Race.NATIVE_AMERICAN.getId()));
    assertEquals(State.MONTANA, converter.getEnumById(State.class, State.MONTANA.getId()));
  }

  public void testGetEnumByValueWithInvalidValues() throws Exception {
    final EnumConverter converter = new EnumConverter();
    com.cp.common.enums.Enum enumx = null;

    assertNull(enumx);

    try {
      enumx = converter.getEnumById(Gender.class, -1);
      fail("Calling getEnumById for Gender with an ID of (-1) should have thrown an ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(-1) is not a valid (ID) for Enum type (" + Gender.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getEnumById for Gender with an ID of (-1) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(enumx);

    try {
      enumx = converter.getEnumByExternalCode(Gender.class, "I");
      fail("Calling getEnumByExternalCode for Gender with an external code of (I) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(I) is not a valid (External Code) for Enum type (" + Gender.class.getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getEnumByExternalCode for Gender with an external code of (I) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(enumx);

    try {
      enumx = converter.getEnumByCode(Gender.class, "it");
      fail("Calling getEnumByCode for Gender with a code of (it) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("(it) is not a valid (Code) for Enum type (" + Gender.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getEnumByCode for Gender with a code of (it) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(enumx);
  }

  public void testGetEnumValues() throws Exception {
    final EnumConverter converter = new EnumConverter();

    final Set expectedGenderValues = Gender.values();
    final Set actualGenderValues = converter.getEnumValues(Gender.class);

    assertNotNull(actualGenderValues);
    assertEquals(expectedGenderValues, actualGenderValues);

    final Set expectedRaceValues = Race.values();
    final Set actualRaceValues = converter.getEnumValues(Race.class);

    assertNotNull(actualRaceValues);
    assertEquals(expectedRaceValues, actualRaceValues);

    final Set expectedStateValues = State.values();
    final Set actualStateValues = converter.getEnumValues(State.class);

    assertNotNull(actualStateValues);
    assertEquals(expectedStateValues, actualStateValues);
  }

  public void testGetEnumValuesOnEnumWithNoValuesMethod() throws Exception {
    final EnumConverter converter = new EnumConverter();
    Set enumValues = null;

    assertNull(enumValues);

    try {
      enumValues = converter.getEnumValues(MockEnum.class);
      fail("Calling getEnumValues on MockEnum should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Failed to get all the values of Enum type (" + MockEnum.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getEnumValues on MockEnum threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(enumValues);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean();

    assertNotNull(bean);
    assertNull(bean.getGender());
    assertNull(bean.getRace());
    assertNull(bean.getState());

    BeanUtil.setPropertyValue(bean, "gender", Gender.FEMALE.getId());
    BeanUtil.setPropertyValue(bean, "race", Race.WHITE.getCode());
    BeanUtil.setPropertyValue(bean, "state", State.OREGON.getExternalCode());

    assertEquals(Gender.FEMALE, bean.getGender());
    assertEquals(Race.WHITE, bean.getRace());
    assertEquals(State.OREGON, bean.getState());

    BeanUtil.setPropertyValue(bean, "gender", null);
    BeanUtil.setPropertyValue(bean, "race", null);
    BeanUtil.setPropertyValue(bean, "state", null);

    assertNull(bean.getGender());
    assertNull(bean.getRace());
    assertNull(bean.getState());
  }

  public static final class MockBean {

    private Gender gender;
    private Race race;
    private State state;

    public Gender getGender() {
      return gender;
    }

    public void setGender(final Gender gender) {
      this.gender = gender;
    }

    public Race getRace() {
      return race;
    }

    public void setRace(final Race race) {
      this.race = race;
    }

    public State getState() {
      return state;
    }

    public void setState(final State state) {
      this.state = state;
    }
  }

  public static final class MockEnum extends AbstractEnum {

    public static final MockEnum MOCK_ENUM_VALUE = new MockEnum(-1, "Mock Enum Code",
      "Mock Enum Description", "Mock Enum External Code", 0);

    public MockEnum(final Integer id, final String code, final String description) {
      super(id, code, description);
    }

    public MockEnum(final Integer id, final String code, final String description, final String externalCode) {
      super(id, code, description, externalCode);
    }

    public MockEnum(final Integer id, final String code, final String description, final String externalCode, final Integer sequence) {
      super(id, code, description, externalCode, sequence);
    }
  }

}
