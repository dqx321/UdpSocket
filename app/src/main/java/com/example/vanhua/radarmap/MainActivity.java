package com.example.vanhua.radarmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.radarmapview.RadarMapView;


public class MainActivity extends AppCompatActivity {


   private RadarMapView radarMap;
   //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        radarMap=findViewById(R.id.radarMap);
        radarMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RadarMapActivity.class));
            }
        });
    }


}
