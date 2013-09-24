package com.breezing.health.transation;import java.util.ArrayList;import android.content.ContentProviderOperation;import android.database.sqlite.SQLiteDatabase;import android.util.Log;import com.breezing.health.providers.Breezing.Account;import com.breezing.health.providers.Breezing.FoodClassify;import com.breezing.health.providers.Breezing.HeatConsumption;import com.breezing.health.providers.Breezing.HeatConsumptionRecord;import com.breezing.health.providers.Breezing.IngestiveRecord;import com.breezing.health.providers.BreezingProvider;import com.breezing.health.providers.Breezing.EnergyCost;import com.breezing.health.providers.Breezing.HeatIngestion;import com.breezing.health.providers.Breezing.Information;import com.breezing.health.providers.Breezing.Ingestion;import com.breezing.health.providers.Breezing.UnitSettings;import com.breezing.health.providers.Breezing.WeightChange;/*** * 所有与批处理数据插入相关部分都放到这个类下面 * * */public class DataBatchInserter {    private static String TAG = "DataBatchInserter";}class HeatIngestionInserter extends DataRowModify {    public HeatIngestionInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " HeatIngestionInserter buildOperation");        if (RowData.length == 7) {            ops.add(ContentProviderOperation.newInsert(HeatIngestion.CONTENT_URI)                    .withValue(HeatIngestion.FOOD_CLASSIFY_ID, RowData[0])                    .withValue(HeatIngestion.FOOD_NAME, RowData[1])                    .withValue(HeatIngestion.NAME_EXPRESS, RowData[2])                    .withValue(HeatIngestion.PRIORITY, RowData[3])                    .withValue(HeatIngestion.FOOD_QUANTITY, RowData[4])                    .withValue(HeatIngestion.CALORIE, RowData[5])                    .withValue(HeatIngestion.FOOD_PICTURE, RowData[6])                    .build());        } else {            ops.add(ContentProviderOperation.newInsert(HeatIngestion.CONTENT_URI)                    .withValue(HeatIngestion.FOOD_CLASSIFY_ID, RowData[0])                    .withValue(HeatIngestion.FOOD_NAME, RowData[1])                    .withValue(HeatIngestion.NAME_EXPRESS, RowData[2])                    .withValue(HeatIngestion.PRIORITY, RowData[3])                    .withValue(HeatIngestion.FOOD_QUANTITY, RowData[4])                    .withValue(HeatIngestion.CALORIE, RowData[5])                                       .build());        }                    }}class FoodClassifyInserter extends DataRowModify {    public FoodClassifyInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " EnergyCostInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(FoodClassify.CONTENT_URI)                .withValue(FoodClassify.FOOD_CLASSIFY_ID, RowData[0])                .withValue(FoodClassify.FOOD_TYPE, RowData[1])                .withValue(FoodClassify.CLASSIFY_PICTURE, RowData[2])                               .build());    }}class EnergyCostInserter extends DataRowModify {    public EnergyCostInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " EnergyCostInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(EnergyCost.CONTENT_URI)                .withValue(EnergyCost.ACCOUNT_ID, RowData[0])                .withValue(EnergyCost.METABOLISM, RowData[1])                .withValue(EnergyCost.SPORT, RowData[2])                .withValue(EnergyCost.DIGEST, RowData[3])                .withValue(EnergyCost.TRAIN, RowData[4])                .withValue(EnergyCost.ENERGY_COST_DATE, RowData[5])                .build());    }}class IngestionInserter extends DataRowModify {    public IngestionInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " IngestionInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(Ingestion.CONTENT_URI)                .withValue(Ingestion.ACCOUNT_ID, RowData[0])                .withValue(Ingestion.BREAKFAST, RowData[1])                .withValue(Ingestion.LUNCH, RowData[2])                .withValue(Ingestion.DINNER, RowData[3])                .withValue(Ingestion.ETC, RowData[4])                .build());    }}class WeightInserter extends DataRowModify {    public WeightInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " WeightInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(WeightChange.CONTENT_URI)                .withValue(WeightChange.ACCOUNT_ID, RowData[0])                .withValue(WeightChange.WEIGHT, RowData[1])                .withValue(WeightChange.EXPECTED_WEIGHT, RowData[2])                .build());    }}class UnitSettingsInserter extends DataRowModify {    public UnitSettingsInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " UnitSettingsInserter buildOperation RowData[0] = " + RowData[0]                 + " RowData[1] = " + RowData[1] + " RowData[2] = " + RowData[2] + " RowData[3] = " + RowData[3]);        ops.add(ContentProviderOperation.newInsert(UnitSettings.CONTENT_URI)                .withValue(UnitSettings.UNIT_TYPE, RowData[0])                .withValue(UnitSettings.UNIT_NAME, RowData[1])                .withValue(UnitSettings.UNIT_UNIFY_DATA, RowData[2])                  .withValue(UnitSettings.UNIT_OBTAIN_DATA, RowData[3])                  .build());    }}class InformationInserter extends DataRowModify {    public InformationInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " InformationInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(Information.CONTENT_URI)                .withValue(Information.ACCOUNT_ID, RowData[0])                .withValue(Information.GENDER, RowData[1])                .withValue(Information.HEIGHT, RowData[2])                .withValue(Information.BIRTHDAY, RowData[3])                    .withValue(Information.CUSTOM, RowData[4])                    .withValue(Information.HEIGHT_UNIT, RowData[5])                .withValue(Information.WEIGHT_UNIT, RowData[6])                    .withValue(Information.DISTANCE_UNIT, RowData[7])                   .build());    }}class AccountInserter extends DataRowModify {    public AccountInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " AccountInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(Account.CONTENT_URI)                .withValue(Account.ACCOUNT_NAME, RowData[0])                .withValue(Account.ACCOUNT_ID, RowData[1])                .withValue(Account.ACCOUNT_PASSWORD, RowData[2])                           .build());    }}class IngestiveRecordInserter extends DataRowModify {    public IngestiveRecordInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " AccountInserter buildOperation");        ops.add(ContentProviderOperation.newInsert(Account.CONTENT_URI)                .withValue(IngestiveRecord.FOOD_NAME, RowData[0])                .withValue(IngestiveRecord.NAME_EXPRESS, RowData[1])                .withValue(IngestiveRecord.FOOD_QUANTITY, RowData[2])                .withValue(IngestiveRecord.CALORIE, RowData[3])                .withValue(IngestiveRecord.DINING, RowData[4])                 .build());    }}class ConsumptionRecordInserter extends DataRowModify {    public ConsumptionRecordInserter() {    }    @Override    public void buildOperation(String[] RowData, ArrayList<ContentProviderOperation> ops) {        Log.d(DataTaskService.TAG, " ConsumptionRecordInserter buildOperation");                if (RowData.length == 5) {            ops.add(ContentProviderOperation.newInsert(HeatConsumption.CONTENT_URI)                    .withValue(HeatConsumption.SPORT_TYPE, RowData[0])                    .withValue(HeatConsumption.SPORT_INTENSITY, RowData[1])                    .withValue(HeatConsumption.SPORT_QUANTITY, RowData[2])                    .withValue(HeatConsumption.SPORT_UNIT, RowData[3])                    .withValue(HeatConsumption.CALORIE, RowData[4])                     .build());        } else {            ops.add(ContentProviderOperation.newInsert(HeatConsumption.CONTENT_URI)                    .withValue(HeatConsumption.SPORT_TYPE, RowData[0])                    .withValue(HeatConsumption.SPORT_INTENSITY, RowData[1])                    .withValue(HeatConsumption.SPORT_QUANTITY, RowData[2])                    .withValue(HeatConsumption.SPORT_UNIT, RowData[3])                    .withValue(HeatConsumption.CALORIE, RowData[4])                     .withValue(HeatConsumption.EQUIPMENT_NAME, RowData[5])                     .build());        }                   }}