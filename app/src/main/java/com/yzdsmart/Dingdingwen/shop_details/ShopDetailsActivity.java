package com.yzdsmart.Dingdingwen.shop_details;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.net.Uri;
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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopScanner;
import com.yzdsmart.Dingdingwen.galley.GalleyActivity;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.qr_scan.QRScannerActivity;
import com.yzdsmart.Dingdingwen.register_login.login.LoginActivity;
import com.yzdsmart.Dingdingwen.tecent_im.bean.UserInfo;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

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
    @BindViews({R.id.left_title, R.id.title_logo})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.is_atte})
    List<View> showViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.shop_images_banner)
    ConvenientBanner shopImagesBanner;
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
    @Nullable
    @BindView(R.id.shop_avater)
    ImageView shopAvaterIV;

    private String bazaCode;//商铺编码
    private Boolean isAtte = false;
    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<ShopScanner> shopScannerList;
    private ShopScannerAdapter shopScannerAdapter;

    private ArrayList<Integer> localImages;//默认banner图片
    private ArrayList<String> shopImageList;
    private ArrayList<String> galleyImages;

    private String shopPhoneNumber;
    private String shopCoor;
    private ShopDetailsContract.ShopDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localImages = new ArrayList<Integer>();
        localImages.add(R.mipmap.shop_banner);
        shopImageList = new ArrayList<String>();
        galleyImages = new ArrayList<String>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
        centerTitleTV.setText("店铺详情");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.qr_code_scanner_icon));

        new ShopDetailsPresenter(this, this);

        shopImagesBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("identity", 1);
                bundle.putInt("type", 1);
                bundle.putString("cust_code", bazaCode);
                openActivity(GalleyActivity.class, bundle, 0);
            }
        });

        shopScannerList = new ArrayList<ShopScanner>();
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "三毛", "http://y3.ifengimg.com/haina/2016_06/048f23be371c78c_w432_h520.jpg", "男", 888, "昨天", "54673646"));
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "小黄", "http://p3.ifengimg.com/a/2016_39/7c512d79019b613_size46_w604_h595.jpg", "女", 123, "08:30", "8964648465"));
        shopScannerList.add(new ShopScanner("f47faba3-5bb8-4bd4-b844-206c80503704", "丫头", "http://d.ifengimg.com/mw978_mh598/p2.ifengimg.com/a/2016_39/d476f58859c90a0_size491_w439_h661.png", "女", 546, "三天前", "2136658"));

        bazaCode = getIntent().getExtras().getString("bazaCode");

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.divider_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        shopScannerAdapter = new ShopScannerAdapter(this);
        hotelUsersRV.setHasFixedSize(true);
        hotelUsersRV.setLayoutManager(mLinearLayoutManager);
        hotelUsersRV.addItemDecoration(dividerItemDecoration);
        hotelUsersRV.setAdapter(shopScannerAdapter);

        shopScannerAdapter.appenList(shopScannerList);

        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
        mPresenter.getShopFollowers(Constants.GET_SHOP_FOLLOWERS_ACTION_CODE, "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_details;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.REQUEST_LOGIN_CODE == requestCode && RESULT_OK == resultCode) {
            mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
            MainActivity.getInstance().chatLogin();
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.is_atte, R.id.title_right_operation_layout, R.id.get_more_followers, R.id.hotel_address, R.id.call_shop})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.is_atte:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.setFollow(isAtte ? Constants.CANCEL_FOCUS_CODE : Constants.SET_FOCUS_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), bazaCode, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
                break;
            case R.id.title_right_operation_layout:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                showMoveDialog(this);
                break;
            case R.id.get_more_followers:
                if (null == SharedPreferencesUtils.getString(this, "cust_code", "") || SharedPreferencesUtils.getString(this, "cust_code", "").trim().length() <= 0 || null == UserInfo.getInstance().getId()) {
                    openActivityForResult(LoginActivity.class, Constants.REQUEST_LOGIN_CODE);
                    return;
                }
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopFollowers(Constants.GET_SHOP_FOLLOWERS_ACTION_CODE, "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), pageIndex, PAGE_SIZE);
                break;
            case R.id.hotel_address:
                if (null == shopCoor || shopCoor.trim().length() <= 0) return;
                MainActivity.getInstance().planRoute(shopCoor);
                closeActivity();
                break;
            case R.id.call_shop:
                if (null != shopPhoneNumber && !"".equals(shopPhoneNumber)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shopPhoneNumber));
                    startActivity(intent);
                }
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
        shopPhoneNumber = shopDetails.getTel();
        isAtte = shopDetails.getAtte();
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.shop_detail_focused) : getResources().getDrawable(R.mipmap.shop_detail_not_focus));
        shopCoor = shopDetails.getCoor();
        Glide.with(this).load(shopDetails.getLogoImageUrl()).asBitmap().placeholder(getResources().getDrawable(R.mipmap.ic_holder_light)).error(getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
        if (shopDetails.getImageLists().size() <= 0) return;
        shopImageList.clear();
        shopImageList.addAll(shopDetails.getImageLists());
        if (shopImageList.size() <= 0) {
            ButterKnife.apply(shopImagesBanner, BUTTERKNIFEGONE);
        } else {
            ButterKnife.apply(shopImagesBanner, BUTTERKNIFEVISIBLE);
            for (int i = 0; i < shopImageList.size(); i++) {
                galleyImages.add(shopImageList.get(i));
                if (i == 3) {
                    break;
                }
            }
            shopImagesBanner.setPages(new CBViewHolderCreator<ShopGalleyViewHolder>() {
                @Override
                public ShopGalleyViewHolder createHolder() {
                    return new ShopGalleyViewHolder();
                }
            }, galleyImages)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        }
        shopImagesBanner.notifyDataSetChanged();
    }

    @Override
    public void onSetFollow(Boolean flag, String action, String msg) {
        if (!flag) {
            showSnackbar(msg);
            return;
        }
        isAtte = Constants.CANCEL_FOCUS_CODE.equals(action) ? false : true;
        isAtteIV.setImageDrawable(isAtte ? getResources().getDrawable(R.mipmap.shop_detail_focused) : getResources().getDrawable(R.mipmap.shop_detail_not_focus));
        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getShopInfo("000000", "000000", bazaCode, SharedPreferencesUtils.getString(this, "cust_code", ""), SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    public void onGetShopFollowers(List<ShopScanner> shopScanners) {
        if (shopScanners.size() <= 0) return;
        pageIndex++;
        shopScannerList.clear();
        shopScannerList.addAll(shopScanners);
        shopScannerAdapter.appenList(shopScannerList);
    }

    class ShopGalleyViewHolder implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.recommend_pre_load)).error(context.getResources().getDrawable(R.mipmap.shop_banner)).into(imageView);
        }
    }
}