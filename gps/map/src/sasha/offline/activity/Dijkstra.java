package sasha.offline.activity;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import sasha.offline.info.Edge;
import sasha.offline.info.Node;
import sasha.offline.info.Vertex;
import sasha.offline.info.Way;

public class Dijkstra {
	HashMap<Long, Node> nodes = new HashMap<Long, Node>();
	HashMap<Long, Way> ways = new HashMap<Long, Way>();
	HashMap<Long, Vertex> vertexes = new HashMap<Long, Vertex>();
	
   public  ArrayList <Node> coolpath = new ArrayList<Node>();
    
	Node n, m;
	double newWeight;

	public void Dijkstra(ArrayList<Node> supernodes, ArrayList<Way> superways,
			ArrayList<Edge> superedges,HashMap<Long, ArrayList<Edge>> forver, long point1, long point2) {
		


		for (int i = 0; i < supernodes.size(); i++) {
			nodes.put(supernodes.get(i).getid(), supernodes.get(i));
		}
		for (int i = 0; i < superways.size(); i++) {
			ways.put(superways.get(i).getwayid(), superways.get(i));
		}

		Iterator iterator = nodes.entrySet().iterator();
		for (int i = 1; i < supernodes.size(); i++) {
			n = supernodes.get(i);
			m = supernodes.get(i - 1);
			newWeight = weight(m, n);
			for (int j = 0; j < superedges.size(); j++) {
				if (superedges.get(j).getStart() == n.getid()) {
					superedges.get(j).setWeight(newWeight);
				}
				if (superedges.get(j).getEnd() == n.getid()) {
					superedges.get(j).setWeight(newWeight);
				}
			}
		}
		
		
		while (iterator.hasNext()) {
			Entry<Long, Node> set = (Entry<Long, Node>) iterator.next();
			Long id = set.getKey();
			Vertex v = new Vertex();
			v.setid(id);

		  v.setAdjacencies(forver.get(v.getid()));
			
			vertexes.put(set.getKey(), v);
		}
		iterator = ways.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Long, Way> set = (Entry<Long, Way>) iterator.next();
		}
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex start = vertexes.get(point1);
		Vertex end = vertexes.get(point2);
		computePaths(start);
		path = getShortestPathTo(end);
		
		coolpath = new ArrayList<Node>();
		for (int i = 0; i < path.size(); i++) {
			coolpath.add(nodes.get(path.get(i).getid()));
		}
	}

	public void calculate() {

	}

	public void computePaths(Vertex source) {
		Vertex v;
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				if (e.getEnd() == u.getid()) {
					v = vertexes.get(e.getStart());
				} else 
					v = vertexes.get(e.getEnd());

				double weight = e.getWeight();
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);

				}

			}
		}
	}

	public List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;

	}

	public Vertex getNeighbors(ArrayList<Node> nodes, ArrayList<Edge> edges,
			Vertex v, Long id) {
		ArrayList<Edge> adj = new ArrayList<Edge>();
		if (id == v.getid()) {
			for (int j = 0; j < edges.size(); j++) {
				for (int i = 0; i < nodes.size(); i++) {
					if ((edges.get(j).getStart() == nodes.get(i).getid())  && (edges.get(j).getEnd() == id)){
						adj.add(edges.get(j));
					}
					if ((edges.get(j).getEnd() == nodes.get(i).getid()) && (edges.get(j).getStart() == id)){
						adj.add(edges.get(j));
					}
				}
			}
			v.setAdjacencies(adj);
		}
		return v;

	}

	public double weight(Node m, Node n) {
		double weight = 0;
		double mlat, mlon, nlat, nlon, finlat, finlon;
		mlat = m.getlat();
		mlon = m.getlon();
		nlat = n.getlat();
		nlon = n.getlon();

		finlat = (mlat - nlat) * (mlat - nlat);
		finlon = (mlon - nlon) * (mlon - nlon);
		weight = Math.sqrt(finlat + finlon);
		weight = weight * 100000;
		return weight;
	}
}
