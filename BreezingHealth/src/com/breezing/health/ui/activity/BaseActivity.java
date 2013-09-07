package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.breezing.health.R;
import com.breezing.health.ui.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

    private LeftMenuFragment mLeftMenuFragment;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (isSlidingMenuEnable()) {
            setBehindContentView(R.layout.menu_frame);
            if (savedInstanceState == null) {
                FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
                mLeftMenuFragment = LeftMenuFragment.newInstance();
                t.replace(R.id.menu_frame, mLeftMenuFragment);
                t.commit();
            } else {
                mLeftMenuFragment = (LeftMenuFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
            }
            
            // customize the SlidingMenu
            SlidingMenu sm = getSlidingMenu();
            sm.setShadowWidthRes(R.dimen.shadow_width);
            sm.setShadowDrawable(R.drawable.drawer_shadow);
            sm.setBehindWidthRes(R.dimen.sliding_menu_width);
            sm.setFadeDegree(0.35f);
            sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        }
    }
    
    public void exitApplication() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    
}
