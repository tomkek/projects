#
# Mini-Smart-Vehicles.
#
# This software is open source. Please see COPYING and AUTHORS for further information.
#

PROJECT (2014-DIT-168-project-template-csv-exporter)

# Include directories from core.
INCLUDE_DIRECTORIES (${libopendavinci_SOURCE_DIR}/include)
INCLUDE_DIRECTORIES (../msv-data/include)
INCLUDE_DIRECTORIES (include)

# Recipe for building "csv-exporter".
FILE(GLOB_RECURSE csv-exporter-sources "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp")
ADD_LIBRARY (csv-exporterlib STATIC ${csv-exporter-sources})
ADD_EXECUTABLE (csv-exporter "${CMAKE_CURRENT_SOURCE_DIR}/apps/MainModule.cpp")
TARGET_LINK_LIBRARIES (csv-exporter csv-exporterlib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS}) 

# Recipe for installing "csv-exporter".
INSTALL(TARGETS csv-exporter RUNTIME DESTINATION bin/2014/DIT-168/project-template) 

# Enable CxxTest for all available testsuites.
IF(CXXTEST_FOUND)
    FILE(GLOB_RECURSE csv-exporter-testsuites "${CMAKE_CURRENT_SOURCE_DIR}/testsuites/*.h")
    
    CXXTEST_ADD_TEST(csv-exporterTestSuite csv-exporterTestSuite.cpp ${csv-exporter-testsuites})
    TARGET_LINK_LIBRARIES(csv-exporterTestSuite csv-exporterlib ${MSVDATA_LIBS} ${OPENDAVINCI_LIBS} ${LIBS})
ENDIF(CXXTEST_FOUND)
