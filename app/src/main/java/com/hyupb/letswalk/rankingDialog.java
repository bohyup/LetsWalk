package com.hyupb.letswalk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alfo6-18 on 2018-05-21.
 */

public class rankingDialog extends Dialog {

    TextView tv,okTv;

    String serverUrl;

    StringBuffer rankingBuffer;

    public rankingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ranking);

        rankingBuffer = new StringBuffer();

        serverUrl="http://tankbro.dothome.co.kr/letswalk/loadRanking.php";
        tv=findViewById(R.id.ranking_tv);
        okTv = findViewById(R.id.ranking_tv_ok);

        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        new Thread(){
            @Override
            public void run() {

                try {
                        URL url = new URL(serverUrl);

                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setDoInput(true);
                        connection.setUseCaches(false);

                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isr);

                        StringBuffer buffer = new StringBuffer();
                        String line = reader.readLine();


                        while (true){
                            buffer.append(line);

                            line = reader.readLine();
                            if(line==null) break;

                            buffer.append("\n");
                        }

                        //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리
                        String[] rows = buffer.toString().split(";");

                        for(int i=0;i<rows.length;i++){
                            //여기 인덱스 초과 오류 및 setText가 안먹었음
                            String[] datas = rows[i].split("&");

                            if(datas.length!=2){

                                continue;
                            }



                            String name = datas[0];
                            int step = Integer.parseInt(datas[1]);

                            rankingBuffer.append(name +"             "+step +"\n");

                            if(i==9) break;
                        }

                    tv.setText(rankingBuffer.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }


}
