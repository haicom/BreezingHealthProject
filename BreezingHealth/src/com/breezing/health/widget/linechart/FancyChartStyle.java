package com.breezing.health.widget.linechart;

import com.breezing.health.tools.Tools;

import android.content.Context;
import android.graphics.Color;

public class FancyChartStyle {

	private boolean drawBackgroundBelowLine;
	
	private int pointColor;
	
	private int horizontalGridColor;
	private int verticalGridColor;
	
	private int xAxisLegendColor;
	private int yAxisLegendColor;
	
	private int boxColor;

	private int pointRadius;
	private int pointStrokeWidth;
	
	private int legendTextSize;
	private int boxTextSize;
	private int boxTextColor;
	
	private int dataLineWidth;
	private int gridLineWidth;
	private int selectedBoxStrokeWidth;
	
	private int chartPaddingLeft;
	private int chartPaddingRight;
	private int chartPaddingTop;
	private int chartPaddingBottom;
	
	public FancyChartStyle(Context context) {
		pointColor = Color.parseColor("#efefeb");
		
		horizontalGridColor = Color.parseColor("#ddddda");
		verticalGridColor = horizontalGridColor;
		
		xAxisLegendColor = Color.parseColor("#372f2b");
		yAxisLegendColor = xAxisLegendColor;
		
		boxColor = Color.parseColor("#f3f8fc");
		
		boxTextColor = Color.parseColor("#372f2b");

		pointColor = Color.parseColor("#ffffff");
		
		pointRadius = Tools.dip2px(context, 6);
		pointStrokeWidth = 3;
		
		legendTextSize = Tools.dip2px(context, 12);
		boxTextSize = Tools.dip2px(context, 12);
		
		dataLineWidth = 5;
		gridLineWidth = 1;
		selectedBoxStrokeWidth = Tools.dip2px(context, 3);
		
		chartPaddingLeft = Tools.dip2px(context, 30);
		chartPaddingRight = Tools.dip2px(context, 30);
		chartPaddingTop = Tools.dip2px(context, 20);
		chartPaddingBottom = Tools.dip2px(context, 20);
		
		drawBackgroundBelowLine = true;
	}
	
	public boolean drawBackgroundBelowLine() {
		return drawBackgroundBelowLine;
	}
	
	public void setDrawBackgroundBelowLine(boolean drawBackgroundBelowLine) {
		this.drawBackgroundBelowLine = drawBackgroundBelowLine;
	}
	
	public void setBoxTextColor(int boxTextColor) {
		this.boxTextColor = boxTextColor;
	}
	
	public int getBoxTextColor() {
		return boxTextColor;
	}
	
	public int getBoxTextSize() {
		return boxTextSize;
	}
	
	public void setBoxTextSize(int boxTextSize) {
		this.boxTextSize = boxTextSize;
	}
	
	public int getPointStrokeWidth() {
		return pointStrokeWidth;
	}
	
	public void setPointStrokeWidth(int pointStrokeWidth) {
		this.pointStrokeWidth = pointStrokeWidth;
	}
	
	public int getChartPaddingBottom() {
		return chartPaddingBottom;
	}
	
	public int getChartPaddingLeft() {
		return chartPaddingLeft;
	}
	
	public int getChartPaddingRight() {
		return chartPaddingRight;
	}
	
	public int getChartPaddingTop() {
		return chartPaddingTop;
	}
	
	public int getSelectedBoxStrokeWidth() {
		return selectedBoxStrokeWidth;
	}
	
	public void setSelectedBoxStrokeWidth(int selectedBoxStrokeWidth) {
		this.selectedBoxStrokeWidth = selectedBoxStrokeWidth;
	}
	
	public int getGridLineWidth() {
		return gridLineWidth;
	}
	
	public void setGridLineWidth(int gridLineWidth) {
		this.gridLineWidth = gridLineWidth;
	}
	
	public int getDataLineWidth() {
		return dataLineWidth;
	}
	
	public void setDataLineWidth(int dataLineWidth) {
		this.dataLineWidth = dataLineWidth;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.pointColor = backgroundColor;
	}

	public void setHorizontalGridColor(int horizontalGridColor) {
		this.horizontalGridColor = horizontalGridColor;
	}

	public void setVerticalGridColor(int verticalGridColor) {
		this.verticalGridColor = verticalGridColor;
	}

	public void setxAxisLegendColor(int xAxisLegendColor) {
		this.xAxisLegendColor = xAxisLegendColor;
	}

	public void setyAxisLegendColor(int yAxisLegendColor) {
		this.yAxisLegendColor = yAxisLegendColor;
	}

	public void setBoxColor(int boxColor) {
		this.boxColor = boxColor;
	}

	public void setLegendTextSize(int legendTextSize) {
		this.legendTextSize = legendTextSize;
	}

	public void setPointRadius(int pointRadius) {
		this.pointRadius = pointRadius;
	}

	public int getPointRadius() {
		return pointRadius;
	}

	public int getLegendTextSize() {
		return legendTextSize;
	}
	
	public int getHorizontalGridColor() {
		return horizontalGridColor;
	}

	public int getVerticalGridColor() {
		return verticalGridColor;
	}

	public int getxAxisLegendColor() {
		return xAxisLegendColor;
	}

	public int getyAxisLegendColor() {
		return yAxisLegendColor;
	}

	public int getBoxColor() {
		return boxColor;
	}
	
	public int getPointColor() {
		return pointColor;
	}
	
}
