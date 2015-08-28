package com.yahoo.shopping.epoch.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.fragments.SpotShowFragment;
import com.yahoo.shopping.epoch.models.SpotPlace;

import java.util.List;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotShowViewPagerAdapter extends FragmentPagerAdapter {
    List<SpotPlace> mSpotPlaces;

    public SpotShowViewPagerAdapter(FragmentManager fm, List<SpotPlace> spotPlaces) {
        super(fm);

        mSpotPlaces = spotPlaces;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.ARGUMENTS_SPOT_PLACE, mSpotPlaces.get(position));

        SpotShowFragment fragment = new SpotShowFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return mSpotPlaces.size();
    }
}
