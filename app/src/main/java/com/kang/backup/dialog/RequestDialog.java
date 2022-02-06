package com.kang.backup.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.backup.R;
import com.kang.backup.SignupActivity;
import com.kang.backup.fragment.RequestDetailFragment;
import com.kang.backup.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RequestDialog extends Dialog {
    private Context context;
    private String request_publisher;
    private String uid;
    private Animation tranlateToLeftAnim, tranlateFromLeftAnim, tranlateToRightAnim, tranlateFromRightAnim;
    SharedPreferences.Editor editor;

    String request_data="", request_stime="", request_etime="", request_txt="";

    String cnt;

    LinearLayout calendar_layout, stime_layout, etime_layout, request_layout, result_layout;

    CalendarView calendar_view;
    TimePicker stime_view,etime_view;
    EditText request_view;
    Button calendar_btn, calendar_back_btn, stime_btn, stime_back_btn, etime_btn, etime_back_btn, request_btn, request_back_btn;
    Button result_btn, result_back_btn;

    TextView result_calendar_view, result_stime_view,result_etime_view, result_request_view;

    Date currenttimeformat;
    String currenttime;
    HashMap<String,Object> hashMap = new HashMap<>();

    int stime, etime;

    public RequestDialog(Context context, String request_publisher) {
        super(context);
        this.context = context;
        this.request_publisher = request_publisher;
        editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "none");

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.request_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 레이아웃
        calendar_layout = dlg.findViewById(R.id.calendar_layout);
        stime_layout = dlg.findViewById(R.id.stime_layout);
        etime_layout = dlg.findViewById(R.id.etime_layout);
        request_layout = dlg.findViewById(R.id.request_layout);
        result_layout = dlg.findViewById(R.id.result_layout);

        // 데이터를 입력받을 뷰
        calendar_view = dlg.findViewById(R.id.calendar_view);
        stime_view = dlg.findViewById(R.id.stime_view);
        etime_view = dlg.findViewById(R.id.etime_view);
        request_view = dlg.findViewById(R.id.request_view);

        // 화면 이동을 위한 버튼
        calendar_btn = dlg.findViewById(R.id.calendar_btn);
        calendar_back_btn = dlg.findViewById(R.id.calendar_back_btn);
        stime_btn = dlg.findViewById(R.id.stime_btn);
        stime_back_btn = dlg.findViewById(R.id.stime_back_btn);
        etime_btn = dlg.findViewById(R.id.etime_btn);
        etime_back_btn = dlg.findViewById(R.id.etime_back_btn);
        request_btn = dlg.findViewById(R.id.request_btn);
        request_back_btn = dlg.findViewById(R.id.request_back_btn);
        result_btn = dlg.findViewById(R.id.result_btn);
        result_back_btn = dlg.findViewById(R.id.result_back_btn);

        // 유저 최종 확인
        result_calendar_view = dlg.findViewById(R.id.result_calendar_view);
        result_stime_view = dlg.findViewById(R.id.result_stime_view);
        result_etime_view = dlg.findViewById(R.id.result_etime_view);
        result_request_view = dlg.findViewById(R.id.result_request_view);

        //anim 폴더의 애니메이션을 가져와서 준비
        tranlateToLeftAnim = AnimationUtils.loadAnimation(context,R.anim.toleft);
        tranlateFromLeftAnim = AnimationUtils.loadAnimation(context,R.anim.fromleft);
        tranlateToRightAnim = AnimationUtils.loadAnimation(context,R.anim.toright);
        tranlateFromRightAnim = AnimationUtils.loadAnimation(context,R.anim.fromright);

        //페이지 슬라이딩 이벤트가 발생했을때 애니메이션이 시작 됐는지 종료 됐는지 감지할 수 있다.
        RequestDialog.SlidingPageAnimationListener animListener = new RequestDialog.SlidingPageAnimationListener();

        // 순번 저장
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(request_publisher).child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cnt = Long.toString(snapshot.getChildrenCount());
                System.out.println("ReceiveRequestCount : " + cnt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int j, int k) {
                request_data = i + " / " + j + " / " + k;
                hashMap.put("requestData", request_data);
                result_calendar_view.setText(request_data);
                stime_layout.setVisibility(View.VISIBLE);
                stime_layout.startAnimation(tranlateToLeftAnim);
                calendar_layout.setVisibility(View.GONE);
                calendar_layout.startAnimation(tranlateFromLeftAnim);
            }
        });

        stime_view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int j) {
                request_stime = i + " : " + j;
                stime = (i*60)+j;
            }
        });

        etime_view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int j) {
                request_etime = i + " : " + j;
                etime = (i*60)+j;
            }
        });

        currenttimeformat = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        currenttime = transFormat.format(currenttimeformat);
        System.out.println(currenttime);

        // 시작시간 화면 이동
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!request_data.equals("")) {

                }
            }
        });

        // 취소 (다이얼로그 종료)
        calendar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 종료시간 화면 이동
        stime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!request_stime.equals("")) {
                    hashMap.put("requestStime", request_stime);
                    result_stime_view.setText(request_stime);
                    etime_layout.setVisibility(View.VISIBLE);
                    etime_layout.startAnimation(tranlateToLeftAnim);
                    stime_layout.setVisibility(View.GONE);
                    stime_layout.startAnimation(tranlateFromLeftAnim);
                }
            }
        });

        // 뒤로 가기 (날짜시간 설정 화면)
        stime_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_layout.setVisibility(View.VISIBLE);
                calendar_layout.startAnimation(tranlateFromRightAnim);
                stime_layout.setVisibility(View.GONE);
                stime_layout.startAnimation(tranlateToRightAnim);
            }
        });

        // 요청사항 화면 이동
        etime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!request_etime.equals("")) {
                    if(stime >= etime) {
                        Toast.makeText(context, "잘못된 시간 설정입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        hashMap.put("requestEtime", request_etime);
                        result_etime_view.setText(request_etime);
                        request_layout.setVisibility(View.VISIBLE);
                        request_layout.startAnimation(tranlateToLeftAnim);
                        etime_layout.setVisibility(View.GONE);
                        etime_layout.startAnimation(tranlateFromLeftAnim);
                    }
                }
            }
        });

        // 뒤로 가기 (시작시간 설정 화면)
        etime_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stime_layout.setVisibility(View.VISIBLE);
                stime_layout.startAnimation(tranlateFromRightAnim);
                etime_layout.setVisibility(View.GONE);
                etime_layout.startAnimation(tranlateToRightAnim);
            }
        });

        // 명세 화면 이동
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_txt = request_view.getText().toString();

                // 해시맵에 데이터 추가
                hashMap.put("requestTxt", request_txt);
                // 명세창에 텍스트 추가
                result_request_view.setText(request_txt);
                // 화면 이동
                result_layout.setVisibility(View.VISIBLE);
                result_layout.startAnimation(tranlateToLeftAnim);
                request_layout.setVisibility(View.GONE);
                request_layout.startAnimation(tranlateFromLeftAnim);
            }
        });

        // 뒤로 가기 (종료시간 설정 화면)
        request_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etime_layout.setVisibility(View.VISIBLE);
                etime_layout.startAnimation(tranlateFromRightAnim);
                request_layout.setVisibility(View.GONE);
                request_layout.startAnimation(tranlateToRightAnim);
            }
        });

        // 데이터 베이스 전송
        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("currentTime", currenttime);
                hashMap.put("sendUser", uid);
                hashMap.put("receiveUser", request_publisher);
                // 상태 0 - 대기중, 1 - 수락, 2 - 거절
                hashMap.put("state", "0");

                // 관심사, 지역 정보 불러오기
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(request_publisher);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (getContext() == null) {
                            return;
                        }

                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        hashMap.put("major", user.getMajor());
                        hashMap.put("area", user.getArea());
                        hashMap.put("requestCnt", cnt);

                        editor.putString("request_cnt", cnt);
                        editor.apply();

                        sendRequest();
                        receiveRequest();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dlg.dismiss();
            }
        });

        // 취소(다이얼 로그 종료)
        result_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    }

    public void sendRequest() {
        // receive request(ReceiveRequest->트레이너id->유저id->번호) 저장
        // 받는 요청
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReceiveRequest").child(request_publisher).child(uid);
        reference.child(cnt).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("트레이너 데이터 베이스 저장 성공");
                }
            }
        });
    }

    public void receiveRequest() {
        // send request(SendRequest->유저id->트레이너id->번호) 저장
        // 보낸
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SendRequest").child(uid).child(request_publisher);
        reference.child(cnt).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("유저 데이터 베이스 저장 성공");

                    editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("request_publisher", request_publisher);
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,
                            new RequestDetailFragment()).commit();
                }
            }
        });
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
