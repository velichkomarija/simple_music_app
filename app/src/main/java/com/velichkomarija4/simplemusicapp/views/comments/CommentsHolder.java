package com.velichkomarija4.simplemusicapp.views.comments;

import android.view.View;
import android.widget.TextView;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

class CommentsHolder extends RecyclerView.ViewHolder {

    private final String VAR_1_FORMAT = "yyyy-MM-dd";
    private final String VAR_2_FORMAT = "HH:mm:ss";
    private final String OLD_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz";

    private TextView mName;
    private TextView mTime;
    private TextView mMsg;

    CommentsHolder(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.textView_name);
        mTime = itemView.findViewById(R.id.textView_time);
        mMsg = itemView.findViewById(R.id.textView_message);
    }

    void bind(Comment item) {
        mName.setText(item.getName());
        mMsg.setText(item.getMessage());
        String time = item.getTime();

        DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -24);
        Date now = cal.getTime();

        Date d = cal.getTime();
        if (time != null) {
            try {
                d = formatter.parse(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (d.after(now)) {
                ((SimpleDateFormat) formatter).applyPattern(VAR_2_FORMAT);
            } else {
                ((SimpleDateFormat) formatter).applyPattern(VAR_1_FORMAT);
            }
            time = formatter.format(d);
        } else {
            time = "";
        }

        mTime.setText(time);
    }
}
