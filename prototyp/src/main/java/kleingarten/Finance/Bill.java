package kleingarten.Finance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bill {

	private @Id @GeneratedValue long id;
	private String billID;
	private double totalFee;
	private String description;

	public Bill(String billID, double totalFee, String description){
		this.billID = billID;
		this.setTotalFee(totalFee);
		this.setDescription(description);
	}
	public long getId(){

		return id;
	}

	public String getBillID(){

		return billID;
	}

	public void setBillID(String billID){

		this.billID = billID;
	}

	public double getTotalFee(){

		return totalFee;
	}

	public void setTotalFee(double totalFee){

		this.totalFee = totalFee;
	}

	public String getDescription(){

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}
}
