/*
 * RecordUtil.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.util.record.Record
 * @see com.cp.common.util.record.RecordTable
 */

package com.cp.common.util.record;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Visitor;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RecordUtil {

  private static final Log log = LogFactory.getLog(RecordUtil.class);

  /**
   * Default private constructor enforcing non-instantiability.
   */
  private RecordUtil() {
  }

  /**
   * Synchronizes access to the specified Record object.
   * @param record is the Record object in which to serialize access.
   * @return a synchronized, thread-safe instance of the Record object.
   */
  public static Record synchronizedRecord(final Record record) {
    return new SynchronizedRecord(record);
  }

  /**
   * Synchronizes access to the specified Record object using a common
   * mutex (mutual exclusive lock).
   * @param record the Record object in which to serialize access.
   * @param mutex the mutual lock Object used to restrict access to the
   * Record object.
   * @return a synchronized version of the specified Record.
   */
  static Record synchronizedRecord(final Record record, final Object mutex) {
    return new SynchronizedRecord(record, mutex);
  }

  /**
   * Synchronizes access to the specified RecordTable object.
   * @param recordTable is the RecordTable object in which to serialize access.
   * @return a synchronized, thread-safe instance of the RecordTable object.
   */
  public static RecordTable synchronizedRecordTable(final RecordTable recordTable) {
    return new SynchronizedRecordTable(recordTable);
  }

  /**
   * Synchronizes access to the specified RecordTable object using a common
   * mutex (mutual exclusive lock).
   * @param recordTable the RecordTable object in which to serialize access.
   * @param mutex the mutual lock Object used to restrict access to the
   * RecordTable object.
   * @return a synchronized version of the specified RecordTable.
   */
  static RecordTable synchronizedRecordTable(final RecordTable recordTable, final Object mutex) {
    return new SynchronizedRecordTable(recordTable, mutex);
  }

  /**
   * Wraps the specified Record object inside a Record implementation that
   * cannot be modified.  The read-only Record implementation takes advantage
   * of the Mutable interface by returning Mutable.IMMUTABLE from the
   * isMutable method and throwing an UnsupportedOperationException from the
   * setMutable method.
   *
   * @param record the Record object to make read-only.
   * @return a Record implementation encapsulating the original Record object
   * with read-only access.
   */
  public static Record unmodifiableRecord(final Record record) {
    return new UnmodifiableRecord(record);
  }

  /**
   * Wraps the specified Record object inside a RecordTable implementation that
   * cannot be modified.  The read-only RecordTable implementation takes advantage
   * of the Mutable interface by returning Mutable.IMMUTABLE from the
   * isMutable method and throwing an UnsupportedOperationException from the
   * setMutable method.
   *
   * @param recordTable the RecordTable object to make read-only.
   * @return a RecordTable implementation encapsulating the original RecordTable
   * object with read-only access.
   */
  public static RecordTable unmodifiableRecordTable(final RecordTable recordTable) {
    return new UnmodifiableRecordTable(recordTable);
  }

  /**
   * SynchronizedRecord is a wrapper Record implementation providing synchronized (serialized) access
   * to a Record object.
   */
  private static class SynchronizedRecord<K> implements Record<K> {
    private Collection values;

    // The Object in which to synchronize the Record with.
    private final Object mutex;

    private Record record;

    private Set entrySet;
    private Set keySet;

    /**
     * Default constructor wrapping the specified Record object for serial access.
     * @param record the Record object in which to provide synchronized access to.
     */
    public SynchronizedRecord(final Record record) {
      this(record, null);
    }

    /**
     * Constructor specifying the Object used as the mutual exclusive lock used to
     * synchronize access to the specified Record object.
     * @param record the Record object to wrap with serial access.
     * @param mutex the mutual exclusive lock used for synchronization of the
     * Record object.
     */
    public SynchronizedRecord(final Record record, Object mutex) {
      if (log.isDebugEnabled()) {
        log.debug("Record: " + record);
        log.debug("Mutex: " + mutex);
      }

      if (ObjectUtil.isNull(record)) {
        log.warn("The record paramter was null!");
        throw new NullPointerException("The Record object to synchronize cannot be null!");
      }

      if (ObjectUtil.isNull(mutex)) {
        log.warn("The mutex was null, defaulting to this!");
        mutex = this;
      }

      this.record = record;
      this.mutex = mutex;
    }

    public void accept(final Visitor visitor) {
      synchronized (mutex) {
        record.accept(visitor);
      }
    }

    public boolean addField(final K field) {
      synchronized (mutex) {
        return record.addField(field);
      }
    }

    public boolean addField(final K field, final Object defaultValue) {
      synchronized (mutex) {
        return record.addField(field, defaultValue);
      }
    }

    public void clear() {
      synchronized (mutex) {
        record.clear();
      }
    }

    public int compareTo(final Record rec) {
      synchronized (mutex) {
        return record.compareTo(rec);
      }
    }

    public boolean containsField(final K field) {
      synchronized (mutex) {
        return record.containsField(field);
      }
    }

    public boolean containsKey(final Object key) {
      synchronized (mutex) {
        return record.containsKey(key);
      }
    }

    public boolean containsValue(final Object value) {
      synchronized (mutex) {
        return record.containsValue(value);
      }
    }

    public Object copy() {
      synchronized (mutex) {
        return AbstractRecordFactory.getInstance().getRecordInstance(this);
      }
    }

    public Set<Map.Entry<K, Object>> entrySet() {
      if (ObjectUtil.isNull(entrySet)) {
        synchronized (mutex) {
          if (ObjectUtil.isNull(entrySet)) {
            entrySet = com.cp.common.util.record.Collections.synchronizedSet(record.entrySet(), mutex);
          }
        }
      }
      return entrySet;
    }

    public Object get(final Object key) {
      synchronized (mutex) {
        return record.get(key);
      }
    }

    public Boolean getBooleanValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getBooleanValue(fieldIndex);
      }
    }

    public Boolean getBooleanValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getBooleanValue(field);
      }
    }

    public Byte getByteValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getByteValue(fieldIndex);
      }
    }

    public Byte getByteValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getByteValue(field);
      }
    }

    public Calendar getCalendarValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getCalendarValue(fieldIndex);
      }
    }

    public Calendar getCalendarValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getCalendarValue(field);
      }
    }

    public Character getCharacterValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getCharacterValue(fieldIndex);
      }
    }

    public Character getCharacterValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getCharacterValue(field);
      }
    }

    public Date getDateValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getDateValue(fieldIndex);
      }
    }

    public Date getDateValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getDateValue(field);
      }
    }

    public Double getDoubleValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getDoubleValue(fieldIndex);
      }
    }

    public Double getDoubleValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getDoubleValue(field);
      }
    }

    public K getField(final int fieldIndex) {
      synchronized (mutex) {
        return (K) record.getField(fieldIndex);
      }
    }

    public int getFieldIndex(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getFieldIndex(field);
      }
    }

    public Float getFloatValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getFloatValue(fieldIndex);
      }
    }

    public Float getFloatValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getFloatValue(field);
      }
    }

    public Integer getIntegerValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getIntegerValue(fieldIndex);
      }
    }

    public Integer getIntegerValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getIntegerValue(field);
      }
    }

    public Long getLongValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getLongValue(fieldIndex);
      }
    }

    public Long getLongValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getLongValue(field);
      }
    }

    public Short getShortValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getShortValue(fieldIndex);
      }
    }

    public Short getShortValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getShortValue(field);
      }
    }

    public String getStringValue(final int fieldIndex) {
      synchronized (mutex) {
        return record.getStringValue(fieldIndex);
      }
    }

    public String getStringValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.getStringValue(field);
      }
    }

    public <T> T getValue(final int fieldIndex) {
      synchronized (mutex) {
        return (T) record.getValue(fieldIndex);
      }
    }

    public <T> T getValue(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return (T) record.getValue(field);
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return record.isEmpty();
      }
    }

    public boolean isMutable() {
      synchronized (mutex) {
        return record.isMutable();
      }
    }

    public Set<K> keySet() {
      if (ObjectUtil.isNull(keySet)) {
        synchronized (mutex) {
          if (ObjectUtil.isNull(keySet)) {
            keySet = com.cp.common.util.record.Collections.synchronizedSet(record.keySet(), mutex);
          }
        }
      }
      return keySet;
    }

    public Object put(final K key, final Object value) {
      synchronized (mutex) {
        return record.put(key, value);
      }
    }

    public void putAll(final Map<? extends K, ? extends Object> t) {
      synchronized (mutex) {
        record.putAll(t);
      }
    }

    public void registerComparator(final Class type, final Comparator comparator) {
      synchronized (mutex) {
        record.registerComparator(type, comparator);
      }
    }

    public Object remove(final Object key) {
      synchronized (mutex) {
        return record.remove(key);
      }
    }

    public Object removeField(final int fieldIndex) {
      synchronized (mutex) {
        return record.removeField(fieldIndex);
      }
    }

    public Object removeField(final K field) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.removeField(field);
      }
    }

    public void setMutable(final boolean mutable) {
      synchronized (mutex) {
        record.setMutable(mutable);
      }
    }

    public Object setValue(final int fieldIndex, final Object value) {
      synchronized (mutex) {
        return record.setValue(fieldIndex, value);
      }
    }

    public Object setValue(final K field, final Object value) throws NoSuchFieldException {
      synchronized (mutex) {
        return record.setValue(field, value);
      }
    }

    public int size() {
      synchronized (mutex) {
        return record.size();
      }
    }

    public Object unregisterComparator(final Class type) {
      synchronized (mutex) {
        return record.unregisterComparator(type);
      }
    }

    public Collection<Object> values() {
      if (ObjectUtil.isNull(values)) {
        synchronized (mutex) {
          if (ObjectUtil.isNull(values)) {
            values = com.cp.common.util.record.Collections.synchronizedCollection(values, mutex);
          }
        }
      }
      return values;
    }
  }

  private static class SynchronizedRecordTable implements RecordTable {

    private final Object mutex;
    private final RecordTable recordTable;

    public SynchronizedRecordTable(final RecordTable recordTable) {
      this(recordTable, null);
    }

    public SynchronizedRecordTable(final RecordTable recordTable, Object mutex) {
      if (log.isDebugEnabled()) {
        log.debug("recordTable (" + recordTable + ")");
        log.debug("mutex (" + mutex + ")");
      }

      if (ObjectUtil.isNull(recordTable)) {
        log.warn("The RecordTable parameter cannot be null!");
        throw new NullPointerException("The RecordTable parameter cannot be null!");
      }

      if (ObjectUtil.isNull(mutex)) {
        log.warn("The mutex was null; defaulting to this!");
        mutex = this;
      }

      this.recordTable = recordTable;
      this.mutex = mutex;
    }

    public void accept(final Visitor visitor) {
      synchronized (mutex) {
        recordTable.accept(visitor);
      }
    }

    public boolean add(final Record rec) {
      synchronized (mutex) {
        return recordTable.add(rec);
      }
    }

    public boolean addAll(final Collection<? extends Record> c) {
      synchronized (mutex) {
        return recordTable.addAll(c);
      }
    }

    public boolean addColumn(final Column column) {
      synchronized (mutex) {
        return recordTable.addColumn(column);
      }
    }

    public boolean addRow(final Record row) {
      synchronized (mutex) {
        return recordTable.addRow(row);
      }
    }

    public void clear() {
      synchronized (mutex) {
        recordTable.clear();
      }
    }

    public Object copy() {
      synchronized (mutex) {
        return recordTable.copy();
      }
    }

    public int columnCount() {
      synchronized (mutex) {
        return recordTable.columnCount();
      }
    }

    public Iterator<Column> columnIterator() {
      synchronized (mutex) {
        return recordTable.columnIterator();
      }
    }

    public boolean contains(final Column column) {
      synchronized (mutex) {
        return recordTable.contains(column);
      }
    }

    public boolean contains(final Object o) {
      synchronized (mutex) {
        return recordTable.contains(o);
      }
    }

    public boolean contains(final Record record) {
      synchronized (mutex) {
        return recordTable.contains(record);
      }
    }

    public boolean containsAll(final Collection c) {
      synchronized (mutex) {
        return recordTable.containsAll(c);
      }
    }

    public Record get(final int index) {
      synchronized (mutex) {
        return recordTable.get(index);
      }
    }

    public <T> T getCellValue(final int rowIndex, final int columnIndex) {
      synchronized (mutex) {
        return (T) recordTable.getCellValue(rowIndex, columnIndex);
      }
    }

    public <T> T getCellValue(final int rowIndex, final Column column) {
      synchronized (mutex) {
        return (T) recordTable.getCellValue(rowIndex, column);
      }
    }

    public Column getColumn(final int columnIndex) {
      synchronized (mutex) {
        return recordTable.getColumn(columnIndex);
      }
    }

    public Column getColumn(final String columnName) {
      synchronized (mutex) {
        return recordTable.getColumn(columnName);
      }
    }

    public int getColumnIndex(final Column column) {
      synchronized (mutex) {
        return recordTable.getColumnIndex(column);
      }
    }

    public List<Column> getColumns() {
      synchronized (mutex) {
        return recordTable.getColumns();
      }
    }

    public Comparator getComparator(final Column column, final Object value) {
      synchronized (mutex) {
        return recordTable.getComparator(column, value);
      }
    }

    public Column getFirstColumn() {
      synchronized (mutex) {
        return recordTable.getFirstColumn();
      }
    }

    public int getFirstColumnIndex() {
      synchronized (mutex) {
        return recordTable.getFirstColumnIndex();
      }
    }

    public Record getFirstRow() {
      synchronized (mutex) {
        return recordTable.getFirstRow();
      }
    }

    public int getFirstRowIndex() {
      synchronized (mutex) {
        return recordTable.getFirstRowIndex();
      }
    }

    public Column getLastColumn() {
      synchronized (mutex) {
        return recordTable.getLastColumn();
      }
    }

    public int getLastColumnIndex() {
      synchronized (mutex) {
        return recordTable.getLastColumnIndex();
      }
    }

    public Record getLastRow() {
      synchronized (mutex) {
        return recordTable.getLastRow();
      }
    }

    public int getLastRowIndex() {
      synchronized (mutex) {
        return recordTable.getLastRowIndex();
      }
    }

    public Record getRow(final int rowIndex) {
      synchronized (mutex) {
        return recordTable.getRow(rowIndex);
      }
    }

    public int getRowIndex(final Record row) {
      synchronized (mutex) {
        return recordTable.getRowIndex(row);
      }
    }

    public boolean insertColumn(final Column column, final int columnIndex) {
      synchronized (mutex) {
        return recordTable.insertColumn(column, columnIndex);
      }
    }

    public boolean insertRow(final Record record, final int rowIndex) {
      synchronized (mutex) {
        return recordTable.insertRow(record, rowIndex);
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return recordTable.isEmpty();
      }
    }

    public boolean isMutable() {
      synchronized (mutex) {
        return recordTable.isMutable();
      }
    }

    public Iterator<Record> iterator() {
      synchronized (mutex) {
        return recordTable.iterator();
      }
    }

    public void registerComparator(final Class type, final Comparator comparator) {
      synchronized (mutex) {
        recordTable.registerComparator(type, comparator);
      }
    }

    public boolean remove(final Object o) {
      synchronized (mutex) {
        return recordTable.remove(o);
      }
    }

    public boolean removeAll() {
      synchronized (mutex) {
        return recordTable.removeAll();
      }
    }

    public boolean removeAll(final Collection c) {
      synchronized (mutex) {
        return recordTable.removeAll(c);
      }
    }

    public Column removeColumn(final int columnIndex) {
      synchronized (mutex) {
        return recordTable.removeColumn(columnIndex);
      }
    }

    public int removeColumn(final Column column) {
      synchronized (mutex) {
        return recordTable.removeColumn(column);
      }
    }

    public Record removeRow(final int rowIndex) {
      synchronized (mutex) {
        return recordTable.removeRow(rowIndex);
      }
    }

    public int removeRow(final Record record) {
      synchronized (mutex) {
        return recordTable.removeRow(record);
      }
    }

    public boolean retainAll(final Collection c) {
      synchronized (mutex) {
        return recordTable.retainAll(c);
      }
    }

    public int rowCount() {
      synchronized (mutex) {
        return recordTable.rowCount();
      }
    }

    public Iterator<Record> rowIterator() {
      synchronized (mutex) {
        return recordTable.rowIterator();
      }
    }

    public void set(final Record record, final int index) throws Exception {
      synchronized (mutex) {
        recordTable.set(record, index);
      }
    }

    public void setCellValue(final int rowIndex, final int columnIndex, final Object value) {
      synchronized (mutex) {
        recordTable.setCellValue(rowIndex, columnIndex, value);
      }
    }

    public void setCellValue(final int rowIndex, final Column column, final Object value) {
      synchronized (mutex) {
        recordTable.setCellValue(rowIndex, column, value);
      }
    }

    public void setMutable(final boolean mutable) {
      synchronized (mutex) {
        recordTable.setMutable(mutable);
      }
    }

    public int size() {
      synchronized (mutex) {
        return recordTable.size();
      }
    }

    public Object[] toArray() {
      synchronized (mutex) {
        return recordTable.toArray();
      }
    }

    public Object[] toArray(final Object a[]) {
      synchronized (mutex) {
        return recordTable.toArray(a);
      }
    }

    public Object[][] toTabular() {
      synchronized (mutex) {
        return recordTable.toTabular();
      }
    }

    public Object[][] toTabular(final int[] rowIndices) {
      synchronized (mutex) {
        return recordTable.toTabular(rowIndices);
      }
    }

    public Object[][] toTabular(final Column[] columns) {
      synchronized (mutex) {
        return recordTable.toTabular(columns);
      }
    }

    public Object[][] toTabular(final int[] rowIndices, final Column[] columns) {
      synchronized (mutex) {
        return recordTable.toTabular(rowIndices, columns);
      }
    }

    public Comparator unregisterComparator(final Class type) {
      synchronized (mutex) {
        return recordTable.unregisterComparator(type);
      }
    }
  }

  /**
   * The UnmodifiableRecord class creates a read-only instance of a Record object.
   */
  private static class UnmodifiableRecord<K> extends com.cp.common.util.record.AbstractRecord<K> {

    private final Record record;

    public UnmodifiableRecord(final Record record) {
      this.record = AbstractRecordFactory.getInstance().getRecordInstance(record);
      this.record.setMutable(Mutable.IMMUTABLE);
    }

    public boolean addField(final K field, final Object defaultValue) {
      log.warn("The Record is immutable!");
      throw new ObjectImmutableException("The Record is immutable!");
    }

    public Object copy() {
      return AbstractRecordFactory.getInstance().getRecordInstance(this);
    }

    public K getField(final int fieldIndex) {
      return (K) record.getField(fieldIndex);
    }

    public int getFieldIndex(final K field) throws NoSuchFieldException {
      return record.getFieldIndex(field);
    }

    public <T> T getValue(final K field) throws NoSuchFieldException {
      return (T) record.getValue(field);
    }

    public final boolean isMutable() {
      return Mutable.IMMUTABLE;
    }

    public Object removeField(final K field) throws NoSuchFieldException {
      log.warn("The Record is immutable!");
      throw new ObjectImmutableException("The Record is immutable!");
    }

    public final void setMutable(final boolean mutable) {
      log.warn("Operation Not Allowed!");
      throw new UnsupportedOperationException("Operation Not Allowed!");
    }

    public Object setValue(final K field, final Object value) throws NoSuchFieldException {
      log.warn("The Record is immutable!");
      throw new ObjectImmutableException("The Record is immutable!");
    }

    public int size() {
      return record.size();
    }
  }

  /**
   * The UnmodifiableRecordTable class creates a read-only instance of a RecordTable object.
   */
  private static class UnmodifiableRecordTable extends AbstractRecordTable {

    private final RecordTable recordTable;

    public UnmodifiableRecordTable(final RecordTable recordTable) {
      this.recordTable = AbstractRecordFactory.getInstance().getRecordTableInstance(recordTable);
      this.recordTable.setMutable(Mutable.IMMUTABLE);
    }

    public Iterator<Column> columnIterator() {
      return recordTable.columnIterator();
    }

    public Object copy() {
      return recordTable.copy();
    }

    public boolean insertColumn(final Column column, final int columnIndex) {
      log.warn("The record table is immutable!");
      throw new ObjectImmutableException("The record table is immutable!");
    }

    public boolean insertRow(final Record record, final int rowIndex) {
      log.warn("The record table is immutable!");
      throw new ObjectImmutableException("The record table is immutable!");
    }

    public boolean isMutable() {
      return Mutable.IMMUTABLE;
    }

    public Iterator<Record> rowIterator() {
      return recordTable.rowIterator();
    }

    public void set(final Record record, final int index) throws Exception {
      log.warn("The record table is immutable!");
      throw new ObjectImmutableException("The record table is immutable!");
    }

    public void setMutable(final boolean mutable) {
      log.warn("Operation Not Allowed!");
      throw new UnsupportedOperationException("Operation Not Allowed!");
    }
  }

}
