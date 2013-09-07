package com.breezing.health.entity;

public class MenuItem {

    private String action;
    private int iconRes;
    private String name;
    
    public MenuItem(String name, int iconRes, String action) {
        this.name = name;
        this.iconRes = iconRes;
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public int getIconRes() {
        return iconRes;
    }
    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
