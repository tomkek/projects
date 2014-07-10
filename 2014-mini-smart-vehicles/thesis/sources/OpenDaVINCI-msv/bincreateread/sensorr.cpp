#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <fstream>
#include <sys/time.h>
using namespace std;

int main()
{

  std::fstream file2("sensors.bin", ios::in | ios::out | ios::binary);

    unsigned int len = 0;
    while (file2.get() != EOF) len++;

    int millis;
    double ir1;
    double ir2;
    double ir3;
    double us1;
    double us2;


  std::fstream file("sensors.bin", ios::in | ios::out | ios::binary);
  cerr << len << endl;

    for(int i = 0; i < len/44; i++){
        file.read((char*)&ir1, sizeof(ir1));
        file.read((char*)&ir2, sizeof(ir2));
        file.read((char*)&ir3, sizeof(ir3));
        file.read((char*)&us1, sizeof(us1));
        file.read((char*)&us2, sizeof(us2));
        file.read((char*)&millis, sizeof(millis));
    cerr<< millis << " -> " << ir1 << "|" << ir2 << "|" << ir3 << "|" << us1 << "|" << us2 << endl;
    }

}




