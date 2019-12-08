package kleingarten.complains;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainsRepository extends CrudRepository<Complains, Long> {

	Streamable<Complains> findAll();

	//Streamable<Complains> findByPlot();


}
