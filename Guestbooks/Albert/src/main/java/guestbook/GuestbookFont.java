package guestbook;

import com.sun.jdi.request.StepRequest;

public class GuestbookFont {

	private int fontBigger;
	private int fontSmaller;


	public GuestbookFont(int fontBigger, int fontSmaller){
		this.fontBigger = fontBigger;
		this.fontSmaller = fontSmaller;
	}

	public void setFontBigger(int fontBigger) {
		if(fontBigger < 0) {
			this.fontBigger++;
		}
	}

	public void setFontSmaller(int fontSmaller) {
		if(fontSmaller > 0){
			this.fontSmaller --;
		}
	}

	public String getFontBigger() {
		return Integer.toString(fontBigger);
	}

	public String getFontSmaller() {
		return Integer.toString(fontSmaller);
	}
}
