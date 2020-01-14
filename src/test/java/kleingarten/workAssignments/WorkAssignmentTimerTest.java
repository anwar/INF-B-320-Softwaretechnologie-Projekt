package kleingarten.workAssignments;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;

import static kleingarten.appointment.WorkAssignmentTimer.timeDifference;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class WorkAssignmentTimerTest {

	@Test
	public void timeDifferenceTest() {
		LocalDateTime from = LocalDateTime.of(2020, Month.APRIL, 13, 19, 48, 12, 0);
		LocalDateTime to = LocalDateTime.of(2020, Month.APRIL, 14, 19, 48, 12, 0);
		assertTrue(timeDifference(from, to).equals("1d 0:00"));
		to = LocalDateTime.of(2020, Month.APRIL, 13, 19, 49, 11, 0);
		assertTrue(timeDifference(from, to).equals("0d 0:00"));
		to = LocalDateTime.of(2020, Month.APRIL, 13, 20, 48, 12, 0);
		assertTrue(timeDifference(from, to).equals("0d 1:00"));
		to = LocalDateTime.of(2020, Month.APRIL, 13, 19, 49, 12, 0);
		assertTrue(timeDifference(from, to).equals("0d 0:01"));
		to = LocalDateTime.of(2020, Month.OCTOBER, 24, 17, 23, 36, 0);
		assertTrue(timeDifference(from, to).equals("193d 21:35"));
	}
}
