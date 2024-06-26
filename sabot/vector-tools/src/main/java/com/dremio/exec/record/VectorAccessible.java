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
package com.dremio.exec.record;

import com.dremio.common.expression.BasePath;
import com.dremio.exec.record.selection.SelectionVector2;
import com.dremio.exec.record.selection.SelectionVector4;
import org.apache.arrow.vector.ValueVector;

// TODO javadoc
public interface VectorAccessible extends Iterable<VectorWrapper<?>> {
  // TODO are these <?> releated in any way? Should they be the same one?
  // TODO javadoc
  public <T extends ValueVector> VectorWrapper<T> getValueAccessorById(
      Class<T> clazz, int... fieldIds);

  /**
   * Get the value vector type and id for the given schema path. The TypedFieldId should store a
   * fieldId which is the same as the ordinal position of the field within the Iterator provided
   * this classes implementation of Iterable<ValueVector>.
   *
   * @param path the path where the vector should be located.
   * @return the local field id associated with this vector. If no field matches this path, this
   *     will return a null TypedFieldId
   */
  public TypedFieldId getValueVectorId(BasePath path);

  /**
   * Get the schema of the current RecordBatch. This changes if and only if a *_NEW_SCHEMA
   * IterOutcome is provided.
   *
   * @return schema of the current batch
   */
  public BatchSchema getSchema();

  /**
   * Get the number of records.
   *
   * @return number of records
   */
  public int getRecordCount();

  public abstract SelectionVector2 getSelectionVector2();

  public abstract SelectionVector4 getSelectionVector4();
}
