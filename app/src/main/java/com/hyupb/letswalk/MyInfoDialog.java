package com.hyupb.letswalk;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

public class MyInfoDialog extends Dialog{

    EditText dgName,dgKg,dgCm,dgFS;
    TextView currect,close;

    public MyInfoDialog(Context context,String name,int kg,int cm,int fs) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setContentView(R.layout.dialog_myinfo);

        dgName = findViewById(R.id.my_name);
        dgKg = findViewById(R.id.my_kg);
        dgCm = findViewById(R.id.my_cm);
        dgFS = findViewById(R.id.my_goal);

        currect = findViewById(R.id.my_currect);
        close = findViewById(R.id.my_close);

        currect.setOnClickListener(dgListner);
        close.setOnClickListener(dgListner);

        dgName.setText(name);
        dgKg.setText(kg+"");
        dgCm.setText(cm+"");
        dgFS.setText(fs+"");

    }

    View.OnClickListener dgListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.my_currect:
                    SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    if(!(dgKg.getText().toString().isEmpty())) {
                        editor.putString("name", dgName.getText().toString());
                        editor.putInt("kg", Integer.parseInt(dgKg.getText().toString()));
                        editor.putInt("cm", Integer.parseInt(dgCm.getText().toString()));
                        editor.putInt("finalStep",Integer.parseInt(dgFS.getText().toString()));
                        editor.commit();

                        Toast.makeText(getContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();

                        dismiss();
                    }else
                        Toast.makeText(getContext(), "정보를 입력하세요", Toast.LENGTH_SHORT).show();


                    break;

                case R.id.my_close:
                    dismiss();
                    break;
            }
        }
    };
}

