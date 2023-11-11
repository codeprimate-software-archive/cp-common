/*
 * CPActionForm.java (c) 27 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.12.21
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.struts.validation.Validator
 * @see org.apache.struts.action.ActionForm
 */

package com.cp.common.struts;

import com.cp.common.lang.ObjectNotFoundException;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import com.cp.common.struts.validation.ValidationException;
import com.cp.common.struts.validation.Validator;
import com.cp.common.util.ConfigurationException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public abstract class CPActionForm extends ActionForm implements Resettable {

  private static final Log logger = LogFactory.getLog(CPActionForm.class);

  private static final Map<String, Validator> validatorCache = new WeakHashMap<String, Validator>();

  private static final String VALIDATION_ERROR_KEY = "VALIDATION_ERROR";

  /**
   * Gets the Validator instance using a variety of methods to validate this ActionForm instance.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @return a Validator instance specified by the mapping from the cache, session of subclass strategy.
   * @throws ObjectNotFoundException if the Validator instance cannot be found.
   * @see CPActionForm#getValidatorInstance(String)
   */
  private synchronized Validator getValidatorInstance(final CPActionMapping mapping, final HttpServletRequest request)
    throws ObjectNotFoundException {
    final String validatorKey = mapping.getValidatorKey();

    if (logger.isDebugEnabled()) {
      logger.debug("validator key (" + validatorKey + ")");
    }

    Validator validator = validatorCache.get(validatorKey);

    if (ObjectUtil.isNull(validator)) {
      // check in session for the Validator
      logger.info("The Validator was not in the cache; searching in session!");
      validator = (Validator) request.getSession().getAttribute(validatorKey);

      if (ObjectUtil.isNull(validator)) {
        logger.info("The Validator was not in session; creating new Validator and caching!");
        validator = getValidatorInstance(validatorKey);
        validatorCache.put(validatorKey, validator);
      }
    }

    return validator;
  }

  /**
   * Method to be overridden by subclasses to get the instance of the Validator needed to validate this form.
   * @param validatorKey a String value indicaing the key referring to the correct Validator instance needed
   * to validate this form.
   * @return a Validator instance for the specified key.
   * @throws ObjectNotFoundException if the Validator instance cannot be found.
   * @see CPActionForm#getValidatorInstance(CPActionMapping, javax.servlet.http.HttpServletRequest)
   */
  protected abstract Validator getValidatorInstance(String validatorKey) throws ObjectNotFoundException;

  /**
   * Resets the internal state of this ActionForm.
   */
  public void reset() {
  }

  /**
   * Overridden method to validate this form by delegating validation to a Validtor instance.
   * @param mapping the ActionMapping object used to configure Struts Actions.  The ActionMapping object is an
   * instance of the CPActionMapping class and contains a reference to the Session Object and Validator keys.
   * @param request the HTTPServletRequest object containing information about the client's HTTP request.
   * @return an ActionErrors object containing validation errors.
   */
  public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request) {
    final ActionErrors errors = super.validate(mapping, request);

    try {
      final Validator validator = getValidatorInstance(AbstractAction.getActionMapping(mapping), request);

      if (ObjectUtil.isNotNull(validator)) {
        try {
          validator.validateDataTypes(this);
          validator.validateRequiredFields(this);
        }
        catch (ValidationException e) {
          errors.add(VALIDATION_ERROR_KEY, new ActionMessage(e.getMessage()));
        }
      }
    }
    catch (ObjectNotFoundException e) {
      throw new ConfigurationException(e.getMessage());
    }

    return errors;
  }

  /**
   * Returns a String representation of this form object.
   * @return a String representation of this form object.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{");

    try {
      final Map formMap = new TreeMap(BeanUtils.describe(this));
      formMap.remove("multipartRequestHandler");
      formMap.remove("servletWrapper");

      boolean condition = false;

      for (Iterator it = formMap.keySet().iterator(); it.hasNext(); ) {
        final String property = (String) it.next();
        final Object value = formMap.get(property);

        if (condition) {
          buffer.append(", ");
        }

        buffer.append(property).append(" = ").append(value);
        condition = true;
      }
    }
    catch (Exception e) {
      logger.warn("Failed to describe form (" + getClass().getName() + ")!", e);
    }

    buffer.append("}:").append(getClass().getName());

    return buffer.toString();
  }

}
