/*
 * BasicCalendarUI.java (c) 2 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.4.16
 */

package com.cp.common.swing.plaf.basic;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.swing.GraphicsUtil;
import com.cp.common.swing.JCalendar;
import com.cp.common.swing.plaf.CalendarUI;
import com.cp.common.util.AbstractDataModel;
import com.cp.common.util.DateUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import org.apache.log4j.Logger;

public class BasicCalendarUI extends CalendarUI {

  private static final Logger logger = Logger.getLogger(BasicCalendarUI.class);

  private static final int FOOTER_HEIGHT = 25;
  private static final int HEADER_HEIGHT = 25;
  private static final int HORIZONTAL_GAP = 5;
  private static final int WEEKDAY_HEIGHT = 20;

  private static final Color BLUEFISH_GRAY = new Color(53, 73, 101);
  private static final Color LIGHT_GRAY = new Color(204, 204, 204);
  private static final Color BUTTON_COLOR = Color.DARK_GRAY;
  private static final Color BUTTON_PRESSED_COLOR = Color.RED;
  private static final Color BUTTON_ROLLOVER_COLOR = Color.BLACK;
  private static final Color CALENDAR_COLOR = Color.DARK_GRAY;
  private static final Color CALENDAR_ROLLOVER_COLOR = Color.BLACK;
  private static final Color CALENDAR_SELECTED_COLOR = BLUEFISH_GRAY;
  private static final Color DISABLED_COLOR = LIGHT_GRAY;
  private static final Color FOOTER_BACKGROUND = LIGHT_GRAY;
  private static final Color FOOTER_FOREGROUND = Color.BLACK;
  private static final Color HEADER_FOREGROUND = BLUEFISH_GRAY;
  private static final Color WEEKDAY_BACKGROUND = BLUEFISH_GRAY;
  private static final Color WEEKDAY_FOREGROUND = Color.WHITE;

  private static final Dimension BUTTON_SIZE = new Dimension(10, 10);
  private static final Dimension PREFERRED_SIZE = new Dimension(200, 165);

  private static final Font HELVETICA_BOLD_14 = new Font("Helvetica", Font.BOLD, 14);
  private static final Font HELVETICA_BOLD_12 = new Font("Helvetica", Font.BOLD, 12);
  private static final Font HELVETICA_BOLD_11 = new Font("Helvetica", Font.BOLD, 11);
  private static final Font HELVETICA_PLAIN_10 = new Font("Helvetica", Font.PLAIN, 10);
  private static final Font CALENDAR_FONT = HELVETICA_PLAIN_10;
  private static final Font CALENDAR_ROLLOVER_FONT = HELVETICA_BOLD_11;
  private static final Font CALENDAR_SELECTED_FONT = HELVETICA_BOLD_12;
  private static final Font FOOTER_FONT = HELVETICA_BOLD_12;
  private static final Font HEADER_FONT = HELVETICA_BOLD_14;
  private static final Font WEEKDAY_FONT = HELVETICA_BOLD_12;

  private static final String[] WEEK_DAYS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

  private BasicCalendarListener calendarListener;

  private Button decrementMonth;
  private Button decrementYear;
  private Button incrementMonth;
  private Button incrementYear;

  private Day[][] calendarMatrix;

  /**
   * Creates an instance of the BasicCalendarUI class responsible for painting the user interface of the JCalendar
   * Swing UI component and defining actions for handling user generated events.
   * @param calendar the JCalendar Swing UI component who's user interface components will be defined by this
   * BasicCalendarUI class.
   */
  public BasicCalendarUI(final JCalendar calendar) {
    initButtons(calendar);
    initCalendar(calendar);
  }

  /**
   * Creates and initializes buttons used in the UI by the user to decrement and increment the month and year
   * of the calendar.
   * @param calendar the JCalendar Swing UI component who's calendar model is updated by this UI's button actions.
   */
  protected void initButtons(final JCalendar calendar) {
    decrementMonth = createButton(calendar, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        calendar.getModel().decrementMonth();
      }
    });
    decrementMonth.setIcon(new ImageIcon(GraphicsUtil.fillLeftArrow(BUTTON_SIZE, BUTTON_COLOR), "Filled Dark Gray Left Arrow"));
    decrementMonth.setPressedIcon(new ImageIcon(GraphicsUtil.fillLeftArrow(BUTTON_SIZE, BUTTON_PRESSED_COLOR), "Filled Red Left Arrow"));
    decrementMonth.setRolloverIcon(new ImageIcon(GraphicsUtil.fillLeftArrow(BUTTON_SIZE, BUTTON_ROLLOVER_COLOR), "Filled Black Left Arrow"));
    decrementMonth.setSize(BUTTON_SIZE);

    decrementYear = createButton(calendar, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        calendar.getModel().decrementYear();
      }
    });
    decrementYear.setIcon(new ImageIcon(GraphicsUtil.fillDoubleLeftArrow(BUTTON_SIZE, BUTTON_COLOR), "Two Filled Dark Gray Left Arrows"));
    decrementYear.setPressedIcon(new ImageIcon(GraphicsUtil.fillDoubleLeftArrow(BUTTON_SIZE, BUTTON_PRESSED_COLOR), "Two Filled Red Left Arrows"));
    decrementYear.setRolloverIcon(new ImageIcon(GraphicsUtil.fillDoubleLeftArrow(BUTTON_SIZE, BUTTON_ROLLOVER_COLOR), "Two Filled Black Left Arrows"));
    decrementYear.setSize(BUTTON_SIZE);

    incrementMonth = createButton(calendar, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        calendar.getModel().incrementMonth();
      }
    });
    incrementMonth.setIcon(new ImageIcon(GraphicsUtil.fillRightArrow(BUTTON_SIZE, BUTTON_COLOR), "Filled Dark Gray Right Arrow"));
    incrementMonth.setPressedIcon(new ImageIcon(GraphicsUtil.fillRightArrow(BUTTON_SIZE, BUTTON_PRESSED_COLOR), "Filled Red Right Arrow"));
    incrementMonth.setRolloverIcon(new ImageIcon(GraphicsUtil.fillRightArrow(BUTTON_SIZE, BUTTON_ROLLOVER_COLOR), "Filled Black Right Arrow"));
    incrementMonth.setSize(BUTTON_SIZE);

    incrementYear = createButton(calendar, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        calendar.getModel().incrementYear();
      }
    });
    incrementYear.setIcon(new ImageIcon(GraphicsUtil.fillDoubleRightArrow(BUTTON_SIZE, BUTTON_COLOR), "Two Filled Dark Gray Right Arrows"));
    incrementYear.setPressedIcon(new ImageIcon(GraphicsUtil.fillDoubleRightArrow(BUTTON_SIZE, BUTTON_PRESSED_COLOR), "Two Filled Red Right Arrows"));
    incrementYear.setRolloverIcon(new ImageIcon(GraphicsUtil.fillDoubleRightArrow(BUTTON_SIZE, BUTTON_ROLLOVER_COLOR), "Two Filled Black Right Arrows"));
    incrementYear.setSize(BUTTON_SIZE);
  }

  /**
   * Creates and initializes the calendar matrix containing the days of the current calendar month.
   * @param calendar the JCalendar Swing UI component who's calendar matrix of the current calendar month
   * is determined and setup.
   */
  protected void initCalendar(final JCalendar calendar) {
    calendarMatrix = new Day[6][7];
    for (int row = 6; --row >= 0; ) {
      for (int col = 7; --col >= 0; ) {
        calendarMatrix[row][col] = createDay(calendar);
      }
    }
    setupCalendarMonth(calendar);
  }

  /**
   * Creates an instance of the Button calendar component class to invoke actions that modify the calendar model
   * upon user input.
   * @param action the Action class defining the calendar model modification to perform.
   * @return an instance of the Button class initialized with the specified Action.
   */
  protected Button createButton(final JCalendar calendar, final Action action) {
    final AbstractButton button = new BasicButton(action);
    button.addPropertyChangeListener(getCalendarListener(calendar));
    getCalendarListener(calendar).addMouseInputListener(button);
    return button;
  }

  /**
   * Creates an instance of the Day class which is an abstraction for representing days in the current calendar
   * month.
   * @param calendar the JCalendar Swing UI component.
   * @return an instance of the Day class initialized with this CalendarUI's default look and feel properties.
   */
  protected Day createDay(final JCalendar calendar) {
    final AbstractDay day = new BasicDay();
    day.addPropertyChangeListener(getCalendarListener(calendar));
    day.setAction(new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        calendar.getModel().set(Calendar.DAY_OF_MONTH, day.getDayOfMonth());
      }
    });
    day.setColor(CALENDAR_COLOR);
    day.setFont(CALENDAR_FONT);
    day.setRolloverColor(CALENDAR_ROLLOVER_COLOR);
    day.setRolloverFont(CALENDAR_ROLLOVER_FONT);
    day.setSelected(false);
    day.setSelectedColor(CALENDAR_SELECTED_COLOR);
    day.setSelectedFont(CALENDAR_SELECTED_FONT);
    getCalendarListener(calendar).addMouseInputListener(day);
    return day;
  }

  /**
   * Used by the Swing framework for creating and instance of this UI with the specified Swing component.
   * @param c the JComponent, or JCalendar, which UI delegates to this class to determine the look and feel.
   * @return an instance of this ComponentUI class to handle the paint operations of the user interface.
   */
  public static ComponentUI createUI(final JComponent c) {
    return new BasicCalendarUI((JCalendar) c);
  }

  /**
   * Returns the Singleton instance of the BasicCalendarListener class which acts as a controller in the
   * UI Delegate reletionship with the UI and is responsible for receiving user input events to the JCalendar
   * Swing UI component and associating actions and behaviors with the events.
   * @param calendar the JCalendar Swing UI component for which the controller is attached.
   * @return an instance of the BasicCalendarListener class acting as the controller for processing user
   * input events to the calenar component.
   */
  protected BasicCalendarListener getCalendarListener(final JCalendar calendar) {
    if (ObjectUtil.isNull(calendarListener)) {
      synchronized (this) {
        if (ObjectUtil.isNull(calendarListener)) {
          calendarListener = new BasicCalendarListener(calendar);
        }
      }
    }
    return calendarListener;
  }

  /**
   * Computes the label position within the rectangular viewable area given the specified FontMetrics used by the
   * Graphics context to paint the label.
   * @param fontMetrics the FontMetrics of the current Font used by the Graphics context to paint the label.
   * @param label a String value specifying the label painted in the viewable area.
   * @param viewableArea the rectangular area in which the label will be painted.
   * @return a Point2D object specifying the labels offseted position within the viewable area to paint the label.
   */
  protected Point2D getLabelPosition(final FontMetrics fontMetrics,
                                     final String label,
                                     final RectangularShape viewableArea) {
    if (logger.isDebugEnabled()) {
      logger.debug("label (" + label + ")");
      logger.debug("viewableArea (" + viewableArea + ")");
    }

    final int maxAscent = fontMetrics.getMaxAscent();
    final int maxDescent = fontMetrics.getMaxDescent();
    final int width = fontMetrics.stringWidth(label);

    if (logger.isDebugEnabled()) {
      logger.debug("maxAscent (" + maxAscent + ")");
      logger.debug("maxDescent (" + maxDescent + ")");
      logger.debug("width (" + width + ")");
    }

    return new Point2D.Double((viewableArea.getX() + ((viewableArea.getWidth() - width) / 2.0)),
      (viewableArea.getY() + (viewableArea.getHeight() / 2.0) - ((maxDescent - maxAscent) / 2.0)));
  }

  /**
   * Returns the preferred size of the calendar component based on the UI subcomponents in the user interface.
   * @param c the JComponent, JCalendar, who's preferred size is determined.
   * @return a Dimension object specifying the preferred size of the specified JComponent.
   */
  public Dimension getPreferredSize(final JComponent c) {
    return PREFERRED_SIZE;
  }

  /**
   * Determines the JCalendar object's viewable area by computing the difference between the calendar's preferred
   * size and the border or insets the calendar.
   * @param calendar the JCalendar object who's viewable area is being determined.
   * @return a Dimension object specifying the size (width and height) of the viewable area of the calendar.
   */
  protected Dimension getViewableArea(final JCalendar calendar) {
    final Dimension preferredSize = getPreferredSize(calendar);
    final Insets borderInsets = calendar.getInsets(new Insets(0, 0, 0, 0));

    if (logger.isDebugEnabled()) {
      logger.debug("preferredSize (" + preferredSize + ")");
      logger.debug("borderInsets (" + borderInsets + ")");
    }

    final int width = (preferredSize.width - (borderInsets.left + borderInsets.right));
    final int height = (preferredSize.height - (borderInsets.top + borderInsets.bottom));

    if (logger.isDebugEnabled()) {
      logger.debug("viewable widht (" + width + ")");
      logger.debug("viewable height (" + height + ")");
    }

    return new Dimension(width, height);
  }

  /**
   * Returns the location, the (x, y) coordinates, of the viewable area with respect to the calendar component's
   * preferred size.
   * @param calendar the JCalendar object who's viewable area location is computed.
   * @return a Point2D object specifying the (x, y) location of the viewable area with respect to the JCalendar's
   * preferred size.
   */
  protected Point2D getViewableAreaLocation(final JCalendar calendar) {
    final Dimension preferredSize = getPreferredSize(calendar);
    final Dimension viewableArea = getViewableArea(calendar);

    if (logger.isDebugEnabled()) {
      logger.debug("preferredSize (" + preferredSize + ")");
      logger.debug("viewableArea (" + viewableArea + ")");
    }

    final double x = ((preferredSize.getWidth() - viewableArea.getWidth()) / 2.0);
    final double y = ((preferredSize.getHeight() - viewableArea.getHeight()) / 2.0);

    if (logger.isDebugEnabled()) {
      logger.debug("x (" + x + ")");
      logger.debug("y (" + y + ")");
    }

    return new Point2D.Double(x, y);
  }

  /**
   * Returns a String array containing the text descriptions for the days of the week (Monday, Tuesday, etc).
   * @return a String array containing text descriptions for the week days.
   */
  protected String[] getWeekdays() {
    return WEEK_DAYS;
  }

  /**
   * Installs hooks between this ComponentUI class and the associated JComponent, JCalendar.
   * @param c installs this UI on the specified JComponent, or JCalendar.
   */
  public void installUI(final JComponent c) {
    final JCalendar calendar = (JCalendar) c;
    c.addMouseListener(getCalendarListener(calendar));
    c.addMouseMotionListener(getCalendarListener(calendar));
  }

  /**
   * Paint draws the basic user interface for the JCalendar Swing UI component.
   * @param g the Graphics context used to paint the user interface of the calendar.
   * @param c the calendar component who's user interface is being painted.
   */
  public void paint(final Graphics g, final JComponent c) {
    final Graphics2D g2 = (Graphics2D) g;
    final JCalendar calendar = (JCalendar) c;
    final Point2D viewableAreaLocation = getViewableAreaLocation(calendar);

    paintBorder(g2, calendar);
    g2.translate(viewableAreaLocation.getX(), viewableAreaLocation.getY());
    paintHeader(g2, calendar);
    paintWeekdays(g2, calendar);
    paintCalendar(g2, calendar);
    paintFooter(g2, calendar);
    g2.translate(-viewableAreaLocation.getX(), -viewableAreaLocation.getY());
  }

  /**
   * Paints the border around the calendar.
   * @param g2 the Graphics context used to paint the border of the calendar.
   * @param calendar the JCalendar Swing UI component for which the border is being painted around.
   */
  protected void paintBorder(final Graphics2D g2, final JCalendar calendar) {
    final Border calendarBorder = calendar.getBorder();
    if (ObjectUtil.isNotNull(calendarBorder)) {
      if (logger.isDebugEnabled()) {
        logger.debug("calendar border (" + calendarBorder.getClass() + ")");
      }
      final Dimension preferredSize = getPreferredSize(calendar);
      calendarBorder.paintBorder(calendar, g2, 0, 0, preferredSize.width, preferredSize.height);
    }
    else {
      logger.debug("The calendar's border was null!");
    }
  }

  /**
   * Paints the current calendar month, or the days in the current calendar month.
   * @param g2 the Graphics context used to paint the current calendar month.
   * @param calendar the JCalendar Swing UI component.
   */
  protected void paintCalendar(final Graphics2D g2, final JCalendar calendar) {
    final Dimension viewableArea = getViewableArea(calendar);
    final RectangularShape calendarViewableArea = new Rectangle2D.Double(0, (HEADER_HEIGHT + WEEKDAY_HEIGHT),
      viewableArea.getWidth(), (viewableArea.getHeight() - HEADER_HEIGHT - WEEKDAY_HEIGHT - FOOTER_HEIGHT));
    final double cellWidth = (calendarViewableArea.getWidth() / 7.0);
    final double cellHeight = (calendarViewableArea.getHeight() / 6.0);
    final Dimension cellSize = new Dimension((int) cellWidth, (int) cellHeight);
    final int currentDay = calendar.getCalendar().get(Calendar.DAY_OF_MONTH);

    if (logger.isDebugEnabled()) {
      logger.debug("viewableArea (" + viewableArea + ")");
      logger.debug("calendarViewableArea (" + calendarViewableArea + ")");
      logger.debug("cellWidth (" + cellWidth + ")");
      logger.debug("cellHeight (" + cellHeight + ")");
      logger.debug("currentDay (" + currentDay + ")");
    }

    double xOffset = 0.0;
    double yOffset = calendarViewableArea.getY();

    if (calendar.getModel().getMonthYearChanged()) {
      setupCalendarMonth(calendar);
    }

    for (int rows = 0; rows < 6; rows++) {
      for (int cols = 0; cols < 7; cols++) {
        calendarMatrix[rows][cols].setLocation(new Point2D.Double(xOffset, yOffset));

        if (calendarMatrix[rows][cols].isEnabled()) {
          calendarMatrix[rows][cols].setSelected(calendarMatrix[rows][cols].getDayOfMonth() == currentDay);
        }

        calendarMatrix[rows][cols].setSize(cellSize);
        calendarMatrix[rows][cols].paint(g2, calendar);
        xOffset += cellWidth;
      }
      xOffset = 0;
      yOffset += cellHeight;
    }
  }

  /**
   * Paints the footer of the JCalendar Swing UI component.  The footer of the calendar displays the current day
   * and date.
   * @param g2 the Graphics context object used to paint the footer of the calendar.
   * @param calendar the JCalendar Swing UI component who's footer will be painted.
   */
  protected void paintFooter(final Graphics2D g2, final JCalendar calendar) {
    final String currentDate = DateUtil.DAY_OF_WEEK_DATE_FORMAT.format(Calendar.getInstance().getTime());
    final Dimension viewableArea = getViewableArea(calendar);
    final RectangularShape footerViewableArea = new Rectangle2D.Double(0, viewableArea.getHeight() - FOOTER_HEIGHT,
      viewableArea.getWidth(), FOOTER_HEIGHT);
    final Point2D labelPosition = getLabelPosition(g2.getFontMetrics(FOOTER_FONT), currentDate, footerViewableArea);

    if (logger.isDebugEnabled()) {
      logger.debug("currentDate (" + currentDate + ")");
      logger.debug("viewableArea (" + viewableArea + ")");
      logger.debug("footerViewableArea (" + footerViewableArea + ")");
      logger.debug("labelPosition (" + labelPosition + ")");
    }

    g2.setColor(FOOTER_BACKGROUND);
    g2.fill(footerViewableArea);
    g2.setColor(FOOTER_FOREGROUND);
    g2.setFont(FOOTER_FONT);
    g2.drawString(currentDate, (float) labelPosition.getX(), (float) labelPosition.getY());
  }

  /**
   * Paints the header of the of the JCalendar Swing UI component.  The header displays buttons to increment
   * and decrement the month and year as well as the current month and year.
   * @param g2 the Graphics context object used to paint the header of the calendar.
   * @param calendar the JCalendar Swing UI component who's header will be painted.
   */
  protected void paintHeader(final Graphics2D g2, final JCalendar calendar) {
    final String monthYear = DateUtil.MONTH_YEAR.format(calendar.getCalendar().getTime());
    final Dimension viewableArea = getViewableArea(calendar);
    final RectangularShape headerViewableArea = new Rectangle2D.Double(0.0, 0.0, viewableArea.getWidth(), HEADER_HEIGHT);
    final double buttonYOffset = ((headerViewableArea.getHeight() - BUTTON_SIZE.getHeight()) / 2.0);
    final Point2D monthYearLabelLocation = getLabelPosition(g2.getFontMetrics(HEADER_FONT), monthYear, headerViewableArea);

    if (logger.isDebugEnabled()) {
      logger.debug("monthYear (" + monthYear + ")");
      logger.debug("viewableArea (" + viewableArea + ")");
      logger.debug("headerViewableArea (" + headerViewableArea + ")");
      logger.debug("buttonYOffset (" + buttonYOffset + ")");
      logger.debug("monthYearLabelLocation (" + monthYearLabelLocation + ")");
    }

    decrementYear.setLocation(new Point2D.Double(HORIZONTAL_GAP, buttonYOffset));
    decrementMonth.setLocation(new Point2D.Double(2 * HORIZONTAL_GAP + BUTTON_SIZE.getWidth(), buttonYOffset));
    incrementMonth.setLocation(new Point2D.Double(headerViewableArea.getWidth() - (2 * HORIZONTAL_GAP) - (2 * BUTTON_SIZE.getWidth()), buttonYOffset));
    incrementYear.setLocation(new Point2D.Double(headerViewableArea.getWidth() - HORIZONTAL_GAP - BUTTON_SIZE.getWidth(), buttonYOffset));

    decrementYear.paint(g2, calendar);
    decrementMonth.paint(g2, calendar);

    g2.setColor(HEADER_FOREGROUND);
    g2.setFont(HEADER_FONT);
    g2.drawString(monthYear, (float) monthYearLabelLocation.getX(), (float) monthYearLabelLocation.getY());

    incrementMonth.paint(g2, calendar);
    incrementYear.paint(g2, calendar);
  }

  /**
   * Paints the days of the week on the calendar face.
   * @param g2 the Graphics context used to paint the days of the week.
   * @param calendar the JCalendar Swing UI component who's week days will be painted.
   */
  protected void paintWeekdays(final Graphics2D g2, final JCalendar calendar) {
    final Dimension viewableArea = getViewableArea(calendar);
    final RectangularShape weekdayViewableArea = new Rectangle2D.Double(0.0, HEADER_HEIGHT, viewableArea.getWidth(), WEEKDAY_HEIGHT);
    final double width = (viewableArea.getWidth() / 7.0);
    double xOffset = 0.0;

    g2.setPaint(WEEKDAY_BACKGROUND);
    g2.fill(weekdayViewableArea);
    g2.setFont(WEEKDAY_FONT);
    g2.setPaint(WEEKDAY_FOREGROUND);

    for (Iterator it = Arrays.asList(getWeekdays()).iterator(); it.hasNext(); xOffset += width) {
      final String weekDayLabel = it.next().toString();
      final RectangularShape weekdayCell = new Rectangle2D.Double(xOffset, HEADER_HEIGHT, width, WEEKDAY_HEIGHT);
      final Point2D location = getLabelPosition(g2.getFontMetrics(WEEKDAY_FONT), weekDayLabel, weekdayCell);
      g2.drawString(weekDayLabel, (float) location.getX(), (float) location.getY());
    }
  }

  /**
   * Sets up the days in the calendar month for the specified JCalendar Swing UI component.
   * @param calendar the JCalendar Swing UI component.
   */
  protected void setupCalendarMonth(final JCalendar calendar) {
    final Calendar currentCalendar = calendar.getCalendar();
    final int currentYear = currentCalendar.get(Calendar.YEAR);
    final int currentMonth = currentCalendar.get(Calendar.MONTH);
    final int currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
    final int currentWeekday = currentCalendar.get(Calendar.DAY_OF_WEEK);
    final int previousMonth = DateUtil.getPreviousMonth(currentMonth);
    final int numberOfDaysInCurrentMonth = calendar.getModel().getNumberOfDaysInMonth(currentMonth, currentYear);
    // NOTE: if the current month is January, the number of days in the previous month (December) of last year
    // would be the same as the number of day in December of the current year.  We need to specify currentYear
    // in the call to getNumberOfDaysInMonth if currentMonth is March and the current year is a leap year (since
    // February will have 29 days instead of 28).
    final int numberOfDaysInPreviousMonth = calendar.getModel().getNumberOfDaysInMonth(previousMonth, currentYear);
    final int firstWeekDayInMonth = calendar.getModel().getFirstDayOfMonth(currentDayOfMonth, currentWeekday);

    if (logger.isDebugEnabled()) {
      logger.debug("currentYear (" + currentYear + ")");
      logger.debug("currentMonth (" + currentMonth + ")");
      logger.debug("currentDayOfMonth (" + currentDayOfMonth + ")");
      logger.debug("currentWeekday (" + currentWeekday + ")");
      logger.debug ("previousMonth (" + previousMonth + ")");
      logger.debug("numberOfDaysInCurrentMonth (" + numberOfDaysInCurrentMonth + ")");
      logger.debug("numberOfDaysInPreviousMonth (" + numberOfDaysInPreviousMonth + ")");
    }

    boolean enabled = true;
    int currentDay = 1;
    int numberOfDaysInMonth = numberOfDaysInCurrentMonth;

    if (firstWeekDayInMonth != Calendar.SUNDAY) {
      enabled = false;
      currentDay = numberOfDaysInPreviousMonth - (firstWeekDayInMonth - 2);
      numberOfDaysInMonth = numberOfDaysInPreviousMonth;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("enabled (" + enabled + ")");
      logger.debug("currentDay (" + currentDay + ")");
      logger.debug("numberOfDaysInMonth (" + numberOfDaysInMonth + ")");
    }

    for (int rows = 0; rows < 6; rows++) {
      for (int cols = 0; cols < 7; cols++) {
        calendarMatrix[rows][cols].setDayOfMonth(currentDay);
        calendarMatrix[rows][cols].setEnabled(enabled);

        if (currentDay++ >= numberOfDaysInMonth) {
          enabled = !enabled;
          currentDay = 1;
          numberOfDaysInMonth = numberOfDaysInCurrentMonth;
        }
      }
    }
  }

  /**
   * Removes the association, hooks, between this UI and the calendar component.
   * @param c the JComponent, or JCalendar, that had this UI installed.
   */
  public void uninstallUI(final JComponent c) {
    final JCalendar calendar = (JCalendar) c;
    c.removeMouseListener(getCalendarListener(calendar));
    c.removeMouseMotionListener(getCalendarListener(calendar));
  }

  /**
   * The Actionable interface defines calendar components that are actionable, which perform some function, or action,
   * when the component receives user input, either from the keyboard or mouse.
   */
  protected interface Actionable {

    public Action getAction();

    public void setAction(Action action);

    public boolean isArmed();

    public void setArmed(boolean armed);

    public boolean isTriggered();

    public void setTriggered(boolean triggered);

    public void doAction(ActionEvent event);
  }

  /**
   * The CalendarComponent interface defines a specification for various subcomponents of the calendar's user interface.
   */
  protected interface CalendarComponent {

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public Point2D getLocation();

    public void setLocation(Point2D location);

    public Dimension getSize();

    public void setSize(Dimension size);

    public boolean contains(Point2D location);

    public void paint(Graphics2D g2, JCalendar calendar);
  }

  /**
   * The Button interface specifies a calendar UI component abstraction that allows the user to interact with the
   * calendar model by incrementing and decrementing the year and month.
   */
  protected interface Button extends CalendarComponent {

    public Icon getIcon();

    public void setIcon(Icon icon);

    public Icon getPressedIcon();

    public void setPressedIcon(Icon pressedIcon);

    public Icon getRolloverIcon();

    public void setRolloverIcon(Icon rolloverIcon);
  }

  /**
   * The Day interface specifies a calendar UI component displaying a single calendar day.
   */
  protected interface Day extends CalendarComponent {

    public Color getColor();

    public void setColor(Color color);

    public int getDayOfMonth();

    public void setDayOfMonth(int dayOfMonth);

    public Font getFont();

    public void setFont(Font font);

    public Color getRolloverColor();

    public void setRolloverColor(Color rolloverColor);

    public Font getRolloverFont();

    public void setRolloverFont(Font rolloverFont);

    public boolean isSelected();

    public void setSelected(boolean selected);

    public Color getSelectedColor();

    public void setSelectedColor(Color selectedColor);

    public Font getSelectedFont();

    public void setSelectedFont(Font selectedFont);
  }

  /**
   * An Abstract base class for defining and implement calendar UI subcomponents that appear in the calendar's
   * user interface.  This abstract class defines properties, behaviors and events common to all calendar UI
   * subcomponents.
   */
  protected abstract class AbstractCalendarComponent extends AbstractDataModel implements CalendarComponent {

    private boolean enabled = true;
    private Dimension size;
    private Point2D location;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(final boolean enabled) {
      if (logger.isDebugEnabled()) {
        logger.debug("enabled (" + enabled + ")");
      }
      this.enabled = enabled;
    }

    public Point2D getLocation() {
      return location;
    }

    public void setLocation(final Point2D location) {
      if (ObjectUtil.isNull(location)) {
        logger.warn("The location property cannot be null!");
        throw new NullPointerException("The location property cannot be null!");
      }
      if (logger.isDebugEnabled()) {
        logger.debug("location (" + location + ")");
      }
      this.location = location;
    }

    public Dimension getSize() {
      return size;
    }

    public void setSize(final Dimension size) {
      if (ObjectUtil.isNull(size)) {
        logger.warn("The size property cannot be null!");
        throw new NullPointerException("The size property cannot be null!");
      }
      if (logger.isDebugEnabled()) {
        logger.debug("size (" + size + ")");
      }
      this.size = size;
    }

    public Object getValue() {
      throw new UnsupportedOperationException("Operation Not Supported!");
    }

    public void setValue(final Object value) {
      throw new UnsupportedOperationException("Operation Not Supported!");
    }

    public boolean contains(final Point2D location) {
      if (logger.isDebugEnabled()) {
        logger.debug("location (" + location + ")");
      }
      return GraphicsUtil.getRectangle(getLocation(), getSize()).contains(location);
    }
  }

  /**
   * An abstract class constituting the basis for actionable calendar UI subcomponents.  This class defined properties,
   * behaviors and events that make a calendar UI subcomponent respond to a user, and perform some action.  This class
   * also forms the basis for receiving and processing user input.
   */
  protected abstract class ActionableCalendarComponent extends AbstractCalendarComponent implements Actionable, MouseInputListener {

    private boolean armed;
    private boolean triggered;

    private Action action;

    public Action getAction() {
      return action;
    }

    public final void setAction(final Action action) {
      this.action = action;
    }

    public boolean isArmed() {
      return armed;
    }

    public void setArmed(final boolean armed) {
      if (logger.isDebugEnabled()) {
        logger.debug("armed (" + armed + ")");
      }
      final Boolean oldArmed = Boolean.valueOf(this.armed);
      this.armed = armed;
      firePropertyChange(new PropertyChangeEvent(this, "armed", oldArmed, Boolean.valueOf(this.armed)));
    }

    public boolean isTriggered() {
      return triggered;
    }

    public void setTriggered(final boolean triggered) {
      if (logger.isDebugEnabled()) {
        logger.debug("triggered (" + triggered + ")");
      }
      final Boolean oldTriggered = Boolean.valueOf(this.triggered);
      this.triggered = triggered;
      firePropertyChange(new PropertyChangeEvent(this, "triggered", oldTriggered, Boolean.valueOf(this.triggered)));
    }

    public void doAction(final ActionEvent event) {
      if (isEnabled() && ObjectUtil.isNotNull(getAction())) {
        if (logger.isDebugEnabled()) {
          logger.debug("firing action for calendar component (" + event.getSource().getClass() + ")");
        }
        getAction().actionPerformed(event);
      }
    }

    protected abstract void handleMousePressed(final MouseEvent event);

    protected abstract void handleMouseReleased(final MouseEvent event);

    public void mouseClicked(final MouseEvent event) {
      logger.debug("mouseClicked (null implementation)");
    }

    public void mouseDragged(final MouseEvent event) {
      if (isEnabled()) {
        if (contains(event.getPoint())) {
          setArmed(true);
        }
        else {
          setArmed(false);
          setTriggered(false);
        }
      }
    }

    public void mouseEntered(final MouseEvent event) {
      if (isEnabled()) {
        setArmed(contains(event.getPoint()));
      }
    }

    public void mouseExited(final MouseEvent event) {
      if (isEnabled()) {
        setArmed(false);
        setTriggered(false);
      }
    }

    public void mouseMoved(final MouseEvent event) {
      if (isEnabled()) {
        setArmed(contains(event.getPoint()));
      }
    }

    public void mousePressed(final MouseEvent event) {
      if (isEnabled()) {
        if (contains(event.getPoint())) {
          setTriggered(true);
          handleMousePressed(event);
        }
      }
    }

    public void mouseReleased(final MouseEvent event) {
      if (isEnabled()) {
        if (contains(event.getPoint())) {
          handleMouseReleased(event);
        }
        setTriggered(false);
      }
    }
  }

  /**
   * The AbstractButton class defines calendar UI button component for triggering some programmatical action.
   */
  protected abstract class AbstractButton extends ActionableCalendarComponent implements Button {

    private Icon icon;
    private Icon pressedIcon;
    private Icon rolloverIcon;

    public AbstractButton(final Action action) {
      setAction(action);
    }

    public Icon getIcon() {
      return icon;
    }

    public final void setIcon(final Icon icon) {
      this.icon = icon;
    }

    public Icon getPressedIcon() {
      return pressedIcon;
    }

    public final void setPressedIcon(final Icon pressedIcon) {
      this.pressedIcon = pressedIcon;
    }

    public Icon getRolloverIcon() {
      return rolloverIcon;
    }

    public final void setRolloverIcon(final Icon rolloverIcon) {
      this.rolloverIcon = rolloverIcon;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{armed = ");
      buffer.append(isArmed());
      buffer.append(", enabled = ").append(isEnabled());
      buffer.append(", icon = ").append(getIcon());
      buffer.append(", location = ").append(getLocation());
      buffer.append(", pressedIcon = ").append(getPressedIcon());
      buffer.append(", rolloverIcon = ").append(getRolloverIcon());
      buffer.append(", size = ").append(getSize());
      buffer.append(", triggered = ").append(isTriggered());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * The AbstractDay class defines a calendar UI subcomponent representation of a calendar day.  Details of appearance
   * are left to subclasses.
   */
  private abstract class AbstractDay extends ActionableCalendarComponent implements Day {

    private boolean selected;

    private int dayOfMonth= 1;

    private Color color;
    private Color rolloverColor;
    private Color selectedColor;

    private Font font;
    private Font rolloverFont;
    private Font selectedFont;

    public Color getColor() {
      return color;
    }

    public void setColor(final Color color) {
      if (logger.isDebugEnabled()) {
        logger.debug("color (" + color + ")");
      }
      this.color = color;
    }

    public int getDayOfMonth() {
      return dayOfMonth;
    }

    public void setDayOfMonth(final int dayOfMonth) {
      if (logger.isDebugEnabled()) {
        logger.debug("dayOfMonth (" + dayOfMonth + ")");
      }
      this.dayOfMonth = dayOfMonth;
    }

    public Font getFont() {
      return font;
    }

    public void setFont(final Font font) {
      if (logger.isDebugEnabled()) {
        logger.debug("font (" + font + ")");
      }
      this.font = font;
    }

    public Color getRolloverColor() {
      return rolloverColor;
    }

    public void setRolloverColor(final Color rolloverColor) {
      if (logger.isDebugEnabled()) {
        logger.debug("rolloverColor (" + rolloverColor + ")");
      }
      this.rolloverColor = rolloverColor;
    }

    public Font getRolloverFont() {
      return rolloverFont;
    }

    public void setRolloverFont(final Font rolloverFont) {
      if (logger.isDebugEnabled()) {
        logger.debug("rolloverFont (" + rolloverFont + ")");
      }
      this.rolloverFont = rolloverFont;
    }

    public boolean isSelected() {
      return selected;
    }

    public void setSelected(final boolean selected) {
      if (logger.isDebugEnabled()) {
        logger.debug("selected (" + selected + ")");
      }
      this.selected = selected;
    }

    public Color getSelectedColor() {
      return selectedColor;
    }

    public void setSelectedColor(final Color selectedColor) {
      if (logger.isDebugEnabled()) {
        logger.debug("selectedColor (" + selectedColor + ")");
      }
      this.selectedColor = selectedColor;
    }

    public Font getSelectedFont() {
      return selectedFont;
    }

    public void setSelectedFont(final Font selectedFont) {
      if (logger.isDebugEnabled()) {
        logger.debug("selectedFont (" + selectedFont + ")");
      }
      this.selectedFont = selectedFont;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{armed = ");
      buffer.append(isArmed());
      buffer.append(", color = ").append(getColor());
      buffer.append(", dayOfMonth = ").append(getDayOfMonth());
      buffer.append(", enabled = ").append(isEnabled());
      buffer.append(", font = ").append(getFont());
      buffer.append(", location = ").append(getLocation());
      buffer.append(", rolloverColor = ").append(getRolloverColor());
      buffer.append(", rolloverFont = ").append(getRolloverFont());
      buffer.append(", selected = ").append(isSelected());
      buffer.append(", selectedColor = ").append(getSelectedColor());
      buffer.append(", selectedFont = ").append(getSelectedFont());
      buffer.append(", size = ").append(getSize());
      buffer.append(", triggered = ").append(isTriggered());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * The BasicButton class specifies the look and feel of a calendar button for the basic look and feel.
   */
  private class BasicButton extends AbstractButton {

    public BasicButton(final Action action) {
      super(action);
    }

    public BasicButton(final Action action, final Icon icon) {
      super(action);
      setIcon(icon);
    }

    public BasicButton(final Action action, final Icon icon, final Icon pressedIcon, final Icon rolloverIcon) {
      super(action);
      setIcon(icon);
      setPressedIcon(pressedIcon);
      setRolloverIcon(rolloverIcon);
    }

    private Color getBorderColor() {
      return (isTriggered() || isArmed() ? Color.black : Color.lightGray);
    }

    private Icon getDisplayIcon() {
      return (isTriggered() ? getPressedIcon() : (isArmed() ? getRolloverIcon() : getIcon()));
    }

    protected void handleMousePressed(final MouseEvent event) {
    }

    protected void handleMouseReleased(final MouseEvent event) {
      doAction(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, null));
    }

    public void paint(final Graphics2D g2, final JCalendar calendar) {
      final Point2D location = getLocation();
      getDisplayIcon().paintIcon(calendar, g2, (int) location.getX(), (int) location.getY()); // paint icon
      g2.setColor(getBorderColor());
      //g2.draw(GraphicsUtil.getRectangle(getLocation(), getSize())); // paint border
    }
  }

  /**
   * The BasicDay class specifies the look and feel of the calendar day for the basic look and feel.
   */
  private class BasicDay extends AbstractDay {

    private String getLabel() {
      return (getDayOfMonth() < 10 ? "0" + getDayOfMonth() : String.valueOf(getDayOfMonth()));
    }

    private Color getDisplayColor() {
      return (isEnabled() ? (isSelected() ? getSelectedColor() : (isArmed() ? getRolloverColor() : getColor())) : DISABLED_COLOR);
    }

    private Font getDisplayFont() {
      return (isSelected() ? getSelectedFont() : (isArmed() ? getRolloverFont() : getFont()));
    }

    protected void handleMousePressed(final MouseEvent event) {
      doAction(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, null));
    }

    protected void handleMouseReleased(final MouseEvent event) {
    }

    public void paint(final Graphics2D g2, final JCalendar calendar) {
      final String label = getLabel();
      final Point2D labelLocation = getLabelPosition(g2.getFontMetrics(getDisplayFont()), label,
        GraphicsUtil.getRectangle(getLocation(), getSize()));

      g2.setColor(getDisplayColor());
      g2.setFont(getDisplayFont());
      g2.drawString(label, (float) labelLocation.getX(), (float) labelLocation.getY());
    }
  }

}
