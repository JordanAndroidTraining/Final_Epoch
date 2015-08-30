package com.yahoo.shopping.epoch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.shopping.epoch.EpochClient;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.fragments.SpotListFragment;
import com.yahoo.shopping.epoch.models.SpotPlace;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import org.apache.http.Header;

public class EpochActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private EpochClient mClient;
    private ArrayList<SpotPlace> mSpotPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        mClient = EpochClient.getInstance();
        mDrawer = (DrawerLayout) findViewById(R.id.activity_epoch_dl_drawer_layout);

        mToolbar = (Toolbar)findViewById(R.id.activity_epoch_tb_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSpotPlacesAndRender();
//        mSpotPlaces = getSpotPlaces();
//        replaceContentFragment();
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
        mClient.getSpotListByType("CountrySide", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mSpotPlaces = SpotPlace.parseFromJSONArray(response.optJSONArray("content"));
                replaceContentFragment();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d();
            }
        });
    }

    private ArrayList<SpotPlace> getSpotPlaces() {
        mSpotPlaces = new ArrayList<>();

        mSpotPlaces.add(new SpotPlace("明故老街",
                "金門縣金城鎮",
                "http://2.bp.blogspot.com/-3jKHiKXv78Y/TVqoHOSVpTI/AAAAAAAAEh0/UnUQaEEqDws/s1600/2.JPG",
                "明洪武二十年(西元1387年),江夏侯周德 興築金門千戶所城,即今之金門城,是「金 門」得名之始。 原所城建有城牆,開四 門,是明代金門的政經與軍事中心,距今 已超過600年。「明故老街」位於金門城北 門外,一條長約百餘公尺的老舊街坊,二 側店坊雖已改建過,但街道中央保留原來 石板鋪設的樣貌,隘門遺蹟依稀可見。 明 故老街為明代建造,距今已年代久遠,其 路面由石塊與磚頭相砌而成,雖已露出疲 態,斑駁景象,但為金門現存惟一形成於 明代的街坊,上百年的悠久歷史,值得人 一同探索。",
                "自行車車道依山環潭,略有上下坡,輕鬆 慢騎,約25分到半小時可騎完。建議攜帶 望遠鏡,可以賞鳥親近生態。",
                "開車1.由金城市區-走環島西路南下-過古 崗湖延指標-舊金城。2.賢厝-水頭-舊金 城。大眾運輸1.由尚義機場搭3路公車至 金城。2.由金城車站搭乘6號公車在舊金 城下即可。",
                "02-23602266"));

        mSpotPlaces.add(new SpotPlace("明故老街2",
                "金門縣金城鎮",
                "http://2.bp.blogspot.com/-3jKHiKXv78Y/TVqoHOSVpTI/AAAAAAAAEh0/UnUQaEEqDws/s1600/2.JPG",
                "明洪武二十年(西元1387年),江夏侯周德 興築金門千戶所城,即今之金門城,是「金 門」得名之始。 原所城建有城牆,開四 門,是明代金門的政經與軍事中心,距今 已超過600年。「明故老街」位於金門城北 門外,一條長約百餘公尺的老舊街坊,二 側店坊雖已改建過,但街道中央保留原來 石板鋪設的樣貌,隘門遺蹟依稀可見。 明 故老街為明代建造,距今已年代久遠,其 路面由石塊與磚頭相砌而成,雖已露出疲 態,斑駁景象,但為金門現存惟一形成於 明代的街坊,上百年的悠久歷史,值得人 一同探索。",
                "自行車車道依山環潭,略有上下坡,輕鬆 慢騎,約25分到半小時可騎完。建議攜帶 望遠鏡,可以賞鳥親近生態。",
                "開車1.由金城市區-走環島西路南下-過古 崗湖延指標-舊金城。2.賢厝-水頭-舊金 城。大眾運輸1.由尚義機場搭3路公車至 金城。2.由金城車站搭乘6號公車在舊金 城下即可。",
                "02-23602266"));

        return mSpotPlaces;
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
}
