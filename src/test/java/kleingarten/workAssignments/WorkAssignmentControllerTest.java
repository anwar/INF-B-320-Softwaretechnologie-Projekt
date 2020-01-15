package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignmentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkAssignmentControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	WorkAssignmentController workAssignmentController;


	/**
	 * @throws Exception test founding of listOfAssignments
	 */
	@Test
	void preventsPublicAccessForListOfAssignments() throws Exception {

		mvc.perform(get("/listOfAssignments")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	/**
	 * @throws Exception test founding of createAssignment
	 */
	@Test
	void preventsPublicAccessForCreateAssignment() throws Exception {

		mvc.perform(get("/createAssignment")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	/**
	 * @throws Exception test founding of workAssignmentModify
	 */
	@Test
	void preventsPublicAccessForWorkAssignmentModify() throws Exception {

		mvc.perform(get("/workAssignmentModify?id=10")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	/**
	 * @throws Exception test founding of addWorkHours
	 */
	@Test
	void preventsPublicAccessAddWorkHours() throws Exception {

		mvc.perform(get("/addWorkHours")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}
}
