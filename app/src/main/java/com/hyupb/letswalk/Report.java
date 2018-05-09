package com.hyupb.letswalk;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    LineChartView daysChart,weeksChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report,container,false);

        daysChart = view.findViewById(R.id.days_chart);

        //x축 라벨 배열
        String[] days = new String[]{"sun","mon","tue","wen","thi","fri","set"};
        List<AxisValue> axisValue = new ArrayList<AxisValue>();

        List<PointValue> values = new ArrayList<PointValue>();
        for(int i=0;i<7;i++){
            values.add(new PointValue(i, StepCount.days[i]).setLabel(days[i]));
            axisValue.add(new AxisValue(i,days[i].toCharArray()));
        }


        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        for(int i=0;i<7;i++){
            data.setAxisXBottom(new Axis().setName("DAYS").setValues(axisValue));
        }

        data.setAxisYLeft(new Axis());


        daysChart.setLineChartData(data);
        //여까지 주차트//////////////////////////////////////////////////////////////

        weeksChart = view.findViewById(R.id.weeks_chart);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Calendar calendar = Calendar.getInstance();
        StepCount.days[calendar.get(Calendar.DAY_OF_WEEK)-1] = StepCount.todayStep;



        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(StepCount.days[calendar.get(Calendar.DAY_OF_WEEK)-1]+"day",StepCount.todayStep);

        editor.commit();
    }
}
