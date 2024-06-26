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

package com.dremio;

import org.junit.Ignore;
import org.junit.Test;

public class TestMergeFilterPlan extends PlanTestBase {

  @Test
  @Ignore
  public void testFilterMerge() throws Exception {
    String viewDDL =
        "create or replace view MyViewWithFilter as "
            + " SELECT  first_name, "
            + "         last_name, "
            + "         full_name, "
            + "         salary, "
            + "         employee_id, "
            + "         store_id, "
            + "         position_id, "
            + "         position_title, "
            + "         education_level "
            + " FROM cp.\"employee.json\" "
            + " WHERE position_id in (1, 2, 3 ) ";

    String querySQL =
        " select dat.store_id\n"
            + "      , sum(dat.store_cost) as total_cost\n"
            + " from (\n"
            + "  select store_id, position_id\n"
            + "            , sum( salary) as store_cost\n"
            + "       from MyViewWithFilter \n"
            + " where full_name in ( select n_name\n"
            + "                      from cp.\"tpch/nation.parquet\")\n"
            + "  and  education_level = 'GRADUATE DEGREE' "
            + "  and position_id in ( select position_id \n"
            + "                       from MyViewWithFilter\n"
            + "                        where position_title like '%VP%'\n"
            + "                      )\n"
            + "  group by store_id, position_id\n"
            + ") dat\n"
            + "group by dat.store_id\n"
            + "order by dat.store_id";

    String expectedPattern1 =
        "Filter(condition=[AND(OR(=($1, 1), =($1, 2), =($1, 3)), =($4, 'GRADUATE DEGREE'))])";
    String expectedPattern2 =
        "Filter(condition=[AND(OR(=($0, 1), =($0, 2), =($0, 3)), LIKE($1, '%VP%'))])";
    String excludedPattern = "Filter(condition=[OR(=($0, 1), =($0, 2), =($0, 3))])";

    test("use dfs_test.tmp");

    test(viewDDL);

    testPlanSubstrPatterns(
        querySQL,
        new String[] {expectedPattern1, expectedPattern2},
        new String[] {excludedPattern});

    test("drop view MyViewWithFilter ");
  }

  @Test
  public void filterMergeAlwaysFalse() throws Exception {
    final String query =
        "SELECT \"integer\" FROM (SELECT \"integer\" FROM cp.\"jsoninput/input2.json\" "
            + "WHERE \"integer\" > 1) WHERE \"integer\" IS NULL";

    testPlanSubstrPatterns(query, new String[] {"Empty"}, new String[] {"Values"});

    testBuilder().sqlQuery(query).expectsEmptyResultSet().go();
  }
}
