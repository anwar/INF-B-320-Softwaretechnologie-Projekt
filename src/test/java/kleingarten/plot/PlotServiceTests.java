package kleingarten.plot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class PlotServiceTests {
	private PlotService plotService;

	public PlotServiceTests(@Autowired PlotService plotService) {
		this.plotService = plotService;
	}

	@Test
	public void nameExistsTest() {
		assertThat(plotService.existsByName("2")).isEqualTo(true);
	}

	@Test
	public void nameExistsNotTest() {
		assertThat(plotService.existsByName("33")).isEqualTo(false);
	}

	@Test
	public void illegalPlotNameTest() {
		assertThrows(IllegalArgumentException.class, () -> plotService.addNewPlot("2", 200, "test"));
	}

}
