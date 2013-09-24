package com.breezing.health.entity;

public class CatagoryEntity {

    private String name;
    private int iconRes;
    private boolean isChecked;
    
    public CatagoryEntity(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIconRes() {
        return iconRes;
    }
    
    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
    
}
