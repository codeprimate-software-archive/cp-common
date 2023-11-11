/*
 * RequestResponse.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see java.io.Serializable
 */

package com.cp.common.net;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RequestResponse implements Serializable {

  protected final Log log = LogFactory.getLog(getClass());

  private Object[] arguments = null;

  private Object returnValue = null;

  private String methodName;

  /**
   * Creates an instance of the RequestResponse class.  The RequestResponse object is used to communicate the
   * client's request to the server, and for the server to return it's response back to the client.
   */
  public RequestResponse() {
  }

  /**
   * This constructor is used by the client app to form a request to the server app.  The client specifies the
   * name of the server's service and arguments to pass the service.
   * @param methodName is a String specifying the name of the service to invoke on the server.
   * @param arguments is an Object array containing parameter values to pass to the server's service.
   */
  public RequestResponse(final String methodName,
                         final Object[] arguments) {
    setMethodName(methodName);
    setArguments(arguments);
  }

  /**
   * This constructor is used by the server app to return a response to the client app which sent the request.
   * The server encapsulates it's response in this object as an answer to the client's request.
   * @param returnValue is the Object used to encapsulate any information for the server's response to the
   * client's request.
   */
  public RequestResponse(final Object returnValue) {
    setReturnValue(returnValue);
  }

  /**
   * Returns the actual values passed to the server's service.
   * @return an Object array containing values for the parameters to the server's service.
   */
  public Object[] getArguments() {
    return arguments;
  }

  /**
   * Returns the name of the service (remote method call) to invoke on the server.
   * @return a String representing the service in which to invoke on the server.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Returns the number, order, and types of the arguments to the service (methodName).
   * @return a Class array containing Class types for the arguments to the server's service.
   */
  public Class[] getParameterTypes() {
    return ClassUtil.getClassTypes(getArguments());
  }

  /**
   * Gets the return value as is.
   * @return an Object encapsulating the return value of the server's service call.
   */
  private Object getReturn() {
    return returnValue;
  }

  /**
   * Returns the response of the server as a result of invoking it's service (method) and processing
   * the client's request.
   * @return an Object encapsulating of the server's response to the client's request.
   * @throws java.lang.Exception if the service call on the server fails.
   */
  public Object getReturnValue() throws Exception {
    if (returnValue instanceof Exception) {
      throw (Exception) returnValue;
    }
    return returnValue;
  }

  /**
   * Sets the values of the parameters to pass to the service call on the server.
   * @param arguments an Object array containing the values to the parameters of the service call on the server.
   */
  public final void setArguments(final Object[] arguments) {
    this.arguments = arguments;
  }

  /**
   * Sets the name of the service to invoke on the remote server.
   * @param methodName a String containing the name of the service (remote method call) to invoke on the server.
   * @throws java.lang.NullPointerException if the methodName parameter is null!
   */
  public final void setMethodName(final String methodName) {
    if (ObjectUtil.isNull(methodName)) {
      log.warn("The methodName cannot be null as it determines the service to invoke on the server!");
      throw new NullPointerException("The methodName cannot be null as it determines the service to invoke on the server!");
    }
    this.methodName = methodName;
  }

  /**
   * Sets the response returned by the server app.
   * @param returnValue the Object encapsulating the server's response to the client's request.
   */
  public void setReturnValue(final Object returnValue) {
    this.returnValue = returnValue;
  }

  /**
   * Returns a String containing the state of this RequestResponse object.
   * @return a String representation of this RequestResponse object.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{arguments = ");
    buffer.append(getArguments());
    buffer.append(", methodName = ").append(getMethodName());
    buffer.append(", parameterTypes = ").append(getParameterTypes());
    buffer.append(", returnValue = ").append(getReturn());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
