
package sasha.offline.info;
import java.util.ArrayList;


/*
 * Author : Aliaksandr Karasiou
 */

public class Way {
	
	private long wayid;           // Way id
	private ArrayList<Long> nodes; // ArrayList of nodes' ids for this Way  

	/*
	 * getters and setters 
	 */
	
	public long getwayid() {
		return wayid;
	}

	public void setwayid(long newwayid) {
		wayid = newwayid;
	}

	public ArrayList<Long> getnodes() {
		return nodes;
	}

	public void setnodes(ArrayList<Long> newnodes) {
		nodes = newnodes;
	}

	/*
	 * Way constructor
	 */
	public Way() {
		wayid = 0;
		nodes = new ArrayList<Long>();
	}

}

