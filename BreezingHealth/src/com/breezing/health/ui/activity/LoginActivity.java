package com.breezing.health.ui.activity;



import com.breezing.health.R;
import com.breezing.health.adapter.AccountRecordAdapter;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.tools.IntentAction;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener , OnItemSelectedListener{
    private static final String TAG = "LoginActivity";

    private Spinner mSpinner;
    private EditText mEditText;
    private Button mButton;

    private AccountRecordAdapter mAdapter;

    private QueryHandler mQueryHandler;

    private String mName;
    private int mAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_login);

        setActionBarTitle(R.string.account_login);
        mSpinner = (Spinner) findViewById(R.id.login_spinner);
        mEditText = (EditText) findViewById(R.id.login_pass);
        mButton = (Button) findViewById(R.id.login_button);
        mButton.setText(R.string.login);
        mButton.setOnClickListener(this);
        mQueryHandler = new QueryHandler(this);


        mAdapter = new AccountRecordAdapter(this,
                   android.R.layout.simple_spinner_item,
                   null,
                   new String[] {Account.ACCOUNT_NAME },
                   new int[] { android.R.id.text1 }
                   );

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mAdapter.setOnDataSetChangedListener(mDataSetChangedListener);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(0);
        mName = mSpinner.getSelectedItem().toString();
     // Specify the layout to use when the list of choices appears
        mSpinner.setOnItemSelectedListener(this);
        doQueryAccountInfo();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private Cursor queryAcctounInfo() {
        StringBuilder where = new StringBuilder();
        where.append(Account.ACCOUNT_DELETED + " =  ? ");

        String sortOrder = Account.ACCOUNT_NAME + " ASC ";

        Cursor cursor = this.getContentResolver().query(
                Account.CONTENT_URI,
                new String[] { Account._ID, Account.ACCOUNT_NAME },
                where.toString(),
                new String[] { String.valueOf(0) },
                sortOrder);
        return cursor;
    }

    private void  doQueryAccountInfo() {
        // Cancel any pending queries
        mQueryHandler.cancelOperation(LOGIN_QUERY_TOKEN);

        StringBuilder where = new StringBuilder();
        where.append(Account.ACCOUNT_DELETED + " =  ? ");

        String sortOrder = Account.ACCOUNT_NAME + " ASC ";
        mQueryHandler.startQuery(LOGIN_QUERY_TOKEN,
                null,
                Account.CONTENT_URI,
                new String[] { Account._ID, Account.ACCOUNT_NAME },
                where.toString(),
                new String[] { String.valueOf(0) },
                sortOrder);


    }


    /**
     * This is our specialization of AsyncQueryHandler applies new cursors
     * to our state as they become available.
     */
    private final class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(Context context) {
            super(context.getContentResolver());
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            if (!isFinishing()) {
                // Update the adapter: we are no longer loading, and have
                // a new cursor for it.
                mAdapter.changeCursor(cursor);

            } else {
                cursor.close();
            }
        }
    }




    @Override
    public void onClick(View  v) {
        int count = queryAccountInfo(mName, mEditText.getText().toString().trim() );
        if (count == 1 ) {
             Intent intent = new Intent(IntentAction.ACTIVITY_MAIN);
             startActivity(intent);
        } else {
             Toast.makeText(this,
                     R.string.account_info_password, Toast.LENGTH_SHORT).show();
        }

    }


    private final AccountRecordAdapter.OnDataSetChangedListener
        mDataSetChangedListener = new AccountRecordAdapter.OnDataSetChangedListener() {

            @Override
            public void onDataSetChanged(AccountRecordAdapter adapter) {

            }

            @Override
            public void onContentChanged(AccountRecordAdapter adapter) {
                doQueryAccountInfo();
            }
    };

    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private int queryAccountInfo(final String accountName, final String accountPass) {
        int count = 0;
        Log.d(TAG, " queryAccountInfo accountName = " + accountName + " accountPass = " + accountPass);
        StringBuilder where = new StringBuilder();
        where.setLength(0);
        where.append(Account.ACCOUNT_NAME + " = ?  AND ");
        where.append(Account.ACCOUNT_PASSWORD + "= ?");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_ID},
                    where.toString(),
                    new String[] { accountName, accountPass},
                    null);

            if (cursor != null) {
               count = cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryAccountInfo count = " + count);

        return count;
    }


    /** Arbitrary number, doesn't matter since we only do one query type. */
   private  static final int LOGIN_QUERY_TOKEN = 42;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
            int position, long id) {
        mSpinner.setSelection(position);
        mName = mSpinner.getSelectedItem().toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}
