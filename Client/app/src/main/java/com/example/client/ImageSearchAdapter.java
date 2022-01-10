package com.example.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.Holder> {

    private Context context;
    private List<ImageSearchDataClass.Image> list = new ArrayList<>();

    public ImageSearchAdapter(Context context, List<ImageSearchDataClass.Image> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image_search, parent, false);
        Holder holder = new Holder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        int itemposition = position;
        String thumbnail_url = list.get(itemposition).getImage_url();
        Glide.with(context).load(thumbnail_url).into(holder.thumbnail);
    }


    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public Holder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}
