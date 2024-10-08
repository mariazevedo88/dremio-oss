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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dremio-specific {@link ResultSet}.
 *
 * @see #unwrap
 */
public interface DremioResultSet extends ResultSet {

  /**
   * Gets the ID of the associated query (the query whose results this ResultSet presents).
   *
   * @throws SQLException if this method is called on a closed result set
   */
  String getQueryId() throws SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: Accepts {@code DremioResultSet.class}.
   */
  @Override
  <T> T unwrap(Class<T> iface) throws SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: Returns true for {@code DremioResultSet.class}.
   */
  @Override
  boolean isWrapperFor(Class<?> iface) throws SQLException;

  // Note:  The commented-out methods are left in to make it easier to match
  // the method order from ResultSet when adding method declarations at this
  // level (e.g., to document Dremio-specific behavior for more) in the future
  // (so the resulting documentation page matches the order in
  // java.sql.ResultSet's page).

  // (Temporary, re matching ResultSet's method order:
  // next()
  // close()
  // wasNull()
  // )

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from all types.
   */
  @Override
  String getString(int columnIndex) throws SQLException;

  // (Temporary, re matching ResultSet's method order:)
  // getBoolean(int)
  // )

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code SMALLINT} ({@code short}), {@code INTEGER} ({@code int}), and {@code BIGINT}
   *       ({@code long})
   *   <li>{@code REAL} ({@code float}), {@code DOUBLE PRECISION} ({@code double}), and {@code
   *       FLOAT} ({@code float} or {@code double})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code byte} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     byte}
   */
  @Override
  byte getByte(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code INTEGER} ({@code int}), and {@code BIGINT} ({@code
   *       long})
   *   <li>{@code REAL} ({@code float}), {@code DOUBLE PRECISION} ({@code double}), and {@code
   *       FLOAT} ({@code float} or {@code double})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code short} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     short}
   */
  @Override
  short getShort(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code SMALLINT} ({@code short}), and {@code BIGINT}
   *       ({@code long})
   *   <li>{@code REAL} ({@code float}), {@code DOUBLE PRECISION} ({@code double}), and {@code
   *       FLOAT} ({@code float} or {@code double})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code int} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     int}
   */
  @Override
  int getInt(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code SMALLINT} ({@code short}), and {@code INTEGER}
   *       ({@code int})
   *   <li>{@code REAL} ({@code float}), {@code DOUBLE PRECISION} ({@code double}), and {@code
   *       FLOAT} ({@code float} or {@code double})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code long} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     long}
   */
  @Override
  long getLong(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code SMALLINT} ({@code short}), and {@code INTEGER}
   *       ({@code int}), {@code BIGINT} ({@code long})
   *   <li>{@code DOUBLE PRECISION} ({@code double}) and {@code FLOAT} (when {@code double})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code float} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     float}
   */
  @Override
  float getFloat(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code SMALLINT} ({@code short}), {@code INTEGER} ({@code
   *       int}), and {@code BIGINT} ({@code long})
   *   <li>{@code REAL} ({@code float}), {@code FLOAT} (when {@code float})
   *   <li>{@code DECIMAL} ({@code BigDecimal})
   * </ul>
   *
   * <p>Conversion throws {@link SQLConversionOverflowException} for a source value whose magnitude
   * is outside the range of {@code double} values.
   *
   * @throws SQLConversionOverflowException if a source value was too large to convert to {@code
   *     double}
   */
  @Override
  double getDouble(int columnIndex) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from types:
   *
   * <ul>
   *   <li>{@code TINYINT} ({@code byte}), {@code SMALLINT} ({@code short}), {@code INTEGER} ({@code
   *       int}), and {@code BIGINT} ({@code long})
   *   <li>{@code REAL} ({@code float}), {@code DOUBLE PRECISION} ({@code double}), and {@code
   *       FLOAT} ({@code float} or {@code double})
   * </ul>
   */
  @Override
  BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException;

  // (Temporary, re matching ResultSet's method order:)
  // getBytes(int)
  // getDate(int)
  // getTime(int)
  // getTimestamp(int)
  // getAsciiStream(int)
  // getUnicodeStream(int)
  // getBinaryStream(int)
  // )

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getString(int)}.
   */
  @Override
  String getString(String columnLabel) throws SQLException;

  // (Temporary, re matching ResultSet's method order:)
  // getBoolean(String)
  // )

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getByte(int)}.
   */
  @Override
  byte getByte(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getShort(int)}.
   */
  @Override
  short getShort(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getInt(int)}.
   */
  @Override
  int getInt(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getLong(int)}.
   */
  @Override
  long getLong(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getFloat(int)}.
   */
  @Override
  float getFloat(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getDouble(int)}.
   */
  @Override
  double getDouble(String columnLabel) throws SQLConversionOverflowException, SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getBigDecimal(int)}.
   */
  @Override
  BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException;

  // (Temporary, re matching ResultSet's method order:)
  // getBytes(String)
  // getDate(String)
  // getTime(String)
  // getTimestamp(String)
  // getAsciiStream(String)
  // getUnicodeStream(String)
  // getBinaryStream(String)
  // getWarnings()
  // clearWarnings()
  // getCursorName()
  // getMetaData()
  // )

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio: Conversions</strong>: Supports conversion from all types.
   */
  @Override
  Object getObject(int columnIndex) throws SQLException;

  /**
   * {@inheritDoc}
   *
   * <p><strong>Dremio</strong>: For conversions, see {@link DremioResultSet#getObject(int)}.
   */
  @Override
  Object getObject(String columnLabel) throws SQLException;

  // (Temporary, re matching ResultSet's method order:)
  // findColumn(String)
  // getCharacterStream(int)
  // getCharacterStream(String)
  // getBigDecimal(int)
  // getBigDecimal(String)
  // isBeforeFirst()
  // isAfterLast()
  // isFirst()
  // isLast()
  // beforeFirst()
  // afterLast()
  // first()
  // last()
  // getRow()
  // absolute(int)
  // relative(int)
  // previous()
  // setFetchDirection(int)
  // getFetchDirection()
  // setFetchSize(int)
  // getFetchSize()
  // getType()
  // getConcurrency()
  // rowUpdated()
  // rowInserted()
  // rowDeleted()
  // updateNull(int)
  // updateBoolean(int, boolean)
  // updateByte(int, byte)
  // updateShort(int, short)
  // updateInt(int, int)
  // updateLong(int, long)
  // updateFloat(int, float)
  // updateDouble(int, double)
  // updateBigDecimal(int, BigDecimal)
  // updateString(int, String)
  // updateBytes(int, byte[])
  // updateDate(int, Date)
  // updateTime(int, Time)
  // updateTimestamp(int, Timestamp)
  // updateAsciiStream(int, InputStream, int)
  // updateBinaryStream(int, InputStream, int)
  // updateCharacterStream(int, Reader, int)
  // updateObject(int, Object, int)
  // updateObject(int, Object)
  // updateNull(String)
  // updateBoolean(String, boolean)
  // updateByte(String, byte)
  // updateShort(String, short)
  // updateInt(String, int)
  // updateLong(String, long)
  // updateFloat(String, float)
  // updateDouble(String, double)
  // updateBigDecimal(String, BigDecimal)
  // updateString(String, String)
  // updateBytes(String, byte[])
  // updateDate(String, Date)
  // updateTime(String, Time)
  // updateTimestamp(String, Timestamp)
  // updateAsciiStream(String, InputStream, int)
  // updateBinaryStream(String, InputStream, int)
  // updateCharacterStream(String, Reader, int)
  // updateObject(String, Object, int)
  // updateObject(String, Object)
  // insertRow()
  // updateRow()
  // deleteRow()
  // refreshRow()
  // cancelRowUpdates()
  // moveToInsertRow()
  // moveToCurrentRow()
  // getStatement()
  // getObject(int, Map<String, Class<?>>)
  // getRef(int)
  // getBlob(int)
  // getClob(int)
  // getArray(int)
  // getObject(String, Map<String, Class<?>>)
  // getRef(String)
  // getBlob(String)
  // getClob(String)
  // getArray(String)
  // getDate(int, Calendar)
  // getDate(String, Calendar)
  // getTime(int, Calendar)
  // getTime(String, Calendar)
  // getTimestamp(int, Calendar)
  // getTimestamp(String, Calendar)
  // getURL(int)
  // getURL(String)
  // updateRef(int, Ref)
  // updateRef(String, Ref)
  // updateBlob(int, Blob)
  // updateBlob(String, Blob)
  // updateClob(int, Clob)
  // updateClob(String, Clob)
  // updateArray(int, Array)
  // updateArray(String, Array)
  // getRowId(int)
  // getRowId(String)
  // updateRowId(int, RowId)
  // updateRowId(String, RowId)
  // getHoldability()
  // isClosed()
  // updateNString(int, String)
  // updateNString(String, String)
  // updateNClob(int, NClob)
  // updateNClob(String, NClob)
  // getNClob(int)
  // getNClob(String)
  // getSQLXML(int)
  // getSQLXML(String)
  // updateSQLXML(int, SQLXML)
  // updateSQLXML(String, SQLXML)
  // getNString(int)
  // getNString(String)
  // getNCharacterStream(int)
  // getNCharacterStream(String)
  // updateNCharacterStream(int, Reader, long)
  // updateNCharacterStream(String, Reader, long)
  // updateAsciiStream(int, InputStream, long)
  // updateBinaryStream(int, InputStream, long)
  // updateCharacterStream(int, Reader, long)
  // updateAsciiStream(String, InputStream, long)
  // updateBinaryStream(String, InputStream, long)
  // updateCharacterStream(String, Reader, long)
  // updateBlob(int, InputStream, long)
  // updateBlob(String, InputStream, long)
  // updateClob(int, Reader, long)
  // updateClob(String, Reader, long)
  // updateNClob(int, Reader, long)
  // updateNClob(String, Reader, long)
  // updateNCharacterStream(int, Reader)
  // updateNCharacterStream(String, Reader)
  // updateAsciiStream(int, InputStream)
  // updateBinaryStream(int, InputStream)
  // updateCharacterStream(int, Reader)
  // updateAsciiStream(String, InputStream)
  // updateBinaryStream(String, InputStream)
  // updateCharacterStream(String, Reader)
  // updateBlob(int, InputStream)
  // updateBlob(String, InputStream)
  // updateClob(int, Reader)
  // updateClob(String, Reader)
  // updateNClob(int, Reader)
  // updateNClob(String, Reader)
  // getObject(int, Class<T>)
  // getObject(String, Class<T>)
  // )

}
