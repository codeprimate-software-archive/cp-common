/*
 * AbstractAction.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.01
 * @see com.cp.common.struts.CPActionForm
 * @see com.cp.common.struts.CPActionMapping
 * @see org.apache.commons.beanutils.BeanUtils
 * @see org.apache.commons.beanutils.PropertyUtils
 * @see org.apache.struts.action.Action
 * @see org.apache.struts.action.ActionForm
 * @see org.apache.struts.action.ActionMapping
 */

package com.cp.common.struts;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectNotFoundException;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConversionException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractAction extends Action {

  protected final Log log = LogFactory.getLog(getClass());

  /**
   * Casts the ActionForm object as an instance of the CPActionForm class.
   * @param form the ActionForm object being cast as an instance of the CPActionForm class.
   * @return an instance of the CPActionForm class from the ActionForm object parameter.
   * @throws IllegalArgumentException if the ActionForm object is not an instance of the CPActionForm class.
   */
  protected static CPActionForm getActionForm(final ActionForm form) {
    if (!(form instanceof CPActionForm)) {
      throw new IllegalArgumentException("Expected an ActionForm instance of type CPActionForm, but was ("
        + ClassUtil.getClassName(form) + ")!");
    }
    return (CPActionForm) form;
  }

  /**
   * Casts the ActionMapping object as an instance of the CPActionMapping class.
   * @param mapping the ActionMapping object being cast as an instance of the CPActionMapping class.
   * @return an instance of the CPActionMapping class from the ActionMapping object parameter.
   * @throws IllegalArgumentException if the ActionMapping object is not an instance of the CPActionMapping class.
   */
  protected static CPActionMapping getActionMapping(final ActionMapping mapping) {
    if (!(mapping instanceof CPActionMapping)) {
      throw new IllegalArgumentException("Expected an ActionMapping instance of type CPActionMapping, but was ("
        + ClassUtil.getClassName(mapping) + ")!");
    }
    return (CPActionMapping) mapping;
  }

  /**
   * Returns the String constant used to lookup the input forward for failure.
   * @return a String value used as a lookup value for retrieving the failure input forward.
   */
  protected static String getFailureForward() {
    return ActionForwardConstants.FAILURE_FORWARD;
  }

  /**
   * Returns the String constant used to lookup the input forward for success.
   * @return a String value used as a lookup value for retrieving the success input forward.
   */
  protected static String getSuccessForward() {
    return ActionForwardConstants.SUCCESS_FORWARD;
  }

  /**
   * Copies the contents of the from object into the to object.
   * @param fromObject the Object to copy from.
   * @param toObject the Object to copy to.
   * @throws ConversionException if the copy from object into the to object fails.
   */
  protected static void copyFromObjectToObject(final Object fromObject, final Object toObject) {
    try {
      final Map<String, Object> fromObjectMap = BeanUtil.getPropertyValues(fromObject);
      fromObjectMap.remove("class");

      BeanUtil.setPropertyValues(toObject, fromObjectMap);
    }
    catch (Exception e) {
      throw new ConversionException("Failed to copy object (" + fromObject.getClass().getName() + ") to object ("
        + toObject.getClass().getName() + ")!", e);
    }
  }

  /**
   * Returns the object in session specified by the sessionObjectKey property in the ActionMapping.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @return the object in session for the configured sessionObjectKey property in the ActionMapping.
   * @throws ObjectNotFoundException if object...
   * @see AbstractAction#getSessionObject(String)
   */
  protected Object getSessionObject(final CPActionMapping mapping, final HttpServletRequest request)
    throws ObjectNotFoundException {
    final String sessionObjectKey = mapping.getSessionObjectKey();

    if (log.isDebugEnabled()) {
      log.debug("session object key (" + sessionObjectKey + ")");
    }

    Object sessionObject = request.getSession(true).getAttribute(mapping.getSessionObjectKey());

    if (ObjectUtil.isNull(sessionObject)) {
      log.warn("The object specified by key (" + mapping.getSessionObjectKey() + ") was not found in session!");
      sessionObject = getSessionObject(mapping.getSessionObjectKey());
      request.getSession().setAttribute(sessionObjectKey, sessionObject);
    }

    return sessionObject;
  }

  /**
   * Strategy method used by subclasses to lookup the session object mapped to the specified key.
   * @param sessionObjectKey a String value indicating the key referencing the session object instance to get.
   * @return an session object instance.
   * @throws ObjectNotFoundException if the object referenced by the key cannot be found.
   * @see AbstractAction#getSessionObject(CPActionMapping, javax.servlet.http.HttpServletRequest)
   */
  protected abstract Object getSessionObject(String sessionObjectKey) throws ObjectNotFoundException;

  /**
   * Stores the specified object in session mapped to the specified key.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param sessionObject the object to store in session mapped to the specified key.
   */
  protected void setSessionObject(final CPActionMapping mapping,
                                  final HttpServletRequest request,
                                  final Object sessionObject) {
    if (ObjectUtil.isNotNull(sessionObject)) {
      if (log.isDebugEnabled()) {
        log.debug("storing object (" + sessionObject.getClass().getName() + ") in session mapped to key ("
          + mapping.getSessionObjectKey() + ")...");
      }
      request.getSession(true).setAttribute(mapping.getSessionObjectKey(), sessionObject);
    }
  }

  /**
   * Implements the logic and behavior of this Action.  Delegates to the corresponding execute method expecting an
   * CPActionMapping and CPActionForm objects.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param form the ActionForm instance containing information from the HTML form.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param response the HTTPServletResponse object used to send information the client.
   * @return an ActionForward object specifing the next Action to be invoked.
   * @throws Exception if execution fails.
   */
  public final ActionForward execute(final ActionMapping mapping,
                                     final ActionForm form,
                                     final HttpServletRequest request,
                                     final HttpServletResponse response)
    throws Exception {
    return execute(getActionMapping(mapping), getActionForm(form), request, response);
  }

  /**
   * Strategy method implemented by subclasses to perform business logic and behavior of this application.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param form the ActionForm instance containing information from the HTML form.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param response the HTTPServletResponse object used to send information the client.
   * @return an ActionForward object specifing the next Action to be invoked.
   * @throws Exception if execution fails.
   */
  protected abstract ActionForward execute(CPActionMapping mapping, CPActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
