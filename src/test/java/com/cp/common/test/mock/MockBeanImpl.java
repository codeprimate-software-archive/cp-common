/*
 * MockBeanImpl.java (c) 18 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.10
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.test.mock.MockBean
 */

package com.cp.common.test.mock;

import com.cp.common.beans.AbstractBean;
import com.cp.common.lang.ObjectUtil;

public class MockBeanImpl extends AbstractBean<Integer> implements MockBean {

  private Object value;

  public MockBeanImpl() {
  }

  public MockBeanImpl(final Integer id) {
    super(id);
  }

  public MockBeanImpl(final Object value) {
    setValue(value);
  }

  public MockBeanImpl(final MockBean bean) {
    setValue(bean.getValue());
  }

  public Object getValue() {
    return value;
  }

  public void setValue(final Object value) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        MockBeanImpl.this.value = value;
      }
    };
    processChange("value", this.value, value, callbackHandler);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof MockBean)) {
      return false;
    }

    final MockBean that = (MockBean) obj;

    return ObjectUtil.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
    return hashValue;
  }

}
