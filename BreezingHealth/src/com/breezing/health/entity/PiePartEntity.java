package com.breezing.health.entity;

public class PiePartEntity {

    private float value;
    private int color;
    
    public PiePartEntity(float value, int color) {
        this.value = value;
        this.color = color;
    }
    
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    
}
