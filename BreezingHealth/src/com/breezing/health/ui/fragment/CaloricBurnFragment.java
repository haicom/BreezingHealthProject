package com.breezing.health.ui.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.AddCaloricRecordAdapter;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.activity.MainActivity;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.PieGraph;
import com.breezing.health.widget.PieSlice;

public class CaloricBurnFragment extends BaseFragment implements OnItemClickListener{
    private static final String TAG = "CaloricBurnFragment";

    private View mFragmentView;

    private GridView mGridView;
    private PieGraph mPieGraph;
    private TextView mBurnCaloric;
    private TextView mMetabolism;
    private AddCaloricRecordAdapter mAdapter;
    private static CaloricBurnFragment mCaloricBurnFragment;
    private int mAccountId;
    private int mDate;
    
    private float mUnifyUnit;
    private String mCaloricUnit;
    
    private AccountEntity mAccount;

    public static CaloricBurnFragment newInstance() {
        CaloricBurnFragment fragment = new CaloricBurnFragment();
        return fragment;
    }

    public static CaloricBurnFragment getInstance() {

        if (mCaloricBurnFragment == null) {
            mCaloricBurnFragment = new CaloricBurnFragment();
        }

        return mCaloricBurnFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = 0;
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        Log.d(TAG, "onCreateView");
        
        mFragmentView = inflater.inflate(R.layout.fragment_caloric_burn, null);       
        mGridView = (GridView) mFragmentView.findViewById(R.id.gridView);
        mPieGraph = (PieGraph) mFragmentView.findViewById(R.id.pie_graph);
        mBurnCaloric = (TextView)mFragmentView.findViewById(R.id.burn_caloric);
        mMetabolism = (TextView)mFragmentView.findViewById(R.id.metabolism);
      


        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Bundle bundel = this.getArguments();
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this.getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(this.getActivity());
        mAccount = query.queryBaseInfoViews(accountId);
        mUnifyUnit = query.queryUnitObtainData( this.getString(R.string.caloric_type), mAccount.getCaloricUnit() );        
       // mDate = bundel.getInt(MainActivity.MAIN_DATE);
        mDate = ( (MainActivity)this.getActivity() ).getDate();
        mCaloricUnit = mAccount.getCaloricUnit();
        mMetabolism.setText(mCaloricUnit);
        Log.d(TAG, "onResume accountId = " + accountId + " mDate = " + mDate);
        if ( ( accountId !=0 ) || ( mDate != 0 ) ) {
            drawPieChar(accountId, mDate);
        }
        
    }

    private static final String[] PROJECTION_ENERGY_COST = new String[] {
        EnergyCost.METABOLISM,      // 1
        EnergyCost.SPORT,    // 2
        EnergyCost.DIGEST,   //3
        EnergyCost.TRAIN ,   //4
        EnergyCost.TOTAL_ENERGY,   //5
        EnergyCost.ENERGY_COST_DATE   //6
    };

    private final static int ENERGY_COST_METABOLISM_INDEX = 0;
    private final static int ENERGY_COST_SPORT_INDEX = 1;
    private final static int ENERGY_COST_DIGEST_INDEX = 2;
    private final static int ENERGY_COST_TRAIN_INDEX = 3;
    private final static int ENERGY_COST_TOTAL_ENERGY_INDEX = 4;
    private final static int ENERGY_COST_ENERGY_COST_DATE_INDEX = 5;


    public void drawPieChar(int accountId, int date) {
        int count = 0;
        float metabolism = 0;
        float sport = 0;
        float digest = 0;
        float train = 0;
        float totalEnergy = 0;
        int energyDate = 0;

        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        Log.d(TAG, " drawPieChar  accountId = " + accountId + " date = " + date);
        
        if ( date == 0 ) {
            return;
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ?  ");
        
        Cursor cursor = null;
        
        try {
            cursor = getActivity().getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId),  String.valueOf(date) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    metabolism = cursor.getFloat(ENERGY_COST_METABOLISM_INDEX) * mUnifyUnit;
                    sport = cursor.getFloat(ENERGY_COST_SPORT_INDEX) * mUnifyUnit;
                    digest = cursor.getFloat(ENERGY_COST_DIGEST_INDEX) * mUnifyUnit;
                    train = cursor.getFloat(ENERGY_COST_TRAIN_INDEX) * mUnifyUnit;
                    totalEnergy =  cursor.getFloat(ENERGY_COST_TOTAL_ENERGY_INDEX) * mUnifyUnit;
                    energyDate = cursor.getInt(ENERGY_COST_ENERGY_COST_DATE_INDEX);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryEnergyCostEveryDay count = " + count + " metabolism = " + metabolism
                + " sport = " + sport + " digest = " + digest + " train = " + train);
        mPieGraph.removeSlices();
        mPieGraph.setPadding(2);
        PieSlice slice;

        if (metabolism != 0) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.brun_green) );
            slice.setValue(metabolism);
            mPieGraph.addSlice(slice);


        }

        if (train != 0 ) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.pale_green) );
            slice.setValue(train);
            mPieGraph.addSlice(slice);
        }

        if ( ( sport + digest) != 0 ) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.pale_yellow) );
            slice.setValue(sport + digest);
            mPieGraph.addSlice(slice);
        }

        if (   ( metabolism == 0 )
               && ( sport == 0 )
               && ( digest == 0 )
               && ( train == 0 ) )  {
            slice = new PieSlice();
            slice.setColor(getActivity().getResources().getColor(R.color.pale_gray) );
            slice.setValue(1);
            mPieGraph.addSlice(slice);
        }

        ArrayList<RecordFunctionEntity> funs = new ArrayList<RecordFunctionEntity>();

        funs.add(new RecordFunctionEntity(R.string.energy_metabolism,
                metabolism, totalEnergy ,
                getActivity().getResources().getColor(R.color.brun_green), R.drawable.ic_metabolize ) );

        funs.add(new RecordFunctionEntity(R.string.exercise,
                train, totalEnergy,
                getActivity().getResources().getColor(R.color.pale_green), R.drawable.ic_sport ) );

        funs.add(new RecordFunctionEntity(R.string.other,
                sport + digest, totalEnergy ,
                getActivity().getResources().getColor(R.color.pale_yellow), R.drawable.ic_other ) );

        mAdapter = new AddCaloricRecordAdapter(getActivity(), funs);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        DecimalFormat weightFormat = new DecimalFormat("#0.0");
        mBurnCaloric.setText( weightFormat.format(totalEnergy) );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecordFunctionEntity recordFunction =  (RecordFunctionEntity) mAdapter.getItem(position);       
        mDate = ( (MainActivity)this.getActivity() ).getDate();
        
        Log.d(TAG, "onItemClick view = " + view + " position =  " + position + " mDate = " + mDate);
        if ( recordFunction.getTitleRes() == R.string.exercise ) {           
            Intent intent = new Intent(IntentAction.ACTIVITY_EXERCISE_RECORD);
            intent.putExtra(ExtraName.EXTRA_DATE, mDate);
            CaloricBurnFragment.this.startActivity(intent); 
        } else if (recordFunction.getTitleRes() == R.string.other) {
            Intent intent = new Intent(IntentAction.ACTIVITY_OTHER_CALORIC_BURN);
            intent.putExtra(ExtraName.EXTRA_ACCOUNT_ID, ( (MainActivity)getActivity() ).getAccountId());
            intent.putExtra(ExtraName.EXTRA_DATE, mDate);
            CaloricBurnFragment.this.startActivity(intent);
        } else if (recordFunction.getTitleRes() == R.string.energy_metabolism) {
            Intent intent = new Intent(IntentAction.ACTIVITY_BREEZING_TEST);
            intent.putExtra(ExtraName.EXTRA_DATE, true);
            CaloricBurnFragment.this.startActivity(intent);
        }
        
    }

}
