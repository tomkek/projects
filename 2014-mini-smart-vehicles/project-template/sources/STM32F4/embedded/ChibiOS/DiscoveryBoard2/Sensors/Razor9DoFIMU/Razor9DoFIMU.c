/*
 * OpenDaVINCI - STM32F4 Discovery Board Software/Hardware Interface.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include "DiscoveryBoard.h"

///////////////////////////////////////////////////////////////////////////////
// Data structures and configuation.
///////////////////////////////////////////////////////////////////////////////

static Thread *ThreadRazor9DoFIMU = NULL;

// Configuration of the Razor 9DoF IMU communication.
const SerialConfig portConfig = {
    57600,
    0,
    USART_CR2_STOP1_BITS | USART_CR2_LINEN,
    USART_CR3_CTSE
};

// Buffer for the data samples.
static int razorData[12] = {0,0,0,0,0,0,0,0,0,0,0,0};
static char word[64]; //has to be here
static uint8_t buf[64];

///////////////////////////////////////////////////////////////////////////////
// Interface methods.
///////////////////////////////////////////////////////////////////////////////

Thread* getThreadRazor9DoFIMU(void) {
    return ThreadRazor9DoFIMU;
}

void getRazor9DoFIMUData(int data[3]) {
    (void)data;
}

void commandPrintRazor9DoFIMU(BaseSequentialStream *chp, int argc, char *argv[]) {
    (void)argc;
    (void)argv;

    chprintf(chp, "Not implemented yet.\r\n");
}

///////////////////////////////////////////////////////////////////////////////
// Sensor reading methods.
///////////////////////////////////////////////////////////////////////////////

void readRazor9DoFIMU(void) {
//    sdRead(&SD3, buf, 1);
}

static WORKING_AREA(workingAreaThread_Razor9DoFIMU, 512);
static msg_t Thread_Razor9DoFIMU(void *arg) {
    (void)arg;
    chRegSetThreadName("Razor9DoFIMU");

    waitForCompletingInitialization();

    while (TRUE) {
        readRazor9DoFIMU();
        chThdSleepMilliseconds(10);
    }

    return (msg_t)0;
}

///////////////////////////////////////////////////////////////////////////////
// Initialization method.
///////////////////////////////////////////////////////////////////////////////

void initializeRazor9DoFIMU(void) {
    int i=0;
    for(i=0;i<64;i++) buf[i]=' ';

    // Initializes the serial driver UART3 in order to access the values from the Razor 9DoF IMU sensor.
    sdStart(&SD3, &portConfig);

    // PD8 (TX) transmit data to the Razor 9DoF IMU.
    palSetPadMode(GPIOD, 8, PAL_MODE_ALTERNATE(STM32F4GPIO_AF_USART3));
    // PD9 (RX) receive data from the Razor 9DoF IMU.
    palSetPadMode(GPIOD, 9, PAL_MODE_ALTERNATE(STM32F4GPIO_AF_USART3));

    // Start infrared reading thread.
    ThreadRazor9DoFIMU = chThdCreateStatic(workingAreaThread_Razor9DoFIMU,
                                           sizeof(workingAreaThread_Razor9DoFIMU),
                                           NORMALPRIO + 10, Thread_Razor9DoFIMU, NULL);
}
