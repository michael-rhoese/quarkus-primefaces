/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.fherfurt.repositories.positions;

import de.fherfurt.domains.Position;
import de.fherfurt.repositories.BaseRepository;
import javax.enterprise.context.SessionScoped;
import java.io.Serial;

/**
 * This repository allows the direct access to {@link Position}s for reading, writing and deletion.
 */
@SessionScoped
public class PositionRepository extends BaseRepository<Position> {
    @Serial
    private static final long serialVersionUID = 2236632415172439519L;
}
