package com.breezing.health.providers;

import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.FoodClassify;
import com.breezing.health.providers.Breezing.HeatConsumption;
import com.breezing.health.providers.Breezing.HeatConsumptionRecord;
import com.breezing.health.providers.Breezing.HeatIngestion;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.providers.Breezing.IngestiveRecord;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BreezingDatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "BreezingDatabaseHelper";
    private static BreezingDatabaseHelper sInstance = null;
    static final String DATABASE_NAME = "breezing.db";
    static final int DATABASE_VERSION = 7;
    private final Context mContext;

    public interface Views {
        public static final String COST_WEEKLY = "view_cost_weekly";
        public static final String COST_MONTHLY = "view_cost_monthly";
        public static final String COST_YEARLY = "view_cost_yearly";
        public static final String INGESTION_WEEKLY = "view_ingestion_weekly";
        public static final String INGESTION_MONTHLY = "view_ingestion_monthly";
        public static final String INGESTION_YEARLY = "view_ingestion_yearly";
        public static final String WEIGHT_WEEKLY = "view_weight_weekly";
        public static final String WEIGHT_MONTHLY = "view_weight_monthly";
        public static final String WEIGHT_YEARLY = "view_weight_yearly";
        public static final String BASE_INFO = "view_base_info";
        public static final String SPORT_TYPE = "view_sport_type";
        public static final String FOOD_TYPE = "view_food_type";
        public static final String FOOD_INGESTION = "view_food_ingestion";
    }

    private BreezingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * Return a singleton helper for the combined Breezing health
     * database.
     */
    /* package */
    static synchronized BreezingDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BreezingDatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAccountTables(db);
        createInformationTables(db);
        createEnergyCostTables(db);
        createIngestionTables(db);
        createWeightChangeTables(db);
        createHeatConsumptionTables(db);       
        createConsumptionRecordTables(db);
        createHeatIngestionTables(db);   
        createFoodClassifyTables(db);
        createIngestiveRecordTables(db);
        createEnergyCostViews(db);
        createIngestionViews(db);
        createWeightChangeViews(db);
        createUnitSettings(db);
        createBaseInfomationViews(db);
        createSportTypeViews(db);       
        createFoodIngestionViews(db);
    }


    /***
     * 产生帐户表
     * @param db
     */
    private void createAccountTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_ACCOUNT  + " ("
              +  Account._ID +  " INTEGER PRIMARY KEY, "
              +  Account.ACCOUNT_NAME + " TEXT NOT NULL , "
              +  Account.ACCOUNT_ID +  " INTEGER NOT NULL DEFAULT 0 , "
              +  Account.ACCOUNT_PASSWORD +  " TEXT NOT NULL , " 
              +  Account.ACCOUNT_DELETED +  " INTEGER NOT NULL DEFAULT 0 " +
                   ");");
    }

    /***
     * 产生基本信息表
     * @param db
     */
    private void createInformationTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_INFORMATION  + " ("
              +   Information._ID + " INTEGER PRIMARY KEY, "
              +   Information.ACCOUNT_ID + " INTEGER NOT NULL , "
              +   Information.GENDER +  " INTEGER NOT NULL , "
              +   Information.HEIGHT +  " FLOAT NOT NULL , "
              +   Information.BIRTHDAY + " INTEGER NOT NULL , "
              +   Information.CUSTOM + " INTEGER NOT NULL , "
              +   Information.HEIGHT_UNIT + " TEXT NOT NULL , "
              +   Information.WEIGHT_UNIT + " TEXT NOT NULL , "
              +   Information.DISTANCE_UNIT + " TEXT NOT NULL , "
              +   Information.ACCOUNT_PICTURE + " TEXT  " +
                   ");");
    }

    /***
     * 产生我的总能量消耗表
     * @param db
     */
    private void createEnergyCostTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_COST  + " ("
                +  EnergyCost._ID + " INTEGER PRIMARY KEY, "
                +  EnergyCost.ACCOUNT_ID + " INTEGER NOT NULL , "
                +  EnergyCost.METABOLISM + " INTEGER NOT NULL , "
                +  EnergyCost.SPORT + " INTEGER NOT NULL , "
                +  EnergyCost.DIGEST + " INTEGER NOT NULL , "
                +  EnergyCost.TRAIN + " INTEGER NOT NULL , "
                +  EnergyCost.TOTAL_ENERGY + " INTEGER NOT NULL , "
                +  EnergyCost.ENERGY_COST_DATE + " INTEGER NOT NULL , "
                +  EnergyCost.DATE + " INTEGER NOT NULL , "
                +  EnergyCost.YEAR + " INTEGER NOT NULL , "
                +  EnergyCost.YEAR_MONTH + " INTEGER NOT NULL , "
                +  EnergyCost.YEAR_WEEK + " INTEGER NOT NULL " +
                   ");");
    }
    /**
     * 产生总能量消耗视图，显示周，月，年
     * @param db
     */
    private void createEnergyCostViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.COST_WEEKLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.COST_MONTHLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.COST_YEARLY + ";");

        String weeklyCostSelect =  "SELECT "
                + EnergyCost.ACCOUNT_ID + ","
                + " round( avg( " + EnergyCost.METABOLISM + ") ) AS " + EnergyCost.AVG_METABOLISM + " , "
                + " round( avg( " + EnergyCost.TOTAL_ENERGY + " ) )  AS " + EnergyCost.AVG_TOTAL_ENERGY + " , "
                + " total( " + EnergyCost.METABOLISM + " ) AS " +  EnergyCost.ALL_METABOLISM + " , "
                + " total( " + EnergyCost.TOTAL_ENERGY + " )  AS " + EnergyCost.ALL_TOTAL_ENERGY + " , "
                + EnergyCost.YEAR_MONTH + " , "
                + EnergyCost.YEAR_WEEK
                + " FROM " + BreezingProvider.TABLE_COST
                + " GROUP BY " +  EnergyCost.ACCOUNT_ID + " , " + EnergyCost.YEAR_WEEK;

        db.execSQL("CREATE VIEW " + Views.COST_WEEKLY + " AS " + weeklyCostSelect);

        String monthlyCostSelect =  "SELECT "
                + EnergyCost.ACCOUNT_ID + ","
                + " round( avg( " + EnergyCost.METABOLISM + ") ) AS " + EnergyCost.AVG_METABOLISM + " , "
                + " round( avg( " + EnergyCost.TOTAL_ENERGY + " ) )  AS " + EnergyCost.AVG_TOTAL_ENERGY + " ,"
                + " total( " + EnergyCost.METABOLISM + " ) AS " + EnergyCost.ALL_METABOLISM + " , "
                + " total( " + EnergyCost.TOTAL_ENERGY + " )  AS " + EnergyCost.ALL_TOTAL_ENERGY + " , "
                + EnergyCost.YEAR + " , "
                + EnergyCost.YEAR_MONTH
                + " FROM " + BreezingProvider.TABLE_COST
                + " GROUP BY " +  EnergyCost.ACCOUNT_ID + "  , " + EnergyCost.YEAR_MONTH;

        db.execSQL("CREATE VIEW " + Views.COST_MONTHLY + " AS " + monthlyCostSelect);

        String yearlyCostSelect =  "SELECT "
                + EnergyCost.ACCOUNT_ID + ","
                + " round( avg( " + EnergyCost.METABOLISM + ") ) AS " + EnergyCost.AVG_METABOLISM + "   , "
                + " round( avg( " + EnergyCost.TOTAL_ENERGY + " ) )  AS " + EnergyCost.AVG_TOTAL_ENERGY + " , "
                + " total( " + EnergyCost.METABOLISM + " ) AS " + EnergyCost.ALL_METABOLISM + " , "
                + " total( " + EnergyCost.TOTAL_ENERGY + " )  AS " + EnergyCost.ALL_TOTAL_ENERGY + " , "
                + EnergyCost.YEAR
                + " FROM " + BreezingProvider.TABLE_COST
                + " GROUP BY " +  EnergyCost.ACCOUNT_ID + " , " + EnergyCost.YEAR;

        db.execSQL("CREATE VIEW " + Views.COST_YEARLY + " AS " + yearlyCostSelect);
    }



    /***
     * 产生我的摄入表
     * @param db
     */
    private void createIngestionTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_INGESTION  + " ("
                  + Ingestion._ID + " INTEGER PRIMARY KEY, "
                  + Ingestion.ACCOUNT_ID + " INTEGER NOT NULL , "
                  + Ingestion.BREAKFAST + " INTEGER NOT NULL , "
                  + Ingestion.LUNCH + " INTEGER NOT NULL , "
                  + Ingestion.DINNER + " INTEGER NOT NULL , "
                  + Ingestion.ETC + " INTEGER NOT NULL , "
                  + Ingestion.TOTAL_INGESTION + " INTEGER NOT NULL , "
                  + Ingestion.DATE + " INTEGER NOT NULL , "
                  + Ingestion.YEAR + " INTEGER NOT NULL , "
                  + Ingestion.YEAR_MONTH + " INTEGER NOT NULL , "
                  + Ingestion.YEAR_WEEK + " INTEGER NOT NULL " +
                   ");");
    }

    /***
     * 产生我的摄入视图，周，月，年
     *
     */
    private void createIngestionViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.INGESTION_WEEKLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.INGESTION_MONTHLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.INGESTION_YEARLY + ";");

        String weeklyIngestionSelect =  "SELECT "
                + Ingestion.ACCOUNT_ID + " , "
                + " round ( avg( " +  Ingestion.TOTAL_INGESTION + " ) ) AS " + Ingestion.AVG_TOTAL_INGESTION + " , "
                + " total( " + Ingestion.TOTAL_INGESTION + " )  AS "+ Ingestion.ALL_TOTAL_INGESTION + " , "
                +  Ingestion.YEAR_MONTH + " , "
                +  Ingestion.YEAR_WEEK
                + " FROM " + BreezingProvider.TABLE_INGESTION
                + " GROUP BY " + Ingestion.ACCOUNT_ID + " , " + Ingestion.YEAR_WEEK;

        db.execSQL("CREATE VIEW " + Views.INGESTION_WEEKLY + " AS " + weeklyIngestionSelect);

        String monthlyIngestionSelect =  "SELECT "
                + Ingestion.ACCOUNT_ID + " , "
                + " round ( avg( " +  Ingestion.TOTAL_INGESTION + " ) ) AS  " + Ingestion.AVG_TOTAL_INGESTION + " , "
                + " total( " + Ingestion.TOTAL_INGESTION + " )  AS "+ Ingestion.ALL_TOTAL_INGESTION + " , "
                +  Ingestion.YEAR + " , "
                +  Ingestion.YEAR_MONTH
                + " FROM " + BreezingProvider.TABLE_INGESTION
                + " GROUP BY " + Ingestion.ACCOUNT_ID + " , " + Ingestion.YEAR_MONTH;

        db.execSQL("CREATE VIEW " + Views.INGESTION_MONTHLY + " AS " + monthlyIngestionSelect);

        String yearlyIngestionSelect =  "SELECT "
                + Ingestion.ACCOUNT_ID + " , "
                + " round ( avg( " +  Ingestion.TOTAL_INGESTION + " ) ) AS   " + Ingestion.AVG_TOTAL_INGESTION + " , "
                + " total( " + Ingestion.TOTAL_INGESTION + " )  AS "+ Ingestion.ALL_TOTAL_INGESTION + " , "
                +  Ingestion.YEAR
                + " FROM " + BreezingProvider.TABLE_INGESTION
                + " GROUP BY " + Ingestion.ACCOUNT_ID + " , " +  Ingestion.YEAR;

        db.execSQL("CREATE VIEW " + Views.INGESTION_YEARLY + " AS " + yearlyIngestionSelect);
    }

    /***
     * 产生体重变化表
     * @param db
     */
    private void createWeightChangeTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_WEIGHT  + " ("
                 + WeightChange._ID + " INTEGER PRIMARY KEY, "
                 + WeightChange.ACCOUNT_ID + " INTEGER NOT NULL , "
                 + WeightChange.WEIGHT + " FLOAT NOT NULL , "
                 + WeightChange.EVERY_WEIGHT + " FLOAT , "
                 + WeightChange.EXPECTED_WEIGHT + " FLOAT NOT NULL , "
                 + WeightChange.DATE + " INTEGER NOT NULL , "
                 + WeightChange.YEAR + " INTEGER NOT NULL , "
                 + WeightChange.YEAR_MONTH + " INTEGER NOT NULL , "
                 + WeightChange.YEAR_WEEK + " INTEGER NOT NULL " +
                   ");");
    }
    
    /***
     * 产生体重变化视图，周，月，年
     * @param db
     */
    private void createWeightChangeViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.WEIGHT_WEEKLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.WEIGHT_MONTHLY + ";");
        db.execSQL("DROP VIEW IF EXISTS " + Views.WEIGHT_YEARLY + ";");

        String weeklyWeightSelect =  " SELECT "
                + WeightChange.ACCOUNT_ID + " , "
                + " round( avg( " + WeightChange.WEIGHT + " ), 2 ) AS " + WeightChange.AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EVERY_WEIGHT + " ), 2 ) AS " + WeightChange.EVERY_AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EXPECTED_WEIGHT + " ) ,2)   AS "
                + WeightChange.AVG_EXPECTED_WEIGHT + " , "
                + WeightChange.YEAR_MONTH + " , "
                + WeightChange.YEAR_WEEK
                + " FROM " + BreezingProvider.TABLE_WEIGHT
                + " GROUP BY " +  WeightChange.ACCOUNT_ID  + " ," + WeightChange.YEAR_WEEK;

        db.execSQL("CREATE VIEW " + Views.WEIGHT_WEEKLY + " AS " + weeklyWeightSelect);

        String monthlyWeightSelect =  " SELECT "
                + WeightChange.ACCOUNT_ID + " , "
                + " round( avg( " + WeightChange.WEIGHT + " ), 2 ) AS " + WeightChange.AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EVERY_WEIGHT + " ), 2 ) AS " + WeightChange.EVERY_AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EXPECTED_WEIGHT + " ) ,2)   AS "
                + WeightChange.AVG_EXPECTED_WEIGHT + " , "
                + WeightChange.YEAR + " , "
                + WeightChange.YEAR_MONTH
                + " FROM " + BreezingProvider.TABLE_WEIGHT
                + " GROUP BY " +  WeightChange.ACCOUNT_ID  + " ," + WeightChange.YEAR_MONTH;

        db.execSQL("CREATE VIEW " + Views.WEIGHT_MONTHLY + " AS " + monthlyWeightSelect);

        String yearlyWeightSelect =  " SELECT "
                + WeightChange.ACCOUNT_ID + " , "
                + " round( avg( " + WeightChange.WEIGHT + " ), 2 ) AS  " +  WeightChange.AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EVERY_WEIGHT + " ), 2 ) AS " + WeightChange.EVERY_AVG_WEIGHT + " , "
                + " round( avg( " + WeightChange.EXPECTED_WEIGHT + " ) ,2) AS "
                + WeightChange.AVG_EXPECTED_WEIGHT + " , "
                + WeightChange.YEAR
                + " FROM " + BreezingProvider.TABLE_WEIGHT
                + " GROUP BY " +  WeightChange.ACCOUNT_ID  + " ," + WeightChange.YEAR;

        db.execSQL("CREATE VIEW " + Views.WEIGHT_YEARLY + " AS " + yearlyWeightSelect);
    }    
   

    /***
     * 产生热量消耗参考表
     * @param db
     */
    private void createHeatConsumptionTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_HEAT_CONSUMPTION  + " ("
                 +  HeatConsumption._ID + " INTEGER PRIMARY KEY , "
                 +  HeatConsumption.SPORT_TYPE + " TEXT NOT NULL , "
                 +  HeatConsumption.SPORT_INTENSITY + " TEXT NOT NULL , "
                 +  HeatConsumption.SPORT_QUANTITY + " INTEGER NOT NULL, "
                 +  HeatConsumption.SPORT_UNIT + " TEXT NOT NULL , "               
                 +  HeatConsumption.CALORIE + " FLOAT NOT NULL , "
                 +  HeatConsumption.EQUIPMENT_NAME + " TEXT " +
                   ");");
    }
    
    /***
     * 产生所有运动类型列表
     * @param db
     */
    private void createSportTypeViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.SPORT_TYPE + ";");
        
        String sportTypeSelect =  " SELECT "
                + HeatConsumption.SPORT_TYPE          
                + " FROM " + BreezingProvider.TABLE_HEAT_CONSUMPTION
                + " GROUP BY " +  HeatConsumption.SPORT_TYPE;

        db.execSQL("CREATE VIEW " + Views.SPORT_TYPE + " AS " + sportTypeSelect);
        
    }

    
    /***
     * 产生热量消耗参考表
     * @param db
     */
    private void createConsumptionRecordTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_HEAT_CONSUMPTION_RECORD  + " ("
                 +  HeatConsumptionRecord._ID + " INTEGER PRIMARY KEY , "
                 +  HeatConsumptionRecord.ACCOUNT_ID + " INTEGER NOT NULL , "
                 +  HeatConsumptionRecord.SPORT_TYPE + " TEXT NOT NULL , "
                 +  HeatConsumptionRecord.SPORT_INTENSITY + " TEXT NOT NULL , "
                 +  HeatConsumptionRecord.SPORT_QUANTITY + " INTEGER NOT NULL, "
                 +  HeatConsumptionRecord.SPORT_UNIT + " TEXT NOT NULL , "               
                 +  HeatConsumptionRecord.CALORIE + " FLOAT NOT NULL , "
                 +  HeatConsumptionRecord.DATE + " INTEGER NOT NULL " +
                   ");");
    }
    
    /***
     * 产生热量摄入参考表
     * @param db
     */
    private void createHeatIngestionTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_HEAT_INGESTION  + " ("
                   +  HeatIngestion._ID + " INTEGER PRIMARY KEY , "
                   +  HeatIngestion.FOOD_CLASSIFY_ID + " INTEGER NOT NULL , "
                   +  HeatIngestion.FOOD_ID + " INTEGER NOT NULL , "
                   +  HeatIngestion.FOOD_NAME + " TEXT NOT NULL , "
                   +  HeatIngestion.NAME_EXPRESS + " TEXT NOT NULL , "
                   +  HeatIngestion.PRIORITY + " INTEGER , "
                   +  HeatIngestion.FOOD_QUANTITY + " INTEGER NOT NULL , "
                   +  HeatIngestion.CALORIE + " INTEGER NOT NULL , " 
                   +  HeatIngestion.FOOD_PICTURE + " TEXT " +
                   ");");
    }
    
    /***
     * 产生热量摄入参考表
     * @param db
     */
    private void createFoodClassifyTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_FOOD_CLASSIFY  + " ("
                   +  FoodClassify._ID + " INTEGER PRIMARY KEY , "
                   +  FoodClassify.FOOD_CLASSIFY_ID + " INTEGER NOT NULL , "
                   +  FoodClassify.FOOD_TYPE + " TEXT NOT NULL , "
                   +  FoodClassify.UNSELECT_PICTURE + " TEXT , "  
                   +  FoodClassify.SELECT_PICTURE + " TEXT  " +                   
                   ");");
    }
    
    /***
     * 产生基本信息 view
     * @param db
     */
    private void createFoodIngestionViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.FOOD_INGESTION + ";");
        
        String foodIngestionSelect =  " SELECT "
                + FoodClassify.FOOD_TYPE + " , "
                + FoodClassify.UNSELECT_PICTURE + " , "
                + HeatIngestion.FOOD_NAME + " , "
                + HeatIngestion.NAME_EXPRESS + " , "
                + HeatIngestion.PRIORITY + " , "
                + HeatIngestion.FOOD_QUANTITY + " , "
                + HeatIngestion.CALORIE + " , "
                + HeatIngestion.FOOD_PICTURE 
                + " FROM " + BreezingProvider.TABLE_HEAT_INGESTION
                + " LEFT OUTER JOIN " + BreezingProvider.TABLE_FOOD_CLASSIFY + " ON "
                + BreezingProvider.TABLE_HEAT_INGESTION + "." 
                + HeatIngestion.FOOD_CLASSIFY_ID + " = "
                + BreezingProvider.TABLE_FOOD_CLASSIFY + "."
                + FoodClassify.FOOD_CLASSIFY_ID;               

        db.execSQL("CREATE VIEW " + Views.FOOD_INGESTION + " AS " + foodIngestionSelect);

    }
    
    /***
     * 产生所有食物类型列表
     * @param db
     */
    private void createFoodTypeViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.FOOD_TYPE + ";");
        
        String foodTypeSelect =  " SELECT "
                + HeatIngestion.FOOD_TYPE          
                + " FROM " + BreezingProvider.TABLE_HEAT_INGESTION
                + " GROUP BY " +  HeatIngestion.FOOD_TYPE ;

        db.execSQL("CREATE VIEW " + Views.FOOD_TYPE + " AS " + foodTypeSelect);
        
    }
    
    /***
     * 我的每天，每餐的摄入详情
     * @param db
     */
    private void createIngestiveRecordTables(SQLiteDatabase db) {
        
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_INGESTIVE_RECORD  + " ("
                   +  IngestiveRecord._ID + " INTEGER PRIMARY KEY , "
                   +  IngestiveRecord.ACCOUNT_ID + " INTEGER NOT NULL , "
                   +  IngestiveRecord.FOOD_ID + " INTEGER NOT NULL , "
                   +  IngestiveRecord.FOOD_QUANTITY + " INTEGER NOT NULL , "               
                   +  IngestiveRecord.DINING + " TEXT NOT NULL , "
                   +  IngestiveRecord.DATE + " INTEGER NOT NULL  " +
                   ");");
    }

    /***
     * 产生单位设置表
     * @param db
     */
    private void createUnitSettings(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BreezingProvider.TABLE_UNIT_SETTINGS  + " ("
                +  UnitSettings._ID + " INTEGER PRIMARY KEY , "
                +  UnitSettings.UNIT_TYPE + " TEXT NOT NULL , "
                +  UnitSettings.UNIT_NAME + " TEXT NOT NULL , "
                +  UnitSettings.UNIT_UNIFY_DATA + " FLOAT NOT NULL , "
                +  UnitSettings.UNIT_OBTAIN_DATA + " FLOAT NOT NULL  " +
                ");");

    }

    /***
     * 产生基本信息 view
     * @param db
     */
    private void createBaseInfomationViews(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + Views.BASE_INFO + ";");
        
        String baseInfoSelect =  " SELECT "
                + Account.INFO_ACCOUNT_ID + " , "
                + Account.INFO_ACCOUNT_NAME + " , "
                + Account.INFO_ACCOUNT_PASSWORD + " , "
                + Information.INFO_GENDER + " , "
                + Information.INFO_HEIGHT + " , "
                + Information.INFO_BIRTHDAY + " , "
                + Information.INFO_CUSTOM + " , "
                + Information.INFO_HEIGHT_UNIT + " , "
                + Information.INFO_WEIGHT_UNIT + " , "
                + Information.INFO_DISTANCE_UNIT + " , "
                + Information.INFO_ACCOUNT_PICTURE + " , "
                + WeightChange.INFO_WEIGHT + " , "
                + WeightChange.INFO_EVERY_WEIGHT + " , "
                + WeightChange.INFO_EXPECTED_WEIGHT + " , "
                + WeightChange.INFO_DATE
                + " FROM " + BreezingProvider.TABLE_ACCOUNT
                + " LEFT OUTER JOIN " + BreezingProvider.TABLE_INFORMATION + " ON "
                + BreezingProvider.TABLE_INFORMATION + "." + Information.ACCOUNT_ID + " = "
                + BreezingProvider.TABLE_ACCOUNT + "." + Account.ACCOUNT_ID
                +  " LEFT OUTER JOIN " + BreezingProvider.TABLE_WEIGHT + " ON "
                + BreezingProvider.TABLE_WEIGHT + "." +  WeightChange.ACCOUNT_ID + " = "
                + BreezingProvider.TABLE_INFORMATION + "."
                + Information.ACCOUNT_ID;

        db.execSQL("CREATE VIEW " + Views.BASE_INFO + " AS " + baseInfoSelect);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, " onUpgrade oldVersion = " + oldVersion + " newVersion = " + newVersion);

        if (oldVersion == 1) {
            upgradeToVersion202(db);
            upgradeToVersion3(db);
            upgradeToVersion4(db);
            upgradeToVersion5(db);
            upgradeToVersion6(db);
            upgradeToVersion7(db);
        } else if (oldVersion == 2) {
            upgradeToVersion3(db);
            upgradeToVersion4(db);
            upgradeToVersion5(db);
            upgradeToVersion6(db);
            upgradeToVersion7(db);
        } else if (oldVersion ==3 ){
            upgradeToVersion4(db);
            upgradeToVersion5(db);
            upgradeToVersion6(db);
            upgradeToVersion7(db);
        } else if (oldVersion == 4 ) {
            upgradeToVersion5(db);
            upgradeToVersion6(db);
            upgradeToVersion7(db);
        } else if (oldVersion == 5) {
            upgradeToVersion6(db);
            upgradeToVersion7(db);
        } else  {
            upgradeToVersion7(db);
        } 
    }

    private void upgradeToVersion202(SQLiteDatabase db) {
        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_COST +
                " ADD " + EnergyCost.ENERGY_COST_DATE + " INTEGER;");
    }
    
    private void upgradeToVersion3(SQLiteDatabase db) {
        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_FOOD_CLASSIFY +
                " ADD " + FoodClassify.SELECT_PICTURE + " TEXT ");
    }
    
    private void upgradeToVersion4(SQLiteDatabase db) {
        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_ACCOUNT  +
                " ADD " +  Account.ACCOUNT_DELETED +  " INTEGER NOT NULL DEFAULT 0 " );
    }
    
    private void upgradeToVersion5(SQLiteDatabase db) {
        
        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_INFORMATION  +
                " ADD " +  Information.ACCOUNT_PICTURE +  " TEXT " );
        
        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_WEIGHT  +
                " ADD " +  WeightChange.EVERY_WEIGHT +  " FLOAT  " );
        createBaseInfomationViews(db);
        createWeightChangeViews(db);
    }

    private void upgradeToVersion6(SQLiteDatabase db) {

        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_HEAT_INGESTION  +
                        " ADD " +  HeatIngestion.FOOD_ID +  " INTEGER " );

        db.execSQL(
                "ALTER TABLE " + BreezingProvider.TABLE_INGESTIVE_RECORD  +
                        " ADD " +  IngestiveRecord.FOOD_ID +  " INTEGER  " );
        createBaseInfomationViews(db);
        createWeightChangeViews(db);
    }
    
    private void upgradeToVersion7(SQLiteDatabase db) {
        
        db.execSQL("DROP TABLE " + BreezingProvider.TABLE_INGESTIVE_RECORD); 
        createIngestiveRecordTables(db);
    }

}
