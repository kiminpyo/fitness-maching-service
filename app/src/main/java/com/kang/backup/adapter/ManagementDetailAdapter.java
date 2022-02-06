package com.kang.backup.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.backup.R;
import com.kang.backup.dialog.ManagementDialog;
import com.kang.backup.model.RequestModel;

import java.util.List;

public class ManagementDetailAdapter extends RecyclerView.Adapter<ManagementDetailAdapter.ViewHolder> {
    private Context mContext;
    // 수락 상태의 요청만 담음(승인한 요청)
    private List<RequestModel> managementLists;
    private boolean isTrainer;
    private String memo_key;
    private ImageView back_btn;

    public ManagementDetailAdapter(Context mContext, List<RequestModel> managementLists) {
        this.mContext = mContext;
        this.managementLists = managementLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ptclass_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SharedPreferences prefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        isTrainer= prefs.getBoolean("isTrainer", true);

        final RequestModel management = managementLists.get(i);

        viewHolder.data_view.setText(management.getRequestData());
        viewHolder.stime_view.setText(management.getRequestStime());
        viewHolder.etime_view.setText(management.getRequestEtime());
        viewHolder.area_view.setText(management.getArea());
        viewHolder.major_view.setText(management.getMajor());

        viewHolder.ptclass_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                ManagementDialog customDialog = new ManagementDialog(mContext, management.getReceiveUser(), management.getSendUser(), management.getRequestCnt());
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return managementLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView data_view, stime_view, etime_view, area_view, major_view;

        public CardView ptclass_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ptclass_card = itemView.findViewById(R.id.ptclass_card);
            data_view = itemView.findViewById(R.id.data_view);
            stime_view = itemView.findViewById(R.id.stime_view);
            etime_view = itemView.findViewById(R.id.etime_view);
            area_view = itemView.findViewById(R.id.area_view);
            major_view = itemView.findViewById(R.id.major_view);
        }
    }

}
