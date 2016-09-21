package com.yzdsmart.Collectmoney.main.find_money;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

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
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bd_map.WalkingRouteOverlay;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.main.personal.PersonalFragment;
import com.yzdsmart.Collectmoney.qr_scan.QRScannerActivity;
import com.yzdsmart.Collectmoney.shop_details.ShopDetailsActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

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
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.find_money_map)
    MapView findMoneyMap;
    @Nullable
    @BindView(R.id.loc_scan_coins)
    ImageButton locScanCoins;

    private static final Integer REQUEST_LOGIN_CODE = 1000;

    private FragmentManager fm;

    private BaiduMap mBaiduMap = null;
    private UiSettings mMapSettings;
    //默认城市经纬度
    private static final LatLng GEO_DEFAULT_CITY = new LatLng(31.79, 119.95);
    //定位坐标点
    private Double locLatitude = null;
    private Double locLongitude = null;
    // 定位相关
    private LocationClient mLocClient;
    private boolean isFirstLoc = true; // 是否首次定位
    private MyLocationListener locListener = new MyLocationListener();
    private Marker locMarker;
    private ArrayList<BitmapDescriptor> locGifList;
    //路径规划相关
    private RoutePlanSearch mSearch = null;// 搜索模块，也可去掉地图模块独立使用
    private RouteLine routeLine = null;
    private WalkingRouteOverlay walkingRouteOverlay = null;
    //周边检索
    //检索到的位置列表信息
    List<Overlay> coinsOverlayList = null;
    //周边商铺检索参数
    private static final Integer PAGE_SIZE = 5;//分页数量
    private Integer page_index = 1;//分页索引 当前页标，从0开始
    private String qLocation = "";//检索中心点
    private Integer searchType = 0;//0 定位获取商铺列表 1 搜索商场附近商铺列表 2 扫码

    private Marker marketMarker;//商场Marker

    //定位频率
    private static final Integer LOC_TIME = 5000;//毫秒
    //上传坐标时间间隔次数
    private Integer uploadCounts = 0;

    //扫描金币动画
    private AnimationDrawable locScanCoinsAnim;

    //获取当前用户周边用户
    private static final Integer personPageSize = 10;
    private Integer personPageIndex = 0;

    private FindMoneyContract.FindMoneyPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();
        coinsOverlayList = new ArrayList<Overlay>();
        //定位图标
        locGifList = new ArrayList<BitmapDescriptor>();
        locGifList.add(BitmapDescriptorFactory.fromResource(R.mipmap.loc_marker1));
        locGifList.add(BitmapDescriptorFactory.fromResource(R.mipmap.loc_marker2));
        locGifList.add(BitmapDescriptorFactory.fromResource(R.mipmap.loc_marker3));
        locGifList.add(BitmapDescriptorFactory.fromResource(R.mipmap.loc_marker4));

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

        locScanCoinsAnim = (AnimationDrawable) locScanCoins.getDrawable();

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
            locScanCoinsAnim.stop();
            mPresenter.unRegisterSubscribe();
        } else {
            findMoneyMap.onResume();
        }
    }

    @Override
    public void onPause() {
        findMoneyMap.onPause();
        locScanCoinsAnim.stop();
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
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout, R.id.loc_scan_coins})
    void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                if (null == SharedPreferencesUtils.getString(getActivity(), "cust_code", "") || SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
                    ((BaseActivity) getActivity()).openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    return;
                }
                fragment = fm.findFragmentByTag("personal");
                if (null == fragment) {
                    fragment = new PersonalFragment();
                }
                ((MainActivity) getActivity()).addOrShowFragment(fragment, "personal");
                break;
            case R.id.title_right_operation_layout:
                if (null == SharedPreferencesUtils.getString(getActivity(), "cust_code", "") || SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
                    ((BaseActivity) getActivity()).openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    return;
                }
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class);
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
                mPresenter.getShopList("000000", qLocation, page_index, PAGE_SIZE, 0);
                break;
        }
    }

    private void initMap() {
        //设置是否显示比例尺控件
        findMoneyMap.showScaleControl(true);
        //设置是否显示缩放控件
        findMoneyMap.showZoomControls(false);
        // 删除百度地图LoGo
        // searchMapView.removeViewAt(1);

        mBaiduMap = findMoneyMap.getMap();
        mMapSettings = mBaiduMap.getUiSettings();
        mMapSettings.setScrollGesturesEnabled(false);
        mBaiduMap.setMaxAndMinZoomLevel(15, 14);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
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
                if (locMarker == marker) {

                } else if (marketMarker == marker) {

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
    }

    private void initLoc() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(LOC_TIME);//设置发起定位请求的间隔时间
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
        zoomToSpan();
    }

    @Override
    public void startRadarScan() {
        locScanCoinsAnim.start();
    }

    @Override
    public void stopRadarScan() {
        locScanCoinsAnim.stop();
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
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

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
            if (null != locMarker) {
                locMarker.remove();
                locMarker = null;
            }
            MarkerOptions locMO = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).icons(locGifList).period(10);//动画速度
            locMarker = (Marker) (mBaiduMap.addOverlay(locMO));//定位图标

            //获取当前位置坐标
            locLatitude = bdLocation.getLatitude();
            locLongitude = bdLocation.getLongitude();
            qLocation = locLongitude + "," + locLatitude;

            SharedPreferencesUtils.setString(getActivity(), "qLocation", qLocation);

            if (null != SharedPreferencesUtils.getString(getActivity(), "cust_code", "") && SharedPreferencesUtils.getString(getActivity(), "cust_code", "").length() > 0) {
                if (uploadCounts == 600) {
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

    public void getShopListNearByMarket(String coor) {
        searchType = 1;
        page_index = 0;
        if (null != marketMarker) {
            marketMarker.remove();
            marketMarker = null;
        }
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        for (Overlay overlay : coinsOverlayList) {
            overlay.remove();
        }
        MarkerOptions marketMO = new MarkerOptions().position(new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.market_icon));
        marketMarker = (Marker) (mBaiduMap.addOverlay(marketMO));//定位图标
        mPresenter.getShopList("000000", coor, page_index, PAGE_SIZE, 1);
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
    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        if (coinsOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : coinsOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }
}
