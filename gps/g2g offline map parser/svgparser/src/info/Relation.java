package info;

import java.util.ArrayList;


/*
 * Author : Aliaksandr Karasiou 
 */

public class Relation {
	
	private long relationid;   // id of relation ( .osm file)
	private ArrayList<Member> members;   // Arraylist of members for this relation(.osm file)
	private ArrayList<Tag> tags;           // ArrayList of tags for this relation (.osm file)

	/*
	 * getters and setters 
	 */
	
	public long getrelationid() {
		return relationid;
	}

	public void setrelationid(long newrelationid) {
		relationid = newrelationid;
	}

	public ArrayList<Member> getmembers() {
		return members;
	}

	public void setmembers(ArrayList<Member> newmembers) {
		members = newmembers;
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
	
	Relation() {
		relationid = 0;
		members = new ArrayList<Member>();
		tags = new ArrayList<Tag>();
	}
}
