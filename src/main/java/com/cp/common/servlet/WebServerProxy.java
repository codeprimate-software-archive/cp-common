/*
 * WebServerProxy.java (c) 15 April 2003
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.29
 */

package com.cp.common.servlet;

import com.cp.common.lang.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class WebServerProxy extends HttpServlet {

  private static final Logger logger = Logger.getLogger(WebServerProxy.class);

  private static final int BUFFER_SIZE = 4096;
  private static final int FLUSH_COUNT = 16;

  private static final String INVALID_ENCODING_MESSAGE = "<p><h1> The Character Encoding (" + StringUtil.REPLACEMENT_TOKEN
        + ") is not valid. </h1></p>";
  private static final String INVALID_URL_MESSAGE = "<p><h1> The URL " + StringUtil.REPLACEMENT_TOKEN
    + " is not valid. </h1></p>";
  private static final String UNAVAILABLE_SERVICE_MESSAGE = "<p><h1> This service is currently not available, please try" +
    " back again later. </h1></p>";
  private static final String URL_REQUEST_PARAMETER = "url";

  /**
   * Handles an HTTP GET method request from a client such as a web browser or a Java applet.  doGet processes the
   * request by calling the handleRequest method.
   * @param request the HttpServletRequest object encapsulating information about the client's request.
   * @param response the HttpServletResponse object encapsulating information about the server's response
   * to the client's request.
   * @throws java.io.IOException if an IO error occurs.
   * @throws javax.servlet.ServletException if the server is unable to fullfill it's requested operation.
   */
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
    handleRequest(request, response);
  }

  /**
   * Handles an HTTP POST method request from a client such as a web browser or a Java applet.  doPost processes the
   * request by calling the handleRequest method.
   * @param request the HttpServletRequest object encapsulating information about the client's request.
   * @param response the HttpServletResponse object encapsulating information about the server's response
   * to the client's request.
   * @throws java.io.IOException if an IO error occurs.
   * @throws javax.servlet.ServletException if the server is unable to fullfill it's requested operation.
   */
  public void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
    handleRequest(request, response);
  }

  /**
   * Handles both HTTP GET/POST method request from a client such as a web browser or a Java applet.  This
   * class acts as a proxy for the client by forwarding the request to the designated server based on URL
   * and returning the server's response back to the client.  The motivation for this Servlet at the time
   * was to tunnel through firewalls or other proxies that tried to block access to certain sites or other
   * Internet resources.
   * @param request the HttpServletRequest object encapsulating information about the client's request.
   * @param response the HttpServletResponse object encapsulating information about the server's response
   * to the client's request.
   * @throws java.io.IOException if an IO error occurs.
   * @throws javax.servlet.ServletException if the server is unable to fullfill it's requested operation.
   */
  public void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
    final OutputStream out = response.getOutputStream();
    final String urlStr = request.getParameter(URL_REQUEST_PARAMETER);

    try {
      final URL url = new URL(URLDecoder.decode(urlStr, request.getCharacterEncoding()));
      if (logger.isDebugEnabled()) {
        logger.debug("url (" + url + ")");
      }

      final URLConnection connection = url.openConnection();
      connection.setUseCaches(false);

      final InputStream in = connection.getInputStream();

      byte[] buffer = new byte[BUFFER_SIZE];
      int len = -1;
      int count = 0;

      while ((len = in.read(buffer, 0, buffer.length)) > -1) {
        out.write(buffer, 0, len);
        if (++count % FLUSH_COUNT == 0) {
          logger.debug("flushing after count = " + count);
          out.flush();
        }
      }

      out.flush();
    }
    catch (MalformedURLException e) {
      logger.error(StringUtil.replace(INVALID_URL_MESSAGE, StringUtil.REPLACEMENT_TOKEN, urlStr), e);
      handleException(StringUtil.replace(INVALID_URL_MESSAGE, StringUtil.REPLACEMENT_TOKEN, urlStr), out);
    }
    catch (UnsupportedEncodingException e) {
      logger.error(StringUtil.replace(INVALID_ENCODING_MESSAGE, StringUtil.REPLACEMENT_TOKEN,
        request.getCharacterEncoding()), e);
      handleException(StringUtil.replace(INVALID_ENCODING_MESSAGE, StringUtil.REPLACEMENT_TOKEN,
        request.getCharacterEncoding()), out);
    }
    catch (IOException e) {
      logger.error(UNAVAILABLE_SERVICE_MESSAGE, e);
      handleException(UNAVAILABLE_SERVICE_MESSAGE, out);
    }
  }

  /**
   * An exception handler used to notify the clients when an exception occurs in the WebServerProxy with a
   * particular message.
   * @param message the message to send to the client.
   * @param out the OutputStream used to write the message back to the client.
   * @throws IOException if an IO error occurs.
   */
  private void handleException(final String message, final OutputStream out) throws IOException {
    final OutputStreamWriter writer = new OutputStreamWriter(out);
    writer.write(message, 0, message.length());
    writer.flush();
  }

}
