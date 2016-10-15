package com.yzdsmart.Collectmoney.galley.preview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.galley.upload.UploadGalleyActivity;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by YZD on 2016/9/21.
 */

public class GalleyPreviewActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, GalleyPreviewContract.GalleyPreviewView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.right_title})
    List<View> showViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.right_title)
    TextView rightTitleTV;
    @Nullable
    @BindView(R.id.snpl_delete_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @Nullable
    @BindView(R.id.add_galley)
    Button addGalley;
    @Nullable
    @BindView(R.id.delete_galley)
    Button deleteGalley;
    @Nullable
    @BindView(R.id.add_galley_layout)
    FrameLayout addGalleyLayout;
    @Nullable
    @BindView(R.id.delete_galley_layout)
    FrameLayout deleteGalleyLayout;

    private Integer identityType;//0 个人 1 商铺
    private Integer userType;//0 自身 1 好友
    private String custCode;
    private List<GalleyInfo> galleyInfoList;
    private ArrayList<String> deleteGalleys;
    private List<Integer> deleteFileList;

    private boolean isGalleyOperated = false;

    private static final Integer PERSONAL_GALLEY_OPERATION_CODE = 1000;
    private static final Integer SHOP_GALLEY_OPERATION_CODE = 1001;

    private static final String PERSONAL_GALLEY_ACTION_CODE = "2102";
    private static final String SHOP_GALLEY_ACTION_CODE = "5101";
    private static final String PERSONAL_GALLEY_DELETE_ACTION_CODE = "4102";
    private static final String SHOP_GALLEY_DELETE_ACTION_CODE = "4201";

    private GalleyPreviewContract.GalleyPreviewPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable deletePersonalGalleySuccessRunnable;
    private Runnable deleteShopGalleySuccessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleyInfoList = new ArrayList<GalleyInfo>();
        deleteGalleys = new ArrayList<String>();
        deleteFileList = new ArrayList<Integer>();

        identityType = getIntent().getExtras().getInt("identity");
        custCode = getIntent().getExtras().getString("cust_code");
//        galleyInfoList = getIntent().getExtras().getParcelableArrayList("galleys");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        switch (identityType) {
            case 0:
                userType = getIntent().getExtras().getInt("type");
                switch (userType) {
                    case 0:
                        centerTitleTV.setText("我的相册");
                        rightTitleTV.setText("选择");
                        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
                        ButterKnife.apply(addGalleyLayout, BUTTERKNIFEVISIBLE);
                        ButterKnife.apply(deleteGalleyLayout, BUTTERKNIFEVISIBLE);
                        break;
                    case 1:
                        centerTitleTV.setText("好友相册");
                        break;
                }
                break;
            case 1:
                centerTitleTV.setText("商铺相册");
                rightTitleTV.setText("选择");
                ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(addGalleyLayout, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(deleteGalleyLayout, BUTTERKNIFEVISIBLE);
                break;
        }

        new GalleyPreviewPresenter(this, this);

        switch (identityType) {
            case 0:
                mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", custCode);
                break;
            case 1:
                mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
                break;
        }

        deletePersonalGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "cust_code", ""));
            }
        };

        deleteShopGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
            }
        };

        mPhotosSnpl.init(this);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        mPhotosSnpl.setDeleteDrawableResId(0);

//        deleteGalleys.clear();
//        for (GalleyInfo galleyInfo : galleyInfoList) {
//            deleteGalleys.add(galleyInfo.getImageFileUrl());
//        }
//        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_galley_preview;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(deletePersonalGalleySuccessRunnable);
        mHandler.removeCallbacks(deleteShopGalleySuccessRunnable);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PERSONAL_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
        } else if (SHOP_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.right_title, R.id.add_galley, R.id.delete_galley})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.right_title:
                if (mPhotosSnpl.getData().size() <= 0) return;
                isGalleyOperated = !isGalleyOperated;
                if (isGalleyOperated) {
                    rightTitleTV.setText("取消");
                    mPhotosSnpl.setDeleteDrawableResId(R.mipmap.bga_pp_ic_delete);
                    addGalley.setEnabled(false);
                    deleteGalley.setEnabled(true);
                } else {
                    rightTitleTV.setText("选择");
                    mPhotosSnpl.setDeleteDrawableResId(0);
                    deleteGalley.setEnabled(false);
                    addGalley.setEnabled(true);
                }
                mPhotosSnpl.refresh();
                break;
            case R.id.add_galley:
                bundle = new Bundle();
                switch (identityType) {
                    case 0:
                        bundle.putInt("type", 0);
                        openActivity(UploadGalleyActivity.class, bundle, PERSONAL_GALLEY_OPERATION_CODE);
                        break;
                    case 1:
                        bundle.putInt("type", 1);
                        openActivity(UploadGalleyActivity.class, bundle, SHOP_GALLEY_OPERATION_CODE);
                        break;
                }
                break;
            case R.id.delete_galley:
                rightTitleTV.setText("选择");
                isGalleyOperated = false;
                mPhotosSnpl.setDeleteDrawableResId(0);
                deleteGalley.setEnabled(false);
                addGalley.setEnabled(true);
                deleteFileList.clear();
                for (String path : mPhotosSnpl.getData()) {
                    for (GalleyInfo galleyInfo : galleyInfoList) {
                        if (galleyInfo.getImageFileUrl().equals(path)) {
                            deleteFileList.add(galleyInfo.getFileId());
                        }
                    }
                }
                switch (identityType) {
                    case 0:
                        mPresenter.deletePersonalGalley(PERSONAL_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), deleteFileList);
                        break;
                    case 1:
                        mPresenter.deleteShopGalley(SHOP_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), deleteFileList);
                        break;
                }
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
        if (mPhotosSnpl.getData().size() <= 0) {
            rightTitleTV.setText("选择");
            isGalleyOperated = false;
            mPhotosSnpl.setDeleteDrawableResId(0);
            deleteGalley.setEnabled(false);
            addGalley.setEnabled(true);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

    }

    @Override
    public void onGetPersonalGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        deleteGalleys.clear();
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            deleteGalleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.removeAllViews();
        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    public void onDeletePersonalGalley() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deletePersonalGalleySuccessRunnable, 500);
    }

    @Override
    public void onGetShopGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        deleteGalleys.clear();
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            deleteGalleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.removeAllViews();
        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    public void onDeleteShopGalley() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deleteShopGalleySuccessRunnable, 500);
    }

    @Override
    public void setPresenter(GalleyPreviewContract.GalleyPreviewPresenter presenter) {
        mPresenter = presenter;
    }
}
