package com.kang.backup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.backup.R;

import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.ViewHolder> {

    private Context mContext;
    private List<String> intoLists;

    public TrainerAdapter(Context mContext, List<String> intoLists) {
        this.mContext = mContext;
        this.intoLists = intoLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trainer_item, viewGroup, false);
        return new TrainerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String str = intoLists.get(i);
        viewHolder.trainer_txt.setText(str);
    }

    @Override
    public int getItemCount() {
        return intoLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView trainer_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trainer_txt = itemView.findViewById(R.id.trainer_txt);
        }
    }
}
