package com.example.mycalendar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private Activity context;
    private List<mainData> dataList;

    public MyAdapter(List<mainData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myadapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        mainData data = dataList.get(position);
        holder.textViewEvent.setText(data.getEvent());
        holder.textViewHours.setText(data.getHours());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEvent;
        TextView textViewHours;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEvent=itemView.findViewById(R.id.TV_ADAPTER_EVENT);
            textViewHours=itemView.findViewById(R.id.TV_ADAPTER_HOURS);


        }
    }
}

