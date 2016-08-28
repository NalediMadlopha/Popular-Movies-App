package com.popular_movies.app.model;

/**
 * Created by Naledi Madlopha on 2016/08/14.
 */
public class Trailer {
    
    private String mId;
    private String mIso_639_1;
    private String mIso_3166_1;
    private String mKey;
    private String mName;
    private String mSite;
    private String mSize;
    private String mType;

    public Trailer() {
    }

    public Trailer(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, String size, String type) {
        this.mId = id;
        this.mIso_639_1 = iso_639_1;
        this.mIso_3166_1 = iso_3166_1;
        this.mKey = key;
        this.mName = name;
        this.mSite = site;
        this.mSize = size;
        this.mType = type;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getIso_639_1() {
        return mIso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.mIso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return mIso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.mIso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + mId + '\'' +
                ", iso_639_1='" + mIso_639_1 + '\'' +
                ", iso_3166_1='" + mIso_3166_1 + '\'' +
                ", key='" + mKey + '\'' +
                ", name='" + mName + '\'' +
                ", site='" + mSite + '\'' +
                ", size='" + mSize + '\'' +
                ", type='" + mType + '\'' +
                '}';
    }
}
