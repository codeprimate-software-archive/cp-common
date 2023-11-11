/*
 * NonLocalDate.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.11.16
 * @deprecated The java.util.Calendar object should be used in place of this
 * NonLocalDate class.
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NonLocalDate extends Date {

  /**
   * Allocates a NonLocalDate object and initializes it so that it represents
   * the time at which it was allocated, measured to the nearest millisecond.
   */
  public NonLocalDate() {
    super(System.currentTimeMillis());
  }

  /**
   * Allocates a NonLocalDate object and initializes it to represent the specified
   * number of milliseconds since the standard base time known as "the epoch",
   * namely January 1, 1970, 00:00:00 GMT.
   *
   * @param millis is a long value representing the number of milliseconds from the epoch.
   */
  public NonLocalDate(final long millis) {
    super(millis);
  }

  /**
   * readObject is the method called during serialization to stream this object over the
   * wire.  The default method, Date.readObject defined in the super class, streams the
   * number of milliseconds at the point where it was created to it's desination, thus
   * adjusted for timezone since the same long value represents a different time across
   * timezones dependent upon your localality.  This new readObject method preserves this
   * point in time by adding or subtracting the difference across timezones.  Thus a
   * date/time created in one timezone represents the same date/time in another timezone.
   *
   * @param in a java.io.ObjectInputStream used to read the NonLocalizedDate object from
   * the input stream.
   * @throws java.lang.ClassNotFoundException if the Object read from the stream is not
   * a NonLocalizedDate object
   * @throws java.io.IOException if the NonLocalizedDate object cannot be read from the
   * stream.
   */
  private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
    int originTimeZoneOffset = in.readInt();
    long milliseconds = in.readLong();
    milliseconds = milliseconds + (originTimeZoneOffset - TimeZone.getDefault().getRawOffset());
    setTime(milliseconds);
  }

  /**
   * toString prints the Date object in the following format month/day/year.
   *
   * @return a Ljava.lang.String representation of the Date object.
   */
  public String toString() {
    return toString("MM/dd/yyyy");
  }

  /**
   * toString prints the Date object in the specified format as determined by
   * the format parameter.
   *
   * @param format a java.lang.String object of the formatted Date.
   * @return a Ljava.lang.String representation of the Date object.
   */
  public String toString(final String format) {
    if (ObjectUtil.isNull(format)) {
      return super.toString();
    }
    return (new SimpleDateFormat(format)).format(this);
  }

  /**
   * writeObject first writes the raw offset of the originating time zone and then serializes
   * the long value representing the number of milliseconds from the epoch at that particular
   * timezone.
   *
   * @param out a java.io.ObjectOutputSteam used to stream this NonLocalizedDate object over
   * the wire.
   * @throws java.io.IOException if the NonLocalizedData object could not be streamed over the
   * wire.
   */
  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeInt(TimeZone.getDefault().getRawOffset());
    out.writeLong(getTime());
  }

}
