/*
 * AbstractSubmitAction.java (c) 27 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 * @see com.cp.common.struts.AbstractAction
 */

package com.cp.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;

public abstract class AbstractSubmitAction extends AbstractAction {

  private static final Log logger = LogFactory.getLog(AbstractSubmitAction.class);

  /**
   * Action.execute method containing logic for information to pass from the form object, encapsulating
   * user entered information from the HTML form on the client, to the domain object to be used by service calls
   * to manipulate, process and possibly persist information.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param form the ActionForm instance containing information from the HTML form.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param response the HTTPServletResponse object used to send information the client.
   * @return an ActionForward object specifing the next Action to be invoked.
   * @throws Exception if execution fails.
   */
  public ActionForward execute(final CPActionMapping mapping,
                               final CPActionForm form,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
    throws Exception {
    final Object sessionObject = getSessionObject(mapping, request);

    if (logger.isDebugEnabled()) {
      logger.debug("form (" + form + ")");
      logger.debug("session object (" + sessionObject + ")");
    }

    copyFormToSessionObject(form, sessionObject);

    return execute(mapping, sessionObject, request, response);
  }

  /**
   * Strategy method used by subclasses to carry out the business function of the application.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param sessionObject is the domain model object of the application.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param response the HTTPServletResponse object used to send information the client.
   * @return an ActionForward object specifing the next Action to be invoked.
   * @throws Exception if execution fails.
   */
  protected abstract ActionForward execute(CPActionMapping mapping, Object sessionObject, HttpServletRequest request, HttpServletResponse response) throws Exception;

  /**
   * Copies information from the ActionForm object containing information entered by the user on the client to the
   * domain object (application business object).
   * @param form the ActionForm object used by the JSP page to store information entered by the user in the HTML form
   * of the client application.
   * @param sessionObject the domain object of the application.
   * @see AbstractAction#copyFromObjectToObject(Object, Object)
   */
  protected void copyFormToSessionObject(final CPActionForm form, final Object sessionObject) {
    copyFromObjectToObject(form, sessionObject);
  }

}
