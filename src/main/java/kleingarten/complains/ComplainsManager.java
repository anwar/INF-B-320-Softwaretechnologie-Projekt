package kleingarten.complains;


import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;

// WIP we still have to do this, but if someone got time, they can start working on this
@Service
public class ComplainsManager {

	private final ComplainsRepository complainsRepository;
	private final TenantManager tenantManager;

	@Autowired
	public ComplainsManager(ComplainsRepository complainsRepository, TenantManager tenantManager){
		this.complainsRepository = complainsRepository;
		this.tenantManager = tenantManager;
	}

	public Streamable<Complains> getAll(){
		 return complainsRepository.findAll();
	}

	public Complains get(long id){
		return complainsRepository.findById(id).get();
	}

	public Streamable<Complains> pendingComplains(){
		return complainsRepository.findAll().filter(c -> c.getState().equals(ComplainsState.PENDING));
	}

	public Streamable<Complains> finishedComplains(){
		return complainsRepository.findAll().filter(c -> c.getState().equals(ComplainsState.FINISHED));
	}


}
