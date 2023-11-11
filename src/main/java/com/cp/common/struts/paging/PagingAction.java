/*
 * PagingAction.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 * @see com.cp.common.struts.DefaultAction
 */

package com.cp.common.struts.paging;

import com.cp.common.struts.CPActionForm;
import com.cp.common.struts.CPActionMapping;
import com.cp.common.struts.DefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

public class PagingAction extends DefaultAction {

  private static final Logger logger = Logger.getLogger(PagingAction.class);

  /**
   * Performs the page movement opertion according to the PagingDirection parameter of the PagingActionForma
   * and sets the forms offset and length attributes accordingly.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward execute(final CPActionMapping mapping,
                               final CPActionForm form,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
      throws Exception {
    final PagingActionForm pagingForm = getPagingForm(form);
    final PagingActionMapping pagingMapping = getPagingMapping(mapping);

    if (logger.isDebugEnabled()) {
      logger.debug("paging form (before): " + pagingForm);
    }

    final int increment = pagingMapping.getIncrement();
    logger.debug("increment = " + increment);

    if (pagingForm.getCollectionSize() > 0) { // Make sure we have results before getting down to business...
      final PagingDirection pagingDirection = (pagingForm.getPagingDirection() != null ?
          pagingForm.getPagingDirection() : pagingMapping.getDirection());

      logger.debug("pagingDirection (" + pagingDirection + ")");

      long length = 0;
      long offset = pagingForm.getOffset() + (pagingDirection.getDirectionValue() * increment + pagingDirection.getDirectionOffset());

      logger.debug("length (before) = " + length);
      logger.debug("offset (before) = " + offset);

      if (offset <= 0) {
        offset = 0;
        length = Math.min(increment, pagingForm.getCollectionSize());
      }
      else if (offset >= pagingForm.getCollectionSize()) {
        final int remainder = pagingForm.getCollectionSize() % increment;
        logger.debug("remainder = " + remainder);

        if (remainder == 0) {
          offset = pagingForm.getCollectionSize() - increment;
          length = increment;
        }
        else {
          int multiple = pagingForm.getCollectionSize() / increment;
          offset = increment * multiple;
          length = remainder;
        }
      }
      else {
        length = Math.min(increment, (pagingForm.getCollectionSize() - offset));
      }

      logger.debug("length (after) = " + length);
      logger.debug("offset (after) = " + offset);

      pagingForm.setLength((int) length);
      pagingForm.setOffset((int) offset);
    }

    pagingForm.setPagingDirection(PagingDirection.SAME);
    pagingForm.setIncrement(increment);

    if (logger.isDebugEnabled()) {
      logger.debug("pagingForm (after): " + pagingForm);
    }

    return mapping.findForward(getSuccessForward());
  }

  /**
   * Returns the ActionForm as an instance of the PagingActionForm class.
   * @param form the ActionForm being cast to a PagingActionForm.
   * @return a instance of the PagingActionForm class from the ActionForm parameter.
   * @throws IllegalArgumentException if the ActionForm is not an instance of the PagingActionForm class.
   */
  private final PagingActionForm getPagingForm(final CPActionForm form) {
    if (!(form instanceof PagingActionForm)) {
      logger.warn("The form type (" + form.getClass().getName() + ") is invalid!");
      throw new IllegalArgumentException("A PagingActionForm must be configured as the ActionForm for this Action.");
    }
    return (PagingActionForm) form;
  }

  /**
   * Returns the ActionMapping as an instance of the PagingActionMapping class.
   * @param mapping the ActionMapping being cast to a PagingActionMapping.
   * @return a instance of the PagingActionMapping class from the ActionMapping parameter.
   * @throws IllegalArgumentException if the ActionMapping is not an instance of the PagingActionMapping class.
   */
  private final PagingActionMapping getPagingMapping(final CPActionMapping mapping) {
    if (!(mapping instanceof PagingActionMapping)) {
      logger.warn("The mapping type (" + mapping.getClass().getName() + ") is invalid!");
      throw new IllegalArgumentException("A PagingActionMapping must be configured as the ActionMapping for this Action.");
    }
    return (PagingActionMapping) mapping;
  }

}
