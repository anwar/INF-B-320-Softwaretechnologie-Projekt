package kleingarten.Finance;

import org.salespointframework.core.SalespointIdentifier;

//import kleingarten.Finance.Fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeeManagement {
	private FeeCatalog feeCatalog;

	@Autowired
	public FeeManagement(FeeCatalog feeCatalog) {
		this.feeCatalog = feeCatalog;
	}

	protected FeeManagement(){}

	public void create(FeeForm form) {

		feeCatalog.save(new Fee("Test-ID", form.getName(), form.getPrices()));
	}
	public Fee save(Fee fee) {

		return feeCatalog.save(fee);
	}
	public Iterable<Fee> getByBillId(String billID){
		return feeCatalog.findByBillID(billID);
	}

	public void delete(SalespointIdentifier id){
		feeCatalog.delete(findById(id).get());
	}

	public Iterable<Fee> findAll(){

		return feeCatalog.findAll();
	}

	public java.util.Optional<Fee> findById(SalespointIdentifier id){

		return feeCatalog.findById(id);

	}

}

