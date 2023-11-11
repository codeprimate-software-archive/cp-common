/*
 * AbstractObjectRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 */

package com.cp.common.log4j;

import com.cp.common.lang.ObjectUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractObjectRendererTest extends TestCase {

  public AbstractObjectRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractObjectRendererTest.class);
    //suite.addTest(new AbstractObjectRendererTest("testName"));
    return suite;
  }

  public void testDoRenderer() throws Exception {
    final ConcreteObjectRenderer concreteObjectRenderer = new ConcreteObjectRenderer();

    assertEquals("TEST", concreteObjectRenderer.doRender("TEST"));
    assertEquals("Jon Doe", concreteObjectRenderer.doRender(new Person("Jon", "Doe")));
    assertEquals("null", concreteObjectRenderer.doRender(null));
  }

  private static final class ConcreteObjectRenderer extends AbstractObjectRenderer {

    public String doRender(final Object obj) {
      return getRenderer(obj).doRender(obj);
    }
  }
  
  private static final class Person {
    
    private String firstName;
    private String lastName;

    public Person() {
    }

    public Person(final String firstName, final String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(final String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(final String lastName) {
      this.lastName = lastName;
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Person)) {
        return false;
      }

      final Person that = (Person) obj;

      return ObjectUtil.equals(getFirstName(), that.getFirstName())
        && ObjectUtil.equals(getLastName(), that.getLastName());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getFirstName());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getLastName());
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer(getFirstName());
      buffer.append(" ");
      buffer.append(getLastName());
      return buffer.toString().trim();
    }
  }

}
