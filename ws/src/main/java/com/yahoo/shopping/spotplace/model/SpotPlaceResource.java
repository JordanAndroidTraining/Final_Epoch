package com.yahoo.shopping.spotplace.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jamesyan on 8/27/15.
 */
public class SpotPlaceResource {
    public enum DataType {JSON, XML, CSV}

    private String url;
    private DataType type;
    private String encoding;
    private Map<SpotPlace.Fields, String> fiedlsMapping;

    public SpotPlaceResource(String url, DataType type, HashMap<SpotPlace.Fields, String> fiedlsMapping) {
        this.url = url;
        this.type = type;
        this.fiedlsMapping = fiedlsMapping;
        this.encoding = "UTF-8";
    }

    public SpotPlaceResource(String url, DataType type, HashMap<SpotPlace.Fields, String> fiedlsMapping, String encoding) {
        this.url = url;
        this.type = type;
        this.fiedlsMapping = fiedlsMapping;
        this.encoding = encoding;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<SpotPlace.Fields, String> getFiedlsMapping() {
        return Collections.unmodifiableMap(fiedlsMapping);
    }

    public void setFiedlsMapping(Map<SpotPlace.Fields, String> fiedlsMapping) {
        this.fiedlsMapping = fiedlsMapping;
    }
}
