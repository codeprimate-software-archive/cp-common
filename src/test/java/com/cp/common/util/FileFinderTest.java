/*
 * FileFinderTest.java (c) 11 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.7.27
 * @see com.cp.common.test.CommonTestCase
 * @see com.cp.common.util.FileFinder
 */

package com.cp.common.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.cp.common.test.CommonTestCase;

import org.junit.Test;

public class FileFinderTest extends CommonTestCase {

  private Map<String, File> getSearchFilesAndPaths() {
    final Map<String, File> searchFiles = new HashMap<String, File>();
    searchFiles.put("log4j.properties", getFile("build/etc/config/log4j.properties"));
    searchFiles.put("fileContentFinderTestFile.txt", getFile("etc/test/fileContentFinderTestFile.txt"));
    searchFiles.put("map.jpg", getFile("build/etc/content/images/map.jpg"));
    searchFiles.put("FileFinder.java", getFile("src/com/cp/common/util/FileFinder.java"));
    searchFiles.put("FileContentFinder.java", getFile("src/com/cp/common/util/FileContentFinder.java"));
    searchFiles.put("ObjectUtil.java", getFile("src/com/cp/common/lang/ObjectUtil.java"));
    searchFiles.put("StringUtil.java", getFile("src/com/cp/common/lang/StringUtil.java"));
    searchFiles.put("cp-common-build.sh", getFile("bak/cp-common-build.sh"));
    return searchFiles;
  }

  @Test
  public void testFileFinder() throws Exception {
    final FileFinder fileFinder = new FileFinder(false, false, true, false, null);
    final Map<String, File> expectedSearchFilesAndPaths = getSearchFilesAndPaths();

    for (final String key : expectedSearchFilesAndPaths.keySet()) {
      assertEquals(expectedSearchFilesAndPaths.get(key), fileFinder.findFile(key, PROJECT_DIRECTORY)[0]);
    }
  }
}
