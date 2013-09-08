package com.breezing.health.entity;

public class MenuItem {

    private int action;
    private int iconRes;
    private String name;
    
    public MenuItem(String name, int iconRes, int action) {
        this.name = name;
        this.iconRes = iconRes;
        this.action = action;
    }
    
    public int getAction() {
        return action;
    }
    public void setAction(int action) {
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
