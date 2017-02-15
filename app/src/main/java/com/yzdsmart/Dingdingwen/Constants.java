package com.yzdsmart.Dingdingwen;

import com.amap.api.maps.model.LatLng;

/**
 * Created by YZD on 2016/10/26.
 */

public class Constants {
    public static final Integer REQUEST_LOGIN_CODE = 1000;
    public static final Integer REQUEST_LOGIN_UPDATE_BAGCKGROUND_BAG_CODE = 1001;
    public static final Integer REQUEST_BIND_BANK_CARD_CODE = 1111;
    public static final Integer REQUEST_BANK_CARD_NUM_CODE = 1222;
    public static final Integer REQUEST_COUPON_EXCHANGE_CODE = 1333;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public static final String REGISTER_ACTION_CODE = "1688";
    public static final String FIND_PWD_ACTION_CODE = "1656";

    public final static String GET_CUST_CODE_ACTION_CODE = "628";
    public static final String GET_CUST_LEVEL_ACTION_CODE = "612";
    public static final String PERSONAL_UPLOAD_AVATER_ACTION_CODE = "2101";//上传头像
    public static final String GET_FOCUSED_SHOP_ACTION_CODE = "9212";//获取用户关注的店铺信息
    public static final String PERSONAL_WITHDRAW_ACTION_CODE = "588";
    public static final String PERSONAL_WITHDRAW_ACTION_TYPE_CODE = "166";
    public static final String PERSONAL_WITHDRAW_LOG_ACTION_CODE = "1668";
    public static final String PERSONAL_BACKGROUND_BAG_ACTION_CODE = "1516";
    public static final String PERSONAL_PAYMENT_ACTION_CODE = "388";
    public static final String PERSONAL_PAYMENT_LOG_ACTION_CODE = "2712";
    public static final String PERSONAL_COUPON_LOG_ACTION_CODE = "2728";

    public static final String RECOMMEND_FRIEND_ACTION_CODE = "8566";

    public static final String GET_COIN_ACTION_CODE = "88";
    public static final String GET_COIN_LOG_ACTION_CODE = "1666";
    public static final String SIGN_ACTION_CODE = "176";
    public static final String SIGN_ACTIVITY_LIST_ACTION_CODE = "53";

    public static final String SET_FOCUS_CODE = "66";//取消关注店铺：56    关注：66
    public static final String CANCEL_FOCUS_CODE = "56";

    public static final String GET_SHOP_INFO_ACTION_CODE = "3666";
    public static final String SHOP_UPLOAD_AVATER_ACTION_CODE = "5001";//上传商铺相册
    public static final String BUY_COIN_ACTION_CODE = "66";
    public static final String BUY_COIN_LOG_ACTION_CODE = "688";
    public static final String GET_LEFT_COINS_ACTION_CODE = "1288";
    public static final String GET_TASKS_LEFT_COINS_ACTION_CODE = "7288";
    public static final String PUBLISH_TASK_LOG_ACTION_CODE = "2188";
    public static final String GET_SHOP_FOLLOWERS_ACTION_CODE = "2112";
    public static final String GET_SHOP_FOCUSER_ACTION_CODE = "9012";
    public final static String GET_SCANNED_LOG_ACTION_CODE = "1688";
    public static final String SHOP_WITHDRAW_ACTION_CODE = "688";
    public static final String SHOP_WITHDRAW_LOG_ACTION_CODE = "5688";

    public static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    public static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    public static final Integer PERSONAL_GALLEY_OPERATION_CODE = 1000;
    public static final Integer SHOP_GALLEY_OPERATION_CODE = 1001;
    public static final String GET_PERSONAL_GALLEY_ACTION_CODE = "2102";
    public static final String GET_SHOP_GALLEY_ACTION_CODE = "5101";
    public static final String PERSONAL_GALLEY_DELETE_ACTION_CODE = "4102";//删除个人相册
    public static final String SHOP_GALLEY_DELETE_ACTION_CODE = "4201";//删除商铺相册
    public static final String PERSONAL_GALLEY_UPLOAD_ACTION_CODE = "2102";//上传个人相册
    public static final String SHOP_GALLEY_UPLOAD_ACTION_CODE = "5101";//上传商铺相册
    public static final String SHOP_PAYMENT_LOG_ACTION_CODE = "2812";
    public static final String SHOP_COUPON_LOG_ACTION_CODE = "2828";

    public static final String SHOP_PAY_LOG_ACTION_CODE = "816";//获取指定商铺支付日志
    public static final String SHOP_COUPON_ACTION_CODE = "211";//获取指定商铺兑换列表
    public static final String COIN_COUPON_ACTION_CODE = "232";//获取指定商铺兑换列表

    public static final String THIRD_PLATFORM_ACTION_CODE = "1692";//获取指定商铺支付日志

    //默认城市经纬度31.816058, 119.980524
    public static final LatLng GEO_DEFAULT_CITY = new LatLng(31.816058, 119.980524);
    //定位频率
    public static final Long LOC_TIME = 5000l;//毫秒
    public static final Long LOC_TIME_OUT = 10000l;//毫秒

    public static final String DIR_NAME = "DingDingWen";//文件目录

    public final static String INKE_APP_ID = "1000290001";//映客app id

    //权限申请
    public static final int DDW_LOCATION_PERM = 1;
}
