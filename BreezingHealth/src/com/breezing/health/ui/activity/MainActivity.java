package com.breezing.health.ui.activity;


import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.activity.CaloricHistoryActivity.CaloricHistoryType;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CalendarDialogFragment;
import com.breezing.health.ui.fragment.CaloricBurnFragment;
import com.breezing.health.ui.fragment.CaloricIntakeFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ImagePickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.imagecrop.ImageUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends ActionBarActivity implements OnClickListener {
    private final static String TAG = "MainActivity";

    private ViewPager mViewPager;
    private Button mWeight;
    private Button mCalendar;
    private ContentResolver mContentResolver;
    private CaloricPagerAdapter mCaloricPagerAdapter;
    private LinePageIndicator mLinePageIndicator;

    private int mDate;
    private int mAccountId;
    private CaloricHistoryType mPosition = CaloricHistoryType.BURN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setSlidingMenuEnable(true);
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_main);
        initViews();        
        initListeners();
        initValues();
        valueToView();
       
    }

    @Override
    protected void onResume() {
        super.onResume();
        String weightString  = getBaseInfoViews(mAccountId);
        mWeight.setText( weightString );
       
        
    }

    private void initValues() {        
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        mDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        String dateString = DateFormatUtil.getCurrentDateString(this, mDate);
        mCalendar.setText(dateString);
        mContentResolver = getContentResolver();
        

    }

    private void initViews() {
        addRightActionItem( new ActionItem(ActionItem.ACTION_HISTORY) );
        addLeftActionItem( new ActionItem(ActionItem.ACTION_MENU) );
        mWeight = (Button) findViewById(R.id.weight);
        mCalendar = (Button) findViewById(R.id.calendar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mLinePageIndicator = (LinePageIndicator) findViewById(R.id.indicator);
    }

    private void valueToView() {
        setActionBarTitle(R.string.app_name);
        mCaloricPagerAdapter = new CaloricPagerAdapter( getSupportFragmentManager() );
        mViewPager.setAdapter(mCaloricPagerAdapter);
        
        CaloricIntakeFragment caloricIntakeFragment  = (CaloricIntakeFragment) mCaloricPagerAdapter.
                getItem(CaloricHistoryType.INTAKE.ordinal());
        
        CaloricBurnFragment caloricBurnFragment  = (CaloricBurnFragment) mCaloricPagerAdapter.
                getItem(CaloricHistoryType.BURN.ordinal());
        
        Bundle IntakeBundle = caloricIntakeFragment.getArguments();
        if (IntakeBundle == null) {
            IntakeBundle = new Bundle();
        }
       
        IntakeBundle.putInt(MAIN_ACCOUNT_ID, mAccountId);
        IntakeBundle.putInt(MAIN_DATE, mDate);
        caloricIntakeFragment.setArguments(IntakeBundle);
        
        
        Bundle burnBundle = caloricBurnFragment.getArguments();
        if (burnBundle == null) {
            burnBundle = new Bundle();
        }
        
        burnBundle.putInt(MAIN_ACCOUNT_ID, mAccountId);
        burnBundle.putInt(MAIN_DATE, mDate);      
        caloricBurnFragment.setArguments(burnBundle);
        mLinePageIndicator.setViewPager(mViewPager);
    }

    private void initListeners() {

        getSlidingMenu().setOnClosedListener(new OnClosedListener() {
            @Override
            public void onClosed() {

            }
        });

        getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {

            @Override
            public void onOpened() {


            }
        });

        mWeight.setOnClickListener(this);
        mCalendar.setOnClickListener(this);

        mLinePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                BLog.v(TAG, "ViewPager position =" + position);
                mPosition = CaloricHistoryType.values()[position];
                mLinePageIndicator.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void drawPiePicture() {
        CaloricBurnFragment caloricBurnFragment  = (CaloricBurnFragment) mCaloricPagerAdapter.
                getItem(CaloricHistoryType.BURN.ordinal());
        caloricBurnFragment.drawPieChar( mAccountId, mDate );
        
        CaloricIntakeFragment caloricIntakeFragment  = (CaloricIntakeFragment) mCaloricPagerAdapter.
                getItem(CaloricHistoryType.INTAKE.ordinal());
        caloricIntakeFragment.drawPieChar( mAccountId, mDate );
    }

    public int getAccountId() {
        return mAccountId;
    }

    public int getDate() {
        return mDate;
    }

    @Override
    public void onClick(View v) {        
        if ( v == mCalendar ) {
            showCalendar();
            return ;
        } else if (v == mWeight) {
            Intent intent = new Intent(IntentAction.ACTIVITY_WEIGHT_RECORD);
            intent.putExtra(MAIN_DATE, mDate);
            startActivity(intent);
            return ;
        }
    }

    private void showCalendar() {
        CalendarDialogFragment calendar = (CalendarDialogFragment) getSupportFragmentManager().
                                           findFragmentByTag("calendar");
//        if (calendar != null) {
//            getSupportFragmentManager().beginTransaction().remove(calendar);
//        }
//        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        calendar = CalendarDialogFragment.getInstance();
        calendar.setTitle( getString(R.string.title_select_date) );
        calendar.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                Log.d(TAG, "showCalendar params[0] = " + params[0] + " params[1] = " + params[1] + " params[2] =  " + params[2] );
                int date = DateFormatUtil.getCompleteDate(
                        Integer.parseInt( String.valueOf( params[0] ) ),
                        Integer.parseInt( String.valueOf( params[1] ) ),
                        Integer.parseInt( String.valueOf( params[2] ) ) );

                mDate = date;
                String dateString = DateFormatUtil.getCurrentDateString(dialog.getActivity(), date);
                Log.d(TAG, "showCalendar dateString = " + dateString + " mDate = " + mDate);
                mCalendar.setText(dateString);
                drawPiePicture();
            }

        });

        calendar.show(getSupportFragmentManager(), "calendar");
    }




    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Information.WEIGHT_UNIT ,     // 1
        WeightChange.EVERY_WEIGHT           // 2

    };

    private static final int INFO_WEIGHT_UNIT_INDEX = 0;
    private static final int INFO_WEIGHT_INDEX = 1;


    public String getBaseInfoViews(int accountId) {
        Log.d(TAG, "queryBaseInfoView");
        String weightUnit = null;
        float  weight = 0;
        float  unit = 1;

        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        String[] args = new String[] { String.valueOf(accountId) };

        Cursor cursor  = mContentResolver.query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);

        if (cursor == null) {
            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
        }


        try {
            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    weightUnit = cursor.getString(INFO_WEIGHT_UNIT_INDEX);
                    weight = cursor.getFloat(INFO_WEIGHT_INDEX);
                }
            }
        } finally {
            cursor.close();
        }
        
        
        //获得需要换算的值
        cursor  = mContentResolver.query( UnitSettings.CONTENT_URI,
                new String[] { UnitSettings.UNIT_OBTAIN_DATA }, 
                UnitSettings.UNIT_TYPE + " =  ? ", 
                new String[] { weightUnit }, 
                null);
        
        if (cursor == null) {
            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
        }


        try {
            
            if (cursor != null) {                
                if ( cursor.getCount() > 0 ) {                    
                    cursor.moveToPosition(0);
                    unit = cursor.getFloat(0);                    
                }                
            }
        } finally {
            cursor.close();
        }
        
        BLog.d(TAG, " getBaseInfoViews unit = " + unit);
        
        DecimalFormat df = new DecimalFormat("#.0");
        String str = df.format(weight * unit);
        Log.d(TAG, "getBaseInfoViews str = " + str);
        return getResources().getString(R.string.breezing_weight, str, weightUnit);
    }

    @Override
    public void onClickActionBarItems(ActionItem item, View v) {

        switch( item.getActionId() ) {
            case ActionItem.ACTION_HISTORY: {
                BLog.v(TAG, "caloric history position =" + mPosition.ordinal());
                Intent intent = new Intent(IntentAction.ACTIVITY_CALORIC_HISTORY);
                intent.putExtra(ExtraName.EXTRA_TYPE, mPosition.ordinal() );              
                startActivity(intent);
                return ;
            }
                
            case ActionItem.ACTION_MENU: {
                toggle();
                return ;
            }
        }
        super.onClickActionBarItems(item, v);
    }
    
    public void showImagePickerDialog() {
        ImagePickerDialogFragment imagePicker = (ImagePickerDialogFragment) getSupportFragmentManager().findFragmentByTag("imagePicker");
        if (imagePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(imagePicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        imagePicker = ImagePickerDialogFragment.newInstance();
        imagePicker.setTitle(getString(R.string.please_pick_image_resource));
        imagePicker.show(getSupportFragmentManager(), "imagePicker");
    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        BLog.d(TAG, "onActivityResult requestCode = " + requestCode + " data = " + data + " resultCode = " + resultCode);
        switch (requestCode) {
        	case ImagePickerDialogFragment.PHOTO_PICKED_WITH_DATA: {
        		Uri uri = data.getData();
        		BLog.v(TAG, "IMAGE DATA URI = " + uri.toString());
    			Intent intent = new Intent(this, com.breezing.health.widget.imagecrop.CropImage.class);
    			Bundle extras = new Bundle();
    			extras.putString("circleCrop", "true");
    			extras.putInt("aspectX", 200);
    			extras.putInt("aspectY", 200);
    			intent.setDataAndType(uri, "image/jpeg");
    			intent.putExtras(extras);
    			startActivityForResult(intent, ImagePickerDialogFragment.REQUEST_CODE_CROP_IMAGE);
    			break;
        	}
        	
        	case ImagePickerDialogFragment.REQUEST_CODE_CROP_IMAGE: {
        		final String srcData = data.getExtras().getString("data-src");
        		updateAvatar(srcData);
				break;
        	}
        	
        	case ImagePickerDialogFragment.REQUEST_CODE_TAKE_PICTURE: {
        	    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        	    Uri uri = Uri.fromFile(f);
                BLog.v(TAG, "IMAGE DATA URI = " + uri.toString());
                Intent intent = new Intent(this, com.breezing.health.widget.imagecrop.CropImage.class);
                Bundle extras = new Bundle();
                extras.putString("circleCrop", "true");
                extras.putInt("aspectX", 200);
                extras.putInt("aspectY", 200);
                intent.setDataAndType(uri, "image/jpeg");
                intent.putExtras(extras);
                startActivityForResult(intent, ImagePickerDialogFragment.REQUEST_CODE_CROP_IMAGE);
                break;
        	}
        	
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void updateAvatar(String srcData) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Information.ACCOUNT_ID + " = ? ");
     
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        final int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        ops.add(ContentProviderOperation.newUpdate(Information.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(accountId) } )
                .withValue(Information.ACCOUNT_PICTURE, srcData)
                .build());
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.modify_avatar_failure), Toast.LENGTH_LONG).show();
            return ;
        }
        
        Toast.makeText(this, getString(R.string.modify_avatar_success), Toast.LENGTH_LONG).show();
    }
    
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		// for define to close avoid warning leak.
		// AlertDialog ImageDialog;
		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			final Bitmap bitmap = ImageUtil.decodeBitmapFromFile(params[0], 300, 300);
			return bitmap;

		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {

				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}
    
    public static final String MAIN_ACCOUNT_ID = "account_id";
    public static final String MAIN_DATE = "date";
    public static final String MAIN_UNIFY_UNIT = "unify_unit";
    public static final String MAIN_CALORIC_UNIT = "caloric_unit";
    
}
