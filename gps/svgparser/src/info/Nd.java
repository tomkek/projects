package info;

/*
 * Author : Aliaksandr Karasiou
 */

public class Nd {
	private long ref;   // "ref" value in nd ( .osm file)

	/*
	 * getter and setter
	 */
	
	public long getref() {
		return ref;
	}

	public void setref(long newref) {
		ref = newref;
	}

	/*
	 * constructor 
	 */
	
	Nd() {
		ref = 0;
	}
}
