/*
 * OpenDaVINCI - STM32F4 Discovery Board Software/Hardware Interface.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include "DiscoveryBoard.h"

#include "Example/RPNCalculator.h"

///////////////////////////////////////////////////////////////////////////////
// Data structures and configuation.
///////////////////////////////////////////////////////////////////////////////

DataT myData;

///////////////////////////////////////////////////////////////////////////////
// Interface methods.
///////////////////////////////////////////////////////////////////////////////

void initializeUserProtocol(void) {
    int i = 0;
    int bufferLength = 1024;
    for (i = 0; i < bufferLength; i++) {
        myData.payload[i] = 0;
        myData.length = 0;
    }
}

void consumeDataFromHost(DataT *ptrToDataFromHost) {
    if (ptrToDataFromHost != NULL) {
        // For now, we simply run the RPN calculator as a demonstration.
        // Example: Processing received input via RPN Calculator.
        int result = RPN_calculator(ptrToDataFromHost->payload);

        chsprintf(myData.payload, "%d", result);
        myData.length = log(result)/log(10) + 1; // Count number of characters of the result.
    }
}

void produceDataForHost_AfterConsumption(DataT *ptrToDataForHost, int maxBufferLength) {
    int i = 0;
    if (ptrToDataForHost != NULL && maxBufferLength > 0) {
        // Copy data to send buffer.
        if (myData.length < maxBufferLength) {
            for(i = 0; i < myData.length; i++) {
                ptrToDataForHost->payload[i] = myData.payload[i];
            }

            ptrToDataForHost->length = myData.length;
        }
    }
}

void produceDataForHost_WithoutConsumption(DataT *ptrToDataForHost, int maxBufferLength) {
    if (ptrToDataForHost != NULL && maxBufferLength > 0) {
        // This method is not used at the moment.
    }
}

