package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    private Context context;
    private List<SearchDataClass.Place> list = new ArrayList<>();
    private Button toMap;

    public SearchAdapter(Context context, List<SearchDataClass.Place> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_search, parent, false);
        Holder holder = new Holder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        int itemposition = position;
        holder.result.setText(list.get(itemposition).getPlace_name());
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_mainactivity = new Intent(context, MainActivity.class);
                intent_to_mainactivity.putExtra("x",list.get(itemposition).getX());
                Log.d("x",list.get(itemposition).getX());
                intent_to_mainactivity.putExtra("y",list.get(itemposition).getY());
                Log.d("y",list.get(itemposition).getY());
                intent_to_mainactivity.putExtra("place_name",list.get(itemposition).getPlace_name());
                intent_to_mainactivity.putExtra("CallType", 1);

                ((SearchActivity)context).setResult(Activity.RESULT_OK, intent_to_mainactivity);
                ((SearchActivity)context).finish();

                // intent_to_mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // context.startActivity(intent_to_mainactivity);

            }
        });
        holder.range.setText(list.get(itemposition).getDistance());
    }


    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        public TextView result;
        public TextView range;

        public Holder(View view) {
            super(view);
            result = (TextView) view.findViewById(R.id.result);
            toMap = (Button) view.findViewById(R.id.toMap);
            range = (TextView) view.findViewById(R.id.range);
        }
    }
}
