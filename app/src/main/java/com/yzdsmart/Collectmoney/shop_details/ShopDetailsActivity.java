package com.yzdsmart.Collectmoney.shop_details;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopScanner;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Collectmoney.login.LoginActivity;
import com.yzdsmart.Collectmoney.main.MainActivity;
import com.yzdsmart.Collectmoney.qr_scan.QRScannerActivity;
import com.yzdsmart.Collectmoney.tecent_im.bean.UserInfo;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/21.
 */
public class ShopDetailsActivity extends BaseActivity implements ShopDetailsContract.ShopDetailsView {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.hotel_user_list)
    RecyclerView hotelUsersRV;
    @Nullable
    @BindView(R.id.hotel_address)
    TextView hotelAddressTV;
    @Nullable
    @BindView(R.id.hotel_name)
    TextView hotelNameTV;
    @Nullable
    @BindView(R.id.focus_person_counts)
    TextView focusPersonCountsTV;
    @Nullable
    @BindView(R.id.daily_coin_counts)
    TextView dailyCoinCountsTV;
    @Nullable
    @BindView(R.id.visit_person_counts)
    TextView visitPersonCountsTV;
    @Nullable
    @BindView(R.id.is_atte)
    ImageView isAtteIV;

    private static final Integer REQUEST_LOGIN_CODE = 1000;

    private String bazaCode;//商铺编码
    private Boolean isAtte = false;
    private static final String GET_SHOP_FOLLOWERS_CODE = "2112";
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private static final String SET_FOCUS_CODE = "66";//取消关注：56    关注：66
    private static final String CANCEL_FOCUS_CODE = "56";

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<ShopScanner> shopScannerList;
    private ShopScannerAdapter shopScannerAdapter;

    private String shopCoor;
    private ShopDetailsContract.ShopDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shopScannerList = new ArrayList<ShopScanner>();
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "三毛", "http://y3.ifengimg.com/haina/2016_06/048f23be371c78c_w432_h520.jpg", "男", 888, "昨天", "54673646"));
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "小黄", "http://p3.ifengimg.com/a/2016_39/7c512d79019b613_size46_w604_h595.jpg", "女", 123, "08:30", "8964648465"));
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "丫头", "http://d.ifengimg.com/mw978_mh598/p2.ifengimg.com/a/2016_39/d476f58859c90a0_size491_w439_h661.png", "女", 546, "三天前", "2136658"));

        bazaCode = getIntent().getExtras().getString("bazaCode");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.qr_code_icon));

        new ShopDetailsPresenter(this, this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        shopScannerAdapter = new ShopScannerAdapter(this);
        hotelUsersRV.setHasFixedSize(true);
        hotelUsersRV.setLayoutManager(mLinearLayoutManager);
        hotelUsersRV.addItemDecoration(dividerItemDecoration);
        hotelUsersRV.setAdapter(shopScannerAdapter);

        shopScannerAdapter.appenList(shopScannerList);

        mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""));

        mPresenter.getShopFollowers(GET_SHOP_FOLLOWERS_CODE, "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_details;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            MainActivity.getInstance().chatLogin();
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.is_atte, R.id.title_right_operation_layout, R.id.get_more_followers, R.id.route_line})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.is_atte:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    return;
                }
                mPresenter.setFollow(isAtte ? CANCEL_FOCUS_CODE : SET_FOCUS_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), bazaCode);
                break;
            case R.id.title_right_operation_layout:
                showMoveDialog(this);
                break;
            case R.id.get_more_followers:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, REQUEST_LOGIN_CODE);
                    return;
                }
                mPresenter.getShopFollowers(GET_SHOP_FOLLOWERS_CODE, "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), ++pageIndex, PAGE_SIZE);
                break;
            case R.id.route_line:
                if (null == shopCoor || shopCoor.trim().length() <= 0) return;
                MainActivity.getInstance().planRoute(shopCoor);
                closeActivity();
                break;
        }
    }

    @Override
    public void setPresenter(ShopDetailsContract.ShopDetailsPresenter presenter) {
        mPresenter = presenter;
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
                openActivity(QRScannerActivity.class, bundle, 0);
                scannerChooseDialog.dismiss();
            }
        });
        payCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt("scanType", 1);
                openActivity(QRScannerActivity.class, bundle, 0);
                scannerChooseDialog.dismiss();
            }
        });
        Window window = scannerChooseDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        scannerChooseDialog.show();
    }

    @Override
    public void onGetShopInfo(ShopInfoRequestResponse shopDetails) {
        hotelNameTV.setText(shopDetails.getName());
        hotelAddressTV.setText(shopDetails.getAddr());
        focusPersonCountsTV.setText("" + shopDetails.getAtteNum());
        dailyCoinCountsTV.setText("" + shopDetails.getTodayGlodNum());
        visitPersonCountsTV.setText("" + shopDetails.getVisiNum());
        isAtte = shopDetails.getAtte();
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.heart_icon_white_checked) : getResources().getDrawable(R.mipmap.heart_icon_white));
        shopCoor = shopDetails.getCoor();
    }

    @Override
    public void onSetFollow(Boolean flag, String action, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        isAtte = CANCEL_FOCUS_CODE.equals(action) ? false : true;
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.heart_icon_white_checked) : getResources().getDrawable(R.mipmap.heart_icon_white));

        mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""));
    }

    @Override
    public void onGetShopFollowers(List<ShopScanner> shopScanners) {
        shopScannerList.clear();
        shopScannerList.addAll(shopScanners);
        shopScannerAdapter.appenList(shopScannerList);
    }
}
