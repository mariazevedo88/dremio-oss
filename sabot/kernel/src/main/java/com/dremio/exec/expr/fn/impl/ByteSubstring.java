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
package com.dremio.exec.expr.fn.impl;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;
import org.apache.arrow.vector.holders.BigIntHolder;
import org.apache.arrow.vector.holders.VarBinaryHolder;

// TODO: implement optional length parameter

/**
 * Evaluate a substring expression for a given value; specifying the start position, and optionally
 * the end position.
 *
 * <p>- If the start position is negative, start from abs(start) characters from the end of the
 * buffer.
 *
 * <p>- If no length is specified, continue to the end of the string.
 *
 * <p>- If the substring expression's length exceeds the value's upward bound, the value's length
 * will be used.
 *
 * <p>- If the substring is invalid, return an empty string.
 */
@FunctionTemplate(
    names = {"bytesubstring", "byte_substr"},
    scope = FunctionTemplate.FunctionScope.SIMPLE,
    nulls = FunctionTemplate.NullHandling.NULL_IF_NULL)
public class ByteSubstring implements SimpleFunction {

  @Param VarBinaryHolder in;
  @Param BigIntHolder offset;
  @Param BigIntHolder length;
  @Output VarBinaryHolder out;

  @Override
  public void setup() {}

  @Override
  public void eval() {
    out.buffer = in.buffer;

    // handle invalid values; e.g. SUBSTRING(value, 0, x) or SUBSTRING(value, x, 0)
    if (offset.value == 0 || length.value <= 0) {
      out.start = 0;
      out.end = 0;
    } else {
      // handle negative and positive offset values
      if (offset.value < 0) {
        out.start = in.end + (int) offset.value;
      } else {
        out.start = in.start + (int) offset.value - 1;
      }
      // calculate end position from length and truncate to upper value bounds
      if (out.start + length.value > in.end) {
        out.end = in.end;
      } else {
        out.end = out.start + (int) length.value;
      }
    }
  }
}
