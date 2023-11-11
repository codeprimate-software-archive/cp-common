/*
 * FileComparatorFactoryTest.java (c) 12 June 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.cp.common.io.FileComparatorFactory
 * @see com.cp.common.test.CommonTestCase
 */

package com.cp.common.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Comparator;

import com.cp.common.test.CommonTestCase;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.Order;

import org.junit.Test;

public class FileComparatorFactoryTest extends CommonTestCase {

  private static final File ape = getFile("/etc/test/subdir1/ape.txt");
  private static final File baboon = getFile("/etc/test/subdir2/baboon.xml");
  private static final File chimp = getFile("/etc/test/subdir2/chimp.htm");
  private static final File chimpanzee = getFile("/etc/test/subdir1/subdir12/chimpanzee.doc");
  private static final File fileContentFinder = getFile("/etc/test/fileContentFinderTestFile.txt");
  private static final File gorilla = getFile("/etc/test/subdir1/subdir11/gorilla.zip");
  private static final File monkey = getFile("/etc/test/subdir1/monkey.jar");
  private static final File orangutang = getFile("/etc/test/subdir1/subdir12/orangutang.bmp");

  @Override
  public void setup() throws Exception {

    super.setup();

    assertTrue("Could not find file (" + ape + ")!", ape.exists());
    assertTrue("Could not find file (" + baboon + ")!", baboon.exists());
    assertTrue("Could not find file (" + chimp + ")!", chimp.exists());
    assertTrue("Could not find file (" + chimpanzee + ")!", chimpanzee.exists());
    assertTrue("Could not find file (" + fileContentFinder + ")!", fileContentFinder.exists());
    assertTrue("Could not find file (" + gorilla + ")!", gorilla.exists());
    assertTrue("Could not find file (" + monkey + ")!", monkey.exists());
    assertTrue("Could not find file (" + orangutang + ")!", orangutang.exists());
  }

  /**
   * The assumed file types for the primate test files are as follows:
   * ape.txt (Text file)
   * monkey.tst (Test file)
   * gorilla.zip (Compressed File)
   * orangutang.bmp (Bitmap Image File)
   * chimpanzee.doc (MS Word Document)
   * chimp.htm (HTML Web Page)
   * baboon.xml (XML Document)
   * @throws Exception if the test fails!
   */
  @Test
  public void testGetFileExtensionComparatorAscendingOrder() throws Exception {
    final Comparator<File> fileExtensionComparator = FileComparatorFactory.getFileExtensionComparator(Order.ASCENDING);

    assertNotNull(fileExtensionComparator);
    TestUtil.assertZero("chimp.htm should be equal to chimp.htm!", fileExtensionComparator.compare(chimp, chimp));
    TestUtil.assertNegative("monkey.jar should come before ape.txt!", fileExtensionComparator.compare(monkey, ape));
    TestUtil.assertPositive("ape.txt should come after monkey.jar!", fileExtensionComparator.compare(ape, monkey));
    TestUtil.assertPositive("baboon.xml should come after monkey.jar!", fileExtensionComparator.compare(baboon, monkey));
    TestUtil.assertPositive("gorilla.zip should come after baboon.xml!", fileExtensionComparator.compare(gorilla, baboon));
    TestUtil.assertNegative("monkey.jar should come before gorilla.zip!", fileExtensionComparator.compare(monkey, gorilla));
    TestUtil.assertNegative("chimpanzee.doc should come before chimp.htm!", fileExtensionComparator.compare(chimpanzee, chimp));
    TestUtil.assertNegative("orangutang.bmp should come before chimpanzee.doc!", fileExtensionComparator.compare(orangutang, chimpanzee));
    TestUtil.assertNegative("orangutang.bmp should come before chimp.htm!", fileExtensionComparator.compare(orangutang, chimp));
    TestUtil.assertPositive("chimp.htm should come after orangutang.bmp!", fileExtensionComparator.compare(chimp, orangutang));
  }

  /**
   * The assumed file types for the primate test files are as follows:
   * ape.txt (Text file)
   * monkey.tst (Test file)
   * gorilla.zip (Compressed File)
   * orangutang.bmp (Bitmap Image File)
   * chimpanzee.doc (MS Word Document)
   * chimp.htm (HTML Web Page)
   * baboon.xml (XML Document)
   * @throws Exception if the test fails!
   */
  @Test
  public void testGetFileExtensionComparatorDescendingOrder() throws Exception {
    final Comparator<File> fileExtensionComparator = FileComparatorFactory.getFileExtensionComparator(Order.DESCENDING);

    assertNotNull(fileExtensionComparator);
    TestUtil.assertZero("chimp.htm should be equal to chimp.htm!", fileExtensionComparator.compare(chimp, chimp));
    TestUtil.assertPositive("monkey.jar should come after ape.txt!", fileExtensionComparator.compare(monkey, ape));
    TestUtil.assertNegative("ape.txt should come before monkey.jar!", fileExtensionComparator.compare(ape, monkey));
    TestUtil.assertNegative("baboon.xml should come before monkey.jar!", fileExtensionComparator.compare(baboon, monkey));
    TestUtil.assertNegative("gorilla.zip should come before baboon.xml!", fileExtensionComparator.compare(gorilla, baboon));
    TestUtil.assertPositive("monkey.jar should come after gorilla.zip!", fileExtensionComparator.compare(monkey, gorilla));
    TestUtil.assertPositive("chimpanzee.doc should come after chimp.htm!", fileExtensionComparator.compare(chimpanzee, chimp));
    TestUtil.assertPositive("orangutang.bmp should come after chimpanzee.doc!", fileExtensionComparator.compare(orangutang, chimpanzee));
    TestUtil.assertPositive("orangutang.bmp should come after chimp.htm!", fileExtensionComparator.compare(orangutang, chimp));
    TestUtil.assertNegative("chimp.htm should come before orangutang.bmp!", fileExtensionComparator.compare(chimp, orangutang));
  }

  @Test
  public void testGetFileNameComparatorAscendingOrder() throws Exception {
    final Comparator<File> fileNameComparator= FileComparatorFactory.getFileNameComparator(Order.ASCENDING);

    assertNotNull(fileNameComparator);
    TestUtil.assertZero("ape should be equal to ape!", fileNameComparator.compare(ape, ape));
    TestUtil.assertNegative("ape should come before monkey!", fileNameComparator.compare(ape, monkey));
    TestUtil.assertPositive("gorilla should come after ape!", fileNameComparator.compare(gorilla, ape));
    TestUtil.assertPositive("monkey should come after gorilla!", fileNameComparator.compare(monkey, gorilla));
    TestUtil.assertPositive("monkey should come after ape!", fileNameComparator.compare(monkey, ape));
    TestUtil.assertNegative("chimpanzee should come before monkey!", fileNameComparator.compare(chimpanzee, monkey));
    TestUtil.assertNegative("chimpanzee should come before orangutang!", fileNameComparator.compare(chimpanzee, orangutang));
    TestUtil.assertPositive("chimpanzee should come after chimp!", fileNameComparator.compare(chimpanzee, chimp));
  }

  @Test
  public void testGetFileNameComparatorDescendingOrder() throws Exception {
    final Comparator<File> fileNameComparator = FileComparatorFactory.getFileNameComparator(Order.DESCENDING);

    assertNotNull(fileNameComparator);
    TestUtil.assertZero("ape should be equal to ape!", fileNameComparator.compare(ape, ape));
    TestUtil.assertPositive("ape should come after monkey!", fileNameComparator.compare(ape, monkey));
    TestUtil.assertNegative("gorilla should come before ape!", fileNameComparator.compare(gorilla, ape));
    TestUtil.assertNegative("monkey should come before gorialla!", fileNameComparator.compare(monkey, gorilla));
    TestUtil.assertNegative("monkey should come before ape!", fileNameComparator.compare(monkey, ape));
    TestUtil.assertPositive("chimpanzee should come after monkey!", fileNameComparator.compare(chimpanzee, monkey));
    TestUtil.assertPositive("chimpanzee should come after orangutang!", fileNameComparator.compare(chimpanzee, orangutang));
    TestUtil.assertNegative("chimpanzee should come before chimp!", fileNameComparator.compare(chimpanzee, chimp));
  }

  @Test
  public void testGetFilePathComparatorAscendingOrder() throws Exception {
    final Comparator<File> filePathComparator = FileComparatorFactory.getFilePathComparator(Order.ASCENDING);

    assertNotNull(filePathComparator);
    TestUtil.assertZero("ape should be equal to ape!", filePathComparator.compare(ape, ape));
    TestUtil.assertNegative("ape should come before baboon!", filePathComparator.compare(ape, baboon));
    TestUtil.assertPositive("baboon should come after chimpanzee!", filePathComparator.compare(baboon, chimpanzee));
    TestUtil.assertPositive("chimpanzee should come after gorilla!", filePathComparator.compare(chimpanzee, gorilla));
    TestUtil.assertNegative("gorilla should come before chimp!", filePathComparator.compare(gorilla, chimp));
    TestUtil.assertNegative("gorilla should come before orangutang!", filePathComparator.compare(gorilla, orangutang));
    TestUtil.assertNegative("chimpanzee should come before baboon!", filePathComparator.compare(chimpanzee, baboon));
    TestUtil.assertZero("ape should be equal to monkey!", filePathComparator.compare(ape, monkey));
    TestUtil.assertZero("baboon should be equal to chimp!", filePathComparator.compare(baboon, chimp));
  }

  @Test
  public void testGetFilePathComparatorDescendingOrder() throws Exception {
    final Comparator<File> filePathComparator = FileComparatorFactory.getFilePathComparator(Order.DESCENDING);

    assertNotNull(filePathComparator);
    TestUtil.assertZero("ape should be equal to ape!", filePathComparator.compare(ape, ape));
    TestUtil.assertPositive("ape should come after baboon!", filePathComparator.compare(ape, baboon));
    TestUtil.assertNegative("baboon should come before chimpanzee!", filePathComparator.compare(baboon, chimpanzee));
    TestUtil.assertNegative("chimpanzee should come before gorilla!", filePathComparator.compare(chimpanzee, gorilla));
    TestUtil.assertPositive("gorilla should come after chimp!", filePathComparator.compare(gorilla, chimp));
    TestUtil.assertPositive("gorilla should come after orangutang!", filePathComparator.compare(gorilla, orangutang));
    TestUtil.assertPositive("chimpanzee should come after baboon!", filePathComparator.compare(chimpanzee, baboon));
    TestUtil.assertZero("ape should be equal to monkey!", filePathComparator.compare(ape, monkey));
    TestUtil.assertZero("baboon should be equal to chimp!", filePathComparator.compare(baboon, chimp));
  }

  /**
   * The assumed file sizes for the primate test files are as follows:
   * ape.txt - 176 bytes
   * monkey.tst - 888 bytes
   * gorilla.zip - 1.38 KB
   * orangutang.bmp - 710 bytes
   * chimpanzee.doc - 354 bytes
   * chimp.htm - 5.56 KB
   * baboon.xml - 176 bytes
   * @throws Exception if the test fails!
   */
  @Test
  public void testGetFileSizeComparatorAscendingOrder() throws Exception {
    final Comparator<File> fileSizeComparator = FileComparatorFactory.getFileSizeComparator(Order.ASCENDING);

    assertNotNull(fileSizeComparator);
    TestUtil.assertZero("gorilla should be equal in size to gorilla!", fileSizeComparator.compare(gorilla, gorilla));
    TestUtil.assertPositive("monkey is larger than orangutang and should come after orangutang!", fileSizeComparator.compare(monkey, orangutang));
    TestUtil.assertPositive("orangutang is larger than ape and should come after ape!", fileSizeComparator.compare(orangutang, ape));
    TestUtil.assertPositive("monkey is larger than ape and should come after ape!", fileSizeComparator.compare(monkey, ape));
    TestUtil.assertNegative("monkey is smaller than chimp and should come before chimp!", fileSizeComparator.compare(monkey, chimp));
    TestUtil.assertNegative("chimpanzee is smaller than chimp and should come before chimp!", fileSizeComparator.compare(chimpanzee, chimp));
    TestUtil.assertPositive("chimpanzee is larger than baboon and should come after baboon!", fileSizeComparator.compare(chimpanzee, baboon));
    TestUtil.assertZero("ape should be equal in size to baboon!", fileSizeComparator.compare(ape, baboon));
  }

  @Test
  public void testGetFileSizeComparatorDescendingOrder() throws Exception {
    final Comparator<File> fileSizeComparator = FileComparatorFactory.getFileSizeComparator(Order.DESCENDING);

    assertNotNull(fileSizeComparator);
    TestUtil.assertZero("gorilla should be equal in size to gorilla!", fileSizeComparator.compare(gorilla, gorilla));
    TestUtil.assertNegative("monkey is larger than orangutang and should come before orangutang!", fileSizeComparator.compare(monkey, orangutang));
    TestUtil.assertNegative("orangutang is larger than ape and should come before ape!", fileSizeComparator.compare(orangutang, ape));
    TestUtil.assertNegative("monkey is larger than ape and should come before ape!", fileSizeComparator.compare(monkey, ape));
    TestUtil.assertPositive("monkey is smaller than chimp and should come after chimp!", fileSizeComparator.compare(monkey, chimp));
    TestUtil.assertPositive("chimpanzee is smaller than chimp and should come after chimp!", fileSizeComparator.compare(chimpanzee, chimp));
    TestUtil.assertNegative("chimpanzee is larger than baboon and should come before baboon!", fileSizeComparator.compare(chimpanzee, baboon));
    TestUtil.assertZero("ape should be equal in size to baboon!", fileSizeComparator.compare(ape, baboon));
  }

  @Test
  public void testGetLastModifiedComparatorAscendingOrder() throws Exception {
    final Comparator<File> lastModifiedComparator = FileComparatorFactory.getLastModifiedComparator(Order.ASCENDING);

    assertNotNull(lastModifiedComparator);
    TestUtil.assertZero("monkey should be equal to monkey!", lastModifiedComparator.compare(monkey, monkey));
    TestUtil.assertNegative("fileContentFinderTestFileFilename should come before gorilla!", lastModifiedComparator.compare(fileContentFinder, gorilla));
    TestUtil.assertPositive("gorilla should come after fileContentFinderTestFileFilename!", lastModifiedComparator.compare(gorilla, fileContentFinder));
  }

  @Test
  public void testGetLastModifiedComparatorDescendingOrder() throws Exception {
    final Comparator<File> lastModifiedComparator = FileComparatorFactory.getLastModifiedComparator(Order.DESCENDING);

    assertNotNull(lastModifiedComparator);
    TestUtil.assertZero("monkey should be equal to monkey!", lastModifiedComparator.compare(monkey, monkey));
    TestUtil.assertPositive("fileContentFinderTestFileFilename should come after gorilla!", lastModifiedComparator.compare(fileContentFinder, gorilla));
    TestUtil.assertNegative("gorilla should come before fileContentFinderTestFileFilename!", lastModifiedComparator.compare(gorilla, fileContentFinder));
  }
}
