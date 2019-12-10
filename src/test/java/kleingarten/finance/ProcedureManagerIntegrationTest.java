package kleingarten.finance;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kleingarten.finance.ProcedureManager;

@SpringBootTest
@Transactional
public class ProcedureManagerIntegrationTest {

	public ProcedureManagerIntegrationTest(@Autowired ProcedureManager pm) {

	}

}

