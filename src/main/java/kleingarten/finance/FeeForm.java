package kleingarten.finance;

import org.hibernate.validator.constraints.Range;
import java.util.*;

public class FeeForm {

	private String title;
	private double count;
	private double basePrice;
	private double price;
	private long id;

	public String getTitle() {

		return title;
	}
	public void setTitle(String title) {

		this.title = title;
	}

	public double getPrice() {

		return basePrice * count;
	}
	public void setPrice(double price) {

		this.price = price;
	}
	public long getId() {
		return id;
	}

	public void setId(long id){
		this.id = id;
	}

	@Range(min = 0, message = "count can not be lower than 0")
	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
}
