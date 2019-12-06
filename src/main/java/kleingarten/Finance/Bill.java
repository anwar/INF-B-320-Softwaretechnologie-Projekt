package kleingarten.Finance;

import javax.persistence.*;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Entity
public class Bill {

	private @Id @GeneratedValue long id;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	/*
	 *billID shows plot Nr. and created year

	private String billID;
	//Procedure procedure;

	public String getBillID() {
		return billID;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}
	*/
	public Bill(){}

	@ManyToMany
	List<Fee> feeList;

	public Bill(Procedure procedure){
		/**
		 * Basic fees for a bill
		 */
		Fee membershipFee = new MembershipFee();
		feeList.add(membershipFee);
		Fee liabilityFee = new LiabilityFee();
		feeList.add(liabilityFee);
		Fee winterServiceFee = new WinterServiceFee();
		feeList.add(winterServiceFee);
		Fee socialContribution = new SocialContribution();
		feeList.add(socialContribution);
		Fee legalProtectionFee = new LegalProtectionFee();
		feeList.add(legalProtectionFee);
		Fee expensesFlat = new ExpensesFlat();
		feeList.add(expensesFlat);
		Fee waterMeterFee = new WaterMeterFee();
		feeList.add(waterMeterFee);
		Fee electricityMeterFee = new ElectricityMeterFee();
		feeList.add(electricityMeterFee);
		/**
		 * Fees relevant to Procedure Class
		 */
		Fee waterFee = new WaterFee();
		feeList.add(waterFee);
		Fee powerFee = new PowerFee();
		feeList.add(powerFee);
		Fee rent = new Rent();
		feeList.add(rent);
		Fee penalty = new Penalty();
		feeList.add(penalty);

		//this.billID = billID;

	}

}



