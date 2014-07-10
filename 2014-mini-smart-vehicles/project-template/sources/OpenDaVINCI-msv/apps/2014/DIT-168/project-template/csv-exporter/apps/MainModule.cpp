/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include "CSVExporter.h"

int32_t main(int32_t argc, char **argv) {
    msv::CSVExporter c(argc, argv);
    return c.runModule();
}
