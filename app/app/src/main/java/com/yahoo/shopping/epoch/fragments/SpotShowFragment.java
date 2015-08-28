package com.yahoo.shopping.epoch.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPlace;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotShowFragment extends Fragment implements View.OnScrollChangeListener {

    private final int MAX_RADIUS = 25;
    private int mRadius = 0;

    private Context mContext;
    private SpotPlace mPlace;
    private ViewHolder mVH = new ViewHolder();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
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

        mVH.svContainer = (ScrollView) view.findViewById(R.id.fragment_spot_show_sv_container);
        mVH.tvTitle = (TextView) view.findViewById(R.id.fragment_spot_show_tv_title);
        mVH.tvAddress = (TextView) view.findViewById(R.id.fragment_spot_show_tv_address);
        mVH.tvFeature = (TextView) view.findViewById(R.id.fragment_spot_show_tv_feature);
        mVH.tvReminder = (TextView) view.findViewById(R.id.fragment_spot_show_tv_reminder);
        mVH.tvTrafficInfo = (TextView) view.findViewById(R.id.fragment_spot_show_tv_trafficinfo);
        mVH.ivImage = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_image);

        setTitleMargin(mVH.tvTitle);

        mVH.tvTitle.setText(mPlace.getTitle());
        mVH.tvAddress.setText(mPlace.getAddress());
        mVH.tvFeature.setText(mPlace.getFeature());
        mVH.tvReminder.setText(mPlace.getReminder());
        mVH.tvTrafficInfo.setText(mPlace.getTrafficInfo());

        Picasso.with(mContext).load(mPlace.getImageUrl()).into(mVH.ivImage);

        mVH.svContainer.setOnScrollChangeListener(this);

        return view;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        // prepare background source bmp
        if (mVH.bmBG == null) {
            mVH.bmBG = ((BitmapDrawable)mVH.ivImage.getDrawable()).getBitmap();
        }
        // calculate radius
        int radius = 1 + (MAX_RADIUS * scrollY / mVH.svContainer.getHeight());
        if (mRadius != radius) {
            mRadius = radius;
            mVH.ivImage.setImageBitmap(getBlurBitmap(mVH.bmBG, radius));
        }
    }

    private Bitmap getBlurBitmap(Bitmap bmp, int radius) {
        return new BlurTransformation(mContext, radius).transform(bmp.copy(bmp.getConfig(), true));
    }

    private class ViewHolder {
        public ScrollView svContainer;
        public TextView tvTitle;
        public TextView tvAddress;
        public TextView tvFeature;
        public TextView tvReminder;
        public TextView tvTrafficInfo;
        public ImageView ivImage;
        public Bitmap bmBG;
    }
}
