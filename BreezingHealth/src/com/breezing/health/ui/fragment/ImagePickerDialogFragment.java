package com.breezing.health.ui.fragment;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

	private View mFragmentView;
	private Button mTakeAPhoto;
	private Button mPickFromGallery;

	private TextView mTitle;
	private Button mCancel;
    private Button mConfirm;

	private File mFileTemp;
	
	private String mTitleString;

	public static ImagePickerDialogFragment newInstance() {
		ImagePickerDialogFragment fragment = new ImagePickerDialogFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		return super.onCreateView(inflater, container, savedInstanceState);
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
	
	public void setFileTemp(File fileTemp) {
		mFileTemp = fileTemp;
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			} else {
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
			BLog.d(TAG, "cannot take picture", e);
		}
	}

	private void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

}
