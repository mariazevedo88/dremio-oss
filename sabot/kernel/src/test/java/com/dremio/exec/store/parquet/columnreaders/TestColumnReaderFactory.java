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
package com.dremio.exec.store.parquet.columnreaders;

import com.dremio.BaseTestQuery;
import org.junit.Ignore;
import org.junit.Test;

public class TestColumnReaderFactory extends BaseTestQuery {

  /** check if Time and TimeStamp are read correctly with dictionary encoding. */
  @Test
  public void testTimeAndTimeStampWithDictionary() throws Exception {
    // the file 'time_dictionary.parquet' uses a PLAIN_DICTIONARY encoding and contains 4 columns:
    // time_opt: INT32/TIME_MILLIS/OPTIONAL
    // time_req: INT32/TIME_MILLIS/REQUIRED
    // timestampt_opt: INT64/TIMESTAMP_MILLIS/OPTIONAL
    // timestampt_req: INT64/TIMESTAMP_MILLIS/REQUIRED

    // query parquet file. We shouldn't get any exception
    testNoResult("SELECT * FROM cp.\"parquet/time_dictionary.parquet\"");
  }

  /** check if Time and TimeStamp are read correctly with plain encoding. */
  @Test
  public void testTimeAndTimeStampWithNoDictionary() throws Exception {
    // the file 'time_dictionary.parquet' uses a PLAIN encoding and contains 4 columns:
    // time_opt: INT32/TIME_MILLIS/OPTIONAL
    // time_req: INT32/TIME_MILLIS/REQUIRED
    // timestampt_opt: INT64/TIMESTAMP_MILLIS/OPTIONAL
    // timestampt_req: INT64/TIMESTAMP_MILLIS/REQUIRED

    // query parquet file. We shouldn't get any exception
    testNoResult("SELECT * FROM cp.\"parquet/time_nodictionary.parquet\"");
  }

  /** check if Decimal9 and Decimal18 are read correctly with dictionary encoding. */
  @Test
  @Ignore
  public void testDecimal9AndDecimal18WithDictionary() throws Exception {
    // the file 'decimal_dictionary.parquet' uses a PLAIN_DICTIONARY encoding and contains 4
    // columns:
    // d9_opt: INT32/DECIMAL9/OPTIONAL
    // d9_req: INT32/DECIMAL9/REQUIRED
    // d18_opt: INT64/DECIMAL18/OPTIONAL
    // d18_req: INT64/DECIMAL18/REQUIRED

    // query parquet file. We shouldn't get any exception
    testNoResult("SELECT * FROM cp.\"parquet/decimal_dictionary.parquet\"");
  }

  /** check if Decimal9 and Decimal18 are read correctly with plain encoding. */
  @Test
  @Ignore
  public void testDecimal9AndDecimal18WithNoDictionary() throws Exception {
    // the file 'decimal_dictionary.parquet' uses a PLAIN encoding and contains 4 columns:
    // d9_opt: INT32/DECIMAL9/OPTIONAL
    // d9_req: INT32/DECIMAL9/REQUIRED
    // d18_opt: INT64/DECIMAL18/OPTIONAL
    // d18_req: INT64/DECIMAL18/REQUIRED

    // query parquet file. We shouldn't get any exception
    testNoResult("SELECT * FROM cp.\"parquet/decimal_nodictionary.parquet\"");
  }

  /** check if BigInt is read correctly with dictionary encoding. */
  @Test
  public void testBigIntWithDictionary() throws Exception {
    String query = "select sum(ts) as total from cp.\"parquet/bigIntDictionary.parquet\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("total")
        .baselineValues(190928593476806865L)
        .build()
        .run();
  }
}
