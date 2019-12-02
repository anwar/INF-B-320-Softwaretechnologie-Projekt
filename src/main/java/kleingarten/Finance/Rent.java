package kleingarten.Finance;

public class Rent extends Fee {
/*	Procedure procedure;

	public Rent(){
		this.title = "Miete";
		this.text = "";
		this.count = procedure.getSize();	// Need to work on later
	}
*/
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.PLOT_DEFAULT;
	}
}
