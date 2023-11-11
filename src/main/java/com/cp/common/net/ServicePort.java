/*
 * ServicePort.java (c) 8 March 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.1.16
 */

package com.cp.common.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public final class ServicePort {

  private static final Logger logger = Logger.getLogger(ServicePort.class);

  // Domain Name Service Port
  public static final ServicePort DNS_PORT =
      new ServicePort(53, "DNS", "Domain Name Service");
  // Echo Service Port
  public static final ServicePort ECHO_PORT =
      new ServicePort(7, "ECHO", "Echo Service");
  // Finger Service Port
  public static final ServicePort FINGER_PORT =
      new ServicePort(79, "FINGER", "Finger Service ");
  // File Transfer Protocol Port
  public static final ServicePort FTP_PORT =
      new ServicePort(21, "FTP", "File Transfer Protocol");
  // Hypertext Transfer Protocol Port
  public static final ServicePort HTTP_PORT =
      new ServicePort(80, "HTTP", "Hypertext Transfer Protocol");
  // Network News Transfer Protocol Port
  public static final ServicePort NNTP_PORT =
      new ServicePort(119, "NNTP", "Network News Transfer Protocol");
  // Post Office Protocol 3 Port
  public static final ServicePort POP3_PORT =
      new ServicePort(110, "POP3", "Post Office Protocol 3");
  // Real Streaming Transfer Protocol Port
  public static final ServicePort RTSP_PORT =
      new ServicePort(554, "RTSP", "Real Streaming Transfer Protocol");
  // Simple Mail Protocol Port
  public static final ServicePort SMTP_PORT =
      new ServicePort(25, "SMTP", "Simple Mail Transfer Protocol");
  // Simple Network Management Protocol Port
  public static final ServicePort SNMP_PORT =
      new ServicePort(161, "SNMP", "Simple Network Management Protocol");
  // Simple Network Management Protocol Trap Port
  public static final ServicePort SNMPTrap_PORT =
      new ServicePort(162, "SNMP Trap", "Simple Network Management Protocol Trap");
  // Telnet Service Port
  public static final ServicePort TELNET_PORT =
      new ServicePort(23, "TELNET", "Telnet Service");

  private static final ServicePort[] SERVICE_PORTS = {
    DNS_PORT,
    ECHO_PORT,
    FINGER_PORT,
    FTP_PORT,
    HTTP_PORT,
    NNTP_PORT,
    POP3_PORT,
    RTSP_PORT,
    SMTP_PORT,
    SNMP_PORT,
    SNMPTrap_PORT,
    TELNET_PORT
  };

  private static final List SERVICE_PORT_LIST =
      Collections.unmodifiableList(Arrays.asList(SERVICE_PORTS));

  private final int port;

  private final String description;
  private final String protocol;

  /**
   * Creates an instance of the SevicePort class.  The default
   * constructor is private to enforce non-instantiability of
   * a enumerated type.
   */
  private ServicePort(final int port,
                      final String protocol,
                      final String description) {
    if (logger.isDebugEnabled()) {
      logger.debug("port = " + port);
      logger.debug("protocol = " + protocol);
      logger.debug("description = " + description);
    }
    this.port = port;
    this.protocol = protocol;
    this.description = description;
  }

  /**
   * Attempts to lookup a service by it's port number return either
   * a valid ServicePort object or null if no enumerated ServicePort
   * object has a port number equal to the port parameter.
   */
  public static final ServicePort findByPort(final int port) {
    logger.debug("port = " + port);
    for (Iterator it = SERVICE_PORT_LIST.iterator(); it.hasNext();) {
      final ServicePort servicePort = (ServicePort) it.next();
      if (logger.isDebugEnabled()) {
        logger.debug("servicePort: " + servicePort);
      }
      if (servicePort.getPort() == port) {
        return servicePort;
      }
    }
    logger.warn("Service with port number (" + port + ") could not be found!");
    return null;
  }

  /**
   * Returns a description of the protocol.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the port number used by the protocol.
   */
  public int getPort() {
    return port;
  }

  /**
   * Returns the acronym of the protocol.
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Returns a list of service ports.
   */
  public static final List getServicePorts() {
    return SERVICE_PORT_LIST;
  }

  /**
   * Compares this ServicePort to the Object parameter for
   * equality.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ServicePort)) {
      return false;
    }

    final ServicePort that = (ServicePort) obj;

    return (getPort() == that.getPort())
        && (getProtocol().equals(that.getProtocol()))
        && (getDescription().equals(that.getDescription()));
  }

  /**
   * Computes a hash code value for this ServicePort.
   */
  public int hashCode() {
    int result = 17;
    result = 37 * result + getPort();
    result = 37 * result + getProtocol().hashCode();
    result = 37 * result + getDescription().hashCode();
    return result;
  }

  /**
   * Returns a String representation of the ServicePort.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{port = ");
    buffer.append(getPort());
    buffer.append(", protocol = ").append(getProtocol());
    buffer.append(", description = ").append(getDescription());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
