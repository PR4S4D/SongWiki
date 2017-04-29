package com.slp.songwiki.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class Artist implements Parcelable {
    private String name;
    private long listeners;
    private String imageLink;
    private String publishedOn;
    private String summary;
    private String content;

    public Artist() {

    }

    public Artist(String name, long listeners, String imageLink) {
        this.name = name;
        this.listeners = listeners;
        this.imageLink = imageLink;
    }


    protected Artist(Parcel in) {
        name = in.readString();
        listeners = in.readLong();
        imageLink = in.readString();
        publishedOn = in.readString();
        summary = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(listeners);
        dest.writeString(imageLink);
        dest.writeString(publishedOn);
        dest.writeString(summary);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getListeners() {
        return listeners;
    }

    public void setListeners(long listeners) {
        this.listeners = listeners;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
