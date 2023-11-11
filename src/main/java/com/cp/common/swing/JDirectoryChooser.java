/*
 * JDirectoryChooser.java (c) 1 June 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.3.27
 */

package com.cp.common.swing;

import com.cp.common.awt.ImageUtil;
import com.cp.common.io.DirectoryOnlyFileFilter;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

public final class JDirectoryChooser extends JDialog {

  private static final Logger logger = Logger.getLogger(JDirectoryChooser.class);

  private static final boolean MODAL = true;

  private static final Dimension DIALOG_SIZE = new Dimension(350, 300);

  private static final String DEFAULT_HOST_NAME = "localhost";
  private static final String DEFAULT_TITLE = "Choose Directory";

  private File selectedDirectory;

  private JTree fileSystemTree;

  private final Set expandedDirectories = new HashSet();

  /**
   * Creates an instance of the JDirectoryChooser class displaying the localhost file system allowing the user
   * to select a directory.
   * @param owner a Frame object owning this dialog.
   */
  public JDirectoryChooser(final Frame owner) {
    super(owner, DEFAULT_TITLE, MODAL);
    setSize(DIALOG_SIZE);
    buildUI();
  }

  /**
   * Returns the root of the file system for the localhost.
   * @return a TreeNode object referring to the root of the file system on the localhost.
   */
  private TreeNode getFileSystemRootNode() {
    final MutableTreeNode fileSystemRootNode = new DefaultMutableTreeNode(getHostName());
    final File[] roots = File.listRoots();

    Arrays.sort(roots);

    for (int index = 0, len = roots.length; index < len; index++) {
      fileSystemRootNode.insert(new DefaultMutableTreeNode(roots[index], true), index);
    }

    return fileSystemRootNode;
  }

  /**
   * Gets an instance of a JTree that is a view of the file system on the localhost.
   * @return a JTree instance constructed from the file system view of the localhost.
   */
  private synchronized JTree getFileSystemTree() {
    if (ObjectUtil.isNull(fileSystemTree)) {
      fileSystemTree = new JTree(new DefaultTreeModel(getFileSystemRootNode()));
      fileSystemTree.setCellRenderer(new FileSystemTreeCellRenderer());
      fileSystemTree.setRootVisible(false);

      fileSystemTree.addMouseListener(new MouseAdapter() {
        public void mouseClicked(final MouseEvent event) {
          final DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) fileSystemTree.getLastSelectedPathComponent();
          if (logger.isDebugEnabled()) {
            logger.debug("selectedTreeNode (" + selectedTreeNode + ")");
          }

          if (ObjectUtil.isNotNull(selectedTreeNode)) {
            setSelectedDiretory((File) selectedTreeNode.getUserObject());
            if (event.getClickCount() > 1 && !expandedDirectories.contains(getSelectedDirectory())) {
              expandDirectory(selectedTreeNode);
              fileSystemTree.expandPath(new TreePath(selectedTreeNode.getPath()));
              expandedDirectories.add(getSelectedDirectory());
            }
          }
          else {
            setSelectedDiretory(null);
          }
        }
      });

      fileSystemTree.addKeyListener(new KeyAdapter() {
        public void keyPressed(final KeyEvent event) {
          if (event.getKeyCode() == KeyEvent.VK_F5) {
            final DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) fileSystemTree.getLastSelectedPathComponent();
            if (logger.isDebugEnabled()) {
              logger.debug("selectedTreeNode (" + selectedTreeNode + ")");
            }

            if (ObjectUtil.isNotNull(selectedTreeNode)) {
              refreshDirectory(selectedTreeNode);
            }
          }
        }
      });
    }
    return fileSystemTree;
  }

  /**
   * Returns the network name of the localhost.
   * @return a String specifying the network name of the localhost.
   */
  private String getHostName() {
    try {
      String hostName = InetAddress.getLocalHost().getHostName();
      hostName = (ObjectUtil.isNull(hostName) ? DEFAULT_HOST_NAME : hostName);

      if (logger.isDebugEnabled()) {
        logger.debug("hostName (" + hostName + ")");
      }

      return hostName;
    }
    catch (Exception ignore) {
      return DEFAULT_HOST_NAME;
    }
  }

  /**
   * Returns the directory that the user selected as a File object.
   * @return a File object referring to the selected directory in the file system on the localhost
   * by the user.
   */
  public File getSelectedDirectory() {
    return selectedDirectory;
  }

  /**
   * Sets the directory selected by the user, denoted as a File object.
   * @param directory a File object referring to the selected directory by the user.
   */
  private void setSelectedDiretory(final File directory) {
    if (logger.isDebugEnabled()) {
      logger.debug("selectedDirectory (" + directory + ")");
    }

    if (ObjectUtil.isNotNull(directory) && !directory.isDirectory()) {
      logger.warn("(" + directory + ") is not a valid directory!");
      throw new IllegalArgumentException("(" + directory + ") is not a valid directory!");
    }

    selectedDirectory = directory;
  }

  /**
   * Constructs the JDirectoryChooser Dialog's tool bar used to select a file system directory and/or
   * close the dialog.
   * @return a JToolBar containing the control buttons of the JDirectoryChooser Dialog.
   */
  private JToolBar buildToolBar() {
    final JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);

    toolbar.setBorder(BorderFactory.createEmptyBorder());
    toolbar.setFloatable(false);
    toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));

    final Dimension BUTTON_SIZE = new Dimension(75, 25);

    final JButton select = (JButton) toolbar.add(new XButton("Select"));

    select.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent event) {
        final File selectedDirectory = (File) ((DefaultMutableTreeNode)
          fileSystemTree.getLastSelectedPathComponent()).getUserObject();
        setSelectedDiretory(selectedDirectory);
        dispose();
      }
    });

    select.setPreferredSize(BUTTON_SIZE);
    toolbar.addSeparator();

    final JButton cancel = (JButton) toolbar.add(new XButton("Cancel"));

    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent event) {
        setSelectedDiretory(null);
        dispose();
      }
    });

    cancel.setPreferredSize(BUTTON_SIZE);

    return toolbar;
  }

  /**
   * Contructs the user interface to the JDirectoryChooser Dialog.  Instantiates UI components and hanles layout of
   * the dialog.
   */
  private void buildUI() {
    getContentPane().add(new JScrollPane(getFileSystemTree(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
    getContentPane().add(buildToolBar(), BorderLayout.SOUTH);
  }

  /**
   * Expands the tree node of the fileSystemTree denoting the directory in the file system on the localhost.
   * @param treeNode is the DefaultMutableTreeNode of the fileSystemTree object being expanded to view the
   * contents of the directory in the file system on the localhost that it represents.
   */
  private void expandDirectory(final DefaultMutableTreeNode treeNode) {
    final File selectedDirectory = (File) treeNode.getUserObject();
    final File[] subDirectories = selectedDirectory.listFiles(new DirectoryOnlyFileFilter());

    if (ArrayUtil.isNotEmpty(subDirectories)) {
      Arrays.sort(subDirectories);
      for (int index = 0; index < subDirectories.length; index++) {
        ((DefaultTreeModel) getFileSystemTree().getModel()).insertNodeInto(
          new DefaultMutableTreeNode(subDirectories[index], true), treeNode, index);
      }
    }
  }

  /**
   * Removes all the parentNode's children and re-adds them to the parentNode to reflect a change to the directory,
   * represented by the node, in file system on the localhost.
   * @param parentNode is the DefaultMutableTreeNode denoting the directory in the file system on the locahost that
   * is being refreshed.
   */
  private void refreshDirectory(final DefaultMutableTreeNode parentNode) {
    for (int index = parentNode.getChildCount(); --index >= 0; ) {
      ((MutableTreeNode) parentNode.getChildAt(index)).removeFromParent();
    }
    ((DefaultTreeModel) getFileSystemTree().getModel()).reload(parentNode);
    expandedDirectories.remove(getSelectedDirectory());
  }

  /**
   * Overridden setVisible method to call the showDialog method.
   */
  public void setVisible() {
    showDialog();
  }

  /**
   * Shows the JDirectoryChooser Dialog with a view of the localhost filesystem.  If the user selects a directory
   * and says "OK", then this Dialog will be closed and the selectedDiretory property will be set to the File
   * object referring to the file system path of the user's choice.
   * @return a File object specifying the selected directory by the user.
   */
  public File showDialog() {
    setLocationRelativeTo(getOwner());
    super.setVisible(true);
    return getSelectedDirectory();
  }

  /**
   * The FileSystemTreeCellRenderer class is responsible for representing the
   * nodes of the fileSystemTree, setting appropriate icons for the nodes that
   * are smbolic to the type of drive/folder in the file system of the
   * localhost.
   */
  public final class FileSystemTreeCellRenderer extends DefaultTreeCellRenderer {

    private final Map resourceMap = new HashMap();

    private synchronized Icon getIcon(final String path) {
      Icon icon = (Icon) resourceMap.get(path);

      if (ObjectUtil.isNull(icon)) {
        final Image iconImage = ImageUtil.getImage(FileSystemTreeCellRenderer.class.getResource(path), JDirectoryChooser.this);
        icon = new ImageIcon(iconImage);
        resourceMap.put(path, icon);
      }

      return icon;
    }

    public Component getTreeCellRendererComponent(final JTree tree,
                                                  final Object value,
                                                  final boolean sel,
                                                  final boolean expanded,
                                                  final boolean leaf,
                                                  final int row,
                                                  final boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

      final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;

      if (treeNode.getLevel() == 0) {
        setText(treeNode.getUserObject().toString());
        setToolTipText(getText());
      }
      else {
        final File directory = (File) treeNode.getUserObject();

        if (tree.isExpanded(new TreePath(treeNode.getPath()))) {
          setIcon(getIcon("/etc/content/icons/openFolder.gif"));
        }
        else {
          setIcon(getIcon("/etc/content/icons/folder.gif"));
        }

        if (treeNode.getLevel() == 1) {
          setText(directory.getAbsolutePath());
        }
        else {
          setText(directory.getName());
        }

        setToolTipText(directory.getAbsolutePath());
      }

      return this;
    }
  }

}
