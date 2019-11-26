package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

	@Autowired
	public BillController( ) {

	}

	@GetMapping("/bill")
	String viewBill(){
		return "bill";
	}

}
