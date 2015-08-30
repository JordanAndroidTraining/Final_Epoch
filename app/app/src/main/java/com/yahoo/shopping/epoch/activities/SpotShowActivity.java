package com.yahoo.shopping.epoch.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.adapters.SpotShowViewPagerAdapter;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPlace;

import java.util.ArrayList;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotShowActivity extends AppCompatActivity {

    private ArrayList<SpotPlace> mSpotPlaces;
    private int mSelectSpotPlacesIndex;
    private SpotShowViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_show);

        mSpotPlaces = getIntent().getParcelableArrayListExtra(AppConstants.INTENT_SPOT_PLACES);
        mSelectSpotPlacesIndex = getIntent().getIntExtra(AppConstants.INTENT_SPOT_SELECT_PLACES_INDEX,0);
        mAdapter = new SpotShowViewPagerAdapter(getSupportFragmentManager(), mSpotPlaces);

        ViewPager vpViewPager = (ViewPager) findViewById(R.id.activity_spotshow_viewpager);
        vpViewPager.setAdapter(mAdapter);
        vpViewPager.setCurrentItem(mSelectSpotPlacesIndex);
    }
}
