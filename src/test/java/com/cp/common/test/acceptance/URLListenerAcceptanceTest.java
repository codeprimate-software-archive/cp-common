/*
 * URLListenerAcceptanceTest.java (c) 16 January 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.net.URLListener
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.WindowUtil;
import com.cp.common.net.Browser;
import com.cp.common.net.URLListener;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.FileFinder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class URLListenerAcceptanceTest extends JFrame implements Browser {

  private static final Dimension SIZE = new Dimension(300, 75);

  private static File webBrowserLocation = null;

  private static final String FRAME_TITLE = "URLListener Test";
  private static final String URL_LABEL = "http://www.codeprimate.com";

  private static URL url = null;
  static {
    try {
      url = new URL(URL_LABEL);
    }
    catch (MalformedURLException e) {
      System.err.println("The URL (" + URL_LABEL + ") is invalid!");
    }
  }

  private static final JLabel urlLabel =
      new JLabel("<html><u>" + url.toExternalForm() + "</u></html>", JLabel.CENTER);

  public URLListenerAcceptanceTest() {
    super(FRAME_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add the URL label.
    urlLabel.addMouseListener(new URLListener(urlLabel, this, url));
    urlLabel.setEnabled(false);
    urlLabel.setForeground(Color.gray);

    getContentPane().add(urlLabel, BorderLayout.CENTER);
    setSize(SIZE);
    setLocation(WindowUtil.getDesktopLocation(getSize()));
    setVisible(true);
  }

  public void goToURL(final URL address) {
    if (webBrowserLocation != null) {
      try {
        final Runtime runner = Runtime.getRuntime();

        final StringBuffer command = new StringBuffer();
        command.append(webBrowserLocation.toString());
        command.append(" ");
        command.append(address.toExternalForm());

        // Open the Web browser and display the contents of the URL (address).
        runner.exec(command.toString());
      }
      catch (IOException e) {
        System.err.println("Unable to open Web browser!");
      }
    }
    else {
      JOptionPane.showMessageDialog(this, "No browser could be located on the localhost!",
          "Browser Not Found Error", JOptionPane.WARNING_MESSAGE);
    }
  }

  public static void main(final String[] args) {
    // Kick off a Thread to look for a Web browser on the lcoalhost.
    new Thread(new Runnable() {
      public void run() {
        final File[] browsers = new FileFinder(false, true, true, false, null)
            .findFile(BrowserFileFilter.INSTANCE);
        if (ArrayUtil.isNotEmpty(browsers)) {
          System.out.println("Found Web browser @ location (" + browsers[0].toString() + ")");
          webBrowserLocation = browsers[0];
          urlLabel.setForeground(Color.blue);
          urlLabel.setEnabled(true);
        }
        else {
          System.err.println("WARNING: A Web browser could not be found on the localhost!");
        }
      }
    }).start();

    // Start the applicant to test the URLListener component.
    new URLListenerAcceptanceTest();
  }

  private static final class BrowserFileFilter implements FileFilter {

    public static final BrowserFileFilter INSTANCE = new BrowserFileFilter();

    private static final String[] browsers = {
      "iexplore.exe",
      "netscp.exe",
      "mozilla.exe"
    };

    private static final Collection BROWSER_LIST =
        Collections.unmodifiableCollection(new ArrayList(Arrays.asList(browsers)));

    private BrowserFileFilter() {
    }

    public boolean accept(final File pathname) {
      //System.out.println(pathname);
      if (pathname.isDirectory()) {
        return true;
      }
      return BROWSER_LIST.contains(getFilename(pathname));
    }

    private String getFilename(final File pathname) {
      return pathname.getName().toLowerCase();
    }
  }

}
