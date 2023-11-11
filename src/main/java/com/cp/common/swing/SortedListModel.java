/*
 * SortedListModel.java (c) 8 August 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.10
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.apache.log4j.Logger;

public class SortedListModel implements ListModel {

  private static final Logger logger = Logger.getLogger(SortedListModel.class);

  private Comparator listModelComparator;

  private List<Integer> indexList;

  private final ListModel listModel;

  public SortedListModel(final ListModel listModel, final Comparator orderBy) {
    if (ObjectUtil.isNull(listModel)) {
      logger.warn("The source ListModel object cannot be null!");
      throw new NullPointerException("The source ListModel object cannot be null!");
    }

    if (ObjectUtil.isNull(orderBy)) {
      logger.warn("The Comparator specifying sort order for the ListModel elements cannot be null!");
      throw new NullPointerException("The Comparator specifying sort order for the ListModel elements cannot be null!");
    }

    this.listModel = listModel;
    this.listModel.addListDataListener(new ListModelDataListener());
    listModelComparator = orderBy;
    indexList = new ArrayList<Integer>(listModel.getSize());

    for (int index = 0, length = listModel.getSize(); index < length; index++) {
      indexList.add(index);
    }

    Collections.sort(indexList, new IndexListComparator());
  }

  public void addListDataListener(final ListDataListener l) {
    getListModel().addListDataListener(l);
  }

  public Object getElementAt(final int index) {
    return getListModel().getElementAt(getIndexList().get(index));
  }

  public int getSize() {
    return getListModel().getSize();
  }

  public void removeListDataListener(final ListDataListener l) {
    getListModel().removeListDataListener(l);
  }

  protected List<Integer> getIndexList() {
    return indexList;
  }

  protected final ListModel getListModel() {
    return listModel;
  }

  public Comparator getOrderBy() {
    return listModelComparator;
  }

  public void setOrderBy(final Comparator orderBy) {
    if (ObjectUtil.isNull(orderBy)) {
      logger.warn("The Comparator specifying sort order for the ListModel elements cannot be null!");
      throw new NullPointerException("The Comparator specifying sort order for the ListModel elements cannot be null!");
    }

    if (!this.listModelComparator.equals(orderBy)) {
      this.listModelComparator = orderBy;
      handleModelChange();
    }
  }

  private void handleModelChange() {
    if (getIndexList().size() != getSize()) {
      initIndexList();
    }

    Collections.sort(getIndexList(), new IndexListComparator());
  }

  protected void initIndexList() {
    indexList = new ArrayList<Integer>(getSize());

    for (int index = 0, size = getSize(); index < size; index++) {
      indexList.add(index);
    }
  }

  private final class IndexListComparator implements Comparator<Integer> {

    public int compare(final Integer index0, final Integer index1) {
      final Object element0 = getListModel().getElementAt(index0);
      final Object element1 = getListModel().getElementAt(index1);
      return getOrderBy().compare(element0, element1);
    }
  }

  private final class ListModelDataListener implements ListDataListener {

    public void contentsChanged(final ListDataEvent e) {
      handleModelChange();
    }

    public void intervalAdded(final ListDataEvent e) {
      handleModelChange();
    }

    public void intervalRemoved(final ListDataEvent e) {
      handleModelChange();
    }
  }

}
