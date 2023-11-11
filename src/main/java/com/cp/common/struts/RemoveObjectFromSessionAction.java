/*
 * RemoveObjectFromSessionAction.java (c) 3 May 2004
 *
 * Copyright (c) 2004, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.1
 * @see com.cp.common.struts.AddObjectToSessionAction
 * @see com.cp.common.struts.DefaultAction
 */

package com.cp.common.struts;

import com.cp.common.lang.Assert;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;

public class RemoveObjectFromSessionAction extends DefaultAction {

  private static final Log logger = LogFactory.getLog(RemoveObjectFromSessionAction.class);

  /**
   * Performs a remove operation of an object from session specified in the ActionMapping for this Action
   * by the sessionObjectKey property.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param form the ActionForm instance containing information from the HTML form.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @param response the HTTPServletResponse object used to send information the client.
   * @return an ActionForward object specifying the next Action to be invoked.
   * @throws Exception if execution fails.
   */
  public ActionForward execute(final CPActionMapping mapping,
                               final CPActionForm form,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
    throws Exception {
    removeObjectFromSession(mapping, request.getSession());
    return super.execute(mapping, form, request, response);
  }

  /**
   * Method used to remove an object from session given the ActionMapping for this Action and the HttpSession
   * of the client's application session.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param session the HttpSession object representing the client's application session, containing all information
   * for the session
   */
  protected void removeObjectFromSession(final CPActionMapping mapping, final HttpSession session) {
    final String sessionObjectKey = mapping.getSessionObjectKey();

    Assert.notEmpty(sessionObjectKey, "The sessionObjectKey property must be specified in the action mapping for the RemoveObjectFromSessionAction!");

    if (logger.isDebugEnabled()) {
      logger.debug("removing object from session referenced by key (" + sessionObjectKey + ")...");
    }

    session.removeAttribute(mapping.getSessionObjectKey());
  }

}
