package com.hyupb.letswalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity {

    FragmentTabHost tabHost;
    ViewPager pager;
    MyAdapter adapter;
    WaveLoadingView waveLoadingView;
    ImageView pauseBtn;
    TextView pauseText;

    Intent manboService;
    BroadcastReceiver receiver;

    boolean flag = StepCount.flag;
    String serviceData;
    int stepCount;
    int finalStep = StepCount.finalStep;

    //키,체중,이동거리,소모칼로리,걸은 시간...
    int cm = StepCount.cm;
    int kg = StepCount.kg;
    double km = StepCount.km;
    double kcal = StepCount.kcal;
    double walkingTimeS = StepCount.walkingTimeS;
    int walkingTimeM = StepCount.walkingTimeM;

    TextView kmTv;
    TextView kcalTv;
    TextView timeTv;

    public Intent getManboService() {
        return manboService;
    }

    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = findViewById(android.R.id.tabhost);

        manboService = new Intent(this, ManboService.class);
        receiver = new PlayingReceiver();

        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("today").setIndicator("TODAY"),DummyFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("report").setIndicator("REPORT"),DummyFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("trails").setIndicator("TRAILS"),DummyFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("more").setIndicator("MORE"),DummyFragment.class,null);


        pager = findViewById(R.id.pager);
        adapter = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


        //탭버튼을 선택했을때 ViewPager의 현재페이지 변경작업
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                switch (tag){
                    case "today":
                        pager.setCurrentItem(0,true);
                        break;
                    case "report":
                        pager.setCurrentItem(1,true);
                        break;
                    case "trails":
                        pager.setCurrentItem(2,true);
                        break;
                    case "more":
                        pager.setCurrentItem(3,true);
                        break;
                }
            }
        });

        //Page를 변경했을때 tab선택을 변경
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(!flag){
            IntentFilter mainFilter = new IntentFilter("com.hyupb.letswalk");
            registerReceiver(receiver, mainFilter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        flag = StepCount.flag;
    }

    class PlayingReceiver extends BroadcastReceiver {

        @Override

        public void onReceive(Context context, Intent intent) {

            //여기서 웨이브 뷰를 불러올거당
            waveLoadingView=pager.getChildAt(0).findViewById(R.id.today_waveLoadingView);

            Log.i("PlayingReceiver", "IN");

            serviceData = intent.getStringExtra("stepService");
            stepCount = Integer.parseInt(serviceData);
            StepCount.todayStep = stepCount;


//        이동거리(m) = ((키(cm) - 100)  * 걸음수)/100
//        마일당 칼로리(cal/mile) =  3.7103 + 0.2678*체중(kg) + (0.0359*(체중(kg)*60*0.0006213)*2)*체중(kg)
//        소비칼로리(cal) = 이동거리(m) * 마일당 칼로리(cal/mile) * 0.0006213

           new Thread(){

                @Override
                public void run() {

                    if(waveLoadingView!=null && stepCount!=0) {
                        km = ((((double)cm - 100d) * (double)stepCount) / 100d);//이동거리(m)
                        kcal = 3.7103 + 0.2678*kg + (0.0359*(kg*60*0.0006213)*2)*(double)kg;//마일당 칼로리(cal)
                        kcal = (double)Math.round(km * kcal * 0.0006213 * 100) / 100d;//소비 칼로리(cal)
                        km = km/1000d;

                        km = (double)Math.round(km*100d)/100d;//이동거리(km)

                        kmTv = pager.getChildAt(0).findViewById(R.id.today_km_tv);
                        kcalTv = pager.getChildAt(0).findViewById(R.id.today_Kcal_tv);
                        timeTv = pager.getChildAt(0).findViewById(R.id.today_time_tv);


                        walkingTimeS = stepCount/2d;
                        if(walkingTimeS >= 60d){
                           walkingTimeM = (int)(walkingTimeS/60d);
                           walkingTimeS = walkingTimeS%60d;
                        }

                        //계산값 static에 저장
                        StepCount.Step = stepCount;
                        StepCount.km = km;
                        StepCount.kcal = kcal;
                        StepCount.walkingTimeM = walkingTimeM;
                        StepCount.walkingTimeS = walkingTimeS;
                        StepCount.flag = flag;



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                kmTv.setText(""+km);
                                kcalTv.setText(""+kcal);
                                timeTv.setText(String.format("%02d:%02d",walkingTimeM,(int)Math.abs(walkingTimeS)));

                                waveLoadingView.setCenterTitle(serviceData);

                                if ( finalStep/10*StepCount.achievementToday <= stepCount) {
                                    waveLoadingView.setProgressValue(StepCount.achievementToday*10);
                                    StepCount.achievementToday++;

                                    if (StepCount.achievementToday == 11) {
                                        StepCount.achievementToday = 10;
                                    }
                                }

                                if(!flag){
                                    pauseBtn = pager.getChildAt(0).findViewById(R.id.today_pause_btn);
                                    pauseBtn.setImageResource(android.R.drawable.ic_media_pause);

                                    pauseText = pager.getChildAt(0).findViewById(R.id.today_pause_tv);
                                    pauseText.setText("pause");

                                    IntentFilter mainFilter = new IntentFilter("com.hyupb.letswalk");
                                    registerReceiver(receiver, mainFilter);
                                }
                            }
                        });
                    }

                }

            }.start();

        }

    }



    //일시정지 버튼
    public void clickSP(View v){

        waveLoadingView = pager.getChildAt(0).findViewById(R.id.today_waveLoadingView);

                if (flag) {
                    try {
                        pauseBtn = pager.getChildAt(0).findViewById(R.id.today_pause_btn);
                        pauseBtn.setImageResource(android.R.drawable.ic_media_pause);

                        pauseText = pager.getChildAt(0).findViewById(R.id.today_pause_tv);
                        pauseText.setText("pause");

                        flag = false;

                        waveLoadingView.setBottomTitle("");

                        IntentFilter mainFilter = new IntentFilter("com.hyupb.letswalk");
                        registerReceiver(receiver, mainFilter);
                        startService(manboService);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    try {
                        pauseBtn = pager.getChildAt(0).findViewById(R.id.today_pause_btn);
                        pauseBtn.setImageResource(android.R.drawable.ic_media_play);

                        pauseText = pager.getChildAt(0).findViewById(R.id.today_pause_tv);
                        pauseText.setText("play");

                        flag = true;

                        waveLoadingView.setBottomTitle("pause");

//                        unregisterReceiver(receiver);
//                        stopService(manboService);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                StepCount.flag = flag;

    }

}



