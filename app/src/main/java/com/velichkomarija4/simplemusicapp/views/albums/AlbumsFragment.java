package com.velichkomarija4.simplemusicapp.views.albums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.velichkomarija4.simplemusicapp.AlbumsActivity;
import com.velichkomarija4.simplemusicapp.ApiUtils;
import com.velichkomarija4.simplemusicapp.Application;
import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.db.MusicDao;
import com.velichkomarija4.simplemusicapp.views.album.DetailAlbumFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlbumsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private AlbumsActivity albumsActivity;

    @NonNull
    private final AlbumsAdapter albumsAdapter = new AlbumsAdapter(album -> {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager !=null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetailAlbumFragment.newInstance(album))
                    .addToBackStack(DetailAlbumFragment.class.getSimpleName())
                    .commit();
        }
    });

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
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
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        errorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albumsActivity.setTitle(R.string.albums);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(albumsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            refresher.setRefreshing(true);
            getAlbums();
        });
    }

    @SuppressLint("CheckResult")
    private void getAlbums() {
        ApiUtils.getApi().getAlbums()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(albums -> {
                    getMusicDao().insertAlbums(albums);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getAlbums();
                    } else {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
                .doFinally(() -> refresher.setRefreshing(false))
                .subscribe(albums -> {
                    errorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    albumsAdapter.addData(albums, true);
                }, throwable -> {
                    errorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    showMessage(R.string.response_code_400_alt);
                });
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private MusicDao getMusicDao() {
        return ((Application) albumsActivity.getApplication()).getDataBase().getMusicDao();
    }
}
