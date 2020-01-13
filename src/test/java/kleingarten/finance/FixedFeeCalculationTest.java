package kleingarten.finance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FixedFeeCalculationTest {
	private Fee fee;

	@BeforeEach
	public void setup() {
		fee = new Fee("test", 10, 1.95);
	}


	@Test
	public void initialTitleTest() {
		assertThat(fee.getTitle()).isEqualTo("test");
	}

	@Test
	public void initialCountTest() {
		assertThat(fee.getCount()).isEqualTo(10);
	}

	@Test
	public void initialBasePriceTest() {
		assertThat(fee.getBasePrice()).isEqualTo(1.95);
	}

	@Test
	public void initialPriceTest() {
		assertThat(fee.getPrice()).isEqualTo(19.5);
	}

}

