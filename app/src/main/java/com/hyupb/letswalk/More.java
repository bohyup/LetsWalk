package com.hyupb.letswalk;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class More extends Fragment {

    TextView info,scoreUpload,rank;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_more,container,false);

        info = view.findViewById(R.id.more_info);
        scoreUpload = view.findViewById(R.id.more_score_upload);
        rank = view.findViewById(R.id.more_rank);

        info.setOnClickListener(moreListner);
        scoreUpload.setOnClickListener(moreListner);
        rank.setOnClickListener(moreListner);

        return view;
    }



    View.OnClickListener moreListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.more_info:

                    break;

                case R.id.more_score_upload:



                    break;

                case R.id.more_rank:

                    break;
            }

        }
    };

}
