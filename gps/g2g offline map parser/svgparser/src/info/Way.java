package info;

import java.util.ArrayList;


/*
 * Author: Aliaksandr Karasiou 
 */

public class Way {
	private long wayid;    // id of way (.osm file)
	private ArrayList<Long> nds;    // ArrayList of references for this way (.osm file)
	private ArrayList<Tag> tags;   // ArrayList of tags for this way (.osm file)


/*
 *  getters and setters
 */
	
	public long getwayid() {
		return wayid;
	}

	public void setwayid(long newwayid) {
		wayid = newwayid;
	}

	public ArrayList<Long> getnds() {
		return nds;
	}

	public void setnds(ArrayList<Long> newnds) {
		nds = newnds;
	}
	
	public ArrayList<Tag> gettags() {
		return tags;
	}

	public void settags(ArrayList<Tag> newtags) {
		tags = newtags;
	}

/*
 *  constructor
 */
	
	Way() {
		wayid = 0;
		nds = new ArrayList<Long>();
		tags = new ArrayList<Tag>();
	}

}
