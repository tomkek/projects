/*
 * OpenDaVINCI - STM32F4 Discovery Board Software/Hardware Interface.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#ifndef PERIPHERALS_H
#define PERIPHERALS_H

#define USE_ONBOARD_ACCELEROMETER  1 // If this is set to 1, the onboard accelerometer can be read. Please be aware that using this sensor limits the RC receiver to read only to two input channels because TIM14 at pin PA7 is in conflict with this sensor.
#define USE_ONBOARD_TEMPERATURE    0 // If the onboard temperature sensor is used the infrared sensors cannot be used!
#define USE_INFRARED               1 // Support reading values from analog infrared sensors (not available if the temperature sensor is used!).
#define USE_ULTRASONIC             1 // Support reading values from I2C ultrasonic sensors.
#define USE_RCRECEIVER             1 // Support for reading ICU data from an RC-receiver hand set. If the onboard accelerometer is not used, up to three channels are supported (otherwise, only two channels are supported).
#define USE_WHEELENCODER           1 // Support for reading ICU data from a four channel wheel encoder. 
#define USE_RAZORBOARD             1 // Support for reading serial data from a 9DoF razor board.
#define USE_ACTORS                 1 // Support for interfacing with steering and acceleration actors.

#endif // PERIPHERALS_H

