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

package kleingarten.finance;

import kleingarten.plot.Plot;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {

	Streamable<Procedure> findAll();

	Procedure findById(long id);

	Streamable<Procedure> findByPlot(Plot plot);

	Streamable<Procedure> findByPlotName(String plotName);


	Streamable<Procedure> findByPlotProductIdentifier(ProductIdentifier plotId);


	Streamable<Procedure> findByYear(int year);

	Streamable<Procedure> findByMainTenant(long tenantId);

	@Query("select p from Procedure p WHERE :tenant in elements(p.subTenants)")
	Streamable<Procedure> findBySubTenant(@Param("tenant") long tenantId);

}
