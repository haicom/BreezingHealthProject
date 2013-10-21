package com.breezing.health.entity;

public class RecordFunctionEntity {
    private int titleRes;
    private float value;
    private float max;
    private int colorRes;
    private int iconRes;

    public RecordFunctionEntity(int titleRes, float value, float max, int colorRes, int iconRes) {
        this.titleRes = titleRes;
        this.value = value;
        this.max = max;
        this.colorRes = colorRes;
        this.iconRes = iconRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getColorRes() {
        return colorRes;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

}
