package com.yahoo.shopping.epoch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.EpochClient;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.Cache;
import com.yahoo.shopping.epoch.models.SpotPlace;
import com.yahoo.shopping.epoch.utils.GoogleImageResult;
import com.yahoo.shopping.epoch.utils.GoogleImageService;

import java.util.List;
import java.util.Set;

public class SpotListAdapter extends ArrayAdapter<SpotPlace> {

    private Context mContext;
    private List<SpotPlace> mSpotPlaces;

    public SpotListAdapter(Context context, int resource, List<SpotPlace> objects) {
        super(context, resource, objects);

        mContext = context;
        mSpotPlaces = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_spot, null);
        }

        // retrive the model
        final SpotPlace place = mSpotPlaces.get(position);

        // fetch the components
        TextView tvTitle = (TextView) convertView.findViewById(R.id.listview_spot_list_tv_title);
        final ImageView ivFavorite = (ImageView) convertView.findViewById(R.id.listview_spot_list_iv_favorite);
        final ImageView ivImage = (ImageView) convertView.findViewById(R.id.listview_spot_list_iv_image);

        // set components content
        tvTitle.setText(place.getTitle());
        setFavoriteContent(ivFavorite, place);
        setRateStarStatus(place.getRating(), convertView);
        setImageContent(ivImage, place);

        return convertView;
    }

    private void setFavoriteContent(final ImageView ivFavorite, final SpotPlace place) {
        Cache prefCache = new Cache(AppConstants.PREFERENCE_FAVORITE_STORAGE_NAME, mContext);
        Set<String> favorites = prefCache.getStringSet(AppConstants.PREFERENCE_FAVORITE_KEY);

        if (favorites.contains(String.valueOf(place.getResourceId()))) {
            ivFavorite.setImageResource(R.drawable.ic_fav_heart1);
        }

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cache prefCache = new Cache(AppConstants.PREFERENCE_FAVORITE_STORAGE_NAME, mContext);
                Set<String> favorites = prefCache.getStringSet(AppConstants.PREFERENCE_FAVORITE_KEY);

                String resourceId = String.valueOf(place.getResourceId());
                if (favorites.contains(resourceId)) {
                    favorites.remove(resourceId);
                    prefCache.storeStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, favorites);
                    ivFavorite.setImageResource(R.drawable.ic_fav_heart0);

                    Toast.makeText(mContext, "已刪除: " + place.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    favorites.add(resourceId);
                    prefCache.storeStringSet(AppConstants.PREFERENCE_FAVORITE_KEY, favorites);
                    ivFavorite.setImageResource(R.drawable.ic_fav_heart1);

                    Toast.makeText(mContext, "已增加: " + place.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRateStarStatus(int rating, View view) {
        ImageView ivSpotListStar1 = (ImageView) view.findViewById(R.id.ivSpotListStar1);
        ImageView ivSpotListStar2 = (ImageView) view.findViewById(R.id.ivSpotListStar2);
        ImageView ivSpotListStar3 = (ImageView) view.findViewById(R.id.ivSpotListStar3);
        ImageView ivSpotListStar4 = (ImageView) view.findViewById(R.id.ivSpotListStar4);
        ImageView ivSpotListStar5 = (ImageView) view.findViewById(R.id.ivSpotListStar5);

        ivSpotListStar1.setImageResource(rating >= 1 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        ivSpotListStar2.setImageResource(rating >= 2 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        ivSpotListStar3.setImageResource(rating >= 3 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        ivSpotListStar4.setImageResource(rating >= 4 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
        ivSpotListStar5.setImageResource(rating >= 5 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0);
    }

    private void setImageContent(final ImageView ivImage, final SpotPlace place) {
        if (place.getImageUrl().isEmpty()) {
            new GoogleImageService().fetchImages(place.getTitle(), new GoogleImageService.OnFetchedListener() {
                @Override
                public void onFetched(List<GoogleImageResult> imageResults, int nextPage) {
                    String url = imageResults.get(0).url;
                    if (!url.isEmpty()) {
                        // update app entity
                        place.setImageUrl(url);

                        // update storage entity
                        new EpochClient().updateImageByResourceId(place.getResourceId(), url, new JsonHttpResponseHandler());

                        // update app image
                        updateImage(ivImage, url);
                    }
                }
            });
        } else {
            updateImage(ivImage, place.getImageUrl());
        }
    }

    private void updateImage(ImageView imageView, String imageUrl) {
        imageView.setImageResource(0);
        Picasso.with(mContext).load(imageUrl).into(imageView);
    }
}
