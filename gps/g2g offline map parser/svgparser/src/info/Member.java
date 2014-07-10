package info;


/*
 * Author : Aliaksandr Karasiou 
 */

public class Member {

	private String type;     // for "type" value in member (in .osm file)
	private long ref;       // for "ref" value in member (in .osm file)
	private String role;   // for "role" value in member (in .osm file)

	
	/*
	 * getters and setters 
	 */
	
	public String gettype() {
		return type;
	}

	public void settype(String newtype) {
		type = newtype;
	}

	public long getref() {
		return ref;
	}

	public void setref(long newref) {
		ref = newref;
	}

	public String getrole() {
		return role;
	}

	public void setrole(String newrole) {
		role = newrole;
	}
  
	/*
	 * constructor 
	 */
	
	Member() {
		type = "";
		ref = 0;
		role = "";
	}
}
