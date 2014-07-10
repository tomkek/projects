/* ====================================
@ Leila Keza
First: Change the path at line 24
Visualize the hardware and simulation differences sensors data
The output is a graph and a txt file showing those differences and statistical values
data source is an int result array from hardware - software data.
This class draw 6 graphs: 4 IR and 2 US sensors
Code for reding the bin files(Sasha and Tomatz)
========================================= */
#include <cv.h>
#include <highgui.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <cmath>
#include <string>
#include <fstream>
#include <stdexcept>
#include <vector>
#include <iterator>
#include <iostream>
#include <sstream>
#include <stdlib.h>
#include <stdio.h>
#include "/home/leila-keza/2014-BSc-SimRealData/2014-mini-smart-vehicles/2014-BSc-SimRealData/sources/statistics/statistics.h"

using namespace std;
using namespace cv;
void sensors_data();// reads from the bin files and wites to the txt file and generates the graphs
Mat drawIntGraph(const double arraySrc[], int nArrayLength, Mat imageDst, double minV, double maxV, int width, int height, char *graphLabel);// draws the graphs

int main(){
sensors_data();
return 0;

	}

void sensors_data(){
// name the sensors graphs
char strMyLogger1 []= "US1";
char strMyLogger2[]= "US2";
char strMyLogger3[]= "IR1";
char strMyLogger4[]= "IR2";
char strMyLogger5[]= "IR3";
char strMyLogger6[]= "IR4";
/* Read from bin files
@Sasha and Tomatz
*/
vector<int> rd_ir1;
vector<int> rd_ir2;
vector<int> rd_ir3;
vector<int> rd_ir4;
vector<int> rd_us1;
vector<int> rd_us2;
vector<int> rd_millis;

    int val;

  std::fstream rd_file("sensorsrd.bin", ios::in | ios::out | ios::binary);

    for(unsigned int i = 0; i < 114; i++){
        rd_file.read((char*)&val, sizeof(val));
        rd_ir1.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_ir2.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_ir3.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_ir4.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_us1.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_us2.push_back(val);
        rd_file.read((char*)&val, sizeof(val));
        rd_millis.push_back(val);
    //    cerr << rd_millis.at(i) <<  rd_ir1.at(i) << " | " << rd_ir2.at(i) << " | " << rd_ir3.at(i) << " | " << rd_ir4.at(i) << " | " << rd_us1.at(i) << " | " << rd_us2.at(i) << " | "<< endl;
    }

    vector<double> hd_ir1;
    vector<double> hd_ir2;
    vector<double> hd_ir3;
    vector<double> hd_ir4;
    vector<double> hd_us1;
    vector<double> hd_us2;
    vector<int> hd_millis;

    double vval;
  std::fstream hd_file("sensorshd.bin", ios::in | ios::out | ios::binary);

    for(int i = 0; i < 114; i++){
        hd_file.read((char*)&vval, sizeof(vval));
        hd_ir1.push_back(vval);
        hd_file.read((char*)&vval, sizeof(vval));
        hd_ir2.push_back(vval);
        hd_file.read((char*)&vval, sizeof(vval));
        hd_ir3.push_back(vval);
        hd_file.read((char*)&vval, sizeof(vval));
        hd_ir4.push_back(vval);
        hd_file.read((char*)&vval, sizeof(vval));
        hd_us1.push_back(vval);
        hd_file.read((char*)&vval, sizeof(vval));
        hd_us2.push_back(vval);
        hd_file.read((char*)&val, sizeof(val));
        hd_millis.push_back(val);
    }

    for(int i = 0; i < 114; i++){
    hd_ir1.at(i) = hd_ir1.at(i)*10;
    hd_ir2.at(i) = hd_ir2.at(i)*10;
    hd_ir3.at(i) = hd_ir3.at(i)*10;
    hd_ir4.at(i) = hd_ir4.at(i)*10;
    hd_us1.at(i) = hd_us1.at(i)*10;
    hd_us2.at(i) = hd_us2.at(i)*10;
    }


    vector<double> diff_ir1;
    vector<double> diff_ir2;
    vector<double> diff_ir3;
    vector<double> diff_ir4;
    vector<double> diff_us1;
    vector<double> diff_us2;
    vector<int> diff_millis;

    double diff;
    for (unsigned int i = 0; i < 114; i++){
        diff = hd_ir1.at(i) - rd_ir1.at(i);
        diff_ir1.push_back(diff);

        diff = hd_ir2.at(i) - rd_ir2.at(i);
        diff_ir2.push_back(diff);

        diff = hd_ir3.at(i) - rd_ir3.at(i);
        diff_ir3.push_back(diff);

        diff = hd_ir4.at(i) - rd_ir4.at(i);
        diff_ir4.push_back(diff);

        diff = hd_us1.at(i) - rd_us1.at(i);
        diff_us1.push_back(diff);

        diff = hd_us2.at(i) - rd_us2.at(i);
        diff_us2.push_back(diff);// end of extracting the data from bin files
        cerr <<  diff_ir1.at(i) << " | " << diff_ir2.at(i) << " | " << diff_ir3.at(i) << " | " << diff_ir4.at(i) << " | " << diff_us1.at(i) << " | " << diff_us2.at(i) << " | "<< endl;

    }


    /* Statical calculations and results txt file
    @ Leila Keza
    */

ostringstream values, line2,txt;

ostringstream themean;

ostringstream themedian;
ostringstream thestandard_devation;
string means;
string med;
string stdv;
string title;
string cols;        //The string
string rows;
txt<<"\t\t"<< "SIMULATION AND HARDWARE SENSORS DATA RECORDING RESULTS"<<endl;
title = txt.str();
line2 << "\t" << "IR 1" << "\t" << "IR 2" << "\t" << "IR 3" << "\t" << "IR 4" << "\t" << "US 1" << "\t" << "US 2"<< "\t" << "Hd timestamp" << "\t" << "Rd Timestamp"<<endl;
cols = line2.str();
ofstream myfile;
myfile.open ("Sensorsdata.txt");
  myfile << title<<"\n";
  myfile<<cols<<"\n";
	double U_S_1[114];
	double U_S_2[114];
	double I_R_1[114];
	double I_R_2[114];
	double IR3[114];
	double IR4[114];

        for (int unsigned i = 0; i < 114; i++){
// graph for the US 1

	U_S_1[i] = diff_us1.at(i);
	Mat bgImg1( 480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg1 = drawIntGraph(U_S_1, 114, bgImg1, -200, 200, 640, 480, strMyLogger1);
	cv::imwrite("US1.jpg", graphImg1);
// graph for the US 2

	U_S_2[i] = diff_us2.at(i);
	Mat bgImg2(480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg2 = drawIntGraph(U_S_2, 114, bgImg2, -200, 200, 640, 480, strMyLogger2);
	cv::imwrite("US2.jpg", graphImg2);

// graph for the IR 1

	I_R_1 [i] = diff_ir1.at(i);
	Mat bgImg5( 480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg5 = drawIntGraph(I_R_1, 114, bgImg5, -200, 200, 640, 480, strMyLogger3);
	cv::imwrite("IR1.jpg", graphImg5);
// graph for the IR 2

	I_R_2[i] = diff_ir2.at(i);
	Mat bgImg6( 480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg6= drawIntGraph(I_R_2, 114, bgImg6, -200, 200, 640, 480, strMyLogger4);
	cv::imwrite("IR2.jpg", graphImg6);

// Graph for the IR 3

	IR3[i] = diff_ir3.at(i);
	Mat bgImg4( 480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg4 = drawIntGraph(IR3, 114, bgImg4, -200, 200, 640, 480, strMyLogger5);
	cv::imwrite("IR4.jpg", graphImg4);
// Graph for the IR 4

	IR4[i] =  diff_ir4.at(i);
	Mat bgImg3( 480, 640, CV_8UC3, Scalar( 200,200,250) );
	Mat graphImg3 = drawIntGraph(IR4, 114, bgImg3, -200, 200, 640, 480, strMyLogger6);
	cv::imwrite("IR3.jpg", graphImg3);
values<<"\t"<<diff_ir1.at(i) << "\t" << diff_ir2.at(i) << "\t" << diff_ir3.at(i) << "\t" << diff_ir4.at(i) << "\t" << diff_us1.at(i) << "\t" << diff_us2.at(i)<<"\t"<<rd_millis.at(i)<<"\t\t"<< hd_millis.at(i)<<endl;
}
 // Mean
double imgmeanUS1 = mean(U_S_1,114);
double imgmeanUS2 = mean(U_S_2,114);
double imgmeanIR1 = mean(I_R_1,114);
double imgmeanIR2 = mean(I_R_2,114);
double imgmeanIR3 = mean(IR3,114);
double imgmeanIR4 = mean(IR4,114);
// median
double imgmedianUS1 = odd_size_median(U_S_1,114);
double imgmedianUS2 = odd_size_median(U_S_2,114);
double imgmedianIR1 = odd_size_median(I_R_1,114);
double imgmedianIR2 = odd_size_median(I_R_2,114);
double imgmedianIR3 = odd_size_median(IR3,114);
double imgmedianIR4 = odd_size_median(IR4,114);
//standard deviation

double imgmstdevUS1 = standard_devation(U_S_1,114,imgmeanUS1);
double imgmstdevUS2 = standard_devation(U_S_2,114,imgmeanUS2);
double imgmstdevIR1 = standard_devation(I_R_1,114,imgmeanIR1);
double imgmstdevIR2 = standard_devation(I_R_2,114,imgmeanIR2);
double imgmstdevIR3 = standard_devation(IR3,114,imgmeanIR3);
double imgmstdevIR4 = standard_devation(IR4,114,imgmeanIR4);
// mode
themean<<"\n\n"<<"MEAN:"<<"\t"<<imgmeanIR1<<"\t"<<imgmeanIR2<<"\t"<<imgmeanIR3<<"\t"<<imgmeanIR4<<"\t"<<imgmeanUS1<<"\t"<<imgmeanUS2<<"\t"<<endl;

themedian<<"\n\n"<<"MEDIAN:"<<"\t"<<imgmedianIR1<<"\t"<<imgmedianIR2<<"\t"<<imgmedianIR3<<"\t"<<imgmedianIR4<<"\t"<<imgmedianUS1<<"\t"<<imgmedianUS2<<"\t"<<endl;
thestandard_devation<<"\n\n"<<"STD DEV:"<<"\t"<<imgmstdevIR1<<"\t"<<imgmstdevIR2<<"\t"<<imgmstdevIR3 <<"\t"<<imgmstdevIR4<<"\t"<<imgmstdevUS1<<"\t"<<imgmstdevUS2<<endl;
rows = values.str();
means = themean.str();
med = themedian.str();
stdv = thestandard_devation.str();

myfile <<rows<<"\n";
myfile <<means;
myfile <<med;
myfile <<stdv;
myfile.close();
}

// Draw the graphs
Mat drawIntGraph(const double arraySrc[], int nArrayLength, Mat imageDst, double minV, double maxV, int width, int height, char graphLabel [])
{


	int w = width;
	int h = height;
	int b = 10;		// border around graph within the image
	if (w <= 20)
		w = nArrayLength + b*2;	// width of the image
	if (h <= 20)
		h = 220;

	int s = h - b*2;// size of graph height
	float xscale = 1.0;
	if (nArrayLength > 1)
		xscale = (w - b*2) / (float)(nArrayLength-1);	// horizontal scale
	Mat imageGraph = Mat(Size(w,h), CV_8UC3, Scalar( 200,200,250));
	imageGraph = imageDst;

	if (minV == 0 && maxV == 0) {
		for (int i=0; i<nArrayLength; i++) {
			double v = arraySrc[i];
			if (v < minV)
				minV = v;
			if (v > maxV)
				maxV = v;
		}
	}
	double diffV = maxV - minV;
	if (diffV == 0)
		diffV = 1;	// Stop a divide-by-zero error
	float fscale = (float)s / (float)diffV;

	// Draw the horizontal & vertical axis
	int y0 = cvRound(minV*fscale);

	// Horizontal and vertical axis
	line(imageGraph, Point(b,h-(b-y0)), Point(w-b, h-(b-y0)), Scalar( 255, 0, 0), 2, CV_AA , 0 );
	line(imageGraph, Point(b,h-(b)), Point(b, h-(b+s)), Scalar( 255, 0, 0), 2, CV_AA , 0 );
		char text[16];

		snprintf(text, sizeof(text)-1, "%f", maxV);

	putText(imageGraph, text,  cvPoint(1, b+4), FONT_HERSHEY_COMPLEX_SMALL, 0.5, cvScalar(0,0,0), 1, CV_AA);
		// Write the scale of the x axis

		snprintf(text, sizeof(text)-1, "%d", (nArrayLength-1) );

	putText(imageGraph, text,  cvPoint(w-b+4-5*strlen(text), (h/2)+10), FONT_HERSHEY_COMPLEX_SMALL, 0.5, cvScalar(0,0,0), 1, CV_AA);

	// Draw the values
	Point ptPrev = Point(b,h-(b-y0));	// Start the lines at the 1st point.
	for (int i=0; i<nArrayLength; i++) {
		int y = cvRound((arraySrc[i] - minV) * fscale);	// Get the values at a bigger scale
		int x = cvRound(i * xscale);
		Point ptNew = Point(b+x, h-(b+y));
	line(imageGraph, ptPrev, ptNew, Scalar( 0, 0, 0), 2, CV_AA , 0 );	// Draw a line from the previous point to the new point
		ptPrev = ptNew;

	}

	// Write the graph label
	if (graphLabel != NULL && strlen(graphLabel) > 0) {
	putText(imageGraph, graphLabel,  cvPoint(300, 10), FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);

	}

	return imageGraph;
}


