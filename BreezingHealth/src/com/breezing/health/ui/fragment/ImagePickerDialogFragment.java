package com.breezing.health.ui.fragment;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.util.BLog;
import com.breezing.health.util.InternalStorageContentProvider;

public class ImagePickerDialogFragment extends BaseDialogFragment implements
		OnClickListener {
	
	public static final String TAG = ImagePickerDialogFragment.class.getName();

	public static final int REQUEST_CODE_GALLERY = 0x101;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x201;
	public static final int REQUEST_CODE_CROP_IMAGE = 0x301;
	public static final int PHOTO_PICKED_WITH_DATA = 0x401;

	private View mFragmentView;
	private Button mTakeAPhoto;
	private Button mPickFromGallery;

	private TextView mTitle;
	private Button mCancel;
    private Button mConfirm;

	private String mTitleString;

	public static ImagePickerDialogFragment newInstance() {
		ImagePickerDialogFragment fragment = new ImagePickerDialogFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mFragmentView = inflater.inflate(R.layout.fragment_dialog_image_picker,
				null);
		mTakeAPhoto = (Button) mFragmentView.findViewById(R.id.camera);
		mPickFromGallery = (Button) mFragmentView.findViewById(R.id.gallery);
		mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
		mCancel.setVisibility(View.INVISIBLE);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        mConfirm.setVisibility(View.INVISIBLE);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        
        if (mTitleString != null) {
            mTitle.setText(mTitleString);
        }

		mTakeAPhoto.setOnClickListener(this);
		mPickFromGallery.setOnClickListener(this);
		
		getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));

		return mFragmentView;
	}

	@Override
	public void onClick(View v) {
		if (v == mTakeAPhoto) {
			takePicture();
			return;
		} else if (v == mPickFromGallery) {
			openGallery();
			return;
		}
	}
	
	public void setTitle(String titleString) {
        mTitleString = titleString;
    }

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
//			intent.putExtra("return-data", true);
//			getActivity().startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
//			
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            intent.putExtra("return-data", true);
            getActivity().startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
			
		} catch (ActivityNotFoundException e) {
			BLog.d(TAG, "cannot take picture", e);
		}
		
		dismiss();
	}

	private void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		photoPickerIntent.putExtra("return-data", true);
		getActivity().startActivityForResult(photoPickerIntent, PHOTO_PICKED_WITH_DATA);
		dismiss();
	}

}
