package kleingarten.Finance;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeCatalog extends CrudRepository<Fee, SalespointIdentifier> {
//CrudRepository
	Streamable<Fee> findByName(String name);
	
	Streamable<Fee> findByBillID(String billID);
}
