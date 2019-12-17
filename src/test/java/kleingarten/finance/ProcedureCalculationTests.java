package kleingarten.finance;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotService;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProcedureCalculationTests {
	private Procedure procedure;
	private ProcedureManager procedureManager;
	private PlotService plotService;
	private Tenant tenant;
	private UserAccountManager userAccountManager;
	private TenantRepository tenantRepository;

	private Plot testPlot;
	private Procedure testProcedure;
	private Fee fee;
	private Fee fee1;
	private Fee fee2;

	public ProcedureCalculationTests(@Autowired PlotService plotService, @Autowired ProcedureManager procedureManager,
									 @Autowired UserAccountManager userAccountManager, @Autowired TenantRepository tenantRepository){
		this.plotService = plotService;
		this.procedureManager = procedureManager;
		this.userAccountManager = userAccountManager;
		this.tenantRepository = tenantRepository;
	}

	@BeforeEach
	public void setup(){
		fee = new Fee("rent", 0, 2);
		fee1 = new Fee("water", 0, 1);
		fee2 = new Fee("power", 0, 1);

		testPlot = plotService.addNewPlot("test", 300, "test");
		tenant = new Tenant("tester", "test", "Test Address",
				"123456789", "11.11.2000",
				userAccountManager.create("tester", Password.UnencryptedPassword.of("123"),
						"tester@gmail.com", Role.of("Hauptpächter")));
		tenantRepository.save(tenant);
		testProcedure = procedureManager.add(new Procedure(2019, testPlot, tenant.getId()));
	}

	/**
	 * Check Baseprice from fee
	 */
	@Test
	public void initialBasePriceTest() {
		assertThat(fee.getBasePrice()).isEqualTo(2);
	}

	/**
	 * check whether the created/added Procedure by testPlot is same as testProcedure
	 */
	@Test
	public void getProcedureTest(){
		assertThat(procedureManager.getProcedure(2019, testPlot)).isEqualTo(testProcedure);
	}

	/**
	 * check the plot size in testProcedure (which is created by testPlot
	 */
	@Test
	public void checkSize(){
		assertThat(testProcedure.getSize()).isEqualTo(300);
	}

	/**
	 * check the waterCount in testProcedure. It should be 0 because it's not given
	 */
	@Test
	public void checkWater(){
		assertThat(testProcedure.getWatercount()).isEqualTo(0);
	}

	/**
	 * check the powerCount in testProcedure. It should be 0 because it's not given
	 */
	@Test
	public void checkPower(){
		assertThat(testProcedure.getPowercount()).isEqualTo(0);
	}

	/**
	 * check the rent Price of the testPlot
	 */
	@Test
	public void checkRentPrice(){
		assertThat(testProcedure.getSize() * fee.getBasePrice()).isEqualTo(600);
	}

	/**
	 * check the water price of the testPlot
	 */
	@Test
	public void checkWaterPrice(){assertThat(testProcedure.getWatercount() * fee1.getBasePrice()).isEqualTo(0);}

	/**
	 * check the power price of the testPlot
	 */
	@Test
	public void checkPowerPrice(){assertThat(testProcedure.getPowercount() * fee2.getBasePrice()).isEqualTo(0);}

}
