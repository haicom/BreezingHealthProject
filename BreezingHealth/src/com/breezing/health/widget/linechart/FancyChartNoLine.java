package com.breezing.health.widget.linechart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.breezing.health.widget.linechart.data.ChartData;
import com.breezing.health.widget.linechart.data.Point;

public class FancyChartNoLine extends View {
    
	private static int TOUCH_THRESHOLD = 20;
	
	private FancyChartStyle chartStyle;
	private FancyChartPointListener onPointClickListener;
	
	private List<ChartData> chartData;
	
	public FancyChartNoLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.chartData = new ArrayList<ChartData>();
		this.chartStyle = new FancyChartStyle(context);
	}
	
	public void addData(ChartData data) {
		if(data.getXValues().size() == 0 && data.getPoints().size() > 0) {
			data.automaticallyAddXValues();
		}
		
		if(data.getYValues().size() == 0 && data.getPoints().size() > 0) {
			data.automaticallyAddYValues();
		}
		
		chartData.add(data);
	}
	
	public void setOnPointClickListener(FancyChartPointListener onPointClickListener) {
		this.onPointClickListener = onPointClickListener;
	}
	
	public FancyChartStyle getChartStyle() {
		return chartStyle;
	}
	
	public List<ChartData> getChartData() {
		return chartData;
	}
	
	public ChartData getLastChartData() {
		return chartData.get(chartData.size()-1);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		calculateCanvasCoordinates();
		Paint backgroundColorPaint = new Paint();
		backgroundColorPaint.setColor(chartStyle.getPointColor());
		backgroundColorPaint.setStyle(Paint.Style.FILL);
		backgroundColorPaint.setAntiAlias(true);
		
		Paint paintLine = new Paint();
		paintLine.setStyle(Paint.Style.FILL);
		paintLine.setStrokeWidth(chartStyle.getDataLineWidth());
		paintLine.setAntiAlias(true);
		
		for(ChartData data : chartData) {
			Paint paintSelectedBorder = new Paint();
			paintSelectedBorder.setColor(data.getBelowLineColor());
			paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
			paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
			paintSelectedBorder.setAntiAlias(true);
			
			paintLine.setColor(data.getLineColor());
			List<Point> points = data.getPoints();
			
			drawLinesBetweenPoints(points, canvas, paintLine);
			
			for(Point point : points) {
				if(point.isSelected) {
					// Draw selected point
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()+chartStyle.getPointStrokeWidth(), paintSelectedBorder);
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
					
					drawPopupBox(canvas, point, data);
				} else {
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius(), paintLine);
					canvas.drawCircle(point.canvasX, point.canvasY, chartStyle.getPointRadius()-chartStyle.getPointStrokeWidth(), backgroundColorPaint);
				}
			}
		}
	}

	private void drawPopupBox(Canvas canvas, Point point, ChartData data) {
		String title = point.title;
		String subtitle = point.subtitle;
		if(title == null || subtitle == null) {
			return;
		}
		
		Paint paintSelectedBorder = new Paint();
		paintSelectedBorder.setColor(data.getBelowLineColor());
		paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
		paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
		paintSelectedBorder.setAntiAlias(true);
		
		int maxTextRowLength = Math.max(title.length(), subtitle.length());
		if (maxTextRowLength <= 3) {
		    maxTextRowLength = 4;
		}
		
		// TODO: Crashes on smaller text lengths
		int rWidth = (int) (maxTextRowLength* chartStyle.getBoxTextSize());
		int rHeight = (int) (chartStyle.getBoxTextSize()*3);
		
		RectF borderRectangle = new RectF(point.canvasX-(rWidth/2), point.canvasY-(rHeight/2)-chartStyle.getPointRadius()*6, point.canvasX+(rWidth/2), point.canvasY+(rHeight/2)-chartStyle.getPointRadius()*6);
		canvas.drawRoundRect(borderRectangle, 5, 5, paintSelectedBorder);
		
		Paint rectangle = new Paint();
		rectangle.setColor(data.getPopupBackgroundColor());
		rectangle.setStyle(Paint.Style.FILL);
		rectangle.setAntiAlias(true);
		
		RectF rInside = new RectF(borderRectangle.left+chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.top+chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.right-chartStyle.getSelectedBoxStrokeWidth(), borderRectangle.bottom-chartStyle.getSelectedBoxStrokeWidth());
		canvas.drawRoundRect(rInside, 5, 5, rectangle);
		
		drawTextInsideBox(canvas, title, subtitle, rInside);
		drawTriangleAtBottom(canvas, borderRectangle, rectangle, data);
	}

	private void drawTriangleAtBottom(Canvas canvas, RectF borderRectangle, Paint rectangle, ChartData data) {
		Paint paintSelectedBorder = new Paint();
		paintSelectedBorder.setColor(data.getBelowLineColor());
		paintSelectedBorder.setStrokeWidth(chartStyle.getSelectedBoxStrokeWidth());
		paintSelectedBorder.setStyle(Paint.Style.FILL_AND_STROKE);
		paintSelectedBorder.setAntiAlias(true);
		
		int triangleSideSize = 10;
		
		float centerX = (borderRectangle.right-borderRectangle.left)/2+borderRectangle.left;
		float bottomY = (borderRectangle.bottom);
		
		Path triangleBorder = new Path();
		triangleBorder.moveTo(centerX-triangleSideSize, bottomY);
		triangleBorder.lineTo(centerX+triangleSideSize, bottomY);
		triangleBorder.lineTo(centerX, bottomY+triangleSideSize);
		triangleBorder.lineTo(centerX-triangleSideSize, bottomY);
		triangleBorder.close();
		canvas.drawPath(triangleBorder, paintSelectedBorder);
		
		int triangleSideSizeSmall = 10-chartStyle.getSelectedBoxStrokeWidth()+1;
		float centerXSmall = (borderRectangle.right-borderRectangle.left)/2+borderRectangle.left;
		float bottomYSmall = (borderRectangle.bottom)-chartStyle.getSelectedBoxStrokeWidth();
		
		Path triangle = new Path();
		triangle.moveTo(centerXSmall-triangleSideSizeSmall, bottomYSmall);
		triangle.lineTo(centerXSmall+triangleSideSizeSmall, bottomYSmall);
		triangle.lineTo(centerXSmall, bottomYSmall+triangleSideSizeSmall);
		triangle.lineTo(centerXSmall-triangleSideSizeSmall, bottomYSmall);
		triangle.close();
		canvas.drawPath(triangle, rectangle);
	}

	private void drawTextInsideBox(Canvas canvas, String title, String subtitle, RectF rInside) {
		Paint textPaint = new Paint();
		textPaint.setColor(chartStyle.getBoxTextColor());
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(chartStyle.getBoxTextSize());
		
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(title, rInside.left + 7, rInside.top + chartStyle.getBoxTextSize(), textPaint);
		textPaint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(subtitle, rInside.left + 7, rInside.top + chartStyle.getBoxTextSize() * 2, textPaint);
	}

	private void drawLinesBetweenPoints(List<Point> points, Canvas canvas, Paint paintLine) {
		for(int i = 0; i < points.size()-1; i++) {
			Point startPoint = points.get(i);
			Point endPoint = points.get(i+1);
			
			canvas.drawLine(startPoint.canvasX, startPoint.canvasY, endPoint.canvasX, endPoint.canvasY, paintLine);
		}
	}

	private void calculateCanvasCoordinates() {
		int minCanvasX = chartStyle.getChartPaddingLeft()/2;
		int maxCanvasX = getWidth()-chartStyle.getChartPaddingRight()/2;
		
		int minCanvasY = chartStyle.getChartPaddingTop()/2;
		int maxCanvasY = getHeight()-chartStyle.getChartPaddingBottom()/2;
		
		for(ChartData data : chartData) {
			List<Point> points = data.getPoints();
			
			for(Point point : points) {
				int newXValue = transformTo(getMinX(), getMaxX(), minCanvasX, maxCanvasX, point.x);
				int newYValue = (maxCanvasY+chartStyle.getChartPaddingBottom())-transformTo(getMinY(), getMaxY(), minCanvasY, maxCanvasY, point.y);
				
				point.canvasX = newXValue;
				point.canvasY = newYValue;
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			float x = event.getX();
			float y = event.getY();
			
			boolean selected = false;
			for(ChartData data : chartData) {
				List<Point> points = data.getPoints();
				
				for(Point point : points) {
					int minX = point.canvasX-TOUCH_THRESHOLD;
					int maxX = point.canvasX+TOUCH_THRESHOLD;
					
					int minY = point.canvasY-TOUCH_THRESHOLD;
					int maxY = point.canvasY+TOUCH_THRESHOLD;
					
					if((x > minX && x < maxX) && (y > minY && y < maxY)) {
						// Point pressed!
						if(point.isSelected) {
							point.isSelected = false;
						} else if(selected == false) {
							selected = true;
							point.isSelected = true;
							
							if(onPointClickListener != null) {
								onPointClickListener.onClick(point);
							}
						}
						
						invalidate();
					} else {
						point.isSelected = false;
					}
				}
			}
		}
		
		return true;
	}
	
	private double getMinX() {
		double minX = -1;
		
		for(ChartData data : chartData) {
			if(minX == -1 || Double.compare(minX, data.getMinX()) < 0) {
				minX = data.getMinX();
			}
		}
		
		return minX == -1 ? 0 : minX;
	}
	
	private double getMaxX() {
		double maxX = 0;
		
		for(ChartData data : chartData) {
			if(Double.compare(data.getMaxX(), maxX) > 0) {
				maxX = data.getMaxX();
			}
		}
		
		return maxX;
	}
	
	private double getMinY() {
		double minY = -1;
		
		for(ChartData data : chartData) {
			if(minY == -1 || Double.compare(minY, data.getMinY()) < 0) {
				minY = data.getMinY();
			}
		}
		
		return minY == -1 ? 0 : minY;
	}
	
	private double getMaxY() {
		double maxY = 0;
		
		for(ChartData data : chartData) {
			if(Double.compare(data.getMaxY(), maxY) > 0) {
				maxY = data.getMaxY();
			}
		}
		
		return maxY;
	}
	
	private int transformTo(double oldMin, double oldMax, int newMin, int newMax, double oldValue) {
		double oldRange = (oldMax - oldMin);
		double newRange = (newMax - newMin);
		double newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
		
		return (int) newValue;
	}
	
	@Override
	public boolean isInEditMode() {
		return true;
	}

	public void clearValues() {
		chartData.clear();
	}

}
