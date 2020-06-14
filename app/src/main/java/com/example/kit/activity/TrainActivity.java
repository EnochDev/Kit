//CPR_tutorial
package com.example.kit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kit.R;
import com.example.kit.Sentence;
import com.example.kit.SentenceAdapter;

import java.util.ArrayList;

public class TrainActivity extends AppCompatActivity {

    MediaPlayer player;
    Toolbar myToolbar;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releasePlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        final ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        sentences.add(new Sentence("0.开始", R.raw.a));
        sentences.add(new Sentence("1.环境", R.raw.b));
        sentences.add(new Sentence("2.检查患者", R.raw.c));
        sentences.add(new Sentence("3.1拍肩呼喊", R.raw.d));
        sentences.add(new Sentence("3.2喂喂", R.raw.e));
        sentences.add(new Sentence("4.1检查呼吸", R.raw.f));
        sentences.add(new Sentence("4.2数数", R.raw.g));
        sentences.add(new Sentence("5.大声呼喊1", R.raw.h));
        sentences.add(new Sentence("6.大声呼喊2", R.raw.i));
        sentences.add(new Sentence("7.检查颈部", R.raw.j));
        sentences.add(new Sentence("8.松开衣服", R.raw.k));
        sentences.add(new Sentence("9.按压开始", R.raw.l));
        sentences.add(new Sentence("10.找点", R.raw.m));
        sentences.add(new Sentence("11.按压数数", R.raw.n));
        sentences.add(new Sentence("12.口对口人工呼吸", R.raw.o));
        sentences.add(new Sentence("13.摆位置", R.raw.p));
        sentences.add(new Sentence("14.捏住鼻孔", R.raw.q));
        sentences.add(new Sentence("15.嘴唇", R.raw.r));
        sentences.add(new Sentence("16.离开", R.raw.s));
        sentences.add(new Sentence("17.观察胸廓", R.raw.t));
        sentences.add(new Sentence("18.松开手指", R.raw.u));
        sentences.add(new Sentence("19.判断", R.raw.v));
        sentences.add(new Sentence("20.观察", R.raw.w));


        SentenceAdapter itemsAdapter = new SentenceAdapter(this, sentences);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releasePlayer();
                Sentence sentence = sentences.get(position);
                player = MediaPlayer.create(TrainActivity.this, sentence.getAudioResourceID());
                player.start();

                player.setOnCompletionListener(completionListener);
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_point:
                Intent i;

                i = new Intent(TrainActivity.this, CameraActivity.class);

                startActivity(i);
                return true;
            case R.id.call120:
                AlertDialog isPhone = new AlertDialog.Builder(TrainActivity.this).create();
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
