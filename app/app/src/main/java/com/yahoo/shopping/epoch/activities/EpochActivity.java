package com.yahoo.shopping.epoch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.shopping.epoch.EpochClient;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.fragments.SpotListFragment;
import com.yahoo.shopping.epoch.models.SpotPlace;

import org.json.JSONObject;

import java.util.ArrayList;


import org.apache.http.Header;

public class EpochActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private NavigationView mSideBarNaviNv;
    private DrawerLayout mDrawer;
    private EpochClient mClient;
    private String mRenderType;
    private ArrayList<SpotPlace> mSpotPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        mClient = EpochClient.getInstance();
        mDrawer = (DrawerLayout) findViewById(R.id.activity_epoch_dl_drawer_layout);

        //get side bar navigation && bind event
        mSideBarNaviNv = (NavigationView) findViewById(R.id.nvView);
        mSideBarNaviNv.setNavigationItemSelectedListener(this);

        // set default render type as countryside
        mRenderType = AppConstants.RENDER_TYPE_COUNTRY_SIDE;

        mToolbar = (Toolbar)findViewById(R.id.activity_epoch_tb_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get data && render it
        getSpotPlacesAndRender();
    }

    private void replaceContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstants.ARGUMENTS_SPOT_PLACES, mSpotPlaces);

        SpotListFragment fragment = new SpotListFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_epoch_fl_content, fragment);
        transaction.commit();
    }

    private void getSpotPlacesAndRender(){
        mClient.getSpotListByType(mRenderType, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mSpotPlaces = SpotPlace.parseFromJSONArray(response.optJSONArray("content"));
                replaceContentFragment();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("getSpotListByType Error",responseString);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // get selected type
        String clickedTitle = (String) menuItem.getTitle();
        String prevRenderType = mRenderType;
        if(clickedTitle.equals(getString(R.string.country_side))){
            mRenderType = AppConstants.RENDER_TYPE_COUNTRY_SIDE;
        }
        if(clickedTitle.equals(getString(R.string.farm_visit))){
            mRenderType = AppConstants.RENDER_TYPE_FARM_VISIT;
        }
        if(clickedTitle.equals(getString(R.string.spot))){
            mRenderType = AppConstants.RENDER_TYPE_SPOT;
        }
        if(clickedTitle.equals(getString(R.string.aborigines))){
            mRenderType = AppConstants.RENDER_TYPE_ABORIGINES;
        }

        //if select different type => re-render list
        if(!prevRenderType.equals(mRenderType)){
            getSpotPlacesAndRender();
        }

        // hide side bar
        mDrawer.closeDrawers();
        return false;
    }
}
