package com.slp.songwiki.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lakshmiprasad on 4/30/2017.
 */

public class Artist implements Parcelable {
    private String name;
    private long listeners;
    private String imageLink;
    private String publishedOn;
    private String summary;
    private String content;
    private String artistLink;
    private List<Artist> similarArtists;
    private List<String> tags;

    public String getArtistLink() {
        return artistLink;
    }

    public void setArtistLink(String artistLink) {
        this.artistLink = artistLink;
    }

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Artist> getSimilarArtists() {
        return similarArtists;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", listeners=" + listeners +
                ", imageLink='" + imageLink + '\'' +
                ", publishedOn='" + publishedOn + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", similarArtists=" + similarArtists +
                '}';
    }

    public void setSimilarArtists(List<Artist> similarArtists) {
        this.similarArtists = similarArtists;
    }

    public Artist(String name, long listeners, String imageLink, String publishedOn, String summary, String content) {
        this.name = name;
        this.listeners = listeners;
        this.imageLink = imageLink;
        this.publishedOn = publishedOn;
        this.summary = summary;

        this.content = content;
    }

    public Artist() {

    }

    public Artist(String name, String imageLink, String artistLink) {
        this.name = name;
        this.imageLink = imageLink;
        this.artistLink = artistLink;
    }

    protected Artist(Parcel in) {
        name = in.readString();
        listeners = in.readLong();
        imageLink = in.readString();
        publishedOn = in.readString();
        summary = in.readString();
        content = in.readString();
        artistLink = in.readString();
        if (in.readByte() == 0x01) {
            similarArtists = new ArrayList<Artist>();
            in.readList(similarArtists, Artist.class.getClassLoader());
        } else {
            similarArtists = null;
        }
        if (in.readByte() == 0x01) {
            tags = new ArrayList<String>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
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
        dest.writeString(artistLink);
        if (similarArtists == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(similarArtists);
        }
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
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
}