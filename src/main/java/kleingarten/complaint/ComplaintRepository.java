/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kleingarten.complaint;

import kleingarten.tenant.Tenant;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * A repository to manage {@link Complaint} instances.
 * The methods are dynamically implemented by Spring Data JPA.
 */
@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {
	/**
	 * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable} instead of {@link Iterable}.
	 */
	@Override
	Streamable<Complaint> findAll();

	/**
	 * Returns all {@link Complaint}s created by a {@link Tenant} and sorted by the given sort criteria.
	 *
	 * @param complainant who authored the {@link Complaint}s
	 * @param sort        the given sorting criteria
	 * @return all {@link Complaint}s matching the query
	 */
	Streamable<Complaint> findByComplainant(Tenant complainant, Sort sort);

	/**
	 * Returns all {@link Complaint}s that are assigned to an Obmann.
	 *
	 * @param assignedObmann who the {@link Complaint}s are assigned to
	 * @param sort           the given sorting criteria
	 * @return all {@link Complaint}s matching the query
	 */
	Streamable<Complaint> findByAssignedObmann(Tenant assignedObmann, Sort sort);
}
