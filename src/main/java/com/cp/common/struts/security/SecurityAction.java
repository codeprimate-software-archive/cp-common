/*
 * SecurityAction.java (c) 23 May 2004
 *
 * Copyright (c) 2004, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 * @see com.cp.common.struts.DefaultAction
 */

package com.cp.common.struts.security;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.struts.CPActionForm;
import com.cp.common.struts.CPActionMapping;
import com.cp.common.struts.DefaultAction;
import com.cp.common.lang.MutableUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SecurityAction extends DefaultAction {

  private static final Log logger = LogFactory.getLog(SecurityAction.class);

  public static final String SECURITY_MESSAGE_KEY = "-security_message_key-";

  /**
   * Performs a security check on the Object in session to determine what access the user is permitted.
   * @param mapping
   * @param cxForm
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward execute(final CPActionMapping mapping,
                               final CPActionForm cxForm,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
      throws Exception {
    final Object sessionObject = getSessionObject(mapping, request);

    if (logger.isInfoEnabled()) {
      logger.info("sessionObject (" + sessionObject.getClass().getName() + ")");
    }

    // if the sessionObject is mutable, then allow whatever action would normally come after this
    // action to execute, else deny access to the Object in session.
    if (MutableUtil.isMutable(sessionObject)) { // mutable
      logger.info("The session object is mutable!");
      return mapping.findForward(getSuccessForward());
    }
    else { // immutable
      logger.info("The session object is immutable!");

      if (((SecurityActionMapping) mapping).isShowMessage()) {
        String securityMessageKey = (String) request.getSession().getAttribute(SECURITY_MESSAGE_KEY);
        securityMessageKey = (ObjectUtil.isNotNull(securityMessageKey) ? securityMessageKey : ((SecurityActionMapping) mapping).getSecurityMessageKey());

        if (logger.isDebugEnabled()) {
          logger.debug("security message key (" + securityMessageKey + ")");
        }

        if (!StringUtil.isEmpty(securityMessageKey)) {
          final ActionMessages messages = new ActionMessages();
          messages.add("SecurityViolation", new ActionMessage(securityMessageKey));
          saveMessages(request, messages);
        }
      }

      final ActionForward failureForward = mapping.findForward(getFailureForward());

      if (logger.isDebugEnabled()) {
        logger.debug("failureForward (" + (ObjectUtil.isNotNull(failureForward) ? failureForward.getPath() : "") + ")");
      }

      // if the failureFoward is not configured for this action mapping, return the input forward.
      return (ObjectUtil.isNotNull(failureForward) ? failureForward : mapping.getInputForward());
    }
  }

}
