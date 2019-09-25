package com.example.vanhua.radarmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.radarmapview.RadarMapView;

import java.util.ArrayList;
import java.util.List;

public class RadarMapActivity extends AppCompatActivity {
    private RadarMapView radarMapView;
    private ArrayList<String> titleList;
    private List<Double> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_map);
        init();
    }

    private void init() {
        titleList=new ArrayList<>();
        data=new ArrayList<>();
        titleList.add("公民素养");
        titleList.add("空间智能");
        titleList.add("审美与表现");
        titleList.add("学习与兴趣");
        data.add(100.0);
        data.add(60.0);
        data.add(70.0);
        data.add(80.0);
        radarMapView=findViewById(R.id.radarMapView);
        radarMapView.setCount(titleList.size());
        radarMapView.setTitles(titleList);
        radarMapView.setData(data);

    }
}
