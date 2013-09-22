package com.breezing.health.entity;

import com.breezing.health.R;

public class ActionItem {
    
    public final static int ACTION_BACK = 0x101;
    public final static int ACTION_DONE = 0x102;
    public final static int ACTION_REFRESH = 0x103;
    public final static int ACTION_TITLE = 0x104;
    public final static int ACTION_MORE = 0X105;
    public final static int ACTION_MENU = 0x106;
    public final static int ACTION_HISTORY = 0x107;
    public final static int ACTION_BREEZING_TEST_HISTORY = 0x108;

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
            
        case ACTION_MORE:
            this.actionName = "more";
            this.actionId = ACTION_MORE;
            this.actionIconRes = R.drawable.ic_action_catagory;
            break;
            
        case ACTION_MENU:
            this.actionName = "menu";
            this.actionId = ACTION_MENU;
            this.actionIconRes = R.drawable.ic_action_menu;
            break;
            
        case ACTION_HISTORY:
            this.actionName = "history";
            this.actionId = ACTION_HISTORY;
            this.actionIconRes = R.drawable.ic_action_chart;
            break;
            
        case ACTION_BREEZING_TEST_HISTORY:
            this.actionName = "test_history";
            this.actionId = ACTION_BREEZING_TEST_HISTORY;
            this.actionIconRes = R.drawable.ic_breezing_test_history;
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
