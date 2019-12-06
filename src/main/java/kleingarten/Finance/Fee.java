package kleingarten.Finance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * The Fee is generated on runtime, depending on the things in Procedure.
 *
 * Main use is to make PDF create easier.
 *
 * @author susho
 *
 */
@Entity
public abstract class Fee {

	private @Id @GeneratedValue long id;

	/**
	 * Title shown on Bill.
	 */
	String title;

	/**
	 * Text shown on Bill. (More Details)
	 */
	String text;

	/**
	 * Item count.
	 */
	double count;
	double defaultPrice;

	public Fee() {
		this.title = "DEFAULT_TITLE";
		this.text = "DEFAULT_TEXT";
		this.count = 1;
		this.defaultPrice = 1;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getDefaultPrice(int year) {
		return 1;
	}

}
