/*
 * CommonEventTestCase.java (c) 26 December 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.27
 * @see com.cp.common.beans.CommonBeanTestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.CommonBeanTestCase;

public abstract class CommonEventTestCase extends CommonBeanTestCase {

  public CommonEventTestCase(final String testName) {
    super(testName);
  }

  protected static Person getPerson() {
    return new PersonImpl();
  }

  protected static Person getPerson(final Integer personId) {
    return new PersonImpl(personId);
  }

  protected static Person getPerson(final String firstName, final String lastName) {
    final Person person = new PersonImpl();
    person.setFirstName(firstName);
    person.setLastName(lastName);
    return person;
  }

  protected static Person getPerson(final Person person) {
    return new PersonImpl(person);
  }

}
