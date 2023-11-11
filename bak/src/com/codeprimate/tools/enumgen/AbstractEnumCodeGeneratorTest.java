/*
 * AbstractEnumCodeGeneratorTest.java (c) 23 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.7
 * @see com.codeprimate.tools.enumgen.AbstractEnumCodeGenerator
 */

package com.codeprimate.tools.enumgen;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.util.TestUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractEnumCodeGeneratorTest extends TestCase {

  private static final String ENUM_CLASS_NAME = "MockEnum";
  private static final String MOCK_ENUM_ENUM_ENTRY_FILE = System.getProperty(TestUtil.PROJECT_DIR_SYSTEM_PROPERTY) + "/etc/codegen/MockEnumEnumEntryFile.txt";
  private static final String MOCK_ENUM_JAVA_FILE = System.getProperty(TestUtil.PROJECT_DIR_SYSTEM_PROPERTY) + "/src/com/cp/common/test/mock/MockEnum.java";
  private static final String PACKAGE_NAME = "com.cp.common.test.mock";
  private static final String PACKAGE_KEY_WORD = "package";
  private static final String SOURCE_FILE = System.getProperty(TestUtil.PROJECT_DIR_SYSTEM_PROPERTY) + "/tmp/" + ENUM_CLASS_NAME + ".java";

  private File sourceFile = null;

  public AbstractEnumCodeGeneratorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractEnumCodeGeneratorTest.class);
    //suite.addTest(new AbstractEnumCodeGeneratorTest("testName"));
    return suite;
  }

  private String readFile(final File theFile) throws IOException {
    BufferedReader reader = null;

    if (theFile.exists()) {
      reader = new BufferedReader(new FileReader(theFile));
    }
    else {
      reader = new BufferedReader(new InputStreamReader(AbstractEnumCodeGeneratorTest.class.getResourceAsStream(
        theFile.getAbsolutePath())));
    }

    final StringBuffer buffer = new StringBuffer();
    String line = null;

    while (ObjectUtil.isNotNull(line = reader.readLine())) {
      buffer.append(line);
      buffer.append("\n");
    }

    reader.close();

    return buffer.toString();
  }

  private String stripHeader(final String contents) {
    int index = contents.indexOf(PACKAGE_KEY_WORD);

    if (index != -1) {
      return contents.substring(index);
    }

    return contents;
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    if (ObjectUtil.isNotNull(sourceFile) && sourceFile.exists()) {
      sourceFile.delete();
    }
  }

  public void testAbstractEnumCodeGenerator() throws Exception {
    final AbstractEnumCodeGenerator.CommandLineArguments arguments = new AbstractEnumCodeGenerator.CommandLineArguments();
    arguments.setEnumClassName(ENUM_CLASS_NAME);
    arguments.setEnumEntryFile(new File(MOCK_ENUM_ENUM_ENTRY_FILE));
    arguments.setPackageName(PACKAGE_NAME);
    arguments.setSourceFile(new File(SOURCE_FILE));

    try {
      new AbstractEnumCodeGenerator(arguments).generateCode();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      fail("Failed to generate code for enum class (" + ENUM_CLASS_NAME + ")!");
    }

    final String testMockEnumFile = stripHeader(readFile(new File(MOCK_ENUM_JAVA_FILE))).trim();
    final String generatedMockEnumFile = stripHeader(readFile(sourceFile = arguments.getSourceFile())).trim();

    assertEquals(testMockEnumFile, generatedMockEnumFile);
  }

  public void testGetEnumSetName() throws Exception {
    final AbstractEnumCodeGenerator.CommandLineArguments arguments = new AbstractEnumCodeGenerator.CommandLineArguments();
    arguments.setPackageName(PACKAGE_NAME);
    arguments.setEnumClassName(ENUM_CLASS_NAME);
    arguments.setEnumEntryFile(new File(MOCK_ENUM_ENUM_ENTRY_FILE));
    arguments.setSourceFile(new File(SOURCE_FILE));

    final AbstractEnumCodeGenerator codeGenerator = new AbstractEnumCodeGenerator(arguments);

    assertNotNull(codeGenerator);
    assertEquals("MOCK_ENUM_SET", codeGenerator.getEnumSetName());

    arguments.setEnumClassName("State");

    assertEquals("STATE_SET", codeGenerator.getEnumSetName());

    arguments.setEnumClassName("Country");

    assertEquals("COUNTRY_SET", codeGenerator.getEnumSetName());

    arguments.setEnumClassName("MaritalStatusPartnership");

    assertEquals("MARITAL_STATUS_PARTNERSHIP_SET", codeGenerator.getEnumSetName());
  }

  public void testGetEnumTypeName() throws Exception {
    final AbstractEnumCodeGenerator.CommandLineArguments arguments = new AbstractEnumCodeGenerator.CommandLineArguments();
    arguments.setPackageName(PACKAGE_NAME);
    arguments.setEnumClassName(ENUM_CLASS_NAME);
    arguments.setEnumEntryFile(new File(MOCK_ENUM_ENUM_ENTRY_FILE));
    arguments.setSourceFile(new File(SOURCE_FILE));

    final AbstractEnumCodeGenerator codeGenerator = new AbstractEnumCodeGenerator(arguments);

    assertNotNull(codeGenerator);
    assertEquals("ENUM_TYPE_ONE", codeGenerator.getEnumTypeName("Enum Type One"));
    assertEquals("ENUM_TYPE_TWO", codeGenerator.getEnumTypeName("Enum Type Two"));
    assertEquals("ENUM_1", codeGenerator.getEnumTypeName("Enum 1"));
    assertEquals("ENUM_2", codeGenerator.getEnumTypeName("ENUM 2"));
  }

}
