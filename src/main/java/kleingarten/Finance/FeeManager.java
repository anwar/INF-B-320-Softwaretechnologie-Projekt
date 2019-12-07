package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeeManager {
	private FeeRepository feeRepository;

	@Autowired
	public FeeManager(FeeRepository feeRepository) {
		this.feeRepository = feeRepository;
	}

	protected FeeManager(){}

	public Fee save(Fee fee) {

		return feeRepository.save(fee);
	}

	public void delete(long id){
		feeRepository.delete(findById(id).get());
	}

	public Iterable<Fee> findAll(){

		return feeRepository.findAll();
	}

	public Fee get(Long id){
		return feeRepository.findById(id).orElse(null);
	}

	public java.util.Optional<Fee> findById(long id){

		return feeRepository.findById(id);

	}

	public void create(FeeForm form) {

		feeRepository.save(new Fee(form.getTitle(), form.getCount(), form.getBasePrice(), form.getPrice()));
	}

}

