package com.yzdsmart.Dingdingwen.main.find_money;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.BaseFragment;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.amap.WalkRouteOverlay;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.main.recommend.RecommendFragment;
import com.yzdsmart.Dingdingwen.scan_coin.QRScannerActivity;
import com.yzdsmart.Dingdingwen.shop_details.ShopDetailsActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.navi_picker.APPUtil;
import com.yzdsmart.Dingdingwen.views.navi_picker.NaviPickerDialog;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/17.
 */
public class FindMoneyFragment extends BaseFragment implements FindMoneyContract.FindMoneyView, RouteSearch.OnRouteSearchListener {
    @Nullable
    @BindViews({R.id.title_left_operation_layout, R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.market_name)
    TextView marketNameTV;
    @Nullable
    @BindView(R.id.find_money_map)
    MapView findMoneyMap;
    @Nullable
    @BindView(R.id.find_operation_layout)
    LinearLayout findOperationLayout;
    @Nullable
    @BindView(R.id.route_plane_layout)
    LinearLayout routePlaneLayout;
    @Nullable
    @BindView(R.id.plane_type)
    TextView planeTypeTV;
    @Nullable
    @BindView(R.id.plane_duration)
    TextView planeDurationTV;
    @Nullable
    @BindView(R.id.plane_distance)
    TextView planeDistanceTV;
    @Nullable
    @BindView(R.id.route_ope_layout)
    RelativeLayout routeOpeLayout;

    private static final String TAG = "FindMoneyFragment";

    private FragmentManager fm;

    //周边商铺检索参数
    private static final Integer PAGE_SIZE = 10;//分页数量
    private Integer page_index = 1;//分页索引 当前页标

    //获取当前用户周边用户
    private static final Integer PERSON_PAGE_SIZE = 10;
    private Integer personPageIndex = 0;

    private FindMoneyContract.FindMoneyPresenter mPresenter;

    //检索到的位置列表信息
    List<Marker> coinsMarkerList = null;

    //默认城市经纬度31.816058, 119.980524
    private static final LatLng GEO_DEFAULT_CITY = new LatLng(31.816058, 119.980524);
    //定位频率
    private static final Long LOC_TIME = 5000l;//毫秒
    private static final Long LOC_TIME_OUT = 5000l;//毫秒
    private Integer zoomDistance = 500;
    //定位坐标点
    private Double locLatitude = null;
    private Double locLongitude = null;
    private String qLocation = "";//检索中心点
    // 是否首次定位
    private boolean isFirstLoc = true;
    //上传坐标时间间隔次数
    private Integer uploadCoorCounts = 0;
    private Integer isAlreadyLocScan = 0;
    private Integer isAlreadyMarketScan = 0;
    private boolean isFirstMapAnimate = true;//是否是点击扫描按钮/商场附近商铺扫描
    private String lastMapStatusLocation = "";//记录地图状态改变后坐标

    private Marker marketMarker;//商场Marker

    private RouteSearch mRouteSearch;
    private boolean isOnRoutePlane = false;
    private LatLng routeTargetLocation;

    private WalkRouteOverlay walkingRouteOverlay;
    private boolean isSearchRoute = false;

    private AMap mAMap;
    private UiSettings mUiSettings;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coinsMarkerList = new ArrayList<Marker>();

        fm = getFragmentManager();

        new FindMoneyPresenter(getActivity(), this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_find_money;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.apply(hideViews, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        centerTitleTV.setText(getActivity().getResources().getString(R.string.app_name));
        centerTitleTV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.find_money_title_logo), null, null, null);
        ButterKnife.apply(findOperationLayout, isOnRoutePlane ? ((BaseActivity) getActivity()).BUTTERKNIFEGONE : ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, isOnRoutePlane ? ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE : ((BaseActivity) getActivity()).BUTTERKNIFEGONE);

        findMoneyMap.onCreate(savedInstanceState);

        //初始化地图
        initMap();
        //定位初始化
        initLoc();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        findMoneyMap.onResume();
        mLocationClient.startLocation();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mPresenter.unRegisterSubscribe();
            mLocationClient.stopLocation();
            findMoneyMap.onPause();
        } else {
            findMoneyMap.onResume();
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        findMoneyMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
        mLocationClient.stopLocation();
    }

    @Override
    public void onDestroyView() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        findMoneyMap.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        findMoneyMap.onSaveInstanceState(outState);
    }

    private NaviPickerDialog naviPickerDialog;

    @Optional
    @OnClick({R.id.center_title, R.id.start_navigation, R.id.find_money_scan, R.id.find_money_pay, R.id.find_money_bag, R.id.find_money_recommend, R.id.loc_scan_coins})
    void onClick(View view) {
        Fragment fragment;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.center_title:
                clearRoutePlan();
                break;
            case R.id.start_navigation:
                naviPickerDialog = new NaviPickerDialog(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pak = (String) view.getTag();
                        Intent intent;
                        switch (pak) {
                            case "com.baidu.BaiduMap":
                                intent = new Intent();
                                double[] startLocation = gaoDeToBaidu(locLongitude, locLatitude);
                                double[] endLocation = gaoDeToBaidu(routeTargetLocation.longitude, routeTargetLocation.latitude);
                                intent.setData(Uri.parse("baidumap://map/direction?origin=" + startLocation[1] + "," + startLocation[0] + "&destination=" + endLocation[1] + "," + endLocation[0] + "&mode=walking"));
                                ((MainActivity) getActivity()).startActivity(intent);
                                break;
                            case "com.autonavi.minimap":
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("androidamap://navi?");
                                try {
                                    //填写应用名称
                                    stringBuilder.append("sourceApplication=" + URLEncoder.encode("叮叮蚊", "utf-8"));
                                    //导航目的地
//                        stringBuilder.append("&poiname=" + URLEncoder.encode(poiItem.getTitle(), "utf-8"));
                                    //目的地经纬度
                                    stringBuilder.append("&lat=" + routeTargetLocation.latitude);
                                    stringBuilder.append("&lon=" + routeTargetLocation.longitude);
//                        stringBuilder.append("&dev=0&style=2");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //调用高德地图APP
                                intent = new Intent();
                                intent.setPackage("com.autonavi.minimap");
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setAction(Intent.ACTION_VIEW);
                                //传递组装的数据
                                intent.setData(Uri.parse(stringBuilder.toString()));
                                ((MainActivity) getActivity()).startActivity(intent);
                                break;
                        }
                        naviPickerDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //###########################################
                        //第一种方式：这种方式需要导入百度sdk，才能进行启调，如果没导入会找不到类
                        //建议使用这种方式，对浏览器的兼容更好。
                        //注释掉这里段代码，取消下面第二种方式的注释可以启用第二种方式
//            NaviParaOption para = new NaviParaOption().startPoint(MyDistanceUtil.entity2Baidu(loc_now)).endPoint(MyDistanceUtil.entity2Baidu(loc_end));
//            BaiduMapNavigation.openWebBaiduMapNavi(para, context);
                        //###########################################

                        //第二种方式：这种方式不需要导入百度sdk，可以直接使用
                        //不推建使用这种方式，浏览器兼容问题比较严重，比如qq浏览器会封杀百度的此功能。
                        //注释掉这里段代码，取消上面第一种方式的注释可以启用第一种方式
                        //###########################################
                        String url = APPUtil.getWebUrl_Baidu(loc_now, loc_end);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getActivity().startActivity(intent);
                        //###########################################
                        naviPickerDialog.dismiss();
                    }
                });
                naviPickerDialog.show();
//                if (Utils.isInstallApp(getActivity(), "com.autonavi.minimap")) {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("androidamap://navi?");
//                    try {
//                        //填写应用名称
//                        stringBuilder.append("sourceApplication=" + URLEncoder.encode("叮叮蚊", "utf-8"));
//                        //导航目的地
////                        stringBuilder.append("&poiname=" + URLEncoder.encode(poiItem.getTitle(), "utf-8"));
//                        //目的地经纬度
//                        stringBuilder.append("&lat=" + routeTargetLocation.latitude);
//                        stringBuilder.append("&lon=" + routeTargetLocation.longitude);
////                        stringBuilder.append("&dev=0&style=2");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    //调用高德地图APP
//                    Intent intent = new Intent();
//                    intent.setPackage("com.autonavi.minimap");
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    //传递组装的数据
//                    intent.setData(Uri.parse(stringBuilder.toString()));
//                    ((MainActivity) getActivity()).startActivity(intent);
//                } else
//                if (Utils.isInstallApp(getActivity(), "com.baidu.BaiduMap")) {
//                    Intent intent = new Intent();
//                    double[] startLocation = gaoDeToBaidu(locLongitude, locLatitude);
//                    double[] endLocation = gaoDeToBaidu(routeTargetLocation.longitude, routeTargetLocation.latitude);
//                    intent.setData(Uri.parse("baidumap://map/direction?origin=" + startLocation[1] + "," + startLocation[0] + "&destination=" + endLocation[1] + "," + endLocation[0] + "&mode=walking"));
//                    ((MainActivity) getActivity()).startActivity(intent);
//                } else {
//                    ((MainActivity) getActivity()).showSnackbar("您还未安装地图应用,请先安装才能导航");
//                }
//                reachTargetLocation();
                break;
            case R.id.find_money_scan:
                bundle = new Bundle();
                bundle.putInt("scanType", 0);
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class, bundle, 0);
                break;
            case R.id.find_money_pay:
                bundle = new Bundle();
                bundle.putInt("scanType", 1);
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class, bundle, 0);
                break;
            case R.id.find_money_bag:
                ((MainActivity) getActivity()).showBackgroundBag();
                break;
            case R.id.find_money_recommend:
                fragment = fm.findFragmentByTag("recommend");
                if (null == fragment) {
                    fragment = new RecommendFragment();
                }
                ((MainActivity) getActivity()).addOrShowFragment(fragment, "recommend");
                break;
        }
    }

    private void initMap() {
        if (null != mAMap) return;
        mAMap = findMoneyMap.getMap();
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                GEO_DEFAULT_CITY,//新的中心点坐标
                14, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (coinsMarkerList.contains(marker)) {
                    if (null != marker.getSnippet()) {
                        String bazaCode = marker.getSnippet();
                        Bundle bundle = new Bundle();
                        bundle.putString("bazaCode", bazaCode);
                        ((BaseActivity) getActivity()).openActivity(ShopDetailsActivity.class, bundle, 0);
                    }
                }
                return true;
            }
        });
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                int zoomLevel = (int) cameraPosition.zoom;
                switch (zoomLevel) {
                    case 12:
                        zoomDistance = 2000;
                        break;
                    case 13:
                        zoomDistance = 1000;
                        break;
                    case 14:
                        zoomDistance = 500;
                        break;
                    case 15:
                        zoomDistance = 200;
                        break;
                    case 16:
                        zoomDistance = 100;
                        break;
                    case 17:
                        zoomDistance = 50;
                        break;
                    default:
                        if (zoomLevel < 12) {
                            zoomDistance = 2000;
                        } else if (zoomLevel > 17) {
                            zoomDistance = 50;
                        }
                        break;
                }
                if (0 == isAlreadyLocScan && 0 == isAlreadyMarketScan) {
                    String mapStatusLocation = cameraPosition.target.longitude + "," + cameraPosition.target.latitude;
                    if (mapStatusLocation.equals(lastMapStatusLocation)) {
                        return;
                    } else {
                        lastMapStatusLocation = mapStatusLocation;
                    }
                    if (isFirstMapAnimate) {
                        isFirstMapAnimate = false;
                        return;
                    }
                    if (isSearchRoute) {
                        isSearchRoute = false;
                        return;
                    }
                    if (!Utils.isNetUsable(getActivity())) {
                        ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                        return;
                    }
                    mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
                } else {
                    String mapStatusLocation = cameraPosition.target.longitude + "," + cameraPosition.target.latitude;
                    if (mapStatusLocation.equals(lastMapStatusLocation)) {
                        return;
                    } else {
                        lastMapStatusLocation = mapStatusLocation;
                    }
                    if (isFirstMapAnimate) {
                        return;
                    } else if (isSearchRoute) {
                        isSearchRoute = false;
                        return;
                    } else {
                        if (!Utils.isNetUsable(getActivity())) {
                            ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                            return;
                        }
                        mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
                    }
                }
            }
        });
    }

    private void initLoc() {
        mLocationClient = new AMapLocationClient(App.getAppInstance());
        mLocationListener = new MyLocationListener();
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(LOC_TIME);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(LOC_TIME_OUT);
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void setPresenter(FindMoneyContract.FindMoneyPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetShopList(List<MarkerOptions> optionsList) {
        for (Marker marker : coinsMarkerList) {
            marker.remove();
        }
        coinsMarkerList.clear();
        for (MarkerOptions options : optionsList) {
            coinsMarkerList.add(mAMap.addMarker(options));
        }
    }

    @Override
    public void startRadarScan() {
//        locScanCoinsAnim.start();
    }

    @Override
    public void stopRadarScan() {
//        locScanCoinsAnim.stop();
    }

    public void clearRoutePlan() {
        if (!isOnRoutePlane) return;
        isOnRoutePlane = false;
        routeTargetLocation = null;
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        ButterKnife.apply(findOperationLayout, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        ButterKnife.apply(routeOpeLayout, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
    }

    private void reachTargetLocation() {
        if (!isOnRoutePlane) return;
        isOnRoutePlane = false;
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routeTargetLocation, 14));
        routeTargetLocation = null;
        ButterKnife.apply(findOperationLayout, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
        ButterKnife.apply(routeOpeLayout, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
    }

    public void locScanCoins() {
        if (null == qLocation || qLocation.length() <= 0) {
            ((BaseActivity) getActivity()).showSnackbar("定位异常,重新定位");
            mLocationClient.startLocation();
            return;
        }
        if (isFirstLoc) return;
        if (0 == isAlreadyLocScan) {
            isAlreadyLocScan = 1;
        }
        if (null != marketMarker) {
            marketMarker.remove();
            marketMarker = null;
        }
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEGONE);
        clearRoutePlan();
        for (Marker marker : coinsMarkerList) {
            marker.remove();
        }
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        LatLng latLng = mAMap.getCameraPosition().target;
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
        String mapStatusLocation = decimalFormat.format(latLng.longitude) + "," + decimalFormat.format(latLng.latitude);
        if (qLocation.equals(mapStatusLocation)) {
            if (!Utils.isNetUsable(getActivity())) {
                ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
        } else {
            LatLng locLatLng = new LatLng(locLatitude, locLongitude);
            float distance = AMapUtils.calculateLineDistance(latLng, locLatLng);
            if (0.5 > distance) {
                if (!Utils.isNetUsable(getActivity())) {
                    ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
                return;
            }
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locLatitude, locLongitude), 17));
        }
    }

    public void getShopListNearByMarket(String marketName, String coor) {
        if (isFirstLoc) return;
        if (0 == isAlreadyMarketScan) {
            isAlreadyMarketScan = 1;
        }
        page_index = 1;
        if (null != marketMarker) {
            marketMarker.remove();
            marketMarker = null;
        }
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEGONE);
        clearRoutePlan();
        for (Marker marker : coinsMarkerList) {
            marker.remove();
        }
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        String marketLongitude = coor.split(",")[0];
        String marketLatitude = coor.split(",")[1];
        LatLng latLng = mAMap.getCameraPosition().target;
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
        String mapStatusLocation = decimalFormat.format(latLng.longitude) + "," + decimalFormat.format(latLng.latitude);
        MarkerOptions marketMO = new MarkerOptions().position(new LatLng(Double.valueOf(marketLatitude), Double.valueOf(marketLongitude))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.market_icon));
        marketMarker = mAMap.addMarker(marketMO);//定位图标
        marketNameTV.setText(marketName);
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEVISIBLE);
        if ((decimalFormat.format(Double.valueOf(marketLongitude)) + "," + decimalFormat.format(Double.valueOf(marketLatitude))).equals(mapStatusLocation)) {
            if (!Utils.isNetUsable(getActivity())) {
                ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getShopList("000000", coor, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
        } else {
            LatLng marketLatLng = new LatLng(Double.valueOf(marketLatitude), Double.valueOf(marketLongitude));
            float distance = AMapUtils.calculateLineDistance(latLng, marketLatLng);
            if (0.2 > distance) {
                if (!Utils.isNetUsable(getActivity())) {
                    ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopList("000000", coor, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
                return;
            }
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketLatLng, 17));
        }
    }

    public void planRoute(String coor) {
        if (null == qLocation || qLocation.length() <= 0) {
            ((BaseActivity) getActivity()).showSnackbar("定位异常,重新定位");
            mLocationClient.startLocation();
            return;
        }
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        //点击检索点显示信息
        LatLonPoint stPoint = new LatLonPoint(locLatitude, locLongitude);//定位坐标点
        LatLonPoint endPoint = new LatLonPoint(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));//目的坐标点
        routeTargetLocation = new LatLng(Double.valueOf(Double.valueOf(coor.split(",")[1])), Double.valueOf(Double.valueOf(coor.split(",")[0])));
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(stPoint, endPoint);
        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        mRouteSearch.calculateWalkRouteAsyn(walkRouteQuery);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (1000 == i) {
            if (null != walkRouteResult && null != walkRouteResult.getPaths()) {
                if (0 < walkRouteResult.getPaths().size()) {
                    DecimalFormat decimalFormat = new DecimalFormat("#0.0");
                    isSearchRoute = true;
                    WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    walkingRouteOverlay = new WalkRouteOverlay(getActivity(), mAMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                    walkingRouteOverlay.removeFromMap();
                    walkingRouteOverlay.addToMap();
                    walkingRouteOverlay.zoomToSpan();
                    String planDistance;
                    String planDuration;
                    if (walkPath.getDistance() / 1000 < 1) {
                        planDistance = walkPath.getDistance() + "米";
                    } else {
                        planDistance = decimalFormat.format(walkPath.getDistance() / 1000d) + "公里";
                    }
                    if (walkPath.getDuration() / 3600 < 1) {
                        planDuration = decimalFormat.format(walkPath.getDuration() / 60d) + "分钟";
                    } else {
                        planDuration = decimalFormat.format(walkPath.getDuration() / 3600d) + "小时";
                    }
                    planeDurationTV.setText(planDuration);
                    planeDistanceTV.setText(planDistance);
                    isOnRoutePlane = true;
                    ButterKnife.apply(findOperationLayout, ((BaseActivity) getActivity()).BUTTERKNIFEGONE);
                    ButterKnife.apply(routePlaneLayout, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
                    ButterKnife.apply(routeOpeLayout, ((BaseActivity) getActivity()).BUTTERKNIFEVISIBLE);
                } else {
                    ((BaseActivity) getActivity()).showSnackbar("未找到规划路线");
                }
            } else {
                ((BaseActivity) getActivity()).showSnackbar("未找到规划路线");
            }
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private class MyLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null == aMapLocation || null == findMoneyMap) return;
            if (0 != aMapLocation.getErrorCode()) return;
            //获取当前位置坐标
            locLatitude = aMapLocation.getLatitude();
            locLongitude = aMapLocation.getLongitude();
            qLocation = locLongitude + "," + locLatitude;

            SharedPreferencesUtils.setString(getActivity(), "qLocation", qLocation);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(locLatitude, locLongitude);
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));
            }

            if (null != SharedPreferencesUtils.getString(getActivity(), "cust_code", "") && SharedPreferencesUtils.getString(getActivity(), "cust_code", "").length() > 0) {
                if (uploadCoorCounts == 600) {
                    if (!Utils.isNetUsable(getActivity())) {
                        ((BaseActivity) getActivity()).showSnackbar(getResources().getString(R.string.net_unusable));
                        return;
                    }
                    mPresenter.uploadCoor("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), qLocation, SharedPreferencesUtils.getString(getActivity(), "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(getActivity(), "ddw_access_token", ""));
                    uploadCoorCounts = 0;
                } else {
                    if (!isFirstLoc) {
                        uploadCoorCounts++;
                    }
                }
            }
        }
    }

    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }
}
