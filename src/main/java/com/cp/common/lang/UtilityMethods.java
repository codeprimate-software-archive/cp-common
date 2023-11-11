/*
 * UtilityMethods.java (c) 17 August 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.24
 */

package com.cp.common.lang;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.util.ComparableComparator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UtilityMethods {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Copies the contents of the specified object to another object.
   * @param fromObject the source object in the copy operation.
   * @return a CopyUtility instance to copy the contents of the specified object to another object.
   */
  public static CopyUtility copy(final Object fromObject) {
    return new CopyUtilityImpl(fromObject);
  }

  /**
   * Determines whether the specified object is of some intrinsic property, such as null or the same
   * as some other object, etc.
   * @param fromObject the source object in the
   * @return an instance of the IsUtility class to determine the intrinsic property of some object.
   */
  public static IsUtility is(final Object fromObject) {
    return new IsUtilityImpl(fromObject);

  }

  protected static abstract class AbstractUtility {

    private final Object fromObject;

    public AbstractUtility(final Object fromObject) {
      this.fromObject = fromObject;
    }

    protected final <T> T getFromObject() {
      return (T) fromObject;
    }
  }

  public static interface CopyUtility {

    public void to(Object toObject);

  }

  protected static class CopyUtilityImpl extends AbstractUtility implements CopyUtility {

    public CopyUtilityImpl(final Object fromObject) {
      super(fromObject);
    }

    public void to(final Object toObject) {
      final Map<String, Object> propertyValueMap = BeanUtil.getPropertyValues(getFromObject());
      propertyValueMap.remove("class");
      BeanUtil.setPropertyValues(toObject, propertyValueMap);
    }
  }

  public static interface IsUtility {

    public <T extends Comparable<T>> boolean after(T toObject);

    public <T extends Comparable<T>> boolean before(T toObject);

    public boolean equal(Object toObject);

    public boolean False();

    public IsUtility not();

    public boolean Null();

    public <T extends Comparable<T>> boolean on(T toObject);

    public <T extends Comparable<T>> boolean onOrAfter(T toObject);

    public <T extends Comparable<T>> boolean onOrBefore(T toObject);

    public boolean same(Object toObject);

    public boolean True();

  }

  protected static class IsUtilityImpl extends AbstractUtility implements IsUtility {

    private boolean expectedOutcome = true;

    public IsUtilityImpl(final Object fromObject) {
      super(fromObject);
    }

    protected boolean isExpectedOutcome() {
      return expectedOutcome;
    }

    public <T extends Comparable<T>> boolean after(final T toObject) {
      return (isExpectedOutcome() == (compareTo(toObject) > 0));
    }

    public <T extends Comparable<T>> boolean before(final T toObject) {
      return (isExpectedOutcome() == (compareTo(toObject) < 0));
    }

    protected <T extends Comparable<T>> int compareTo(final T toObject) {
      return ComparableComparator.<T>getInstance().compare(IsUtilityImpl.this.<T>getFromObject(), toObject);
    }

    public boolean equal(final Object toObject) {
      return (isExpectedOutcome() == ObjectUtil.equals(getFromObject(), toObject));
    }

    public boolean False() {
      return (!isExpectedOutcome() == Boolean.parseBoolean(StringUtil.toString(getFromObject())));
    }

    public IsUtility not() {
      expectedOutcome = !expectedOutcome;
      return this;
    }

    public boolean Null() {
      return (isExpectedOutcome() == ObjectUtil.isNull(getFromObject()));
    }

    public <T extends Comparable<T>> boolean on(final T toObject) {
      return (isExpectedOutcome() == (compareTo(toObject) == 0));
    }

    public <T extends Comparable<T>> boolean onOrAfter(final T toObject) {
      return (isExpectedOutcome() == (compareTo(toObject) >= 0));
    }

    public <T extends Comparable<T>> boolean onOrBefore(final T toObject) {
      return (isExpectedOutcome() == (compareTo(toObject) <= 0));
    }

    public boolean same(final Object toObject) {
      return (isExpectedOutcome() == ObjectUtil.isSame(getFromObject(), toObject));
    }

    public boolean True() {
      return (isExpectedOutcome() == Boolean.parseBoolean(StringUtil.toString(getFromObject())));
    }
  }

}
