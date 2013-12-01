package com.breezing.health.widget;
 
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.breezing.health.R;
 
/**
 * 
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 * 
 * @author antoine chenzhong
 *
 */
public class NoticeDialog extends Dialog {
 
	public NoticeDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public NoticeDialog(Context context) {
        super(context);
    }
 
    protected NoticeDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	LayoutParams params = getWindow().getAttributes();
        params.height = LayoutParams.MATCH_PARENT;
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    
    public void setMessage(int resId) {
    	View message = findViewById(R.id.message);
    	if (message != null && message instanceof TextView) {
    		((TextView)message).setVisibility(View.VISIBLE);
    		((TextView)message).setText(resId);
    	}
    }
    
    public void setMessage(String msg) {
    	View message = findViewById(R.id.message);
    	if (message != null && message instanceof TextView) {
    		((TextView)message).setVisibility(View.VISIBLE);
    		((TextView)message).setText(msg);
    	}
    }

	/**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
 
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private int iconResource;
        private View contentView;
        private boolean cancelable = true;
 
        private DialogInterface.OnClickListener 
                        positiveButtonClickListener,
                        negativeButtonClickListener;
        
        private DialogInterface.OnCancelListener cancelListener;
 
        public Builder(Context context) {
            this.context = context;
        }
 
        /**
         * Set the Dialog message from String
         * @param title
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
 
        /**
         * Set the Dialog message from resource
         * @param title
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }
        
        public Builder setIcon(int iconResource) {
            this.iconResource = iconResource;
            return this;
        }
 
        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }
 
        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
 
        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
 
        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }
        
        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) {
        	this.cancelListener = listener;
        	return this;
        }
 
        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        
        public Builder setCancelable(boolean cancelable) {
        	this.cancelable = cancelable;
        	return this;
        }
 
        /**
         * Create the custom dialog
         */
        public NoticeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final NoticeDialog dialog = new NoticeDialog(context, R.style.NoticeDialogStyle);
            View layout = inflater.inflate(getLayoutRes(), null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.enter))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.enter))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.enter).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.cancel))
                    .setText(negativeButtonText);
                    ((Button) layout.findViewById(R.id.cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.cancel).setVisibility(
                        View.GONE);
            }
            
            if (cancelListener != null) {
            	dialog.setOnCancelListener(cancelListener);
            }
            
            if (title != null) {
            	((TextView) layout.findViewById(
                		R.id.title)).setText(title);
            } else {
            	((TextView) layout.findViewById(
                		R.id.title)).setVisibility(View.GONE);
            }
            
            if (iconResource != 0) {
                ((ImageView) layout.findViewById(
                        R.id.icon)).setImageResource(iconResource);
            }
            
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(
                		R.id.message)).setText(message);
                ((TextView) layout.findViewById(
                		R.id.message)).setMovementMethod(new ScrollingMovementMethod());
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.message))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.message))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT, 
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(cancelable);
            
            return dialog;
        }

		protected int getLayoutRes() {
			return R.layout.dialog_notice;
		}
 
    }
 
}