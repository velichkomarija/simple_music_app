package com.velichkomarija4.simplemusicapp.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment implements Serializable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "album_id")
    @SerializedName("album_id")
    private int mAlbumId;

    @ColumnInfo(name = "name")
    @SerializedName("author")
    private String mName;

    @ColumnInfo(name = "time")
    @SerializedName("timestamp")
    private String mTime;

    @ColumnInfo(name = "message")
    @SerializedName("text")
    private String mMessage;

    public Comment(int mId, int mAlbumId, String mMessage) {
        this.mId = mId;
        this.mAlbumId = mAlbumId;
        this.mMessage = mMessage;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int nAlbumId) {
        this.mAlbumId = nAlbumId;
    }
}
