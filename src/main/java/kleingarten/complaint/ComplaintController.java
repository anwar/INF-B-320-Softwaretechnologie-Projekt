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

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A controller to handle web requests to manage {@link Complaint}s.
 */
@Controller
public class ComplaintController {
	private ComplaintManager complaintManager;

	/**
	 * @param complaintManager must not be {@literal null}
	 */
	ComplaintController(ComplaintManager complaintManager) {
		Assert.notNull(complaintManager, "complaintManager must not be null!");
		this.complaintManager = complaintManager;
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/complaints")
	String complains(Model model) {
		model.addAttribute("complaints", complaintManager.getAll());
		return "complaint/complaints";
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/addComplaint/{plot}")
	String addComplaint(Model model) {
		model.addAttribute("complaints", complaintManager.getAll());
		return "complaint/addComplaint";
	}
}
