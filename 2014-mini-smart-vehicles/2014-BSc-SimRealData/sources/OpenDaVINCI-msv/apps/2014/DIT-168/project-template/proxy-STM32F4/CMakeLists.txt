#
# Mini-Smart-Vehicles.
#
# This software is open source. Please see COPYING and AUTHORS for further information.
#

PROJECT (2014-DIT-168-project-template-proxy-STM32F4)

# Include directories from core.
INCLUDE_DIRECTORIES (${libopendavinci_SOURCE_DIR}/include)
INCLUDE_DIRECTORIES (../msv-data/include)
INCLUDE_DIRECTORIES (include)
INCLUDE_DIRECTORIES (.)

# Command to generate the source and header files for the protocol.
ADD_CUSTOM_COMMAND(
  OUTPUT "${CMAKE_CURRENT_SOURCE_DIR}/Data.c"
  COMMAND java 
  ARGS    -jar ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../3rdParty/protoc-1.0M4.jar -I=${CMAKE_CURRENT_SOURCE_DIR} --c_out=${CMAKE_CURRENT_SOURCE_DIR} Data.proto && cp ${CMAKE_CURRENT_SOURCE_DIR}/Data.c ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../../STM32F4/embedded/ChibiOS/DiscoveryBoard2/Data.c && cp ${CMAKE_CURRENT_SOURCE_DIR}/Data.h ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../../STM32F4/embedded/ChibiOS/DiscoveryBoard2/Data.h
  DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/Data.proto"
)

# Add explicit dependency between Data.c and STM32F4Tester3.cpp.
SET_PROPERTY(SOURCE "${CMAKE_CURRENT_SOURCE_DIR}/src/STM32F4Tester3.cpp" APPEND PROPERTY OBJECT_DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/Data.c")

# Recipe for building "proxy-STM32F4".
FILE(GLOB_RECURSE proxy-STM32F4-sources "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp")
ADD_LIBRARY (proxy-STM32F4lib STATIC ${proxy-STM32F4-sources})
ADD_EXECUTABLE (proxy-STM32F4 "${CMAKE_CURRENT_SOURCE_DIR}/apps/MainModule.cpp")
ADD_EXECUTABLE (STM32F4Test2 "${CMAKE_CURRENT_SOURCE_DIR}/apps/STM32F4Test2.cpp")
ADD_EXECUTABLE (STM32F4Test3 "${CMAKE_CURRENT_SOURCE_DIR}/apps/STM32F4Test3.cpp" "${CMAKE_CURRENT_SOURCE_DIR}/Data.c")
TARGET_LINK_LIBRARIES (proxy-STM32F4 proxy-STM32F4lib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS}) 
TARGET_LINK_LIBRARIES (STM32F4Test2 proxy-STM32F4lib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS}) 
TARGET_LINK_LIBRARIES (STM32F4Test3 proxy-STM32F4lib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS}) 

# Recipe for installing "proxy-STM32F4".
INSTALL(TARGETS proxy-STM32F4 RUNTIME DESTINATION bin/2014/DIT-168/project-template) 
INSTALL(TARGETS STM32F4Test2 RUNTIME DESTINATION bin/2014/DIT-168/project-template) 
INSTALL(TARGETS STM32F4Test3 RUNTIME DESTINATION bin/2014/DIT-168/project-template) 

# Enable CxxTest for all available testsuites.
IF(CXXTEST_FOUND)
    FILE(GLOB_RECURSE proxy-STM32F4-testsuites "${CMAKE_CURRENT_SOURCE_DIR}/testsuites/*.h")
    
    CXXTEST_ADD_TEST(proxy-STM32F4TestSuite proxy-STM32F4TestSuite.cpp ${proxy-STM32F4-testsuites})
    TARGET_LINK_LIBRARIES(proxy-STM32F4TestSuite proxy-STM32F4lib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS})
ENDIF(CXXTEST_FOUND)
