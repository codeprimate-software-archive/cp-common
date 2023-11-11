/*
 * JDateField.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.swing.JTimeField
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
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public final class JDateField extends JTextField implements PropertyChangeListener {

  private static final Logger logger = Logger.getLogger(JDateField.class);

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

  private static final Font MONOSPACED = new Font("Monospaced", Font.PLAIN, 12);

  private static final String DEFAULT_DATE_VALUE = "  /  /  ";

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  private DataModel dataModel;

  private SelectionModel selectionModel;

  /**
   * Creates an instance of the JDateField UI component initialized to today's date.
   */
  public JDateField() {
    this(Calendar.getInstance());
  }

  /**
   * Creates an instance of the JDateField component initialized to the specified Calendar.
   * @param calendar a java.util.Calendar object specifying the date value used to initialze the contents of this
   * JDateField component.
   */
  public JDateField(final Calendar calendar) {
    super(10);
    addKeyListener(new DateFieldKeyListener());
    addMouseInputListener(new DateFieldMouseInputListener());
    setDataModel(new DefaultDateModel(calendar));
    setFont(MONOSPACED);
    setSelectionModel(new DateFieldSelectionModel());
    super.setText(ObjectUtil.isNull(calendar) ? DEFAULT_DATE_VALUE : DATE_FORMAT.format(calendar.getTime()));
  }

  /**
   * Creates an instance of the JDateField component initialized to the specified Date.
   * @param date a java.util.Date object specifying the date value used to initialze the contents of this
   * JDateField component.
   */
  public JDateField(final Date date) {
    this(DateUtil.getCalendar(date));
  }

  /**
   * Creates an instance of the JDateField component intialized with the date represented by the specified
   * String value.
   * @param dateValue a String representation of a date value in the following format MM/dd/yyyy.
   */
  public JDateField(final String dateValue) throws ParseException {
    this(StringUtil.isEmpty(dateValue) ? (Date) null : DATE_FORMAT.parse(dateValue));
  }

  /**
   * Factory method to create an instance of the JDateField component initialized to the specified String
   * value representing the date.
   * @param value a String date representation to initialize the JDateField component.
   * @return an instance of the JDateField class initialized to the specified date represented with the
   * String value.
   * @throws java.lang.IllegalArgumentException if the date represented by the String value is not valid!
   */
  public static JDateField getDateField(final String value) {
    logger.debug("value (" + value + ")");
    try {
      return new JDateField(value);
    }
    catch (ParseException e) {
      logger.error("(" + value + ") is not a valid date!", e);
      throw new IllegalArgumentException("(" + value + ") is not a valid date!");
    }
  }

  /**
   * Adds the specified MouseInputListener object as both the mouse listener and mouse motion listener.
   * @param listener the javax.swing.event.MouseInputListener to be registered with this date field component
   * as the mouse listener and mouse motion listener.
   */
  private void addMouseInputListener(final MouseInputListener listener) {
    addMouseListener(listener);
    addMouseMotionListener(listener);
  }

  /**
   * Creates the default implementation of the model to be used at construction if one isn't explicitly given.
   * Returns an instance of DateDocument.
   * @return the default javax.swing.text.Document object used to represent the content for this date field
   * component.
   */
  protected Document createDefaultModel() {
    return new DateDocument();
  }

  /**
   * Receives notification of the property change events from the DataModel when the date value for this
   * component changes.
   * @param event the PropertyChangeEvent tracking the date changes.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    final Calendar date = (Calendar) event.getNewValue();
    if (logger.isDebugEnabled()) {
      logger.debug("date change (" + date + ")");
    }
    super.setText(DATE_FORMAT.format(date.getTime()));
  }

  /**
   * Returns the date value of the date field as a Calendar object.
   * @return a java.util.Calendar object representing the date value in the date field.
   */
  public Calendar getCalendar() {
    return getDataModel().getCalendar();
  }

  /**
   * Sets the date value of the date field to the specified Calendar value and updates the view.
   * @param calendar the Calendar object specifying the date value to set on the date field.
   */
  public void setCalendar(final Calendar calendar) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendar (" + calendar + ")");
    }
    getDataModel().setCalendar(calendar);
  }

  /**
   * Returns the data model used to contain the data, maintain state and manage changes to the properties of this
   * date field component.
   * @return the data model for the date field component.
   */
  public DataModel getDataModel() {
    return dataModel;
  }

  /**
   * Sets the data dataModel to the specified DateModel for this date field to contain the data, maintain state and
   * handle changes to the properties of this date field.
   * @param dataModel the DateModel used by this date field to manage state.
   */
  public void setDataModel(final DataModel dataModel) {
    if (logger.isDebugEnabled()) {
      logger.debug("dataModel (" + dataModel + ")");
    }
    if (ObjectUtil.isNull(dataModel)) {
      logger.warn("The data model for this date field component cannot be null!");
      throw new NullPointerException("The data model for this date field component cannot be null!");
    }
    this.dataModel = dataModel;
    this.dataModel.addPropertyChangeListener(this);
  }

  /**
   * Returns the date value contained by this date field as a Date object.
   * @return a java.util.Date object representing the date value specified by this date input field component.
   */
  public Date getDate() {
    return getDataModel().getDate();
  }

  /**
   * Sets the JDateField to the specified date by formatting the Date object and extracting the String representation
   * of the date value and calling setText.
   * @param date is the specified Date value used to set the contents of this date input field component.
   */
  public void setDate(final Date date) {
    if (logger.isDebugEnabled()) {
      logger.debug("date (" + date + ")");
    }
    getDataModel().setDate(date);
  }

  /**
   * Returns the SelectionModel for this date field compnent, which manages the selection of the individual date fields
   * in a date.
   * @return the SelectionModel for this date field.
   */
  public SelectionModel getSelectionModel() {
    return selectionModel;
  }

  /**
   * Sets the specified SelectionModel for this date field component.
   * @param selectionModel the SelectionModel used to manage date field selections of a date for this date field
   * component.
   */
  public void setSelectionModel(final SelectionModel selectionModel) {
    if (logger.isDebugEnabled()) {
      logger.debug("selectionModel (" + selectionModel + ")");
    }
    this.selectionModel = (ObjectUtil.isNotNull(selectionModel) ? selectionModel : new DateFieldSelectionModel());
  }

  /**
   * Overridden setText method sets the Calendar to the specified date and then calls super.setText to set the
   * String representation of the date in this date field.  Note, the date should be in the following format
   * MM/dd/yyyy.
   * @param text a String value representing the date to set in this date field component.
   */
  public void setText(final String text) {
    logger.debug("text (" + text + ")");
    try {
      getDataModel().setDate(ObjectUtil.isNull(text) ? null : DATE_FORMAT.parse(text));
    }
    catch (ParseException e) {
      logger.error("The text (" + text + ") is not a valid date format!  Please specify the date in the following format ("
        + DATE_FORMAT.toString() + ").", e);
      throw new IllegalArgumentException("The text (" + text + ") is not a valid date format!"
        + "  Please specify the date in the following format (" + DATE_FORMAT.toString() + ").");
    }
  }

  /**
   * Interface used to describe the data, and operations on the data, that are required by this JDateField component.
   */
  public static interface DataModel {

    public Calendar getCalendar();

    public void setCalendar(Calendar calendar);

    public Date getDate();

    public void setDate(Date date);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void roll(int calendarField, boolean up);

    public void set(int calendarField, int value);

    public void removePropertyChangeListener(PropertyChangeListener listener);
  }

  /**
   * The DateDocument class is used by the JDateField component to format the JTextField component as a
   * date input field with forwarded slashes separating the month, day, and year.
   */
  private final class DateDocument extends PlainDocument {

    private static final char DATE_SEPARATOR = '/';

    /**
     * Called by the JDateField component to enter the specified date content into the JTextField component of this
     * date field at the specified position formatted with the given AttributeSet object.
     * @param offset is an integer offset into the Document to insert content.
     * @param value String content to insert into the Document of this date field component.
     * @param attrSet AttributeSet used to sytle the content.
     * @exception BadLocationException  the given insert position is not attrSet valid
     *   position within the document
     */
    public void insertString(final int offset,
                             final String value,
                             final AttributeSet attrSet)
      throws BadLocationException {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
        logger.debug("attrSet (" + attrSet + ")");
      }

      if (!isValidDate(offset, value)) {
        TOOLKIT.beep();
      }
      else {
        super.insertString(offset, value, attrSet);
      }

      // TODO: determine if I need this method call.
      getSelectionModel().selectDateField(getSelectionModel().getSelectedDateField());
    }

    /**
     * Determines whether the given String value at the specified offset is a valid Date or Date field.
     * NOTE: the implementation of this method is tied to the DATE_FORMAT DateFormat constant of the JDateField class.
     * @param value String object representing the date or date field.
     * @return a boolean value of true if the String value contains a valid date field or represents a valid date,
     * false otherwise.
     */
    private boolean isValidDate(final int offset, final String value) {
      if (logger.isDebugEnabled()) {
        logger.debug("offset = " + offset);
        logger.debug("value (" + value + ")");
      }

      if (StringUtil.isEmpty(value) || value.length() != 10) {
        logger.warn("(" + value + ") is not a valid date!");
        throw new IllegalArgumentException("(" + value + ") is not a valid date!");
        //return false;
      }

      if (DEFAULT_DATE_VALUE.equals(value)) {
        logger.debug("value (" + value + ") equals DEFAULT_DATE_VALUE (" + DEFAULT_DATE_VALUE + ")");
        return true;
      }

      final char[] chars = value.toCharArray();
      boolean valid = true; // innocent until proven guilty

      for (int index = 0; index < chars.length && valid; index++) {
        final char c = chars[index];
        logger.debug("c = " + c);
        switch (offset + index) {
          case 2:
          case 5:
            valid &= (c == DATE_SEPARATOR);
            break;
          default:
            valid &= Character.isDigit(c);
        }
      }

      return valid;
    }
  }

  /**
   * This class represents the various fields of a Date (or Calendar object), such as Month, Day, Year, etc.
   */
  private static final class DateField {

    private int calendarConstant;
    private int selectionEnd;
    private int selectionStart;

    private DateField next;
    private DateField previous;

    private String description;

    private DateField(final String description,
                      final int calendarConstant,
                      final int selectionStart,
                      final int selectionEnd) {
      this.description = description;
      this.calendarConstant = calendarConstant;
      this.selectionStart = selectionStart;
      this.selectionEnd = selectionEnd;
    }

    /**
     * Returns the Calendar constant of the date field that this object represents.
     * @return a Calendar constant of the date field that this object represents.
     */
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

    /**
     * Returns a descriptive name for the date field represented by this object.
     * @return a String description of the date field.
     */
    public String getDescription() {
      return description;
    }

    /**
     * Returns the next date field in the order of date fields according to the specified date format.
     * @return a DateField object representing the date field after this date field.
     */
    public DateField getNext() {
      return next;
    }

    /**
     * Sets the next date field in the order of date fields according to the specified date format.
     * @param next is the next DateField object after this date field.
     */
    public void setNext(final DateField next) {
      this.next = next;
    }

    /**
     * Returns the previous date field in the order of date fields according to the specified date format.
     * @return a DateField object representing the date field before this date field.
     */
    public DateField getPrevious() {
      return previous;
    }

    /**
     * Sets the previous date field in the order of date fields according to the specified date format.
     * @param previous is the previous DateField object before this date field.
     */
    public void setPrevious(final DateField previous) {
      this.previous = previous;
    }

    /**
     * Used by the SelectionModel to keep track of the selected portion of the date value in the input component
     * view.
     * @return the an integer position in the view specifying the end of the selected portion of the date value.
     */
    public int getSelectionEnd() {
      return selectionEnd;
    }

    /**
     * Used by the SelectionModel to keep track of the selected portion of the date value in the input component
     * view.
     * @return the an integer position in the view specifying the start of the selected portion of the date value.
     */
    public int getSelectionStart() {
      return selectionStart;
    }

    /**
     * Determines whether this DateField object is equal to the specified Object.
     * @param obj the Object used in testing equality with this DateField.
     * @return a boolean value if the specified Object is equal to this DateField, false otherwise.
     */
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof DateField)) {
        return false;
      }

      final DateField that = (DateField) obj;

      return (getCalendarConstant() == that.getCalendarConstant())
        && getDescription().equals(that.getDescription())
        && (getSelectionEnd() == that.getSelectionEnd())
        && (getSelectionStart() == that.getSelectionStart());
    }

    /**
     * Computes a hash value based on the state of this DateField object.
     * @return an integer hash value for the state of this DateField object.
     */
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + new Integer(getCalendarConstant()).hashCode();
      hashValue = 37 * hashValue + getDescription().hashCode();
      hashValue = 37 * hashValue + new Integer(getSelectionEnd()).hashCode();
      hashValue = 37 * hashValue + new Integer(getSelectionStart()).hashCode();
      return hashValue;
    }

    /**
     * Returns a String representation of this DateField object.
     * @return a String containing state information of this DateField object.
     */
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
   * The DateFieldKeyListener class is used by the JDateField component to track LEFT, RIGHT, UP, AND DOWN arrow keyboard
   * key events.
   */
  private final class DateFieldKeyListener extends KeyAdapter {

    /**
     * Processes the key pressed event.
     * @param event the KeyEvent object representing the key pressed event.
     */
    public void keyPressed(final KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_SPACE:
          event.consume(); // cannot remove the contents of the date
          TOOLKIT.beep();
          break;
        case KeyEvent.VK_ALT:
          break;
        case KeyEvent.VK_END:
          event.consume();
          getSelectionModel().selectYear();
          break;
        case KeyEvent.VK_HOME:
          event.consume();
          getSelectionModel().selectMonth();
          break;
        case KeyEvent.VK_LEFT:
          event.consume();
          getSelectionModel().selectPrevious();
          break;
        case KeyEvent.VK_RIGHT:
          event.consume();
          getSelectionModel().selectNext();
          break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
          getDataModel().roll(getSelectionModel().getSelectedDateField().getCalendarConstant(),
            (event.getKeyCode() == KeyEvent.VK_UP));
          break;
        default:
          event.consume();
          if (KeyEventUtil.isNumeric(event.getKeyCode())) {
            final int calendarField = getSelectionModel().getSelectedDateField().getCalendarConstant();
            final int value = Integer.parseInt(String.valueOf(event.getKeyChar()));
            getDataModel().set(calendarField, value);
          }
      }
    }

    /**
     * Processes the key typed event.  Consumes all key typed events!  This method is needed for property
     * functioning of the JDateField component.
     * @param event the KeyEvent object representing the key typed event.
     */
    public void keyTyped(final KeyEvent event) {
      event.consume();
    }
  }

  /**
   * The DateFieldMouseInputListener class is used by the JDateField component to
   * track mouse clicked/pressed events when the user sets focus to this date
   * field component.
   */
  private final class DateFieldMouseInputListener extends MouseInputAdapter {

    /**
     * Called to handle the MouseEvents defined by this DateFieldMouseInputListener class.
     * @param event the MouseEvent object capturing information about the mouse event.
     */
    private void handleMouseEvent(final MouseEvent event) {
      getSelectionModel().selectMonth();
      event.consume();
    }

    /**
     * Called when a mouse button has been clicked in the date field component.
     * @param me a MouseEvent object capturing the mouse clicked event in the date field component.
     */
    public void mouseClicked(final MouseEvent me) {
      handleMouseEvent(me);
    }

    /**
     * Called when the mouse has been dragged in the date field component.
     * @param me a MouseEvent object capturing the mouse drag event in the date field component.
     */
    public void mouseDragged(final MouseEvent me) {
      handleMouseEvent(me);
    }

    /**
     * Called when a mouse button has been pressed in the date field component.
     * @param me a MouseEvent object capturing the mouse pressed event in the date field component.
     */
    public void mousePressed(final MouseEvent me) {
      handleMouseEvent(me);
    }
  }

  /**
   * Interface to describe the operations required by the JDateField component to select the various date
   * fields in the component view.
   */
  public static interface SelectionModel {

    public DateField getSelectedDateField();

    public void selectDateField(DateField dateField);

    public void selectDay();

    public void selectMonth();

    public void selectNext();

    public void selectPrevious();

    public void selectYear();
  }

  /**
   * The default SelectionModel for instances of the JDateField component.  This SelectionModel highlights the
   * selected date field.
   */
  private final class DateFieldSelectionModel implements PropertyChangeListener, SelectionModel {

    private final Color SELECTION_COLOR = new Color(10, 36, 106);

    private final DateField DAY = new DateField("day", Calendar.DAY_OF_MONTH, 3, 5);
    private final DateField MONTH = new DateField("month", Calendar.MONTH, 0, 2);
    private final DateField YEAR = new DateField("year", Calendar.YEAR, 6, 10);

    // instance initializer block used to setup date field navigation
    {
      DAY.setNext(YEAR);
      DAY.setPrevious(MONTH);
      MONTH.setNext(DAY);
      MONTH.setPrevious(YEAR);
      YEAR.setNext(MONTH);
      YEAR.setPrevious(DAY);
    }

    private DateField selectedDateField = MONTH;

    /**
     * Creates a default instance of the DateFieldSelectionModel class with selected text color set to white
     * and selection color set to navy blue.
     */
    public DateFieldSelectionModel() {
      addPropertyChangeListener(this);
      setSelectedTextColor(Color.white);
      setSelectionColor(SELECTION_COLOR);
    }

    /**
     * Returns the selected date field as an instance of the DateField class.
     * @return a DateField object representing the selected date field.
     */
    public DateField getSelectedDateField() {
      return selectedDateField;
    }

    /**
     * Receives notification of date value changes in the JDateField component.
     * @param event a PropertyChangeEvent encasulating the date value changes in the JDateField component.
     */
    public void propertyChange(final PropertyChangeEvent event) {
      if (event.getPropertyName().equals("text")) {
        selectDateField(getSelectedDateField());
      }
    }

    /**
     * Sets the selected date field of the date value.
     * @param dateField a DateField object representing the date field of the date value to select.
     */
    public void selectDateField(final DateField dateField) {
      if (logger.isDebugEnabled()) {
        logger.debug("selecting DateField (" + dateField + ")");
      }
      selectedDateField = dateField;
      setSelectionStart(selectedDateField.getSelectionStart());
      setSelectionEnd(selectedDateField.getSelectionEnd());
    }

    /**
     * Selects the day date field.
     */
    public void selectDay() {
      selectDateField(DAY);
    }

    /**
     * Selects the month date field.
     */
    public void selectMonth() {
      selectDateField(MONTH);
    }

    /**
     * Selects the next date field in succession to the current date field selected.
     */
    public void selectNext() {
      selectDateField(getSelectedDateField().getNext());
    }

    /**
     * Selects the previous date field in precession to the current date field selected.
     */
    public void selectPrevious() {
      selectDateField(getSelectedDateField().getPrevious());
    }

    /**
     * Selects the year date field.
     */
    public void selectYear() {
      selectDateField(YEAR);
    }
  }

}
