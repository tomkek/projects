/* @Leila Keza

To run this : Change the path for statistics folder(line 44)
 *This program includes all statistical and geometrical calculations for the images 
4 functions :
1. Mat histogram(Mat img) that generates the histograms; 
2. double getPSNR(const Mat& I1, const Mat& I2) that 
calculates the resemblance between two images and return PNSR value;
3. struct img_comparison(Mat 1, Mat 2) 
that locates 6 the ROI on the 2 images,
Generates the histograms on the resultant image from the Mat 1- Mat2
- combines the histograms per image
- apply the remblace calculations(PSNR) on each ROI
- return a struct of combined histograms per image and the PSNR value 
4. Main function contains: 
- codes to read the recordings, images and sensors( Done by Sasha and Tomasz)
- Codes for writing results files( calling the statistcs.cpp for statistical calculations)
Images and sensors files used (provided by Sasha and Tomasz)
*/
#include <string>      
#include <iostream>     
#include <sstream> 
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <cv.h>
#include <highgui.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <cmath>
#include <fstream>
#include <stdexcept>     
#include <vector>         
#include <iterator> 
#include <cmath>
#include <stdexcept>     
#include <vector>   
#include <iomanip>  
#include <sstream>  
#include <opencv2/core/core.hpp>        
#include <opencv2/imgproc/imgproc.hpp>  
#include <opencv2/highgui/highgui.hpp>  
#include <iostream>
//Include statistics
#include "/home/leila-keza/2014-BSc-SimRealData/2014-mini-smart-vehicles/2014-BSc-SimRealData/sources/statistics/statistics.h"

using namespace cv;
using namespace std;

Mat histogram(Mat img);
double getPSNR(const Mat& I1, const Mat& I2);
struct img_comparison{
double percent;
Mat hist;
};
img_comparison diff_hist(Mat im, Mat im1);


int main(){
/*Reading from the bin files
Tomasz and Sasha
*/
fstream hd_file;
hd_file.open ("timestamphd.bin", ios::in | ios::out | ios::binary);
    unsigned int i;
    int val;
    vector<int> hd_millis;
    hd_file.seekg(20,ios::beg);
    for(i =0; i < 114; i++){
        hd_file.read((char*)&val, sizeof(val));
        hd_millis.push_back(val);
    }
fstream rd_file;
    rd_file.open ("timestamprd.bin", ios::in | ios::out | ios::binary);
    vector<int> rd_millis;
    rd_file.seekg(8,ios::beg);
    for(i =0; i < 114; i++){
        rd_file.read((char*)&val, sizeof(val));
        rd_millis.push_back(val);
    }
    
// writing the fist lines on the image comparison results file
 ostringstream line2;
 ostringstream txt;
ostringstream values;
string title; 
string cols;       
string rows;
// The title
txt<<"\t\t"<< "SIMULATION AND HARDWARE IMAGES DATA RECORDING RESULTS"<<endl;
title = txt.str();
// colomns' names
line2<<"\t"<<"PSNR value"<<"\t"<<"Hardware timestamp"<<"\t"<<"Software Timestamp"<<endl;
cols = line2.str();
ofstream myfile;
myfile.open ("imagedata.txt");
  myfile << title<<"\n";
  myfile<<cols<<"\n";
  
  img_comparison structure;
  

string s;
double image[114];
ostringstream themean;
ostringstream themedian;
ostringstream thestandard_devation;

string means;
string med;
string stdv;

/*This is a for loop where it goes image by image from two folders, both hardware and simulator
Sasha and Tomasz
*/
    for(i = 0; i < 114; i++){
    ostringstream temp;

    stringstream ss;
    ss << hd_millis.at(i);
    string str = ss.str();
    string g = "hd/"+ str +".jpg";
    cv::Mat hesperia = cv::imread(g,0);//reading as grayscale

    stringstream ss2;
    ss2 << rd_millis.at(i);
    string str2 = ss2.str();
    string g2 = "rd/0"+ str2 +".jpg";
    cv::Mat hardware = cv::imread(g2,0);// reading as grayscale
    
    /*Image comparison and statistical analysis and writing the results files
    Leila Keza
    */
       Mat ghes, gh, imhes, imh;   
   
// eadges' detection in order to just comprare the edges 
cv::Canny(hesperia,ghes,127,255);
cv::Canny(hardware,gh,127,255);
// return to RGB format to avoid RGB errors while calculating the PSNR value
Mat a;
Mat b;
cvtColor(ghes, a, CV_GRAY2RGB);
cvtColor(gh, b, CV_GRAY2RGB);


// getting the content of the struct 
structure = diff_hist(b, a);
image[i]= structure.percent;
double sc = structure.percent;

temp <<i;
s = temp.str();

values<<"\t"<< setiosflags(ios::fixed) << setprecision(2)<<sc<<"\t\t"<<str2<<"\t\t"<<str<<endl;
rows = values.str();
 cout<< "PSNR"<< sc<<"Time Stamp hardware: "<< str2<<"Time stamp Hesperia"<<str<<"\t S values"<<s<<endl;
 imwrite("Hist"+s+".jpg", structure.hist);
 

}

 // Mean
double imgmean = mean(image,114);
// median
double imgmedian = odd_size_median(image,114);
//standard deviation
double imgmstdev = standard_devation(image,114,imgmean);

themean<<"\n\n"<<"MEAN:"<<"\t"<<setiosflags(ios::fixed) << setprecision(2)<<imgmean<<endl;
themedian<<"\n\n"<<"MEDIAN:"<<"\t"<<setiosflags(ios::fixed) << setprecision(2)<<imgmedian<<endl;
thestandard_devation<<"\n\n"<<"STANDARD DEVIATION:"<<"\t"<<setiosflags(ios::fixed) << setprecision(2)<<imgmstdev<<endl;

means = themean.str();
med = themedian.str();
stdv = thestandard_devation.str();

myfile <<rows<<"\n";

myfile <<means;
myfile <<med;
myfile <<stdv;

  return 0;
 myfile.close();
}// end of main

/* Compute the histogram of the diff_Image
Leia
*/

	Mat histogram(Mat img){
	
	vector<Mat> bgr_planes;

  split(img, bgr_planes );

  /// Establish the number of bins
  int histSize = 256;

  /// Set the ranges ( for B,G,R) )
  float range[] = { 0, 256 } ;
  const float* histRange = { range };

  bool uniform = true; bool accumulate = false;

  Mat b_hist, g_hist, r_hist;

  /// Compute the histograms:
  calcHist( &bgr_planes[0], 1, 0, Mat(), b_hist, 1, &histSize, &histRange, uniform, accumulate );
  calcHist( &bgr_planes[1], 1, 0, Mat(), g_hist, 1, &histSize, &histRange, uniform, accumulate );
  calcHist( &bgr_planes[2], 1, 0, Mat(), r_hist, 1, &histSize, &histRange, uniform, accumulate );

  // Draw the histograms for B, G and R
  int hist_w = 512; int hist_h = 400;
  int bin_w = cvRound( (double) hist_w/histSize );

  Mat histImage( hist_h, hist_w, CV_8UC3, Scalar( 200,200,255) );

  /// Normalize the result to [ 0, histImage.rows ]
  normalize(b_hist, b_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat() );
  normalize(g_hist, g_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat() );
  normalize(r_hist, r_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat() );

  /// Draw for each channel
  for( int i = 1; i < histSize; i++ )
  {
      line( histImage, Point( bin_w*(i-1), hist_h - cvRound(b_hist.at<float>(i-1)) ) ,
                       Point( bin_w*(i), hist_h - cvRound(b_hist.at<float>(i)) ),
                       Scalar( 0, 0, 255), 5, 8, 0  );
      line( histImage, Point( bin_w*(i-1), hist_h - cvRound(g_hist.at<float>(i-1)) ) ,
                       Point( bin_w*(i), hist_h - cvRound(g_hist.at<float>(i)) ),
                       Scalar( 200,200,250), 6, 8, 0  );
      line( histImage, Point( bin_w*(i-1), hist_h - cvRound(r_hist.at<float>(i-1)) ) ,
                       Point( bin_w*(i), hist_h - cvRound(r_hist.at<float>(i)) ),
                       Scalar( 0, 0, 0), 5, 10, 0  );
  }

return histImage;
}
/* compute the ROI, 
generate the histograms and combine them in one frame 
It return the PSNR value that will contribute to the report
 histograms for the visualisation
 
 Leila
 */

img_comparison diff_hist(Mat im, Mat im1 ){

img_comparison comparison;
// ROI of the first image

Mat roi1, roi2, roi3, roi4, roi5, roi6,
roia, roib, roic, roid, roie, roif;
roi1 = Mat(im, Rect(0, 100, 145, 215));
roi2 = Mat(im, Rect(150, 100, 245, 215));
roi3 = Mat(im, Rect(400, 100, 235, 215));
roi4 = Mat(im, Rect(0, 320, 145, 160));
roi5 = Mat(im, Rect(150, 320, 245, 160));
roi6 = Mat(im, Rect(400, 320, 235, 160));
//ROI of the second Image
roia = Mat(im1, Rect(0, 100, 145, 215));
roib = Mat(im1, Rect(150, 100, 245, 215));
roic = Mat(im1, Rect(400, 100, 235, 215));
roid = Mat(im1, Rect(0, 320, 145, 160));
roie = Mat(im1, Rect(150, 320, 245, 160));
roif = Mat(im1, Rect(400, 320, 235, 160));

double total_resemlance, psnrV1,psnrV2,psnrV3,psnrV4,psnrV5,psnrV6;
// get the PSNR values

	psnrV1 = getPSNR(roia, roi1);
	psnrV2 = getPSNR(roib, roi2);
	psnrV3 = getPSNR(roib, roi2);
	psnrV4 = getPSNR(roib, roi2);
	psnrV5 = getPSNR(roib, roi2);
	psnrV6 = getPSNR(roib, roi2);
	
        	cout << setiosflags(ios::fixed) << setprecision(3) << psnrV1 << "dB1"<<endl;
        		cout << setiosflags(ios::fixed) << setprecision(3) << psnrV2 << "dB2"<<endl;
        			cout << setiosflags(ios::fixed) << setprecision(3) << psnrV3 << "dB3"<<endl;	cout << setiosflags(ios::fixed) << setprecision(3) << psnrV4 << "dB4"<<endl;
        				cout << setiosflags(ios::fixed) << setprecision(3) << psnrV5 << "dB5"<<endl;
        					cout << setiosflags(ios::fixed) << setprecision(3) << psnrV6 << "dB6"<<endl;
     total_resemlance = 0.01 *psnrV1 + 0.39 * psnrV2 + 0.4 * psnrV3 + 0.01 * psnrV4 + 0.18 * psnrV5 + 0.01 * psnrV6;

        
comparison.percent = total_resemlance;

	
Mat diff_im1, diff_im2,diff_im3,diff_im4,diff_im5,diff_im6;

// Calculate the diff_ROI 
absdiff(roi1, roia, diff_im1); 
absdiff(roi2, roib, diff_im2);
absdiff(roi3, roic, diff_im3);
absdiff(roi4, roid, diff_im4);
absdiff(roi5, roie, diff_im5);
absdiff(roi6, roif, diff_im6);

// Draw histogram of each ROI

Mat dst1, dst2,dst3,dst4,dst5,dst6; 
dst1 = histogram(diff_im1);
dst2 = histogram(diff_im2);
dst3 =histogram(diff_im3);
dst4 = histogram(diff_im4);
dst5 = histogram(diff_im5);
dst6 = histogram(diff_im6);

// Combine the histograms in one big frame

Mat final_hist;
Mat Frame;
final_hist = Mat(Size(640, 480), CV_8UC3, Scalar( 200,200,200));

/*select the ROI on the final frame where 
the histograms will be displayed*/

Mat merge1, merge2, merge3, merge4, merge5, merge6; 
merge1 = Mat(final_hist, Rect(0, 100, 145, 215));
merge2 = Mat(final_hist, Rect(150, 100, 245, 215));
merge3 = Mat(final_hist, Rect(400, 100, 235, 215));
merge4 = Mat(final_hist, Rect(0, 320, 145, 160));
merge5 = Mat(final_hist, Rect(150, 320, 245, 160));
merge6 = Mat(final_hist, Rect(400, 320, 235, 160));
/* Resize the histograms to the their respective ROI size*/
Size size1(145,215);
Size size2(245,215);
Size size3(235,215);
Size size4(145,160);
Size size5(245,160);
Size size6(235,160);

// Combine the ROI histograms in one Frame
Mat dest1, dest2,dest3,dest4,dest5,dest6 ;
resize(dst1,dest1,size1);
resize(dst2,dest2,size2);
resize(dst3,dest3,size3);
resize(dst4,dest4,size4);
resize(dst5,dest5,size5);
resize(dst6,dest6,size6);
dest1.copyTo(merge1);
dest2.copyTo(merge2);
dest3.copyTo(merge3);
dest4.copyTo(merge4);
dest5.copyTo(merge5);
dest6.copyTo(merge6);
// print the lables on each ROI histogram
putText(final_hist, "Diff_ROI_1", cvPoint(30,120), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);
putText(final_hist, "Diff_ROI_2", cvPoint(200,120), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);
putText(final_hist, "Diff_ROI_3", cvPoint(440,120), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);
putText(final_hist, "Diff_ROI_4", cvPoint(30,340), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);
putText(final_hist, "Diff_ROI_5", cvPoint(200,340), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);
putText(final_hist, "Diff_ROI_6", cvPoint(440,340), 
    FONT_HERSHEY_COMPLEX_SMALL, 0.6, cvScalar(0,0,0), 1, CV_AA);

 // Return the combined histogram Frames to be saved or displayed for the data visualisation
comparison.hist = final_hist.clone();
return comparison;

}
//calculate the PSNR
double getPSNR(const Mat& I1, const Mat& I2)
{
    Mat s1;
    
    absdiff(I1, I2, s1);       // |I1 - I2|
    s1.convertTo(s1, CV_32F);  // cannot make a square on 8 bits
    s1 = s1.mul(s1);           // |I1 - I2|^2

    Scalar s = sum(s1);         // sum elements per channel

    double sse = s.val[0];// black and white image

    if( sse <= 1e-10) // for small values return zero
        return 0;
    else
    {
        double  mse =sse /(double)(I1.channels() * I1.total());
        double psnr = 10.0*log10((255*255)/mse);
        return psnr;
    }
}
