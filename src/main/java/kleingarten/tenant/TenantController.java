package kleingarten.tenant;


import com.mysema.commons.lang.Assert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class TenantController {
private final TenantManager tenantManager;

TenantController(TenantManager tenantManager){
	Assert.notNull(tenantManager,"TenantManager must not be null");
	this.tenantManager = tenantManager;
}
	@GetMapping("/tenants")
	String tenants(){
	return "tenants";
	}
}