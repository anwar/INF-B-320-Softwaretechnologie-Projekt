package kleingarten.plot;

import org.salespointframework.catalog.ProductIdentifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UpdateChairmanForm {
	private List<ProductIdentifier> updatedPlots;

	public UpdateChairmanForm() {}

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
