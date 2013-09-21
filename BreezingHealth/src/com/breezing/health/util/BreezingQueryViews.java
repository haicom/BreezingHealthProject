package com.breezing.health.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;


import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.providers.Breezing.HeatIngestion;

public class BreezingQueryViews {
    private static final String TAG = "BreezingQueryViews";
    private ContentResolver mContentResolver;
    
    public BreezingQueryViews(Context context) {
        mContentResolver = context.getContentResolver();
    }
    
    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Account.ACCOUNT_NAME,          // 0
        Account.ACCOUNT_PASSWORD,      // 1
        Information.GENDER,    // 2
        Information.HEIGHT ,     //3
        Information.BIRTHDAY ,   //4
        Information.CUSTOM ,            //5
        WeightChange.WEIGHT,            //6
        WeightChange.EXPECTED_WEIGHT,   //7
        WeightChange.DATE              //8
    };
    
    private static final int INFO_ACCOUNT_NAME_INDEX = 0;
    private static final int INFO_ACCOUNT_PASSWORD_INDEX = 1;
    private static final int INFO_GENDER_INDEX = 2;
    private static final int INFO_HEIGHT_INDEX = 3;
    private static final int INFO_BIRTHDAY_INDEX = 4;
    private static final int INFO_CUSTOM_INDEX = 5;
    private static final int INFO_WEIGHT_INDEX = 6;
    private static final int INFO_EXPECTED_WEIGHT_INDEX = 7;
    private static final int INFO_DATE_INDEX = 8;
    
    /**
     * 根据某一个帐户id查询基本信息视图
     */
    public void queryBaseInfoViews(int accountId) {
        Log.d(TAG, "queryBaseInfoView");
        
        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        String[] args = new String[] {String.valueOf(accountId)};
        
        Cursor cursor  = mContentResolver.query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                String  accountName = cursor.getString(INFO_ACCOUNT_NAME_INDEX);
                String  accountPass = cursor.getString(INFO_ACCOUNT_PASSWORD_INDEX);
                int     gender = cursor.getInt(INFO_GENDER_INDEX);
                float  height = cursor.getFloat(INFO_HEIGHT_INDEX);
                int    birthday = cursor.getInt(INFO_BIRTHDAY_INDEX);
                int    custom = cursor.getInt(INFO_CUSTOM_INDEX);
                float  weight = cursor.getFloat(INFO_WEIGHT_INDEX);
                float  expectedWeight = cursor.getFloat(INFO_EXPECTED_WEIGHT_INDEX);
                float  date = cursor.getFloat(INFO_DATE_INDEX);
                Log.d(TAG, " testCostWeeklyAndAccount accountName = " + accountName + " accountPass = " + accountPass
                        + " gender = " + gender + " height = " + height
                        + " birthday = " + birthday + " birthday = " + birthday 
                        + " custom =  " + custom + " weight = " + weight 
                        + " expectedWeight = " + expectedWeight
                        + " date = " + date);
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的总能量消耗查看每一周，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_ENERGY_COST_WEEKLY = new String[] {
        EnergyCost.ACCOUNT_ID,          // 0
        EnergyCost.AVG_METABOLISM,      // 1
        EnergyCost.AVG_TOTAL_ENERGY,    // 2
        EnergyCost.ALL_METABOLISM ,     //3
        EnergyCost.ALL_TOTAL_ENERGY ,   //4
        EnergyCost.YEAR_WEEK            //5
    };
    
    private static final int ACCOUNT_ID_COLUMN_WEEKLY_INDEX = 0;
    private static final int AVG_METABOLISM_COLUMN_WEEKLY_INDEX = 1;
    private static final int AVG_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX = 2;
    private static final int ALL_METABOLISM_COLUMN_WEEKLY_INDEX = 3;
    private static final int ALL_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX = 4;
    private static final int YEAR_WEEK_COLUMN_WEEKLY_INDEX = 5;
    
    /**
     * 根据某一个帐户id查询我的总能量消耗每周信息
     */
    public void queryCostWeeklyAndAccount(int account) {
        Log.d(TAG, "testCostWeeklyAndAccount");
        String accountClause = EnergyCost.ACCOUNT_ID + " = ?";
        String sortOrder = EnergyCost.YEAR_WEEK + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(EnergyCost.CONTENT_WEEKLY_URI,
                PROJECTION_ENERGY_COST_WEEKLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testCostWeekly cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_COLUMN_WEEKLY_INDEX);
                long avgMetabolism = cursor.getLong(AVG_METABOLISM_COLUMN_WEEKLY_INDEX);
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX);
                long allMetabolism = cursor.getLong(ALL_METABOLISM_COLUMN_WEEKLY_INDEX);
                long allTotalEnergy = cursor.getLong(ALL_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX);
                long yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " testCostWeeklyAndAccount accountId = " + accountId + " avgMetabolism = " + avgMetabolism
                        + " avgTotalEnergy = " + avgTotalEnergy + " allMetabolism = " + allMetabolism
                        + " allTotalEnergy = " + allTotalEnergy + " yearWeek = " + yearWeek);
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的总能量消耗查看每月，某一个帐户的月信息列表
     */
    private static final String[] PROJECTION_ENERGY_MONTHLY_COST = new String[] {
        EnergyCost.ACCOUNT_ID,          // 0
        EnergyCost.AVG_METABOLISM,      // 1
        EnergyCost.AVG_TOTAL_ENERGY,    // 2
        EnergyCost.ALL_METABOLISM ,     //3
        EnergyCost.ALL_TOTAL_ENERGY ,   //4
        EnergyCost.YEAR_MONTH           //5
    };

    private static final int ACCOUNT_ID_COLUMN_MONTHLY_INDEX = 0;
    private static final int AVG_METABOLISM_COLUMN_MONTHLY_INDEX = 1;
    private static final int AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX = 2;
    private static final int ALL_METABOLISM_COLUMN_MONTHLY_INDEX = 3;
    private static final int ALL_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX = 4;
    private static final int YEAR_MONTH_COLUMN_MONTHLY_INDEX = 5;
    
    /**
     * 根据某一个帐户id查询我的总能量消耗月信息
     */
    public void queryCostlyMonthlyAndAccount(int account) {
        Log.d(TAG, "testCostlyMonthlyAndAccount");
        String accountClause = EnergyCost.ACCOUNT_ID + " = ?";
        String sortOrder = EnergyCost.YEAR_MONTH + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(EnergyCost.CONTENT_MONTHLY_URI,
                PROJECTION_ENERGY_MONTHLY_COST, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testCostWeekly cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_COLUMN_MONTHLY_INDEX);
                long avgMetabolism = cursor.getLong(AVG_METABOLISM_COLUMN_MONTHLY_INDEX);
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX);
                long allMetabolism = cursor.getLong(ALL_METABOLISM_COLUMN_MONTHLY_INDEX);
                long allTotalEnergy = cursor.getLong(ALL_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX);
                long yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_MONTHLY_INDEX);
                Log.d(TAG, " testCostlyMonthlyAndAccount accountId = " + accountId + " avgMetabolism = " + avgMetabolism
                        + " avgTotalEnergy = " + avgTotalEnergy + " allMetabolism = " + allMetabolism
                        + " allTotalEnergy = " + allTotalEnergy + " yearWeek = " + yearMonth);
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的总能量消耗查看每年，某个帐户的年信息
     */
    private static final String[] PROJECTION_ENERGY_YEARLY_COST = new String[] {
        EnergyCost.ACCOUNT_ID,          // 0
        EnergyCost.AVG_METABOLISM,      // 1
        EnergyCost.AVG_TOTAL_ENERGY,    // 2
        EnergyCost.ALL_METABOLISM ,     //3
        EnergyCost.ALL_TOTAL_ENERGY ,   //4
        EnergyCost.YEAR           //5
    };

    private static final int ACCOUNT_ID_COLUMN_YEARLY_INDEX = 0;
    private static final int AVG_METABOLISM_COLUMN_YEARLY_INDEX = 1;
    private static final int AVG_TOTAL_ENERGY_COLUMN_YEARLY_INDEX = 2;
    private static final int ALL_METABOLISM_COLUMN_YEARLY_INDEX = 3;
    private static final int ALL_TOTAL_ENERGY_COLUMN_YEARLY_INDEX = 4;
    private static final int YEAR_COLUMN_YEARLY_INDEX = 5;
    
    /**
     * 根据某一个帐户id查询我的总能量消耗年信息
     */
    public void queryCostlyYearlyAndAccount(int account) {
        Log.d(TAG, "testCostlyMonthlyAndAccount");
        String accountClause = EnergyCost.ACCOUNT_ID + " = ?";
        String sortOrder = EnergyCost.YEAR + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(EnergyCost.CONTENT_YEAR_URI,
                PROJECTION_ENERGY_YEARLY_COST, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testCostWeekly cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_COLUMN_YEARLY_INDEX);
                long avgMetabolism = cursor.getLong(AVG_METABOLISM_COLUMN_YEARLY_INDEX);
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_YEARLY_INDEX);
                long allMetabolism = cursor.getLong(ALL_METABOLISM_COLUMN_YEARLY_INDEX);
                long allTotalEnergy = cursor.getLong(ALL_TOTAL_ENERGY_COLUMN_YEARLY_INDEX);
                long year = cursor.getInt(YEAR_COLUMN_YEARLY_INDEX);
                Log.d(TAG, " testCostlyYearlyAndAccount accountId = " + accountId + " avgMetabolism = " + avgMetabolism
                        + " avgTotalEnergy = " + avgTotalEnergy + " allMetabolism = " + allMetabolism
                        + " allTotalEnergy = " + allTotalEnergy + " year = " + year);
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 获取食物种类
     */
    private static final String[] PROJECTION_FOOD_SORT = new String[] {
        HeatIngestion.FOOD_NAME,          // 0
        HeatIngestion.NAME_EXPRESS,      // 1
        HeatIngestion.PRIORITY,    // 2
        HeatIngestion.FOOD_QUANTITY ,     //3
        HeatIngestion.CALORIE   //4       
    };
    
    
    private static final int FOOD_NAME_INGESTION_INDEX = 0;
    private static final int NAME_EXPRESS_INGESTION_INDEX = 1;
    private static final int PRIORITY_INGESTION_INDEX = 2;
    private static final int FOOD_QUANTITY_INGESTION_INDEX = 3;
    private static final int CALORIE_INGESTION_INDEX = 4;
    /**
     * 获得食物种类通过食物的类型
     * 
     * 
     */
    public void getFoodSortFromFoodTypes(String[] foodTypes) {
        
        if (foodTypes.length == 0) {
            return;
        }
        
        StringBuilder foodBuilder = new StringBuilder();
        boolean first = true;
        Cursor cursor = null;
        for (String food: foodTypes) {
            if (first) {
                first = false;
                foodBuilder.append(food);
            } else {
                foodBuilder.append(',').append(food);
            }            
        }
        
     // Check whether there is content URI.
        if (first) return ;
        
        if (foodBuilder.length() > 0 ) {
            final String whereClause = HeatIngestion.FOOD_TYPE + " IN (" + foodBuilder.toString() + ")";
            cursor = mContentResolver.query(
                    HeatIngestion.CONTENT_URI, PROJECTION_FOOD_SORT, whereClause, null, null);
        }
        
        if ( cursor == null ) {
            return;
        }
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                String foodName = cursor.getString(FOOD_NAME_INGESTION_INDEX);
                String nameExpress = cursor.getString(NAME_EXPRESS_INGESTION_INDEX);
                int  priority = cursor.getInt(PRIORITY_INGESTION_INDEX);
                int  foodQuantity = cursor.getInt(FOOD_QUANTITY_INGESTION_INDEX);
                int  calorie = cursor.getInt(CALORIE_INGESTION_INDEX);             
            
             
                Log.d(TAG, " getFoodSortFromFoodTypes foodName = " + foodName + " nameExpress = " 
                   + nameExpress
                   + " priority = " + priority
                   + " foodQuantity = " + foodQuantity 
                   + " calorie = " + calorie);
            }
        } finally {
            cursor.close();
        }
    }
    /**
     * 我的能量摄入查看每一周，某帐户的周信息
     */        
    private static final String[] PROJECTION_INGESTION_WEEKLY = new String[] {
        Ingestion.ACCOUNT_ID,               // 0
        Ingestion.AVG_TOTAL_INGESTION,      // 1
        Ingestion.ALL_TOTAL_INGESTION,      // 2
        Ingestion.YEAR_WEEK                 //3     
    };

    private static final int ACCOUNT_ID_INGESTION_WEEKLY_INDEX = 0;
    private static final int AVG_TOTAL_INGESTION_WEEKLY_INDEX = 1;
    private static final int ALL_TOTAL_INGESTION_WEEKLY_INDEX = 2;
    private static final int YEAR_WEEK_INGESTION_INDEX = 3;
    
    /**
     * 根据某一个帐户id查询我的能量摄入周信息
     */
    public void queryIngestionWeeklyAndAccount(int account) {
        
        Log.d(TAG, "testIngestionWeeklyAndAccount");
        String accountClause = Ingestion.ACCOUNT_ID + " = ?";
        String sortOrder = Ingestion.YEAR_WEEK + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(Ingestion.CONTENT_WEEKLY_URI,
                PROJECTION_INGESTION_WEEKLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testIngestionWeeklyAndAccount cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_INGESTION_WEEKLY_INDEX);
                long avgTotalIngestion = cursor.getLong(AVG_TOTAL_INGESTION_WEEKLY_INDEX);
                long allTotalIngestion = cursor.getLong(ALL_TOTAL_INGESTION_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_INGESTION_INDEX);
             
                Log.d(TAG, " testIngestionWeeklyAndAccount accountId = " + accountId + " avgTotalIngestion = " 
                   + avgTotalIngestion
                   + " allTotalIngestion = " + allTotalIngestion + " yearWeek = " + yearWeek );
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的能量摄入查看每一月，某帐户的月信息
     */       
    private static final String[] PROJECTION_INGESTION_MONTHLY = new String[] {
        Ingestion.ACCOUNT_ID,            // 0
        Ingestion.AVG_TOTAL_INGESTION,      // 1
        Ingestion.ALL_TOTAL_INGESTION,      // 2
        Ingestion.YEAR_MONTH               //3     
    };

    private static final int ACCOUNT_ID_INGESTION_MONTHLY_INDEX = 0;
    private static final int AVG_TOTAL_INGESTION_MONTHLY_INDEX = 1;
    private static final int ALL_TOTAL_INGESTION_MONTHLY_INDEX = 2;
    private static final int YEAR_MONTH_INGESTION_INDEX = 3;
    
    /**
     * 根据某一个帐户id查询我的能量摄入月信息
     */
    public void queryIngestionMonthlyAndAccount(int account) {
        
        Log.d(TAG, "testIngestionMonthlyAndAccount");
        String accountClause = Ingestion.ACCOUNT_ID + " = ?";
        String sortOrder = Ingestion.YEAR_MONTH + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(Ingestion.CONTENT_MONTHLY_URI,
                PROJECTION_INGESTION_MONTHLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testIngestionMonthlyAndAccount cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_INGESTION_MONTHLY_INDEX);
                long avgTotalIngestion = cursor.getLong(AVG_TOTAL_INGESTION_MONTHLY_INDEX);
                long allTotalIngestion = cursor.getLong(ALL_TOTAL_INGESTION_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_INGESTION_INDEX);
             
                Log.d(TAG, " testIngestionMonthlyAndAccount accountId = " + accountId + " avgTotalIngestion = " 
                   + avgTotalIngestion
                   + " allTotalIngestion = " + allTotalIngestion + " yearMonth = " + yearMonth );
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的能量摄入查看每一年，某帐户的月信息
     */   
    private static final String[] PROJECTION_INGESTION_YEARLY = new String[] {
        Ingestion.ACCOUNT_ID,          // 0
        Ingestion.AVG_TOTAL_INGESTION,      // 1
        Ingestion.ALL_TOTAL_INGESTION,    // 2
        Ingestion.YEAR         //3     
    };

    private static final int ACCOUNT_ID_INGESTION_YEARLY_INDEX = 0;
    private static final int AVG_TOTAL_INGESTION_YEARLY_INDEX = 1;
    private static final int ALL_TOTAL_INGESTION_YEARLY_INDEX = 2;
    private static final int YEAR_INGESTION_INDEX = 3;
    
    /**
     * 根据某一个帐户id查询我的能量摄入年信息
     */
    public void queryIngestionYearlyAndAccount(int account) {
        
        Log.d(TAG, "testIngestionYearlyAndAccount");
        String accountClause = Ingestion.ACCOUNT_ID + " = ?";
        String sortOrder = Ingestion.YEAR + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(Ingestion.CONTENT_YEAR_URI,
                PROJECTION_INGESTION_YEARLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testIngestionYearlyAndAccount cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_INGESTION_YEARLY_INDEX);
                long avgTotalIngestion = cursor.getLong(AVG_TOTAL_INGESTION_YEARLY_INDEX);
                long allTotalIngestion = cursor.getLong(ALL_TOTAL_INGESTION_YEARLY_INDEX);
                int  year = cursor.getInt(YEAR_INGESTION_INDEX);
             
                Log.d(TAG, " testIngestionYearlyAndAccount accountId = " + accountId + " avgTotalIngestion = " 
                   + avgTotalIngestion
                   + " allTotalIngestion = " + allTotalIngestion + " yearMonth = " + year );
            }
        } finally {
            cursor.close();
        }
    }
    
    /**
     * 我的体重变化查看每一周，某一个帐户的周信息
     */
        
    private static final String[] PROJECTION_WEIGHT_WEEKLY = new String[] {
        WeightChange.ACCOUNT_ID,               // 0
        WeightChange.AVG_WEIGHT,      // 1
        WeightChange.AVG_EXPECTED_WEIGHT,      // 2
        WeightChange.YEAR_WEEK                 //3     
    };

    private static final int ACCOUNT_ID_WEIGHT_WEEKLY_INDEX = 0;
    private static final int AVG_WEIGHT_WEEKLY_INDEX = 1;
    private static final int AVG_EXPECTED_WEIGHT_WEEKLY_INDEX = 2;
    private static final int YEAR_WEEK_WEIGHT_INDEX = 3;

    /**
     * 根据某一个帐户id查询我的体重变化周信息
     */
    public void queryWeightWeeklyAndAccount(int account) {
        
        Log.d(TAG, "testWeightWeeklyAndAccount");
        String accountClause = WeightChange.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.YEAR_WEEK + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(WeightChange.CONTENT_WEEKLY_URI,
                PROJECTION_WEIGHT_WEEKLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testWeightWeeklyAndAccount cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_WEIGHT_WEEKLY_INDEX);
                float avgWeight = cursor.getFloat(AVG_WEIGHT_WEEKLY_INDEX);
                float avgExpectedWeight = cursor.getFloat(AVG_EXPECTED_WEIGHT_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_WEIGHT_INDEX);
             
                Log.d(TAG, " testWeightWeeklyAndAccount accountId = " + accountId
                        + " avgWeight = " 
                   + avgWeight
                   + " avgExpectedWeight = " + avgExpectedWeight + " yearWeek = " + yearWeek );
            }
        } finally {
            cursor.close();
        }
    }
     
    /**
     * 我的体重变化查看每一月，某一个帐户的月信息
     */
    private static final String[] PROJECTION_WEIGHT_MONTHLY = new String[] {
        WeightChange.ACCOUNT_ID,               // 0
        WeightChange.AVG_WEIGHT,      // 1
        WeightChange.AVG_EXPECTED_WEIGHT,      // 2
        WeightChange.YEAR_MONTH                 //3     
    };

    private static final int ACCOUNT_ID_WEIGHT_MONTHLY_INDEX = 0;
    private static final int AVG_WEIGHT_MONTHLY_INDEX = 1;
    private static final int AVG_EXPECTED_WEIGHT_MONTHLY_INDEX = 2;
    private static final int YEAR_MONTH_WEIGHT_INDEX = 3;
    
    /**
     * 根据某一个帐户id查询我的体重变化月信息
     */
    public void queryWeightMonthlyAndAccount(int account) {
        
        Log.d(TAG, "testWeightMonthlyAndAccount");
        String accountClause = WeightChange.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.YEAR_MONTH + " DESC";
        String[] args = new String[] {String.valueOf(account)};
        Cursor cursor  = mContentResolver.query(WeightChange.CONTENT_MONTHLY_URI,
                PROJECTION_WEIGHT_MONTHLY, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testWeightWeeklyAndAccount cursor = " + cursor);
        }

  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(ACCOUNT_ID_WEIGHT_MONTHLY_INDEX);
                float avgWeight = cursor.getFloat(AVG_WEIGHT_MONTHLY_INDEX);
                float avgExpectedWeight = cursor.getFloat(AVG_EXPECTED_WEIGHT_MONTHLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_MONTH_WEIGHT_INDEX);
             
                Log.d(TAG, " testWeightMonthlyAndAccount accountId = " + accountId
                        + " avgWeight = " 
                   + avgWeight
                   + " avgExpectedWeight = " + avgExpectedWeight + " yearWeek = " + yearWeek );
            }
        } finally {
            cursor.close();
        }
    }
     
     /**
      * 我的体重变化查看每年，某一个帐户的年信息
      */
     private static final String[] PROJECTION_WEIGHT_YEARLY = new String[] {
         WeightChange.ACCOUNT_ID,               // 0
         WeightChange.AVG_WEIGHT,      // 1
         WeightChange.AVG_EXPECTED_WEIGHT,      // 2
         WeightChange.YEAR                 //3     
     };

     private static final int ACCOUNT_ID_WEIGHT_YEARLY_INDEX = 0;
     private static final int AVG_WEIGHT_YEARLY_INDEX = 1;
     private static final int AVG_EXPECTED_WEIGHT_YEARLY_INDEX = 2;
     private static final int YEAR_WEIGHT_INDEX = 3;
     
     /**
      * 我的体重变化查看每一月，某一个帐户的年信息
      */
     public void queryWeightYearlyAndAccount(int account) {
         
         Log.d(TAG, "testWeightYearlyAndAccount");
         String accountClause = WeightChange.ACCOUNT_ID + " = ?";
         String sortOrder = WeightChange.YEAR + " DESC";
         String[] args = new String[] {String.valueOf(account)};
         Cursor cursor  = mContentResolver.query(WeightChange.CONTENT_YEAR_URI,
                 PROJECTION_WEIGHT_YEARLY, accountClause, args, sortOrder);
         if (cursor == null) {
             Log.d(TAG, " testWeightYearlyAndAccount cursor = " + cursor);
         }

   
         try {
             cursor.moveToPosition(-1);
             while (cursor.moveToNext() ) {
                 int  accountId = cursor.getInt(ACCOUNT_ID_WEIGHT_YEARLY_INDEX);
                 float avgWeight = cursor.getFloat(AVG_WEIGHT_YEARLY_INDEX);
                 float avgExpectedWeight = cursor.getFloat(AVG_EXPECTED_WEIGHT_YEARLY_INDEX);
                 int  yearWeek = cursor.getInt(YEAR_WEIGHT_INDEX);
              
                 Log.d(TAG, " testWeightYearlyAndAccount accountId = " + accountId
                         + " avgWeight = " 
                    + avgWeight
                    + " avgExpectedWeight = " + avgExpectedWeight + " yearWeek = " + yearWeek );
             }
         } finally {
             cursor.close();
         }
     }
}
