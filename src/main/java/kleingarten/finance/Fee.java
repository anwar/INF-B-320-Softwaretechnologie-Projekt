package kleingarten.finance;

public class Fee {

	private String title;
	private double count;
	private double basePrice;
	private double sum = 0;

	/**
	 * Construct a Fee item. Used to generate a Bill as PDF.
	 *
	 * @param title     as String
	 * @param count     as double
	 * @param basePrice as double
	 */
	public Fee(String title, double count, double basePrice) {

		this.title = title;
		this.count = count;
		this.basePrice = basePrice;
	}

	public String getTitle() {
		return title;
	}

	public double getPrice() {
		return basePrice * count;
	}

	public double getCount() {
		return count;
	}

	public double getBasePrice() {
		return basePrice;
	}


	@Override
	public String toString() {

		var builder = new StringBuilder();
		builder.append("Fee{").append(", title=")
				.append(title).append(", count=")
				.append(count).append(", basePrice=")
				.append(basePrice).append(", price=")
				.append(basePrice * count).append("}");

		return builder.toString();
	}
}
