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
package com.dremio.exec.planner.logical;

import com.dremio.common.expression.FunctionCall;
import com.dremio.common.expression.LogicalExpression;
import java.util.List;

public class ExprHelper {
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ExprHelper.class);

  private static final String COMPOUND_FAIL_MESSAGE =
      "The current Optiq based logical plan interpreter does not complicated expressions.  For Order By and Filter";

  public static String getAggregateFieldName(FunctionCall c) {
    List<LogicalExpression> exprs = c.args;
    if (exprs.size() != 1) {
      throw new UnsupportedOperationException(COMPOUND_FAIL_MESSAGE);
    }
    return getFieldName(exprs.iterator().next());
  }

  public static String getFieldName(LogicalExpression e) {
    // if(e instanceof SchemaPath) return ((SchemaPath) e).getPath().toString();
    throw new UnsupportedOperationException(COMPOUND_FAIL_MESSAGE);
  }
}
