/*
 * AbstractEnumCodeGenerator.java (c) 20 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.24
 */

package com.codeprimate.tools.enumgen;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConfigurationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractEnumCodeGenerator {

  private static final Log logger = LogFactory.getLog(AbstractEnumCodeGenerator.class);

  // Tags
  private static final String CODE = "<CODE>";
  private static final String CREATED_DATE = "<CREATED_DATE>";
  private static final String DESCRIPTION = "<DESCRIPTION>";
  private static final String ENUM_CLASS_NAME = "<ENUM_CLASS_NAME>";
  private static final String ENUM_TYPE_NAME = "<ENUM_TYPE_NAME>";
  private static final String ENUMERATED_TYPES = "<ENUMERATED_TYPES>";
  private static final String ENUMERATION_SET = "<ENUMERATION_SET>";
  private static final String ENUMERATION_SET_ADDITIONS = "<ENUMERATION_SET_ADDITIONS>";
  private static final String PACKAGE_NAME = "<PACKAGE_NAME>";
  private static final String VERSION_DATE = "<VERSION_DATE>";

  // Delimeters
  private static final String COMMA = ",";
  private static final String FOUR_SPACES = "    ";
  private static final String ONE_SPACE = " ";
  private static final String TWO_SPACES = "  ";
  private static final String UNDERSCORE = "_";

  // AbstractEnum code
  private static final String ADD_METHOD = ".add(" + ENUM_TYPE_NAME + ");";
  private static final String ENUM_TYPE_INSTANTIATION = " = get" + ENUM_CLASS_NAME + "Factory().createInstance(\"" + CODE + "\", \"" + DESCRIPTION + "\");";
  private static final String ENUM_TYPE_MODIFIERS = "public static final ";
  private static final String SET_TYPE = "SET";

  // AbstractEnum Template Pathname & Source File Path
  private static final String DEFAULT_TEMPLATE_FILE_PATH = "/etc/tmpl/AbstractEnum.java.tmpl";
  private static final String SOURCE_FILE_PATH = "tmp/";
  private static final String SOURCE_FILE_EXT = ".java";

  // AbstractEnum Template File
  private static final File DEFAULT_TEMPLATE_FILE = new File(DEFAULT_TEMPLATE_FILE_PATH);

  private static final DateFormat createdDateFormat = new SimpleDateFormat("dd MMM yyyy");
  private static final DateFormat versionDateFormat = new SimpleDateFormat("yyyy.MM.dd");

  private final CommandLineArguments arguments;

  public AbstractEnumCodeGenerator(final CommandLineArguments arguments) {
    if (ObjectUtil.isNull(arguments)) {
      logger.warn("The command-line arguments must be specified to configure the AbstractEnumCodeGenerator!");
      throw new NullPointerException("The command-line arguments must be specified to configure the AbstractEnumCodeGenerator!");
    }

    this.arguments = validate(arguments);
  }

  final void generateCode() throws Exception {
    String template = readTemplateFile(getArguments().getTemplateFile());

    // substitute created date and version information
    final Date now = Calendar.getInstance().getTime();

    template = StringUtil.replace(template, CREATED_DATE, createdDateFormat.format(now));
    template = StringUtil.replace(template, VERSION_DATE, versionDateFormat.format(now));

    // substitute enum class name; set the enumeration Set
    final Set<CodeDescriptionEntry> enumEntrySet = readEnumEntryFile(getArguments().getEnumEntryFile());
    final Set<String> enumeratedTypeSet = new TreeSet<String>();
    final String enumSetName = getEnumSetName();

    template = StringUtil.replace(template, PACKAGE_NAME, getArguments().getPackageName());
    template = StringUtil.replace(template, ENUM_CLASS_NAME, getArguments().getEnumClassName());
    template = StringUtil.replace(template, ENUMERATED_TYPES, getEnumeratedTypes(enumEntrySet, enumeratedTypeSet));
    template = StringUtil.replace(template, ENUMERATION_SET, enumSetName);
    template = StringUtil.replace(template, ENUMERATION_SET_ADDITIONS, getEnumerationSetAdditions(enumSetName, enumeratedTypeSet));

    writeSourceFile(template, getArguments().getSourceFile());
    if (logger.isInfoEnabled()) {
      logger.info("Successfully generated source code to (" + getArguments().getEnumClassName()
        + ") enumerated-type class and saved to path (" + getArguments().getSourceFile().getAbsolutePath() + ")");
    }
  }

  private CommandLineArguments getArguments() {
    return arguments;
  }

  private String getEnumeratedTypes(final Set<CodeDescriptionEntry> codeSet, final Set<String> enumeratedTypeSet) {
    final StringBuffer buffer = new StringBuffer();
    int count = 0;
    int size = codeSet.size();

    for (CodeDescriptionEntry entry : codeSet) {
      final String enumTypeName = getEnumTypeName(entry.getDescription());

      if (logger.isDebugEnabled()) {
        logger.debug("enumTypeName (" + enumTypeName + ")");
      }

      if (count++ > 0) {
        buffer.append(TWO_SPACES);
      }

      buffer.append(ENUM_TYPE_MODIFIERS);
      buffer.append(getArguments().getEnumClassName());
      buffer.append(ONE_SPACE);
      buffer.append(enumTypeName);

      String enumTypeInstantiation = StringUtil.replace(ENUM_TYPE_INSTANTIATION, ENUM_CLASS_NAME, getArguments().getEnumClassName());
      enumTypeInstantiation = StringUtil.replace(enumTypeInstantiation, CODE, entry.getCode());
      enumTypeInstantiation = StringUtil.replace(enumTypeInstantiation, DESCRIPTION, entry.getDescription());

      if (logger.isDebugEnabled()) {
        logger.debug("enumTypeInstantiation (" + enumTypeInstantiation + ")");
      }

      buffer.append(enumTypeInstantiation);
      buffer.append((count < size) ? "\n" : "");

      // Add the enumerated type to the Set
      enumeratedTypeSet.add(enumTypeName);
    }

    return buffer.toString();
  }

  private String getEnumerationSetAdditions(final String enumSetName, final Set<String> enumeratedTypeSet) {
    final StringBuffer buffer = new StringBuffer();
    int count = 0;
    int size = enumeratedTypeSet.size();

    for (String enumeratedTypeName : enumeratedTypeSet) {
      if (count++ > 0) {
        buffer.append(FOUR_SPACES);
      }

      buffer.append(enumSetName);
      buffer.append(StringUtil.replace(ADD_METHOD, ENUM_TYPE_NAME, enumeratedTypeName));
      buffer.append((count < size) ? "\n" : "");
    }

    return buffer.toString();
  }

  protected String getEnumSetName() {
    final String enumClassName = getArguments().getEnumClassName();
    final StringBuffer buffer = new StringBuffer();
    int beginIndex = 0;

    for (int index = 1; index < enumClassName.length(); index++) {
      final char chr = enumClassName.charAt(index);

      if (chr == Character.toUpperCase(chr)) {
        if (beginIndex > 0) {
          buffer.append(UNDERSCORE);
        }

        buffer.append(enumClassName.substring(beginIndex, index).toUpperCase());
        beginIndex = index;
      }
    }

    buffer.append(beginIndex > 0 ? UNDERSCORE : "");
    buffer.append(enumClassName.substring(beginIndex).toUpperCase());
    buffer.append(UNDERSCORE);
    buffer.append(SET_TYPE);

    return buffer.toString();
  }

  protected String getEnumTypeName(final String value) {
    final StringBuffer buffer = new StringBuffer();
    final StringTokenizer parser = new StringTokenizer(value, ONE_SPACE);

    while (parser.hasMoreTokens()) {
      buffer.append(parser.nextToken().toUpperCase());
      buffer.append(parser.hasMoreTokens() ? UNDERSCORE : "");
    }

    return buffer.toString();
  }

  private Set<CodeDescriptionEntry> readEnumEntryFile(final File enumEntryFile) throws FileNotFoundException, IOException {
    if (logger.isInfoEnabled()) {
      logger.info("Reading enum entry file (" + enumEntryFile + ")");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("delimeter (" + getArguments().getDelimeter() + ")");
    }

    final Set<CodeDescriptionEntry> enumEntrySet = new TreeSet<CodeDescriptionEntry>();
    final BufferedReader reader = new BufferedReader(new FileReader(enumEntryFile));
    String line = null;

    while (ObjectUtil.isNotNull(line = reader.readLine())) {
      final StringTokenizer parser = new StringTokenizer(line, getArguments().getDelimeter());
      final String description = parser.nextToken();
      final String code = parser.nextToken();
      enumEntrySet.add(new CodeDescriptionEntry(code, description));
    }

    reader.close();

    return enumEntrySet;
  }

  private String readTemplateFile(final File templateFile) throws FileNotFoundException, IOException {
    if (logger.isInfoEnabled()) {
      logger.info("Reading template file (" + templateFile + ")");
    }

    BufferedReader reader = null;

    if (templateFile.exists()) {
      reader = new BufferedReader(new FileReader(templateFile));
    }
    else {
      reader = new BufferedReader(new InputStreamReader(AbstractEnumCodeGenerator.class.getResourceAsStream(
        DEFAULT_TEMPLATE_FILE_PATH)));
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

  static CommandLineArguments validate(final CommandLineArguments arguments) {
    if (ObjectUtil.isNull(arguments.getEnumEntryFile())) {
      logger.warn("The enum entry file must be specified!");
      throw new ConfigurationException("Please specify the enum entry file.  On the command-line, use the switch ("
        + CommandLineArguments.ENUM_ENTRY_FILE_SWITCH + ").");
    }

    if (ObjectUtil.isNull(arguments.getDelimeter())) {
      arguments.setDelimeter(COMMA);
    }

    if (ObjectUtil.isNull(arguments.getEnumClassName())) {
      logger.warn("The class name of the Enum must be specified!");
      throw new ConfigurationException("Please specify the class name of the Enum.  On the command-line, use the switch ("
        + CommandLineArguments.ENUM_SWITCH + ").");
    }

    if (ObjectUtil.isNull(arguments.getPackageName())) {
      logger.warn("The package name of the Enum must be specified!");
      throw new ConfigurationException("Please specify the package name for the Enum.  On the command-line, use the switch ("
        + CommandLineArguments.PACKAGE_SWITCH + ").");
    }

    if (ObjectUtil.isNull(arguments.getSourceFile())) {
      final StringBuffer sourcePathname = new StringBuffer();
      sourcePathname.append(SOURCE_FILE_PATH);
      sourcePathname.append(arguments.getEnumClassName());
      sourcePathname.append(SOURCE_FILE_EXT);
      arguments.setSourceFile(new File(sourcePathname.toString()));
    }

    if (ObjectUtil.isNull(arguments.getTemplateFile())) {
      arguments.setTemplateFile(DEFAULT_TEMPLATE_FILE);
    }

    return arguments;
  }

  private void writeSourceFile(final String content, final File sourceFile) throws IOException {
    if (logger.isDebugEnabled()) {
      logger.debug("Writing content (" + content + ")\nto source file (" + sourceFile + ")");
    }

    final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(sourceFile)));
    writer.println(content);
    writer.flush();
    writer.close();
  }

  public static void main(final String[] args) throws Exception {
    new AbstractEnumCodeGenerator(CommandLineArguments.parse(args)).generateCode();
  }

  private static final class CodeDescriptionEntry implements Comparable<CodeDescriptionEntry> {

    private String code;
    private String description;

    public CodeDescriptionEntry(final String code, final String description) {
      setCode(code);
      setDescription(description);
    }

    public String getCode() {
      return code;
    }

    private void setCode(final String code) {
      if (ObjectUtil.isNull(code)) {
        logger.warn("The code property value cannot be null!");
        throw new NullPointerException("The code property value cannot be null!");
      }

      this.code = code;
    }

    public String getDescription() {
      return description;
    }

    private void setDescription(final String description) {
      if (ObjectUtil.isNull(description)) {
        logger.warn("The description property value cannot be null!");
        throw new NullPointerException("The description property value cannot be null!");
      }
      this.description = description;
    }

    public int compareTo(final CodeDescriptionEntry entry) {
      return getDescription().compareTo(entry.getDescription());
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof CodeDescriptionEntry)) {
        return false;
      }

      final CodeDescriptionEntry that = (CodeDescriptionEntry) obj;

      return getCode().equals(that.getCode())
        && getDescription().equals(that.getDescription());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + getCode().hashCode();
      hashValue = 37 * hashValue + getDescription().hashCode();
      return hashValue;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{code = ");
      buffer.append(getCode());
      buffer.append(", description = ").append(getDescription());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  public static final class CommandLineArguments {

    public static final String DELIMETER_SWITCH = "-delim";
    public static final String ENUM_ENTRY_FILE_SWITCH = "-enumentryfile";
    public static final String ENUM_SWITCH = "-enum";
    public static final String PACKAGE_SWITCH = "-package";
    public static final String SOURCE_FILE_SWITCH = "-srcfile";
    public static final String TEMPLATE_FILE_SWITCH = "-tmplfile";

    private File enumEntryFile;
    private File sourceFile;
    private File templateFile;

    private String delimeter = COMMA;
    private String enumClassName;
    private String packageName;

    public String getDelimeter() {
      return delimeter;
    }

    public void setDelimeter(final String delimeter) {
      this.delimeter = delimeter;
    }

    public String getEnumClassName() {
      return enumClassName;
    }

    public void setEnumClassName(final String enumClassName) {
      this.enumClassName = enumClassName;
    }

    public File getEnumEntryFile() {
      return enumEntryFile;
    }

    public void setEnumEntryFile(final File enumEntryFile) {
      this.enumEntryFile = enumEntryFile;
    }

    public String getPackageName() {
      return packageName;
    }

    public void setPackageName(final String packageName) {
      this.packageName = packageName;
    }

    public File getSourceFile() {
      return sourceFile;
    }

    public void setSourceFile(final File sourceFile) {
      this.sourceFile = sourceFile;
    }

    public File getTemplateFile() {
      return templateFile;
    }

    public void setTemplateFile(final File templateFile) {
      this.templateFile = templateFile;
    }

    static CommandLineArguments parse(final String[] args) {
      final CommandLineArguments commandLineArguments = new CommandLineArguments();

      for (int index = 0; index < args.length; index++) {
        final String arg = args[index];

        if (logger.isDebugEnabled()) {
          logger.debug("arg (" + arg + ")");
        }

        if (arg.equals(DELIMETER_SWITCH)) {
          commandLineArguments.setDelimeter(args[++index]);
        }
        else if (arg.equals(ENUM_ENTRY_FILE_SWITCH)) {
          commandLineArguments.setEnumEntryFile(new File(args[++index]));
        }
        else if (arg.equals(ENUM_SWITCH)) {
          commandLineArguments.setEnumClassName(args[++index]);
        }
        else if (arg.equals(PACKAGE_SWITCH)) {
          commandLineArguments.setPackageName(args[++index]);
        }
        else if (arg.equals(SOURCE_FILE_SWITCH)) {
          commandLineArguments.setSourceFile(new File(args[++index]));
        }
        else if (arg.equals(TEMPLATE_FILE_SWITCH)) {
          commandLineArguments.setTemplateFile(new File(args[++index]));
        }
        else {
          logger.warn("Encountered invalid command-line arugment (" + arg + ")!");
        }
      }

      return commandLineArguments;
    }
  }

}
