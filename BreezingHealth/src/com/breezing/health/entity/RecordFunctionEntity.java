package com.breezing.health.entity;

public class RecordFunctionEntity {
    private int titleRes;
    private int value;
    private int max;
    private int colorRes;

    public RecordFunctionEntity(int titleRes, int value, int max, int colorRes) {
        this.titleRes = titleRes;
        this.value = value;
        this.max = max;
        this.colorRes = colorRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getColorRes() {
        return colorRes;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

}
