package com.josholadele.huaweitest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class ParcelableAuth implements Parcelable {
    public boolean isEmail;
    public String countryCode;
    public String phoneNumber;
    public String email;
    public String password;
    public String userId;

    protected ParcelableAuth(Parcel in) {
        isEmail = in.readInt() == 1;
        countryCode = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        password = in.readString();
        userId = in.readString();
    }

    public ParcelableAuth() {
    }

    public ParcelableAuth(boolean isEmail, String countryCode, String phoneNumber, String email, String password) {
        this.isEmail = isEmail;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public ParcelableAuth(boolean isEmail, String countryCode, String phoneNumber, String email, String password, String userId) {
        this.isEmail = isEmail;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public static final Creator<ParcelableAuth> CREATOR = new Creator<ParcelableAuth>() {
        @Override
        public ParcelableAuth createFromParcel(Parcel in) {
            return new ParcelableAuth(in);
        }

        @Override
        public ParcelableAuth[] newArray(int size) {
            return new ParcelableAuth[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(isEmail ? 1 : 0);
        parcel.writeString(countryCode);
        parcel.writeString(phoneNumber);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(userId);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
