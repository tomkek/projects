package info;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;


/*
 * Author : Aliaksandr Karasiou
 */

public class Cleaning {

	public static void main(String[] args) throws IOException {
		
		/*
		 * File Chooser to choose a file (OpenStreetMap files)
		 */
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			FileReader reader = new FileReader(file);
			BufferedReader input = new BufferedReader(reader);
			
			/*
			 * StringBuilder "read" is the main one, where the whole information is saved in converted format (im taking
			 *  from file only specific information i need)
			 * 
			 */
			StringBuilder read = new StringBuilder();
			StringBuilder t = new StringBuilder();
			String currentLine = "";
			while (input.ready()) {
				currentLine = input.readLine();
				if (!currentLine.isEmpty()) {

					String[] temp = currentLine.split(" ");
					if (temp.length > 1) {
						if (temp[1].equals("<bounds")) {
							t = new StringBuilder();
							t.append(temp[1] + " ");
							t.append(temp[2].substring(8, temp[2].length() - 1)
									+ " ");
							t.append(temp[3].substring(8, temp[3].length() - 1)
									+ " ");
							t.append(temp[4].substring(8, temp[4].length() - 1)
									+ " ");
							t.append(temp[5].substring(8, temp[5].length() - 3)
									+ " ");
							t.append(temp[5].substring(temp[5].length() - 2,
									temp[5].length()) + " ");
							read.append(t + "\n");
						}

						if (temp[1].equals("<node")) {
							t = new StringBuilder();
							t.append(temp[1] + " ");
							t.append(temp[2].substring(4, temp[2].length() - 1)
									+ " ");
							t.append(temp[3].substring(5, temp[3].length() - 1)
									+ " ");
							t.append(temp[4].substring(5, temp[4].length() - 1)
									+ " ");
							String check = temp[temp.length - 1].substring(
									temp[temp.length - 1].length() - 2,
									temp[temp.length - 1].length());
							if (check.equals("\">")) {
								check = ">";
							}
							t.append(check);
							read.append(t + "\n");
						}
						if (temp[1].equals("</node>")) {
							t = new StringBuilder();
							t.append(temp[1]);
							read.append(t + "\n");
						}

						if (temp[1].equals("<way")) {
							t = new StringBuilder();
							t.append(temp[1] + " ");
							t.append(temp[2].substring(4, temp[2].length() - 1)
									+ " ");
							t.append(">");
							read.append(t + "\n");
						}
						if (temp[1].equals("</way>")) {
							t = new StringBuilder();
							t.append(temp[1]);
							read.append(t + "\n");
						}

						if (temp[1].equals("<relation")) {
							t = new StringBuilder();
							t.append(temp[1] + " ");
							t.append(temp[2].substring(4, temp[2].length() - 1)
									+ " ");
							t.append(">");
							read.append(t + "\n");
						}
						if (temp[1].equals("</relation>")) {
							t = new StringBuilder();
							t.append(temp[1]);
							read.append(t + "\n");
						}

						if (temp.length > 2) {
							if (temp[2].equals("<tag")) {
								t = new StringBuilder();
								t.append(temp[2] + " ");
								temp = currentLine.split("\"");

								t.append(temp[1] + " ");
								t.append(temp[3] + " ");
								t.append("/>");
								read.append(t + "\n");
							}
							if (temp[2].equals("<nd")) {
								t = new StringBuilder();
								t.append(temp[2] + " ");
								t.append(temp[3].substring(5,
										temp[3].length() - 3) + " ");
								t.append("/>");
								read.append(t + "\n");
							}
							if (temp[2].equals("<member")) {
								t = new StringBuilder();
								t.append(temp[2] + " ");
								t.append(temp[3].substring(6,
										temp[3].length() - 1) + " ");
								t.append(temp[4].substring(5,
										temp[4].length() - 1) + " ");
								t.append(temp[5].substring(6,
										temp[5].length() - 3) + " ");
								t.append("/>");
								read.append(t + "\n");
							}

						}
					}
				}
			}

			/*
			 * create an object of class Methods
			 */
			Methods e = new Methods();
			ArrayList<StringBuilder> layers = new ArrayList<StringBuilder>();
			
			InputStream is = new ByteArrayInputStream(read.toString()
					.getBytes());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			/*
			 * call a method to save all the information
			 */
			
			e.saving(br);
			
			
			/*
			 * call a method which returns all the nodes which represent suburbs' names
			 */
			
			ArrayList<Node> suburbs = e.suburbnames();
			
			StringBuilder sbsub = new StringBuilder();
			
			/*
			 * convert suburbs to StringBuilder in a proper way in order to save info to text file afterwards  
			 */
			
			for (int i = 0; i < suburbs.size(); i++){
				sbsub.append(suburbs.get(i).getlat()+" "+ suburbs.get(i).getlon()+"\n");
                for (int j = 0; j < suburbs.get(i).gettags().size(); j++){
                	if (suburbs.get(i).gettags().get(j).getk().equals("name")){
                		 sbsub.append(suburbs.get(i).gettags().get(j).getv()+"\n");
                	}
                }
			}
			
			
			/*
			 * convert POI to StringBuilders in a proper way in order to save info to text file afterwards 
			 * (calling a method for this)
			 */		
			StringBuilder sbres = e.poi("restaurant");
			StringBuilder sbcafes = e.poi("cafe");
			StringBuilder sbfastfood = e.poi("fast_food");
			StringBuilder sbpub = e.poi("pub");
			StringBuilder sbshop = e.poi("supermarket");
			StringBuilder sbhotel = e.poi("hotel");
			StringBuilder sbhospital = e.poi("hospital");
			StringBuilder sbgas = e.poi("fuel");
		    	
			/*
			 * Here I save all POIs to separate text files
			 */
			FileWriter outfilex9 = new FileWriter("fuel.txt");
			BufferedWriter outx9 = new BufferedWriter(outfilex9);
			outx9.write(sbgas.toString());
			outx9.close();
			
			FileWriter outfilex8 = new FileWriter("hospital.txt");
			BufferedWriter outx8 = new BufferedWriter(outfilex8);
			outx8.write(sbhospital.toString());
			outx8.close();
			
			FileWriter outfilex7 = new FileWriter("hotel.txt");
			BufferedWriter outx7 = new BufferedWriter(outfilex7);
			outx7.write(sbhotel.toString());
			outx7.close();
			
			FileWriter outfilex6 = new FileWriter("supermarket.txt");
			BufferedWriter outx6 = new BufferedWriter(outfilex6);
			outx6.write(sbshop.toString());
			outx6.close();
			
			FileWriter outfilex5 = new FileWriter("pub.txt");
			BufferedWriter outx5 = new BufferedWriter(outfilex5);
			outx5.write(sbpub.toString());
			outx5.close();
			
			
			FileWriter outfilex4 = new FileWriter("fastfood.txt");
			BufferedWriter outx4 = new BufferedWriter(outfilex4);
			outx4.write(sbfastfood.toString());
			outx4.close();	
			
			FileWriter outfilex3 = new FileWriter("cafes.txt");
			BufferedWriter outx3 = new BufferedWriter(outfilex3);
			outx3.write(sbcafes.toString());
			outx3.close();	
			
			FileWriter outfilex2 = new FileWriter("restaurants.txt");
			BufferedWriter outx2 = new BufferedWriter(outfilex2);
			outx2.write(sbres.toString());
			outx2.close();
			
			/*
			 * saving suburbs' names to separate text file
			 */
			
			FileWriter outfilex1 = new FileWriter("suburbs.txt");
			BufferedWriter outx1 = new BufferedWriter(outfilex1);
			outx1.write(sbsub.toString());
			outx1.close();
			
			

			


			
			/*
			 * calling a method to get layers of map and information of all the roads on the map
			 */
			
			layers = e.LoadInfo(br);
			
			/*
			 * saving all the layers as .svg files (separately) and navigation information (all the roads) as a .txt file 
			 */
			
			FileWriter outfile = new FileWriter("layer0.svg");
			BufferedWriter out = new BufferedWriter(outfile);
			out.write(layers.get(0).toString());
			out.close();

			FileWriter outfile2 = new FileWriter("layer1.svg");
			BufferedWriter out2 = new BufferedWriter(outfile2);
			out2.write(layers.get(1).toString());
			out2.close();

			FileWriter outfile3 = new FileWriter("layer2.svg");
			BufferedWriter out3 = new BufferedWriter(outfile3);
			out3.write(layers.get(2).toString());
			out3.close();

			FileWriter outfile4 = new FileWriter("layer3.svg");
			BufferedWriter out4 = new BufferedWriter(outfile4);
			out4.write(layers.get(3).toString());
			out4.close();

			FileWriter outfile5 = new FileWriter("layer4.svg");
			BufferedWriter out5 = new BufferedWriter(outfile5);
			out5.write(layers.get(4).toString());
			out5.close();

			FileWriter outfile6 = new FileWriter("layer5.svg");
			BufferedWriter out6 = new BufferedWriter(outfile6);
			out6.write(layers.get(5).toString());
			out6.close();
			
			FileWriter outfile7 = new FileWriter("nav.txt");
			BufferedWriter out7 = new BufferedWriter(outfile7);
			out7.write(layers.get(6).toString());
			out7.close();
			
			/*
			 * calling a method to save information to StringBuilders for navigation (vertexes and edges)
			 */
//			StringBuilder sbedge = e.edges();
//			StringBuilder sbvertexes = e.setdijinfo();
//			
//			
//			/*
//			 * saving vertexes and edges to separate text files
//			 */
//			
//			FileWriter outfilex10 = new FileWriter("edges.txt");
//			BufferedWriter outx10 = new BufferedWriter(outfilex10);
//			outx10.write(sbedge.toString());
//			outx10.close();
//			
//			FileWriter outfile11 = new FileWriter("vertexes.txt");
//			BufferedWriter out11 = new BufferedWriter(outfile11);
//			out11.write(sbvertexes.toString());
//			out11.close();


		} else {
			System.out.println("No file selected");
		}

	}

}