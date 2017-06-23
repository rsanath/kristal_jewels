package com.talenttakeaways.kristaljewels.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.beans.Review;

import java.util.ArrayList;

/**
 * Created by sanath on 19/06/17.
 */

public class CommentsAdapter extends ArrayAdapter {

    Context context;
    ArrayList<Review> commentsList;

    public CommentsAdapter(Context context, ArrayList<Review> commentsList) {
        super(context, R.layout.comment_list_view, commentsList);
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View commentsRow = layoutInflater.inflate(R.layout.comment_list_view, parent, false);

        TextView commentAuthor = (TextView) commentsRow.findViewById(R.id.comment_author_name);
        TextView commentMessage = (TextView) commentsRow.findViewById(R.id.comment_message);

        commentAuthor.setText(commentsList.get(position).getReviewAuthorName());
        commentMessage.setText(commentsList.get(position).getReviewMessage());

        return commentsRow;

    }
}
