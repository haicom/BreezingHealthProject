package com.breezing.health.entity;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public enum LeftMenuFunction {

    SETTINGS(R.string.settings, R.drawable.menu_settings_selector, IntentAction.ACTIVITY_SETTINGS)
    , SHARE(R.string.share, R.drawable.menu_share_selector, IntentAction.ACTIVITY_SHARE)
    , HISTORY(R.string.history, R.drawable.menu_history_selector, IntentAction.ACTIVITY_BALANCE_HISTORY)
    , MORE(R.string.more, R.drawable.menu_more_selector, IntentAction.ACTIVITY_MORE);
    
    private LeftMenuFunction(int titleRes, int iconRes, String intent) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.intent = intent;
    }
    
    public int titleRes;
    public int iconRes;
    public String intent;
    
}
