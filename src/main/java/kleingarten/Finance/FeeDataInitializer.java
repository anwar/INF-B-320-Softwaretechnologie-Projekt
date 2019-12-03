package kleingarten.Finance;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FeeDataInitializer implements DataInitializer {


	private static final Logger LOG = LoggerFactory.getLogger(FeeDataInitializer.class);
	List<Fee> feeList;

	@Override
	public void initialize() {


		Fee waterFee = new WaterFee();
		Fee powerFee = new PowerFee();
		Fee rent = new Rent();
		Fee pentalty = new Penalty();
		Fee membershipFee = new MembershipFee();
		Fee liabilityFee = new LiabilityFee();
		Fee winterServiceFee = new WinterServiceFee();
		Fee socialContribution = new SocialContribution();
		Fee legalProtectionFee = new LegalProtectionFee();
		Fee expensesFlat = new ExpensesFlat();
		Fee waterMeterFee = new WaterMeterFee();
		Fee electricityMeterFee = new ElectricityMeterFee();

		LOG.info("PREIS TEST01:   "+waterFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST02:   "+powerFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST03:   "+rent.getDefaultPrice(2018));
		LOG.info("PREIS TEST04:   "+pentalty.getDefaultPrice(2018));
		LOG.info("PREIS TEST05:   "+membershipFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST06:   "+liabilityFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST07:   "+winterServiceFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST08:   "+socialContribution.getDefaultPrice(2018));
		LOG.info("PREIS TEST09:   "+legalProtectionFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST10:   "+expensesFlat.getDefaultPrice(2018));
		LOG.info("PREIS TEST11:   "+waterMeterFee.getDefaultPrice(2018));
		LOG.info("PREIS TEST12:   "+electricityMeterFee.getDefaultPrice(2018));

		LOG.info("List:  " + feeList);


	}

}
