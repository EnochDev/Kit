//CPR_player
package com.example.kit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kit.R;

import java.util.concurrent.TimeUnit;

public class RunningActivity extends AppCompatActivity {

    Chronometer chronometer;
    Toolbar myToolbar;

    private MediaPlayer cprPlayer;
    private Handler myHandler = new Handler();
    private Button prCPRButton;
    private Button stopCPRButton;
    private Button foreCPRButton;
    private Button backCPRButton;
    private Button restartCPRButton;
    private SeekBar seekBar;
    private TextView curTime;
    private TextView endTime;
    private double startTime;
    private double finalTime;

    private int forwardTime = 3000;
    private int backwardTime = 3000;
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            startTime = cprPlayer.getCurrentPosition();
            curTime.setText(String.format("%d 分 %d 秒",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        prCPRButton = (Button) findViewById(R.id.CPR_pause_resume_button);
        stopCPRButton = (Button) findViewById(R.id.CPR_stop_button);
        foreCPRButton = (Button) findViewById(R.id.CPR_forward_button);
        backCPRButton = (Button) findViewById(R.id.CPR_backward_button);
        restartCPRButton = (Button) findViewById(R.id.CPR_restart_button);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        curTime = (TextView) findViewById(R.id.curTime);
        endTime = (TextView) findViewById(R.id.endTime);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (cprPlayer == null) {
            cprPlayer = MediaPlayer.create(this, R.raw.cpr_guide);
        }

        chronometer.start();
        startTime = 0;
        finalTime = cprPlayer.getDuration();
        seekBar.setMax((int) finalTime);


        endTime.setText(String.format("%d 分 %d 秒",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        curTime.setText(String.format("%d 分 %d 秒",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        cprPlayer.start();
        myHandler.postDelayed(UpdateSongTime, 100);

        prCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cprPlayer.isPlaying()) {
                    cprPlayer.pause();
                    prCPRButton.setText(R.string.resume);
                } else {
                    cprPlayer.start();
                    prCPRButton.setText(R.string.pause);
                    //myHandler.postDelayed(UpdateSongTime,100);
                }

            }
        });

        stopCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCPRPlayer();
                finish();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

           /* @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 拖动停止时调用
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 拖动开始时调用

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 拖动改变时调用
                // 获取seeKbar的当前值
                startTime = seekBar.getProgress();
               // cprPlayer.seekTo((int) startTime);

            }*/
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

           }

            //值改变前
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //值改变后
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                cprPlayer.seekTo(seekBar.getProgress());
            }
        });

        foreCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startTime + forwardTime <= finalTime) {
                    startTime += forwardTime;
                    Toast.makeText(getApplicationContext(), "您前进3秒", Toast.LENGTH_SHORT).show();
                } else {
                    startTime = finalTime;
                    Toast.makeText(getApplicationContext(), "您已前进到结束", Toast.LENGTH_SHORT).show();
                }
                cprPlayer.seekTo((int) startTime);
            }
        });

        backCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startTime - backwardTime >= 0) {
                    startTime -= backwardTime;
                    Toast.makeText(getApplicationContext(), "您后退3秒", Toast.LENGTH_SHORT).show();
                } else {
                    startTime = 0;
                    Toast.makeText(getApplicationContext(), "您已后退到开始", Toast.LENGTH_SHORT).show();
                }
                cprPlayer.seekTo((int) startTime);
            }
        });

        restartCPRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                Toast.makeText(getApplicationContext(), "您已重新开始！", Toast.LENGTH_SHORT).show();
                cprPlayer.seekTo((int) startTime);
                cprPlayer.start();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCPRPlayer();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (cprPlayer!=null &&cprPlayer.isPlaying()) {
            cprPlayer.pause();
            prCPRButton.setText(R.string.resume);
        }
    }

    private void releaseCPRPlayer() {
        myHandler.removeCallbacks(UpdateSongTime);
        if (cprPlayer != null) {
            cprPlayer.release();
        }
        cprPlayer = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_point:
                Intent i;

                i = new Intent(RunningActivity.this, CameraActivity.class);

                startActivity(i);
                return true;
            case R.id.call120:
                AlertDialog isPhone = new AlertDialog.Builder(RunningActivity.this).create();
                // 设置对bai话框标题
                isPhone.setTitle("系统提示");
                // 设置对话框消息
                isPhone.setMessage("确定要拨打120吗？\n\n120急救电话是紧急情况下求助的生命线，\n非紧急情况下不要随便拨打。\n\n拨打前请先了解当前地理位置和病人情况");
                // 添加选择按钮并注册监听
                isPhone.setButton("确定", listener);
                isPhone.setButton2("取消", listener);
                // 显示对话框
                isPhone.show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
