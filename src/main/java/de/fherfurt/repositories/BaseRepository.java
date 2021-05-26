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
package de.fherfurt.repositories;

import de.fherfurt.domains.BaseEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class BaseRepository<E extends BaseEntity> implements PanacheRepository<E>, Serializable {

    @Serial
    private static final long serialVersionUID = -2041714946937577049L;

    public Long save(E entity) {
        if (Objects.isNull(entity.getId())) {
            persist(entity);
        } else {
            this.getEntityManager().merge(entity);
        }

        return entity.getId();
    }

    public void remove(E entity) {
        final E entityToDelete = this.findById(entity.getId());
        this.delete(entityToDelete);
    }
}
