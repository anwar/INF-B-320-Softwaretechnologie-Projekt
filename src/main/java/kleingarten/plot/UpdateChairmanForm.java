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

package kleingarten.plot;

import org.salespointframework.catalog.ProductIdentifier;

import java.util.List;

public class UpdateChairmanForm {
	private List<ProductIdentifier> updatedPlots;

	public UpdateChairmanForm() {
	}

	public UpdateChairmanForm(List<ProductIdentifier> updatedPlots) {
		this.updatedPlots = updatedPlots;
	}

	public List<ProductIdentifier> getUpdatedPlots() {
		return updatedPlots;
	}

	public void setUpdatedPlots(List<ProductIdentifier> updatedPlots) {
		this.updatedPlots = updatedPlots;
	}
}
