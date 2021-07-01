package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumber implements Parcelable {
    public List names;
    public List numbers;

    public PhoneNumber(List names, List numbers){
        this.names = names;
        this.numbers = numbers;
    }

    protected PhoneNumber(Parcel in) {
        names = new ArrayList();
        in.readTypedList(names,PhoneNumber.CREATOR);
        numbers= new ArrayList();
        in.readTypedList(numbers,PhoneNumber.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(names);
        dest.writeList(numbers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
        @Override
        public PhoneNumber createFromParcel(Parcel in) {
            return new PhoneNumber(in);
        }

        @Override
        public PhoneNumber[] newArray(int size) {
            return new PhoneNumber[size];
        }
    };
}
