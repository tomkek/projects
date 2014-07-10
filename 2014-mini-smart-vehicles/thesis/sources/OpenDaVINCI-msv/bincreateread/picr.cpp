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

int main()
{

  cv::Mat mat(480,640,CV_32FC3 );
  // cvConvert( img, mat );

  //binary attempt
    std::fstream file;
    file.open("picture.bin", ios::in | ios::out | ios::binary);

    std::fstream file2("picture.bin", ios::in | ios::out | ios::binary);
    unsigned int len = 0;
    while (file2.get() != EOF) len++;
    cerr << len << endl;

    for(int i = 0; i < len/921604; i++) {
    char x;
    uint8_t* pixelPtr =  (uint8_t*)mat.data;

    for(int i = 0; i <= mat.rows; i++) //coordinate x loop
      {
        for(int j = 0; j <= mat.cols; j++) //coordinate y loop
        {
	    cv::Scalar_<uint8_t> bgrPixel;
        file.read((char*)&x, sizeof(x));
        bgrPixel[0] = x;
	//cerr << x << " || ";
	    pixelPtr[i*mat.cols*3 + j*3 + 0] = bgrPixel[0]; // B
        file.read((char*)&x, sizeof(x));
        bgrPixel[1] = x;
	//cerr << x << " || ";
	    pixelPtr[i*mat.cols*3 + j*3 + 1] = bgrPixel[1]; // G
        file.read((char*)&x, sizeof(x));
        bgrPixel[2] = x;
	//cerr << x << " || ";
	    pixelPtr[i*mat.cols*3 + j*3 + 2] = bgrPixel[2]; // R
        }
      }
    int mill;
    file.read((char*)&mill, sizeof(mill));
    cerr << i << " " << mill << endl;

    Mat M(480,640, CV_8UC3, pixelPtr);
    uint8_t* pixelPtr2 = (uint8_t*)M.data;
    int cn = M.channels();
    for(int i = 0; i <= M.rows; i++) //coordinate x loop
      {
        for(int j = 0; j <= M.cols; j++) //coordinate y loop
        {
	    cv::Scalar_<uint8_t> bgrPixel;
	    bgrPixel[0] = pixelPtr[i*M.cols*cn + j*cn + 0]; // B
	    bgrPixel[1] = pixelPtr[i*M.cols*cn + j*cn + 1]; // G
	    bgrPixel[2] = pixelPtr[i*M.cols*cn + j*cn + 2]; // R
        }
      }
      std::string s = to_string(mill);
      imwrite( "pictures/"+s+".jpg", M);
    }

}




