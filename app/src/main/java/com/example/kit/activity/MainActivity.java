//CPR_entrance
/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.kit.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kit.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.example.kit.activity.EasyLog;

public class MainActivity extends AppCompatActivity {

    ImageView    callButton;
    Button    playCPRButton;
    ImageView searchButton;
    ImageView testButton;
    ImageView instructionButton;
    ImageView trainButton;
    //Switch    modeSwitch;

    public static String address1 = null;
    public static String address2 = null;

    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};

    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码

    AlertDialog mPermissionDialog;
    String mPackName = "com.example.kit.activity";

    private void initPermission(){
        mPermissionList.clear();//清空已经允许的没有通过的权限
        //逐个判断是否还有未通过的权限
        for (int i = 0;i<permissions.length;i++){
            if (ContextCompat.checkSelfPermission(this,permissions[i])!=
                    PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permissions[i]);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size()>0){//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this,permissions,mRequestCode);
        }else {
            //权限已经都通过了，可以将程序继续打开了
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode==requestCode){
            for (int i=0;i<grantResults.length;i++){
                if (grantResults[i]==-1){
                    hasPermissionDismiss=true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss){//如果有没有被允许的权限
            showPermissionDialog();
        }else {
            //权限已经都通过了，可以将程序继续打开了
        }
    }


    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }


    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();

        playCPRButton = (Button) findViewById(R.id.CPR_play_button);
        searchButton = (ImageView) findViewById(R.id.search);
        testButton = (ImageView) findViewById(R.id.test);
        instructionButton= (ImageView) findViewById(R.id.train);
        trainButton = (ImageView) findViewById(R.id.trainmode);
        //modeSwitch = (Switch) findViewById(R.id.modeSwitch);
        callButton = (ImageView)  findViewById(R.id.phone);

        EasyLog easyLog = new EasyLog();

        try{
            easyLog.appendLog("123456",getApplicationContext());
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }


        playCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j;
                j = new Intent(MainActivity.this, RunningActivity.class);
                startActivity(j);
            }
        });

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j;
                j = new Intent(MainActivity.this, TrainActivity.class);
                startActivity(j);
            }
        });

        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j;
                j = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(j);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j;
                j = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(j);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k;
                k = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(k);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isPhone = new AlertDialog.Builder(MainActivity.this).create();
                // 设置对bai话框标题
                isPhone.setTitle("系统提示");
                // 设置对话框消息
                isPhone.setMessage("确定要拨打120吗？\n\n120急救电话是紧急情况下求助的生命线，\n非紧急情况下不要随便拨打。\n\n拨打前请先了解当前地理位置和病人情况");
                // 添加选择按钮并注册监听
                isPhone.setButton("确定", listener);
                isPhone.setButton2("取消", listener);
                // 显示对话框
                isPhone.show();

            }
        });

    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    String phone = "120";//获取输入的电话号码
                    Intent intent = new Intent();//创建打电话的意图
                    intent.setAction(Intent.ACTION_CALL);//设置拨打电话的动作
                    intent.setData(Uri.parse("tel:" + phone));//设置拨打电话的号码
                    startActivity(intent);//开启打电话的意图
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };


}
