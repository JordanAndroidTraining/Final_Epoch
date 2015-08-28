package com.yahoo.shopping.epoch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.models.SpotPlace;

import java.util.List;

/**
 * Created by jamesyan on 8/28/15.
 */
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

        SpotPlace place = mSpotPlaces.get(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.listview_spot_list_tv_title);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.listview_spot_list_iv_image);

        tvTitle.setText(place.getTitle());
        Picasso.with(mContext).load(place.getImageUrl()).into(ivImage);

        return convertView;
    }
}
