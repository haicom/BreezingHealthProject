/*
 * 	   Created by Daniel Nadeau
 * 	   daniel.nadeau01@gmail.com
 * 	   danielnadeau.blogspot.com
 *
 * 	   Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.breezing.health.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class PieGraph extends View {
    private final static String TAG = "PieGraph";
    
	private ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
	private final Paint paint = new Paint();
	private final Path path = new Path();

	private int indexSelected = -1;
	private int thickness = 50;
	private OnSliceClickedListener listener;
	private float mPadding = 0;


	public PieGraph(Context context) {
		super(context);
	}
	public PieGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		paint.reset();
		paint.setAntiAlias(true);
		float midX, midY, radius, innerRadius;
		path.reset();

		float currentAngle = 270;
        float currentSweep;
        int totalValue = 0;
		//float padding = 2;

		midX = getWidth()/2;
		midY = getHeight()/2;
		
		if (midX < midY){
			radius = midX;
		} else {
			radius = midY;
		}
		radius -= mPadding;
		innerRadius = radius - thickness;

		for (PieSlice slice : slices){
			totalValue += slice.getValue();
		}
        
	//	Log.d(TAG, "onDraw mPadding  = " + mPadding + " totalValue = " + totalValue);
		
		int count = 0;
		for (PieSlice slice : slices){
			Path p = new Path();
			paint.setColor(slice.getColor());
			currentSweep = (slice.getValue()/totalValue)*(360);
			p.arcTo(new RectF(midX-radius, midY-radius, midX+radius, midY+radius), currentAngle + mPadding, currentSweep - mPadding);
			p.arcTo(new RectF(midX-innerRadius, midY-innerRadius, midX+innerRadius, midY+innerRadius ), ( currentAngle + mPadding ) + ( currentSweep - mPadding ), -( currentSweep - mPadding ) );
			p.close();
            
			//Log.d(TAG, " onDraw currentAngle = " + currentAngle + " mPadding = " + mPadding + " midX = " + midX + " midY = " + midY);
			slice.setPath(p);
			slice.setRegion(new Region((int)(midX-radius), (int)(midY-radius), (int)(midX+radius), (int)(midY+radius)));
			canvas.drawPath(p, paint);

			if (indexSelected == count && listener != null){
				path.reset();
				paint.setColor(slice.getColor());
				paint.setColor(Color.parseColor("#33B5E5"));
				paint.setAlpha(100);

				if (slices.size() > 1) {
					path.arcTo( new RectF(midX-radius-(mPadding*2), midY-radius-(mPadding*2), midX+radius+(mPadding*2), midY+radius+(mPadding*2)), currentAngle, currentSweep+mPadding);
					path.arcTo( new RectF(midX-innerRadius+(mPadding*2), midY-innerRadius+(mPadding*2), midX+innerRadius-(mPadding*2), midY+innerRadius-(mPadding*2)), currentAngle + currentSweep + mPadding, -(currentSweep + mPadding));
					path.close();
				} else {
					path.addCircle(midX, midY, radius + mPadding, Direction.CW);
				}

				canvas.drawPath(path, paint);
				paint.setAlpha(255);
			}

			currentAngle = currentAngle + currentSweep;

			count++;
		}


	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

	    Point point = new Point();
	    point.x = (int) event.getX();
	    point.y = (int) event.getY();

	    int count = 0;
	    for (PieSlice slice : slices){
	    	Region r = new Region();
	    	r.setPath(slice.getPath(), slice.getRegion());
            if (r.contains(point.x, point.y) && event.getAction() == MotionEvent.ACTION_DOWN) {
                indexSelected = count;
	    	} else if (event.getAction() == MotionEvent.ACTION_UP){
                if (r.contains(point.x, point.y) && listener != null) {
                    if (indexSelected > -1){
		    			listener.onClick(indexSelected);
	    			}
	    			indexSelected = -1;
	    		}

	    	}
		    count++;
	    }

	    if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
	    	postInvalidate();
	    }



	    return true;
	}

	public ArrayList<PieSlice> getSlices() {
		return slices;
	}
	
	public void setSlices(ArrayList<PieSlice> slices) {
		this.slices = slices;
		postInvalidate();
	}
	
	public PieSlice getSlice(int index) {
		return slices.get(index);
	}
	
	public void addSlice(PieSlice slice) {
		this.slices.add(slice);
		postInvalidate();
	}
	
	public void setPadding(float padding) {
	    mPadding = padding;
	}
	public void setOnSliceClickedListener(OnSliceClickedListener listener) {
		this.listener = listener;
	}

	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
		postInvalidate();
	}

	public void removeSlices(){
		for (int i = slices.size()-1; i >= 0; i--){
			slices.remove(i);
		}
		postInvalidate();
	}

	public static interface OnSliceClickedListener {
		public abstract void onClick(int index);
	}

}
