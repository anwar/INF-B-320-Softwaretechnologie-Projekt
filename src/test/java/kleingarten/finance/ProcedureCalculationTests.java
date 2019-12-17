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
	private Fee fee;
	private UserAccountManager userAccountManager;
	private TenantRepository tenantRepository;
	private Plot testPlot;
	private Procedure testProcedure;

	public ProcedureCalculationTests(){
	}

	@BeforeEach
	public void setup(){
		fee = new Fee("test", 0, 2);
		testPlot = plotService.addNewPlot("test", 300, "test");
		tenant = new Tenant("tester", "test", "Test Address",
				"123456789", "11.11.2000",
				userAccountManager.create("tester", Password.UnencryptedPassword.of("123"),
						"tester@gmail.com", Role.of("Hauptpächter")));
		tenantRepository.save(tenant);
		testProcedure = procedureManager.add(new Procedure(2019, testPlot, tenant.getId()));
	}

	@Test
	public void initialBasePriceTest() {
		assertThat(fee.getBasePrice()).isEqualTo(2);
	}

	@Test
	public void getProcedureTest(){
		assertThat(procedureManager.getProcedure(2019, testPlot)).isEqualTo(testProcedure);
	}

	@Test
	public void checkProcedure(){
		assertThat(testProcedure.getSize()).isEqualTo(300);
	}

	@Test
	public void checkRentPrice(){
		assertThat(testProcedure.getSize() * fee.getBasePrice()).isEqualTo(600);
	}

}
