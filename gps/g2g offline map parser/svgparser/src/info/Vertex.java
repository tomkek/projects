package info;
import java.util.ArrayList;

/*
 * Author : Tomasz Rakalski ( detailed explanation is in android application code)
 */

public class Vertex implements Comparable {

	public Long id;
	public ArrayList<Edge> adjacencies = new ArrayList<Edge>();
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;

	public void setAdjacencies(ArrayList<Edge> newA) {
		adjacencies = newA;
	}

	public ArrayList<Edge> getAdjacencies() {
		return adjacencies;
	}

	public void setid(long newid) {
		id = newid;
	}

	public long getid() {
		return id;
	}

	public Vertex() {
		id = (long) 0;
		adjacencies = new ArrayList<Edge>();
		minDistance = Double.POSITIVE_INFINITY;
	}

	public int compareTo(Object arg0) {
		if (this.minDistance > ((Vertex) arg0).minDistance) {
			return +1;
		} else if (this.minDistance == ((Vertex) arg0).minDistance){
			return 0;
		} else
			return -1;
	}

}
