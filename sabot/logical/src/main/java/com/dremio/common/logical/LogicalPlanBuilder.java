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
package com.dremio.common.logical;

import com.dremio.common.logical.PlanProperties.Generator.ResultMode;
import com.dremio.common.logical.PlanProperties.PlanType;
import com.dremio.common.logical.data.LogicalOperator;
import com.dremio.common.store.StoragePluginConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/** A programmatic builder for logical plans. */
public class LogicalPlanBuilder {
  private PlanProperties planProperties;
  private ImmutableMap.Builder<String, StoragePluginConfig> storageEngines = ImmutableMap.builder();
  private ImmutableList.Builder<LogicalOperator> operators = ImmutableList.builder();

  public LogicalPlanBuilder planProperties(PlanProperties planProperties) {
    this.planProperties = planProperties;
    return this;
  }

  public LogicalPlanBuilder planProperties(
      PlanType type, int version, String generatorType, String generatorInfo, ResultMode mode) {
    this.planProperties =
        PlanProperties.builder()
            .generator(generatorType, generatorInfo)
            .type(type)
            .version(version)
            .resultMode(mode)
            .build();
    return this;
  }

  public LogicalPlanBuilder addStorageEngine(String name, StoragePluginConfig config) {
    this.storageEngines.put(name, config);
    return this;
  }

  public LogicalPlanBuilder addLogicalOperator(LogicalOperator operator) {
    this.operators.add(operator);
    return this;
  }

  public LogicalPlan build() {
    return new LogicalPlan(
        this.planProperties, this.storageEngines.build(), this.operators.build());
  }
}
