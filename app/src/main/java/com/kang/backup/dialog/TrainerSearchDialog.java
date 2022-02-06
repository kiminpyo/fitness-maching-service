package com.kang.backup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.backup.R;

import java.util.ArrayList;
import java.util.List;

public class TrainerSearchDialog extends Dialog {
    private TrainerSearchDialogListener trainerSearchDialogListener;
    private Context context;

    String gender="", area="", major="";

    public TrainerSearchDialog(Context context, TrainerSearchDialogListener trainerSearchDialogListener) {
        super(context);
        this.context = context;
        this.trainerSearchDialogListener = trainerSearchDialogListener;
    }

    public interface TrainerSearchDialogListener {
        void clickBtn(String sGender, String sArea, String sMajor);
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        RadioButton gender_btn_1, gender_btn_2;
        RadioButton area_btn_1, area_btn_2, area_btn_3, area_btn_4, area_btn_5, area_btn_6 ,area_btn_7, area_btn_8;
        RadioButton major_btn_1, major_btn_2, major_btn_3, major_btn_4, major_btn_5, major_btn_6;
        Button save_btn, cancel_btn;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.search_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        gender_btn_1 = (RadioButton) dlg.findViewById(R.id.gender_btn1);
        gender_btn_2 = (RadioButton) dlg.findViewById(R.id.gender_btn2);
        area_btn_1 = (RadioButton) dlg.findViewById(R.id.area_btn1);
        area_btn_2 = (RadioButton) dlg.findViewById(R.id.area_btn2);
        area_btn_3 = (RadioButton) dlg.findViewById(R.id.area_btn3);
        area_btn_4 = (RadioButton) dlg.findViewById(R.id.area_btn4);
        area_btn_5 = (RadioButton) dlg.findViewById(R.id.area_btn5);
        area_btn_6 = (RadioButton) dlg.findViewById(R.id.area_btn6);
        area_btn_7 = (RadioButton) dlg.findViewById(R.id.area_btn7);
        area_btn_8 = (RadioButton) dlg.findViewById(R.id.area_btn8);
        major_btn_1 = (RadioButton) dlg.findViewById(R.id.major_btn1);
        major_btn_2 = (RadioButton) dlg.findViewById(R.id.major_btn2);
        major_btn_3 = (RadioButton) dlg.findViewById(R.id.major_btn3);
        major_btn_4 = (RadioButton) dlg.findViewById(R.id.major_btn4);
        major_btn_5 = (RadioButton) dlg.findViewById(R.id.major_btn5);
        major_btn_6 = (RadioButton) dlg.findViewById(R.id.major_btn6);

        save_btn = (Button) dlg.findViewById(R.id.save_btn);
        cancel_btn = (Button) dlg.findViewById(R.id.cancel_btn);

        // --------------gender---------------------------------------------------------------

        gender_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = gender_btn_1.getText().toString();
            }
        });

        gender_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = gender_btn_2.getText().toString();
            }
        });

        // --------------area---------------------------------------------------------------

        area_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_1.getText().toString();
            }
        });

        area_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_2.getText().toString();
            }
        });

        area_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_3.getText().toString();
            }
        });

        area_btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_4.getText().toString();
            }
        });

        area_btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_5.getText().toString();
            }
        });

        area_btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_6.getText().toString();
            }
        });

        area_btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_7.getText().toString();
            }
        });

        area_btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = area_btn_8.getText().toString();
            }
        });

        // --------------major---------------------------------------------------------------

        major_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_1.getText().toString();
            }
        });

        major_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_2.getText().toString();
            }
        });

        major_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_3.getText().toString();
            }
        });

        major_btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_4.getText().toString();
            }
        });

        major_btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_5.getText().toString();
            }
        });

        major_btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                major = major_btn_6.getText().toString();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trainerSearchDialogListener.clickBtn(gender, area, major);
                dlg.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
