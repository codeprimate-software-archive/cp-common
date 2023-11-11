/*
 * AbstractParameterHandler.java (c) 28 4 2008 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.21
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.InvocationArgument
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Assert;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractParameterHandler implements Parameterizable {

  protected final Log logger = LogFactory.getLog(getClass());

  private final List<InvocationArgument> invocationArgumentList;

  /**
   * Creates an intance of the AbstractParameterHandler class
   */
  public AbstractParameterHandler() {
    invocationArgumentList = getInvocationArgumentListImpl();
  }

  /**
   * Gets the List of InvocationArguments.
   * @return a List object containing an ordered collection of InvocationArgument objects.
   */
  protected List<InvocationArgument> getInvocationArgumentList() {
    return invocationArgumentList;
  }

  /**
   * Gets the List implementation used to keep an ordered collection of InvocationArguments.
   * @return a List implementation to store the ordered collection of InvocationArguments.
   */
  protected List<InvocationArgument> getInvocationArgumentListImpl() {
    return new LinkedList<InvocationArgument>();
  }

  /**
   * Adds the specified InvocationArgument, modeling a parameter to a constructor or method argument on the object
   * implementing this interface.
   * @param invocationArgument an InvocationArgument modeling a parameter to a contructor or method argument of the
   * implementing object.
   * @return a boolean value indicating whether the specified InvocationArgument was added to the
   * list of constructor or method arguments of the implementing object.
   * @throws NullPointerException if the InvocationArgument parameter is null!
   * @see Parameterizable#remove(InvocationArgument)
   */
  public boolean add(final InvocationArgument invocationArgument) {
    Assert.notNull(invocationArgument,  "The invocation argument being added to the list of constructor/method parameters cannot be null!");
    return getInvocationArgumentList().add(invocationArgument);
  }

  /**
   * Gets the InvocationArgument in the ordered list of constructor or methods arguments for this implementing object
   * using the specified index.
   * @param index an integer index specifying the InvocationArgument in the ordered list of constructor or method
   * arguments to return.
   * @return an InvocationArgument at the specified index in the ordered list of constructor or method arguments for the
   * implementing object.
   * @throws IndexOutOfBoundsException if the index does not reference an InvocationArgument in the list of constructor
   * or method arguments.
   * @see Parameterizable#getInvocationArguments()
   */
  public InvocationArgument getInvocationArgument(final int index) {
    return getInvocationArgumentList().get(index);
  }

  /**
   * Gets the ordered list of InvocationArguments for the constructor or method of the implementing object.
   * @return an ordered List of constructor or method InvocationArgument objects.
   * @see Parameterizable#getInvocationArgument(int)
   */
  public List<InvocationArgument> getInvocationArguments() {
    return Collections.unmodifiableList(getInvocationArgumentList());
  }

  /**
   * Gets a Class array containing the types of the arguments to the constructor or method invocation of the
   * implementing object.
   * @return a Class array containing the types of the constructor or method arguments.
   * @see Parameterizable#getInvocationArgumentValues()
   */
  public Class[] getInvocationArgumentTypes() {
    final List<InvocationArgument> invocationArguments = getInvocationArgumentList();
    final Class[] invocationArgumentTypes = new Class[invocationArguments.size()];
    int index = 0;

    for (final InvocationArgument invocationArgument : invocationArguments) {
      invocationArgumentTypes[index++] = invocationArgument.getType();
    }

    return invocationArgumentTypes;
  }

  /**
   * Gets an Object array containing the values of the arguments to the constructor or method invocation of the
   * implementing object.
   * @return an Object array containing the values of the constructor or method arguments.
   * @see Parameterizable#getInvocationArgumentTypes()
   */
  public Object[] getInvocationArgumentValues() {
    final List<InvocationArgument> invocationArguments = getInvocationArgumentList();
    final Object[] invocationArgumentValues = new Object[invocationArguments.size()];
    int index = 0;

    for (final InvocationArgument invocationArgument : invocationArguments) {
      invocationArgumentValues[index++] = invocationArgument.getValue();
    }

    return invocationArgumentValues;
  }

  /**
   * Removes the specified InvocationArgument, modeling a parameter to a constructor or method argument on the object
   * implementing this interface.
   * @param invocationArgument an InvocationArgument modeling a parameter to a contructor or method argument of the
   * implementing object.
   * @return a boolean value indicating whether the specified InvocationArgument was successfully removed from the
   * list of constructor or method arguments of the implementing object.
   * @see Parameterizable#add(InvocationArgument)
   */
  public boolean remove(final InvocationArgument invocationArgument) {
    return getInvocationArgumentList().remove(invocationArgument);
  }

}
