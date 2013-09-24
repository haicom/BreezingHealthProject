package com.breezing.health.entity;

public class FoodEntity {

	private String foodType;
    private String foodName;
    private String nameExpress;
    private int priority;
    private int foodQuantity;
    private int calorie;
    private int imageRes;
    private int selectedNumber;
    
    
    public String getFoodType() {
		return foodType;
	}
    public void setFoodType(String foodType) {
		this.foodType = foodType;
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
