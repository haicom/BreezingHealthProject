package com.breezing.health.entity.enums;

import com.breezing.health.R;

public enum HelperStep {

	STEP_ONE(R.string.helper_step_one_intro, R.drawable.bg_step1, R.drawable.default_step1)
	, STEP_TWO(R.string.helper_step_two_intro, R.drawable.bg_step2, R.drawable.default_step2)
	, STEP_THREE(R.string.helper_step_three_intro, R.drawable.bg_step3, R.drawable.default_step3)
	, STEP_FOUR(R.string.helper_step_four_intro, R.drawable.bg_step4, R.drawable.default_step4);

    private HelperStep(int nameRes, int bgRes, int stepRes) {
        this.nameRes = nameRes;
        this.bgRes = bgRes;
        this.stepRes = stepRes;
    }
    
    public int nameRes;
    public int bgRes;
    public int stepRes;
	
}
