package com.breezing.health.widget;



import java.util.ArrayList;
import java.util.List;

import com.breezing.health.util.Formatter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class BarChart extends View {
    private static final String TAG = "BarChart";
	private List<Double> mDataY;
	private List<String> mDataX;

	private Paint mPaint;
	private Paint mStrokePaint;
	private Paint mAxisPaint;
	private TextPaint mTextPaint;
	
	private int mLeftOffset;
	private int mBarSpace;
	
	private static final int BAR_UP_COLOR = Color.parseColor("#ea4200");
	private static final int BAR_DOWN_COLOR = Color.parseColor("#53b20b");
	private static final int BAR_TEXT_COLOR = Color.parseColor("#372f2b");
	private static final int BAR_AXIS_COLOR = Color.parseColor("#ddddda");

	public BarChart(Context context) {
		super(context);
		init();
	}

	public BarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();		
	}

	private void init() {
		mLeftOffset = 60;
		mBarSpace = 10;


		mDataX = new ArrayList<String>();
		mDataY = new ArrayList<Double>();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		//mPaint.setColor(Color.parseColor("#0089bb"));

		mStrokePaint = new Paint();
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStyle(Style.STROKE);
		mStrokePaint.setStrokeWidth(1);
		mStrokePaint.setColor(BAR_TEXT_COLOR);

		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextAlign(Align.RIGHT);
		mTextPaint.setTextSize(16);
		mTextPaint.setColor(BAR_TEXT_COLOR);

		mAxisPaint = new Paint();
		mAxisPaint.setAntiAlias(true);
		mAxisPaint.setStyle(Style.STROKE);
		mAxisPaint.setStrokeWidth(1);
		mAxisPaint.setColor(BAR_AXIS_COLOR);

	}
	
	private String formatValues(double value) {
		double range = getMaxValue() - getMinValue();
		int pos = 0;
		if(range < 1) {
			pos = 3;
		} else if(range < 10) {
			pos = 2;
		} else {
			pos = 1;
		}
		String formattedValue = String.valueOf( Formatter.round(value, pos) );
		return formattedValue;
	}

	protected double getMaxValue() {
		double max = Double.MIN_VALUE;
		for (Double v : mDataY) {
			if (Math.abs(v) > max) {
				max = Math.abs(v);
			}
		}
		return Math.abs(max);
	}

	protected double getMinValue() {
	    double max = Double.MIN_VALUE;
        for (Double v : mDataY) {
            if ( Math.abs(v) > max ) {
                max = Math.abs(v);
            }
        }
        return -Math.abs(max);
	}

	public void addData(String x, Double y) {
		mDataX.add(x);
		mDataY.add(y);
	}
	
	public void clearDate() {
	    mDataX.clear();
	    mDataY.clear();
	}

	private boolean isEmpty() {
		return mDataX.isEmpty() || mDataY.isEmpty();
	}

	private int getSize() {
		return mDataY.size();
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    
		if (isEmpty()) {
			return;
		}

		int bottomOffset = 25;
		float height = getHeight();
		float width = getWidth();
		float barMaxHeight = height - (bottomOffset);
		float barWidth = (width - mLeftOffset) / getSize();		
		double min = getMinValue();
		double max = getMaxValue();
		double range = max - min;
		min = min - range/10;
		max = max + range/10;
		range = max - min;
		
		mTextPaint.setTextAlign(Align.LEFT);
		
		Log.d(TAG, "onDraw  min = " + min + " max = " + max + " range ＝ "  + range);
		if ( verifyYvalue() == true  ) {
            for (int i = 0; i < 9; i++) {
                double value = (range / 8) * i + min;
                float yPos = barMaxHeight - (barMaxHeight / 8) * i;
                canvas.drawLine(mLeftOffset - 10, yPos, width, yPos, mAxisPaint);
                if (yPos < 1) {
                    yPos = 30;
                }
                canvas.drawText(formatValues(value), 0, yPos, mTextPaint);
            }
        }

        canvas.drawLine(mLeftOffset, barMaxHeight, mLeftOffset, 0, mAxisPaint);
        canvas.drawLine(mLeftOffset, barMaxHeight, width, barMaxHeight,
                mAxisPaint);

		mTextPaint.setTextAlign(Align.RIGHT);
		
		
        for (int i = 0; i < getSize(); i++) {
            
            double value = mDataY.get(i);
            String label = mDataX.get(i);
            float barHeight = (float) (barMaxHeight * ( (value - min) / range) );
            float left = i * barWidth + mLeftOffset;
            RectF rect = null;
            Log.d(TAG, " onDraw i =  " + i + " value =  " + value + " label =  " + label
                    + " barMaxHeight = " + barMaxHeight + " barHeight = " + barHeight);
            if (value > 0) {
                rect = new RectF(left, barMaxHeight - barHeight, left
                        + barWidth - mBarSpace, barMaxHeight / 2);
                mPaint.setColor(BAR_UP_COLOR);
            } else {
                float top = barMaxHeight - barHeight;
                float right = left + barWidth - mBarSpace;
                
                Log.d(TAG, "onDraw i = " + i + " left = " + left + " top = " + top + 
                        " right = " + right + " bottom = " + barMaxHeight / 2);
//              rect = new RectF(left, barMaxHeight - barHeight, left
//                        + barWidth - mBarSpace, barMaxHeight / 2);
                rect = new RectF(left, barMaxHeight / 2, left
              + barWidth - mBarSpace, barMaxHeight - barHeight);
                mPaint.setColor(BAR_DOWN_COLOR);
            }
            
            Log.d(TAG, "onDraw rect = " + rect.toString() );
            canvas.drawLine(left + barWidth / 2, barMaxHeight, left + barWidth / 2, 0, mAxisPaint);
            canvas.drawRect(rect, mPaint);
            float textX = left + barWidth;
            float textY = barMaxHeight + 20;
            canvas.save();

            canvas.drawText(label, textX, textY, mTextPaint);
            canvas.restore();
        }
	}
	
	private boolean verifyYvalue() {
	    boolean result = false;
	    for (Double v : mDataY) {
	        Log.d(TAG, "verifyYvalue v = " + v);
            if (v != 0 ) {
                result = true;
                break;
            }
        }
	    
	    return result;
	}

}
