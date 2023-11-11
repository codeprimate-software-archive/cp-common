/*
 * BatchProcess.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 */

package com.cp.common.batch;

import com.cp.common.lang.Initializable;

public interface BatchProcess extends Initializable<BatchContext> {

  /**
   * Inokes and runs the batch process.
   */
  public void run();

  /**
   * Finalizes the batch process by performing any cleanup tasks, releasing resources, flushing of log files,
   * and committing or rollback of any transactions
   * @param context the BatchContext object encapsulating configuration information and access to shared resources
   * used by the various threads of execution throughout the batch process.
   */
  public void finalize(BatchContext context);

}
