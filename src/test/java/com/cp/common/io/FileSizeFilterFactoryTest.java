/*
 * FileSizeFilterFactoryTest.java (c) 12 June 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.11
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.FileSizeFilterFactory
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FileSizeFilterFactoryTest extends CommonIOTestCase {

  // The File instance variable has the size of the file in bytes appended to the variables name.
  private static final File ape176 = getFile("/etc/test/subdir1/ape.txt");
  private static final File baboon176 = getFile("/etc/test/subdir2/baboon.xml");
  private static final File chimp5694 = getFile("/etc/test/subdir2/chimp.htm");
  private static final File chimpanzee354 = getFile("/etc/test/subdir1/subdir12/chimpanzee.doc");
  private static final File fileContentFinder1215 = getFile("/etc/test/fileContentFinderTestFile.txt");
  private static final File gorilla1422 = getFile("/etc/test/subdir1/subdir11/gorilla.zip");
  private static final File monkey888 = getFile("/etc/test/subdir1/monkey.jar");
  private static final File orangutang710 = getFile("/etc/test/subdir1/subdir12/orangutang.bmp");

  private static final Set<File> fileSet = new HashSet<File>(11);

  static {
    fileSet.add(ape176);
    fileSet.add(baboon176);
    fileSet.add(chimp5694);
    fileSet.add(chimpanzee354);
    fileSet.add(fileContentFinder1215);
    fileSet.add(gorilla1422);
    fileSet.add(monkey888);
    fileSet.add(orangutang710);
  }

  @Before
  public void setup() throws Exception {

    super.setup();

    assertTrue("Could not find file (" + ape176 + ")!", ape176.exists());
    assertTrue("Could not find file (" + baboon176 + ")!", baboon176.exists());
    assertTrue("Could not find file (" + chimp5694 + ")!", chimp5694.exists());
    assertTrue("Could not find file (" + chimpanzee354 + ")!", chimpanzee354.exists());
    assertTrue("Could not find file (" + fileContentFinder1215 + ")!", fileContentFinder1215.exists());
    assertTrue("Could not find file (" + gorilla1422 + ")!", gorilla1422.exists());
    assertTrue("Could not find file (" + monkey888 + ")!", monkey888.exists());
    assertTrue("Could not find file (" + orangutang710 + ")!", orangutang710.exists());
    assertFalse(fileSet.isEmpty());
    assertEquals(8, fileSet.size());
  }

  private Set<File> getFilteredFileSet(final FileFilter filter, final Set<File> fileSet) {
    assertNotNull("The FileFilter object cannot be null!", filter);

    final Set<File> filteredFileSet = new HashSet<File>(fileSet.size());

    for (final File file : fileSet) {
      if (filter.accept(file)) {
        filteredFileSet.add(file);
      }
    }

    return filteredFileSet;
  }

  @Test
  public void testGetIsSizeFileFilter() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getIsSizeFileFilter(176), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(2, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(ape176));
    assertTrue(filteredFileSet.contains(baboon176));

    filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getIsSizeFileFilter(5694), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(1, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(chimp5694));

    filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getIsSizeFileFilter(2080), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetMaxSizeFileFilter() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getMaxSizeFileFilter(800), fileSet);

    assertFalse("The filteredFileSet should not be empty!", filteredFileSet.isEmpty());
    assertEquals("The filteredFileSet should contain 4 files!", 4, filteredFileSet.size());
    assertTrue("ape176 should be contained in the filteredFileSet!", filteredFileSet.contains(ape176));
    assertTrue("baboon176 should be contained in the filteredFileSet!", filteredFileSet.contains(baboon176));
    assertTrue("chimpanzee354 should be contained in the filteredFileSet!", filteredFileSet.contains(chimpanzee354));
    assertTrue("orangutang710 should be contained in the filteredFileSet!", filteredFileSet.contains(orangutang710));

    filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getMaxSizeFileFilter(99), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetMinSizeFileFilter() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getMinSizeFileFilter(800), fileSet);

    assertFalse("The filteredFileSet should not be empty!", filteredFileSet.isEmpty());
    assertEquals("The filteredFileSet should contain 4 files!", 4, filteredFileSet.size());
    assertTrue("chimp5694 should be contained in the filteredFileSet!", filteredFileSet.contains(chimp5694));
    assertTrue("fileContentFinder1215 should be contained in the filteredFileSet!", filteredFileSet.contains(fileContentFinder1215));
    assertTrue("gorilla1422 should be contained in the filteredFileSet!", filteredFileSet.contains(gorilla1422));
    assertTrue("monkey888 should be contained in the filteredFileSet!", filteredFileSet.contains(monkey888));

    filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getMinSizeFileFilter(9999), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }

  @Test
  public void testGetRangeSizeFileFilter() throws Exception {
    Set<File> filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getRangeSizeFileFilter(200, 1000), fileSet);

    assertFalse(filteredFileSet.isEmpty());
    assertEquals(3, filteredFileSet.size());
    assertTrue(filteredFileSet.contains(chimpanzee354));
    assertTrue(filteredFileSet.contains(orangutang710));
    assertTrue(filteredFileSet.contains(monkey888));

    filteredFileSet = getFilteredFileSet(FileSizeFilterFactory.getRangeSizeFileFilter(500, 700), fileSet);

    assertTrue(filteredFileSet.isEmpty());
    assertEquals(0, filteredFileSet.size());
  }
}
