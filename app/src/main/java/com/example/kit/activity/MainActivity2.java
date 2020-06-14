package com.example.kit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.kit.activity.CircleMenuLayout.OnMenuItemClickListener;
import com.example.kit.R;

import static com.example.kit.activity.MainActivity.address1;
import static com.example.kit.activity.MainActivity.address2;

public class MainActivity2 extends AppCompatActivity {
    private CircleMenuLayout mCircleMenuLayout;         //自定义圆盘菜单
    private String[] mItemTexts = new String[]{"吃东西被噎住","触电","狗咬伤","流鼻血","烧伤或烫伤"};  //圆盘菜单显示文字
    private int[] mItemImgs = new int[]{R.drawable.choke,
            R.drawable.electric,R.drawable.dog,
            R.drawable.nosebleed,R.drawable.fire};     //圆盘菜单显示图片

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    Toolbar myToolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myToolbar2 = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar2);

        // 注册监听
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /**可选，设置定位模式，默认高精度LocationMode.Hight_Accuracy：高精度；* LocationMode. Battery_Saving：低功耗；LocationMode. Device_Sensors：仅使用设备；*/
        option.setCoorType("gcj02gcj02");
        /**可选，设置返回经纬度坐标类型，默认gcj02gcj02：国测局坐标；bd09ll：百度经纬度坐标；bd09：百度墨卡托坐标；海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标*/
        option.setScanSpan(1000);/**可选，设置发起定位请求的间隔，int类型，单位ms如果设置为0，则代表单次定位，即仅定位一次，默认为0如果设置非0，需设置1000ms以上才有效*/
        option.setOpenGps(true);/**可选，设置是否使用gps，默认false使用高精度和仅用设备两种定位模式的，参数必须设置为true*/
        option.setLocationNotify(true);/**可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false*/
        option.setIgnoreKillProcess(false);/**定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)*/
        option.setWifiCacheTimeOut(5 * 60 * 1000);/**可选，7.2版本新增能力如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位*/
        option.setEnableSimulateGps(false);/**可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false*/
        option.setIsNeedAddress(true);/**可选，设置是否需要地址信息，默认不需要*/
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setAddrType("all");
        mLocationClient.setLocOption(option);/**mLocationClient为第二步初始化过的LocationClient对象需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用*/

        // 启动定位
        mLocationClient.start();

        //初始化圆盘控件
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        //初始化圆盘控件菜单
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);
        //自定义控件单击事件
        mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Intent intent = new Intent(MainActivity2.this, AidActivity.class);
                intent.putExtra("pos", pos);
                startActivity(intent);
            }
            @Override
            //圆盘中间单击事件
            public void itemCenterClick(View view) {
                Toast.makeText(getApplicationContext(), "急救知识集锦", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public class MyLocationListener extends BDAbstractLocationListener {
        String    saveA = null;
        String    saveD = null;
        @Override
        public void onReceiveLocation(BDLocation location) {
            TextView title = (TextView)findViewById(R.id.location);
            String s = location.getAddrStr();
            String t = location.getLocationDescribe();
            if(saveA==null&&saveD==null&&s==null&&t==null&&address1==null&&address2==null){
                title.setText("\n\t\t\t\t\t\t\t\t\t\t\t\t\t定位中，请稍候...");
            }else{
                if(s!=null&&t!=null){
                    saveA = s;
                    saveD = t;
                    address1 = s;
                    address2 = t;
                }
                title.setText("\n您的位置是：" + address1 + "\n详细信息为：" + address2);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_point:
                Intent i;

                i = new Intent(MainActivity2.this, CameraActivity.class);

                startActivity(i);
                return true;
            case R.id.call120:
                AlertDialog isPhone = new AlertDialog.Builder(MainActivity2.this).create();
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
