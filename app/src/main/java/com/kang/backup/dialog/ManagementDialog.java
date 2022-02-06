package com.kang.backup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.LoginActivity;
import com.kang.backup.MypageActivity;
import com.kang.backup.R;
import com.kang.backup.TrainerMainActivity;
import com.kang.backup.UserMainActivity;
import com.kang.backup.model.MemoModel;
import com.kang.backup.model.RequestModel;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ManagementDialog extends Dialog {
    private TextView calendar_view, stime_view, etime_view, request_view;
    private boolean isTrainer;
    private EditText memo_view;
    private Button cancel_btn, save_btn;

    private Context context;

    private String receiveuser, senduser, cnt, memo_key;

    public ManagementDialog(Context context, String receiveuser, String senduser, String cnt) {
        super(context);
        this.context = context;
        this.receiveuser = receiveuser;
        this.senduser = senduser;
        this.cnt = cnt;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.management_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        SharedPreferences prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        isTrainer= prefs.getBoolean("isTrainer", true);

        calendar_view = dlg.findViewById(R.id.calendar_view);
        stime_view = dlg.findViewById(R.id.stime_view);
        etime_view = dlg.findViewById(R.id.etime_view);
        request_view = dlg.findViewById(R.id.request_view);
        memo_view = dlg.findViewById(R.id.memo_view);
        cancel_btn = dlg.findViewById(R.id.cancel_btn);
        save_btn = dlg.findViewById(R.id.save_btn);

        memo_key = receiveuser+"_"+senduser+"_"+cnt;

        if(memo_view.getText() == null) {
            if(isTrainer) {
                memo_view.setHint("메모를 작성해 주세요.");
            } else {
                memo_view.setHint("작성된 메모가 없습니다.");
            }
        }

        // 요청 정보 추출
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(receiveuser).child(senduser).child(cnt);
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.getValue() == null)) {
                    RequestModel request = snapshot.getValue(RequestModel.class);
                    calendar_view.setText(request.getRequestData());
                    stime_view.setText(request.getRequestStime());
                    stime_view.setText(request.getRequestEtime());
                    request_view.setText(request.getRequestTxt());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Memo").child(memo_key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    MemoModel memo = snapshot.getValue(MemoModel.class);
                    memo_view.setText(memo.getMemoContext());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!memo_view.getText().equals("")) {
                    System.out.println("메모 저장");
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("memoContext", memo_view.getText().toString());
                    hashMap.put("memoKey", memo_key);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Memo").child(memo_key);
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                System.out.println("메모 저장 성공");
                                Toast.makeText(context, "저장을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                memo_view.setEnabled(false);
                                memo_view.clearFocus();
                            } else {
                            }
                        }
                    });

                }
            }
        });

    }


}
