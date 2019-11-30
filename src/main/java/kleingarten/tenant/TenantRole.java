package kleingarten.tenant;

import org.salespointframework.useraccount.Role;

import java.util.LinkedList;
import java.util.List;

public class TenantRole implements Comparable {

	private Role role;
	static private final List<String> list = new LinkedList<String>();
	static {
		list.add("Hauptpächter");
		list.add("Nebenpächter");
		list.add("Vorstandsvorsitzender");
		list.add("Protokollant");
		list.add("Stellvertreter");
		list.add("Obmann");
		list.add("Kassierer");
		list.add("Wassermann");
	}

	public TenantRole(Role role){
		this.role = role;
	}
	private TenantRole(){
	}

	@Override
	public String toString(){
		return this.role.toString();
	}

	@Override
	public int compareTo(Object o) {
		TenantRole r = (TenantRole) o;
		return Integer.compare(list.indexOf(this.toString()), list.indexOf(r.toString()));
	}

}
