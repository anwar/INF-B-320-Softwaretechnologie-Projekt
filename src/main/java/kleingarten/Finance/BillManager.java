package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BillManager {
	private BillRepository billRepository;
	//private String getBillID;


	@Autowired
	public BillManager(BillRepository billRepository) {
		this.billRepository = billRepository;
	}
	/*
	Streamable<Bill> findAll(){
		return billRepository.findAll();
	};


	Bill findById(long id) {
		return billRepository.findById(id);
	};

	Streamable<Bill> findByPlotId(String plotId){
		return billRepository.findByPlotId(plotId);
	}
	;
	*/

	//Streamable<Bill> findByYear(int year);

	//Streamable<Bill> findByMainTenant( long tenantId);

}
