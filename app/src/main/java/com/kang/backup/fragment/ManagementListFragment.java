package com.kang.backup.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.adapter.ManagementListAdapter;
import com.kang.backup.model.RequestModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagementListFragment extends Fragment {

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private List<HashMap> uidLists = new ArrayList<>();

    private ManagementListAdapter managementListAdapter;

    private TextView title;

    String uid;

    boolean isTrainer;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_management_list, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        uid = prefs.getString("uid", "none");

        isTrainer = prefs.getBoolean("isTrainer", true);

        title = view.findViewById(R.id.title);

        if(isTrainer)
            title.setText("매칭 회원 목록");
        else
            title.setText("매칭 트레이너 목록");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        getRequestList();

        return view;
    }

    private void getRequestList() {

        if(isTrainer) {
            // 보는사람이 트레이너라면 receiveRequest에 접근
            reference = FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(uid);
            System.out.println("트레이너 접근");
        } else {
            // 보는사람이 트레이너라면 sendRequest에 접근
            reference = FirebaseDatabase.getInstance().getReference("SendRequest").child(uid);
            System.out.println("유저 접근");
        }

        System.out.println(uid);
        System.out.println("reference : "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    HashMap<String, String> uList = new HashMap<>();
                    int cnt = 0;
                    boolean flag = false;
                    for(DataSnapshot sn : snapshot.getChildren()) {
                        RequestModel request = sn.getValue(RequestModel.class);
                        // 요청 상태가 수락(1)이면 담는다.
                        if(request.getState().equals("1")) {
                            flag = true;
                            // 수락 상태가 1인 데이터의 갯수
                            cnt++;
                            uList.put("memoKey",request.getMemoKey());
                        }
                    }

                    String user_id = snapshot.getKey();
                    if(flag) {
                        System.out.println(user_id+" : "+cnt);

                        uList.put("request_publisher", user_id);
                        uList.put("cnt",  Integer.toString(cnt));
                        uidLists.add(uList);
                    }
                }
                if(getContext() == null) {
                    return;
                } else {
                    System.out.println("uidLists Size : "+uidLists.size());

                    managementListAdapter = new ManagementListAdapter(getContext(), uidLists);

                    recyclerView.setAdapter(managementListAdapter);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}