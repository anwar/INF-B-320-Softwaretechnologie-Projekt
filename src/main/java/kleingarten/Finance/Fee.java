package kleingarten.Finance;

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
public abstract class Fee {
	
	

	/**
	 * Title shown on Bill.
	 */
	String title;
	
	/**
	 * Text shown on Bill.
	 */
	String text;
	
	/**
	 * Item count.
	 */
	double count;
	
	public Fee() {
		this.title="DEFAULT_TITLE";
		this.text="DEFAULT_TEXT";
		
		this.count = 1;
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	public double getCount(){
		return count;
	}
	
	public double getDefaultPrice(int year) {
		return 1;
	}
	
}
