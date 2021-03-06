package com.yahoo.shopping.epoch.utils;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.shopping.epoch.constants.AppConstants;
import com.yahoo.shopping.epoch.models.Cache;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleImageService {

    public ArrayList<GoogleImageResult> mImages = new ArrayList<>();
    public QueryParam mQueryParam = new QueryParam();
    public int mNextPageStartIndex = 0;

    private Context mContext;
    private boolean mCacheEnabled;
    private Cache mCache;

    public GoogleImageService() {
        mContext = null;
        mCacheEnabled = false;
        mCache = null;
    }

    public GoogleImageService(Context context, boolean cacheEnabled) {
        mContext = context;
        mCacheEnabled = cacheEnabled;

        if (cacheEnabled) {
            mCache = new Cache(AppConstants.PREFERENCE_IMAGE_STORAGE_NAME, context);
        }
    }

    public void fetchImages(String query) {
        fetchImages(query, null, 0, 0, null);
    }

    public void fetchImages(String query, final OnFetchedListener listener) {
        fetchImages(query, listener, 0, 0, null);
    }

    public void fetchImages(String query, final OnFetchedListener listener, int count) {
        fetchImages(query, listener, count, 0, null);
    }

    public void fetchImages(String query, final OnFetchedListener listener, int count, int start) {
        fetchImages(query, listener, count, start, null);
    }

    public void fetchImages(String query, final OnFetchedListener listener, int count, int start, QueryParam param) {
        final String url = genRequestUrl(query, count, start, param);

        String cachedResponse = "";
        if (mCache != null) {
            cachedResponse = mCache.getString(url);
        }

        if (!cachedResponse.isEmpty()) {
            try {
                Log.d("XXX: Cache:", url);

                invokeListener(new JSONObject(cachedResponse), listener);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // do async http request
            Log.d("XXX: QueryURL:", url);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (mCache != null) {
                            mCache.storeString(url, response.toString());
                        }
                        invokeListener(response, listener);
                    } catch (JSONException e) {
                        Log.d("XXX: QueryERR:", response.toString());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("XXX: QueryERR:", throwable.getMessage());
                }
            });
        }
    }

    private String genRequestUrl(String query, int count, int start, QueryParam param) {
        // if end, do nothing, just exit
        if (start == -1) {
            return "";
        }
        // if first, clear list
        if (start == 0) {
            mImages.clear();
        }
        // if default count
        if (count == 0) {
            count = 8;
        }

        // prepare api param
        if (param == null) {
            param = new QueryParam();
        }
        mQueryParam = param;

        // encode query string
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // prepare api url
        String url = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + count + "&q=" + query + "&start=" + start;
        if (param.as_sitesearch != "") {
            url += "&as_sitesearch=" + param.as_sitesearch.toLowerCase();
        }
        if (param.imgcolor != "") {
            url += "&imgcolor=" + param.imgcolor.toLowerCase();
        }
        if (param.imgtype != "") {
            url += "&imgtype=" + param.imgtype.toLowerCase();
        }
        if (param.imgsz != "") {
            url += "&imgsz=" + param.imgsz.toLowerCase();
        }

        return url;
    }

    private void invokeListener(JSONObject response, OnFetchedListener listener) throws JSONException {
        // load images from JSONArray
        JSONObject rootObj = response.getJSONObject("responseData");
        JSONArray results = rootObj.getJSONArray("results");
        mImages.addAll(GoogleImageResult.fromJSONArray(results));

        // get pagination data
        JSONObject cursor = rootObj.getJSONObject("cursor");
        JSONObject nextPage = cursor.getJSONArray("pages").optJSONObject(cursor.getInt("currentPageIndex") + 1);
        mNextPageStartIndex = (nextPage == null) ? -1 : nextPage.getInt("start");

        // notify listener
        if (listener != null) {
            listener.onFetched(mImages, mNextPageStartIndex);
        }
    }

    public static class QueryParam implements Serializable {
        public interface IMGSZ {
            String NotSpecified = "";
            String Face = "face";
            String Photo = "photo";
            String ClipArt = "clipart";
            String LineArt = "lineart";
        }

        public interface IMGTYPE {
            String NotSpecified = "";
            String Icon = "icon";
            String Small = "small";
            String Medium = "medium";
            String Large = "large";
            String XLarge = "xlarge";
            String XXLarge = "xxlarge";
            String Huge = "huge";
        }

        public interface IMGCOLOR {
            String NotSpecified = "";
            String Black = "black";
            String Blue = "blue";
            String Brown = "brown";
            String Gray = "gray";
            String Green = "green";
            String Orange = "orange";
            String Pink = "pink";
            String Purple = "purple";
            String Red = "red";
            String Teal = "teal";
            String White = "white";
            String Yellow = "yellow";
        }

        public String imgsz = IMGSZ.NotSpecified;
        public String imgtype = IMGTYPE.NotSpecified;
        public String imgcolor = IMGCOLOR.NotSpecified;
        public String as_sitesearch = "";
    }

    public interface OnFetchedListener {
        void onFetched(List<GoogleImageResult> imageResults, int nextPage);
    }
}
