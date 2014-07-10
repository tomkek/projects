#ifndef statistics
#define statistics

#include <iostream>
#include <cmath>
#include <string>
#include <fstream>
#include <stdexcept>     
#include <vector>         
#include <iterator> 
using namespace std;
double mean(double data[],int size);
double odd_size_median(double data[],int size);
double even_size_median(double data[],int size);
double standard_devation(double data[],int size,double m);
double mode(double data[],int size);
#endif 
