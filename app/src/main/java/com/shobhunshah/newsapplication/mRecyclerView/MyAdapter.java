package com.shobhunshah.newsapplication.mRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shobhunshah.newsapplication.NewsItems.ItemDisplayedOnCard;
import com.shobhunshah.newsapplication.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by shobhun shah on 1/25/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder>
{
    Context context;
    List<ItemDisplayedOnCard> onCard = Collections.emptyList();

    public MyAdapter(Context context, List<ItemDisplayedOnCard> onCard)
    {
        this.context = context;
        this.onCard = onCard;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_news_item,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position)
    {
        ItemDisplayedOnCard item = onCard.get(position);
        holder.txtTitle.setText(item.title);
        holder.txtAuthor.setText(item.author);
        holder.txtDescription.setText(item.description);
        holder.txtUrl.setText(item.url);
        Picasso.with(context).load(item.urlToImage).into(holder.imgNewsItem);

        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/BurgerFrogDEMO.otf");
        holder.txtTitle.setTypeface(type);
        Typeface type1 = Typeface.createFromAsset(context.getAssets(),"fonts/beyond_the_mountains.ttf");
        holder.txtDescription.setTypeface(type1);

        // Handles click on Url
        holder.txtUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent webViewIntent = new Intent(Intent.ACTION_VIEW);
                webViewIntent.setData(Uri.parse(onCard.get(position).url));

                if (webViewIntent.resolveActivity(context.getPackageManager()) != null)
                {
                    context.startActivity(webViewIntent);
                }
                else
                {
                    Toast.makeText(context,"No application to show!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return onCard.size();
    }
}
