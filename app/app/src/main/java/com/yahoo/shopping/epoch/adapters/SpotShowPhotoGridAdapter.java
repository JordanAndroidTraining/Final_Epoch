package com.yahoo.shopping.epoch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.activities.PhotoDisplayActivity;
import com.yahoo.shopping.epoch.models.SpotPhoto;

import java.util.List;

public class SpotShowPhotoGridAdapter extends RecyclerView.Adapter<SpotShowPhotoGridAdapter.ViewHolder> {
    private Context mContext;
    private List<SpotPhoto> mPhotos;

    public SpotShowPhotoGridAdapter(List<SpotPhoto> photos) {
        mPhotos = photos;
    }

    @Override
    public SpotShowPhotoGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_spot_show_photos, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SpotShowPhotoGridAdapter.ViewHolder vh, int position) {
        SpotPhoto photo = mPhotos.get(position);

        vh.itemView.setTag(photo);
        vh.tvDescription.setText(photo.title);
        vh.ivPhoto.setImageResource(0);
        vh.ivPhoto.setBackgroundColor(0);
        Picasso.with(mContext).load(photo.tbUrl).into(vh.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivPhoto;
        public TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.fragment_spot_show_rv_photos_image);
            tvDescription = (TextView) itemView.findViewById(R.id.fragment_spot_show_rv_photos_description);
            itemView.setOnClickListener(this);
        }

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
    }
}
