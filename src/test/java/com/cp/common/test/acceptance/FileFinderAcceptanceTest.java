/*
 * FileFinderAcceptanceTest.java (c) 14 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.11.14
 * @see com.cp.common.util.FileFinder
 */

package com.cp.common.test.acceptance;

import com.cp.common.log4j.LoggingConfigurer;
import com.cp.common.util.FileFinder;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class FileFinderAcceptanceTest {

  private static final Logger logger = Logger.getLogger(FileFinderAcceptanceTest.class);

  private static final String LINUX_FILENAME = "vmlinuz";
  private static final String WINDOWS_FILENAME = "NTOSKRNL.EXE";
  private static final String WINDOWS_NAME = "Windows";
  private static final String WINDOWS_ROOT = "C:";

  static {
    LoggingConfigurer.configure();
  }

  private static String getFilename() {
    if (System.getProperty("os.name").indexOf(WINDOWS_NAME) != -1) {
      return getWindowsFilename();
    }
    return getLinuxFilename();
  }

  private static String getLinuxFilename() {
    return LINUX_FILENAME;
  }

  private static String getWindowsFilename() {
    return WINDOWS_FILENAME;
  }

  private static File getDefaultSearchPath() {
    if (System.getProperty("os.name").indexOf(WINDOWS_NAME) != -1) {
      return getDefaultWindowsRoot();
    }
    return getLinuxRoot();
  }

  private static File getDefaultWindowsRoot() {
    final File[] roots = File.listRoots();
    for (Iterator it = Arrays.asList(roots).iterator(); it.hasNext(); ) {
      final File root = (File) it.next();
      if (root.getAbsolutePath().startsWith(WINDOWS_ROOT)) {
        return root;
      }
    }
    return roots[0];
  }

  private static File getLinuxRoot() {
    return File.listRoots()[0];
  }

  public static void main(final String[] args) {
    if (logger.isDebugEnabled()) {
      logger.debug("OS: " + System.getProperty("os.name"));
      for (Iterator it = Arrays.asList(File.listRoots()).iterator(); it.hasNext(); ) {
        logger.debug("Root: " + it.next());
      }
    }

    final File searchPath = getDefaultSearchPath();
    logger.info("Searching (" + searchPath + ") for file (" + getFilename() +").");

    // Create an instance of the FileFinder class and peform a search operation.
    //final FileFinder fileFinder = new FileFinder(false, false, true, true, System.out);
    final FileFinder fileFinder = new FileFinder(false, false, true, false, null);
    final File[] files = fileFinder.findFile(getFilename(), searchPath);

    for (Iterator it = Arrays.asList(files).iterator(); it.hasNext(); ) {
      logger.info("Found: " + it.next());
    }
  }

}
