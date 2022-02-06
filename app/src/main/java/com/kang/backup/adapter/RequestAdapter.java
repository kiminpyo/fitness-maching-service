package com.kang.backup.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.fragment.RequestDetailFragment;
import com.kang.backup.fragment.TrainerDetailFragment;
import com.kang.backup.model.RequestModel;
import com.kang.backup.model.UserModel;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context mContext;
    private List<RequestModel> requestLists;
    private boolean isTrainer;
    private String request_publisher;
    private DatabaseReference rf;

    public RequestAdapter(Context mContext, List<RequestModel> requestList) {
        this.mContext = mContext;
        this.requestLists = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.request_item, viewGroup, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SharedPreferences prefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        this.isTrainer= prefs.getBoolean("isTrainer", true);
        final RequestModel request = requestLists.get(i);
        if(isTrainer){
            // 트레이너라면 요청을 보낸 유저(sendUser) 정보를 읽어옴
            request_publisher = request.getSendUser();
        } else {
            // 유저라면 요청 받은 트레이너(receiveUser,자신) 정보를 읽어옴
            request_publisher = request.getReceiveUser();
        }

        rf = FirebaseDatabase.getInstance().getReference("Users").child(request_publisher);
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);

                Glide.with(mContext).load(user.getImageurl()).into(viewHolder.profile_image);
                if(user.isTrainer())
                    viewHolder.nametext.setText(user.getUsername()+"트레이너님");
                else
                    viewHolder.nametext.setText(user.getUsername()+"회원님");
                viewHolder.gendertext.setText(user.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewHolder.areatext.setText(request.getArea());
        viewHolder.majortext.setText(request.getMajor());

        if(request.getState().equals("0")) {
            viewHolder.statetext.setText("대기중");
        } else if(request.getState().equals("1")) {
            viewHolder.statetext.setTextColor(Color.parseColor("#17F611"));
            viewHolder.statetext.setText("수락");
        } else if(request.getState().equals("2")) {
            viewHolder.statetext.setTextColor(Color.parseColor("#FF3036"));
            viewHolder.statetext.setText("거절");
        }

        viewHolder.datatext.setText(request.getCurrentTime());

        viewHolder.request_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                if(isTrainer){
                    // 트레이너라면 요청을 보낸 유저(sendUser) 정보를 읽어옴
                    editor.putString("request_publisher", request.getSendUser());
                } else {
                    // 유저라면 요청 받은 트레이너(receiveUser,자신) 정보를 읽어옴
                    editor.putString("request_publisher", request.getReceiveUser());
                }
                editor.putString("request_cnt", request.getRequestCnt());
                editor.apply();



                // 글 작성자의 프로필 프레그먼트로 화면 전환
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new RequestDetailFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_image;

        public CardView request_card;

        public TextView nametext, gendertext, areatext, majortext, datatext, statetext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            nametext = itemView.findViewById(R.id.nametext);
            gendertext = itemView.findViewById(R.id.gendertext);
            areatext = itemView.findViewById(R.id.areatext);
            majortext = itemView.findViewById(R.id.majortext);
            datatext = itemView.findViewById(R.id.datatext);
            statetext = itemView.findViewById(R.id.statetext);
            request_card = itemView.findViewById(R.id.request_card);
        }
    }

}
