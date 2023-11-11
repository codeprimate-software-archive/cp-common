/*
 * FileContentFinder.java (c) 15 November 2002
 *
 * The FileContentFinder class searches for the first occurence of a specified line
 * in the specified file and returns the line number on which the line was found
 * (contained, is more accurate).
 *
 * The class will return a -1 if the line does not exist in the specified file.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.1.16
 * @see java.io.LineNumberReader
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class FileContentFinder {

  private static final Logger logger = Logger.getLogger(FileContentFinder.class);

  private static final boolean DEFAULT_CACHE_FILE_CONTENTS = false;

  private static boolean cacheFileContents = DEFAULT_CACHE_FILE_CONTENTS;

  private static Map fileContentCache = new HashMap();

  /**
   * Default constructor for this class.  The constructor is private to enforce
   * non-instantiability.
   */
  private FileContentFinder() {
  }

  /**
   * Sets whether the FileContentFinder class should cache the contents of files
   * searched.
   * @param cache a boolean value indicating if file contents are to be cached.
   */
  public static void cacheFileContents(final boolean cache) {
    logger.debug("cache = " + cache);
    cacheFileContents = cache;
  }

  /**
   * Gets a LineNumberReader for the specified file, checking first to see if the file
   * has been cached, and then reading the file from the file system if the file has
   * not been cached.
   * @param file the file to search and obtain the content for.
   * @return a LineNumberReader to search and locate content based on line number.
   * @throws FileNotFoundException if the file could not be found in the local file
   * system.
   */
  private static LineNumberReader getFileContents(final File file) throws FileNotFoundException {
    logger.debug("file: " + file);

    Reader fileReader = null;
    final Object fileContents = fileContentCache.get(file);

    if (ObjectUtil.isNotNull(fileContents)) {
      logger.debug("file contents in cache!");
      fileReader = new StringReader(fileContents.toString());
    }
    else {
      logger.debug("file contents NOT in cache, reading from file system!");
      fileReader = new FileReader(file);
    }

    return new LineNumberReader(new BufferedReader(fileReader));
  }

  /**
   * Opens the file and searches for the lineOfText returning the line number if the
   * line is found, or a -1 if the line is not contained in the file.
   * @param file the File object referring to the file whose contents will be searched
   * for lineOfText.
   * @param lineOfText the line of text to search for in file.
   * @return the line number of the line in the file or a -1 if the line is not in the
   * file.
   * @throws java.io.IOException if the file cannot be read.
   */
  public static int getLineNumber(final File file,
                                  final String lineOfText)
      throws IOException {
    if (!file.exists()) {
      logger.warn("The file ("+file.getAbsolutePath() +") does not exist.");
      throw new FileNotFoundException("The file \""+file.getAbsolutePath() +"\" does not exist.");
    }

    final LineNumberReader fileReader = getFileContents(file);
    final StringBuffer buffer = new StringBuffer();
    String line = null;
    int lineNumber = -1;

    while ((line = fileReader.readLine()) != null) {
      if (logger.isDebugEnabled()) {
        logger.debug("line: " + line);
      }
      if (isCachingFileContents()) {
        buffer.append(line).append("\n");
      }
      if ((line.indexOf(lineOfText) != -1) && (lineNumber == -1)) {
        lineNumber = fileReader.getLineNumber();
        logger.debug("Found line \"" + lineOfText + "\" @ line " + lineNumber);
        if (!isCachingFileContents()) {
          break;
        }
      }
    }

    fileReader.close();

    if (isCachingFileContents() && !fileContentCache.containsKey(file)) {
      fileContentCache.put(file, buffer.toString());
    }

    return lineNumber;
  }

  /**
   * Returns a String containing help information on how to use this class.  This method
   * is typically called to display help information at the command line.
   * @return a String containing help information.
   */
  private static String help() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("\nGetLineNumberForLine v1.0 - Copyright (c) 2002, Code Primate \n");
    buffer.append("All Rights Reserved.\n\n");
    buffer.append("help:\n\n");
    buffer.append("> java jjb.toolbox.util.FileContentFinder <file name> <line of text>\n\n");
    buffer.append("args:\n");
    buffer.append("\t<file name>     is the name of the file to search.\n");
    buffer.append("\t<line of text>  is the line of text to report the line number for.\n\n");
    return buffer.toString();
  }

  /**
   * Returns a boolean value indicating whether file content is being cached for
   * subsequent reads.
   * @return a boolean value of true if file content is cached, false otherwise.
   */
  public static boolean isCachingFileContents() {
    return cacheFileContents;
  }

  /**
   * Clears the content of the cache.
   */
  public static void refresh() {
    logger.debug("clearing file content cache!");
    fileContentCache.clear();
  }

  /**
   * Causes the contents of the specified file to be reloaded.  If the file has not
   * been previously cached, then this method does nothing.
   * @param file the file whose cached contents should be refreshed on the next
   * read.
   */
  public static void refresh(final File file) {
    fileContentCache.remove(file);
  }

  /**
   * The executable method of this program.
   * @param args the command-line arguments.
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println(help());
      System.exit(1);
    }

    final File file = new File(args[0]);
    final String lineOfText = args[1];

    System.out.println("The line \"" + lineOfText + "\" is on line " + getLineNumber(file,lineOfText));
  }

}
