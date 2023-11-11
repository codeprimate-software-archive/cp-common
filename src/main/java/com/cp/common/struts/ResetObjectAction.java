/*
 * ResetObjectAction.java (c) 24 January 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.struts.DefaultAction
 */

package com.cp.common.struts;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Resettable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;

public class ResetObjectAction extends DefaultAction {

  /**
   * Resets the session objects state if the object implements the Resettable interface.
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

    if (sessionObject instanceof Resettable) {
      if (log.isDebugEnabled()) {
        log.debug("Resetting the state of session object (" + ClassUtil.getClassName(sessionObject) + ")...");
      }
      ((Resettable) sessionObject).reset();
    }
    else {
      log.warn("The session object (" + ClassUtil.getClassName(sessionObject)
        + ") does not implement the Resettable interface.");
    }

    return mapping.findForward(getSuccessForward());
  }

}
