package com.hyupb.letswalk;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

public class WalkingTrails extends Fragment {

    String apikey = "f6734812b0034f179602153dba140b10";
    String walkUrl;

    ListView listView;
    ArrayAdapter adapter;
    ImageView iv;

    ArrayList<String> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_walking_trails,container,false);

        listView = view.findViewById(R.id.listview);
        adapter = new MyListAdapter(getContext(),R.layout.items,items);
        listView.setAdapter(adapter);

        iv = view.findViewById(R.id.iv);

        walkUrl= "https://newsapi.org/v2/top-headlines?country=kr&category=health&apiKey="+apikey;

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            JSONObject jsonObject = readJsonFromUrl(walkUrl);

                            JSONArray jsonArray = new JSONArray((jsonObject.getJSONArray("articles")).toString());
                            Log.i("aa",jsonArray.toString());

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jo = jsonArray.getJSONObject(i);

                                String imgUrl = jo.getString("urlToImage");
                                String title = jo.getString("title");
                                String content = jo.getString("description");
                                String source = jo.getString("url");

                                String a = "";

                                a = imgUrl +"&"+title+"&"+content+"&"+source;

                                items.add(a);

//                                items.add(imgUrl);
//                                items.add(title);
//                                items.add(content);
//                                items.add(source);
                            }

                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    Log.i("bb",items.get(0).toString());
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        return view;
    }

    private String jsonReadAll(Reader reader) throws IOException {

        StringBuilder sb = new StringBuilder();

        int cp;

        while ((cp = reader.read()) != -1) {

            sb.append((char) cp);

        }

        return sb.toString();

    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

        InputStream is = new URL(url).openStream();

        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String jsonText = jsonReadAll(rd);

            JSONObject json = new JSONObject(jsonText);

            return json;

        } finally {

            is.close();

        }

    }



}
