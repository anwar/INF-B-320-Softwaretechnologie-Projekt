// Copyright 2019-2020 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.tenant;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;


/**
 * Extension of {@link CrudRepository} to add specific query methods
 */
public interface TenantRepository extends CrudRepository<Tenant, Long> {

	/**
	 * Return all {@link Tenant}s
	 *
	 * @return {@link Streamable} of {@link Tenant}, never {@literal null}
	 */
	@Override
	Streamable<Tenant> findAll();

	/**
	 * Return all {@link Tenant}s with the given userAccount of type {@link UserAccount}
	 *
	 * @param userAccount userAccount as {@link UserAccount}, must not be {@literal null}
	 * @return tenants as {@link Streamable} of {@link Tenant}, never {@literal null}
	 */
	Optional<Tenant> findByUserAccount(UserAccount userAccount);

}
