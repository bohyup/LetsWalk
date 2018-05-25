package com.hyupb.letswalk;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;


public class Report extends Fragment {

    LineChartView daysChart, monthChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report,container,false);

        daysChart = view.findViewById(R.id.days_chart);

        //데이터 불러오기
        SharedPreferences pref = getContext().getSharedPreferences("Data",MODE_PRIVATE);


        //x축 라벨 배열
        String[] days = new String[]{"sun","mon","tue","wen","thi","fri","set"};
        List<AxisValue> axisValue = new ArrayList<AxisValue>();

        List<PointValue> values = new ArrayList<PointValue>();
        for(int i=0;i<7;i++){
            values.add(new PointValue(i, StepCount.days[i]));
            axisValue.add(new AxisValue(i,days[i].toCharArray()));
        }


        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        for(int i=0;i<7;i++){
            data.setAxisXBottom(new Axis().setName("WEEK").setValues(axisValue));
        }


        daysChart.setLineChartData(data);
        //여까지 일차트//////////////////////////////////////////////////////////////c

        int[] month = {0,0,0,0,0,0,0,0,0,0,0,0};
        for(int i=0;i<month.length;i++){
            month[i] = pref.getInt(i+"month",0);
        }
        month[1] = 10;

        monthChart = view.findViewById(R.id.month_chart);

        List<PointValue> values2 = new ArrayList<>();
        List<AxisValue> axisValue2 = new ArrayList<AxisValue>();

        for(int i=0;i<month.length;i++){
            values2.add(new PointValue(i,month[i]));
            axisValue2.add(new AxisValue(i,((i+1)+"").toCharArray()));
        }

        Line line2 = new Line(values2).setColor(Color.BLUE).setCubic(true);
        List<Line> lines2 = new ArrayList<Line>();
        lines2.add(line2);

        LineChartData data2 = new LineChartData();
        data2.setLines(lines2);

        for(int i=0;i<month.length;i++){
            data2.setAxisXBottom(new Axis().setName("MONTH").setValues(axisValue2));
        }

        monthChart.setLineChartData(data2);
        Log.i("aaa", "aaa");
        Log.i("aaa", "aaa");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Calendar calendar = Calendar.getInstance();
        StepCount.days[calendar.get(Calendar.DAY_OF_WEEK)-1] = StepCount.todayStep;


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(StepCount.days[calendar.get(Calendar.DAY_OF_WEEK)-1]+"day",StepCount.days[calendar.get(Calendar.DAY_OF_WEEK)-1]);

        editor.commit();
    }
}
