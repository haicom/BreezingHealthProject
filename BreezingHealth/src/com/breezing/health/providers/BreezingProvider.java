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
import com.breezing.health.providers.BreezingDatabaseHelper.Views;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.DateFormatUtil;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BreezingProvider extends  SQLiteContentProvider {
    private final static String TAG = "BreezingProvider";
    public  static String TABLE_ACCOUNT = "account";
    public  static String TABLE_INFORMATION = "information";
    public  static String TABLE_COST = "cost";
    public  static String TABLE_INGESTION = "ingestion";
    public  static String TABLE_WEIGHT = "weight";
    public  static String TABLE_HEAT_CONSUMPTION  = "heat_consumption";
    public  static String TABLE_HEAT_CONSUMPTION_RECORD = "consumption_record";
    public  static String TABLE_HEAT_INGESTION = "heat_ingestion";
    public  static String TABLE_FOOD_CLASSIFY = "food_classify";
    public  static String TABLE_INGESTIVE_RECORD = "ingestive_record";
    public  static String TABLE_UNIT_SETTINGS = "unit_settings";

    private SQLiteOpenHelper mOpenHelper;

    @Override
    public String getType(Uri url) {
        Log.d(TAG, " getType uri =   " + url);
        // Generate the body of the query.
        int match = sURLMatcher.match(url);
        switch (match) {
            case BREEZING_ACCOUNT:
                return Account.CONTENT_TYPE;
            case BREEZING_ACCOUNT_ID:
                return Account.CONTENT_ITEM_TYPE;
            case BREEZING_INFORMATION:
                return Information.CONTENT_TYPE;
            case BREEZING_INFORMATION_ID:
                return Information.CONTENT_ITEM_TYPE;
            case BREEZING_COST:
                return EnergyCost.CONTENT_TYPE;
            case BREEZING_COST_ID:
                return EnergyCost.CONTENT_ITEM_TYPE;
            case BREEZING_INGESTION:
                return Ingestion.CONTENT_TYPE;
            case BREEZING_INGESTION_ID:
                return Ingestion.CONTENT_ITEM_TYPE;
            case BREEZING_WEIGHT:
                return WeightChange.CONTENT_TYPE;
            case BREEZING_WEIGHT_ID:
                return WeightChange.CONTENT_ITEM_TYPE;
            case BREEZING_HEAT_CONSUMPTION:
                return HeatConsumption.CONTENT_TYPE;
            case BREEZING_HEAT_CONSUMPTION_ID:
                return HeatConsumption.CONTENT_ITEM_TYPE;
            case BREEZING_CONSUMPTION_RECORD:
                return HeatConsumptionRecord.CONTENT_TYPE;
            case BREEZING_CONSUMPTION_RECORD_ID:
                return HeatConsumptionRecord.CONTENT_ITEM_TYPE;
            case BREEZING_HEAT_INGESTION:
                return HeatIngestion.CONTENT_TYPE;
            case BREEZING_HEAT_INGESTION_ID:
                return HeatIngestion.CONTENT_ITEM_TYPE;
            case BREEZING_FOOD_CLASSIFY:
                return FoodClassify.CONTENT_TYPE;
            case BREEZING_FOOD_CLASSIFY_ID:
                return FoodClassify.CONTENT_ITEM_TYPE;
            case BREEZING_INGESTIVE_RECORD:
                return IngestiveRecord.CONTENT_TYPE;
            case BREEZING_INGESTIVE_RECORD_ID:
                return IngestiveRecord.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    private void notifyChange(Uri uri) {
        Log.d(TAG, " notifyChange uri = " + uri);
        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
    }

    @Override
    public boolean onCreate() {
        super.onCreate();
        try {
            return initialize();
        } catch (RuntimeException e) {
            Log.e(TAG, "Cannot start provider", e);
            return false;
        }
    }

    private boolean initialize() {
        final Context context = getContext();
        mOpenHelper = getDatabaseHelper();
        mDb = mOpenHelper.getWritableDatabase();
        return (mDb != null);
    }

    @Override
    public Cursor query(Uri url, String[] projectionIn, String selection,
            String[] selectionArgs, String sort) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        // Generate the body of the query.
        int match = sURLMatcher.match(url);
        switch (match) {
            case BREEZING_ACCOUNT:
                qb.setTables(TABLE_ACCOUNT);
                break;
            case BREEZING_ACCOUNT_ID:
                qb.setTables(TABLE_ACCOUNT);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_INFORMATION:
                qb.setTables(TABLE_INFORMATION);
                break;
            case BREEZING_INFORMATION_ID:
                qb.setTables(TABLE_INFORMATION);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_BASE_INFORMATION:
                qb.setTables(Views.BASE_INFO);
                break;
            case BREEZING_COST:
                qb.setTables(TABLE_COST);
                break;
            case BREEZING_COST_ID:
                qb.setTables(TABLE_COST);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_COST_WEEKLY:
                qb.setTables(Views.COST_WEEKLY);
                break;
            case BREEZING_COST_MONTHLY:
                qb.setTables(Views.COST_MONTHLY);
                break;
            case BREEZING_COST_YEARLY:
                qb.setTables(Views.COST_YEARLY);
                break;
            case BREEZING_INGESTION:
                qb.setTables(TABLE_INGESTION);
                break;
            case BREEZING_INGESTION_ID:
                qb.setTables(TABLE_INGESTION);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_INGESTION_WEEKLY:
                qb.setTables(Views.INGESTION_WEEKLY);
                break;
            case BREEZING_INGESTION_MONTHLY:
                qb.setTables(Views.INGESTION_MONTHLY);
                break;
            case BREEZING_INGESTION_YEARLY:
                qb.setTables(Views.INGESTION_YEARLY);
                break;
            case BREEZING_WEIGHT:
                qb.setTables(TABLE_WEIGHT);
                break;
            case BREEZING_WEIGHT_ID:
                qb.setTables(TABLE_WEIGHT);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_WEIGHT_WEEKLY:
                qb.setTables(Views.WEIGHT_WEEKLY);
                break;
            case BREEZING_WEIGHT_MONTHLY:
                qb.setTables(Views.WEIGHT_MONTHLY);
                break;
            case BREEZING_WEIGHT_YEARLY:
                qb.setTables(Views.WEIGHT_YEARLY);
                break;
            case BREEZING_HEAT_CONSUMPTION:
                qb.setTables(TABLE_HEAT_CONSUMPTION);
                break;
            case BREEZING_HEAT_CONSUMPTION_ID:
                qb.setTables(TABLE_HEAT_CONSUMPTION);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_SPORT_TYPE:
                qb.setTables(Views.SPORT_TYPE);
                break;
            case BREEZING_CONSUMPTION_RECORD:
                qb.setTables(TABLE_HEAT_CONSUMPTION_RECORD);
                break;
            case BREEZING_CONSUMPTION_RECORD_ID:
                qb.setTables(TABLE_HEAT_CONSUMPTION_RECORD);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_HEAT_INGESTION:
                qb.setTables(TABLE_HEAT_INGESTION);
                break;
            case BREEZING_HEAT_INGESTION_ID:
                qb.setTables(TABLE_HEAT_INGESTION);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_FOOD_CLASSIFY:
                qb.setTables(TABLE_FOOD_CLASSIFY);
                break;
            case BREEZING_FOOD_CLASSIFY_ID:
                qb.setTables(TABLE_FOOD_CLASSIFY);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;                 
            case BREEZING_FOOD_TYPE:
                qb.setTables(TABLE_FOOD_CLASSIFY);
                break;
            case BREEZING_FOOD_INGESTION:
                qb.setTables(Views.FOOD_INGESTION);
                break;
            case BREEZING_INGESTIVE_RECORD:
                qb.setTables(TABLE_INGESTIVE_RECORD);
                break;
            case BREEZING_INGESTIVE_RECORD_ID:
                qb.setTables(TABLE_INGESTIVE_RECORD);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            case BREEZING_UNIT_SETTINGS:
                qb.setTables(TABLE_UNIT_SETTINGS);
                break;
            case BREEZING_UNIT_SETTINGS_ID:
                qb.setTables(TABLE_UNIT_SETTINGS);
                qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
                break;
            default:
                Log.e(TAG, "query: invalid request: " + url);
                return null;
        }

        String finalSortOrder = null;
        if (TextUtils.isEmpty(sort)) {
            if (qb.getTables().equals(TABLE_COST)) {
                finalSortOrder = Breezing.EnergyCost.DEFAULT_SORT_ORDER;
            } else if (qb.getTables().equals(TABLE_INGESTION)) {
                finalSortOrder = Breezing.Ingestion.DEFAULT_SORT_ORDER;
            } else if (qb.getTables().equals(TABLE_WEIGHT)) {
                finalSortOrder = Breezing.WeightChange.DEFAULT_SORT_ORDER;
            } else if (qb.getTables().equals(TABLE_HEAT_CONSUMPTION_RECORD)) {
                finalSortOrder = Breezing.HeatConsumptionRecord.DEFAULT_SORT_ORDER;
            } else if ( qb.getTables().equals(TABLE_FOOD_CLASSIFY) ) {
                finalSortOrder = Breezing.FoodClassify.DEFAULT_SORT_ORDER;
            } else if ( qb.getTables().equals(TABLE_HEAT_INGESTION) ) {
                finalSortOrder = Breezing.HeatIngestion.DEFAULT_SORT_ORDER;
            }
        } else {
            finalSortOrder = sort;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = qb.query(db, projectionIn, selection,
                selectionArgs, null, null, finalSortOrder);

        // TODO: Does this need to be a URI for this provider.
        ret.setNotificationUri(getContext().getContentResolver(), url);
        return ret;
    }

    @Override
    public int delete(Uri url, String where, String[] whereArgs) {
        return super.delete(url, where, whereArgs);
    }

    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        return super.insert(url, initialValues);
    }

    @Override
    public int update(Uri url, ContentValues values, String where,
            String[] whereArgs) {
        return super.update(url, values, where, whereArgs);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        Log.d(TAG, " applyBatch ");
        return super.applyBatch(operations);
    }

    /***
     * 添加  EnergyCost Ingestion WeightChange 这几个表基本的日期格式
     * @param initialValues
     * @param values
     * @param addDate
     */
    private ContentValues addDateFormat(ContentValues initialValues, ContentValues values, boolean addDate) {
        int dateInt = simpleDateFormat("yyyyMMdd");
        int year = 0;
        int month = 0;
        int week = 0;
        int weekOfYear = 0; 

        if (addDate) {
            values.put(Breezing.BaseDateColumns.DATE, dateInt);
            year = simpleDateFormat("yyyy");
            month = simpleDateFormat("yyyyMM");
            weekOfYear = CalendarUtil.getWeekOfYear(new Date());
            week = DateFormatUtil.getCompleteWeek(year, weekOfYear);
        } else {
            Date date = null;            
            dateInt = values.getAsInteger(Breezing.BaseDateColumns.DATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                date = dateFormat.parse( String.valueOf(dateInt) );
            } catch (ParseException e) {                
                e.printStackTrace();
            }
            year = simpleDateFormat("yyyy", dateInt);
            month = simpleDateFormat("yyyyMM", dateInt);
            weekOfYear = CalendarUtil.getWeekOfYear(date);
            week = DateFormatUtil.getCompleteWeek(year, weekOfYear);
        }

        
        values.put(Breezing.BaseDateColumns.YEAR, year);        
        values.put(Breezing.BaseDateColumns.YEAR_MONTH, month);
        //int week = simpleDateFormat("yyyyww");        
        values.put(Breezing.BaseDateColumns.YEAR_WEEK, week);

        return values;
    }

    /***
     * 获得现在日期格式并转化为int类型
     * @param format
     * @return
     */
    private int simpleDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = sdf.format(new Date());
        Log.d(TAG, "format = " + format + " sdf.format(new Date())  = " +  sdf.format(new Date()));
        Date date = null;
        try {
            date =  sdf.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(TAG, " simpleDateFormat date = " + date);
        int intDate = Integer.parseInt(strDate);
        Log.d(TAG, "simpleDateFormat longDate = " + intDate);
        return intDate;
    }
    
    /***
     * 获得现在日期格式并转化为int类型
     * @param format
     * @return
     */
    private int simpleDateFormat(String format, int inputDate) {
        Date date = null;
        if (inputDate != 0 ) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                date = dateFormat.parse( String.valueOf(inputDate) );
            } catch (ParseException e) {                
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }        
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = sdf.format( date );
        Log.d(TAG, "format = " + format + " sdf.format(new Date())  = " +  sdf.format(new Date()));
        

        Log.d(TAG, " simpleDateFormat date = " + date);
        int intDate = Integer.parseInt(strDate);
        Log.d(TAG, "simpleDateFormat longDate = " + intDate);
        return intDate;
    }



    @Override
    protected SQLiteOpenHelper getDatabaseHelper(Context context) {
        return BreezingDatabaseHelper.getInstance(getContext());
    }

    @Override
    protected Uri insertInTransaction(Uri url, ContentValues initialValues) {
        long rowID = 0;
        int match = sURLMatcher.match(url);

        Log.d(TAG, "Insert uri = " + url + ", match = " + match);


        switch (match) {
            case BREEZING_ACCOUNT:
                rowID = mDb.insert(TABLE_ACCOUNT, Account.ACCOUNT_ID, initialValues);
                break;
            case BREEZING_INFORMATION:
                rowID = mDb.insert(TABLE_INFORMATION, Information.ACCOUNT_ID, initialValues);
                break;
            case BREEZING_COST:
                rowID = insertCostTable(initialValues);
                break;
            case BREEZING_INGESTION:
                rowID = insertIngestionTable(initialValues);
                break;
            case BREEZING_WEIGHT:
                rowID = insertWeightTable(initialValues);
                break;
            case BREEZING_HEAT_CONSUMPTION:
                rowID = mDb.insert(TABLE_HEAT_CONSUMPTION, HeatConsumption.SPORT_TYPE, initialValues);
                break;                
            case BREEZING_CONSUMPTION_RECORD:
                rowID = insertConsumptionRecordTable(initialValues);
                break;
            case BREEZING_HEAT_INGESTION:
                rowID = mDb.insert(TABLE_HEAT_INGESTION, HeatIngestion.FOOD_TYPE, initialValues);
                break;
            case BREEZING_FOOD_CLASSIFY:
                rowID = mDb.insert(TABLE_FOOD_CLASSIFY, FoodClassify.FOOD_TYPE, initialValues);
                break;
            case BREEZING_INGESTIVE_RECORD:
                rowID = insertIngestiveRecordTable(initialValues);
                break;
            case BREEZING_UNIT_SETTINGS:
                rowID = mDb.insert(TABLE_UNIT_SETTINGS, UnitSettings.UNIT_TYPE, initialValues);
                break;
            default:
                Log.e(TAG, "insert: invalid request: " + url);
                return null;
        }

        if (rowID > 0) {
            Uri uri = ContentUris.withAppendedId(url, rowID);

            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.d(TAG, "insert " + uri + " succeeded");
            }
            notifyChange(url);
            return uri;
        } else {
            Log.e(TAG,"insert: failed! " + initialValues.toString());
        }

        return null;
    }

    private long insertCostTable(ContentValues initialValues) {
        int totalEnergy = 0;
        boolean addDate = false;
        ContentValues values = new ContentValues(initialValues);

        if (!initialValues.containsKey(EnergyCost.DATE)) {
            addDate = true;
        }

        if (initialValues.containsKey(EnergyCost.METABOLISM)) {
            totalEnergy += initialValues.getAsInteger(EnergyCost.METABOLISM);
        }

        if (initialValues.containsKey(EnergyCost.SPORT)) {
            totalEnergy += initialValues.getAsInteger(EnergyCost.SPORT);
        }

        if (initialValues.containsKey(EnergyCost.DIGEST)) {
            totalEnergy += initialValues.getAsInteger(EnergyCost.DIGEST);
        }

        if (initialValues.containsKey(EnergyCost.TRAIN)) {
            totalEnergy += initialValues.getAsInteger(EnergyCost.TRAIN);
        }

        values.put(EnergyCost.TOTAL_ENERGY, totalEnergy);
        values = addDateFormat(initialValues, values, addDate);
        return mDb.insert(TABLE_COST, EnergyCost.ACCOUNT_ID, values);
    }

    private long insertIngestionTable(ContentValues initialValues) {
        int totalIngestion = 0;
        boolean addDate = false;
        ContentValues values = new ContentValues(initialValues);
        if (!initialValues.containsKey(Ingestion.DATE)) {
            addDate = true;
        }

        if (initialValues.containsKey(Ingestion.BREAKFAST)) {
            totalIngestion += initialValues.getAsInteger(Ingestion.BREAKFAST);
        }

        if (initialValues.containsKey(Ingestion.LUNCH)) {
            totalIngestion += initialValues.getAsInteger(Ingestion.LUNCH);
        }

        if (initialValues.containsKey(Ingestion.DINNER)) {
            totalIngestion += initialValues.getAsInteger(Ingestion.DINNER);
        }

        if (initialValues.containsKey(Ingestion.ETC)) {
            totalIngestion += initialValues.getAsInteger(Ingestion.ETC);
        }

        values.put(Breezing.Ingestion.TOTAL_INGESTION, totalIngestion);
        values = addDateFormat(initialValues, values, addDate);

        return mDb.insert(TABLE_INGESTION, Ingestion.ACCOUNT_ID, values);
    }

    private long insertWeightTable(ContentValues initialValues) {
        boolean addDate = false;
        ContentValues values = new ContentValues(initialValues);
        if (!initialValues.containsKey(WeightChange.DATE)) {
            addDate = true;
        }
        values = addDateFormat(initialValues, values, addDate);
        return mDb.insert(TABLE_WEIGHT, WeightChange.ACCOUNT_ID, values);
    }

    private long insertConsumptionRecordTable(ContentValues initialValues) {
        boolean addDate = false;
        ContentValues values = new ContentValues(initialValues);

        if (!initialValues.containsKey(HeatConsumptionRecord.DATE)) {
            addDate = true;
        }

        if (addDate) {
            int date = simpleDateFormat("yyyyMMdd");
            values.put(HeatConsumptionRecord.DATE, date);
        }

        return mDb.insert(TABLE_HEAT_CONSUMPTION_RECORD, HeatConsumptionRecord.SPORT_TYPE, values);
    }

    private long insertIngestiveRecordTable(ContentValues initialValues) {
        boolean addDate = false;
        ContentValues values = new ContentValues(initialValues);

        if (!initialValues.containsKey(IngestiveRecord.DATE)) {
            addDate = true;
        }

        if (addDate) {
            int date = simpleDateFormat("yyyyMMdd");
            values.put(IngestiveRecord.DATE, date);
        }

        return mDb.insert(TABLE_INGESTIVE_RECORD, IngestiveRecord.FOOD_NAME, values);
    }

    @Override
    protected int updateInTransaction(Uri url, ContentValues values, String where,
                String[] whereArgs) {
        int count = 0;
        String table = null;
        String extraWhere = null;

        int match = sURLMatcher.match(url);
        switch (match) {
            case BREEZING_ACCOUNT:
                table = TABLE_ACCOUNT;
                break;
            case BREEZING_ACCOUNT_ID:
                table = TABLE_ACCOUNT;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_INFORMATION:
                table = TABLE_INFORMATION;
                break;
            case BREEZING_INFORMATION_ID:
                table = TABLE_INFORMATION;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_COST:
                table = TABLE_COST;
                break;
            case BREEZING_COST_ID:
                table = TABLE_COST;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_INGESTION:
                table = TABLE_INGESTION;
                break;
            case BREEZING_INGESTION_ID:
                table = TABLE_INGESTION;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_FOOD_CLASSIFY:
                table = TABLE_FOOD_CLASSIFY;
                break;
            case BREEZING_FOOD_CLASSIFY_ID:
                table = TABLE_FOOD_CLASSIFY;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;     
            case BREEZING_WEIGHT:
                table = TABLE_WEIGHT;
                break;
            case BREEZING_WEIGHT_ID:
                table = TABLE_WEIGHT;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_HEAT_CONSUMPTION:
                table = TABLE_HEAT_CONSUMPTION;
                break;
            case BREEZING_HEAT_CONSUMPTION_ID:
                table = TABLE_HEAT_CONSUMPTION;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_CONSUMPTION_RECORD:
                table = TABLE_HEAT_CONSUMPTION_RECORD;
                break;
            case BREEZING_CONSUMPTION_RECORD_ID:
                table = TABLE_HEAT_CONSUMPTION_RECORD;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_HEAT_INGESTION:
                table = TABLE_HEAT_INGESTION;
                break;
            case BREEZING_HEAT_INGESTION_ID:
                table = TABLE_HEAT_INGESTION;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_INGESTIVE_RECORD:
                table = TABLE_INGESTIVE_RECORD;
                break;
            case BREEZING_INGESTIVE_RECORD_ID:
                table = TABLE_INGESTIVE_RECORD;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            case BREEZING_UNIT_SETTINGS:
                table = TABLE_UNIT_SETTINGS;
                break;
            case BREEZING_UNIT_SETTINGS_ID:
                table = TABLE_UNIT_SETTINGS;
                extraWhere = "_id=" + url.getPathSegments().get(1);
                break;
            default:
                throw new UnsupportedOperationException(
                        "URI " + url + " not supported");
        }

        if (extraWhere != null) {
            where = DatabaseUtils.concatenateWhere(where, extraWhere);
        }

        Log.d(TAG, "update where = " + where);
        count = mDb.update(table, values, where, whereArgs);

        if (count > 0) {
            Log.d(TAG, "update " + url + " succeeded");
            notifyChange(url);
        }

        return count;
    }

    @Override
    protected int deleteInTransaction(Uri url, String where,
            String[] whereArgs) {
        int count = 0;
        int match = sURLMatcher.match(url);


        switch (match) {
            case BREEZING_ACCOUNT:
                count = mDb.delete(TABLE_ACCOUNT, where, whereArgs);
                break;
            case BREEZING_ACCOUNT_ID:
                int accountId;

                try {
                    accountId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + accountId, where);
                count = mDb.delete(TABLE_ACCOUNT, where, whereArgs);
                break;
            case BREEZING_INFORMATION:
                count = mDb.delete(TABLE_INFORMATION, where, whereArgs);
                break;
            case BREEZING_INFORMATION_ID:
                int informationId = 0;

                try {
                    informationId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + informationId, where);
                count = mDb.delete(TABLE_INFORMATION, where, whereArgs);
                break;
            case BREEZING_COST:
                count = mDb.delete(TABLE_COST, where, whereArgs);
                break;
            case BREEZING_COST_ID:
                int costId;

                try {
                    costId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + costId, where);
                count = mDb.delete(TABLE_COST, where, whereArgs);
                break;
            case BREEZING_INGESTION:
                count = mDb.delete(TABLE_INGESTION, where, whereArgs);
                break;
            case BREEZING_INGESTION_ID:
                int ingestionId;

                try {
                    ingestionId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + ingestionId, where);
                count = mDb.delete(TABLE_INGESTION, where, whereArgs);
                break;
            case BREEZING_FOOD_CLASSIFY:
                count = mDb.delete(TABLE_FOOD_CLASSIFY, where, whereArgs);
                break;
            case BREEZING_FOOD_CLASSIFY_ID:
                int classifyId;

                try {
                    classifyId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + classifyId, where);
                count = mDb.delete(TABLE_FOOD_CLASSIFY, where, whereArgs);
                break;    
            case BREEZING_WEIGHT:
                count = mDb.delete(TABLE_WEIGHT, where, whereArgs);
                break;
            case BREEZING_WEIGHT_ID:
                int weightId;

                try {
                    weightId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + weightId, where);
                count = mDb.delete(TABLE_WEIGHT, where, whereArgs);
                break;
            case BREEZING_HEAT_CONSUMPTION:
                count = mDb.delete(TABLE_HEAT_CONSUMPTION, where, whereArgs);
                break;
            case BREEZING_HEAT_CONSUMPTION_ID:
                int consumptionId;
                try {
                    consumptionId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + consumptionId, where);
                count = mDb.delete(TABLE_HEAT_CONSUMPTION, where, whereArgs);
                break;
            case BREEZING_CONSUMPTION_RECORD:
                count = mDb.delete(TABLE_HEAT_CONSUMPTION_RECORD, where, whereArgs);
                break;
            case BREEZING_CONSUMPTION_RECORD_ID:
                int recordId;
                try {
                    recordId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + recordId, where);
                count = mDb.delete(TABLE_HEAT_CONSUMPTION_RECORD, where, whereArgs);
                break;
            case BREEZING_HEAT_INGESTION:
                count = mDb.delete(TABLE_HEAT_INGESTION, where, whereArgs);
                break;
            case BREEZING_HEAT_INGESTION_ID:
                int heatIngestion;
                try {
                    heatIngestion = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + heatIngestion, where);
                count = mDb.delete(TABLE_HEAT_INGESTION, where, whereArgs);
                break;
            case BREEZING_INGESTIVE_RECORD:
                count = mDb.delete(TABLE_INGESTIVE_RECORD, where, whereArgs);
                break;
            case BREEZING_INGESTIVE_RECORD_ID:
                int ingestiveRecord;
                try {
                    ingestiveRecord = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + ingestiveRecord, where);
                count = mDb.delete(TABLE_INGESTIVE_RECORD, where, whereArgs);
                break;
            case BREEZING_UNIT_SETTINGS:
                count = mDb.delete(TABLE_UNIT_SETTINGS, where, whereArgs);
                break;
            case BREEZING_UNIT_SETTINGS_ID:
                int settingsId;
                try {
                    settingsId = Integer.parseInt(url.getPathSegments().get(1));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        "Bad message id: " + url.getPathSegments().get(1));
                }

                where = DatabaseUtils.concatenateWhere("_id = " + settingsId, where);
                count = mDb.delete(TABLE_UNIT_SETTINGS, where, whereArgs);
                break;
            default:
                Log.e(TAG, "query: invalid request: " + url);
        }

        return count;
    }

    @Override
    protected void notifyChange() {
        ContentResolver cr = getContext().getContentResolver();
        //cr.notifyChange(uri, null);
    }

    private static final int BREEZING_ACCOUNT = 1;
    private static final int BREEZING_ACCOUNT_ID = 2;
    private static final int BREEZING_INFORMATION = 3;
    private static final int BREEZING_INFORMATION_ID = 4;
    private static final int BREEZING_BASE_INFORMATION = 5;
    private static final int BREEZING_COST = 6;
    private static final int BREEZING_COST_ID = 7;
    private static final int BREEZING_COST_WEEKLY = 8;
    private static final int BREEZING_COST_MONTHLY = 9;
    private static final int BREEZING_COST_YEARLY = 10;
    private static final int BREEZING_INGESTION = 11;
    private static final int BREEZING_INGESTION_ID = 12;
    private static final int BREEZING_INGESTION_WEEKLY = 13;
    private static final int BREEZING_INGESTION_MONTHLY = 14;
    private static final int BREEZING_INGESTION_YEARLY = 15;
    private static final int BREEZING_WEIGHT = 16;
    private static final int BREEZING_WEIGHT_ID = 17;
    private static final int BREEZING_WEIGHT_WEEKLY = 18;
    private static final int BREEZING_WEIGHT_MONTHLY = 19;
    private static final int BREEZING_WEIGHT_YEARLY = 20;
    private static final int BREEZING_HEAT_CONSUMPTION = 21;
    private static final int BREEZING_HEAT_CONSUMPTION_ID = 22;
    private static final int BREEZING_SPORT_TYPE = 23;
    private static final int BREEZING_CONSUMPTION_RECORD = 24;
    private static final int BREEZING_CONSUMPTION_RECORD_ID = 25;
    private static final int BREEZING_HEAT_INGESTION = 26;
    private static final int BREEZING_HEAT_INGESTION_ID = 27;
    private static final int BREEZING_FOOD_CLASSIFY = 28;
    private static final int BREEZING_FOOD_CLASSIFY_ID = 29;
    private static final int BREEZING_FOOD_INGESTION = 30;   
    private static final int BREEZING_FOOD_TYPE = 31;
    private static final int BREEZING_INGESTIVE_RECORD = 32;
    private static final int BREEZING_INGESTIVE_RECORD_ID = 33;
    private static final int BREEZING_UNIT_SETTINGS = 34;
    private static final int BREEZING_UNIT_SETTINGS_ID = 35;

    private static final UriMatcher sURLMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURLMatcher.addURI(Breezing.AUTHORITY, "account", BREEZING_ACCOUNT);
        sURLMatcher.addURI(Breezing.AUTHORITY, "account/#", BREEZING_ACCOUNT_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "information", BREEZING_INFORMATION);
        sURLMatcher.addURI(Breezing.AUTHORITY, "information/#", BREEZING_INFORMATION_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "base_info", BREEZING_BASE_INFORMATION);
        sURLMatcher.addURI(Breezing.AUTHORITY, "cost", BREEZING_COST);
        sURLMatcher.addURI(Breezing.AUTHORITY, "cost/#", BREEZING_COST_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "cost_weekly", BREEZING_COST_WEEKLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "cost_monthly", BREEZING_COST_MONTHLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "cost_yearly", BREEZING_COST_YEARLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestion", BREEZING_INGESTION);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestion/#", BREEZING_INGESTION_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestion_weekly", BREEZING_INGESTION_WEEKLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestion_monthly", BREEZING_INGESTION_MONTHLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestion_yearly", BREEZING_INGESTION_YEARLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "weight", BREEZING_WEIGHT);
        sURLMatcher.addURI(Breezing.AUTHORITY, "weight/#", BREEZING_WEIGHT_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "weight_weekly", BREEZING_WEIGHT_WEEKLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "weight_monthly", BREEZING_WEIGHT_MONTHLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "weight_yearly", BREEZING_WEIGHT_YEARLY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "heat_consumption", BREEZING_HEAT_CONSUMPTION);
        sURLMatcher.addURI(Breezing.AUTHORITY, "heat_consumption/#", BREEZING_HEAT_CONSUMPTION_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "sport_type", BREEZING_SPORT_TYPE);
        sURLMatcher.addURI(Breezing.AUTHORITY, "consumption_record", BREEZING_CONSUMPTION_RECORD);
        sURLMatcher.addURI(Breezing.AUTHORITY, "consumption_record/#", BREEZING_CONSUMPTION_RECORD_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "heat_ingestion", BREEZING_HEAT_INGESTION);
        sURLMatcher.addURI(Breezing.AUTHORITY, "heat_ingestion/#", BREEZING_HEAT_INGESTION_ID);
        
        sURLMatcher.addURI(Breezing.AUTHORITY, "food_classify", BREEZING_FOOD_CLASSIFY);
        sURLMatcher.addURI(Breezing.AUTHORITY, "food_classify/#", BREEZING_FOOD_CLASSIFY_ID);
        
        sURLMatcher.addURI(Breezing.AUTHORITY, "food_ingestion", BREEZING_FOOD_INGESTION);
       
        
        sURLMatcher.addURI(Breezing.AUTHORITY, "food_type", BREEZING_FOOD_TYPE);
        
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestive_record", BREEZING_INGESTIVE_RECORD);
        sURLMatcher.addURI(Breezing.AUTHORITY, "ingestive_record/#", BREEZING_INGESTIVE_RECORD_ID);
        sURLMatcher.addURI(Breezing.AUTHORITY, "unit_settings", BREEZING_UNIT_SETTINGS);
        sURLMatcher.addURI(Breezing.AUTHORITY, "unit_settings/#", BREEZING_UNIT_SETTINGS_ID);
    }


}
