/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.jdbc;

import com.dremio.jdbc.impl.DriverImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

/** Main class of Dremio JDBC driver. */
public class Driver implements java.sql.Driver {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Driver.class);

  /** Delegate for everything except registration with DriverManager. */
  private final DriverImpl impl;

  // The following should be the last static initialization, so that any other
  // static initialization is completed before we create an instance and let
  // DriverManager access it:

  static {
    // Special code for supporting Java9 and higher.
    // Netty requires some extra properties to unlock some native memory management api
    // Dremio require in order to process data coming from the server
    // Setting this property if not already set externally
    // This has to be done before any netty class is being loaded
    final String key = "io.netty.tryReflectionSetAccessible";
    final String tryReflectionSetAccessible = System.getProperty(key);
    if (tryReflectionSetAccessible == null) {
      logger.info("Enabling Netty native memory API for Java9+");
      System.setProperty(key, Boolean.TRUE.toString());
    } else {
      logger.info("{} key has been set to '{}'", key, tryReflectionSetAccessible);
    }

    // Upon loading of class, register an instance with DriverManager.
    try {
      DriverManager.registerDriver(new Driver());
    } catch (Error | SQLException e) {
      logger.warn("Error in registering Dremio JDBC driver {}: {}", Driver.class, e, e);
    }
  }

  /**
   * Ensures that class is loaded.
   *
   * <p>(Avoids extra instance of calling {@code new Driver();}; avoids verbosity of {@code
   * Class.forName("com.dremio.jdbc.Driver");}.)
   */
  public static boolean load() {
    return true;
  }

  public Driver() {
    impl = new DriverImpl();
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    return impl.connect(url, info);
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return impl.acceptsURL(url);
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return impl.getPropertyInfo(url, info);
  }

  @Override
  public int getMajorVersion() {
    return impl.getMajorVersion();
  }

  @Override
  public int getMinorVersion() {
    return impl.getMinorVersion();
  }

  @Override
  public boolean jdbcCompliant() {
    return impl.jdbcCompliant();
  }

  @Override
  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return impl.getParentLogger();
  }
}
