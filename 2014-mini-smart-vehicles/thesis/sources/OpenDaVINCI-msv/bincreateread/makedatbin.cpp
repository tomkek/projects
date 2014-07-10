#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <opencv/highgui.h>
#include <fstream>
#include <sys/time.h>
#include <cv.h>

using namespace cv;
using namespace std;

int main() {
  
  fstream file2("timestamp.bin", ios::in | ios::out | ios::binary); 
  unsigned int len = 0; while (file2.get() != EOF) len++;
  file2.close();

  fstream file;
  file.open("timestamp.bin", ios::in | ios::out | ios::binary);
  vector<int> millis;

  int mill;
  for(int i = 0; i < len/4; i++){
    file.read((char*)&mill, sizeof(mill));

    millis.push_back(mill);
  }
  file.close();
  fstream myFile;
  myFile.open("picture.bin", ios::out | ios::binary);
  string filename;
  stringstream stream;

  for(int i = 0; i < len/4;  i < i++) {
    stream.str(string());
    stream << millis.at(i);

    filename = "pics/" + stream.str() + ".jpg";
    Mat mat = imread(filename);
    cerr << filename << endl;

    uint8_t* pixelPtr = (uint8_t*)mat.data;
    int cn = mat.channels();

    char val;
    
    for(int i = 0; i <= mat.rows; i++) //coordinate x loop
      {
	for(int j = 0; j <= mat.cols; j++) //coordinate y loop
	  {
	    cv::Scalar_<uint8_t> bgrPixel;
	    bgrPixel[0] = pixelPtr[i*mat.cols*cn + j*cn + 0]; // B
	    bgrPixel[1] = pixelPtr[i*mat.cols*cn + j*cn + 1]; // G
	    bgrPixel[2] = pixelPtr[i*mat.cols*cn + j*cn + 2]; // R
	    val = bgrPixel[0];
	    myFile.write((char*)&val, sizeof(val));
	    val = bgrPixel[1];
	    myFile.write((char*)&val, sizeof(val));
	    val = bgrPixel[2];
	    myFile.write((char*)&val, sizeof(val));
	    
	  }
	//break;
      }

    myFile.write((char*)&millis.at(i), sizeof(millis.at(i)));
    cerr << millis.at(i) << endl;
		 
  }
  myFile.close();
  
  
  

}
