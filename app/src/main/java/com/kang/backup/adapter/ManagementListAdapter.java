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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.fragment.ManagementDetailFragment;
import com.kang.backup.model.UserModel;

import java.util.HashMap;
import java.util.List;

public class ManagementListAdapter extends RecyclerView.Adapter<ManagementListAdapter.ViewHolder> {
    private Context mContext;
    // 수락 상태의 요청만 담음(승인한 요청)
    private List<HashMap> uidLists;
    private DatabaseReference rf, reference;

    public ManagementListAdapter(Context mContext, List<HashMap> uidLists) {
        this.mContext = mContext;
        this.uidLists = uidLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.management_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HashMap<String, String> request_info = uidLists.get(i);

        String request_publisher = request_info.get("request_publisher");
        String cnt = request_info.get("cnt");

        viewHolder.cnttext.setText(cnt);

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
                viewHolder.idtext.setText(user.getId());
                viewHolder.calltext.setText(user.getCallnum());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewHolder.management_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                // 접속자가 트레이너라면 유저id, 유저라면 트레이너id
                editor.putString("request_publisher", request_info.get("request_publisher"));
                editor.putString("cnt", request_info.get("cnt"));
                editor.putString("memoKey", request_info.get("memoKey"));
                editor.apply();
                // 관리 디테일 프레그먼트로 화면 전환
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new ManagementDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uidLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_image;

        public CardView management_card;

        public TextView nametext, gendertext, idtext, calltext, cnttext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            nametext = itemView.findViewById(R.id.nametext);
            gendertext = itemView.findViewById(R.id.gendertext);
            idtext = itemView.findViewById(R.id.idtext);
            calltext = itemView.findViewById(R.id.calltext);
            cnttext = itemView.findViewById(R.id.cnttext);
            management_card = itemView.findViewById(R.id.management_card);
        }
    }

}
