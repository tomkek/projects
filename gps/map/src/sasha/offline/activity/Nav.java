package sasha.offline.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import sasha.offline.activity.R;
import sasha.offline.info.Edge;
import sasha.offline.info.Node;
import sasha.offline.info.Vertex;
import sasha.offline.info.Way;
import android.content.Context;


/*
 * Author: Aliaksandr Karasiou
 */
public class Nav {

   	public ArrayList<Node> nodes = new ArrayList<Node>();    // ArrayList of nodes for navigation
	public ArrayList<Way> ways = new ArrayList<Way>();       // ArrayList of ways for navigation
	public ArrayList<Edge> edges = new ArrayList<Edge>();    // ArrayList of edges for navigation
	public ArrayList<Vertex> vertexes = new ArrayList<Vertex>();   // ArrayList of vertexes for navigation

	public HashMap<Long, ArrayList<Edge>> forvertexes = new HashMap<Long, ArrayList<Edge>>(); // HashMap for vertexes

	/*
	 * minimum and maximum x, y coordinates for map boundaries 
	 */
	public static double minx;     
	public static double miny;   

	public static double maxx;
	public static double maxy;

	
	/*
	 * "static value for map"
	 */
	public static int x = 40000;
	public static int y = 20000;

	/*
	 * This method reads file nav.txt , converts information and sets boundaries
	 * of map
	 */
	public void savebounds(Context context) throws IOException {

		InputStream is = context.getResources().openRawResource(R.raw.nav);
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		String[] temp = input.readLine().split(" ");

		minx = Double.parseDouble(temp[0]);
		maxx = Double.parseDouble(temp[1]);
		miny = Double.parseDouble(temp[2]);
		maxy = Double.parseDouble(temp[3]);
	}

	/*
	 * This method reads files nav.txt , edges.txt , vertexes.txt, converts all information
	 * and sets ArrayList of nodes, ways, edges, vertexes.
	 */
	
	public void savenav(Context context) throws IOException {

		InputStream is = context.getResources().openRawResource(R.raw.nav);
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		String read = "";
		String[] temp = null;

		while ((read = input.readLine()) != null) {
			temp = read.split(" ");

			if (temp[0].equals("w")) {
				Way w = new Way();
				w.setwayid(Long.parseLong(temp[1]));

				read = input.readLine();
				temp = read.split(" ");

				ArrayList<Long> ref = new ArrayList<Long>();

				while (!temp[0].equals("/w")) {

					Node node = new Node();
					node.setid(Long.parseLong(temp[0]));
					node.setlat(Double.parseDouble(temp[1]));
					node.setlon(Double.parseDouble(temp[2]));

					nodes.add(node);
					ref.add(node.getid());

					read = input.readLine();
					temp = read.split(" ");
				}

				w.setnodes(ref);
				ways.add(w);
			}
		}

		InputStream is2 = context.getResources().openRawResource(R.raw.edges);
		BufferedReader input2 = new BufferedReader(new InputStreamReader(is2));

		String read2 = "";
		String[] temp2 = null;

		while ((read2 = input2.readLine()) != null) {
			temp2 = read2.split(" ");

			Edge edge = new Edge();

			edge.setStart(Long.parseLong(temp2[1]));
			edge.setEnd(Long.parseLong(temp2[2]));

			edges.add(edge);
		}

		InputStream is3 = context.getResources()
				.openRawResource(R.raw.vertexes);
		BufferedReader input3 = new BufferedReader(new InputStreamReader(is3));

		String read3 = "";
		String[] temp3 = null;

		while ((read3 = input3.readLine()) != null) {
			temp3 = read3.split(" ");
			if (temp3[0].equals("v")) {
				Vertex v = new Vertex();
				v.setid(Long.parseLong(temp3[1]));
				read3 = input3.readLine();
				temp3 = read3.split(" ");

				ArrayList<Edge> veredges = new ArrayList<Edge>();

				while (!temp3[0].equals("/v")) {

					Edge edge = new Edge();
					edge.setStart(Long.parseLong(temp3[0]));
					edge.setEnd(Long.parseLong(temp3[1]));

					veredges.add(edge);
					read3 = input3.readLine();
					temp3 = read3.split(" ");
				}

				v.setAdjacencies(veredges);
				vertexes.add(v);
			}
		}

		for (int i = 0; i < vertexes.size(); i++) {
			forvertexes.put(vertexes.get(i).getid(), vertexes.get(i)
					.getAdjacencies());
		}
	}

}