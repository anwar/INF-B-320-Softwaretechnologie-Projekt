package kleingarten.plotManagement;

import org.springframework.data.util.Streamable;

import javax.validation.constraints.NotEmpty;

class PlotRegistrationForm {

	@NotEmpty(message = "{PlotRegistrationForm.size.NotEmpty}")
	private final String size;

	@NotEmpty(message = "{PlotRegistrationForm.indicator.NotEmpty}")
	private final String indicator;

	@NotEmpty(message = "{PlotRegistrationForm.description.NotEmpty}")
	private final String description;


	public PlotRegistrationForm(String size, String indicator, String description) {
		this.size = size;
		this.indicator = indicator;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getSize() {
		return size;
	}

	public String getIndicator() {
		return indicator;
	}
}
