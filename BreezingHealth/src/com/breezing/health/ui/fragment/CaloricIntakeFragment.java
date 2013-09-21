package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.AddCaloricRecordAdapter;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.ui.activity.MainActivity;
import com.breezing.health.widget.PieGraph;
import com.breezing.health.widget.PieSlice;

public class CaloricIntakeFragment extends BaseFragment {  
    private static final String TAG = "CaloricIntakeFragment";
    
    private static CaloricIntakeFragment mCaloricIntakeFragment;
    
    private View mFragmentView;
    private GridView mGridView;
    private PieGraph mPieGraph;
    private TextView mUptakeCaloric;
    private AddCaloricRecordAdapter mAdapter;

    public static CaloricIntakeFragment newInstance() {
        CaloricIntakeFragment fragment = new CaloricIntakeFragment();
        return fragment;
    }

    public static CaloricIntakeFragment getInstance(int num) {
        if (mCaloricIntakeFragment == null) {
            mCaloricIntakeFragment = new CaloricIntakeFragment();
            Bundle args = new Bundle();
            args.putInt(CaloricPagerAdapter.MAIN_INTERFACE_SAVE_NUM, num);
            mCaloricIntakeFragment.setArguments(args);
        }
        return mCaloricIntakeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       
        Log.d(TAG, " onCreateView ");
        mFragmentView = inflater.inflate(R.layout.fragment_caloric_intake, null);     
        
     
        mGridView = (GridView) mFragmentView.findViewById(R.id.gridView);
        mPieGraph = (PieGraph) mFragmentView.findViewById(R.id.pie_graph);
        mUptakeCaloric = (TextView)mFragmentView.findViewById(R.id.uptake_caloric);

//        ArrayList<PiePartEntity> pieParts = new ArrayList<PiePartEntity>();
//        pieParts.add(new PiePartEntity(10.0f, R.color.black));
//        pieParts.add(new PiePartEntity(20.0f, R.color.orange));
//        pieParts.add(new PiePartEntity(30.0f, R.color.red));
//        pieParts.add(new PiePartEntity(40.0f, R.color.gray));
//
//        try {
//            mPieChart.setAdapter(pieParts);
//            mPieChart.setClickable(false);
//            mPieChart.setOnSelectedListener(new OnSelectedLisenter() {
//                @Override
//                public void onSelected(int iSelectedIndex) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<RecordFunctionEntity> funs = new ArrayList<RecordFunctionEntity>();
//        funs.add(new RecordFunctionEntity(R.string.breakfast, 30, 100, R.color.orange));
//        funs.add(new RecordFunctionEntity(R.string.lunch, 55, 100, R.color.orange));
//        funs.add(new RecordFunctionEntity(R.string.dinner, 15, 100, R.color.orange));
//        funs.add(new RecordFunctionEntity(R.string.other, 15, 100, R.color.orange));
//        mAdapter = new AddCaloricRecordAdapter(getActivity(), funs);
//        mGridView.setAdapter(mAdapter);
       Bundle bundel = getArguments();
       
       int accountId = bundel.getInt(MainActivity.MAIN_ACCOUNT_ID);
       int date = bundel.getInt(MainActivity.MAIN_DATE);
       
       drawPieChar(accountId, date);
       
       return mFragmentView;
    }
    
    private static final String[] PROJECTION_INGESTION = new String[] {
        Ingestion.BREAKFAST,    // 1
        Ingestion.LUNCH,        // 2
        Ingestion.DINNER,       // 3
        Ingestion.ETC,          // 4
        Ingestion.TOTAL_INGESTION // 5
    };

    private final static int INGESTION_BREAKFAST_INDEX = 0;
    private final static int INGESTION_LUNCH_INDEX = 1;
    private final static int INGESTION_DINNER_INDEX = 2;
    private final static int INGESTION_ETC_INDEX = 3;
    private final static int INGESTION_TOTAL_ENERGY_INDEX = 4;
 


    public void drawPieChar(int accountId, int date) {
        
        int count = 0;        
        int breakfast = 0;
        int lunch = 0;
        int dinner = 0;
        int etc = 0;
        int totalIngestion = 0;
        
        String sortOrder = Ingestion.DATE + " DESC";

        Log.d(TAG, " drawPieChar  accountId = " + accountId + " date = " + date);
        
        if ( date == 0 ) {
            return;
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(Ingestion.DATE + " = ?  ");
        
        Cursor cursor = null;
        
        try {
            cursor = getActivity().getContentResolver().query(Ingestion.CONTENT_URI,
                    PROJECTION_INGESTION,
                    stringBuilder.toString(),
                    new String[] { String.valueOf( accountId ),  String.valueOf( date ) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    breakfast = cursor.getInt(INGESTION_BREAKFAST_INDEX);
                    lunch = cursor.getInt(INGESTION_LUNCH_INDEX);
                    dinner = cursor.getInt(INGESTION_DINNER_INDEX);
                    etc = cursor.getInt(INGESTION_ETC_INDEX);
                    totalIngestion =  cursor.getInt(INGESTION_TOTAL_ENERGY_INDEX);                    
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " drawPieChar totalIngestion = " + totalIngestion + " breakfast = " + breakfast
                + " lunch = " + lunch + " dinner = " + dinner + " etc = " + etc);
        
        mPieGraph.removeSlices();
        mPieGraph.setPadding(2);
        PieSlice slice;
        

        if (breakfast != 0) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.red_brown) );
            slice.setValue(breakfast);
            mPieGraph.addSlice(slice);


        }

        if (lunch != 0 ) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.brown_yellow) );
            slice.setValue(lunch);
            mPieGraph.addSlice(slice);
        }
        
        if ( dinner != 0 ) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.orange_yellow) );
            slice.setValue(dinner);
            mPieGraph.addSlice(slice);
        }
        
        if ( etc != 0 ) {
            slice = new PieSlice();
            slice.setColor( getActivity().getResources().getColor(R.color.pink_yellow) );
            slice.setValue(etc);
            mPieGraph.addSlice(slice);
        }

        if (   ( breakfast == 0 )
               && ( lunch == 0 )
               && ( dinner == 0 )
               && ( etc == 0 ) )  {
            slice = new PieSlice();
            slice.setColor(getActivity().getResources().getColor(R.color.pale_gray) );
            slice.setValue(1);
            mPieGraph.addSlice(slice);
        }

        ArrayList<RecordFunctionEntity> funs = new ArrayList<RecordFunctionEntity>();

        funs.add(new RecordFunctionEntity(R.string.breakfast,
                breakfast, totalIngestion ,
                getActivity().getResources().getColor(R.color.red_brown), R.drawable.ic_breakfast ) );

        funs.add(new RecordFunctionEntity(R.string.lunch,
                lunch, totalIngestion,
                getActivity().getResources().getColor(R.color.brown_yellow), R.drawable.ic_lunch ) );

        funs.add(new RecordFunctionEntity(R.string.dinner,
                dinner, totalIngestion ,
                getActivity().getResources().getColor(R.color.orange_yellow), R.drawable.ic_dinner ) );
        
        funs.add(new RecordFunctionEntity(R.string.other,
                etc, totalIngestion ,
                getActivity().getResources().getColor(R.color.pink_yellow), R.drawable.ic_other ) );

        mAdapter = new AddCaloricRecordAdapter(getActivity(), funs);
        mGridView.setAdapter(mAdapter);
        mUptakeCaloric.setText(String.valueOf(totalIngestion));
    }

}
