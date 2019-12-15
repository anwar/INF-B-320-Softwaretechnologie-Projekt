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

import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for {@link ComplaintManager}.
 */
@SpringBootTest
@Transactional
public class ComplaintManagerTests {

	private ComplaintManager complaintManager;
	private TenantManager tenantManager;
	private UserAccountManager userAccountManager;

	public ComplaintManagerTests(@Autowired ComplaintManager complaintManager,
								 @Autowired TenantManager tenantManager,
								 @Autowired UserAccountManager userAccountManager) {
		this.complaintManager = complaintManager;
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
	}

	@Test
	void getForObmann() {
		assertFalse(complaintManager.getForObmann(tenantManager.findByRole(Role.of("Obmann")).get(0)).isEmpty());

	}

	@Test
	void getForComplainant() {
		assertFalse(complaintManager.getForComplainant(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("neptun").get())).isEmpty());
	}
}
