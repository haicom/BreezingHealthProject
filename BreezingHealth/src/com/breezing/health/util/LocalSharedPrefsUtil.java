package com.breezing.health.util;import android.content.Context;import android.content.SharedPreferences;import android.util.Log;public class LocalSharedPrefsUtil {    public final static String TAG = "LocalSharedPrefsUtil";        private LocalSharedPrefsUtil() {        // Forbidden being instantiated.    }        /***     * 从本地存储中从中获得的版本信息根据名称     * @param dataName  通过名称获得版本信息     * @return     */    public static String getSharedPrefsVersion(Context context, String dataName) {         SharedPreferences  sharedPreferences = context.getSharedPreferences(DATA_PREFS_SAVE, context.MODE_PRIVATE);         String dataVersion = sharedPreferences.getString(dataName, null);         Log.d(TAG, " getSharedPrefsVersion dataVersion = " + dataVersion);         return dataVersion;    }    /***     * 保存版本信息根据名称     * @param dataName     * @param dataVersion     */    public static void saveSharedPrefsVersion(Context context, String dataName, String dataVersion) {        SharedPreferences  sharedPreferences = context.getSharedPreferences(DATA_PREFS_SAVE, context.MODE_PRIVATE);        SharedPreferences.Editor editor = sharedPreferences.edit();        editor.putString(dataName, dataVersion);        editor.commit();    }        /***     * 通过关键字获得本地信息值     * @param context     * @param key     * @return     */    public static String getSharedPrefsValueString(Context context, String key) {         SharedPreferences  sharedPreferences = context.getSharedPreferences(DATA_PREFS_SAVE, context.MODE_PRIVATE);         String value = sharedPreferences.getString(key, null);         Log.d(TAG, " getSharedPrefsValue value = " + value);         return value;    }        public static int  getSharedPrefsValueInt(Context context, String key) {        SharedPreferences  sharedPreferences = context.getSharedPreferences(DATA_PREFS_SAVE, context.MODE_PRIVATE);        int value = sharedPreferences.getInt(key, 0);        Log.d(TAG, " getSharedPrefsValue value = " + value);        return value;   }        /***     * 保存帐户ID保存帐户密码     * @param accountId     * @param accountPass     */    public static void saveSharedPrefsAccount(Context context, int accountId, String accountPass) {        SharedPreferences  sharedPreferences = context.getSharedPreferences(DATA_PREFS_SAVE, context.MODE_PRIVATE);        SharedPreferences.Editor editor = sharedPreferences.edit();        editor.putInt(PREFS_ACCOUNT_ID, accountId);        editor.putString(String.valueOf(accountId), accountPass);        editor.commit();    }           public static final String PREFS_ACCOUNT_ID = "account_id";    private static final String DATA_PREFS_SAVE = "DataSave";}