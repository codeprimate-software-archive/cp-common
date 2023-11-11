/*
 * Parameterizable.java (c) 28 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.17
 * @com.cp.common.beans.definition.InvocationArgument
 */

package com.cp.common.beans.definition;

import java.util.List;

public interface Parameterizable {

  /**
   * Adds the specified InvocationArgument, modeling a parameter to a constructor or method argument on the object
   * implementing this interface.
   * @param invocationArgument an InvocationArgument modeling a parameter to a contructor or method argument of the
   * implementing object.
   * @return a boolean value indicating whether the specified InvocationArgument was successfully added to the
   * list of constructor or method arguments of the implementing object.
   * @throws NullPointerException if the InvocationArgument parameter is null!
   * @see Parameterizable#remove(InvocationArgument)
   */
  public boolean add(InvocationArgument invocationArgument);

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
  public InvocationArgument getInvocationArgument(int index);

  /**
   * Gets the ordered list of InvocationArguments for the constructor or method of the implementing object.
   * @return an ordered List of constructor or method InvocationArgument objects.
   * @see Parameterizable#getInvocationArgument(int)
   */
  public List<InvocationArgument> getInvocationArguments();

  /**
   * Gets a Class array containing the types of the arguments to the constructor or method invocation of the
   * implementing object.
   * @return a Class array containing the types of the constructor or method arguments.
   * @see Parameterizable#getInvocationArgumentValues()
   */
  public Class[] getInvocationArgumentTypes();

  /**
   * Gets an Object array containing the values of the arguments to the constructor or method invocation of the
   * implementing object.
   * @return an Object array containing the values of the constructor or method arguments.
   * @see Parameterizable#getInvocationArgumentTypes()
   */
  public Object[] getInvocationArgumentValues();

  /**
   * Removes the specified InvocationArgument, modeling a parameter to a constructor or method argument on the object
   * implementing this interface.
   * @param invocationArgument an InvocationArgument modeling a parameter to a contructor or method argument of the
   * implementing object.
   * @return a boolean value indicating whether the specified InvocationArgument was successfully removed from the
   * list of constructor or method arguments of the implementing object.
   * @see Parameterizable#add(InvocationArgument)
   */
  public boolean remove(InvocationArgument invocationArgument);

}
