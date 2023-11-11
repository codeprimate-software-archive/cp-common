/*
 * AboutInfo.java (c) 8 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.23
 */

package com.cp.common.util;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;

public interface AboutInfo extends Serializable {

  public static final char COPYRIGHT_SYMBOL = '\u00A9';
  public static final String ALL_RIGHTS_RESERVED = "All rights reserved.";

  /**
   * Returns the image for the company logo.
   * @return and Image object of the company logo.
   */
  public Image getCompanyLogo();

  /**
   * Sets the image for the company logo.
   * @param companyLogo is an Image object representing the company logo.
   */
  public void setCompanyLogo(Image companyLogo);

  /**
   * Returns the name of the company.
   * @return a String value for the name of the company.
   */
  public String getCompanyName();

  /**
   * Sets the name of the company.
   * @param companyName is a String value specifying the name of the company.
   */
  public void setCompanyName(String companyName);

  /**
   * Returns a uniform resource locator referring to the website of the company.
   * @return a URL object referencing the company's website.
   */
  public URL getCompanyURL();

  /**
   * Sets the uniform resource locator referring to the company's website.
   * @param companyURL is a URL object referring to the company's website.
   */
  public void setCompanyURL(URL companyURL);

  /**
   * Returns legal information pertaining to copyrighted material.  This information is usually
   * derived from the other properties of this class, such as companyName, etc.
   * @return a String value representing the copyright.
   */
  public String getCopyright();

  /**
   * Returns infomration about the End User License Agreement.
   * @return a String value containing the end user license agreement.
   */
  public String getEULA();

  /**
   * Returns licensee information for the license holder of the product.
   * @return a String value specifying the license holder of the product.
   */
  public String getLicensee();

  /**
   * Sets the license holder of the product.
   * @param licensee is String value specifying the licensee holder of the product.
   */
  public void setLicensee(String licensee);

  /**
   * Returns an image of the product, or a brand logo for the product.
   * @return an Image object representing the brand logo of the product.
   */
  public Image getProductLogo();

  /**
   * Sets the image used for the brand logo of the product.
   * @param productLogo is an Image object representing the brand logo of the product.
   */
  public void setProductLogo(Image productLogo);

  /**
   * Returns the name of the product.
   * @return a String value specifying the name of the product.
   */
  public String getProductName();

  /**
   * Sets the name of the product.
   * @param productName is a String value specifying the name of the product.
   */
  public void setProductName(String productName);

  /**
   * Returns the product version number.
   * @return a String value specifying the product version number.
   */
  public String getVersionNumber();

  /**
   * Sets the product version number.
   * @param versionNumber is a String value representing the product's version number.
   */
  public void setVersionNumber(String versionNumber);

}
