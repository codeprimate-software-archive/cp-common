/*
 * FileFinder.java (c) 21 September 2001
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.io.ComposableFileFilter
 * @see com.cp.common.io.FileUtil
 * @see java.io.File
 * @see java.io.FileFilter
 */

package com.cp.common.util;

import com.cp.common.io.ComposableFileFilter;
import com.cp.common.io.FileUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileFinder {

  private static final boolean DEFAULT_FIND_ALL = true;
  private static final boolean DEFAULT_IGNORE_CASE = false;
  private static final boolean DEFAULT_RECURSE = true;
  private static final boolean DEFAULT_VERBOSE = false;

  private static final String HELP_SWITCH = "-help";
  private static final String QUESTION_MARK_SWITCH = "-?";
  private static final String WILDCARD = "*";
  private static final String WILDCARD_REG_EXP = "(.)*";

  private boolean found = false;

  private final Config config;

  private File searchPath;

  protected final Log logger = LogFactory.getLog(getClass());

  private PrintStream debug;

  private String filename;

  /**
   * Creates a new instance of the FileFinder class to locate files in the file system
   * of the localhost.
   */
  public FileFinder() {
    this(DEFAULT_FIND_ALL, DEFAULT_IGNORE_CASE, DEFAULT_RECURSE, DEFAULT_VERBOSE, System.out);
  }

  /**
   * Creates a new instance of the FileFinder class to locate files in the file system
   * of the localhost.  This constructor initializes the state of the FileFinder class
   * which determines the behavior of the search operation.
   *
   * @param findAll is a boolean value determing if all instances of the file in the
   * file system of the localhost should be found.
   * @param ignoreCase is a boolean value indicating that the FileFinder should ignore
   * case when comparing the file being searched to files in the file system.
   * @param recurse is a boolean value indicating that subdirectoris of the searchPath
   * should be searched.
   * @param verbose is a boolean value indicating that the search operation should
   * output it's current action and profile the searching method.
   * @param debug is a PrintStream object used to output the verbose information.
   */
  public FileFinder(final boolean findAll,
                    final boolean ignoreCase,
                    final boolean recurse,
                    final boolean verbose,
                    final PrintStream debug) {
    config = new Config(findAll, ignoreCase, recurse, verbose);
    this.debug = debug;
  }

  /**
   * Returns the configuration object for this FileFinder instance.
   * @return a Config object storing the configuration state of this FileFinder
   * instance.
   */
  private Config getConfig() {
    return config;
  }

  /**
   * Returns the name of the file being searched by this FileFinder instance.
   * @return a String representation of the name of the file being searched.
   */
  private String getFilename() {
    return filename;
  }

  /**
   * Sets the name of the file to search for in the file system of the localhost.
   * NOTE: this method is only used when the FileFinder is used at the command-line.
   */
  private void setFilename(final String filename) {
    if (StringUtil.isEmpty(filename)) {
      logger.warn("'" + filename + "' is a not a valid filename!");
      throw new IllegalArgumentException("(" + filename + ") is a not a valid filename!");
    }
    logger.debug("filename = '" + filename + "'");
    this.filename = filename;
  }

  /**
   * Determines whether the specified File to search has been found by the search
   * operation.
   * @return a boolean value indicating if the File object has been found.
   */
  private boolean isFound() {
    return found;
  }

  /**
   * Sets whether the specified File object has been found by the search operation.
   * @param found a boolean value indicating if the File object has been found.
   */
  private void setFound(final boolean found) {
    logger.debug("found = " + found);
    this.found = found;
  }

  /**
   * Returns the root path used to begin the search operation searching for file
   * with filename.
   * @return a File object representing the starting point of the search operation.
   */
  private File getSearchPath() {
    return searchPath;
  }

  /**
   * Sets the starting location in the file system of the localhost to begin the
   * search operation.
   * @param searchPath the starting location in the file system of the localhost
   * to begin the search.
   */
  private void setSearchPath(final File searchPath) {
    if (ObjectUtil.isNull(searchPath)) {
      logger.warn("The searchPath parameter cannot be null!");
      throw new NullPointerException("The searchPath parameter cannot be null!");
    }
    if (logger.isDebugEnabled()) {
      logger.debug("searchPath (" +searchPath + ")");
    }
    this.searchPath = searchPath;
  }

  // TODO: add comment!
  private FileFilter getFileFilter(String filename) {
    if (StringUtil.isEmpty(filename) || StringUtil.contains(filename, File.separator)) {
      logger.warn("(" + filename + ") is not a valid file name!");
      throw new IllegalArgumentException("(" + filename + ") is not a valid file name!");
    }

    if (StringUtil.contains(filename, WILDCARD)) {
      logger.debug("filename (" + filename + ") contains wildcard!");
      FileFilter fileFilter = null;

      final String fileExtension = FileUtil.getFileExtension(filename);
      logger.debug("fileExtension = '" + fileExtension + "'");
      if (!StringUtil.isEmpty(fileExtension) && !StringUtil.contains(fileExtension, WILDCARD)) {
        filename = StringUtil.replace(filename, "."+fileExtension, ""); // remove fileExtension from filename.
        fileFilter = new FileExtensionFileFilter(fileExtension);
      }

      filename = StringUtil.replace(filename, WILDCARD, WILDCARD_REG_EXP);
      logger.debug("filename = '" + filename + "'");

      return ComposableFileFilter.add(fileFilter, new RegexFileFilter(filename));
    }
    else {
      logger.debug("filename (" + filename + ") contains no wildcard!");
      return new DefaultFileFilter(filename);
    }
  }

  /**
   * Determines whether the File object is a valid directory.
   * @param file the File object to determine if it refers to a directory.
   * @return a boolean value indicating whether the specified File object
   * is a valid directory.
   */
  private boolean isValidDirectory(final File file) {
    return (ObjectUtil.isNotNull(file) && file.isDirectory());
  }

  /**
   * Searches the file system of the localhost starting at searchPath in search
   * of the file.
   * @return an array of File objects referring to the path(s) found by the
   * search operation.
   */
  private File[] findFile() {
    if (StringUtil.isEmpty(getFilename())) {
      logger.warn("The name of the file to search must be specified!");
      throw new IllegalStateException("The name of the file to search must be specified!");
    }
    return findFile(getFilename(), getSearchPath());
  }

  // TODO: add comment!
  public File[] findFile(final String filename) {
    return findFile(getFileFilter(filename), File.listRoots());
  }

  // TODO: add comment!
  public File[] findFile(final String filename, final File searchPath) {
    if (!isValidDirectory(searchPath)) {
      logger.warn("'" + searchPath + "' is not a valid directory!");
      throw new IllegalArgumentException("'" + searchPath + "' is not a valid directory!");
    }
    return findFile(getFileFilter(filename), new File[] { searchPath });
  }

  // TODO: add comment!
  public File[] findFile(final String filename, final File[] searchPaths) {
    for (int index = searchPaths.length; --index >= 0; ) {
      if (!isValidDirectory(searchPaths[index])) {
        logger.warn("'" + searchPaths[index] + "' is not a valid directory!");
        throw new IllegalArgumentException("'" + searchPaths[index] + "' is not a valid directory!");
      }
    }
    return findFile(getFileFilter(filename), searchPaths);
  }

  // TODO: add comment!
  public File[] findFile(final FileFilter fileFilter) {
    return findFile(fileFilter, File.listRoots());
  }

  // TODO: add comment!
  public File[] findFile(final FileFilter fileFilter, final File searchPath) {
    if (!isValidDirectory(searchPath)) {
      logger.warn("'" + searchPath + "' is not a valid directory!");
      throw new IllegalArgumentException("'" + searchPath + "' is not a valid directory!");
    }
    return findFile(fileFilter, new File[] { searchPath });
  }

  // TODO: add comment!
  public File[] findFile(final FileFilter fileFilter, final File[] searchPaths) {
    if (ObjectUtil.isNull(fileFilter)) {
      logger.warn("The fileFilter cannot be null!");
      throw new NullPointerException("The fileFilter cannot be null!");
    }

    if (ArrayUtil.isEmpty(searchPaths)) {
      logger.warn("The paths to search cannot be null or empty!");
      throw new IllegalArgumentException("The paths to search cannot be null or empty!");
    }

    final List searchPathsList = new ArrayList();
    for (int index = searchPaths.length; --index >= 0; ) {
      if (isValidDirectory(searchPaths[index])) {
        searchPathsList.add(searchPaths[index]);
      }
      else {
        if (logger.isDebugEnabled()) {
          logger.debug("searchPath (" + searchPaths[index] + ") is not a valid directory!");
        }
      }
    }

    java.util.Collections.sort(searchPathsList);

    final Set matches = new HashSet();
    setFound(false);

    final long t1 = System.currentTimeMillis();

    for (Iterator it = searchPathsList.iterator(); it.hasNext() && (getConfig().isFindAll() || !isFound()); ) {
      findFile(fileFilter, (File) it.next(), matches, "");
    }

    final long t2 = System.currentTimeMillis();

    if (getConfig().isVerbose()) {
      debug.println("\nTotal Time: " + (t2 - t1) + " millisecond(s)");
    }

    return (File[]) matches.toArray(new File[matches.size()]);
  }

  /**
   * findFile traverses the local file system for the file in question.
   * Depending on the program switches/parameters, the program will either
   * find all occurences or the first occurence and may or may not check
   * subdirectories for the file.  Another switch may be used to ignore case
   * which is useful when running this program on Unix/Linux boxes.
   */
  private void findFile(final FileFilter fileFilter, final File searchPath, final Set matches, final String offset) {
    try {
      if (getConfig().isVerbose()) {
        logger.debug("Searching " + searchPath);
        debug.println(offset + searchPath.toString());
      }

      final File[] fileList = searchPath.listFiles(fileFilter);
      if (ArrayUtil.isEmpty(fileList)) {
        logger.debug("The file list for search path (" + searchPath + ") was null or empty!");
        return;
      }

      for (Iterator it = Arrays.asList(fileList).iterator(); it.hasNext() && (getConfig().isFindAll() || !isFound()); ) {
        final File file = (File) it.next();
        if (logger.isDebugEnabled()) {
          logger.debug("file: " + file);
        }

        if (file.isDirectory() && getConfig().isRecurse()) {
          logger.debug("File was a directory and recursion is set!");
          findFile(fileFilter, file, matches, offset + "  ");  // RECURSE
        }
        else if (file.isFile()) {
          logger.debug("Found file!");
          setFound(true);
          matches.add(file);
        }
        else {
          logger.warn("Undetermined File object (" + file + ")!");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Parses the command-line arguments to the program initializing various
   * state properties of the FileFinder instance.
   *
   * @param args a String array containing the arguments to this program.
   * @return a boolean value indicating that all parameters were read in
   * and the state of the FileFinder instance was set appropriately.
   */
  private boolean init(String[] args) {
    int index = 0;
    for (Iterator it = Arrays.asList(args).iterator(); it.hasNext(); index++) {
      final String arg = it.next().toString();
      logger.debug("arg" + index + " = '" + arg + "'");

      if (arg.equalsIgnoreCase(Config.FIND_ALL_SWITCH)) {
        getConfig().setFindAll(true);
      }
      else if (arg.equalsIgnoreCase(Config.IGNORE_CASE_SWITCH)) {
        getConfig().setIgnoreCase(true);
      }
      else if (arg.equalsIgnoreCase(Config.RECURSE_SWITCH)) {
        getConfig().setRecurse(true);
      }
      else if (arg.equalsIgnoreCase(Config.VERBOSE_SWITCH)) {
        getConfig().setVerbose(true);
      }
      else if (arg.equalsIgnoreCase(QUESTION_MARK_SWITCH) || arg.equalsIgnoreCase(HELP_SWITCH)) {
        help();
        return false;
      }
      else if (!arg.startsWith("-") && ObjectUtil.isNull(searchPath)) {
        setSearchPath(new File(arg));
      }
      else if (!arg.startsWith("-")) {
        setFilename(arg);
      }
      else {
        logger.warn(arg + " is an invalid switch!");
        throw new IllegalArgumentException(arg + " is an invalid switch!");
      }
    }
    return true;
  }

  /**
   * Prints program help information.
   */
  private static String help() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("FileFinder v1.0 - Copyright (c) 2001, Code Primate\n");
    buffer.append("All Rights Reserved\n\n");
    buffer.append("usage: \n");
    buffer.append("\t> java FileFinder [options] <basepath> <file>\n\n");
    buffer.append("options:\n");
    buffer.append("\t -findall        sets whether FileFinder will find the first occurrence\n");
    buffer.append("\t                 or all occurrences of the file from the basepath.\n");
    buffer.append("\t -ignorecase     case insensitive file search.\n");
    buffer.append("\t -recurse        set whether FileFinder searches subdirectories.\n");
    buffer.append("\t -verbose        verbose output and profiling information.\n");
    buffer.append("\t -? -help        prints this help message.\n\n");
    return buffer.toString();
  }

  /**
   * main is the executable method for this class.
   * @param args is a String array containing the command-line arguments to this program.
   */
  public static void main(final String[] args) {
    try {
      if (ArrayUtil.isEmpty(args)) {
        System.out.println(help());
        System.exit(-1);
      }

      final FileFinder fileFinder = new FileFinder(false, false, false, false, System.out);

      if (fileFinder.init(args)) {
        System.out.println("Running file search...");
        final File[] paths = fileFinder.findFile();

        if (ArrayUtil.isNotEmpty(paths)) {
          final int numberOfPaths = paths.length;
          for (int index = numberOfPaths; --index >= 0;) {
            System.out.println("Found in " + paths[index].toString());
          }
        }
        else {
          System.err.println("File (" + fileFinder.getFilename() + ") not found!");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  /**
   * The Config class is used to store configuration information for the FileFinder class.
   */
  protected class Config {
    // NOTE: must be lower case!
    public static final String FIND_ALL_SWITCH = "-findall";
    public static final String IGNORE_CASE_SWITCH = "-ignorecase";
    public static final String RECURSE_SWITCH = "-recurse";
    public static final String VERBOSE_SWITCH = "-verbose";

    private boolean findAll = false;
    private boolean ignoreCase = false;
    private boolean recurse = true;
    private boolean verbose = false;

    public Config() {
    }

    public Config(final boolean findAll,
                  final boolean ignoreCase,
                  final boolean recurse,
                  final boolean verbose) {
      setFindAll(findAll);
      setIgnoreCase(ignoreCase);
      setRecurse(recurse);
      setVerbose(verbose);
    }

    public final boolean isFindAll() {
      return findAll;
    }

    public final void setFindAll(final boolean findAll) {
      logger.debug("setting findAll = " + findAll);
      this.findAll = findAll;
    }

    public final boolean isIgnoreCase() {
      return ignoreCase;
    }

    public final void setIgnoreCase(boolean ignoreCase) {
      logger.debug("setting ignoreCase = " + ignoreCase);
      this.ignoreCase = ignoreCase;
    }

    public final boolean isRecurse() {
      return recurse;
    }

    public final void setRecurse(boolean recurse) {
      logger.debug("setting recurse = " + recurse);
      this.recurse = recurse;
    }

    public final boolean isVerbose() {
      return verbose;
    }

    public final void setVerbose(boolean verbose) {
      logger.debug("setting verbose = " + verbose);
      this.verbose = verbose;
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{findAll = ");
      buffer.append(isFindAll());
      buffer.append(", ignoreCase = ").append(isIgnoreCase());
      buffer.append(", recurse = ").append(isRecurse());
      buffer.append(", verbose = ").append(isVerbose());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * The DefaultFileFilter class is used to match a file by exact name.
   */
  protected final class DefaultFileFilter implements FileFilter {

    private final String filename;

    public DefaultFileFilter(final String filename) {
      if (StringUtil.isEmpty(filename)) {
        logger.warn("'" + filename + "' is not a valid filename!");
        throw new IllegalArgumentException("'" + filename + "' is not a valid filename!");
      }
      this.filename = filename.trim();
    }

    public boolean accept(final File pathname) {
      // Handle Directory
      if (pathname.isDirectory() && getConfig().isRecurse()) {
        logger.debug("pathname (" + pathname + ") is a directory and recurse is set!");
        return true;
      }

      // Handle File
      if (pathname.isFile()) {
        logger.debug("pathname is a file!");
        if (getConfig().isIgnoreCase()) {
          logger.debug("Ignoring case on compare!");
          return pathname.getName().equalsIgnoreCase(getFilename());
        }
        logger.debug("Performing case-sensitive comparison!");
        return pathname.getName().equals(getFilename());
      }

      return false;
    }

    private String getFilename() {
      return filename;
    }
  }

  /**
   * The RegexFileFilter class uses regular expressions (patterns) to match
   * files by name.
   */
  protected class RegexFileFilter implements FileFilter {

    private final Pattern pattern;

    public RegexFileFilter(final String pattern) {
      logger.debug("pattern = " + pattern);

      // Validate the Pattern
      if (StringUtil.isEmpty(pattern)) {
        logger.warn("'" + pattern + "' is not a valid pattern!");
        throw new IllegalArgumentException("'" + pattern + "' is not a valid pattern!");
      }

      // Configure the FileFilter for String Case
      if (getConfig().isIgnoreCase()) {
        logger.debug("Case Insensitive Search Enabled!");
        this.pattern = Pattern.compile(pattern, (Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));
      }
      else {
        logger.debug("Case Sensitive Search Enabled!");
        this.pattern = Pattern.compile(pattern);
      }
    }

    public boolean accept(final File pathname) {
      // Handle Directory
      if (pathname.isDirectory() && getConfig().isRecurse()) {
        logger.debug("pathname (" + pathname + ") is a directory and recurse is set, returning true.");
        return true;
      }

      // Handle File
      if (pathname.isFile()) {
        final String filename = pathname.getName();
        logger.debug("filename = '" + filename + "'");
        return getPattern().matcher(filename).matches();
      }

      return false;
    }

    private Pattern getPattern() {
      return pattern;
    }
  }

  /**
   * This FileFinder class is used to discriminate files based on file extension.
   */
  protected class FileExtensionFileFilter implements FileFilter {
    private final Set fileExtensions;

    /**
     * Creates a new instance of the FileExtensionFileFilter class used by the
     * File.listFiles method to filter the contents of the search directory.
     *
     * @param fileExtension is a String representation of a file extension
     * indicating that only files with the following extension are accepted
     * by this FileFilter.
     */
    private FileExtensionFileFilter(final String fileExtension) {
      this(new String[] { fileExtension });
    }

    /**
     * Creates a new instance of the FileExtensionFileFilter class used by the
     * File.listFiles method to filter the contents of the search directory.
     *
     * @param fileExtensions is a String array specifying which file extensions
     * a searched file should be a type of in order to be accepted by this
     * filter.
     */
    private FileExtensionFileFilter(final String[] fileExtensions) {
      this.fileExtensions = new HashSet();
      if (ObjectUtil.isNotNull(fileExtensions)) {
        for (Iterator it = Arrays.asList(fileExtensions).iterator(); it.hasNext(); ) {
          final String fileExtension = (String) it.next();
          logger.debug("ext = " + fileExtension);
          if (!StringUtil.isEmpty(fileExtension)) {
            this.fileExtensions.add(fileExtension.trim().toLowerCase());
          }
        }
      }
      else {
        logger.debug("fileExtensions was null!");
      }
    }

    /**
     * Tests whether the specified File object is accepted by this FileFilter.
     *
     * @param pathname a File object refering to the abstract pathname to be accepted
     * or rejected by this FileFilter.
     * @return a boolean value of true if and only if the File object is accepted by
     * this FileFilter.
     */
    public boolean accept(final File pathname) {
      if (logger.isDebugEnabled()) {
        logger.debug("pathname: " + pathname);
      }

      // Handle Directory
      if (pathname.isDirectory() && getConfig().isRecurse()) {
        logger.debug("pathname (" + pathname + ") is a directory and recurse is set!");
        return true;
      }

      // Handle File
      if (pathname.isFile()) {
        final String fileExtension = FileUtil.getFileExtension(pathname);
        logger.debug("fileExtension = '" + fileExtension + "'");
        if (CollectionUtil.isEmpty(fileExtensions) || fileExtensions.contains(fileExtension.trim().toLowerCase())) {
          logger.debug("fileExtensions is empty of contains the fileExtension (" + fileExtension + ")");
          return true;
        }
      }

      return false;
    }
  }

}
