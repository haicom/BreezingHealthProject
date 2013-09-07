package com.breezing.health.entity;

import com.breezing.health.R;

public class ActionItem {
    
    public final static int ACTION_BACK = 0x101;
    public final static int ACTION_DONE = 0x102;
    public final static int ACTION_REFRESH = 0x103;
    public final static int ACTION_TITLE = 0x104;

    private String actionName;
    private int actionId;
    private int actionIconRes;
    
    public ActionItem (int actionType) {
        switch (actionType) {
        case ACTION_BACK:
            this.actionName = "back";
            this.actionId = ACTION_BACK;
            this.actionIconRes = R.drawable.btn_back_selector;
            break;
            
        case ACTION_DONE:
            this.actionName = "done";
            this.actionId = ACTION_DONE;
            this.actionIconRes = R.drawable.btn_submit_selector;
            break;
            
        case ACTION_REFRESH:
            this.actionName = "refresh";
            this.actionId = ACTION_REFRESH;
            this.actionIconRes = R.drawable.btn_refresh_selector;
            break;
            
        case ACTION_TITLE:
            this.actionName = "title";
            this.actionId = ACTION_TITLE;
            this.actionIconRes = 0;
            break;
        }
    }
    
    public ActionItem (int actionId, String actionName, int actionIconRes) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionIconRes = actionIconRes;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionIconRes() {
        return actionIconRes;
    }

    public void setActionIconRes(int actionIconRes) {
        this.actionIconRes = actionIconRes;
    }

}
