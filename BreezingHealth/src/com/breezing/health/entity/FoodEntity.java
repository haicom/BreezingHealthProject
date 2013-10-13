package com.breezing.health.entity;

public class FoodEntity {
	private int mFoodClassifyId;
	private int mFoodId;
    private String mFoodName;
    private String mNameExpress;
    private int mPriority;
    private int mFoodQuantity;
    private int mCalorie;
    private String mImageRes;
    private int mSelectedNumber = 0;
    
    public int getFoodId() {
        return mFoodId;
    }

    public void setFoodId(int foodId) {
        this.mFoodId = foodId;
    }
    
    public int getFoodClassifyId() {
        return mFoodClassifyId;
    }

    public void setFoodClassifyId(int foodClassifyId) {
        this.mFoodClassifyId = foodClassifyId;
    }

	public String getFoodName() {
		return mFoodName;
	}

	public void setFoodName(String foodName) {
		this.mFoodName = foodName;
	}

	public String getNameExpress() {
		return mNameExpress;
	}

	public void setNameExpress(String nameExpress) {
		this.mNameExpress = nameExpress;
	}

	public int getPriority() {
		return mPriority;
	}

	public void setPriority(int priority) {
		this.mPriority = priority;
	}

	public int getFoodQuantity() {
		return mFoodQuantity;
	}

	public void setFoodQuantity(int foodQuantity) {
		this.mFoodQuantity = foodQuantity;
	}

	public int getCalorie() {
		return mCalorie;
	}

	public void setCalorie(int calorie) {
		this.mCalorie = calorie;
	}

	public String getImageRes() {
		return mImageRes;
	}

	public void setImageRes(String imageRes) {
		this.mImageRes = imageRes;
	}

	public int getSelectedNumber() {
		return mSelectedNumber;
	}
	public void setSelectedNumber(int selectedNumber) {
		this.mSelectedNumber = selectedNumber;
	}
    public void increaseSelectedNumber() {
        mSelectedNumber++;
    }
    
    public void decreaseSelectedNumber() {
        if ( mSelectedNumber > 0 ) {
            mSelectedNumber--;
        }
    }
   
    
}
