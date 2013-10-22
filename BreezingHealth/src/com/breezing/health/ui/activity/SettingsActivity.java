package com.breezing.health.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.application.SysApplication;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.BreezingDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ImagePickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";
    private UITableView mTableView;
    private StringBuilder mStringBuilder;
    private BasicItem mNameItem;
    private Button mButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_settings);
        initValues();
        initViews();
        initListeners();
    }

    private void initValues() {
       
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
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String accountName = queryAccountName(accountId);
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
    private String queryAccountName(int accountId ) {

        String accountName = null;

        mStringBuilder.setLength(0);
        mStringBuilder.append(Account.ACCOUNT_ID + " =? ");

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_NAME},
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
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
