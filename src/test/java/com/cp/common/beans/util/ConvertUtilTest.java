/*
 * ConvertUtilTest.java (c) 2 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.3
 * @see com.cp.common.beans.CommonBeanTestCase
 * @see com.cp.common.beans.util.ConvertUtil
 */

package com.cp.common.beans.util;

import com.cp.common.beans.CommonBeanTestCase;
import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.beans.util.converters.NumberConverter;
import com.cp.common.beans.util.converters.UserConverter;
import com.cp.common.enums.Gender;
import com.cp.common.enums.Race;
import com.cp.common.enums.State;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.IntegerConverter;

public class ConvertUtilTest extends CommonBeanTestCase {

  public ConvertUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ConvertUtilTest.class);
    //suite.addTest(new ConvertUtilTest("testName"));
    return suite;
  }

  public void testConvert() throws Exception {
    assertNull(ConvertUtil.convert(Object.class, null));
    assertNull(ConvertUtil.convert(String.class, "null"));
    assertNull(ConvertUtil.convert(String.class, " null "));
    assertEquals("", ConvertUtil.convert(String.class, ""));
    assertEquals(" ", ConvertUtil.convert(String.class, " "));
    assertEquals("test", ConvertUtil.convert(String.class, "test"));
    assertEquals(Boolean.TRUE, ConvertUtil.convert(Boolean.class, "true"));
    assertEquals(Boolean.FALSE, ConvertUtil.convert(Boolean.class, "false"));
    assertEquals(new Character('A'), ConvertUtil.convert(Character.class, "A"));
    assertEquals(new Character('z'), ConvertUtil.convert(Character.class, "z"));
    assertEquals(new Double(3.14159), ConvertUtil.convert(Double.class, "3.14159"));
    assertEquals(new Long(123456789L), ConvertUtil.convert(Long.class, "123456789"));
    assertEquals(new PersonImpl(1), ConvertUtil.convert(Person.class, 1));
    assertEquals(Gender.FEMALE, ConvertUtil.convert(Gender.class, Gender.FEMALE.getId()));
    assertEquals(Race.WHITE, ConvertUtil.convert(Race.class, Race.WHITE.getCode()));
    assertEquals(State.WISCONSIN, ConvertUtil.convert(State.class, State.WISCONSIN.getExternalCode()));
    assertEquals(new DefaultProcess("system"), ConvertUtil.convert(com.cp.common.beans.Process.class, "system"));
    assertEquals(new URL("http://www.codeprimate.com"), ConvertUtil.convert(URL.class, "http://www.codeprimate.com"));
    assertEquals(new DefaultUser("root"), ConvertUtil.convert(User.class, "root"));
    assertEquals("test", ConvertUtil.convert(MockObject.class, "test"));
  }

  public void testConvertWithNullType() throws Exception {
    try {
      ConvertUtil.convert(null, "1");
      fail("Calling convert with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The class type to convert to cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetConverter() throws Exception {
    final Converter integerConverter = new IntegerConverter();
    final Converter numberConverter = new NumberConverter();

    final Map<Class, Converter> converterMap = new HashMap<Class, Converter>(2);
    converterMap.put(Integer.class, integerConverter);

    Converter actualConverter = ConvertUtil.getConverter(converterMap, Integer.class);

    assertNotNull(actualConverter);
    assertEquals(integerConverter, actualConverter);

    actualConverter = ConvertUtil.getConverter(converterMap, Long.class);

    assertNull(actualConverter);

    converterMap.put(Number.class, numberConverter);

    actualConverter = ConvertUtil.getConverter(converterMap, Long.class);

    assertNotNull(actualConverter);
    assertEquals(numberConverter, actualConverter);

    actualConverter = ConvertUtil.getConverter(converterMap, Integer.class);

    assertNotNull(actualConverter);
    assertEquals(integerConverter, actualConverter);
  }

  public void testGetConverterWithNullClassType() throws Exception {
    try {
      ConvertUtil.getConverter(Collections.<Class, Converter>emptyMap(), null);
      fail("Calling getConverter with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The class type for the registered Converter cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getConverter with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetConverterWithNullConverterMap() throws Exception {
    try {
      ConvertUtil.getConverter(null, Object.class);
      fail("Calling getConverter with a null Converter Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Converter Map cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getConverter with a null Converter Map threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testRegister() throws Exception {
    assertNull(ConvertUtil.convert(String.class, null));

    ConvertUtil.register(String.class, new MockStringConverter());

    assertEquals("null", ConvertUtil.convert(String.class, null));

    ConvertUtil.unregister(String.class);
  }

  public void testRegisterWithNullClassType() throws Exception {
    try {
      ConvertUtil.register(null, new MockStringConverter());
      fail("Calling register with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The class type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling register with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testRegisterWithNullConverter() throws Exception {
    try {
      ConvertUtil.register(Object.class, null);
      fail("Calling register with a null Converter should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The converter cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling register with a null Converter threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testUnregister() throws Exception {
    ConvertUtil.register(User.class, new MockUserConverter());

    assertNull(ConvertUtil.convert(User.class, null));
    assertEquals(new MockUser("blumj"), ConvertUtil.convert(User.class, "blumj"));

    ConvertUtil.unregister(User.class);

    assertNull(ConvertUtil.convert(User.class, null));
    assertEquals(new DefaultUser("blumj"), ConvertUtil.convert(User.class, "blumj"));
  }

  private static final class MockObject {
  }

  private static final class MockStringConverter implements Converter {

    public Object convert(final Class type, final Object value) {
      return ObjectUtil.getDefaultValue(value, "null");
    }
  }

  private static final class MockUserConverter extends UserConverter {

    protected User getUserImpl(final String username) {
      return new MockUser(username);
    }
  }

  private static final class MockUser implements User {

    private final String username;

    public MockUser(final String username) {
      Assert.notNull(username, "The username cannot be null!");
      this.username = (username + "@home.com");
    }

    public String getUsername() {
      return username;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof User)) {
        return false;
      }

      final User that = (User) obj;

      return ObjectUtil.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getUsername());
      return hashValue;
    }

    @Override
    public String toString() {
      return getUsername();
    }
  }

}
