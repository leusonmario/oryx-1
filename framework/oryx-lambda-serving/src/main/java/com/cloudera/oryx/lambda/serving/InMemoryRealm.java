/*
 * Copyright (c) 2014, Cloudera, Inc. and Intel Corp. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.oryx.lambda.serving;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;

/**
 * Adapted from Tomcat's {@link org.apache.catalina.realm.MemoryRealm}.
 * This implementation of {@link RealmBase} lets you add users programmatically to an in-memory store.
 */
public final class InMemoryRealm extends RealmBase {

  static final String NAME = "Oryx";

  static final String AUTH_ROLE = "oryx-user";

  /**
   * The set of valid Principals for this Realm, keyed by user name.
   */
  private final Map<String, GenericPrincipal> principals = new HashMap<>();

  @Override
  public Principal authenticate(String username, String credentials) {
    GenericPrincipal principal = principals.get(username);
    boolean validated = principal != null && getCredentialHandler().matches(credentials, principal.getPassword());
    return validated ? principal : null;
  }

  void addUser(String username, String password) {
    principals.put(username, new GenericPrincipal(username, password, Collections.singletonList(AUTH_ROLE)));
  }

  /**
   * @deprecated because superclass method is deprecated
   */
  @Override
  @Deprecated
  protected String getName() {
    return NAME;
  }

  @Override
  protected String getPassword(String username) {
    GenericPrincipal principal = principals.get(username);
    return principal == null ? null : principal.getPassword();
  }

  @Override
  protected Principal getPrincipal(String username) {
    return principals.get(username);
  }

}
