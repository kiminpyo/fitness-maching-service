package com.kang.backup.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.adapter.RequestAdapter;
import com.kang.backup.model.RequestModel;

import java.util.ArrayList;
import java.util.List;

public class RequestListFragment extends Fragment {

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private List<RequestModel> requestLists = new ArrayList<>();

    private RequestAdapter requestAdapter;

    String uid;

    boolean isTrainer;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        uid = prefs.getString("uid", "none");

        isTrainer = prefs.getBoolean("isTrainer", true);

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

        System.out.println("reference : "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    for(DataSnapshot requestSnapshot : snapshot.getChildren()) {
                        RequestModel request = requestSnapshot.getValue(RequestModel.class);
                        requestLists.add(request);
                    }

                }

                if(getContext() == null) {
                    return;
                } else {
                    System.out.println(requestLists.size());
                    requestAdapter = new RequestAdapter(getContext(), requestLists);

                    recyclerView.setAdapter(requestAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}