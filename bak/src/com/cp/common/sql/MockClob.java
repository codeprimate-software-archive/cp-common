/*
 * MockClob.java (c) 7 January 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.9
 */

package com.cp.common.sql;

import com.cp.common.lang.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class MockClob implements Clob {

  private static final Logger logger = Logger.getLogger(MockClob.class);

  private String clob;

  /**
   * Creates an instance of the MockClob class initialized with the specified String value.
   * @param clob the String value specifying the contents of the SQL CLOB.
   */
  public MockClob(final String clob) {
    this.clob = clob;
  }

  /**
   * Retrieves the CLOB value designated by this Clob object as an ascii stream.
   * @return a java.io.InputStream object containing the CLOB data.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public InputStream getAsciiStream() throws SQLException {
    return new ByteArrayInputStream(clob.getBytes());
  }

  /**
   * Retrieves the CLOB value designated by this Clob object as a java.io.Reader object (or as a stream of characters).
   * @return a java.io.Reader object containing the CLOB data.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public Reader getCharacterStream() throws SQLException {
    return new StringReader(clob);
  }

  /**
   * Retrieves a copy of the specified substring in the CLOB value designated by this Clob object. The substring begins
   * at position pos and has up to length consecutive characters.
   * @param position the first character of the substring to be extracted. The first character is at position 1.
   * @param length the number of consecutive characters to be copied.
   * @return a String that is the specified substring in the CLOB value designated by this Clob object.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public String getSubString(final long position, final int length) throws SQLException {
    final int beginIndex = (int) (position - 1);
    final int endIndex = (beginIndex + length);
    return clob.substring(beginIndex, endIndex);
  }

  /**
   * Retrieves the number of characters in the CLOB value designated by this Clob object.
   * @return length of the CLOB in characters.
   * @throws SQLException if there is an error accessing the length of the CLOB value.
   */
  public long length() throws SQLException {
    return clob.length();
  }

  /**
   * Retrieves the character position at which the specified substring searchstr appears in the SQL CLOB value
   * represented by this Clob object. The search begins at position start.
   * @param searchstr the substring for which to search.
   * @param start the position at which to begin searching; the first position is 1.
   * @return the position at which the substring appears or -1 if it is not present; the first position is 1.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public long position(final String searchstr, final long start) throws SQLException {
    return position(new MockClob(searchstr), start);
  }

  /**
   * Retrieves the character position at which the specified Clob object searchstr appears in this Clob object. The
   * search begins at position start.
   * @param searchstr the Clob object for which to search.
   * @param start the position at which to begin searching; the first position is 1.
   * @return the position at which the Clob object appears or -1 if it is not present; the first position is 1.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public long position(final Clob searchstr, long start) throws SQLException {
    return clob.indexOf(searchstr.getSubString(0, (int) searchstr.length()), (int) --start);
  }

  /**
   * Retrieves a stream to be used to write Ascii characters to the CLOB value that this Clob object represents,
   * starting at position position.
   * @param position the position at which to start writing to this CLOB object.
   * @return the stream to which ASCII encoded characters can be written.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public OutputStream setAsciiStream(final long position) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves a stream to be used to write a stream of Unicode characters to the CLOB value that this Clob object
   * represents, at position position.
   * @param position the position at which to start writing to the CLOB value.
   * @return a stream to which Unicode encoded characters can be written.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public Writer setCharacterStream(final long position) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Writes the given Java String to the CLOB value that this Clob object designates at the position position.
   * @param position the position at which to start writing to the CLOB value that this Clob object represents.
   * @param str the string to be written to the CLOB value that this Clob designates.
   * @return the number of characters written.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public int setString(final long position, String str) throws SQLException {
    return setString(position, str, 0, str.length());
  }

  /**
   * Writes length characters of str, starting at character offset, to the CLOB value that this Clob represents.
   * @param position the position at which to start writing to the CLOB value that this Clob object represents.
   * @param str the string to be written to the CLOB value that this Clob designates.
   * @param offset the offset into str to start reading the characters to be written.
   * @param length the number of characters to be written.
   * @return the number of characters written.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public int setString(long position, final String str, final int offset, final int length) throws SQLException {
    clob = StringUtil.insert(clob, str.substring(offset, length), (int) --position);
    return length;
  }

  /**
   * Truncates the CLOB value that this Clob designates to have a length of len characters.
   * @param length the length, in bytes, to which the CLOB value should be truncated.
   * @throws SQLException if there is an error accessing the CLOB value.
   */
  public void truncate(final long length) throws SQLException {
    clob = StringUtil.truncate(clob, (int) length);
  }

}
