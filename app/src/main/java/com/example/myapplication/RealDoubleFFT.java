package com.example.myapplication;

public class RealDoubleFFT extends RealDoubleFFT_Mixed
{

     public double norm_factor;
     private double wavetable[];
     private int ndim;


     public RealDoubleFFT(int n)
     {
          ndim = n;
          norm_factor = n;
          if(wavetable == null || wavetable.length !=(2*ndim+15))
          {
              wavetable = new double[2*ndim + 15];
          }
          rffti(ndim, wavetable);
     }

     public void ft(double x[])
     {
         if(x.length != ndim)
              throw new IllegalArgumentException("The length of data can not match that of the wavetable");
         rfftf(ndim, x, wavetable);
     }


     public void bt(double x[])
     {
         if(x.length != ndim)
              throw new IllegalArgumentException("The length of data can not match that of the wavetable");
         rfftb(ndim, x, wavetable);
     }


}
