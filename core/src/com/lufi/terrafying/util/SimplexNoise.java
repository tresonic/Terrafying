package com.lufi.terrafying.util;

import java.util.Random;

public class SimplexNoise {

    SimplexNoise_octave[] octaves;
    double[] frequencies;
    double[] amplitudes;

    int largestFeature;
    double persistence;
    int seed;

    /**
     * @param largestFeature 
     * @param persistence
     * @param seed the random seed to use
     */
    public SimplexNoise(int largestFeature,double persistence, int seed){
        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));
        //System.out.println(numberOfOctaves);

        octaves=new SimplexNoise_octave[numberOfOctaves];
        frequencies=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];

        Random rnd=new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoise_octave(rnd.nextInt());

            frequencies[i] = Math.pow(2,i);
            //System.out.println("F="+frequencies[i]+" "+i);
            amplitudes[i] = (Math.pow(persistence,octaves.length-i)/2);
            //System.out.println("A="+amplitudes[i]+" "+i);
        }

    }


    public double getNoise(int x, int y){
        double result=0;

        for(int i=0;i<octaves.length;i++){
          result=result+octaves[i].noise(x/frequencies[i], y/frequencies[i])* amplitudes[i];
        }
        return result;
    }

    public double getNoise(int x,int y, int z){
        double result=0;

        for(int i=0;i<octaves.length;i++){
          double frequency = Math.pow(2,i);
          double amplitude = Math.pow(persistence,octaves.length-i);
          result=result+octaves[i].noise(x/frequency, y/frequency,z/frequency)* amplitude;
        }
        return result;
    }
    
    public double getSingleNoise(int x, int y, int z){
    	double result = 0;
    	SimplexNoise_octave myoctave = new SimplexNoise_octave(0);
    	result = myoctave.noise(x,y,z);
    	return result;
    }
    
    public double getDoubleNoise(int x, int y, int z){
    	double result = 0;
    	SimplexNoise_octave myoctave = new SimplexNoise_octave(0);
    	SimplexNoise_octave myoctave2 = new SimplexNoise_octave(1);
    	result = (myoctave.noise(x,y,z) + 
    			(myoctave2.noise(x,y,z) /2)
    			);
    	result = result / 2;
    	return result;
    }
    
    public double getQuadNoise(int x, int y, int z){
    	double result = 0;
    	SimplexNoise_octave myoctave = new SimplexNoise_octave(0);
    	SimplexNoise_octave myoctave2 = new SimplexNoise_octave(10);
    	SimplexNoise_octave myoctave3 = new SimplexNoise_octave(20);
    	SimplexNoise_octave myoctave4 = new SimplexNoise_octave(40);
    	result = (myoctave.noise(x,y,z) + 
    			(myoctave2.noise(x,y,z) /2) +
    			(myoctave3.noise(x,y,z) /4) +
    			(myoctave4.noise(x,y,z) /8) 
    			);
    	result = result / 4;
    	return result;
    }
    
    public double getOctNoise(int x, int y, int z){
    	double result = 0;
    	SimplexNoise_octave myoctave = new SimplexNoise_octave(0);
    	SimplexNoise_octave myoctave2 = new SimplexNoise_octave(10);
    	SimplexNoise_octave myoctave3 = new SimplexNoise_octave(20);
    	SimplexNoise_octave myoctave4 = new SimplexNoise_octave(40);
    	SimplexNoise_octave myoctave5 = new SimplexNoise_octave(80);
    	SimplexNoise_octave myoctave6 = new SimplexNoise_octave(100);
    	SimplexNoise_octave myoctave7 = new SimplexNoise_octave(120);
    	SimplexNoise_octave myoctave8 = new SimplexNoise_octave(140);
    	result = (myoctave.noise(x,y,z) + 
    			(myoctave2.noise(x,y,z) /2) +
    			(myoctave3.noise(x,y,z) /4) +
    			(myoctave4.noise(x,y,z) /8) +
    			(myoctave5.noise(x,y,z) /16) +
    			(myoctave6.noise(x,y,z) /32) +
    			(myoctave7.noise(x,y,z) /64) +
    			(myoctave8.noise(x,y,z) /128) 
    			);
    	result = result / 8;
    	return result;
    }
} 
