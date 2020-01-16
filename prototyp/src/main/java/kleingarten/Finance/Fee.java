package kleingarten.Finance;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Fee extends AbstractEntity<SalespointIdentifier> {
	public Fee(){}

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "FEE_ID"))
	private SalespointIdentifier id; // = new SalespointIdentifier(); check out the videoshop customer.

	private String billID;

	private String name;
	private double prices;

	// Fee: Need to add Quantity!! Electricity-/Water Mileage and Plot Size.
	public Fee(String billID, String name, double prices){

		this.id = new SalespointIdentifier();
		this.billID = billID;
		setName(name);
		setPrices(prices);
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public double getPrices(){
		return prices;
	}

	public void setPrices(double prices){
		this.prices = prices;
	}
	@Override
	public SalespointIdentifier getId() {
		return id;
	}
}
