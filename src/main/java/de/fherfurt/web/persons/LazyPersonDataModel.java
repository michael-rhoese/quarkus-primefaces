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
package de.fherfurt.web.persons;

import de.fherfurt.domains.Address;
import de.fherfurt.domains.Person;
import de.fherfurt.repositories.addresses.AddressRepository;
import de.fherfurt.repositories.persons.PersonRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import java.io.Serial;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@RequiredArgsConstructor(staticName = "of")
public class LazyPersonDataModel extends LazyDataModel<Person> {
    @Serial
    private static final long serialVersionUID = 4671115893735409258L;

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    private final List<Person> cache = new ArrayList<>();

    @Getter
    @Setter
    private Person selected;

    @Override
    public List<Person> load(int start, int page, Map<String, SortMeta> sorts, Map<String, FilterMeta> filters) {

        cache.clear();

        final Map<String, Object> usedFilters = filters.values().stream()
                .collect(Collectors.toMap(FilterMeta::getField, FilterMeta::getFilterValue));

        final String hql = filters.keySet()
                .stream()
                .map(column -> column + " = :" + column)
                .collect(Collectors.joining(" and "));

        final Sort usedSorts = prepareSorting(sorts);

        cache.addAll(personRepository.find(hql, usedSorts, usedFilters).page(Page.of(start, page)).list());

        setRowCount((int) personRepository.count(hql, usedFilters));

        return cache;
    }

    private Sort prepareSorting(final Map<String, SortMeta> sorts) {

        final AtomicReference<Sort> res = new AtomicReference<>(null);
        if (Objects.isNull(sorts) || sorts.isEmpty()) {
            return res.get();
        }

        final AtomicBoolean initialized = new AtomicBoolean(false);
        sorts.forEach((key, value) -> {
            if (!initialized.getAndSet(true)) {
                res.set(Sort.by(key, value.getOrder().isAscending() ? Sort.Direction.Ascending : Sort.Direction.Descending));
            }

            res.get().and(key, value.getOrder().isAscending() ? Sort.Direction.Ascending : Sort.Direction.Descending);
        });

        return res.get();
    }

    @Override
    public Person getRowData(String rowKey) {
        return cache.stream().filter(entry -> Objects.equals(String.valueOf(entry.getId()), rowKey)).findFirst().orElse(null);
    }

    @Override
    public String getRowKey(Person object) {
        return String.valueOf(object.getId());
    }

    public void save() {
        if (Objects.isNull(this.getSelected())) {
            return;
        }

        if (Objects.nonNull(this.getSelected().getAddress()) && Objects.nonNull(this.getSelected().getAddress().getId())) {
            Address loaded = addressRepository.findById(this.getSelected().getAddress().getId());
            this.getSelected().setAddress(loaded);
        }

        this.personRepository.save(this.getSelected());
    }

    public void delete(Person person) {
        this.personRepository.remove(person);
    }
}
