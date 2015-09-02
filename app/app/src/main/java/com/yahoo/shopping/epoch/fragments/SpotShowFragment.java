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
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
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
import com.yahoo.shopping.epoch.activities.PhotoDisplayActivity;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.Comment;
import com.yahoo.shopping.epoch.models.SpotPhoto;
import com.yahoo.shopping.epoch.models.SpotPlace;
import com.yahoo.shopping.epoch.utils.GoogleImageResult;
import com.yahoo.shopping.epoch.utils.GoogleImageService;

import java.net.URLEncoder;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class SpotShowFragment extends Fragment implements View.OnScrollChangeListener, View.OnClickListener {
    private final int MAX_RADIUS = 25;
    private int mRadius = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private SpotPlace mPlace;
    private ViewHolder mVH = new ViewHolder();
    private GoogleImageService mGIS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mPlace = getArguments().getParcelable(AppConstants.ARGUMENTS_SPOT_PLACE);
        mGIS = new GoogleImageService(getActivity(), true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_spot_show, null);

        mVH.svContainer = (ScrollView) view.findViewById(R.id.fragment_spot_show_sv_container);
        mVH.llFavStar = (LinearLayout) view.findViewById(R.id.fragment_spot_show_ll_rating);
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
        mVH.ivMakeComment2 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_makecomment2);
        mVH.tvPhotosC = (TextView) view.findViewById(R.id.fragment_spot_show_tv_photos_caption);
        mVH.rlPhotos = (PercentRelativeLayout) view.findViewById(R.id.fragment_spot_show_rl_photos);
        mVH.llComments = (LinearLayout) view.findViewById(R.id.fragment_spot_show_ll_comments);

        initToolbar();
        initScrollView();
        initBackgorund();

        fetchPhotoGrid(mPlace.getTitle());

        initComments();

        return view;
    }

    private void setTitleMargin(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = (int) (metrics.heightPixels - (35 + 22 + 7 + 15 + 5) * metrics.density);

        view.requestLayout();
    }

    private void setFavStarRating(View view, int rating) {
        ImageView fs1 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar1);
        ImageView fs2 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar2);
        ImageView fs3 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar3);
        ImageView fs4 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar4);
        ImageView fs5 = (ImageView) view.findViewById(R.id.fragment_spot_show_iv_favstar5);
        fs1.setImageResource(rating >= 1 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        fs2.setImageResource(rating >= 2 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        fs3.setImageResource(rating >= 3 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        fs4.setImageResource(rating >= 4 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        fs5.setImageResource(rating >= 5 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
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

        mVH.ivMakeComment2.setTag(mPlace.getResourceId());
        mVH.ivMakeComment2.setOnClickListener(this);
    }

    private void initScrollView() {
        setTitleMargin(mVH.llFavStar);
        setFavStarRating(mVH.llFavStar, mPlace.getRating());

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

    private void initComments() {
        List<Comment> comments = mPlace.getCommentList();
        if (comments.size() == 0) {
            TextView tvTips = new TextView(mContext);
            tvTips.setTextColor(Color.DKGRAY);
            tvTips.setText("(尚無留言)");
            mVH.llComments.addView(tvTips);
            return;
        }
        mVH.llComments.removeAllViews();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            View llComment = mInflater.inflate(R.layout.fragment_spot_show_comment, null);
            TextView tvComment = (TextView) llComment.findViewById(R.id.fragment_spot_show_ll_comments_content);
            setFavStarRating(llComment, comment.getRating());
            tvComment.setText(comment.getComment());
            llComment.setBackgroundResource(android.R.color.transparent);
            mVH.llComments.addView(llComment);
        }
    }

    private void renderPhotoGrid(List<SpotPhoto> photos) {
        // loop to create photo grid
        int ids = 99;
        for (int i = 0; i < photos.size(); i++) {
            SpotPhoto photo = photos.get(i);
            // create ImageView
            ImageView img = new ImageView(mContext);
            img.setId(++ids);
            img.setAdjustViewBounds(true);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // prepare layout params
            PercentRelativeLayout.LayoutParams lp = new PercentRelativeLayout.LayoutParams(PercentRelativeLayout.LayoutParams.WRAP_CONTENT, PercentRelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.getPercentLayoutInfo().widthPercent = 0.5f;
            if (ids > 100) {
                if (ids % 2 == 0) {
                    lp.addRule(PercentRelativeLayout.ALIGN_PARENT_LEFT);
                    img.setPadding(20, 10, 10, 10);
                } else {
                    lp.addRule(PercentRelativeLayout.ALIGN_PARENT_RIGHT);
                    img.setPadding(10, 10, 20, 10);
                }
                lp.addRule(PercentRelativeLayout.BELOW, ids - 2);
            } else {
                lp.addRule(PercentRelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(PercentRelativeLayout.ALIGN_PARENT_TOP);
                img.setPadding(20, 10, 10, 10);
            }
            img.setLayoutParams(lp);
            // add to container
            mVH.rlPhotos.addView(img);
            // load image
            Picasso.with(mContext).load(photo.tbUrl).into(img);
            // bind click event
            img.setTag(photo);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // prepare Intent
                    Intent i = new Intent(v.getContext(), PhotoDisplayActivity.class);
                    // get image info object
                    SpotPhoto photo = (SpotPhoto) v.getTag();
                    // put img info object into Intent
                    i.putExtra("photo", photo);
                    // launch ImageDisplay layer
                    v.getContext().startActivity(i);
                }
            });
        }
    }

    private void fetchPhotoGrid(String keyword) {
        // remove old photos first
        mVH.rlPhotos.removeAllViews();
        // fetch Photos
        mGIS.fetchImages(keyword, new GoogleImageService.OnFetchedListener() {
            @Override
            public void onFetched(List<GoogleImageResult> imageResults, int nextPage) {
                List<SpotPhoto> photos = SpotPhoto.fromGoogleImageResults(imageResults);
                if (photos.size() == 0) {
                    mVH.tvPhotosC.setVisibility(View.GONE);
                    mVH.rlPhotos.setVisibility(View.GONE);
                } else {
                    renderPhotoGrid(photos);
                    mVH.tvPhotosC.setVisibility(View.VISIBLE);
                    mVH.rlPhotos.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        Animation animMagnify = AnimationUtils.loadAnimation(mContext, R.anim.magnify);
        switch (v.getId()) {
            case R.id.fragment_spot_show_iv_makecall:
                // shake icon
                mVH.ivMakeCall.startAnimation(animShake);
                // make call
                makePhoneCall((String) v.getTag());
                break;
            case R.id.fragment_spot_show_iv_viewmap:
                // magnify icon
                mVH.ivViewMap.startAnimation(animMagnify);
                launchGoogleMap((String) v.getTag());
                break;
            case R.id.fragment_spot_show_iv_makecomment:
            case R.id.fragment_spot_show_iv_makecomment2:
                // magnify icon
                mVH.ivMakeComment.startAnimation(animMagnify);
                mVH.ivMakeComment2.startAnimation(animMagnify);
                // startActivity
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra(AppConstants.INTENT_COMMENT_RESOURCE_ID, (int) v.getTag());
                startActivityForResult(intent, AppConstants.INTENT_COMMENT_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == AppConstants.INTENT_COMMENT_REQUEST_CODE) {
                // get SpotPlace data from CommentActivity
                mPlace = data.getParcelableExtra(AppConstants.COMMENT_RESULT_EXTRA_KEY);
                // refresh data
                initScrollView();
                initComments();
            }
        }
        if (resultCode == getActivity().RESULT_CANCELED) {
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
                } else if (radius < MAX_RADIUS) {
                    mVH.ivImage.setImageBitmap(getBlurBitmap(mVH.bmBG, radius));
                }
            }
        }
        // Alpha Toolbar
        int alpha = 100 * scrollY / containerHeight;
        if (alpha > 200) { alpha = 200; }
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
        public ImageView ivMakeComment2;
        public TextView tvPhotosC;
        public PercentRelativeLayout rlPhotos;
        public LinearLayout llComments;
    }
}
