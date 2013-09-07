package com.breezing.health.entity;

public class RecordFunctionEntity {

    private int titleRes;
    private float percentage;
    private int colorRes;
    
    public RecordFunctionEntity(int titleRes, float percentage, int colorRes) {
        this.titleRes = titleRes;
        this.percentage = percentage;
        this.colorRes = colorRes;
    }
    
    public int getTitleRes() {
        return titleRes;
    }
    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }
    public float getPercentage() {
        return percentage;
    }
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
    public int getColorRes() {
        return colorRes;
    }
    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }
    
}
