package com.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private List<Items> list;

    private Context context;

    public ListAdapter(Context context, List<Items> list) {
        this.context = context;
        this.list = list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_list_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
     final Items items = list.get(position);
        viewHolder.tvType.setText(items.getType());
        viewHolder.tvTitle.setText(items.getTitle());
        viewHolder.tvSortName.setText(items.getSortName());

        System.out.println("Image URL : "+items.getImageUrl());
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.signature(ObjectKey(signature))
                .override(100, 100); // resize does not respect aspect ratio
        Glide.with(context)
                .asBitmap()
                .load(items.getImageUrl())
                .apply(requestOptions)
                .into(viewHolder.thumbnail);
        //Picasso.with(context).load(items.getImageUrl()).into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvTitle, tvType,tvSortName;
            public ImageView thumbnail;
            public ViewHolder(@NonNull View itemView) {


                super(itemView);
                tvType = (TextView) itemView.findViewById(R.id.tvType);
                tvSortName = (TextView) itemView.findViewById(R.id.tvSortName);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            }
        }
}
