package com.breezing.health.entity.enums;

import com.breezing.health.R;

public enum ChartModel {
    WEEK(R.string.week_chart_model), MONTH(R.string.month_chart_model), YEAR(R.string.year_chart_model);

    private ChartModel(int nameRes) {
        this.nameRes = nameRes;
    }

    public int nameRes;
}
