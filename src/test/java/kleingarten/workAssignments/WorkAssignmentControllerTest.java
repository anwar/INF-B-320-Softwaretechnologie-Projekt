package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkAssignmentControllerTest {

	@Autowired MockMvc mvc;
	@Autowired WorkAssignmentController workAssignmentController;


	@Test
	void preventsPublicAccessForListOfAssignments() throws Exception {

		mvc.perform(get("/listOfAssignments")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	@Test
	void preventsPublicAccessForCreateAssignment() throws Exception {

		mvc.perform(get("/createAssignment")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	@Test
	void preventsPublicAccessForWorkAssignmentModify() throws Exception {

		mvc.perform(get("/workAssignmentModify?id=10")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}
	@Test
	void preventsPublicAccessAddWorkHours() throws Exception {

		mvc.perform(get("/addWorkHours")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}
}
