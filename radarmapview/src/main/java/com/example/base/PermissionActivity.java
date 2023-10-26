package com.example.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;


public abstract class PermissionActivity extends Activity  {

    private final int REQUEST_CODE = 1000;
    private String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载自定义地图
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestPermission(permissions);
    }






    //权限申请
    protected  void requestPermission(String...permission){
        //当版本大于23申请动态权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(doRequestPermission(permission)){
            }
        }else {
        }

    }

    private boolean doRequestPermission(String...permissions) {
        boolean isPermission = true;
        List<String> permissionList=new ArrayList<>();
        for (String permission:permissions) {
            int result= ActivityCompat.checkSelfPermission(this,permission);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
                isPermission = false;
            }
        }
        if(permissionList.size() > 0){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),REQUEST_CODE);
        }
        return isPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:

                break;
                default:
                    break;
        }
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            if(this.getCurrentFocus()!=null){
                if(this.getCurrentFocus().getWindowToken()!=null){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }




}
