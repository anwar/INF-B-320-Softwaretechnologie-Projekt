package kleingarten.tenant;

import org.salespointframework.useraccount.Role;

import java.util.LinkedList;
import java.util.List;

public class TenantRole implements Comparable {

	private Role role;
	/**
	 * List is a sorted list to compare the streamable of {@link Role} of a {@link Tenant}
	 */
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

	/**
	 * Constructor of {@link TenantRole} to be used by Spring
	 * @param role of type {@link Role}
	 */
	public TenantRole(Role role){
		this.role = role;
	}
	private TenantRole(){
	}

	/**
	 * Method to cast a {@link Role} into a String
	 * @return {@link Role} as String
	 */
	@Override
	public String toString(){
		return this.role.toString();
	}

	/**
	 * Method to compare a {@link Role} of a {@link Tenant} to an object in the list to sort the {@link Role} of the
	 * {@link Tenant}
	 * @param o {@link Object} to compare to
	 * @return Integer for comparing lists
	 */
	@Override
	public int compareTo(Object o) {
		TenantRole r = (TenantRole) o;
		return Integer.compare(list.indexOf(this.toString()), list.indexOf(r.toString()));
	}

	public static List<String> getRoleList(){
		return list;
	}

}
