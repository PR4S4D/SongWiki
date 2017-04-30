package com.slp.songwiki.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lshivaram on 4/30/2017.
 */
public class Track implements Parcelable {
    private String title;
    private String artist;
    private String album;
    private long duration;
    private long listeners;
    private String content;
    private String imageLink;
    private String summary;

    public Track(String title, String artist, long listeners, String imageLink) {
        this.title = title;
        this.artist = artist;
        this.listeners = listeners;
        this.imageLink = imageLink;
    }

    public Track() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getListeners() {
        return listeners;
    }

    public void setListeners(long listeners) {
        this.listeners = listeners;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    protected Track(Parcel in) {
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readLong();
        listeners = in.readLong();
        content = in.readString();
        imageLink = in.readString();
        summary = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeLong(duration);
        dest.writeLong(listeners);
        dest.writeString(content);
        dest.writeString(imageLink);
        dest.writeString(summary);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", listeners=" + listeners +
                ", content='" + content + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

}