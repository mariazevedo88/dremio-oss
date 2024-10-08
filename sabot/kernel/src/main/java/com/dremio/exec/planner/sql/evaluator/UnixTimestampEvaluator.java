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
package com.dremio.exec.planner.sql.evaluator;

import com.dremio.sabot.exec.context.ContextInformation;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.type.SqlTypeName;

public final class UnixTimestampEvaluator implements FunctionEval {
  public static final UnixTimestampEvaluator INSTANCE = new UnixTimestampEvaluator();

  private UnixTimestampEvaluator() {}

  @Override
  public RexNode evaluate(EvaluationContext cx, RexCall call) {
    ContextInformation contextInformation = cx.getContextInformation();
    RexBuilder rexBuilder = cx.getRexBuilder();
    long value = contextInformation.getQueryStartTime() / 1000;
    RelDataType bigintType = rexBuilder.getTypeFactory().createSqlType(SqlTypeName.BIGINT);
    return rexBuilder.makeLiteral(value, bigintType);
  }
}
