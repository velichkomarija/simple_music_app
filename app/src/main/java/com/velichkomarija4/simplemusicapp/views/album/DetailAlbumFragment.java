package com.velichkomarija4.simplemusicapp.views.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.velichkomarija4.simplemusicapp.AlbumsActivity;
import com.velichkomarija4.simplemusicapp.ApiUtils;
import com.velichkomarija4.simplemusicapp.Application;
import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.db.MusicDao;
import com.velichkomarija4.simplemusicapp.model.Album;
import com.velichkomarija4.simplemusicapp.model.AlbumSong;
import com.velichkomarija4.simplemusicapp.model.Song;
import com.velichkomarija4.simplemusicapp.views.comments.CommentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private Album album;
    private AlbumsActivity albumsActivity;

    @NonNull
    private final SongsAdapter songsAdapter = new SongsAdapter();

    public static DetailAlbumFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        albumsActivity = (AlbumsActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu,
                                    MenuInflater inflater) {
        inflater.inflate(R.menu.comment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionComment) {
            Intent intent = new Intent(albumsActivity, CommentActivity.class);
            intent.putExtra(ALBUM_KEY, album.getId());
            startActivity(intent);
            albumsActivity.finish();
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler);
        refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        errorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable(ALBUM_KEY);
            if (album != null) {
                albumsActivity.setTitle(album.getName());
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(albumsActivity));
        recyclerView.setAdapter(songsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            refresher.setRefreshing(true);
            getAlbum();
        });
    }

    @SuppressLint("CheckResult")
    private void getAlbum() {
        ApiUtils.getApi()
                .getAlbum(album.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    getMusicDao().insertAlbum(album);
                    createAlbumSong(album);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getAlbumWithId(album.getId());
                    } else {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
                .doFinally(() -> refresher.setRefreshing(false))
                .subscribe(album -> {
                    errorView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (album.getSongs() != null) {
                        songsAdapter.addData(album.getSongs(), true);
                    } else {
                        List<Song> songs = getMusicDao().getAlbumSong(this.album.getId());
                        songsAdapter.addData(songs, true);
                    }
                }, throwable -> {
                    errorView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    showMessage(R.string.request_error);
                });
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(albumsActivity, string, Toast.LENGTH_LONG).show();
    }

    private MusicDao getMusicDao() {
        return ((Application) albumsActivity.getApplication()).getDataBase().getMusicDao();
    }

    private void createAlbumSong(Album album) {
        MusicDao musicDao = getMusicDao();
        List<AlbumSong> albumSongs = new ArrayList<>();

        for (Song song : album.getSongs()) {
            String id = album.getId() + String.valueOf(song.getId());
            AlbumSong albumSong = new AlbumSong(id, album.getId(), song.getId());
            albumSongs.add(albumSong);
        }

        musicDao.insertSongs(album.getSongs());
        musicDao.setLinksAlbumSongs(albumSongs);
    }
}
