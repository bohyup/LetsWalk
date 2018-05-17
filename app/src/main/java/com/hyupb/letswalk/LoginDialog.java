package com.hyupb.letswalk;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alfo6-18 on 2018-05-17.
 */

public class LoginDialog extends Dialog{

    EditText dgName,dgKg,dgCm;
    TextView login,exit;

    public LoginDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경 투명
        setContentView(R.layout.login_dialog);

        dgName = findViewById(R.id.login_name);
        dgKg = findViewById(R.id.login_kg);
        dgCm = findViewById(R.id.login_cm);

        login = findViewById(R.id.login_login);
        exit = findViewById(R.id.login_exit);

        login.setOnClickListener(dgListner);
        exit.setOnClickListener(dgListner);

    }

    View.OnClickListener dgListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.login_login:
                    SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);

                    Log.d("Is first Time?", "first");
                    SharedPreferences.Editor editor = pref.edit();

                    if(!(dgKg.getText().toString().isEmpty())) {
                        editor.putString("name", dgName.getText().toString());
                        editor.putInt("kg", Integer.parseInt(dgKg.getText().toString()));
                        editor.putInt("cm", Integer.parseInt(dgCm.getText().toString()));
                        editor.putBoolean("isFirst", true);
                        editor.commit();

                        dismiss();
                    }else
                        Toast.makeText(getContext(), "정보를 입력하세요", Toast.LENGTH_SHORT).show();


                    break;

                case R.id.login_exit:
                    dismiss();


                    break;
            }
        }
    };
}

