/*
 * JSplashWindow.java (c) 18 July 2002
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.10
 * @see com.cp.common.awt.ImageUtil
 * @see com.cp.common.awt.WindowUtil
 * @see com.cp.common.util.TimeUnit
 * @see java.awt.Robot
 * @see javax.swing.JWindow
 */

package com.cp.common.swing;

import com.cp.common.awt.ImageUtil;
import com.cp.common.awt.WindowUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.NumberUtil;
import com.cp.common.util.TimeUnit;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class JSplashWindow extends JWindow {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the JSplashWindow UI component class initialized with the specified JFrame owner
   * and splash image.
   * @param owner the application frame owning this Splash Window.
   * @param img the splash image to display in the Splash Window.
   */
  public JSplashWindow(final JFrame owner, final Image img) {
    this(owner, img, 0);
  }

  /**
   * Creates an instance of the JSplashWindow UI component class by calling the parent constructor to set ownership,
   * register a MouseListener on the Splash Window, set size and location and kick off a Thread to close the Splash
   * Window after a specified duration.
   * @param owner a JFrame component that is the parent of the splash window.
   * @param img Image object that will be displayed in the splash window.
   * @param numberOfSeconds is an integer value specifying the number of seconds to display the splash window.
   */
  public JSplashWindow(final JFrame owner, final Image img, final int numberOfSeconds) {
    super(owner);
    Assert.notNull(img, "The image displayed in the Splash Window cannot be null!");
    addMouseListener(new SplashWindowMouseListener());
    getContentPane().add(createImageComponent(createSplashImage(img)));
    pack();
    setLocationRelativeTo(null);

    if (NumberUtil.isGreaterThan(0, numberOfSeconds)) {
      // Note, paint problem occurred using SwingUtilities.invokeLater(new SplashWindowRunner(numberOfSeconds)); method.
      new Thread(new SplashWindowRunner(numberOfSeconds)).start();
    }
    else {
      owner.addWindowListener(new FrameOwnerWindowListener());
      setVisible(true);
    }
  }

  /**
   * Creates and JComponent object to display the image in the Splash Window.
   * @param img the image to display in the Splash Window.
   * @return a JComponent object capable of displaying the image in the Splash Window.
   */
  private JComponent createImageComponent(final Image img) {
    final JLabel imageComponent = new JLabel(new ImageIcon(img));
    imageComponent.setOpaque(false);
    return imageComponent;
  }

  /**
   * Composes a new Image to be used as the Splash image from a combination of the screen shot and user's
   * specified image.  The screen capture is used to give the appearance that the Splash Window is transparent.
   * @param img the user image used to overlay the screen capture to form the Splash image.
   * @return an Image object composed of the screen shot and user image constituting the Splash image.
   */
  private Image createSplashImage(final Image img) {
    Assert.notNull(img, "The image to be displayed by the Splash Window cannot be null!");

    try {
      final Dimension splashWindowSize = ImageUtil.getImageSize(img, this);
      final Image splashImage = new Robot().createScreenCapture(new Rectangle(
        WindowUtil.getDesktopLocation(splashWindowSize), splashWindowSize));

      final MediaTracker mediaTracker = new MediaTracker(this);
      mediaTracker.addImage(splashImage, 0);
      mediaTracker.waitForAll();

      final Graphics g = splashImage.getGraphics();
      g.drawImage(img, 0, 0, this);

      return splashImage;
    }
    catch (AWTException e) {
      logger.error("Failed to capture screen image!", e);
      throw new IllegalStateException("Failed to capture screen image!", e);
    }
    catch (InterruptedException e) {
      logger.error("Failed to wait while capturing the screen image!", e);
    }

    return null;
  }

  private final class FrameOwnerWindowListener extends WindowAdapter {

    public void windowOpened(final WindowEvent e) {
      setVisible(false);
      dispose();
    }
  }

  private final class SplashWindowMouseListener extends MouseAdapter {

    public void mousePressed(final MouseEvent e) {
      setVisible(false);
      dispose();
    }
  }

  private final class SplashWindowRunner implements Runnable {

    private final int numberOfSeconds;

    /**
     * Creates an instance of the SplashWindowRunner Runnable class.
     * @param numberOfSeconds the number of seconds to delay the Thread that runs this Runnable.
     */
    private SplashWindowRunner(final int numberOfSeconds) {
      Assert.greaterThan(numberOfSeconds, 0, "The number of seconds (" + numberOfSeconds
        + ") must be greater than zero!");
      this.numberOfSeconds = numberOfSeconds;
    }

    /**
     * The run method is called by a Swing Thread and is responsible for displaying the JSplashWindow UI component
     * on the user's desktop for the specified delayed number of seconds.
     */
    public void run() {
      setVisible(true);

      try {
        Thread.sleep(TimeUnit.SECOND.getInTermsOf(numberOfSeconds, TimeUnit.MILLISECOND));
      }
      catch (InterruptedException ignore) {
        logger.warn("The Thread (" + Thread.currentThread().getName() + ") was interrupted before the delay of ("
          + numberOfSeconds + " second(s)) expired!", ignore);
      }

      setVisible(false);
      dispose();
    }
  }

}
