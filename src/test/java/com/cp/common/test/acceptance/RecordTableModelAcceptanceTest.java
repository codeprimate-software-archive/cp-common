/*
 * RecordTableModelAcceptanceTest.java (c) 16 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.swing.table.RecordTableModel
 */

package com.cp.common.test.acceptance;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.swing.table.RecordTableModel;
import com.cp.common.util.DateUtil;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordTable;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class RecordTableModelAcceptanceTest extends JFrame {

  private static final Dimension FRAME_SIZE = new Dimension(640, 240);
  private static final Dimension TABLE_SIZE = new Dimension(640, 240);
  private static final String FRAME_TITLE = "RecordTableModel Test";

  public RecordTableModelAcceptanceTest(final String frameTitle) {
    super(frameTitle);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new GridLayout(1, 1));
    getContentPane().add(buildTable());
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JScrollPane buildTable() {
    final JTable table = new JTable(new MyRecordTableModel(getRecordTable()));
    table.setMinimumSize(TABLE_SIZE);
    table.setPreferredSize(TABLE_SIZE);
    return new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  private com.cp.common.util.record.Record getRecord(final Integer personId,
                           final String firstName,
                           final String lastName,
                           final Calendar dateOfBirth) {
    final Record record = AbstractRecordFactory.getInstance().getRecordInstance();
    record.addField("personId", personId);
    record.addField("firstName", firstName);
    record.addField("lastName", lastName);
    record.addField("dateOfBirth", dateOfBirth);
    return record;
  }

  private RecordTable getRecordTable() {
    final com.cp.common.util.record.Column[] columns = {
      new com.cp.common.util.record.ColumnImpl("personId", Integer.class),
      new com.cp.common.util.record.ColumnImpl("firstName", String.class),
      new com.cp.common.util.record.ColumnImpl("lastName", String.class),
      new com.cp.common.util.record.ColumnImpl("dateOfBirth", Calendar.class)
    };

    columns[0].setDisplayName("Person ID");
    columns[1].setDisplayName("First Name");
    columns[2].setDisplayName("Last Name");
    columns[3].setDisplayName("Date of Birth");

    final com.cp.common.util.record.RecordTable recordTable = com.cp.common.util.record.AbstractRecordFactory.getInstance().getRecordTableInstance(columns);

    recordTable.add(getRecord(0, "Jack", "Handy", DateUtil.getCalendar(1955, Calendar.MARCH, 12)));
    recordTable.add(getRecord(1, "Jon", "Doe", DateUtil.getCalendar(1972, Calendar.DECEMBER, 21)));
    recordTable.add(getRecord(2, "Sandy", "Handy", DateUtil.getCalendar(1933, Calendar.SEPTEMBER, 30)));
    recordTable.add(getRecord(3, "Jane", "Doe", DateUtil.getCalendar(1943, Calendar.APRIL, 4)));
    recordTable.add(getRecord(4, "Bob", "Smith", DateUtil.getCalendar(1948, Calendar.MAY, 14)));
    recordTable.add(getRecord(5, "Pam", "Silvers", DateUtil.getCalendar(1962, Calendar.JULY, 5)));
    recordTable.add(getRecord(6, "Tea", "Leoni", DateUtil.getCalendar(1971, Calendar.APRIL, 22)));
    recordTable.add(getRecord(7, "Sara", "Hill", DateUtil.getCalendar(1975, Calendar.JANUARY, 22)));

    return recordTable;
  }

  private final class MyRecordTableModel extends RecordTableModel {

    private final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    private MyRecordTableModel(final com.cp.common.util.record.RecordTable recordTable) {
      super(recordTable);
    }

    public Object getValueAt(final int rowIndex, final int columnIndex) {
      Object value = super.getValueAt(rowIndex, columnIndex);
      if (ObjectUtil.isNotNull(value) && Calendar.class.isAssignableFrom(value.getClass())) {
        value = formatter.format(((Calendar) value).getTime());
      }
      return value;
    }
  }

  public static void main(final String[] args) {
    new RecordTableModelAcceptanceTest(FRAME_TITLE);
  }

}
