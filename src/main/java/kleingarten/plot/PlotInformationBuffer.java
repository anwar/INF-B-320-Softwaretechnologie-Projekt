package kleingarten.plot;

import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.ProductIdentifier;

import javax.money.format.MonetaryFormats;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class which saves all information of a {@link Plot} and the associated {@link kleingarten.finance.Procedure},
 * is needed to show the details of a specific {@link Plot}
 */
public class PlotInformationBuffer {
	ProductIdentifier plotId;
	String plotName;
	String plotSize;
	String plotDescription;
	String plotPrice;
	Map<Tenant, String> mainTenantRoles;
	Map<Tenant, String> subTenantRoles;
	String workHours;

	/**
	 * Constructor of the class
	 *
	 * @param plot associated {@link Plot} which details and information of the renting should be saved
	 */
	public PlotInformationBuffer(final Plot plot) {
		plotId = plot.getId();
		plotName = plot.getName();
		plotSize = plot.getSize() + " m²";
		plotDescription = plot.getDescription();
		plotPrice = MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getPrice()) + " Euro";
		mainTenantRoles = new HashMap<>();
		subTenantRoles = new HashMap<>();
	}

	/**
	 * Getter for the name of the associated {@link Plot}
	 *
	 * @return name of the {@link Plot} as {@link String}
	 */
	public String getPlotName() {
		return plotName;
	}

	/**
	 * Setter for the name of the associated {@link Plot}
	 *
	 * @param plotName name of the {@link Plot} as {@link String}
	 */
	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}

	/**
	 * Getter for the size of the associated {@link Plot}
	 *
	 * @return size of the {@link Plot} as int
	 */
	public String getPlotSize() {
		return plotSize;
	}

	/**
	 * Setter for the size of the associated {@link Plot}
	 *
	 * @param plotSize size of the {@link Plot} as int
	 */
	public void setPlotSize(String plotSize) {
		this.plotSize = plotSize + " m²";
	}

	/**
	 * Getter for the description of the associated {@link Plot}
	 *
	 * @return description of the {@link Plot} as {@link String}
	 */
	public String getPlotDescription() {
		return plotDescription;
	}

	/**
	 * Setter for the description of the associated {@link Plot}
	 *
	 * @param plotDescription description of the {@link Plot} as {@link String}
	 */
	public void setPlotDescription(String plotDescription) {
		this.plotDescription = plotDescription;
	}

	/**
	 * Getter for the price of the associated {@link Plot}
	 *
	 * @return price of the {@link Plot} as {@link String}
	 */
	public String getPlotPrice() {
		return plotPrice;
	}

	/**
	 * Setter for the price of the associated {@link Plot}
	 *
	 * @param plotPrice price of the {@link Plot} as {@link String}
	 */
	public void setPlotPrice(String plotPrice) {
		this.plotPrice = plotPrice + " Euro";
	}

	/**
	 * Getter for the information of the {@link Tenant} who rents the associated {@link Plot} and which
	 * {@link org.salespointframework.useraccount.Role}s he has
	 *
	 * @return {@link Map} with the {@link Tenant} and his {@link org.salespointframework.useraccount.Role}s
	 * as {@link String}
	 */
	public Map<Tenant, String> getMainTenantRoles() {
		return mainTenantRoles;
	}

	/**
	 * Setter for the information of the {@link Tenant} who rents the associated {@link Plot} and which
	 * {@link org.salespointframework.useraccount.Role}s he has
	 *
	 * @param mainTenantRole {@link Map} with the {@link Tenant} and his {@link org.salespointframework.useraccount.Role}s
	 *                       as {@link String}
	 */
	public void setMainTenantRoles(Map<Tenant, String> mainTenantRole) {
		this.mainTenantRoles = mainTenantRole;
	}

	/**
	 * Getter for the information of the {@link Tenant}s who rent the associated {@link Plot} and which
	 * {@link org.salespointframework.useraccount.Role}s they have
	 *
	 * @return {@link Map} with the {@link Tenant}s and their {@link org.salespointframework.useraccount.Role}s
	 * as {@link String}
	 */
	public Map<Tenant, String> getSubTenantRoles() {
		return subTenantRoles;
	}

	/**
	 * Setter for the information of the {@link Tenant}s who rent the associated {@link Plot} and which
	 * {@link org.salespointframework.useraccount.Role}s they have
	 *
	 * @param subTenantRoles {@link Map} with the {@link Tenant}s and their {@link org.salespointframework.useraccount.Role}s
	 *                       as {@link String}
	 */
	public void setSubTenantRoles(Map<Tenant, String> subTenantRoles) {
		this.subTenantRoles = subTenantRoles;
	}

	/**
	 * Getter for the work minutes of the associated {@link Plot}
	 *
	 * @return work minutes of the {@link Plot} as {@link String}
	 */
	public String getWorkHours() {
		return workHours;
	}

	/**
	 * Setter for the work minutes of the associated {@link Plot}
	 *
	 * @param workHours work minutes of the {@link Plot} as {@link String}
	 */
	public void setWorkHours(String workHours) {
		this.workHours = workHours + " min";
	}

	/**
	 * Getter for the {@link ProductIdentifier} of the associated {@link Plot}
	 *
	 * @return {@link ProductIdentifier} of the {@link Plot}
	 */
	public ProductIdentifier getPlotId() {
		return plotId;
	}

	/**
	 * Setter for the {@link ProductIdentifier} of the associated {@link Plot}
	 *
	 * @param plotId {@link ProductIdentifier} of the {@link Plot}
	 */
	public void setPlotId(ProductIdentifier plotId) {
		this.plotId = plotId;
	}
}
