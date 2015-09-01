package com.yahoo.shopping.epoch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.models.SpotPhoto;

public class PhotoDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        // get photo info from Intent
        SpotPhoto photo = (SpotPhoto) getIntent().getSerializableExtra("photo");
        // get ImageView
        ImageView ivImageFull = (ImageView) findViewById(R.id.ivPhotoFull);
        // bind click event to close layer
        ivImageFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // load photo url
        Picasso.with(this).load(photo.photoUrl).into(ivImageFull);
    }

}
