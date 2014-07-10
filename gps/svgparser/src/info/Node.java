package info;

import java.util.ArrayList;


/*
 *  Author : Aliaksandr Karasiou
 */

public class Node {
	private long id;   // id value for Node (.osm file)
	private double lat;  // latitude value for Node (.osm file)
	private double lon;  // longitute value for Node (.osm file)
	private ArrayList<Tag> tags;   // ArrayList of tags for this node (.osm file)

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

	public ArrayList<Tag> gettags() {
		return tags;
	}

	public void settags(ArrayList<Tag> newtags) {
		tags = newtags;
	}

	/*
	 * constructor 
	 */
	
	Node() {
		id = 0;
		lat = 0;
		lon = 0;
		tags = new ArrayList<Tag>();
	}

}
