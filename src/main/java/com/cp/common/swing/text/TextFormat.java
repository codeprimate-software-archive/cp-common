/*
 * TextFormat.java (c) 20 July 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.5
 */

package com.cp.common.swing.text;

import javax.swing.text.Document;

public interface TextFormat {

  /**
   * Verifies that the text that will be inserted into the JTextField component has a valid format and performs
   * any other mutations on the text as determined by the validation and formatting rules specified by instances
   * of this interface.
   * @param doc the Document object representing the model used by the JTextField component to format and verify
   * input.
   * @param offset is an integer value specifying the offset into the Document.
   * @param text a String object containing the text to format and validate before inserting into the
   * JTextField component.
   * @throws com.cp.common.swing.text.InvalidTextFormatException if the text format is not valid input to the
   * text field component.
   */
  public String format(Document doc, int offset, String text) throws InvalidTextFormatException;

}
