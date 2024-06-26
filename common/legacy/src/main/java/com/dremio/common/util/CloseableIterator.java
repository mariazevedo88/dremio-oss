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
package com.dremio.common.util;

import java.util.Iterator;

/**
 * This interface is used by iterators that use releasable resources during iteration. The consumer
 * of a CloseableIterator should ensure that the close() method is always called, for example by
 * putting such a call in a finally block.
 *
 * @param <T>
 */
public interface CloseableIterator<T> extends Iterator<T>, AutoCloseable {}
