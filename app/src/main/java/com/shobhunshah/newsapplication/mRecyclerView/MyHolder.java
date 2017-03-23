package com.shobhunshah.newsapplication.mRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhunshah.newsapplication.R;

/**
 * Created by shobhun shah on 1/25/2017.
 */

public class MyHolder extends RecyclerView.ViewHolder
{
    TextView txtAuthor , txtTitle ,txtDescription ,txtUrl ;
    ImageView imgNewsItem ;

    public MyHolder(View itemView)
    {
        super(itemView);
        txtAuthor = (TextView) itemView.findViewById(R.id.txtAuthor);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
        txtUrl = (TextView) itemView.findViewById(R.id.txtUrl);
        imgNewsItem = (ImageView) itemView.findViewById(R.id.imgNewsItem);
    }
}
