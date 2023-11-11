/*
 * FileExtensionFileFilterTest.java (c) 16 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import com.cp.common.util.CollectionUtil;

import org.junit.Test;

public class FileExtensionFileFilterTest extends CommonIOTestCase {

  @Test
  public void testInstantiation() throws Exception {
    FileExtensionFileFilter fileFilter = new FileExtensionFileFilter(false, true, "bmp", "gif", "jpg");

    assertNotNull(fileFilter);
    assertTrue(fileFilter.isExclusive());
    assertFalse(fileFilter.isInclusive());
    assertTrue(fileFilter.isShowHidden());
    assertTrue(fileFilter.getFileExtensionSet().containsAll(CollectionUtil.getSet("bmp", "gif", "jpg")));
  }

  @Test
  public void testInstantiationWithEmptyFileExtensions() throws Exception {
    try {
      new FileExtensionFileFilter(new String[0]);
      fail("Instantiating an instance of the FileExtensionFileFilter with an empty file extensions array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("At least one valid file extension must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the FileExtensionFileFilter with an empty file extensions array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testInstantiationWithNullFileExtensions() throws Exception {
    try {
      new FileExtensionFileFilter((String[]) null);
      fail("Instantiating an instance of the FileExtensionFileFilter with a null file extensions array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("At least one valid file extension must be specified!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the FileExtensionFileFilter with a null file extensions array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testAcceptExclusive() throws Exception {
    FileExtensionFileFilter fileFilter = new FileExtensionFileFilter(FileExtensionFileFilter.EXCLUSIVE, false, "CSS", "HTML", "JS", "XML");

    assertNotNull(fileFilter);
    assertTrue(fileFilter.isExclusive());
    assertFalse(fileFilter.isInclusive());
    assertFalse(fileFilter.isShowHidden());
    assertFalse(fileFilter.accept(createFile("defaultStyle.css")));
    assertFalse(fileFilter.accept(createFile("WindowsStyle.Css")));
    assertFalse(fileFilter.accept(createFile("MACOSX.CSS")));
    assertTrue(fileFilter.accept(createFile("home.htm")));
    assertTrue(fileFilter.accept(createFile("serverSide.inc")));
    assertTrue(fileFilter.accept(createFile("index.jsp")));
    assertFalse(fileFilter.accept(createFile("comingsoon.html")));
    assertTrue(fileFilter.accept(createFile("ObjectUtil.java")));
    assertTrue(fileFilter.accept(createFile("validation.javascript")));
    assertTrue(fileFilter.accept(createFile("StringUtils.pl")));
    assertTrue(fileFilter.accept(createFile("classDiagram.uml")));
    assertTrue(fileFilter.accept(createFile("spreadsheet.xls")));
    assertTrue(fileFilter.accept(createFile("what.xlm")));
    assertFalse(fileFilter.accept(createFile("struts-config.xml")));
    assertTrue(fileFilter.accept(createFile("beans.xm1")));
    assertTrue(fileFilter.accept(createFile("xml.help")));
  }

  @Test
  public void testAcceptInclusive() throws Exception {
    FileExtensionFileFilter fileFilter = new FileExtensionFileFilter("DOC", "PDF", "RTF", "TXT");

    assertNotNull(fileFilter);
    assertFalse(fileFilter.isExclusive());
    assertTrue(fileFilter.isInclusive());
    assertFalse(fileFilter.isShowHidden());
    assertTrue(fileFilter.accept(createFile("readme.txt")));
    assertTrue(fileFilter.accept(createFile("readMe.Txt")));
    assertTrue(fileFilter.accept(createFile("README.TXT")));
    assertFalse(fileFilter.accept(createFile("readme.text")));
    assertFalse(fileFilter.accept(createFile("RTF.bin")));
    assertTrue(fileFilter.accept(createFile("resume.PDF")));
    assertFalse(fileFilter.accept(createFile("speech.odt")));
    assertFalse(fileFilter.accept(createFile("speechDOC")));
    assertTrue(fileFilter.accept(createFile("wordperfect.doc")));
    assertFalse(fileFilter.accept(createFile("picture.jpg")));
    assertFalse(fileFilter.accept(createFile("music.mp3")));
  }

  @Test
  public void testAcceptInclusiveWithHiddenFiles() throws Exception {
    File tempFile = new File("tmp.file") {
      @Override
      public boolean isFile() {
        return true;
      }

      @Override
      public boolean isHidden() {
        return true;
      }
    };

    FileExtensionFileFilter fileFilter = new FileExtensionFileFilter(FileExtensionFileFilter.INCLUSIVE, false, "file");

    assertNotNull(fileFilter);
    assertFalse(fileFilter.isExclusive());
    assertTrue(fileFilter.isInclusive());
    assertFalse(fileFilter.isShowHidden());
    assertFalse(fileFilter.accept(tempFile));

    fileFilter = new FileExtensionFileFilter(FileExtensionFileFilter.INCLUSIVE, true, "file");

    assertNotNull(fileFilter);
    assertFalse(fileFilter.isExclusive());
    assertTrue(fileFilter.isInclusive());
    assertTrue(fileFilter.isShowHidden());
    assertTrue(fileFilter.accept(tempFile));
  }

  @Test
  public void testGetFileExtension() throws Exception {
    final FileExtensionFileFilter fileFilter = new FileExtensionFileFilter("test");

    assertEquals("java", fileFilter.getFileExtension("JAVA"));
    assertEquals("cpp", fileFilter.getFileExtension(" Cpp "));
    assertEquals("c", fileFilter.getFileExtension("c"));
  }

  @Test
  public void testGetFileExtensionWithEmptyFileExtension() throws Exception {
    try {
      new FileExtensionFileFilter("test").getFileExtension(" ");
      fail("Calling getFileExtension on FileExtensionFileFilter with an empty String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("( ) is not a valid file extension!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileExtension on FileExtensionFileFilter with an empty String threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetFileExtensionWithNullFileExtension() throws Exception {
    try {
      new FileExtensionFileFilter("test").getFileExtension(null);
      fail("Calling getFileExtension on FileExtensionFileFilter with a null String should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(null) is not a valid file extension!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileExtension on FileExtensionFileFilter with a null String threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }
}
