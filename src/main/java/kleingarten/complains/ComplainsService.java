package kleingarten.complains;

import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Component
public class ComplainsService {

	private final ComplainsRepository complains;
	private final ComplainsManager complainsManager;
	@Autowired
	private final TenantManager tenantManager;

	ComplainsService(@Autowired ComplainsRepository complains, ComplainsManager complainsManager,  @Autowired TenantManager tenantManager){
		this.complains = complains;
		this.complainsManager = complainsManager;

		this.tenantManager = tenantManager;
	}

	public String getAuthorName(Complains complains){
		return tenantManager.get(complains.getAuthor()).getForename() + " " + tenantManager.get(complains.getAuthor()).getSurname();
	}

	public String getSubjectName(Complains complains){
		return tenantManager.get(complains.getSubject()).getForename() + " " + tenantManager.get(complains.getSubject()).getSurname();
	}
}
