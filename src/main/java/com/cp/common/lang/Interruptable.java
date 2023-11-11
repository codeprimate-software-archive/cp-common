package com.cp.common.lang;

/**
 * The Interruptable class is...
 * <p/>
 * Interruptable.java (c) 09 December 2010
 * @author jblum
 * @version $Revision: 1.1 $
 */
public interface Interruptable {

  public boolean isInterrupted();

  public void interrupt();

}
