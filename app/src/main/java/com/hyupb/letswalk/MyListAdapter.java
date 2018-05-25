package com.hyupb.letswalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by alfo6-18 on 2018-05-14.
 */

public class MyListAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> items;
    int i=0;

    int newsNum;

    Typeface typeFace;


    public MyListAdapter(@NonNull Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;

        typeFace = Typeface.createFromAsset(context.getAssets(), "NanumPen.ttf");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View listItem = inflater.inflate(R.layout.items,null,true);
        ImageView iv = listItem.findViewById(R.id.item_iv);
        TextView titleTv = listItem.findViewById(R.id.item_title);
        TextView contentTv = listItem.findViewById(R.id.item_content);
        TextView sourceTv = listItem.findViewById(R.id.item_source);
        //폰트추가
        titleTv.setTypeface(typeFace);
        contentTv.setTypeface(typeFace);
        sourceTv.setTypeface(typeFace);

            String[] item = items.get(i).split("&");

            Glide.with(context).load(item[0]).into(iv);
            titleTv.setText(item[1]);
            contentTv.setText(item[2]);
            sourceTv.setText(item[3]);

            //한번만 받아오기(일단 임시방편으로 오류막음)
            if(i==1)
            newsNum = Integer.parseInt(item[4]);

            Log.i("newsNum",newsNum+"");

            i++;
            if(i>1){
                if(i>newsNum)
                    i=0;
            }



        return listItem;
    }
}
