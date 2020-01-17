package kleingarten.Finance;


import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kleingarten.tenant.TenantDataInitializer;


@Component
public class FeeDataInitializer implements DataInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(FeeDataInitializer.class);
	private final FeeManagement feeManagement;


	FeeDataInitializer(FeeManagement feeManagement) {
		Assert.notNull(feeManagement, "Catalog must not be null!");
		this.feeManagement = feeManagement;
		}


		@Override
		public void initialize() {

			LOG.info("Creating default fee items");
			if (feeManagement.findAll().iterator().hasNext()) {
				return;
			}
			feeManagement.save(new Fee("1","Membership Fee", 17.25));
			feeManagement.save(new Fee("1","Liability Fee", 0.35));
			feeManagement.save(new Fee("1","Winter Service Fee", 3));
			feeManagement.save(new Fee("1","Social Contribution", 0.5));
			feeManagement.save(new Fee("1","Legal Protection Contribution", 0.75));
			feeManagement.save(new Fee("1","Expenses Flat", 12));
			feeManagement.save(new Fee("2","Water Meter Fee", 2.6));
			feeManagement.save(new Fee("2","Electricity Meter Fee", 1.55));

			LOG.info("fertig");
		}
}
