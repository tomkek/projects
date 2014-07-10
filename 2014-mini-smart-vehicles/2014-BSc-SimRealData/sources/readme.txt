SIMULATION AND REAL DATA COMPARISON README FILE


Move to Image_Substraction dir  to test the pictures

 compile like this if using g++

g++ -Wall -g -o final image_comparison.cpp /home/leila-keza/2014-BSc-SimRealData/2014-mini-smart-vehicles/2014-BSc-SimRealData/sources/statistics/statistics.cpp `pkg-config --cflags --libs opencv`

run ./image 

The  statistics .cpp  path is included for statistical calculations on the images 

move to  sensors_data_Test to run sensors's test

run . /sensors or compile like this if using g++

g++ -Wall -g -o sensors SensorsTest.cpp /home/leila-keza/2014-BSc-SimRealData/2014-mini-smart-vehicles/2014-BSc-SimRealData/sources/statistics/statistics.cpp `pkg-config --cflags --libs opencv`

run  . /sensors
The  statistics.cpp  path is included for statistical calculations on the sensors data


