/*
 * OpenDaVINCI.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include "Example4.h"

int32_t main(int32_t argc, char **argv) {
    linearkalmanfilter::LinearKalmanFilter lkf(argc, argv);
    return lkf.runModule();
}
