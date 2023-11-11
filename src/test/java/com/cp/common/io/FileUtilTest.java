/*
 * FileUtilTest.java (c) 6 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.10
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.FileUtil
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class FileUtilTest extends CommonIOTestCase {

  private String getFileLocation(final String pathname) {
    String absolutePath = new File(pathname).getAbsolutePath();
    absolutePath += (absolutePath.endsWith(File.separator) ? "" : File.separator);
    return absolutePath;
  }

  @Test
  public void testGetFileExtension() throws Exception {
    assertEquals("java", FileUtil.getFileExtension(createFile("FileUtil.java")));
    assertEquals("java", FileUtil.getFileExtension(createFile("src/com/cp/common/io/FileUtil.java")));
    assertEquals("class", FileUtil.getFileExtension(createFile("build/com/cp/common/io/FileUtil.class")));
    assertEquals("BAK", FileUtil.getFileExtension(createFile("/local/home/user/tmp/file.BAK")));
    assertEquals("", FileUtil.getFileExtension(createFile("/remote/home/user/tmp/file")));
    assertEquals("", FileUtil.getFileExtension(""));
    assertEquals("", FileUtil.getFileExtension(" "));
    assertEquals("", FileUtil.getFileExtension("file.  "));
    assertEquals("tmp", FileUtil.getFileExtension(" .tmp   "));
  }

  @Test
  public void testGetFileExtensionWithNullFile() throws Exception {
    try {
      FileUtil.getFileExtension((File) null);
      fail("Calling getFileExtension with a null File object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The file cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileExtension with a null File object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetFileExtensionWithNullFilename() throws Exception {
    try {
      FileUtil.getFileExtension((String) null);
      fail("Calling getFileExtension with a null filename should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The name of the file cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileExtension with a null filename threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetFileLocation() throws Exception {
    assertEquals(File.separator, FileUtil.getFileLocation(File.separator));
    assertEquals(getFileLocation("/local/home/user/"), FileUtil.getFileLocation(createFile("/local/home/user/.bash_profile")));
    assertEquals(getFileLocation("/remote/home/user/tmp/"), FileUtil.getFileLocation(createFile("/remote/home/user/tmp/file.tmp")));
    assertEquals("src/com/mycompany/mypackage/", FileUtil.getFileLocation("src/com/mycompany/mypackage/MyClass.java"));
    assertEquals("/", FileUtil.getFileLocation("/index.html"));
    assertEquals("", FileUtil.getFileLocation("FileUtil.java"));
    assertEquals("", FileUtil.getFileLocation(" "));
    assertEquals("", FileUtil.getFileLocation("."));
    assertEquals("", FileUtil.getFileLocation(".."));
  }

  @Test
  public void testGetFileLocationWitNullFile() throws Exception {
    try {
      FileUtil.getFileLocation((File) null);
      fail("Calling getFileLocation with a null File object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The file cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileLocation with a null File object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetFileLocationWitNullPathname() throws Exception {
    try {
      FileUtil.getFileLocation((String) null);
      fail("Calling getFileLocation with a null pathname should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The pathname of the file cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getFileLocation with a null pathname threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  @Test
  public void testGetFilename() throws Exception {
    assertEquals("", FileUtil.getFilename(null));
    assertEquals(" ", FileUtil.getFilename(createFile(" ")));
    assertEquals("file", FileUtil.getFilename(createFile("file")));
    assertEquals(".tmp", FileUtil.getFilename(createFile(".tmp")));
    assertEquals("FileUtil.java", FileUtil.getFilename(createFile("FileUtil.java")));
    assertEquals("MyClass.java", FileUtil.getFilename(createFile("src/com/mycompany/mypackage/MyClass.java")));
    assertEquals("file.tmp", FileUtil.getFilename(createFile("/local/home/user/tmp/file.tmp")));
  }
}
