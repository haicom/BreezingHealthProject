package com.breezing.health.entity.enums;

import com.breezing.health.R;

public enum TestStep {

    SCAN_QR(R.string.scan_qr_step)
    , INSERT_GAS_MOUTH(R.string.insert_gas_mouth_step)
    , BEGIN_BREEZING(R.string.begin_breezing_step)
    , INSERT_FORMAL_CHIP(R.string.inser_formal_chip_step);

    private TestStep(int nameRes) {
        this.nameRes = nameRes;
    }
    
    public int nameRes;
    
}
