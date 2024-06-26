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
package com.dremio.sabot.op.receiver;

import com.dremio.sabot.exec.rpc.AckSender;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Queues;
import java.util.Queue;

public class ResponseSenderQueue {
  static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(ResponseSenderQueue.class);

  private Queue<AckSender> q = Queues.newConcurrentLinkedQueue();

  public void enqueueResponse(AckSender sender) {
    q.add(sender);
  }

  public void flushResponses() {
    flushResponses(Integer.MAX_VALUE);
  }

  /**
   * Flush only up to a count responses
   *
   * @param count
   * @return
   */
  public int flushResponses(int count) {
    logger.trace("queue.size: {}, count: {}", q.size(), count);
    int i = 0;
    while (!q.isEmpty() && i < count) {
      AckSender s = q.poll();
      if (s != null) {
        s.sendOk();
      }
      i++;
    }
    return i;
  }

  @VisibleForTesting
  boolean isEmpty() {
    return q.isEmpty();
  }
}
