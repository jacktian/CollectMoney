package com.yzdsmart.Collectmoney.main.find_money;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
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
public class FindMoneyFragment extends BaseFragment implements FindMoneyContract.FindMoneyView {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.find_money_map)
    MapView findMoneyMap;

    private FragmentManager fm;

    private BaiduMap mBaiduMap = null;
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
    //周边检索
    //检索到的位置列表信息
    List<Overlay> mOverlayList = null;
    //周边商铺检索参数
    private static final Integer PAGE_SIZE = 5;//分页数量
    private Integer page_index = 0;//分页索引 当前页标，从0开始
    private String qLocation = "";//检索中心点

    private FindMoneyContract.FindMoneyPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();
        mOverlayList = new ArrayList<Overlay>();
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

        //初始化地图
        initMap();
        //定位初始化
        initLoc();
        //路径描线
//        initRoute();
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
        } else {
            findMoneyMap.onResume();
        }
    }

    @Override
    public void onPause() {
        findMoneyMap.onPause();
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
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        if (null ==  SharedPreferencesUtils.getString(getActivity(), "cust_code", "") ||  SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
            ((BaseActivity) getActivity()).openActivity(LoginActivity.class);
            return;
        }
        Fragment fragment;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                fragment = fm.findFragmentByTag("personal");
                if (null == fragment) {
                    fragment = new PersonalFragment();
                }
                ((MainActivity) getActivity()).addOrShowFragment(fragment, "personal");
                break;
            case R.id.title_right_operation_layout:
                ((BaseActivity) getActivity()).openActivity(QRScannerActivity.class);
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
        //设置默认显示城市
        MapStatusUpdate msu_cz = MapStatusUpdateFactory.newLatLng(GEO_DEFAULT_CITY);
        mBaiduMap.setMapStatus(msu_cz);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (null ==  SharedPreferencesUtils.getString(getActivity(), "cust_code", "") ||  SharedPreferencesUtils.getString(getActivity(), "cust_code", "").trim().length() <= 0) {
                    ((BaseActivity) getActivity()).openActivity(LoginActivity.class);
                    return true;
                }
                if (locMarker == marker) {
                    ((BaseActivity) getActivity()).showSnackbar("哈哈");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("bazaCode", marker.getExtraInfo().getString("bazaCode"));
                    ((BaseActivity) getActivity()).openActivity(ShopDetailsActivity.class, bundle, 0);
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
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为1000ms
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initRoute() {
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(31.701841, 119.942198));
        points.add(new LatLng(31.701595, 119.944138));
        points.add(new LatLng(31.700182, 119.944066));
        points.add(new LatLng(31.697048, 119.946869));
        points.add(new LatLng(31.696557, 119.953983));
        points.add(new LatLng(31.691088, 119.954199));
        points.add(new LatLng(31.696495, 119.958367));
        points.add(new LatLng(31.689736, 119.965913));
        points.add(new LatLng(31.688998, 119.973674));
        points.add(new LatLng(31.687401, 119.977986));
        points.add(new LatLng(31.686878, 119.977016));

        //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.RED));

        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .colorsValues(colors).points(points);
        //添加在地图中
        mBaiduMap.addOverlay(ooPolyline);
        System.out.println("------------------->" + DistanceUtil.getDistance(new LatLng(31.701841, 119.942198), new LatLng(31.686878, 119.977016)));
        //缩放地图，使所有Overlay都在合适的视野内
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));
    }

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
        mOverlayList.clear();
        for (MarkerOptions options : optionsList) {
            // 在地图上添加Marker，并显示
            mOverlayList.add(mBaiduMap.addOverlay(options));
        }
        //缩放地图，使所有Overlay都在合适的视野内 注： 该方法只对Marker类型的overlay有效
        zoomToSpan();
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
            MarkerOptions locMO = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).icons(locGifList);
            locMarker = (Marker) (mBaiduMap.addOverlay(locMO));//定位图标

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                //获取当前位置坐标
                locLatitude = bdLocation.getLatitude();
                locLongitude = bdLocation.getLongitude();
                qLocation = locLongitude + "," + locLatitude;

                mPresenter.getShopList("000000", qLocation, page_index, PAGE_SIZE);
            }
        }
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
        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
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
