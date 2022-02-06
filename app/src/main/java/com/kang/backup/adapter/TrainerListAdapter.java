package com.kang.backup.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.kang.backup.R;
import com.kang.backup.fragment.TrainerDetailFragment;
import com.kang.backup.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerListAdapter.myViewHolder> {
    public Context mContext;
    public List<UserModel> mList;

    public TrainerListAdapter(Context mContext, List<UserModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final UserModel user = mList.get(position);
        holder.id.setText(user.getId());
        holder.username.setText(user.getUsername());
        holder.area.setText(user.getArea());
        holder.major.setText(user.getMajor());


        Glide.with(holder.img.getContext())
                .load(user.getImageurl())
                .placeholder(R.drawable.common_full_open_on_phone)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.trainer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SharedPreferences.Editor 공유 데이터(세션)에 Key : Value("profileid" : 글 작성자) 삽입
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("publisher", user.getPublisher());
                editor.apply();

                // 글 작성자의 프로필 프레그먼트로 화면 전환
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                        new TrainerDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

class myViewHolder extends RecyclerView.ViewHolder{

        //  CircleImageView ;
        TextView id, major, username, area, gender;
        CircleImageView img;
        RelativeLayout trainer_card;

        public myViewHolder(@NonNull View itemview){
            super(itemview);
            area = (TextView)itemview.findViewById((R.id.areatext));
            major = (TextView) itemview.findViewById(R.id.majortext);
            img = (CircleImageView)itemview.findViewById(R.id.img1);
            id = (TextView) itemview.findViewById(R.id.emailtext);
            username = (TextView) itemview.findViewById(R.id.nametext);
            trainer_card = (RelativeLayout) itemview.findViewById(R.id.trainer_card);
        }
    }
}
