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
package com.dremio.exec.planner;

import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.rel.logical.LogicalJoin;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.tools.RelBuilder;

/**
 * Rewrite filter conditions to avoid NLJ E.g., (t1.x = t2.y AND ….) OR (t1.x = t2.y AND ….) can be
 * rewritten as (t1.x = t2.y) AND (… OR …)
 */
public class ExtractCommonConjunctionInJoinRule extends ExtractCommonConjunctionRule {
  public static final ExtractCommonConjunctionInJoinRule INSTANCE =
      new ExtractCommonConjunctionInJoinRule();

  private ExtractCommonConjunctionInJoinRule() {
    super(operand(LogicalJoin.class, any()), "ExtractCommonConjunctionsInJoin");
  }

  @Override
  public void onMatch(RelOptRuleCall call) {
    LogicalJoin join = call.rel(0);
    RexNode condition = join.getCondition();
    RexNode newCondition =
        new RexConjunctionExtractor(join.getCluster().getRexBuilder()).apply(condition);
    if (condition != newCondition) {
      final RelBuilder relBuilder =
          call.builder()
              .push(join.getLeft())
              .push(join.getRight())
              .join(join.getJoinType(), newCondition);
      call.transformTo(relBuilder.build());
    }
  }
}
