package kleingarten.Finance;

public class ExpensesFlat extends Fee {

	public ExpensesFlat(){
		this.title = "Aufwandspauschale";
		this.text = "";
		this.count = 1;
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.EXPENSESFLAT_DEFAULT;
	}
}
