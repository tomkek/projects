package info;

/*
 * Author : Tomasz Rakalski ( detailed explanation is in android application code)
 */

public class Edge {
	private long startPoint;
	private long endPoint;
	private long id;
	private double weight;
	
	public long getStart(){
		return startPoint;
	}
	public long getEnd(){
		return endPoint;
	}
	public void setStart(long newId){
		startPoint = newId;
	}
	public void setEnd(long newId){
		endPoint = newId;
	}
	public long getID(){
		return id;
	}
	public void setID(long newID){
		id = newID;
	}
	public double getWeight(){
		return weight;
	}
	public void setWeight(double newWeight){
		weight = newWeight;
	}
	Edge(){
		long startPoint = 0;
		long endPoint = 0;
		long id = 0;
		double weight = 0;
	}
	
}
