package com.breezing.health.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ImageView;

import com.breezing.health.R;
import com.breezing.health.util.BLog;

public class Tools {
    
    private static final String TAG = "Tools";

    public static String EncoderByMd5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = (md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String getVersionCode(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
        return packInfo.versionCode + "";
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    
    /**
     * Convert char to byte
     * 
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public static int getStringLength(String str) {
        int length = 0;
        char[] chars = str.toCharArray();
        final int arrayLength = chars.length;
        for (int i = 0; i < arrayLength; i++) {
            if (isChinese(chars[i])) {
                length = length + 2;
            } else {
                length++;
            }
        }
        return length;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    
    public static String getLocalMacAddress(Context context) { 
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
        WifiInfo info = wifi.getConnectionInfo();
        if (info.getMacAddress() == null) {
            return "";
        }
        return info.getMacAddress(); 
    }
    
    public static String getApplicationVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return "Android" + packInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "Android";
    }
    
    public static boolean checkMobileValidity(String mobile) {
        if (mobile != null && mobile.length() == 11) {
            return true;
        }
        return false;
    }
    
    public static String getCurrentDate() {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now);
    }
    
    public static void makeACall(Context context, String mobile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }
    
    /**
     * send sms
     * @param context
     * @param message  message content
     */
    public static void sendSMS(Context context, String message) {
        if (message == null) {
            message = "";
        }
        
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }
    
    /**
     * send email
     * @param context
     * @param subject
     * @param text
     */
    public static void sendEMAIL(Context context, String subject, String text) {
        if (subject == null) {
            subject = "";
        }
        
        if (text == null) {
            text = "";
        }
        
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }
    
    public static void sendMessage(Context context, String subject, String message) {
        if (message == null) {
            message = "";
        }
        
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);  
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.please_choose_share_method)));

    }
    
    public static void refreshVane(int number, View vane) {
        if (number > 99999) {
            return ;
        }
        
        int kNumber = 0;
        if (number > 9999) {
            kNumber = number % 10000;
            final int m = number / 10000;
            
            vane.findViewById(R.id.ring_fifth_layout).setVisibility(View.VISIBLE);
            vane.findViewById(R.id.number_fifth_layout).setVisibility(View.VISIBLE);
            ((ImageView)vane.findViewById(R.id.number_fifth)).setImageResource(getVaneImageRes(m));
        } else {
            kNumber = number;
            
            vane.findViewById(R.id.ring_fifth_layout).setVisibility(View.GONE);
            vane.findViewById(R.id.number_fifth_layout).setVisibility(View.GONE);
        }
        
        final int k = kNumber / 1000;
        final int h = kNumber % 1000 / 100;
        final int t = kNumber % 1000 % 100 / 10;
        final int u = kNumber % 1000 % 100 % 10;
        
        BLog.v(TAG, "number =" + number + ";k =" + k + ";h = " + h + "; t = " + t + ";u = " + u);
        
        ((ImageView)vane.findViewById(R.id.number_first)).setImageResource(getVaneImageRes(k));
        ((ImageView)vane.findViewById(R.id.number_second)).setImageResource(getVaneImageRes(h));
        ((ImageView)vane.findViewById(R.id.number_third)).setImageResource(getVaneImageRes(t));
        ((ImageView)vane.findViewById(R.id.number_forth)).setImageResource(getVaneImageRes(u));
    }
    
    public static int getVaneImageRes(int number) {
        switch(number) {
        case 0:
            return R.drawable.number0;
        case 1:
            return R.drawable.number1;
        case 2:
            return R.drawable.number2;
        case 3:
            return R.drawable.number3;
        case 4:
            return R.drawable.number4;
        case 5:
            return R.drawable.number5;
        case 6:
            return R.drawable.number6;
        case 7:
            return R.drawable.number7;
        case 8:
            return R.drawable.number8;
        case 9:
            return R.drawable.number9;
        default:
            return 0;
        }
    }
    
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    
}
