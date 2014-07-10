package sasha.offline.info;


/*
 * Author : Aliaksandr Karasiou
 */

public class Tag {
	private String k;   // Tag key
	private String v;   // Tag value

	/*
	 * getters and setters
	 */
	
	public Tag(String k, String v) {
		this.k = k;
		this.v = v;
	}

	public Tag() {
		k = "";
		v = "";
	}

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

