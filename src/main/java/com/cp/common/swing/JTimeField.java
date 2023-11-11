/*
 * JTimeField.java (c) 17 April 2002
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.1.15
 * @see com.cp.common.swing.JDateField
 * @see java.beans.PropertyChangeListener
 * @see javax.swing.JTextField
 */

package com.cp.common.swing;

import com.cp.common.awt.event.KeyEventUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.DateUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public final class JTimeField extends JTextField implements PropertyChangeListener {

  private static final Logger logger = Logger.getLogger(JTimeField.class);

  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");

  private static final Font MONOSPACED = new Font("Monospaced", Font.PLAIN, 12);

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  private DataModel timeModel;

  private SelectionModel selectionModel;

  /**
   * Creates an instance of the JTimeField component class.
   */
  public JTimeField() {
    this(Calendar.getInstance());
  }

  /**
   * Creates an instance of the JTimeField component class initialized to the specified time represented with a
   * Calendar object.
   */
  public JTimeField(final Calendar time) {
    super(8);
    addKeyListener(new TimeFieldKeyListener());
    addMouseInputListener(new TimeFieldMouseInputListener());
    setDataModel(new DefaultTimeModel(time));
    setFont(MONOSPACED);
    setSelectionModel(new TimeFieldSelectionModel());
    super.setText(ObjectUtil.isNull(time) ? TIME_FORMAT.format(Calendar.getInstance().getTime())
      : TIME_FORMAT.format(time.getTime()));
  }

  /**
   * Creates an instance of the JTimeField class initialized to the specified String value of time.  The format
   * of the String value is 'hh:mm a'.
   * @param timeValue a String value representing the time to initialize the JTimeField component.
   * @throws ParseException if the format to the String time value is not valid.
   */
  public JTimeField(final String timeValue) throws ParseException {
    this(StringUtil.isEmpty(timeValue) ? (Calendar) null : DateUtil.getCalendar(TIME_FORMAT.parse(timeValue)));
  }

  /**
   * Factory method to get an instance of the JTimeField component class.  This method specifically handles
   * the ParseException and throws an IllegalArgumentException instead.
   * @param timeValue a String value representing the time to initialize the JTimeField component.
   * @return an instance of the JTimeField component class if property constructed.
   * @throws IllegalArgumentException if the timeValue String is an invalid time.
   */
  public static JTimeField getTimeField(final String timeValue) {
    if (logger.isDebugEnabled()) {
      logger.debug("timeValue (" + timeValue + ")");
    }
    try {
      return new JTimeField(timeValue);
    }
    catch (ParseException e) {
      logger.warn("(" + timeValue + ") is not a valid time!");
      throw new IllegalArgumentException("(" + timeValue + ") is not a valid time!");
    }
  }

  /**
   * Adds the specified MouseInputListener object as both the mouse listener and mouse motion listener.
   * @param listener the javax.swing.event.MouseInputListener to be registered with this time field component
   * as the mouse listener and mouse motion listener.
   */
  private void addMouseInputListener(final MouseInputListener listener) {
    addMouseListener(listener);
    addMouseMotionListener(listener);
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of the TimeDocument.
   * @return a javax.swing.text.Document object representing the default model implementation for the time field
   * text component.
   */
  protected Document createDefaultModel() {
    return new TimeDocument();
  }

  /**
   * Receives notification of the property change events from the DataModel when the time value for this
   * component changes.
   * @param event the PropertyChangeEvent tracking the time changes.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    final Calendar time = (Calendar) event.getNewValue();
    if (logger.isDebugEnabled()) {
      logger.debug("time change (" + time + ")");
    }
    super.setText(TIME_FORMAT.format(time.getTime()));
  }

  public Calendar getCalendar() {
    return getDataModel().getCalendar();
  }

  public void setCalendar(final Calendar time) {
    if (logger.isDebugEnabled()) {
      logger.debug("time (" + time + ")");
    }
    getDataModel().setCalendar(time);
  }

  public DataModel getDataModel() {
    return timeModel;
  }

  public void setDataModel(final DataModel timeModel) {
    if (logger.isDebugEnabled()) {
      logger.debug("timeModel (" + timeModel + ")");
    }
    if (ObjectUtil.isNull(timeModel)) {
      logger.warn("The DataModel for the time field component cannot be null!");
      throw new NullPointerException("The DataModel for the time field component cannot be null!");
    }
    this.timeModel = timeModel;
    this.timeModel.addPropertyChangeListener(this);
  }

  public SelectionModel getSelectionModel() {
    return selectionModel;
  }

  public void setSelectionModel(final SelectionModel selectionModel) {
    if (logger.isDebugEnabled()) {
      logger.debug("selectionModel (" + selectionModel + ")");
    }
    this.selectionModel = (ObjectUtil.isNotNull(selectionModel) ? selectionModel : new TimeFieldSelectionModel());
  }

  public void setText(final String text) {
    if (logger.isDebugEnabled()) {
      logger.debug("text (" + text + ")");
    }
    try {
      getDataModel().setTime(ObjectUtil.isNull(text) ? Calendar.getInstance() : DateUtil.getCalendar(TIME_FORMAT.parse(text)));
    }
    catch (ParseException e) {
      logger.error("The text (" + text + ") is not a valid time format!  Please specify the time in the following format ("
        + TIME_FORMAT.toString() + ").", e);
      throw new IllegalArgumentException("The text (" + text + ") is not a valid time format!  Please specify the time in the following format ("
        + TIME_FORMAT.toString() + ").");
    }
  }

  public static interface DataModel {

    public Calendar getCalendar();

    public void setCalendar(Calendar calendar);

    public Calendar getTime();

    public void setTime(Calendar time);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void roll(int timeField, boolean up);

    public void set(int timeField, int value);

    public void toggleAmPm();

    public void removePropertyChangeListener(PropertyChangeListener listener);
  }

  public static interface SelectionModel {

    public boolean isAmPmSelected();

    public TimeField getSelectedTimeField();

    public void setSelectedTimeField(TimeField selectedTimeField);

    public void selectAmPm();

    public void selectHour();

    public void selectMinute();

    public void selectNext();

    public void selectPrevious();

    public void selectSecond();
  }

  /**
   * The TimeDocument class is the default document used by the JTimeField component class  to represent time
   * in a standard JTextField component.
   */
  private final class TimeDocument extends PlainDocument {

    private static final char A_CHAR = 'a';
    private static final char COLAN_CHAR = ':';
    private static final char M_CHAR = 'm';
    private static final char P_CHAR = 'p';
    private static final char SPACE_CHAR = ' ';

    private static final int AM_PM_POSITION = 6;
    private static final int HOUR_MINUTE_COLAN_POSITION = 2;
    private static final int SPACE_POSITION = 5;

    public void insertString(final int offset, final String value, final AttributeSet attrSet)
        throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
        logger.debug("attrSet (" + attrSet + ")");
      }

      if (!isValidTime(offset, value)) {
        TOOLKIT.beep();
      }
      else {
        super.insertString(offset, value, attrSet);
      }

      getSelectionModel().setSelectedTimeField(getSelectionModel().getSelectedTimeField());
    }

    /**
     * Determines whether the given String value at the specified offset is a valid time.
     * @param value a String value representing the time.
     * @return a boolean value of true if the String value is a valid time, false otherwise.
     */
    private boolean isValidTime(final int offset, final String value) {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
      }
      boolean valid = true;  // innocent until proven guilty
      for (int index = 0, len = value.length(); index < len && valid; index++) {
        final char c = value.charAt(index);
        logger.debug("c = '" + c + "'");
        switch (offset + index) {
          case HOUR_MINUTE_COLAN_POSITION:
            valid &= (c == COLAN_CHAR);
            break;
          case SPACE_POSITION:
            valid &= (c == SPACE_CHAR);
            break;
          case AM_PM_POSITION:
            valid &= (Character.toLowerCase(c) == A_CHAR || Character.toLowerCase(c) == P_CHAR);
            break;
          case 7:
            valid &= (Character.toLowerCase(c) == M_CHAR);
            break;
          default:
            valid &= Character.isDigit(c);
        }
      }
      return valid;
    }
  }

  private static final class TimeField {

    private final int calendarConstant;
    private final int selectionEnd;
    private final int selectionStart;

    private final String description;

    private TimeField next;
    private TimeField previous;

    public TimeField(final String description,
                     final int calendarConstant,
                     final int selectionStart,
                     final int selectionEnd) {
      this.description = description;
      this.calendarConstant = calendarConstant;
      this.selectionStart = selectionStart;
      this.selectionEnd = selectionEnd;
    }

    public int getCalendarConstant() {
      return calendarConstant;
    }

    /**
     * Returns a descriptive name for the Calendar constant (Calendar.YEAR, Calenar.MONTH, etc).
     * NOTE: this was not added to the DateUtil generic class since this method is specific to debugging information
     * of this class and does not handle internationalization properly.
     * @param calendarConstant a constant representing the (YEAR, MONTH, etc) in the Calendar object.
     * @return a descriptive name for the Calendar constant.
     */
    private static String getCalendarConstantDescription(final int calendarConstant) {
      logger.debug("calendarConstant = " + calendarConstant);
      switch (calendarConstant) {
        case Calendar.YEAR:
          return "Year";
        case Calendar.MONTH:
          return "Month";
        case Calendar.DAY_OF_MONTH:
          return "Day of Month";
        case Calendar.DAY_OF_WEEK:
          return "Day of Week";
        case Calendar.DAY_OF_WEEK_IN_MONTH:
          return "Day of Week in Month";
        case Calendar.DAY_OF_YEAR:
          return "Day of Year";
        case Calendar.HOUR:
          return "Hour";
        case Calendar.HOUR_OF_DAY:
          return "Hour of Day";
        case Calendar.MINUTE:
          return "Minute";
        case Calendar.SECOND:
          return "Second";
        case Calendar.MILLISECOND:
          return "Millisecond";
        default:
          return "Unknown";
      }
    }

    public String getDescription() {
      return description;
    }

    public TimeField getNext() {
      return next;
    }

    public void setNext(final TimeField next) {
      if (logger.isDebugEnabled()) {
        logger.debug("next (" + next + ")");
      }
      this.next = next;
    }

    public TimeField getPrevious() {
      return previous;
    }

    public void setPrevious(final TimeField previous) {
      if (logger.isDebugEnabled()) {
        logger.debug("previous (" + previous + ")");
      }
      this.previous = previous;
    }

    public int getSelectionEnd() {
      return selectionEnd;
    }

    public int getSelectionStart() {
      return selectionStart;
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof TimeField)) {
        return false;
      }

      final TimeField that = (TimeField) obj;

      return (getCalendarConstant() == that.getCalendarConstant())
        && ObjectUtil.equals(getDescription(), that.getDescription())
        && (getSelectionEnd() == that.getSelectionEnd())
        && (getSelectionStart() == that.getSelectionStart());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + getCalendarConstant();
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
      hashValue = 37 * hashValue + getSelectionEnd();
      hashValue = 37 * hashValue + getSelectionStart();
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{calendarConstant = ");
      buffer.append(getCalendarConstantDescription(getCalendarConstant()));
      buffer.append(", description = ").append(getDescription());
      buffer.append(", selectionEnd = ").append(getSelectionEnd());
      buffer.append(", selectionStart = ").append(getSelectionStart());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * The TimeFieldKeyListener is used by the JTimeField component class to track key events targeted at
   * editing and traversing the time field text component.
   */
  private final class TimeFieldKeyListener extends KeyAdapter {

    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_SPACE:
          e.consume(); // cannot remove the contents of time
          TOOLKIT.beep();
          break;
        case KeyEvent.VK_HOME:
          e.consume();
          getSelectionModel().selectHour();
          break;
        case KeyEvent.VK_LEFT:
          e.consume();
          getSelectionModel().selectPrevious();
          break;
        case KeyEvent.VK_END:
          e.consume();
          getSelectionModel().selectAmPm();
          break;
        case KeyEvent.VK_RIGHT:
          e.consume();
          getSelectionModel().selectNext();
          break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
          if (getSelectionModel().isAmPmSelected()) {
            logger.debug("am/pm is selected!");
            getDataModel().toggleAmPm();
          }
          else {
            getDataModel().roll(getSelectionModel().getSelectedTimeField().getCalendarConstant(),
              (e.getKeyCode() == KeyEvent.VK_UP));
          }
          break;
        default:
          if (KeyEventUtil.isNumeric(e.getKeyCode())) {
            final int timeField = getSelectionModel().getSelectedTimeField().getCalendarConstant();
            final int value = Integer.parseInt(String.valueOf(e.getKeyChar()));
            getDataModel().set(timeField, value);
          }
      }
    }

    /**
     * Processes the key typed event.  Consumes all key typed events!  This method is needed for proper functioning
     * of the JTimeField component.
     * @param event the KeyEvent object representing the key typed event.
     */
    public void keyTyped(final KeyEvent event) {
      event.consume();
    }
  }

  /**
   * The TimeFieldMouseInputListener class is used by the JTimeField component class to track mouse events
   * when the user sets focus to this time field text component.
   */
  private final class TimeFieldMouseInputListener extends MouseInputAdapter {

    /**
     * Called to handle the MouseEvents defined by this TimeFieldMouseListener class.
     * @param event the MouseEvent object capturing information about the mouse event.
     */
    private void handleMouseEvent(final MouseEvent event) {
      getSelectionModel().selectHour();
      event.consume();
    }

    /**
     * Called when a mouse button has been clicked in the time field component.
     * @param event a MouseEvent object capturing the mouse clicked event in the date field component.
     */
    public void mouseClicked(final MouseEvent event) {
      handleMouseEvent(event);
    }

    /**
     * Called when the mouse has been dragged in the time field component.
     * @param event a MouseEvent object capturing the mouse drag event in the date field component.
     */
    public void mouseDragged(final MouseEvent event) {
      handleMouseEvent(event);
    }

    /**
     * Called when a mouse button has been pressed in the time field component.
     * @param event a MouseEvent object capturing the mouse pressed event in the date field component.
     */
    public void mousePressed(final MouseEvent event) {
      handleMouseEvent(event);
    }
  }

  private final class TimeFieldSelectionModel implements SelectionModel {

    private final Color selectedTextColor = Color.white;
    private final Color selectionColor = new Color(10, 36, 106);

    private final TimeField AMPM = new TimeField("am/pm", Calendar.AM_PM, 6, 8);
    private final TimeField HOUR = new TimeField("hour", Calendar.HOUR_OF_DAY, 0, 2);
    private final TimeField MINUTE = new TimeField("minute", Calendar.MINUTE, 3, 5);
    private final TimeField SECOND = new TimeField("second", Calendar.SECOND, 6, 8);

    {
      AMPM.setNext(HOUR);
      AMPM.setPrevious(MINUTE);
      MINUTE.setNext(AMPM);
      MINUTE.setPrevious(HOUR);
      HOUR.setNext(MINUTE);
      HOUR.setPrevious(AMPM);
    }

    private TimeField selectedTimeField = HOUR;

    public TimeFieldSelectionModel() {
      setSelectedTextColor(selectedTextColor);
      setSelectionColor(selectionColor);
    }

    public boolean isAmPmSelected() {
      return AMPM.equals(getSelectedTimeField());
    }

    public TimeField getSelectedTimeField() {
      return selectedTimeField;
    }

    public void setSelectedTimeField(final TimeField selectedTimeField) {
      if (logger.isDebugEnabled()) {
        logger.debug("selectedTimeField (" + selectedTimeField + ")");
      }
      this.selectedTimeField = selectedTimeField;
      setSelectionStart(this.selectedTimeField.getSelectionStart());
      setSelectionEnd(this.selectedTimeField.getSelectionEnd());
    }

    public void selectAmPm() {
      setSelectedTimeField(AMPM);
    }

    public void selectHour() {
      setSelectedTimeField(HOUR);
    }

    public void selectMinute() {
      setSelectedTimeField(MINUTE);
    }

    public void selectNext() {
      setSelectedTimeField(getSelectedTimeField().getNext());
    }

    public void selectPrevious() {
      setSelectedTimeField(getSelectedTimeField().getPrevious());
    }

    public void selectSecond() {
      setSelectedTimeField(SECOND);
    }
  }

}
