package sasha.offline.info;


public class Edge {
	
	private long startPoint;
	private long endPoint;
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
	public double getWeight(){
		return weight;
	}
	public void setWeight(double newWeight){
		weight = newWeight;
	}
	public Edge(){
		long startPoint = 0;
		long endPoint = 0;
		double weight = 0;
	}
	
}
