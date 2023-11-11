/*
 * AbstractAboutInfo.java (c) 8 August 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.5
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.awt.Image;
import java.net.URL;
import org.apache.log4j.Logger;

public abstract class AbstractAboutInfo implements AboutInfo {

  private static final Logger logger = Logger.getLogger(AbstractAboutInfo.class);

  private Image companyLogo;
  private Image productLogo;

  private String companyName;
  private String licensee;
  private String productName;
  private String versionNumber;

  private URL companyURL;

  public Image getCompanyLogo() {
    return companyLogo;
  }

  public void setCompanyLogo(final Image companyLogo) {
    this.companyLogo = companyLogo;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(final String companyName) {
    logger.debug("companyName (" + companyName + ")");
    this.companyName = companyName;
  }

  public URL getCompanyURL() {
    return companyURL;
  }

  public void setCompanyURL(final URL companyURL) {
    logger.debug("companyURL (" + companyURL.toExternalForm() + ")");
    this.companyURL = companyURL;
  }

  public String getLicensee() {
    return licensee;
  }

  public void setLicensee(final String licensee) {
    logger.debug("licensee (" + licensee + ")");
    this.licensee = licensee;
  }

  public Image getProductLogo() {
    return productLogo;
  }

  public void setProductLogo(final Image productLogo) {
    this.productLogo = productLogo;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(final String productName) {
    logger.debug("productName (" + productName + ")");
    this.productName = productName;
  }

  public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(final String versionNumber) {
    logger.debug("versionNumber (" + versionNumber + ")");
    this.versionNumber = versionNumber;
  }

  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof AbstractAboutInfo)) {
      return false;
    }

    final AbstractAboutInfo that = (AbstractAboutInfo) obj;

    return ObjectUtil.equals(getCompanyName(), that.getCompanyName())
      && ObjectUtil.equals(getCompanyURL(), that.getCompanyURL())
      && ObjectUtil.equals(getLicensee(), that.getLicensee())
      && ObjectUtil.equals(getProductName(), that.getProductName())
      && ObjectUtil.equals(getVersionNumber(), that.getVersionNumber());
  }

  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getCompanyName());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getCompanyURL());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getLicensee());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getProductName());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getVersionNumber());
    return hashValue;
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer("{companyName = ");
    buffer.append(getCompanyName());
    buffer.append(", companyURL = ").append(getCompanyURL());
    buffer.append(", licensee = ").append(getLicensee());
    buffer.append(", productName = ").append(getProductName());
    buffer.append(", versionNumber = ").append(getVersionNumber());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
