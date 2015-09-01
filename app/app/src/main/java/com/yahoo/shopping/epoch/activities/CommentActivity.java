package com.yahoo.shopping.epoch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.shopping.epoch.EpochClient;
import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.SpotPlace;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {
    private int mResourceId;
    private ImageView mCloseCommentIv;
    private TextView mSubmitCommentTv;
    private EditText mCommentContentEt;
    private ImageView mRateStarIv1;
    private ImageView mRateStarIv2;
    private ImageView mRateStarIv3;
    private ImageView mRateStarIv4;
    private ImageView mRateStarIv5;
    private EpochClient mClient;
    private String mComment ="";
    private String mSubject = "";
    private String mImgUrl = "";
    private int mRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResourceId = getIntent().getIntExtra(AppConstants.INTENT_COMMENT_RESOURCE_ID,0);
        setContentView(R.layout.activity_comment);

        mClient = EpochClient.getInstance();

        // get item reference
        mCloseCommentIv = (ImageView) findViewById(R.id.commentCloseIconIv);
        mSubmitCommentTv = (TextView) findViewById(R.id.commentPostIconTv);
        mCommentContentEt = (EditText) findViewById(R.id.commentContentEt);
        mRateStarIv1 = (ImageView) findViewById(R.id.commentRateIconIv1);
        mRateStarIv2 = (ImageView) findViewById(R.id.commentRateIconIv2);
        mRateStarIv3 = (ImageView) findViewById(R.id.commentRateIconIv3);
        mRateStarIv4 = (ImageView) findViewById(R.id.commentRateIconIv4);
        mRateStarIv5 = (ImageView) findViewById(R.id.commentRateIconIv5);


        // bind event
        mCloseCommentIv.setOnClickListener(this);
        mSubmitCommentTv.setOnClickListener(this);
        mCommentContentEt.setOnKeyListener(this);
        mRateStarIv1.setOnClickListener(this);
        mRateStarIv2.setOnClickListener(this);
        mRateStarIv3.setOnClickListener(this);
        mRateStarIv4.setOnClickListener(this);
        mRateStarIv5.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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

    public void doMakeComment(){

        mClient.postCommentByResourceId(mResourceId, mSubject, mComment, mImgUrl, mRating, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("JordanTest","make comment success!!");
                refetchSpotDataAndGoBack();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("JordanTest", "make comment fail| msg: " + responseString);
                refetchSpotDataAndGoBack();
            }
        });
    }

    public void refetchSpotDataAndGoBack(){
        mClient.getSpotByResourceId(mResourceId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SpotPlace spot = SpotPlace.parseFromJSONObject(response);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(AppConstants.COMMENT_RESULT_EXTRA_KEY, spot);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentCloseIconIv:
                finish();
                break;
            case R.id.commentPostIconTv:
                mComment = String.valueOf(mCommentContentEt.getText());
                doMakeComment();
                break;
            case R.id.commentRateIconIv1:
                processAndRenderRateStar(1);
                break;
            case R.id.commentRateIconIv2:
                processAndRenderRateStar(2);
                break;
            case R.id.commentRateIconIv3:
                processAndRenderRateStar(3);
                break;
            case R.id.commentRateIconIv4:
                processAndRenderRateStar(4);
                break;
            case R.id.commentRateIconIv5:
                processAndRenderRateStar(5);
                break;
        }
    }

    private void processAndRenderRateStar(int rating) {
        mRateStarIv1.setImageResource(rating >= 1 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0_grey);
        mRateStarIv2.setImageResource(rating >= 2 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0_grey);
        mRateStarIv3.setImageResource(rating >= 3 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0_grey);
        mRateStarIv4.setImageResource(rating >= 4 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0_grey);
        mRateStarIv5.setImageResource(rating >= 5 ? R.drawable.ic_action_favstar1 : R.drawable.ic_action_favstar0_grey);
        mRating = rating;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()){
            case R.id.commentContentEt:
                updateSubmitBtn();
                break;
        }

        return false;
    }

    private void updateSubmitBtn() {
        if(mCommentContentEt.getText().length() > 0){
            mSubmitCommentTv.setTextColor(getResources().getColor(R.color.available_blue));
            mSubmitCommentTv.setClickable(true);
        }
        else {
            mSubmitCommentTv.setTextColor(getResources().getColor(R.color.unavailable_grey));
            mSubmitCommentTv.setClickable(false);
        }
    }
}
