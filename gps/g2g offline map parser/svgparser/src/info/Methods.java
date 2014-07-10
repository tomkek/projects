package info;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Author : Aliaksandr Karasiou 
 */

public class Methods {

	/*
	 * ArrayList for nodes from .osm file
	 */

	public ArrayList<Node> nodes = new ArrayList<Node>();

	/*
	 * HashMap with a key = node id, value = position in ArrayList of nodes
	 */

	public HashMap<Long, Integer> fornodes = new HashMap<Long, Integer>();

	/*
	 * // ArrayList for saving all the ways from .osm file
	 */
	public ArrayList<Way> ways = new ArrayList<Way>();

	/*
	 * // HashMap with a key = way id, value = position in ArrayList of nodes
	 */

	public HashMap<Long, Integer> forways = new HashMap<Long, Integer>();

	/*
	 * ArrayList for saving all the relations from .osm file
	 */

	public ArrayList<Relation> relations = new ArrayList<Relation>();

	/*
	 * ArrayList for saving ways which represent all the roads on the map
	 */

	public ArrayList<Way> navigation = new ArrayList<Way>();

	/*
	 * ArrayList for saving all the edges
	 */

	public ArrayList<Edge> edges = new ArrayList<Edge>();

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
	 * converting all the information from .osm file, filling ArrayList of
	 * nodes, ways and relations for nodes i'm taking only tags i will use in
	 * order to make program faster afterwards
	 */

	public void saving(BufferedReader br) {

		System.out.println("Im in nodes");
		String read = "";
		ArrayList<Long> nds = new ArrayList<Long>();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ArrayList<Member> members = new ArrayList<Member>();

		try {
			while ((read = br.readLine()) != null) {
				String[] temp = read.split(" ");

				// setting bounds of map

				if (temp[0].equals("<bounds")) {
					minx = Double.parseDouble(temp[1]);
					miny = Double.parseDouble(temp[2]);
					maxx = Double.parseDouble(temp[3]);
					maxy = Double.parseDouble(temp[4]);
				}
				if (temp[0].equals("<node")
						&& temp[temp.length - 1].equals("/>")) {

					// saving node

					Node q = new Node();
					q.setid(Long.parseLong(temp[1]));
					q.setlat(Double.parseDouble(temp[2]));
					q.setlon(Double.parseDouble(temp[3]));
					nodes.add(q);

				}
				if (temp[0].equals("<node")
						&& !temp[temp.length - 1].equals("/>")) {

					// saving node with tags

					Node q = new Node();
					q.setid(Long.parseLong(temp[1]));
					q.setlat(Double.parseDouble(temp[2]));
					q.setlon(Double.parseDouble(temp[3]));

					read = br.readLine();
					tags = new ArrayList<Tag>();
					temp = read.split(" ");

					while (!temp[0].equals("</node>")) {
						Tag tag = new Tag();
						if (temp[1].equals("amenity")
								|| temp[1].equals("cuisine")
								|| temp[1].equals("website")
								|| temp[1].equals("phone")
								|| temp[1].equals("contact:phone")
								|| temp[1].equals("brand")
								|| temp[1].equals("opening_hours")
								|| temp[1].equals("email")
								|| temp[1].equals("name")
								|| temp[1].equals("place")
								|| temp[1].equals("shop")
								|| temp[1].equals("tourism")) {

							tag.setk(temp[1]);

							StringBuilder sb = new StringBuilder();

							for (int i = 2; i < temp.length; i++) {
								sb.append(temp[i] + " ");
							}
							sb.replace(sb.length() - 4, sb.length(), "");

							tag.setv(sb.toString());
							tags.add(tag);

						}

						read = br.readLine();
						temp = read.split(" ");
					}

					q.settags(tags);
					nodes.add(q);

				}

				if (temp[0].equals("<way")) {

					// saving way

					Way w = new Way();
					long id = Long.parseLong(temp[1]);
					w.setwayid(id);
					read = br.readLine();
					temp = read.split(" ");
					nds = new ArrayList<Long>();
					tags = new ArrayList<Tag>();

					while (!temp[0].equals("</way>")) {

						// saving references for this way

						if (temp[0].equals("<nd")) {
							nds.add(Long.parseLong(temp[1]));
						}

						// saving tags for this way

						if (temp[0].equals("<tag")) {
							Tag tag = new Tag();
							tag.setk(temp[1]);
							tag.setv(read.substring(
									temp[0].length() + temp[1].length() + 2,
									read.length() - 3));
							tags.add(tag);
						}
						read = br.readLine();
						temp = read.split(" ");
					}

					w.setnds(nds);
					w.settags(tags);
					ways.add(w);
				}

				if (temp[0].equals("<relation")) {

					// saving relation

					Relation r = new Relation();
					r.setrelationid(Long.parseLong(temp[1]));

					read = br.readLine();
					temp = read.split(" ");
					members = new ArrayList<Member>();
					tags = new ArrayList<Tag>();

					while (!temp[0].equals("</relation>")) {

						// saving members of this relation

						if (temp[0].equals("<member")) {
							Member m = new Member();
							m.settype(temp[1]);
							m.setref(Long.parseLong(temp[2]));
							m.setrole(temp[3]);
							members.add(m);
						}

						// saving tags of this relation

						if (temp[0].equals("<tag")) {
							Tag tag = new Tag();
							tag.setk(temp[1]);
							tag.setv(read.substring(
									temp[0].length() + temp[1].length() + 2,
									read.length() - 3));
							tags.add(tag);
						}
						read = br.readLine();
						temp = read.split(" ");
					}

					r.setmembers(members);
					r.settags(tags);
					relations.add(r);
				}
			}

			System.out.println(nodes.size());
			System.out.println(ways.size());
			System.out.println(relations.size());

			/*
			 * a method to eliminate duplicates , it is useful if you download
			 * several .osm files and combine them together. it is for big maps
			 * (from openstreetmap web-site you can download a file with max
			 * 50.000 nodes)
			 */

//			dublicatesrelation(relations);
//			dublicatesways(ways);
//			dublicatesnodes(nodes);

			System.out.println();
			System.out.println("after cleaning    " + relations.size());
			System.out.println("after cleaning    " + ways.size());
			System.out.println("after cleaning    " + nodes.size());

			// filling HashMaps

			for (int i = 0; i < nodes.size(); i++) {
				fornodes.put(nodes.get(i).getid(), i);
			}

			for (int i = 0; i < ways.size(); i++) {
				forways.put(ways.get(i).getwayid(), i);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("FINISHED!");

	}

	// method for setting all map layers (.svg format) and information about all
	// roads

	public ArrayList<StringBuilder> LoadInfo(BufferedReader br) {

		ArrayList<StringBuilder> layers = new ArrayList<StringBuilder>();

		navigation = new ArrayList<Way>(); // here i will put all the roads

		/*
		 * each StringBuilder represents each layer
		 */

		StringBuilder l0 = new StringBuilder();
		StringBuilder l1 = new StringBuilder();
		StringBuilder l2 = new StringBuilder();
		StringBuilder l3 = new StringBuilder();
		StringBuilder l4 = new StringBuilder();
		StringBuilder l5 = new StringBuilder();

		/*
		 * setting parameters for .svg format
		 */

		l0.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");
		l1.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");
		l2.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");
		l3.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");
		l4.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");
		l5.append("<svg xmlns= \"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"1000px\" height=\"1000px\">  \n");

		l0.append("<polyline fill=\"#ffffff\" stroke-width=\"1\" points=\" 0,0 "
				+ (maxx - minx)
				* x
				+ ",0 "
				+ (maxx - minx)
				* x
				+ ","
				+ (maxy - miny) * y + " 0," + (maxy - miny) * y + " \"/>");

		// going through all the ways

		for (int i = 0; i < ways.size(); i++) {

			// creating temporary ArrayList of nodes for 1 way

			ArrayList<Node> item = new ArrayList<Node>();

			// filling it with he help of HashMap

			for (int j = 0; j < ways.get(i).getnds().size(); j++) {
				long temp = ways.get(i).getnds().get(j);
				int temp2 = fornodes.get(temp);
				item.add(nodes.get(temp2));
			}

			/*
			 * here i check tags of each way and according to tags I add a way
			 * to one of the layers with its own parameters (color, opacity,
			 * stroke wigth etc.
			 */

			if (!ways.get(i).gettags().isEmpty()) {
				for (int u = 0; u < ways.get(i).gettags().size(); u++) {
					if (ways.get(i).gettags().get(u).getk().equals("leisure")) {
						if (ways.get(i).gettags().get(u).getv()
								.equals("garden")
								|| ways.get(i).gettags().get(u).getv()
										.equals("golf_course")
								|| ways.get(i).gettags().get(u).getv()
										.equals("nature_reserve")
								|| ways.get(i).gettags().get(u).getv()
										.equals("park")
								|| ways.get(i).gettags().get(u).getv()
										.equals("pitch")) {
							l2.append("<polyline fill=\"#00C100\" opacity=\".4\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}
						if (ways.get(i).gettags().get(u).getv()
								.equals("stadium")) {
							l3.append("<polyline fill=\"#A0A0A0\" stroke-width=\"1\" points=\"");
							setpoints(item, l3);
						}

					}

					if (ways.get(i).gettags().get(u).getk().equals("sport")) {
						if (ways.get(i).gettags().get(u).getv().equals("golf")
								|| ways.get(i).gettags().get(u).getv()
										.equals("soccer")
								|| ways.get(i).gettags().get(u).getv()
										.equals("tennis")) {
							l2.append("<polyline fill=\"#00C100\" opacity=\".4\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}
						if (ways.get(i).gettags().get(u).getv()
								.equals("swimming")) {
							l2.append("<polyline fill=\"#0080FF\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}

					}

					if (ways.get(i).gettags().get(u).getk().equals("landuse")) {
						if (ways.get(i).gettags().get(u).getv()
								.equals("construction")) {

							l3.append("<polyline fill=\"#808000\" stroke-width=\"1\" points=\"");
							setpoints(item, l3);
						}
						if (ways.get(i).gettags().get(u).getv()
								.equals("forest")
								|| ways.get(i).gettags().get(u).getv()
										.equals("grass")
								|| ways.get(i).gettags().get(u).getv()
										.equals("greenfield")
								|| ways.get(i).gettags().get(u).getv()
										.equals("wood")) {
							l2.append("<polyline fill=\"#00C100\" opacity=\".4\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}

						if (ways.get(i).gettags().get(u).getv()
								.equals("industrial")) {

							l2.append("<polyline fill=\"#757575\" opacity=\".2\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}
						if (ways.get(i).gettags().get(u).getv()
								.equals("cemetery")) {

							l2.append("<polyline fill=\"#00C100\" opacity=\".2\" stroke-width=\"1\" points=\"");
							setpoints(item, l2);
						}

					}

					if (ways.get(i).gettags().get(u).getk().equals("building")) {
						l3.append("<polyline fill=\"#A0A0A0\" stroke-width=\"1\" points=\"");
						setpoints(item, l3);
					}

					if (ways.get(i).gettags().get(u).getv().equals("suburb")) {
						l1.append("<polyline fill=\"#FFFFFF\" stroke-width=\"1\" points=\"");
						setpoints(item, l1);
					}

					if (ways.get(i).gettags().get(u).getk().equals("natural")) {
						if (ways.get(i).gettags().get(u).getv()
								.equals("coastline")) {
							l0.append("<polyline fill=\"#0080FF\" stroke-width=\"1\" points=\"");
							setpoints(item, l0);
							if (ways.get(i).gettags().get(u).getv()
									.equals("water")) {
								l2.append("<polyline fill=\"#0080FF\" stroke-width=\"1\" points=\"");
								setpoints(item, l2);
							}
							if (ways.get(i).gettags().get(u).getv()
									.equals("wood")) {
								l2.append("<polyline fill=\"#00C100\" opacity=\".4\" stroke-width=\"1\" points=\"");
								setpoints(item, l2);
							}
							if (ways.get(i).gettags().get(u).getv()
									.equals("beach")) {
								l2.append("<polyline fill=\"#F4F039\" opacity=\".5\" stroke-width=\"1\" points=\"");
								setpoints(item, l2);
							}
						}
					}

					if (ways.get(i).gettags().get(u).getk().equals("waterway")) {
						if (ways.get(i).gettags().get(u).getv().equals("canal")
								|| ways.get(i).gettags().get(u).getv()
										.equals("river")
								|| ways.get(i).gettags().get(u).getv()
										.equals("stream")) {
							l2.append("<polyline stroke=\"#0080FF\" stroke-width=\"2\" points=\"");
							setpoints(item, l2);

							if (ways.get(i).gettags().get(u).getv()
									.equals("riverbank")) {
								l2.append("<polyline fill=\"#0080FF\" stroke-width=\"1\" points=\"");
								setpoints(item, l2);
							}
						}
					}

					if (ways.get(i).gettags().get(u).getv().equals("parking")) {
						l3.append("<polyline fill=\"#A3B6D3\" stroke-width=\"1\" points=\"");
						setpoints(item, l3);
					}

					if (ways.get(i).gettags().get(u).getk().equals("highway")) {
						/*
						 * when there is a way with tag key "highway" i add it
						 * to ArrayList of roads
						 */
						navigation.add(ways.get(i));

						if (ways.get(i).gettags().get(u).getv()
								.equals("footway")
								|| ways.get(i).gettags().get(u).getv()
										.equals("path")
								|| ways.get(i).gettags().get(u).getv()
										.equals("cycleway")
								|| ways.get(i).gettags().get(u).getv()
										.equals("service")
								|| ways.get(i).gettags().get(u).getv()
										.equals("living_street")
								|| ways.get(i).gettags().get(u).getv()
										.equals("steps")
								|| ways.get(i).gettags().get(u).getv()
										.equals("services")
								|| ways.get(i).gettags().get(u).getv()
										.equals("track")
								|| ways.get(i).gettags().get(u).getv()
										.equals("pedestrian")) {
							l4.append("<polyline stroke=\"#00FF00\" stroke-width=\".6\" points=\"");
							setpoints(item, l4);

							for (int h = 0; h < ways.get(i).gettags().size(); h++) {
								if ((ways.get(i).gettags().get(h).getk()
										.equals("bridge") || ways.get(i)
										.gettags().get(h).getk()
										.equals("tunnel"))
										&& ways.get(i).gettags().get(h).getv()
												.equals("yes")) {
									l4.append("<polyline stroke=\"#00A500\" opacity=\".8\" stroke-width=\"0.6\" points=\"");
									setpoints(item, l4);
								}
							}

							if (ways.get(i).gettags().get(u).getv()
									.equals("pedestrian")) {
								l2.append("<polyline fill=\"#00FF00\" opacity=\".4\" stroke-width=\"0.8\" points=\"");
								setpoints(item, l2);
							}
						}

						if (ways.get(i).gettags().get(u).getv()
								.equals("residential")
								|| ways.get(i).gettags().get(u).getv()
										.equals("unclassified")
								|| ways.get(i).gettags().get(u).getv()
										.equals("secondary")
								|| ways.get(i).gettags().get(u).getv()
										.equals("tertiary")
								|| ways.get(i).gettags().get(u).getv()
										.equals("secondary_link")
								|| ways.get(i).gettags().get(u).getv()
										.equals("road")
								|| ways.get(i).gettags().get(u).getv()
										.equals("raceway")
								|| ways.get(i).gettags().get(u).getv()
										.equals("primary")
								|| ways.get(i).gettags().get(u).getv()
										.equals("primary_link")) {
							l4.append("<polyline stroke=\"#EBDD2E\" stroke-width=\"2\" points=\"");
							setpoints(item, l4);

							for (int h = 0; h < ways.get(i).gettags().size(); h++) {
								if ((ways.get(i).gettags().get(h).getk()
										.equals("bridge") || ways.get(i)
										.gettags().get(h).getk()
										.equals("tunnel"))
										&& ways.get(i).gettags().get(h).getv()
												.equals("yes")) {
									l4.append("<polyline stroke=\"#C6B813\" opacity=\".8\" stroke-width=\"2\" points=\"");
									setpoints(item, l4);
								}
							}
						}

						if (ways.get(i).gettags().get(u).getv().equals("trunk")
								|| ways.get(i).gettags().get(u).getv()
										.equals("motorway")
								|| ways.get(i).gettags().get(u).getv()
										.equals("trunk_link")
								|| ways.get(i).gettags().get(u).getv()
										.equals("motorway_link")) {
							l5.append("<polyline stroke=\"#008200\" stroke-width=\"2\" points=\"");
							setpoints(item, l5);

							for (int h = 0; h < ways.get(i).gettags().size(); h++) {
								if ((ways.get(i).gettags().get(h).getk()
										.equals("bridge") || ways.get(i)
										.gettags().get(h).getk()
										.equals("tunnel"))
										&& ways.get(i).gettags().get(h).getv()
												.equals("yes")) {
									l5.append("<polyline stroke=\"#005B00\" opacity=\".8\" stroke-width=\"2\" points=\"");
									setpoints(item, l5);
								}
							}
						}
					}
					if (ways.get(i).gettags().get(u).getk().equals("man_made")) {
						l4.append("<polyline stroke=\"#2F4F4F\" stroke-width=\"1\" points=\"");
						setpoints(item, l4);
					}

					if (ways.get(i).gettags().get(u).getk().equals("railway")) {
						if (ways.get(i).gettags().get(u).getv().equals("rail")
								|| ways.get(i).gettags().get(u).getv()
										.equals("turntable")) {
							l4.append("<polyline stroke=\"#000000\"  opacity=\".9\" stroke-width=\"0.8\" points=\"");
							setpoints(item, l4);
						}
						if (ways.get(i).gettags().get(u).getv()
								.equals("platform")) {
							l2.append("<polyline fill=\"#00FF00\" opacity=\".4\" stroke-width=\"0.8\" points=\"");
							setpoints(item, l2);
						}
					}

				}
			}

		}

		for (int i = 0; i < relations.size(); i++) {
			if (!relations.get(i).gettags().isEmpty()) {
				if (relations.get(i).gettags().get(0).getk().equals("building")
						&& relations.get(i).gettags().get(1).getv()
								.equals("multipolygon")) {

					for (int j = 0; j < relations.get(i).getmembers().size(); j++) {
						if (relations.get(i).getmembers().get(j).getrole()
								.equals("outer")) {

							ArrayList<Node> item = new ArrayList<Node>();

							long ref = relations.get(i).getmembers().get(j)
									.getref();

							int position = forways.get(ref);
							Way w = ways.get(position);

							for (int j2 = 0; j2 < w.getnds().size(); j2++) {
								long temp = w.getnds().get(j2);
								int temp2 = fornodes.get(temp);
								item.add(nodes.get(temp2));
							}

							l3.append("<polyline fill=\"#A0A0A0\" stroke-width=\"1\" points=\"");
							setpoints(item, l3);
						} else {
							ArrayList<Node> item = new ArrayList<Node>();

							long ref = relations.get(i).getmembers().get(j)
									.getref();
							try {
								int position = forways.get(ref);
								Way w = ways.get(position);

								for (int j2 = 0; j2 < w.getnds().size(); j2++) {
									long temp = w.getnds().get(j2);
									int temp2 = fornodes.get(temp);
									item.add(nodes.get(temp2));
								}

								l3.append("<polyline fill=\"#FFFFFF\" stroke-width=\"1\" points=\"");
								setpoints(item, l3);
							} catch (NullPointerException e) {
								e.getMessage();
							}

						}

					}
				}
			}

		}

		/*
		 * setboundary method is for putting black background on places which
		 * are out of bounds of a map according to coordinates for map
		 * boundaries
		 */

		setboundary(l0);
		setboundary(l1);
		setboundary(l2);
		setboundary(l3);
		setboundary(l4);
		setboundary(l5);

		l0.append("</svg>");
		l1.append("</svg>");
		l2.append("</svg>");
		l3.append("</svg>");
		l4.append("</svg>");
		l5.append("</svg>");

		// adding layers and navigation info to ArrayList
		layers.add(l0);
		layers.add(l1);
		layers.add(l2);
		layers.add(l3);
		layers.add(l4);
		layers.add(l5);
		layers.add(navigation());

		return layers;
	}

	/*
	 * a method for setting boundaries of a map to draw
	 */

	private void setboundary(StringBuilder s) {

		s.append("<polyline fill=\"#000000\" stroke-width=\"1\" points=\"");
		s.append("-10000,-10000 10000,-10000 10000,0 -10000,0 \"/>  \n");

		s.append("<polyline fill=\"#000000\" stroke-width=\"1\" points=\"");
		s.append((maxx - minx) * x + ",-10000 10000,-10000 10000,10000 "
				+ (maxx - minx) * x + ",10000 \"/>  \n");

		s.append("<polyline fill=\"#000000\" stroke-width=\"1\" points=\"");
		s.append("-10000," + (maxy - miny) * y + " 10000," + (maxy - miny) * y
				+ " 10000,10000 -10000,10000 \"/>  \n");

		s.append("<polyline fill=\"#000000\" stroke-width=\"1\" points=\"");
		s.append("-10000,-10000 0,-10000 0,10000 -10000,10000 \"/>  \n");

	}

	// a method which sets variables for boundaries

	public void setbounds(BufferedReader br) {
		try {
			String read = "";

			read = br.readLine();
			String[] temp = read.split(" ");
			if (temp[0].equals("<bounds")) {
				minx = Double.parseDouble(temp[1]);
				miny = Double.parseDouble(temp[2]);
				maxx = Double.parseDouble(temp[3]);
				maxy = Double.parseDouble(temp[4]);

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// creating a StringBuilder for all the roads in order to save it to file

	private StringBuilder navigation() {

		StringBuilder sb = new StringBuilder();

		sb.append(minx + " " + maxx + " " + miny + " " + maxy + "\n");
		for (int i = 0; i < navigation.size(); i++) {

			ArrayList<Node> item = new ArrayList<Node>();

			for (int j = 0; j < navigation.get(i).getnds().size(); j++) {
				long temp = navigation.get(i).getnds().get(j);
				int temp2 = fornodes.get(temp);
				item.add(nodes.get(temp2));
			}

			sb.append("w " + navigation.get(i).getwayid() + "\n");

			for (int j = 0; j < item.size(); j++) {
				sb.append(item.get(j).getid() + " " + item.get(j).getlat()
						+ " " + item.get(j).getlon() + "\n");
			}
			sb.append("/w" + "\n");

		}

		return sb;
	}

	// getting ArrayList of nodes which represent all existing suburbs' names
	// according to tags

	public ArrayList<Node> suburbnames() {
		ArrayList<Node> suburbs = new ArrayList<Node>();
		for (int i = 0; i < nodes.size(); i++) {
			if (!nodes.get(i).gettags().isEmpty()) {
				for (int u = 0; u < nodes.get(i).gettags().size(); u++) {
					if (nodes.get(i).gettags().get(u).getv().equals("suburb")) {
						suburbs.add(nodes.get(i));
					}
				}
			}
		}
		return suburbs;
	}

	/*
	 * a method which takes string with name of POI and checks if it exists in
	 * tags for a node. If it is, method will arrange tags in a proper way for
	 * me to read it afterwards from file in android application. Returns
	 * StringBuilder in order to save to .txt file.
	 */

	public StringBuilder poi(String place) {
		ArrayList<Node> res = new ArrayList<Node>();
		for (int i = 0; i < nodes.size(); i++) {
			if (!nodes.get(i).gettags().isEmpty()) {
				for (int u = 0; u < nodes.get(i).gettags().size(); u++) {

					if (nodes.get(i).gettags().get(u).getv().equals(place)) {

						ArrayList<Tag> tags = new ArrayList<Tag>();
						Node e = new Node();
						tags = nodes.get(i).gettags();
						ArrayList<Tag> newtags = new ArrayList<Tag>();

						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("name")) {
								newtags.add(tags.get(j));
							}
						}
						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("brand")) {
								newtags.add(tags.get(j));
							}
						}

						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("cuisine")) {
								newtags.add(tags.get(j));
							}
						}
						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("phone")
									|| tags.get(j).getk()
											.equals("contact:phone")) {
								newtags.add(tags.get(j));
							}
						}
						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("opening_hours")) {
								newtags.add(tags.get(j));
							}
						}
						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("website")) {
								newtags.add(tags.get(j));
							}
						}
						for (int j = 0; j < tags.size(); j++) {

							if (tags.get(j).getk().equals("email")) {
								newtags.add(tags.get(j));
							}
						}

						e.setlat(nodes.get(i).getlat());
						e.setlon(nodes.get(i).getlon());
						e.settags(newtags);
						res.add(e);

					}
				}
			}
		}

		StringBuilder sbpoi = new StringBuilder();

		for (int i = 0; i < res.size(); i++) {
			sbpoi.append("r " + res.get(i).getlat() + " " + res.get(i).getlon()
					+ "\n");
			for (int j = 0; j < res.get(i).gettags().size(); j++) {
				sbpoi.append(res.get(i).gettags().get(j).getk() + " "
						+ res.get(i).gettags().get(j).getv() + "\n");
			}
			if (res.get(i).gettags().size() == 0) {
				sbpoi.append("name Uknown " + place + "\n");
				sbpoi.append("/r\n");
			} else {
				sbpoi.append("/r\n");
			}

		}

		return sbpoi;
	}

	// method for eliminating dublicates of relations

	private void dublicatesrelation(ArrayList<Relation> array) {
		int size = array.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			count++;
			long id = array.get(i).getrelationid();
			for (int j = 0; j < size; j++) {

				if (j != i && id == array.get(j).getrelationid()) {
					array.remove(j);
					size--;
				}
			}
		}
	}

	// method for eliminating dublicates of ways

	private void dublicatesways(ArrayList<Way> array) {
		int size = array.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			count++;
			long id = array.get(i).getwayid();
			for (int j = 0; j < size; j++) {

				if (j != i && id == array.get(j).getwayid()) {
					array.remove(j);
					size--;
				}
			}
		}
	}

	// method for eliminating dublicates of nodes
	private void dublicatesnodes(ArrayList<Node> array) {
		int size = array.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			count++;
			long id = array.get(i).getid();
			for (int j = 0; j < size; j++) {

				if (j != i && id == array.get(j).getid()) {
					array.remove(j);
					size--;
				}
			}
		}
	}

	/*
	 * method for setting coordinates for drawing stuff. (setting to
	 * StringBuilder)
	 */

	private void setpoints(ArrayList<Node> item, StringBuilder sb) {
		for (int j = 0; j < item.size(); j++) {
			float x1 = (float) (item.get(j).getlat() - minx) * x;
			float y1 = (float) (item.get(j).getlon() - miny) * y;
			sb.append(x1 + "," + y1 + " ");
		}
		sb.append("\"/>  \n");
	}

	/*
	 * method for setting vertexes, eliminating dublicates and converting to
	 * StringBuilder
	 */

	public StringBuilder setdijinfo() {
		System.out.println("WAYS " + ways.size());

		System.out.println("NAVIG " + navigation.size());

		ArrayList<Vertex> vertexes = new ArrayList<Vertex>();

		for (int i = 0; i < navigation.size(); i++) {

			for (int j = 0; j < navigation.get(i).getnds().size(); j++) {
				Vertex v = new Vertex();
				v.setid(navigation.get(i).getnds().get(j));
				vertexes.add(v);
			}
		}

		for (int i = 0; i < vertexes.size(); i++) {

			ArrayList<Edge> adj = new ArrayList<Edge>();

			for (int j = 0; j < edges.size(); j++) {

				if (vertexes.get(i).getid() == edges.get(j).getStart()
						|| vertexes.get(i).getid() == edges.get(j).getEnd()) {
					adj.add(edges.get(j));
				}

			}
			vertexes.get(i).setAdjacencies(adj);

		}

		int y = vertexes.size();
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < y; j++) {
				if (vertexes.get(i).getid() == vertexes.get(j).getid()
						&& j != i) {
					vertexes.remove(j);
					y--;
				}
			}
		}

		StringBuilder sbvertexes = new StringBuilder();

		for (int i = 0; i < vertexes.size(); i++) {
			sbvertexes.append("v " + vertexes.get(i).getid() + "\n");

			for (int j = 0; j < vertexes.get(i).getAdjacencies().size(); j++) {
				sbvertexes.append(vertexes.get(i).getAdjacencies().get(j)
						.getStart()
						+ " "
						+ vertexes.get(i).getAdjacencies().get(j).getEnd()
						+ "\n");
			}
			sbvertexes.append("/v\n");
		}

		return sbvertexes;
	}

	/*
	 * method for setting edges, eliminating dublicates and converting to
	 * StringBuilder
	 */

	public StringBuilder edges() {

		StringBuilder sbedges = new StringBuilder();

		for (int i = 0; i < navigation.size(); i++) {
			for (int j = 0; j < navigation.get(i).getnds().size(); j++) {

				if (j == 0) {

					Edge edge = new Edge();

					edge.setStart(navigation.get(i).getnds().get(j));

					edge.setEnd(navigation.get(i).getnds().get(j + 1));

					edges.add(edge);

				}

				if (j == navigation.get(i).getnds().size() - 1) {

					Edge edge = new Edge();

					edge.setStart(navigation.get(i).getnds().get(j));

					edge.setEnd(navigation.get(i).getnds().get(j - 1));

					edges.add(edge);

				}

				if (j > 0 && j < navigation.get(i).getnds().size() - 1) {

					Edge edge = new Edge();

					edge.setStart(navigation.get(i).getnds().get(j));

					edge.setEnd(navigation.get(i).getnds().get(j + 1));
					edges.add(edge);

					edge = new Edge();

					edge.setStart(navigation.get(i).getnds().get(j));

					edge.setEnd(navigation.get(i).getnds().get(j - 1));

					edges.add(edge);
				}

			}
		}

		int h = edges.size();

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				if (edges.get(i).getStart() == edges.get(j).getEnd()
						&& edges.get(i).getEnd() == edges.get(j).getStart()) {
					edges.remove(j);
					h--;
				}
			}
		}

		for (int i = 0; i < edges.size(); i++) {
			sbedges.append("e " + edges.get(i).getStart() + " "
					+ edges.get(i).getEnd() + "\n");
		}
		return sbedges;
	}

}