package com.hyupb.letswalk;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;


import static android.content.Context.MODE_PRIVATE;

public class More extends Fragment {

    TextView info,scoreUpload,rank;

    private String name;
    private int step;

    Typeface typeFace;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_more,container,false);

        //폰트추가
        typeFace = Typeface.createFromAsset(view.getContext().getAssets(), "NanumPen.ttf");

        info = view.findViewById(R.id.more_info);
        scoreUpload = view.findViewById(R.id.more_score_upload);
        rank = view.findViewById(R.id.more_rank);

        info.setTypeface(typeFace);
        scoreUpload.setTypeface(typeFace);
        rank.setTypeface(typeFace);

        info.setOnClickListener(moreListner);
        scoreUpload.setOnClickListener(moreListner);
        rank.setOnClickListener(moreListner);

        SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);

        name = pref.getString("name","no name");
        step = StepCount.todayStep;

        return view;
    }



    View.OnClickListener moreListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.more_info:

                    SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);

                    String name1 = getName();
                    int kg = pref.getInt("kg",70);
                    int cm = pref.getInt("cm",170);
                    int finalStep = pref.getInt("finalStep",5000);

                    new MyInfoDialog(getContext(),name1,kg,cm,finalStep).show();

                    break;

                case R.id.more_score_upload:

                    String serverUrl = "http://tankbro.dothome.co.kr/letswalk/uploadDB.php";

                    String name = getName();
                    int step = getStep();

                    //insertDB.php에 파일전송요청 객체 생성
                    SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //insertDB.php의 echo결과 보여주기
                            new AlertDialog.Builder(getContext()).setMessage(response).setPositiveButton("OK",null).create().show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //요청객체에 데이터 추가하기
                    multiPartRequest.addStringParam("name",name);
                    multiPartRequest.addStringParam("step",step+"");

                    //요청큐 객체 생성
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    //요청큐에 요청객체 추가...
                    requestQueue.add(multiPartRequest);

                    break;

                case R.id.more_rank:
                    loadRanking();


                    break;
            }

        }
    };

    void loadRanking(){

        rankingDialog dialog = new rankingDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

}
