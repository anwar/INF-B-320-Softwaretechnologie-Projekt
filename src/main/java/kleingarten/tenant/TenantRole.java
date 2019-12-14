package kleingarten.tenant;

import org.salespointframework.useraccount.Role;
import org.springframework.web.client.HttpClientErrorException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TenantRole implements Comparable {

	private Role role;

	/**
	 * Class to specify if a {@link Role} is unique
	 */
	private static class RoleEntry{

		String name;
		Boolean unique;

		/**
		 * Private constructor of class {@link RoleEntry}, which is used by the Spring Framework
		 */
		private RoleEntry(){}

		/**
		 * public constructor for the {@link RoleEntry}
		 * @param name of the {@link Role}
		 * @param unique status of the {@link Role}
		 */
		public RoleEntry(String name, Boolean unique){
			this.name = name;
			this.unique = unique;
		}
	}

	/**
	 * List is a sorted list to compare the streamable of {@link Role} of a {@link Tenant}
	 */
	static private final List<RoleEntry> list = new LinkedList<RoleEntry>();
	static {
		list.add(new RoleEntry("Hauptpächter", Boolean.FALSE));
		list.add(new RoleEntry("Nebenpächter", Boolean.FALSE));
		list.add(new RoleEntry("Vorstandsvorsitzender", Boolean.TRUE));
		list.add(new RoleEntry("Protokollant", Boolean.FALSE));
		list.add(new RoleEntry("Stellvertreter", Boolean.TRUE));
		list.add(new RoleEntry("Obmann", Boolean.FALSE));
		list.add(new RoleEntry("Kassierer", Boolean.TRUE));
		list.add(new RoleEntry("Wassermann", Boolean.FALSE));
	}

	/**
	 * Method to get the index of a {@link Role} in {@link List}
	 * @param name of {@link Role} to get the index of
	 * @return index of {@link Role} in the list as {@link Integer}
	 * or throws new {@link NullPointerException}
	 */
	Integer indexOf(String name)
	{
		for(RoleEntry l : list){
			if(l.name.equals(name)){
				return list.indexOf(l);
			}
		}
		throw new NullPointerException();
	}

	/**
	 * Constructor of {@link TenantRole} to be used by Spring
	 * @param role of type {@link Role}
	 */
	public TenantRole(Role role){
		this.role = role;
	}


	/**
	 * Private Constructor of {@link TenantRole} to be used by Spring
	 */
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
		return Integer.compare(indexOf(this.toString()), indexOf(r.toString()));
	}

	/**
	 * @return {@link List} of {@link Role}s
	 */
	public static List<String> getRoleList(){
		return list.stream().map(n -> n.name).collect(Collectors.toList());
	}

	/**
	 * @return {@link List} of unique {@link Role}s
	 */
	public static List<String> getUniqueRoleList()
	{
		return list.stream().filter(n -> n.unique).map(n -> n.name).collect(Collectors.toList());
	}

	/**
	 * Method tests if a {@link Role} is unique
	 * @param role to be tested if it's unique
	 * @return
	 */
	public static Boolean isUnique(String role){
		return getUniqueRoleList().contains(role);
	}

}
