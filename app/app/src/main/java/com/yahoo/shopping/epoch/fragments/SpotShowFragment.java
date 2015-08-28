package com.yahoo.shopping.epoch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPlace;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotShowFragment extends Fragment {
    private SpotPlace mPlace;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlace = getArguments().getParcelable(AppConstants.ARGUMENTS_SPOT_PLACE);
    }

    private void setTitleMargin(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = (int) (metrics.heightPixels - (35 + 28 + 7) * metrics.density);

        view.requestLayout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot_show, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.fragment_spot_show_tv_title);
        TextView tvAddress = (TextView) view.findViewById(R.id.fragment_spot_show_tv_address);
        TextView tvFeature = (TextView) view.findViewById(R.id.fragment_spot_show_tv_feature);
        TextView tvReminder = (TextView) view.findViewById(R.id.fragment_spot_show_tv_reminder);
        TextView tvTrafficInfo = (TextView) view.findViewById(R.id.fragment_spot_show_tv_trafficinfo);
        ImageView ivImage = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_image);

        setTitleMargin(tvTitle);

        tvTitle.setText(mPlace.getTitle());
        tvAddress.setText(mPlace.getAddress());
        tvFeature.setText(mPlace.getFeature());
        tvReminder.setText(mPlace.getReminder());
        tvTrafficInfo.setText(mPlace.getTrafficInfo());

        Picasso.with(getActivity()).load(mPlace.getImageUrl()).into(ivImage);

        return view;
    }
}
