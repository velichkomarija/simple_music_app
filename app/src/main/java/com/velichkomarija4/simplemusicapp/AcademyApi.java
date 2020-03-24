package com.velichkomarija4.simplemusicapp;

import com.velichkomarija4.simplemusicapp.model.Album;
import com.velichkomarija4.simplemusicapp.model.Comment;
import com.velichkomarija4.simplemusicapp.model.Song;
import com.velichkomarija4.simplemusicapp.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body User user);

    @GET("user")
    Single<User> auth();

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("comments")
    Single<List<Comment>> getComments();

    @POST("comments")
    Completable comment(@Body Comment comment);

    @GET("songs")
    Call<List<Song>> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);
}
