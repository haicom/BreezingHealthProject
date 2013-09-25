package com.breezing.health.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.breezing.health.R;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

public class CalendarUtil {
    private static final String TAG = "CalendarUtil";
    private static final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    private static final Calendar  todayCalendar = Calendar.getInstance();

    public static String getMonthNameFromLocale(int month) {
        if (month < 0 || month > 11)
            throw new IndexOutOfBoundsException(
                    "Invalid index, value must be between 0 and 11");

        return dateFormatSymbols.getMonths()[month].toUpperCase();
    }

    public static String getShortWeekdayName(int day) {
        if (day < 1 || day > 7)
            throw new IndexOutOfBoundsException(
                    "Invalid index, value must be between 1 and 7");
        String dayName = dateFormatSymbols.getShortWeekdays()[day];
        return dayName.replace(dayName.charAt(0),
                Character.toUpperCase(dayName.charAt(0)));
    }

    public static boolean isTodayInCalendar(Calendar calendar) {
        return todayCalendar.get(Calendar.DAY_OF_MONTH) == calendar
                .get(Calendar.DAY_OF_MONTH)
                && todayCalendar.get(Calendar.MONTH) == calendar
                        .get(Calendar.MONTH)
                && todayCalendar.get(Calendar.YEAR) == calendar
                        .get(Calendar.YEAR);
    }

    public static boolean isWeekendDayInCalendar(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static boolean isDateBeforeToday(Calendar calendar) {
        Calendar validationCalendar = Calendar.getInstance();
        validationCalendar.setTime(calendar.getTime());
        validationCalendar.set(Calendar.MILLISECOND,
                todayCalendar.get(Calendar.MILLISECOND));
        validationCalendar.set(Calendar.SECOND,
                todayCalendar.get(Calendar.SECOND));
        validationCalendar.set(Calendar.MINUTE,
                todayCalendar.get(Calendar.MINUTE));
        validationCalendar.set(Calendar.HOUR_OF_DAY,
                todayCalendar.get(Calendar.HOUR_OF_DAY));
        return validationCalendar.getTime().before(todayCalendar.getTime());
    }

    public static boolean isSameDateInCalendar(Calendar calendar, Date date) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        return dateCalendar.get(Calendar.DAY_OF_MONTH) == calendar
                .get(Calendar.DAY_OF_MONTH)
                && dateCalendar.get(Calendar.MONTH) == calendar
                        .get(Calendar.MONTH)
                && dateCalendar.get(Calendar.YEAR) == calendar
                        .get(Calendar.YEAR);
    }

    public static String getLocaleFormatDate(Date date, Context context) {
        return DateFormat.getDateFormat(context).format(date);
    }

    public static int getTotalWeeksInYear(int year) {

        Calendar cal = new  GregorianCalendar();

        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
        cal.set(GregorianCalendar.DAY_OF_MONTH, 31);

        final int weekOfLastDay = cal.get(GregorianCalendar.WEEK_OF_YEAR);

        if (weekOfLastDay != 1) {
            return cal.get(GregorianCalendar.WEEK_OF_YEAR);
        } else {
            cal.set(GregorianCalendar.YEAR, year);
            cal.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            final int weekOfFirstDay = cal.get(GregorianCalendar.WEEK_OF_YEAR);
            if (weekOfFirstDay == 1) {
                return 52;
            } else {
                return 51;
            }
        }
    }


    /**
     * 得到某一年周的总数
     *
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, Calendar.DECEMBER, CALENDAR_UTIL_DECEMBER_DAY,
                CALENDAR_UTIL_DECEMBER_HOUR, CALENDAR_UTIL_DECEMBER_MINUTES,
                CALENDAR_UTIL_DECEMBER_SECOND);

        return getWeekOfYear(calendar.getTime());
    }

    /**
     * 获取某年某周的开始日期和结束日期
     *
     * @param year
     * @param week
     * @return
     * @throws Exception
     */
    public static String[] getStartEndOfWeek(int year, int week)
            throws Exception {
        Log.d(TAG, " getStartEndOfWeek year = " + year + " week = " + week);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
        // calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();
        String startDateInStr = format.format(startDate);
        calendar.add(GregorianCalendar.DATE, CALENDAR_UTIL_DECEMBER_WEEK);

        Date enddate = calendar.getTime();
        String endDateInStr = format.format(enddate);
        Log.d(TAG, "getStartEndOfWeek  " + " year = " + year + " week = "
                + week + " startDateInStr = " + startDateInStr
                + " endDateInStr = " + endDateInStr);
        return new String[] { startDateInStr, endDateInStr };
    }

    /***
     * 获得指定周的第一天，按照星期一计算
     * @param year
     * @param week
     * @return
     */
    public static Calendar getFirstDayOfWeek(int year, int week) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
        // calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar;
    }

    /***
     * 获得指定周的最后一天，按照星期一计算
     * @param year
     * @param week
     * @return
     */
    public static Calendar getLastDayOfWeek(int year, int week) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
        // calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(GregorianCalendar.DATE, CALENDAR_UTIL_DECEMBER_WEEK);
        return calendar;
    }

    /***
     * 通过Calendar获得字符型日期
     * @param calendar
     * @return
     */
    public static String getDayFromCalendar(Calendar calendar) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = calendar.getTime();

        return simpleDateFormat.format(date);
    }


    /***
     *
     * @return
     */
    public static String[] getYearsFrom2013() {

        Calendar calendar = new GregorianCalendar();

        int currentYear = calendar.get(GregorianCalendar.YEAR);

        if (currentYear < CALENDAR_UTIL_YEAR_2013) {
            return null;
        }

        final int interval = currentYear - CALENDAR_UTIL_YEAR_2013;

        String[] years = new String[interval + 1];
        for (int i = CALENDAR_UTIL_YEAR_2013; i <= currentYear; i++) {
            years[i - CALENDAR_UTIL_YEAR_2013] = String.valueOf(i);
        }

        return years;
    }

    /****
     * 获得某一个月的周数
     * @param year
     * @param yearWeek
     * @return
     */
    public static int getWeekNumberOfMonth(int year, int yearWeek) {
        Calendar calendar = new GregorianCalendar();

        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, yearWeek);

        // calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /***
     * 获得指定月的全部周数
     * @param year
     * @param month
     * @return
     */
    public static int getTotalWeeksInMonth(int year, int month) {

        Calendar calendar = new GregorianCalendar();

        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        int maxMum = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        calendar.add(GregorianCalendar.DATE, maxMum - 1);

        Log.d(TAG, "getTotalWeeksInMonth maxMum = " + maxMum + " month = "
        + month + " calendar.get(GregorianCalendar.MONTH)  =" + calendar.get(GregorianCalendar.MONTH)
        + " calendar.get(GregorianCalendar.DATE) = " + calendar.get(GregorianCalendar.DATE) );
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /****
     * 得到指定月第一天的Calendar
     * @param year
     * @param month
     * @return
     */
    public static Calendar getFirstCalendarOfMonth(int year, int month) {

        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);

        return calendar;
    }

    public static int getCurrentYear() {
        Calendar calendar =  new GregorianCalendar();
        int currentYear = calendar.get(GregorianCalendar.YEAR);
        return currentYear;
    }

    public static int getCurrentMonth() {
        final Calendar calendar =  new GregorianCalendar();
        final int currentMonth = calendar.get(GregorianCalendar.MONTH) + 1;
        return currentMonth;
    }

    public static int getCurrentWeek() {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int currentWeek = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        BLog.d(TAG, " getCurrentWeek currentWeek = " + currentWeek);
        return currentWeek;
    }

    /***
     * 获得当前日期的周数
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Log.d(TAG, "getWeekOfYear date = " + date.getDate()
                + " date.getMonth() = " +
                date.getMonth());
        Calendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);

        Log.d(TAG, "getWeekOfYear weekNum = " + weekNum);

        return weekNum;

    }

    public static String getFirstDayAndLastDayOfWeek(Context context,
            int year, int week) {
        int currentYear = 0;
        int firstMonth = 0;
        int lastMonth = 0;
        int firstDay = 0;
        int lastDay = 0;

        String[] startEnd = null;
        Log.d(TAG, "getFirstDayAndLastDayOfWeek years = " + year
                + " week  = " + week);
        try {
            startEnd = CalendarUtil.getStartEndOfWeek(year, week);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getFirstDayAndLastDayOfWeek startEnd[0] = " + startEnd[0]
                + " startEnd[1]  = " + startEnd[1]);
        String firstDate = startEnd[0];

        if (firstDate.length() == WEEK_PICKER_DATE_LEN) {

            currentYear = Integer.valueOf(firstDate.subSequence(0, WEEK_PICKER_YEAR)
                    .toString());

            firstMonth = Integer.valueOf(firstDate.subSequence(
                    WEEK_PICKER_YEAR, WEEK_PICKER_YEAR + WEEK_PICKER_MONTH)
                    .toString());

            firstDay = Integer.valueOf(firstDate.subSequence(
                    WEEK_PICKER_YEAR + WEEK_PICKER_MONTH, firstDate.length())
                    .toString());

        }

        Log.d(TAG, "getFirstDayAndLastDayOfWeek year = " + year
                + " firstMonth = " + firstMonth + " firstDay =" + firstDay);

        String lastDate = startEnd[1];

        if (lastDate.length() == WEEK_PICKER_DATE_LEN) {
            currentYear = Integer.valueOf(lastDate.subSequence(0, WEEK_PICKER_YEAR)
                    .toString());

            lastMonth = Integer.valueOf(lastDate.subSequence(WEEK_PICKER_YEAR,
                    WEEK_PICKER_YEAR + WEEK_PICKER_MONTH).toString());
            lastDay = Integer.valueOf(lastDate.subSequence(
                    WEEK_PICKER_YEAR + WEEK_PICKER_MONTH, lastDate.length())
                    .toString());
        }

        Log.d(TAG, "getFirstDayAndLastDayOfWeek year = " + year
                + " lastMonth = " + lastMonth + " lastDay =" + lastDay);
        return context.getString(R.string.first_day_last_day, firstMonth,
                firstDay, lastMonth, lastDay);

    }

    private static final int CALENDAR_UTIL_YEAR_2013        = 2013;
    private static final int CALENDAR_UTIL_DECEMBER_DAY     = 31;
    private static final int CALENDAR_UTIL_DECEMBER_WEEK    = 6;
    private static final int CALENDAR_UTIL_DECEMBER_HOUR    = 24;
    private static final int CALENDAR_UTIL_DECEMBER_MINUTES = 59;
    private static final int CALENDAR_UTIL_DECEMBER_SECOND  = 59;

    private static final int WEEK_PICKER_DATE_LEN           = 8;
    private static final int WEEK_PICKER_YEAR               = 4;
    private static final int WEEK_PICKER_MONTH              = 2;
}
