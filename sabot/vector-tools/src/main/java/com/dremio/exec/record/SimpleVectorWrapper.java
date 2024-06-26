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
import com.google.common.base.Preconditions;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.ValueVector;
import org.apache.arrow.vector.complex.AbstractStructVector;
import org.apache.arrow.vector.complex.FieldIdUtil2;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.util.CallBack;
import org.apache.arrow.vector.util.TransferPair;

public class SimpleVectorWrapper<T extends ValueVector> implements VectorWrapper<T> {
  static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(SimpleVectorWrapper.class);

  private T vector;

  public SimpleVectorWrapper(T v) {
    this.vector = v;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<T> getVectorClass() {
    return (Class<T>) vector.getClass();
  }

  @Override
  public Field getField() {
    return vector.getField();
  }

  @Override
  public T getValueVector() {
    return vector;
  }

  @Override
  public T[] getValueVectors() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isHyper() {
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public VectorWrapper<T> cloneAndTransfer(BufferAllocator allocator, CallBack callback) {
    try {
      TransferPair tp = vector.getTransferPair(vector.getField(), allocator, callback);
      tp.transfer();
      return new SimpleVectorWrapper<T>((T) tp.getTo());
    } catch (RuntimeException ex) {
      throw ex;
    }
  }

  @Override
  public void close() {
    clear();
  }

  @Override
  public void clear() {
    vector.clear();
  }

  public static <T extends ValueVector> SimpleVectorWrapper<T> create(T v) {
    return new SimpleVectorWrapper<T>(v);
  }

  @Override
  public VectorWrapper<?> getChildWrapper(int[] ids) {
    if (ids.length == 1) {
      return this;
    }

    ValueVector vector = this.vector;
    for (int i = 1; i < ids.length; i++) {
      final AbstractStructVector mapLike = AbstractStructVector.class.cast(vector);
      if (mapLike == null) {
        return null;
      }
      vector = mapLike.getChildByOrdinal(ids[i]);
    }

    return new SimpleVectorWrapper<>(vector);
  }

  @Override
  public TypedFieldId getFieldIdIfMatches(int id, BasePath expectedPath) {
    return FieldIdUtil2.getFieldId(getValueVector().getField(), id, expectedPath, true);
  }

  @Override
  public void transfer(VectorWrapper<?> destination) {
    Preconditions.checkArgument(destination instanceof SimpleVectorWrapper);
    Preconditions.checkArgument(getField().getType().equals(destination.getField().getType()));
    vector.makeTransferPair(((SimpleVectorWrapper<?>) destination).vector).transfer();
  }
}
