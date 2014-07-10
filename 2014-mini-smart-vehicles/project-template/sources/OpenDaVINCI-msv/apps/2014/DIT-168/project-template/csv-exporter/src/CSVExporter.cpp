/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include <stdio.h>
#include <math.h>

#include "core/base/FIFOQueue.h"
#include "core/io/ContainerConference.h"
#include "core/data/Container.h"
#include "core/data/Constants.h"
#include "core/data/control/VehicleControl.h"
#include "core/data/environment/VehicleData.h"

// Data structures from msv-data library:
#include "SensorBoardData.h"
#include "UserButtonData.h"

#include "CSVExporter.h"

namespace msv {

        using namespace std;
        using namespace core::base;
        using namespace core::data;
        using namespace core::data::control;
        using namespace core::data::environment;

        CSVExporter::CSVExporter(const int32_t &argc, char **argv) :
	        ConferenceClientModule(argc, argv, "csvexporter") {
        }

        CSVExporter::~CSVExporter() {}

        void CSVExporter::setUp() {
	        // This method will be call automatically _before_ running body().
        }

        void CSVExporter::tearDown() {
	        // This method will be call automatically _after_ return from body().
        }

        // This method will do the main data processing job.
        ModuleState::MODULE_EXITCODE CSVExporter::body() {
            FIFOQueue fifo;
            addDataStoreFor(fifo);

            VehicleData vd;
            SensorBoardData sbd;
            UserButtonData ubd;
            VehicleControl vc;


            cout << "Entry-ID,";
            for (uint32_t i = 0; i < 6; i++) {
                cout << "Sensor ID (" << i << ")," << "Distance(" << i << ")" << ",";
            }
            cout << "ButtonStatus" << "," << "ButtonPressDuration" << ",";
            cout << "Heading" << "," << "AbsTraveledPath" << ",";
            cout << "Speed" << "," << "SteeringWheelAngle" << endl;


            uint32_t counter = 0;
	        while (getModuleState() == ModuleState::RUNNING) {
        		while (!fifo.isEmpty()) {
                    bool hasData = false;
        			Container c = fifo.leave();

        			if (c.getDataType() == Container::VEHICLEDATA) {
        				vd = c.getData<VehicleData>();
                        counter++;
                        hasData = true;
        			}
        			if (c.getDataType() == Container::USER_DATA_0) {
        				sbd = c.getData<SensorBoardData>();
                        counter++;
                        hasData = true;
        			}
        			if (c.getDataType() == Container::USER_BUTTON) {
        				ubd = c.getData<UserButtonData>();
                        counter++;
                        hasData = true;
        			}
        			if (c.getDataType() == Container::VEHICLECONTROL) {
        				vc = c.getData<VehicleControl>();
                        counter++;
                        hasData = true;
        			}

                    if (hasData) {
                        cout << counter << ",";
                        for (uint32_t i = 0; i < 6; i++) {
                            cout << i << "," << sbd.getDistance(i) << ",";
                        }
                        cout << ubd.getButtonStatus() << "," << ubd.getDuration() << ",";
                        cout << vd.getHeading() << "," << vd.getAbsTraveledPath() << ",";
                        cout << vc.getSpeed() << "," << vc.getSteeringWheelAngle() << endl;
                    }
        		}
	        }

	        return ModuleState::OKAY;
        }
} // msv

