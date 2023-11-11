/*
 * RMIServlet.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.28
 */

package com.cp.common.servlet;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.net.RequestResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class RMIServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(RMIServlet.class);

  /**
   * Handles an HTTP POST method request sent by a client to this Servlet.  This Servlet functions like an RMI call
   * in that it invoke a method of this Servlets (service call) based on the requested operation of the client.
   * @param request the HttpServletRequest object encapsulating information about the client's request.
   * @param response the HttpServletResponse object encapsulating information about the server's response
   * to the client's request.
   * @throws java.io.IOException if an IO error occurs.
   * @throws javax.servlet.ServletException if the server is unable to fullfill it's requested operation.
   */
  public void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException, ServletException {
    RequestResponse rr = null;
    try {
      final ObjectInputStream objIn = new ObjectInputStream(request.getInputStream());
      rr = (RequestResponse) objIn.readObject();

      final Method servletMethod = getClass().getMethod(rr.getMethodName(), rr.getParameterTypes());
      rr.setReturnValue(servletMethod.invoke(this, rr.getArguments()));
    }
    catch (InvocationTargetException e) {
      logger.error("The service call (" + rr.getMethodName() + ") on server (" + getClass().getName() + ") failed to complete sucessfully!");
      rr.setReturnValue(e.getTargetException());
    }
    catch (Exception e) {
      logger.error("Failed to invoke service (" + getResponse(rr).getMethodName() + ") on server (" + getClass().getName() + ")!", e);
      throw new ServletException("Failed to invoke service (" + getResponse(rr).getMethodName() + ") on server ("
        + getClass().getName() + ")!", e);
    }
    finally {
      rr = getResponse(rr);
      response.setContentType("java-internal/" + rr.getClass().getName());

      final ObjectOutputStream objOut = new ObjectOutputStream(response.getOutputStream());
      objOut.writeObject(rr);
      objOut.flush();
      objOut.close();
    }
  }

  /**
   * Creates a new RequestResponse if the specified RequestResponse object is null, otherwise this method returns
   * the specified RequestResponse object.
   * @param rr the RequestResponse object to return if the not null!
   * @return a RequestResponse object.
   */
  private RequestResponse getResponse(final RequestResponse rr) {
    return (RequestResponse) ObjectUtil.getDefaultValue(rr, new RequestResponse());
  }

}
