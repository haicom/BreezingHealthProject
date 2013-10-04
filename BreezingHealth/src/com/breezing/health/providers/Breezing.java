package com.breezing.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class Breezing {
    private final static String TAG = "Breezing";
    /** The authority for the contacts provider */
    public static final String AUTHORITY = "breezing";
    /** A content:// style uri to the authority for the contacts provider */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    // Constructor
    public Breezing() {

    }

    public static final class Account implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "account");
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_ID   = "account_id";
        public static final String ACCOUNT_PASSWORD = "account_password";

        public static final String INFO_ACCOUNT_ID = BreezingProvider.TABLE_ACCOUNT + "." + ACCOUNT_ID;
        public static final String INFO_ACCOUNT_NAME = BreezingProvider.TABLE_ACCOUNT + "." + ACCOUNT_NAME;
        public static final String INFO_ACCOUNT_PASSWORD = BreezingProvider.TABLE_ACCOUNT + "." + ACCOUNT_PASSWORD;
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/account";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/account";


    }

    public static final class Information implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "information");

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_BASE_INFO_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "base_info");

        public static final String ACCOUNT_ID   = "account_id";
        public static final String GENDER   = "gender";
        public static final String HEIGHT   = "height";
        public static final String WEIGHT   = "weight";
        public static final String HOPE_WEIGHT = "hope_weight";
        public static final String BIRTHDAY   = "birthday";
        public static final String CUSTOM   = "custom";
        public static final String EXPECTED_WEIGHT   = "expected_weight";
        public static final String HEIGHT_UNIT = "height_unit";
        public static final String WEIGHT_UNIT = "weight_unit";
        public static final String DISTANCE_UNIT = "distance_unit";

        public static final String INFO_GENDER = BreezingProvider.TABLE_INFORMATION + "." + GENDER;
        public static final String INFO_HEIGHT = BreezingProvider.TABLE_INFORMATION + "." + HEIGHT;
        public static final String INFO_BIRTHDAY = BreezingProvider.TABLE_INFORMATION + "." + BIRTHDAY;
        public static final String INFO_CUSTOM = BreezingProvider.TABLE_INFORMATION + "." + CUSTOM;
        public static final String INFO_HEIGHT_UNIT = BreezingProvider.TABLE_INFORMATION + "." + HEIGHT_UNIT;
        public static final String INFO_WEIGHT_UNIT = BreezingProvider.TABLE_INFORMATION + "." + WEIGHT_UNIT;
        public static final String INFO_DISTANCE_UNIT = BreezingProvider.TABLE_INFORMATION + "." + DISTANCE_UNIT;
       
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/information";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/information";
    }

    /**
     * Base columns for tables that contain EnergyCost Ingestion WeightChange.
     */
    public interface BaseDateColumns extends BaseColumns {
        public static final String DATE   = "date";
        public static final String YEAR   = "year";
        public static final String YEAR_MONTH   = "year_month";
        public static final String YEAR_WEEK   = "year_week";
    }

    public static final class EnergyCost implements BaseDateColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "cost");

        public static final Uri CONTENT_WEEKLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "cost_weekly");

        public static final Uri CONTENT_MONTHLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "cost_monthly");

        public static final Uri CONTENT_YEAR_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "cost_yearly");


        public static final String ACCOUNT_ID   = "account_id";
        public static final String METABOLISM   = "metabolism";
        public static final String SPORT   = "sport";
        public static final String DIGEST   = "digest";
        public static final String TRAIN   = "train";
        public static final String TOTAL_ENERGY   = "total_energy";
        public static final String ENERGY_COST_DATE = "energy_cost_date";
        public static final String AVG_METABOLISM = "avg_metabolism";
        public static final String AVG_TOTAL_ENERGY = "avg_total_energy";
        public static final String ALL_METABOLISM = "all_metabolism";
        public static final String ALL_TOTAL_ENERGY = "all_total_energy";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/cost";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/cost";
    }

    public static final class Ingestion implements BaseDateColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "ingestion");

        public static final Uri CONTENT_WEEKLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "ingestion_weekly");

        public static final Uri CONTENT_MONTHLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "ingestion_monthly");

        public static final Uri CONTENT_YEAR_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "ingestion_yearly");

        public static final String ACCOUNT_ID   = "account_id";
        public static final String BREAKFAST   = "breakfast";
        public static final String LUNCH   = "lunch";
        public static final String DINNER   = "dinner";
        public static final String ETC  = "etc";
        public static final String TOTAL_INGESTION   = "total_ingestion";
        public static final String AVG_TOTAL_INGESTION = "avg_total_ingestion";
        public static final String ALL_TOTAL_INGESTION = "all_total_ingestion";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/ingestion";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/ingestion";
    }

    public static final class WeightChange implements BaseDateColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "weight");

        public static final Uri CONTENT_WEEKLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "weight_weekly");

        public static final Uri CONTENT_MONTHLY_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "weight_monthly");

        public static final Uri CONTENT_YEAR_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "weight_yearly");

        public static final String ACCOUNT_ID   = "account_id";
        public static final String WEIGHT   = "weight";
        public static final String EXPECTED_WEIGHT   = "expected_weight";
        public static final String AVG_WEIGHT = "avg_weight";
        public static final String AVG_EXPECTED_WEIGHT = "avg_expected_weight";

        public static final String INFO_WEIGHT = BreezingProvider.TABLE_WEIGHT + "." + WEIGHT;
        public static final String INFO_EXPECTED_WEIGHT = BreezingProvider.TABLE_WEIGHT + "." + EXPECTED_WEIGHT;
        public static final String INFO_DATE = BreezingProvider.TABLE_WEIGHT + "." + DATE;

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/weight";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/weight";
    }

    public static final class HeatConsumption implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "heat_consumption");
        public static final String SPORT_TYPE   = "sport_type";
        public static final String SPORT_INTENSITY = "sport_intensity";
        public static final String SPORT_QUANTITY = "sport_quantity";
        public static final String SPORT_UNIT = "sport_unit";
        public static final String CALORIE   = "calorie";
        public static final String EQUIPMENT_NAME = "equipment_name";
        
//        public static final String SPORT_LONG   = "sport_long";
//        public static final String SPORT_STRENGTH = "sport_strength";
//        public static final String SPORT_DISTANCE   = "sport_distance";
//        public static final String SPORT_TIMES   = "sport_times";
//       
//        public static final String DATE   = "date";
//        /**
//         * The default sort order for this table
//         */
//        public static final String DEFAULT_SORT_ORDER = "date DESC";
        
        public static final Uri CONTENT_SPORT_TYPE = Uri.withAppendedPath(AUTHORITY_URI,
                "sport_type");
        
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/heat_consumption";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/heat_consumption";
    }
    
    public static final class HeatConsumptionRecord implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "consumption_record");
        
        public static final String ACCOUNT_ID   = "account_id";
        public static final String SPORT_TYPE   = "sport_type";
        public static final String SPORT_INTENSITY = "sport_intensity";
        public static final String SPORT_QUANTITY = "sport_quantity";
        public static final String SPORT_UNIT = "sport_unit";
        public static final String CALORIE   = "calorie";
        public static final String EQUIPMENT_NAME = "equipment_name";
        public static final String DATE = "date";
        
        
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/consumption_record";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/consumption_record";
    }
    
    public static final class HeatIngestion implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "heat_ingestion");
        
        public static final String FOOD_CLASSIFY_ID = "classify_id";
        public static final String FOOD_TYPE   = "food_type ";
        public static final String FOOD_NAME   = "food_name";
        public static final String NAME_EXPRESS   = "name_express";
        public static final String PRIORITY   = "priority";
        public static final String FOOD_QUANTITY   = "food_quantity";
        public static final String CALORIE   = "calorie";
        public static final String FOOD_PICTURE = "food_picture";                
                
        public static final Uri CONTENT_FOOD_TYPE = Uri.withAppendedPath(AUTHORITY_URI,
                "classify_id");
        
        public static final Uri CONTENT_FOOD_INGESTION = Uri.withAppendedPath(AUTHORITY_URI,
                "food_ingestion");
        
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "classify_id DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/heat_ingestion";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/heat_ingestion";
    }

    public static final class FoodClassify implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "food_classify");
        
        public static final String FOOD_CLASSIFY_ID = "classify_id";
        public static final String FOOD_TYPE   = "food_type ";
        public static final String UNSELECT_PICTURE   = "classify_picture";  
        public static final String SELECT_PICTURE   = "select_picture"; 
        
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "classify_id DESC";
        
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/food_classify";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/food_classify";
    }
    
    public static final class IngestiveRecord implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "ingestive_record");
        
        public static final String ACCOUNT_ID   = "account_id";
        public static final String FOOD_NAME   = "food_name";
        public static final String NAME_EXPRESS   = "name_express";
        public static final String FOOD_QUANTITY   = "food_quantity";
        public static final String CALORIE   = "calorie";
        public static final String DINING   = "dining";
        public static final String DATE   = "date";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/ingestive_record";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/ingestive_record";
    }

    public static final class UnitSettings implements BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "unit_settings");
        public static final String UNIT_TYPE   = "unit_type ";
        public static final String UNIT_NAME   = "unit_name";
        public static final String UNIT_UNIFY_DATA = "unit_unify_data";
        public static final String UNIT_OBTAIN_DATA = "unit_obtain_data";
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/unit_settings";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/unit_settings";
    }
}
