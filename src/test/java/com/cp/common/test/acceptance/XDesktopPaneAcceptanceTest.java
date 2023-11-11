/*
 * XDesktopPaneAcceptanceTest.java (c) 20 March 2005
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.swing.XDesktopPane
 */

package com.cp.common.test.acceptance;

import com.cp.common.awt.ImageUtil;
import com.cp.common.io.FileUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.log4j.LoggingConfigurer;
import com.cp.common.swing.CascadeAction;
import com.cp.common.swing.GraphicsUtil;
import com.cp.common.swing.SplitAction;
import com.cp.common.swing.TileAction;
import com.cp.common.swing.XDesktopPane;
import com.cp.common.swing.XInternalFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

public class XDesktopPaneAcceptanceTest extends JFrame {

  private static final boolean DEFAULT_CLOSABLE = true;
  private static final boolean DEFAULT_ICONIFIABLE = true;
  private static final boolean DEFAULT_MAXIMIZABLE = true;
  private static final boolean DEFAULT_RESIZABLE = true;

  private static final Dimension FRAME_SIZE = new Dimension(800, 600);
  private static final Dimension ICON_SISZE = new Dimension(15, 15);

  private static File currentDirectoryPath = new File(System.getProperty("user.home"));

  private static final String FRAME_TITLE = "XDesktopPane Test";

  private int frameCount = 0;

  static {
    LoggingConfigurer.configure();
  }

  private Icon transparentIcon;

  private final XDesktopPane desktop;

  public XDesktopPaneAcceptanceTest(final String title) {
    super(title);
    desktop = new XDesktopPane();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setAppIcon(new File("etc/content/icons/desktop.gif"));
    setJMenuBar(buildMenuBar());
    setContentPane(desktop);
    setSize(FRAME_SIZE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JMenuBar buildMenuBar() {
    final JMenuBar menuBar = new JMenuBar();
    final JMenu file = menuBar.add(new JMenu("File"));

    file.add(new AbstractAction("New", GraphicsUtil.getNewDocumentIcon(ICON_SISZE)) {
      public void actionPerformed(final ActionEvent event) {
        final XInternalFrame frame = new TestInternalFrame(null, null, "frame " + (++frameCount), DEFAULT_RESIZABLE,
          DEFAULT_CLOSABLE, DEFAULT_MAXIMIZABLE, DEFAULT_ICONIFIABLE);
        desktop.open(frame);
      }
    });

    file.addSeparator();

    file.add(new AbstractAction("Exit", getTransparentIcon(ICON_SISZE)) {
      public void actionPerformed(final ActionEvent event) {
        dispose();
        System.exit(0);
      }
    });

    final JMenu window = menuBar.add(new JMenu("Window"));

    window.add(new AbstractAction("Close", getImageIcon(new File("etc/content/icons/close.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.close();
      }
    });

    window.add(new AbstractAction("Close All", getImageIcon(new File("etc/content/icons/closeAll.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.closeAll();
      }
    });

    window.addSeparator();

    window.add(new AbstractAction("Deiconify All", getImageIcon(new File("etc/content/icons/deiconifyAll.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.deiconifyAll();
      }
    });

    window.add(new AbstractAction("Iconify All", getImageIcon(new File("etc/content/icons/iconifyAll.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.iconifyAll();
      }
    });

    window.add(new AbstractAction("Maximize All", getImageIcon(new File("etc/content/icons/maximizeAll.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.maximizeAll();
      }
    });

    window.add(new AbstractAction("Minimize All", getImageIcon(new File("etc/content/icons/minimizeAll.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.minimizeAll();
      }
    });

    window.add(new AbstractAction("Restore All", getTransparentIcon(ICON_SISZE)) {
      public void actionPerformed(final ActionEvent event) {
        desktop.restoreAll();
      }
    });

    window.addSeparator();

    final JMenuItem next = window.add(new AbstractAction("Next", getImageIcon(new File("etc/content/icons/next.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.next();
      }
    });
    next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_ALT, false));
    //next.setMnemonic(KeyEvent.VK_RIGHT);

    final JMenuItem previous = window.add(new AbstractAction("Previous", getImageIcon(new File("etc/content/icons/previous.gif"))) {
      public void actionPerformed(final ActionEvent event) {
        desktop.previous();
      }
    });
    previous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_ALT, false));
    //previous.setMnemonic(KeyEvent.VK_LEFT);

    window.addSeparator();

    window.add(new CascadeAction(desktop, "Cascade", getImageIcon(new File("etc/content/icons/cascade.gif"))));
    window.add(new SplitAction(desktop, SwingConstants.HORIZONTAL, "Split Horizontal", getImageIcon(new File("etc/content/icons/splitHorizontal.gif"))));
    window.add(new SplitAction(desktop, SwingConstants.VERTICAL, "Split Vertical", getImageIcon(new File("etc/content/icons/splitVertical.gif"))));
    window.add(new TileAction(desktop, "Tile", getImageIcon(new File("etc/content/icons/tile.gif"))));

    return menuBar;
  }

  private Icon getImageIcon(final File imageFile) {
    try {
      return new ImageIcon(imageFile.toURI().toURL());
    }
    catch (MalformedURLException e) {
      e.printStackTrace(System.err);
    }
    return null;
  }

  private Icon getTransparentIcon(final Dimension iconSize) {
    if (ObjectUtil.isNull(transparentIcon)) {
      synchronized (this) {
        if (ObjectUtil.isNull(transparentIcon)) {
          transparentIcon = GraphicsUtil.getTransparentIcon(iconSize);
        }
      }
    }
    return transparentIcon;
  }

  private void setAppIcon(final File imageFile) {
    try {
      setIconImage(ImageUtil.getImage(imageFile, this));
    }
    catch (FileNotFoundException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void main(final String[] args) {
    new XDesktopPaneAcceptanceTest(FRAME_TITLE);
  }

  private final class TestInternalFrame extends XInternalFrame {

    private final JTextArea textArea;

    public TestInternalFrame(final XInternalFrame next,
                             final XInternalFrame previous,
                             final String windowTitle,
                             final boolean resizable,
                             final boolean closable,
                             final boolean maximizable,
                             final boolean iconifiable) {
      super(next, previous, windowTitle, resizable, closable, maximizable, iconifiable);
      textArea = new JTextArea();
      textArea.setEditable(false);
      getContentPane().add(buildToolBar(), BorderLayout.NORTH);
      getContentPane().add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
    }

    public TestInternalFrame(final XInternalFrame next,
                             final XInternalFrame previous,
                             final String windowTitle) {
      this(next, previous, windowTitle, DEFAULT_RESIZABLE, DEFAULT_CLOSABLE, DEFAULT_MAXIMIZABLE, DEFAULT_ICONIFIABLE);
    }

    private JToolBar buildToolBar() {
      final JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);

      toolBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      toolBar.setFloatable(false);
      toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

      toolBar.add(new AbstractAction("Open", GraphicsUtil.getOpenFolderIcon(ICON_SISZE)) {
        public void actionPerformed(final ActionEvent event) {
          final JFileChooser fileChooser = new JFileChooser(currentDirectoryPath);

          fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
              return (f.isDirectory() || "java".equals(FileUtil.getFileExtension(f)));
            }

            public String getDescription() {
              return "Java Files";
            }
          });

          final int option = fileChooser.showOpenDialog(XDesktopPaneAcceptanceTest.this);
          if (option == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            currentDirectoryPath = fileChooser.getCurrentDirectory();

            try {
              final BufferedReader fileReader = new BufferedReader(new FileReader(selectedFile));
              final StringBuffer fileContents = new StringBuffer();
              String line = null;

              while ((line = fileReader.readLine()) != null) {
                fileContents.append(line);
                fileContents.append("\n");
              }

              fileReader.close();
              setTitle(selectedFile.getAbsolutePath());
              textArea.setText(fileContents.toString());
            }
            catch (FileNotFoundException e) {
              JOptionPane.showMessageDialog(XDesktopPaneAcceptanceTest.this, e.getMessage(), "File Not Found Error",
                JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException e) {
              JOptionPane.showMessageDialog(XDesktopPaneAcceptanceTest.this, e.getMessage(), "File Read Error",
                JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      });

      return toolBar;
    }
  }

}
