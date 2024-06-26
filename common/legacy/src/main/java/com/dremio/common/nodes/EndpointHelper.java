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
package com.dremio.common.nodes;

import com.dremio.exec.proto.CoordinationProtos.NodeEndpoint;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Helper class to build minimal endpoints. Used in rpcs sent to executor nodes, and to track status
 * of running fragments.
 */
public class EndpointHelper {

  public static NodeEndpoint getMinimalEndpoint(NodeEndpoint endpoint) {
    NodeEndpoint.Builder builder = NodeEndpoint.newBuilder();
    if (endpoint.hasAddress()) {
      builder.setAddress(endpoint.getAddress());
    }
    if (endpoint.hasFabricPort()) {
      builder.setFabricPort(endpoint.getFabricPort());
    }
    if (endpoint.hasEngineId()) {
      builder.setEngineId(endpoint.getEngineId());
    }
    return builder.build();
  }

  public static String getMinimalString(NodeEndpoint endpoint) {
    return endpoint.getAddress() + ":" + endpoint.getFabricPort();
  }

  public static String getMinimalString(Collection<NodeEndpoint> endpoints) {
    return endpoints.stream()
        .map(EndpointHelper::getMinimalString)
        .collect(Collectors.joining(","));
  }

  public static NodeEndpoint hostPortStrToConduitEndpoint(String hostPortString) {
    String[] parts = hostPortString.split(":");
    Preconditions.checkArgument(parts.length == 2);

    NodeEndpoint.Builder builder = NodeEndpoint.newBuilder();
    builder.setAddress(parts[0]);
    builder.setConduitPort(Integer.valueOf(parts[1]));
    return builder.build();
  }
}
