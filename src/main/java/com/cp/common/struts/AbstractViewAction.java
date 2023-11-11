/*
 * AbstractViewAction.java (c) 27 January 2007
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

public abstract class AbstractViewAction extends AbstractAction {

  private static final Log logger = LogFactory.getLog(AbstractViewAction.class);

  /**
   * Action.execute method containing logic for information to pass from the domain object to the form object
   * used by the JSP page to view information.
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
      logger.debug("session object (" + sessionObject + ")");
    }

    form.reset();
    copySessionObjectToForm(sessionObject, form);

    return mapping.findForward(getSuccessForward());
  }

  /**
   * Copies information from the domain object (application business object) in session to the ActionForm object
   * used by the JSP page to populate information in the HTML form to be viewed by the user in the client application.
   * @param sessionObject the domain object of the application.
   * @param form the ActionForm object used by the JSP page to populate information in the HTML form to be viewed
   * by the user in the client application.
   * @see AbstractAction#copyFromObjectToObject(Object, Object)
   */
  protected void copySessionObjectToForm(final Object sessionObject, final CPActionForm form) {
    copyFromObjectToObject(sessionObject, form);
  }

}
