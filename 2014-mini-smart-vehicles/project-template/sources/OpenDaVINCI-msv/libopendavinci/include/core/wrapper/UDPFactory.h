/*
 * OpenDaVINCI.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#ifndef OPENDAVINCI_CORE_WRAPPER_UDPFACTORY_H_
#define OPENDAVINCI_CORE_WRAPPER_UDPFACTORY_H_

// core/platform.h must be included to setup platform-dependent header files and configurations.
#include "core/platform.h"
#include "core/wrapper/UDPSender.h"
#include "core/wrapper/UDPReceiver.h"

namespace core {
    namespace wrapper {

        struct OPENDAVINCI_API UDPFactory
        {
            /**
             * This method creates a UDP sender.
             *
             * @param address Address.
             * @param port Port.
             * @return A new UDPSender
             */
            static UDPSender* createUDPSender(const string &address, const uint32_t &port);

            /**
             * This method creates a UDP receiver.
             *
             * @param address Address to bind on.
             * @param port Port.
             * @return A new UDPReceiver
             */
            static UDPReceiver* createUDPReceiver(const string &address, const uint32_t &port);
        };
    }
} // core::wrapper

#endif /*OPENDAVINCI_CORE_WRAPPER_UDPFACTORY_H_*/
