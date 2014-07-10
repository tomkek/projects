/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#ifndef CSVEXPORTER_H_
#define CSVEXPORTER_H_

#include "core/base/ConferenceClientModule.h"

namespace msv {

    using namespace std;

    /**
     * This class is printing Container data as CSV export.
     */
    class CSVExporter : public core::base::ConferenceClientModule {
        private:
            /**
             * "Forbidden" copy constructor. Goal: The compiler should warn
             * already at compile time for unwanted bugs caused by any misuse
             * of the copy constructor.
             *
             * @param obj Reference to an object of this class.
             */
            CSVExporter(const CSVExporter &/*obj*/);

            /**
             * "Forbidden" assignment operator. Goal: The compiler should warn
             * already at compile time for unwanted bugs caused by any misuse
             * of the assignment operator.
             *
             * @param obj Reference to an object of this class.
             * @return Reference to this instance.
             */
            CSVExporter& operator=(const CSVExporter &/*obj*/);

        public:
            /**
             * Constructor.
             *
             * @param argc Number of command line arguments.
             * @param argv Command line arguments.
             */
            CSVExporter(const int32_t &argc, char **argv);

            virtual ~CSVExporter();

            core::base::ModuleState::MODULE_EXITCODE body();

        private:
            virtual void setUp();

            virtual void tearDown();
    };

} // msv

#endif /*CSVEXPORTER_H_*/
