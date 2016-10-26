package com.yzdsmart.Collectmoney.main.find_money;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.Constants;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bd_map.WalkingRouteOverlay;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.main.recommend.RecommendFragment;
import com.yzdsmart.Collectmoney.qr_scan.QRScannerActivity;
import com.yzdsmart.Collectmoney.register_login.login.LoginActivity;
import com.yzdsmart.Collectmoney.shop_details.ShopDetailsActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class FindMoneyFragment extends BaseFragment implements FindMoneyContract.FindMoneyView, OnGetRoutePlanResultListener {
    @Nullable
    @BindViews({R.id.title_left_operation_layout, R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.find_money_map)
    MapView findMoneyMap;
    @Nullable
    @BindView(R.id.market_name)
    TextView marketNameTV;

    private FragmentManager fm;

    private BaiduMap mBaiduMap = null;
    private UiSettings mMapSettings;
    //默认城市经纬度119.980524,31.816058
    private static final LatLng GEO_DEFAULT_CITY = new LatLng(31.816058, 119.980524);
    private Integer zoomDistance = 500;
    private Marker mapCenterMarker;
    //定位坐标点
    private Double locLatitude = null;
    private Double locLongitude = null;
    // 定位相关
    private LocationClient mLocClient;
    private boolean isFirstLoc = true; // 是否首次定位

    private MyLocationListener locListener = new MyLocationListener();
    private Marker locMarker;
    //路径规划相关
    private RoutePlanSearch mSearch = null;// 搜索模块，也可去掉地图模块独立使用
    private RouteLine routeLine = null;
    private WalkingRouteOverlay walkingRouteOverlay = null;
    //周边检索
    //检索到的位置列表信息
    List<Overlay> coinsOverlayList = null;
    //周边商铺检索参数
    private static final Integer PAGE_SIZE = 5;//分页数量
    private Integer page_index = 1;//分页索引 当前页标
    private String qLocation = "";//检索中心点
    private Integer searchType = 0;//0 定位获取商铺列表 1 搜索商场附近商铺列表 2 扫码
//    private InfoWindow marketInfoWindow;

    private boolean isFirstMapAnimate = true;//是否是点击扫描按钮/商场附近商铺扫描
    private String lastMapStatusLocation = "";//记录地图状态改变后坐标
    private Integer isAlreadyLocScan = 0;
    private Integer isAlreadyMarketScan = 0;

    private Marker marketMarker;//商场Marker

    //定位频率
    private static final Integer LOC_TIME = 5000;//毫秒
    //上传坐标时间间隔次数
    private Integer uploadCounts = 0;

    //扫描金币动画
//    private AnimationDrawable locScanCoinsAnim;

    //获取当前用户周边用户
    private static final Integer personPageSize = 10;
    private Integer personPageIndex = 0;

    private FindMoneyContract.FindMoneyPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();
        coinsOverlayList = new ArrayList<Overlay>();

        new FindMoneyPresenter(getActivity(), this);

        setMapCustomFile(getActivity());
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

//        locScanCoinsAnim = (AnimationDrawable) locScanCoins.getDrawable();

        //初始化地图
        initMap();
        //定位初始化
        initLoc();
        //路径描线
//        initRoute();
        // 初始化路径规划模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        findMoneyMap.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            findMoneyMap.onPause();
//            locScanCoinsAnim.stop();
            mPresenter.unRegisterSubscribe();
        } else {
            findMoneyMap.onResume();
        }
    }

    @Override
    public void onPause() {
        findMoneyMap.onPause();
//        locScanCoinsAnim.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        if (null != findMoneyMap) {
            findMoneyMap.onDestroy();
            findMoneyMap = null;
        }
        super.onDestroy();
    }

    @Optional
    @OnClick({R.id.find_money_scan, R.id.find_money_recommend, R.id.loc_scan_coins})
    void onClick(View view) {
        Fragment fragment;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.find_money_scan:
                if (null == SharedPreferencesUtils.getString(getActivity(), "cust_code", "") || SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
                    ((BaseActivity) getActivity()).openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                bundle = new Bundle();
                bundle.putInt("scanType", 0);
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class, bundle, 0);
                break;
            case R.id.loc_scan_coins:
                if (null != marketMarker) {
                    marketMarker.remove();
                    marketMarker = null;
                }
                if (null != walkingRouteOverlay) {
                    walkingRouteOverlay.removeFromMap();
                    walkingRouteOverlay = null;
                }
                searchType = 0;
                for (Overlay overlay : coinsOverlayList) {
                    overlay.remove();
                }
                mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE);
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

    private Dialog scannerChooseDialog;
    private TextView scanCoin, payCoin;

    private void showMoveDialog(Context context) {
        final Bundle bundle = new Bundle();
        scannerChooseDialog = new Dialog(context, R.style.qr_scanner_popup);
        scannerChooseDialog.setContentView(R.layout.qr_scanner_choose);
        scanCoin = (TextView) scannerChooseDialog.findViewById(R.id.scan_coin);
        payCoin = (TextView) scannerChooseDialog.findViewById(R.id.pay_coin);
        scanCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt("scanType", 0);
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class, bundle, 0);
                scannerChooseDialog.dismiss();
            }
        });
        payCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt("scanType", 1);
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class, bundle, 0);
                scannerChooseDialog.dismiss();
            }
        });
        Window window = scannerChooseDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        scannerChooseDialog.show();
    }

    private void initMap() {
        //设置是否显示比例尺控件
        findMoneyMap.showScaleControl(true);
        //设置是否显示缩放控件
        findMoneyMap.showZoomControls(false);
        // 删除百度地图LoGo
        // searchMapView.removeViewAt(1);
        findMoneyMap.setMapCustomEnable(true);//自定义样式

        mBaiduMap = findMoneyMap.getMap();
        mMapSettings = mBaiduMap.getUiSettings();
//        mMapSettings.setScrollGesturesEnabled(false);
//        mBaiduMap.setMaxAndMinZoomLevel(18, 14);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(15));
        //设置默认显示城市
        MapStatusUpdate msu_cz = MapStatusUpdateFactory.newLatLng(GEO_DEFAULT_CITY);
        mBaiduMap.setMapStatus(msu_cz);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                if (null == SharedPreferencesUtils.getString(getActivity(), "cust_code", "") || SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
//                    ((BaseActivity) getActivity()).openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
//                    return true;
//                }

//                if (locMarker == marker) {
//
//                } else
                if (marketMarker == marker) {

                } else {
                    if (null != marker.getExtraInfo() && null != marker.getExtraInfo().getString("bazaCode")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("bazaCode", marker.getExtraInfo().getString("bazaCode"));
                        ((BaseActivity) getActivity()).openActivity(ShopDetailsActivity.class, bundle, 0);
                    }
                }
                return true;
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                int zoomLevel = (int) mapStatus.zoom;
                switch (zoomLevel) {
                    case 13:
                        zoomDistance = 2000;
                        break;
                    case 14:
                        zoomDistance = 1000;
                        break;
                    case 15:
                        zoomDistance = 500;
                        break;
                    case 16:
                        zoomDistance = 200;
                        break;
                    case 17:
                        zoomDistance = 100;
                        break;
                    case 18:
                        zoomDistance = 50;
                        break;
                    default:
                        if (zoomLevel < 13) {
                            zoomDistance = 2000;
                        } else if (zoomLevel > 18) {
                            zoomDistance = 50;
                        }
                        break;
                }
                if (0 == isAlreadyLocScan && 0 == isAlreadyMarketScan) {
                    String mapStatusLocation = mapStatus.target.longitude + "," + mapStatus.target.latitude;
                    if (mapStatusLocation.equals(lastMapStatusLocation)) {
                        return;
                    } else {
                        lastMapStatusLocation = mapStatusLocation;
                    }
                    if (isFirstMapAnimate) {
                        isFirstMapAnimate = false;
                        return;
                    }
                    mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE);
                } else {
                    String mapStatusLocation = mapStatus.target.longitude + "," + mapStatus.target.latitude;
                    if (mapStatusLocation.equals(lastMapStatusLocation)) {
                        return;
                    } else {
                        lastMapStatusLocation = mapStatusLocation;
                    }
                    if (isFirstMapAnimate) {
                        return;
                    } else {
                        mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE);
                    }
                }
            }
        });
    }

    private void initLoc() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(LOC_TIME);//设置发起定位请求的间隔时间
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true); // 打开gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

//    private void initRoute() {
//        // 构造折线点坐标
//        List<LatLng> points = new ArrayList<LatLng>();
//        points.add(new LatLng(31.701841, 119.942198));
//        points.add(new LatLng(31.701595, 119.944138));
//        points.add(new LatLng(31.700182, 119.944066));
//        points.add(new LatLng(31.697048, 119.946869));
//        points.add(new LatLng(31.696557, 119.953983));
//        points.add(new LatLng(31.691088, 119.954199));
//        points.add(new LatLng(31.696495, 119.958367));
//        points.add(new LatLng(31.689736, 119.965913));
//        points.add(new LatLng(31.688998, 119.973674));
//        points.add(new LatLng(31.687401, 119.977986));
//        points.add(new LatLng(31.686878, 119.977016));
//
//        //构建分段颜色索引数组
//        List<Integer> colors = new ArrayList<>();
//        colors.add(Integer.valueOf(Color.RED));
//
//        OverlayOptions ooPolyline = new PolylineOptions().width(10)
//                .colorsValues(colors).points(points);
//        //添加在地图中
//        mBaiduMap.addOverlay(ooPolyline);
//        System.out.println("------------------->" + DistanceUtil.getDistance(new LatLng(31.701841, 119.942198), new LatLng(31.686878, 119.977016)));
//        //缩放地图，使所有Overlay都在合适的视野内
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng point : points) {
//            builder.include(point);
//        }
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                .newLatLngBounds(builder.build()));
//    }

    @Override
    public void setPresenter(FindMoneyContract.FindMoneyPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onGetShopList(List<MarkerOptions> optionsList) {
        //先清除图层
        // mBaiduMap.clear();
        for (Overlay overlay : coinsOverlayList) {
            overlay.remove();
        }
        coinsOverlayList.clear();
        for (MarkerOptions options : optionsList) {
            // 在地图上添加Marker，并显示
            coinsOverlayList.add(mBaiduMap.addOverlay(options));
        }
        //缩放地图，使所有Overlay都在合适的视野内 注： 该方法只对Marker类型的overlay有效
//        zoomToSpan();
    }

    @Override
    public void startRadarScan() {
//        locScanCoinsAnim.start();
    }

    @Override
    public void stopRadarScan() {
//        locScanCoinsAnim.stop();
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (null == walkingRouteResult || SearchResult.ERRORNO.NO_ERROR != walkingRouteResult.error) {
            ((BaseActivity) getActivity()).showSnackbar("未找到规划路线!");
        }
        if (SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR == walkingRouteResult.error) {
            return;
        }
        if (SearchResult.ERRORNO.NO_ERROR == walkingRouteResult.error) {
            routeLine = walkingRouteResult.getRouteLines().get(0);
            System.out.println("onGetWalkingRouteResult---->步行行驶距离：" + routeLine.getDistance());
            walkingRouteOverlay = new MyWalkingRouteOverlay(mBaiduMap);
            walkingRouteOverlay.setData((WalkingRouteLine) routeLine);
            walkingRouteOverlay.addToMap();
            walkingRouteOverlay.zoomToSpan();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    //定位监听器
    private class MyLocationListener implements BDLocationListener {
        // map view 销毁后不在处理新接收的位置
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == bdLocation || null == findMoneyMap) {
                return;
            }
            //获取当前位置坐标
            locLatitude = bdLocation.getLatitude();
            locLongitude = bdLocation.getLongitude();
            qLocation = locLongitude + "," + locLatitude;

            SharedPreferencesUtils.setString(getActivity(), "qLocation", qLocation);

//            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(locLatitude).longitude(locLongitude).build();
//            mBaiduMap.setMyLocationData(locData);

            if (null != locMarker) {
                locMarker.remove();
                locMarker = null;
            }
//            MarkerOptions locMO = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).icons(locGifList).period(10);//动画速度
//            MarkerOptions locMO = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_loc_icon));
//            locMarker = (Marker) (mBaiduMap.addOverlay(locMO));//定位图标

            if (null != SharedPreferencesUtils.getString(getActivity(), "cust_code", "") && SharedPreferencesUtils.getString(getActivity(), "cust_code", "").length() > 0) {
                if (uploadCounts == 600) {
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(locLatitude, locLongitude)));
                    mPresenter.uploadCoor("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), qLocation);
                    uploadCounts = 0;
                } else {
                    if (!isFirstLoc) {
                        uploadCounts++;
                    }
                }
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
//                if (null != SharedPreferencesUtils.getString(getActivity(), "cust_code", "") && SharedPreferencesUtils.getString(getActivity(), "cust_code", "").length() > 0) {
//                    mPresenter.getPersonBearby("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), qLocation, personPageIndex, personPageSize);
//                }
            }
        }
    }

    // 定制RouteOverly
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }
    }

    public void locScanCoins() {
        if (isFirstLoc) return;
        if (0 == isAlreadyLocScan) {
            isAlreadyLocScan = 1;
        }
        searchType = 0;
        if (null != marketMarker) {
            marketMarker.remove();
            marketMarker = null;
        }
//        if (null != marketInfoWindow) {
//            mBaiduMap.hideInfoWindow();
//            marketInfoWindow = null;
//        }
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEGONE);
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        for (Overlay overlay : coinsOverlayList) {
            overlay.remove();
        }
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));
        LatLng latLng = mBaiduMap.getMapStatus().target;
        DecimalFormat decimalFormat = new DecimalFormat(".######");
        String mapStatusLocation = decimalFormat.format(latLng.longitude) + "," + decimalFormat.format(latLng.latitude);
        if (qLocation.equals(mapStatusLocation)) {
            mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE);
        } else {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(locLatitude, locLongitude)));
        }
    }

    public void getShopListNearByMarket(String marketName, String coor) {
        if (isFirstLoc) return;
        if (0 == isAlreadyMarketScan) {
            isAlreadyMarketScan = 1;
        }
        searchType = 1;
        page_index = 1;
        if (null != marketMarker) {
            marketMarker.remove();
            marketMarker = null;
        }
//        if (null != marketInfoWindow) {
//            mBaiduMap.hideInfoWindow();
//            marketInfoWindow = null;
//        }
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEGONE);
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        for (Overlay overlay : coinsOverlayList) {
            overlay.remove();
        }
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));
        String marketLongitude = coor.split(",")[0];
        String marketLatitude = coor.split(",")[1];
        LatLng latLng = mBaiduMap.getMapStatus().target;
        DecimalFormat decimalFormat = new DecimalFormat(".######");
        String mapStatusLocation = decimalFormat.format(latLng.longitude) + "," + decimalFormat.format(latLng.latitude);
        MarkerOptions marketMO = new MarkerOptions().position(new LatLng(Double.valueOf(marketLatitude), Double.valueOf(marketLongitude))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.market_icon));
        marketMarker = (Marker) (mBaiduMap.addOverlay(marketMO));//定位图标
        marketNameTV.setText(marketName);
        ButterKnife.apply(marketNameTV, BaseActivity.BUTTERKNIFEVISIBLE);
//        View popInfoView = getActivity().getLayoutInflater().inflate(R.layout.market_pop_window, null);
//        TextView marketNameTV = (TextView) popInfoView.findViewById(R.id.market_name);
//        marketNameTV.setText(marketName);
//        // 计算与你开发时设定的屏幕大小的纵横比
//        int screenWidth = Utils.deviceWidth(getActivity());
//        int screenHeight = Utils.deviceHeight(getActivity());
//        float ratioWidth = (float) screenWidth / 480;
//        float ratioHeight = (float) screenHeight / 800;
//        float ratio = Math.min(ratioWidth, ratioHeight);
//        //view 坐标 y偏移量
//        marketInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(popInfoView), new LatLng(Double.valueOf(marketLatitude), Double.valueOf(marketLongitude)),//把弹出框的view添加到InfoWindow
//                Math.round(-40 * ratio), new InfoWindow.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick() {
//
//            }
//        });
//        mBaiduMap.showInfoWindow(marketInfoWindow);
        if ((decimalFormat.format(Double.valueOf(marketLongitude)) + "," + decimalFormat.format(Double.valueOf(marketLatitude))).equals(mapStatusLocation)) {
            mPresenter.getShopList("000000", coor, zoomDistance, page_index, PAGE_SIZE);
        } else {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(Double.valueOf(marketLatitude), Double.valueOf(marketLongitude))));
        }
    }

    public void planRoute(String coor) {
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        //点击检索点显示信息
        LatLng stLocation = new LatLng(locLatitude, locLongitude);//定位坐标点
        LatLng endLocation = new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));//目的坐标点
        // 设置起终点信息
        PlanNode stNode = PlanNode.withLocation(stLocation);
        PlanNode endNode = PlanNode.withLocation(endLocation);
        //步行路线规划
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(endNode));
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     * <p>
     * 注： 该方法只对Marker类型的overlay有效
     * </p>
     */
//    public void zoomToSpan() {
//        if (mBaiduMap == null) {
//            return;
//        }
//        if (coinsOverlayList.size() > 0) {
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (Overlay overlay : coinsOverlayList) {
//                // polyline 中的点可能太多，只按marker 缩放
//                if (overlay instanceof Marker) {
//                    builder.include(((Marker) overlay).getPosition());
//                }
//            }
//            mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                    .newLatLngBounds(builder.build()));
//        }
//    }

    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open("customConfigdir/custom_config.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_config.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/custom_config.txt");
    }
}
