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
package com.dremio.common.logical.data;

import com.dremio.common.expression.FieldReference;
import com.dremio.common.expression.LogicalExpression;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ref", "expr"})
public class NamedExpression {
  private final LogicalExpression expr;
  private final FieldReference ref;

  @JsonCreator
  public NamedExpression(
      @JsonProperty("expr") LogicalExpression expr, @JsonProperty("ref") FieldReference ref) {
    super();
    this.expr = expr;
    this.ref = ref;
  }

  public LogicalExpression getExpr() {
    return expr;
  }

  public FieldReference getRef() {
    return ref;
  }

  @Override
  public String toString() {
    return "NamedExpression [expr=" + expr + ", ref=" + ref + "]";
  }
}
