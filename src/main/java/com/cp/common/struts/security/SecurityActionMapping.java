/*
 * SecurityActionMapping.java (c) 3 May 2004
 *
 * Copyright (c) 2004, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.23
 */

package com.cp.common.struts.security;

import com.cp.common.struts.CPActionMapping;
import org.apache.log4j.Logger;

public class SecurityActionMapping extends CPActionMapping {

  private static final Logger logger = Logger.getLogger(SecurityActionMapping.class);

  private static final boolean DEFAULT_SHOW_MESSAGE = true;

  private boolean showMessage = DEFAULT_SHOW_MESSAGE;

  private String securityMessageKey = null;

  /**
   * Returns the resource boundle property key used to get the localized message for the security
   * alert.
   * @return a String value containing the key referencing the appropriate security message in the
   * resource boundle properties file.
   */
  public String getSecurityMessageKey() {
    return securityMessageKey;
  }

  /**
   * Sets the key contained in the resource boundle property file referring to the localized security message.
   * @param securityMessageKey a String value containing the key in the resource boundle property file
   * referring to the appropriate security message.
   */
  public void setSecurityMessageKey(final String securityMessageKey) {
    logger.debug("securityMessageKey (" + securityMessageKey + ")");
    this.securityMessageKey = securityMessageKey;
  }

  /**
   * Determines whether the security violation should alert the user's attention with a message.
   * @return a boolean value indicating whether a security alert message should inform the user
   * of the security violation.
   */
  public boolean isShowMessage() {
    return showMessage;
  }

  /**
   * Sets whether the security violation should alert the user's attention with a message.
   * @param showMessage a boolean value indicating whether a security alert message should inform the user
   * of the security violation.
   */
  public void setShowMessage(final boolean showMessage) {
    logger.debug("showMessage = " + showMessage);
    this.showMessage = showMessage;
  }

}
