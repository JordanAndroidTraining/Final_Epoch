package com.yahoo.shopping.epoch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.activities.SpotShowActivity;
import com.yahoo.shopping.epoch.adapters.SpotListAdapter;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPlace;

import java.util.ArrayList;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotListFragment extends Fragment{

    private ArrayList<SpotPlace> mSpotPlaces;
    private SpotListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotPlaces = getArguments().getParcelableArrayList(AppConstants.ARGUMENTS_SPOT_PLACES);
        mAdapter = new SpotListAdapter(getActivity(), 0, mSpotPlaces);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot_list, null);

        ListView lvList = (ListView) view.findViewById(R.id.fragment_spot_list_lv_list);
        lvList.setAdapter(mAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SpotShowActivity.class);
                intent.putParcelableArrayListExtra(AppConstants.INTENT_SPOT_PLACES, mSpotPlaces);

                startActivity(intent);
            }
        });

        return view;
    }
}
