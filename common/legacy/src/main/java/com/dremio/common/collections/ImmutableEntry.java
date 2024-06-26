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
package com.dremio.common.collections;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Map;

public class ImmutableEntry<K, V> implements Map.Entry<K, V> {
  private final K key;
  private final V value;

  public ImmutableEntry(final K key, final V value) {
    this.key = Preconditions.checkNotNull(key, "key is required");
    this.value = Preconditions.checkNotNull(value, "value is required");
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public V setValue(final V value) {
    throw new UnsupportedOperationException("entry is immutable");
  }

  @Override
  public boolean equals(final Object other) {
    if (other instanceof ImmutableEntry && other.getClass() == getClass()) {
      final ImmutableEntry<K, V> entry = (ImmutableEntry<K, V>) other;
      return Objects.equal(key, entry.key) && Objects.equal(value, entry.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, value);
  }
}
