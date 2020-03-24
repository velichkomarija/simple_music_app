package com.velichkomarija4.simplemusicapp.model;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Album.class, parentColumns = "id", childColumns = "album_id"),
        @ForeignKey(entity = Song.class, parentColumns = "id", childColumns = "song_id")})
public class AlbumSong {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NotNull
    private String mId;

    @ColumnInfo(name = "album_id")
    private int mAlbumId;

    @ColumnInfo(name = "song_id")
    private int mSongId;

    public AlbumSong(String id, int albumId, int songId) {
        mId = id;
        mAlbumId = albumId;
        mSongId = songId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int albumId) {
        mAlbumId = albumId;
    }

    public int getSongId() {
        return mSongId;
    }

    public void setSongId(int songId) {
        mSongId = songId;
    }

    @Override
    public String toString() {
        return "AlbumSong{" + "mId=" + mId +
                ", mAlbumId='" + mAlbumId +
                ", mSongId='" + mSongId + '}';
    }
}
