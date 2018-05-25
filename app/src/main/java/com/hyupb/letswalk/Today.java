package com.hyupb.letswalk;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import me.itangqi.waveloadingview.WaveLoadingView;

import static android.content.Context.MODE_PRIVATE;

public class Today extends Fragment {

    WaveLoadingView waveLoadingView;
    ImageView pauseImg;
    TextView pauseText;
    TextView kmTv;
    TextView kcalTv;
    TextView timeTv;

    ImageView resetImg;
    Intent manboService;
    BroadcastReceiver receiver;

    Typeface typeFace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_today,container,false);

        //폰트추가
        typeFace = Typeface.createFromAsset(view.getContext().getAssets(), "NanumPen.ttf");



        SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);
        StepCount.Step = pref.getInt("step",0);
        StepCount.finalStep = pref.getInt("finalStep",100);
        StepCount.achievementToday = pref.getInt("achievementToday",1);
        StepCount.cm = pref.getInt("cm",170);
        StepCount.kg = pref.getInt("kg",70);
        StepCount.km = Math.round((double)pref.getFloat("km",0)*100)/100d;
        StepCount.kcal = Math.round((double)pref.getFloat("kcal",0)*100)/100d;
        StepCount.walkingTimeM = pref.getInt("walkingTimeM",0);
        StepCount.walkingTimeS = (double)pref.getFloat("walkingTimeS",0);
        StepCount.flag = pref.getBoolean("flag",true);

        manboService = ((MainActivity)getContext()).getManboService();
        receiver = ((MainActivity)getContext()).getReceiver();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        waveLoadingView = getView().findViewById(R.id.today_waveLoadingView);
        pauseImg = getView().findViewById(R.id.today_pause_btn);
        pauseText = getView().findViewById(R.id.today_pause_tv);
        kmTv = getView().findViewById(R.id.today_km_tv);
        kcalTv = getView().findViewById(R.id.today_Kcal_tv);
        timeTv = getView().findViewById(R.id.today_time_tv);

        //폰트추가
        kcalTv.setTypeface(typeFace);
        kmTv.setTypeface(typeFace);
        timeTv.setTypeface(typeFace);

        waveLoadingView.setTopTitle("Goal Steps : "+StepCount.finalStep);
        waveLoadingView.setCenterTitle(StepCount.Step+"");
        waveLoadingView.setProgressValue((StepCount.achievementToday-1)*10);
        kmTv.setText(StepCount.km+"");
        kcalTv.setText(StepCount.kcal+"");
        timeTv.setText(String.format("%02d:%02d",StepCount.walkingTimeM,(int)Math.abs(StepCount.walkingTimeS)));

        if(StepCount.flag){
            pauseImg.setImageResource(android.R.drawable.ic_media_play);
            pauseText.setText("play");
        }else {
            pauseImg.setImageResource(android.R.drawable.ic_media_pause);
            pauseText.setText("pause");
        }

        resetImg = getView().findViewById(R.id.today_reset_iv);

        resetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StepCount.flag) {

                    getContext().unregisterReceiver(receiver);
                    getContext().stopService(manboService);

                    waveLoadingView.setBottomTitle("reset!!");
                    waveLoadingView.setProgressValue(0);
                    pauseImg.setImageResource(android.R.drawable.ic_media_play);
                    pauseText.setText("play");
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("step",StepCount.Step);
        editor.putInt("finalStep",StepCount.finalStep);
        editor.putInt("achievementToday",StepCount.achievementToday);
        editor.putInt("cm",StepCount.cm);
        editor.putInt("kg",StepCount.kg);
        editor.putFloat("km",(float)StepCount.km);
        editor.putFloat("kcal",(float)StepCount.kcal);
        editor.putInt("walkingTimeM",StepCount.walkingTimeM);
        editor.putFloat("walkingTimeS",(float)StepCount.walkingTimeS);
        editor.putBoolean("flag",StepCount.flag);

        editor.commit();
    }

}
