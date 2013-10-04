package com.breezing.health.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseInformationOutput implements Parcelable {
    int  mAccountId;
    String mName;
    int    mGender;
    int    mAge;
    int    mCustom;
    String mHeightUnit;
    String mWeightUnit;
    float  mHeight;
    float  mWeight;
    float  mExpectedWeight;
    int    mDate;
    
    public BaseInformationOutput(){
        super();
    }
    
    public BaseInformationOutput(Parcel in) {
        mAccountId = in.readInt();
        mName = in.readString();
        mGender = in.readInt();
        mAge = in.readInt();
        mCustom = in.readInt();
        mHeightUnit = in.readString();
        mWeightUnit = in.readString();
        mHeight = in.readFloat();
        mWeight = in.readFloat();
        mExpectedWeight = in.readFloat();
        mDate = in.readInt();
    }
    
    public int getAccount() {
        return mAccountId;
    }
    
    public void setAccount(int account) {
        this.mAccountId = account;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String name) {
        this.mName = name;
    }
    
    public int getGender() {
        return mGender;
    }
    
    public void setGender(int gender) {
        this.mGender = gender;
    }
    
    public int getAge() {
        return mAge;
    }
    public void setAge(int age) {
        this.mAge = age;
    }
    
   
    
    public String getHeightUnit() {
        return mHeightUnit;
    }
    
    public void setHeightUnit(String heightUnit) {
        this.mHeightUnit = heightUnit;
    }
    
    public String getWeightUnit() {
        return mWeightUnit;
    }   
    
    public void setWeightUnit(String weightUnit) {
        this.mWeightUnit = weightUnit;
    }
    
    
    public void setCustom(int custom) {
        this.mCustom = custom;
    }
    
    public int getCustom() {
        return mCustom;
    }
    
    public float getHeight() {
        return mHeight;
    }    
    
    public void setHeight(float height) {
        this.mHeight = height;
    }
    
    public float getWeight() {
        return mWeight;
    }
    
    public void setWeight(float weight) {
        this.mWeight = weight;
    }
    
    public float getExpectedWeight() {
        return mExpectedWeight;
    }
    
    public void setExpectedWeight(float expectedWeight) {
        this.mExpectedWeight = expectedWeight;
    }
    
    public int getDate() {
        return mDate;
    }
    
    public void setDate(int date) {
        this.mDate = date;
    }   

    @Override
    public int describeContents() {       
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mAccountId);
        out.writeString(mName);
        out.writeInt(mGender);
        out.writeInt(mAge);
        out.writeInt(mCustom); 
        out.writeString(mHeightUnit);
        out.writeString(mWeightUnit);
        out.writeFloat(mHeight);
        out.writeFloat(mWeight);
        out.writeFloat(mExpectedWeight);
        out.writeInt(mDate);
    }
   
    public static final Parcelable.Creator<BaseInformationOutput> CREATOR
        = new Parcelable.Creator<BaseInformationOutput>() {
            public BaseInformationOutput createFromParcel(Parcel in) {
                return new BaseInformationOutput(in);
            }

        public BaseInformationOutput[] newArray(int size) {
           return new BaseInformationOutput[size];
        }
    };
}
