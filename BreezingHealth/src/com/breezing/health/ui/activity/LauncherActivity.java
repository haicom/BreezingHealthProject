package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public class LauncherActivity extends BaseActivity {

    private final int MSG_AUTO = 110;
    
    private Handler mHandler;
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApplication();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        
        mHandler = new Handler() {
            
            @Override
            public void dispatchMessage(Message msg) {
                // TODO Auto-generated method stub
                
                final int what = msg.what;
                switch(what) {
                case MSG_AUTO:
                    Intent intent = new Intent(IntentAction.ACTIVITY_FILLIN_INFORMATION);
                    startActivity(intent);
                    finish();
                    return ;
                }
                
                super.dispatchMessage(msg);
            }
            
        };
        
        mHandler.sendEmptyMessageDelayed(MSG_AUTO, 3 * 1000);
    }
    
}
