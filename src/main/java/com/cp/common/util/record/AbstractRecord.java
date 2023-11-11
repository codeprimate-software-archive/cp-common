/*
 * AbstractRecord.java (c) 17 April 2002
 *
 * The AbstractRecord class provides a default implementation of the Map
 * interface as well as the Comparable interface.  Also, certain features
 * of a Record interface common to all Record objects are also implemented
 * in this class.
 *
 * To implement the Record interface, a programmer only need to extend the
 * AbstractRecord implementation and override the following methods:
 *
 * <code>
 *  addField(field:String, defaultValue:Object)
 *  getField(fieldIndex:I):String
 *  getFieldIndex(field:String):I
 *  getValue(field:String):Object
 *  removeField(field:String):Object
 *  setValue(field:String, value:Object)
 *  size():I
 * </code>
 *
 * Copyright (c) 2002, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see java.util.AbstractMap
 * @see com.cp.common.util.record.Record
 * @see com.codeprimate.util.record.DefaultRecord;
 */

package com.cp.common.util.record;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.MutableUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ComparableComparator;
import com.cp.common.util.Visitor;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractRecord<K> extends AbstractMap<K, Object> implements Record<K> {

  private boolean mutable = Mutable.MUTABLE;

  private transient int modCount = 0;

  protected final Log log = LogFactory.getLog(getClass());

  private Map<Class, Comparator> comparatorMap = new HashMap<Class, Comparator>();

  /**
   * Default constructor for the AbstractRecord class.
   */
  public AbstractRecord() {
  }

  /**
   * The interface used by Visitor objects to traverse the Object graph and perform the defined operation
   * of the Visitor on this Object.  This method implements the Visitor design pattern allowing new
   * operations to be performed on this Object without having to modify the Object's interface (contractual
   * agreement).
   * @param visitor the Visitor object used to invoke or perform some operation on this Object.
   * @see com.cp.common.util.Visitor#visit
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Adds the specified field to the this Record, thus modifying it's structure.  The field value
   * defaults to null.
   * @param field the field to add to this Record.
   * @return a boolean value indicating whether the specified field was added to this Record.
   */
  public final boolean addField(final K field) {
    return addField(field, null);
  }

  /**
   * An AbstractMap method used to clear the contents of this Map structure (both keys and values).
   * For the case of the Record structure, clear removes all fields and corresponding values.
   * @see Map#clear
   */
  public final void clear() {
    super.clear();
  }

  /**
   * Constructs a clone of this Record object implemented using a shallow-copy.  The cloned Record structure
   * will contain the same fields and refer to the same values as this Record object.  The implementation of
   * this method calls the copy method of the Copyable interface.
   * @return a clone of this Record.
   * @throws CloneNotSupportedException if the clone operation is not supported.
   * @see java.lang.Cloneable#clone
   * @see com.cp.common.lang.Copyable#copy
   */
  public final Object clone() throws CloneNotSupportedException {
    return copy();
  }

  /**
   * Computes the logical ordering of this Record object to the Record parameter.
   * @param record expected Record object used to compare for absolute ordering with this Record object.
   * @return a negative integer value if this Record object is logically less than the Record parameter,
   * zero if they are equal, and a positive integer value if this Record object is logically greater than
   * the Record parameter.
   * @see Comparable#compareTo
   */
  public int compareTo(final Record record) {
    K field = null;

    try {
      if (log.isDebugEnabled()) {
        log.debug("Record Type (" + ClassUtil.getClassName(record) + ")");
      }

      // verify Record structure compatibility
      if ((size() != record.size()) || !keySet().containsAll(record.keySet())) {
        log.warn(ClassUtil.getClassName(record) + " is not a compatible Record implementation!");
        throw new IllegalArgumentException(ClassUtil.getClassName(record) + " is not a compatible Record implementation!");
      }

      int compareValue = 0;

      for (Iterator<K> it = keySet().iterator(); it.hasNext() && (compareValue == 0); ) {
        field = it.next();

        if (log.isDebugEnabled()) {
          log.debug("field (" + field + ")");
        }

        final Object value = getValue(field);
        final Object recordValue = record.getValue(field);

        if (log.isDebugEnabled()) {
          log.debug("value (" + value + ")");
          log.debug("record value (" + recordValue + ")");
        }

        Comparator comparator = null;

        if (ObjectUtil.isNull(value) || ObjectUtil.isNull(recordValue)) {
          comparator = new ComparableComparator();
        }
        else {
          comparator = ObjectUtil.getDefaultValue(getComparator(value), new ComparableComparator());
        }

        if (log.isDebugEnabled()) {
          log.debug("comparator (" + comparator + ")");
        }

        compareValue = comparator.compare(value, recordValue);

        if (log.isDebugEnabled()) {
          log.debug("compareValue (" + compareValue + ")");
        }
      }

      return compareValue;
    }
    catch (NoSuchFieldException e) {
      log.error("Programming Error!  Incompatible Record (" + ClassUtil.getClassName(record)
        + ") implementation!  The field (" + field + ") was not contained by the other Record structure in the comparison!", e);
      throw new IllegalArgumentException("Programming Error!  Incompatible Record (" + ClassUtil.getClassName(record)
        + ") implementation!  The field (" + field + ") was not contained by the other Record structure in the comparison!", e);
    }
  }

  /**
   * Determines whether this Record contains the specified field.
   * @param field the field being determined for containment by this Record.
   * @return a boolean value indicating if this Record contains the specified field.
   */
  public boolean containsField(final K field) {
    if (log.isDebugEnabled()) {
      log.debug("contains field (" + field + ")");
    }

    return keySet().contains(field);
  }

  /**
   * Determines whether the specified key (field) is part of this Record.
   * @param key the field tested for containment by this Record object.
   * @return a boolean value indicating if this Record contains the specified key (field).
   * @see java.util.Map#containsKey
   */
  public final boolean containsKey(final Object key) {
    return super.containsKey(key);
  }

  /**
   * Determines whether the specified value is mapped to a field in this Record object.
   * @param value the Object value tested for containment by this Record object.
   * @return a boolean value indicating if the Object value is mapped to one of the fields
   * in this Record.
   * @see java.util.Map#containsValue
   */
  public final boolean containsValue(final Object value) {
    return super.containsValue(value);
  }

  /**
   * Returns a Set view of the mappings (key-value pairs) contained in this Record.
   * @return a Set object containing field to value mappings constituting the structure of this Record.
   * @see java.util.Map#entrySet()
   */
  public final Set<Map.Entry<K, Object>> entrySet() {
    return new AbstractSet<Map.Entry<K, Object>>() {
      int expectedModCount = getModCount();

      public boolean add(final Map.Entry<K, Object> mapEntry) {
        Assert.notNull(mapEntry, "Cannot add a null field-value mapping to this Record!");
        verifyMutable();
        return addField(mapEntry.getKey(), mapEntry.getValue());
      }

      public Iterator<Map.Entry<K, Object>> iterator() {
        return new Iterator<Map.Entry<K, Object>>() {
          boolean calledNextMethod = false;
          int fieldIndex = 0;
          RecordEntry recordEntry = null;

          public boolean hasNext() {
            return (fieldIndex < size());
          }

          public Map.Entry<K, Object> next() {
            if (expectedModCount != getModCount()) {
              log.warn("The underlying Record has been modified outside this Iterator!");
              throw new ConcurrentModificationException("The underlying Record has been modified outside this Iterator!");
            }

            if (fieldIndex >= size()) {
              log.warn("No more elements!");
              throw new NoSuchElementException("No more elements!");
            }

            calledNextMethod = true;
            recordEntry = new RecordEntry(fieldIndex++);

            return recordEntry;
          }

          public void remove() {
            verifyMutable();
            try {
              Assert.state(calledNextMethod, "The next method has not yet been called, or the remove method has already been called after the last call to the next method.");

              // Remove the field!
              AbstractRecord.this.removeField(recordEntry.getKey());

              fieldIndex--;
              expectedModCount = incModCount();
              calledNextMethod = false;
            }
            catch (NoSuchFieldException ignore) {
              log.warn("Programming error... the NoSuchFieldException should not have been thrown!", ignore);
            }
          }
        };
      }

      public int size() {
        return AbstractRecord.this.size();
      }
    };
  }

  /**
   * Determines whether some Object is equal to this Record.
   * @param obj the Object being compared with this Record.
   * @return a boolean value indicating whether the Object is equal to this Record.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Record)) {
      return false;
    }

    final Record that = (Record) obj;

    return ObjectUtil.equals(entrySet(), that.entrySet());
  }

  /**
   * Returns the value associated with the specified key (field) in this Record structure.
   * @param key the Object value signifying the key, or field, in this Record structure.
   * @return the Object value mapped to the specified key (field) in the Record object.
   * @see java.util.Map#get
   */
  public final Object get(final Object key) {
    return super.get(key);
  }

  /**
   * Gets the value of the field at index as a Boolean.
   * @param fieldIndex the index of the field in the Record for which to return the value for.
   * @return the specified field value as a Boolean.
   * @throws java.lang.ClassCastException if the value of the field is not of type Boolean.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Boolean getBooleanValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Boolean.
   * @param field the field to return a Boolean value for.
   * @return the field value as a Boolean.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Boolean getBooleanValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Byte.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Byte.
   * @throws java.lang.ClassCastException if the value of the field is not of type Byte.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Byte getByteValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Byte.
   * @param field the field to return a Byte value for.
   * @return the field value as a Byte.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Byte getByteValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Calendar.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Calendar.
   * @throws java.lang.ClassCastException if the value of the field is not of type Calendar.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Calendar getCalendarValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Calendar.
   * @param field the field to return a Calendar value for.
   * @return the field value as a Calendar.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Calendar getCalendarValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Character.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Character.
   * @throws java.lang.ClassCastException if the value of the field is not of type Character.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Character getCharacterValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Character.
   * @param field the field to return a Character value for.
   * @return the field value as a Character.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Character getCharacterValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Returns the Comparator object registered for the specified Object type of the value.
   * @param value the Object value who's type is used to return the registered Comparator object.
   * @return a Comparator object for the specified Object parameter type, or null if a Comparator
   * has not been registered for the specified Object's parameter type.
   */
  protected Comparator getComparator(final Object value) {
    final Class valueType = (ObjectUtil.isNull(value) ? null : value.getClass());

    if (log.isDebugEnabled()) {
      log.debug("value type (" + valueType + ")");
    }

    final Comparator comparator = comparatorMap.get(valueType);

    if (log.isDebugEnabled()) {
      log.debug("comparator (" + ClassUtil.getClassName(comparator) + ")");
    }

    if (ObjectUtil.isNull(comparator) && ObjectUtil.isNotNull(valueType)) {
      for (final Class type : comparatorMap.keySet()) {
        if (type.isAssignableFrom(valueType)) {
          return comparatorMap.get(type);
        }
      }
    }

    return comparator;
  }

  /**
   * Gets the value of the field at index as a Date.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Date.
   * @throws java.lang.ClassCastException if the value of the field is not of type Date.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Date getDateValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Date.
   * @param field the field to return a Date value for.
   * @return the field value as a Date.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Date getDateValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Double.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Double.
   * @throws java.lang.ClassCastException if the value of the field is not of type Double.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Double getDoubleValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Double.
   * @param field the field to return a Double value for.
   * @return the field value as a Double.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Double getDoubleValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Float.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Float.
   * @throws java.lang.ClassCastException if the value of the field is not of type Float.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Float getFloatValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Float.
   * @param field the field to return a Float value for.
   * @return the field value as a Float.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Float getFloatValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Integer.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Integer.
   * @throws java.lang.ClassCastException if the value of the field is not of type Integer.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Integer getIntegerValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Integer.
   * @param field the field to return a Integer value for.
   * @return the field value as a Integer.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Integer getIntegerValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a Long.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Long.
   * @throws java.lang.ClassCastException if the value of the field is not of type Long.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Long getLongValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Long.
   * @param field the field to return a Long value for.
   * @return the field value as a Long.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Long getLongValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Returns the count of modifications performed on this Record.
   * @return an integer value of the number of operations that modified the structure or contents
   * of this Record.
   */
  protected synchronized final int getModCount() {
    return modCount;
  }

  /**
   * Gets the value of the field at index as a Short.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Short.
   * @throws java.lang.ClassCastException if the value of the field is not of type Short.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Short getShortValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a Short.
   * @param field the field to return a Short value for.
   * @return the field value as a Short.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Short getShortValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at index as a String.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a String.
   * @throws java.lang.ClassCastException if the value of the field is not of type String.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public String getStringValue(final int fieldIndex) {
    return getValue(fieldIndex);
  }

  /**
   * Gets the value of the specified field as a String.
   * @param field the field to return a String value for.
   * @return the field value as a String.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public String getStringValue(final K field) throws NoSuchFieldException {
    return getValue(field);
  }

  /**
   * Gets the value of the field at the specified index in this Record.
   * @param fieldIndex the index of the field in the Record in which to return the value.
   * @return the specified field value.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public <T> T getValue(final int fieldIndex) {
    if (log.isDebugEnabled()) {
      log.debug("getting value @ index (" + fieldIndex + ")");
    }

    try {
      validateFieldIndex(fieldIndex);
      return (T) getValue(getField(fieldIndex));
    }
    catch (NoSuchFieldException e) {
      log.warn("no such field (" + getField(fieldIndex) + ")!", e);
    }

    return null;
  }

  /**
   * Computes the hash value of this Record.
   * @return a integer value of the computed hash of this Record.
   */
  public int hashCode() {
    return entrySet().hashCode();
  }

  /**
   * Increments the count of operations modifying this Record.
   * @return the new value of the modCount property.
   */
  protected synchronized final int incModCount() {
    return (++modCount);
  }

  /**
   * Determines whether this Record structure is empty, or whether there exist any field-value mappings.
   * @return a boolean value indicating if this Record object is empty or not.
   * @see java.util.Map#isEmpty
   */
  public final boolean isEmpty() {
    return super.isEmpty();
  }

  /**
   * Determines whether this Record object can be modified, or is read-only.
   * @return a boolean value indicating whether this Record object can be modified or not.
   * @see com.cp.common.lang.Mutable#isMutable
   */
  public boolean isMutable() {
    return mutable;
  }

  /**
   * Returns a Set view of the keys, or fields, contained in this Record structure.
   * @return a Set view of the keys, or fields, of this Record object.
   * @see java.util.Map#keySet
   */
  public final Set<K> keySet() {
    return super.keySet();
  }

  /**
   * Maps the specified key (or field) to the specified value in this Record.
   * @param key the field associated with the corresponding Object value.
   * @param value the Object value mapped to the specified key.
   * @return the previous value associated with the key, or "value" if no other value
   * was mapped to the specified key.
   * @see java.util.Map#put
   */
  public final Object put(final K key, final Object value) {
    verifyMutable();

    if (containsKey(key)) {
      try {
        if (log.isDebugEnabled()) {
          log.debug("record contains key (" + key + ")");
        }
        return setValue(key, value);
      }
      catch (NoSuchFieldException ignore) {
        // The containsKey method prevents this Exception from happening!
        log.warn("Programming error... the NoSuchFieldException should not have been thrown!", ignore);
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("adding key (" + key + ") having value (" + value + ") to record");
    }

    addField(key, value);

    return value;
  }

  /**
   * Copies all of the mappings from the specified Map, or Record object, intto this Record.
   * @param map the Map object from which mappings are copied to this Record.
   * @see java.util.Map#putAll
   */
  public final void putAll(final Map<? extends K, ? extends Object> map) {
    super.putAll(map);
  }

  /**
   * Registers the specified Comparator object for the specified class type.  Thus, during
   * a comparison of Objects of the specified type, the associated Comparator to this type
   * will be used to determine the relationship.
   * @param type the Class type of the Objects for which the specified Comparator should be used.
   * @param comparator the Comparator instance used to the compare Objects of the specified type.
   */
  public void registerComparator(final Class type, final Comparator comparator) {
    Assert.notNull(type, "The class type for which the Comparator will be used cannot be null!");
    comparatorMap.put(type, comparator);
  }

  /**
   * Remove the specified key (field) from this Record.
   * @param key the field to remove from this Record.
   * @return the value mapped to the specified key (field) in this Record.
   * @see java.util.Map#remove
   */
  public final Object remove(final Object key) {
    return super.remove(key);
  }

  /**
   * Removes the field at index in this Record and returns it's value.
   * @param fieldIndex the integer index of the field to remove from this Record.
   * @return the value of the field being removed from this Record.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Object removeField(final int fieldIndex) {
    verifyMutable();

    if (log.isDebugEnabled()) {
      log.debug("removing field @ index (" + fieldIndex + ")");
    }

    try {
      validateFieldIndex(fieldIndex);
      return removeField(getField(fieldIndex));
    }
    catch (NoSuchFieldException e) {
      log.warn("no such field (" + getField(fieldIndex) + ")!", e);
    }

    return null;
  }

  /**
   * Sets whether this Record object can be modified or not.
   * @param mutable a boolean property indicating if this Record object can be modfied, or is read-only.
   * @see com.cp.common.lang.Mutable#setMutable
   */
  public void setMutable(final boolean mutable) {
    if (log.isDebugEnabled()) {
      log.debug("mutable (" + mutable + ")");
    }

    this.mutable = mutable;
  }

  /**
   * Sets the value of the field at index in this Record to the specified Object value.
   * @param fieldIndex the index of the field in this Record.
   * @param value the Object value to set on the specified field.
   * @return the original value of the field at index in this Record.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Object setValue(final int fieldIndex, final Object value) {
    verifyMutable();

    if (log.isDebugEnabled()) {
      log.debug("setting value (" + value + ") @ index (" + fieldIndex + ")");
    }

    try {
      validateFieldIndex(fieldIndex);
      return setValue(getField(fieldIndex), value);
    }
    catch (NoSuchFieldException e) {
      log.warn("no such field @ index (" + fieldIndex + ")!", e);
    }

    return null;
  }

  /**
   * Returns a String representation of this Record.
   * @return a String describing this Record.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("[");

    for (Map.Entry mapEntry : entrySet()) {
      buffer.append("{field = ");
      buffer.append(mapEntry.getKey());
      buffer.append(", value = ");
      buffer.append(mapEntry.getValue());
      buffer.append(size() > 1 ? "}, " : "}");
    }

    buffer.append("]:").append(getClass().getName());

    return buffer.toString();
  }

  /**
   * Unregisters the Comparator object for the specified class type.  If the class type
   * was not mapped to a Comparator object, then the method returns null.
   * @param type the class type for which the Comparator was registered.
   * @return the Comparator registered with the specified class type.
   */
  public Object unregisterComparator(final Class type) {
    return comparatorMap.remove(type);
  }

  /**
   * Verifies that the field index is constrained by the bounds of the Record structure.
   * @param fieldIndex the index of the field in this Record structure.
   * @throws java.lang.IndexOutOfBoundsException if the field index is invalid.
   */
  protected void validateFieldIndex(final int fieldIndex) {
    if (log.isDebugEnabled()) {
      log.debug("validating fieldIndex (" + fieldIndex + ")");
    }

    if (fieldIndex < 0 || fieldIndex >= size()) {
      log.warn("Invalid field index (" + fieldIndex + ").  Expected a value between 0 and " + (size() - 1));
      throw new IndexOutOfBoundsException("Invalid field index (" + fieldIndex + ").  Expected a value between 0 and "
        + (size() - 1));
    }
  }

  /**
   * Returns a Collection view of the values contained in this Record.
   * @return a Collection view of the values in this Record.
   * @see java.util.Map#values
   */
  public final Collection<Object> values() {
    return super.values();
  }

  /**
   * A convenience method used to verify that this Record is mutable.  If this Record is immutable,
   * then the method throws a runtime com.cp.common.lang.ObjectImmutableException.
   */
  protected final void verifyMutable() {
    MutableUtil.verifyMutable(this);
  }

  /**
   * The RecordEntry class is an implementation of the Map.Entry interface for return Iterator
   * objects over this Record object.
   */
  private class RecordEntry implements Map.Entry<K, Object> {

    private final int fieldIndex;

    /**
     * Creates an instance of the RecordEntry class, an implmentation of the Map.Entry interface,
     * representing a mapping between a Record field, specified by the field index, and it's
     * associated value.
     * @param fieldIndex the index of the field in this Record.
     * @throws java.lang.IndexOutOfBoundsException if the field index is not valid!
     */
    public RecordEntry(final int fieldIndex) {
      if (log.isDebugEnabled()) {
        log.debug("fieldIndex (" + fieldIndex + ")");
      }
      validateFieldIndex(fieldIndex);
      this.fieldIndex = fieldIndex;
    }

    /**
     * Returns the index of the field in this Record referenced in this mapping.
     * @return an integer value specifying the field index of the referenced field.
     */
    public int getFieldIndex() {
      return fieldIndex;
    }

    /**
     * Returns the field in this Record referenced in this mapping according to the field index.
     * @return the Record field referenced in this mapping.
     */
    public K getKey() {
      return getField(getFieldIndex());
    }

    /**
     * Returns the Object value associated to the corresponding field of this mapping.
     * @return an Object value associated with the field in this mapping.
     */
    public Object getValue() {
      return AbstractRecord.this.getValue(getFieldIndex());
    }

    /**
     * Sets the value for the associated field in this mapping.
     * @param value the Object value for the specified field in this mapping.
     * @return the old value previously associated with the Record field.
     */
    public Object setValue(final Object value) {
      return AbstractRecord.this.setValue(fieldIndex, value);
    }

    /**
     * Determines whether the specified Object is equal to this RecordEntry.
     * @param obj the object being compared for equality with this RecordEntry object.
     * @return a boolean value indicating if the specified Object and this RecordEntry
     * are equal.
     */
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof com.cp.common.util.record.AbstractRecord.RecordEntry)) {
        return false;
      }

      final RecordEntry that = (RecordEntry) obj;

      return getFieldIndex() == that.getFieldIndex()
        && ObjectUtil.equals(getKey(), that.getKey())
        && ObjectUtil.equals(getValue(), that.getValue());
    }

    /**
     * Computes the hash value of this RecordEntry.
     * @return an integer value specifying the hash value of this RecordEntry.
     */
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + new Integer(getFieldIndex()).hashCode();
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getKey());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
      return hashValue;
    }

    /**
     * Returns a String representation of this RecordEntry.
     * @return a String representation of this RecordEntry.
     */
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{fieldIndex = ");
      buffer.append(getFieldIndex());
      buffer.append(", key = ").append(getKey());
      buffer.append(", value = ").append(getValue());
      buffer.append("):").append(getClass().getName());
      return buffer.toString();
    }
  }

}
