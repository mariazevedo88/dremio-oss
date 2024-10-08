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
package com.dremio.exec.planner.sql.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParserPos;

public class CompoundIdentifier extends SqlIdentifier {

  List<IdentifierHolder> ids;

  private static List<String> getNames(List<IdentifierHolder> identifiers) {
    List<String> names = Lists.newArrayListWithCapacity(identifiers.size());
    for (IdentifierHolder h : identifiers) {
      names.add(h.value);
    }
    return names;
  }

  public CompoundIdentifier(List<IdentifierHolder> identifiers) {
    super(
        getNames(identifiers),
        null,
        SqlParserPos.sum(
            identifiers.stream()
                .map(identifierHolder -> identifierHolder.parserPos)
                .collect(Collectors.toList())),
        identifiers.stream()
            .map(identifierHolder -> identifierHolder.parserPos)
            .collect(Collectors.toList()));
    this.ids = identifiers;
  }

  static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(CompoundIdentifier.class);

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private List<IdentifierHolder> identifiers = Lists.newArrayList();

    public CompoundIdentifier build() {
      return new CompoundIdentifier(identifiers);
    }

    public void addString(String name, boolean star, SqlParserPos pos) {
      if (star) {
        assert "*".equals(name);
        identifiers.add(new IdentifierHolder("", pos, false));
      } else {
        identifiers.add(new IdentifierHolder(name, pos, false));
      }
    }

    public void addIndex(int index, SqlParserPos pos) {
      identifiers.add(new IdentifierHolder(Integer.toString(index), pos, true));
    }
  }

  public SqlNode getAsSqlNode() {
    if (ids.size() == 1) {
      return new SqlIdentifier(Collections.singletonList(ids.get(0).value), ids.get(0).parserPos);
    }

    int startIndex;
    SqlNode node;

    if (ids.get(1).isArray()) {
      // handle everything post zero index as item operator.
      startIndex = 1;
      node =
          new SqlIdentifier( //
              ImmutableList.of(ids.get(0).value), //
              null, //
              ids.get(0).parserPos, //
              ImmutableList.of(ids.get(0).parserPos));
    } else {
      // handle everything post two index as item operator.
      startIndex = 2;
      node =
          new SqlIdentifier( //
              ImmutableList.of(ids.get(0).value, ids.get(1).value), //
              null, //
              ids.get(0).parserPos, //
              ImmutableList.of(ids.get(0).parserPos, ids.get(1).parserPos));
    }

    for (int i = startIndex; i < ids.size(); i++) {
      IdentifierHolder holder = ids.get(i);
      if (holder.isArray()) {
        SqlLiteral literal = SqlLiteral.createExactNumeric(holder.value, holder.parserPos);
        node =
            new SqlBasicCall(
                SqlStdOperatorTable.ITEM, new SqlNode[] {node, literal}, holder.parserPos);
      } else {
        if (node instanceof SqlIdentifier) {
          int pos = ((SqlIdentifier) node).names.size();
          node = ((SqlIdentifier) node).add(pos, holder.value, holder.parserPos);
        } else {
          SqlIdentifier identifier = new SqlIdentifier(holder.value, holder.parserPos);
          node =
              new SqlBasicCall(
                  SqlStdOperatorTable.DOT, new SqlNode[] {node, identifier}, holder.parserPos);
        }
      }
    }

    return node;
  }

  public SqlNode getAsCompoundIdentifier() {
    List<String> names = Lists.newArrayListWithCapacity(ids.size());
    List<SqlParserPos> pos = Lists.newArrayListWithCapacity(ids.size());
    for (int i = 0; i < ids.size(); i++) {
      IdentifierHolder holder = ids.get(i);
      names.add(holder.value);
      pos.add(holder.parserPos);
    }
    return new SqlIdentifier(names, null, SqlParserPos.sum(pos), pos);
  }

  private static class IdentifierHolder {
    String value;
    SqlParserPos parserPos;
    boolean isArray;

    public IdentifierHolder(String value, SqlParserPos parserPos, boolean isArray) {
      super();
      this.isArray = isArray;
      this.value = value;
      this.parserPos = parserPos;
    }

    public boolean isArray() {
      return isArray;
    }

    public SqlNode getNode(SqlNode node) {
      SqlLiteral literal = null;
      if (isArray) {
        literal = SqlLiteral.createExactNumeric(value, parserPos);
      } else {
        literal = SqlLiteral.createCharString(value, parserPos);
      }
      return new SqlBasicCall(SqlStdOperatorTable.ITEM, new SqlNode[] {node, literal}, parserPos);
    }
  }
}
