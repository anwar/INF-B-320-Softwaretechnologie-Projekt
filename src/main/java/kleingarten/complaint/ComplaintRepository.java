package kleingarten.complaint;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {

	Streamable<Complaint> findAll();

	//Streamable<Complains> findByPlot();


}
