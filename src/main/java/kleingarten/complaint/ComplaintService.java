package kleingarten.complaint;

import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplaintService {

	private final ComplaintRepository complaints;
	private final ComplaintManager complaintManager;
	@Autowired
	private final TenantManager tenantManager;

	ComplaintService(@Autowired ComplaintRepository complaints, ComplaintManager complaintManager, @Autowired TenantManager tenantManager) {
		this.complaints = complaints;
		this.complaintManager = complaintManager;

		this.tenantManager = tenantManager;
	}

	public String getAuthorName(Complaint complaints) {
		return tenantManager.get(complaints.getAuthor()).getForename() + " " + tenantManager.get(complaints.getAuthor()).getSurname();
	}

	public String getSubjectName(Complaint complaints) {
		return tenantManager.get(complaints.getSubject()).getForename() + " " + tenantManager.get(complaints.getSubject()).getSurname();
	}
}
