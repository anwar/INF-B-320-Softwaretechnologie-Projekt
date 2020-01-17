package kleingarten.Finance;

import org.hibernate.validator.constraints.Range;
import org.salespointframework.core.SalespointIdentifier;

public class FeeForm {

	private String name;
	private double prices;
	private SalespointIdentifier id;

	public String getName() {

		return name;
	}
	public void setName(String name) {

		this.name = name;
	}
	@Range(min = 0, message = "price can not be lower than 0")
	public double getPrices() {

		return prices;
	}
	public void setPrices(double prices) {

		this.prices = prices;
	}
	public String getId() {
		return id.toString();
	}
	
	public void setId(String id) {
		
		this.id = new SalespointIdentifier(id);
	}
}