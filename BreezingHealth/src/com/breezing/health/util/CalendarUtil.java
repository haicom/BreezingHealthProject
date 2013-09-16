package com.breezing.health.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.text.format.DateFormat;

public class CalendarUtil {

    private static final int YEAR2013 = 2008;
    
    private static final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    private static final Calendar todayCalendar = Calendar.getInstance();

    public static String getMonthNameFromLocale(int month){
        if(month < 0 || month > 11)
            throw new IndexOutOfBoundsException("Invalid index, value must be between 0 and 11");

        return dateFormatSymbols.getMonths()[month].toUpperCase();
    }

    public static String getShortWeekdayName(int day){
        if(day < 1 || day > 7)
            throw new IndexOutOfBoundsException("Invalid index, value must be between 1 and 7");
        String dayName = dateFormatSymbols.getShortWeekdays()[day];
        return dayName.replace(dayName.charAt(0), Character.toUpperCase(dayName.charAt(0)));
    }


    public static boolean isTodayInCalendar(Calendar calendar){
        return todayCalendar.get(Calendar.DAY_OF_MONTH) ==   calendar.get(Calendar.DAY_OF_MONTH)
                && todayCalendar.get(Calendar.MONTH) ==   calendar.get(Calendar.MONTH)
                && todayCalendar.get(Calendar.YEAR) ==   calendar.get(Calendar.YEAR);
    }

    public static boolean isWeekendDayInCalendar(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_WEEK) ==   Calendar.SATURDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) ==   Calendar.SUNDAY;
    }

    public static boolean isDateBeforeToday(Calendar calendar){
        Calendar validationCalendar = Calendar.getInstance();
        validationCalendar.setTime(calendar.getTime());
        validationCalendar.set(Calendar.MILLISECOND, todayCalendar.get(Calendar.MILLISECOND));
        validationCalendar.set(Calendar.SECOND, todayCalendar.get(Calendar.SECOND));
        validationCalendar.set(Calendar.MINUTE, todayCalendar.get(Calendar.MINUTE));
        validationCalendar.set(Calendar.HOUR_OF_DAY, todayCalendar.get(Calendar.HOUR_OF_DAY));
        return  validationCalendar.getTime().before(todayCalendar.getTime());
    }

    public static boolean isSameDateInCalendar(Calendar calendar, Date date){
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        return dateCalendar.get(Calendar.DAY_OF_MONTH) ==   calendar.get(Calendar.DAY_OF_MONTH)
                && dateCalendar.get(Calendar.MONTH) ==   calendar.get(Calendar.MONTH)
                && dateCalendar.get(Calendar.YEAR) ==   calendar.get(Calendar.YEAR);
    }

    public static String getLocaleFormatDate(Date date, Context context){
        return DateFormat.getDateFormat(context).format(date);
    }

    public static int getTotalWeeksInYear(int year) {
        Calendar cal = GregorianCalendar.getInstance();
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
     * 获取某年某周的开始日期和结束日期
     * @param year
     * @param week
     * @return
     * @throws Exception
     */
    public static String[] getStartEndOFWeek(int year, int week) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
        calendar.set(GregorianCalendar.YEAR, year);

        Date startDate = calendar.getTime();
        String startDateInStr = format.format(startDate);

        calendar.add(GregorianCalendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDateInStr = format.format(enddate);

        return new String[] {startDateInStr, endDateInStr};
    }
    
    public static String[] getYearsFrom2013() {
        final Calendar calendar = GregorianCalendar.getInstance();
        final int currentYear = calendar.get(GregorianCalendar.YEAR);
        if (currentYear < YEAR2013) {
            return null;
        }
        final int interval = currentYear - YEAR2013;
        String[] years = new String[interval + 1];
        for (int i = YEAR2013; i <= currentYear; i++) {
            years[i - YEAR2013] = String.valueOf(i);
        }
        
        return years;
    }
    
    public static int getTotalWeeksInMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int numOfWeeksInMonth = 1;
        while (c.get(Calendar.MONTH) == month) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                numOfWeeksInMonth++;
            }
        }
        return numOfWeeksInMonth;
    }
    
    public static int getCurrentYear() {
    	final Calendar calendar = GregorianCalendar.getInstance();
        final int currentYear = calendar.get(GregorianCalendar.YEAR);
        return currentYear;
    }
    
    public static int getCurrentMonth() {
    	final Calendar calendar = GregorianCalendar.getInstance();
        final int currentMonth = calendar.get(GregorianCalendar.MONTH) + 1;
        return currentMonth;
    }
    
    public static int getCurrentWeek() {
    	final Calendar calendar = GregorianCalendar.getInstance();
        final int currentWeek = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        return currentWeek;
    }
    
}
