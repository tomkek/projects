package info;


/*
 *   Author : Aliaksandr Karasiou 
 */


public class Tag {
	private String k; // key value of the tag (.osm file)
	private String v;  // "value" value of the tag (.osm file) 

	
	/*
	 * constructor
	 */
	
	Tag() {
		k = "";
		v = "";
	}

	
	/*
	 * getters and setters
	 */
	
	public String getk() {
		return k;
	}

	public void setk(String newk) {
		k = newk;
	}

	public String getv() {
		return v;
	}

	public void setv(String newv) {
		v = newv;
	}

}
