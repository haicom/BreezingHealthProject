package com.breezing.health.entity;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public enum LeftMenuFunction {

    SETTINGS(R.string.settings, R.drawable.ic_launcher, IntentAction.ACTIVITY_SETTINGS)
    , SHARE(R.string.share, R.drawable.ic_launcher, IntentAction.ACTIVITY_SHARE)
    , HISTORY(R.string.history, R.drawable.ic_launcher, IntentAction.ACTIVITY_HISTORY)
    , MORE(R.string.more, R.drawable.ic_launcher, IntentAction.ACTIVITY_MORE);
    
    private LeftMenuFunction(int titleRes, int iconRes, String intent) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.intent = intent;
    }
    
    public int titleRes;
    public int iconRes;
    public String intent;
    
    public static LeftMenuFunction[] createLeftMenu() {
        return new LeftMenuFunction[] {SETTINGS, SHARE, HISTORY, MORE};
    }
    
}
