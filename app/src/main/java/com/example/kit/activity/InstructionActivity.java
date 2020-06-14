package com.example.kit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.example.kit.R;

/**
 * @创建者 LancerW
 * @创建时间 2020/6/11/011 23:11
 * @描述
 */
public class InstructionActivity extends Activity {

    //定义ImageView对象
    private ImageView       iv;
    //图片的下标
    private int             count = 0;
    //定义手势监听对象
    private GestureDetector gd;
    private int[]           photoIndex = new int[]{
            R.drawable.instruct1,R.drawable.instruct4,R.drawable.instruct3,R.drawable.instruct2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        //获取图片id
        iv = findViewById(R.id.photo);
        //OnGestureListener处理手势监听
        gd = new GestureDetector(this,OnGestureListener);
    }

    //当此Activity被触摸时回调,这里用到了触摸回调：onTouchEvent
    //按下回调是：onKeyDown，抬起回调：onKeyUp
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        //必须是true
        return true;
    }
    /**
     *自定义GestureDetector的手势识别监听器
     */
    private GestureDetector.OnGestureListener OnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        //滑屏:用户按下触摸屏、快速移动后松开.识别是滑屏后回调onFling方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //获取手指触碰坐标，计算是向左滑还是向右滑
            float x = e2.getX()-e1.getX();
            if(x>0){
                count ++;
                count = count%(photoIndex.length);
            }else if(x<0){
                count --;
                count = (count+(photoIndex.length))%(photoIndex.length);
            }
            //切换图片
            iv.setImageResource(photoIndex[count]);
            return true;
        }
    };
}
