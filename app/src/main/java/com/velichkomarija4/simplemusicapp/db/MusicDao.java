package com.velichkomarija4.simplemusicapp.db;

import com.velichkomarija4.simplemusicapp.model.Album;
import com.velichkomarija4.simplemusicapp.model.AlbumSong;
import com.velichkomarija4.simplemusicapp.model.Comment;
import com.velichkomarija4.simplemusicapp.model.Song;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinkAlbumSong(AlbumSong albumSong);

    @Query("select * from album where id = :albumId")
    Album getAlbumWithId(int albumId);

    @Query("SELECT * from album")
    List<Album> getAlbums();

    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumSongId order by song_id DESC" )
    List<Song> getAlbumSong(int albumSongId);

    @Query("select * from comment where album_id= :albumId")
    List<Comment> getCommentsByAlbumId(int albumId);

    @Delete
    void deleteAlbum(Album album);

    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);

}