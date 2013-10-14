package com.breezing.health.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.application.SysApplication;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.BreezingDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ImagePickerDialogFragment;
import com.breezing.health.util.InternalStorageContentProvider;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.imagecrop.CropImage;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";

    private UITableView mTableView;

    private StringBuilder mStringBuilder;

    private int mAccountId;

    private BasicItem mNameItem;

    private Button mButton;
    
    private File mTempFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_settings);
        initValues();
        initViews();
        initListeners();
    }

    private void initValues() {
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        mStringBuilder = new StringBuilder();
    }

    private void initViews() {
        setActionBarTitle(R.string.settings);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mTableView = (UITableView) findViewById(R.id.tableView);
        mButton = (Button) findViewById(R.id.button);
        mButton.setText(R.string.quit_current_account);



    }

    @Override
    protected void onResume() {
        super.onResume();
        valueToView();

    }

    private void valueToView() {
        String accountName = queryAccountName();
        mTableView.clear();
        createList();
        mNameItem.setTitle(accountName);
        mTableView.commit();

        Log.d(TAG, "onResume mTableView.getChildCount() =  " + mTableView.getChildCount() );
    }

    private void initListeners() {
        mButton.setOnClickListener(this);

        mTableView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(View view, ViewGroup contentView, String action, GroupIndex index) {
                if (action != null) {
                    if (action.equals(IntentAction.INTENT_IMAGE_CROP)) {
                    	showImagePickerDialog();
                    } else {
                    	Intent intent = new Intent(action);
                        startActivity(intent);
                    }
                    return;
                }
            }
        });
    }
    
    private void showImagePickerDialog() {
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mTempFile = new File(Environment.getExternalStorageDirectory(), InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
    	} else {
    		mTempFile = new File(getFilesDir(), InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
    	}
    	
    	ImagePickerDialogFragment imagePicker = (ImagePickerDialogFragment) getSupportFragmentManager().findFragmentByTag("imagePicker");
        if (imagePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(imagePicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        imagePicker.setFileTemp(mTempFile);
        imagePicker.setTitle(getString(R.string.please_pick_image_resource));
        imagePicker.show(getSupportFragmentManager(), "imagePicker");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        Bitmap bitmap;
        switch (requestCode) {
            case ImagePickerDialogFragment.REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mTempFile);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();
                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                break;
                
            case ImagePickerDialogFragment.REQUEST_CODE_TAKE_PICTURE:
                startCropImage();
                break;
                
            case ImagePickerDialogFragment.REQUEST_CODE_CROP_IMAGE:
            	String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                bitmap = BitmapFactory.decodeFile(mTempFile.getPath());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mTempFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, ImagePickerDialogFragment.REQUEST_CODE_CROP_IMAGE);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * create UITableView items
     */
    private void createList() {
        mNameItem = new BasicItem(R.drawable.user_image,
                    null,
                    null,
                    IntentAction.ACTIVITY_ACCOUNT_DETAIL );
        mTableView.addBasicItem(mNameItem);
        
        BasicItem modifyAvatar = new BasicItem(getString(R.string.modify_avatar),
        		getString(R.string.modify_avatar_summary),
                IntentAction.INTENT_IMAGE_CROP);
        mTableView.addBasicItem(modifyAvatar);

        mTableView.addBasicItem(getString(R.string.modify_password),
                   getString(R.string.modify_password_summary),
                   IntentAction.ACTIVITY_MODIFY_PASSWORD);

        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.account_management),
                   getString(R.string.account_management_summary),
                   IntentAction.ACTIVITY_ACCOUNT_MANAGEMENT);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.unit_settings),
                   getString(R.string.unit_settings_summary),
                   IntentAction.ACTIVITY_UNIT_SETTINGS);
        mTableView.addBasicItem(getString(R.string.bluetooth_device_settings),
                   getString(R.string.bluetooth_device_settings_summary),
                   IntentAction.ACTIVITY_BTDEVICE_SETTINGS);
    }

    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private String queryAccountName( ) {

        String accountName = null;

        mStringBuilder.setLength(0);
        mStringBuilder.append(Account.ACCOUNT_ID + " =? ");

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_NAME},
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId) },
                    null);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    accountName = cursor.getString(0);
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryAccountName accountName = " + accountName);

        return accountName;
    }

    private void showDialog() {
        BreezingDialogFragment dialog = (BreezingDialogFragment) getSupportFragmentManager().findFragmentByTag(ACCOUNT_LOGIN_DIALOG);
        if (dialog != null) {
            getSupportFragmentManager().beginTransaction().remove(dialog);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);


        dialog = BreezingDialogFragment.newInstance( this.getString(R.string.account_quit_confirm) );
        dialog.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                SysApplication.getInstance().exit();
                int account=  LocalSharedPrefsUtil.
                        getSharedPrefsValueInt(dialog.getActivity(),
                        LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
                LocalSharedPrefsUtil.saveSharedPrefsValueInt(dialog.getActivity(),
                        LocalSharedPrefsUtil.PREFS_ACCOUNT_ID,
                        0);
                LocalSharedPrefsUtil.saveSharedPrefsVersion(dialog.getActivity(),
                        String.valueOf(account),
                        String.valueOf(0) );
                LocalSharedPrefsUtil.saveSharedPrefsValueInt(dialog.getActivity(),
                        LocalSharedPrefsUtil.PREFS_USER_LOGIN,
                        LocalSharedPrefsUtil.USER_NEEDFUL_LOGIN);
                Intent intent = new Intent(IntentAction.ACTIVITY_LOGIN);
                dialog.startActivity(intent);

            }
        });

        dialog.show(getSupportFragmentManager(), ACCOUNT_LOGIN_DIALOG);
    }

    @Override
    public void onClick(View v) {
        showDialog();
    }

    private static final String ACCOUNT_LOGIN_DIALOG = "dialog";

}
