package com.breezing.health.ui.fragment;

import com.breezing.health.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BreezingDialogFragment extends BaseDialogFragment  implements OnClickListener {
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    public static BreezingDialogFragment newInstance(String title) {
        BreezingDialogFragment fragment = new BreezingDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    
    
    public void setPositiveClickListener(DialogFragmentInterface.OnClickListener listener) {
        mPositiveClickListener = listener;
    }
    
    public void setNegativeClickListener(DialogFragmentInterface.OnClickListener listener) {
        mNegativeClickListener = listener;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        return new AlertDialog.Builder( getActivity() )       
        .setTitle(title)
        .setPositiveButton(R.string.alert_dialog_ok,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mPositiveClickListener.onClick(BreezingDialogFragment.this);
                    BreezingDialogFragment.this.dismiss();
                }
            }
        )
        .setNegativeButton(R.string.alert_dialog_cancel,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mNegativeClickListener.onClick(BreezingDialogFragment.this);
                    BreezingDialogFragment.this.dismiss();
                }
            }
        ).create();
    }


    @Override
    public void onClick(View v) {
        
        
    }
      


   
    
}

     



