package com.velichkomarija4.simplemusicapp.db;

import com.velichkomarija4.simplemusicapp.model.Album;
import com.velichkomarija4.simplemusicapp.model.AlbumSong;
import com.velichkomarija4.simplemusicapp.model.Comment;
import com.velichkomarija4.simplemusicapp.model.Song;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Album.class, Song.class, AlbumSong.class, Comment.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
