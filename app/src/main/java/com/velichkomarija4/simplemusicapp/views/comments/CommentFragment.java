package com.velichkomarija4.simplemusicapp.views.comments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.velichkomarija4.simplemusicapp.ApiUtils;
import com.velichkomarija4.simplemusicapp.Application;
import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.db.MusicDao;
import com.velichkomarija4.simplemusicapp.model.Comment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private TextView noComments;
    private EditText commentEditText;
    private ImageButton sendButton;
    private CommentActivity commentActivity;
    private int albumId;
    private boolean firstPlay = true;

    @NonNull
    private final CommentsAdapter commentsAdapter = new CommentsAdapter();

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            refresher.setRefreshing(true);
            getComments();
        });
    }

    static CommentFragment newInstance(int album) {
        Bundle args = new Bundle();
        args.putInt(ALBUM_KEY, album);

        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setArguments(args);

        return commentFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        commentActivity = (CommentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        errorView = view.findViewById(R.id.errorView);
        noComments = view.findViewById(R.id.textView_noComment);
        commentEditText = view.findViewById(R.id.messageText);
        sendButton = view.findViewById(R.id.buttonSend);

        sendButton.setOnClickListener(view1 -> {
            sendComment();
        });

        commentEditText.setOnKeyListener((view12, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                sendComment();
                return true;
            }
            return false;
        });
    }

    private void sendComment() {
        String text = commentEditText.getText().toString();
        if (text.length() != 0) {
            Comment comment = new Comment(new Random().nextInt(50), albumId, text);
            postComment(comment);
        } else {
            showMessage(R.string.empty_message);
        }

    }

    @SuppressLint("CheckResult")
    private void postComment(Comment comment) {
        ApiUtils.getApi()
                .comment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    commentEditText.setText("");
                    onRefresh();
                })
                .subscribe(() -> {
                            // do nothing
                        }, throwable -> {
                            comment.setName(commentActivity
                                    .getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
                                    .getString("USER_NAME", ""));
                            Calendar calendar = Calendar.getInstance();
                            comment.setTime(android.text.format.DateFormat.format("yyyy-MM-dd'T'HH:mm:ssz",
                                    new java.util.Date()).toString());
                            getMusicDao().insertComment(comment);
                            showMessage(R.string.request_error);
                        }
                );
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        albumId = getArguments().getInt(ALBUM_KEY);

        recyclerView.setLayoutManager(new LinearLayoutManager(commentActivity));
        recyclerView.setAdapter(commentsAdapter);

        onRefresh();
    }

    @SuppressLint("CheckResult")
    private void getComments() {
        ApiUtils.getApi().getComments()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> {
                    getMusicDao().insertComments(comments);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        commentActivity.runOnUiThread(() ->showMessage(R.string.no_internrt_connection));
                        firstPlay = true;
                        return getMusicDao().getCommentsByAlbumId(albumId);
                    } else {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
                .doFinally(() -> {
                    refresher.setRefreshing(false);
                    firstPlay = false;
                })
                .subscribe(comments -> {
                    errorView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    List<Comment> commentList = getMusicDao().getCommentsByAlbumId(albumId);
                    if (commentList.size() != 0) {
                        boolean flagUpdate = commentsAdapter.addData(commentList, true);
                        if (!firstPlay) {
                            if (flagUpdate) {
                                showMessage(R.string.comments_update);
                            } else {
                                showMessage(R.string.no_new_comments);
                                                   }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noComments.setVisibility(View.VISIBLE);
                    }
                }, throwable -> {
                    errorView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    showMessage(R.string.no_comments);
                });

    }

    private MusicDao getMusicDao() {
        return ((Application) commentActivity.getApplication()).getDataBase().getMusicDao();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(commentActivity, string, Toast.LENGTH_LONG).show();
    }
}
