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
package com.dremio.exec.nested;

import com.dremio.BaseTestQuery;
import org.junit.Ignore;
import org.junit.Test;

public class TestFastComplexSchema extends BaseTestQuery {

  @Test
  public void test() throws Exception {
    //   test("select r.r_name, t1.f from cp.\"tpch/region.parquet\" r join (select flatten(x) as f
    // from (select convert_from('[0, 1]', 'json') as x from cp.\"tpch/region.parquet\")) t1 on t1.f
    // = r.r_regionkey");
    test(
        "SELECT r.r_name, \n"
            + "       t1.f \n"
            + "FROM   cp.\"tpch/region.parquet\" r \n"
            + "       JOIN (SELECT flatten(x) AS f \n"
            + "             FROM   (SELECT Convert_from('[0, 1]', 'json') AS x \n"
            + "                     FROM   cp.\"tpch/region.parquet\")) t1 \n"
            + "         ON t1.f = cast(r.r_regionkey as bigint)");
  }

  @Test
  public void test2() throws Exception {
    test("alter session set \"planner.enable_hashjoin\" = false");
    test("alter session set \"planner.enable_mergejoin\" = true");
    test("alter session set \"planner.slice_target\" = 1");
    test(
        "SELECT r.r_name, \n"
            + "       t1.f \n"
            + "FROM   cp.\"tpch/region.parquet\" r \n"
            + "       JOIN (SELECT Flatten(x) AS f \n"
            + "             FROM   (SELECT Convert_from('[0, 1]', 'json') AS x \n"
            + "                     FROM   cp.\"tpch/region.parquet\")) t1 \n"
            + "         ON t1.f = cast(r.r_regionkey as bigint) \n"
            + "ORDER  BY r.r_name");
    test("alter session set \"planner.enable_hashjoin\" = true");
    test("alter session set \"planner.enable_mergejoin\" = false");
    test("alter session set \"planner.slice_target\" = 1000000");
  }

  @Test
  @Ignore("schema change")
  public void test3() throws Exception {
    test("alter session set \"planner.enable_hashjoin\" = false");
    test("alter session set \"planner.slice_target\" = 1");
    test(
        "select f from\n"
            + "(select convert_from(nation, 'json') as f from\n"
            + "(select concat('{\"name\": \"', n.n_name, '\", ', '\"regionkey\": ', r.r_regionkey, '}') as nation\n"
            + "       from cp.\"tpch/nation.parquet\" n,\n"
            + "            cp.\"tpch/region.parquet\" r\n"
            + "        where \n"
            + "        n.n_regionkey = r.r_regionkey)) t\n"
            + "order by t.f.name");
  }

  @Test
  @Ignore("schema change")
  public void test4() throws Exception {
    test("alter session set \"planner.enable_hashjoin\" = false");
    test("alter session set \"planner.slice_target\" = 1");
    test(
        "SELECT f \n"
            + "FROM   (SELECT Convert_from(nation, 'json') AS f \n"
            + "        FROM   (SELECT Concat('{\"name\": \"', n.n_name, '\", ', '\"regionkey\": ', \n"
            + "                       r.r_regionkey, \n"
            + "                               '}') AS \n"
            + "                       nation \n"
            + "                FROM   cp.\"tpch/nation.parquet\" n, \n"
            + "                       cp.\"tpch/region.parquet\" r \n"
            + "                WHERE  n.n_regionkey = r.r_regionkey \n"
            + "                       AND r.r_regionkey = 4)) t \n"
            + "ORDER  BY t.f.name");
  }
}
