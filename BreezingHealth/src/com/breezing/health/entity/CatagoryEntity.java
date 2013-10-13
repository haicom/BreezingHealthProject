package com.breezing.health.entity;

public class CatagoryEntity {
    private int id;
    private String name;
    private String iconRes;
    private boolean isChecked;
    
    public CatagoryEntity(int id, String name, String iconRes) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
    }    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIconRes() {
        return iconRes;
    }
    
    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    
}
