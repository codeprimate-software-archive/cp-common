/*
 * MockBlob.java (c) 7 January 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.9
 */

package com.cp.common.sql;

import com.cp.common.lang.ObjectUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class MockBlob implements Blob {

  private static final Logger logger = Logger.getLogger(MockBlob.class);

  private byte[] blob;

  /**
   * Creates an instance of the MockBlob class initialized with the specified byte array.
   * @param blob a byte array containing the contents of the SQL BLOB.
   */
  public MockBlob(final byte[] blob) {
    if (ObjectUtil.isNull(blob)) {
      logger.warn("The source byte array for this Blob cannot be null!");
      throw new NullPointerException("The source byte array for this Blob cannot be null!");
    }
    this.blob = blob;
  }

  /**
   * Retrieves the BLOB value designated by this Blob instance as a stream.
   * @return a stream containing the BLOB data.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public InputStream getBinaryStream() throws SQLException {
    return new ByteArrayInputStream(blob);
  }

  /**
   * Retrieves a single byte at index from the SQL Blob.
   * @param index an integer value specifying the byte index in the SQL Blob.
   * @return the byte value in the SQL Blob at index.
   */
  final byte getByte(final int index) {
    return blob[index];
  }

  /**
   * Retrieves all or part of the BLOB value that this Blob object represents, as an array of bytes. This byte array
   * contains up to length consecutive bytes starting at position position.
   * @param position the ordinal position of the first byte in the BLOB value to be extracted; the first byte is at
   * position 1.
   * @param length the number of consecutive bytes to be copied.
   * @return a byte array containing up to length consecutive bytes from the BLOB value designated by this Blob object,
   * starting with the byte at position position.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public byte[] getBytes(long position, final int length) throws SQLException {
    try {
      final byte[] dest = new byte[length];
      System.arraycopy(blob, (int) --position, dest, 0, length);
      return dest;
    }
    catch (IndexOutOfBoundsException e) {
      logger.error("An error occurred while accessing the BLOB value!", e);
      throw new SQLException("An error occurred while accessing the BLOB value!");
    }
  }

  /**
   * Returns the number of bytes in the BLOB value designated by this Blob object.
   * @return length of the BLOB in bytes.
   * @throws SQLException if there is an error accessing the length of the BLOB.
   */
  public long length() throws SQLException {
    return blob.length;
  }

  /**
   * Retrieves the byte position at which the specified byte array pattern begins within the BLOB value that this Blob
   * object represents. The search for pattern begins at position start.
   * @param pattern the byte array for which to search.
   * @param start the position at which to begin searching; the first position is 1.
   * @return the position at which the pattern appears, else -1.
   * @throws SQLException if there is an error accessing the BLOB.
   */
  public long position(final byte[] pattern, final long start) throws SQLException {
    return position(new MockBlob(pattern), start);
  }

  /**
   * Retrieves the byte position in the BLOB value designated by this Blob object at which pattern begins. The search
   * begins at position start.
   * @param pattern the Blob object designating the BLOB value for which to search.
   * @param start the position in the BLOB value at which to begin searching; the first position is 1.
   * @return the position at which the pattern begins, else -1.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public long position(final Blob pattern, final long start) throws SQLException {
    final int endIndex = (int) (length() - pattern.length());

    for (int startIndex = (int) start; startIndex < endIndex; startIndex++) {
      if (equals(getBytes(startIndex, (int) pattern.length()), pattern.getBytes(0, (int) pattern.length()))) {
        return startIndex;
      }
    }

    return -1;
  }

  /**
   * A convenience method to determine the equality of two byte arrays.
   * @param array1 the first byte array in the equality comparison.
   * @param array2 the second byte array in the equality comparison.
   * @return a boolean value indicating whether the size and contents of the two byte arrays are equal.
   */
  private boolean equals(final byte[] array1, final byte[] array2) {
    if (array1.length == array2.length) {
      for (int index = array1.length; --index >= 0; ) {
        if (array1[index] != array2[index]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Retrieves a stream that can be used to write to the BLOB value that this Blob object represents. The stream begins
   * at position position.
   * @param position the position in the BLOB value at which to start writing.
   * @return a java.io.OutputStream object to which data can be written.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public OutputStream setBinaryStream(final long position) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Writes the given array of bytes to the BLOB value that this Blob object represents, starting at position pos, and
   * returns the number of bytes written.
   * @param position the position in the BLOB object at which to start writing.
   * @param bytes the array of bytes to be written to the BLOB value that this Blob object represents.
   * @return the number of bytes written.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public int setBytes(final long position, final byte[] bytes) throws SQLException {
    return setBytes(position, bytes, 0, bytes.length);
  }

  /**
   * Writes all or part of the given byte array to the BLOB value that this Blob object represents and returns the
   * number of bytes written. Writing starts at position pos in the BLOB value; len bytes from the given byte array
   * are written.
   * @param position the position in the BLOB object at which to start writing.
   * @param bytes the array of bytes to be written to this BLOB object.
   * @param offset the offset into the array bytes at which to start reading the bytes to be set.
   * @param length the number of bytes to be written to the BLOB value from the array of bytes bytes.
   * @return the number of bytes written.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public int setBytes(long position, final byte[] bytes, int offset, final int length) throws SQLException {
    position--;

    for (int count = 0; count < length; count++) {
      try {
        blob[(int) position++] = bytes[offset++];
      }
      catch (ArrayIndexOutOfBoundsException e) {
        logger.error("An error occurred while accessing the BLOB value!", e);
        throw new SQLException("An error occurred while accessing the BLOB value!");
      }
    }

    return length;
  }

  /**
   * Truncates the BLOB value that this Blob object represents to be len bytes in length.
   * @param length the length, in bytes, to which the BLOB value that this Blob object represents should be truncated.
   * @throws SQLException if there is an error accessing the BLOB value.
   */
  public void truncate(final long length) throws SQLException {
    try {
      final byte[] dest = new byte[(int) length];
      System.arraycopy(blob, 0, dest, 0, (int) length);
      blob = dest;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      logger.error("An error occurred while accessing the BLOB value!", e);
      throw new SQLException("An error occurred while accessing the BLOB value!");
    }
  }

}
