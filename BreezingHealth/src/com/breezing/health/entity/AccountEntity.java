package com.breezing.health.entity;

import com.breezing.health.R;

public class AccountEntity {

	private int accountId;
    private String  accountName;
    private String  accountPass;
    private int     gender;
    private float  height;
    private int    birthday;
    private int    custom;
    private float  weight;
    private float  expectedWeight;
    private float  date;
    private String heightUnit;
    private String weightUnit;
    private String distanceUnit;
    private String caloricUnit;
    
    
    
    public String getHeightUnit() {
        return heightUnit;
    }
    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }
    public String getWeightUnit() {
        return weightUnit;
    }
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
    public String getDistanceUnit() {
        return distanceUnit;
    }
    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }
    public String getCaloricUnit() {
        return caloricUnit;
    }
    public void setCaloricUnit(String caloricUnit) {
        this.caloricUnit = caloricUnit;
    }
    public int getAccountId() {
		return accountId;
	}
    public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountPass() {
        return accountPass;
    }
    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }
    
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public int getBirthday() {
        return birthday;
    }
    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }
    public int getCustom() {
        return custom;
    }
    public void setCustom(int custom) {
        this.custom = custom;
    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public float getExpectedWeight() {
        return expectedWeight;
    }
    public void setExpectedWeight(float expectedWeight) {
        this.expectedWeight = expectedWeight;
    }
    public float getDate() {
        return date;
    }
    public void setDate(float date) {
        this.date = date;
    }
    
    
    
}
