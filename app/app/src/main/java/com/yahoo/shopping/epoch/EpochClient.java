package com.yahoo.shopping.epoch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.shopping.epoch.constants.AppConstants;

/**
 * Created by jordanhsu on 8/30/15.
 */
public class EpochClient {
    private AsyncHttpClient client = new AsyncHttpClient();

    public static EpochClient getInstance() {
        return new EpochClient();
    }

    public void getSpotListByKeyword(String keyword, AsyncHttpResponseHandler responseHandler) {
        String apiUrl = AppConstants.REST_BASE_URL + "resources/search";
        RequestParams params = new RequestParams();
        params.put("keyword", keyword);
        client.get(apiUrl, params, responseHandler);
    }

    public void getSpotListByType(String type, AsyncHttpResponseHandler responseHandler) {
        String apiUrl = AppConstants.REST_BASE_URL + "resources";
        RequestParams params = new RequestParams();
        params.put("type", type);
        client.get(apiUrl, params, responseHandler);
    }

    public void postCommentByResourceId(int resourceId, String subject, String comment, String imageUrl, int rating, AsyncHttpResponseHandler responseHandler) {
        String apiUrl = AppConstants.REST_BASE_URL + "resources/" + String.valueOf(resourceId) + "/comments";
        RequestParams params = new RequestParams();
        params.put("subject", subject);
        params.put("comment", comment);
        params.put("imageUrl", imageUrl);
        params.put("rating", rating);
        client.post(apiUrl, params, responseHandler);
    }

    public void updateImageByResourceId(int resourceId, String imageUrl, AsyncHttpResponseHandler responseHandler) {
        String apiUrl = AppConstants.REST_BASE_URL + "resources/" + String.valueOf(resourceId) + "/imageUrl";

        RequestParams params = new RequestParams();
        params.put("imageUrl", imageUrl);

        client.post(apiUrl, params, responseHandler);
    }
}
