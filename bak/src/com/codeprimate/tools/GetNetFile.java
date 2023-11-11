/*
 * GetNetFile.java (c) 29 September 2001
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.24
 */

package com.codeprimate.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetNetFile {

  private static final long KB = 1024;
  private static final long MB = 1048576;
  private static final long GB = 1073741824;
  private static final long BUFFER_SIZE = 64 * KB;

  private static final Log logger = LogFactory.getLog(GetNetFile.class);

  /**
   * Constructs a GetNetFile object to transfer a file from a remote
   * Server to the localhost.
   */
  private GetNetFile() {
  }

  public static void streamFile(final URL fromLocation, final File toLocation) throws IOException {
    streamFile(fromLocation, toLocation, new NullCallback());
  }

  /**
   * Transfers a file from the specified remote location to a location on the localhost.
   * @param fromLocation java.net.URL object referring to the remote location of the file to transfer
   * to the localhost.
   * @param toLocation java.io.File object specifying the location on the localhost to store the file.
   * @throws java.io.IOException if the stream is corrupted during the file transfer.
   */
  public static void streamFile(final URL fromLocation, final File toLocation, final Callback callback)
      throws IOException {
    final URLConnection connection = fromLocation.openConnection();
    if (logger.isDebugEnabled()) {
      logger.debug("Downloading (" + connection.getContentLength() + ") bytes from (" + fromLocation.toExternalForm() + ")!");
    }

    callback.notifyContentLength(connection.getContentLength());

    final BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
    final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toLocation));

    final byte[] buffer = new byte[(int) BUFFER_SIZE];
    int len = -1;
    int totalLen = 0;

    while ((len = in.read(buffer, 0, buffer.length)) != -1) {
      out.write(buffer, 0, len);
      totalLen += len;
      callback.notifyProgress(totalLen);
    }

    out.flush();
    out.close();
    in.close();
  }

  /**
   * main is the executable method of this program.
   * @param args java.lang.String array containing the arguments to this program.
   * Note: that arg[0] contains the URL of the remote file to transfer and arg[1]
   * contains the destination of the file on the localhost.
   */
  public static void main(String[] args) throws Exception {
    streamFile(new URL(args[0]), new File(args[1]));
  }

  public static interface Callback {
    public void notifyContentLength(final int contentLength);
    public void notifyProgress(final int currentLength);
  }

  private static final class NullCallback implements Callback {
    public void notifyContentLength(final int contentLength) {
      logger.debug("contentLength = " + contentLength);
    }

    public void notifyProgress(final int currentLength) {
      logger.debug("currentLength = " + currentLength);
    }
  }

}
