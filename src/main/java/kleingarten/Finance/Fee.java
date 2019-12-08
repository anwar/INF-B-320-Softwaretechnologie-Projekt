package kleingarten.Finance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fees")
public class Fee {
	public Fee(){}

	private @Id @GeneratedValue long id;

	private String title;
	private double count;
	private double basePrice;
	private double price;
	private double sum = 0;

	/**
	 * Constructor of fee
	 * @param title
	 * @param count
	 * @param basePrice
	 * @param price
	 */
	public Fee(String title, double count, double basePrice, double price){

		this.title = title;
		this.count = count;
		this.basePrice = basePrice;
		this.price = basePrice * count;
		this.sum += price;
		this.id = id;
	}

	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}

	public double getPrice(){
		return basePrice * count;
	}

	public void setPrice(double price){
		this.price = price;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	public long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	@Override
	public String toString() {

		var builder = new StringBuilder();
		builder.append("Fee{id=").append(id).append(", title=")
				.append(title).append(", count=")
				.append(count).append(", basePrice=")
				.append(basePrice).append(", price=")
				.append(price).append("}");

		return builder.toString();
	}

	public double getSum() {
		return sum += price;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
}
