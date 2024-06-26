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
package com.dremio.exec.physical.base;

/**
 * An operator which specifically is a lowest level leaf node of a query plan across all possible
 * fragments. Currently, the only operator that is a Leaf node are GroupScan nodes. Ultimately this
 * could include use of Cache scans and other types of atypical data production systems.
 */
public interface Leaf extends FragmentLeaf {}
