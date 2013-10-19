package com.breezing.health.entity;

import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.WeightChange;

public class WeightInfoEntity {
   String mWeightUnit;
   float mWeight;
   float mEveryWeight;
   float mExpectedWeight;
   int   mDate;
   
   
    public String getWeightUnit() {
        return mWeightUnit;
    }
    public void setWeightUnit(String weightUnit) {
        this.mWeightUnit = weightUnit;
    }
    public float getWeight() {
        return mWeight;
    }
    public void setWeight(float weight) {
        mWeight = weight;
    }
    public float getEveryWeight() {
        return mEveryWeight;
    }
    public void setEveryWeight(float everyWeight) {
        this.mEveryWeight = everyWeight;
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

}
