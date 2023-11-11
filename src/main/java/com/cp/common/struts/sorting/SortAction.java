/*
 * SortAction.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.struts.DefaultAction
 */

package com.cp.common.struts.sorting;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.struts.CPActionForm;
import com.cp.common.struts.CPActionMapping;
import com.cp.common.struts.DefaultAction;
import com.cp.common.util.ComparableComparator;
import com.cp.common.util.sort.QuickSort;
import com.cp.common.util.sort.SortType;
import com.cp.common.util.sort.Sorter;
import java.util.Comparator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

public class SortAction extends DefaultAction {

  private static final Logger logger = Logger.getLogger(SortAction.class);

  private Comparator comparator = null;

  /**
   * Implements a sort action with the help of a custom Comparator to order the elements of the pre-determined
   * Sortable collection using the specified sorting algorithm.
   * @param mapping the SortActionMapping containing configuration information from the struts config to
   * manipulate the sort action.
   * @param form the SortActionForm containing the Sortable Collection and specifying the sortKey used to
   * order the elements.
   * @param request the HttpServletRequest object encapsulating the clients request.
   * @param response the HttpServletResponse object encapsulating the servers response.
   * @return an ActionForward specifying the next Action to execute.
   * @throws Exception if the execute method is unable to sort the Sortable Collection according to the sortKey
   * using the configured sorting algorithm.
   */
  public ActionForward execute(final CPActionMapping mapping,
                               final CPActionForm form,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
      throws Exception {
    final SortActionForm actionForm = getActionForm(form);
    final SortActionMapping actionMapping = getActionMapping(mapping);

    // Get instance of Comparator.
    final Comparator comparator = getSortActionComparator(actionMapping, actionForm);
    if (logger.isDebugEnabled()) {
      logger.debug("Comparator (" + comparator + ")");
    }

    // Get instance of Sorter.
    final Sorter sorter = getSorter(actionMapping, comparator);
    if (logger.isDebugEnabled()) {
      logger.debug("sorter (" + sorter + ")");
    }

    // Sort the Sortable collection using the configured sorting algorithm.
    sorter.sort(actionForm.getSortable());

    // Reset the SortActionForm instance.
    actionForm.reset(mapping, request);

    return mapping.findForward(getSuccessForward());
  }

  /**
   * Returns the ActionForm as an instance of the SortActionForm.
   * @param form the ActionForm being cast to a SortActionForm.
   * @return a SortActionForm instance.
   */
  protected static SortActionForm getActionForm(final CPActionForm form) {
    if (!(form instanceof SortActionForm)) {
      logger.warn("The ActionForm must be of type SortActionForm.");
      throw new IllegalArgumentException("The ActionForm must be of type SortActionForm.");
    }
    return (SortActionForm) form;
  }

  /**
   * Returns the ActionMapping as an instance of the SortActionMapping.
   * @param mapping the ActionMapping being cast to a SortActionMapping.
   * @return a SortActionMapping instance.
   */
  protected static SortActionMapping getActionMapping(final CPActionMapping mapping) {
    if (!(mapping instanceof SortActionMapping)) {
      logger.warn("The ActionMapping must be of type SortActionMapping.");
      throw new IllegalArgumentException("The ActionMapping must be of type SortActionMapping.");
    }
    return (SortActionMapping) mapping;
  }

  /**
   * Returns the configured Comparator or SortActionComparator instance used to order the elements of the
   * specified Sortable Collection.
   * @param mapping the SortActionMapping containing configuration information about the Comparator class.
   * @param form the SortActionForm containing the sortKey used by the SortActionComparator to determine
   * which property of the elements in the Sortable Collection to order by.
   * @return a Comparator instance used to order the elements in the Sortable Collection.
   */
  public Comparator getSortActionComparator(final SortActionMapping mapping, final SortActionForm form) {
    if (ObjectUtil.isNull(comparator)) {
      try {
        comparator = (Comparator) ClassUtil.getInstance(mapping.getComparator());
        if (comparator instanceof SortActionComparator) {
          logger.debug("The comparator (" + comparator.getClass().getName() + " is an instance of SortActionComparator!");
          ((SortActionComparator) comparator).setSortKey(form.getSortKey());
        }
      }
      catch (Exception warn) {
        logger.warn("Failed to create an instance of Comparator (" + mapping.getComparator() + ")!");
        comparator = new ComparableComparator();
        logger.info("Defaulting Comparator to " + comparator.getClass().getName());

      }
    }
    return comparator;
  }

  /**
   * Returns the Sorter instance used the sort the specified Sortable Collection.
   * @param mapping the SortActionMapping containing configuration information on the Sorter class used
   * to perform the sort.
   * @param orderBy the Comparator object used to order the elements in the specified Sortable Collection.
   * @return a Sorter instance used to perform the sort.
   */
  public Sorter getSorter(final SortActionMapping mapping, final Comparator orderBy) {
    final String sortAlgorithm = mapping.getSortAlgorithm();
    if (logger.isDebugEnabled()) {
      logger.debug("sortAlgorithm (" + sortAlgorithm + ")");
    }

    // First, determine if the sortAlgorithm property refers to a class.
    try {
      final Class sortAlgorithmClass = Class.forName(sortAlgorithm);
      if (!Sorter.class.isAssignableFrom(sortAlgorithmClass)) {
        throw new IllegalArgumentException("The sortAlgorithmClass (" + sortAlgorithmClass.getName() + ") is not of type Sorter!");
      }
      logger.info("The sortAlgorithm (" + sortAlgorithm + ") is an instance of " + Sorter.class.getName() + "!");
      return (Sorter) ClassUtil.getInstance(sortAlgorithmClass, new Object[] { orderBy });
    }
    // Else, treat the sortAlgorithm property as a description of the sort algorithm specifying the SortType and use it
    // to call the factory method and get the Sorter class.
    catch (Exception e) {
      logger.warn(e);
      final SortType sortType = SortType.getSortType(sortAlgorithm);
      if (ObjectUtil.isNull(sortType)) {
        return new QuickSort(orderBy);
      }
      // TODO use a factory to get the sorting algorithm, instance of the Sorter class.
      return new QuickSort(orderBy);
    }
  }

}
