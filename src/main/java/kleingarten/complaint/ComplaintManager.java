package kleingarten.complaint;


import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

// WIP we still have to do this, but if someone got time, they can start working on this
@Service
public class ComplaintManager {

	private final ComplaintRepository complaintRepository;
	private final TenantManager tenantManager;

	@Autowired
	public ComplaintManager(ComplaintRepository complaintRepository, TenantManager tenantManager) {
		this.complaintRepository = complaintRepository;
		this.tenantManager = tenantManager;
	}

	public Streamable<Complaint> getAll() {
		return complaintRepository.findAll();
	}

	public Complaint get(long id) {
		return complaintRepository.findById(id).get();
	}

	public Streamable<Complaint> pendingComplains() {
		return complaintRepository.findAll().filter(c -> c.getState().equals(ComplaintState.PENDING));
	}

	public Streamable<Complaint> finishedComplains() {
		return complaintRepository.findAll().filter(c -> c.getState().equals(ComplaintState.FINISHED));
	}


}
