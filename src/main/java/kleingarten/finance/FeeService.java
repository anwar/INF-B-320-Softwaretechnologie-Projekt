package kleingarten.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService implements FeeServiceI {

	@Autowired
	private FeeRepository feeRepository;

	@Override
	public List<Fee> findAll(){
		return (List<Fee>) feeRepository.findAll();
	}
}
