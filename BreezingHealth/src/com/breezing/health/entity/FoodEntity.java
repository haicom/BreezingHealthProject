package com.breezing.health.entity;

public class FoodEntity {

	private int foodClassifyId;
    private String foodName;
    private String nameExpress;
    private int priority;
    private int foodQuantity;
    private int calorie;
    private int imageRes;
    private int selectedNumber;
    
    
    public int getFoodClassifyId() {
        return foodClassifyId;
    }
    public void setFoodClassifyId(int foodClassifyId) {
        this.foodClassifyId = foodClassifyId;
    }
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getNameExpress() {
		return nameExpress;
	}
	public void setNameExpress(String nameExpress) {
		this.nameExpress = nameExpress;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getFoodQuantity() {
		return foodQuantity;
	}
	public void setFoodQuantity(int foodQuantity) {
		this.foodQuantity = foodQuantity;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public int getImageRes() {
		return imageRes;
	}
	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}
	public int getSelectedNumber() {
		return selectedNumber;
	}
	public void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}
    public void increaseSelectedNumber() {
    	selectedNumber++;
    }
    
    public void decreaseSelectedNumber() {
    	if (selectedNumber > 0) {
    		selectedNumber--;
    	}
    }
   
    
}
