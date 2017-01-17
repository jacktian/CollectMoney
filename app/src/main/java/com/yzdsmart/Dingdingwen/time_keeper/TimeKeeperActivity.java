package com.yzdsmart.Dingdingwen.time_keeper;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.scan_coin.QRScannerActivity;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomRoundProgress;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Optional;
import dev.xesam.android.toolbox.timer.CountTimer;

/**
 * Created by YZD on 2017/1/17.
 */

public class TimeKeeperActivity extends BaseActivity implements LocationSource, AMapLocationListener {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.sign_map)
    MapView signMapView;
    @Nullable
    @BindView(R.id.lock_progress)
    CustomRoundProgress lockProgressCRP;
    @Nullable
    @BindView(R.id.lock_btn)
    ImageButton lockBtnIB;
    @Nullable
    @BindView(R.id.count_timer)
    TextView countTimerTV;

    private static final String TAG = "TimeKeeperActivity";

    private AMap mAMap;
    private UiSettings mUiSettings;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private OnLocationChangedListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private CameraUpdate mLocationUpdate = null;
    //定位坐标点
    private Double locLatitude = null;
    private Double locLongitude = null;
    private Integer defaultZoomLevel = 19;

    private static final int STROKE_COLOR = Color.argb(180, 152, 152, 152);
    private static final int FILL_COLOR = Color.argb(10, 242, 0, 68);

    private Handler mHandler = new Handler();
    private Runnable lockRunnable = null;
    private static final Integer DEFAULT_MAX_PROCESS = 3;
    //点名进度
    private Integer lockProgress = 0;
    //判断是否离开点名按钮
    private Boolean isLockPressed = true;

    private CountTimer countTimer;
    private DateTimeFormatter countTimerFormat = DateTimeFormat.forPattern("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        MobclickAgent.openActivityDurationTrack(false);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        signMapView.onCreate(savedInstanceState);

        initMap();

        lockRunnable = new Runnable() {
            @Override
            public void run() {
                if (DEFAULT_MAX_PROCESS == lockProgress) {
                    lockProgressCRP.setVisibility(View.GONE);
                    mHandler.removeCallbacks(this);
                    lockProgress = 0;
                    lockProgressCRP.setProgress(lockProgress);
                    return;
                }
                lockProgress++;
                lockProgressCRP.setProgress(lockProgress);
                mHandler.postDelayed(this, 1000);
            }
        };

        countTimer = new CountTimer(10) {
            @Override
            protected void onStart(long millisFly) {
                super.onStart(millisFly);
            }

            @Override
            protected void onCancel(long millisFly) {
                super.onCancel(millisFly);
            }

            @Override
            protected void onPause(long millisFly) {
                super.onPause(millisFly);
            }

            @Override
            protected void onResume(long millisFly) {
                super.onResume(millisFly);
            }

            @Override
            protected void onTick(long millisFly) {
                super.onTick(millisFly);
                countTimerTV.setText(countTimerFormat.print(millisFly));
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_time_keeper;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        signMapView.onResume();
        mLocationClient.startLocation();
        countTimer.start();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        signMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        if (null != mLocationClient) {
            mLocationListener = null;
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        signMapView.onDestroy();
        mHandler.removeCallbacks(lockRunnable);
        countTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        signMapView.onSaveInstanceState(outState);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.close_btn, R.id.scan_btn})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
            case R.id.close_btn:
                closeActivity();
                break;
            case R.id.scan_btn:
                bundle = new Bundle();
                bundle.putInt("scanType", 0);
                openActivity(QRScannerActivity.class, bundle, 0);
                closeActivity();
                break;
        }
    }

    @Optional
    @OnTouch(R.id.lock_btn)
    boolean startLock(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startLock();
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动离开点名按钮
                if (event.getX() < 0 || Utils.px2dp(this, event.getX()) > 50 || event.getY() < 0 || Utils.px2dp(this, event.getY()) > 50) {
                    isLockPressed = false;
                    submitLock();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isLockPressed) {
                    submitLock();
                }
                break;
        }
        return false;
    }

    /**
     * 开始锁定
     */
    private void startLock() {
        isLockPressed = true;
        lockProgressCRP.setVisibility(View.VISIBLE);
        mHandler.postDelayed(lockRunnable, 1000);
    }

    /**
     * 手指离开锁定按钮
     */
    private void submitLock() {
        lockProgressCRP.setVisibility(View.GONE);
        mHandler.removeCallbacks(lockRunnable);
        lockProgress = 0;
        lockProgressCRP.setProgress(lockProgress);
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        if (null != mAMap) return;
        mAMap = signMapView.getMap();
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                Constants.GEO_DEFAULT_CITY,//新的中心点坐标
                defaultZoomLevel, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
        // 设置定位监听
        mAMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setupLocationStyle();
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                defaultZoomLevel = (int) cameraPosition.zoom;
            }
        });
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null == aMapLocation || null == signMapView) return;
        if (0 != aMapLocation.getErrorCode()) return;
        mLocationListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
        mLocationUpdate = null;
        //获取当前位置坐标
        locLatitude = aMapLocation.getLatitude();
        locLongitude = aMapLocation.getLongitude();
        LatLng ll = new LatLng(locLatitude, locLongitude);
        mLocationUpdate = CameraUpdateFactory.newLatLngZoom(ll, defaultZoomLevel);
        mAMap.moveCamera(mLocationUpdate);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (null == mLocationClient) {
            //初始化定位
            mLocationClient = new AMapLocationClient(App.getAppInstance());
            mLocationListener = onLocationChangedListener;
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(Constants.LOC_TIME);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(Constants.LOC_TIME_OUT);
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    @Override
    public void deactivate() {
        if (null != mLocationClient) {
            mLocationListener = null;
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(2);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
    }
}
