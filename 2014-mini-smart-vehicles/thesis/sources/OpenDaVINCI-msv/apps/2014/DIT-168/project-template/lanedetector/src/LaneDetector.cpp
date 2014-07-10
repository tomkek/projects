/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include <iostream>
#include <opencv/cv.h>
#include <opencv/highgui.h>

#include <ueye.h>
#include <uEye.h>

#include "core/base/KeyValueConfiguration.h"
#include "core/data/Container.h"
#include "core/data/image/SharedImage.h"
#include "core/io/ContainerConference.h"
#include "core/wrapper/SharedMemoryFactory.h"
#include "core/base/Service.h"

// Data structures from msv-data library:
#include "SteeringData.h"

#include "LaneDetector.h"

//const unsigned long long size = 8ULL*1024ULL*1024ULL;

namespace msv {

    using namespace std;
    using namespace core::base;
    using namespace core::data;
    using namespace core::data::image;
  
  LaneDetector::LaneDetector(const int32_t &argc, char **argv) : ConferenceClientModule(argc, argv, "lanedetector"),
								 m_hasAttachedToSharedImageMemory(false),
        m_sharedImageMemory(),
        m_image(NULL),
        m_cameraId(-1),
        m_debug(false) {}

    LaneDetector::~LaneDetector() {}

    void LaneDetector::setUp() {
	    // This method will be call automatically _before_ running body().
	    if (m_debug) {
		    // Create an OpenCV-window.
		    cvNamedWindow("WindowShowImage", CV_WINDOW_AUTOSIZE);
		    cvMoveWindow("WindowShowImage", 300, 100);
	    }
    }

    void LaneDetector::tearDown() {
	    // This method will be call automatically _after_ return from body().
	    if (m_image != NULL) {
		    cvReleaseImage(&m_image);
	    }

	    if (m_debug) {
		    cvDestroyWindow("WindowShowImage");
	    }
    }

    bool LaneDetector::readSharedImage(Container &c) {
	    bool retVal = false;

	    if (c.getDataType() == Container::SHARED_IMAGE) {
		    SharedImage si = c.getData<SharedImage> ();

		    // Check if we have already attached to the shared memory.
		    if (!m_hasAttachedToSharedImageMemory) {
			    m_sharedImageMemory
					    = core::wrapper::SharedMemoryFactory::attachToSharedMemory(
							    si.getName());
		    }

		    // Check if we could successfully attach to the shared memory.
		    if (m_sharedImageMemory->isValid()) {
			    // Lock the memory region to gain exclusive access. REMEMBER!!! DO NOT FAIL WITHIN lock() / unlock(), otherwise, the image producing process would fail.
			    m_sharedImageMemory->lock();
			    {
				    const uint32_t numberOfChannels = 3;
				    // For example, simply show the image.
				    if (m_image == NULL) {
					    m_image = cvCreateImage(cvSize(si.getWidth(), si.getHeight()), IPL_DEPTH_8U, numberOfChannels);
				    }

				    // Copying the image data is very expensive...
				    if (m_image != NULL) {
					    memcpy(m_image->imageData,
							   m_sharedImageMemory->getSharedMemory(),
							   si.getWidth() * si.getHeight() * numberOfChannels);
				    }
			    }

			    // Release the memory region so that the image produce (i.e. the camera for example) can provide the next raw image data.
			    m_sharedImageMemory->unlock();

			    // Mirror the image.
			    cvFlip(m_image, 0, -1);

			    retVal = true;
		    }
	    }
	    return retVal;
    }

    // You should start your work in this method.
    void LaneDetector::processImage() {
        // Example: Show the image.
        if (m_debug) {
            if (m_image != NULL) {
                cvShowImage("WindowShowImage", m_image);
                cvWaitKey(10);
            }
        }

        // 1. Do something with the image m_image here, for example: find lane marking features, optimize quality, ...
	


        // 2. Calculate desired steering commands from your image features to be processed by driver.



        // Here, you see an example of how to send the data structure SteeringData to the ContainerConference. This data structure will be received by all running components. In our example, it will be processed by Driver.
        SteeringData sd;
        sd.setExampleData(1234.56);

        // Create container for finally sending the data.
        Container c(Container::USER_DATA_1, sd);
        // Send container.
        getConference().send(c);
    }
  const string currentDateTime() {
    
    time_t now = time(0);
    struct tm tstruct;
    char buf[80];
    tstruct = *localtime(&now);
    
    strftime(buf, sizeof(buf), "%X", &tstruct);
    return buf;
    
  }
  string remove_letter( string str, char c )
  {
    str.erase( remove( str.begin(), str.end(), c ), str.end() ) ;
    return str ;
  }
    // This method will do the main data processing job.
    // Therefore, it tries to open the real camera first. If that fails, the virtual camera images from camgen are used.
    ModuleState::MODULE_EXITCODE LaneDetector::body() {
	    // Get configuration data.
	    KeyValueConfiguration kv = getKeyValueConfiguration();
	    m_cameraId = kv.getValue<int32_t> ("lanedetector.camera_id");
	    //m_cameraId = 1;
	    m_debug = kv.getValue<int32_t> ("lanedetector.debug") == 1;

	    bool use_real_camera = true;

	    // Try to open the camera device.
	    CvCapture *capture = cvCaptureFromCAM(m_cameraId);
	    if (!capture) {
		    cerr << "Could not open real camera; falling back to SHARED_IMAGE." << endl;
		    use_real_camera = false;
	    }
	    //
	    fstream myFile;
	    myFile.open("timestamp.bin", ios::out | ios::binary);

	    while (getModuleState() == ModuleState::RUNNING) {
		    bool has_next_frame = false;

		    // Use the shared memory image.
		    if (!use_real_camera) {
			    // Get the most recent available container for a SHARED_IMAGE.
			    Container c = getKeyValueDataStore().get(Container::SHARED_IMAGE);

			    if (c.getDataType() == Container::SHARED_IMAGE) {
				    // Example for processing the received container.
				    has_next_frame = readSharedImage(c);
			    }
		    } else {
			    // Use the real camera.
			    if (cvGrabFrame(capture)) {
				    m_image = cvRetrieveFrame(capture);
				    has_next_frame = true;
			    }
		    }

		    // Process the read image.
		    if (true == has_next_frame) {
		      //IMAGE RECORD 
		      cv::Mat mat(m_image);
		      //char val;

		      string test;
		      string t;
		      stringstream teststream;	      
		      /*
      		      uint8_t* pixelPtr = (uint8_t*)mat.data;
		      int cn = mat.channels();
		      
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
		      */
		      timeval time;
		      gettimeofday(&time, NULL);		      
		      unsigned long millis = (time.tv_usec / 1000);
		      teststream.str(string());
		      teststream << millis;
		      //test = remove_letter(currentDateTime(), ':') + teststream.str();
		      


		      //.jpg write
		      if (millis < 10){
			test = "pics/" + remove_letter(currentDateTime(), ':') + "00" + teststream.str() + ".jpg";
		      }
		      else if (millis < 100 and millis >= 10) {
			  test = "pics/" + remove_letter(currentDateTime(), ':') + "0" + teststream.str() + ".jpg";

			}
			else {
			  test = "pics/" + remove_letter(currentDateTime(), ':') + teststream.str() + ".jpg";

			}
		      //cerr << test << endl;
		      teststream.str(string());
		      teststream << millis;
		      if (millis < 10) {
			t = remove_letter(currentDateTime(), ':') + "00" + teststream.str();
		      }
		      else if( millis < 100 and millis >= 10) {
			  t = remove_letter(currentDateTime(), ':') + "0" + teststream.str();
			}
			else {
			  t = remove_letter(currentDateTime(), ':') + teststream.str();
			}
		      cerr << t << endl;
		      int timestamp;
		      timestamp = atoi(t.c_str());

		      myFile.write((char*)&timestamp, sizeof(timestamp));

		      cv::imwrite(test, mat);
		      
			    processImage();
		    }

		    if (use_real_camera) {
			    // Unset m_image only for the real camera to avoid memory leaks.
			    m_image = NULL;
		    }
	    }
	    myFile.close();

	    if (capture != NULL) {
		    cvReleaseCapture(&capture);
	    }

	    return ModuleState::OKAY;
    }
  
  
} // msv

