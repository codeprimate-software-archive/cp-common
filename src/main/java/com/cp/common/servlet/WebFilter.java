/*
 * WebFilter.java (c) 23 February 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.2.23
 */

package com.cp.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebFilter {

  public boolean accept(HttpServletRequest request, HttpServletResponse response);

}
