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

import kleingarten.tenant.Tenant;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Class which describes a {@link Plot} in the system
 */
@Entity
public class Plot extends Product {

	private PlotStatus status;

	private int size;
	private String description;

	@OneToOne
	private Tenant chairman;

	/**
	 * Private constructor of class {@link Plot}, which is used by the Spring Framework
	 */
	private Plot() {
		this.status = null;
		this.description = null;
		this.chairman = null;
	}

	/**
	 * Constructor of class {@link Plot}
	 *
	 * @param name        number to identify the {@link Plot} as String to be easy readable by the user
	 * @param size        size of the {@link Plot} as int
	 * @param description String that describes the look of a {@link Plot}
	 */
	public Plot(String name, int size, String description) {
		super(name, Money.of(0, EURO));
		this.status = PlotStatus.FREE;
		this.size = size;
		this.description = description;
		this.chairman = null;
	}

	/**
	 * Getter for the status of a {@link Plot}
	 *
	 * @return status as Enum of type {@link PlotStatus}
	 */
	public PlotStatus getStatus() {
		return status;
	}

	/**
	 * Setter for the status of a {@link Plot}
	 *
	 * @param status status as {@link PlotStatus}
	 */
	public void setStatus(PlotStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status must not be null!");
		}
		this.status = status;
	}

	/**
	 * Getter for the size of a {@link Plot}
	 *
	 * @return size as int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setter for the size of a {@link Plot}
	 *
	 * @param size size as int
	 */
	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive!");
		}
		this.size = size;
	}

	/**
	 * Getter for the description of a {@link Plot}
	 *
	 * @return description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of a {@link Plot}
	 *
	 * @param description description as String
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description must not be null!");
		}
		this.description = description;
	}

	/**
	 * Getter for the estimator (or price) of a {@link Plot}
	 *
	 * @return estimator as {@link MonetaryAmount}
	 */
	public MonetaryAmount getEstimator() {
		return this.getPrice();
	}

	/**
	 * Setter for the estimator (or price) of a {@link Plot}
	 *
	 * @param estimator estimator as {@link MonetaryAmount}
	 */
	public void setEstimator(MonetaryAmount estimator) {
		if (estimator == null) {
			throw new IllegalArgumentException("Estimator must not be null!");
		}
		if (estimator.isNegative()) {
			throw new IllegalArgumentException("Estimator must be positive!");
		}
		this.setPrice(estimator);
	}

	/**
	 * Getter for the associated chairman of type {@link Tenant}
	 *
	 * @return associated chairman as {@link Tenant}
	 */
	public Tenant getChairman() {
		return chairman;
	}

	/**
	 * Setter for the associated chairman of type {@link Tenant}
	 *
	 * @param chairman chairman which should be associated as {@link Tenant}
	 */
	public void setChairman(Tenant chairman) {
		this.chairman = chairman;
	}

}
