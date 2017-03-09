package com.yzdsmart.Dingdingwen.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.meelive.ingkee.sdk.plugin.IInkeCallback;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.meelive.ingkee.sdk.plugin.entity.ShareInfo;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.App;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.amap.WalkRouteOverlay;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.bean.MarketShop;
import com.yzdsmart.Dingdingwen.coupon_exchange.CouponExchangeActivity;
import com.yzdsmart.Dingdingwen.money_friendship.MoneyFriendshipActivity;
import com.yzdsmart.Dingdingwen.money_friendship.conversation.ConversationFragment;
import com.yzdsmart.Dingdingwen.personal.PersonalActivity;
import com.yzdsmart.Dingdingwen.qr_scan.QRScannerActivity;
import com.yzdsmart.Dingdingwen.recommend.RecommendActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.shop_details.ShopDetailsActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.Conversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.CustomMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupManageConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.MessageFactory;
import com.yzdsmart.Dingdingwen.tecent_im.bean.NormalConversation;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.tecent_im.service.TLSService;
import com.yzdsmart.Dingdingwen.tecent_im.utils.PushUtil;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.BetterSpinner;
import com.yzdsmart.Dingdingwen.views.GuideView;
import com.yzdsmart.Dingdingwen.views.navi_picker.NaviPickerDialog;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by YZD on 2016/8/17.
 */
public class MainActivity extends BaseActivity implements MainContract.MainView, RouteSearch.OnRouteSearchListener, GeocodeSearch.OnGeocodeSearchListener {
    @Nullable
    @BindViews({R.id.title_left_operation_layout, R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.unread_conversation_bubble)
    TextView unreadConversationBubbleTV;
    @Nullable
    @BindView(R.id.loc_scan_coins)
    ImageButton searchIB;
    @Nullable
    @BindView(R.id.find_money_scan)
    ImageButton scanIB;
    @Nullable
    @BindView(R.id.find_money_pay)
    ImageButton payIB;
    @Nullable
    @BindView(R.id.find_money_bag)
    ImageButton bagIB;
    @Nullable
    @BindView(R.id.find_money_recommend)
    ImageButton recommendIB;
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
    @BindView(R.id.plane_duration)
    TextView planeDurationTV;
    @Nullable
    @BindView(R.id.plane_distance)
    TextView planeDistanceTV;
    @Nullable
    @BindView(R.id.route_ope_layout)
    RelativeLayout routeOpeLayout;
    @Nullable
    @BindView(R.id.custom_message)
    TextView customMsgTV;

    private static final String TAG = "MainActivity";

    private UltimateRecyclerView backgroundBagRV, marketLevelShopsRV;
    private TextView backgroundBagLoginCheck;

    //连续双击返回键退出程序
    private Long lastKeyDown = 0l;

    private boolean isForeground = true;

    private MainContract.MainPresenter mPresenter;

    private NaviPickerDialog naviPickerDialog;

    private FragmentManager fm;

    //周边商铺检索参数
    private static final Integer PAGE_SIZE = 30;//分页数量
    private Integer page_index = 1;//分页索引 当前页标

    //检索到的位置列表信息
    List<Marker> coinsMarkerList = null;

    private Integer zoomDistance = 2000;
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

    private RouteSearch mRouteSearch;//路径规划
    private boolean isOnRoutePlane = false;
    private LatLng routeTargetLocation;
    private String routeTargetRegion;
    private String routeTargetName;

    private GeocodeSearch mGeocodeSearch;//地址反编译

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

    private TLSService tlsService;

    private static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    private List<Conversation> conversationList;
    private List<Conversation> pgConversationList = null;//个人或者群消息
    private GroupManageConversation groupManageConversation;

    private Handler mRefreshAccessTokenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Utils.isNetUsable(MainActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            if (SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", "") != null && SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", "").trim().length() > 0) {
                mPresenter.refreshAccessToken("refresh_token", SharedPreferencesUtils.getString(MainActivity.this, "ddw_refresh_token", ""));
            } else {
                getRefreshToken();
            }
        }
    };
    private boolean stopRefreshAccessToken = false;//停止刷新

    private GridLayoutManager mGridLayoutManager;
    private BackgroundBagAdapter bagAdapter;
    private boolean isFirstLoadBag = true;
    private BottomSheetDialog backgroundBagBottomSheetDialog;

    private LinearLayoutManager mLinearLayoutManager;
    private MarketShopsAdapter marketShopsAdapter;
    private Integer marketShopsPageIndex = 1;
    private static final Integer MARKET_SHOPS_PAGE_SIZE = 10;
    private String marketName = "";
    private String marketLevel = "";
    private boolean isFirstLoadMarketShops = true;
    private BottomSheetDialog marketShopsBottomSheetDialog;

    private List<String> customMsgList;
    private Handler showCustomMsgHandler = new Handler();
    private Runnable showCustomMsgRunnable;

    private GuideView scanGuideView;
    private GuideView payGuideView;
    private GuideView bagGuideView;
    private GuideView recommendGuideView;
    private GuideView searchGuideView;

    private void setGuideView() {
        // 使用图片
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView scanIV = new ImageView(this);
        scanIV.setImageResource(R.mipmap.scan_guide_view);
        scanIV.setLayoutParams(params);

        ImageView payIV = new ImageView(this);
        payIV.setImageResource(R.mipmap.pay_guide_view);
        payIV.setLayoutParams(params);

        ImageView bagIV = new ImageView(this);
        bagIV.setImageResource(R.mipmap.bag_guide_view);
        bagIV.setLayoutParams(params);

        ImageView recommendIV = new ImageView(this);
        recommendIV.setImageResource(R.mipmap.recommend_guide_view);
        recommendIV.setLayoutParams(params);

        ImageView searchIV = new ImageView(this);
        searchIV.setImageResource(R.mipmap.search_guide_view);
        searchIV.setLayoutParams(params);

        scanGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(scanIB)//设置目标
                .setCustomGuideView(scanIV)
                .setDirction(GuideView.Direction.RIGHT_BOTTOM)
                .setOffset((int) (-100 * Utils.getScreenRatio(this)), 0)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(this.getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        scanGuideView.hide();
                        payGuideView.show();
                    }
                })
                .build();

        payGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(payIB)//设置目标
                .setCustomGuideView(payIV)
                .setDirction(GuideView.Direction.LEFT_BOTTOM)
                .setOffset((int) (160 * Utils.getScreenRatio(this)), 0)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(this.getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        payGuideView.hide();
                        bagGuideView.show();
                    }
                })
                .build();

        bagGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(bagIB)//设置目标
                .setCustomGuideView(bagIV)
                .setDirction(GuideView.Direction.RIGHT_BOTTOM)
                .setOffset((int) (-160 * Utils.getScreenRatio(this)), 0)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(this.getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        bagGuideView.hide();
                        recommendGuideView.show();
                    }
                })
                .build();

        recommendGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(recommendIB)//设置目标
                .setCustomGuideView(recommendIV)
                .setDirction(GuideView.Direction.LEFT_BOTTOM)
                .setOffset((int) (100 * Utils.getScreenRatio(this)), 0)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(this.getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        recommendGuideView.hide();
                        searchGuideView.show();
                    }
                })
                .build();

        searchGuideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(searchIB)//设置目标
                .setCustomGuideView(searchIV)
                .setDirction(GuideView.Direction.TOP)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        SharedPreferencesUtils.setString(MainActivity.this, "hasGuide", "true");
                        searchGuideView.hide();
                    }
                })
                .build();

        scanGuideView.show();
    }

    private IInkeCallback inkeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = this;

        coinsMarkerList = new ArrayList<Marker>();
        conversationList = new LinkedList<Conversation>();
        pgConversationList = new ArrayList<Conversation>();
        customMsgList = new ArrayList<String>();

        fm = getFragmentManager();

        bagAdapter = new BackgroundBagAdapter(this);
        mGridLayoutManager = new GridLayoutManager(this, 5);

        mLinearLayoutManager = new LinearLayoutManager(this);
        marketShopsAdapter = new MarketShopsAdapter(this);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText(getResources().getString(R.string.app_name));
        centerTitleTV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.find_money_title_logo), null, null, null);
        ButterKnife.apply(findOperationLayout, isOnRoutePlane ? BUTTERKNIFEGONE : BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, isOnRoutePlane ? BUTTERKNIFEVISIBLE : BUTTERKNIFEGONE);

        if (null == SharedPreferencesUtils.getString(MainActivity.this, "hasGuide", "") || SharedPreferencesUtils.getString(MainActivity.this, "hasGuide", "").length() <= 0) {
            setGuideView();
        }

        inkeCallback = new IInkeCallback() {
            @Override
            public void loginTrigger() {

            }

            @Override
            public void payTrigger(String s, String s1) {

            }

            @Override
            public void shareTrigger(ShareInfo shareInfo) {

            }

            @Override
            public void createLiveReturnTrigger(String s) {
                Toast.makeText(MainActivity.this, "-->" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void stopLiveTrigger(String s) {

            }
        };

        InKeSdkPluginAPI.register(inkeCallback, Constants.INKE_APP_ID, 1, 0);

        initBackgroundBagBottomSheetDialog();
        initMarketShopsBottomSheetDialog();

        tlsService = TLSService.getInstance();

        new MainPresenter(this, this, tlsService);

        MobclickAgent.openActivityDurationTrack(false);

        findMoneyMap.onCreate(savedInstanceState);

        //初始化地图
        initMap();
        //定位初始化
        initLoc();

        imLogin();

        initJPush();

        registerCustomMessageReceiver();

        showCustomMsgRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isForeground) {
                    showCustomMsgHandler.removeCallbacks(this);
                    return;
                }
                if (null == customMsgList || customMsgList.size() <= 0) {
                    setCustomMsgTV(false);
                    showCustomMsgHandler.postDelayed(this, 5000);
                    return;
                }
                showCustomMsg(customMsgList.get(0));
                customMsgList.remove(0);
                showCustomMsgHandler.postDelayed(this, 5000);
            }
        };

        getRefreshToken();
        refreshAccessToken();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        findMoneyMap.onResume();
        if (null != UserInfo.getInstance().getId()) {
            PushUtil.getInstance(this).reset();
            updateUnreadConversationBubble();
        }
        startCustomMsgTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        findMoneyMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground = false;
        mPresenter.unRegisterSubscribe();
        stopCustomMsgTimer();
    }

    @Override
    protected void onDestroy() {
        JPushInterface.stopPush(App.getAppInstance());
        stopRefreshAccessToken = true;
        if (null != showCustomMsgHandler && null != showCustomMsgRunnable) {
            showCustomMsgHandler.removeCallbacks(showCustomMsgRunnable);
        }
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationListener = null;
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        findMoneyMap.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            imLogin();
        } else if (Constants.REQUEST_LOGIN_UPDATE_BAGCKGROUND_BAG_CODE == requestCode && RESULT_OK == resultCode) {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEGONE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEVISIBLE);
            imLogin();
            bagAdapter.clearList();
            getBackgroundBag();
        }
    }

    @Optional
    @OnClick({R.id.money_friend_radio, R.id.loc_scan_coins, R.id.personal_radio, R.id.background_bag_layout, R.id.quit_background_bag, R.id.center_title, R.id.start_navigation, R.id.find_money_scan, R.id.find_money_pay, R.id.find_money_bag, R.id.find_money_recommend, R.id.show_market_shops})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.money_friend_radio:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                openActivity(MoneyFriendshipActivity.class);
                break;
            case R.id.loc_scan_coins:
                locScanCoins();
                break;
            case R.id.personal_radio:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                openActivity(PersonalActivity.class);
                break;
            case R.id.center_title:
                clearRoutePlan();
                break;
            case R.id.start_navigation:
                if (null == routeTargetLocation) {
                    showSnackbar("无法解析目的地地址");
                    return;
                }
                naviPickerDialog = new NaviPickerDialog(MainActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pak = (String) view.getTag();
                        Intent intent;
                        switch (pak) {
                            case "com.baidu.BaiduMap":
                                intent = new Intent();
                                double[] endLocation = gaoDeToBaidu(routeTargetLocation.longitude, routeTargetLocation.latitude);
                                intent.setData(Uri.parse("baidumap://map/marker?location=" + endLocation[1] + "," + endLocation[0] + "&title=" + routeTargetName + "&traffic=on"));//地图标注
                                startActivity(intent);
                                break;
                            case "com.autonavi.minimap":
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("androidamap://viewMap?");//地图标注
                                try {
                                    //填写应用名称
                                    stringBuilder.append("sourceApplication=" + URLEncoder.encode("叮叮蚊", "utf-8"));
                                    //导航目的地
                                    stringBuilder.append("&poiname=" + URLEncoder.encode(routeTargetName, "utf-8"));
                                    //目的地经纬度
                                    stringBuilder.append("&lat=" + routeTargetLocation.latitude);
                                    stringBuilder.append("&lon=" + routeTargetLocation.longitude);
                                    stringBuilder.append("&dev=1");
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
                                startActivity(intent);
                                break;
                        }
                        naviPickerDialog.dismiss();
                        reachTargetLocation();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == routeTargetRegion || routeTargetRegion.length() <= 0) {
                            showSnackbar("无法解析目的地地址");
                            return;
                        }
                        //手机没有安装百度、高德地图，调用浏览器百度导航
                        double[] startLocation = gaoDeToBaidu(locLongitude, locLatitude);
                        double[] endLocation = gaoDeToBaidu(routeTargetLocation.longitude, routeTargetLocation.latitude);
                        String url = "http://api.map.baidu.com/direction?origin=latlng:" + startLocation[1] + "," + startLocation[0] + "|name:起点&destination=latlng:" + endLocation[1] + "," + endLocation[0] + "|name:终点&region=" + routeTargetRegion + "&mode=walking&output=html&src=叮叮蚊";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        naviPickerDialog.dismiss();
                        reachTargetLocation();
                    }
                });
                naviPickerDialog.show();
                break;
            case R.id.find_money_scan:
                bundle = new Bundle();
                bundle.putInt("scanType", 0);
                openActivity(QRScannerActivity.class, bundle, 0);
                break;
            case R.id.find_money_pay:
                bundle = new Bundle();
                bundle.putInt("scanType", 1);
                openActivity(QRScannerActivity.class, bundle, 0);
                break;
            case R.id.find_money_bag:
                showBackgroundBag();
                break;
            case R.id.find_money_recommend:
                openActivity(RecommendActivity.class);
                break;
            case R.id.show_market_shops:
                showMarketShops();
                break;
        }
    }

    public void planRoute(String coor, String shopName) {
        if (null == qLocation || qLocation.length() <= 0) {
            if (null != mLocationClient) {
                mLocationClient.startLocation();
            }
            return;
        }
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        routeTargetName = shopName;
        //点击检索点显示信息
        LatLonPoint stPoint = new LatLonPoint(locLatitude, locLongitude);//定位坐标点
        LatLonPoint endPoint = new LatLonPoint(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));//目的坐标点
        RegeocodeQuery query = new RegeocodeQuery(endPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        mGeocodeSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
        routeTargetLocation = new LatLng(Double.valueOf(Double.valueOf(coor.split(",")[1])), Double.valueOf(Double.valueOf(coor.split(",")[0])));
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(stPoint, endPoint);
        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        mRouteSearch.calculateWalkRouteAsyn(walkRouteQuery);
    }

    public void clearRoutePlan() {
        if (!isOnRoutePlane) return;
        isOnRoutePlane = false;
        routeTargetLocation = null;
        if (null != walkingRouteOverlay) {
            walkingRouteOverlay.removeFromMap();
            walkingRouteOverlay = null;
        }
        ButterKnife.apply(findOperationLayout, BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, BUTTERKNIFEGONE);
        ButterKnife.apply(routeOpeLayout, BUTTERKNIFEGONE);
        FrameLayout.LayoutParams customMsgTVParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customMsgTVParams.setMargins(0, 0, 0, 0);
        customMsgTV.setLayoutParams(customMsgTVParams);
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
        ButterKnife.apply(findOperationLayout, BUTTERKNIFEVISIBLE);
        ButterKnife.apply(routePlaneLayout, BUTTERKNIFEGONE);
        ButterKnife.apply(routeOpeLayout, BUTTERKNIFEGONE);
        FrameLayout.LayoutParams customMsgTVParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customMsgTVParams.setMargins(0, 0, 0, 0);
        customMsgTV.setLayoutParams(customMsgTVParams);
    }

    public void exchangeCoupon(Integer goldType) {
        Bundle bundle = new Bundle();
        bundle.putInt("couponType", 1);
        bundle.putInt("goldType", goldType);
        openActivity(CouponExchangeActivity.class, bundle, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Long currentTime = System.currentTimeMillis();
                if (1000 > (currentTime - lastKeyDown)) {
                    App.getAppInstance().exitApp();
                } else {
                    showSnackbar(getResources().getString(R.string.double_click_exit));
                    lastKeyDown = currentTime;
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        findMoneyMap.onSaveInstanceState(outState);
    }

    @Override
    public void chatLoginSuccess() {
        mPresenter.imSDKLogin();
    }

    @Override
    public void imSDKLoginSuccess() {
        //退到后台发送通知
        //初始化程序后台后消息推送
        PushUtil.getInstance(this);
        mPresenter.getConversation();
    }

    @Override
    public void onIMOffline() {
        JPushInterface.stopPush(App.getAppInstance());
        openActivityClear(MainActivity.class, null, 0);
    }

    @Override
    public void initConversations(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NormalConversation(item));
                    break;
            }
        }
    }

    @Override
    public void updateConversation(TIMMessage message) {
        if (null == message) {
            Fragment chatFragment = fm.findFragmentByTag("conversation");
            if (null != chatFragment) {
                ((ConversationFragment) chatFragment).updateConversions(conversationList);
            }
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            mPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NormalConversation conversation = new NormalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NormalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        updateUnreadConversationBubble();
    }

    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (null == groupManageConversation) {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        } else {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        updateUnreadConversationBubble();
    }

    @Override
    public void reRegisterApp() {
        if (null == SharedPreferencesUtils.getString(this, "isFirstOpen", "") || SharedPreferencesUtils.getString(this, "isFirstOpen", "").trim().length() <= 0) {
            if (!Utils.isNetUsable(MainActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.appRegister(Utils.getDeviceId(this), "182c79dbf8871689878b0de620006ea3", "7ed99e89c4831f169627b5d20a0020f7c1a9b026244e6364ac1c12a9fa2314fe");
        }
    }

    @Override
    public void onAppRegister(boolean flag) {
        if (flag) {
            mRefreshAccessTokenHandler.sendEmptyMessage(0);
            SharedPreferencesUtils.setString(this, "isFirstOpen", "true");
        }
    }

    @Override
    public void getRefreshToken() {
        if (!Utils.isNetUsable(MainActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getRefreshToken("password", Utils.getDeviceId(this), "8e2f773d58c51c3a1854c059af34b49f");
    }

    @Override
    public void refreshAccessToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopRefreshAccessToken) {
                    try {
                        Thread.sleep(3600000);
                        mRefreshAccessTokenHandler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onGetBackgroundBag(List<CoinType> coinTypes) {
        if (coinTypes.size() == 0) {
//            backgroundBagRV.showEmptyView();
            showSnackbar("没有数据,下拉刷新");
        } else {
//            backgroundBagRV.hideEmptyView();
            bagAdapter.appendList(coinTypes);
        }
    }

    @Override
    public void onDismissBackgroundBag() {
        backgroundBagBottomSheetDialog.dismiss();
    }

    public void initBackgroundBag() {
        bagAdapter.clearList();
        isFirstLoadBag = true;
    }

    @Override
    public void updateAccessToken() {
        mRefreshAccessTokenHandler.sendEmptyMessage(0);
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
    public void onGetMarketShops(List<MarketShop> marketShops) {
        marketShopsAdapter.appendList(marketShops);
        if (MARKET_SHOPS_PAGE_SIZE > marketShops.size()) {
            marketLevelShopsRV.disableLoadmore();
            return;
        }
        marketShopsPageIndex++;
        if (!marketLevelShopsRV.isLoadMoreEnabled()) {
            marketLevelShopsRV.reenableLoadmore();
        }
    }

    @Override
    public void setPresenter(MainContract.MainPresenter presenter) {
        mPresenter = presenter;
    }

    private void imLogin() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (SharedPreferencesUtils.getString(this, "im_account", "").length() > 0 && SharedPreferencesUtils.getString(this, "im_password", "").length() > 0) {
            String im_name = SharedPreferencesUtils.getString(this, "im_account", "");
            String im_pwd = SharedPreferencesUtils.getString(this, "im_password", "");
            mPresenter.chatLogin(im_name, im_pwd);
        }
    }

    public void chatLogin() {
        imLogin();
    }

    public void updateUnreadConversationBubble() {
        if (null == unreadConversationBubbleTV) return;
        long unreadCount = getTotalConversationUnreadNum();
        if (unreadCount <= 0) {
            unreadConversationBubbleTV.setVisibility(View.INVISIBLE);
        } else {
            unreadConversationBubbleTV.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unreadCount);
            unreadConversationBubbleTV.setBackgroundResource(R.mipmap.tecent_point1);
            if (unreadCount > 99) {
                unreadConversationBubbleTV.setBackgroundResource(R.mipmap.tecent_point2);
                unReadStr = getResources().getString(R.string.time_more);
            }
            unreadConversationBubbleTV.setText(unReadStr);
        }
    }

    /**
     * 统计未读信息数
     *
     * @return
     */
    private long getTotalConversationUnreadNum() {
        long num = 0;
        pgConversationList.clear();
        for (Conversation conversation : conversationList) {
            if (null != conversation.getType()) {
                switch (conversation.getType()) {
                    case C2C:
                        pgConversationList.add(conversation);
                        break;
                    case Group:
                        if (GroupInfo.getInstance().isInGroup(conversation.getIdentify())) {
                            pgConversationList.add(conversation);
                        }
                        break;
                }
            }
        }
        for (Conversation conversation : pgConversationList) {
            num += conversation.getUnreadNum();
        }
        return num;
    }

    public void showBackgroundBag() {
        backgroundBagBottomSheetDialog.show();
        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0) {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEVISIBLE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEGONE);
            bagAdapter.clearList();
            if (isFirstLoadBag) {
                isFirstLoadBag = false;
            }
        } else {
            ButterKnife.apply(backgroundBagLoginCheck, BUTTERKNIFEGONE);
            ButterKnife.apply(backgroundBagRV, BUTTERKNIFEVISIBLE);
            if (isFirstLoadBag) {
                isFirstLoadBag = false;
                getBackgroundBag();
            }
        }
    }

    public void showMarketShops() {
        marketShopsBottomSheetDialog.show();
        if (isFirstLoadMarketShops) {
            isFirstLoadMarketShops = false;
            getMarketShops();
        }
    }

    private void initBackgroundBagBottomSheetDialog() {
        backgroundBagBottomSheetDialog = new BottomSheetDialog(this, R.style.background_bag_dialog);
        View backgroundBagView = LayoutInflater.from(this).inflate(R.layout.background_bag_layout, null);
        backgroundBagLoginCheck = (TextView) backgroundBagView.findViewById(R.id.login_check);
        backgroundBagRV = (UltimateRecyclerView) backgroundBagView.findViewById(R.id.background_bag_list);
        backgroundBagRV.setAdapter(bagAdapter);
        backgroundBagRV.setLayoutManager(mGridLayoutManager);
        backgroundBagRV.setHasFixedSize(true);
        backgroundBagRV.setSaveEnabled(true);
        backgroundBagRV.setClipToPadding(false);
        backgroundBagRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundBagRV.setRefreshing(false);
                bagAdapter.clearList();
                getBackgroundBag();
            }
        });
        backgroundBagView.findViewById(R.id.quit_background_bag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundBagBottomSheetDialog.dismiss();
            }
        });
        backgroundBagLoginCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_UPDATE_BAGCKGROUND_BAG_CODE);
            }
        });
        backgroundBagBottomSheetDialog.setContentView(backgroundBagView);
        backgroundBagBottomSheetDialog.setCancelable(false);
    }

    private void initMarketShopsBottomSheetDialog() {
        final String[] marketNameArray = getResources().getStringArray(R.array.market_name_array);
        ArrayAdapter<String> marketNameAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.market_name_item, marketNameArray);

        marketShopsBottomSheetDialog = new BottomSheetDialog(this, R.style.market_shops_dialog);
        View marketShopsView = LayoutInflater.from(this).inflate(R.layout.market_shops_layout, null);

        BetterSpinner marketNameSpinner = (BetterSpinner) marketShopsView.findViewById(R.id.market_name);
        marketNameSpinner.setAdapter(marketNameAdapter);
        if (0 < marketNameArray.length) {
            marketName = marketNameArray[0];
            marketNameSpinner.setText(marketName);
        }
        marketNameSpinner.setOnSpinnerItemSelected(new BetterSpinner.OnSpinnerItemSelected() {
            @Override
            public void onSuccess(Integer i) {
                if (marketName.equals(marketNameArray[i])) return;
                marketName = marketNameArray[i];
                marketShopsPageIndex = 1;
                marketShopsAdapter.clearList();
                if (null == marketLevel || "".equals(marketLevel)) return;
                getMarketShops();
            }
        });

        RadioGroup marketLevelsRG = (RadioGroup) marketShopsView.findViewById(R.id.market_levels);
        RadioButton defaultCheckedLevelRB = (RadioButton) marketLevelsRG.getChildAt(4);
        defaultCheckedLevelRB.setChecked(true);
        marketLevel = defaultCheckedLevelRB.getText().toString();
        marketLevelsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i - 1);
                marketLevel = radioButton.getText().toString();
                marketShopsPageIndex = 1;
                marketShopsAdapter.clearList();
                if (null == marketName || "".equals(marketName)) return;
                getMarketShops();
            }
        });

        marketLevelShopsRV = (UltimateRecyclerView) marketShopsView.findViewById(R.id.market_level_shops);
        marketLevelShopsRV.setLayoutManager(mLinearLayoutManager);
        marketLevelShopsRV.setHasFixedSize(true);
        marketLevelShopsRV.setAdapter(marketShopsAdapter);
        marketLevelShopsRV.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                marketLevelShopsRV.setRefreshing(false);
                marketShopsAdapter.clearList();
                marketShopsPageIndex = 1;
                if (null == marketLevel || "".equals(marketLevel) || null == marketName || "".equals(marketName))
                    return;
                getMarketShops();
            }
        });
        marketLevelShopsRV.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                if (!Utils.isNetUsable(MainActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                getMarketShops();
            }
        });

        marketShopsView.findViewById(R.id.quit_background_bag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketShopsBottomSheetDialog.dismiss();
            }
        });
        marketShopsBottomSheetDialog.setContentView(marketShopsView);
        marketShopsBottomSheetDialog.setCancelable(false);
    }

    private void getBackgroundBag() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        if (SharedPreferencesUtils.getString(MainActivity.this, "baza_code", "").trim().length() > 0) {
            mPresenter.shopBackgroundBag(Constants.PERSONAL_BACKGROUND_BAG_ACTION_CODE, "000000", SharedPreferencesUtils.getString(MainActivity.this, "baza_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        } else {
            mPresenter.personalBackgroundBag(Constants.PERSONAL_BACKGROUND_BAG_ACTION_CODE, Constants.PERSONAL_WITHDRAW_ACTION_TYPE_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        }
    }

    private void getMarketsInfo() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getMarketsInfo(Constants.GET_MARKET_SHOPS_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    private void getMarketShops() {
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getMarketShops("000000", marketName, marketLevel, marketShopsPageIndex, MARKET_SHOPS_PAGE_SIZE, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJPush() {
        JPushInterface.init(App.getAppInstance());
        if (null == SharedPreferencesUtils.getString(this, "push_alias", "") || "".equals(SharedPreferencesUtils.getString(this, "push_alias", ""))) {
            if (null != SharedPreferencesUtils.getString(this, "baza_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "baza_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "baza_code", "").replaceAll("-", ""));
            } else if (null != SharedPreferencesUtils.getString(this, "cust_code", "") && !"".equals(SharedPreferencesUtils.getString(this, "cust_code", ""))) {
                setAlias(SharedPreferencesUtils.getString(this, "cust_code", "").replaceAll("-", ""));
            } else {
                return;
            }
        }
        if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0)
            return;
        JPushInterface.resumePush(App.getAppInstance());
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
            switch (code) {
                case 0:
//                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedPreferencesUtils.setString(MainActivity.this, "push_alias", alias);
                    break;
                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
//                    logs = "Failed with errorCode = " + code;
            }
//            showSnackbar(logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(getApplicationContext(),
                            (String) msg.obj,
                            mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerCustomMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("com.yzdsmart.Dingdingwen.CUSTOM_MESSAGE_RECEIVED_ACTION");
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                routeTargetRegion = result.getRegeocodeAddress().getFormatAddress();
            } else {
                showSnackbar("无法解析目的地地址");
            }
        } else {
            showSnackbar("无法解析目的地地址");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

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
                    walkingRouteOverlay = new WalkRouteOverlay(MainActivity.this, mAMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
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
                    ButterKnife.apply(findOperationLayout, BUTTERKNIFEGONE);
                    ButterKnife.apply(routePlaneLayout, BUTTERKNIFEVISIBLE);
                    ButterKnife.apply(routeOpeLayout, BUTTERKNIFEVISIBLE);
                    FrameLayout.LayoutParams customMsgTVParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    customMsgTVParams.setMargins(0, Utils.dp2px(MainActivity.this, 19), 0, 0);
                    customMsgTV.setLayoutParams(customMsgTVParams);
                } else {
                    showSnackbar("未找到规划路线");
                }
            } else {
                showSnackbar("未找到规划路线");
            }
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzdsmart.Dingdingwen.CUSTOM_MESSAGE_RECEIVED_ACTION".equals(intent.getAction())) {
                String custom_title = intent.getStringExtra("custom_title");
                String custom_message = intent.getStringExtra("custom_message");
                String custom_content_type = intent.getStringExtra("custom_content_type");
                StringBuilder showMsg = new StringBuilder();
//                showMsg.append("Title:" + custom_title + "\n");
//                showMsg.append("Message:" + custom_message + "\n");
                showMsg.append(custom_message);
//                showMsg.append("Content Type:" + custom_content_type);
                if (null != custom_content_type && "ddwsys".equals(custom_content_type)) {
                    saveMessage(showMsg.toString());
                }
            }
        }
    }

    private void saveMessage(String message) {
        if (null != customMsgList) {
            customMsgList.add(message);
        }
    }

    private void showCustomMsg(String msg) {
        showCustomMessage(msg);
    }

    /**
     * 启动自定义消息定时器
     */
    private void startCustomMsgTimer() {
        showCustomMsgHandler.postDelayed(showCustomMsgRunnable, 5000);
    }

    /**
     * 停止自定义消息定时器
     */
    private void stopCustomMsgTimer() {
        showCustomMsgHandler.removeCallbacks(showCustomMsgRunnable);
        setCustomMsgTV(false);
    }

    private void initMap() {
        if (null != mAMap) return;
        mAMap = findMoneyMap.getMap();
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                Constants.GEO_DEFAULT_CITY,//新的中心点坐标
                14, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        )));
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (coinsMarkerList.contains(marker)) {
                    if (null != marker.getSnippet()) {
                        String bazaCode = marker.getSnippet();
                        Bundle bundle = new Bundle();
                        bundle.putString("bazaCode", bazaCode);
                        openActivity(ShopDetailsActivity.class, bundle, 0);
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
                    if (!Utils.isNetUsable(MainActivity.this)) {
                        showSnackbar(getResources().getString(R.string.net_unusable));
                        return;
                    }
                    mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(MainActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(MainActivity.this, "ddw_access_token", ""));
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
                        if (!Utils.isNetUsable(MainActivity.this)) {
                            showSnackbar(getResources().getString(R.string.net_unusable));
                            return;
                        }
                        mPresenter.getShopList("000000", mapStatusLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(MainActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(MainActivity.this, "ddw_access_token", ""));
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
        mLocationOption.setInterval(Constants.LOC_TIME);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//        mLocationOption.setGpsFirst(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(Constants.LOC_TIME_OUT);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
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

            SharedPreferencesUtils.setString(MainActivity.this, "qLocation", qLocation);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(locLatitude, locLongitude);
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));
            }

            if (null != SharedPreferencesUtils.getString(MainActivity.this, "cust_code", "") && SharedPreferencesUtils.getString(MainActivity.this, "cust_code", "").length() > 0) {
                if (uploadCoorCounts == 600) {
                    if (!Utils.isNetUsable(MainActivity.this)) {
                        showSnackbar(getResources().getString(R.string.net_unusable));
                        return;
                    }
                    mPresenter.uploadCoor("000000", SharedPreferencesUtils.getString(MainActivity.this, "cust_code", ""), qLocation, SharedPreferencesUtils.getString(MainActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(MainActivity.this, "ddw_access_token", ""));
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

    public void locScanCoins() {
        if (null == qLocation || qLocation.length() <= 0) {
//            ((BaseActivity) MainActivity.this).showSnackbar("定位异常,重新定位");
            if (null != mLocationClient) {
                mLocationClient.startLocation();
            }
            return;
        }
        if (isFirstLoc) return;
        if (0 == isAlreadyLocScan) {
            isAlreadyLocScan = 1;
        }
        clearRoutePlan();
        for (Marker marker : coinsMarkerList) {
            marker.remove();
        }
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        LatLng latLng = mAMap.getCameraPosition().target;
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
        String mapStatusLocation = decimalFormat.format(latLng.longitude) + "," + decimalFormat.format(latLng.latitude);
        if (qLocation.equals(mapStatusLocation)) {
            if (!Utils.isNetUsable(MainActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(MainActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(MainActivity.this, "ddw_access_token", ""));
        } else {
            LatLng locLatLng = new LatLng(locLatitude, locLongitude);
            float distance = AMapUtils.calculateLineDistance(latLng, locLatLng);
            if (0.5 > distance) {
                if (!Utils.isNetUsable(MainActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopList("000000", qLocation, zoomDistance, page_index, PAGE_SIZE, SharedPreferencesUtils.getString(MainActivity.this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(MainActivity.this, "ddw_access_token", ""));
                return;
            }
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locLatitude, locLongitude), 17));
        }
    }

    public void showCustomMessage(String msg) {
        if (null == msg) return;
        customMsgTV.setText(msg);
        setCustomMsgTV(true);
    }

    public void setCustomMsgTV(boolean flag) {
        ButterKnife.apply(customMsgTV, flag ? BUTTERKNIFEVISIBLE : BUTTERKNIFEGONE);
    }
}
