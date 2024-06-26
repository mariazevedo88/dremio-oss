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
package com.dremio.common.memory;

import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.OutOfMemoryException;

/** Utility functions related to memory allocation. */
public class AllocatorUtil {

  /**
   * Ensures there is headroom in the allocator. Throws an exception if not sufficient.
   *
   * @param allocator
   * @param headRoom
   */
  public static void ensureHeadroom(BufferAllocator allocator, long headRoom)
      throws OutOfMemoryException {
    if (allocator.getHeadroom() >= headRoom) {
      // nothing to do in this case.
      return;
    }
    try (ArrowBuf buffer = allocator.buffer(headRoom)) {
      // do a dummy allocation so that we get the allocation states as part of the exception.
    }
  }
}
