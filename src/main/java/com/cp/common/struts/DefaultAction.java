/*
 * DefaultAction.java (c) 27 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 * @see com.cp.common.struts.AbstractAction
 */

package com.cp.common.struts;

import com.cp.common.lang.ObjectNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;

public class DefaultAction extends AbstractAction {

  /**
   * Strategy method used by subclasses to lookup the session object mapped to the specified key.
   * @param sessionObjectKey a String value indicating the key referencing the session object instance to get.
   * @return an session object instance.
   * @throws ObjectNotFoundException if the object referenced by the key cannot be found.
   * @see AbstractAction#getSessionObject(CPActionMapping, javax.servlet.http.HttpServletRequest)
   */
  protected Object getSessionObject(final String sessionObjectKey) throws ObjectNotFoundException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Pass through execute method to success forward.
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
    return mapping.findForward(getSuccessForward());
  }

}
