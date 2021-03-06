package com.yahoo.shopping.epoch.activities;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.shopping.epoch.EpochClient;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.fragments.SpotListFragment;
import com.yahoo.shopping.epoch.models.Cache;
import com.yahoo.shopping.epoch.models.SpotPlace;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EpochActivity extends AppCompatActivity {
    private NavigationView mSideBarNaviNv;
    private DrawerLayout mDrawer;
    private EpochClient mClient;
    private String mTripType;
    private String mSearchKeyword;
    private ArrayList<SpotPlace> mSpotPlaces;
    private ImageView mDice1Iv;
    private ImageView mDice2Iv;
    private ImageView mDice3Iv;
    private final static int[] DICE_ANIMATIONS = {R.drawable.dice_anim1,R.drawable.dice_anim2,R.drawable.dice_anim3,R.drawable.dice_anim4,R.drawable.dice_anim5,R.drawable.dice_anim6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        mClient = EpochClient.getInstance();
        mDrawer = (DrawerLayout) findViewById(R.id.activity_epoch_dl_drawer_layout);

        //get side bar navigation && bind event
        mSideBarNaviNv = (NavigationView) findViewById(R.id.nvView);
        mSideBarNaviNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                navigationItemSelectHandler(menuItem);
                return false;
            }
        });


        // set default trip type as countryside
        mTripType = AppConstants.TRIP_TYPE_COUNTRY_SIDE;

        Toolbar mToolbar = (Toolbar) findViewById(R.id.activity_epoch_tb_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);




        //get data && render it
        //default render type is RENDER_TYPE_CLUSTER
        getSpotPlacesAndRender(AppConstants.RENDER_TYPE_CLUSTER);
    }

    private synchronized void replaceContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstants.ARGUMENTS_SPOT_PLACES, mSpotPlaces);

        SpotListFragment fragment = new SpotListFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_epoch_fl_content, fragment);
        transaction.commit();
    }

    private void performGetFavoriteSpots() {
        Cache prefCache = new Cache(AppConstants.PREFERENCE_FAVORITE_STORAGE_NAME, this);
        Set<String> favorites = prefCache.getStringSet(AppConstants.PREFERENCE_FAVORITE_KEY);
        new FetchMultipleResourcesAsyncTask().execute(favorites);
    }

    private void getSpotPlacesAndRender(int renderType) {
        switch (renderType) {
            case AppConstants.RENDER_TYPE_CLUSTER:
                mClient.getSpotListByType(mTripType, new MultiSpotPlacesHttpResponseHandler());
                break;
            case AppConstants.RENDER_TYPE_SEARCH:
                mClient.getSpotListByKeyword(mSearchKeyword, new MultiSpotPlacesHttpResponseHandler());
                break;
        }
    }

    public void searchSubmitHandler(String keyword) {
        mSearchKeyword = keyword;
        getSpotPlacesAndRender(AppConstants.RENDER_TYPE_SEARCH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate search view
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSubmitHandler(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.action_dice_spot_place:
                randomR12N();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void randomR12N() {
        // Get dice reference
        mDice1Iv = (ImageView) findViewById(R.id.dice1Iv);
        mDice2Iv = (ImageView) findViewById(R.id.dice2Iv);
        mDice3Iv = (ImageView) findViewById(R.id.dice3Iv);

        showDices();
        final AnimationDrawable diceAnim1 = DoDiceAnimation(mDice1Iv);
        final AnimationDrawable diceAnum2 = DoDiceAnimation(mDice2Iv);
        final AnimationDrawable diceAnum3 = DoDiceAnimation(mDice3Iv);
        final Context selfContext = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                diceAnim1.stop();
                diceAnum2.stop();
                diceAnum3.stop();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideDices();
                        Random rand = new Random();
                        int position = rand.nextInt(mSpotPlaces.size());
                        Intent intent = new Intent(selfContext, SpotShowActivity.class);
                        intent.putParcelableArrayListExtra(AppConstants.INTENT_SPOT_PLACES, mSpotPlaces);
                        intent.putExtra(AppConstants.INTENT_SPOT_SELECT_PLACES_INDEX, position);
                        startActivity(intent);
                    }
                }, 1000);
            }
        }, 2000);
    }

    private AnimationDrawable DoDiceAnimation(ImageView v){
        Random rand = new Random();
        int resId = DICE_ANIMATIONS[rand.nextInt(DICE_ANIMATIONS.length)];
        v.setBackgroundResource(resId);
        AnimationDrawable anim = (AnimationDrawable) v.getBackground();
        anim.start();
        return anim;
    }

    private void hideDices(){
        if(mDice1Iv != null)
            mDice1Iv.setVisibility(View.GONE);
        if(mDice2Iv != null)
            mDice2Iv.setVisibility(View.GONE);
        if(mDice3Iv != null)
            mDice3Iv.setVisibility(View.GONE);
    }

    private void showDices(){
        if(mDice1Iv != null)
            mDice1Iv.setVisibility(View.VISIBLE);
        if(mDice2Iv != null)
            mDice2Iv.setVisibility(View.VISIBLE);
        if(mDice3Iv != null)
            mDice3Iv.setVisibility(View.VISIBLE);
    }

    public void navigationItemSelectHandler(MenuItem menuItem) {
        // get selected type
        String clickedTitle = (String) menuItem.getTitle();
        String prevTripType = mTripType;

        if (clickedTitle.equals(getString(R.string.country_side))) {
            mTripType = AppConstants.TRIP_TYPE_COUNTRY_SIDE;
        } else if (clickedTitle.equals(getString(R.string.farm_visit))) {
            mTripType = AppConstants.TRIP_TYPE_FARM_VISIT;
        } else if (clickedTitle.equals(getString(R.string.spot))) {
            mTripType = AppConstants.TRIP_TYPE_SPOT;
        } else if (clickedTitle.equals(getString(R.string.aborigines))) {
            mTripType = AppConstants.TRIP_TYPE_ABORIGINES;
        }
        else {
            if (clickedTitle.equals("最愛地點")) {
                mTripType = "";
                performGetFavoriteSpots();
                mDrawer.closeDrawer(mSideBarNaviNv);
                return;
            } else { // virtual type => use search
                mTripType = "";
                searchSubmitHandler(clickedTitle);
                mDrawer.closeDrawer(mSideBarNaviNv);
                return;
            }
        }

        //if select different type => re-render list
        if (!prevTripType.equals(mTripType)) {
            getSpotPlacesAndRender(AppConstants.RENDER_TYPE_CLUSTER);
        }

        mDrawer.closeDrawer(mSideBarNaviNv);
    }

    private class MultiSpotPlacesHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            mSpotPlaces = SpotPlace.parseFromJSONArray(response.optJSONArray("content"));
            replaceContentFragment();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("getSpotListByType Error", responseString);
        }
    }

    private class FetchMultipleResourcesAsyncTask extends AsyncTask<Set<String>, Void, List<SpotPlace>> {
        private String fetchContent(String url) throws IOException {
            StringBuilder jsonString = new StringBuilder();

            URLConnection urlConnection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            do {
                line = reader.readLine();

                if (line != null) {
                    jsonString.append(line);
                }
            } while (line != null);

            return jsonString.toString();
        }

        @Override
        protected List<SpotPlace> doInBackground(Set<String>... params) {
            Set<String> favorites = params[0];
            List<SpotPlace> places = new ArrayList<>();

            for (String resourceId : favorites) {
                String apiUrl = AppConstants.REST_BASE_URL + "resources/" + resourceId;

                try {
                    String content = fetchContent(apiUrl);
                    places.add(SpotPlace.parseFromJSONObject(new JSONObject(content)));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<SpotPlace> spotPlaces) {
            mSpotPlaces.clear();

            if (spotPlaces != null) {
                mSpotPlaces.addAll(spotPlaces);
            }

            replaceContentFragment();
        }
    }
}
