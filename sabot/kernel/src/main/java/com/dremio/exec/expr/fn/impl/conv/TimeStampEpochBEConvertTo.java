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
package com.dremio.exec.expr.fn.impl.conv;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.FunctionTemplate.FunctionScope;
import com.dremio.exec.expr.annotations.FunctionTemplate.NullHandling;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;
import javax.inject.Inject;
import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.vector.holders.TimeStampMilliHolder;
import org.apache.arrow.vector.holders.VarBinaryHolder;

@FunctionTemplate(
    name = "convert_toTIMESTAMP_EPOCH_BE",
    scope = FunctionScope.SIMPLE,
    nulls = NullHandling.NULL_IF_NULL)
public class TimeStampEpochBEConvertTo implements SimpleFunction {

  @Param TimeStampMilliHolder in;
  @Output VarBinaryHolder out;
  @Inject ArrowBuf buffer;

  @Override
  public void setup() {
    buffer = buffer.reallocIfNeeded(8);
  }

  @Override
  public void eval() {
    buffer.clear();
    buffer.writeLong(Long.reverseBytes(in.value));
    out.buffer = buffer;
    out.start = 0;
    out.end = 8;
  }
}
