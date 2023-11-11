/*
 * SourceCodeFileFilter.java (c) 12 May 2003
 *
 * The SourceCodeFileFilter class is used to determine whether or not the specified File object pathname
 * refers to a source code file based on file extension (type) and is accepted by this file filter.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.8
 * @see com.cp.common.io.FileExtensionFileFilter
 */

package com.cp.common.io.support;

import com.cp.common.io.FileExtensionFileFilter;

public class SourceCodeFileFilter extends FileExtensionFileFilter {

  // A String array containing the source code file extensions accepted by this file filter.
  protected static final String[] SUPPORTED_SOURCE_CODE_FILE_EXTENSIONS = {
    "ada",        // Ada
    "asm",        // Assembly Language
    "bas",        // Basic
    "c",          // C
    "cbl",        // Cobol
    "class",      // Class file
    "cpp",        // C++
    "cs",         // C sharp
    "css",        // Cascading Style Sheet
    "dll",        // Dynamic Linked Library
    "dtd",        // Document Type Definition
    "fpp",        // Fortran
    "hh",         // C++ header file
    "html",       // Hypertext Markup Language
    "inc",        // Include file
    "java",       // Java
    "js",         // Javascript
    "lsp",        // Lisp
    "mak",        // Make file
    "mf",         // Java manifest file
    "o",          // Compile object file
    "ocx",        // ActiveX Control
    "pas",        // Pascal
    "properties", // Pascal
    "pl",         // Perl
    "pyd",        // Python
    "rb",         // Ruby
    "sh",         // Bash shell script
    "so",         // Shared library
    "sql",        // Structured Query Language
    "src",        // Source file
    "vb",         // Visual Basic
    "xml",        // Extensible Markup Language
    "xsd",        // XML Schema Definition
  };

  /**
   * Creates an instance of the SourceCodeFileFilter class to filter source code files from the file system.
   */
  public SourceCodeFileFilter() {
    super(SUPPORTED_SOURCE_CODE_FILE_EXTENSIONS);
  }

  /**
   * Creates an instance of the SourceCodeFileFilter class to filter source code files from the file system.
   * @param inclusive a boolean value configuring the filter to include or exclude Files satisfying
   * the criteria of this filter.
   * @param showHidden is a boolean value specifying whether hidden files should be included or filtered by this filter.
   */
  public SourceCodeFileFilter(final boolean inclusive, final boolean showHidden) {
    super(inclusive, showHidden, SUPPORTED_SOURCE_CODE_FILE_EXTENSIONS);
  }

}
