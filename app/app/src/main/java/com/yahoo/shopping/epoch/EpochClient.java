package com.yahoo.shopping.epoch;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by jordanhsu on 8/30/15.
 */
public class EpochClient {
    public final String REST_BASE_URL = "http://192.168.1.111:8080/";
    private AsyncHttpClient client = new AsyncHttpClient();

    public static EpochClient getInstance(){
        return new EpochClient();
    }

    public void getSearchResult(String keyword){
        String apiUrl = REST_BASE_URL + "";
    }

    public void getSpotListByType(String type,AsyncHttpResponseHandler responseHandler){
        String apiUrl = REST_BASE_URL + "resources";
        RequestParams params = new RequestParams();
        params.put("type",type);
        client.get(apiUrl, params, responseHandler);
    }

}
