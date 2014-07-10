/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include <stdio.h>
#include <math.h>
#include <opencv/cv.h>
#include <opencv/highgui.h>
#include "core/io/ContainerConference.h"
#include "core/data/Container.h"
#include "core/data/Constants.h"
#include "core/data/control/VehicleControl.h"
#include "core/data/environment/VehicleData.h"

// Data structures from msv-data library:
#include "SteeringData.h"
#include "SensorBoardData.h"
#include "UserButtonData.h"

#include "Driver.h"

namespace msv {

        using namespace std;
        using namespace core::base;
        using namespace core::data;
        using namespace core::data::control;
        using namespace core::data::environment;

        Driver::Driver(const int32_t &argc, char **argv) :
	        ConferenceClientModule(argc, argv, "Driver") {
        }

        Driver::~Driver() {}

        void Driver::setUp() {

	        // This method will be call automatically _before_ running body().
        }

        void Driver::tearDown() {
	        // This method will be call automatically _after_ return from body().
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
        ModuleState::MODULE_EXITCODE Driver::body() {
                std::ofstream myFile("sensors.bin", ios::binary);
	        while (getModuleState() == ModuleState::RUNNING) {
                // In the following, you find example for the various data sources that are available:

		        // 1. Get most recent vehicle data:
		        Container containerVehicleData = getKeyValueDataStore().get(Container::VEHICLEDATA);
		        VehicleData vd = containerVehicleData.getData<VehicleData> ();
		 //       cerr << "Most recent vehicle data: '" << vd.toString() << "'" << endl;

		        // 2. Get most recent sensor board data:
		        Container containerSensorBoardData = getKeyValueDataStore().get(Container::USER_DATA_0);
		        SensorBoardData sbd = containerSensorBoardData.getData<SensorBoardData> ();
			
		      timeval time;
		      string test;
		      stringstream teststream;		      
		      
		      double val = sbd.getDistance(0);
		      myFile.write((char*)&val, sizeof(val));
		      
		      val = sbd.getDistance(1);
		      myFile.write((char*)&val, sizeof(val));
		      
		      val = sbd.getDistance(2);
		      myFile.write((char*)&val, sizeof(val));
		      
		      val = sbd.getDistance(3);
		      myFile.write((char*)&val, sizeof(val));
		      
		      val = sbd.getDistance(4);
		      myFile.write((char*)&val, sizeof(val));

		      gettimeofday(&time, NULL);
		      unsigned long millis = (time.tv_usec / 1000);
		      
		      teststream << millis;
		      if (millis < 10){
			test = remove_letter(currentDateTime(), ':') + "00" + teststream.str();
		      }
		      else if (millis < 100 and millis >= 10) {
			test = remove_letter(currentDateTime(), ':') + "0" + teststream.str();
			
		      }
		      else {
			test = remove_letter(currentDateTime(), ':') + teststream.str();
			
		      }
		      
		      
		      cerr << test << endl;
		      int timestamp;
		      timestamp = atoi(test.c_str());

		      myFile.write((char*)&timestamp, sizeof(timestamp));
			

		        // 3. Get most recent user button data:
		        Container containerUserButtonData = getKeyValueDataStore().get(Container::USER_BUTTON);
		        UserButtonData ubd = containerUserButtonData.getData<UserButtonData> ();
		  //      cerr << "Most recent user button data: '" << ubd.toString() << "'" << endl;

		        // 4. Get most recent steering data as fill from lanedetector for example:
		        Container containerSteeringData = getKeyValueDataStore().get(Container::USER_DATA_1);
		        SteeringData sd = containerSteeringData.getData<SteeringData> ();
		 //       cerr << "Most recent steering data: '" << sd.toString() << "'" << endl;



                // Design your control algorithm here depending on the input data from above.



		        // Create vehicle control data.
		        VehicleControl vc;

                // With setSpeed you can set a desired speed for the vehicle in the range of -2.0 (backwards) .. 0 (stop) .. +2.0 (forwards)
		        vc.setSpeed(1.5);

                // With setSteeringWheelAngle, you can steer in the range of -26 (left) .. 0 (straight) .. +25 (right)
                double desiredSteeringWheelAngle = 0; // 4 degree but SteeringWheelAngle expects the angle in radians!
		        vc.setSteeringWheelAngle(desiredSteeringWheelAngle * Constants::DEG2RAD);

                // You can also turn on or off various lights:
                vc.setBrakeLights(false);
                vc.setLeftFlashingLights(false);
                vc.setRightFlashingLights(true);

		        // Create container for finally sending the data.
		        Container c(Container::VEHICLECONTROL, vc);
		        // Send container.
		        getConference().send(c);
	        }
            myFile.close();
	        return ModuleState::OKAY;
        }
} // msv

