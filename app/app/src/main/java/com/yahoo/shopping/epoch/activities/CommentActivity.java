package com.yahoo.shopping.epoch.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.shopping.epoch.R;
import com.yahoo.shopping.epoch.constants.AppConstants;

import org.w3c.dom.Text;

public class CommentActivity extends ActionBarActivity implements View.OnClickListener {
    private int mResourceId;
    private ImageView mCloseCommentIv;
    private TextView mSubmitCommentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResourceId = getIntent().getIntExtra(AppConstants.INTENT_COMMENT_RESOURCE_ID,0);
        setContentView(R.layout.activity_comment);

        // get item reference
        mCloseCommentIv = (ImageView) findViewById(R.id.commentCloseIconIv);
        mSubmitCommentTv = (TextView) findViewById(R.id.commentPostIconTv);

        // bind event
        mCloseCommentIv.setOnClickListener(this);
        mSubmitCommentTv.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentCloseIconIv:
                finish();
                break;
            case R.id.commentPostIconTv:
                break;
        }
    }
}
