package com.example.vanhua.radarmap;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.base.BaseActivity;
import com.example.radarmapview.RadarMapView;


/**
 * @author CIDI
 */
public class MainActivity extends BaseActivity {


    private RadarMapView radarMap;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPerMissions(needPermissions);
        init();
    }

    private void init() {
        radarMap = findViewById(R.id.radarMap);
        radarMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RadarMapActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
