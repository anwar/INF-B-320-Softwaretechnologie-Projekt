package kleingarten.plot;

import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.Role;

import javax.money.format.MonetaryFormats;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class PlotInformationBuffer {
	Plot associatedPlot;
	ProductIdentifier plotId;
	String plotName;
	int plotSize;
	String plotDescription;
	String plotPrice;
	Map<Tenant, String> mainTenantRoles;
	Map<Tenant, String> subTenantRoles;
	String workHours;

	public PlotInformationBuffer(final Plot plot) {
		associatedPlot = plot;
		plotId = plot.getId();
		plotName = plot.getName();
		plotSize = plot.getSize();
		plotDescription = plot.getDescription();
		plotPrice = MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getPrice());
		mainTenantRoles = new HashMap<>();
		subTenantRoles = new HashMap<>();
	}

	public String getPlotName() {
		return plotName;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}

	public int getPlotSize() {
		return plotSize;
	}

	public void setPlotSize(int plotSize) {
		this.plotSize = plotSize;
	}

	public String getPlotDescription() {
		return plotDescription;
	}

	public void setPlotDescription(String plotDescription) {
		this.plotDescription = plotDescription;
	}

	public String getPlotPrice() {
		return plotPrice;
	}

	public void setPlotPrice(String plotPrice) {
		this.plotPrice = plotPrice;
	}

	public Map<Tenant, String> getMainTenantRoles() {
		return mainTenantRoles;
	}

	public void setMainTenantRoles(Map<Tenant, String> mainTenantRole) {
		this.mainTenantRoles = mainTenantRole;
	}

	public Map<Tenant, String> getSubTenantRoles() {
		return subTenantRoles;
	}

	public void setSubTenantRoles(Map<Tenant, String> subTenantRoles) {
		this.subTenantRoles = subTenantRoles;
	}

	public String getWorkHours() {
		return workHours;
	}

	public void setWorkHours(String workHours) {
		this.workHours = workHours;
	}

	public Plot getAssociatedPlot() {
		return associatedPlot;
	}

	public void setAssociatedPlot(Plot associatedPlot) {
		this.associatedPlot = associatedPlot;
	}

	public ProductIdentifier getPlotId() {
		return plotId;
	}

	public void setPlotId(ProductIdentifier plotId) {
		this.plotId = plotId;
	}
}
