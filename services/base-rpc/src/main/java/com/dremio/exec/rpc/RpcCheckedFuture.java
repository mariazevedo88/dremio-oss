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
package com.dremio.exec.rpc;

import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.buffer.ByteBuf;

class RpcCheckedFuture<T> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<T>
    implements RpcFuture<T> {

  volatile ByteBuf buffer;

  public RpcCheckedFuture(ListenableFuture<T> delegate) {
    super(delegate);
  }

  public void setBuffer(ByteBuf buffer) {
    if (buffer != null) {
      buffer.retain();
      this.buffer = buffer;
    }
  }

  @Override
  public ByteBuf getBuffer() {
    return buffer;
  }
}
