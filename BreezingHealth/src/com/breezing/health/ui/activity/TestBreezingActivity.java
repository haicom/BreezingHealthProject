package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.breezing.health.R;
import com.breezing.health.entity.BreezingTestReport;
import com.breezing.health.util.ExtraName;

public class TestBreezingActivity extends ActionBarActivity implements OnClickListener {

	private EditText mMetabolism;
	private EditText mSport;
	private EditText mDigest;
	
	private Button mConfirm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentFrame(R.layout.activity_test_breezing);
		initValue();
		initView();
		valueToView();
		initListener();
	}

	private void initValue() {
		
	}

	private void initView() {
		setActionBarTitle(R.string.breezing_test);
		
		mMetabolism = (EditText) findViewById(R.id.metabolism);
		mSport = (EditText) findViewById(R.id.sport);
		mDigest = (EditText) findViewById(R.id.digest);
		mConfirm = (Button) findViewById(R.id.confirm);
	}

	private void valueToView() {
		
	}

	private void initListener() {
		mConfirm.setOnClickListener(this);
	}
	
	private boolean checkInputValues() {
		if (mMetabolism.getText().length() == 0) {
			mMetabolism.setError(getString(R.string.error_input_metabolism));
			return false;
		} else if (mSport.getText().length() == 0) {
			mSport.setError(getString(R.string.error_input_sport));
			return false;
		} else if (mDigest.getText().length() == 0) {
			mDigest.setError(getString(R.string.error_input_digest));
			return false;
		}
		
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == mConfirm) {
			final boolean checkResult = checkInputValues();
			if (checkResult) {
				BreezingTestReport report = new BreezingTestReport();
				report.setMetabolism(Integer.parseInt(mMetabolism.getText().toString()));
				report.setSport(Integer.parseInt(mSport.getText().toString()));
				report.setDigest(Integer.parseInt(mDigest.getText().toString()));
				
				getIntent().putExtra(ExtraName.EXTRA_DATA, report);
				setResult(RESULT_OK, getIntent());
				finish();
			}
			return ;
		}
	}
	
}
