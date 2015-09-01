package com.yahoo.shopping.epoch.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.activities.CommentActivity;
import com.yahoo.shopping.epoch.adapters.SpotShowPhotoGridAdapter;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPhoto;
import com.yahoo.shopping.epoch.models.SpotPlace;
import com.yahoo.shopping.epoch.utils.GoogleImageResult;
import com.yahoo.shopping.epoch.utils.GoogleImageService;

import java.net.URLEncoder;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class SpotShowFragment extends Fragment
        implements View.OnScrollChangeListener, View.OnClickListener /*, GoogleImageService.OnFetchedListener*/ {
    private final int MAX_RADIUS = 25;
    private final int PHOTO_GRID_SPAN_COUNT = 2;

    private int mRadius = 0;

    private Context mContext;
    private SpotPlace mPlace;
    private ViewHolder mVH = new ViewHolder();
    private GoogleImageService mGIS = new GoogleImageService();
    private StaggeredGridLayoutManager mPhotoGridLayoutManager = new StaggeredGridLayoutManager(PHOTO_GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mPlace = getArguments().getParcelable(AppConstants.ARGUMENTS_SPOT_PLACE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot_show, null);

        mVH.svContainer = (ScrollView) view.findViewById(R.id.fragment_spot_show_sv_container);
        mVH.llFavStar = (LinearLayout) view.findViewById(R.id.fragment_spot_show_ll_favstar);
        mVH.ivFavStar1 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar1);
        mVH.ivFavStar2 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar2);
        mVH.ivFavStar3 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar3);
        mVH.ivFavStar4 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar4);
        mVH.ivFavStar5 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar5);
        mVH.tvTitle = (TextView) view.findViewById(R.id.fragment_spot_show_tv_title);
        mVH.tvAddressC = (TextView) view.findViewById(R.id.fragment_spot_show_tv_feature_caption);
        mVH.tvAddress = (TextView) view.findViewById(R.id.fragment_spot_show_tv_address);
        mVH.tvFeatureC = (TextView) view.findViewById(R.id.fragment_spot_show_tv_feature_caption);
        mVH.tvFeature = (TextView) view.findViewById(R.id.fragment_spot_show_tv_feature);
        mVH.tvReminderC = (TextView) view.findViewById(R.id.fragment_spot_show_tv_reminder_caption);
        mVH.tvReminder = (TextView) view.findViewById(R.id.fragment_spot_show_tv_reminder);
        mVH.tvTrafficInfoC = (TextView) view.findViewById(R.id.fragment_spot_show_tv_trafficinfo_caption);
        mVH.tvTrafficInfo = (TextView) view.findViewById(R.id.fragment_spot_show_tv_trafficinfo);
        mVH.ivImage = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_image);
        mVH.ivViewMap = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_viewmap);
        mVH.ivMakeCall = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_makecall);
        mVH.rlToolbar = (LinearLayout) view.findViewById(R.id.fragment_spot_show_rl_toolbar);
        mVH.ivMakeComment = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_makecomment);
        mVH.rvPhotoGrid = (RecyclerView) view.findViewById(R.id.fragment_spot_show_rv_photos);
        mVH.rvPhotoGrid.setLayoutManager(mPhotoGridLayoutManager);

        initToolbar();
        initScrollView();
        initBackgorund();

        fetchPhotoGrid(mPlace.getTitle());

        return view;
    }

    private void setTitleMargin(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = (int) (metrics.heightPixels - (35 + 22 + 7 + 15 + 5) * metrics.density);

        view.requestLayout();
    }

    private void setFavStarRating(int rating) {
        mVH.ivFavStar1.setImageResource(rating >= 1 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        mVH.ivFavStar2.setImageResource(rating >= 2 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        mVH.ivFavStar3.setImageResource(rating >= 3 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        mVH.ivFavStar4.setImageResource(rating >= 4 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        mVH.ivFavStar5.setImageResource(rating >= 5 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
    }

    private void initToolbar() {
        mVH.rlToolbar.bringToFront();

        mVH.ivMakeCall.setTag(mPlace.getPhoneNumber());
        mVH.ivMakeCall.setOnClickListener(this);
        mVH.ivMakeCall.setVisibility(mPlace.getPhoneNumber().length() > 0 ? View.VISIBLE : View.GONE);

        mVH.ivViewMap.setTag(mPlace.getTitle());
        mVH.ivViewMap.setOnClickListener(this);
        mVH.ivViewMap.setVisibility(mPlace.getTitle().length() > 0 ? View.VISIBLE : View.GONE);

        mVH.ivMakeComment.setTag(mPlace.getResourceId());
        mVH.ivMakeComment.setOnClickListener(this);
    }

    private void initScrollView() {
        setTitleMargin(mVH.llFavStar);
        setFavStarRating(mPlace.getRating());

        mVH.tvTitle.setText(mPlace.getTitle());
        mVH.tvAddress.setText(mPlace.getAddress());
        mVH.tvFeature.setText(mPlace.getFeature());
        mVH.tvReminder.setText(mPlace.getReminder());
        mVH.tvTrafficInfo.setText(mPlace.getTrafficInfo());

        mVH.tvAddress.setVisibility(mPlace.getAddress().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvAddressC.setVisibility(mPlace.getAddress().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvFeature.setVisibility(mPlace.getFeature().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvFeatureC.setVisibility(mPlace.getFeature().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvReminder.setVisibility(mPlace.getReminder().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvReminderC.setVisibility(mPlace.getReminder().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvTrafficInfo.setVisibility(mPlace.getTrafficInfo().length() > 0 ? View.VISIBLE : View.GONE);
        mVH.tvTrafficInfoC.setVisibility(mPlace.getTrafficInfo().length() > 0 ? View.VISIBLE : View.GONE);

        mVH.svContainer.setOnScrollChangeListener(this);
    }

    private void initBackgorund() {
        mVH.ivImage.setImageResource(0);
        Picasso.with(mContext).load(mPlace.getImageUrl()).into(mVH.ivImage);
    }

    private void fetchPhotoGrid(String keyword) {
        // fetch Photos
        mGIS.fetchImages(keyword, new GoogleImageService.OnFetchedListener() {
            @Override
            public void onFetched(List<GoogleImageResult> imageResults, int nextPage) {
                List<SpotPhoto> photos = SpotPhoto.fromGoogleImageResults(imageResults);
                SpotShowPhotoGridAdapter adapter = new SpotShowPhotoGridAdapter(photos);
                mVH.rvPhotoGrid.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_spot_show_iv_makecall:
                // shake icon
                Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                mVH.ivMakeCall.startAnimation(animShake);
                // make call
                makePhoneCall((String) v.getTag());
                break;
            case R.id.fragment_spot_show_iv_viewmap:
                // magnify icon
                Animation animMagnify = AnimationUtils.loadAnimation(mContext, R.anim.magnify);
                mVH.ivViewMap.startAnimation(animMagnify);
                launchGoogleMap((String) v.getTag());
                break;
            case R.id.fragment_spot_show_iv_makecomment:
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra(AppConstants.INTENT_COMMENT_RESOURCE_ID, (int) v.getTag());
                //startActivity(intent);
                startActivityForResult(intent,AppConstants.INTENT_COMMENT_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == AppConstants.INTENT_COMMENT_REQUEST_CODE){

                // get SpotPlace data from CommentActivity
                SpotPlace updatedSpot = data.getParcelableExtra(AppConstants.COMMENT_RESULT_EXTRA_KEY);


                // do something if comment activity is finish!

            }
        }
        if(resultCode == getActivity().RESULT_CANCELED){
            // do error handle (no extra data pass back)
        }
    }

    private void makePhoneCall(String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void launchGoogleMap(String address) {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + URLEncoder.encode(address, "UTF-8"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int containerHeight = mVH.svContainer.getHeight();
        // Blur Background
        Drawable draw = mVH.ivImage.getDrawable();
        if (draw != null) {
            // prepare background source bmp
            if (mVH.bmBG == null) {
                mVH.bmBG = ((BitmapDrawable) draw).getBitmap();
            }
            // calculate radius
            int radius = 1 + (MAX_RADIUS * scrollY / containerHeight);
            if (mRadius != radius) {
                mRadius = radius;
                if (radius == 1) {
                    mVH.ivImage.setImageBitmap(mVH.bmBG);
                } else {
                    mVH.ivImage.setImageBitmap(getBlurBitmap(mVH.bmBG, radius));
                }
            }
        }
        // Alpha Toolbar
        int alpha = 100 * scrollY / containerHeight;
        mVH.rlToolbar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    }

    private Bitmap getBlurBitmap(Bitmap bmp, int radius) {
        if (radius < 1) {
            radius = 1;
        }
        if (radius > 25) {
            radius = 25;
        }
        return new BlurTransformation(mContext, radius).transform(bmp.copy(bmp.getConfig(), true));
    }

    private class ViewHolder {
        public ScrollView svContainer;
        public LinearLayout llFavStar;
        public ImageView ivFavStar1;
        public ImageView ivFavStar2;
        public ImageView ivFavStar3;
        public ImageView ivFavStar4;
        public ImageView ivFavStar5;
        public TextView tvTitle;
        public TextView tvAddressC;
        public TextView tvAddress;
        public TextView tvFeatureC;
        public TextView tvFeature;
        public TextView tvReminderC;
        public TextView tvReminder;
        public TextView tvTrafficInfoC;
        public TextView tvTrafficInfo;
        public ImageView ivImage;
        public LinearLayout rlToolbar;
        public ImageView ivViewMap;
        public ImageView ivMakeCall;
        public Bitmap bmBG;
        public ImageView ivMakeComment;
        public RecyclerView rvPhotoGrid;
    }
}
