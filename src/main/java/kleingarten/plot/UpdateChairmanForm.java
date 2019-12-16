package kleingarten.plot;

import org.salespointframework.catalog.ProductIdentifier;

import java.util.List;

public class UpdateChairmanForm {
	private List<ProductIdentifier> updatedPlots;

	public UpdateChairmanForm() {
	}

	public UpdateChairmanForm(List<ProductIdentifier> updatedPlots) {
		this.updatedPlots = updatedPlots;
	}

	public List<ProductIdentifier> getUpdatedPlots() {
		return updatedPlots;
	}

	public void setUpdatedPlots(List<ProductIdentifier> updatedPlots) {
		this.updatedPlots = updatedPlots;
	}
}
