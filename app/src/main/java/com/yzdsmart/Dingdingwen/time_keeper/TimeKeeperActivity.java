package com.yzdsmart.Dingdingwen.time_keeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.SignProcessStep;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.scan_coin.QRScannerActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomRoundProgress;
import com.yzdsmart.Dingdingwen.views.SlideLockView;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
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
//LocationSource, AMapLocationListener,
public class TimeKeeperActivity extends BaseActivity implements TimeKeeperContract.TimeKeeperView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    //    @Nullable
//    @BindView(R.id.sign_map)
//    MapView signMapView;
    @Nullable
    @BindView(R.id.sign_process_list)
    UltimateRecyclerView signProcessRV;
    @Nullable
    @BindView(R.id.lock_progress)
    CustomRoundProgress lockProgressCRP;
    @Nullable
    @BindView(R.id.lock_btn)
    ImageButton lockBtnIB;
    @Nullable
    @BindView(R.id.count_timer)
    TextView countTimerTV;
    @Nullable
    @BindView(R.id.count_layout)
    RelativeLayout countLayoutRL;
    @Nullable
    @BindView(R.id.unlock_btn)
    SlideLockView unlockBtnSLV;

    private static final String TAG = "TimeKeeperActivity";

    private TimeKeeperContract.TimeKeeperPresenter mPresenter;

    private String activityCode = "";

//    private AMap mAMap;
//    private UiSettings mUiSettings;
//    //声明AMapLocationClient类对象
//    private AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    private OnLocationChangedListener mLocationListener = null;
//    //声明AMapLocationClientOption对象
//    private AMapLocationClientOption mLocationOption = null;
//    private CameraUpdate mLocationUpdate = null;
//    //定位坐标点
//    private Double locLatitude = null;
//    private Double locLongitude = null;
//    private Integer defaultZoomLevel = 19;
//
//    private static final int STROKE_COLOR = Color.argb(180, 152, 152, 152);
//    private static final int FILL_COLOR = Color.argb(10, 242, 0, 68);

    private Handler mHandler = new Handler();
    private Runnable lockRunnable = null;
    private static final Integer DEFAULT_MAX_PROCESS = 100;
    //锁屏进度
    private Integer lockProgress = 0;
    //判断是否离开锁屏按钮
    private Boolean isLockPressed = true;

    private Boolean isScreenLocked = false;

    private DateTimeFormatter mDateTimeFormatter;
    private Integer startDuration = 0;
    private CountTimer countTimer;

    private SignProcessAdapter signProcessAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<SignProcessStep> signProcessStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            activityCode = savedInstanceState.getString("activityCode");
        } else {
            activityCode = getIntent().getExtras().getString("activityCode");
        }

        mDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        signProcessStepList = new ArrayList<SignProcessStep>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        MobclickAgent.openActivityDurationTrack(false);

        new TimeKeeperPresenter(this, this);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
//        signMapView.onCreate(savedInstanceState);

//        initMap();

        signProcessAdapter = new SignProcessAdapter(this);
        mGridLayoutManager = new GridLayoutManager(this, 4);
        signProcessRV.setAdapter(signProcessAdapter);
        signProcessRV.setLayoutManager(mGridLayoutManager);
        signProcessRV.setHasFixedSize(true);
        signProcessRV.setSaveEnabled(true);
        signProcessRV.setClipToPadding(false);

        unlockBtnSLV.setmLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                isScreenLocked = false;
                ButterKnife.apply(countLayoutRL, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(titleLeftOpeIV, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(unlockBtnSLV, BUTTERKNIFEGONE);
            }
        });

        lockRunnable = new Runnable() {
            @Override
            public void run() {
                lockProgress += 2;
                lockProgressCRP.setProgress(lockProgress);
                if (DEFAULT_MAX_PROCESS == lockProgress) {
                    isScreenLocked = true;
                    ButterKnife.apply(titleLeftOpeIV, BUTTERKNIFEGONE);
                    ButterKnife.apply(countLayoutRL, BUTTERKNIFEGONE);
                    lockProgressCRP.setVisibility(View.GONE);
                    mHandler.removeCallbacks(this);
                    lockProgress = 0;
                    lockProgressCRP.setProgress(lockProgress);
                    ButterKnife.apply(unlockBtnSLV, BUTTERKNIFEVISIBLE);
                } else {
                    mHandler.postDelayed(this, 1);
                }
            }
        };

        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
            openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
            return;
        }
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getSignActivityList(Constants.SIGN_ACTIVITY_LIST_ACTION_CODE, "", SharedPreferencesUtils.getString(this, "cust_code", ""), activityCode, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
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
//        signMapView.onResume();
//        mLocationClient.startLocation();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        signMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
//        if (null != mLocationClient) {
//            mLocationListener = null;
//            mLocationClient.onDestroy();
//            mLocationClient = null;
//            mLocationOption = null;
//        }
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        signMapView.onDestroy();
        mHandler.removeCallbacks(lockRunnable);
        if (null != countTimer) {
            countTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            if (null == MainActivity.getInstance()) return;
            MainActivity.getInstance().chatLogin();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
//        signMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isScreenLocked) {
                    showSnackbar("请先解锁");
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
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
        mHandler.postDelayed(lockRunnable, 1);
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
//    private void initMap() {
//        if (null != mAMap) return;
//        mAMap = signMapView.getMap();
//        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                Constants.GEO_DEFAULT_CITY,//新的中心点坐标
//                defaultZoomLevel, //新的缩放级别
//                0, //俯仰角0°~45°（垂直与地图时为0）
//                0  ////偏航角 0~360° (正北方为0)
//        )));
//        // 设置定位监听
//        mAMap.setLocationSource(this);
//        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        mAMap.setMyLocationEnabled(true);
//        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
//        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        setupLocationStyle();
//        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//
//            }
//
//            @Override
//            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                defaultZoomLevel = (int) cameraPosition.zoom;
//            }
//        });
//        mUiSettings = mAMap.getUiSettings();
//        mUiSettings.setScaleControlsEnabled(false);
//        mUiSettings.setZoomControlsEnabled(false);
//    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (null == aMapLocation || null == signMapView) return;
//        if (0 != aMapLocation.getErrorCode()) return;
//        mLocationListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//        mLocationUpdate = null;
//        //获取当前位置坐标
//        locLatitude = aMapLocation.getLatitude();
//        locLongitude = aMapLocation.getLongitude();
//        LatLng ll = new LatLng(locLatitude, locLongitude);
//        mLocationUpdate = CameraUpdateFactory.newLatLngZoom(ll, defaultZoomLevel);
//        mAMap.moveCamera(mLocationUpdate);
//    }
//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        if (null == mLocationClient) {
//            //初始化定位
//            mLocationClient = new AMapLocationClient(App.getAppInstance());
//            mLocationListener = onLocationChangedListener;
//            mLocationClient.setLocationListener(this);
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            mLocationOption.setInterval(Constants.LOC_TIME);
//            //设置是否返回地址信息（默认返回地址信息）
//            mLocationOption.setNeedAddress(true);
//            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//            mLocationOption.setHttpTimeOut(Constants.LOC_TIME_OUT);
//            mLocationClient.setLocationOption(mLocationOption);
//        }
//    }
//
//    @Override
//    public void deactivate() {
//        if (null != mLocationClient) {
//            mLocationListener = null;
//            mLocationClient.onDestroy();
//            mLocationClient = null;
//            mLocationOption = null;
//        }
//    }
//
//    private void setupLocationStyle() {
//        // 自定义系统定位蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//                fromResource(R.mipmap.gps_point));
//        // 自定义精度范围的圆形边框颜色
//        myLocationStyle.strokeColor(STROKE_COLOR);
//        //自定义精度范围的圆形边框宽度
//        myLocationStyle.strokeWidth(2);
//        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(FILL_COLOR);
//        // 将自定义的 myLocationStyle 对象添加到地图上
//        mAMap.setMyLocationStyle(myLocationStyle);
//    }
    @Override
    public void setPresenter(TimeKeeperContract.TimeKeeperPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetSignActivityList(Boolean flag, String errorInfo, SignDataRequestResponse.DataBean data) {
        if (!flag) {
            showSnackbar(errorInfo);
            return;
        }
        if (null == data.getActiName() || "".equals(data.getActiName())) {
            showSnackbar("活动未开始");
            return;
        }
        String[] actiRoutes = null, goRoutes = null;
        if (null != data.getActiRoute() && !"".equals(data.getActiRoute())) {
            actiRoutes = data.getActiRoute().split(",");
        }
        if (null != data.getGoRoute() && !"".equals(data.getGoRoute())) {
            goRoutes = data.getGoRoute().split(",");
        }
        if (null != actiRoutes) {
            SignProcessStep signProcessStep = null;
            signProcessStepList.clear();
            if (null != goRoutes) {
                if (actiRoutes.length == goRoutes.length) {
                    for (int i = 0; i < actiRoutes.length; i++) {
                        if (i == (actiRoutes.length - 1)) {
                            signProcessStep = new SignProcessStep(actiRoutes[i], true, true);
                        } else {
                            signProcessStep = new SignProcessStep(actiRoutes[i], true, false);
                        }
                        signProcessStepList.add(signProcessStep);
                    }
                    signProcessAdapter.clearList();
                    signProcessAdapter.appendList(signProcessStepList);
                    if (null == data.getFirstDateTime() || "".equals(data.getFirstDateTime()) || null == data.getLastDateTime() || "".equals(data.getLastDateTime()))
                        return;
                    startDuration = Seconds.secondsBetween(mDateTimeFormatter.parseDateTime(data.getFirstDateTime()), mDateTimeFormatter.parseDateTime(data.getLastDateTime())).getSeconds();
                    countTimerTV.setText(formatTime((long) startDuration * 1000));
                } else {
                    for (int i = 0; i < goRoutes.length; i++) {
                        signProcessStep = new SignProcessStep(actiRoutes[i], true, false);
                        signProcessStepList.add(signProcessStep);
                    }
                    for (int i = goRoutes.length; i < actiRoutes.length; i++) {
                        if (i == (actiRoutes.length - 1)) {
                            signProcessStep = new SignProcessStep(actiRoutes[i], false, true);
                        } else {
                            signProcessStep = new SignProcessStep(actiRoutes[i], false, false);
                        }
                        signProcessStepList.add(signProcessStep);
                    }
                    signProcessAdapter.clearList();
                    signProcessAdapter.appendList(signProcessStepList);
                    if (null == data.getFirstDateTime() || "".equals(data.getFirstDateTime()))
                        return;
                    startDuration = Seconds.secondsBetween(mDateTimeFormatter.parseDateTime(data.getFirstDateTime()), new DateTime()).getSeconds();
                    countTimer = new CountTimer(1000) {
                        @Override
                        protected void onTick(long millisFly) {
                            super.onTick(millisFly);
                            countTimerTV.setText(formatTime(millisFly + startDuration * 1000));
                        }
                    };
                    countTimer.start();
                }
            } else {
                for (int i = 0; i < actiRoutes.length; i++) {
                    if (i == (actiRoutes.length - 1)) {
                        signProcessStep = new SignProcessStep(actiRoutes[i], false, true);
                    } else {
                        signProcessStep = new SignProcessStep(actiRoutes[i], false, false);
                    }
                    signProcessStepList.add(signProcessStep);
                }
                signProcessAdapter.clearList();
                signProcessAdapter.appendList(signProcessStepList);
            }
        }
    }

    /**
     * 毫秒转化时分秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;

        Long hour = ms / hh;
        Long minute = (ms - hour * hh) / mi;
        Long second = (ms - hour * hh - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        sb.append(hour >= 10 ? (hour + ":") : ("0" + hour + ":"));
        sb.append(minute >= 10 ? (minute + ":") : ("0" + minute + ":"));
        sb.append(second >= 10 ? second : "0" + second);
        return sb.toString();
    }
}
