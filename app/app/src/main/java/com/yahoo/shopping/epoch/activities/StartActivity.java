package com.yahoo.shopping.epoch.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;

public class StartActivity extends ActionBarActivity {

    private ImageView mBgImage;
    private Runnable mRunable;

    private void launchMainActivity() {
        Intent i = new Intent(this, EpochActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mBgImage = (ImageView) findViewById(R.id.startBgIv);

        // do animation
        doScaleImageAnimation(mBgImage, 1.15f, 1.0f, 10, AppConstants.SPLASH_SHOW_DURATION);

        // prepare runnable
        mRunable = new Runnable() {
            public void run() {
                launchMainActivity();
            }
        };
        // go to Epoch Activity after x seconds have passed
        mBgImage.postDelayed(mRunable, AppConstants.SPLASH_SHOW_DURATION);

        // cancel delay, direct go to Epoch Activity
        mBgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBgImage.removeCallbacks(mRunable);
                launchMainActivity();
            }
        });

    }

    public void doScaleImageAnimation(final View view, Float startScale, Float endScale, int startDuration , int endDuration){

        ObjectAnimator animStart = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", startScale),
                PropertyValuesHolder.ofFloat("scaleY", startScale));
        animStart.setDuration(startDuration);


        ObjectAnimator animEnd = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", endScale),
                PropertyValuesHolder.ofFloat("scaleY", endScale));
        animEnd.setDuration(endDuration);
        animEnd.setInterpolator(new AccelerateInterpolator());

        //anim.start();

        AnimatorSet animatorSet = new AnimatorSet(); // need to initilize again?
        animatorSet.playSequentially(animStart, animEnd);
        animatorSet.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
