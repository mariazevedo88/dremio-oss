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

/** {@link JdbcApiSqlException} for invalid-parameter-value conditions. */
public class InvalidParameterSqlException extends JdbcApiSqlException {

  private static final long serialVersionUID = 2015_05_05L;

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String, String, int)}. */
  public InvalidParameterSqlException(String reason, String SQLState, int vendorCode) {
    super(reason, SQLState, vendorCode);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String, String)}. */
  public InvalidParameterSqlException(String reason, String SQLState) {
    super(reason, SQLState);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String)}. */
  public InvalidParameterSqlException(String reason) {
    super(reason);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(Throwable cause)}. */
  public InvalidParameterSqlException(Throwable cause) {
    super(cause);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String, Throwable)}. */
  public InvalidParameterSqlException(String reason, Throwable cause) {
    super(reason, cause);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String, String, Throwable)}. */
  public InvalidParameterSqlException(String reason, String sqlState, Throwable cause) {
    super(reason, sqlState, cause);
  }

  /** See {@link JdbcApiSqlException#JdbcApiSqlException(String, String, int, Throwable)}. */
  public InvalidParameterSqlException(
      String reason, String sqlState, int vendorCode, Throwable cause) {
    super(reason, sqlState, vendorCode, cause);
  }
}
