package com.breezing.health.util;import com.breezing.health.R;import android.content.Context;public class ChangeUnitUtil {    private final static String TAG = "UnitChangeUnit";    public final static int GENDER_MALE = 1;    public final static int GENDER_FEMALE = 2;    public final static int CUSTOM_LONG_TIME_SITTING = 1;    public final static int CUSTOM_LOW_ACTIVITY = 2;    public final static int CUSTOM_ACTIVITY = 3;    public final static int CUSTOM_OFTEN_ACTIVITY =4;    private ChangeUnitUtil() {    }    public static int changeGenderUtil(Context context, String gender) {        int result = 0;        if ( context.getString(R.string.male).equals(gender) ) {            result = GENDER_MALE;        } else if ( context.getString(R.string.female).equals(gender) ) {            result = GENDER_FEMALE;        }        return result;    }        public static int changeGenderToId(int gender) {        int result = 0;                if (gender == GENDER_MALE) {            result = R.string.male;                    } else if (gender == GENDER_FEMALE) {            result = R.string.female;         }                return result;    }		public static String changeGenderUtil(Context context, int gender) {        String result = "";        if ( gender == GENDER_MALE ) {            result = context.getString(R.string.male);        } else if ( gender == GENDER_FEMALE ) {            result = context.getString(R.string.female);        }        return result;    }    public static int changeCustomUtil(Context context, String type) {        int custom = 0;        context.getResources().getStringArray(R.array.jobTypes);        if ( context.getResources().             getString(R.string.longtime_sitting).equals(type) ) {            custom = CUSTOM_LONG_TIME_SITTING;        } else if ( context.getResources().                    getString(R.string.low_activity).equals(type) ) {            custom = CUSTOM_LOW_ACTIVITY;        } else if ( context.getResources().                    getString(R.string.activity).equals(type)) {            custom = CUSTOM_ACTIVITY;        } else if ( context.getResources().                    getString(R.string.often_activity).equals(type)) {            custom = CUSTOM_OFTEN_ACTIVITY;        }        return custom;    }        public static int changeCustomUtilToId(int custom) {        int result = 0;                if (custom == CUSTOM_LONG_TIME_SITTING) {            result = R.string.longtime_sitting;        } else if (custom == CUSTOM_LOW_ACTIVITY) {            result = R.string.low_activity;        } else if (custom == CUSTOM_ACTIVITY) {            result = R.string.activity;        } else if (custom == CUSTOM_OFTEN_ACTIVITY) {            result = R.string.often_activity;        }               return result;    }}