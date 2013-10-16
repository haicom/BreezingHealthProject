package com.breezing.health.widget;

import android.util.Log;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.widget.linechart.data.ChartData;
import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.chart.renderer.category.StandardBarPainter;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import com.breezing.health.R;

import android.content.Context;
import android.graphics.Color;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * BalanceBarChartView
 */
public class BalanceBarChartView extends BarChartBaseView {
    
    /**
     * constructor
     * @param context
     */
    public BalanceBarChartView(Context context) {
        super(context);

        CategoryDataset dataset = createDataset()   ;
        AFreeChart chart = createChart(context, dataset);

        setChart(chart);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset() {

        // row keys...
        String series1 = "First";
        String series2 = "Second";
        // column keys...
        String category1 = "1Category 1";
        String category2 = "2Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";
        String category6 = "Category 6";
        String category7 = "Category 7";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1100.0, series1, category1);
        dataset.addValue(1400.0, series1, category2);
        dataset.addValue(1300.0, series1, category3);
        dataset.addValue(1500.0, series1, category4);
        dataset.addValue(1500.0, series1, category5);
        dataset.addValue(-1500.0, series1, category6);
        dataset.addValue(-1700.0, series1, category7);

        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static AFreeChart createChart(Context context, CategoryDataset dataset) {

        // create the chart...
        AFreeChart chart = ChartFactory.createBarChart(
            context.getString(R.string.caloric_balance_history),      // chart title
            context.getString(R.string.date),               // domain axis label
            context.getString(R.string.balance),                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            false,                     // include legend
            false,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaintType(new SolidColor(Color.TRANSPARENT));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setAutoPopulateSeriesFillPaint(false);
        renderer.setAutoPopulateSeriesShape(false);
        renderer.setAutoPopulateSeriesStroke(false);
        renderer.setAutoPopulateSeriesPaint(false);
        renderer.setBarPainter(new StandardBarPainter());


        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
        GradientColor gp1 = new GradientColor(Color.GREEN, Color.rgb(0, 64, 0));
        
        renderer.setSeriesPaintType(0, gp0);
        renderer.setSeriesPaintType(1, gp1);
//        renderer.setSeriesPaintType(0, new SolidColor(Color.BLUE));
//        renderer.setSeriesPaintType(1, new SolidColor(Color.GREEN));
//        renderer.setSeriesPaintType(2, new SolidColor(Color.RED));

        return chart;

    }

   
//    private ChartData drawIntakeChartDataWeekly( ChartData chartData ) {
//
//        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);
//
//
//        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
//        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);
//
//        HashMap<Integer, Integer> hashMap =
//                new HashMap<Integer, Integer>();
//
//        Log.d(TAG, " drawChartDataWeekly yearWeek = " + yearWeek
//                + " mYear = " + mYear + " mWeek = " + mWeek);
//
//        int index = 0;
//
//        do {
//
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.setLength(0);
//
//            int month = fistCalendar.get(Calendar.MONTH) + 1;
//            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
//            int date = Integer.valueOf(
//                    CalendarUtil.getDayFromCalendar( fistCalendar ) );
//
//            stringBuilder.append( String.valueOf(month) );
//            stringBuilder.append(".");
//            stringBuilder.append( String.valueOf(day) );
//            chartData.addXValue( index, stringBuilder.toString() );
//            hashMap.put(date, index);
//
//            fistCalendar.add(GregorianCalendar.DATE, 1);
//            Log.d(TAG, " drawChartDataWeekly index = " + index);
//            index++;
//        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
//
//        chartData = fillInTotalIngestionInWeek( yearWeek, chartData, hashMap );
//        return chartData;
//    }

}
