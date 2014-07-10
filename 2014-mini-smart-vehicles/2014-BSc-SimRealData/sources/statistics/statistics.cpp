/* ====================================
Basic statistical calculations:

Mean, Median, mode and standard_devation

========================================= */

#include <iostream>
#include <cmath>
#include <string>
#include <fstream>
#include <stdexcept>     
#include <vector>         
#include <iterator>
#include "statistics.h"
using namespace std;

double mode(double data[],int size){
   int i,j,max=0;
   int freq[size];
   //Set frequency equal to each data
   for(i=0;i<size;i++){
          freq[i]=1;
          }
    //set next data equal 0 if appear previous
    for(i=0;i<size;i++){
        for(j=1;j<size;j++)
        if(data[i]==data[i+j]){
        freq[i]++;
        data[i+j]=0;
        }
    }
    //Max of Frequency is mode
    for(i=0;i<size;i++){
        if(freq[i]>max)
           max=freq[i];
    }
    //Find Index of Max frequency
    int index;
    for(i=0;i<size;i++)
       if(max==freq[i])
            index=i;
    cout<<"\nMode: "<<data[index];
    cout<<"\n===========================================\n\n\n";
    //Show Histogram.
    cout<<"\t"<<"Histogram of Frequency"<<"\t";
     cout<<"\n===========================================\n\n";
     char g=219;

    for(i=0;i<size;i++){
        if(data[i]!=0){
        cout<<"| "<<data[i]<<"\t"<<"\t";
        for(j=1;j<=freq[i];j++)
        cout<<g;
        cout<<" "<<freq[i];
        cout<<"\n\n";
        
        }
      }
      cout<<"===========================================\n\n";
	return data[index];
	}
	
	double standard_devation(double data[],int size,double m){
	double sum_standard=0,standard;
	for(int i=0;i<size;i++){
	sum_standard+=pow((data[i]-m),2);
	}
	standard=sqrt(sum_standard/size);
	return standard;
	}
	
	
	double even_size_median(int data[],int size){
    //sort data from big to small
 	for(int i=0;i<size;i++){
    int max=i;
    for(int j=i+1;j<size;j++){
    if(data[j]>data[max])
    max=j;
    }
    double t=data[i];
    data[i]=data[max];
    data[max]=t;
	}
	double even_median;
	double first_middle,second_middle;
	first_middle=data[(size/2)-1];
	second_middle=data[size/2];
	even_median=(first_middle+second_middle)/2;
	return even_median;
	}
	
	
 	double odd_size_median(double data[],int size){
 	//sort data from big to small
 	for(int i=0;i<size;i++){
    int max=i;
    for(int j=i+1;j<size;j++){
    if(data[j]>data[max])
    max=j;
    }
    double t=data[i];
    data[i]=data[max];
    data[max]=t;
	}
	int median =(size+1)/2;
    int odd_median=data[median-1];
    return odd_median;
 	};
 	
	double mean(double data[],int size){
	double sum=0;
	for(int i=0;i<size;i++)
	sum+=data[i];
	double mean;
	mean=(double)sum/size;
	return mean;
	}

