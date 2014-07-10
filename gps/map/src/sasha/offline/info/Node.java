package sasha.offline.info;

import java.util.ArrayList;

/*
 * Author : Aliaksandr Karasiou
 */
public class Node {
	private long id; // Node id
	private double lat; // Node latitude
	private double lon; // Node longitude
	private String name; // Node name
	private ArrayList<Tag> tags; // ArrayList of tags for this Node

	
	/*
	 * getters and setters
	 */
	public long getid() {
		return id;
	}

	public void setid(long newid) {
		id = newid;
	}

	public double getlat() {
		return lat;
	}

	public void setlat(double newlat) {
		lat = newlat;
	}

	public double getlon() {
		return lon;
	}

	public void setlon(double newlon) {
		lon = newlon;
	}

	public String getname() {
		return name;
	}

	public void setname(String newname) {
		name = newname;
	}

	public ArrayList<Tag> gettags() {
		return tags;
	}

	public void settags(ArrayList<Tag> newtags) {
		tags = newtags;
	}

		/*
		 * Node constructor
		 */
	public Node() {
		id = 0;
		lat = 0;
		lon = 0;
		name = "";
		tags = new ArrayList<Tag>();
	}

}
