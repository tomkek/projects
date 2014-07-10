/*
 * Mini-Smart-Vehicles.
 *
 * This software is open source. Please see COPYING and AUTHORS for further information.
 */

#include <cstring>
#include <cmath>

#include <string>

#include "core/macros.h"
#include "core/base/KeyValueConfiguration.h"
#include "core/io/URL.h"
#include "core/wrapper/SerialPortFactory.h"
#include "core/wrapper/SerialPort.h"

#include "STM32F4Protocol.h"
#include "STM32F4Tester3.h"

#include "Data.h"

namespace msv {

    using namespace std;
    using namespace core::base;
    using namespace core::io;

    STM32F4Tester3::STM32F4Tester3(const int32_t &argc, char **argv) :
	    ConferenceClientModule(argc, argv, "proxy-STM32F4"),
        m_partialData(),
        m_receivedPayload()
     {}

    STM32F4Tester3::~STM32F4Tester3() {
    }

    void STM32F4Tester3::setUp() {
	    // This method will be call automatically _before_ running body().
    }

    void STM32F4Tester3::tearDown() {
	    // This method will be call automatically _after_ return from body().
    }

    void STM32F4Tester3::handleConnectionError() {
        cout << "STM32F4Tester3: TODO: Handle connection error here." << endl;
    }

    void STM32F4Tester3::receivedPartialString(const string &s) {
        m_partialData.write(s.c_str(), s.length());

        cout << "Received: '" << s << "', buffer: '" << m_partialData.str() << "'" << endl;

        decodeNetstring();

        // Put the write pointer to the end of the stream.
        m_partialData.seekp(0, ios_base::end);
    }

    void STM32F4Tester3::decodeNetstring(void) {
        // Netstrings have the following format:
        // ASCII Number representing the length of the payload + ':' + payload + ','

        m_partialData.seekg(0, ios_base::beg);

        // Start decoding only if we have received enough data.
        while(m_partialData.str().length() > 3) {
            unsigned int lengthOld = m_partialData.str().length();
            const char *receiveBuffer = m_partialData.str().c_str();
            char *colonSign = NULL;
            unsigned int lengthOfPayload = strtol(receiveBuffer, &colonSign, 10);
            if (lengthOfPayload == 0) {
                // Remove empty Netstring: 0:, (size is 3).

                m_partialData.str(m_partialData.str().substr(3));
                continue; // Try to decode next Netstring if any.
            }

            if (*colonSign == 0x3a) {
                // Found colon sign. Now, check if (receiveBuffer + 1 + lengthOfPayload) == ','.
                if ((colonSign[1 + lengthOfPayload]) == 0x2c) {
                    // Successfully found a complete Netstring.
                    m_receivedPayload = m_partialData.str().substr((colonSign + 1) - receiveBuffer, lengthOfPayload);

                    // Remove decoded Netstring: "<lengthOfPayload> : <payload> ,"
                    int lengthOfNetstring = (colonSign + 1 + lengthOfPayload + 1) - receiveBuffer;

                    m_partialData.str(m_partialData.str().substr(lengthOfNetstring));
                }
            }

            if (lengthOld == m_partialData.str().length()) {
                // This should not happen, received data might be corrupt.

                // Reset buffer.
                m_partialData.str("");
            }
        }    
    }

    string STM32F4Tester3::encodeNetstring(const string &d) {
        stringstream netstring;
        if (d.length() > 0) {
            netstring << (int)d.length() << ":" << d << ",";
        }
        else {
            netstring << "0:,";
        }
        return netstring.str();
    }

    // This method will do the main data processing job.
    ModuleState::MODULE_EXITCODE STM32F4Tester3::body() {
        // Get configuration data.
        KeyValueConfiguration kv = getKeyValueConfiguration();

        const URL u(kv.getValue<string>("proxy-STM32F4.serial_port"));
        const string SERIAL_PORT = u.getResource();
        const uint32_t SERIAL_SPEED = kv.getValue<uint32_t>("proxy-STM32F4.serial_speed");

        cerr << "STM32F4Tester3: Connecting to port " << SERIAL_PORT << "@" << SERIAL_SPEED << endl;

        // Open serial port.
        core::wrapper::SerialPort *serialPort = core::wrapper::SerialPortFactory::createSerialPort(SERIAL_PORT, SERIAL_SPEED);
        serialPort->setPartialStringReceiver(this);

        // Start receiving.
        serialPort->start();

        struct ToSTM32F4Board toBoard;
        struct FromSTM32F4Board fromBoard;
        char buffer[40];
        while (getModuleState() == ModuleState::RUNNING) {
            cout << "Type summand a:" << endl;
            cin >> toBoard._summand1;

            cout << "Type summand b:" << endl;
            cin >> toBoard._summand2;

            int length = ToSTM32F4Board_write_delimited_to(&toBoard, buffer, 0);
            cout << "Length: " << length << endl;
            string data(buffer, length);

            for(int i = 0; i < length; i++) {
                cout << (int)(buffer[i]) << ":'" << (char)(buffer[i]) << "', " << (int)(data.at(i)) << ":'" << (char)(data.at(i)) << "'" << endl;
            }

            cout << endl;

            string netstring = encodeNetstring(data);

            for(unsigned int i = 0; i < netstring.length(); i++) {
                cout << (int)(netstring.at(i)) << ":'" << (char)(netstring.at(i)) << "'" << endl;
            }

            cout << "Sending '" << netstring << "'" << endl;
            serialPort->send(netstring);

            sleep(1);

            cout << "Receive buffer '" << m_receivedPayload << "'" << endl;

            FromSTM32F4Board_read_delimited_from((char*)m_receivedPayload.c_str(), &fromBoard, 0);

            cout << "Result: " << fromBoard._sum << endl;
        }

        // Stop receiving.
        serialPort->stop();

        // Destroy connections to UDP_Server.
        OPENDAVINCI_CORE_DELETE_POINTER(serialPort);

        return ModuleState::OKAY;
    }

} // msv
