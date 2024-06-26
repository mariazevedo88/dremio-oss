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
package com.dremio.exec.store.hive.exec;

import com.dremio.exec.catalog.StoragePluginId;
import com.dremio.exec.store.StoragePluginResolver;
import com.dremio.exec.store.SupportsPF4JStoragePlugin;
import com.dremio.exec.store.hive.HiveCommonUtilities;
import com.dremio.exec.store.hive.StoragePluginCreator;
import com.dremio.exec.store.hive.proxy.HiveProxiedOrcScanFilter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Helper class for deserializing an OrcScanFilter that delegates to a OrcScanFilter that is in a
 * separate classloader.
 */
public class HiveProxyingOrcScanFilterDeserializer
    extends StdDeserializer<HiveProxyingOrcScanFilter> {

  public HiveProxyingOrcScanFilterDeserializer() {
    this(null);
  }

  public HiveProxyingOrcScanFilterDeserializer(Class<?> clazz) {
    super(clazz);
  }

  @Override
  public HiveProxyingOrcScanFilter deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    // TODO: Optimize performance as described in https://dremio.atlassian.net/browse/DX-17732
    final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    final StoragePluginId pluginId =
        HiveCommonUtilities.deserialize(
            jsonParser,
            deserializationContext,
            node.get(HiveProxyingOrcScanFilter.JSON_PROP_PLUGINID),
            StoragePluginId.class);

    // get subscan class type from plugin.
    final StoragePluginResolver storagePluginResolver =
        (StoragePluginResolver)
            deserializationContext.findInjectableValue(
                StoragePluginResolver.class.getName(), null, null);
    final SupportsPF4JStoragePlugin pf4JStoragePlugin = storagePluginResolver.getSource(pluginId);
    final StoragePluginCreator.PF4JStoragePlugin plugin = pf4JStoragePlugin.getPF4JStoragePlugin();
    final Class<? extends HiveProxiedOrcScanFilter> scanClazz = plugin.getOrcScanFilterClass();

    final JavaType scanType = deserializationContext.getTypeFactory().constructType(scanClazz);
    final BeanDescription description = deserializationContext.getConfig().introspect(scanType);
    final JsonDeserializer<Object> orcScanFilterDeserializer =
        deserializationContext
            .getFactory()
            .createBeanDeserializer(deserializationContext, scanType, description);

    if (orcScanFilterDeserializer instanceof ResolvableDeserializer) {
      ((ResolvableDeserializer) orcScanFilterDeserializer).resolve(deserializationContext);
    }

    final JsonParser movedParser =
        jsonParser
            .getCodec()
            .treeAsTokens(node.get(HiveProxyingOrcScanFilter.JSON_PROP_WRAPPEDHIVEORCSCANFILTER));
    deserializationContext.getConfig().initialize(movedParser);

    if (movedParser.getCurrentToken() == null) {
      movedParser.nextToken();
    }

    final HiveProxiedOrcScanFilter orcScanFilter =
        (HiveProxiedOrcScanFilter)
            orcScanFilterDeserializer.deserialize(movedParser, deserializationContext);
    return new HiveProxyingOrcScanFilter(pluginId, orcScanFilter);
  }
}
